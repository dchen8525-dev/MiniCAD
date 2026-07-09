package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
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
public final class StepBlockInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity blockDefinition;
    private final long blockAddress;
    private final int blockUsed;
    private final String blockStatus;

    public StepBlockInstance(int id, String name, StepEntity blockDefinition, long blockAddress, int blockUsed, String blockStatus) {
        this.id = id;
        this.name = name;
        this.blockDefinition = blockDefinition;
        this.blockAddress = blockAddress;
        this.blockUsed = blockUsed;
        this.blockStatus = blockStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepBlockInstance that = (StepBlockInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(blockDefinition, that.blockDefinition) && blockAddress == that.blockAddress && blockUsed == that.blockUsed && Objects.equals(blockStatus, that.blockStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, blockDefinition, blockAddress, blockUsed, blockStatus);
    }

    @Override
    public String toString() {
        return "StepBlockInstance{" + "id=" + id + "name=" + name + "blockDefinition=" + blockDefinition + "blockAddress=" + blockAddress + "blockUsed=" + blockUsed + "blockStatus=" + blockStatus + "}";
    }
}