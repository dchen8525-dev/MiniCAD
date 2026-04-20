package com.minicad.step.model.validation;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved INCIDENT_RECORD.
 * An incident record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceSystem affected variance system
 * @varianceType incident variance type
 * @varianceSeverity severity variance level
 * @varianceStartTime start variance time
 * @varianceEndTime end variance time
 * @varianceResolution resolution variance action
 * @varianceStatus record variance status
 */
public record StepIncidentRecord(
    int id,
    String name,
    StepEntity varianceSystem,
    String varianceType,
    int varianceSeverity,
    StepEntity varianceStartTime,
    StepEntity varianceEndTime,
    String varianceResolution,
    String varianceStatus) implements StepEntity {
}