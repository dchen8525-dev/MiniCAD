package com.minicad.step.model.technical.tolerance;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved QUALIFIED_REPRESENTATION_ITEM.
 * A representation item that has been qualified with additional tolerance or geometric information.
 */
/**
 * Resolved QUALIFIED_REPRESENTATION_ITEM.
 * A representation item that has been qualified with additional tolerance or geometric information.
 */
public final class StepQualifiedRepresentationItem implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity qualifiedItem;

    public StepQualifiedRepresentationItem(int id, String name, StepEntity qualifiedItem) {
        this.id = id;
        this.name = name;
        this.qualifiedItem = qualifiedItem;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getQualifiedItem() {
        return qualifiedItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepQualifiedRepresentationItem that = (StepQualifiedRepresentationItem) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(qualifiedItem, that.qualifiedItem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, qualifiedItem);
    }

    @Override
    public String toString() {
        return "StepQualifiedRepresentationItem{" + "id=" + id + "name=" + name + "qualifiedItem=" + qualifiedItem + "}";
    }
}
