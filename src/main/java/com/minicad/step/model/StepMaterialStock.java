package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved MATERIAL_STOCK.
 * A material stock entity.
 *
 * @param id STEP instance id
 * @param name stock name
 * @param materialType material type classification
 * @varianceQuantity stock variance quantity available
 * @varianceUnit quantity variance unit
 * @varianceLocation stock variance location
 * @varianceCost unit variance cost
 * @varianceLeadTime procurement variance lead time
 * @varianceStatus stock variance status
 */
public final class StepMaterialStock extends AbstractStepEntity {
    private final String materialType;
    private final int varianceQuantity;
    private final StepEntity varianceUnit;
    private final String varianceLocation;
    private final double varianceCost;
    private final double varianceLeadTime;
    private final String varianceStatus;

    public StepMaterialStock(int id, String name, String materialType, int varianceQuantity, StepEntity varianceUnit, String varianceLocation, double varianceCost, double varianceLeadTime, String varianceStatus) {
        super(id, name);
        this.materialType = materialType;
        this.varianceQuantity = varianceQuantity;
        this.varianceUnit = varianceUnit;
        this.varianceLocation = varianceLocation;
        this.varianceCost = varianceCost;
        this.varianceLeadTime = varianceLeadTime;
        this.varianceStatus = varianceStatus;
    }

    public String getMaterialType() {
        return materialType;
    }

    public int getVarianceQuantity() {
        return varianceQuantity;
    }

    public StepEntity getVarianceUnit() {
        return varianceUnit;
    }

    public String getVarianceLocation() {
        return varianceLocation;
    }

    public double getVarianceCost() {
        return varianceCost;
    }

    public double getVarianceLeadTime() {
        return varianceLeadTime;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("materialType", materialType);
        state.put("varianceQuantity", varianceQuantity);
        state.put("varianceUnit", varianceUnit);
        state.put("varianceLocation", varianceLocation);
        state.put("varianceCost", varianceCost);
        state.put("varianceLeadTime", varianceLeadTime);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
