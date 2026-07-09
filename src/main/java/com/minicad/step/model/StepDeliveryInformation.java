package com.minicad.step.model.resource;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepDeliveryInformation implements StepEntity {
    private final int id;
    private final String name;
    private final String deliveryId;
    private final List<StepEntity> deliveryItems;
    private final StepEntity deliveryAddress;
    private final StepEntity shippingDate;
    private final StepEntity varianceDate;
    private final String varianceMethod;
    private final String deliveryStatus;

    public StepDeliveryInformation(int id, String name, String deliveryId, List<StepEntity> deliveryItems, StepEntity deliveryAddress, StepEntity shippingDate, StepEntity varianceDate, String varianceMethod, String deliveryStatus) {
        this.id = id;
        this.name = name;
        this.deliveryId = deliveryId;
        this.deliveryItems = deliveryItems == null ? null : java.util.List.copyOf(deliveryItems);
        this.deliveryAddress = deliveryAddress;
        this.shippingDate = shippingDate;
        this.varianceDate = varianceDate;
        this.varianceMethod = varianceMethod;
        this.deliveryStatus = deliveryStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDeliveryId() {
        return deliveryId;
    }

    public List<StepEntity> getDeliveryItems() {
        return deliveryItems;
    }

    public StepEntity getDeliveryAddress() {
        return deliveryAddress;
    }

    public StepEntity getShippingDate() {
        return shippingDate;
    }

    public StepEntity getVarianceDate() {
        return varianceDate;
    }

    public String getVarianceMethod() {
        return varianceMethod;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDeliveryInformation that = (StepDeliveryInformation) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(deliveryId, that.deliveryId) && Objects.equals(deliveryItems, that.deliveryItems) && Objects.equals(deliveryAddress, that.deliveryAddress) && Objects.equals(shippingDate, that.shippingDate) && Objects.equals(varianceDate, that.varianceDate) && Objects.equals(varianceMethod, that.varianceMethod) && Objects.equals(deliveryStatus, that.deliveryStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, deliveryId, deliveryItems, deliveryAddress, shippingDate, varianceDate, varianceMethod, deliveryStatus);
    }

    @Override
    public String toString() {
        return "StepDeliveryInformation{" + "id=" + id + "name=" + name + "deliveryId=" + deliveryId + "deliveryItems=" + deliveryItems + "deliveryAddress=" + deliveryAddress + "shippingDate=" + shippingDate + "varianceDate=" + varianceDate + "varianceMethod=" + varianceMethod + "deliveryStatus=" + deliveryStatus + "}";
    }
}