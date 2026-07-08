package com.minicad.step.model.geometry;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepBSplineSurfaceWithKnotsAndBreakpoints implements StepEntity {
    private final int id;
    private final String name;
    private final int uDegree;
    private final int vDegree;
    private final List<List<StepCartesianPoint>> controlPoints;
    private final List<Integer> uKnotMultiplicities;
    private final List<Integer> vKnotMultiplicities;
    private final List<Double> uKnots;
    private final List<Double> vKnots;
    private final List<Double> uBreakpoints;
    private final List<Double> vBreakpoints;
    private final String surfaceForm;
    private final boolean uClosed;
    private final boolean vClosed;
    private final boolean selfIntersect;

    public StepBSplineSurfaceWithKnotsAndBreakpoints(int id, String name, int uDegree, int vDegree, List<List<StepCartesianPoint>> controlPoints, List<Integer> uKnotMultiplicities, List<Integer> vKnotMultiplicities, List<Double> uKnots, List<Double> vKnots, List<Double> uBreakpoints, List<Double> vBreakpoints, String surfaceForm, boolean uClosed, boolean vClosed, boolean selfIntersect) {
        this.id = id;
        this.name = name;
        this.uDegree = uDegree;
        this.vDegree = vDegree;
        this.controlPoints = controlPoints == null ? null : java.util.List.copyOf(controlPoints);
        this.uKnotMultiplicities = uKnotMultiplicities == null ? null : java.util.List.copyOf(uKnotMultiplicities);
        this.vKnotMultiplicities = vKnotMultiplicities == null ? null : java.util.List.copyOf(vKnotMultiplicities);
        this.uKnots = uKnots == null ? null : java.util.List.copyOf(uKnots);
        this.vKnots = vKnots == null ? null : java.util.List.copyOf(vKnots);
        this.uBreakpoints = uBreakpoints == null ? null : java.util.List.copyOf(uBreakpoints);
        this.vBreakpoints = vBreakpoints == null ? null : java.util.List.copyOf(vBreakpoints);
        this.surfaceForm = surfaceForm;
        this.uClosed = uClosed;
        this.vClosed = vClosed;
        this.selfIntersect = selfIntersect;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getUDegree() {
        return uDegree;
    }

    public int getVDegree() {
        return vDegree;
    }

    public List<List<StepCartesianPoint>> getControlPoints() {
        return controlPoints;
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

    public String getSurfaceForm() {
        return surfaceForm;
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
    public List<List<StepCartesianPoint>> controlPoints() { return getControlPoints(); }
    public int uDegree() { return getUDegree(); }
    public int vDegree() { return getVDegree(); }
    public List<Integer> uKnotMultiplicities() { return getUKnotMultiplicities(); }
    public List<Integer> vKnotMultiplicities() { return getVKnotMultiplicities(); }
    public List<Double> uKnots() { return getUKnots(); }
    public List<Double> vKnots() { return getVKnots(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepBSplineSurfaceWithKnotsAndBreakpoints that = (StepBSplineSurfaceWithKnotsAndBreakpoints) o;
        return id == that.id && Objects.equals(name, that.name) && uDegree == that.uDegree && vDegree == that.vDegree && Objects.equals(controlPoints, that.controlPoints) && Objects.equals(uKnotMultiplicities, that.uKnotMultiplicities) && Objects.equals(vKnotMultiplicities, that.vKnotMultiplicities) && Objects.equals(uKnots, that.uKnots) && Objects.equals(vKnots, that.vKnots) && Objects.equals(uBreakpoints, that.uBreakpoints) && Objects.equals(vBreakpoints, that.vBreakpoints) && Objects.equals(surfaceForm, that.surfaceForm) && uClosed == that.uClosed && vClosed == that.vClosed && selfIntersect == that.selfIntersect;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, uDegree, vDegree, controlPoints, uKnotMultiplicities, vKnotMultiplicities, uKnots, vKnots, uBreakpoints, vBreakpoints, surfaceForm, uClosed, vClosed, selfIntersect);
    }

    @Override
    public String toString() {
        return "StepBSplineSurfaceWithKnotsAndBreakpoints{" + "id=" + id + "name=" + name + "uDegree=" + uDegree + "vDegree=" + vDegree + "controlPoints=" + controlPoints + "uKnotMultiplicities=" + uKnotMultiplicities + "vKnotMultiplicities=" + vKnotMultiplicities + "uKnots=" + uKnots + "vKnots=" + vKnots + "uBreakpoints=" + uBreakpoints + "vBreakpoints=" + vBreakpoints + "surfaceForm=" + surfaceForm + "uClosed=" + uClosed + "vClosed=" + vClosed + "selfIntersect=" + selfIntersect + "}";
    }
}