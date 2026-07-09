package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepMaterialTest implements StepEntity {
    private final int id;
    private final String name;
    private final String testType;
    private final StepEntity testSample;
    private final List<Double> testParameters;
    private final List<Double> testResults;
    private final String testStandard;
    private final StepEntity testDate;
    private final String testStatus;

    public StepMaterialTest(int id, String name, String testType, StepEntity testSample, List<Double> testParameters, List<Double> testResults, String testStandard, StepEntity testDate, String testStatus) {
        this.id = id;
        this.name = name;
        this.testType = testType;
        this.testSample = testSample;
        this.testParameters = testParameters == null ? null : java.util.List.copyOf(testParameters);
        this.testResults = testResults == null ? null : java.util.List.copyOf(testResults);
        this.testStandard = testStandard;
        this.testDate = testDate;
        this.testStatus = testStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepMaterialTest that = (StepMaterialTest) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(testType, that.testType) && Objects.equals(testSample, that.testSample) && Objects.equals(testParameters, that.testParameters) && Objects.equals(testResults, that.testResults) && Objects.equals(testStandard, that.testStandard) && Objects.equals(testDate, that.testDate) && Objects.equals(testStatus, that.testStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, testType, testSample, testParameters, testResults, testStandard, testDate, testStatus);
    }

    @Override
    public String toString() {
        return "StepMaterialTest{" + "id=" + id + "name=" + name + "testType=" + testType + "testSample=" + testSample + "testParameters=" + testParameters + "testResults=" + testResults + "testStandard=" + testStandard + "testDate=" + testDate + "testStatus=" + testStatus + "}";
    }
}