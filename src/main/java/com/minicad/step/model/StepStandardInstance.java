package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved STANDARD_INSTANCE.
 * A standard instance entity.
 *
 * @param id STEP instance id
 * @param name standard instance name
 * @param standardDefinition standard variance definition reference
 * @param standardCompliance standard variance compliance level
 * @param standardCertifications standard variance certifications
 * @param standardStatus standard variance status
 */
public final class StepStandardInstance extends AbstractStepEntity {
    private final StepEntity standardDefinition;
    private final String standardCompliance;
    private final List<StepEntity> standardCertifications;
    private final String standardStatus;

    public StepStandardInstance(int id, String name, StepEntity standardDefinition, String standardCompliance, List<StepEntity> standardCertifications, String standardStatus) {
        super(id, name);
        this.standardDefinition = standardDefinition;
        this.standardCompliance = standardCompliance;
        this.standardCertifications = standardCertifications == null ? null : java.util.List.copyOf(standardCertifications);
        this.standardStatus = standardStatus;
    }

    public StepEntity getStandardDefinition() {
        return standardDefinition;
    }

    public String getStandardCompliance() {
        return standardCompliance;
    }

    public List<StepEntity> getStandardCertifications() {
        return standardCertifications;
    }

    public String getStandardStatus() {
        return standardStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("standardDefinition", standardDefinition);
        state.put("standardCompliance", standardCompliance);
        state.put("standardCertifications", standardCertifications);
        state.put("standardStatus", standardStatus);
        return state;
    }
}
