package com.minicad.step.model.tolerance;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepChainDimensionRepresentation implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> items;
    private final StepEntity context;
    private final StepEntity chainOrigin;

    public StepChainDimensionRepresentation(int id, String name, List<StepEntity> items, StepEntity context, StepEntity chainOrigin) {
        this.id = id;
        this.name = name;
        this.items = items == null ? null : java.util.List.copyOf(items);
        this.context = context;
        this.chainOrigin = chainOrigin;
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

    public StepEntity getChainOrigin() {
        return chainOrigin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepChainDimensionRepresentation that = (StepChainDimensionRepresentation) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(items, that.items) && Objects.equals(context, that.context) && Objects.equals(chainOrigin, that.chainOrigin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, items, context, chainOrigin);
    }

    @Override
    public String toString() {
        return "StepChainDimensionRepresentation{" + "id=" + id + "name=" + name + "items=" + items + "context=" + context + "chainOrigin=" + chainOrigin + "}";
    }
}