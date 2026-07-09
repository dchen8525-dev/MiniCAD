package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved COMPOSITE_TEXT.
 * Text composed of multiple text literals and text paths.
 *
 * @param id STEP instance id
 * @param name text name
 * @param collection collection of text elements
 */
/**
 * Resolved COMPOSITE_TEXT.
 * Text composed of multiple text literals and text paths.
 *
 * @param id STEP instance id
 * @param name text name
 * @param collection collection of text elements
 */
public final class StepCompositeText implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> collection;

    public StepCompositeText(int id, String name, List<StepEntity> collection) {
        this.id = id;
        this.name = name;
        this.collection = collection == null ? null : java.util.List.copyOf(collection);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StepEntity> getCollection() {
        return collection;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCompositeText that = (StepCompositeText) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(collection, that.collection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, collection);
    }

    @Override
    public String toString() {
        return "StepCompositeText{" + "id=" + id + "name=" + name + "collection=" + collection + "}";
    }
}
