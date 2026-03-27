package com.minicad.step.model;

import java.util.List;

/**
 * Minimal resolved SEAM_CURVE.
 *
 * @param id STEP id
 * @param name STEP label
 * @param curve3d referenced 3D curve
 * @param associatedGeometry seam-associated PCURVE items
 * @param masterRepresentation preferred representation enum
 */
public record StepSeamCurve(
        int id,
        String name,
        StepEntity curve3d,
        List<StepEntity> associatedGeometry,
        String masterRepresentation
) implements StepEntity {

    public StepSeamCurve {
        associatedGeometry = List.copyOf(associatedGeometry);
    }
}
