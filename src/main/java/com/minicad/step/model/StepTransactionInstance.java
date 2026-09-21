package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved TRANSACTION_INSTANCE.
 * A transaction instance entity.
 *
 * @param id STEP instance id
 * @param name transaction instance name
 * @param transactionDefinition transaction variance definition reference
 * @param transactionState transaction variance state
 * @param transactionStartTime transaction variance start time
 * @param transactionEndTime transaction variance end time
 * @param transactionResult transaction variance result
 * @param transactionStatus transaction variance status
 */
public final class StepTransactionInstance extends AbstractStepEntity {
    private final StepEntity transactionDefinition;
    private final String transactionState;
    private final StepEntity transactionStartTime;
    private final StepEntity transactionEndTime;
    private final String transactionResult;
    private final String transactionStatus;

    public StepTransactionInstance(int id, String name, StepEntity transactionDefinition, String transactionState, StepEntity transactionStartTime, StepEntity transactionEndTime, String transactionResult, String transactionStatus) {
        super(id, name);
        this.transactionDefinition = transactionDefinition;
        this.transactionState = transactionState;
        this.transactionStartTime = transactionStartTime;
        this.transactionEndTime = transactionEndTime;
        this.transactionResult = transactionResult;
        this.transactionStatus = transactionStatus;
    }

    public StepEntity getTransactionDefinition() {
        return transactionDefinition;
    }

    public String getTransactionState() {
        return transactionState;
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
        state.put("transactionDefinition", transactionDefinition);
        state.put("transactionState", transactionState);
        state.put("transactionStartTime", transactionStartTime);
        state.put("transactionEndTime", transactionEndTime);
        state.put("transactionResult", transactionResult);
        state.put("transactionStatus", transactionStatus);
        return state;
    }
}
