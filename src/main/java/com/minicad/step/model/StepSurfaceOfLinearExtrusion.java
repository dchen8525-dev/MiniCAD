package com.minicad.step.model;

import java.util.Map;

/**
 * Resolved SURFACE_OF_LINEAR_EXTRUSION.
 *
 * @param id step id
 * @param name step label
 * @param sweptCurve directrix curve
 * @param extrusionAxis extrusion vector
 */
public final class StepSurfaceOfLinearExtrusion extends AbstractStepEntity {
    private final StepEntity sweptCurve;
    private final StepVector extrusionAxis;

    public StepSurfaceOfLinearExtrusion(int id, String name, StepEntity sweptCurve, StepVector extrusionAxis) {
        super(id, name);
        this.sweptCurve = sweptCurve;
        this.extrusionAxis = extrusionAxis;
    }

    public StepEntity getSweptCurve() {
        return sweptCurve;
    }

    public StepVector getExtrusionAxis() {
        return extrusionAxis;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity sweptCurve() { return getSweptCurve(); }
    public StepVector extrusionAxis() { return getExtrusionAxis(); }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "sweptCurve", sweptCurve, "extrusionAxis", extrusionAxis);
    }
}
