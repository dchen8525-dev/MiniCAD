package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved BLENDED_SURFACE.
 * A blended surface entity.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param blendingType blending type classification
 * @param primarySurface primary surface for blend
 * @param secondarySurface secondary surface for blend
 * @param blendRadius blend radius
 * @param blendCurve blend curve defining the blend path
 */
public final class StepBlendedSurface extends AbstractStepEntity {
    private final String blendingType;
    private final StepEntity primarySurface;
    private final StepEntity secondarySurface;
    private final double blendRadius;
    private final StepEntity blendCurve;

    public StepBlendedSurface(int id, String name, String blendingType, StepEntity primarySurface, StepEntity secondarySurface, double blendRadius, StepEntity blendCurve) {
        super(id, name);
        this.blendingType = blendingType;
        this.primarySurface = primarySurface;
        this.secondarySurface = secondarySurface;
        this.blendRadius = blendRadius;
        this.blendCurve = blendCurve;
    }

    public String getBlendingType() {
        return blendingType;
    }

    public StepEntity getPrimarySurface() {
        return primarySurface;
    }

    public StepEntity getSecondarySurface() {
        return secondarySurface;
    }

    public double getBlendRadius() {
        return blendRadius;
    }

    public StepEntity getBlendCurve() {
        return blendCurve;
    }

    // Record-style accessors
    public StepEntity primarySurface() { return getPrimarySurface(); }
    public StepEntity secondarySurface() { return getSecondarySurface(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("blendingType", blendingType);
        state.put("primarySurface", primarySurface);
        state.put("secondarySurface", secondarySurface);
        state.put("blendRadius", blendRadius);
        state.put("blendCurve", blendCurve);
        return state;
    }
}
