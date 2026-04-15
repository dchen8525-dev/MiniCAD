package com.minicad.step.model;

import java.util.List;

/**
 * Resolved BEST_PRACTICE.
 * A best practice entity.
 *
 * @param id STEP instance id
 * @param name practice name
 * @variancePractice practice variance description
 * @varianceArea applicable variance area
 * @varianceBenefits practice variance benefits
 * @varianceReference reference variance documentation
 * @varianceAdoption adoption variance level
 * @varianceStatus practice variance status
 */
public record StepBestPractice(
    int id,
    String name,
    String variancePractice,
    String varianceArea,
    List<String> varianceBenefits,
    StepEntity varianceReference,
    int varianceAdoption,
    String varianceStatus) implements StepEntity {
}