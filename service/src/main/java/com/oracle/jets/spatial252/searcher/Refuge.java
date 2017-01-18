/**
 * 
 */
package com.oracle.jets.spatial252.searcher;

import oracle.spatial.geometry.JGeometry;

/**
 * 避難所のモデルクラス
 * 
 * @author rnakai
 */
public class Refuge extends SpatialObject {

    private final String area;         // P20_001
    private final String name;         // P20_002
    private final String address;      // P20_003
    private final String type;         // P20_004
    private final int capacity;        // P20_005
    private final String scale;        // P20_006
    private final int earthquake;      // P20_007
    private final int tsunami;         // P20_008
    private final int flood;           // P20_009
    private final int volcanic;        // P20_010
    private final int otherHazard;     // P20_011
    private final int notDefined;      // P20_012
    private final int level;           // レベル;
    private final String remarks;      // 備考
    private final JGeometry location;  // Geometry

    public static class Builder {

        private final long id;
        private final JGeometry origin;
        private double distance = -1;

        private String area = null;         // P20_001
        private String name = null;         // P20_002
        private String address = null;      // P20_003
        private String type = null;         // P20_004
        private int capacity = -1;          // P20_005
        private String scale = null;        // P20_006
        private int earthquake = -1;        // P20_007
        private int tsunami = -1;           // P20_008
        private int flood = -1;             // P20_009
        private int volcanic = -1;          // P20_010
        private int otherHazard = -1;       // P20_011
        private int notDefined = -1;        // P20_012
        private int level = -1;             // レベル;
        private String remarks = null;      // 備考
        private JGeometry location = null;  // Geometry

        public Builder(long id, JGeometry origin) {
            this.id = id;
            this.origin = origin;
        }

        public Builder(long id, JGeometry origin, double distance) {
            this.id = id;
            this.origin = origin;
            this.distance = distance;
        }

        public Refuge Build() {
            return new Refuge(id, origin, distance,
                    area, name, address, type, capacity,
                    scale, earthquake, tsunami, flood, volcanic, otherHazard,
                    notDefined, level, remarks, location);
        }

        public Builder setArea(String area) {
            this.area = area;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setAddress(String address) {
            this.address = address;
            return this;
        }

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public Builder setCapacity(int capacity) {
            this.capacity = capacity;
            return this;
        }

        public Builder setScale(String scale) {
            this.scale = scale;
            return this;
        }

        public Builder setEarthquake(int earthquake) {
            this.earthquake = earthquake;
            return this;
        }

        public Builder setTsunami(int tsunami) {
            this.tsunami = tsunami;
            return this;
        }

        public Builder setFlood(int flood) {
            this.flood = flood;
            return this;
        }

        public Builder setVolcanic(int volcanic) {
            this.volcanic = volcanic;
            return this;
        }

        public Builder setOtherHazard(int otherHazard) {
            this.otherHazard = otherHazard;
            return this;
        }

        public Builder setNotDefined(int notDefined) {
            this.notDefined = notDefined;
            return this;
        }

        public Builder setLevel(int level) {
            this.level = level;
            return this;
        }

        public Builder setRemarks(String remarks) {
            this.remarks = remarks;
            return this;
        }

        public Builder setLocation(JGeometry location) {
            this.location = location;
            return this;
        }
    }

    private Refuge(long id, JGeometry origin, double distance,
            String area, String name, String address, String type,
            int capacity, String scale, int earthquake, int tsunami, int flood,
            int volcanic, int otherHazard, int notDefined, int level,
            String remarks, JGeometry location) {
        super(id, origin, distance);
        this.area = area;
        this.name = name;
        this.address = address;
        this.type = type;
        this.capacity = capacity;
        this.scale = scale;
        this.earthquake = earthquake;
        this.tsunami = tsunami;
        this.flood = flood;
        this.volcanic = volcanic;
        this.otherHazard = otherHazard;
        this.notDefined = notDefined;
        this.level = level;
        this.remarks = remarks;
        this.location = location;
    }

    public String getArea() {
        return area;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getType() {
        return type;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getScale() {
        return scale;
    }

    public int getEarthquake() {
        return earthquake;
    }

    public int getTsunami() {
        return tsunami;
    }

    public int getFlood() {
        return flood;
    }

    public int getVolcanic() {
        return volcanic;
    }

    public int getOtherHazard() {
        return otherHazard;
    }

    public int getNotDefined() {
        return notDefined;
    }

    public int getLevel() {
        return level;
    }

    public String getRemarks() {
        return remarks;
    }

    public JGeometry getLocation() {
        return location;
    }

}
