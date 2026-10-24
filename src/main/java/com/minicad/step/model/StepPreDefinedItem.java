package com.minicad.step.model;

import java.util.Map;

/**
 * Minimal PRE_DEFINED_ITEM.
 *
 * @param id step id
 * @param name predefined item name
 */
public final class StepPreDefinedItem extends AbstractStepEntity {
    public StepPreDefinedItem(int id, String name) {
        super(id, name);
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName());
    }
}
