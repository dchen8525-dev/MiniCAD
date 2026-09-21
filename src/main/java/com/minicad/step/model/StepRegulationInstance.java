package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved REGULATION_INSTANCE.
 * A regulation instance entity.
 *
 * @param id STEP instance id
 * @param name regulation instance name
 * @param regulationDefinition regulation variance definition reference
 * @param regulationCompliance regulation variance compliance status
 * @param regulationViolations regulation variance violations
 * @param regulationStatus regulation variance status
 */
public final class StepRegulationInstance extends AbstractStepEntity {
    private final StepEntity regulationDefinition;
    private final String regulationCompliance;
    private final int regulationViolations;
    private final String regulationStatus;

    public StepRegulationInstance(int id, String name, StepEntity regulationDefinition, String regulationCompliance, int regulationViolations, String regulationStatus) {
        super(id, name);
        this.regulationDefinition = regulationDefinition;
        this.regulationCompliance = regulationCompliance;
        this.regulationViolations = regulationViolations;
        this.regulationStatus = regulationStatus;
    }

    public StepEntity getRegulationDefinition() {
        return regulationDefinition;
    }

    public String getRegulationCompliance() {
        return regulationCompliance;
    }

    public int getRegulationViolations() {
        return regulationViolations;
    }

    public String getRegulationStatus() {
        return regulationStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("regulationDefinition", regulationDefinition);
        state.put("regulationCompliance", regulationCompliance);
        state.put("regulationViolations", regulationViolations);
        state.put("regulationStatus", regulationStatus);
        return state;
    }
}
