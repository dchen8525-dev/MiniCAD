package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved TRANSACTION_RECORD.
 * A transaction record entity.
 *
 * @param id STEP instance id
 * @param name transaction name
 * @param transactionType transaction variance type
 * @param transactionId transaction variance transaction ID
 * @param transactionTarget transaction variance target reference
 * @param transactionStartTime transaction variance start time
 * @param transactionEndTime transaction variance end time
 * @param transactionResult transaction variance result
 * @param transactionStatus transaction variance status
 */
public final class StepTransactionRecord extends AbstractStepEntity {
    private final String transactionType;
    private final String transactionId;
    private final StepEntity transactionTarget;
    private final StepEntity transactionStartTime;
    private final StepEntity transactionEndTime;
    private final String transactionResult;
    private final String transactionStatus;

    public StepTransactionRecord(int id, String name, String transactionType, String transactionId, StepEntity transactionTarget, StepEntity transactionStartTime, StepEntity transactionEndTime, String transactionResult, String transactionStatus) {
        super(id, name);
        this.transactionType = transactionType;
        this.transactionId = transactionId;
        this.transactionTarget = transactionTarget;
        this.transactionStartTime = transactionStartTime;
        this.transactionEndTime = transactionEndTime;
        this.transactionResult = transactionResult;
        this.transactionStatus = transactionStatus;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public StepEntity getTransactionTarget() {
        return transactionTarget;
    }

    public StepEntity getTransactionStartTime() {
        return transactionStartTime;
    }

    public StepEntity getTransactionEndTime() {
        return transactionEndTime;
    }

    public String getTransactionResult() {
        return transactionResult;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("transactionType", transactionType);
        state.put("transactionId", transactionId);
        state.put("transactionTarget", transactionTarget);
        state.put("transactionStartTime", transactionStartTime);
        state.put("transactionEndTime", transactionEndTime);
        state.put("transactionResult", transactionResult);
        state.put("transactionStatus", transactionStatus);
        return state;
    }
}
