package com.minicad.step.model.action;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepActionChainInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity chainDefinition;
    private final String chainState;
    private final int chainCurrentAction;
    private final int chainCompletedActions;
    private final String chainStatus;

    public StepActionChainInstance(int id, String name, StepEntity chainDefinition, String chainState, int chainCurrentAction, int chainCompletedActions, String chainStatus) {
        this.id = id;
        this.name = name;
        this.chainDefinition = chainDefinition;
        this.chainState = chainState;
        this.chainCurrentAction = chainCurrentAction;
        this.chainCompletedActions = chainCompletedActions;
        this.chainStatus = chainStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepActionChainInstance that = (StepActionChainInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(chainDefinition, that.chainDefinition) && Objects.equals(chainState, that.chainState) && chainCurrentAction == that.chainCurrentAction && chainCompletedActions == that.chainCompletedActions && Objects.equals(chainStatus, that.chainStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, chainDefinition, chainState, chainCurrentAction, chainCompletedActions, chainStatus);
    }

    @Override
    public String toString() {
        return "StepActionChainInstance{" + "id=" + id + "name=" + name + "chainDefinition=" + chainDefinition + "chainState=" + chainState + "chainCurrentAction=" + chainCurrentAction + "chainCompletedActions=" + chainCompletedActions + "chainStatus=" + chainStatus + "}";
    }
}