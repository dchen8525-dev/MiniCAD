package com.minicad.step.model.classification;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal NAME_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedName assigned name
 */
public record StepNameAssignment(
        int id,
        String assignedName
) implements StepEntity {

    @Override
    public String name() {
        return assignedName;
    }
}
