package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved EVENT_CHAIN_INSTANCE.
 * An event chain instance entity.
 *
 * @param id STEP instance id
 * @param name event chain instance name
 * @param chainDefinition chain variance definition reference
 * @param chainState chain variance state
 * @param chainCurrentEvent chain variance current event position
 * @param chainCompletedEvents chain variance completed event count
 * @param chainStatus chain variance status
 */
public final class StepEventChainInstance extends AbstractStepEntity {
    private final StepEntity chainDefinition;
    private final String chainState;
    private final int chainCurrentEvent;
    private final int chainCompletedEvents;
    private final String chainStatus;

    public StepEventChainInstance(int id, String name, StepEntity chainDefinition, String chainState, int chainCurrentEvent, int chainCompletedEvents, String chainStatus) {
        super(id, name);
        this.chainDefinition = chainDefinition;
        this.chainState = chainState;
        this.chainCurrentEvent = chainCurrentEvent;
        this.chainCompletedEvents = chainCompletedEvents;
        this.chainStatus = chainStatus;
    }

    public StepEntity getChainDefinition() {
        return chainDefinition;
    }

    public String getChainState() {
        return chainState;
    }

    public int getChainCurrentEvent() {
        return chainCurrentEvent;
    }

    public int getChainCompletedEvents() {
        return chainCompletedEvents;
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
        state.put("chainCurrentEvent", chainCurrentEvent);
        state.put("chainCompletedEvents", chainCompletedEvents);
        state.put("chainStatus", chainStatus);
        return state;
    }
}
