package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal SHAPE_ASPECT_OCCURRENCE metadata.
 *
 * @param id STEP instance id
 * @param name aspect name
 * @param description aspect description
 * @param ofShape owning product definition shape
 * @param productDefinitional LOGICAL value encoded as T, F or U
 * @param definition occurrence definition
 * @param entityName concrete STEP entity name
 */
public final class StepShapeAspectOccurrence extends AbstractStepEntity {
    private final String description;
    private final StepProductDefinitionShape ofShape;
    private final String productDefinitional;
    private final StepEntity definition;
    private final String entityName;

    public StepShapeAspectOccurrence(int id, String name, String description, StepProductDefinitionShape ofShape, String productDefinitional, StepEntity definition, String entityName) {
        super(id, name);
        this.description = description;
        this.ofShape = ofShape;
        this.productDefinitional = productDefinitional;
        this.definition = definition;
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

    public StepEntity getDefinition() {
        return definition;
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

    public StepEntity definition() {
        return definition;
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
        state.put("definition", definition);
        state.put("entityName", entityName);
        return state;
    }
}
