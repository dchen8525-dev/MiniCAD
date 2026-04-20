package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved QUASI_UNIFORM_SURFACE marker with inherited B-spline data when present.
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
public record StepQuasiUniformSurface(
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

    public StepQuasiUniformSurface {
        controlPoints = controlPoints.stream().map(List::copyOf).toList();
    }

    public StepQuasiUniformSurface(int id, String name) {
        this(id, name, -1, -1, List.of(), "", false, false, false);
    }
}
