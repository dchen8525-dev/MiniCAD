package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved DATA_FLOW_DEFINITION.
 * A data flow definition entity.
 *
 * @param id STEP instance id
 * @param name data flow name
 * @param flowType flow variance type
 * @param flowDirection flow variance direction
 * @param flowSource flow variance source reference
 * @param flowTarget flow variance target reference
 * @param flowProtocol flow variance protocol
 * @param flowStatus flow variance status
 */
public final class StepDataFlowDefinition extends AbstractStepEntity {
    private final String flowType;
    private final String flowDirection;
    private final StepEntity flowSource;
    private final StepEntity flowTarget;
    private final String flowProtocol;
    private final String flowStatus;

    public StepDataFlowDefinition(int id, String name, String flowType, String flowDirection, StepEntity flowSource, StepEntity flowTarget, String flowProtocol, String flowStatus) {
        super(id, name);
        this.flowType = flowType;
        this.flowDirection = flowDirection;
        this.flowSource = flowSource;
        this.flowTarget = flowTarget;
        this.flowProtocol = flowProtocol;
        this.flowStatus = flowStatus;
    }

    public String getFlowType() {
        return flowType;
    }

    public String getFlowDirection() {
        return flowDirection;
    }

    public StepEntity getFlowSource() {
        return flowSource;
    }

    public StepEntity getFlowTarget() {
        return flowTarget;
    }

    public String getFlowProtocol() {
        return flowProtocol;
    }

    public String getFlowStatus() {
        return flowStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("flowType", flowType);
        state.put("flowDirection", flowDirection);
        state.put("flowSource", flowSource);
        state.put("flowTarget", flowTarget);
        state.put("flowProtocol", flowProtocol);
        state.put("flowStatus", flowStatus);
        return state;
    }
}
