package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved ARCHIVE_RECORD.
 * An archive record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceData archived variance data
 * @varianceLocation archive variance location
 * @varianceDate archive variance date
 * @varianceRetention retention variance period
 * @varianceAccess access variance restrictions
 * @varianceStatus record variance status
 */
/**
 * Resolved ARCHIVE_RECORD.
 * An archive record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceData archived variance data
 * @varianceLocation archive variance location
 * @varianceDate archive variance date
 * @varianceRetention retention variance period
 * @varianceAccess access variance restrictions
 * @varianceStatus record variance status
 */
public final class StepArchiveRecord implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity varianceData;
    private final String varianceLocation;
    private final StepEntity varianceDate;
    private final double varianceRetention;
    private final String varianceAccess;
    private final String varianceStatus;

    public StepArchiveRecord(int id, String name, StepEntity varianceData, String varianceLocation, StepEntity varianceDate, double varianceRetention, String varianceAccess, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceData = varianceData;
        this.varianceLocation = varianceLocation;
        this.varianceDate = varianceDate;
        this.varianceRetention = varianceRetention;
        this.varianceAccess = varianceAccess;
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

    public double getVarianceRetention() {
        return varianceRetention;
    }

    public String getVarianceAccess() {
        return varianceAccess;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepArchiveRecord that = (StepArchiveRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceData, that.varianceData) && Objects.equals(varianceLocation, that.varianceLocation) && Objects.equals(varianceDate, that.varianceDate) && varianceRetention == that.varianceRetention && Objects.equals(varianceAccess, that.varianceAccess) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceData, varianceLocation, varianceDate, varianceRetention, varianceAccess, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepArchiveRecord{" + "id=" + id + "name=" + name + "varianceData=" + varianceData + "varianceLocation=" + varianceLocation + "varianceDate=" + varianceDate + "varianceRetention=" + varianceRetention + "varianceAccess=" + varianceAccess + "varianceStatus=" + varianceStatus + "}";
    }
}