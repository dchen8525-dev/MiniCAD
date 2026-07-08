package com.minicad.step.model.technical.tolerance;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved PLUS_MINUS_TOLERANCE_WITH_MODIFIERS.
 * A plus-minus tolerance with modifiers entity.
 *
 * @param id STEP instance id
 * @param name tolerance name
 * @param upperDeviation upper deviation value
 * @param lowerDeviation lower deviation value
 * * @param deviationUnit deviation unit
 * @param modifiers tolerance modifiers
 */
/**
 * Resolved PLUS_MINUS_TOLERANCE_WITH_MODIFIERS.
 * A plus-minus tolerance with modifiers entity.
 *
 * @param id STEP instance id
 * @param name tolerance name
 * @param upperDeviation upper deviation value
 * @param lowerDeviation lower deviation value
 * * @param deviationUnit deviation unit
 * @param modifiers tolerance modifiers
 */
public final class StepPlusMinusToleranceWithModifiers implements StepEntity {
    private final int id;
    private final String name;
    private final Double upperDeviation;
    private final Double lowerDeviation;
    private final StepEntity deviationUnit;
    private final List<String> modifiers;

    public StepPlusMinusToleranceWithModifiers(int id, String name, Double upperDeviation, Double lowerDeviation, StepEntity deviationUnit, List<String> modifiers) {
        this.id = id;
        this.name = name;
        this.upperDeviation = upperDeviation;
        this.lowerDeviation = lowerDeviation;
        this.deviationUnit = deviationUnit;
        this.modifiers = modifiers == null ? null : java.util.List.copyOf(modifiers);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getUpperDeviation() {
        return upperDeviation;
    }

    public Double getLowerDeviation() {
        return lowerDeviation;
    }

    public StepEntity getDeviationUnit() {
        return deviationUnit;
    }

    public List<String> getModifiers() {
        return modifiers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPlusMinusToleranceWithModifiers that = (StepPlusMinusToleranceWithModifiers) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(upperDeviation, that.upperDeviation) && Objects.equals(lowerDeviation, that.lowerDeviation) && Objects.equals(deviationUnit, that.deviationUnit) && Objects.equals(modifiers, that.modifiers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, upperDeviation, lowerDeviation, deviationUnit, modifiers);
    }

    @Override
    public String toString() {
        return "StepPlusMinusToleranceWithModifiers{" + "id=" + id + "name=" + name + "upperDeviation=" + upperDeviation + "lowerDeviation=" + lowerDeviation + "deviationUnit=" + deviationUnit + "modifiers=" + modifiers + "}";
    }
}