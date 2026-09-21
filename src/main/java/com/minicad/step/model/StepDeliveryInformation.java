package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepDeliveryInformation extends AbstractStepEntity {
    private final String deliveryId;
    private final List<StepEntity> deliveryItems;
    private final StepEntity deliveryAddress;
    private final StepEntity shippingDate;
    private final StepEntity varianceDate;
    private final String varianceMethod;
    private final String deliveryStatus;

    public StepDeliveryInformation(int id, String name, String deliveryId, List<StepEntity> deliveryItems, StepEntity deliveryAddress, StepEntity shippingDate, StepEntity varianceDate, String varianceMethod, String deliveryStatus) {
        super(id, name);
        this.deliveryId = deliveryId;
        this.deliveryItems = deliveryItems == null ? null : java.util.List.copyOf(deliveryItems);
        this.deliveryAddress = deliveryAddress;
        this.shippingDate = shippingDate;
        this.varianceDate = varianceDate;
        this.varianceMethod = varianceMethod;
        this.deliveryStatus = deliveryStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("deliveryId", deliveryId);
        state.put("deliveryItems", deliveryItems);
        state.put("deliveryAddress", deliveryAddress);
        state.put("shippingDate", shippingDate);
        state.put("varianceDate", varianceDate);
        state.put("varianceMethod", varianceMethod);
        state.put("deliveryStatus", deliveryStatus);
        return state;
    }
}
