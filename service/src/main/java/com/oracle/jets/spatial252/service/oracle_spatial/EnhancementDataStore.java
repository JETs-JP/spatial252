package com.oracle.jets.spatial252.service.oracle_spatial;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

class EnhancementDataStore {

    private static final String RESOURCE_PATH = "refuges.json";

    private static EnhancementDataStore instance = null;

    private Map<Long, EnhancementData> cache = new HashMap<Long, EnhancementData>();

    private EnhancementDataStore() {
        loadResource();
    }

    static EnhancementDataStore getInstance() {
        if (instance == null) {
            instance = new EnhancementDataStore();
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

    EnhancementData getEnhancementData(Long id) {
        return cache.get(id);
    }

}
