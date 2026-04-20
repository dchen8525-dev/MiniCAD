package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved SURFACE_CURVE_SWEPT_AREA_SOLID.
 * A swept solid where the trajectory follows a surface curve.
 *
 * @param id STEP instance id
 * @param name solid name
 * @param sweptArea profile to sweep
 * @param referenceSurface surface the trajectory follows
 * @param trajectory path curve
 * @param startPoint start parameter
 * @param endPoint end parameter
 */
public record StepSurfaceCurveSweptAreaSolid(
    int id,
    String name,
    StepEntity sweptArea,
    StepEntity referenceSurface,
    StepEntity trajectory,
    double startPoint,
    double endPoint) implements StepEntity {
}
