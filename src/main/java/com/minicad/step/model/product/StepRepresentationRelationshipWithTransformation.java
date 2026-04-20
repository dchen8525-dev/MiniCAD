package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;

import com.minicad.step.model.workflow.StepRepresentation;
/**
 * Minimal representation relationship with transformation.
 *
 * @param id STEP instance id
 * @param name relationship name
 * @param description optional description
 * @param rep1 relating representation
 * @param rep2 related representation
 * @param transformationOperator linked item-defined transformation
 */
public record StepRepresentationRelationshipWithTransformation(
        int id,
        String name,
        String description,
        StepRepresentation rep1,
        StepRepresentation rep2,
        StepItemDefinedTransformation transformationOperator
) implements StepEntity {
}
