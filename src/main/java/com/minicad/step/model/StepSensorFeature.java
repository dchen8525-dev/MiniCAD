package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepSensorFeature extends AbstractStepEntity {
    private final String sensorType;
    private final StepEntity sensorGeometry;
    private final StepEntity sensorPosition;
    private final List<Double> measurementRange;
    private final double varianceResolution;
    private final String sensorInterface;

    public StepSensorFeature(int id, String name, String sensorType, StepEntity sensorGeometry, StepEntity sensorPosition, List<Double> measurementRange, double varianceResolution, String sensorInterface) {
        super(id, name);
        this.sensorType = sensorType;
        this.sensorGeometry = sensorGeometry;
        this.sensorPosition = sensorPosition;
        this.measurementRange = measurementRange == null ? null : java.util.List.copyOf(measurementRange);
        this.varianceResolution = varianceResolution;
        this.sensorInterface = sensorInterface;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("sensorType", sensorType);
        state.put("sensorGeometry", sensorGeometry);
        state.put("sensorPosition", sensorPosition);
        state.put("measurementRange", measurementRange);
        state.put("varianceResolution", varianceResolution);
        state.put("sensorInterface", sensorInterface);
        return state;
    }
}
