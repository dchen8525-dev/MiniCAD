package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Minimal product category metadata.
 *
 * @param id STEP instance id
 * @param name category name
 * @param description category description
 * @param products categorized products
 */
/**
 * Minimal product category metadata.
 *
 * @param id STEP instance id
 * @param name category name
 * @param description category description
 * @param products categorized products
 */
public final class StepProductRelatedProductCategory implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final List<StepProduct> products;

    public StepProductRelatedProductCategory(int id, String name, String description, List<StepProduct> products) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.products = products == null ? null : java.util.List.copyOf(products);
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

    public List<StepProduct> getProducts() {
        return products;
    }

    // Record-style accessors
    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public List<StepProduct> products() {
        return products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepProductRelatedProductCategory that = (StepProductRelatedProductCategory) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(products, that.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, products);
    }

    @Override
    public String toString() {
        return "StepProductRelatedProductCategory{" + "id=" + id + "name=" + name + "description=" + description + "products=" + products + "}";
    }
}
