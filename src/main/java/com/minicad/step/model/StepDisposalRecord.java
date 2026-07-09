package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved DISPOSAL_RECORD.
 * A disposal record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceItem disposed variance item
 * @varianceMethod disposal variance method
 * @varianceDate disposal variance date
 * @varianceAuthorization authorization variance reference
 * @varianceEnvironmental environmental variance compliance
 * @varianceStatus record variance status
 */
/**
 * Resolved DISPOSAL_RECORD.
 * A disposal record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceItem disposed variance item
 * @varianceMethod disposal variance method
 * @varianceDate disposal variance date
 * @varianceAuthorization authorization variance reference
 * @varianceEnvironmental environmental variance compliance
 * @varianceStatus record variance status
 */
public final class StepDisposalRecord implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity varianceItem;
    private final String varianceMethod;
    private final StepEntity varianceDate;
    private final StepEntity varianceAuthorization;
    private final String varianceEnvironmental;
    private final String varianceStatus;

    public StepDisposalRecord(int id, String name, StepEntity varianceItem, String varianceMethod, StepEntity varianceDate, StepEntity varianceAuthorization, String varianceEnvironmental, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceItem = varianceItem;
        this.varianceMethod = varianceMethod;
        this.varianceDate = varianceDate;
        this.varianceAuthorization = varianceAuthorization;
        this.varianceEnvironmental = varianceEnvironmental;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getVarianceItem() {
        return varianceItem;
    }

    public String getVarianceMethod() {
        return varianceMethod;
    }

    public StepEntity getVarianceDate() {
        return varianceDate;
    }

    public StepEntity getVarianceAuthorization() {
        return varianceAuthorization;
    }

    public String getVarianceEnvironmental() {
        return varianceEnvironmental;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDisposalRecord that = (StepDisposalRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceItem, that.varianceItem) && Objects.equals(varianceMethod, that.varianceMethod) && Objects.equals(varianceDate, that.varianceDate) && Objects.equals(varianceAuthorization, that.varianceAuthorization) && Objects.equals(varianceEnvironmental, that.varianceEnvironmental) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceItem, varianceMethod, varianceDate, varianceAuthorization, varianceEnvironmental, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepDisposalRecord{" + "id=" + id + "name=" + name + "varianceItem=" + varianceItem + "varianceMethod=" + varianceMethod + "varianceDate=" + varianceDate + "varianceAuthorization=" + varianceAuthorization + "varianceEnvironmental=" + varianceEnvironmental + "varianceStatus=" + varianceStatus + "}";
    }
}