package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved CHAIN_DIMENSION_REPRESENTATION.
 * A chain dimension representation entity.
 *
 * @param id STEP instance id
 * * @param name representation name
 * @param items representation items (chain of dimensions)
 * * @param context representation context
 * @param chainOrigin chain origin point
 */
public final class StepChainDimensionRepresentation extends AbstractStepEntity {
    private final List<StepEntity> items;
    private final StepEntity context;
    private final StepEntity chainOrigin;

    public StepChainDimensionRepresentation(int id, String name, List<StepEntity> items, StepEntity context, StepEntity chainOrigin) {
        super(id, name);
        this.items = items == null ? null : java.util.List.copyOf(items);
        this.context = context;
        this.chainOrigin = chainOrigin;
    }

    public List<StepEntity> getItems() {
        return items;
    }

    public StepEntity getContext() {
        return context;
    }

    public StepEntity getChainOrigin() {
        return chainOrigin;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("items", items);
        state.put("context", context);
        state.put("chainOrigin", chainOrigin);
        return state;
    }
}
