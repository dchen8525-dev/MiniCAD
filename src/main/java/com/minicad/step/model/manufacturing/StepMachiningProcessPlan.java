package com.minicad.step.model.manufacturing;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepMachiningProcessPlan implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> items;
    private final StepEntity context;
    private final List<StepEntity> operations;

    public StepMachiningProcessPlan(int id, String name, List<StepEntity> items, StepEntity context, List<StepEntity> operations) {
        this.id = id;
        this.name = name;
        this.items = items == null ? null : java.util.List.copyOf(items);
        this.context = context;
        this.operations = operations == null ? null : java.util.List.copyOf(operations);
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

    public List<StepEntity> getOperations() {
        return operations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepMachiningProcessPlan that = (StepMachiningProcessPlan) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(items, that.items) && Objects.equals(context, that.context) && Objects.equals(operations, that.operations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, items, context, operations);
    }

    @Override
    public String toString() {
        return "StepMachiningProcessPlan{" + "id=" + id + "name=" + name + "items=" + items + "context=" + context + "operations=" + operations + "}";
    }
}