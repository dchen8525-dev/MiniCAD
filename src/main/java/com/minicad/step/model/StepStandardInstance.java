package com.minicad.step.model;

import java.util.List;

/**
 * Resolved STANDARD_INSTANCE.
 * A standard instance entity.
 *
 * @param id STEP instance id
 * @param name standard instance name
 * @param standardDefinition standard variance definition reference
 * @param standardCompliance standard variance compliance level
 * @param standardCertifications standard variance certifications
 * @param standardStatus standard variance status
 */
public record StepStandardInstance(
    int id,
    String name,
    StepEntity standardDefinition,
    String standardCompliance,
    List<StepEntity> standardCertifications,
    String standardStatus) implements StepEntity {
}