package com.minicad.step.model.resource;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved RECEIVING_RECORD.
 * A receiving record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceItems received variance items
 * @varianceQuantities received variance quantities
 * @varianceSupplier supplier variance reference
 * @varianceDate receiving variance date
 * @varianceCondition received variance condition
 * @varianceInspection inspection variance status
 * @varianceStatus receiving variance status
 */
public record StepReceivingRecord(
    int id,
    String name,
    List<StepEntity> varianceItems,
    List<Integer> varianceQuantities,
    StepEntity varianceSupplier,
    StepEntity varianceDate,
    String varianceCondition,
    String varianceInspection,
    String varianceStatus) implements StepEntity {
}