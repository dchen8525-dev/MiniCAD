package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepSafetyRequirement extends AbstractStepEntity {
    private final String requirementType;
    private final String requirementDescription;
    private final int variancePriority;
    private final String requirementStandard;
    private final String varianceCompliance;
    private final List<StepEntity> varianceActions;

    public StepSafetyRequirement(int id, String name, String requirementType, String requirementDescription, int variancePriority, String requirementStandard, String varianceCompliance, List<StepEntity> varianceActions) {
        super(id, name);
        this.requirementType = requirementType;
        this.requirementDescription = requirementDescription;
        this.variancePriority = variancePriority;
        this.requirementStandard = requirementStandard;
        this.varianceCompliance = varianceCompliance;
        this.varianceActions = varianceActions == null ? null : java.util.List.copyOf(varianceActions);
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("requirementType", requirementType);
        state.put("requirementDescription", requirementDescription);
        state.put("variancePriority", variancePriority);
        state.put("requirementStandard", requirementStandard);
        state.put("varianceCompliance", varianceCompliance);
        state.put("varianceActions", varianceActions);
        return state;
    }
}
