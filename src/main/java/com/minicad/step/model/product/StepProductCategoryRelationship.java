package com.minicad.step.model.product;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Minimal PRODUCT_CATEGORY_RELATIONSHIP metadata.
 *
 * @param id STEP instance id
 * @param name relationship name
 * @param description relationship description
 * @param category parent category
 * @param subCategory child category
 */
/**
 * Minimal PRODUCT_CATEGORY_RELATIONSHIP metadata.
 *
 * @param id STEP instance id
 * @param name relationship name
 * @param description relationship description
 * @param category parent category
 * @param subCategory child category
 */
public final class StepProductCategoryRelationship implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepProductCategory category;
    private final StepProductCategory subCategory;

    public StepProductCategoryRelationship(int id, String name, String description, StepProductCategory category, StepProductCategory subCategory) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.subCategory = subCategory;
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

    public StepProductCategory getCategory() {
        return category;
    }

    public StepProductCategory getSubCategory() {
        return subCategory;
    }

    // Record-style accessors
    public StepProductCategory category() {
        return category;
    }

    public StepProductCategory subCategory() {
        return subCategory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepProductCategoryRelationship that = (StepProductCategoryRelationship) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(category, that.category) && Objects.equals(subCategory, that.subCategory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, category, subCategory);
    }

    @Override
    public String toString() {
        return "StepProductCategoryRelationship{" + "id=" + id + "name=" + name + "description=" + description + "category=" + category + "subCategory=" + subCategory + "}";
    }
}
