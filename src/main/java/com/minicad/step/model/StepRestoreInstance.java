package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved RESTORE_INSTANCE.
 * A restore instance entity.
 *
 * @param id STEP instance id
 * @param name restore instance name
 * @param restoreDefinition restore variance definition reference
 * @param restoreStartTime restore variance start time
 * @param restoreEndTime restore variance end time
 * @param restoreResult restore variance result
 * @param restoreValid restore variance valid flag
 * @param restoreStatus restore variance status
 */
/**
 * Resolved RESTORE_INSTANCE.
 * A restore instance entity.
 *
 * @param id STEP instance id
 * @param name restore instance name
 * @param restoreDefinition restore variance definition reference
 * @param restoreStartTime restore variance start time
 * @param restoreEndTime restore variance end time
 * @param restoreResult restore variance result
 * @param restoreValid restore variance valid flag
 * @param restoreStatus restore variance status
 */
public final class StepRestoreInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity restoreDefinition;
    private final StepEntity restoreStartTime;
    private final StepEntity restoreEndTime;
    private final String restoreResult;
    private final boolean restoreValid;
    private final String restoreStatus;

    public StepRestoreInstance(int id, String name, StepEntity restoreDefinition, StepEntity restoreStartTime, StepEntity restoreEndTime, String restoreResult, boolean restoreValid, String restoreStatus) {
        this.id = id;
        this.name = name;
        this.restoreDefinition = restoreDefinition;
        this.restoreStartTime = restoreStartTime;
        this.restoreEndTime = restoreEndTime;
        this.restoreResult = restoreResult;
        this.restoreValid = restoreValid;
        this.restoreStatus = restoreStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getRestoreDefinition() {
        return restoreDefinition;
    }

    public StepEntity getRestoreStartTime() {
        return restoreStartTime;
    }

    public StepEntity getRestoreEndTime() {
        return restoreEndTime;
    }

    public String getRestoreResult() {
        return restoreResult;
    }

    public boolean isRestoreValid() {
        return restoreValid;
    }

    public String getRestoreStatus() {
        return restoreStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepRestoreInstance that = (StepRestoreInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(restoreDefinition, that.restoreDefinition) && Objects.equals(restoreStartTime, that.restoreStartTime) && Objects.equals(restoreEndTime, that.restoreEndTime) && Objects.equals(restoreResult, that.restoreResult) && restoreValid == that.restoreValid && Objects.equals(restoreStatus, that.restoreStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, restoreDefinition, restoreStartTime, restoreEndTime, restoreResult, restoreValid, restoreStatus);
    }

    @Override
    public String toString() {
        return "StepRestoreInstance{" + "id=" + id + "name=" + name + "restoreDefinition=" + restoreDefinition + "restoreStartTime=" + restoreStartTime + "restoreEndTime=" + restoreEndTime + "restoreResult=" + restoreResult + "restoreValid=" + restoreValid + "restoreStatus=" + restoreStatus + "}";
    }
}