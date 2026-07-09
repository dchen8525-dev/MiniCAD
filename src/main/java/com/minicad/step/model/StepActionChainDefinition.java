package com.minicad.step.model.action;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepActionChainDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String chainType;
    private final List<StepEntity> chainActions;
    private final List<String> chainDependencies;
    private final boolean chainParallel;
    private final String chainStatus;

    public StepActionChainDefinition(int id, String name, String chainType, List<StepEntity> chainActions, List<String> chainDependencies, boolean chainParallel, String chainStatus) {
        this.id = id;
        this.name = name;
        this.chainType = chainType;
        this.chainActions = chainActions == null ? null : java.util.List.copyOf(chainActions);
        this.chainDependencies = chainDependencies == null ? null : java.util.List.copyOf(chainDependencies);
        this.chainParallel = chainParallel;
        this.chainStatus = chainStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepActionChainDefinition that = (StepActionChainDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(chainType, that.chainType) && Objects.equals(chainActions, that.chainActions) && Objects.equals(chainDependencies, that.chainDependencies) && chainParallel == that.chainParallel && Objects.equals(chainStatus, that.chainStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, chainType, chainActions, chainDependencies, chainParallel, chainStatus);
    }

    @Override
    public String toString() {
        return "StepActionChainDefinition{" + "id=" + id + "name=" + name + "chainType=" + chainType + "chainActions=" + chainActions + "chainDependencies=" + chainDependencies + "chainParallel=" + chainParallel + "chainStatus=" + chainStatus + "}";
    }
}