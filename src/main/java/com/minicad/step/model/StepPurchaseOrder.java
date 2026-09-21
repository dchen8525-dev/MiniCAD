package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepPurchaseOrder extends AbstractStepEntity {
    private final String orderId;
    private final List<StepEntity> varianceItems;
    private final List<Integer> varianceQuantities;
    private final StepEntity varianceSupplier;
    private final StepEntity varianceDate;
    private final StepEntity varianceDelivery;
    private final double varianceTotal;
    private final String varianceStatus;

    public StepPurchaseOrder(int id, String name, String orderId, List<StepEntity> varianceItems, List<Integer> varianceQuantities, StepEntity varianceSupplier, StepEntity varianceDate, StepEntity varianceDelivery, double varianceTotal, String varianceStatus) {
        super(id, name);
        this.orderId = orderId;
        this.varianceItems = varianceItems == null ? null : java.util.List.copyOf(varianceItems);
        this.varianceQuantities = varianceQuantities == null ? null : java.util.List.copyOf(varianceQuantities);
        this.varianceSupplier = varianceSupplier;
        this.varianceDate = varianceDate;
        this.varianceDelivery = varianceDelivery;
        this.varianceTotal = varianceTotal;
        this.varianceStatus = varianceStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("orderId", orderId);
        state.put("varianceItems", varianceItems);
        state.put("varianceQuantities", varianceQuantities);
        state.put("varianceSupplier", varianceSupplier);
        state.put("varianceDate", varianceDate);
        state.put("varianceDelivery", varianceDelivery);
        state.put("varianceTotal", varianceTotal);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
