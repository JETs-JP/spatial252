package com.oracle.jets.spatial252.service.oracle_spatial;

import com.oracle.jets.spatial252.service.Direction;
import com.oracle.jets.spatial252.service.Point;
import com.oracle.jets.spatial252.service.RefugeWithDirection;
import com.oracle.jets.spatial252.service.oracle_spatial.searcher.Refuge;

public class RefugeWithDirectionImpl implements RefugeWithDirection {

    private final Refuge refuge;

    private final Direction direction;

    private EnhancementData enhancementData;

    private boolean available = true;

    RefugeWithDirectionImpl(
            Refuge refuge, Direction direction) {
        this.refuge = refuge;
        this.direction = direction;
    }

    void disable() {
        DisabledRefugesList.getInstance().add(this.getId());
        available = false;
    }

    void enable() {
        DisabledRefugesList.getInstance().remove(this.getId());
        available = true;
    }

    void setEnhancementData(EnhancementData enhancementData) {
        this.enhancementData = enhancementData;
    }

    @Override
    public long getId() {
        return refuge.getId();
    }

    @Override
    public String getName() {
        return refuge.getName();
    }

    @Override
    public Point getLocation() {
        double[] point = refuge.getLocation().getPoint();
        return new Point(point[1], point[0]);
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public String getCongestion() {
        if (enhancementData == null) {
            return null;
        }
        return enhancementData.getCongestion();
    }

    @Override
    public int getFood() {
        if (enhancementData == null) {
            return 0;
        }
        return enhancementData.getFood();
    }

    @Override
    public int getBlanket() {
        if (enhancementData == null) {
            return 0;
        }
        return enhancementData.getBlanket();
    }

    @Override
    public boolean isAccessible() {
        return enhancementData != null && enhancementData.isAccessible();
    }

    @Override
    public int getMilk() {
        if (enhancementData == null) {
            return 0;
        }
        return enhancementData.getMilk();
    }

    @Override
    public int getBabyFood() {
        if (enhancementData == null) {
            return 0;
        }
        return enhancementData.getBabyFood();
    }

    @Override
    public boolean isNursingRoom() {
        return enhancementData != null && enhancementData.isNursingRoom();
    }

    @Override
    public int getSanitaryGoods() {
        if (enhancementData == null) {
            return 0;
        }
        return enhancementData.getSanitaryGoods();
    }

    @Override
    public int getNapkin() {
        if (enhancementData == null) {
            return 0;
        }
        return enhancementData.getNapkin();
    }

    @Override
    public boolean isBath() {
        return enhancementData != null && enhancementData.isBath();
    }

    @Override
    public boolean isPet() {
        return enhancementData != null && enhancementData.isPet();
    }

    @Override
    public boolean isMultilingual() {
        return enhancementData != null && enhancementData.isMultilingual();
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

}
