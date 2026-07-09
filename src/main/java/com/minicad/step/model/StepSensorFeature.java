package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved SENSOR_FEATURE.
 * A sensor feature entity.
 *
 * @param id STEP instance id
 * @param name sensor name
 * @param sensorType sensor type (position, temperature, pressure, vision)
 * @param sensorGeometry sensor geometry representation
 * @param sensorPosition sensor position placement
 * @param measurementRange sensor measurement range
 * @varianceResolution sensor variance resolution
 * @param sensorInterface sensor interface specification
 */
/**
 * Resolved SENSOR_FEATURE.
 * A sensor feature entity.
 *
 * @param id STEP instance id
 * @param name sensor name
 * @param sensorType sensor type (position, temperature, pressure, vision)
 * @param sensorGeometry sensor geometry representation
 * @param sensorPosition sensor position placement
 * @param measurementRange sensor measurement range
 * @varianceResolution sensor variance resolution
 * @param sensorInterface sensor interface specification
 */
public final class StepSensorFeature implements StepEntity {
    private final int id;
    private final String name;
    private final String sensorType;
    private final StepEntity sensorGeometry;
    private final StepEntity sensorPosition;
    private final List<Double> measurementRange;
    private final double varianceResolution;
    private final String sensorInterface;

    public StepSensorFeature(int id, String name, String sensorType, StepEntity sensorGeometry, StepEntity sensorPosition, List<Double> measurementRange, double varianceResolution, String sensorInterface) {
        this.id = id;
        this.name = name;
        this.sensorType = sensorType;
        this.sensorGeometry = sensorGeometry;
        this.sensorPosition = sensorPosition;
        this.measurementRange = measurementRange == null ? null : java.util.List.copyOf(measurementRange);
        this.varianceResolution = varianceResolution;
        this.sensorInterface = sensorInterface;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSensorType() {
        return sensorType;
    }

    public StepEntity getSensorGeometry() {
        return sensorGeometry;
    }

    public StepEntity getSensorPosition() {
        return sensorPosition;
    }

    public List<Double> getMeasurementRange() {
        return measurementRange;
    }

    public double getVarianceResolution() {
        return varianceResolution;
    }

    public String getSensorInterface() {
        return sensorInterface;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSensorFeature that = (StepSensorFeature) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(sensorType, that.sensorType) && Objects.equals(sensorGeometry, that.sensorGeometry) && Objects.equals(sensorPosition, that.sensorPosition) && Objects.equals(measurementRange, that.measurementRange) && varianceResolution == that.varianceResolution && Objects.equals(sensorInterface, that.sensorInterface);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, sensorType, sensorGeometry, sensorPosition, measurementRange, varianceResolution, sensorInterface);
    }

    @Override
    public String toString() {
        return "StepSensorFeature{" + "id=" + id + "name=" + name + "sensorType=" + sensorType + "sensorGeometry=" + sensorGeometry + "sensorPosition=" + sensorPosition + "measurementRange=" + measurementRange + "varianceResolution=" + varianceResolution + "sensorInterface=" + sensorInterface + "}";
    }
}