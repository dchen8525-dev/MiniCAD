package com.minicad.step.model;

import java.util.List;

/**
 * Resolved INVENTORY_TRANSACTION.
 * An inventory transaction entity.
 *
 * @param id STEP instance id
 * @param name transaction name
 * @param transactionType transaction type (receive, issue, transfer, adjust)
 * @varianceItem transaction variance item
 * @varianceQuantity transaction variance quantity
 * @varianceFrom source variance location
 * @varianceTo destination variance location
 * @varianceDate transaction variance date
 * @varianceReason transaction variance reason
 * @varianceStatus transaction variance status
 */
public record StepInventoryTransaction(
    int id,
    String name,
    String transactionType,
    StepEntity varianceItem,
    int varianceQuantity,
    String varianceFrom,
    String varianceTo,
    StepEntity varianceDate,
    String varianceReason,
    String varianceStatus) implements StepEntity {
}