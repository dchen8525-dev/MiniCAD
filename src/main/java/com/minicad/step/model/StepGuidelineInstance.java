package com.minicad.step.model;

import java.util.List;

/**
 * Resolved GUIDELINE_INSTANCE.
 * A guideline instance entity.
 *
 * @param id STEP instance id
 * @param name guideline instance name
 * @param guidelineDefinition guideline variance definition reference
 * @param guidelineState guideline variance state
 * @param guidelineAppliedCount guideline variance applied count
 * @param guidelineStatus guideline variance status
 */
public record StepGuidelineInstance(
    int id,
    String name,
    StepEntity guidelineDefinition,
    String guidelineState,
    int guidelineAppliedCount,
    String guidelineStatus) implements StepEntity {
}