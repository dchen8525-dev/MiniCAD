package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal DEGENERATE_TOROIDAL_SURFACE parse-only surface.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param position surface placement
 * @param majorRadius major radius
 * @param minorRadius minor radius
 * @param selectOuter selected torus side flag
 */
/**
 * Minimal DEGENERATE_TOROIDAL_SURFACE parse-only surface.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param position surface placement
 * @param majorRadius major radius
 * @param minorRadius minor radius
 * @param selectOuter selected torus side flag
 */
public final class StepDegenerateToroidalSurface implements StepEntity {
    private final int id;
    private final String name;
    private final StepAxis2Placement3D position;
    private final double majorRadius;
    private final double minorRadius;
    private final boolean selectOuter;

    public StepDegenerateToroidalSurface(int id, String name, StepAxis2Placement3D position, double majorRadius, double minorRadius, boolean selectOuter) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.majorRadius = majorRadius;
        this.minorRadius = minorRadius;
        this.selectOuter = selectOuter;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepAxis2Placement3D getPosition() {
        return position;
    }

    public double getMajorRadius() {
        return majorRadius;
    }

    public double getMinorRadius() {
        return minorRadius;
    }

    public boolean isSelectOuter() {
        return selectOuter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDegenerateToroidalSurface that = (StepDegenerateToroidalSurface) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(position, that.position) && majorRadius == that.majorRadius && minorRadius == that.minorRadius && selectOuter == that.selectOuter;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, position, majorRadius, minorRadius, selectOuter);
    }

    @Override
    public String toString() {
        return "StepDegenerateToroidalSurface{" + "id=" + id + "name=" + name + "position=" + position + "majorRadius=" + majorRadius + "minorRadius=" + minorRadius + "selectOuter=" + selectOuter + "}";
    }
}
