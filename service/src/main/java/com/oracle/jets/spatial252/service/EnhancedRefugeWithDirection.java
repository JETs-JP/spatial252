package com.oracle.jets.spatial252.service;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EnhancedRefugeWithDirection implements RefugeWithDirection {

    private final RefugeWithDirectionImpl refugeWithDirection;

    private final EnhancementData enhancementData;

    private final boolean enabled;

    static List<Long> disabledRefuges = new ArrayList<>();

    public static EnhancedRefugeWithDirection enhance(
            RefugeWithDirectionImpl refugeWithDirection) {
        if (refugeWithDirection == null) {
            throw new NullPointerException();
        }
        return new EnhancedRefugeWithDirection(refugeWithDirection);
    }

    private EnhancedRefugeWithDirection(RefugeWithDirectionImpl refugeWithDirection) {
        this.refugeWithDirection = refugeWithDirection;
        this.enhancementData = EnhancementDataCache.
                getInstance().getEnhancementData(refugeWithDirection.getId());
        if (disabledRefuges.contains(refugeWithDirection.getId())) {
            enabled = false;
        } else {
            enabled = true;
        }
    }

    public static void disable(Long id) {
        if (id == null) {
            return;
        }
        disabledRefuges.add(id);
    }

    public long getId() {
        return refugeWithDirection.getId();
    }

    public String getArea() {
        return refugeWithDirection.getArea();
    }

    public String getName() {
        return refugeWithDirection.getName();
    }

    public String getAddress() {
        return refugeWithDirection.getAddress();
    }

    public String getType() {
        return refugeWithDirection.getType();
    }

    public int getCapacity() {
        return refugeWithDirection.getCapacity();
    }

    public String getScale() {
        return refugeWithDirection.getScale();
    }

    public boolean getEarthquake() {
        return refugeWithDirection.getEarthquake();
    }

    public boolean getTsunami() {
        return refugeWithDirection.getTsunami();
    }

    public boolean getFlood() {
        return refugeWithDirection.getFlood();
    }

    public boolean getVolcanic() {
        return refugeWithDirection.getVolcanic();
    }

    public boolean getOtherHazard() {
        return refugeWithDirection.getOtherHazard();
    }

    public boolean getNotDefined() {
        return refugeWithDirection.getNotDefined();
    }

    public int getLevel() {
        return refugeWithDirection.getLevel();
    }

    public String getRemarks() {
        return refugeWithDirection.getRemarks();
    }

    public Point getLocation() {
        return refugeWithDirection.getLocation();
    }

    public Direction getDirection() {
        return refugeWithDirection.getDirection();
    }

    public String getCongestion() {
        if (enhancementData == null) {
            return null;
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
 
    public boolean isEnabled() {
        return enabled;
    }

}
