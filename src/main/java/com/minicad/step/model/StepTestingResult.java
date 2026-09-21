package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved TESTING_RESULT.
 * A testing result entity.
 *
 * @param id STEP instance id
 * @param name result name
 * @varianceItem tested variance item
 * @varianceType testing variance type (unit, integration, system)
 * @varianceCases testing variance cases
 * @variancePassed passed variance test count
 * @varianceFailed failed variance test count
 * @varianceStatus result variance status
 */
public final class StepTestingResult extends AbstractStepEntity {
    private final StepEntity varianceItem;
    private final String varianceType;
    private final List<StepEntity> varianceCases;
    private final int variancePassed;
    private final int varianceFailed;
    private final String varianceStatus;

    public StepTestingResult(int id, String name, StepEntity varianceItem, String varianceType, List<StepEntity> varianceCases, int variancePassed, int varianceFailed, String varianceStatus) {
        super(id, name);
        this.varianceItem = varianceItem;
        this.varianceType = varianceType;
        this.varianceCases = varianceCases == null ? null : java.util.List.copyOf(varianceCases);
        this.variancePassed = variancePassed;
        this.varianceFailed = varianceFailed;
        this.varianceStatus = varianceStatus;
    }

    public StepEntity getVarianceItem() {
        return varianceItem;
    }

    public String getVarianceType() {
        return varianceType;
    }

    public List<StepEntity> getVarianceCases() {
        return varianceCases;
    }

    public int getVariancePassed() {
        return variancePassed;
    }

    public int getVarianceFailed() {
        return varianceFailed;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceItem", varianceItem);
        state.put("varianceType", varianceType);
        state.put("varianceCases", varianceCases);
        state.put("variancePassed", variancePassed);
        state.put("varianceFailed", varianceFailed);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
