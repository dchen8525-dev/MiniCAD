package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved ANNOTATION_RECORD.
 * An annotation record entity.
 *
 * @param id STEP instance id
 * @param name annotation name
 * @param annotationType annotation variance type
 * @param annotationText annotation variance text
 * @param annotationTarget annotation variance target reference
 * @param annotationAuthor annotation variance author reference
 * @param annotationTimestamp annotation variance timestamp
 * @param annotationStatus annotation variance status
 */
public final class StepAnnotationRecord extends AbstractStepEntity {
    private final String annotationType;
    private final String annotationText;
    private final StepEntity annotationTarget;
    private final StepEntity annotationAuthor;
    private final StepEntity annotationTimestamp;
    private final String annotationStatus;

    public StepAnnotationRecord(int id, String name, String annotationType, String annotationText, StepEntity annotationTarget, StepEntity annotationAuthor, StepEntity annotationTimestamp, String annotationStatus) {
        super(id, name);
        this.annotationType = annotationType;
        this.annotationText = annotationText;
        this.annotationTarget = annotationTarget;
        this.annotationAuthor = annotationAuthor;
        this.annotationTimestamp = annotationTimestamp;
        this.annotationStatus = annotationStatus;
    }

    public String getAnnotationType() {
        return annotationType;
    }

    public String getAnnotationText() {
        return annotationText;
    }

    public StepEntity getAnnotationTarget() {
        return annotationTarget;
    }

    public StepEntity getAnnotationAuthor() {
        return annotationAuthor;
    }

    public StepEntity getAnnotationTimestamp() {
        return annotationTimestamp;
    }

    public String getAnnotationStatus() {
        return annotationStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("annotationType", annotationType);
        state.put("annotationText", annotationText);
        state.put("annotationTarget", annotationTarget);
        state.put("annotationAuthor", annotationAuthor);
        state.put("annotationTimestamp", annotationTimestamp);
        state.put("annotationStatus", annotationStatus);
        return state;
    }
}
