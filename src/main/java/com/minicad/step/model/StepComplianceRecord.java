package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved COMPLIANCE_RECORD.
 * A compliance record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceItem compliance variance item
 * @varianceStandard compliance variance standard
 * @varianceRequirements compliance variance requirements
 * @varianceEvidence compliance variance evidence
 * @varianceDate compliance variance date
 * @varianceStatus record variance status
 */
/**
 * Resolved COMPLIANCE_RECORD.
 * A compliance record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceItem compliance variance item
 * @varianceStandard compliance variance standard
 * @varianceRequirements compliance variance requirements
 * @varianceEvidence compliance variance evidence
 * @varianceDate compliance variance date
 * @varianceStatus record variance status
 */
public final class StepComplianceRecord implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity varianceItem;
    private final String varianceStandard;
    private final List<String> varianceRequirements;
    private final List<StepEntity> varianceEvidence;
    private final StepEntity varianceDate;
    private final String varianceStatus;

    public StepComplianceRecord(int id, String name, StepEntity varianceItem, String varianceStandard, List<String> varianceRequirements, List<StepEntity> varianceEvidence, StepEntity varianceDate, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceItem = varianceItem;
        this.varianceStandard = varianceStandard;
        this.varianceRequirements = varianceRequirements == null ? null : java.util.List.copyOf(varianceRequirements);
        this.varianceEvidence = varianceEvidence == null ? null : java.util.List.copyOf(varianceEvidence);
        this.varianceDate = varianceDate;
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

    public String getVarianceStandard() {
        return varianceStandard;
    }

    public List<String> getVarianceRequirements() {
        return varianceRequirements;
    }

    public List<StepEntity> getVarianceEvidence() {
        return varianceEvidence;
    }

    public StepEntity getVarianceDate() {
        return varianceDate;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepComplianceRecord that = (StepComplianceRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceItem, that.varianceItem) && Objects.equals(varianceStandard, that.varianceStandard) && Objects.equals(varianceRequirements, that.varianceRequirements) && Objects.equals(varianceEvidence, that.varianceEvidence) && Objects.equals(varianceDate, that.varianceDate) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceItem, varianceStandard, varianceRequirements, varianceEvidence, varianceDate, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepComplianceRecord{" + "id=" + id + "name=" + name + "varianceItem=" + varianceItem + "varianceStandard=" + varianceStandard + "varianceRequirements=" + varianceRequirements + "varianceEvidence=" + varianceEvidence + "varianceDate=" + varianceDate + "varianceStatus=" + varianceStatus + "}";
    }
}