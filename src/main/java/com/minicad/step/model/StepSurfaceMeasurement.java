package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved SURFACE_MEASUREMENT.
 * A surface measurement entity.
 *
 * @param id STEP instance id
 * @param name measurement name
 * @param surfaceGeometry surface being measured
 * @param roughnessParameters roughness parameters (Ra, Rz, Rq)
 * @param measuredValues measured roughness values
 * @param measurementMethod measurement method specification
 * @param measurementArea measurement area/location
 * @param passFailStatus pass/fail status result
 */
public final class StepSurfaceMeasurement extends AbstractStepEntity {
    private final StepEntity surfaceGeometry;
    private final List<String> roughnessParameters;
    private final List<Double> measuredValues;
    private final String measurementMethod;
    private final StepEntity measurementArea;
    private final String passFailStatus;

    public StepSurfaceMeasurement(int id, String name, StepEntity surfaceGeometry, List<String> roughnessParameters, List<Double> measuredValues, String measurementMethod, StepEntity measurementArea, String passFailStatus) {
        super(id, name);
        this.surfaceGeometry = surfaceGeometry;
        this.roughnessParameters = roughnessParameters == null ? null : java.util.List.copyOf(roughnessParameters);
        this.measuredValues = measuredValues == null ? null : java.util.List.copyOf(measuredValues);
        this.measurementMethod = measurementMethod;
        this.measurementArea = measurementArea;
        this.passFailStatus = passFailStatus;
    }

    public StepEntity getSurfaceGeometry() {
        return surfaceGeometry;
    }

    public List<String> getRoughnessParameters() {
        return roughnessParameters;
    }

    public List<Double> getMeasuredValues() {
        return measuredValues;
    }

    public String getMeasurementMethod() {
        return measurementMethod;
    }

    public StepEntity getMeasurementArea() {
        return measurementArea;
    }

    public String getPassFailStatus() {
        return passFailStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("surfaceGeometry", surfaceGeometry);
        state.put("roughnessParameters", roughnessParameters);
        state.put("measuredValues", measuredValues);
        state.put("measurementMethod", measurementMethod);
        state.put("measurementArea", measurementArea);
        state.put("passFailStatus", passFailStatus);
        return state;
    }
}
