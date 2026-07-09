package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepBlockDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String blockType;
    private final int blockSize;
    private final int blockAlignment;
    private final String blockChecksum;
    private final String blockStatus;

    public StepBlockDefinition(int id, String name, String blockType, int blockSize, int blockAlignment, String blockChecksum, String blockStatus) {
        this.id = id;
        this.name = name;
        this.blockType = blockType;
        this.blockSize = blockSize;
        this.blockAlignment = blockAlignment;
        this.blockChecksum = blockChecksum;
        this.blockStatus = blockStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepBlockDefinition that = (StepBlockDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(blockType, that.blockType) && blockSize == that.blockSize && blockAlignment == that.blockAlignment && Objects.equals(blockChecksum, that.blockChecksum) && Objects.equals(blockStatus, that.blockStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, blockType, blockSize, blockAlignment, blockChecksum, blockStatus);
    }

    @Override
    public String toString() {
        return "StepBlockDefinition{" + "id=" + id + "name=" + name + "blockType=" + blockType + "blockSize=" + blockSize + "blockAlignment=" + blockAlignment + "blockChecksum=" + blockChecksum + "blockStatus=" + blockStatus + "}";
    }
}