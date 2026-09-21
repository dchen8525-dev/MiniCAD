package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

public final class StepRenderingProperties extends AbstractStepEntity {
    private final double specularExponent;
    private final double specularRoughness;

    public StepRenderingProperties(int id, String name, double specularExponent, double specularRoughness) {
        super(id, name);
        this.specularExponent = specularExponent;
        this.specularRoughness = specularRoughness;
    }

    public double getSpecularExponent() {
        return specularExponent;
    }

    public double getSpecularRoughness() {
        return specularRoughness;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("specularExponent", specularExponent);
        state.put("specularRoughness", specularRoughness);
        return state;
    }
}
