package com.minicad.step.model;

import java.util.LinkedHashMap;
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
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("surface", surface);
        state.put("roughnessValues", roughnessValues);
        state.put("qualityGrade", qualityGrade);
        state.put("measurementMethod", measurementMethod);
        state.put("direction", direction);
        return state;
    }
}
