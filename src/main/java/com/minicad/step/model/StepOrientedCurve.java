package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
/**
 * Minimal ORIENTED_CURVE parse-only curve wrapper.
 *
 * @param id STEP instance id
 * @param name curve name
 * @param curveElement referenced curve
 * @param orientation orientation sense
 */
/**
 * Minimal ORIENTED_CURVE parse-only curve wrapper.
 *
 * @param id STEP instance id
 * @param name curve name
 * @param curveElement referenced curve
 * @param orientation orientation sense
 */
public final class StepOrientedCurve implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity curveElement;
    private final boolean orientation;

    public StepOrientedCurve(int id, String name, StepEntity curveElement, boolean orientation) {
        this.id = id;
        this.name = name;
        this.curveElement = curveElement;
        this.orientation = orientation;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getCurveElement() {
        return curveElement;
    }

    public boolean isOrientation() {
        return orientation;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity curveElement() { return getCurveElement(); }
    public boolean orientation() { return isOrientation(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepOrientedCurve that = (StepOrientedCurve) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(curveElement, that.curveElement) && orientation == that.orientation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, curveElement, orientation);
    }

    @Override
    public String toString() {
        return "StepOrientedCurve{" + "id=" + id + "name=" + name + "curveElement=" + curveElement + "orientation=" + orientation + "}";
    }
}
