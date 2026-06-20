package com.minicad.geometry2d;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Parabola2GeometryTest {
    @Test
    void checkPerpendicularOrientation() {
        Direction2 yAxis = new Direction2(0, 1);
        Direction2 perp = yAxis.perpendicular();
        
        System.out.println("Y-axis: " + yAxis);
        System.out.println("Perpendicular: " + perp);
        
        // Test parabola containment to verify geometry
        Point2 vertex = new Point2(0, 0);
        Parabola2 parabola = new Parabola2(vertex, yAxis, 2.0);
        
        // Test point at x=4, should be at y=2 according to parabola equation
        Point2 testPoint = new Point2(4, 2);
        System.out.println("Point (4,2) on parabola: " + parabola.contains(testPoint));
        
        // Find what t value gives x=4
        Point2 p = parabola.pointAt(-4);
        System.out.println("pointAt(-4): " + p);
        
        p = parabola.pointAt(4);
        System.out.println("pointAt(4): " + p);
    }
}
