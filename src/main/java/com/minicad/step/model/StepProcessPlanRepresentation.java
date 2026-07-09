package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepProcessPlanRepresentation implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> items;
    private final StepEntity context;
    private final List<StepEntity> processSteps;

    public StepProcessPlanRepresentation(int id, String name, List<StepEntity> items, StepEntity context, List<StepEntity> processSteps) {
        this.id = id;
        this.name = name;
        this.items = items == null ? null : java.util.List.copyOf(items);
        this.context = context;
        this.processSteps = processSteps == null ? null : java.util.List.copyOf(processSteps);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepProcessPlanRepresentation that = (StepProcessPlanRepresentation) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(items, that.items) && Objects.equals(context, that.context) && Objects.equals(processSteps, that.processSteps);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, items, context, processSteps);
    }

    @Override
    public String toString() {
        return "StepProcessPlanRepresentation{" + "id=" + id + "name=" + name + "items=" + items + "context=" + context + "processSteps=" + processSteps + "}";
    }
}