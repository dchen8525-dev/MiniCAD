package com.minicad.step.model.resource;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepMaintenanceSchedule(
    int id,
    String name,
    String maintenanceType,
    List<StepEntity> maintenanceItems,
    String varianceInterval,
    List<StepEntity> maintenanceTasks,
    List<StepEntity> varianceResources,
    String scheduleStatus) implements StepEntity {
}