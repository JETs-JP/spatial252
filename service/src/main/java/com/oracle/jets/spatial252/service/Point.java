package com.oracle.jets.spatial252.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 地図上の座標点
 * 
 * @author hhayakaw
 *
 */
public class Point {

    /**
     * 緯度
     */
    private final double lat;
    /**
     * 経度
     */
    private final double lng;

    /**
     * コンストラクタ
     * 
     * @param lat   緯度
     * @param lng   経度
     */
    @JsonCreator
    public Point(
            @JsonProperty("lat") double lat,
            @JsonProperty("lng") double lng) {
        this.lat = lat;
        this.lng = lng;
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
    public double getLng() {
        return lng;
    }

}
