package com.minicad.step.model;

import java.util.Map;

/**
 * Minimal TESSELLATED_TRIANGLE.
 * A single triangle in a tessellated face.
 *
 * @param id STEP id
 * @param vertices the three vertices of the triangle
 */
public final class StepTessellatedTriangle extends AbstractStepEntity {
    private final StepEntity vertex1;
    private final StepEntity vertex2;
    private final StepEntity vertex3;

    public StepTessellatedTriangle(int id, String name, StepEntity vertex1, StepEntity vertex2, StepEntity vertex3) {
        super(id, name);
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.vertex3 = vertex3;
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
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "vertex1", vertex1, "vertex2", vertex2, "vertex3", vertex3);
    }
}
