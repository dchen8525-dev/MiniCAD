package com.minicad.step.model.workflow;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved SAFETY_REQUIREMENT.
 * A safety requirement entity.
 *
 * @param id STEP instance id
 * @param name requirement name
 * @param requirementType requirement type (guarding, interlock, PPE)
 * @param requirementDescription requirement description
 * @variancePriority requirement variance priority
 * @param requirementStandard applicable safety standard
 * @varianceCompliance compliance variance status
 * @varianceActions required variance actions
 */
/**
 * Resolved SAFETY_REQUIREMENT.
 * A safety requirement entity.
 *
 * @param id STEP instance id
 * @param name requirement name
 * @param requirementType requirement type (guarding, interlock, PPE)
 * @param requirementDescription requirement description
 * @variancePriority requirement variance priority
 * @param requirementStandard applicable safety standard
 * @varianceCompliance compliance variance status
 * @varianceActions required variance actions
 */
public final class StepSafetyRequirement implements StepEntity {
    private final int id;
    private final String name;
    private final String requirementType;
    private final String requirementDescription;
    private final int variancePriority;
    private final String requirementStandard;
    private final String varianceCompliance;
    private final List<StepEntity> varianceActions;

    public StepSafetyRequirement(int id, String name, String requirementType, String requirementDescription, int variancePriority, String requirementStandard, String varianceCompliance, List<StepEntity> varianceActions) {
        this.id = id;
        this.name = name;
        this.requirementType = requirementType;
        this.requirementDescription = requirementDescription;
        this.variancePriority = variancePriority;
        this.requirementStandard = requirementStandard;
        this.varianceCompliance = varianceCompliance;
        this.varianceActions = varianceActions == null ? null : java.util.List.copyOf(varianceActions);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRequirementType() {
        return requirementType;
    }

    public String getRequirementDescription() {
        return requirementDescription;
    }

    public int getVariancePriority() {
        return variancePriority;
    }

    public String getRequirementStandard() {
        return requirementStandard;
    }

    public String getVarianceCompliance() {
        return varianceCompliance;
    }

    public List<StepEntity> getVarianceActions() {
        return varianceActions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSafetyRequirement that = (StepSafetyRequirement) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(requirementType, that.requirementType) && Objects.equals(requirementDescription, that.requirementDescription) && variancePriority == that.variancePriority && Objects.equals(requirementStandard, that.requirementStandard) && Objects.equals(varianceCompliance, that.varianceCompliance) && Objects.equals(varianceActions, that.varianceActions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, requirementType, requirementDescription, variancePriority, requirementStandard, varianceCompliance, varianceActions);
    }

    @Override
    public String toString() {
        return "StepSafetyRequirement{" + "id=" + id + "name=" + name + "requirementType=" + requirementType + "requirementDescription=" + requirementDescription + "variancePriority=" + variancePriority + "requirementStandard=" + requirementStandard + "varianceCompliance=" + varianceCompliance + "varianceActions=" + varianceActions + "}";
    }
}