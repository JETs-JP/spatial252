package com.oracle.jets.spatial252.service.oracle_spatial;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.oracle.jets.spatial252.service.AdventiveRefuge;

class AdventiveRefugesStore {

    private Map<Long, AdventiveRefuge> cache = new HashMap<>();

    private static AdventiveRefugesStore instance;

    static AdventiveRefugesStore getInstance() {
        if (instance == null) {
            instance = new AdventiveRefugesStore();
        }
        return instance;
    }

    // uninstaciable
    private AdventiveRefugesStore() {}

    AdventiveRefuge add(AdventiveRefuge refuge) {
        return cache.put(refuge.getId(), refuge);
    }

    AdventiveRefuge get(Long id) {
        return cache.get(id);
    }

    // TODO getAll()はクローンしたオブジェクトを返却する
    Collection<AdventiveRefuge> getAll() {
        return cache.values();
    }

}
