package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal PRODUCT_CATEGORY_RELATIONSHIP metadata.
 *
 * @param id STEP instance id
 * @param name relationship name
 * @param description relationship description
 * @param category parent category
 * @param subCategory child category
 */
public final class StepProductCategoryRelationship extends AbstractStepEntity {
    private final String description;
    private final StepProductCategory category;
    private final StepProductCategory subCategory;

    public StepProductCategoryRelationship(int id, String name, String description, StepProductCategory category, StepProductCategory subCategory) {
        super(id, name);
        this.description = description;
        this.category = category;
        this.subCategory = subCategory;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("category", category);
        state.put("subCategory", subCategory);
        return state;
    }
}
