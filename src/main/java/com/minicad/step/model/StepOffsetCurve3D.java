package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

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
public final class StepOffsetCurve3D extends AbstractStepEntity {
    private final StepEntity basisCurve;
    private final double distance;
    private final boolean selfIntersect;
    private final StepDirection refDirection;

    public StepOffsetCurve3D(int id, String name, StepEntity basisCurve, double distance, boolean selfIntersect, StepDirection refDirection) {
        super(id, name);
        this.basisCurve = basisCurve;
        this.distance = distance;
        this.selfIntersect = selfIntersect;
        this.refDirection = refDirection;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("basisCurve", basisCurve);
        state.put("distance", distance);
        state.put("selfIntersect", selfIntersect);
        state.put("refDirection", refDirection);
        return state;
    }
}
