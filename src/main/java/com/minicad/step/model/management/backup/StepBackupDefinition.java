package com.minicad.step.model.management.backup;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved BACKUP_DEFINITION.
 * A backup definition entity.
 *
 * @param id STEP instance id
 * @param name backup name
 * @param backupType backup variance type
 * @param backupSource backup variance source reference
 * @param backupTarget backup variance target reference
 * @param backupSchedule backup variance schedule
 * @param backupRetention backup variance retention period
 * @param backupStatus backup variance status
 */
/**
 * Resolved BACKUP_DEFINITION.
 * A backup definition entity.
 *
 * @param id STEP instance id
 * @param name backup name
 * @param backupType backup variance type
 * @param backupSource backup variance source reference
 * @param backupTarget backup variance target reference
 * @param backupSchedule backup variance schedule
 * @param backupRetention backup variance retention period
 * @param backupStatus backup variance status
 */
public final class StepBackupDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String backupType;
    private final StepEntity backupSource;
    private final StepEntity backupTarget;
    private final String backupSchedule;
    private final int backupRetention;
    private final String backupStatus;

    public StepBackupDefinition(int id, String name, String backupType, StepEntity backupSource, StepEntity backupTarget, String backupSchedule, int backupRetention, String backupStatus) {
        this.id = id;
        this.name = name;
        this.backupType = backupType;
        this.backupSource = backupSource;
        this.backupTarget = backupTarget;
        this.backupSchedule = backupSchedule;
        this.backupRetention = backupRetention;
        this.backupStatus = backupStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBackupType() {
        return backupType;
    }

    public StepEntity getBackupSource() {
        return backupSource;
    }

    public StepEntity getBackupTarget() {
        return backupTarget;
    }

    public String getBackupSchedule() {
        return backupSchedule;
    }

    public int getBackupRetention() {
        return backupRetention;
    }

    public String getBackupStatus() {
        return backupStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepBackupDefinition that = (StepBackupDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(backupType, that.backupType) && Objects.equals(backupSource, that.backupSource) && Objects.equals(backupTarget, that.backupTarget) && Objects.equals(backupSchedule, that.backupSchedule) && backupRetention == that.backupRetention && Objects.equals(backupStatus, that.backupStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, backupType, backupSource, backupTarget, backupSchedule, backupRetention, backupStatus);
    }

    @Override
    public String toString() {
        return "StepBackupDefinition{" + "id=" + id + "name=" + name + "backupType=" + backupType + "backupSource=" + backupSource + "backupTarget=" + backupTarget + "backupSchedule=" + backupSchedule + "backupRetention=" + backupRetention + "backupStatus=" + backupStatus + "}";
    }
}