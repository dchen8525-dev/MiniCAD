package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Minimal representation or shape representation.
 *
 * @param id STEP instance id
 * @param name representation name
 * @param items referenced items
 * @param context representation context
 * @param shapeRepresentation whether this entity originated from SHAPE_REPRESENTATION
 * @param entityName concrete STEP entity name
 */
public final class StepRepresentation extends AbstractStepEntity {
    private final List<StepEntity> items;
    private final StepEntity context;
    private final boolean shapeRepresentation;
    private final String entityName;

    public StepRepresentation(int id, String name, List<StepEntity> items, StepEntity context, boolean shapeRepresentation, String entityName) {
        super(id, name);
        this.items = items == null ? null : java.util.List.copyOf(items);
        this.context = context;
        this.shapeRepresentation = shapeRepresentation;
        this.entityName = entityName;
    }

    public List<StepEntity> getItems() {
        return items;
    }

    public StepEntity getContext() {
        return context;
    }

    public boolean isShapeRepresentation() {
        return shapeRepresentation;
    }

    public String getEntityName() {
        return entityName;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public List<StepEntity> items() { return getItems(); }
    public StepEntity context() { return getContext(); }
    public boolean shapeRepresentation() { return isShapeRepresentation(); }
    public String entityName() { return getEntityName(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("items", items);
        state.put("context", context);
        state.put("shapeRepresentation", shapeRepresentation);
        state.put("entityName", entityName);
        return state;
    }
}
