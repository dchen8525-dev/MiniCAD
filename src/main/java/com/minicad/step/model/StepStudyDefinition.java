package com.minicad.step.model;

import java.util.List;
import java.util.Map;

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
public final class StepStudyDefinition extends AbstractStepEntity {
    private final String studyType;
    private final String studyObjective;
    private final String studyMethodology;
    private final List<String> studyParameters;
    private final String studyStatus;

    public StepStudyDefinition(int id, String name, String studyType, String studyObjective, String studyMethodology, List<String> studyParameters, String studyStatus) {
        super(id, name);
        this.studyType = studyType;
        this.studyObjective = studyObjective;
        this.studyMethodology = studyMethodology;
        this.studyParameters = studyParameters == null ? null : java.util.List.copyOf(studyParameters);
        this.studyStatus = studyStatus;
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
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "studyType", studyType, "studyObjective", studyObjective,
                "studyMethodology", studyMethodology, "studyParameters", studyParameters,
                "studyStatus", studyStatus);
    }
}
