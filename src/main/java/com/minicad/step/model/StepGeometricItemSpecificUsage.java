package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Minimal semantic PMI link from a callout to a geometric item.
 *
 * @param id STEP instance id
 * @param name usage name
 * @param description usage description
 * @param usage source PMI item
 * @param identifiedItem referenced geometric item
 */
/**
 * Minimal semantic PMI link from a callout to a geometric item.
 *
 * @param id STEP instance id
 * @param name usage name
 * @param description usage description
 * @param usage source PMI item
 * @param identifiedItem referenced geometric item
 */
public final class StepGeometricItemSpecificUsage implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepEntity usage;
    private final StepEntity identifiedItem;

    public StepGeometricItemSpecificUsage(int id, String name, String description, StepEntity usage, StepEntity identifiedItem) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.identifiedItem = identifiedItem;
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

    public StepEntity getUsage() {
        return usage;
    }

    public StepEntity getIdentifiedItem() {
        return identifiedItem;
    }

    // Record-style accessors
    public StepEntity usage() {
        return usage;
    }

    public StepEntity identifiedItem() {
        return identifiedItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepGeometricItemSpecificUsage that = (StepGeometricItemSpecificUsage) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(usage, that.usage) && Objects.equals(identifiedItem, that.identifiedItem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, usage, identifiedItem);
    }

    @Override
    public String toString() {
        return "StepGeometricItemSpecificUsage{" + "id=" + id + "name=" + name + "description=" + description + "usage=" + usage + "identifiedItem=" + identifiedItem + "}";
    }
}
