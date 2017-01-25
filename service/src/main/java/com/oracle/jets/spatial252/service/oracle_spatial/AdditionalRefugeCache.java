package com.oracle.jets.spatial252.service.oracle_spatial;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.oracle.jets.spatial252.service.AdditionalRefuge;

@Component
public class AdditionalRefugeCache {

    private List<AdditionalRefuge> refuges = new ArrayList<AdditionalRefuge>();

    private static AdditionalRefugeCache instance = null;

    static AdditionalRefugeCache getInstance() {
        if (instance == null) {
            instance = new AdditionalRefugeCache();
        }
        return instance;
    }

    void add(AdditionalRefuge refuge) {
        refuges.add(refuge);
    }

    // TODO getAll()はクローンしたオブジェクトを返却する
    List<AdditionalRefuge> getAll() {
        return refuges;
    }

}
