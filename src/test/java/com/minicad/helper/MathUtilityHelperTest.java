package com.minicad.helper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for MathUtilityHelper.unwrapPeriodic, including the period<=0 guard
 * that prevents an infinite loop.
 */
class MathUtilityHelperTest {

    private static final double TAU = 2.0 * Math.PI;

    @Test
    void unwrapsForwardAcrossPeriod() {
        double result = MathUtilityHelper.unwrapPeriodic(Math.PI * 1.5, 0.0, TAU);
        assertEquals(-Math.PI / 2.0, result, 1e-12);
    }

    @Test
    void unwrapsBackwardAcrossPeriod() {
        double result = MathUtilityHelper.unwrapPeriodic(-Math.PI * 1.5, 0.0, TAU);
        assertEquals(Math.PI / 2.0, result, 1e-12);
    }

    @Test
    void returnsValueWhenPreviousNull() {
        assertEquals(1.23, MathUtilityHelper.unwrapPeriodic(1.23, null, TAU), 1e-12);
    }

    @Test
    void nonPositivePeriodDoesNotHangAndReturnsInput() {
        // Regression: period <= 0 previously spun forever in a while loop.
        double value = 5.0;
        assertEquals(value, MathUtilityHelper.unwrapPeriodic(value, 0.0, 0.0), 1e-12);
        assertEquals(value, MathUtilityHelper.unwrapPeriodic(value, 0.0, -1.0), 1e-12);
    }

    @Test
    void nonFinitePeriodReturnsInput() {
        assertEquals(2.0, MathUtilityHelper.unwrapPeriodic(2.0, 1.0, Double.NaN), 1e-12);
    }
}
