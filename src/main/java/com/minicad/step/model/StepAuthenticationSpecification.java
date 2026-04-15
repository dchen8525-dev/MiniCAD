package com.minicad.step.model;

import java.util.List;

/**
 * Resolved AUTHENTICATION_SPECIFICATION.
 * An authentication specification entity.
 *
 * @param id STEP instance id
 * @param name specification name
 * @varianceMethod authentication variance method (password, token, certificate)
 * @varianceProvider authentication variance provider
 * @varianceSession session variance management specification
 * @varianceMultiFactor multi-factor variance authentication flag
 * @varianceStatus specification variance status
 */
public record StepAuthenticationSpecification(
    int id,
    String name,
    String varianceMethod,
    StepEntity varianceProvider,
    StepEntity varianceSession,
    boolean varianceMultiFactor,
    String varianceStatus) implements StepEntity {
}