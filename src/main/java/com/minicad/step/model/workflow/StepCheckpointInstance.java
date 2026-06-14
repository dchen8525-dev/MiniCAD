package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved CHECKPOINT_INSTANCE.
 * A checkpoint instance entity.
 *
 * @param id STEP instance id
 * @param name checkpoint instance name
 * @param checkpointDefinition checkpoint variance definition reference
 * @param checkpointTime checkpoint variance creation time
 * @param checkpointSize checkpoint variance size
 * @param checkpointValid checkpoint variance valid flag
 * @param checkpointStatus checkpoint variance status
 */
/**
 * Resolved CHECKPOINT_INSTANCE.
 * A checkpoint instance entity.
 *
 * @param id STEP instance id
 * @param name checkpoint instance name
 * @param checkpointDefinition checkpoint variance definition reference
 * @param checkpointTime checkpoint variance creation time
 * @param checkpointSize checkpoint variance size
 * @param checkpointValid checkpoint variance valid flag
 * @param checkpointStatus checkpoint variance status
 */
public final class StepCheckpointInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity checkpointDefinition;
    private final StepEntity checkpointTime;
    private final long checkpointSize;
    private final boolean checkpointValid;
    private final String checkpointStatus;

    public StepCheckpointInstance(int id, String name, StepEntity checkpointDefinition, StepEntity checkpointTime, long checkpointSize, boolean checkpointValid, String checkpointStatus) {
        this.id = id;
        this.name = name;
        this.checkpointDefinition = checkpointDefinition;
        this.checkpointTime = checkpointTime;
        this.checkpointSize = checkpointSize;
        this.checkpointValid = checkpointValid;
        this.checkpointStatus = checkpointStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getCheckpointDefinition() {
        return checkpointDefinition;
    }

    public StepEntity getCheckpointTime() {
        return checkpointTime;
    }

    public long getCheckpointSize() {
        return checkpointSize;
    }

    public boolean isCheckpointValid() {
        return checkpointValid;
    }

    public String getCheckpointStatus() {
        return checkpointStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCheckpointInstance that = (StepCheckpointInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(checkpointDefinition, that.checkpointDefinition) && Objects.equals(checkpointTime, that.checkpointTime) && checkpointSize == that.checkpointSize && checkpointValid == that.checkpointValid && Objects.equals(checkpointStatus, that.checkpointStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, checkpointDefinition, checkpointTime, checkpointSize, checkpointValid, checkpointStatus);
    }

    @Override
    public String toString() {
        return "StepCheckpointInstance{" + "id=" + id + "name=" + name + "checkpointDefinition=" + checkpointDefinition + "checkpointTime=" + checkpointTime + "checkpointSize=" + checkpointSize + "checkpointValid=" + checkpointValid + "checkpointStatus=" + checkpointStatus + "}";
    }
}