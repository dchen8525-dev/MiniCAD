package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved SWEPT_DISK_SOLID.
 * A solid formed by sweeping a circular disk along a curve.
 *
 * @param id STEP instance id
 * @param name solid name
 * @param sweptCurve the curve along which to sweep
 * @param radius disk radius
 * @param innerRadius inner disk radius (0 for solid disk)
 */
public final class StepSweptDiskSolid extends AbstractStepEntity {
    private final StepEntity sweptCurve;
    private final double radius;
    private final Double innerRadius;

    public StepSweptDiskSolid(int id, String name, StepEntity sweptCurve, double radius, Double innerRadius) {
        super(id, name);
        this.sweptCurve = sweptCurve;
        this.radius = radius;
        this.innerRadius = innerRadius;
    }

    public StepEntity getSweptCurve() {
        return sweptCurve;
    }

    public double getRadius() {
        return radius;
    }

    public Double getInnerRadius() {
        return innerRadius;
    }

    // Record-style accessors
    public StepEntity sweptCurve() { return sweptCurve; }
    public double radius() { return radius; }
    public Double innerRadius() { return innerRadius; }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("sweptCurve", sweptCurve);
        state.put("radius", radius);
        state.put("innerRadius", innerRadius);
        return state;
    }
}
