package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepHeatingFeature implements StepEntity {
    private final int id;
    private final String name;
    private final String heatingType;
    private final StepEntity heatingGeometry;
    private final double heatingCapacity;
    private final List<StepEntity> heatingElements;
    private final List<Double> operatingTemperature;
    private final StepEntity heatingControl;

    public StepHeatingFeature(int id, String name, String heatingType, StepEntity heatingGeometry, double heatingCapacity, List<StepEntity> heatingElements, List<Double> operatingTemperature, StepEntity heatingControl) {
        this.id = id;
        this.name = name;
        this.heatingType = heatingType;
        this.heatingGeometry = heatingGeometry;
        this.heatingCapacity = heatingCapacity;
        this.heatingElements = heatingElements == null ? null : java.util.List.copyOf(heatingElements);
        this.operatingTemperature = operatingTemperature == null ? null : java.util.List.copyOf(operatingTemperature);
        this.heatingControl = heatingControl;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepHeatingFeature that = (StepHeatingFeature) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(heatingType, that.heatingType) && Objects.equals(heatingGeometry, that.heatingGeometry) && heatingCapacity == that.heatingCapacity && Objects.equals(heatingElements, that.heatingElements) && Objects.equals(operatingTemperature, that.operatingTemperature) && Objects.equals(heatingControl, that.heatingControl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, heatingType, heatingGeometry, heatingCapacity, heatingElements, operatingTemperature, heatingControl);
    }

    @Override
    public String toString() {
        return "StepHeatingFeature{" + "id=" + id + "name=" + name + "heatingType=" + heatingType + "heatingGeometry=" + heatingGeometry + "heatingCapacity=" + heatingCapacity + "heatingElements=" + heatingElements + "operatingTemperature=" + operatingTemperature + "heatingControl=" + heatingControl + "}";
    }
}