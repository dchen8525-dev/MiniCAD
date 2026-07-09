package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepMaterialStock implements StepEntity {
    private final int id;
    private final String name;
    private final String materialType;
    private final int varianceQuantity;
    private final StepEntity varianceUnit;
    private final String varianceLocation;
    private final double varianceCost;
    private final double varianceLeadTime;
    private final String varianceStatus;

    public StepMaterialStock(int id, String name, String materialType, int varianceQuantity, StepEntity varianceUnit, String varianceLocation, double varianceCost, double varianceLeadTime, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.materialType = materialType;
        this.varianceQuantity = varianceQuantity;
        this.varianceUnit = varianceUnit;
        this.varianceLocation = varianceLocation;
        this.varianceCost = varianceCost;
        this.varianceLeadTime = varianceLeadTime;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepMaterialStock that = (StepMaterialStock) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(materialType, that.materialType) && varianceQuantity == that.varianceQuantity && Objects.equals(varianceUnit, that.varianceUnit) && Objects.equals(varianceLocation, that.varianceLocation) && varianceCost == that.varianceCost && varianceLeadTime == that.varianceLeadTime && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, materialType, varianceQuantity, varianceUnit, varianceLocation, varianceCost, varianceLeadTime, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepMaterialStock{" + "id=" + id + "name=" + name + "materialType=" + materialType + "varianceQuantity=" + varianceQuantity + "varianceUnit=" + varianceUnit + "varianceLocation=" + varianceLocation + "varianceCost=" + varianceCost + "varianceLeadTime=" + varianceLeadTime + "varianceStatus=" + varianceStatus + "}";
    }
}