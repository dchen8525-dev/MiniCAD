package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved LOGGING_SPECIFICATION.
 * A logging specification entity.
 *
 * @param id STEP instance id
 * @param name specification name
 * @varianceEvents logged variance events
 * @varianceFormat log variance format
 * @varianceLevel log variance level (info, warning, error)
 * @varianceDestination log variance destination
 * @varianceRetention retention variance period
 * @varianceStatus specification variance status
 */
public final class StepLoggingSpecification extends AbstractStepEntity {
    private final List<String> varianceEvents;
    private final String varianceFormat;
    private final String varianceLevel;
    private final String varianceDestination;
    private final double varianceRetention;
    private final String varianceStatus;

    public StepLoggingSpecification(int id, String name, List<String> varianceEvents, String varianceFormat, String varianceLevel, String varianceDestination, double varianceRetention, String varianceStatus) {
        super(id, name);
        this.varianceEvents = varianceEvents == null ? null : java.util.List.copyOf(varianceEvents);
        this.varianceFormat = varianceFormat;
        this.varianceLevel = varianceLevel;
        this.varianceDestination = varianceDestination;
        this.varianceRetention = varianceRetention;
        this.varianceStatus = varianceStatus;
    }

    public List<String> getVarianceEvents() {
        return varianceEvents;
    }

    public String getVarianceFormat() {
        return varianceFormat;
    }

    public String getVarianceLevel() {
        return varianceLevel;
    }

    public String getVarianceDestination() {
        return varianceDestination;
    }

    public double getVarianceRetention() {
        return varianceRetention;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("varianceEvents", varianceEvents);
        state.put("varianceFormat", varianceFormat);
        state.put("varianceLevel", varianceLevel);
        state.put("varianceDestination", varianceDestination);
        state.put("varianceRetention", varianceRetention);
        state.put("varianceStatus", varianceStatus);
        return state;
    }
}
