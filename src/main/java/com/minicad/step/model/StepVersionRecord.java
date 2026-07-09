package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved VERSION_RECORD.
 * A version record entity.
 *
 * @param id STEP instance id
 * @param name version name
 * @param versionNumber version variance number
 * @param versionLabel version variance label
 * @param versionTarget version variance target reference
 * @param versionAuthor version variance author reference
 * @param versionTimestamp version variance timestamp
 * @param versionStatus version variance status
 */
/**
 * Resolved VERSION_RECORD.
 * A version record entity.
 *
 * @param id STEP instance id
 * @param name version name
 * @param versionNumber version variance number
 * @param versionLabel version variance label
 * @param versionTarget version variance target reference
 * @param versionAuthor version variance author reference
 * @param versionTimestamp version variance timestamp
 * @param versionStatus version variance status
 */
public final class StepVersionRecord implements StepEntity {
    private final int id;
    private final String name;
    private final String versionNumber;
    private final String versionLabel;
    private final StepEntity versionTarget;
    private final StepEntity versionAuthor;
    private final StepEntity versionTimestamp;
    private final String versionStatus;

    public StepVersionRecord(int id, String name, String versionNumber, String versionLabel, StepEntity versionTarget, StepEntity versionAuthor, StepEntity versionTimestamp, String versionStatus) {
        this.id = id;
        this.name = name;
        this.versionNumber = versionNumber;
        this.versionLabel = versionLabel;
        this.versionTarget = versionTarget;
        this.versionAuthor = versionAuthor;
        this.versionTimestamp = versionTimestamp;
        this.versionStatus = versionStatus;
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

    public String getVersionLabel() {
        return versionLabel;
    }

    public StepEntity getVersionTarget() {
        return versionTarget;
    }

    public StepEntity getVersionAuthor() {
        return versionAuthor;
    }

    public StepEntity getVersionTimestamp() {
        return versionTimestamp;
    }

    public String getVersionStatus() {
        return versionStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepVersionRecord that = (StepVersionRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(versionNumber, that.versionNumber) && Objects.equals(versionLabel, that.versionLabel) && Objects.equals(versionTarget, that.versionTarget) && Objects.equals(versionAuthor, that.versionAuthor) && Objects.equals(versionTimestamp, that.versionTimestamp) && Objects.equals(versionStatus, that.versionStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, versionNumber, versionLabel, versionTarget, versionAuthor, versionTimestamp, versionStatus);
    }

    @Override
    public String toString() {
        return "StepVersionRecord{" + "id=" + id + "name=" + name + "versionNumber=" + versionNumber + "versionLabel=" + versionLabel + "versionTarget=" + versionTarget + "versionAuthor=" + versionAuthor + "versionTimestamp=" + versionTimestamp + "versionStatus=" + versionStatus + "}";
    }
}