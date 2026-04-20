package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

import com.minicad.step.model.annotation.StepPresentationStyleAssignment;

/**
 * Minimal projection curve occurrence.
 *
 * @param id STEP instance id
 * @param name occurrence name
 * @param styles style assignments
 * @param item projected curve geometry
 */
public record StepProjectionCurve(
        int id,
        String name,
        List<StepPresentationStyleAssignment> styles,
        StepEntity item
) implements StepEntity {

    public StepProjectionCurve {
        styles = List.copyOf(styles);
    }
}
