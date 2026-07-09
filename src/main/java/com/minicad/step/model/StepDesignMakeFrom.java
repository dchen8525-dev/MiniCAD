package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved DESIGN_MAKE_FROM.
 * Design-to-manufacturing mapping.
 */
/**
 * Resolved DESIGN_MAKE_FROM.
 * Design-to-manufacturing mapping.
 */
public final class StepDesignMakeFrom implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepEntity design;
    private final StepEntity manufacturing;

    public StepDesignMakeFrom(int id, String name, String description, StepEntity design, StepEntity manufacturing) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.design = design;
        this.manufacturing = manufacturing;
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

    public StepEntity getDesign() {
        return design;
    }

    public StepEntity getManufacturing() {
        return manufacturing;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDesignMakeFrom that = (StepDesignMakeFrom) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(design, that.design) && Objects.equals(manufacturing, that.manufacturing);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, design, manufacturing);
    }

    @Override
    public String toString() {
        return "StepDesignMakeFrom{" + "id=" + id + "name=" + name + "description=" + description + "design=" + design + "manufacturing=" + manufacturing + "}";
    }
}
