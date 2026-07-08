package com.minicad.step.model.core.base;

import java.util.List;
import java.util.Objects;

/**
 * Resolved WITH_DESCRIPTIVE_REPRESENTATION_ITEM.
 * A representation that includes descriptive text items.
 *
 * @param id STEP instance id
 * @param name representation name
 * @param description descriptive text
 * @param items representation items
 * @param context representation context
 */
/**
 * Resolved WITH_DESCRIPTIVE_REPRESENTATION_ITEM.
 * A representation that includes descriptive text items.
 *
 * @param id STEP instance id
 * @param name representation name
 * @param description descriptive text
 * @param items representation items
 * @param context representation context
 */
public final class StepWithDescriptiveRepresentationItem implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final List<StepEntity> items;
    private final StepEntity context;

    public StepWithDescriptiveRepresentationItem(int id, String name, String description, List<StepEntity> items, StepEntity context) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.items = items == null ? null : java.util.List.copyOf(items);
        this.context = context;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<StepEntity> getItems() {
        return items;
    }

    public StepEntity getContext() {
        return context;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepWithDescriptiveRepresentationItem that = (StepWithDescriptiveRepresentationItem) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(items, that.items) && Objects.equals(context, that.context);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, items, context);
    }

    @Override
    public String toString() {
        return "StepWithDescriptiveRepresentationItem{" + "id=" + id + "name=" + name + "description=" + description + "items=" + items + "context=" + context + "}";
    }
}
