package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved RESTORE_RECORD.
 * A restore record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceData restored variance data
 * @varianceSource restore variance source/backup
 * @varianceDate restore variance date
 * @varianceVerified verification variance status
 * @varianceStatus record variance status
 */
/**
 * Resolved RESTORE_RECORD.
 * A restore record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceData restored variance data
 * @varianceSource restore variance source/backup
 * @varianceDate restore variance date
 * @varianceVerified verification variance status
 * @varianceStatus record variance status
 */
public final class StepRestoreRecord implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity varianceData;
    private final StepEntity varianceSource;
    private final StepEntity varianceDate;
    private final boolean varianceVerified;
    private final String varianceStatus;

    public StepRestoreRecord(int id, String name, StepEntity varianceData, StepEntity varianceSource, StepEntity varianceDate, boolean varianceVerified, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceData = varianceData;
        this.varianceSource = varianceSource;
        this.varianceDate = varianceDate;
        this.varianceVerified = varianceVerified;
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

    public StepEntity getVarianceSource() {
        return varianceSource;
    }

    public StepEntity getVarianceDate() {
        return varianceDate;
    }

    public boolean isVarianceVerified() {
        return varianceVerified;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepRestoreRecord that = (StepRestoreRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceData, that.varianceData) && Objects.equals(varianceSource, that.varianceSource) && Objects.equals(varianceDate, that.varianceDate) && varianceVerified == that.varianceVerified && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceData, varianceSource, varianceDate, varianceVerified, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepRestoreRecord{" + "id=" + id + "name=" + name + "varianceData=" + varianceData + "varianceSource=" + varianceSource + "varianceDate=" + varianceDate + "varianceVerified=" + varianceVerified + "varianceStatus=" + varianceStatus + "}";
    }
}