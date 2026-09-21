package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepGuidelineInstance extends AbstractStepEntity {
    private final StepEntity guidelineDefinition;
    private final String guidelineState;
    private final int guidelineAppliedCount;
    private final String guidelineStatus;

    public StepGuidelineInstance(int id, String name, StepEntity guidelineDefinition, String guidelineState, int guidelineAppliedCount, String guidelineStatus) {
        super(id, name);
        this.guidelineDefinition = guidelineDefinition;
        this.guidelineState = guidelineState;
        this.guidelineAppliedCount = guidelineAppliedCount;
        this.guidelineStatus = guidelineStatus;
    }

    public StepEntity getGuidelineDefinition() {
        return guidelineDefinition;
    }

    public String getGuidelineState() {
        return guidelineState;
    }

    public int getGuidelineAppliedCount() {
        return guidelineAppliedCount;
    }

    public String getGuidelineStatus() {
        return guidelineStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("guidelineDefinition", guidelineDefinition);
        state.put("guidelineState", guidelineState);
        state.put("guidelineAppliedCount", guidelineAppliedCount);
        state.put("guidelineStatus", guidelineStatus);
        return state;
    }
}
