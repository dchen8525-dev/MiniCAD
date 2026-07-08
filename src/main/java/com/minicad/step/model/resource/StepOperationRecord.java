package com.minicad.step.model.resource;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved OPERATION_RECORD.
 * An operation record entity.
 *
 * @param id STEP instance id
 * @param name operation name
 * @param operationType operation variance type
 * @param operationName operation variance operation name
 * @param operationTarget operation variance target reference
 * @param operationActor operation variance actor reference
 * @param operationTimestamp operation variance timestamp
 * @param operationResult operation variance result
 * @param operationStatus operation variance status
 */
/**
 * Resolved OPERATION_RECORD.
 * An operation record entity.
 *
 * @param id STEP instance id
 * @param name operation name
 * @param operationType operation variance type
 * @param operationName operation variance operation name
 * @param operationTarget operation variance target reference
 * @param operationActor operation variance actor reference
 * @param operationTimestamp operation variance timestamp
 * @param operationResult operation variance result
 * @param operationStatus operation variance status
 */
public final class StepOperationRecord implements StepEntity {
    private final int id;
    private final String name;
    private final String operationType;
    private final String operationName;
    private final StepEntity operationTarget;
    private final StepEntity operationActor;
    private final StepEntity operationTimestamp;
    private final String operationResult;
    private final String operationStatus;

    public StepOperationRecord(int id, String name, String operationType, String operationName, StepEntity operationTarget, StepEntity operationActor, StepEntity operationTimestamp, String operationResult, String operationStatus) {
        this.id = id;
        this.name = name;
        this.operationType = operationType;
        this.operationName = operationName;
        this.operationTarget = operationTarget;
        this.operationActor = operationActor;
        this.operationTimestamp = operationTimestamp;
        this.operationResult = operationResult;
        this.operationStatus = operationStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOperationType() {
        return operationType;
    }

    public String getOperationName() {
        return operationName;
    }

    public StepEntity getOperationTarget() {
        return operationTarget;
    }

    public StepEntity getOperationActor() {
        return operationActor;
    }

    public StepEntity getOperationTimestamp() {
        return operationTimestamp;
    }

    public String getOperationResult() {
        return operationResult;
    }

    public String getOperationStatus() {
        return operationStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepOperationRecord that = (StepOperationRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(operationType, that.operationType) && Objects.equals(operationName, that.operationName) && Objects.equals(operationTarget, that.operationTarget) && Objects.equals(operationActor, that.operationActor) && Objects.equals(operationTimestamp, that.operationTimestamp) && Objects.equals(operationResult, that.operationResult) && Objects.equals(operationStatus, that.operationStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, operationType, operationName, operationTarget, operationActor, operationTimestamp, operationResult, operationStatus);
    }

    @Override
    public String toString() {
        return "StepOperationRecord{" + "id=" + id + "name=" + name + "operationType=" + operationType + "operationName=" + operationName + "operationTarget=" + operationTarget + "operationActor=" + operationActor + "operationTimestamp=" + operationTimestamp + "operationResult=" + operationResult + "operationStatus=" + operationStatus + "}";
    }
}