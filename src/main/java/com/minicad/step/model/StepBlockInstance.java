package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved BLOCK_INSTANCE.
 * A block instance entity.
 *
 * @param id STEP instance id
 * @param name block instance name
 * @param blockDefinition block variance definition reference
 * @param blockAddress block variance address
 * @param blockUsed block variance used size
 * @param blockStatus block variance status
 */
public final class StepBlockInstance extends AbstractStepEntity {
    private final StepEntity blockDefinition;
    private final long blockAddress;
    private final int blockUsed;
    private final String blockStatus;

    public StepBlockInstance(int id, String name, StepEntity blockDefinition, long blockAddress, int blockUsed, String blockStatus) {
        super(id, name);
        this.blockDefinition = blockDefinition;
        this.blockAddress = blockAddress;
        this.blockUsed = blockUsed;
        this.blockStatus = blockStatus;
    }

    public StepEntity getBlockDefinition() {
        return blockDefinition;
    }

    public long getBlockAddress() {
        return blockAddress;
    }

    public int getBlockUsed() {
        return blockUsed;
    }

    public String getBlockStatus() {
        return blockStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("blockDefinition", blockDefinition);
        state.put("blockAddress", blockAddress);
        state.put("blockUsed", blockUsed);
        state.put("blockStatus", blockStatus);
        return state;
    }
}
