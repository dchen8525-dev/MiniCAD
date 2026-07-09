package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepPurchaseOrder implements StepEntity {
    private final int id;
    private final String name;
    private final String orderId;
    private final List<StepEntity> varianceItems;
    private final List<Integer> varianceQuantities;
    private final StepEntity varianceSupplier;
    private final StepEntity varianceDate;
    private final StepEntity varianceDelivery;
    private final double varianceTotal;
    private final String varianceStatus;

    public StepPurchaseOrder(int id, String name, String orderId, List<StepEntity> varianceItems, List<Integer> varianceQuantities, StepEntity varianceSupplier, StepEntity varianceDate, StepEntity varianceDelivery, double varianceTotal, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.orderId = orderId;
        this.varianceItems = varianceItems == null ? null : java.util.List.copyOf(varianceItems);
        this.varianceQuantities = varianceQuantities == null ? null : java.util.List.copyOf(varianceQuantities);
        this.varianceSupplier = varianceSupplier;
        this.varianceDate = varianceDate;
        this.varianceDelivery = varianceDelivery;
        this.varianceTotal = varianceTotal;
        this.varianceStatus = varianceStatus;
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

    public List<StepEntity> getVarianceItems() {
        return varianceItems;
    }

    public List<Integer> getVarianceQuantities() {
        return varianceQuantities;
    }

    public StepEntity getVarianceSupplier() {
        return varianceSupplier;
    }

    public StepEntity getVarianceDate() {
        return varianceDate;
    }

    public StepEntity getVarianceDelivery() {
        return varianceDelivery;
    }

    public double getVarianceTotal() {
        return varianceTotal;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPurchaseOrder that = (StepPurchaseOrder) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(orderId, that.orderId) && Objects.equals(varianceItems, that.varianceItems) && Objects.equals(varianceQuantities, that.varianceQuantities) && Objects.equals(varianceSupplier, that.varianceSupplier) && Objects.equals(varianceDate, that.varianceDate) && Objects.equals(varianceDelivery, that.varianceDelivery) && varianceTotal == that.varianceTotal && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, orderId, varianceItems, varianceQuantities, varianceSupplier, varianceDate, varianceDelivery, varianceTotal, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepPurchaseOrder{" + "id=" + id + "name=" + name + "orderId=" + orderId + "varianceItems=" + varianceItems + "varianceQuantities=" + varianceQuantities + "varianceSupplier=" + varianceSupplier + "varianceDate=" + varianceDate + "varianceDelivery=" + varianceDelivery + "varianceTotal=" + varianceTotal + "varianceStatus=" + varianceStatus + "}";
    }
}