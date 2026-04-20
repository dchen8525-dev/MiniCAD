package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal shape representation relationship.
 *
 * @param id STEP instance id
 * @param name relationship name
 * @param description optional description
 * @param rep1 relating representation
 * @param rep2 related representation
 */
public record StepShapeRepresentationRelationship(
        int id,
        String name,
        String description,
        StepRepresentation rep1,
        StepRepresentation rep2
) implements StepEntity {
}
