package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved TOROIDAL_SURFACE_WITH_CYLINDRICAL_AXIS.
 * A toroidal surface where the axis is defined by a cylindrical axis placement.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param position axis placement
 * @param majorRadius major radius of the torus
 * @param minorRadius minor radius of the torus
 */
public final class StepToroidalSurfaceWithCylindricalAxis extends AbstractStepEntity {
    private final StepAxis1Placement position;
    private final double majorRadius;
    private final double minorRadius;

    public StepToroidalSurfaceWithCylindricalAxis(int id, String name, StepAxis1Placement position, double majorRadius, double minorRadius) {
        super(id, name);
        this.position = position;
        this.majorRadius = majorRadius;
        this.minorRadius = minorRadius;
    }

    public StepAxis1Placement getPosition() {
        return position;
    }

    public double getMajorRadius() {
        return majorRadius;
    }

    public double getMinorRadius() {
        return minorRadius;
    }

    // Record-style accessors
    public StepAxis1Placement position() { return getPosition(); }
    public double majorRadius() { return getMajorRadius(); }
    public double minorRadius() { return getMinorRadius(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("position", position);
        state.put("majorRadius", majorRadius);
        state.put("minorRadius", minorRadius);
        return state;
    }
}
