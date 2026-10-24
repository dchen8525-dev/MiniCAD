package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved ARCHIVE_DEFINITION.
 * An archive definition entity.
 *
 * @param id STEP instance id
 * @param name archive name
 * @param archiveType archive variance type
 * @param archiveSource archive variance source reference
 * @param archiveTarget archive variance target reference
 * @param archiveFormat archive variance format
 * @param archiveRetention archive variance retention period
 * @param archiveStatus archive variance status
 */
public final class StepArchiveDefinition extends AbstractStepEntity {
    private final String archiveType;
    private final StepEntity archiveSource;
    private final StepEntity archiveTarget;
    private final String archiveFormat;
    private final int archiveRetention;
    private final String archiveStatus;

    public StepArchiveDefinition(int id, String name, String archiveType, StepEntity archiveSource, StepEntity archiveTarget, String archiveFormat, int archiveRetention, String archiveStatus) {
        super(id, name);
        this.archiveType = archiveType;
        this.archiveSource = archiveSource;
        this.archiveTarget = archiveTarget;
        this.archiveFormat = archiveFormat;
        this.archiveRetention = archiveRetention;
        this.archiveStatus = archiveStatus;
    }

    public String getArchiveType() {
        return archiveType;
    }

    public StepEntity getArchiveSource() {
        return archiveSource;
    }

    public StepEntity getArchiveTarget() {
        return archiveTarget;
    }

    public String getArchiveFormat() {
        return archiveFormat;
    }

    public int getArchiveRetention() {
        return archiveRetention;
    }

    public String getArchiveStatus() {
        return archiveStatus;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "archiveType", archiveType, "archiveSource", archiveSource,
                "archiveTarget", archiveTarget, "archiveFormat", archiveFormat,
                "archiveRetention", archiveRetention, "archiveStatus", archiveStatus);
    }
}
