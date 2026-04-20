package com.minicad.step.model.topology;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved FACE_BOUND or FACE_OUTER_BOUND.
 *
 * @param id step id
 * @param name step label
 * @param loop referenced loop
 * @param orientation orientation flag
 * @param outer whether this is the outer bound
 */
public record StepFaceBound(int id, String name, StepLoop loop, boolean orientation, boolean outer)
        implements StepEntity {
}
