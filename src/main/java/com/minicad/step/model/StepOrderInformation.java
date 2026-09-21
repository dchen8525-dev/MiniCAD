package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepOrderInformation extends AbstractStepEntity {
    private final String orderId;
    private final List<StepEntity> orderItems;
    private final List<Integer> orderQuantity;
    private final StepEntity orderDate;
    private final StepEntity deliveryDate;
    private final String orderStatus;
    private final StepEntity orderCustomer;

    public StepOrderInformation(int id, String name, String orderId, List<StepEntity> orderItems, List<Integer> orderQuantity, StepEntity orderDate, StepEntity deliveryDate, String orderStatus, StepEntity orderCustomer) {
        super(id, name);
        this.orderId = orderId;
        this.orderItems = orderItems == null ? null : java.util.List.copyOf(orderItems);
        this.orderQuantity = orderQuantity == null ? null : java.util.List.copyOf(orderQuantity);
        this.orderDate = orderDate;
        this.deliveryDate = deliveryDate;
        this.orderStatus = orderStatus;
        this.orderCustomer = orderCustomer;
    }

    public String getOrderId() {
        return orderId;
    }

    public List<StepEntity> getOrderItems() {
        return orderItems;
    }

    public List<Integer> getOrderQuantity() {
        return orderQuantity;
    }

    public StepEntity getOrderDate() {
        return orderDate;
    }

    public StepEntity getDeliveryDate() {
        return deliveryDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public StepEntity getOrderCustomer() {
        return orderCustomer;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("orderId", orderId);
        state.put("orderItems", orderItems);
        state.put("orderQuantity", orderQuantity);
        state.put("orderDate", orderDate);
        state.put("deliveryDate", deliveryDate);
        state.put("orderStatus", orderStatus);
        state.put("orderCustomer", orderCustomer);
        return state;
    }
}
