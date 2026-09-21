package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved TOOL_STOCK.
 * A tool stock entity.
 *
 * @param id STEP instance id
 * @param name stock name
 * @param toolType tool type classification
 * @varianceQuantity stock variance quantity available
 * @varianceCondition tool variance condition (new, used, reconditioned)
 * @varianceLocation stock variance location
 * @varianceCost unit variance cost
 * @varianceLife tool variance expected life
 * @varianceStatus stock variance status
 */
public final class StepToolStock extends AbstractStepEntity {
    private final String toolType;
    private final int varianceQuantity;
    private final String varianceCondition;
    private final String varianceLocation;
    private final double varianceCost;
    private final double varianceLife;
    private final String varianceStatus;

    public StepToolStock(int id, String name, String toolType, int varianceQuantity, String varianceCondition, String varianceLocation, double varianceCost, double varianceLife, String varianceStatus) {
        super(id, name);
        this.toolType = toolType;
        this.varianceQuantity = varianceQuantity;
        this.varianceCondition = varianceCondition;
        this.varianceLocation = varianceLocation;
        this.varianceCost = varianceCost;
        this.varianceLife = varianceLife;
        this.varianceStatus = varianceStatus;
    }

    public String getToolType() {
        return toolType;
    }

    public int getVarianceQuantity() {
        return varianceQuantity;
    }

    public String getVarianceCondition() {
        return varianceCondition;
    }

    public String getVarianceLocation() {
        return varianceLocation;
    }

    public double getVarianceCost() {
        return varianceCost;
    }

    public double getVarianceLife() {
        return varianceLife;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("toolType", toolType);
        state.put("varianceQuantity", varianceQuantity);
        state.put("varianceCondition", varianceCondition);
        state.put("varianceLocation", varianceLocation);
        state.put("varianceCost", varianceCost);
        state.put("varianceLife", varianceLife);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
