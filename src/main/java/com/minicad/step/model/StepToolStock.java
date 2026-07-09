package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepToolStock implements StepEntity {
    private final int id;
    private final String name;
    private final String toolType;
    private final int varianceQuantity;
    private final String varianceCondition;
    private final String varianceLocation;
    private final double varianceCost;
    private final double varianceLife;
    private final String varianceStatus;

    public StepToolStock(int id, String name, String toolType, int varianceQuantity, String varianceCondition, String varianceLocation, double varianceCost, double varianceLife, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.toolType = toolType;
        this.varianceQuantity = varianceQuantity;
        this.varianceCondition = varianceCondition;
        this.varianceLocation = varianceLocation;
        this.varianceCost = varianceCost;
        this.varianceLife = varianceLife;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepToolStock that = (StepToolStock) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(toolType, that.toolType) && varianceQuantity == that.varianceQuantity && Objects.equals(varianceCondition, that.varianceCondition) && Objects.equals(varianceLocation, that.varianceLocation) && varianceCost == that.varianceCost && varianceLife == that.varianceLife && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, toolType, varianceQuantity, varianceCondition, varianceLocation, varianceCost, varianceLife, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepToolStock{" + "id=" + id + "name=" + name + "toolType=" + toolType + "varianceQuantity=" + varianceQuantity + "varianceCondition=" + varianceCondition + "varianceLocation=" + varianceLocation + "varianceCost=" + varianceCost + "varianceLife=" + varianceLife + "varianceStatus=" + varianceStatus + "}";
    }
}