package com.minicad.step.model.fea;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved THERMAL_BOUNDARY_CONDITION.
 * Thermal boundary condition for FEA.
 */
/**
 * Resolved THERMAL_BOUNDARY_CONDITION.
 * Thermal boundary condition for FEA.
 */
public final class StepThermalBoundaryCondition implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity appliedTo;
    private final double temperature;
    private final double heatFlux;

    public StepThermalBoundaryCondition(int id, String name, StepEntity appliedTo, double temperature, double heatFlux) {
        this.id = id;
        this.name = name;
        this.appliedTo = appliedTo;
        this.temperature = temperature;
        this.heatFlux = heatFlux;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getAppliedTo() {
        return appliedTo;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getHeatFlux() {
        return heatFlux;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepThermalBoundaryCondition that = (StepThermalBoundaryCondition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(appliedTo, that.appliedTo) && temperature == that.temperature && heatFlux == that.heatFlux;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, appliedTo, temperature, heatFlux);
    }

    @Override
    public String toString() {
        return "StepThermalBoundaryCondition{" + "id=" + id + "name=" + name + "appliedTo=" + appliedTo + "temperature=" + temperature + "heatFlux=" + heatFlux + "}";
    }
}
