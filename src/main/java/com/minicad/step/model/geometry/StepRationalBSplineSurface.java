package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Minimal rational B-spline surface.
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
 * @param weightsData rational weights grid
 * @param uMultiplicities optional U multiplicities
 * @param vMultiplicities optional V multiplicities
 * @param uKnots optional U knot values
 * @param vKnots optional V knot values
 * @param knotSpec optional knot-spec enum
 */
public record StepRationalBSplineSurface(
        int id,
        String name,
        int uDegree,
        int vDegree,
        List<List<StepCartesianPoint>> controlPoints,
        String surfaceForm,
        boolean uClosed,
        boolean vClosed,
        boolean selfIntersect,
        List<List<Double>> weightsData,
        List<Integer> uMultiplicities,
        List<Integer> vMultiplicities,
        List<Double> uKnots,
        List<Double> vKnots,
        String knotSpec
) implements StepEntity {

    public StepRationalBSplineSurface {
        controlPoints = controlPoints.stream().map(List::copyOf).toList();
        weightsData = weightsData.stream().map(List::copyOf).toList();
        uMultiplicities = List.copyOf(uMultiplicities);
        vMultiplicities = List.copyOf(vMultiplicities);
        uKnots = List.copyOf(uKnots);
        vKnots = List.copyOf(vKnots);
    }
}
