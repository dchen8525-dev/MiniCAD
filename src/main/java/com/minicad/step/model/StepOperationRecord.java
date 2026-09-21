package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepOperationRecord extends AbstractStepEntity {
    private final String operationType;
    private final String operationName;
    private final StepEntity operationTarget;
    private final StepEntity operationActor;
    private final StepEntity operationTimestamp;
    private final String operationResult;
    private final String operationStatus;

    public StepOperationRecord(int id, String name, String operationType, String operationName, StepEntity operationTarget, StepEntity operationActor, StepEntity operationTimestamp, String operationResult, String operationStatus) {
        super(id, name);
        this.operationType = operationType;
        this.operationName = operationName;
        this.operationTarget = operationTarget;
        this.operationActor = operationActor;
        this.operationTimestamp = operationTimestamp;
        this.operationResult = operationResult;
        this.operationStatus = operationStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("operationType", operationType);
        state.put("operationName", operationName);
        state.put("operationTarget", operationTarget);
        state.put("operationActor", operationActor);
        state.put("operationTimestamp", operationTimestamp);
        state.put("operationResult", operationResult);
        state.put("operationStatus", operationStatus);
        return state;
    }
}
