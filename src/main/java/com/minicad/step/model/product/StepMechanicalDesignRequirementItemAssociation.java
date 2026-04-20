package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;

import com.minicad.step.model.workflow.StepRepresentation;
/**
 * Minimal mechanical design requirement item association.
 *
 * @param id STEP instance id
 * @param name usage name
 * @param description usage description
 * @param definition usage definition/select target
 * @param usedRepresentation representation carrying the item
 * @param identifiedItem identified item reference
 * @param requirement requirement object
 */
public record StepMechanicalDesignRequirementItemAssociation(
        int id,
        String name,
        String description,
        StepEntity definition,
        StepRepresentation usedRepresentation,
        StepEntity identifiedItem,
        StepEntity requirement
) implements StepEntity {
}
