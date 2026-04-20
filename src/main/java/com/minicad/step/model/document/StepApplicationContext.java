package com.minicad.step.model.document;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal application context.
 *
 * @param id STEP instance id
 * @param application application domain text
 */
public record StepApplicationContext(int id, String application) implements StepEntity {

    @Override
    public String name() {
        return "";
    }
}
