package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved MACHINING_WORK_PLAN.
 * A machining work plan representation entity.
 *
 * @param id STEP instance id
 * @param name representation name
 * @param items representation items (work steps)
 * @param context representation context
 * @param machiningSetup machining setup operations
 */
public final class StepMachiningWorkPlan extends AbstractStepEntity {
    private final List<StepEntity> items;
    private final StepEntity context;
    private final List<StepEntity> machiningSetup;

    public StepMachiningWorkPlan(int id, String name, List<StepEntity> items, StepEntity context, List<StepEntity> machiningSetup) {
        super(id, name);
        this.items = items == null ? null : java.util.List.copyOf(items);
        this.context = context;
        this.machiningSetup = machiningSetup == null ? null : java.util.List.copyOf(machiningSetup);
    }

    public List<StepEntity> getItems() {
        return items;
    }

    public StepEntity getContext() {
        return context;
    }

    public List<StepEntity> getMachiningSetup() {
        return machiningSetup;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("items", items);
        state.put("context", context);
        state.put("machiningSetup", machiningSetup);
        return state;
    }
}
