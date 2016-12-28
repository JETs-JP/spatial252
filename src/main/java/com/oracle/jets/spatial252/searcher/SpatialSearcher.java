package com.oracle.jets.spatial252.searcher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Struct;
import java.util.List;

import javax.sql.DataSource;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;

import oracle.spatial.geometry.JGeometry;

public abstract class SpatialSearcher<V extends SpatialObject> {

    protected TableDefinition tableDefinition;

    /**
     * すべてのカラムの値を取得するかどうか（デフォルト: false）
     */
    protected boolean fetchAllColmuns = false;

    protected boolean fetchDistance = false;

    private SpatialSearchStrategy strategy = new NearestNeighborStrategy(1);

    protected static final String DISTANCE_ATTR_LABEL = "DIST";

    @Autowired
    private DataSource dataSource;

    // デフォルトはidのみ
    public SpatialSearcher<V> fetchAllAttributes(boolean fetchAllAttrs) {
        this.fetchAllColmuns = fetchAllAttrs;
        return this;
    }

    public SpatialSearcher<V> fetchDistance(boolean fetchDistance) {
        this.fetchDistance = fetchDistance;
        return this;
    }

    public SpatialSearcher<V> setGeoSearchStrategy(SpatialSearchStrategy strategy) {
        this.strategy = strategy;
        return this;
    }

    public @NotNull List<V> search(JGeometry origin) throws SQLException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(buildQuery());
            Struct struct = JGeometry.storeJS(origin, connection);
            statement.setObject(1, struct);
            resultSet = statement.executeQuery();
            return buildResult(resultSet, origin);
        } catch (SQLException e) {
            // TODO: デバッグログ
            e.printStackTrace();
            throw e;
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

    private String buildQuery() {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT");
        // column
        builder.append(" ");
        if (fetchAllColmuns) {
            List<String> labels = tableDefinition.getAllColumnLabels();
            for (int i = 0; i < labels.size(); i++) {
                if (i > 0) {
                    builder.append(", ");
                }
                builder.append(labels.get(i));
            }
        } else {
            builder.append(tableDefinition.getIdColumnLabel());
        }
        // dist
        if (fetchDistance) {
            builder.append(", ");
            builder.append("SDO_NN_DISTANCE(1) ");
            builder.append(DISTANCE_ATTR_LABEL);
        }
        // table name
        builder.append(" ");
        builder.append("FROM");
        builder.append(" ");
        builder.append(tableDefinition.getTableName());
        // search strategy
        builder.append(" ");
        builder.append("WHERE");
        builder.append(" ");
        builder.append(strategy.expand());
        // order by
        if (fetchDistance) {
            builder.append(" ");
            builder.append("ORDER BY ");
            builder.append(DISTANCE_ATTR_LABEL);
        }
        return builder.toString();
    }

    abstract @NotNull protected List<V> buildResult(
            ResultSet resultSet, JGeometry origin) throws SQLException;

}
