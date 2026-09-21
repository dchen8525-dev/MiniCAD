package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved RECOVERY_DEFINITION.
 * A recovery definition entity.
 *
 * @param id STEP instance id
 * @param name recovery name
 * @param recoveryType recovery variance type
 * @param recoveryStrategy recovery variance strategy
 * @param recoverySteps recovery variance recovery steps
 * @param recoveryTimeout recovery variance timeout
 * @param recoveryStatus recovery variance status
 */
public final class StepRecoveryDefinition extends AbstractStepEntity {
    private final String recoveryType;
    private final String recoveryStrategy;
    private final List<String> recoverySteps;
    private final int recoveryTimeout;
    private final String recoveryStatus;

    public StepRecoveryDefinition(int id, String name, String recoveryType, String recoveryStrategy, List<String> recoverySteps, int recoveryTimeout, String recoveryStatus) {
        super(id, name);
        this.recoveryType = recoveryType;
        this.recoveryStrategy = recoveryStrategy;
        this.recoverySteps = recoverySteps == null ? null : java.util.List.copyOf(recoverySteps);
        this.recoveryTimeout = recoveryTimeout;
        this.recoveryStatus = recoveryStatus;
    }

    public String getRecoveryType() {
        return recoveryType;
    }

    public String getRecoveryStrategy() {
        return recoveryStrategy;
    }

    public List<String> getRecoverySteps() {
        return recoverySteps;
    }

    public int getRecoveryTimeout() {
        return recoveryTimeout;
    }

    public String getRecoveryStatus() {
        return recoveryStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("recoveryType", recoveryType);
        state.put("recoveryStrategy", recoveryStrategy);
        state.put("recoverySteps", recoverySteps);
        state.put("recoveryTimeout", recoveryTimeout);
        state.put("recoveryStatus", recoveryStatus);
        return state;
    }
}
