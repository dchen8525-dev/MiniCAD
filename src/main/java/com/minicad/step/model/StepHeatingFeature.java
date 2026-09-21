package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved HEATING_FEATURE.
 * A heating feature entity.
 *
 * @param id STEP instance id
 * @param name heating name
 * @param heatingType heating type (electric, gas, induction)
 * @param heatingGeometry heating geometry representation
 * @param heatingCapacity heating capacity specification
 * @param heatingElements heating element features
 * @param operatingTemperature operating temperature range
 * @param heatingControl heating control specification
 */
public final class StepHeatingFeature extends AbstractStepEntity {
    private final String heatingType;
    private final StepEntity heatingGeometry;
    private final double heatingCapacity;
    private final List<StepEntity> heatingElements;
    private final List<Double> operatingTemperature;
    private final StepEntity heatingControl;

    public StepHeatingFeature(int id, String name, String heatingType, StepEntity heatingGeometry, double heatingCapacity, List<StepEntity> heatingElements, List<Double> operatingTemperature, StepEntity heatingControl) {
        super(id, name);
        this.heatingType = heatingType;
        this.heatingGeometry = heatingGeometry;
        this.heatingCapacity = heatingCapacity;
        this.heatingElements = heatingElements == null ? null : java.util.List.copyOf(heatingElements);
        this.operatingTemperature = operatingTemperature == null ? null : java.util.List.copyOf(operatingTemperature);
        this.heatingControl = heatingControl;
    }

    public String getHeatingType() {
        return heatingType;
    }

    public StepEntity getHeatingGeometry() {
        return heatingGeometry;
    }

    public double getHeatingCapacity() {
        return heatingCapacity;
    }

    public List<StepEntity> getHeatingElements() {
        return heatingElements;
    }

    public List<Double> getOperatingTemperature() {
        return operatingTemperature;
    }

    public StepEntity getHeatingControl() {
        return heatingControl;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("heatingType", heatingType);
        state.put("heatingGeometry", heatingGeometry);
        state.put("heatingCapacity", heatingCapacity);
        state.put("heatingElements", heatingElements);
        state.put("operatingTemperature", operatingTemperature);
        state.put("heatingControl", heatingControl);
        return state;
    }
}
