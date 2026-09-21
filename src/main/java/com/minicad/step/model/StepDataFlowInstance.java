package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved DATA_FLOW_INSTANCE.
 * A data flow instance entity.
 *
 * @param id STEP instance id
 * @param name data flow instance name
 * @param flowDefinition flow variance definition reference
 * @param flowState flow variance state
 * @param flowRate flow variance current rate
 * @param flowData flow variance data content
 * @param flowStatus flow variance status
 */
public final class StepDataFlowInstance extends AbstractStepEntity {
    private final StepEntity flowDefinition;
    private final String flowState;
    private final double flowRate;
    private final List<String> flowData;
    private final String flowStatus;

    public StepDataFlowInstance(int id, String name, StepEntity flowDefinition, String flowState, double flowRate, List<String> flowData, String flowStatus) {
        super(id, name);
        this.flowDefinition = flowDefinition;
        this.flowState = flowState;
        this.flowRate = flowRate;
        this.flowData = flowData == null ? null : java.util.List.copyOf(flowData);
        this.flowStatus = flowStatus;
    }

    public StepEntity getFlowDefinition() {
        return flowDefinition;
    }

    public String getFlowState() {
        return flowState;
    }

    public double getFlowRate() {
        return flowRate;
    }

    public List<String> getFlowData() {
        return flowData;
    }

    public String getFlowStatus() {
        return flowStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("flowDefinition", flowDefinition);
        state.put("flowState", flowState);
        state.put("flowRate", flowRate);
        state.put("flowData", flowData);
        state.put("flowStatus", flowStatus);
        return state;
    }
}
