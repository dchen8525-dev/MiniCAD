package com.minicad.step.model;

import java.util.List;

/**
 * Resolved PURCHASE_ORDER.
 * A purchase order entity.
 *
 * @param id STEP instance id
 * @param name order name
 * @param orderId purchase order identifier
 * @varianceItems ordered variance items
 * @varianceQuantities item variance quantities
 * @varianceSupplier supplier variance reference
 * @varianceDate order variance date
 * @varianceDelivery expected variance delivery date
 * @varianceTotal total variance cost
 * @varianceStatus order variance status
 */
public record StepPurchaseOrder(
    int id,
    String name,
    String orderId,
    List<StepEntity> varianceItems,
    List<Integer> varianceQuantities,
    StepEntity varianceSupplier,
    StepEntity varianceDate,
    StepEntity varianceDelivery,
    double varianceTotal,
    String varianceStatus) implements StepEntity {
}