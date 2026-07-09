package com.minicad.step.model.product;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepAssemblyProcessPlan implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> items;
    private final StepEntity context;
    private final List<StepEntity> assemblySequence;

    public StepAssemblyProcessPlan(int id, String name, List<StepEntity> items, StepEntity context, List<StepEntity> assemblySequence) {
        this.id = id;
        this.name = name;
        this.items = items == null ? null : java.util.List.copyOf(items);
        this.context = context;
        this.assemblySequence = assemblySequence == null ? null : java.util.List.copyOf(assemblySequence);
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

    public List<StepEntity> getAssemblySequence() {
        return assemblySequence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAssemblyProcessPlan that = (StepAssemblyProcessPlan) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(items, that.items) && Objects.equals(context, that.context) && Objects.equals(assemblySequence, that.assemblySequence);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, items, context, assemblySequence);
    }

    @Override
    public String toString() {
        return "StepAssemblyProcessPlan{" + "id=" + id + "name=" + name + "items=" + items + "context=" + context + "assemblySequence=" + assemblySequence + "}";
    }
}