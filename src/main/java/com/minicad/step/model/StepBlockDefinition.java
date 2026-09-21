package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved BLOCK_DEFINITION.
 * A block definition entity.
 *
 * @param id STEP instance id
 * @param name block name
 * @param blockType block variance type
 * @param blockSize block variance size
 * @param blockAlignment block variance alignment
 * @param blockChecksum block variance checksum type
 * @param blockStatus block variance status
 */
public final class StepBlockDefinition extends AbstractStepEntity {
    private final String blockType;
    private final int blockSize;
    private final int blockAlignment;
    private final String blockChecksum;
    private final String blockStatus;

    public StepBlockDefinition(int id, String name, String blockType, int blockSize, int blockAlignment, String blockChecksum, String blockStatus) {
        super(id, name);
        this.blockType = blockType;
        this.blockSize = blockSize;
        this.blockAlignment = blockAlignment;
        this.blockChecksum = blockChecksum;
        this.blockStatus = blockStatus;
    }

    public String getBlockType() {
        return blockType;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public int getBlockAlignment() {
        return blockAlignment;
    }

    public String getBlockChecksum() {
        return blockChecksum;
    }

    public String getBlockStatus() {
        return blockStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("blockType", blockType);
        state.put("blockSize", blockSize);
        state.put("blockAlignment", blockAlignment);
        state.put("blockChecksum", blockChecksum);
        state.put("blockStatus", blockStatus);
        return state;
    }
}
