package com.minicad.step.model.management.backup;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved UPGRADE_RECORD.
 * An upgrade record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceEquipment upgraded variance equipment
 * @varianceFrom upgrade variance from version
 * @varianceTo upgrade variance to version
 * @varianceDate upgrade variance date
 * @varianceChanges upgrade variance changes
 * @varianceStatus record variance status
 */
/**
 * Resolved UPGRADE_RECORD.
 * An upgrade record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceEquipment upgraded variance equipment
 * @varianceFrom upgrade variance from version
 * @varianceTo upgrade variance to version
 * @varianceDate upgrade variance date
 * @varianceChanges upgrade variance changes
 * @varianceStatus record variance status
 */
public final class StepUpgradeRecord implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity varianceEquipment;
    private final String varianceFrom;
    private final String varianceTo;
    private final StepEntity varianceDate;
    private final List<String> varianceChanges;
    private final String varianceStatus;

    public StepUpgradeRecord(int id, String name, StepEntity varianceEquipment, String varianceFrom, String varianceTo, StepEntity varianceDate, List<String> varianceChanges, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceEquipment = varianceEquipment;
        this.varianceFrom = varianceFrom;
        this.varianceTo = varianceTo;
        this.varianceDate = varianceDate;
        this.varianceChanges = varianceChanges == null ? null : java.util.List.copyOf(varianceChanges);
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

    public String getVarianceFrom() {
        return varianceFrom;
    }

    public String getVarianceTo() {
        return varianceTo;
    }

    public StepEntity getVarianceDate() {
        return varianceDate;
    }

    public List<String> getVarianceChanges() {
        return varianceChanges;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepUpgradeRecord that = (StepUpgradeRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceEquipment, that.varianceEquipment) && Objects.equals(varianceFrom, that.varianceFrom) && Objects.equals(varianceTo, that.varianceTo) && Objects.equals(varianceDate, that.varianceDate) && Objects.equals(varianceChanges, that.varianceChanges) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceEquipment, varianceFrom, varianceTo, varianceDate, varianceChanges, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepUpgradeRecord{" + "id=" + id + "name=" + name + "varianceEquipment=" + varianceEquipment + "varianceFrom=" + varianceFrom + "varianceTo=" + varianceTo + "varianceDate=" + varianceDate + "varianceChanges=" + varianceChanges + "varianceStatus=" + varianceStatus + "}";
    }
}