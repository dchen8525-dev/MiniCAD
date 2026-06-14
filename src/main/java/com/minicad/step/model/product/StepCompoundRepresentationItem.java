package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved COMPOUND_REPRESENTATION_ITEM.
 */
/**
 * Resolved COMPOUND_REPRESENTATION_ITEM.
 */
public final class StepCompoundRepresentationItem implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> items;

    public StepCompoundRepresentationItem(int id, String name, List<StepEntity> items) {
        this.id = id;
        this.name = name;
        this.items = items == null ? null : java.util.List.copyOf(items);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCompoundRepresentationItem that = (StepCompoundRepresentationItem) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, items);
    }

    @Override
    public String toString() {
        return "StepCompoundRepresentationItem{" + "id=" + id + "name=" + name + "items=" + items + "}";
    }
}
