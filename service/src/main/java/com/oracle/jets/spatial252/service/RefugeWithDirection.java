package com.oracle.jets.spatial252.service;

import com.fasterxml.jackson.annotation.JsonInclude;

@SuppressWarnings("unused")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public interface RefugeWithDirection {

    long getId();

    String getName();

    Point getLocation();

    Direction getDirection();

    String getCongestion();

    int getFood();

    int getBlanket();

    boolean isAccessible();

    int getMilk();

    int getBabyFood();

    boolean isNursingRoom();

    int getSanitaryGoods();

    int getNapkin();

    boolean isBath();

    boolean isPet();

    boolean isMultilingual();

    boolean isAvailable();

}
