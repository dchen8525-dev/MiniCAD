package com.minicad.step.model.workflow;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepDataFlowDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String flowType;
    private final String flowDirection;
    private final StepEntity flowSource;
    private final StepEntity flowTarget;
    private final String flowProtocol;
    private final String flowStatus;

    public StepDataFlowDefinition(int id, String name, String flowType, String flowDirection, StepEntity flowSource, StepEntity flowTarget, String flowProtocol, String flowStatus) {
        this.id = id;
        this.name = name;
        this.flowType = flowType;
        this.flowDirection = flowDirection;
        this.flowSource = flowSource;
        this.flowTarget = flowTarget;
        this.flowProtocol = flowProtocol;
        this.flowStatus = flowStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDataFlowDefinition that = (StepDataFlowDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(flowType, that.flowType) && Objects.equals(flowDirection, that.flowDirection) && Objects.equals(flowSource, that.flowSource) && Objects.equals(flowTarget, that.flowTarget) && Objects.equals(flowProtocol, that.flowProtocol) && Objects.equals(flowStatus, that.flowStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, flowType, flowDirection, flowSource, flowTarget, flowProtocol, flowStatus);
    }

    @Override
    public String toString() {
        return "StepDataFlowDefinition{" + "id=" + id + "name=" + name + "flowType=" + flowType + "flowDirection=" + flowDirection + "flowSource=" + flowSource + "flowTarget=" + flowTarget + "flowProtocol=" + flowProtocol + "flowStatus=" + flowStatus + "}";
    }
}