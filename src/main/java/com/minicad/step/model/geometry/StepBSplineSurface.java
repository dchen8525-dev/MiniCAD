package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Minimal B_SPLINE_SURFACE without knot data.
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
 */
public record StepBSplineSurface(
        int id,
        String name,
        int uDegree,
        int vDegree,
        List<List<StepCartesianPoint>> controlPoints,
        String surfaceForm,
        boolean uClosed,
        boolean vClosed,
        boolean selfIntersect
) implements StepEntity {

    public StepBSplineSurface {
        controlPoints = controlPoints.stream().map(List::copyOf).toList();
    }
}
