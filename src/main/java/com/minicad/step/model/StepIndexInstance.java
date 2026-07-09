package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved INDEX_INSTANCE.
 * An index instance entity.
 *
 * @param id STEP instance id
 * @param name index instance name
 * @param indexDefinition index variance definition reference
 * @param indexState index variance state
 * @param indexEntries index variance entry count
 * @param indexSize index variance size in bytes
 * @param indexStatus index variance status
 */
/**
 * Resolved INDEX_INSTANCE.
 * An index instance entity.
 *
 * @param id STEP instance id
 * @param name index instance name
 * @param indexDefinition index variance definition reference
 * @param indexState index variance state
 * @param indexEntries index variance entry count
 * @param indexSize index variance size in bytes
 * @param indexStatus index variance status
 */
public final class StepIndexInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity indexDefinition;
    private final String indexState;
    private final long indexEntries;
    private final long indexSize;
    private final String indexStatus;

    public StepIndexInstance(int id, String name, StepEntity indexDefinition, String indexState, long indexEntries, long indexSize, String indexStatus) {
        this.id = id;
        this.name = name;
        this.indexDefinition = indexDefinition;
        this.indexState = indexState;
        this.indexEntries = indexEntries;
        this.indexSize = indexSize;
        this.indexStatus = indexStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getIndexDefinition() {
        return indexDefinition;
    }

    public String getIndexState() {
        return indexState;
    }

    public long getIndexEntries() {
        return indexEntries;
    }

    public long getIndexSize() {
        return indexSize;
    }

    public String getIndexStatus() {
        return indexStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepIndexInstance that = (StepIndexInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(indexDefinition, that.indexDefinition) && Objects.equals(indexState, that.indexState) && indexEntries == that.indexEntries && indexSize == that.indexSize && Objects.equals(indexStatus, that.indexStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, indexDefinition, indexState, indexEntries, indexSize, indexStatus);
    }

    @Override
    public String toString() {
        return "StepIndexInstance{" + "id=" + id + "name=" + name + "indexDefinition=" + indexDefinition + "indexState=" + indexState + "indexEntries=" + indexEntries + "indexSize=" + indexSize + "indexStatus=" + indexStatus + "}";
    }
}