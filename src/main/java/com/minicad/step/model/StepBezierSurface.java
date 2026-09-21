package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved BEZIER_SURFACE marker with inherited B-spline data when present.
 *
 * @param id STEP instance id
 * @param name inherited geometric-representation-item name when available
 * @param uDegree U degree, or {@code -1} when this is only a marker
 * @param vDegree V degree, or {@code -1} when this is only a marker
 * @param controlPoints control-point grid indexed as [u][v]
 * @param surfaceForm surface form enum
 * @param uClosed U closed flag
 * @param vClosed V closed flag
 * @param selfIntersect self-intersection flag
 */
public final class StepBezierSurface extends AbstractStepControlPointSurface {
    private final boolean uClosed;
    private final boolean vClosed;
    private final boolean selfIntersect;

    public StepBezierSurface(int id, String name, int uDegree, int vDegree, List<List<StepCartesianPoint>> controlPoints, String surfaceForm, boolean uClosed, boolean vClosed, boolean selfIntersect) {
        super(id, name, uDegree, vDegree, controlPoints, surfaceForm);
        this.uClosed = uClosed;
        this.vClosed = vClosed;
        this.selfIntersect = selfIntersect;
    }

    public boolean isUClosed() {
        return uClosed;
    }

    public boolean isVClosed() {
        return vClosed;
    }

    public boolean isSelfIntersect() {
        return selfIntersect;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("uDegree", getUDegree());
        state.put("vDegree", getVDegree());
        state.put("controlPoints", getControlPoints());
        state.put("surfaceForm", getSurfaceForm());
        state.put("uClosed", uClosed);
        state.put("vClosed", vClosed);
        state.put("selfIntersect", selfIntersect);
        return state;
    }
}
