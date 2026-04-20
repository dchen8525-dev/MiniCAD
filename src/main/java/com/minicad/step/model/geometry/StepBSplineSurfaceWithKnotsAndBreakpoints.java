package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepBSplineSurfaceWithKnotsAndBreakpoints(
    int id,
    String name,
    int uDegree,
    int vDegree,
    List<List<StepCartesianPoint>> controlPoints,
    List<Integer> uKnotMultiplicities,
    List<Integer> vKnotMultiplicities,
    List<Double> uKnots,
    List<Double> vKnots,
    List<Double> uBreakpoints,
    List<Double> vBreakpoints,
    String surfaceForm,
    boolean uClosed,
    boolean vClosed,
    boolean selfIntersect) implements StepEntity {
}