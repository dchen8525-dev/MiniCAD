package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved BASELINE_DEFINITION.
 * A baseline definition entity.
 *
 * @param id STEP instance id
 * @param name baseline name
 * @param baselineType baseline variance type
 * @param baselineValues baseline variance values
 * @param baselineUnit baseline variance unit
 * @param baselineDescription baseline variance description
 * @param baselineStatus baseline variance status
 */
/**
 * Resolved BASELINE_DEFINITION.
 * A baseline definition entity.
 *
 * @param id STEP instance id
 * @param name baseline name
 * @param baselineType baseline variance type
 * @param baselineValues baseline variance values
 * @param baselineUnit baseline variance unit
 * @param baselineDescription baseline variance description
 * @param baselineStatus baseline variance status
 */
public final class StepBaselineDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String baselineType;
    private final List<Double> baselineValues;
    private final StepEntity baselineUnit;
    private final String baselineDescription;
    private final String baselineStatus;

    public StepBaselineDefinition(int id, String name, String baselineType, List<Double> baselineValues, StepEntity baselineUnit, String baselineDescription, String baselineStatus) {
        this.id = id;
        this.name = name;
        this.baselineType = baselineType;
        this.baselineValues = baselineValues == null ? null : java.util.List.copyOf(baselineValues);
        this.baselineUnit = baselineUnit;
        this.baselineDescription = baselineDescription;
        this.baselineStatus = baselineStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBaselineType() {
        return baselineType;
    }

    public List<Double> getBaselineValues() {
        return baselineValues;
    }

    public StepEntity getBaselineUnit() {
        return baselineUnit;
    }

    public String getBaselineDescription() {
        return baselineDescription;
    }

    public String getBaselineStatus() {
        return baselineStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepBaselineDefinition that = (StepBaselineDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(baselineType, that.baselineType) && Objects.equals(baselineValues, that.baselineValues) && Objects.equals(baselineUnit, that.baselineUnit) && Objects.equals(baselineDescription, that.baselineDescription) && Objects.equals(baselineStatus, that.baselineStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, baselineType, baselineValues, baselineUnit, baselineDescription, baselineStatus);
    }

    @Override
    public String toString() {
        return "StepBaselineDefinition{" + "id=" + id + "name=" + name + "baselineType=" + baselineType + "baselineValues=" + baselineValues + "baselineUnit=" + baselineUnit + "baselineDescription=" + baselineDescription + "baselineStatus=" + baselineStatus + "}";
    }
}