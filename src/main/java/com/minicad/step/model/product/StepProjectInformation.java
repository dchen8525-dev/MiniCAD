package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved PROJECT_INFORMATION.
 * A project information entity.
 *
 * @param id STEP instance id
 * @param name project name
 * @param projectId project identifier
 * @param projectType project type (design, manufacturing, research)
 * @varianceMembers project variance team members
 * @varianceStart project variance start date
 * @varianceEnd project variance end date
 * @varianceBudget project variance budget
 * @varianceStatus project variance status
 */
public record StepProjectInformation(
    int id,
    String name,
    String projectId,
    String projectType,
    List<StepEntity> varianceMembers,
    StepEntity varianceStart,
    StepEntity varianceEnd,
    double varianceBudget,
    String varianceStatus) implements StepEntity {
}