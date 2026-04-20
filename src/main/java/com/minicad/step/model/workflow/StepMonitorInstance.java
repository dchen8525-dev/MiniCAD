package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved MONITOR_INSTANCE.
 * A monitor instance entity.
 *
 * @param id STEP instance id
 * @param name monitor instance name
 * @param monitorDefinition monitor variance definition reference
 * @param monitorState monitor variance state
 * @param monitorLastCheck monitor variance last check time
 * @param monitorAlerts monitor variance alert count
 * @param monitorStatus monitor variance status
 */
public record StepMonitorInstance(
    int id,
    String name,
    StepEntity monitorDefinition,
    String monitorState,
    StepEntity monitorLastCheck,
    int monitorAlerts,
    String monitorStatus) implements StepEntity {
}