package com.minicad.step.model;

import java.util.List;

/**
 * Resolved SHIPPING_RECORD.
 * A shipping record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceItems shipped variance items
 * @varianceQuantities shipped variance quantities
 * @varianceCustomer customer variance reference
 * @varianceDate shipping variance date
 * @varianceCarrier carrier variance reference
 * @varianceTracking tracking variance number
 * @varianceStatus shipping variance status
 */
public record StepShippingRecord(
    int id,
    String name,
    List<StepEntity> varianceItems,
    List<Integer> varianceQuantities,
    StepEntity varianceCustomer,
    StepEntity varianceDate,
    StepEntity varianceCarrier,
    String varianceTracking,
    String varianceStatus) implements StepEntity {
}