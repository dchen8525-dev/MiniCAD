package com.minicad.step.model;

import java.util.Map;

/**
 * Resolved LOT_EFFECTIVITY.
 */
public final class StepLotEffectivity extends AbstractStepEntity {
    private final StepEntity effectivityLot;

    public StepLotEffectivity(int id, String name, StepEntity effectivityLot) {
        super(id, name);
        this.effectivityLot = effectivityLot;
    }

    public StepEntity getEffectivityLot() {
        return effectivityLot;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "effectivityLot", effectivityLot);
    }
}
