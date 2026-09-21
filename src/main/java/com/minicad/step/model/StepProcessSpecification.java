package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved PROCESS_SPECIFICATION.
 * A process specification entity.
 *
 * @param id STEP instance id
 * @param name specification name
 * @varianceProcess specified variance process
 * @varianceParameters process variance parameters
 * @varianceRanges parameter variance ranges
 * @varianceMaterials material variance requirements
 * @varianceTools tool variance requirements
 * @varianceStatus specification variance status
 */
public final class StepProcessSpecification extends AbstractStepEntity {
    private final StepEntity varianceProcess;
    private final List<String> varianceParameters;
    private final List<Double> varianceRanges;
    private final List<StepEntity> varianceMaterials;
    private final List<StepEntity> varianceTools;
    private final String varianceStatus;

    public StepProcessSpecification(int id, String name, StepEntity varianceProcess, List<String> varianceParameters, List<Double> varianceRanges, List<StepEntity> varianceMaterials, List<StepEntity> varianceTools, String varianceStatus) {
        super(id, name);
        this.varianceProcess = varianceProcess;
        this.varianceParameters = varianceParameters == null ? null : java.util.List.copyOf(varianceParameters);
        this.varianceRanges = varianceRanges == null ? null : java.util.List.copyOf(varianceRanges);
        this.varianceMaterials = varianceMaterials == null ? null : java.util.List.copyOf(varianceMaterials);
        this.varianceTools = varianceTools == null ? null : java.util.List.copyOf(varianceTools);
        this.varianceStatus = varianceStatus;
    }

    public StepEntity getVarianceProcess() {
        return varianceProcess;
    }

    public List<String> getVarianceParameters() {
        return varianceParameters;
    }

    public List<Double> getVarianceRanges() {
        return varianceRanges;
    }

    public List<StepEntity> getVarianceMaterials() {
        return varianceMaterials;
    }

    public List<StepEntity> getVarianceTools() {
        return varianceTools;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceProcess", varianceProcess);
        state.put("varianceParameters", varianceParameters);
        state.put("varianceRanges", varianceRanges);
        state.put("varianceMaterials", varianceMaterials);
        state.put("varianceTools", varianceTools);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
