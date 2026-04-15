package com.minicad.step.model;

import java.util.List;

/**
 * Resolved TRANSACTION_DEFINITION.
 * A transaction definition entity.
 *
 * @param id STEP instance id
 * @param name transaction name
 * @param transactionType transaction variance type
 * @param transactionIsolation transaction variance isolation level
 * @param transactionTimeout transaction variance timeout
 * @param transactionOperations transaction variance operations
 * @param transactionStatus transaction variance status
 */
public record StepTransactionDefinition(
    int id,
    String name,
    String transactionType,
    String transactionIsolation,
    int transactionTimeout,
    List<String> transactionOperations,
    String transactionStatus) implements StepEntity {
}