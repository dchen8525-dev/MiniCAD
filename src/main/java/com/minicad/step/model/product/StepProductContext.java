package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;

import com.minicad.step.model.document.StepApplicationContext;
/**
 * Minimal product context.
 *
 * @param id STEP instance id
 * @param name context name
 * @param disciplineType discipline type
 * @param frameOfReference referenced application context
 * @param entityName concrete STEP entity name
 */
public record StepProductContext(
        int id,
        String name,
        String disciplineType,
        StepApplicationContext frameOfReference,
        String entityName
) implements StepEntity {

    @Override
    public String name() {
        return name;
    }
}
