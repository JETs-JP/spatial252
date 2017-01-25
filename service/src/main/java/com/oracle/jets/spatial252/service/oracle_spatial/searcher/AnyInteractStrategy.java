package com.oracle.jets.spatial252.service.oracle_spatial.searcher;

/**
 * @author hhayakaw
 *
 * 指定されたオブジェクトに対するインタラクションを持つ、
 * 全てのオブジェクト取得する検索条件
 */
public class AnyInteractStrategy implements SpatialSearchStrategy {

    /* (non-Javadoc)
     * @see jp.gr.java_conf.hhayakawa_jp.spatial.dbcore.GeoSearchStrategy#expand()
     */
    @Override
    public String expand() {
        return "SDO_ANYINTERACT(geometry, ?) = 'TRUE'";
    }

}
