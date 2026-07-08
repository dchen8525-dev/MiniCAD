package com.minicad.step.model.validation;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepLessonLearned implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity varianceProject;
    private final String varianceSituation;
    private final String varianceLesson;
    private final String varianceRecommendation;
    private final String varianceCategory;
    private final StepEntity varianceDate;
    private final String varianceStatus;

    public StepLessonLearned(int id, String name, StepEntity varianceProject, String varianceSituation, String varianceLesson, String varianceRecommendation, String varianceCategory, StepEntity varianceDate, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceProject = varianceProject;
        this.varianceSituation = varianceSituation;
        this.varianceLesson = varianceLesson;
        this.varianceRecommendation = varianceRecommendation;
        this.varianceCategory = varianceCategory;
        this.varianceDate = varianceDate;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepLessonLearned that = (StepLessonLearned) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceProject, that.varianceProject) && Objects.equals(varianceSituation, that.varianceSituation) && Objects.equals(varianceLesson, that.varianceLesson) && Objects.equals(varianceRecommendation, that.varianceRecommendation) && Objects.equals(varianceCategory, that.varianceCategory) && Objects.equals(varianceDate, that.varianceDate) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceProject, varianceSituation, varianceLesson, varianceRecommendation, varianceCategory, varianceDate, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepLessonLearned{" + "id=" + id + "name=" + name + "varianceProject=" + varianceProject + "varianceSituation=" + varianceSituation + "varianceLesson=" + varianceLesson + "varianceRecommendation=" + varianceRecommendation + "varianceCategory=" + varianceCategory + "varianceDate=" + varianceDate + "varianceStatus=" + varianceStatus + "}";
    }
}