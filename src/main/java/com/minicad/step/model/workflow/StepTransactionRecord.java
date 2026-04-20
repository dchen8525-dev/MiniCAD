package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepTransactionRecord(
    int id,
    String name,
    String transactionType,
    String transactionId,
    StepEntity transactionTarget,
    StepEntity transactionStartTime,
    StepEntity transactionEndTime,
    String transactionResult,
    String transactionStatus) implements StepEntity {
}