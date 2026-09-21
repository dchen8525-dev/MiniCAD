package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved PART_STOCK.
 * A part stock entity.
 *
 * @param id STEP instance id
 * @param name stock name
 * @param partType part type/part number
 * @varianceQuantity stock variance quantity available
 * @varianceLocation stock variance location
 * @varianceCost unit variance cost
 * @varianceMin reorder variance minimum threshold
 * @varianceMax stock variance maximum limit
 * @varianceStatus stock variance status
 */
public final class StepPartStock extends AbstractStepEntity {
    private final String partType;
    private final int varianceQuantity;
    private final String varianceLocation;
    private final double varianceCost;
    private final int varianceMin;
    private final int varianceMax;
    private final String varianceStatus;

    public StepPartStock(int id, String name, String partType, int varianceQuantity, String varianceLocation, double varianceCost, int varianceMin, int varianceMax, String varianceStatus) {
        super(id, name);
        this.partType = partType;
        this.varianceQuantity = varianceQuantity;
        this.varianceLocation = varianceLocation;
        this.varianceCost = varianceCost;
        this.varianceMin = varianceMin;
        this.varianceMax = varianceMax;
        this.varianceStatus = varianceStatus;
    }

    public String getPartType() {
        return partType;
    }

    public int getVarianceQuantity() {
        return varianceQuantity;
    }

    public String getVarianceLocation() {
        return varianceLocation;
    }

    public double getVarianceCost() {
        return varianceCost;
    }

    public int getVarianceMin() {
        return varianceMin;
    }

    public int getVarianceMax() {
        return varianceMax;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("partType", partType);
        state.put("varianceQuantity", varianceQuantity);
        state.put("varianceLocation", varianceLocation);
        state.put("varianceCost", varianceCost);
        state.put("varianceMin", varianceMin);
        state.put("varianceMax", varianceMax);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
