package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepProjectInformation extends AbstractStepEntity {
    private final String projectId;
    private final String projectType;
    private final List<StepEntity> varianceMembers;
    private final StepEntity varianceStart;
    private final StepEntity varianceEnd;
    private final double varianceBudget;
    private final String varianceStatus;

    public StepProjectInformation(int id, String name, String projectId, String projectType, List<StepEntity> varianceMembers, StepEntity varianceStart, StepEntity varianceEnd, double varianceBudget, String varianceStatus) {
        super(id, name);
        this.projectId = projectId;
        this.projectType = projectType;
        this.varianceMembers = varianceMembers == null ? null : java.util.List.copyOf(varianceMembers);
        this.varianceStart = varianceStart;
        this.varianceEnd = varianceEnd;
        this.varianceBudget = varianceBudget;
        this.varianceStatus = varianceStatus;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getProjectType() {
        return projectType;
    }

    public List<StepEntity> getVarianceMembers() {
        return varianceMembers;
    }

    public StepEntity getVarianceStart() {
        return varianceStart;
    }

    public StepEntity getVarianceEnd() {
        return varianceEnd;
    }

    public double getVarianceBudget() {
        return varianceBudget;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("projectId", projectId);
        state.put("projectType", projectType);
        state.put("varianceMembers", varianceMembers);
        state.put("varianceStart", varianceStart);
        state.put("varianceEnd", varianceEnd);
        state.put("varianceBudget", varianceBudget);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
