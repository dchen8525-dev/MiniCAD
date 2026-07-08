package com.minicad.step.model.management.config;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;

/**
 * Resolved DATE_TIME_EFFECTIVITY.
 */
/**
 * Resolved DATE_TIME_EFFECTIVITY.
 */
public final class StepDateTimeEffectivity implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity effectiveDateTime;

    public StepDateTimeEffectivity(int id, String name, StepEntity effectiveDateTime) {
        this.id = id;
        this.name = name;
        this.effectiveDateTime = effectiveDateTime;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getEffectiveDateTime() {
        return effectiveDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDateTimeEffectivity that = (StepDateTimeEffectivity) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(effectiveDateTime, that.effectiveDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, effectiveDateTime);
    }

    @Override
    public String toString() {
        return "StepDateTimeEffectivity{" + "id=" + id + "name=" + name + "effectiveDateTime=" + effectiveDateTime + "}";
    }
}
