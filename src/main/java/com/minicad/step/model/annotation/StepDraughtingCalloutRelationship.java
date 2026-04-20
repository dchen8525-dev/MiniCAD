package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal draughting callout relationship.
 *
 * @param id STEP instance id
 * @param name relationship name
 * @param description relationship description
 * @param relatingCallout source callout
 * @param relatedCallout target callout
 */
public record StepDraughtingCalloutRelationship(
        int id,
        String name,
        String description,
        StepDraughtingCallout relatingCallout,
        StepDraughtingCallout relatedCallout
) implements StepEntity {
}
