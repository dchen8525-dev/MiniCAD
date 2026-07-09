package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved STUDY_DEFINITION.
 * A study definition entity.
 *
 * @param id STEP instance id
 * @param name study name
 * @param studyType study variance type
 * @param studyObjective study variance objective
 * @param studyMethodology study variance methodology
 * @param studyParameters study variance parameters
 * @param studyStatus study variance status
 */
/**
 * Resolved STUDY_DEFINITION.
 * A study definition entity.
 *
 * @param id STEP instance id
 * @param name study name
 * @param studyType study variance type
 * @param studyObjective study variance objective
 * @param studyMethodology study variance methodology
 * @param studyParameters study variance parameters
 * @param studyStatus study variance status
 */
public final class StepStudyDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String studyType;
    private final String studyObjective;
    private final String studyMethodology;
    private final List<String> studyParameters;
    private final String studyStatus;

    public StepStudyDefinition(int id, String name, String studyType, String studyObjective, String studyMethodology, List<String> studyParameters, String studyStatus) {
        this.id = id;
        this.name = name;
        this.studyType = studyType;
        this.studyObjective = studyObjective;
        this.studyMethodology = studyMethodology;
        this.studyParameters = studyParameters == null ? null : java.util.List.copyOf(studyParameters);
        this.studyStatus = studyStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStudyType() {
        return studyType;
    }

    public String getStudyObjective() {
        return studyObjective;
    }

    public String getStudyMethodology() {
        return studyMethodology;
    }

    public List<String> getStudyParameters() {
        return studyParameters;
    }

    public String getStudyStatus() {
        return studyStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepStudyDefinition that = (StepStudyDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(studyType, that.studyType) && Objects.equals(studyObjective, that.studyObjective) && Objects.equals(studyMethodology, that.studyMethodology) && Objects.equals(studyParameters, that.studyParameters) && Objects.equals(studyStatus, that.studyStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, studyType, studyObjective, studyMethodology, studyParameters, studyStatus);
    }

    @Override
    public String toString() {
        return "StepStudyDefinition{" + "id=" + id + "name=" + name + "studyType=" + studyType + "studyObjective=" + studyObjective + "studyMethodology=" + studyMethodology + "studyParameters=" + studyParameters + "studyStatus=" + studyStatus + "}";
    }
}