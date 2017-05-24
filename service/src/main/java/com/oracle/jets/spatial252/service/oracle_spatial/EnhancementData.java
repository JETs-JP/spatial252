package com.oracle.jets.spatial252.service.oracle_spatial;

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

    String getCongestion() {
        return congestion;
    }

    int getFood() {
        return food;
    }

    int getBlanket() {
        return blanket;
    }

    boolean isAccessible() {
        return accessible;
    }

    int getMilk() {
        return milk;
    }

    int getBabyFood() {
        return babyFood;
    }

    boolean isNursingRoom() {
        return nursingRoom;
    }

    int getSanitaryGoods() {
        return sanitaryGoods;
    }

    int getNapkin() {
        return napkin;
    }

    boolean isBath() {
        return bath;
    }

    boolean isPet() {
        return pet;
    }

    boolean isMultilingual() {
        return multilingual;
    }

}
