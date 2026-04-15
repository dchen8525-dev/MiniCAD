package com.minicad.step.model;

import java.util.List;

/**
 * Resolved LOAD_CASE.
 * A load case entity.
 *
 * @param id STEP instance id
 * @param name load case name
 * @param caseType case variance type
 * @param caseLoads case variance load definitions
 * @param caseDescription case variance description
 * @param caseStatus case variance status
 */
public record StepLoadCase(
    int id,
    String name,
    String caseType,
    List<StepEntity> caseLoads,
    String caseDescription,
    String caseStatus) implements StepEntity {
}