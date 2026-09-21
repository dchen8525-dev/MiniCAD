package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepImprovementProject extends AbstractStepEntity {
    private final String varianceArea;
    private final String varianceObjective;
    private final List<StepEntity> varianceActions;
    private final List<StepEntity> varianceResources;
    private final List<StepEntity> varianceTimeline;
    private final double varianceBenefit;
    private final String varianceStatus;

    public StepImprovementProject(int id, String name, String varianceArea, String varianceObjective, List<StepEntity> varianceActions, List<StepEntity> varianceResources, List<StepEntity> varianceTimeline, double varianceBenefit, String varianceStatus) {
        super(id, name);
        this.varianceArea = varianceArea;
        this.varianceObjective = varianceObjective;
        this.varianceActions = varianceActions == null ? null : java.util.List.copyOf(varianceActions);
        this.varianceResources = varianceResources == null ? null : java.util.List.copyOf(varianceResources);
        this.varianceTimeline = varianceTimeline == null ? null : java.util.List.copyOf(varianceTimeline);
        this.varianceBenefit = varianceBenefit;
        this.varianceStatus = varianceStatus;
    }

    public String getVarianceArea() {
        return varianceArea;
    }

    public String getVarianceObjective() {
        return varianceObjective;
    }

    public List<StepEntity> getVarianceActions() {
        return varianceActions;
    }

    public List<StepEntity> getVarianceResources() {
        return varianceResources;
    }

    public List<StepEntity> getVarianceTimeline() {
        return varianceTimeline;
    }

    public double getVarianceBenefit() {
        return varianceBenefit;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceArea", varianceArea);
        state.put("varianceObjective", varianceObjective);
        state.put("varianceActions", varianceActions);
        state.put("varianceResources", varianceResources);
        state.put("varianceTimeline", varianceTimeline);
        state.put("varianceBenefit", varianceBenefit);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
