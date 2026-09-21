package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved THERMAL_BOUNDARY_CONDITION.
 * Thermal boundary condition for FEA.
 */
public final class StepThermalBoundaryCondition extends AbstractStepEntity {
    private final StepEntity appliedTo;
    private final double temperature;
    private final double heatFlux;

    public StepThermalBoundaryCondition(int id, String name, StepEntity appliedTo, double temperature, double heatFlux) {
        super(id, name);
        this.appliedTo = appliedTo;
        this.temperature = temperature;
        this.heatFlux = heatFlux;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("appliedTo", appliedTo);
        state.put("temperature", temperature);
        state.put("heatFlux", heatFlux);
        return state;
    }
}
