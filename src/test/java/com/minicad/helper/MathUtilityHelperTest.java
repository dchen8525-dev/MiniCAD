package com.minicad.helper;

import com.minicad.geometry.CartesianPoint;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for MathUtilityHelper.unwrapPeriodic, including the period<=0 guard
 * that prevents an infinite loop, and for MathUtilityHelper.transformCartesian,
 * which is the only home of the 4x4 point transform (a byte-identical copy in
 * preview/sampling/MatrixTransformHelper had no caller and was deleted with it).
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

    @Test
    void transformCartesianIdentityKeepsThePoint() {
        CartesianPoint transformed = MathUtilityHelper.transformCartesian(
                new CartesianPoint(1.0, 2.0, 3.0), identity());

        assertEquals(1.0, transformed.x(), 1e-12);
        assertEquals(2.0, transformed.y(), 1e-12);
        assertEquals(3.0, transformed.z(), 1e-12);
    }

    @Test
    void transformCartesianAppliesTheTranslationColumn() {
        // Translation lives in the last column (m3, m7, m11), not the last row.
        double[] translation = {
                1.0, 0.0, 0.0, 5.0,
                0.0, 1.0, 0.0, 7.0,
                0.0, 0.0, 1.0, 9.0,
                0.0, 0.0, 0.0, 1.0
        };
        CartesianPoint transformed = MathUtilityHelper.transformCartesian(
                new CartesianPoint(1.0, 2.0, 3.0), translation);

        assertEquals(6.0, transformed.x(), 1e-12);
        assertEquals(9.0, transformed.y(), 1e-12);
        assertEquals(12.0, transformed.z(), 1e-12);
    }

    @Test
    void transformCartesianAppliesScale() {
        double[] scale = {
                2.0, 0.0, 0.0, 0.0,
                0.0, 2.0, 0.0, 0.0,
                0.0, 0.0, 2.0, 0.0,
                0.0, 0.0, 0.0, 1.0
        };
        CartesianPoint transformed = MathUtilityHelper.transformCartesian(
                new CartesianPoint(1.0, 2.0, 3.0), scale);

        assertEquals(2.0, transformed.x(), 1e-12);
        assertEquals(4.0, transformed.y(), 1e-12);
        assertEquals(6.0, transformed.z(), 1e-12);
    }

    @Test
    void transformCartesianRotatesAboutZ() {
        double[] rotation = {
                0.0, -1.0, 0.0, 0.0,
                1.0, 0.0, 0.0, 0.0,
                0.0, 0.0, 1.0, 0.0,
                0.0, 0.0, 0.0, 1.0
        };
        CartesianPoint transformed = MathUtilityHelper.transformCartesian(
                new CartesianPoint(1.0, 0.0, 0.0), rotation);

        assertEquals(0.0, transformed.x(), 1e-12);
        assertEquals(1.0, transformed.y(), 1e-12);
        assertEquals(0.0, transformed.z(), 1e-12);
    }

    private static double[] identity() {
        return new double[]{
                1.0, 0.0, 0.0, 0.0,
                0.0, 1.0, 0.0, 0.0,
                0.0, 0.0, 1.0, 0.0,
                0.0, 0.0, 0.0, 1.0
        };
    }
}
