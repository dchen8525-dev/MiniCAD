package com.minicad.step.model.fea;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved LOAD_CASE.
 * A load case entity.
 *
 * @param id STEP instance id
 * @param name load case name
 * @param caseType case variance type
 * @param caseLoads case variance load definitions
 * @param caseDescription case variance description
 * @param caseStatus case variance status
 */
/**
 * Resolved LOAD_CASE.
 * A load case entity.
 *
 * @param id STEP instance id
 * @param name load case name
 * @param caseType case variance type
 * @param caseLoads case variance load definitions
 * @param caseDescription case variance description
 * @param caseStatus case variance status
 */
public final class StepLoadCase implements StepEntity {
    private final int id;
    private final String name;
    private final String caseType;
    private final List<StepEntity> caseLoads;
    private final String caseDescription;
    private final String caseStatus;

    public StepLoadCase(int id, String name, String caseType, List<StepEntity> caseLoads, String caseDescription, String caseStatus) {
        this.id = id;
        this.name = name;
        this.caseType = caseType;
        this.caseLoads = caseLoads == null ? null : java.util.List.copyOf(caseLoads);
        this.caseDescription = caseDescription;
        this.caseStatus = caseStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCaseType() {
        return caseType;
    }

    public List<StepEntity> getCaseLoads() {
        return caseLoads;
    }

    public String getCaseDescription() {
        return caseDescription;
    }

    public String getCaseStatus() {
        return caseStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepLoadCase that = (StepLoadCase) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(caseType, that.caseType) && Objects.equals(caseLoads, that.caseLoads) && Objects.equals(caseDescription, that.caseDescription) && Objects.equals(caseStatus, that.caseStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, caseType, caseLoads, caseDescription, caseStatus);
    }

    @Override
    public String toString() {
        return "StepLoadCase{" + "id=" + id + "name=" + name + "caseType=" + caseType + "caseLoads=" + caseLoads + "caseDescription=" + caseDescription + "caseStatus=" + caseStatus + "}";
    }
}