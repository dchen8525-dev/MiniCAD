package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved HEAT_TREATMENT_SPECIFICATION.
 * A heat treatment specification entity.
 *
 * @param id STEP instance id
 * @param name specification name
 * @param specificationType specification variance type
 * @param specificationDescription specification variance description
 * @param specificationParameters specification variance parameters
 * @param specificationStatus specification variance status
 */
/**
 * Resolved HEAT_TREATMENT_SPECIFICATION.
 * A heat treatment specification entity.
 *
 * @param id STEP instance id
 * @param name specification name
 * @param specificationType specification variance type
 * @param specificationDescription specification variance description
 * @param specificationParameters specification variance parameters
 * @param specificationStatus specification variance status
 */
public final class StepHeatTreatmentSpecification implements StepEntity {
    private final int id;
    private final String name;
    private final String specificationType;
    private final String specificationDescription;
    private final List<String> specificationParameters;
    private final String specificationStatus;

    public StepHeatTreatmentSpecification(int id, String name, String specificationType, String specificationDescription, List<String> specificationParameters, String specificationStatus) {
        this.id = id;
        this.name = name;
        this.specificationType = specificationType;
        this.specificationDescription = specificationDescription;
        this.specificationParameters = specificationParameters == null ? null : java.util.List.copyOf(specificationParameters);
        this.specificationStatus = specificationStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSpecificationType() {
        return specificationType;
    }

    public String getSpecificationDescription() {
        return specificationDescription;
    }

    public List<String> getSpecificationParameters() {
        return specificationParameters;
    }

    public String getSpecificationStatus() {
        return specificationStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepHeatTreatmentSpecification that = (StepHeatTreatmentSpecification) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(specificationType, that.specificationType) && Objects.equals(specificationDescription, that.specificationDescription) && Objects.equals(specificationParameters, that.specificationParameters) && Objects.equals(specificationStatus, that.specificationStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, specificationType, specificationDescription, specificationParameters, specificationStatus);
    }

    @Override
    public String toString() {
        return "StepHeatTreatmentSpecification{" + "id=" + id + "name=" + name + "specificationType=" + specificationType + "specificationDescription=" + specificationDescription + "specificationParameters=" + specificationParameters + "specificationStatus=" + specificationStatus + "}";
    }
}