package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepAnnotationRecord(
    int id,
    String name,
    String annotationType,
    String annotationText,
    StepEntity annotationTarget,
    StepEntity annotationAuthor,
    StepEntity annotationTimestamp,
    String annotationStatus) implements StepEntity {
}