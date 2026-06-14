package com.minicad.step.model.expression;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;

/**
 * SIMPLE_GENERIC_EXPRESSION entity model.
 * Simple generic expression (literal or simple value).
 *
 * @param id STEP instance id
 * @param name entity label
 * @param entityName actual entity type name (for subtype handling)
 */
public final class StepSimpleGenericExpression implements StepEntity {
    private final int id;
    private final String name;
    private final String entityName;

    public StepSimpleGenericExpression(
        int id,
        String name,
        String entityName) {
        this.id = id;
        this.name = name;
        this.entityName = entityName;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getEntityName() {
        return entityName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSimpleGenericExpression that = (StepSimpleGenericExpression) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return "StepSimpleGenericExpression{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", entityName='" + entityName + '\'' +
            '}';
    }
}