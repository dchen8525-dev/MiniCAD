package com.minicad.step.model;

import java.util.Map;

/**
 * Minimal ORGANIZATION_ROLE metadata.
 *
 * @param id STEP instance id
 * @param name role label
 */
public final class StepOrganizationRole extends AbstractStepEntity {
    public StepOrganizationRole(int id, String name) {
        super(id, name);
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName());
    }
}
