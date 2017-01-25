package com.oracle.jets.spatial252.service.oracle_spatial.searcher;

import java.util.ArrayList;
import java.util.List;

class RefugeTableDefinition implements TableDefinition {

    private static RefugeTableDefinition instance = null;

    static RefugeTableDefinition getInstance() {
        if (instance == null) {
            instance = new RefugeTableDefinition();
        }
        return instance;
    }

    // uninstanciable
    private RefugeTableDefinition() {}

    static final String TABLE_NAME = "HINANBASHO";

    static enum Column {
        COL_HINANJO_ID("NO"),
        COL_HINANJO_AREA("P20_001"),
        COL_HINANJO_NAME("P20_002"),
        COL_HINANJO_ADDR("P20_003"),
        COL_HINANJO_TYPE("P20_004"),
        COL_HINANJO_CAPACITY("P20_005"),
        COL_HINANJO_SCALE("P20_006"),
        COL_HINANJO_EARTHQUAKE("P20_007"),
        COL_HINANJO_TSUNAMI("P20_008"),
        COL_HINANJO_FLOOD("P20_009"),
        COL_HINANJO_VOLCANIC("P20_010"),
        COL_HINANJO_OTHER("P20_011"),
        COL_HINANJO_NOTDIFINED("P20_012"),
        COL_HINANJO_LEVEL("レベル"),
        COL_HINANJO_REMARKS("備考"),
        COL_HINANJO_GEOM("GEOMETRY"),
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
        return Column.COL_HINANJO_ID.getLabel();
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
