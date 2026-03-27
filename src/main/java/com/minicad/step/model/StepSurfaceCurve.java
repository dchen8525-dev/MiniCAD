package com.minicad.step.model;

import java.util.List;

/**
 * Resolved SURFACE_CURVE with supported 3D curve geometry.
 *
 * @param id step id
 * @param name step label
 * @param curve3d referenced 3D curve
 * @param associatedGeometry associated PCURVE or surface-geometry items
 * @param masterRepresentation preferred representation enum
 */
public record StepSurfaceCurve(
        int id,
        String name,
        StepEntity curve3d,
        List<StepEntity> associatedGeometry,
        String masterRepresentation
) implements StepEntity {

    public StepSurfaceCurve {
        associatedGeometry = List.copyOf(associatedGeometry);
    }
}
