package com.minicad.step.model.date_time;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal DATE_ROLE metadata.
 *
 * @param id STEP instance id
 * @param name role label
 */
public record StepDateRole(
        int id,
        String name
) implements StepEntity {
}
