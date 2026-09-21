package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepGuidelineDefinition extends AbstractStepEntity {
    private final String guidelineType;
    private final String guidelineContent;
    private final List<String> guidelineRecommendations;
    private final String guidelineStatus;

    public StepGuidelineDefinition(int id, String name, String guidelineType, String guidelineContent, List<String> guidelineRecommendations, String guidelineStatus) {
        super(id, name);
        this.guidelineType = guidelineType;
        this.guidelineContent = guidelineContent;
        this.guidelineRecommendations = guidelineRecommendations == null ? null : java.util.List.copyOf(guidelineRecommendations);
        this.guidelineStatus = guidelineStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("guidelineType", guidelineType);
        state.put("guidelineContent", guidelineContent);
        state.put("guidelineRecommendations", guidelineRecommendations);
        state.put("guidelineStatus", guidelineStatus);
        return state;
    }
}
