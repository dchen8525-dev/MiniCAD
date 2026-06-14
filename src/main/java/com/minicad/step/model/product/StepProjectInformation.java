package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepProjectInformation implements StepEntity {
    private final int id;
    private final String name;
    private final String projectId;
    private final String projectType;
    private final List<StepEntity> varianceMembers;
    private final StepEntity varianceStart;
    private final StepEntity varianceEnd;
    private final double varianceBudget;
    private final String varianceStatus;

    public StepProjectInformation(int id, String name, String projectId, String projectType, List<StepEntity> varianceMembers, StepEntity varianceStart, StepEntity varianceEnd, double varianceBudget, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.projectId = projectId;
        this.projectType = projectType;
        this.varianceMembers = varianceMembers == null ? null : java.util.List.copyOf(varianceMembers);
        this.varianceStart = varianceStart;
        this.varianceEnd = varianceEnd;
        this.varianceBudget = varianceBudget;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepProjectInformation that = (StepProjectInformation) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(projectId, that.projectId) && Objects.equals(projectType, that.projectType) && Objects.equals(varianceMembers, that.varianceMembers) && Objects.equals(varianceStart, that.varianceStart) && Objects.equals(varianceEnd, that.varianceEnd) && varianceBudget == that.varianceBudget && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, projectId, projectType, varianceMembers, varianceStart, varianceEnd, varianceBudget, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepProjectInformation{" + "id=" + id + "name=" + name + "projectId=" + projectId + "projectType=" + projectType + "varianceMembers=" + varianceMembers + "varianceStart=" + varianceStart + "varianceEnd=" + varianceEnd + "varianceBudget=" + varianceBudget + "varianceStatus=" + varianceStatus + "}";
    }
}