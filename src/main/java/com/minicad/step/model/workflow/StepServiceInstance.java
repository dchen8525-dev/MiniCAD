package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved SERVICE_INSTANCE.
 * A service instance entity.
 *
 * @param id STEP instance id
 * @param name service instance name
 * @param serviceDefinition service variance definition reference
 * @param serviceState service variance state
 * @param serviceAvailability service variance availability
 * @param serviceResponseTime service variance response time
 * @param serviceStatus service variance status
 */
public record StepServiceInstance(
    int id,
    String name,
    StepEntity serviceDefinition,
    String serviceState,
    double serviceAvailability,
    double serviceResponseTime,
    String serviceStatus) implements StepEntity {
}