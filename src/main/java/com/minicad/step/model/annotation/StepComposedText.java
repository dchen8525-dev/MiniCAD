package com.minicad.step.model.annotation;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved COMPOSED_TEXT.
 * Composed text with extent information.
 *
 * @param id STEP instance id
 * @param name text name
 * @param collection collection of text elements
 * @param extent bounding extent
 */
/**
 * Resolved COMPOSED_TEXT.
 * Composed text with extent information.
 *
 * @param id STEP instance id
 * @param name text name
 * @param collection collection of text elements
 * @param extent bounding extent
 */
public final class StepComposedText implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> collection;
    private final StepEntity extent;

    public StepComposedText(int id, String name, List<StepEntity> collection, StepEntity extent) {
        this.id = id;
        this.name = name;
        this.collection = collection == null ? null : java.util.List.copyOf(collection);
        this.extent = extent;
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

    public StepEntity getExtent() {
        return extent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepComposedText that = (StepComposedText) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(collection, that.collection) && Objects.equals(extent, that.extent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, collection, extent);
    }

    @Override
    public String toString() {
        return "StepComposedText{" + "id=" + id + "name=" + name + "collection=" + collection + "extent=" + extent + "}";
    }
}
