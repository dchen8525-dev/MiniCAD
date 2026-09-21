package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved SURFACE_TEXTURE_REPRESENTATION_ITEM.
 * A surface texture representation item entity.
 *
 * @param id STEP instance id
 * @param name item name
 * @param roughnessValue roughness value
 * @param roughnessUnit roughness unit
 * @param measurementMethod measurement method
 */
public final class StepSurfaceTextureRepresentationItem extends AbstractStepEntity {
    private final Double roughnessValue;
    private final StepEntity roughnessUnit;
    private final String measurementMethod;

    public StepSurfaceTextureRepresentationItem(int id, String name, Double roughnessValue, StepEntity roughnessUnit, String measurementMethod) {
        super(id, name);
        this.roughnessValue = roughnessValue;
        this.roughnessUnit = roughnessUnit;
        this.measurementMethod = measurementMethod;
    }

    public Double getRoughnessValue() {
        return roughnessValue;
    }

    public StepEntity getRoughnessUnit() {
        return roughnessUnit;
    }

    public String getMeasurementMethod() {
        return measurementMethod;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("roughnessValue", roughnessValue);
        state.put("roughnessUnit", roughnessUnit);
        state.put("measurementMethod", measurementMethod);
        return state;
    }
}
