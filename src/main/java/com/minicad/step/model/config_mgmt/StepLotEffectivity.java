package com.minicad.step.model.config_mgmt;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;

/**
 * Resolved LOT_EFFECTIVITY.
 */
/**
 * Resolved LOT_EFFECTIVITY.
 */
public final class StepLotEffectivity implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity effectivityLot;

    public StepLotEffectivity(int id, String name, StepEntity effectivityLot) {
        this.id = id;
        this.name = name;
        this.effectivityLot = effectivityLot;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getEffectivityLot() {
        return effectivityLot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepLotEffectivity that = (StepLotEffectivity) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(effectivityLot, that.effectivityLot);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, effectivityLot);
    }

    @Override
    public String toString() {
        return "StepLotEffectivity{" + "id=" + id + "name=" + name + "effectivityLot=" + effectivityLot + "}";
    }
}
