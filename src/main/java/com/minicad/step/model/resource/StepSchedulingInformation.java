package com.minicad.step.model.resource;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved SCHEDULING_INFORMATION.
 * A scheduling information entity.
 *
 * @param id STEP instance id
 * @param name scheduling name
 * @param plannedStart planned start time
 * @param plannedEnd planned end time
 * @param actualStart actual start time
 * @param actualEnd actual end time
 * @param schedulingStatus scheduling status (planned, started, completed)
 * @param schedulingDependencies scheduling dependencies
 */
public record StepSchedulingInformation(
    int id,
    String name,
    double plannedStart,
    double plannedEnd,
    double actualStart,
    double actualEnd,
    String schedulingStatus,
    List<StepEntity> schedulingDependencies) implements StepEntity {
}