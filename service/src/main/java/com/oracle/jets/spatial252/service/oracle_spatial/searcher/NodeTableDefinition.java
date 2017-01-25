package com.oracle.jets.spatial252.service.oracle_spatial.searcher;

import java.util.ArrayList;
import java.util.List;

class NodeTableDefinition implements TableDefinition {

    private static NodeTableDefinition instance = null;

    static TableDefinition getInstance() {
        if (instance == null) {
            instance = new NodeTableDefinition();
        }
        return instance;
    }

    // uninstanciable
    private NodeTableDefinition() {}

    static final String TABLE_NAME = "ZROAD_NET_NODE$";

    static enum Column {
        COL_NODE_ID("NODE_ID"),
        ;

        private final String label;

        private Column(String label) {
            this.label = label;
        }

        String getLabel() {
            return label;
        }

    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String getIdColumnLabel() {
        return Column.COL_NODE_ID.getLabel();
    }

    @Override
    public List<String> getAllColumnLabels() {
        Column[] columns = Column.values();
        List<String> labels = new ArrayList<String>(columns.length);
        for (Column column : columns) {
            labels.add(column.getLabel());
        }
        return labels;
    }

}
