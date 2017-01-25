package com.oracle.jets.spatial252.service;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EnhancedRefugeWithDirection implements RefugeWithDirection {

    private final RefugeWithDirectionImpl refuge;

    private final EnhancementData enhancementData;

    public EnhancedRefugeWithDirection(RefugeWithDirectionImpl refugeWithDirection) {
        // TODP parameter check
        this.refuge = refugeWithDirection;
        this.enhancementData = EnhancementDataCache.
                getInstance().getEnhancementData(refuge.getId());
    }

    public long getId() {
        return refuge.getId();
    }

    public String getArea() {
        return refuge.getArea();
    }

    public String getName() {
        return refuge.getName();
    }

    public String getAddress() {
        return refuge.getAddress();
    }

    public String getType() {
        return refuge.getType();
    }

    public int getCapacity() {
        return refuge.getCapacity();
    }

    public String getScale() {
        return refuge.getScale();
    }

    public boolean getEarthquake() {
        return refuge.getEarthquake();
    }

    public boolean getTsunami() {
        return refuge.getTsunami();
    }

    public boolean getFlood() {
        return refuge.getFlood();
    }

    public boolean getVolcanic() {
        return refuge.getVolcanic();
    }

    public boolean getOtherHazard() {
        return refuge.getOtherHazard();
    }

    public boolean getNotDefined() {
        return refuge.getNotDefined();
    }

    public int getLevel() {
        return refuge.getLevel();
    }

    public String getRemarks() {
        return refuge.getRemarks();
    }

    public Point getLocation() {
        return refuge.getLocation();
    }

    public Direction getDirection() {
        return refuge.getDirection();
    }

    public int getCongestion() {
        if (enhancementData == null) {
            return 0;
        }
        return enhancementData.getCongestion();
    }

    public int getFood() {
        if (enhancementData == null) {
            return 0;
        }
        return enhancementData.getFood();
    }

    public int getBlanket() {
        if (enhancementData == null) {
            return 0;
        }
        return enhancementData.getBlanket();
    }

    public boolean isAccessible() {
        if (enhancementData == null) {
            return false;
        }
        return enhancementData.isAccessible();
    }

    public int getMilk() {
        if (enhancementData == null) {
            return 0;
        }
        return enhancementData.getMilk();
    }

    public int getBabyFood() {
        if (enhancementData == null) {
            return 0;
        }
        return enhancementData.getBabyFood();
    }

    public boolean isNursingRoom() {
        if (enhancementData == null) {
            return false;
        }
        return enhancementData.isNursingRoom();
    }

    public int getSanitaryGoods() {
        if (enhancementData == null) {
            return 0;
        }
        return enhancementData.getSanitaryGoods();
    }

    public int getNapkin() {
        if (enhancementData == null) {
            return 0;
        }
        return enhancementData.getNapkin();
    }

    public boolean isBath() {
        if (enhancementData == null) {
            return false;
        }
        return enhancementData.isBath();
    }

    public boolean isPet() {
        if (enhancementData == null) {
            return false;
        }
        return enhancementData.isPet();
    }

    public boolean isMultilingual() {
        if (enhancementData == null) {
            return false;
        }
        return enhancementData.isMultilingual();
    }
 
}
