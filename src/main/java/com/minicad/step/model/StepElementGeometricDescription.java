package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved ELEMENT_GEOMETRIC_DESCRIPTION.
 * Geometric description of a finite element.
 */
/**
 * Resolved ELEMENT_GEOMETRIC_DESCRIPTION.
 * Geometric description of a finite element.
 */
public final class StepElementGeometricDescription implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepEntity elementVolume;

    public StepElementGeometricDescription(int id, String name, String description, StepEntity elementVolume) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.elementVolume = elementVolume;
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

    public StepEntity getElementVolume() {
        return elementVolume;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepElementGeometricDescription that = (StepElementGeometricDescription) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(elementVolume, that.elementVolume);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, elementVolume);
    }

    @Override
    public String toString() {
        return "StepElementGeometricDescription{" + "id=" + id + "name=" + name + "description=" + description + "elementVolume=" + elementVolume + "}";
    }
}
