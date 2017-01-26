package com.oracle.jets.spatial252.service;

public interface RefugeWithDirection {

    public long getId();

//    public String getArea();

    public String getName();

//    public String getAddress();

//    public String getType();

//    public int getCapacity();

//    public String getScale();

//    public boolean getEarthquake();

//    public boolean getTsunami();

//    public boolean getFlood();

//    public boolean getVolcanic();

//    public boolean getOtherHazard();

//    public boolean getNotDefined();

//    public int getLevel();

//    public String getRemarks();

    public Point getLocation();

    public Direction getDirection();

    public String getCongestion();

    public int getFood();

    public int getBlanket();

    public boolean isAccessible();

    public int getMilk();

    public int getBabyFood();

    public boolean isNursingRoom();

    public int getSanitaryGoods();

    public int getNapkin();

    public boolean isBath();

    public boolean isPet();

    public boolean isMultilingual();

    public boolean isEnabled();

}
