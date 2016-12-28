package com.oracle.jets.spatial252.searcher;

/**
 * @author hhayakaw
 *
 * 近傍にあるオブジェクトを、距離が近い順に取得する検索条件
 */
public class NearestNeighborStrategy implements SpatialSearchStrategy {

    private final int limit;

    /**
     * コンストラクタ
     * 
     * @param limit 取得するオブジェクトの上限数
     */
    public NearestNeighborStrategy(int limit) {
        this.limit = limit;
    }

    /* (non-Javadoc)
     * @see jp.gr.java_conf.hhayakawa_jp.spatial.dbcore.GeoSearchStrategy#expand()
     */
    @Override
    public String expand() {
        StringBuilder builder = new StringBuilder();
        builder.append("SDO_NN(GEOMETRY, ?, 'SDO_NUM_RES=");
        builder.append(limit);
        builder.append("', 1) = 'TRUE'");
        return builder.toString();
    }

}
