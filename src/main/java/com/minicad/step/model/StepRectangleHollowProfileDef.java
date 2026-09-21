package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved RECTANGLE_HOLLOW_PROFILE_DEF.
 * A rectangular hollow cross-section profile.
 *
 * @param id STEP instance id
 * @param name profile name
 * @param position placement for the profile
 * @param xDim outer width
 * @param yDim outer height
 * @param wallThickness wall thickness
 * @param innerRadius inner corner radius (0 if sharp)
 */
public final class StepRectangleHollowProfileDef extends AbstractStepEntity {
    private final StepAxis2Placement2D position;
    private final double xDim;
    private final double yDim;
    private final double wallThickness;
    private final double innerRadius;

    public StepRectangleHollowProfileDef(int id, String name, StepAxis2Placement2D position, double xDim, double yDim, double wallThickness, double innerRadius) {
        super(id, name);
        this.position = position;
        this.xDim = xDim;
        this.yDim = yDim;
        this.wallThickness = wallThickness;
        this.innerRadius = innerRadius;
    }

    public StepAxis2Placement2D getPosition() {
        return position;
    }

    public double getXDim() {
        return xDim;
    }

    public double getYDim() {
        return yDim;
    }

    public double getWallThickness() {
        return wallThickness;
    }

    public double getInnerRadius() {
        return innerRadius;
    }

    // Record-style accessors
    public double xDim() { return xDim; }
    public double yDim() { return yDim; }
    public double wallThickness() { return wallThickness; }
    public double innerRadius() { return innerRadius; }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("position", position);
        state.put("xDim", xDim);
        state.put("yDim", yDim);
        state.put("wallThickness", wallThickness);
        state.put("innerRadius", innerRadius);
        return state;
    }
}
