package com.oracle.jets.spatial252.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EnhancementData {

    private final Long id;
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

    @JsonCreator
    public EnhancementData(
            @JsonProperty("id") Long id,
            @JsonProperty("congestion") String congestion,
            @JsonProperty("food") int food,
            @JsonProperty("blanket") int blanket,
            @JsonProperty("accessible") boolean accessible,
            @JsonProperty("milk") int milk,
            @JsonProperty("babyFood") int baby_food,
            @JsonProperty("nursingRoom") boolean nursing_room,
            @JsonProperty("sanitaryGoods") int sanitary_goods,
            @JsonProperty("napkin") int napkin,
            @JsonProperty("bath") boolean bath,
            @JsonProperty("pet") boolean pet,
            @JsonProperty("multilingual") boolean multilingual) {
        super();
        this.id = id;
        this.congestion = congestion;
        this.food = food;
        this.blanket = blanket;
        this.accessible = accessible;
        this.milk = milk;
        this.babyFood = baby_food;
        this.nursingRoom = nursing_room;
        this.sanitaryGoods = sanitary_goods;
        this.napkin = napkin;
        this.bath = bath;
        this.pet = pet;
        this.multilingual = multilingual;
    }

    public Long getId() {
        return id;
    }

    public String getCongestion() {
        return congestion;
    }

    public int getFood() {
        return food;
    }

    public int getBlanket() {
        return blanket;
    }

    public boolean isAccessible() {
        return accessible;
    }

    public int getMilk() {
        return milk;
    }

    public int getBabyFood() {
        return babyFood;
    }

    public boolean isNursingRoom() {
        return nursingRoom;
    }

    public int getSanitaryGoods() {
        return sanitaryGoods;
    }

    public int getNapkin() {
        return napkin;
    }

    public boolean isBath() {
        return bath;
    }

    public boolean isPet() {
        return pet;
    }

    public boolean isMultilingual() {
        return multilingual;
    }

}
