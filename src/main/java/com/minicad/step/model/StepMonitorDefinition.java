package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved MONITOR_DEFINITION.
 * A monitor definition entity.
 *
 * @param id STEP instance id
 * @param name monitor name
 * @param monitorType monitor variance type
 * @param monitorTarget monitor variance target reference
 * @param monitorMetrics monitor variance metrics
 * @param monitorInterval monitor variance check interval
 * @param monitorThresholds monitor variance thresholds
 * @param monitorStatus monitor variance status
 */
/**
 * Resolved MONITOR_DEFINITION.
 * A monitor definition entity.
 *
 * @param id STEP instance id
 * @param name monitor name
 * @param monitorType monitor variance type
 * @param monitorTarget monitor variance target reference
 * @param monitorMetrics monitor variance metrics
 * @param monitorInterval monitor variance check interval
 * @param monitorThresholds monitor variance thresholds
 * @param monitorStatus monitor variance status
 */
public final class StepMonitorDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String monitorType;
    private final StepEntity monitorTarget;
    private final List<StepEntity> monitorMetrics;
    private final int monitorInterval;
    private final List<StepEntity> monitorThresholds;
    private final String monitorStatus;

    public StepMonitorDefinition(int id, String name, String monitorType, StepEntity monitorTarget, List<StepEntity> monitorMetrics, int monitorInterval, List<StepEntity> monitorThresholds, String monitorStatus) {
        this.id = id;
        this.name = name;
        this.monitorType = monitorType;
        this.monitorTarget = monitorTarget;
        this.monitorMetrics = monitorMetrics == null ? null : java.util.List.copyOf(monitorMetrics);
        this.monitorInterval = monitorInterval;
        this.monitorThresholds = monitorThresholds == null ? null : java.util.List.copyOf(monitorThresholds);
        this.monitorStatus = monitorStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMonitorType() {
        return monitorType;
    }

    public StepEntity getMonitorTarget() {
        return monitorTarget;
    }

    public List<StepEntity> getMonitorMetrics() {
        return monitorMetrics;
    }

    public int getMonitorInterval() {
        return monitorInterval;
    }

    public List<StepEntity> getMonitorThresholds() {
        return monitorThresholds;
    }

    public String getMonitorStatus() {
        return monitorStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepMonitorDefinition that = (StepMonitorDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(monitorType, that.monitorType) && Objects.equals(monitorTarget, that.monitorTarget) && Objects.equals(monitorMetrics, that.monitorMetrics) && monitorInterval == that.monitorInterval && Objects.equals(monitorThresholds, that.monitorThresholds) && Objects.equals(monitorStatus, that.monitorStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, monitorType, monitorTarget, monitorMetrics, monitorInterval, monitorThresholds, monitorStatus);
    }

    @Override
    public String toString() {
        return "StepMonitorDefinition{" + "id=" + id + "name=" + name + "monitorType=" + monitorType + "monitorTarget=" + monitorTarget + "monitorMetrics=" + monitorMetrics + "monitorInterval=" + monitorInterval + "monitorThresholds=" + monitorThresholds + "monitorStatus=" + monitorStatus + "}";
    }
}