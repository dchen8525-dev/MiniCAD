package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved GUIDELINE_DEFINITION.
 * A guideline definition entity.
 *
 * @param id STEP instance id
 * @param name guideline name
 * @param guidelineType guideline variance type
 * @param guidelineContent guideline variance content
 * @param guidelineRecommendations guideline variance recommendations
 * @param guidelineStatus guideline variance status
 */
public record StepGuidelineDefinition(
    int id,
    String name,
    String guidelineType,
    String guidelineContent,
    List<String> guidelineRecommendations,
    String guidelineStatus) implements StepEntity {
}