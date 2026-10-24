package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved SURFACE_QUALITY.
 * A surface quality entity.
 *
 * @param id STEP instance id
 * @param name quality name
 * @param surface surface reference
 * @param roughnessValues surface roughness values (Ra, Rz, etc.)
 * @param qualityGrade quality grade classification
 * @param measurementMethod measurement method
 * @param direction measurement direction
 */
public final class StepSurfaceQuality extends AbstractStepEntity {
    private final StepEntity surface;
    private final List<Double> roughnessValues;
    private final String qualityGrade;
    private final String measurementMethod;
    private final StepEntity direction;

    public StepSurfaceQuality(int id, String name, StepEntity surface, List<Double> roughnessValues, String qualityGrade, String measurementMethod, StepEntity direction) {
        super(id, name);
        this.surface = surface;
        this.roughnessValues = roughnessValues == null ? null : java.util.List.copyOf(roughnessValues);
        this.qualityGrade = qualityGrade;
        this.measurementMethod = measurementMethod;
        this.direction = direction;
    }

    public StepEntity getSurface() {
        return surface;
    }

    public List<Double> getRoughnessValues() {
        return roughnessValues;
    }

    public String getQualityGrade() {
        return qualityGrade;
    }

    public String getMeasurementMethod() {
        return measurementMethod;
    }

    public StepEntity getDirection() {
        return direction;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "surface", surface, "roughnessValues", roughnessValues,
                "qualityGrade", qualityGrade, "measurementMethod", measurementMethod, "direction", direction);
    }
}
