package com.oracle.jets.spatial252;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

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
import com.oracle.jets.spatial252.searcher.SpatialObject;

import oracle.spatial.geometry.JGeometry;
import oracle.spatial.network.NetworkMetadata;
import oracle.spatial.network.lod.LODNetworkException;
import oracle.spatial.network.lod.LODNetworkManager;
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
    private DataSource dataSource;

    private NetworkIO networkIO;

    private static final String NETWORK_NAME = "ZROAD_NET";

    @Override
    public Direction getShortestDirection(Point origin, Point destination) {
        try {
            Connection connection = dataSource.getConnection();
            NetworkMetadata meta = LODNetworkManager.getNetworkMetadata(
                    connection, NETWORK_NAME, NETWORK_NAME);
            networkIO = LODNetworkManager.getCachedNetworkIO(
                    connection, NETWORK_NAME, NETWORK_NAME, meta);
        } catch (LODNetworkException | SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        LogicalSubPath path = null;
        try {
            // TODO: NetworkAnalystをプールする仕組みが必要かも
            NetworkAnalyst analyst = LODNetworkManager.getNetworkAnalyst(networkIO);
//            NetworkAnalyst analyst = context.getBean(NetworkAnalyst.class);
            path = analyst.shortestPathDijkstra(
                    getPointOnNet(origin.toJGeometry()),
                    getPointOnNet(destination.toJGeometry()),
                    0, null);
            return Path2Direction(path);
        } catch (Exception e) {
            // TODO: handle error
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 
     * @param point
     * @return
     */
    private PointOnNet getPointOnNet(JGeometry point) {
        SpatialObject nearest = getNearestGeoObject(point);
        if (nearest instanceof Node) {
            return new PointOnNet(nearest.getId());
        } else if (nearest instanceof Link) {
            return getPointOnLink((Link) nearest);
        } else {
            throw new IllegalStateException();
        }
    }

    private SpatialObject getNearestGeoObject(JGeometry point) {
        Link link = null;
        Node node = null;
        try {
            LinkSearcher linkSearcher = context.getBean(LinkSearcher.class);
            link = linkSearcher.fetchAllAttributes(false)
                    .fetchDistance(true)
                    .setGeoSearchStrategy(new NearestNeighborStrategy(1))
                    .search(point).get(0);
            NodeSearcher nodeSearcher = context.getBean(NodeSearcher.class);
            node = nodeSearcher.fetchAllAttributes(false)
                    .fetchDistance(true)
                    .setGeoSearchStrategy(new NearestNeighborStrategy(1))
                    .search(point).get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (link.furtherThan(node) < 0) {
            return link;
        } else {
            return node;
        }
    }

    private PointOnNet getPointOnLink(Link link) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Connection connection = null;
        try {
            JGeometry line = networkIO.readSpatialLink(link.getId(), true).getGeometry();
            String QUERY_POINT_ON_LINE = "SELECT getRatio(?, ?) from dual";
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(QUERY_POINT_ON_LINE);
            statement.setObject(1, JGeometry.storeJS(link.getOrigin(), connection));
            statement.setObject(2, JGeometry.storeJS(line, connection));
            resultSet = statement.executeQuery();
            double percentage = 0;
            if (resultSet.next()) {
                percentage = resultSet.getDouble(1);
            } else {
                // TODO: define Exception
                throw new IllegalStateException();
            }
            return new PointOnNet(link.getId(), percentage / 100);
        } catch (LODNetworkException | SQLException e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // do nothing.
            }
        }
    }

    @Override
    public List<RefugeWithDirection> getNearestRefuges(Point origin, int limit) {
        try {
            Connection connection = dataSource.getConnection();
            NetworkMetadata meta = LODNetworkManager.getNetworkMetadata(
                    connection, NETWORK_NAME, NETWORK_NAME);
            networkIO = LODNetworkManager.getCachedNetworkIO(
                    connection, NETWORK_NAME, NETWORK_NAME, meta);
        } catch (LODNetworkException | SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
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
                    NetworkAnalyst analyst = LODNetworkManager.getNetworkAnalyst(networkIO);
                    path = analyst.shortestPathDijkstra(
                            getPointOnNet(origin.toJGeometry()),
                            getPointOnNet(r.getLocation()),
                            0, null);
                } catch (LODNetworkException e) {
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
     * @param lsp
     * @return
     */
    private JGeometry getPathGeometry(LogicalSubPath lsp) {
        JGeometry geom = null;
        try {
            SpatialSubPath ssp = networkIO.readSpatialSubPath(lsp);
            geom = ssp.getGeometry();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return geom;
    }

}
