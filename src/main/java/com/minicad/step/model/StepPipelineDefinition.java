package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved PIPELINE_DEFINITION.
 * A pipeline definition entity.
 *
 * @param id STEP instance id
 * @param name pipeline name
 * @param pipelineType pipeline variance type
 * @param pipelineStages pipeline variance stage definitions
 * @param pipelineParallel pipeline variance parallel execution flag
 * @param pipelineTimeout pipeline variance timeout
 * @param pipelineStatus pipeline variance status
 */
public final class StepPipelineDefinition extends AbstractStepEntity {
    private final String pipelineType;
    private final List<StepEntity> pipelineStages;
    private final boolean pipelineParallel;
    private final int pipelineTimeout;
    private final String pipelineStatus;

    public StepPipelineDefinition(int id, String name, String pipelineType, List<StepEntity> pipelineStages, boolean pipelineParallel, int pipelineTimeout, String pipelineStatus) {
        super(id, name);
        this.pipelineType = pipelineType;
        this.pipelineStages = pipelineStages == null ? null : java.util.List.copyOf(pipelineStages);
        this.pipelineParallel = pipelineParallel;
        this.pipelineTimeout = pipelineTimeout;
        this.pipelineStatus = pipelineStatus;
    }

    public String getPipelineType() {
        return pipelineType;
    }

    public List<StepEntity> getPipelineStages() {
        return pipelineStages;
    }

    public boolean isPipelineParallel() {
        return pipelineParallel;
    }

    public int getPipelineTimeout() {
        return pipelineTimeout;
    }

    public String getPipelineStatus() {
        return pipelineStatus;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "pipelineType", pipelineType,
                "pipelineStages", pipelineStages, "pipelineParallel", pipelineParallel,
                "pipelineTimeout", pipelineTimeout, "pipelineStatus", pipelineStatus);
    }
}
