package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved SIGNAL_INSTANCE.
 * A signal instance entity.
 *
 * @param id STEP instance id
 * @param name signal instance name
 * @param signalDefinition signal variance definition reference
 * @param signalSource signal variance source reference
 * @param signalValue signal variance current value
 * @param signalHistory signal variance history samples
 * @param signalStatus signal variance status
 */
public final class StepSignalInstance extends AbstractStepEntity {
    private final StepEntity signalDefinition;
    private final StepEntity signalSource;
    private final double signalValue;
    private final List<Double> signalHistory;
    private final String signalStatus;

    public StepSignalInstance(int id, String name, StepEntity signalDefinition, StepEntity signalSource, double signalValue, List<Double> signalHistory, String signalStatus) {
        super(id, name);
        this.signalDefinition = signalDefinition;
        this.signalSource = signalSource;
        this.signalValue = signalValue;
        this.signalHistory = signalHistory == null ? null : java.util.List.copyOf(signalHistory);
        this.signalStatus = signalStatus;
    }

    public StepEntity getSignalDefinition() {
        return signalDefinition;
    }

    public StepEntity getSignalSource() {
        return signalSource;
    }

    public double getSignalValue() {
        return signalValue;
    }

    public List<Double> getSignalHistory() {
        return signalHistory;
    }

    public String getSignalStatus() {
        return signalStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("signalDefinition", signalDefinition);
        state.put("signalSource", signalSource);
        state.put("signalValue", signalValue);
        state.put("signalHistory", signalHistory);
        state.put("signalStatus", signalStatus);
        return state;
    }
}
