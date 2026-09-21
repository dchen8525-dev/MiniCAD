package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved COOLING_FEATURE.
 * A cooling feature entity.
 *
 * @param id STEP instance id
 * @param name cooling name
 * @param coolingType cooling type (air, liquid, refrigeration)
 * @param coolingGeometry cooling geometry representation
 * @param coolingCapacity cooling capacity specification
 * @param coolantType coolant type specification
 * @param coolingChannels cooling channel features
 * @param operatingTemperature operating temperature range
 */
public final class StepCoolingFeature extends AbstractStepEntity {
    private final String coolingType;
    private final StepEntity coolingGeometry;
    private final double coolingCapacity;
    private final StepEntity coolantType;
    private final List<StepEntity> coolingChannels;
    private final List<Double> operatingTemperature;

    public StepCoolingFeature(int id, String name, String coolingType, StepEntity coolingGeometry, double coolingCapacity, StepEntity coolantType, List<StepEntity> coolingChannels, List<Double> operatingTemperature) {
        super(id, name);
        this.coolingType = coolingType;
        this.coolingGeometry = coolingGeometry;
        this.coolingCapacity = coolingCapacity;
        this.coolantType = coolantType;
        this.coolingChannels = coolingChannels == null ? null : java.util.List.copyOf(coolingChannels);
        this.operatingTemperature = operatingTemperature == null ? null : java.util.List.copyOf(operatingTemperature);
    }

    public String getCoolingType() {
        return coolingType;
    }

    public StepEntity getCoolingGeometry() {
        return coolingGeometry;
    }

    public double getCoolingCapacity() {
        return coolingCapacity;
    }

    public StepEntity getCoolantType() {
        return coolantType;
    }

    public List<StepEntity> getCoolingChannels() {
        return coolingChannels;
    }

    public List<Double> getOperatingTemperature() {
        return operatingTemperature;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("coolingType", coolingType);
        state.put("coolingGeometry", coolingGeometry);
        state.put("coolingCapacity", coolingCapacity);
        state.put("coolantType", coolantType);
        state.put("coolingChannels", coolingChannels);
        state.put("operatingTemperature", operatingTemperature);
        return state;
    }
}
