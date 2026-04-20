package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved FACETTED_BREP.
 * A faceted B-rep defined by a closed shell of planar faces.
 *
 * @param id STEP id
 * @param name STEP label
 * @param outer the outer closed shell
 */
public record StepFacettedBrep(
        int id,
        String name,
        StepEntity outer
) implements StepEntity {
}
