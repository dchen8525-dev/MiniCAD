package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved BACKUP_INSTANCE.
 * A backup instance entity.
 *
 * @param id STEP instance id
 * @param name backup instance name
 * @param backupDefinition backup variance definition reference
 * @param backupStartTime backup variance start time
 * @param backupEndTime backup variance end time
 * @param backupSize backup variance size
 * @param backupValid backup variance valid flag
 * @param backupStatus backup variance status
 */
/**
 * Resolved BACKUP_INSTANCE.
 * A backup instance entity.
 *
 * @param id STEP instance id
 * @param name backup instance name
 * @param backupDefinition backup variance definition reference
 * @param backupStartTime backup variance start time
 * @param backupEndTime backup variance end time
 * @param backupSize backup variance size
 * @param backupValid backup variance valid flag
 * @param backupStatus backup variance status
 */
public final class StepBackupInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity backupDefinition;
    private final StepEntity backupStartTime;
    private final StepEntity backupEndTime;
    private final long backupSize;
    private final boolean backupValid;
    private final String backupStatus;

    public StepBackupInstance(int id, String name, StepEntity backupDefinition, StepEntity backupStartTime, StepEntity backupEndTime, long backupSize, boolean backupValid, String backupStatus) {
        this.id = id;
        this.name = name;
        this.backupDefinition = backupDefinition;
        this.backupStartTime = backupStartTime;
        this.backupEndTime = backupEndTime;
        this.backupSize = backupSize;
        this.backupValid = backupValid;
        this.backupStatus = backupStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getBackupDefinition() {
        return backupDefinition;
    }

    public StepEntity getBackupStartTime() {
        return backupStartTime;
    }

    public StepEntity getBackupEndTime() {
        return backupEndTime;
    }

    public long getBackupSize() {
        return backupSize;
    }

    public boolean isBackupValid() {
        return backupValid;
    }

    public String getBackupStatus() {
        return backupStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepBackupInstance that = (StepBackupInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(backupDefinition, that.backupDefinition) && Objects.equals(backupStartTime, that.backupStartTime) && Objects.equals(backupEndTime, that.backupEndTime) && backupSize == that.backupSize && backupValid == that.backupValid && Objects.equals(backupStatus, that.backupStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, backupDefinition, backupStartTime, backupEndTime, backupSize, backupValid, backupStatus);
    }

    @Override
    public String toString() {
        return "StepBackupInstance{" + "id=" + id + "name=" + name + "backupDefinition=" + backupDefinition + "backupStartTime=" + backupStartTime + "backupEndTime=" + backupEndTime + "backupSize=" + backupSize + "backupValid=" + backupValid + "backupStatus=" + backupStatus + "}";
    }
}