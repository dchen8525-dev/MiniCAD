package com.minicad.step.model.validation;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved LESSON_LEARNED.
 * A lesson learned entity.
 *
 * @param id STEP instance id
 * @param name lesson name
 * @varianceProject source variance project
 * @varianceSituation situation variance description
 * @varianceLesson lesson variance learned
 * @varianceRecommendation recommendation variance for future
 * @varianceCategory lesson variance category
 * @varianceDate documented variance date
 * @varianceStatus lesson variance status
 */
public record StepLessonLearned(
    int id,
    String name,
    StepEntity varianceProject,
    String varianceSituation,
    String varianceLesson,
    String varianceRecommendation,
    String varianceCategory,
    StepEntity varianceDate,
    String varianceStatus) implements StepEntity {
}