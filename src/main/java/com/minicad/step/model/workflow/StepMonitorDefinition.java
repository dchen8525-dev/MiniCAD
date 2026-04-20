package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepMonitorDefinition(
    int id,
    String name,
    String monitorType,
    StepEntity monitorTarget,
    List<StepEntity> monitorMetrics,
    int monitorInterval,
    List<StepEntity> monitorThresholds,
    String monitorStatus) implements StepEntity {
}