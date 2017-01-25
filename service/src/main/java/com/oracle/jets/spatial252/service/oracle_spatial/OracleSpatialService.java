package com.oracle.jets.spatial252.service.oracle_spatial;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.oracle.jets.spatial252.service.Direction;
import com.oracle.jets.spatial252.service.GeometryService;
import com.oracle.jets.spatial252.service.Point;
import com.oracle.jets.spatial252.service.Polygon;
import com.oracle.jets.spatial252.service.RefugeWithDirection;
import com.oracle.jets.spatial252.service.Spatial252ServiceException;
import com.oracle.jets.spatial252.service.oracle_spatial.searcher.AnyInteractStrategy;
import com.oracle.jets.spatial252.service.oracle_spatial.searcher.Link;
import com.oracle.jets.spatial252.service.oracle_spatial.searcher.LinkSearcher;
import com.oracle.jets.spatial252.service.oracle_spatial.searcher.NearestNeighborStrategy;
import com.oracle.jets.spatial252.service.oracle_spatial.searcher.Node;
import com.oracle.jets.spatial252.service.oracle_spatial.searcher.NodeSearcher;
import com.oracle.jets.spatial252.service.oracle_spatial.searcher.Refuge;
import com.oracle.jets.spatial252.service.oracle_spatial.searcher.RefugeSearcher;

import oracle.spatial.geometry.JGeometry;
import oracle.spatial.network.lod.LODNetworkException;
import oracle.spatial.network.lod.LogicalSubPath;
import oracle.spatial.network.lod.NetworkAnalyst;
import oracle.spatial.network.lod.NetworkIO;
import oracle.spatial.network.lod.NetworkUpdate;
import oracle.spatial.network.lod.PointOnNet;
import oracle.spatial.network.lod.SpatialLink;
import oracle.spatial.network.lod.SpatialNode;

@Component
class OracleSpatialService implements GeometryService {

    // TODO: Sercherのファクトリーを作ってComponentとするべきかも
    @Autowired
    private ApplicationContext context;

    @Autowired
    private OracleDbFunctionUtils dbFunctionUtils;

    // TODO: NetworkAnalyst,NetworkIO,NetworkUpdateに関連する責務は、別クラスに出す
    @Autowired
    private NetworkAnalyst analyst;

    @Autowired
    private NetworkIO netIo;

    @Autowired
    private NetworkUpdate networkUpdate;

    // TODO: この設計は見直したい
    @Autowired
    private DisabledPolygonsCache disabledPolygons;

    /* (non-Javadoc)
     * @see com.oracle.jets.spatial252.GeometryService#getShortestDirection(com.oracle.jets.spatial252.Point, com.oracle.jets.spatial252.Point)
     */
    @Override
    public Direction getShortestDirection(Point origin, Point destination)
            throws Spatial252ServiceException {
        try {
            LogicalSubPath path = analyst.shortestPathDijkstra(
                    getPointOnNet(toJGeometry(origin)),
                    getPointOnNet(toJGeometry(destination)),
                    0, null);
            return path2Direction(path);
        } catch (LODNetworkException | SQLException e) {
            throw new Spatial252ServiceException(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /* (non-Javadoc)
     * @see com.oracle.jets.spatial252.GeometryService#getNearestRefuges(com.oracle.jets.spatial252.Point, int)
     */
    @Override
    public List<RefugeWithDirection> getNearestRefuges(Point origin, int limit)
            throws Spatial252ServiceException {
        try {
            RefugeSearcher searcher = context.getBean(RefugeSearcher.class);
            List<Refuge> refuges = searcher.fetchAllAttributes(true)
                    .fetchDistance(false)
                    .setGeoSearchStrategy(new NearestNeighborStrategy(limit))
                    .search(toJGeometry(origin));
            List<RefugeWithDirection> retval =
                    new ArrayList<RefugeWithDirection>(limit);
            for (Refuge refuge : refuges) {
                LogicalSubPath path = analyst.shortestPathDijkstra(
                            getPointOnNet(toJGeometry(origin)),
                            getPointOnNet(refuge.getLocation()),
                            0, null);
                Direction direction = path2Direction(path);
                retval.add(new RefugeWithDirection(refuge, direction));
            }
            return retval;
        } catch (LODNetworkException | SQLException e) {
            throw new Spatial252ServiceException(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 指定された座標点から最も近傍にある、経路ネットワーク上の点を取得する
     * 
     * @param point 検索の起点とする座標点
     * @return 指定された座標点から最も近傍にある、経路ネットワーク上の点
     * 
     * @throws LODNetworkException
     *      経路ネットワークからジオメトリの取得に失敗した場合
     * @throws SQLException
     *      リンクまたはノードの検索に失敗した場合
     */
    private PointOnNet getPointOnNet(JGeometry point)
            throws LODNetworkException, SQLException {
        LinkSearcher linkSearcher = context.getBean(LinkSearcher.class);
        Link link = linkSearcher.fetchAllAttributes(false)
                .fetchDistance(true)
                .setGeoSearchStrategy(new NearestNeighborStrategy(1))
                .search(point).get(0);
        NodeSearcher nodeSearcher = context.getBean(NodeSearcher.class);
        Node node = nodeSearcher.fetchAllAttributes(false)
                .fetchDistance(true)
                .setGeoSearchStrategy(new NearestNeighborStrategy(1))
                .search(point).get(0);
        if (link == null || node == null) {
            throw new IllegalStateException("Link or Node records are missing.");
        }
        if (link.furtherThan(node) < 0) {
            JGeometry origin = link.getOrigin();
            JGeometry line = netIo.readSpatialLink(link.getId(), true).getGeometry();
            double percentage = dbFunctionUtils.getRatio(origin, line);
            return new PointOnNet(link.getId(), percentage / 100);
        } else {
            return new PointOnNet(node.getId());
        }
    }

    /**
     * LogicalSubPathからDirectionオブジェクトを取得する
     * 
     * @param path LogicalSubPath型の経路表現
     * @return このアプリケーション固有の経路表現（Direction型）
     * 
     * @throws LODNetworkException
     *      経路ネットワークからジオメトリの取得に失敗した場合
     */
    private Direction path2Direction(LogicalSubPath path) throws LODNetworkException {
        if (path == null) {
            throw new NullPointerException();
        }
        //コストを算出
        double[] costs = path.getCosts();
        double totalCost = 0;
        for (double cost : costs) {
            totalCost += cost;
        }
        //LogicalSubPathからジオメトリ全体を取得
        JGeometry geometry = netIo.readSpatialSubPath(path).getGeometry();
        double[] ordinates = geometry.getOrdinatesArray();
        List<Point> wayPoints = new ArrayList<Point>(ordinates.length / 2);
        for (int i = 0; i < ordinates.length; i += 2) {
            wayPoints.add(new Point(ordinates[i + 1], ordinates[i]));
        }
        Direction direction = new Direction(wayPoints, totalCost);
        return direction;
    }

    /**
     * 指定されたPointオブジェクトのJGeometry型の表現を取得する
     * 
     * @return 指定されたPointオブジェクトのJGeometry型の表現
     */
    private static JGeometry toJGeometry(Point point) {
        if (point == null) {
            return null;
        }
        double[] coordinates = {point.getLon(), point.getLat()};
        return JGeometry.createPoint(coordinates, 2, 8307);
    }

    /* (non-Javadoc)
     * @see com.oracle.jets.spatial252.service.GeometryService#disable(com.oracle.jets.spatial252.service.Polygon)
     */
    @Override
    public void disable(Polygon disableArea) throws Spatial252ServiceException {
        JGeometry disableGeoms = toJGeometry(disableArea);
        try {
            // 関連するノードの無効化
            NodeSearcher nodeSearcher = context.getBean(NodeSearcher.class);
            List<Node> nodes = nodeSearcher.fetchAllAttributes(false)
                    .fetchDistance(false)
                    .setGeoSearchStrategy(new AnyInteractStrategy())
                    .search(disableGeoms);
            for (Node node : nodes) {
                SpatialNode sn = netIo.readSpatialNode(node.getId(), null);
                sn.setIsActive(false);
                networkUpdate.updateNode(
                        sn, netIo.readNodePartitionId(sn.getId(), 1), 1);
            }
            // 関連するリンクの無効化
            LinkSearcher linkSearcher = context.getBean(LinkSearcher.class);
            List<Link> links = linkSearcher.fetchAllAttributes(false)
                    .fetchDistance(false)
                    .setGeoSearchStrategy(new AnyInteractStrategy())
                    .search(disableGeoms);
            for (Link link : links) {
                SpatialLink sl = netIo.readSpatialLink(link.getId(), null);
                sl.setIsActive(false);
                networkUpdate.updateLink(
                        sl, netIo.readNodePartitionId(sl.getStartNodeId(), 1));
                networkUpdate.updateLink(
                        sl, netIo.readNodePartitionId(sl.getEndNodeId(), 1));
            }
            HashMap<Integer, NetworkUpdate> map = new HashMap<>();
            map.put(1, networkUpdate);
            analyst.setNetworkUpdate(map);
            disabledPolygons.add(disableArea);
        } catch (LODNetworkException | SQLException e) {
            throw new Spatial252ServiceException(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 指定されたPolygonオブジェクトのJGeometry型の表現を取得する
     * 
     * @return 指定されたPointオブジェクトのJGeometry型の表現
     */
    private static JGeometry toJGeometry(Polygon polygon) {
        if (polygon == null) {
            return null;
        }
        List<Point> points = polygon.getCoordinates();
        double[] coordinates = new double[points.size() * 2];
        for (int i = 0; i < points.size(); i++) {
            coordinates[i * 2] = points.get(i).getLon();
            coordinates[i * 2 + 1] = points.get(i).getLat();
        }
        return JGeometry.createLinearPolygon(coordinates, 2, 8307);
    }

    @Override
    public List<Polygon> getDisabledArea() throws Spatial252ServiceException {
        return disabledPolygons.getAll();
    }

}
