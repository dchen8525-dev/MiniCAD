package com.minicad.step.model;

import java.util.List;
import java.util.Map;

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
public final class StepEventChainDefinition extends AbstractStepEntity {
    private final String chainType;
    private final List<StepEntity> chainEvents;
    private final String chainOrder;
    private final boolean chainParallel;
    private final String chainStatus;

    public StepEventChainDefinition(int id, String name, String chainType, List<StepEntity> chainEvents, String chainOrder, boolean chainParallel, String chainStatus) {
        super(id, name);
        this.chainType = chainType;
        this.chainEvents = chainEvents == null ? null : java.util.List.copyOf(chainEvents);
        this.chainOrder = chainOrder;
        this.chainParallel = chainParallel;
        this.chainStatus = chainStatus;
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
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "chainType", chainType, "chainEvents", chainEvents,
                "chainOrder", chainOrder, "chainParallel", chainParallel, "chainStatus", chainStatus);
    }
}
