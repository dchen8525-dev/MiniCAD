package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepGuidelineDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String guidelineType;
    private final String guidelineContent;
    private final List<String> guidelineRecommendations;
    private final String guidelineStatus;

    public StepGuidelineDefinition(int id, String name, String guidelineType, String guidelineContent, List<String> guidelineRecommendations, String guidelineStatus) {
        this.id = id;
        this.name = name;
        this.guidelineType = guidelineType;
        this.guidelineContent = guidelineContent;
        this.guidelineRecommendations = guidelineRecommendations == null ? null : java.util.List.copyOf(guidelineRecommendations);
        this.guidelineStatus = guidelineStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGuidelineType() {
        return guidelineType;
    }

    public String getGuidelineContent() {
        return guidelineContent;
    }

    public List<String> getGuidelineRecommendations() {
        return guidelineRecommendations;
    }

    public String getGuidelineStatus() {
        return guidelineStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepGuidelineDefinition that = (StepGuidelineDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(guidelineType, that.guidelineType) && Objects.equals(guidelineContent, that.guidelineContent) && Objects.equals(guidelineRecommendations, that.guidelineRecommendations) && Objects.equals(guidelineStatus, that.guidelineStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, guidelineType, guidelineContent, guidelineRecommendations, guidelineStatus);
    }

    @Override
    public String toString() {
        return "StepGuidelineDefinition{" + "id=" + id + "name=" + name + "guidelineType=" + guidelineType + "guidelineContent=" + guidelineContent + "guidelineRecommendations=" + guidelineRecommendations + "guidelineStatus=" + guidelineStatus + "}";
    }
}