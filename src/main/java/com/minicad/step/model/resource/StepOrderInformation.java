package com.minicad.step.model.resource;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepOrderInformation implements StepEntity {
    private final int id;
    private final String name;
    private final String orderId;
    private final List<StepEntity> orderItems;
    private final List<Integer> orderQuantity;
    private final StepEntity orderDate;
    private final StepEntity deliveryDate;
    private final String orderStatus;
    private final StepEntity orderCustomer;

    public StepOrderInformation(int id, String name, String orderId, List<StepEntity> orderItems, List<Integer> orderQuantity, StepEntity orderDate, StepEntity deliveryDate, String orderStatus, StepEntity orderCustomer) {
        this.id = id;
        this.name = name;
        this.orderId = orderId;
        this.orderItems = orderItems == null ? null : java.util.List.copyOf(orderItems);
        this.orderQuantity = orderQuantity == null ? null : java.util.List.copyOf(orderQuantity);
        this.orderDate = orderDate;
        this.deliveryDate = deliveryDate;
        this.orderStatus = orderStatus;
        this.orderCustomer = orderCustomer;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepOrderInformation that = (StepOrderInformation) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(orderId, that.orderId) && Objects.equals(orderItems, that.orderItems) && Objects.equals(orderQuantity, that.orderQuantity) && Objects.equals(orderDate, that.orderDate) && Objects.equals(deliveryDate, that.deliveryDate) && Objects.equals(orderStatus, that.orderStatus) && Objects.equals(orderCustomer, that.orderCustomer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, orderId, orderItems, orderQuantity, orderDate, deliveryDate, orderStatus, orderCustomer);
    }

    @Override
    public String toString() {
        return "StepOrderInformation{" + "id=" + id + "name=" + name + "orderId=" + orderId + "orderItems=" + orderItems + "orderQuantity=" + orderQuantity + "orderDate=" + orderDate + "deliveryDate=" + deliveryDate + "orderStatus=" + orderStatus + "orderCustomer=" + orderCustomer + "}";
    }
}