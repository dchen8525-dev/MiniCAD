package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;

/**
 * Resolved DATE_EFFECTIVITY.
 */
/**
 * Resolved DATE_EFFECTIVITY.
 */
public final class StepDateEffectivity implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity effectiveDate;

    public StepDateEffectivity(int id, String name, StepEntity effectiveDate) {
        this.id = id;
        this.name = name;
        this.effectiveDate = effectiveDate;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getEffectiveDate() {
        return effectiveDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDateEffectivity that = (StepDateEffectivity) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(effectiveDate, that.effectiveDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, effectiveDate);
    }

    @Override
    public String toString() {
        return "StepDateEffectivity{" + "id=" + id + "name=" + name + "effectiveDate=" + effectiveDate + "}";
    }
}
