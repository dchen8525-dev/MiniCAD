package com.minicad.step.model.config_mgmt;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved START_WORK.
 * A start work record in AP203 configuration management.
 */
/**
 * Resolved START_WORK.
 * A start work record in AP203 configuration management.
 */
public final class StepStartWork implements StepEntity {
    private final int id;
    private final String name;
    private final String description;

    public StepStartWork(int id, String name, String description) {
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
        StepStartWork that = (StepStartWork) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description);
    }

    @Override
    public String toString() {
        return "StepStartWork{" + "id=" + id + "name=" + name + "description=" + description + "}";
    }
}
