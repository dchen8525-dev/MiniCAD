package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepArchiveInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity archiveDefinition;
    private final StepEntity archiveTime;
    private final long archiveSize;
    private final int archiveEntries;
    private final String archiveStatus;

    public StepArchiveInstance(int id, String name, StepEntity archiveDefinition, StepEntity archiveTime, long archiveSize, int archiveEntries, String archiveStatus) {
        this.id = id;
        this.name = name;
        this.archiveDefinition = archiveDefinition;
        this.archiveTime = archiveTime;
        this.archiveSize = archiveSize;
        this.archiveEntries = archiveEntries;
        this.archiveStatus = archiveStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepArchiveInstance that = (StepArchiveInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(archiveDefinition, that.archiveDefinition) && Objects.equals(archiveTime, that.archiveTime) && archiveSize == that.archiveSize && archiveEntries == that.archiveEntries && Objects.equals(archiveStatus, that.archiveStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, archiveDefinition, archiveTime, archiveSize, archiveEntries, archiveStatus);
    }

    @Override
    public String toString() {
        return "StepArchiveInstance{" + "id=" + id + "name=" + name + "archiveDefinition=" + archiveDefinition + "archiveTime=" + archiveTime + "archiveSize=" + archiveSize + "archiveEntries=" + archiveEntries + "archiveStatus=" + archiveStatus + "}";
    }
}