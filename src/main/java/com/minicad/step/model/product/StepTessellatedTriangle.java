package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal TESSELLATED_TRIANGLE.
 * A single triangle in a tessellated face.
 *
 * @param id STEP id
 * @param vertices the three vertices of the triangle
 */
/**
 * Minimal TESSELLATED_TRIANGLE.
 * A single triangle in a tessellated face.
 *
 * @param id STEP id
 * @param vertices the three vertices of the triangle
 */
public final class StepTessellatedTriangle implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity vertex1;
    private final StepEntity vertex2;
    private final StepEntity vertex3;

    public StepTessellatedTriangle(int id, String name, StepEntity vertex1, StepEntity vertex2, StepEntity vertex3) {
        this.id = id;
        this.name = name;
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.vertex3 = vertex3;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getVertex1() {
        return vertex1;
    }

    public StepEntity getVertex2() {
        return vertex2;
    }

    public StepEntity getVertex3() {
        return vertex3;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity vertex1() { return getVertex1(); }
    public StepEntity vertex2() { return getVertex2(); }
    public StepEntity vertex3() { return getVertex3(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTessellatedTriangle that = (StepTessellatedTriangle) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(vertex1, that.vertex1) && Objects.equals(vertex2, that.vertex2) && Objects.equals(vertex3, that.vertex3);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, vertex1, vertex2, vertex3);
    }

    @Override
    public String toString() {
        return "StepTessellatedTriangle{" + "id=" + id + "name=" + name + "vertex1=" + vertex1 + "vertex2=" + vertex2 + "vertex3=" + vertex3 + "}";
    }
}
