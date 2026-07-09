package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved RETRIEVAL_RECORD.
 * A retrieval record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceData retrieved variance data
 * @varianceArchive archive variance source
 * @varianceDate retrieval variance date
 * @varianceRequester requester variance reference
 * @variancePurpose retrieval variance purpose
 * @varianceStatus record variance status
 */
/**
 * Resolved RETRIEVAL_RECORD.
 * A retrieval record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceData retrieved variance data
 * @varianceArchive archive variance source
 * @varianceDate retrieval variance date
 * @varianceRequester requester variance reference
 * @variancePurpose retrieval variance purpose
 * @varianceStatus record variance status
 */
public final class StepRetrievalRecord implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity varianceData;
    private final StepEntity varianceArchive;
    private final StepEntity varianceDate;
    private final StepEntity varianceRequester;
    private final String variancePurpose;
    private final String varianceStatus;

    public StepRetrievalRecord(int id, String name, StepEntity varianceData, StepEntity varianceArchive, StepEntity varianceDate, StepEntity varianceRequester, String variancePurpose, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceData = varianceData;
        this.varianceArchive = varianceArchive;
        this.varianceDate = varianceDate;
        this.varianceRequester = varianceRequester;
        this.variancePurpose = variancePurpose;
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

    public StepEntity getVarianceArchive() {
        return varianceArchive;
    }

    public StepEntity getVarianceDate() {
        return varianceDate;
    }

    public StepEntity getVarianceRequester() {
        return varianceRequester;
    }

    public String getVariancePurpose() {
        return variancePurpose;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepRetrievalRecord that = (StepRetrievalRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceData, that.varianceData) && Objects.equals(varianceArchive, that.varianceArchive) && Objects.equals(varianceDate, that.varianceDate) && Objects.equals(varianceRequester, that.varianceRequester) && Objects.equals(variancePurpose, that.variancePurpose) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceData, varianceArchive, varianceDate, varianceRequester, variancePurpose, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepRetrievalRecord{" + "id=" + id + "name=" + name + "varianceData=" + varianceData + "varianceArchive=" + varianceArchive + "varianceDate=" + varianceDate + "varianceRequester=" + varianceRequester + "variancePurpose=" + variancePurpose + "varianceStatus=" + varianceStatus + "}";
    }
}