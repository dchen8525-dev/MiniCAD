package com.minicad.step.model.resource;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepEquipmentStock implements StepEntity {
    private final int id;
    private final String name;
    private final String equipmentType;
    private final int varianceQuantity;
    private final String varianceCondition;
    private final String varianceLocation;
    private final double varianceValue;
    private final StepEntity varianceMaintenance;
    private final String varianceStatus;

    public StepEquipmentStock(int id, String name, String equipmentType, int varianceQuantity, String varianceCondition, String varianceLocation, double varianceValue, StepEntity varianceMaintenance, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.equipmentType = equipmentType;
        this.varianceQuantity = varianceQuantity;
        this.varianceCondition = varianceCondition;
        this.varianceLocation = varianceLocation;
        this.varianceValue = varianceValue;
        this.varianceMaintenance = varianceMaintenance;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepEquipmentStock that = (StepEquipmentStock) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(equipmentType, that.equipmentType) && varianceQuantity == that.varianceQuantity && Objects.equals(varianceCondition, that.varianceCondition) && Objects.equals(varianceLocation, that.varianceLocation) && varianceValue == that.varianceValue && Objects.equals(varianceMaintenance, that.varianceMaintenance) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, equipmentType, varianceQuantity, varianceCondition, varianceLocation, varianceValue, varianceMaintenance, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepEquipmentStock{" + "id=" + id + "name=" + name + "equipmentType=" + equipmentType + "varianceQuantity=" + varianceQuantity + "varianceCondition=" + varianceCondition + "varianceLocation=" + varianceLocation + "varianceValue=" + varianceValue + "varianceMaintenance=" + varianceMaintenance + "varianceStatus=" + varianceStatus + "}";
    }
}