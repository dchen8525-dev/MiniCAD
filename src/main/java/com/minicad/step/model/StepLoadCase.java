package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved LOAD_CASE.
 * A load case entity.
 *
 * @param id STEP instance id
 * @param name load case name
 * @param caseType case variance type
 * @param caseLoads case variance load definitions
 * @param caseDescription case variance description
 * @param caseStatus case variance status
 */
public final class StepLoadCase extends AbstractStepEntity {
    private final String caseType;
    private final List<StepEntity> caseLoads;
    private final String caseDescription;
    private final String caseStatus;

    public StepLoadCase(int id, String name, String caseType, List<StepEntity> caseLoads, String caseDescription, String caseStatus) {
        super(id, name);
        this.caseType = caseType;
        this.caseLoads = caseLoads == null ? null : java.util.List.copyOf(caseLoads);
        this.caseDescription = caseDescription;
        this.caseStatus = caseStatus;
    }

    public String getCaseType() {
        return caseType;
    }

    public List<StepEntity> getCaseLoads() {
        return caseLoads;
    }

    public String getCaseDescription() {
        return caseDescription;
    }

    public String getCaseStatus() {
        return caseStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("caseType", caseType);
        state.put("caseLoads", caseLoads);
        state.put("caseDescription", caseDescription);
        state.put("caseStatus", caseStatus);
        return state;
    }
}
