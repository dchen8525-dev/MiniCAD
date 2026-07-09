package com.minicad.step.model.workflow;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved MODULE_INSTANCE.
 * A module instance entity.
 *
 * @param id STEP instance id
 * @param name module instance name
 * @param moduleDefinition module variance definition reference
 * @param moduleState module variance state
 * @param moduleVersion module variance version
 * @param moduleConfig module variance configuration
 * @param moduleStatus module variance status
 */
/**
 * Resolved MODULE_INSTANCE.
 * A module instance entity.
 *
 * @param id STEP instance id
 * @param name module instance name
 * @param moduleDefinition module variance definition reference
 * @param moduleState module variance state
 * @param moduleVersion module variance version
 * @param moduleConfig module variance configuration
 * @param moduleStatus module variance status
 */
public final class StepModuleInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity moduleDefinition;
    private final String moduleState;
    private final String moduleVersion;
    private final List<String> moduleConfig;
    private final String moduleStatus;

    public StepModuleInstance(int id, String name, StepEntity moduleDefinition, String moduleState, String moduleVersion, List<String> moduleConfig, String moduleStatus) {
        this.id = id;
        this.name = name;
        this.moduleDefinition = moduleDefinition;
        this.moduleState = moduleState;
        this.moduleVersion = moduleVersion;
        this.moduleConfig = moduleConfig == null ? null : java.util.List.copyOf(moduleConfig);
        this.moduleStatus = moduleStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getModuleDefinition() {
        return moduleDefinition;
    }

    public String getModuleState() {
        return moduleState;
    }

    public String getModuleVersion() {
        return moduleVersion;
    }

    public List<String> getModuleConfig() {
        return moduleConfig;
    }

    public String getModuleStatus() {
        return moduleStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepModuleInstance that = (StepModuleInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(moduleDefinition, that.moduleDefinition) && Objects.equals(moduleState, that.moduleState) && Objects.equals(moduleVersion, that.moduleVersion) && Objects.equals(moduleConfig, that.moduleConfig) && Objects.equals(moduleStatus, that.moduleStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, moduleDefinition, moduleState, moduleVersion, moduleConfig, moduleStatus);
    }

    @Override
    public String toString() {
        return "StepModuleInstance{" + "id=" + id + "name=" + name + "moduleDefinition=" + moduleDefinition + "moduleState=" + moduleState + "moduleVersion=" + moduleVersion + "moduleConfig=" + moduleConfig + "moduleStatus=" + moduleStatus + "}";
    }
}