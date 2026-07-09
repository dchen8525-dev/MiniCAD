package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved BACKUP_RECORD.
 * A backup record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceData backed variance up data
 * @varianceLocation backup variance location
 * @varianceDate backup variance date
 * @varianceSize backup variance size
 * @varianceType backup variance type (full, incremental)
 * @varianceStatus record variance status
 */
/**
 * Resolved BACKUP_RECORD.
 * A backup record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceData backed variance up data
 * @varianceLocation backup variance location
 * @varianceDate backup variance date
 * @varianceSize backup variance size
 * @varianceType backup variance type (full, incremental)
 * @varianceStatus record variance status
 */
public final class StepBackupRecord implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity varianceData;
    private final String varianceLocation;
    private final StepEntity varianceDate;
    private final double varianceSize;
    private final String varianceType;
    private final String varianceStatus;

    public StepBackupRecord(int id, String name, StepEntity varianceData, String varianceLocation, StepEntity varianceDate, double varianceSize, String varianceType, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceData = varianceData;
        this.varianceLocation = varianceLocation;
        this.varianceDate = varianceDate;
        this.varianceSize = varianceSize;
        this.varianceType = varianceType;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getVarianceData() {
        return varianceData;
    }

    public String getVarianceLocation() {
        return varianceLocation;
    }

    public StepEntity getVarianceDate() {
        return varianceDate;
    }

    public double getVarianceSize() {
        return varianceSize;
    }

    public String getVarianceType() {
        return varianceType;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepBackupRecord that = (StepBackupRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceData, that.varianceData) && Objects.equals(varianceLocation, that.varianceLocation) && Objects.equals(varianceDate, that.varianceDate) && varianceSize == that.varianceSize && Objects.equals(varianceType, that.varianceType) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceData, varianceLocation, varianceDate, varianceSize, varianceType, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepBackupRecord{" + "id=" + id + "name=" + name + "varianceData=" + varianceData + "varianceLocation=" + varianceLocation + "varianceDate=" + varianceDate + "varianceSize=" + varianceSize + "varianceType=" + varianceType + "varianceStatus=" + varianceStatus + "}";
    }
}