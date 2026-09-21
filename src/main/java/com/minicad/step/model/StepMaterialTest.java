package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepMaterialTest extends AbstractStepEntity {
    private final String testType;
    private final StepEntity testSample;
    private final List<Double> testParameters;
    private final List<Double> testResults;
    private final String testStandard;
    private final StepEntity testDate;
    private final String testStatus;

    public StepMaterialTest(int id, String name, String testType, StepEntity testSample, List<Double> testParameters, List<Double> testResults, String testStandard, StepEntity testDate, String testStatus) {
        super(id, name);
        this.testType = testType;
        this.testSample = testSample;
        this.testParameters = testParameters == null ? null : java.util.List.copyOf(testParameters);
        this.testResults = testResults == null ? null : java.util.List.copyOf(testResults);
        this.testStandard = testStandard;
        this.testDate = testDate;
        this.testStatus = testStatus;
    }

    public String getTestType() {
        return testType;
    }

    public StepEntity getTestSample() {
        return testSample;
    }

    public List<Double> getTestParameters() {
        return testParameters;
    }

    public List<Double> getTestResults() {
        return testResults;
    }

    public String getTestStandard() {
        return testStandard;
    }

    public StepEntity getTestDate() {
        return testDate;
    }

    public String getTestStatus() {
        return testStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("testType", testType);
        state.put("testSample", testSample);
        state.put("testParameters", testParameters);
        state.put("testResults", testResults);
        state.put("testStandard", testStandard);
        state.put("testDate", testDate);
        state.put("testStatus", testStatus);
        return state;
    }
}
