package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved MACHINING_PROCESS_PLAN.
 * A machining process plan representation entity.
 *
 * @param id STEP instance id
 * @param name representation name
 * @param items representation items (process steps)
 * @param context representation context
 * @param operations machining operations sequence
 */
public final class StepMachiningProcessPlan extends AbstractStepEntity {
    private final List<StepEntity> items;
    private final StepEntity context;
    private final List<StepEntity> operations;

    public StepMachiningProcessPlan(int id, String name, List<StepEntity> items, StepEntity context, List<StepEntity> operations) {
        super(id, name);
        this.items = items == null ? null : java.util.List.copyOf(items);
        this.context = context;
        this.operations = operations == null ? null : java.util.List.copyOf(operations);
    }

    public List<StepEntity> getItems() {
        return items;
    }

    public StepEntity getContext() {
        return context;
    }

    public List<StepEntity> getOperations() {
        return operations;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("items", items);
        state.put("context", context);
        state.put("operations", operations);
        return state;
    }
}
