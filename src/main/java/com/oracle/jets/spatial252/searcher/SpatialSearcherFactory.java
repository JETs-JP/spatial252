package com.oracle.jets.spatial252.searcher;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class SpatialSearcherFactory {

    // TODO: connection poolからsearcherのコネクションが取得できるようになったらこれを使う
    public static <T extends SpatialSearcher<?>> T getSearcher(Class<T> searcherType) {
        try {
            Constructor<T> constructor = searcherType.getConstructor();
            T searcher = constructor.newInstance();
            return searcher;
        } catch (NoSuchMethodException | SecurityException |
                InstantiationException | IllegalAccessException |
                IllegalArgumentException | InvocationTargetException e) {
            throw new IllegalStateException(
                    "Failed to create a searcher instance using reflection.", e);
        }
    }

}
