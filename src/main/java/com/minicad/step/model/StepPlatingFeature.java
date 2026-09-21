package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved PLATING_FEATURE.
 * A plating feature entity.
 *
 * @param id STEP instance id
 * @param name plating name
 * @param platingType plating type (electroplating, electroless, anodizing)
 * @param platingMaterial plating material specification
 * @param platingThickness plating thickness
 * @param appliedSurfaces surfaces to be plated
 * @param platingParameters plating process parameters
 * @param platingQuality plating quality grade
 */
public final class StepPlatingFeature extends AbstractStepEntity {
    private final String platingType;
    private final StepEntity platingMaterial;
    private final double platingThickness;
    private final List<StepEntity> appliedSurfaces;
    private final List<Double> platingParameters;
    private final String platingQuality;

    public StepPlatingFeature(int id, String name, String platingType, StepEntity platingMaterial, double platingThickness, List<StepEntity> appliedSurfaces, List<Double> platingParameters, String platingQuality) {
        super(id, name);
        this.platingType = platingType;
        this.platingMaterial = platingMaterial;
        this.platingThickness = platingThickness;
        this.appliedSurfaces = appliedSurfaces == null ? null : java.util.List.copyOf(appliedSurfaces);
        this.platingParameters = platingParameters == null ? null : java.util.List.copyOf(platingParameters);
        this.platingQuality = platingQuality;
    }

    public String getPlatingType() {
        return platingType;
    }

    public StepEntity getPlatingMaterial() {
        return platingMaterial;
    }

    public double getPlatingThickness() {
        return platingThickness;
    }

    public List<StepEntity> getAppliedSurfaces() {
        return appliedSurfaces;
    }

    public List<Double> getPlatingParameters() {
        return platingParameters;
    }

    public String getPlatingQuality() {
        return platingQuality;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("platingType", platingType);
        state.put("platingMaterial", platingMaterial);
        state.put("platingThickness", platingThickness);
        state.put("appliedSurfaces", appliedSurfaces);
        state.put("platingParameters", platingParameters);
        state.put("platingQuality", platingQuality);
        return state;
    }
}
