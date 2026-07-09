package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved RESTORE_DEFINITION.
 * A restore definition entity.
 *
 * @param id STEP instance id
 * @param name restore name
 * @param restoreType restore variance type
 * @param restoreSource restore variance source backup reference
 * @param restoreTarget restore variance target reference
 * @param restoreOptions restore variance options
 * @param restoreStatus restore variance status
 */
/**
 * Resolved RESTORE_DEFINITION.
 * A restore definition entity.
 *
 * @param id STEP instance id
 * @param name restore name
 * @param restoreType restore variance type
 * @param restoreSource restore variance source backup reference
 * @param restoreTarget restore variance target reference
 * @param restoreOptions restore variance options
 * @param restoreStatus restore variance status
 */
public final class StepRestoreDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String restoreType;
    private final StepEntity restoreSource;
    private final StepEntity restoreTarget;
    private final List<String> restoreOptions;
    private final String restoreStatus;

    public StepRestoreDefinition(int id, String name, String restoreType, StepEntity restoreSource, StepEntity restoreTarget, List<String> restoreOptions, String restoreStatus) {
        this.id = id;
        this.name = name;
        this.restoreType = restoreType;
        this.restoreSource = restoreSource;
        this.restoreTarget = restoreTarget;
        this.restoreOptions = restoreOptions == null ? null : java.util.List.copyOf(restoreOptions);
        this.restoreStatus = restoreStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRestoreType() {
        return restoreType;
    }

    public StepEntity getRestoreSource() {
        return restoreSource;
    }

    public StepEntity getRestoreTarget() {
        return restoreTarget;
    }

    public List<String> getRestoreOptions() {
        return restoreOptions;
    }

    public String getRestoreStatus() {
        return restoreStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepRestoreDefinition that = (StepRestoreDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(restoreType, that.restoreType) && Objects.equals(restoreSource, that.restoreSource) && Objects.equals(restoreTarget, that.restoreTarget) && Objects.equals(restoreOptions, that.restoreOptions) && Objects.equals(restoreStatus, that.restoreStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, restoreType, restoreSource, restoreTarget, restoreOptions, restoreStatus);
    }

    @Override
    public String toString() {
        return "StepRestoreDefinition{" + "id=" + id + "name=" + name + "restoreType=" + restoreType + "restoreSource=" + restoreSource + "restoreTarget=" + restoreTarget + "restoreOptions=" + restoreOptions + "restoreStatus=" + restoreStatus + "}";
    }
}