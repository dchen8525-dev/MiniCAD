package com.minicad.geometry;

import com.minicad.common.GeometryException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for curve utility methods.
 */
class CurveUtilityTest {

    @Test
    void degenerateCurveBoundingBox() {
        CartesianPoint point = new CartesianPoint(5, 10, 15);
        DegenerateCurve3 curve = new DegenerateCurve3(point);
        BoundingBox3 box = curve.boundingBox();
        assertEquals(5.0, box.minX(), 1e-10);
        assertEquals(5.0, box.maxX(), 1e-10);
        assertEquals(10.0, box.minY(), 1e-10);
        assertEquals(10.0, box.maxY(), 1e-10);
        assertEquals(15.0, box.minZ(), 1e-10);
        assertEquals(15.0, box.maxZ(), 1e-10);
    }

    @Test
    void degenerateCurveSample() {
        CartesianPoint point = new CartesianPoint(1, 2, 3);
        DegenerateCurve3 curve = new DegenerateCurve3(point);
        java.util.List<CartesianPoint> samples = curve.sample(10);
        assertEquals(2, samples.size());
        assertEquals(point, samples.get(0));
        assertEquals(point, samples.get(1));
    }

    @Test
    void degenerateCurvePointAt() {
        CartesianPoint point = new CartesianPoint(7, 8, 9);
        DegenerateCurve3 curve = new DegenerateCurve3(point);
        assertEquals(point, curve.pointAt(0));
        assertEquals(point, curve.pointAt(0.5));
        assertEquals(point, curve.pointAt(1.0));
        assertEquals(point, curve.pointAt(-100));
    }

    @Test
    void degenerateCurveTangentThrows() {
        CartesianPoint point = new CartesianPoint(0, 0, 0);
        DegenerateCurve3 curve = new DegenerateCurve3(point);
        assertThrows(GeometryException.class, () -> curve.tangentAt(0));
    }

    @Test
    void degenerateCurveClosestPointTo() {
        CartesianPoint degeneratePoint = new CartesianPoint(5, 10, 15);
        DegenerateCurve3 curve = new DegenerateCurve3(degeneratePoint);

        // Any point should return the degenerate point
        CartesianPoint query = new CartesianPoint(100, 200, 300);
        CartesianPoint closest = curve.closestPointTo(query);
        assertEquals(degeneratePoint, closest);

        // Same point should return the degenerate point
        CartesianPoint samePoint = curve.closestPointTo(degeneratePoint);
        assertEquals(degeneratePoint, samePoint);
    }

    @Test
    void degenerateCurveDistanceTo() {
        CartesianPoint degeneratePoint = new CartesianPoint(5, 10, 15);
        DegenerateCurve3 curve = new DegenerateCurve3(degeneratePoint);

        // Distance should be the distance to the degenerate point
        CartesianPoint query = new CartesianPoint(10, 10, 15);
        double distance = curve.distanceTo(query);
        assertEquals(5.0, distance, 1e-10);

        // Distance from same point should be zero
        double zeroDistance = curve.distanceTo(degeneratePoint);
        assertEquals(0.0, zeroDistance, 1e-10);
    }

    @Test
    void surfaceCurveBoundingBox() {
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        SurfaceCurve3 surfaceCurve = new SurfaceCurve3(line);
        // For Line3, bounding box delegates to sampling which should return points at origin and pointAt(1)
        BoundingBox3 box = surfaceCurve.boundingBox();
        assertTrue(box.contains(new CartesianPoint(0, 0, 0)));
    }

    @Test
    void surfaceCurveTangent() {
        Circle circle = new Circle(
            new Axis2Placement3D(
                new CartesianPoint(0, 0, 0),
                new Direction3(0, 0, 1),
                new Direction3(1, 0, 0)
            ),
            5.0
        );
        SurfaceCurve3 surfaceCurve = new SurfaceCurve3(circle);

        Vector3 tangent = surfaceCurve.tangentAt(0);
        assertNotNull(tangent);
        assertEquals(1.0, tangent.norm(), 1e-10);
        assertEquals(0.0, tangent.x(), 1e-10);
        assertEquals(1.0, tangent.y(), 1e-10);
    }

    @Test
    void surfaceCurveSample() {
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        SurfaceCurve3 surfaceCurve = new SurfaceCurve3(line);
        java.util.List<CartesianPoint> samples = surfaceCurve.sample(10);
        assertEquals(11, samples.size());
        assertEquals(new CartesianPoint(0, 0, 0), samples.get(0));
        assertEquals(new CartesianPoint(1, 0, 0), samples.get(10));
    }

    @Test
    void hyperbola3ClosestPointTo() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Hyperbola3 hyperbola = new Hyperbola3(position, 5.0, 3.0);

        CartesianPoint point = new CartesianPoint(20, 0, 0);
        CartesianPoint closest = hyperbola.closestPointTo(point);
        // Should find a point on the hyperbola
        assertNotNull(closest);
        assertTrue(hyperbola.boundingBox().contains(closest) || closest.distanceTo(point) < point.distanceTo(new CartesianPoint(0, 0, 0)));
    }

    @Test
    void hyperbola3DistanceTo() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Hyperbola3 hyperbola = new Hyperbola3(position, 5.0, 3.0);

        CartesianPoint point = new CartesianPoint(20, 0, 0);
        double distance = hyperbola.distanceTo(point);
        assertTrue(distance >= 0);
    }

    @Test
    void parabola3ClosestPointTo() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Parabola3 parabola = new Parabola3(position, 2.0);

        CartesianPoint point = new CartesianPoint(10, 0, 0);
        CartesianPoint closest = parabola.closestPointTo(point);
        assertNotNull(closest);
    }

    @Test
    void parabola3DistanceTo() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Parabola3 parabola = new Parabola3(position, 2.0);

        CartesianPoint point = new CartesianPoint(10, 0, 0);
        double distance = parabola.distanceTo(point);
        assertTrue(distance >= 0);
    }

    @Test
    void clothoid3ClosestPointTo() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Clothoid3 clothoid = new Clothoid3(position, 1.0, 0.1);

        CartesianPoint point = new CartesianPoint(5, 5, 0);
        CartesianPoint closest = clothoid.closestPointTo(point);
        assertNotNull(closest);
    }

    @Test
    void clothoid3DistanceTo() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Clothoid3 clothoid = new Clothoid3(position, 1.0, 0.1);

        CartesianPoint point = new CartesianPoint(5, 5, 0);
        double distance = clothoid.distanceTo(point);
        assertTrue(distance >= 0);
    }
}