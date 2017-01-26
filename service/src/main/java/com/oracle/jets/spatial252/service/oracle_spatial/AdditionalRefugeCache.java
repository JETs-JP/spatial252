package com.oracle.jets.spatial252.service.oracle_spatial;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.oracle.jets.spatial252.service.AdditionalRefuge;

@Component
public class AdditionalRefugeCache {

    //private List<AdditionalRefuge> refuges = new ArrayList<AdditionalRefuge>();
    private Map<Long, AdditionalRefuge> cache = new HashMap<Long, AdditionalRefuge>();

    private static AdditionalRefugeCache instance = null;

    static AdditionalRefugeCache getInstance() {
        if (instance == null) {
            instance = new AdditionalRefugeCache();
        }
        return instance;
    }

    void add(AdditionalRefuge refuge) {
        cache.put(refuge.getId(), refuge);
    }

    AdditionalRefuge get(Long id) {
        return cache.get(id);
    }

    // TODO getAll()はクローンしたオブジェクトを返却する
    Collection<AdditionalRefuge> getAll() {
        return cache.values();
    }

}
