package com.minicad.geometry2d;

import org.junit.jupiter.api.Test;

import java.util.List;

public class Parabola2DebugTest {
    @Test
    void debugParabolaPointAt() {
        Point2 vertex = new Point2(0, 0);
        Direction2 axisDir = new Direction2(0, 1);
        Parabola2 parabola = new Parabola2(vertex, axisDir, 2.0);

        System.out.println("Testing parabola with focalDistance=2.0:");
        for (double t = -2; t <= 2; t += 0.5) {
            Point2 p = parabola.pointAt(t);
            System.out.println(String.format("t=%.1f: point=(%.4f, %.4f)", t, p.x(), p.y()));
        }

        System.out.println("\nSample(4):");
        List<Point2> samples = parabola.sample(4);
        for (int i = 0; i < samples.size(); i++) {
            Point2 p = samples.get(i);
            System.out.println(String.format("  sample[%d]=(%.4f, %.4f)", i, p.x(), p.y()));
        }

        System.out.println("\nAxis direction: " + axisDir);
        System.out.println("Perpendicular: " + axisDir.perpendicular());
    }
}
