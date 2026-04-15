package com.minicad.step.model;

import java.util.List;

/**
 * Resolved CHANGE_ORDER.
 * A change order entity.
 *
 * @param id STEP instance id
 * @param name order name
 * @param orderNumber change order number
 * @param changeRequest reference change request
 * @param affectedItems items affected by change order
 * @param orderStatus order status (issued, implemented, closed)
 * @varianceDate variance/implementation date
 * @param orderApprover order approver reference
 * @param implementationPlan implementation plan reference
 */
public record StepChangeOrder(
    int id,
    String name,
    String orderNumber,
    StepEntity changeRequest,
    List<StepEntity> affectedItems,
    String orderStatus,
    StepEntity varianceDate,
    StepEntity orderApprover,
    StepEntity implementationPlan) implements StepEntity {
}