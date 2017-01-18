package com.oracle.jets.spatial252.searcher;

import java.util.List;

/**
 * @author hhayakaw
 *
 */
interface TableDefinition {

    /**
     * @return
     */
    String getTableName();

    /**
     * @return
     */
    String getIdColumnLabel();

    /**
     * @return
     */
    List<String> getAllColumnLabels();

}