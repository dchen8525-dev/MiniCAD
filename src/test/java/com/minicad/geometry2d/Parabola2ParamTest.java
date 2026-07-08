package com.minicad.geometry2d;

import org.junit.jupiter.api.Test;

public class Parabola2ParamTest {
    @Test
    void testStandardParametrization() {
        // Standard parabola: y = x²/(4f) where f is focal distance
        // Standard parametrization: x = t, y = t²/(4f)
        // But test expects: pointAt(1) = (-4, 2) for f=2

        System.out.println("Testing parameter scaling:");
        System.out.println("Test expects: pointAt(1) = (-4, 2) for focalDistance=2");
        System.out.println("Test expects: pointAt(-1) = (4, 2)");

        // From equation y = x²/(4f):
        // When x = 4, y = 16/(4*2) = 2 ✓
        // When x = -4, y = 2 ✓

        // So the relationship is: t=±1 should give x=±4, y=2
        // This suggests: x = 4*t, y = (4t)²/(4f) = 16t²/(4f) = 4t²/f

        Point2 vertex = new Point2(0, 0);
        Direction2 axisDir = new Direction2(0, 1);
        double focalDistance = 2.0;

        // With x = 4*t and y = 4*t²/f:
        double t = 1.0;
        double x = 4 * t;
        double y = 4 * t * t / focalDistance;
        System.out.println(String.format("Proposed: t=%.1f -> x=%.1f, y=%.1f", t, x, y));

        // Alternative: x = 2*f*t
        t = 1.0;
        x = 2 * focalDistance * t;
        y = (2 * focalDistance * t) * (2 * focalDistance * t) / (4 * focalDistance);
        System.out.println(String.format("Alternative: t=%.1f -> x=%.1f, y=%.1f", t, x, y));
    }
}
