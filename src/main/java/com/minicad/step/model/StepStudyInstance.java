package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepStudyInstance extends AbstractStepEntity {
    private final StepEntity studyDefinition;
    private final String studyState;
    private final StepEntity studyStartTime;
    private final StepEntity studyEndTime;
    private final List<StepEntity> studyResults;
    private final String studyStatus;

    public StepStudyInstance(int id, String name, StepEntity studyDefinition, String studyState, StepEntity studyStartTime, StepEntity studyEndTime, List<StepEntity> studyResults, String studyStatus) {
        super(id, name);
        this.studyDefinition = studyDefinition;
        this.studyState = studyState;
        this.studyStartTime = studyStartTime;
        this.studyEndTime = studyEndTime;
        this.studyResults = studyResults == null ? null : java.util.List.copyOf(studyResults);
        this.studyStatus = studyStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("studyDefinition", studyDefinition);
        state.put("studyState", studyState);
        state.put("studyStartTime", studyStartTime);
        state.put("studyEndTime", studyEndTime);
        state.put("studyResults", studyResults);
        state.put("studyStatus", studyStatus);
        return state;
    }
}
