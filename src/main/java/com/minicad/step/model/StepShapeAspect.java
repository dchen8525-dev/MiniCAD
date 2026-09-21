package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal shape aspect.
 *
 * @param id STEP instance id
 * @param name aspect name
 * @param description aspect description
 * @param ofShape owning product definition shape
 * @param productDefinitional STEP LOGICAL value as text
 * @param entityName concrete STEP entity name
 */
public final class StepShapeAspect extends AbstractStepEntity {
    private final String description;
    private final StepProductDefinitionShape ofShape;
    private final String productDefinitional;
    private final String entityName;

    public StepShapeAspect(int id, String name, String description, StepProductDefinitionShape ofShape, String productDefinitional, String entityName) {
        super(id, name);
        this.description = description;
        this.ofShape = ofShape;
        this.productDefinitional = productDefinitional;
        this.entityName = entityName;
    }

    public String getDescription() {
        return description;
    }

    public StepProductDefinitionShape getOfShape() {
        return ofShape;
    }

    public String getProductDefinitional() {
        return productDefinitional;
    }

    public String getEntityName() {
        return entityName;
    }

    // Record-style accessors
    public String name() {
        return getName();
    }

    public String description() {
        return description;
    }

    public StepProductDefinitionShape ofShape() {
        return ofShape;
    }

    public String productDefinitional() {
        return productDefinitional;
    }

    public String entityName() {
        return entityName;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("ofShape", ofShape);
        state.put("productDefinitional", productDefinitional);
        state.put("entityName", entityName);
        return state;
    }
}
