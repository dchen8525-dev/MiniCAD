package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved ORDINATE_DIMENSION_REPRESENTATION.
 * An ordinate dimension representation entity.
 *
 * @param id STEP instance id
 * * @param name representation name
 * @param items representation items
 * * @param context representation context
 * @param ordinateOrigin ordinate origin point
 * @param ordinateDirection ordinate direction
 */
public final class StepOrdinateDimensionRepresentation extends AbstractStepEntity {
    private final List<StepEntity> items;
    private final StepEntity context;
    private final StepEntity ordinateOrigin;
    private final StepEntity ordinateDirection;

    public StepOrdinateDimensionRepresentation(int id, String name, List<StepEntity> items, StepEntity context, StepEntity ordinateOrigin, StepEntity ordinateDirection) {
        super(id, name);
        this.items = items == null ? null : java.util.List.copyOf(items);
        this.context = context;
        this.ordinateOrigin = ordinateOrigin;
        this.ordinateDirection = ordinateDirection;
    }

    public List<StepEntity> getItems() {
        return items;
    }

    public StepEntity getContext() {
        return context;
    }

    public StepEntity getOrdinateOrigin() {
        return ordinateOrigin;
    }

    public StepEntity getOrdinateDirection() {
        return ordinateDirection;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("items", items);
        state.put("context", context);
        state.put("ordinateOrigin", ordinateOrigin);
        state.put("ordinateDirection", ordinateDirection);
        return state;
    }
}
