package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepPipelineDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String pipelineType;
    private final List<StepEntity> pipelineStages;
    private final boolean pipelineParallel;
    private final int pipelineTimeout;
    private final String pipelineStatus;

    public StepPipelineDefinition(int id, String name, String pipelineType, List<StepEntity> pipelineStages, boolean pipelineParallel, int pipelineTimeout, String pipelineStatus) {
        this.id = id;
        this.name = name;
        this.pipelineType = pipelineType;
        this.pipelineStages = pipelineStages == null ? null : java.util.List.copyOf(pipelineStages);
        this.pipelineParallel = pipelineParallel;
        this.pipelineTimeout = pipelineTimeout;
        this.pipelineStatus = pipelineStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPipelineDefinition that = (StepPipelineDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(pipelineType, that.pipelineType) && Objects.equals(pipelineStages, that.pipelineStages) && pipelineParallel == that.pipelineParallel && pipelineTimeout == that.pipelineTimeout && Objects.equals(pipelineStatus, that.pipelineStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, pipelineType, pipelineStages, pipelineParallel, pipelineTimeout, pipelineStatus);
    }

    @Override
    public String toString() {
        return "StepPipelineDefinition{" + "id=" + id + "name=" + name + "pipelineType=" + pipelineType + "pipelineStages=" + pipelineStages + "pipelineParallel=" + pipelineParallel + "pipelineTimeout=" + pipelineTimeout + "pipelineStatus=" + pipelineStatus + "}";
    }
}