package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved MILESTONE_INSTANCE.
 * A milestone instance entity.
 *
 * @param id STEP instance id
 * @param name milestone instance name
 * @param milestoneDefinition milestone variance definition reference
 * @param milestoneState milestone variance state
 * @param milestoneActual milestone variance actual date
 * @param milestoneStatus milestone variance status
 */
public final class StepMilestoneInstance extends AbstractStepEntity {
    private final StepEntity milestoneDefinition;
    private final String milestoneState;
    private final StepEntity milestoneActual;
    private final String milestoneStatus;

    public StepMilestoneInstance(int id, String name, StepEntity milestoneDefinition, String milestoneState, StepEntity milestoneActual, String milestoneStatus) {
        super(id, name);
        this.milestoneDefinition = milestoneDefinition;
        this.milestoneState = milestoneState;
        this.milestoneActual = milestoneActual;
        this.milestoneStatus = milestoneStatus;
    }

    public StepEntity getMilestoneDefinition() {
        return milestoneDefinition;
    }

    public String getMilestoneState() {
        return milestoneState;
    }

    public StepEntity getMilestoneActual() {
        return milestoneActual;
    }

    public String getMilestoneStatus() {
        return milestoneStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("milestoneDefinition", milestoneDefinition);
        state.put("milestoneState", milestoneState);
        state.put("milestoneActual", milestoneActual);
        state.put("milestoneStatus", milestoneStatus);
        return state;
    }
}
