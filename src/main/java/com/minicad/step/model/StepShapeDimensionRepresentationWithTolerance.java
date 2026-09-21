package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved SHAPE_DIMENSION_REPRESENTATION_WITH_TOLERANCE.
 * A shape dimension representation with tolerance entity.
 *
 * @param id STEP instance id
 * @param name representation name
 * @param items representation items
 * * @param context representation context
 * @param tolerance tolerance associated with the dimension
 */
public final class StepShapeDimensionRepresentationWithTolerance extends AbstractStepEntity {
    private final List<StepEntity> items;
    private final StepEntity context;
    private final StepEntity tolerance;

    public StepShapeDimensionRepresentationWithTolerance(int id, String name, List<StepEntity> items, StepEntity context, StepEntity tolerance) {
        super(id, name);
        this.items = items == null ? null : java.util.List.copyOf(items);
        this.context = context;
        this.tolerance = tolerance;
    }

    public List<StepEntity> getItems() {
        return items;
    }

    public StepEntity getContext() {
        return context;
    }

    public StepEntity getTolerance() {
        return tolerance;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("items", items);
        state.put("context", context);
        state.put("tolerance", tolerance);
        return state;
    }
}
