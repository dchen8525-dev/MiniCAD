package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved CONICAL_SURFACE.
 *
 * @param id step id
 * @param name step label
 * @param position surface placement
 * @param radius radius at placement origin
 * @param semiAngle semi-angle in radians
 */
public final class StepConicalSurface extends AbstractStepEntity {
    private final StepAxis2Placement3D position;
    private final double radius;
    private final double semiAngle;

    public StepConicalSurface(int id, String name, StepAxis2Placement3D position, double radius, double semiAngle) {
        super(id, name);
        this.position = position;
        this.radius = radius;
        this.semiAngle = semiAngle;
    }

    public StepAxis2Placement3D getPosition() {
        return position;
    }

    public double getRadius() {
        return radius;
    }

    public double getSemiAngle() {
        return semiAngle;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepAxis2Placement3D position() { return getPosition(); }
    public double radius() { return getRadius(); }
    public double semiAngle() { return getSemiAngle(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("position", position);
        state.put("radius", radius);
        state.put("semiAngle", semiAngle);
        return state;
    }
}
