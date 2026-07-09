package com.minicad.step.model.action;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepEventChainInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity chainDefinition;
    private final String chainState;
    private final int chainCurrentEvent;
    private final int chainCompletedEvents;
    private final String chainStatus;

    public StepEventChainInstance(int id, String name, StepEntity chainDefinition, String chainState, int chainCurrentEvent, int chainCompletedEvents, String chainStatus) {
        this.id = id;
        this.name = name;
        this.chainDefinition = chainDefinition;
        this.chainState = chainState;
        this.chainCurrentEvent = chainCurrentEvent;
        this.chainCompletedEvents = chainCompletedEvents;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepEventChainInstance that = (StepEventChainInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(chainDefinition, that.chainDefinition) && Objects.equals(chainState, that.chainState) && chainCurrentEvent == that.chainCurrentEvent && chainCompletedEvents == that.chainCompletedEvents && Objects.equals(chainStatus, that.chainStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, chainDefinition, chainState, chainCurrentEvent, chainCompletedEvents, chainStatus);
    }

    @Override
    public String toString() {
        return "StepEventChainInstance{" + "id=" + id + "name=" + name + "chainDefinition=" + chainDefinition + "chainState=" + chainState + "chainCurrentEvent=" + chainCurrentEvent + "chainCompletedEvents=" + chainCompletedEvents + "chainStatus=" + chainStatus + "}";
    }
}