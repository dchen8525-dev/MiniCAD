package com.minicad.step.model;

import java.util.List;

/**
 * Resolved STRESS_TEST_RESULT.
 * A stress test result entity.
 *
 * @param id STEP instance id
 * @param name result name
 * @param testType test variance type
 * @param testValue test variance measured value
 * @param testUnit test variance unit reference
 * @param testLimit test variance limit value
 * @param testPass test variance pass/fail status
 * @param testStatus test variance status
 */
public record StepStressTestResult(
    int id,
    String name,
    String testType,
    double testValue,
    StepEntity testUnit,
    double testLimit,
    boolean testPass,
    String testStatus) implements StepEntity {
}