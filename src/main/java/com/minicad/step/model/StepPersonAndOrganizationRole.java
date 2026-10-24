package com.minicad.step.model;

import java.util.Map;

/**
 * Minimal PERSON_AND_ORGANIZATION_ROLE metadata.
 *
 * @param id STEP instance id
 * @param name role label
 */
public final class StepPersonAndOrganizationRole extends AbstractStepEntity {
    public StepPersonAndOrganizationRole(int id, String name) {
        super(id, name);
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName());
    }
}
