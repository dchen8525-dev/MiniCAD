package com.minicad.step.model;

import java.util.List;

/**
 * Resolved MATERIAL_TEST.
 * A material test entity.
 *
 * @param id STEP instance id
 * @param name test name
 * @param testType material test type (tensile, hardness, impact)
 * @param testSample test sample reference
 * @param testParameters test parameters
 * @param testResults test result values
 * @param testStandard test standard reference
 * @param testDate test execution date
 * @param testStatus test status result
 */
public record StepMaterialTest(
    int id,
    String name,
    String testType,
    StepEntity testSample,
    List<Double> testParameters,
    List<Double> testResults,
    String testStandard,
    StepEntity testDate,
    String testStatus) implements StepEntity {
}