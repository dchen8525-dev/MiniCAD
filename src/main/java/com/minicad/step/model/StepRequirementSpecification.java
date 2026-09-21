package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepRequirementSpecification extends AbstractStepEntity {
    private final String specificationId;
    private final List<StepEntity> requirements;
    private final String specificationType;
    private final String specificationStatus;
    private final StepEntity specificationVersion;

    public StepRequirementSpecification(int id, String name, String specificationId, List<StepEntity> requirements, String specificationType, String specificationStatus, StepEntity specificationVersion) {
        super(id, name);
        this.specificationId = specificationId;
        this.requirements = requirements == null ? null : java.util.List.copyOf(requirements);
        this.specificationType = specificationType;
        this.specificationStatus = specificationStatus;
        this.specificationVersion = specificationVersion;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("specificationId", specificationId);
        state.put("requirements", requirements);
        state.put("specificationType", specificationType);
        state.put("specificationStatus", specificationStatus);
        state.put("specificationVersion", specificationVersion);
        return state;
    }
}
