package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved ACTION_CHAIN_INSTANCE.
 * An action chain instance entity.
 *
 * @param id STEP instance id
 * @param name action chain instance name
 * @param chainDefinition chain variance definition reference
 * @param chainState chain variance state
 * @param chainCurrentAction chain variance current action
 * @param chainCompletedActions chain variance completed action count
 * @param chainStatus chain variance status
 */
public final class StepActionChainInstance extends AbstractStepEntity {
    private final StepEntity chainDefinition;
    private final String chainState;
    private final int chainCurrentAction;
    private final int chainCompletedActions;
    private final String chainStatus;

    public StepActionChainInstance(int id, String name, StepEntity chainDefinition, String chainState, int chainCurrentAction, int chainCompletedActions, String chainStatus) {
        super(id, name);
        this.chainDefinition = chainDefinition;
        this.chainState = chainState;
        this.chainCurrentAction = chainCurrentAction;
        this.chainCompletedActions = chainCompletedActions;
        this.chainStatus = chainStatus;
    }

    public StepEntity getChainDefinition() {
        return chainDefinition;
    }

    public String getChainState() {
        return chainState;
    }

    public int getChainCurrentAction() {
        return chainCurrentAction;
    }

    public int getChainCompletedActions() {
        return chainCompletedActions;
    }

    public String getChainStatus() {
        return chainStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("chainDefinition", chainDefinition);
        state.put("chainState", chainState);
        state.put("chainCurrentAction", chainCurrentAction);
        state.put("chainCompletedActions", chainCompletedActions);
        state.put("chainStatus", chainStatus);
        return state;
    }
}
