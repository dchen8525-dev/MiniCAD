package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;

import com.minicad.step.model.geometry.StepAxis2Placement3D;
import java.util.Objects;
/**
 * Minimal item-defined transformation between two placement items.
 *
 * @param id STEP instance id
 * @param name transformation name
 * @param description optional description
 * @param transformItem1 source placement item
 * @param transformItem2 target placement item
 */
/**
 * Minimal item-defined transformation between two placement items.
 *
 * @param id STEP instance id
 * @param name transformation name
 * @param description optional description
 * @param transformItem1 source placement item
 * @param transformItem2 target placement item
 */
public final class StepItemDefinedTransformation implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepAxis2Placement3D transformItem1;
    private final StepAxis2Placement3D transformItem2;

    public StepItemDefinedTransformation(int id, String name, String description, StepAxis2Placement3D transformItem1, StepAxis2Placement3D transformItem2) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.transformItem1 = transformItem1;
        this.transformItem2 = transformItem2;
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

    public StepAxis2Placement3D getTransformItem1() {
        return transformItem1;
    }

    public StepAxis2Placement3D getTransformItem2() {
        return transformItem2;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public String description() { return getDescription(); }
    public StepAxis2Placement3D transformItem1() { return getTransformItem1(); }
    public StepAxis2Placement3D transformItem2() { return getTransformItem2(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepItemDefinedTransformation that = (StepItemDefinedTransformation) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(transformItem1, that.transformItem1) && Objects.equals(transformItem2, that.transformItem2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, transformItem1, transformItem2);
    }

    @Override
    public String toString() {
        return "StepItemDefinedTransformation{" + "id=" + id + "name=" + name + "description=" + description + "transformItem1=" + transformItem1 + "transformItem2=" + transformItem2 + "}";
    }
}
