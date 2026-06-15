package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved OFFSET_CURVE_3D.
 *
 * @param id step id
 * @param name step label
 * @param basisCurve basis curve
 * @param distance offset distance
 * @param selfIntersect self-intersection flag
 * @param refDirection reference direction
 */
/**
 * Resolved OFFSET_CURVE_3D.
 *
 * @param id step id
 * @param name step label
 * @param basisCurve basis curve
 * @param distance offset distance
 * @param selfIntersect self-intersection flag
 * @param refDirection reference direction
 */
public final class StepOffsetCurve3D implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity basisCurve;
    private final double distance;
    private final boolean selfIntersect;
    private final StepDirection refDirection;

    public StepOffsetCurve3D(int id, String name, StepEntity basisCurve, double distance, boolean selfIntersect, StepDirection refDirection) {
        this.id = id;
        this.name = name;
        this.basisCurve = basisCurve;
        this.distance = distance;
        this.selfIntersect = selfIntersect;
        this.refDirection = refDirection;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getBasisCurve() {
        return basisCurve;
    }

    public double getDistance() {
        return distance;
    }

    public boolean isSelfIntersect() {
        return selfIntersect;
    }

    public StepDirection getRefDirection() {
        return refDirection;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity basisCurve() { return getBasisCurve(); }
    public double distance() { return getDistance(); }
    public boolean selfIntersect() { return isSelfIntersect(); }
    public StepDirection refDirection() { return getRefDirection(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepOffsetCurve3D that = (StepOffsetCurve3D) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(basisCurve, that.basisCurve) && distance == that.distance && selfIntersect == that.selfIntersect && Objects.equals(refDirection, that.refDirection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, basisCurve, distance, selfIntersect, refDirection);
    }

    @Override
    public String toString() {
        return "StepOffsetCurve3D{" + "id=" + id + "name=" + name + "basisCurve=" + basisCurve + "distance=" + distance + "selfIntersect=" + selfIntersect + "refDirection=" + refDirection + "}";
    }
}
