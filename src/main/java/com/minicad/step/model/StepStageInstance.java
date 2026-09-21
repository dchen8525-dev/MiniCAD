package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved STAGE_INSTANCE.
 * A stage instance entity.
 *
 * @param id STEP instance id
 * @param name stage instance name
 * @param stageDefinition stage variance definition reference
 * @param stageState stage variance state
 * @param stageStartTime stage variance start time
 * @param stageEndTime stage variance end time
 * @param stageProgress stage variance progress percentage
 * @param stageStatus stage variance status
 */
public final class StepStageInstance extends AbstractStepEntity {
    private final StepEntity stageDefinition;
    private final String stageState;
    private final StepEntity stageStartTime;
    private final StepEntity stageEndTime;
    private final double stageProgress;
    private final String stageStatus;

    public StepStageInstance(int id, String name, StepEntity stageDefinition, String stageState, StepEntity stageStartTime, StepEntity stageEndTime, double stageProgress, String stageStatus) {
        super(id, name);
        this.stageDefinition = stageDefinition;
        this.stageState = stageState;
        this.stageStartTime = stageStartTime;
        this.stageEndTime = stageEndTime;
        this.stageProgress = stageProgress;
        this.stageStatus = stageStatus;
    }

    public StepEntity getStageDefinition() {
        return stageDefinition;
    }

    public String getStageState() {
        return stageState;
    }

    public StepEntity getStageStartTime() {
        return stageStartTime;
    }

    public StepEntity getStageEndTime() {
        return stageEndTime;
    }

    public double getStageProgress() {
        return stageProgress;
    }

    public String getStageStatus() {
        return stageStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("stageDefinition", stageDefinition);
        state.put("stageState", stageState);
        state.put("stageStartTime", stageStartTime);
        state.put("stageEndTime", stageEndTime);
        state.put("stageProgress", stageProgress);
        state.put("stageStatus", stageStatus);
        return state;
    }
}
