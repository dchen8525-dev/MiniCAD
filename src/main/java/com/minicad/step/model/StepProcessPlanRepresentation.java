package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved PROCESS_PLAN_REPRESENTATION.
 * A process plan representation entity.
 *
 * @param id STEP instance id
 * @param name plan name
 * @param items representation items
 * @param context representation context
 * @param processSteps process step sequence
 */
public final class StepProcessPlanRepresentation extends AbstractStepEntity {
    private final List<StepEntity> items;
    private final StepEntity context;
    private final List<StepEntity> processSteps;

    public StepProcessPlanRepresentation(int id, String name, List<StepEntity> items, StepEntity context, List<StepEntity> processSteps) {
        super(id, name);
        this.items = items == null ? null : java.util.List.copyOf(items);
        this.context = context;
        this.processSteps = processSteps == null ? null : java.util.List.copyOf(processSteps);
    }

    public List<StepEntity> getItems() {
        return items;
    }

    public StepEntity getContext() {
        return context;
    }

    public List<StepEntity> getProcessSteps() {
        return processSteps;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("items", items);
        state.put("context", context);
        state.put("processSteps", processSteps);
        return state;
    }
}
