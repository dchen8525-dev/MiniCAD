package com.minicad.step.model;

import java.util.Map;

/**
 * Minimal OFFSET_CURVE_2D parse-only curve.
 *
 * @param id STEP instance id
 * @param name curve name
 * @param basisCurve curve being offset
 * @param distance offset distance
 * @param selfIntersect self-intersection flag
 */
public final class StepOffsetCurve2D extends AbstractStepEntity {
    private final StepEntity basisCurve;
    private final double distance;
    private final boolean selfIntersect;

    public StepOffsetCurve2D(int id, String name, StepEntity basisCurve, double distance, boolean selfIntersect) {
        super(id, name);
        this.basisCurve = basisCurve;
        this.distance = distance;
        this.selfIntersect = selfIntersect;
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
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "basisCurve", basisCurve, "distance", distance,
                "selfIntersect", selfIntersect);
    }
}
