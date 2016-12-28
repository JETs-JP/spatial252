package com.oracle.jets.spatial252.searcher;

import java.util.ArrayList;
import java.util.List;

/**
 * リンクの情報が格納されたテーブルのテーブル定義
 * 
 * @author hhayakaw
 *
 */
class LinkTableDefinition implements TableDefinition {

    private static LinkTableDefinition instance = null;

    static TableDefinition getInstance() {
        if (instance == null) {
            instance = new LinkTableDefinition();
        }
        return instance;
    }

    // uninstanciable
    private LinkTableDefinition() {}

    /**
     * テーブル名
     */
    static final String TABLE_NAME = "ZROAD_NET_LINK$";

    /**
     * このテーブルの列の定義
     * 
     * @author hhayakaw
     *
     */
    static enum Column {
        COL_LINK_ID("LINK_ID"),
        ;

        private final String label;

        private Column(String label) {
            this.label = label;
        }

        /**
         * 列名を取得する
         * 
         * @return 列名
         */
        String getLabel() {
            return label;
        }

    }

    /* (non-Javadoc)
     * @see jp.gr.java_conf.hhayakawa_jp.spatial.dbcore.TableDefinition#getTableName()
     */
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    /* (non-Javadoc)
     * @see jp.gr.java_conf.hhayakawa_jp.spatial.dbcore.TableDefinition#getIdColumnLabel()
     */
    @Override
    public String getIdColumnLabel() {
        return Column.COL_LINK_ID.getLabel();
    }

    /* (non-Javadoc)
     * @see jp.gr.java_conf.hhayakawa_jp.spatial.dbcore.TableDefinition#getAllColumnLabels()
     */
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
