package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal OFFSET_CURVE_2D parse-only curve.
 *
 * @param id STEP instance id
 * @param name curve name
 * @param basisCurve curve being offset
 * @param distance offset distance
 * @param selfIntersect self-intersection flag
 */
/**
 * Minimal OFFSET_CURVE_2D parse-only curve.
 *
 * @param id STEP instance id
 * @param name curve name
 * @param basisCurve curve being offset
 * @param distance offset distance
 * @param selfIntersect self-intersection flag
 */
public final class StepOffsetCurve2D implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity basisCurve;
    private final double distance;
    private final boolean selfIntersect;

    public StepOffsetCurve2D(int id, String name, StepEntity basisCurve, double distance, boolean selfIntersect) {
        this.id = id;
        this.name = name;
        this.basisCurve = basisCurve;
        this.distance = distance;
        this.selfIntersect = selfIntersect;
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

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity basisCurve() { return getBasisCurve(); }
    public double distance() { return getDistance(); }
    public boolean selfIntersect() { return isSelfIntersect(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepOffsetCurve2D that = (StepOffsetCurve2D) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(basisCurve, that.basisCurve) && distance == that.distance && selfIntersect == that.selfIntersect;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, basisCurve, distance, selfIntersect);
    }

    @Override
    public String toString() {
        return "StepOffsetCurve2D{" + "id=" + id + "name=" + name + "basisCurve=" + basisCurve + "distance=" + distance + "selfIntersect=" + selfIntersect + "}";
    }
}
