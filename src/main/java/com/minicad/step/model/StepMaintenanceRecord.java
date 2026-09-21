package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved MAINTENANCE_RECORD.
 * A maintenance record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceEquipment maintained variance equipment
 * @varianceType maintenance variance type
 * @varianceDate maintenance variance date
 * @varianceActions maintenance variance actions
 * @varianceParts maintenance variance parts used
 * @varianceCost maintenance variance cost
 * @varianceStatus record variance status
 */
public final class StepMaintenanceRecord extends AbstractStepEntity {
    private final StepEntity varianceEquipment;
    private final String varianceType;
    private final StepEntity varianceDate;
    private final List<String> varianceActions;
    private final List<StepEntity> varianceParts;
    private final double varianceCost;
    private final String varianceStatus;

    public StepMaintenanceRecord(int id, String name, StepEntity varianceEquipment, String varianceType, StepEntity varianceDate, List<String> varianceActions, List<StepEntity> varianceParts, double varianceCost, String varianceStatus) {
        super(id, name);
        this.varianceEquipment = varianceEquipment;
        this.varianceType = varianceType;
        this.varianceDate = varianceDate;
        this.varianceActions = varianceActions == null ? null : java.util.List.copyOf(varianceActions);
        this.varianceParts = varianceParts == null ? null : java.util.List.copyOf(varianceParts);
        this.varianceCost = varianceCost;
        this.varianceStatus = varianceStatus;
    }

    public StepEntity getVarianceEquipment() {
        return varianceEquipment;
    }

    public String getVarianceType() {
        return varianceType;
    }

    public StepEntity getVarianceDate() {
        return varianceDate;
    }

    public List<String> getVarianceActions() {
        return varianceActions;
    }

    public List<StepEntity> getVarianceParts() {
        return varianceParts;
    }

    public double getVarianceCost() {
        return varianceCost;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceEquipment", varianceEquipment);
        state.put("varianceType", varianceType);
        state.put("varianceDate", varianceDate);
        state.put("varianceActions", varianceActions);
        state.put("varianceParts", varianceParts);
        state.put("varianceCost", varianceCost);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
