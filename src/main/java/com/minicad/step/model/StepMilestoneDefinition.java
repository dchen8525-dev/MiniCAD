package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved MILESTONE_DEFINITION.
 * A milestone definition entity.
 *
 * @param id STEP instance id
 * @param name milestone name
 * @param milestoneType milestone variance type
 * @param milestoneDescription milestone variance description
 * @param milestoneTarget milestone variance target date
 * @param milestoneStatus milestone variance status
 */
public final class StepMilestoneDefinition extends AbstractStepEntity {
    private final String milestoneType;
    private final String milestoneDescription;
    private final StepEntity milestoneTarget;
    private final String milestoneStatus;

    public StepMilestoneDefinition(int id, String name, String milestoneType, String milestoneDescription, StepEntity milestoneTarget, String milestoneStatus) {
        super(id, name);
        this.milestoneType = milestoneType;
        this.milestoneDescription = milestoneDescription;
        this.milestoneTarget = milestoneTarget;
        this.milestoneStatus = milestoneStatus;
    }

    public String getMilestoneType() {
        return milestoneType;
    }

    public String getMilestoneDescription() {
        return milestoneDescription;
    }

    public StepEntity getMilestoneTarget() {
        return milestoneTarget;
    }

    public String getMilestoneStatus() {
        return milestoneStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("milestoneType", milestoneType);
        state.put("milestoneDescription", milestoneDescription);
        state.put("milestoneTarget", milestoneTarget);
        state.put("milestoneStatus", milestoneStatus);
        return state;
    }
}
