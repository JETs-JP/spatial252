package com.oracle.jets.spatial252.service.oracle_spatial.searcher;

/**
 * @author hhayakaw
 *
 * 検索条件
 * 
 */
public interface SpatialSearchStrategy {

    /**
     * SQLのWHERE句に当てはめる文字列に展開します。
     * 
     * @return SQLのWHERE句に当てはめる文字列
     */
    String expand();

}
