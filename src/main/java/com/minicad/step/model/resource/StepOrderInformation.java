package com.minicad.step.model.resource;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved ORDER_INFORMATION.
 * An order information entity.
 *
 * @param id STEP instance id
 * @param name order name
 * @param orderId order identifier
 * @param orderItems ordered items
 * @param orderQuantity order quantities
 * @param orderDate order date
 * @param deliveryDate expected delivery date
 * @param orderStatus order status (pending, confirmed, shipped)
 * @param orderCustomer customer reference
 */
public record StepOrderInformation(
    int id,
    String name,
    String orderId,
    List<StepEntity> orderItems,
    List<Integer> orderQuantity,
    StepEntity orderDate,
    StepEntity deliveryDate,
    String orderStatus,
    StepEntity orderCustomer) implements StepEntity {
}