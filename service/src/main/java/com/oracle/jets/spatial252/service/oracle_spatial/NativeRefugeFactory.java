package com.oracle.jets.spatial252.service.oracle_spatial;

import com.oracle.jets.spatial252.service.Direction;
import com.oracle.jets.spatial252.service.RefugeWithDirection;
import com.oracle.jets.spatial252.service.oracle_spatial.searcher.Refuge;

class NativeRefugeFactory {

    static RefugeWithDirection create(Refuge refuge, Direction direction) {
        if (refuge == null) {
            throw new NullPointerException();
        }
        if (direction == null) {
            throw new NullPointerException();
        }
        RefugeWithDirectionImpl nativeRefuge = new RefugeWithDirectionImpl(
                refuge, direction);
        EnhancementData enhancementData =
                EnhancementDataStore.getInstance().getEnhancementData(refuge.getId());
        nativeRefuge.setEnhancementData(enhancementData);
        if (DisabledRefugesList.getInstance().contains(refuge.getId())) {
            nativeRefuge.disable();
        }
        return nativeRefuge;
    }

}
