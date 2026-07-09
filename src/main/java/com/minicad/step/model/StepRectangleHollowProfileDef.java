package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;

import com.minicad.step.model.geometry.StepAxis2Placement2D;
import java.util.Objects;
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
public final class StepRectangleHollowProfileDef implements StepEntity {
    private final int id;
    private final String name;
    private final StepAxis2Placement2D position;
    private final double xDim;
    private final double yDim;
    private final double wallThickness;
    private final double innerRadius;

    public StepRectangleHollowProfileDef(int id, String name, StepAxis2Placement2D position, double xDim, double yDim, double wallThickness, double innerRadius) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.xDim = xDim;
        this.yDim = yDim;
        this.wallThickness = wallThickness;
        this.innerRadius = innerRadius;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepRectangleHollowProfileDef that = (StepRectangleHollowProfileDef) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(position, that.position) && xDim == that.xDim && yDim == that.yDim && wallThickness == that.wallThickness && innerRadius == that.innerRadius;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, position, xDim, yDim, wallThickness, innerRadius);
    }

    @Override
    public String toString() {
        return "StepRectangleHollowProfileDef{" + "id=" + id + "name=" + name + "position=" + position + "xDim=" + xDim + "yDim=" + yDim + "wallThickness=" + wallThickness + "innerRadius=" + innerRadius + "}";
    }
}
