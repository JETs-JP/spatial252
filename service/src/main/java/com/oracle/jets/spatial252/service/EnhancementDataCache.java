package com.oracle.jets.spatial252.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EnhancementDataCache {

    private static final String RESOURCE_PATH = "refuges.json";

    private static EnhancementDataCache instance = null;

    private Map<Long, EnhancementData> cache = new HashMap<Long, EnhancementData>();

    private EnhancementDataCache() {
        loadResource();
    }

    static EnhancementDataCache getInstance() {
        if (instance == null) {
            instance = new EnhancementDataCache();
        }
        return instance;
    }

    private void loadResource() {
        try {
            InputStream in = this.getClass().getClassLoader().
                    getResourceAsStream(RESOURCE_PATH);
            List<EnhancementData> data = new ObjectMapper().
                    readValue(in, new TypeReference<List<EnhancementData>>() {});
            for (EnhancementData d : data) {
                cache.put(d.getId(), d);
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public EnhancementData getEnhancementData(Long id) {
        return cache.get(id);
    }

}
