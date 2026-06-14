package com.minicad.step.model.fea;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;

/**
 * Resolved FEA_TRUSS_ELEMENT_PROPERTY.
 */
/**
 * Resolved FEA_TRUSS_ELEMENT_PROPERTY.
 */
public final class StepFeaTrussElementProperty implements StepEntity {
    private final int id;
    private final String name;
    private final double area;
    private final StepEntity material;

    public StepFeaTrussElementProperty(int id, String name, double area, StepEntity material) {
        this.id = id;
        this.name = name;
        this.area = area;
        this.material = material;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getArea() {
        return area;
    }

    public StepEntity getMaterial() {
        return material;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFeaTrussElementProperty that = (StepFeaTrussElementProperty) o;
        return id == that.id && Objects.equals(name, that.name) && area == that.area && Objects.equals(material, that.material);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, area, material);
    }

    @Override
    public String toString() {
        return "StepFeaTrussElementProperty{" + "id=" + id + "name=" + name + "area=" + area + "material=" + material + "}";
    }
}
