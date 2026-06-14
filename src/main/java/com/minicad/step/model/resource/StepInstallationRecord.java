package com.minicad.step.model.resource;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved INSTALLATION_RECORD.
 * An installation record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceEquipment installed variance equipment
 * @varianceLocation installation variance location
 * @varianceDate installation variance date
 * @varianceInstaller installer variance person/team
 * @varianceChecks installation variance verification checks
 * @varianceStatus record variance status
 */
/**
 * Resolved INSTALLATION_RECORD.
 * An installation record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceEquipment installed variance equipment
 * @varianceLocation installation variance location
 * @varianceDate installation variance date
 * @varianceInstaller installer variance person/team
 * @varianceChecks installation variance verification checks
 * @varianceStatus record variance status
 */
public final class StepInstallationRecord implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity varianceEquipment;
    private final String varianceLocation;
    private final StepEntity varianceDate;
    private final StepEntity varianceInstaller;
    private final List<String> varianceChecks;
    private final String varianceStatus;

    public StepInstallationRecord(int id, String name, StepEntity varianceEquipment, String varianceLocation, StepEntity varianceDate, StepEntity varianceInstaller, List<String> varianceChecks, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceEquipment = varianceEquipment;
        this.varianceLocation = varianceLocation;
        this.varianceDate = varianceDate;
        this.varianceInstaller = varianceInstaller;
        this.varianceChecks = varianceChecks == null ? null : java.util.List.copyOf(varianceChecks);
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

    public String getVarianceLocation() {
        return varianceLocation;
    }

    public StepEntity getVarianceDate() {
        return varianceDate;
    }

    public StepEntity getVarianceInstaller() {
        return varianceInstaller;
    }

    public List<String> getVarianceChecks() {
        return varianceChecks;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepInstallationRecord that = (StepInstallationRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceEquipment, that.varianceEquipment) && Objects.equals(varianceLocation, that.varianceLocation) && Objects.equals(varianceDate, that.varianceDate) && Objects.equals(varianceInstaller, that.varianceInstaller) && Objects.equals(varianceChecks, that.varianceChecks) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceEquipment, varianceLocation, varianceDate, varianceInstaller, varianceChecks, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepInstallationRecord{" + "id=" + id + "name=" + name + "varianceEquipment=" + varianceEquipment + "varianceLocation=" + varianceLocation + "varianceDate=" + varianceDate + "varianceInstaller=" + varianceInstaller + "varianceChecks=" + varianceChecks + "varianceStatus=" + varianceStatus + "}";
    }
}