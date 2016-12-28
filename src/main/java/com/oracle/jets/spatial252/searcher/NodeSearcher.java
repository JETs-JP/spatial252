package com.oracle.jets.spatial252.searcher;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import oracle.spatial.geometry.JGeometry;

@Component
@Scope("prototype")
public class NodeSearcher extends SpatialSearcher<Node> {

    public NodeSearcher() {
        super();
        tableDefinition = NodeTableDefinition.getInstance();
    }

    @Override
    protected @NotNull List<Node> buildResult(
            ResultSet resultSet, JGeometry origin) throws SQLException {
        List<Node> nodes = new ArrayList<Node>();
        while (resultSet.next()) {
            if (fetchDistance) {
                nodes.add(new Node(
                        resultSet.getLong(tableDefinition.getIdColumnLabel()),
                        origin, resultSet.getDouble(DISTANCE_ATTR_LABEL)));
            } else {
                nodes.add(new Node(
                        resultSet.getLong(tableDefinition.getIdColumnLabel()),
                        origin));
            }
        }
        return nodes;
    }

 }