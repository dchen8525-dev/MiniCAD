package com.minicad.step.model.classification;

import com.minicad.step.model.base.StepEntity;

import com.minicad.step.model.product.StepProductDefinitionShape;
import java.util.Objects;
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
public final class StepShapeAspect implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepProductDefinitionShape ofShape;
    private final String productDefinitional;
    private final String entityName;

    public StepShapeAspect(int id, String name, String description, StepProductDefinitionShape ofShape, String productDefinitional, String entityName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ofShape = ofShape;
        this.productDefinitional = productDefinitional;
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

    public String getEntityName() {
        return entityName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepShapeAspect that = (StepShapeAspect) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(ofShape, that.ofShape) && Objects.equals(productDefinitional, that.productDefinitional) && Objects.equals(entityName, that.entityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, ofShape, productDefinitional, entityName);
    }

    @Override
    public String toString() {
        return "StepShapeAspect{" + "id=" + id + "name=" + name + "description=" + description + "ofShape=" + ofShape + "productDefinitional=" + productDefinitional + "entityName=" + entityName + "}";
    }
}
