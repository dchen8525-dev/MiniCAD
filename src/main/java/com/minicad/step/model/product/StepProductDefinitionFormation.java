package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal product definition formation.
 *
 * @param id STEP instance id
 * @param name formation name
 * @param description optional description
 * @param ofProduct referenced product
 */
/**
 * Minimal product definition formation.
 *
 * @param id STEP instance id
 * @param name formation name
 * @param description optional description
 * @param ofProduct referenced product
 */
public final class StepProductDefinitionFormation implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepProduct ofProduct;

    public StepProductDefinitionFormation(int id, String name, String description, StepProduct ofProduct) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ofProduct = ofProduct;
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

    public StepProduct getOfProduct() {
        return ofProduct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepProductDefinitionFormation that = (StepProductDefinitionFormation) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(ofProduct, that.ofProduct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, ofProduct);
    }

    @Override
    public String toString() {
        return "StepProductDefinitionFormation{" + "id=" + id + "name=" + name + "description=" + description + "ofProduct=" + ofProduct + "}";
    }
}
