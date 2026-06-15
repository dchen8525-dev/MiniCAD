package com.minicad.step.model.resource;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal CONTRACT_TYPE metadata.
 *
 * @param id STEP instance id
 * @param description type description
 */
/**
 * Minimal CONTRACT_TYPE metadata.
 *
 * @param id STEP instance id
 * @param description type description
 */
public final class StepContractType implements StepEntity {
    private final int id;
    private final String description;

    public StepContractType(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return description != null ? description : "";
    }

    // Record-style accessor - name from description
    public String name() {
        return description;
    }

    public String kind() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepContractType that = (StepContractType) o;
        return id == that.id && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description);
    }

    @Override
    public String toString() {
        return "StepContractType{" + "id=" + id + "description=" + description + "}";
    }
}
