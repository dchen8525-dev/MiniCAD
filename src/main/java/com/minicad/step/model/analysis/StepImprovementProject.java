package com.minicad.step.model.analysis;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved IMPROVEMENT_PROJECT.
 * An improvement project entity.
 *
 * @param id STEP instance id
 * @param name project name
 * @varianceArea improvement variance area
 * @varianceObjective improvement variance objective
 * @varianceActions improvement variance actions
 * @varianceResources required variance resources
 * @varianceTimeline project variance timeline
 * @varianceBenefit expected variance benefit
 * @varianceStatus project variance status
 */
public record StepImprovementProject(
    int id,
    String name,
    String varianceArea,
    String varianceObjective,
    List<StepEntity> varianceActions,
    List<StepEntity> varianceResources,
    List<StepEntity> varianceTimeline,
    double varianceBenefit,
    String varianceStatus) implements StepEntity {
}