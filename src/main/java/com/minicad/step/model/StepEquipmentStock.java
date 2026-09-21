package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved EQUIPMENT_STOCK.
 * An equipment stock entity.
 *
 * @param id STEP instance id
 * @param name stock name
 * @param equipmentType equipment type classification
 * @varianceQuantity stock variance quantity available
 * @varianceCondition equipment variance condition
 * @varianceLocation stock variance location
 * @varianceValue equipment variance value
 * @varianceMaintenance last variance maintenance date
 * @varianceStatus stock variance status
 */
public final class StepEquipmentStock extends AbstractStepEntity {
    private final String equipmentType;
    private final int varianceQuantity;
    private final String varianceCondition;
    private final String varianceLocation;
    private final double varianceValue;
    private final StepEntity varianceMaintenance;
    private final String varianceStatus;

    public StepEquipmentStock(int id, String name, String equipmentType, int varianceQuantity, String varianceCondition, String varianceLocation, double varianceValue, StepEntity varianceMaintenance, String varianceStatus) {
        super(id, name);
        this.equipmentType = equipmentType;
        this.varianceQuantity = varianceQuantity;
        this.varianceCondition = varianceCondition;
        this.varianceLocation = varianceLocation;
        this.varianceValue = varianceValue;
        this.varianceMaintenance = varianceMaintenance;
        this.varianceStatus = varianceStatus;
    }

    public String getEquipmentType() {
        return equipmentType;
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

    public double getVarianceValue() {
        return varianceValue;
    }

    public StepEntity getVarianceMaintenance() {
        return varianceMaintenance;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("equipmentType", equipmentType);
        state.put("varianceQuantity", varianceQuantity);
        state.put("varianceCondition", varianceCondition);
        state.put("varianceLocation", varianceLocation);
        state.put("varianceValue", varianceValue);
        state.put("varianceMaintenance", varianceMaintenance);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
