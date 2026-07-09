package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved STANDARD_DEFINITION.
 * A standard definition entity.
 *
 * @param id STEP instance id
 * @param name standard name
 * @param standardType standard variance type
 * @param standardCode standard variance code/identifier
 * @param standardVersion standard variance version
 * @param standardRequirements standard variance requirements
 * @param standardStatus standard variance status
 */
/**
 * Resolved STANDARD_DEFINITION.
 * A standard definition entity.
 *
 * @param id STEP instance id
 * @param name standard name
 * @param standardType standard variance type
 * @param standardCode standard variance code/identifier
 * @param standardVersion standard variance version
 * @param standardRequirements standard variance requirements
 * @param standardStatus standard variance status
 */
public final class StepStandardDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String standardType;
    private final String standardCode;
    private final String standardVersion;
    private final List<String> standardRequirements;
    private final String standardStatus;

    public StepStandardDefinition(int id, String name, String standardType, String standardCode, String standardVersion, List<String> standardRequirements, String standardStatus) {
        this.id = id;
        this.name = name;
        this.standardType = standardType;
        this.standardCode = standardCode;
        this.standardVersion = standardVersion;
        this.standardRequirements = standardRequirements == null ? null : java.util.List.copyOf(standardRequirements);
        this.standardStatus = standardStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStandardType() {
        return standardType;
    }

    public String getStandardCode() {
        return standardCode;
    }

    public String getStandardVersion() {
        return standardVersion;
    }

    public List<String> getStandardRequirements() {
        return standardRequirements;
    }

    public String getStandardStatus() {
        return standardStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepStandardDefinition that = (StepStandardDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(standardType, that.standardType) && Objects.equals(standardCode, that.standardCode) && Objects.equals(standardVersion, that.standardVersion) && Objects.equals(standardRequirements, that.standardRequirements) && Objects.equals(standardStatus, that.standardStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, standardType, standardCode, standardVersion, standardRequirements, standardStatus);
    }

    @Override
    public String toString() {
        return "StepStandardDefinition{" + "id=" + id + "name=" + name + "standardType=" + standardType + "standardCode=" + standardCode + "standardVersion=" + standardVersion + "standardRequirements=" + standardRequirements + "standardStatus=" + standardStatus + "}";
    }
}