package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved COATING_SPECIFICATION.
 * A coating specification entity.
 *
 * @param id STEP instance id
 * @param name specification name
 * @param specificationType specification variance type
 * @param specificationDescription specification variance description
 * @param specificationRequirements specification variance requirements
 * @param specificationStatus specification variance status
 */
/**
 * Resolved COATING_SPECIFICATION.
 * A coating specification entity.
 *
 * @param id STEP instance id
 * @param name specification name
 * @param specificationType specification variance type
 * @param specificationDescription specification variance description
 * @param specificationRequirements specification variance requirements
 * @param specificationStatus specification variance status
 */
public final class StepCoatingSpecification implements StepEntity {
    private final int id;
    private final String name;
    private final String specificationType;
    private final String specificationDescription;
    private final List<String> specificationRequirements;
    private final String specificationStatus;

    public StepCoatingSpecification(int id, String name, String specificationType, String specificationDescription, List<String> specificationRequirements, String specificationStatus) {
        this.id = id;
        this.name = name;
        this.specificationType = specificationType;
        this.specificationDescription = specificationDescription;
        this.specificationRequirements = specificationRequirements == null ? null : java.util.List.copyOf(specificationRequirements);
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

    public List<String> getSpecificationRequirements() {
        return specificationRequirements;
    }

    public String getSpecificationStatus() {
        return specificationStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCoatingSpecification that = (StepCoatingSpecification) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(specificationType, that.specificationType) && Objects.equals(specificationDescription, that.specificationDescription) && Objects.equals(specificationRequirements, that.specificationRequirements) && Objects.equals(specificationStatus, that.specificationStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, specificationType, specificationDescription, specificationRequirements, specificationStatus);
    }

    @Override
    public String toString() {
        return "StepCoatingSpecification{" + "id=" + id + "name=" + name + "specificationType=" + specificationType + "specificationDescription=" + specificationDescription + "specificationRequirements=" + specificationRequirements + "specificationStatus=" + specificationStatus + "}";
    }
}