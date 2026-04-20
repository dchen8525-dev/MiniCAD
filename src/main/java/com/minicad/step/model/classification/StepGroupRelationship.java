package com.minicad.step.model.classification;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal GROUP_RELATIONSHIP metadata.
 *
 * @param id STEP instance id
 * @param name relationship name
 * @param description relationship description
 * @param relatingGroup source group
 * @param relatedGroup target group
 * @param entityName concrete STEP entity name
 */
public record StepGroupRelationship(
        int id,
        String name,
        String description,
        StepGroup relatingGroup,
        StepGroup relatedGroup,
        String entityName
) implements StepEntity {
}
