package com.minicad.step.model.approval;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal CERTIFICATION_TYPE metadata.
 *
 * @param id STEP instance id
 * @param description type description
 */
/**
 * Minimal CERTIFICATION_TYPE metadata.
 *
 * @param id STEP instance id
 * @param description type description
 */
public final class StepCertificationType implements StepEntity {
    private final int id;
    private final String description;

    public StepCertificationType(int id, String description) {
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

    // Record-style accessor
    public String kind() {
        return description;
    }

    public String description() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCertificationType that = (StepCertificationType) o;
        return id == that.id && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description);
    }

    @Override
    public String toString() {
        return "StepCertificationType{" + "id=" + id + "description=" + description + "}";
    }
}
