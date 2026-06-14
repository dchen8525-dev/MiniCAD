package com.minicad.step.model.backup_recovery;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepArchiveDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String archiveType;
    private final StepEntity archiveSource;
    private final StepEntity archiveTarget;
    private final String archiveFormat;
    private final int archiveRetention;
    private final String archiveStatus;

    public StepArchiveDefinition(int id, String name, String archiveType, StepEntity archiveSource, StepEntity archiveTarget, String archiveFormat, int archiveRetention, String archiveStatus) {
        this.id = id;
        this.name = name;
        this.archiveType = archiveType;
        this.archiveSource = archiveSource;
        this.archiveTarget = archiveTarget;
        this.archiveFormat = archiveFormat;
        this.archiveRetention = archiveRetention;
        this.archiveStatus = archiveStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepArchiveDefinition that = (StepArchiveDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(archiveType, that.archiveType) && Objects.equals(archiveSource, that.archiveSource) && Objects.equals(archiveTarget, that.archiveTarget) && Objects.equals(archiveFormat, that.archiveFormat) && archiveRetention == that.archiveRetention && Objects.equals(archiveStatus, that.archiveStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, archiveType, archiveSource, archiveTarget, archiveFormat, archiveRetention, archiveStatus);
    }

    @Override
    public String toString() {
        return "StepArchiveDefinition{" + "id=" + id + "name=" + name + "archiveType=" + archiveType + "archiveSource=" + archiveSource + "archiveTarget=" + archiveTarget + "archiveFormat=" + archiveFormat + "archiveRetention=" + archiveRetention + "archiveStatus=" + archiveStatus + "}";
    }
}