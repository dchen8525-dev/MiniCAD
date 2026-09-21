package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved B_SPLINE_SURFACE_WITH_KNOTS_AND_BREAKPOINTS.
 * A B-spline surface with explicit knot and breakpoint information.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param uDegree polynomial degree in U direction
 * @param vDegree polynomial degree in V direction
 * @param controlPoints control point entities (grid)
 * @param uKnotMultiplicities knot multiplicity values in U direction
 * @param vKnotMultiplicities knot multiplicity values in V direction
 * @param uKnots knot values in U direction
 * @param vKnots knot values in V direction
 * @param uBreakpoints breakpoint parameter values in U direction
 * @param vBreakpoints breakpoint parameter values in V direction
 * @param surfaceForm surface form indicator
 * @param uClosed whether the surface is closed in U direction
 * @param vClosed whether the surface is closed in V direction
 * @param selfIntersect whether the surface self-intersects
 */
public final class StepBSplineSurfaceWithKnotsAndBreakpoints extends AbstractStepControlPointSurface {
    private final List<Integer> uKnotMultiplicities;
    private final List<Integer> vKnotMultiplicities;
    private final List<Double> uKnots;
    private final List<Double> vKnots;
    private final List<Double> uBreakpoints;
    private final List<Double> vBreakpoints;
    private final boolean uClosed;
    private final boolean vClosed;
    private final boolean selfIntersect;

    public StepBSplineSurfaceWithKnotsAndBreakpoints(int id, String name, int uDegree, int vDegree, List<List<StepCartesianPoint>> controlPoints, List<Integer> uKnotMultiplicities, List<Integer> vKnotMultiplicities, List<Double> uKnots, List<Double> vKnots, List<Double> uBreakpoints, List<Double> vBreakpoints, String surfaceForm, boolean uClosed, boolean vClosed, boolean selfIntersect) {
        super(id, name, uDegree, vDegree, controlPoints, surfaceForm);
        this.uKnotMultiplicities = uKnotMultiplicities == null ? null : java.util.List.copyOf(uKnotMultiplicities);
        this.vKnotMultiplicities = vKnotMultiplicities == null ? null : java.util.List.copyOf(vKnotMultiplicities);
        this.uKnots = uKnots == null ? null : java.util.List.copyOf(uKnots);
        this.vKnots = vKnots == null ? null : java.util.List.copyOf(vKnots);
        this.uBreakpoints = uBreakpoints == null ? null : java.util.List.copyOf(uBreakpoints);
        this.vBreakpoints = vBreakpoints == null ? null : java.util.List.copyOf(vBreakpoints);
        this.uClosed = uClosed;
        this.vClosed = vClosed;
        this.selfIntersect = selfIntersect;
    }

    public List<Integer> getUKnotMultiplicities() {
        return uKnotMultiplicities;
    }

    public List<Integer> getVKnotMultiplicities() {
        return vKnotMultiplicities;
    }

    public List<Double> getUKnots() {
        return uKnots;
    }

    public List<Double> getVKnots() {
        return vKnots;
    }

    public List<Double> getUBreakpoints() {
        return uBreakpoints;
    }

    public List<Double> getVBreakpoints() {
        return vBreakpoints;
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

    // Record-style accessors
    public List<Integer> uKnotMultiplicities() { return getUKnotMultiplicities(); }
    public List<Integer> vKnotMultiplicities() { return getVKnotMultiplicities(); }
    public List<Double> uKnots() { return getUKnots(); }
    public List<Double> vKnots() { return getVKnots(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("uDegree", getUDegree());
        state.put("vDegree", getVDegree());
        state.put("controlPoints", getControlPoints());
        state.put("uKnotMultiplicities", uKnotMultiplicities);
        state.put("vKnotMultiplicities", vKnotMultiplicities);
        state.put("uKnots", uKnots);
        state.put("vKnots", vKnots);
        state.put("uBreakpoints", uBreakpoints);
        state.put("vBreakpoints", vBreakpoints);
        state.put("surfaceForm", getSurfaceForm());
        state.put("uClosed", uClosed);
        state.put("vClosed", vClosed);
        state.put("selfIntersect", selfIntersect);
        return state;
    }
}