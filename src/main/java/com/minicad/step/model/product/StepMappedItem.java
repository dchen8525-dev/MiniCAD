package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal MAPPED_ITEM.
 *
 * @param id step id
 * @param mappingSource representation map
 * @param mappingTarget target representation item
 */
/**
 * Minimal MAPPED_ITEM.
 *
 * @param id step id
 * @param mappingSource representation map
 * @param mappingTarget target representation item
 */
public final class StepMappedItem implements StepEntity {
    private final int id;
    private final StepRepresentationMap mappingSource;
    private final StepEntity mappingTarget;

    public StepMappedItem(int id, StepRepresentationMap mappingSource, StepEntity mappingTarget) {
        this.id = id;
        this.mappingSource = mappingSource;
        this.mappingTarget = mappingTarget;
    }

    public int getId() {
        return id;
    }

    public StepRepresentationMap getMappingSource() {
        return mappingSource;
    }

    public StepEntity getMappingTarget() {
        return mappingTarget;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepMappedItem that = (StepMappedItem) o;
        return id == that.id && Objects.equals(mappingSource, that.mappingSource) && Objects.equals(mappingTarget, that.mappingTarget);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mappingSource, mappingTarget);
    }

    @Override
    public String toString() {
        return "StepMappedItem{" + "id=" + id + "mappingSource=" + mappingSource + "mappingTarget=" + mappingTarget + "}";
    }
}
