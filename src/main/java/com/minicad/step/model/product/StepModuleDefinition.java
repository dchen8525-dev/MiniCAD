package com.minicad.step.model.product;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved MODULE_DEFINITION.
 * A module definition entity.
 *
 * @param id STEP instance id
 * @param name module name
 * @param moduleType module variance type
 * @param moduleDescription module variance description
 * @param moduleComponents module variance components
 * @param moduleInterfaces module variance interfaces
 * @param moduleStatus module variance status
 */
/**
 * Resolved MODULE_DEFINITION.
 * A module definition entity.
 *
 * @param id STEP instance id
 * @param name module name
 * @param moduleType module variance type
 * @param moduleDescription module variance description
 * @param moduleComponents module variance components
 * @param moduleInterfaces module variance interfaces
 * @param moduleStatus module variance status
 */
public final class StepModuleDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String moduleType;
    private final String moduleDescription;
    private final List<StepEntity> moduleComponents;
    private final List<StepEntity> moduleInterfaces;
    private final String moduleStatus;

    public StepModuleDefinition(int id, String name, String moduleType, String moduleDescription, List<StepEntity> moduleComponents, List<StepEntity> moduleInterfaces, String moduleStatus) {
        this.id = id;
        this.name = name;
        this.moduleType = moduleType;
        this.moduleDescription = moduleDescription;
        this.moduleComponents = moduleComponents == null ? null : java.util.List.copyOf(moduleComponents);
        this.moduleInterfaces = moduleInterfaces == null ? null : java.util.List.copyOf(moduleInterfaces);
        this.moduleStatus = moduleStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getModuleType() {
        return moduleType;
    }

    public String getModuleDescription() {
        return moduleDescription;
    }

    public List<StepEntity> getModuleComponents() {
        return moduleComponents;
    }

    public List<StepEntity> getModuleInterfaces() {
        return moduleInterfaces;
    }

    public String getModuleStatus() {
        return moduleStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepModuleDefinition that = (StepModuleDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(moduleType, that.moduleType) && Objects.equals(moduleDescription, that.moduleDescription) && Objects.equals(moduleComponents, that.moduleComponents) && Objects.equals(moduleInterfaces, that.moduleInterfaces) && Objects.equals(moduleStatus, that.moduleStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, moduleType, moduleDescription, moduleComponents, moduleInterfaces, moduleStatus);
    }

    @Override
    public String toString() {
        return "StepModuleDefinition{" + "id=" + id + "name=" + name + "moduleType=" + moduleType + "moduleDescription=" + moduleDescription + "moduleComponents=" + moduleComponents + "moduleInterfaces=" + moduleInterfaces + "moduleStatus=" + moduleStatus + "}";
    }
}