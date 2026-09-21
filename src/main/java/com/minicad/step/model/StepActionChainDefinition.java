package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved ACTION_CHAIN_DEFINITION.
 * An action chain definition entity.
 *
 * @param id STEP instance id
 * @param name action chain name
 * @param chainType chain variance type
 * @param chainActions chain variance action definitions
 * @param chainDependencies chain variance dependencies
 * @param chainParallel chain variance parallel execution flag
 * @param chainStatus chain variance status
 */
public final class StepActionChainDefinition extends AbstractStepEntity {
    private final String chainType;
    private final List<StepEntity> chainActions;
    private final List<String> chainDependencies;
    private final boolean chainParallel;
    private final String chainStatus;

    public StepActionChainDefinition(int id, String name, String chainType, List<StepEntity> chainActions, List<String> chainDependencies, boolean chainParallel, String chainStatus) {
        super(id, name);
        this.chainType = chainType;
        this.chainActions = chainActions == null ? null : java.util.List.copyOf(chainActions);
        this.chainDependencies = chainDependencies == null ? null : java.util.List.copyOf(chainDependencies);
        this.chainParallel = chainParallel;
        this.chainStatus = chainStatus;
    }

    public String getChainType() {
        return chainType;
    }

    public List<StepEntity> getChainActions() {
        return chainActions;
    }

    public List<String> getChainDependencies() {
        return chainDependencies;
    }

    public boolean isChainParallel() {
        return chainParallel;
    }

    public String getChainStatus() {
        return chainStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("chainType", chainType);
        state.put("chainActions", chainActions);
        state.put("chainDependencies", chainDependencies);
        state.put("chainParallel", chainParallel);
        state.put("chainStatus", chainStatus);
        return state;
    }
}
