package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved MEASURE_QUALIFICATION.
 * Qualification of a measure value.
 *
 * @param id STEP instance id
 * @param name qualification name
 * @param qualifiedMeasure qualified measure reference
 * @param qualifiers list of qualifiers
 */
/**
 * Resolved MEASURE_QUALIFICATION.
 * Qualification of a measure value.
 *
 * @param id STEP instance id
 * @param name qualification name
 * @param qualifiedMeasure qualified measure reference
 * @param qualifiers list of qualifiers
 */
public final class StepMeasureQualification implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity qualifiedMeasure;
    private final List<StepEntity> qualifiers;

    public StepMeasureQualification(int id, String name, StepEntity qualifiedMeasure, List<StepEntity> qualifiers) {
        this.id = id;
        this.name = name;
        this.qualifiedMeasure = qualifiedMeasure;
        this.qualifiers = qualifiers == null ? null : java.util.List.copyOf(qualifiers);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getQualifiedMeasure() {
        return qualifiedMeasure;
    }

    public List<StepEntity> getQualifiers() {
        return qualifiers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepMeasureQualification that = (StepMeasureQualification) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(qualifiedMeasure, that.qualifiedMeasure) && Objects.equals(qualifiers, that.qualifiers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, qualifiedMeasure, qualifiers);
    }

    @Override
    public String toString() {
        return "StepMeasureQualification{" + "id=" + id + "name=" + name + "qualifiedMeasure=" + qualifiedMeasure + "qualifiers=" + qualifiers + "}";
    }
}