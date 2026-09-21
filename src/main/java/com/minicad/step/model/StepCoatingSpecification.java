package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepCoatingSpecification extends AbstractStepEntity {
    private final String specificationType;
    private final String specificationDescription;
    private final List<String> specificationRequirements;
    private final String specificationStatus;

    public StepCoatingSpecification(int id, String name, String specificationType, String specificationDescription, List<String> specificationRequirements, String specificationStatus) {
        super(id, name);
        this.specificationType = specificationType;
        this.specificationDescription = specificationDescription;
        this.specificationRequirements = specificationRequirements == null ? null : java.util.List.copyOf(specificationRequirements);
        this.specificationStatus = specificationStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("specificationType", specificationType);
        state.put("specificationDescription", specificationDescription);
        state.put("specificationRequirements", specificationRequirements);
        state.put("specificationStatus", specificationStatus);
        return state;
    }
}
