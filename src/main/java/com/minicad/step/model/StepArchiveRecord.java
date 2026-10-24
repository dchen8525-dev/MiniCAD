package com.minicad.step.model;

import java.util.List;
import java.util.Map;

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
public final class StepArchiveRecord extends AbstractStepEntity {
    private final StepEntity varianceData;
    private final String varianceLocation;
    private final StepEntity varianceDate;
    private final double varianceRetention;
    private final String varianceAccess;
    private final String varianceStatus;

    public StepArchiveRecord(int id, String name, StepEntity varianceData, String varianceLocation, StepEntity varianceDate, double varianceRetention, String varianceAccess, String varianceStatus) {
        super(id, name);
        this.varianceData = varianceData;
        this.varianceLocation = varianceLocation;
        this.varianceDate = varianceDate;
        this.varianceRetention = varianceRetention;
        this.varianceAccess = varianceAccess;
        this.varianceStatus = varianceStatus;
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
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "varianceData", varianceData,
                "varianceLocation", varianceLocation, "varianceDate", varianceDate,
                "varianceRetention", varianceRetention, "varianceAccess", varianceAccess,
                "varianceStatus", varianceStatus);
    }
}
