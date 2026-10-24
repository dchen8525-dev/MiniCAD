package com.minicad.step.model;

import java.util.Map;

/**
 * Minimal LANGUAGE metadata.
 *
 * @param id STEP instance id
 * @param name language name
 */
public final class StepLanguage extends AbstractStepEntity {
    public StepLanguage(int id, String name) {
        super(id, name);
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName());
    }
}
