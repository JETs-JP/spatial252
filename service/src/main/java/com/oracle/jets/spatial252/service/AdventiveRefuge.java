package com.oracle.jets.spatial252.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AdventiveRefuge implements RefugeWithDirection {

    private final long id;
    private final String name;
    private final Point location;
    private final String congestion;
    private final int food;
    private final int blanket;
    private final boolean accessible;
    private final int milk;
    private final int babyFood;
    private final boolean nursingRoom;
    private final int sanitaryGoods;
    private final int napkin;
    private final boolean bath;
    private final boolean pet;
    private final boolean multilingual;

    private Direction direction;

    @JsonCreator
    public AdventiveRefuge(
            @JsonProperty("id") long id,
            @JsonProperty("name") String name,
            @JsonProperty("location") Point location,
            @JsonProperty("congestion") String congestion,
            @JsonProperty("food") int food,
            @JsonProperty("blanket") int blanket,
            @JsonProperty("accessible") boolean accessible,
            @JsonProperty("milk") int milk,
            @JsonProperty("babyFood") int babyFood,
            @JsonProperty("nursingRoom") boolean nursingRoom,
            @JsonProperty("sanitaryGoods") int sanitaryGoods,
            @JsonProperty("napkin") int napkin,
            @JsonProperty("path") boolean bath,
            @JsonProperty("pet") boolean pet,
            @JsonProperty("multilingual") boolean multilingual) {
        super();
        this.id = id;
        this.name = name;
        this.location = location;
        this.congestion = congestion;
        this.food = food;
        this.blanket = blanket;
        this.accessible = accessible;
        this.milk = milk;
        this.babyFood = babyFood;
        this.nursingRoom = nursingRoom;
        this.sanitaryGoods = sanitaryGoods;
        this.napkin = napkin;
        this.bath = bath;
        this.pet = pet;
        this.multilingual = multilingual;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Point getLocation() {
        return location;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public String getCongestion() {
        return congestion;
    }

    @Override
    public int getFood() {
        return food;
    }

    @Override
    public int getBlanket() {
        return blanket;
    }

    @Override
    public boolean isAccessible() {
        return accessible;
    }

    @Override
    public int getMilk() {
        return milk;
    }

    @Override
    public int getBabyFood() {
        return babyFood;
    }

    @Override
    public boolean isNursingRoom() {
        return nursingRoom;
    }

    @Override
    public int getSanitaryGoods() {
        return sanitaryGoods;
    }

    @Override
    public int getNapkin() {
        return napkin;
    }

    @Override
    public boolean isBath() {
        return bath;
    }

    @Override
    public boolean isPet() {
        return pet;
    }

    @Override
    public boolean isMultilingual() {
        return multilingual;
    }

    public boolean isAvailable() {
        // TODO 実装
        return true;
    }

}
