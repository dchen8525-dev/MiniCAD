package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved WARRANTY_INFORMATION.
 * A warranty information entity.
 *
 * @param id STEP instance id
 * @param name warranty name
 * @param warrantyType warranty type (standard, extended, service)
 * @param warrantyPeriod warranty period duration
 * @param warrantyStart warranty start date
 * @param warrantyEnd warranty end date
 * @varianceConditions warranty variance conditions
 * @param warrantyProvider warranty provider reference
 * @param warrantyStatus warranty status (active, expired)
 */
public final class StepWarrantyInformation extends AbstractStepEntity {
    private final String warrantyType;
    private final double warrantyPeriod;
    private final StepEntity warrantyStart;
    private final StepEntity warrantyEnd;
    private final List<String> varianceConditions;
    private final StepEntity warrantyProvider;
    private final String warrantyStatus;

    public StepWarrantyInformation(int id, String name, String warrantyType, double warrantyPeriod, StepEntity warrantyStart, StepEntity warrantyEnd, List<String> varianceConditions, StepEntity warrantyProvider, String warrantyStatus) {
        super(id, name);
        this.warrantyType = warrantyType;
        this.warrantyPeriod = warrantyPeriod;
        this.warrantyStart = warrantyStart;
        this.warrantyEnd = warrantyEnd;
        this.varianceConditions = varianceConditions == null ? null : java.util.List.copyOf(varianceConditions);
        this.warrantyProvider = warrantyProvider;
        this.warrantyStatus = warrantyStatus;
    }

    public String getWarrantyType() {
        return warrantyType;
    }

    public double getWarrantyPeriod() {
        return warrantyPeriod;
    }

    public StepEntity getWarrantyStart() {
        return warrantyStart;
    }

    public StepEntity getWarrantyEnd() {
        return warrantyEnd;
    }

    public List<String> getVarianceConditions() {
        return varianceConditions;
    }

    public StepEntity getWarrantyProvider() {
        return warrantyProvider;
    }

    public String getWarrantyStatus() {
        return warrantyStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("warrantyType", warrantyType);
        state.put("warrantyPeriod", warrantyPeriod);
        state.put("warrantyStart", warrantyStart);
        state.put("warrantyEnd", warrantyEnd);
        state.put("varianceConditions", varianceConditions);
        state.put("warrantyProvider", warrantyProvider);
        state.put("warrantyStatus", warrantyStatus);
        return state;
    }
}
