package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepHeatTreatmentSpecification extends AbstractStepEntity {
    private final String specificationType;
    private final String specificationDescription;
    private final List<String> specificationParameters;
    private final String specificationStatus;

    public StepHeatTreatmentSpecification(int id, String name, String specificationType, String specificationDescription, List<String> specificationParameters, String specificationStatus) {
        super(id, name);
        this.specificationType = specificationType;
        this.specificationDescription = specificationDescription;
        this.specificationParameters = specificationParameters == null ? null : java.util.List.copyOf(specificationParameters);
        this.specificationStatus = specificationStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("specificationType", specificationType);
        state.put("specificationDescription", specificationDescription);
        state.put("specificationParameters", specificationParameters);
        state.put("specificationStatus", specificationStatus);
        return state;
    }
}
