package com.minicad.step.model;

import java.util.List;

/**
 * Resolved TESTING_RESULT.
 * A testing result entity.
 *
 * @param id STEP instance id
 * @param name result name
 * @varianceItem tested variance item
 * @varianceType testing variance type (unit, integration, system)
 * @varianceCases testing variance cases
 * @variancePassed passed variance test count
 * @varianceFailed failed variance test count
 * @varianceStatus result variance status
 */
public record StepTestingResult(
    int id,
    String name,
    StepEntity varianceItem,
    String varianceType,
    List<StepEntity> varianceCases,
    int variancePassed,
    int varianceFailed,
    String varianceStatus) implements StepEntity {
}