package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Minimal SECURITY_CLASSIFICATION_LEVEL metadata.
 *
 * @param id STEP instance id
 * @param name level label
 */
/**
 * Minimal SECURITY_CLASSIFICATION_LEVEL metadata.
 *
 * @param id STEP instance id
 * @param name level label
 */
public final class StepSecurityClassificationLevel implements StepEntity {
    private final int id;
    private final String name;

    public StepSecurityClassificationLevel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // Record-style accessor
    public String securityLevel() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSecurityClassificationLevel that = (StepSecurityClassificationLevel) o;
        return id == that.id && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "StepSecurityClassificationLevel{" + "id=" + id + "name=" + name + "}";
    }
}
