package com.minicad.step.model.profile_analysis.analysis;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepImprovementProject implements StepEntity {
    private final int id;
    private final String name;
    private final String varianceArea;
    private final String varianceObjective;
    private final List<StepEntity> varianceActions;
    private final List<StepEntity> varianceResources;
    private final List<StepEntity> varianceTimeline;
    private final double varianceBenefit;
    private final String varianceStatus;

    public StepImprovementProject(int id, String name, String varianceArea, String varianceObjective, List<StepEntity> varianceActions, List<StepEntity> varianceResources, List<StepEntity> varianceTimeline, double varianceBenefit, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceArea = varianceArea;
        this.varianceObjective = varianceObjective;
        this.varianceActions = varianceActions == null ? null : java.util.List.copyOf(varianceActions);
        this.varianceResources = varianceResources == null ? null : java.util.List.copyOf(varianceResources);
        this.varianceTimeline = varianceTimeline == null ? null : java.util.List.copyOf(varianceTimeline);
        this.varianceBenefit = varianceBenefit;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepImprovementProject that = (StepImprovementProject) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceArea, that.varianceArea) && Objects.equals(varianceObjective, that.varianceObjective) && Objects.equals(varianceActions, that.varianceActions) && Objects.equals(varianceResources, that.varianceResources) && Objects.equals(varianceTimeline, that.varianceTimeline) && varianceBenefit == that.varianceBenefit && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceArea, varianceObjective, varianceActions, varianceResources, varianceTimeline, varianceBenefit, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepImprovementProject{" + "id=" + id + "name=" + name + "varianceArea=" + varianceArea + "varianceObjective=" + varianceObjective + "varianceActions=" + varianceActions + "varianceResources=" + varianceResources + "varianceTimeline=" + varianceTimeline + "varianceBenefit=" + varianceBenefit + "varianceStatus=" + varianceStatus + "}";
    }
}