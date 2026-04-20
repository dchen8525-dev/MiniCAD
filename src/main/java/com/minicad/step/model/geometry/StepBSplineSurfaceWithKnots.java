package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved non-rational B_SPLINE_SURFACE_WITH_KNOTS.
 *
 * @param id STEP id
 * @param name STEP label
 * @param uDegree U degree
 * @param vDegree V degree
 * @param controlPoints control-point grid indexed as [u][v]
 * @param surfaceForm surface form enum
 * @param uClosed U closed flag
 * @param vClosed V closed flag
 * @param selfIntersect self-intersection flag
 * @param uMultiplicities U multiplicities
 * @param vMultiplicities V multiplicities
 * @param uKnots unique U knots
 * @param vKnots unique V knots
 * @param knotSpec knot-spec enum
 */
public record StepBSplineSurfaceWithKnots(
        int id,
        String name,
        int uDegree,
        int vDegree,
        List<List<StepCartesianPoint>> controlPoints,
        String surfaceForm,
        boolean uClosed,
        boolean vClosed,
        boolean selfIntersect,
        List<Integer> uMultiplicities,
        List<Integer> vMultiplicities,
        List<Double> uKnots,
        List<Double> vKnots,
        String knotSpec
) implements StepEntity {

    public StepBSplineSurfaceWithKnots {
        controlPoints = controlPoints.stream().map(List::copyOf).toList();
        uMultiplicities = List.copyOf(uMultiplicities);
        vMultiplicities = List.copyOf(vMultiplicities);
        uKnots = List.copyOf(uKnots);
        vKnots = List.copyOf(vKnots);
    }
}
