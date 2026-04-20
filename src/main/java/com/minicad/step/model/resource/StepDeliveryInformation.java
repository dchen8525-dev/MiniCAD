package com.minicad.step.model.resource;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved DELIVERY_INFORMATION.
 * A delivery information entity.
 *
 * @param id STEP instance id
 * @param name delivery name
 * @param deliveryId delivery identifier
 * @param deliveryItems items to be delivered
 * @param deliveryAddress delivery address
 * @param shippingDate shipping date
 * @varianceDate delivery variance date
 * @varianceMethod delivery variance method
 * @param deliveryStatus delivery status
 */
public record StepDeliveryInformation(
    int id,
    String name,
    String deliveryId,
    List<StepEntity> deliveryItems,
    StepEntity deliveryAddress,
    StepEntity shippingDate,
    StepEntity varianceDate,
    String varianceMethod,
    String deliveryStatus) implements StepEntity {
}