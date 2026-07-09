package com.minicad.step.model.workflow;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepTransactionRecord implements StepEntity {
    private final int id;
    private final String name;
    private final String transactionType;
    private final String transactionId;
    private final StepEntity transactionTarget;
    private final StepEntity transactionStartTime;
    private final StepEntity transactionEndTime;
    private final String transactionResult;
    private final String transactionStatus;

    public StepTransactionRecord(int id, String name, String transactionType, String transactionId, StepEntity transactionTarget, StepEntity transactionStartTime, StepEntity transactionEndTime, String transactionResult, String transactionStatus) {
        this.id = id;
        this.name = name;
        this.transactionType = transactionType;
        this.transactionId = transactionId;
        this.transactionTarget = transactionTarget;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTransactionRecord that = (StepTransactionRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(transactionType, that.transactionType) && Objects.equals(transactionId, that.transactionId) && Objects.equals(transactionTarget, that.transactionTarget) && Objects.equals(transactionStartTime, that.transactionStartTime) && Objects.equals(transactionEndTime, that.transactionEndTime) && Objects.equals(transactionResult, that.transactionResult) && Objects.equals(transactionStatus, that.transactionStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, transactionType, transactionId, transactionTarget, transactionStartTime, transactionEndTime, transactionResult, transactionStatus);
    }

    @Override
    public String toString() {
        return "StepTransactionRecord{" + "id=" + id + "name=" + name + "transactionType=" + transactionType + "transactionId=" + transactionId + "transactionTarget=" + transactionTarget + "transactionStartTime=" + transactionStartTime + "transactionEndTime=" + transactionEndTime + "transactionResult=" + transactionResult + "transactionStatus=" + transactionStatus + "}";
    }
}