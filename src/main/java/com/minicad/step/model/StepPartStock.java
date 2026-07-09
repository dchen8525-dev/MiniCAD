package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepPartStock implements StepEntity {
    private final int id;
    private final String name;
    private final String partType;
    private final int varianceQuantity;
    private final String varianceLocation;
    private final double varianceCost;
    private final int varianceMin;
    private final int varianceMax;
    private final String varianceStatus;

    public StepPartStock(int id, String name, String partType, int varianceQuantity, String varianceLocation, double varianceCost, int varianceMin, int varianceMax, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.partType = partType;
        this.varianceQuantity = varianceQuantity;
        this.varianceLocation = varianceLocation;
        this.varianceCost = varianceCost;
        this.varianceMin = varianceMin;
        this.varianceMax = varianceMax;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPartStock that = (StepPartStock) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(partType, that.partType) && varianceQuantity == that.varianceQuantity && Objects.equals(varianceLocation, that.varianceLocation) && varianceCost == that.varianceCost && varianceMin == that.varianceMin && varianceMax == that.varianceMax && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, partType, varianceQuantity, varianceLocation, varianceCost, varianceMin, varianceMax, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepPartStock{" + "id=" + id + "name=" + name + "partType=" + partType + "varianceQuantity=" + varianceQuantity + "varianceLocation=" + varianceLocation + "varianceCost=" + varianceCost + "varianceMin=" + varianceMin + "varianceMax=" + varianceMax + "varianceStatus=" + varianceStatus + "}";
    }
}