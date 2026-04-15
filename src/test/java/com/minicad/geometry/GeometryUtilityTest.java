package com.minicad.geometry;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for geometry utility methods.
 */
class GeometryUtilityTest {

    @Test
    void pointMidpoint() {
        CartesianPoint p1 = new CartesianPoint(0, 0, 0);
        CartesianPoint p2 = new CartesianPoint(10, 10, 10);
        CartesianPoint mid = p1.midpoint(p2);
        assertEquals(5.0, mid.x());
        assertEquals(5.0, mid.y());
        assertEquals(5.0, mid.z());
    }

    @Test
    void pointInterpolate() {
        CartesianPoint p1 = new CartesianPoint(0, 0, 0);
        CartesianPoint p2 = new CartesianPoint(10, 0, 0);
        CartesianPoint quarter = p1.interpolate(p2, 0.25);
        assertEquals(2.5, quarter.x());
        CartesianPoint half = p1.interpolate(p2, 0.5);
        assertEquals(5.0, half.x());
        CartesianPoint threeQuarters = p1.interpolate(p2, 0.75);
        assertEquals(7.5, threeQuarters.x());
    }

    @Test
    void pointProjectOntoLine() {
        CartesianPoint point = new CartesianPoint(5, 5, 0);
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        CartesianPoint projected = point.projectOnto(line);
        assertEquals(5.0, projected.x());
        assertEquals(0.0, projected.y());
        assertEquals(0.0, projected.z());
    }

    @Test
    void pointProjectOntoPlane() {
        CartesianPoint point = new CartesianPoint(5, 5, 10);
        Plane plane = new Plane(new CartesianPoint(0, 0, 0), new Direction3(0, 0, 1));
        CartesianPoint projected = point.projectOnto(plane);
        assertEquals(5.0, projected.x());
        assertEquals(5.0, projected.y());
        assertEquals(0.0, projected.z());
    }

    @Test
    void lineClosestPointTo() {
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        CartesianPoint point = new CartesianPoint(5, 10, 0);
        CartesianPoint closest = line.closestPointTo(point);
        assertEquals(5.0, closest.x());
        assertEquals(0.0, closest.y());
        assertEquals(0.0, closest.z());
    }

    @Test
    void lineParameterOfClosestPoint() {
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        CartesianPoint point = new CartesianPoint(10, 5, 0);
        double param = line.parameterOfClosestPoint(point);
        assertEquals(10.0, param);
    }

    @Test
    void planeClosestPointTo() {
        Plane plane = new Plane(new CartesianPoint(0, 0, 5), new Direction3(0, 0, 1));
        CartesianPoint point = new CartesianPoint(10, 10, 20);
        CartesianPoint closest = plane.closestPointTo(point);
        assertEquals(10.0, closest.x());
        assertEquals(10.0, closest.y());
        assertEquals(5.0, closest.z());
    }

    @Test
    void circleClosestPointTo() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Circle circle = new Circle(position, 5.0);
        // Point at (10, 0, 0) should find closest point at angle 0
        CartesianPoint point = new CartesianPoint(10, 0, 0);
        CartesianPoint closest = circle.closestPointTo(point);
        assertEquals(5.0, closest.x(), 0.001);
        assertEquals(0.0, closest.y(), 0.001);
        assertEquals(0.0, closest.z(), 0.001);
    }

    @Test
    void circleDistanceTo() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Circle circle = new Circle(position, 5.0);
        // Point at (10, 0, 0) should be 5 units away from circle (distance = 10 - 5 = 5)
        CartesianPoint point = new CartesianPoint(10, 0, 0);
        double distance = circle.distanceTo(point);
        assertEquals(5.0, distance, 0.001);
    }

    @Test
    void pointSubtractVector() {
        CartesianPoint p = new CartesianPoint(10, 10, 10);
        Vector3 v = new Vector3(3, 4, 5);
        CartesianPoint result = p.subtractVector(v);
        assertEquals(7.0, result.x(), 1e-10);
        assertEquals(6.0, result.y(), 1e-10);
        assertEquals(5.0, result.z(), 1e-10);
    }

    @Test
    void pointDistanceSquaredTo() {
        CartesianPoint p1 = new CartesianPoint(0, 0, 0);
        CartesianPoint p2 = new CartesianPoint(3, 4, 5);
        double squared = p1.distanceSquaredTo(p2);
        assertEquals(50.0, squared, 1e-10);  // 9 + 16 + 25 = 50
    }

    @Test
    void pointOffsetDirection() {
        CartesianPoint p = new CartesianPoint(0, 0, 0);
        Direction3 d = new Direction3(1, 0, 0);
        CartesianPoint offset = p.offset(d, 10);
        assertEquals(10.0, offset.x(), 1e-10);
        assertEquals(0.0, offset.y(), 1e-10);
        assertEquals(0.0, offset.z(), 1e-10);
    }

    @Test
    void pointOffsetVector() {
        CartesianPoint p = new CartesianPoint(5, 10, 15);
        Vector3 v = new Vector3(3, 4, 5);
        CartesianPoint offset = p.offset(v);
        assertEquals(8.0, offset.x(), 1e-10);
        assertEquals(14.0, offset.y(), 1e-10);
        assertEquals(20.0, offset.z(), 1e-10);
    }

    @Test
    void pointApproxEquals() {
        CartesianPoint p1 = new CartesianPoint(0.0, 0.0, 0.0);
        CartesianPoint p2 = new CartesianPoint(0.0001, 0.0001, 0.0001);
        assertTrue(p1.approxEquals(p2, 0.001));

        CartesianPoint p3 = new CartesianPoint(1.0, 0.0, 0.0);
        assertFalse(p1.approxEquals(p3, 0.001));
    }

    @Test
    void pointScaleAbout() {
        CartesianPoint origin = new CartesianPoint(0, 0, 0);
        CartesianPoint p = new CartesianPoint(10, 20, 30);
        CartesianPoint scaled = p.scaleAbout(origin, 2);
        assertEquals(20.0, scaled.x(), 1e-10);
        assertEquals(40.0, scaled.y(), 1e-10);
        assertEquals(60.0, scaled.z(), 1e-10);
    }

    @Test
    void pointMirrorThrough() {
        Plane plane = new Plane(new CartesianPoint(0, 0, 0), new Direction3(0, 0, 1));
        CartesianPoint p = new CartesianPoint(10, 10, 10);
        CartesianPoint mirrored = p.mirrorThrough(plane);
        assertEquals(10.0, mirrored.x(), 1e-10);
        assertEquals(10.0, mirrored.y(), 1e-10);
        assertEquals(-10.0, mirrored.z(), 1e-10);
    }

    @Test
    void pointTransform() {
        Transformation3 translation = Transformation3.translation(5, 10, 15);
        CartesianPoint p = new CartesianPoint(0, 0, 0);
        CartesianPoint transformed = p.transform(translation);
        assertEquals(5.0, transformed.x(), 1e-10);
        assertEquals(10.0, transformed.y(), 1e-10);
        assertEquals(15.0, transformed.z(), 1e-10);
    }

    @Test
    void pointToPoint2() {
        CartesianPoint p3d = new CartesianPoint(5, 10, 15);
        com.minicad.geometry2d.Point2 p2d = p3d.toPoint2();
        assertEquals(5.0, p2d.x(), 1e-10);
        assertEquals(10.0, p2d.y(), 1e-10);
    }

    @Test
    void pointOrigin() {
        CartesianPoint origin = CartesianPoint.origin();
        assertEquals(0.0, origin.x(), 1e-10);
        assertEquals(0.0, origin.y(), 1e-10);
        assertEquals(0.0, origin.z(), 1e-10);
    }

    @Test
    void pointFromArray() {
        double[] coords = {5.0, 10.0, 15.0};
        CartesianPoint p = CartesianPoint.fromArray(coords);
        assertEquals(5.0, p.x(), 1e-10);
        assertEquals(10.0, p.y(), 1e-10);
        assertEquals(15.0, p.z(), 1e-10);
    }

    @Test
    void planeSignedDistanceTo() {
        Plane plane = new Plane(new CartesianPoint(0, 0, 5), new Direction3(0, 0, 1));
        CartesianPoint above = new CartesianPoint(0, 0, 10);
        assertEquals(5.0, plane.signedDistanceTo(above), 1e-10);

        CartesianPoint below = new CartesianPoint(0, 0, 0);
        assertEquals(-5.0, plane.signedDistanceTo(below), 1e-10);

        CartesianPoint onPlane = new CartesianPoint(10, 10, 5);
        assertEquals(0.0, plane.signedDistanceTo(onPlane), 1e-10);
    }

    @Test
    void planeContains() {
        Plane plane = new Plane(new CartesianPoint(0, 0, 5), new Direction3(0, 0, 1));

        CartesianPoint onPlane = new CartesianPoint(10, 10, 5);
        assertTrue(plane.contains(onPlane));

        CartesianPoint offPlane = new CartesianPoint(10, 10, 10);
        assertFalse(plane.contains(offPlane));
    }

    @Test
    void planeIntersectLine() {
        Plane plane = new Plane(new CartesianPoint(0, 0, 5), new Direction3(0, 0, 1));
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(0, 0, 1));

        CartesianPoint intersection = plane.intersect(line);
        assertEquals(0.0, intersection.x(), 1e-10);
        assertEquals(0.0, intersection.y(), 1e-10);
        assertEquals(5.0, intersection.z(), 1e-10);
    }

    @Test
    void ellipse3ClosestPointTo() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Ellipse3 ellipse = new Ellipse3(position, 4.0, 2.0);

        // Point along major axis
        CartesianPoint point = new CartesianPoint(10, 0, 0);
        CartesianPoint closest = ellipse.closestPointTo(point);
        assertEquals(4.0, closest.x(), 0.1);
        assertEquals(0.0, closest.y(), 0.1);
    }

    @Test
    void ellipse3DistanceTo() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Ellipse3 ellipse = new Ellipse3(position, 4.0, 2.0);

        // Point along major axis - distance should be 10 - 4 = 6
        CartesianPoint point = new CartesianPoint(10, 0, 0);
        double distance = ellipse.distanceTo(point);
        assertEquals(6.0, distance, 0.1);
    }
}