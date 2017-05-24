package com.oracle.jets.spatial252.service.oracle_spatial;

import java.util.ArrayList;
import java.util.List;

class DisabledRefugesList {

    private List<Long> disabledRefuges = new ArrayList<>();

    private static DisabledRefugesList instance = null;

    static DisabledRefugesList getInstance() {
        if (instance == null) {
            instance = new DisabledRefugesList();
        }
        return instance;
    }

    // uninstanciable
    private DisabledRefugesList() {}

    void add(Long id) {
        disabledRefuges.add(id);
    }

    boolean remove(Long id) {
        return disabledRefuges.remove(id);
    }

    boolean contains(Long id) {
        return disabledRefuges.contains(id);
    }
}
