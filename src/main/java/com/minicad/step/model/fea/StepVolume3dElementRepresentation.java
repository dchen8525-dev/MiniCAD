package com.minicad.step.model.fea;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved VOLUME_3D_ELEMENT_REPRESENTATION.
 * A representation of 3D volume finite elements.
 */
/**
 * Resolved VOLUME_3D_ELEMENT_REPRESENTATION.
 * A representation of 3D volume finite elements.
 */
public final class StepVolume3dElementRepresentation implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> elements;
    private final StepEntity mesh;

    public StepVolume3dElementRepresentation(int id, String name, List<StepEntity> elements, StepEntity mesh) {
        this.id = id;
        this.name = name;
        this.elements = elements == null ? null : java.util.List.copyOf(elements);
        this.mesh = mesh;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StepEntity> getElements() {
        return elements;
    }

    public StepEntity getMesh() {
        return mesh;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepVolume3dElementRepresentation that = (StepVolume3dElementRepresentation) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(elements, that.elements) && Objects.equals(mesh, that.mesh);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, elements, mesh);
    }

    @Override
    public String toString() {
        return "StepVolume3dElementRepresentation{" + "id=" + id + "name=" + name + "elements=" + elements + "mesh=" + mesh + "}";
    }
}
