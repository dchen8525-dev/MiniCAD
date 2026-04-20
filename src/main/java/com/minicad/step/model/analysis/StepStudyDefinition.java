package com.minicad.step.model.analysis;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepStudyDefinition(
    int id,
    String name,
    String studyType,
    String studyObjective,
    String studyMethodology,
    List<String> studyParameters,
    String studyStatus) implements StepEntity {
}