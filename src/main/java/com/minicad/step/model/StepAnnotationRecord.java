package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepAnnotationRecord implements StepEntity {
    private final int id;
    private final String name;
    private final String annotationType;
    private final String annotationText;
    private final StepEntity annotationTarget;
    private final StepEntity annotationAuthor;
    private final StepEntity annotationTimestamp;
    private final String annotationStatus;

    public StepAnnotationRecord(int id, String name, String annotationType, String annotationText, StepEntity annotationTarget, StepEntity annotationAuthor, StepEntity annotationTimestamp, String annotationStatus) {
        this.id = id;
        this.name = name;
        this.annotationType = annotationType;
        this.annotationText = annotationText;
        this.annotationTarget = annotationTarget;
        this.annotationAuthor = annotationAuthor;
        this.annotationTimestamp = annotationTimestamp;
        this.annotationStatus = annotationStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAnnotationRecord that = (StepAnnotationRecord) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(annotationType, that.annotationType) && Objects.equals(annotationText, that.annotationText) && Objects.equals(annotationTarget, that.annotationTarget) && Objects.equals(annotationAuthor, that.annotationAuthor) && Objects.equals(annotationTimestamp, that.annotationTimestamp) && Objects.equals(annotationStatus, that.annotationStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, annotationType, annotationText, annotationTarget, annotationAuthor, annotationTimestamp, annotationStatus);
    }

    @Override
    public String toString() {
        return "StepAnnotationRecord{" + "id=" + id + "name=" + name + "annotationType=" + annotationType + "annotationText=" + annotationText + "annotationTarget=" + annotationTarget + "annotationAuthor=" + annotationAuthor + "annotationTimestamp=" + annotationTimestamp + "annotationStatus=" + annotationStatus + "}";
    }
}