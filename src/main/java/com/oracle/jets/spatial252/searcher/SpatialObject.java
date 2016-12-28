package com.oracle.jets.spatial252.searcher;

import oracle.spatial.geometry.JGeometry;

public abstract class SpatialObject {

    private final long id;
    private final JGeometry origin;
    private double distance = -1;

    SpatialObject(long id, JGeometry origin) {
        super();
        this.id = id;
        this.origin = origin;
    }

    SpatialObject(long id, JGeometry origin, double distance) {
        super();
        this.id = id;
        this.origin = origin;
        this.distance = distance;
    }

    public long getId() {
        return id;
    }

    public JGeometry getOrigin() {
        return origin;
    }

    /**
     * 検索で指定した起点からの距離を取得する
     * 
     * @return 検索の起点からの距離。未設定の場合-1
     */
    public double getDistance() {
        return distance;
    }

    public int furtherThan(SpatialObject another) {
        if (another == null) {
            throw new NullPointerException();
        }
        if (this.getDistance() < 0) {
            throw new IllegalStateException();
        }
        if (another.getDistance() < 0 || !origin.equals(another.getOrigin())) {
            throw new IllegalArgumentException();
        }
        if (distance < another.getDistance()) {
            return -1;
        }
        if (distance > another.getDistance()) {
            return 1;
        }
        return 0;
    }

}
