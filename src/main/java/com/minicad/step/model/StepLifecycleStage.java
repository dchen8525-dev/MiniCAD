package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved LIFECYCLE_STAGE.
 * A lifecycle stage entity.
 *
 * @param id STEP instance id
 * @param name stage name
 * @param stageType lifecycle stage type (design, manufacturing, service, disposal)
 * @param stageDescription stage description
 * @param stageStartDate stage start date
 * @param stageEndDate stage end date
 * @param stageOwner stage owner/responsible party
 * @param stageRequirements stage-specific requirements
 */
public final class StepLifecycleStage extends AbstractStepEntity {
    private final String stageType;
    private final String stageDescription;
    private final StepEntity stageStartDate;
    private final StepEntity stageEndDate;
    private final StepEntity stageOwner;
    private final List<StepEntity> stageRequirements;

    public StepLifecycleStage(int id, String name, String stageType, String stageDescription, StepEntity stageStartDate, StepEntity stageEndDate, StepEntity stageOwner, List<StepEntity> stageRequirements) {
        super(id, name);
        this.stageType = stageType;
        this.stageDescription = stageDescription;
        this.stageStartDate = stageStartDate;
        this.stageEndDate = stageEndDate;
        this.stageOwner = stageOwner;
        this.stageRequirements = stageRequirements == null ? null : java.util.List.copyOf(stageRequirements);
    }

    public String getStageType() {
        return stageType;
    }

    public String getStageDescription() {
        return stageDescription;
    }

    public StepEntity getStageStartDate() {
        return stageStartDate;
    }

    public StepEntity getStageEndDate() {
        return stageEndDate;
    }

    public StepEntity getStageOwner() {
        return stageOwner;
    }

    public List<StepEntity> getStageRequirements() {
        return stageRequirements;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("stageType", stageType);
        state.put("stageDescription", stageDescription);
        state.put("stageStartDate", stageStartDate);
        state.put("stageEndDate", stageEndDate);
        state.put("stageOwner", stageOwner);
        state.put("stageRequirements", stageRequirements);
        return state;
    }
}
