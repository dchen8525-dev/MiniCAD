package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved PIPELINE_INSTANCE.
 * A pipeline instance entity.
 *
 * @param id STEP instance id
 * @param name pipeline instance name
 * @param pipelineDefinition pipeline variance definition reference
 * @param pipelineState pipeline variance state
 * @param pipelineCurrentStage pipeline variance current stage
 * @param pipelineStartTime pipeline variance start time
 * @param pipelineEndTime pipeline variance end time
 * @param pipelineStatus pipeline variance status
 */
public final class StepPipelineInstance extends AbstractStepEntity {
    private final StepEntity pipelineDefinition;
    private final String pipelineState;
    private final int pipelineCurrentStage;
    private final StepEntity pipelineStartTime;
    private final StepEntity pipelineEndTime;
    private final String pipelineStatus;

    public StepPipelineInstance(int id, String name, StepEntity pipelineDefinition, String pipelineState, int pipelineCurrentStage, StepEntity pipelineStartTime, StepEntity pipelineEndTime, String pipelineStatus) {
        super(id, name);
        this.pipelineDefinition = pipelineDefinition;
        this.pipelineState = pipelineState;
        this.pipelineCurrentStage = pipelineCurrentStage;
        this.pipelineStartTime = pipelineStartTime;
        this.pipelineEndTime = pipelineEndTime;
        this.pipelineStatus = pipelineStatus;
    }

    public StepEntity getPipelineDefinition() {
        return pipelineDefinition;
    }

    public String getPipelineState() {
        return pipelineState;
    }

    public int getPipelineCurrentStage() {
        return pipelineCurrentStage;
    }

    public StepEntity getPipelineStartTime() {
        return pipelineStartTime;
    }

    public StepEntity getPipelineEndTime() {
        return pipelineEndTime;
    }

    public String getPipelineStatus() {
        return pipelineStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("pipelineDefinition", pipelineDefinition);
        state.put("pipelineState", pipelineState);
        state.put("pipelineCurrentStage", pipelineCurrentStage);
        state.put("pipelineStartTime", pipelineStartTime);
        state.put("pipelineEndTime", pipelineEndTime);
        state.put("pipelineStatus", pipelineStatus);
        return state;
    }
}
