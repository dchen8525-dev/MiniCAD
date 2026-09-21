package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved ARCHIVE_INSTANCE.
 * An archive instance entity.
 *
 * @param id STEP instance id
 * @param name archive instance name
 * @param archiveDefinition archive variance definition reference
 * @param archiveTime archive variance creation time
 * @param archiveSize archive variance size
 * @param archiveEntries archive variance entry count
 * @param archiveStatus archive variance status
 */
public final class StepArchiveInstance extends AbstractStepEntity {
    private final StepEntity archiveDefinition;
    private final StepEntity archiveTime;
    private final long archiveSize;
    private final int archiveEntries;
    private final String archiveStatus;

    public StepArchiveInstance(int id, String name, StepEntity archiveDefinition, StepEntity archiveTime, long archiveSize, int archiveEntries, String archiveStatus) {
        super(id, name);
        this.archiveDefinition = archiveDefinition;
        this.archiveTime = archiveTime;
        this.archiveSize = archiveSize;
        this.archiveEntries = archiveEntries;
        this.archiveStatus = archiveStatus;
    }

    public StepEntity getArchiveDefinition() {
        return archiveDefinition;
    }

    public StepEntity getArchiveTime() {
        return archiveTime;
    }

    public long getArchiveSize() {
        return archiveSize;
    }

    public int getArchiveEntries() {
        return archiveEntries;
    }

    public String getArchiveStatus() {
        return archiveStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("archiveDefinition", archiveDefinition);
        state.put("archiveTime", archiveTime);
        state.put("archiveSize", archiveSize);
        state.put("archiveEntries", archiveEntries);
        state.put("archiveStatus", archiveStatus);
        return state;
    }
}
