package com.minicad.step.model.resource;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved MAINTENANCE_SCHEDULE.
 * A maintenance schedule entity.
 *
 * @param id STEP instance id
 * @param name schedule name
 * @param maintenanceType maintenance type (preventive, corrective, predictive)
 * @param maintenanceItems items to be maintained
 * @varianceInterval maintenance variance interval
 * @param maintenanceTasks maintenance tasks specification
 * @varianceResources required variance resources
 * @param scheduleStatus schedule status
 */
/**
 * Resolved MAINTENANCE_SCHEDULE.
 * A maintenance schedule entity.
 *
 * @param id STEP instance id
 * @param name schedule name
 * @param maintenanceType maintenance type (preventive, corrective, predictive)
 * @param maintenanceItems items to be maintained
 * @varianceInterval maintenance variance interval
 * @param maintenanceTasks maintenance tasks specification
 * @varianceResources required variance resources
 * @param scheduleStatus schedule status
 */
public final class StepMaintenanceSchedule implements StepEntity {
    private final int id;
    private final String name;
    private final String maintenanceType;
    private final List<StepEntity> maintenanceItems;
    private final String varianceInterval;
    private final List<StepEntity> maintenanceTasks;
    private final List<StepEntity> varianceResources;
    private final String scheduleStatus;

    public StepMaintenanceSchedule(int id, String name, String maintenanceType, List<StepEntity> maintenanceItems, String varianceInterval, List<StepEntity> maintenanceTasks, List<StepEntity> varianceResources, String scheduleStatus) {
        this.id = id;
        this.name = name;
        this.maintenanceType = maintenanceType;
        this.maintenanceItems = maintenanceItems == null ? null : java.util.List.copyOf(maintenanceItems);
        this.varianceInterval = varianceInterval;
        this.maintenanceTasks = maintenanceTasks == null ? null : java.util.List.copyOf(maintenanceTasks);
        this.varianceResources = varianceResources == null ? null : java.util.List.copyOf(varianceResources);
        this.scheduleStatus = scheduleStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMaintenanceType() {
        return maintenanceType;
    }

    public List<StepEntity> getMaintenanceItems() {
        return maintenanceItems;
    }

    public String getVarianceInterval() {
        return varianceInterval;
    }

    public List<StepEntity> getMaintenanceTasks() {
        return maintenanceTasks;
    }

    public List<StepEntity> getVarianceResources() {
        return varianceResources;
    }

    public String getScheduleStatus() {
        return scheduleStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepMaintenanceSchedule that = (StepMaintenanceSchedule) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(maintenanceType, that.maintenanceType) && Objects.equals(maintenanceItems, that.maintenanceItems) && Objects.equals(varianceInterval, that.varianceInterval) && Objects.equals(maintenanceTasks, that.maintenanceTasks) && Objects.equals(varianceResources, that.varianceResources) && Objects.equals(scheduleStatus, that.scheduleStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, maintenanceType, maintenanceItems, varianceInterval, maintenanceTasks, varianceResources, scheduleStatus);
    }

    @Override
    public String toString() {
        return "StepMaintenanceSchedule{" + "id=" + id + "name=" + name + "maintenanceType=" + maintenanceType + "maintenanceItems=" + maintenanceItems + "varianceInterval=" + varianceInterval + "maintenanceTasks=" + maintenanceTasks + "varianceResources=" + varianceResources + "scheduleStatus=" + scheduleStatus + "}";
    }
}