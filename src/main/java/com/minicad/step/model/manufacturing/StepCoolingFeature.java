package com.minicad.step.model.manufacturing;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepCoolingFeature implements StepEntity {
    private final int id;
    private final String name;
    private final String coolingType;
    private final StepEntity coolingGeometry;
    private final double coolingCapacity;
    private final StepEntity coolantType;
    private final List<StepEntity> coolingChannels;
    private final List<Double> operatingTemperature;

    public StepCoolingFeature(int id, String name, String coolingType, StepEntity coolingGeometry, double coolingCapacity, StepEntity coolantType, List<StepEntity> coolingChannels, List<Double> operatingTemperature) {
        this.id = id;
        this.name = name;
        this.coolingType = coolingType;
        this.coolingGeometry = coolingGeometry;
        this.coolingCapacity = coolingCapacity;
        this.coolantType = coolantType;
        this.coolingChannels = coolingChannels == null ? null : java.util.List.copyOf(coolingChannels);
        this.operatingTemperature = operatingTemperature == null ? null : java.util.List.copyOf(operatingTemperature);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCoolingFeature that = (StepCoolingFeature) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(coolingType, that.coolingType) && Objects.equals(coolingGeometry, that.coolingGeometry) && coolingCapacity == that.coolingCapacity && Objects.equals(coolantType, that.coolantType) && Objects.equals(coolingChannels, that.coolingChannels) && Objects.equals(operatingTemperature, that.operatingTemperature);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coolingType, coolingGeometry, coolingCapacity, coolantType, coolingChannels, operatingTemperature);
    }

    @Override
    public String toString() {
        return "StepCoolingFeature{" + "id=" + id + "name=" + name + "coolingType=" + coolingType + "coolingGeometry=" + coolingGeometry + "coolingCapacity=" + coolingCapacity + "coolantType=" + coolantType + "coolingChannels=" + coolingChannels + "operatingTemperature=" + operatingTemperature + "}";
    }
}