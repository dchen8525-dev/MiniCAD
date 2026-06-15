package com.minicad.step.model.classification;

import com.minicad.step.model.base.StepEntity;

import com.minicad.step.model.product.StepProductDefinitionShape;
import java.util.Objects;
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
public final class StepShapeAspectOccurrence implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepProductDefinitionShape ofShape;
    private final String productDefinitional;
    private final StepEntity definition;
    private final String entityName;

    public StepShapeAspectOccurrence(int id, String name, String description, StepProductDefinitionShape ofShape, String productDefinitional, StepEntity definition, String entityName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ofShape = ofShape;
        this.productDefinitional = productDefinitional;
        this.definition = definition;
        this.entityName = entityName;
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
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepShapeAspectOccurrence that = (StepShapeAspectOccurrence) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(ofShape, that.ofShape) && Objects.equals(productDefinitional, that.productDefinitional) && Objects.equals(definition, that.definition) && Objects.equals(entityName, that.entityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, ofShape, productDefinitional, definition, entityName);
    }

    @Override
    public String toString() {
        return "StepShapeAspectOccurrence{" + "id=" + id + "name=" + name + "description=" + description + "ofShape=" + ofShape + "productDefinitional=" + productDefinitional + "definition=" + definition + "entityName=" + entityName + "}";
    }
}
