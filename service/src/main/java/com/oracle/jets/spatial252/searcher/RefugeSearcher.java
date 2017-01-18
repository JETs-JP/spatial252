package com.oracle.jets.spatial252.searcher;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.oracle.jets.spatial252.searcher.RefugeTableDefinition.Column;

import oracle.spatial.geometry.JGeometry;

@Component
@Scope("prototype")
public class RefugeSearcher extends SpatialSearcher<Refuge> {

    public RefugeSearcher() {
        super();
        tableDefinition = RefugeTableDefinition.getInstance();
    }

    @Override
    protected @NotNull List<Refuge> buildResult(
            ResultSet resultSet, JGeometry origin) throws SQLException {
        List<Refuge> refuges = new ArrayList<Refuge>();
        while (resultSet.next()) {
            Refuge.Builder builder = null;
            if (fetchDistance) {
                builder = new Refuge.Builder(
                        resultSet.getLong(tableDefinition.getIdColumnLabel()),
                        origin, resultSet.getDouble(DISTANCE_ATTR_LABEL));
            } else {
                builder = new Refuge.Builder(
                        resultSet.getLong(tableDefinition.getIdColumnLabel()),
                        origin);
            }
            Refuge refuge = null;
            if (fetchAllColmuns) {
                refuge = builder
                        .setName(resultSet.getString(Column.COL_HINANJO_NAME.getLabel()))
                        .setAddress(resultSet.getString(Column.COL_HINANJO_ADDR.getLabel()))
                        .setType(resultSet.getString(Column.COL_HINANJO_TYPE.getLabel()))
                        .setCapacity(resultSet.getInt(Column.COL_HINANJO_CAPACITY.getLabel()))
                        .setScale(resultSet.getString(Column.COL_HINANJO_SCALE.getLabel()))
                        .setEarthquake(resultSet.getInt(Column.COL_HINANJO_EARTHQUAKE.getLabel()))
                        .setTsunami(resultSet.getInt(Column.COL_HINANJO_TSUNAMI.getLabel()))
                        .setFlood(resultSet.getInt(Column.COL_HINANJO_FLOOD.getLabel()))
                        .setVolcanic(resultSet.getInt(Column.COL_HINANJO_VOLCANIC.getLabel()))
                        .setOtherHazard(resultSet.getInt(Column.COL_HINANJO_OTHER.getLabel()))
                        .setNotDefined(resultSet.getInt(Column.COL_HINANJO_NOTDIFINED.getLabel()))
                        .setLevel(resultSet.getInt(Column.COL_HINANJO_LEVEL.getLabel()))
                        .setRemarks(resultSet.getString(Column.COL_HINANJO_REMARKS.getLabel()))
                        .setLocation(JGeometry.loadJS((Struct) resultSet.getObject(Column.COL_HINANJO_GEOM.getLabel())))
                        .Build();
            } else {
                refuge = builder.Build();
            }
            refuges.add(refuge);
        }
        return refuges;
    }

}