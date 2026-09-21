package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved COMPOUND_REPRESENTATION_ITEM.
 *
 * @param id STEP instance id
 * @param name entity label
 * @param items list of representation items
 * @param entityName actual entity type name (for subtype handling)
 */
public final class StepCompoundRepresentationItem extends AbstractStepEntity {
    private final List<StepEntity> items;
    private final String entityName;

    public StepCompoundRepresentationItem(int id, String name, List<StepEntity> items, String entityName) {
        super(id, name);
        this.items = items == null ? null : java.util.List.copyOf(items);
        this.entityName = entityName;
    }

    public List<StepEntity> getItems() {
        return items;
    }

    public String getEntityName() {
        return entityName;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public List<StepEntity> items() { return items; }
    public String entityName() { return entityName; }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("items", items);
        state.put("entityName", entityName);
        return state;
    }
}
