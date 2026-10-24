package com.minicad.step.model;

import java.util.Map;

/**
 * Resolved COMPOSITE_SHAPE_ASPECT.
 */
public final class StepCompositeShapeAspect extends AbstractStepEntity {
    private final String description;
    private final StepEntity ofShape;
    private final boolean productDefinitional;

    public StepCompositeShapeAspect(int id, String name, String description, StepEntity ofShape, boolean productDefinitional) {
        super(id, name);
        this.description = description;
        this.ofShape = ofShape;
        this.productDefinitional = productDefinitional;
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
        return getName();
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
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "description", description, "ofShape", ofShape,
                "productDefinitional", productDefinitional);
    }
}
