package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved ASSEMBLY_PROCESS_PLAN.
 * An assembly process plan representation entity.
 *
 * @param id STEP instance id
 * @param name representation name
 * @param items representation items (assembly steps)
 * @param context representation context
 * @param assemblySequence assembly sequence operations
 */
public final class StepAssemblyProcessPlan extends AbstractStepEntity {
    private final List<StepEntity> items;
    private final StepEntity context;
    private final List<StepEntity> assemblySequence;

    public StepAssemblyProcessPlan(int id, String name, List<StepEntity> items, StepEntity context, List<StepEntity> assemblySequence) {
        super(id, name);
        this.items = items == null ? null : java.util.List.copyOf(items);
        this.context = context;
        this.assemblySequence = assemblySequence == null ? null : java.util.List.copyOf(assemblySequence);
    }

    public List<StepEntity> getItems() {
        return items;
    }

    public StepEntity getContext() {
        return context;
    }

    public List<StepEntity> getAssemblySequence() {
        return assemblySequence;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("items", items);
        state.put("context", context);
        state.put("assemblySequence", assemblySequence);
        return state;
    }
}
