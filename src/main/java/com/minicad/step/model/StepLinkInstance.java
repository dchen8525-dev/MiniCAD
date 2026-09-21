package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved LINK_INSTANCE.
 * A link instance entity.
 *
 * @param id STEP instance id
 * @param name link instance name
 * @param linkDefinition link variance definition reference
 * @param linkState link variance state
 * @param linkUtilization link variance utilization
 * @param linkStatus link variance status
 */
public final class StepLinkInstance extends AbstractStepEntity {
    private final StepEntity linkDefinition;
    private final String linkState;
    private final double linkUtilization;
    private final String linkStatus;

    public StepLinkInstance(int id, String name, StepEntity linkDefinition, String linkState, double linkUtilization, String linkStatus) {
        super(id, name);
        this.linkDefinition = linkDefinition;
        this.linkState = linkState;
        this.linkUtilization = linkUtilization;
        this.linkStatus = linkStatus;
    }

    public StepEntity getLinkDefinition() {
        return linkDefinition;
    }

    public String getLinkState() {
        return linkState;
    }

    public double getLinkUtilization() {
        return linkUtilization;
    }

    public String getLinkStatus() {
        return linkStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("linkDefinition", linkDefinition);
        state.put("linkState", linkState);
        state.put("linkUtilization", linkUtilization);
        state.put("linkStatus", linkStatus);
        return state;
    }
}
