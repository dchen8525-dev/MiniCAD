package com.minicad.step.model;

import java.util.Map;

/**
 * Resolved QUALIFIED_REPRESENTATION_ITEM.
 * A representation item that has been qualified with additional tolerance or geometric information.
 */
public final class StepQualifiedRepresentationItem extends AbstractStepEntity {
    private final StepEntity qualifiedItem;

    public StepQualifiedRepresentationItem(int id, String name, StepEntity qualifiedItem) {
        super(id, name);
        this.qualifiedItem = qualifiedItem;
    }

    public StepEntity getQualifiedItem() {
        return qualifiedItem;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "qualifiedItem", qualifiedItem);
    }
}
