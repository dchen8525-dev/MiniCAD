package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved REQUIREMENT_SPECIFICATION.
 * A requirement specification entity.
 *
 * @param id STEP instance id
 * @param name specification name
 * @param specificationId specification identifier
 * @param requirements list of requirements
 * @param specificationType specification type (functional, performance, safety)
 * @param specificationStatus specification status
 * @param specificationVersion specification version reference
 */
/**
 * Resolved REQUIREMENT_SPECIFICATION.
 * A requirement specification entity.
 *
 * @param id STEP instance id
 * @param name specification name
 * @param specificationId specification identifier
 * @param requirements list of requirements
 * @param specificationType specification type (functional, performance, safety)
 * @param specificationStatus specification status
 * @param specificationVersion specification version reference
 */
public final class StepRequirementSpecification implements StepEntity {
    private final int id;
    private final String name;
    private final String specificationId;
    private final List<StepEntity> requirements;
    private final String specificationType;
    private final String specificationStatus;
    private final StepEntity specificationVersion;

    public StepRequirementSpecification(int id, String name, String specificationId, List<StepEntity> requirements, String specificationType, String specificationStatus, StepEntity specificationVersion) {
        this.id = id;
        this.name = name;
        this.specificationId = specificationId;
        this.requirements = requirements == null ? null : java.util.List.copyOf(requirements);
        this.specificationType = specificationType;
        this.specificationStatus = specificationStatus;
        this.specificationVersion = specificationVersion;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSpecificationId() {
        return specificationId;
    }

    public List<StepEntity> getRequirements() {
        return requirements;
    }

    public String getSpecificationType() {
        return specificationType;
    }

    public String getSpecificationStatus() {
        return specificationStatus;
    }

    public StepEntity getSpecificationVersion() {
        return specificationVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepRequirementSpecification that = (StepRequirementSpecification) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(specificationId, that.specificationId) && Objects.equals(requirements, that.requirements) && Objects.equals(specificationType, that.specificationType) && Objects.equals(specificationStatus, that.specificationStatus) && Objects.equals(specificationVersion, that.specificationVersion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, specificationId, requirements, specificationType, specificationStatus, specificationVersion);
    }

    @Override
    public String toString() {
        return "StepRequirementSpecification{" + "id=" + id + "name=" + name + "specificationId=" + specificationId + "requirements=" + requirements + "specificationType=" + specificationType + "specificationStatus=" + specificationStatus + "specificationVersion=" + specificationVersion + "}";
    }
}