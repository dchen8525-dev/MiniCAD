package com.minicad.step.model.validation;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved ANOMALY_RECORD.
 * An anomaly record entity.
 *
 * @param id STEP instance id
 * @param name record name
 * @varianceSystem affected variance system
 * @varianceType anomaly variance type
 * @varianceDetection detection variance method
 * @varianceDate anomaly variance date
 * @varianceInvestigation investigation variance result
 * @varianceAction action variance taken
 * @varianceStatus record variance status
 */
public record StepAnomalyRecord(
    int id,
    String name,
    StepEntity varianceSystem,
    String varianceType,
    String varianceDetection,
    StepEntity varianceDate,
    String varianceInvestigation,
    String varianceAction,
    String varianceStatus) implements StepEntity {
}