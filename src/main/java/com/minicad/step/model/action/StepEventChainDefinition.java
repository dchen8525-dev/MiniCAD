package com.minicad.step.model.action;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved EVENT_CHAIN_DEFINITION.
 * An event chain definition entity.
 *
 * @param id STEP instance id
 * @param name event chain name
 * @param chainType chain variance type
 * @param chainEvents chain variance event definitions
 * @param chainOrder chain variance ordering
 * @param chainParallel chain variance parallel flag
 * @param chainStatus chain variance status
 */
/**
 * Resolved EVENT_CHAIN_DEFINITION.
 * An event chain definition entity.
 *
 * @param id STEP instance id
 * @param name event chain name
 * @param chainType chain variance type
 * @param chainEvents chain variance event definitions
 * @param chainOrder chain variance ordering
 * @param chainParallel chain variance parallel flag
 * @param chainStatus chain variance status
 */
public final class StepEventChainDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String chainType;
    private final List<StepEntity> chainEvents;
    private final String chainOrder;
    private final boolean chainParallel;
    private final String chainStatus;

    public StepEventChainDefinition(int id, String name, String chainType, List<StepEntity> chainEvents, String chainOrder, boolean chainParallel, String chainStatus) {
        this.id = id;
        this.name = name;
        this.chainType = chainType;
        this.chainEvents = chainEvents == null ? null : java.util.List.copyOf(chainEvents);
        this.chainOrder = chainOrder;
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

    public List<StepEntity> getChainEvents() {
        return chainEvents;
    }

    public String getChainOrder() {
        return chainOrder;
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
        StepEventChainDefinition that = (StepEventChainDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(chainType, that.chainType) && Objects.equals(chainEvents, that.chainEvents) && Objects.equals(chainOrder, that.chainOrder) && chainParallel == that.chainParallel && Objects.equals(chainStatus, that.chainStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, chainType, chainEvents, chainOrder, chainParallel, chainStatus);
    }

    @Override
    public String toString() {
        return "StepEventChainDefinition{" + "id=" + id + "name=" + name + "chainType=" + chainType + "chainEvents=" + chainEvents + "chainOrder=" + chainOrder + "chainParallel=" + chainParallel + "chainStatus=" + chainStatus + "}";
    }
}