package com.oracle.jets.spatial252.service;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Polygon {

    private final String name;

    private final String description;

    private final List<Point> coordinates;

    @JsonCreator
    public Polygon(@JsonProperty("name") String name, 
            @JsonProperty("description") String description,
            @JsonProperty("coordinates") List<Point> coordinates) {
        this.name = name;
        this.description = description;
        this.coordinates = Collections.unmodifiableList(coordinates);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Point> getCoordinates() {
        return coordinates;
    }

}
