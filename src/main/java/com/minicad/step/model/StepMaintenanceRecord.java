package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepMaintenanceRecord implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity varianceEquipment;
    private final String varianceType;
    private final StepEntity varianceDate;
    private final List<String> varianceActions;
    private final List<StepEntity> varianceParts;
    private final double varianceCost;
    private final String varianceStatus;

    public StepMaintenanceRecord(int id, String name, StepEntity varianceEquipment, String varianceType, StepEntity varianceDate, List<String> varianceActions, List<StepEntity> varianceParts, double varianceCost, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceEquipment = varianceEquipment;
        this.varianceType = varianceType;
        this.varianceDate = varianceDate;
        this.varianceActions = varianceActions == null ? null : java.util.List.copyOf(varianceActions);
        this.varianceParts = varianceParts == null ? null : java.util.List.copyOf(varianceParts);
        this.varianceCost = varianceCost;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepMaintenanceRecord that = (StepMaintenanceRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceEquipment, that.varianceEquipment) && Objects.equals(varianceType, that.varianceType) && Objects.equals(varianceDate, that.varianceDate) && Objects.equals(varianceActions, that.varianceActions) && Objects.equals(varianceParts, that.varianceParts) && varianceCost == that.varianceCost && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceEquipment, varianceType, varianceDate, varianceActions, varianceParts, varianceCost, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepMaintenanceRecord{" + "id=" + id + "name=" + name + "varianceEquipment=" + varianceEquipment + "varianceType=" + varianceType + "varianceDate=" + varianceDate + "varianceActions=" + varianceActions + "varianceParts=" + varianceParts + "varianceCost=" + varianceCost + "varianceStatus=" + varianceStatus + "}";
    }
}