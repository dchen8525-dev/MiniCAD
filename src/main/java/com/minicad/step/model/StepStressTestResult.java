package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepStressTestResult extends AbstractStepEntity {
    private final String testType;
    private final double testValue;
    private final StepEntity testUnit;
    private final double testLimit;
    private final boolean testPass;
    private final String testStatus;

    public StepStressTestResult(int id, String name, String testType, double testValue, StepEntity testUnit, double testLimit, boolean testPass, String testStatus) {
        super(id, name);
        this.testType = testType;
        this.testValue = testValue;
        this.testUnit = testUnit;
        this.testLimit = testLimit;
        this.testPass = testPass;
        this.testStatus = testStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("testType", testType);
        state.put("testValue", testValue);
        state.put("testUnit", testUnit);
        state.put("testLimit", testLimit);
        state.put("testPass", testPass);
        state.put("testStatus", testStatus);
        return state;
    }
}
