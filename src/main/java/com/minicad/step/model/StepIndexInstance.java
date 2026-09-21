package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepIndexInstance extends AbstractStepEntity {
    private final StepEntity indexDefinition;
    private final String indexState;
    private final long indexEntries;
    private final long indexSize;
    private final String indexStatus;

    public StepIndexInstance(int id, String name, StepEntity indexDefinition, String indexState, long indexEntries, long indexSize, String indexStatus) {
        super(id, name);
        this.indexDefinition = indexDefinition;
        this.indexState = indexState;
        this.indexEntries = indexEntries;
        this.indexSize = indexSize;
        this.indexStatus = indexStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("indexDefinition", indexDefinition);
        state.put("indexState", indexState);
        state.put("indexEntries", indexEntries);
        state.put("indexSize", indexSize);
        state.put("indexStatus", indexStatus);
        return state;
    }
}
