package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved STAGE_DEFINITION.
 * A stage definition entity.
 *
 * @param id STEP instance id
 * @param name stage name
 * @param stageType stage variance type
 * @param stageSequence stage variance sequence number
 * @param stageTasks stage variance task definitions
 * @param stageDependencies stage variance dependencies
 * @param stageStatus stage variance status
 */
public final class StepStageDefinition extends AbstractStepEntity {
    private final String stageType;
    private final int stageSequence;
    private final List<StepEntity> stageTasks;
    private final List<StepEntity> stageDependencies;
    private final String stageStatus;

    public StepStageDefinition(int id, String name, String stageType, int stageSequence, List<StepEntity> stageTasks, List<StepEntity> stageDependencies, String stageStatus) {
        super(id, name);
        this.stageType = stageType;
        this.stageSequence = stageSequence;
        this.stageTasks = stageTasks == null ? null : java.util.List.copyOf(stageTasks);
        this.stageDependencies = stageDependencies == null ? null : java.util.List.copyOf(stageDependencies);
        this.stageStatus = stageStatus;
    }

    public String getStageType() {
        return stageType;
    }

    public int getStageSequence() {
        return stageSequence;
    }

    public List<StepEntity> getStageTasks() {
        return stageTasks;
    }

    public List<StepEntity> getStageDependencies() {
        return stageDependencies;
    }

    public String getStageStatus() {
        return stageStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("stageType", stageType);
        state.put("stageSequence", stageSequence);
        state.put("stageTasks", stageTasks);
        state.put("stageDependencies", stageDependencies);
        state.put("stageStatus", stageStatus);
        return state;
    }
}
