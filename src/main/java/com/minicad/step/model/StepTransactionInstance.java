package com.minicad.step.model.workflow;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepTransactionInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity transactionDefinition;
    private final String transactionState;
    private final StepEntity transactionStartTime;
    private final StepEntity transactionEndTime;
    private final String transactionResult;
    private final String transactionStatus;

    public StepTransactionInstance(int id, String name, StepEntity transactionDefinition, String transactionState, StepEntity transactionStartTime, StepEntity transactionEndTime, String transactionResult, String transactionStatus) {
        this.id = id;
        this.name = name;
        this.transactionDefinition = transactionDefinition;
        this.transactionState = transactionState;
        this.transactionStartTime = transactionStartTime;
        this.transactionEndTime = transactionEndTime;
        this.transactionResult = transactionResult;
        this.transactionStatus = transactionStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTransactionInstance that = (StepTransactionInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(transactionDefinition, that.transactionDefinition) && Objects.equals(transactionState, that.transactionState) && Objects.equals(transactionStartTime, that.transactionStartTime) && Objects.equals(transactionEndTime, that.transactionEndTime) && Objects.equals(transactionResult, that.transactionResult) && Objects.equals(transactionStatus, that.transactionStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, transactionDefinition, transactionState, transactionStartTime, transactionEndTime, transactionResult, transactionStatus);
    }

    @Override
    public String toString() {
        return "StepTransactionInstance{" + "id=" + id + "name=" + name + "transactionDefinition=" + transactionDefinition + "transactionState=" + transactionState + "transactionStartTime=" + transactionStartTime + "transactionEndTime=" + transactionEndTime + "transactionResult=" + transactionResult + "transactionStatus=" + transactionStatus + "}";
    }
}