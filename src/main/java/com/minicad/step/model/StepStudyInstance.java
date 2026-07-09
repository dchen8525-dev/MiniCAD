package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved STUDY_INSTANCE.
 * A study instance entity.
 *
 * @param id STEP instance id
 * @param name study instance name
 * @param studyDefinition study variance definition reference
 * @param studyState study variance state
 * @param studyStartTime study variance start time
 * @param studyEndTime study variance end time
 * @param studyResults study variance results
 * @param studyStatus study variance status
 */
/**
 * Resolved STUDY_INSTANCE.
 * A study instance entity.
 *
 * @param id STEP instance id
 * @param name study instance name
 * @param studyDefinition study variance definition reference
 * @param studyState study variance state
 * @param studyStartTime study variance start time
 * @param studyEndTime study variance end time
 * @param studyResults study variance results
 * @param studyStatus study variance status
 */
public final class StepStudyInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity studyDefinition;
    private final String studyState;
    private final StepEntity studyStartTime;
    private final StepEntity studyEndTime;
    private final List<StepEntity> studyResults;
    private final String studyStatus;

    public StepStudyInstance(int id, String name, StepEntity studyDefinition, String studyState, StepEntity studyStartTime, StepEntity studyEndTime, List<StepEntity> studyResults, String studyStatus) {
        this.id = id;
        this.name = name;
        this.studyDefinition = studyDefinition;
        this.studyState = studyState;
        this.studyStartTime = studyStartTime;
        this.studyEndTime = studyEndTime;
        this.studyResults = studyResults == null ? null : java.util.List.copyOf(studyResults);
        this.studyStatus = studyStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getStudyDefinition() {
        return studyDefinition;
    }

    public String getStudyState() {
        return studyState;
    }

    public StepEntity getStudyStartTime() {
        return studyStartTime;
    }

    public StepEntity getStudyEndTime() {
        return studyEndTime;
    }

    public List<StepEntity> getStudyResults() {
        return studyResults;
    }

    public String getStudyStatus() {
        return studyStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepStudyInstance that = (StepStudyInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(studyDefinition, that.studyDefinition) && Objects.equals(studyState, that.studyState) && Objects.equals(studyStartTime, that.studyStartTime) && Objects.equals(studyEndTime, that.studyEndTime) && Objects.equals(studyResults, that.studyResults) && Objects.equals(studyStatus, that.studyStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, studyDefinition, studyState, studyStartTime, studyEndTime, studyResults, studyStatus);
    }

    @Override
    public String toString() {
        return "StepStudyInstance{" + "id=" + id + "name=" + name + "studyDefinition=" + studyDefinition + "studyState=" + studyState + "studyStartTime=" + studyStartTime + "studyEndTime=" + studyEndTime + "studyResults=" + studyResults + "studyStatus=" + studyStatus + "}";
    }
}