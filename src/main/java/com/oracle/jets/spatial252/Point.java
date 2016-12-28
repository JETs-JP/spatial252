package com.oracle.jets.spatial252;

import oracle.spatial.geometry.JGeometry;

/**
 * 地図上の座標点
 * 
 * @author hhayakaw
 *
 */
class Point {

    /**
     * 緯度
     */
    private final double lat;
    /**
     * 経度
     */
    private final double lon;
    /**
     * このオブジェクトのJGeometry型の表現
     */
    private JGeometry jgeometry = null;

    /**
     * コンストラクタ
     * 
     * @param lat   緯度
     * @param lon   経度
     */
    Point(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    /**
     * 緯度を取得する
     * 
     * @return 緯度
     */
    public double getLat() {
        return lat;
    }

    /**
     * 経度を取得する
     * 
     * @return 経度
     */
    public double getLon() {
        return lon;
    }

    /**
     * このオブジェクトのJGeometry型の表現を取得する
     * 
     * @return このオブジェクトのJGeometry型の表現
     */
    JGeometry toJGeometry() {
        if (jgeometry == null) {
            double[] array = {lon, lat};
            jgeometry = JGeometry.createPoint(array, 2, 8307);
        }
        return jgeometry;
    }

}
