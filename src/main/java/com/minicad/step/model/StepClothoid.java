package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved CLOTHOID (Euler spiral / transition curve).
 *
 * @param id STEP instance id
 * @param name clothoid name
 * @param position placement defining the clothoid's local coordinate system
 * @param xAxisIntercept x-coordinate where the clothoid intersects the x-axis
 * @param curvature curvature parameter of the clothoid
 */
public final class StepClothoid extends AbstractStepEntity {
    private final StepEntity position;
    private final double xAxisIntercept;
    private final double curvature;

    public StepClothoid(int id, String name, StepEntity position, double xAxisIntercept, double curvature) {
        super(id, name);
        this.position = position;
        this.xAxisIntercept = xAxisIntercept;
        this.curvature = curvature;
    }

    public StepEntity getPosition() {
        return position;
    }

    public double getXAxisIntercept() {
        return xAxisIntercept;
    }

    public double getCurvature() {
        return curvature;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity position() { return getPosition(); }
    public double xAxisIntercept() { return getXAxisIntercept(); }
    public double curvature() { return getCurvature(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("position", position);
        state.put("xAxisIntercept", xAxisIntercept);
        state.put("curvature", curvature);
        return state;
    }
}
