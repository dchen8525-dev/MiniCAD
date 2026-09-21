package com.minicad.step.model;

import java.util.LinkedHashMap;
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
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("ofShape", ofShape);
        state.put("productDefinitional", productDefinitional);
        return state;
    }
}
