package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepLessonLearned extends AbstractStepEntity {
    private final StepEntity varianceProject;
    private final String varianceSituation;
    private final String varianceLesson;
    private final String varianceRecommendation;
    private final String varianceCategory;
    private final StepEntity varianceDate;
    private final String varianceStatus;

    public StepLessonLearned(int id, String name, StepEntity varianceProject, String varianceSituation, String varianceLesson, String varianceRecommendation, String varianceCategory, StepEntity varianceDate, String varianceStatus) {
        super(id, name);
        this.varianceProject = varianceProject;
        this.varianceSituation = varianceSituation;
        this.varianceLesson = varianceLesson;
        this.varianceRecommendation = varianceRecommendation;
        this.varianceCategory = varianceCategory;
        this.varianceDate = varianceDate;
        this.varianceStatus = varianceStatus;
    }

    public StepEntity getVarianceProject() {
        return varianceProject;
    }

    public String getVarianceSituation() {
        return varianceSituation;
    }

    public String getVarianceLesson() {
        return varianceLesson;
    }

    public String getVarianceRecommendation() {
        return varianceRecommendation;
    }

    public String getVarianceCategory() {
        return varianceCategory;
    }

    public StepEntity getVarianceDate() {
        return varianceDate;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceProject", varianceProject);
        state.put("varianceSituation", varianceSituation);
        state.put("varianceLesson", varianceLesson);
        state.put("varianceRecommendation", varianceRecommendation);
        state.put("varianceCategory", varianceCategory);
        state.put("varianceDate", varianceDate);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
