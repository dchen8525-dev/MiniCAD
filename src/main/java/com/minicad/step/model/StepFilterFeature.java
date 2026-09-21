package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved FILTER_FEATURE.
 * A filter feature entity.
 *
 * @param id STEP instance id
 * @param name filter name
 * @param filterType filter type (air, liquid, magnetic)
 * @param filterGeometry filter geometry representation
 * @param filterMedia filter media specification
 * @varianceMicron variance micron rating
 * @varianceFlow variance flow capacity
 * @param replacementInterval replacement interval specification
 */
public final class StepFilterFeature extends AbstractStepEntity {
    private final String filterType;
    private final StepEntity filterGeometry;
    private final StepEntity filterMedia;
    private final double varianceMicron;
    private final double varianceFlow;
    private final String replacementInterval;

    public StepFilterFeature(int id, String name, String filterType, StepEntity filterGeometry, StepEntity filterMedia, double varianceMicron, double varianceFlow, String replacementInterval) {
        super(id, name);
        this.filterType = filterType;
        this.filterGeometry = filterGeometry;
        this.filterMedia = filterMedia;
        this.varianceMicron = varianceMicron;
        this.varianceFlow = varianceFlow;
        this.replacementInterval = replacementInterval;
    }

    public String getFilterType() {
        return filterType;
    }

    public StepEntity getFilterGeometry() {
        return filterGeometry;
    }

    public StepEntity getFilterMedia() {
        return filterMedia;
    }

    public double getVarianceMicron() {
        return varianceMicron;
    }

    public double getVarianceFlow() {
        return varianceFlow;
    }

    public String getReplacementInterval() {
        return replacementInterval;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("filterType", filterType);
        state.put("filterGeometry", filterGeometry);
        state.put("filterMedia", filterMedia);
        state.put("varianceMicron", varianceMicron);
        state.put("varianceFlow", varianceFlow);
        state.put("replacementInterval", replacementInterval);
        return state;
    }
}
