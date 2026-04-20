package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepTransactionInstance(
    int id,
    String name,
    StepEntity transactionDefinition,
    String transactionState,
    StepEntity transactionStartTime,
    StepEntity transactionEndTime,
    String transactionResult,
    String transactionStatus) implements StepEntity {
}