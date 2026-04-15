package com.minicad.geometry;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Ellipse3 class.
 */
class Ellipse3Test {

    @Test
    void ellipsePointAt() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Ellipse3 ellipse = new Ellipse3(position, 4.0, 2.0);

        // Point at angle=0 should be at (semiAxis1, 0, 0)
        CartesianPoint p0 = ellipse.pointAt(0);
        assertEquals(4.0, p0.x(), 1e-10);
        assertEquals(0.0, p0.y(), 1e-10);
        assertEquals(0.0, p0.z(), 1e-10);

        // Point at angle=PI/2 should be at (0, semiAxis2, 0)
        CartesianPoint p90 = ellipse.pointAt(Math.PI / 2);
        assertEquals(0.0, p90.x(), 1e-10);
        assertEquals(2.0, p90.y(), 1e-10);
        assertEquals(0.0, p90.z(), 1e-10);

        // Point at angle=PI should be at (-semiAxis1, 0, 0)
        CartesianPoint p180 = ellipse.pointAt(Math.PI);
        assertEquals(-4.0, p180.x(), 1e-10);
        assertEquals(0.0, p180.y(), 1e-10);
        assertEquals(0.0, p180.z(), 1e-10);
    }

    @Test
    void ellipseContains() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Ellipse3 ellipse = new Ellipse3(position, 4.0, 2.0);

        // Points on the ellipse
        assertTrue(ellipse.contains(new CartesianPoint(4, 0, 0)));
        assertTrue(ellipse.contains(new CartesianPoint(0, 2, 0)));
        assertTrue(ellipse.contains(new CartesianPoint(-4, 0, 0)));

        // Point not on the ellipse
        assertFalse(ellipse.contains(new CartesianPoint(2, 1, 0)));
        assertFalse(ellipse.contains(new CartesianPoint(0, 0, 1)));
    }

    @Test
    void ellipseSample() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Ellipse3 ellipse = new Ellipse3(position, 4.0, 2.0);

        java.util.List<CartesianPoint> samples = ellipse.sample(4);
        assertEquals(5, samples.size());  // 4 segments = 5 points

        // First point should be at angle 0
        assertEquals(4.0, samples.get(0).x(), 1e-10);
        assertEquals(0.0, samples.get(0).y(), 1e-10);

        // Last point should be back at angle 2PI
        assertEquals(4.0, samples.get(4).x(), 1e-10);
        assertEquals(0.0, samples.get(4).y(), 1e-10);
    }

    @Test
    void ellipseArcSample() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Ellipse3 ellipse = new Ellipse3(position, 4.0, 2.0);

        java.util.List<CartesianPoint> samples = ellipse.sample(4, 0, Math.PI / 2);
        assertEquals(5, samples.size());
        assertEquals(4.0, samples.get(0).x(), 1e-10);
        assertEquals(0.0, samples.get(0).y(), 1e-10);
        assertEquals(0.0, samples.get(4).x(), 1e-10);
        assertEquals(2.0, samples.get(4).y(), 1e-10);
    }

    @Test
    void ellipseTangentAt() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Ellipse3 ellipse = new Ellipse3(position, 4.0, 2.0);

        // Tangent at angle=0 should point along +Y
        Vector3 t0 = ellipse.tangentAt(0);
        assertEquals(0.0, t0.x(), 1e-10);
        assertEquals(1.0, t0.y(), 1e-10);
        assertEquals(0.0, t0.z(), 1e-10);

        // Tangent at angle=PI/2 should point along -X
        Vector3 t90 = ellipse.tangentAt(Math.PI / 2);
        assertEquals(-1.0, t90.x(), 1e-10);
        assertEquals(0.0, t90.y(), 1e-10);
        assertEquals(0.0, t90.z(), 1e-10);
    }

    @Test
    void ellipseNormalInPlaneAt() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Ellipse3 ellipse = new Ellipse3(position, 4.0, 2.0);

        // Normal at angle=0 should point along +X
        Vector3 n0 = ellipse.normalInPlaneAt(0);
        assertEquals(1.0, n0.x(), 1e-10);
        assertEquals(0.0, n0.y(), 1e-10);
        assertEquals(0.0, n0.z(), 1e-10);

        // Normal at angle=PI/2 should point along +Y
        Vector3 n90 = ellipse.normalInPlaneAt(Math.PI / 2);
        assertEquals(0.0, n90.x(), 1e-10);
        assertEquals(1.0, n90.y(), 1e-10);
        assertEquals(0.0, n90.z(), 1e-10);
    }

    @Test
    void ellipseCurvatureAt() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Ellipse3 ellipse = new Ellipse3(position, 4.0, 2.0);

        // Curvature at angle=0 (at major axis endpoint): k = a/b^2 = 4/4 = 1
        double k0 = ellipse.curvatureAt(0);
        assertEquals(1.0, k0, 1e-10);

        // Curvature at angle=PI/2 (at minor axis endpoint): k = b/a^2 = 2/16 = 0.125
        double k90 = ellipse.curvatureAt(Math.PI / 2);
        assertEquals(0.125, k90, 1e-10);
    }

    @Test
    void ellipseBinormalAt() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Ellipse3 ellipse = new Ellipse3(position, 4.0, 2.0);

        Vector3 binormal = ellipse.binormalAt(0);
        assertEquals(0.0, binormal.x(), 1e-10);
        assertEquals(0.0, binormal.y(), 1e-10);
        assertEquals(1.0, binormal.z(), 1e-10);
    }

    @Test
    void ellipsePerimeter() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Ellipse3 ellipse = new Ellipse3(position, 4.0, 2.0);

        // Using Ramanujan's formula, approximate perimeter
        double perimeter = ellipse.perimeter();
        assertTrue(perimeter > 0);
        // Approximate value: perimeter ~ 2*PI * sqrt((a^2+b^2)/2) ~ 19.4
        assertTrue(perimeter > 15 && perimeter < 25);
    }

    @Test
    void ellipseBoundingBox() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Ellipse3 ellipse = new Ellipse3(position, 4.0, 2.0);

        BoundingBox3 box = ellipse.boundingBox();
        assertEquals(-4.0, box.minX(), 1e-10);
        assertEquals(-2.0, box.minY(), 1e-10);
        assertEquals(4.0, box.maxX(), 1e-10);
        assertEquals(2.0, box.maxY(), 1e-10);
        assertEquals(0.0, box.minZ(), 1e-10);
        assertEquals(0.0, box.maxZ(), 1e-10);
    }

    @Test
    void ellipseBoundingBoxOffset() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(10, 20, 5),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Ellipse3 ellipse = new Ellipse3(position, 4.0, 2.0);

        BoundingBox3 box = ellipse.boundingBox();
        assertEquals(6.0, box.minX(), 1e-10);  // 10 - 4
        assertEquals(18.0, box.minY(), 1e-10); // 20 - 2
        assertEquals(14.0, box.maxX(), 1e-10); // 10 + 4
        assertEquals(22.0, box.maxY(), 1e-10); // 20 + 2
        assertEquals(5.0, box.minZ(), 1e-10);
        assertEquals(5.0, box.maxZ(), 1e-10);
    }

    @Test
    void ellipseArcBoundingBox() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Ellipse3 ellipse = new Ellipse3(position, 4.0, 2.0);

        // Arc from 0 to PI/2
        BoundingBox3 box = ellipse.boundingBox(0, Math.PI / 2);
        assertTrue(box.minX() >= -1);
        assertTrue(box.maxX() <= 5);
        assertTrue(box.minY() >= -1);
        assertTrue(box.maxY() <= 3);
    }

    @Test
    void ellipseClosestPointTo() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Ellipse3 ellipse = new Ellipse3(position, 4.0, 2.0);

        // Point outside ellipse along major axis
        CartesianPoint outside = new CartesianPoint(10, 0, 0);
        CartesianPoint closest = ellipse.closestPointTo(outside);
        assertEquals(4.0, closest.x(), 0.1);
        assertEquals(0.0, closest.y(), 0.1);

        // Point inside ellipse
        CartesianPoint inside = new CartesianPoint(1, 1, 0);
        CartesianPoint closestInside = ellipse.closestPointTo(inside);
        assertTrue(ellipse.contains(closestInside) || closestInside.distanceTo(inside) < 1.0);
    }

    @Test
    void ellipseDistanceTo() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Ellipse3 ellipse = new Ellipse3(position, 4.0, 2.0);

        // Point outside at (10, 0, 0) - distance should be approx 6
        CartesianPoint outside = new CartesianPoint(10, 0, 0);
        double distance = ellipse.distanceTo(outside);
        assertEquals(6.0, distance, 0.1);
    }

    @Test
    void ellipseAngleOf() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Ellipse3 ellipse = new Ellipse3(position, 4.0, 2.0);

        // Angle at (4, 0, 0) should be 0
        CartesianPoint p0 = new CartesianPoint(4, 0, 0);
        assertEquals(0.0, ellipse.angleOf(p0), 1e-10);

        // Angle at (0, 2, 0) should be PI/2
        CartesianPoint p90 = new CartesianPoint(0, 2, 0);
        assertEquals(Math.PI / 2, ellipse.angleOf(p90), 1e-10);
    }

    @Test
    void ellipseIsCircleWhenAxesEqual() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Ellipse3 ellipse = new Ellipse3(position, 5.0, 5.0);

        // Perimeter should equal circumference of circle
        assertEquals(2 * Math.PI * 5, ellipse.perimeter(), 0.01);

        // Bounding box should be square (width = height = 10)
        BoundingBox3 box = ellipse.boundingBox();
        assertEquals(10.0, box.width(), 1e-10);  // maxX - minX = 5 - (-5) = 10
        assertEquals(10.0, box.height(), 1e-10);
    }
}