package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepMaintenanceSchedule extends AbstractStepEntity {
    private final String maintenanceType;
    private final List<StepEntity> maintenanceItems;
    private final String varianceInterval;
    private final List<StepEntity> maintenanceTasks;
    private final List<StepEntity> varianceResources;
    private final String scheduleStatus;

    public StepMaintenanceSchedule(int id, String name, String maintenanceType, List<StepEntity> maintenanceItems, String varianceInterval, List<StepEntity> maintenanceTasks, List<StepEntity> varianceResources, String scheduleStatus) {
        super(id, name);
        this.maintenanceType = maintenanceType;
        this.maintenanceItems = maintenanceItems == null ? null : java.util.List.copyOf(maintenanceItems);
        this.varianceInterval = varianceInterval;
        this.maintenanceTasks = maintenanceTasks == null ? null : java.util.List.copyOf(maintenanceTasks);
        this.varianceResources = varianceResources == null ? null : java.util.List.copyOf(varianceResources);
        this.scheduleStatus = scheduleStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("maintenanceType", maintenanceType);
        state.put("maintenanceItems", maintenanceItems);
        state.put("varianceInterval", varianceInterval);
        state.put("maintenanceTasks", maintenanceTasks);
        state.put("varianceResources", varianceResources);
        state.put("scheduleStatus", scheduleStatus);
        return state;
    }
}
