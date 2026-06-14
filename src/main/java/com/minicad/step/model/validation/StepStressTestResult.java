package com.minicad.step.model.validation;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepStressTestResult implements StepEntity {
    private final int id;
    private final String name;
    private final String testType;
    private final double testValue;
    private final StepEntity testUnit;
    private final double testLimit;
    private final boolean testPass;
    private final String testStatus;

    public StepStressTestResult(int id, String name, String testType, double testValue, StepEntity testUnit, double testLimit, boolean testPass, String testStatus) {
        this.id = id;
        this.name = name;
        this.testType = testType;
        this.testValue = testValue;
        this.testUnit = testUnit;
        this.testLimit = testLimit;
        this.testPass = testPass;
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

    public double getTestValue() {
        return testValue;
    }

    public StepEntity getTestUnit() {
        return testUnit;
    }

    public double getTestLimit() {
        return testLimit;
    }

    public boolean isTestPass() {
        return testPass;
    }

    public String getTestStatus() {
        return testStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepStressTestResult that = (StepStressTestResult) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(testType, that.testType) && testValue == that.testValue && Objects.equals(testUnit, that.testUnit) && testLimit == that.testLimit && testPass == that.testPass && Objects.equals(testStatus, that.testStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, testType, testValue, testUnit, testLimit, testPass, testStatus);
    }

    @Override
    public String toString() {
        return "StepStressTestResult{" + "id=" + id + "name=" + name + "testType=" + testType + "testValue=" + testValue + "testUnit=" + testUnit + "testLimit=" + testLimit + "testPass=" + testPass + "testStatus=" + testStatus + "}";
    }
}