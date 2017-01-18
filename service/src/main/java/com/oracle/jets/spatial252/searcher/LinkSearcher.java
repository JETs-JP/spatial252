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
public class LinkSearcher extends SpatialSearcher<Link> {

    public LinkSearcher() {
        super();
        tableDefinition = LinkTableDefinition.getInstance();
    }

    @Override
    protected @NotNull List<Link> buildResult(
            ResultSet resultSet, JGeometry origin) throws SQLException {
        List<Link> links = new ArrayList<Link>();
        while (resultSet.next()) {
            if (fetchDistance) {
                links.add(new Link(
                        resultSet.getLong(tableDefinition.getIdColumnLabel()),
                        origin, resultSet.getDouble(DISTANCE_ATTR_LABEL)));
            } else {
                links.add(new Link(
                        resultSet.getLong(tableDefinition.getIdColumnLabel()),
                        origin));
            }
        }
        return links;
    }

 }