package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved VERSION_CONTROL.
 * A version control entity.
 *
 * @param id STEP instance id
 * @param name version name
 * @param versionNumber version number/identifier
 * @param versionDescription version description
 * @param versionDate version release date
 * @param versionAuthor version author
 * @param versionStatus version status (draft, released, archived)
 * @param previousVersion previous version reference
 * @param versionChanges changes from previous version
 */
/**
 * Resolved VERSION_CONTROL.
 * A version control entity.
 *
 * @param id STEP instance id
 * @param name version name
 * @param versionNumber version number/identifier
 * @param versionDescription version description
 * @param versionDate version release date
 * @param versionAuthor version author
 * @param versionStatus version status (draft, released, archived)
 * @param previousVersion previous version reference
 * @param versionChanges changes from previous version
 */
public final class StepVersionControl implements StepEntity {
    private final int id;
    private final String name;
    private final String versionNumber;
    private final String versionDescription;
    private final StepEntity versionDate;
    private final StepEntity versionAuthor;
    private final String versionStatus;
    private final StepEntity previousVersion;
    private final List<StepEntity> versionChanges;

    public StepVersionControl(int id, String name, String versionNumber, String versionDescription, StepEntity versionDate, StepEntity versionAuthor, String versionStatus, StepEntity previousVersion, List<StepEntity> versionChanges) {
        this.id = id;
        this.name = name;
        this.versionNumber = versionNumber;
        this.versionDescription = versionDescription;
        this.versionDate = versionDate;
        this.versionAuthor = versionAuthor;
        this.versionStatus = versionStatus;
        this.previousVersion = previousVersion;
        this.versionChanges = versionChanges == null ? null : java.util.List.copyOf(versionChanges);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public String getVersionDescription() {
        return versionDescription;
    }

    public StepEntity getVersionDate() {
        return versionDate;
    }

    public StepEntity getVersionAuthor() {
        return versionAuthor;
    }

    public String getVersionStatus() {
        return versionStatus;
    }

    public StepEntity getPreviousVersion() {
        return previousVersion;
    }

    public List<StepEntity> getVersionChanges() {
        return versionChanges;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepVersionControl that = (StepVersionControl) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(versionNumber, that.versionNumber) && Objects.equals(versionDescription, that.versionDescription) && Objects.equals(versionDate, that.versionDate) && Objects.equals(versionAuthor, that.versionAuthor) && Objects.equals(versionStatus, that.versionStatus) && Objects.equals(previousVersion, that.previousVersion) && Objects.equals(versionChanges, that.versionChanges);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, versionNumber, versionDescription, versionDate, versionAuthor, versionStatus, previousVersion, versionChanges);
    }

    @Override
    public String toString() {
        return "StepVersionControl{" + "id=" + id + "name=" + name + "versionNumber=" + versionNumber + "versionDescription=" + versionDescription + "versionDate=" + versionDate + "versionAuthor=" + versionAuthor + "versionStatus=" + versionStatus + "previousVersion=" + previousVersion + "versionChanges=" + versionChanges + "}";
    }
}