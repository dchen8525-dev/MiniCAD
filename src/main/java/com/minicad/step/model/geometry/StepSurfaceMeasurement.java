package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepSurfaceMeasurement implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity surfaceGeometry;
    private final List<String> roughnessParameters;
    private final List<Double> measuredValues;
    private final String measurementMethod;
    private final StepEntity measurementArea;
    private final String passFailStatus;

    public StepSurfaceMeasurement(int id, String name, StepEntity surfaceGeometry, List<String> roughnessParameters, List<Double> measuredValues, String measurementMethod, StepEntity measurementArea, String passFailStatus) {
        this.id = id;
        this.name = name;
        this.surfaceGeometry = surfaceGeometry;
        this.roughnessParameters = roughnessParameters == null ? null : java.util.List.copyOf(roughnessParameters);
        this.measuredValues = measuredValues == null ? null : java.util.List.copyOf(measuredValues);
        this.measurementMethod = measurementMethod;
        this.measurementArea = measurementArea;
        this.passFailStatus = passFailStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSurfaceMeasurement that = (StepSurfaceMeasurement) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(surfaceGeometry, that.surfaceGeometry) && Objects.equals(roughnessParameters, that.roughnessParameters) && Objects.equals(measuredValues, that.measuredValues) && Objects.equals(measurementMethod, that.measurementMethod) && Objects.equals(measurementArea, that.measurementArea) && Objects.equals(passFailStatus, that.passFailStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surfaceGeometry, roughnessParameters, measuredValues, measurementMethod, measurementArea, passFailStatus);
    }

    @Override
    public String toString() {
        return "StepSurfaceMeasurement{" + "id=" + id + "name=" + name + "surfaceGeometry=" + surfaceGeometry + "roughnessParameters=" + roughnessParameters + "measuredValues=" + measuredValues + "measurementMethod=" + measurementMethod + "measurementArea=" + measurementArea + "passFailStatus=" + passFailStatus + "}";
    }
}