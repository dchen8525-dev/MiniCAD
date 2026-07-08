package com.minicad.step.model.fea;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved SURFACE_3D_ELEMENT_PROPERTY.
 * Properties associated with 3D surface/shell elements.
 */
/**
 * Resolved SURFACE_3D_ELEMENT_PROPERTY.
 * Properties associated with 3D surface/shell elements.
 */
public final class StepSurface3dElementProperty implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity element;
    private final StepEntity property;
    private final StepEntity material;

    public StepSurface3dElementProperty(int id, String name, StepEntity element, StepEntity property, StepEntity material) {
        this.id = id;
        this.name = name;
        this.element = element;
        this.property = property;
        this.material = material;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getElement() {
        return element;
    }

    public StepEntity getProperty() {
        return property;
    }

    public StepEntity getMaterial() {
        return material;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSurface3dElementProperty that = (StepSurface3dElementProperty) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(element, that.element) && Objects.equals(property, that.property) && Objects.equals(material, that.material);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, element, property, material);
    }

    @Override
    public String toString() {
        return "StepSurface3dElementProperty{" + "id=" + id + "name=" + name + "element=" + element + "property=" + property + "material=" + material + "}";
    }
}
