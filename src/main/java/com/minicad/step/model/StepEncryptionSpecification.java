package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved ENCRYPTION_SPECIFICATION.
 * An encryption specification entity.
 *
 * @param id STEP instance id
 * @param name specification name
 * @varianceAlgorithm encryption variance algorithm
 * @varianceKeySize key variance size
 * @varianceMode encryption variance mode
 * @varianceKeyManagement key variance management specification
 * @varianceStatus specification variance status
 */
public final class StepEncryptionSpecification extends AbstractStepEntity {
    private final String varianceAlgorithm;
    private final int varianceKeySize;
    private final String varianceMode;
    private final StepEntity varianceKeyManagement;
    private final String varianceStatus;

    public StepEncryptionSpecification(int id, String name, String varianceAlgorithm, int varianceKeySize, String varianceMode, StepEntity varianceKeyManagement, String varianceStatus) {
        super(id, name);
        this.varianceAlgorithm = varianceAlgorithm;
        this.varianceKeySize = varianceKeySize;
        this.varianceMode = varianceMode;
        this.varianceKeyManagement = varianceKeyManagement;
        this.varianceStatus = varianceStatus;
    }

    public String getVarianceAlgorithm() {
        return varianceAlgorithm;
    }

    public int getVarianceKeySize() {
        return varianceKeySize;
    }

    public String getVarianceMode() {
        return varianceMode;
    }

    public StepEntity getVarianceKeyManagement() {
        return varianceKeyManagement;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceAlgorithm", varianceAlgorithm);
        state.put("varianceKeySize", varianceKeySize);
        state.put("varianceMode", varianceMode);
        state.put("varianceKeyManagement", varianceKeyManagement);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
