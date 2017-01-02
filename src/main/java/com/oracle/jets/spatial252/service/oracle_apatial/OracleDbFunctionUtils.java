package com.oracle.jets.spatial252.service.oracle_apatial;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import oracle.spatial.geometry.JGeometry;

@Component
public class OracleDbFunctionUtils {

    @Autowired
    DataSource dataSource;

    private static final String QUERY_POINT_ON_LINE = "SELECT GETRATIO(?, ?) FROM DUAL";

    double getRatio(JGeometry point, JGeometry line) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(QUERY_POINT_ON_LINE);
            statement.setObject(1, JGeometry.storeJS(point, connection));
            statement.setObject(2, JGeometry.storeJS(line, connection));
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble(1);
            } else {
                // TODO: define Exception
                throw new IllegalStateException();
            }
        } catch (SQLException e) {
            // TODO: handle exception
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // do nothing.
            }
        }
    }

    
}
