package com.minicad.step.model.config_mgmt;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved CONFIGURATION_RECORD.
 * A configuration record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceItem configured variance item
 * @varianceSettings configuration variance settings
 * @varianceDate configuration variance date
 * @varianceReason configuration variance reason
 * @variancePrevious previous variance configuration
 * @varianceStatus record variance status
 */
/**
 * Resolved CONFIGURATION_RECORD.
 * A configuration record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceItem configured variance item
 * @varianceSettings configuration variance settings
 * @varianceDate configuration variance date
 * @varianceReason configuration variance reason
 * @variancePrevious previous variance configuration
 * @varianceStatus record variance status
 */
public final class StepConfigurationRecord implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity varianceItem;
    private final List<String> varianceSettings;
    private final StepEntity varianceDate;
    private final String varianceReason;
    private final StepEntity variancePrevious;
    private final String varianceStatus;

    public StepConfigurationRecord(int id, String name, StepEntity varianceItem, List<String> varianceSettings, StepEntity varianceDate, String varianceReason, StepEntity variancePrevious, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceItem = varianceItem;
        this.varianceSettings = varianceSettings == null ? null : java.util.List.copyOf(varianceSettings);
        this.varianceDate = varianceDate;
        this.varianceReason = varianceReason;
        this.variancePrevious = variancePrevious;
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

    public List<String> getVarianceSettings() {
        return varianceSettings;
    }

    public StepEntity getVarianceDate() {
        return varianceDate;
    }

    public String getVarianceReason() {
        return varianceReason;
    }

    public StepEntity getVariancePrevious() {
        return variancePrevious;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepConfigurationRecord that = (StepConfigurationRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceItem, that.varianceItem) && Objects.equals(varianceSettings, that.varianceSettings) && Objects.equals(varianceDate, that.varianceDate) && Objects.equals(varianceReason, that.varianceReason) && Objects.equals(variancePrevious, that.variancePrevious) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceItem, varianceSettings, varianceDate, varianceReason, variancePrevious, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepConfigurationRecord{" + "id=" + id + "name=" + name + "varianceItem=" + varianceItem + "varianceSettings=" + varianceSettings + "varianceDate=" + varianceDate + "varianceReason=" + varianceReason + "variancePrevious=" + variancePrevious + "varianceStatus=" + varianceStatus + "}";
    }
}