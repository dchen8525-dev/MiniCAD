package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved DECOMMISSION_RECORD.
 * A decommission record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceEquipment decommissioned variance equipment
 * @varianceReason decommission variance reason
 * @varianceDate decommission variance date
 * @varianceDisposition disposition variance action
 * @varianceDocumentation documentation variance reference
 * @varianceStatus record variance status
 */
/**
 * Resolved DECOMMISSION_RECORD.
 * A decommission record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceEquipment decommissioned variance equipment
 * @varianceReason decommission variance reason
 * @varianceDate decommission variance date
 * @varianceDisposition disposition variance action
 * @varianceDocumentation documentation variance reference
 * @varianceStatus record variance status
 */
public final class StepDecommissionRecord implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity varianceEquipment;
    private final String varianceReason;
    private final StepEntity varianceDate;
    private final String varianceDisposition;
    private final StepEntity varianceDocumentation;
    private final String varianceStatus;

    public StepDecommissionRecord(int id, String name, StepEntity varianceEquipment, String varianceReason, StepEntity varianceDate, String varianceDisposition, StepEntity varianceDocumentation, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceEquipment = varianceEquipment;
        this.varianceReason = varianceReason;
        this.varianceDate = varianceDate;
        this.varianceDisposition = varianceDisposition;
        this.varianceDocumentation = varianceDocumentation;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getVarianceEquipment() {
        return varianceEquipment;
    }

    public String getVarianceReason() {
        return varianceReason;
    }

    public StepEntity getVarianceDate() {
        return varianceDate;
    }

    public String getVarianceDisposition() {
        return varianceDisposition;
    }

    public StepEntity getVarianceDocumentation() {
        return varianceDocumentation;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDecommissionRecord that = (StepDecommissionRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceEquipment, that.varianceEquipment) && Objects.equals(varianceReason, that.varianceReason) && Objects.equals(varianceDate, that.varianceDate) && Objects.equals(varianceDisposition, that.varianceDisposition) && Objects.equals(varianceDocumentation, that.varianceDocumentation) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceEquipment, varianceReason, varianceDate, varianceDisposition, varianceDocumentation, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepDecommissionRecord{" + "id=" + id + "name=" + name + "varianceEquipment=" + varianceEquipment + "varianceReason=" + varianceReason + "varianceDate=" + varianceDate + "varianceDisposition=" + varianceDisposition + "varianceDocumentation=" + varianceDocumentation + "varianceStatus=" + varianceStatus + "}";
    }
}