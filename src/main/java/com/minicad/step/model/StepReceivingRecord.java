package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepReceivingRecord extends AbstractStepEntity {
    private final List<StepEntity> varianceItems;
    private final List<Integer> varianceQuantities;
    private final StepEntity varianceSupplier;
    private final StepEntity varianceDate;
    private final String varianceCondition;
    private final String varianceInspection;
    private final String varianceStatus;

    public StepReceivingRecord(int id, String name, List<StepEntity> varianceItems, List<Integer> varianceQuantities, StepEntity varianceSupplier, StepEntity varianceDate, String varianceCondition, String varianceInspection, String varianceStatus) {
        super(id, name);
        this.varianceItems = varianceItems == null ? null : java.util.List.copyOf(varianceItems);
        this.varianceQuantities = varianceQuantities == null ? null : java.util.List.copyOf(varianceQuantities);
        this.varianceSupplier = varianceSupplier;
        this.varianceDate = varianceDate;
        this.varianceCondition = varianceCondition;
        this.varianceInspection = varianceInspection;
        this.varianceStatus = varianceStatus;
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

    public String getVarianceCondition() {
        return varianceCondition;
    }

    public String getVarianceInspection() {
        return varianceInspection;
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
        state.put("varianceSupplier", varianceSupplier);
        state.put("varianceDate", varianceDate);
        state.put("varianceCondition", varianceCondition);
        state.put("varianceInspection", varianceInspection);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
