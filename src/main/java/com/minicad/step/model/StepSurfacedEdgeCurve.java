package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved SURFACED_EDGE_CURVE.
 * An edge curve with associated surface geometry.
 *
 * @param id STEP instance id
 * @param name edge name
 * @param edgeGeometry the underlying curve
 * @param surface1 first associated surface
 * @param surface2 second associated surface
 * @param sameSurface flag indicating surfaces are identical
 */
public final class StepSurfacedEdgeCurve extends AbstractStepEntity {
    private final StepEntity edgeGeometry;
    private final StepEntity surface1;
    private final StepEntity surface2;
    private final boolean sameSurface;

    public StepSurfacedEdgeCurve(int id, String name, StepEntity edgeGeometry, StepEntity surface1, StepEntity surface2, boolean sameSurface) {
        super(id, name);
        this.edgeGeometry = edgeGeometry;
        this.surface1 = surface1;
        this.surface2 = surface2;
        this.sameSurface = sameSurface;
    }

    public StepEntity getEdgeGeometry() {
        return edgeGeometry;
    }

    public StepEntity getSurface1() {
        return surface1;
    }

    public StepEntity getSurface2() {
        return surface2;
    }

    public boolean isSameSurface() {
        return sameSurface;
    }

    // Record-style accessors
    public StepEntity edgeGeometry() { return getEdgeGeometry(); }
    public StepEntity surface1() { return getSurface1(); }
    public StepEntity surface2() { return getSurface2(); }
    public boolean sameSurface() { return isSameSurface(); }
    public boolean reversed() { return false; }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("edgeGeometry", edgeGeometry);
        state.put("surface1", surface1);
        state.put("surface2", surface2);
        state.put("sameSurface", sameSurface);
        return state;
    }
}
