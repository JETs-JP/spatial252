package com.oracle.jets.spatial252;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.oracle.jets.spatial252.searcher.Link;
import com.oracle.jets.spatial252.searcher.LinkSearcher;
import com.oracle.jets.spatial252.searcher.NearestNeighborStrategy;
import com.oracle.jets.spatial252.searcher.Node;
import com.oracle.jets.spatial252.searcher.NodeSearcher;
import com.oracle.jets.spatial252.searcher.Refuge;
import com.oracle.jets.spatial252.searcher.RefugeSearcher;

import oracle.spatial.geometry.JGeometry;
import oracle.spatial.network.lod.LODNetworkException;
import oracle.spatial.network.lod.LogicalSubPath;
import oracle.spatial.network.lod.NetworkAnalyst;
import oracle.spatial.network.lod.NetworkIO;
import oracle.spatial.network.lod.PointOnNet;
import oracle.spatial.network.lod.SpatialSubPath;

@Component
class OracleSpatialService implements GeometryService {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private OracleDbFunctionUtils dbFunctionUtils;

    @Override
    public Direction getShortestDirection(Point origin, Point destination) {
            NetworkAnalyst analyst = context.getBean(NetworkAnalyst.class);
            try {
                LogicalSubPath path = analyst.shortestPathDijkstra(
                        getPointOnNet(origin.toJGeometry()),
                        getPointOnNet(destination.toJGeometry()),
                        0, null);
                return Path2Direction(path);
            } catch (LODNetworkException | SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
    }

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
            NetworkIO networkIo = context.getBean(NetworkIO.class);
            JGeometry line = networkIo.readSpatialLink(link.getId(), true).getGeometry();
            double percentage = dbFunctionUtils.getRatio(origin, line);
            return new PointOnNet(link.getId(), percentage / 100);
        } else {
            return new PointOnNet(node.getId());
        }
    }

    @Override
    public List<RefugeWithDirection> getNearestRefuges(Point origin, int limit) {
        try {
            RefugeSearcher searcher = context.getBean(RefugeSearcher.class);
            List<Refuge> refuges = searcher.fetchAllAttributes(true)
                    .fetchDistance(true)
                    .setGeoSearchStrategy(new NearestNeighborStrategy(limit))
                    .search(origin.toJGeometry());
            List<RefugeWithDirection> retval =
                    new ArrayList<RefugeWithDirection>(limit);
            refuges.stream().forEach(r -> {
                LogicalSubPath path = null;
                // TODO: 正しいエラーハンドリング
                try {
                    NetworkAnalyst analyst = context.getBean(NetworkAnalyst.class);
                    path = analyst.shortestPathDijkstra(
                            getPointOnNet(origin.toJGeometry()),
                            getPointOnNet(r.getLocation()),
                            0, null);
                } catch (LODNetworkException | SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Direction direction = Path2Direction(path);
                retval.add(new RefugeWithDirection(r, direction));
            });
            return retval;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    private Direction Path2Direction(LogicalSubPath path) {
        // TODO: null check
        double[] ordinates = getPathGeometry(path).getOrdinatesArray();
        Direction direction = new Direction();
        for (int i = 0; i < ordinates.length; i += 2) {
            direction.AddWayPoint(new Point(ordinates[i + 1], ordinates[i]));
        }
        return direction;
    }

    /**
     * パスに含まれるリンクのジオメトリ全体を返します
     * 
     * @param path
     * @return
     */
    private JGeometry getPathGeometry(LogicalSubPath path) {
        NetworkIO networkIo = context.getBean(NetworkIO.class);
        try {
            SpatialSubPath ssp = networkIo.readSpatialSubPath(path);
            return ssp.getGeometry();
        } catch (Exception e) {
            // TODO: handle error
            e.printStackTrace();
            return null;
        }
    }

}
