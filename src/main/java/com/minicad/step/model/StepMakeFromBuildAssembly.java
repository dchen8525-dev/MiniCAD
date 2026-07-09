package com.minicad.step.model.product;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved MAKE_FROM_BUILD_ASSEMBLY.
 * Manufacturing assembly definition.
 */
/**
 * Resolved MAKE_FROM_BUILD_ASSEMBLY.
 * Manufacturing assembly definition.
 */
public final class StepMakeFromBuildAssembly implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepEntity assembly;

    public StepMakeFromBuildAssembly(int id, String name, String description, StepEntity assembly) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.assembly = assembly;
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

    public StepEntity getAssembly() {
        return assembly;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepMakeFromBuildAssembly that = (StepMakeFromBuildAssembly) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(assembly, that.assembly);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, assembly);
    }

    @Override
    public String toString() {
        return "StepMakeFromBuildAssembly{" + "id=" + id + "name=" + name + "description=" + description + "assembly=" + assembly + "}";
    }
}
