package com.minicad.step.model.classification;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;

/**
 * Resolved COMPOSITE_SHAPE_ASPECT.
 */
/**
 * Resolved COMPOSITE_SHAPE_ASPECT.
 */
public final class StepCompositeShapeAspect implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepEntity ofShape;
    private final boolean productDefinitional;

    public StepCompositeShapeAspect(int id, String name, String description, StepEntity ofShape, boolean productDefinitional) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ofShape = ofShape;
        this.productDefinitional = productDefinitional;
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

    public StepEntity getOfShape() {
        return ofShape;
    }

    public boolean isProductDefinitional() {
        return productDefinitional;
    }

    // Record-style accessors
    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public StepEntity ofShape() {
        return ofShape;
    }

    public boolean productDefinitional() {
        return productDefinitional;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCompositeShapeAspect that = (StepCompositeShapeAspect) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(ofShape, that.ofShape) && productDefinitional == that.productDefinitional;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, ofShape, productDefinitional);
    }

    @Override
    public String toString() {
        return "StepCompositeShapeAspect{" + "id=" + id + "name=" + name + "description=" + description + "ofShape=" + ofShape + "productDefinitional=" + productDefinitional + "}";
    }
}
