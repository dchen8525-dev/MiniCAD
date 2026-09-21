package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepShippingRecord extends AbstractStepEntity {
    private final List<StepEntity> varianceItems;
    private final List<Integer> varianceQuantities;
    private final StepEntity varianceCustomer;
    private final StepEntity varianceDate;
    private final StepEntity varianceCarrier;
    private final String varianceTracking;
    private final String varianceStatus;

    public StepShippingRecord(int id, String name, List<StepEntity> varianceItems, List<Integer> varianceQuantities, StepEntity varianceCustomer, StepEntity varianceDate, StepEntity varianceCarrier, String varianceTracking, String varianceStatus) {
        super(id, name);
        this.varianceItems = varianceItems == null ? null : java.util.List.copyOf(varianceItems);
        this.varianceQuantities = varianceQuantities == null ? null : java.util.List.copyOf(varianceQuantities);
        this.varianceCustomer = varianceCustomer;
        this.varianceDate = varianceDate;
        this.varianceCarrier = varianceCarrier;
        this.varianceTracking = varianceTracking;
        this.varianceStatus = varianceStatus;
    }

    public List<StepEntity> getVarianceItems() {
        return varianceItems;
    }

    public List<Integer> getVarianceQuantities() {
        return varianceQuantities;
    }

    public StepEntity getVarianceCustomer() {
        return varianceCustomer;
    }

    public StepEntity getVarianceDate() {
        return varianceDate;
    }

    public StepEntity getVarianceCarrier() {
        return varianceCarrier;
    }

    public String getVarianceTracking() {
        return varianceTracking;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceItems", varianceItems);
        state.put("varianceQuantities", varianceQuantities);
        state.put("varianceCustomer", varianceCustomer);
        state.put("varianceDate", varianceDate);
        state.put("varianceCarrier", varianceCarrier);
        state.put("varianceTracking", varianceTracking);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
