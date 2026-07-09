package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Minimal PRODUCT_CATEGORY metadata.
 *
 * @param id STEP instance id
 * @param name category name
 * @param description category description
 */
/**
 * Minimal PRODUCT_CATEGORY metadata.
 *
 * @param id STEP instance id
 * @param name category name
 * @param description category description
 */
public final class StepProductCategory implements StepEntity {
    private final int id;
    private final String name;
    private final String description;

    public StepProductCategory(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepProductCategory that = (StepProductCategory) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description);
    }

    @Override
    public String toString() {
        return "StepProductCategory{" + "id=" + id + "name=" + name + "description=" + description + "}";
    }
}
