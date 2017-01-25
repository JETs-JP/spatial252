package com.oracle.jets.spatial252.service.oracle_spatial.searcher;

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