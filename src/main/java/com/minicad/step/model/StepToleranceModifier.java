package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved TOLERANCE_MODIFIER.
 * A tolerance modifier entity.
 *
 * @param id STEP instance id
 * @param name modifier name
 * @param modifierType modifier type (M, L, S, etc.)
 * @param modifierValue modifier value if applicable
 * @param appliedTolerance tolerance the modifier applies to
 */
/**
 * Resolved TOLERANCE_MODIFIER.
 * A tolerance modifier entity.
 *
 * @param id STEP instance id
 * @param name modifier name
 * @param modifierType modifier type (M, L, S, etc.)
 * @param modifierValue modifier value if applicable
 * @param appliedTolerance tolerance the modifier applies to
 */
public final class StepToleranceModifier implements StepEntity {
    private final int id;
    private final String name;
    private final String modifierType;
    private final double modifierValue;
    private final StepEntity appliedTolerance;

    public StepToleranceModifier(int id, String name, String modifierType, double modifierValue, StepEntity appliedTolerance) {
        this.id = id;
        this.name = name;
        this.modifierType = modifierType;
        this.modifierValue = modifierValue;
        this.appliedTolerance = appliedTolerance;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getModifierType() {
        return modifierType;
    }

    public double getModifierValue() {
        return modifierValue;
    }

    public StepEntity getAppliedTolerance() {
        return appliedTolerance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepToleranceModifier that = (StepToleranceModifier) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(modifierType, that.modifierType) && modifierValue == that.modifierValue && Objects.equals(appliedTolerance, that.appliedTolerance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, modifierType, modifierValue, appliedTolerance);
    }

    @Override
    public String toString() {
        return "StepToleranceModifier{" + "id=" + id + "name=" + name + "modifierType=" + modifierType + "modifierValue=" + modifierValue + "appliedTolerance=" + appliedTolerance + "}";
    }
}