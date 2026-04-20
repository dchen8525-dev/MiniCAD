package com.minicad.step.model.topology;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved SUBEDGE.
 *
 * @param id STEP id
 * @param name STEP label
 * @param start start vertex
 * @param end end vertex
 * @param parentEdge parent edge or subedge
 */
public record StepSubedge(
        int id,
        String name,
        StepEntity start,
        StepEntity end,
        StepEntity parentEdge
) implements StepEntity {
}
