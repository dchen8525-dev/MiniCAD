package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepDataFlowInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity flowDefinition;
    private final String flowState;
    private final double flowRate;
    private final List<String> flowData;
    private final String flowStatus;

    public StepDataFlowInstance(int id, String name, StepEntity flowDefinition, String flowState, double flowRate, List<String> flowData, String flowStatus) {
        this.id = id;
        this.name = name;
        this.flowDefinition = flowDefinition;
        this.flowState = flowState;
        this.flowRate = flowRate;
        this.flowData = flowData == null ? null : java.util.List.copyOf(flowData);
        this.flowStatus = flowStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDataFlowInstance that = (StepDataFlowInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(flowDefinition, that.flowDefinition) && Objects.equals(flowState, that.flowState) && flowRate == that.flowRate && Objects.equals(flowData, that.flowData) && Objects.equals(flowStatus, that.flowStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, flowDefinition, flowState, flowRate, flowData, flowStatus);
    }

    @Override
    public String toString() {
        return "StepDataFlowInstance{" + "id=" + id + "name=" + name + "flowDefinition=" + flowDefinition + "flowState=" + flowState + "flowRate=" + flowRate + "flowData=" + flowData + "flowStatus=" + flowStatus + "}";
    }
}