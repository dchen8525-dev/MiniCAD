package com.minicad.geometry;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for bounding box computations on geometry classes.
 */
class BoundingBoxTest {

    @Test
    void boundingBox3Empty() {
        BoundingBox3 empty = BoundingBox3.empty();
        assertTrue(empty.isEmpty());
        assertTrue(empty.minX() > empty.maxX());
    }

    @Test
    void boundingBox3OfPoint() {
        CartesianPoint point = new CartesianPoint(1, 2, 3);
        BoundingBox3 box = BoundingBox3.of(point);
        assertFalse(box.isEmpty());
        assertEquals(1.0, box.minX());
        assertEquals(2.0, box.minY());
        assertEquals(3.0, box.minZ());
        assertEquals(1.0, box.maxX());
        assertEquals(2.0, box.maxY());
        assertEquals(3.0, box.maxZ());
        assertEquals(0.0, box.width());
        assertEquals(0.0, box.height());
        assertEquals(0.0, box.depth());
    }

    @Test
    void boundingBox3OfTwoPoints() {
        CartesianPoint p1 = new CartesianPoint(0, 0, 0);
        CartesianPoint p2 = new CartesianPoint(10, 5, 3);
        BoundingBox3 box = BoundingBox3.of(p1, p2);
        assertEquals(0.0, box.minX());
        assertEquals(0.0, box.minY());
        assertEquals(0.0, box.minZ());
        assertEquals(10.0, box.maxX());
        assertEquals(5.0, box.maxY());
        assertEquals(3.0, box.maxZ());
        assertEquals(10.0, box.width());
        assertEquals(5.0, box.height());
        assertEquals(3.0, box.depth());
    }

    @Test
    void boundingBox3Union() {
        BoundingBox3 box1 = BoundingBox3.of(new CartesianPoint(0, 0, 0), new CartesianPoint(5, 5, 5));
        BoundingBox3 box2 = BoundingBox3.of(new CartesianPoint(3, 3, 3), new CartesianPoint(10, 10, 10));
        BoundingBox3 union = box1.union(box2);
        assertEquals(0.0, union.minX());
        assertEquals(0.0, union.minY());
        assertEquals(0.0, union.minZ());
        assertEquals(10.0, union.maxX());
        assertEquals(10.0, union.maxY());
        assertEquals(10.0, union.maxZ());
    }

    @Test
    void boundingBox3Intersection() {
        BoundingBox3 box1 = BoundingBox3.of(new CartesianPoint(0, 0, 0), new CartesianPoint(10, 10, 10));
        BoundingBox3 box2 = BoundingBox3.of(new CartesianPoint(5, 5, 5), new CartesianPoint(15, 15, 15));
        BoundingBox3 intersection = box1.intersection(box2);
        assertEquals(5.0, intersection.minX());
        assertEquals(5.0, intersection.minY());
        assertEquals(5.0, intersection.minZ());
        assertEquals(10.0, intersection.maxX());
        assertEquals(10.0, intersection.maxY());
        assertEquals(10.0, intersection.maxZ());
    }

    @Test
    void boundingBox3Contains() {
        BoundingBox3 box = BoundingBox3.of(new CartesianPoint(0, 0, 0), new CartesianPoint(10, 10, 10));
        assertTrue(box.contains(new CartesianPoint(5, 5, 5)));
        assertTrue(box.contains(new CartesianPoint(0, 0, 0)));
        assertTrue(box.contains(new CartesianPoint(10, 10, 10)));
        assertFalse(box.contains(new CartesianPoint(11, 5, 5)));
    }

    @Test
    void boundingBox3Expand() {
        BoundingBox3 box = BoundingBox3.of(new CartesianPoint(0, 0, 0), new CartesianPoint(10, 10, 10));
        BoundingBox3 expanded = box.expand(2);
        assertEquals(-2.0, expanded.minX());
        assertEquals(-2.0, expanded.minY());
        assertEquals(-2.0, expanded.minZ());
        assertEquals(12.0, expanded.maxX());
        assertEquals(12.0, expanded.maxY());
        assertEquals(12.0, expanded.maxZ());
    }

    @Test
    void circleBoundingBox() {
        // Circle in XY plane at (5,5,0) with radius 2
        // Axis is (0,0,1), refDirection is (1,0,0)
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(5, 5, 0),
            new Direction3(0, 0, 1),  // axis (Z direction)
            new Direction3(1, 0, 0)   // refDirection (X reference)
        );
        Circle circle = new Circle(position, 2.0);
        BoundingBox3 box = circle.boundingBox();
        // Circle centered at (5,5,0) with radius 2 in XY plane
        assertTrue(box.contains(new CartesianPoint(5, 5, 0)));
        // Should be roughly in range 3-7 for x and y
        assertEquals(3.0, box.minX(), 0.001);
        assertEquals(7.0, box.maxX(), 0.001);
        assertEquals(3.0, box.minY(), 0.001);
        assertEquals(7.0, box.maxY(), 0.001);
        assertEquals(0.0, box.minZ(), 0.001);
        assertEquals(0.0, box.maxZ(), 0.001);
    }

    @Test
    void ellipseBoundingBox() {
        // Ellipse in XY plane at (0,0,0) with semi-axes 4 and 2
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),  // axis (Z direction)
            new Direction3(1, 0, 0)   // refDirection (X reference)
        );
        Ellipse3 ellipse = new Ellipse3(position, 4.0, 2.0);
        BoundingBox3 box = ellipse.boundingBox();
        // Ellipse with semi-axes 4 and 2
        assertTrue(box.contains(new CartesianPoint(0, 0, 0)));
        assertEquals(-4.0, box.minX(), 0.001);
        assertEquals(4.0, box.maxX(), 0.001);
        assertEquals(-2.0, box.minY(), 0.001);
        assertEquals(2.0, box.maxY(), 0.001);
    }

    @Test
    void polyline3BoundingBox() {
        Polyline3 polyline = new Polyline3(java.util.List.of(
            new CartesianPoint(0, 0, 0),
            new CartesianPoint(5, 3, 2),
            new CartesianPoint(10, 0, 4)
        ));
        BoundingBox3 box = polyline.boundingBox();
        assertEquals(0.0, box.minX());
        assertEquals(0.0, box.minY());
        assertEquals(0.0, box.minZ());
        assertEquals(10.0, box.maxX());
        assertEquals(3.0, box.maxY());
        assertEquals(4.0, box.maxZ());
    }

    @Test
    void polyline3Length() {
        Polyline3 polyline = new Polyline3(java.util.List.of(
            new CartesianPoint(0, 0, 0),
            new CartesianPoint(5, 0, 0),
            new CartesianPoint(5, 5, 0)
        ));
        assertEquals(10.0, polyline.length(), 0.001);
    }

    @Test
    void line3BoundingBox() {
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0));
        BoundingBox3 box = line.boundingBox(0, 10);
        assertEquals(0.0, box.minX());
        assertEquals(0.0, box.minY());
        assertEquals(0.0, box.minZ());
        assertEquals(10.0, box.maxX());
        assertEquals(0.0, box.maxY());
        assertEquals(0.0, box.maxZ());
    }

    @Test
    void sphereBoundingBox() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        SphericalSurface sphere = new SphericalSurface(position, 5.0);
        BoundingBox3 box = sphere.boundingBox();
        assertEquals(-5.0, box.minX());
        assertEquals(-5.0, box.minY());
        assertEquals(-5.0, box.minZ());
        assertEquals(5.0, box.maxX());
        assertEquals(5.0, box.maxY());
        assertEquals(5.0, box.maxZ());
    }

    @Test
    void torusBoundingBox() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        ToroidalSurface torus = new ToroidalSurface(position, 4.0, 1.0);
        BoundingBox3 box = torus.boundingBox();
        // Torus extends from (major-minor) to (major+minor) in radial directions
        assertEquals(-5.0, box.minX());
        assertEquals(-5.0, box.minY());
        assertEquals(-1.0, box.minZ());
        assertEquals(5.0, box.maxX());
        assertEquals(5.0, box.maxY());
        assertEquals(1.0, box.maxZ());
    }

    @Test
    void cylindricalBoundingBox() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),  // axis is Z
            new Direction3(1, 0, 0)   // refDirection
        );
        CylindricalSurface cylinder = new CylindricalSurface(position, 3.0);
        BoundingBox3 box = cylinder.boundingBox(-2, 5);
        // Cylinder with radius 3, height from -2 to 5 along Z axis
        assertEquals(-3.0, box.minX());
        assertEquals(-3.0, box.minY());
        assertEquals(-2.0, box.minZ());
        assertEquals(3.0, box.maxX());
        assertEquals(3.0, box.maxY());
        assertEquals(5.0, box.maxZ());
    }

    @Test
    void boundingBox3SurfaceArea() {
        BoundingBox3 box = BoundingBox3.of(new CartesianPoint(0, 0, 0), new CartesianPoint(2, 3, 4));
        // Surface area = 2*(w*h + h*d + w*d) = 2*(2*3 + 3*4 + 2*4) = 2*(6 + 12 + 8) = 52
        assertEquals(52.0, box.surfaceArea(), 1e-10);
    }

    @Test
    void boundingBox3ClosestPointTo() {
        BoundingBox3 box = BoundingBox3.of(new CartesianPoint(0, 0, 0), new CartesianPoint(10, 10, 10));

        // Point inside - closest point should be the point itself
        CartesianPoint inside = new CartesianPoint(5, 5, 5);
        CartesianPoint closestInside = box.closestPointTo(inside);
        assertEquals(5.0, closestInside.x(), 1e-10);
        assertEquals(5.0, closestInside.y(), 1e-10);
        assertEquals(5.0, closestInside.z(), 1e-10);

        // Point outside - closest point should be on boundary
        CartesianPoint outside = new CartesianPoint(15, 5, 5);
        CartesianPoint closestOutside = box.closestPointTo(outside);
        assertEquals(10.0, closestOutside.x(), 1e-10);
        assertEquals(5.0, closestOutside.y(), 1e-10);
        assertEquals(5.0, closestOutside.z(), 1e-10);
    }

    @Test
    void boundingBox3DistanceTo() {
        BoundingBox3 box = BoundingBox3.of(new CartesianPoint(0, 0, 0), new CartesianPoint(10, 10, 10));

        // Point inside - distance should be 0
        CartesianPoint inside = new CartesianPoint(5, 5, 5);
        assertEquals(0.0, box.distanceTo(inside), 1e-10);

        // Point outside - distance should be positive
        CartesianPoint outside = new CartesianPoint(15, 5, 5);
        assertEquals(5.0, box.distanceTo(outside), 1e-10);
    }

    @Test
    void boundingBox3PointAt() {
        BoundingBox3 box = BoundingBox3.of(new CartesianPoint(0, 0, 0), new CartesianPoint(10, 20, 30));

        // Point at (0, 0, 0) - should be min corner
        CartesianPoint p000 = box.pointAt(0, 0, 0);
        assertEquals(0.0, p000.x(), 1e-10);
        assertEquals(0.0, p000.y(), 1e-10);
        assertEquals(0.0, p000.z(), 1e-10);

        // Point at (1, 1, 1) - should be max corner
        CartesianPoint p111 = box.pointAt(1, 1, 1);
        assertEquals(10.0, p111.x(), 1e-10);
        assertEquals(20.0, p111.y(), 1e-10);
        assertEquals(30.0, p111.z(), 1e-10);

        // Point at (0.5, 0.5, 0.5) - should be center
        CartesianPoint pMid = box.pointAt(0.5, 0.5, 0.5);
        assertEquals(5.0, pMid.x(), 1e-10);
        assertEquals(10.0, pMid.y(), 1e-10);
        assertEquals(15.0, pMid.z(), 1e-10);
    }

    @Test
    void boundingBox3ContainsBox() {
        BoundingBox3 outer = BoundingBox3.of(new CartesianPoint(0, 0, 0), new CartesianPoint(20, 20, 20));
        BoundingBox3 inner = BoundingBox3.of(new CartesianPoint(5, 5, 5), new CartesianPoint(15, 15, 15));

        assertTrue(outer.contains(inner));
        assertFalse(inner.contains(outer));

        // Overlapping but not contained
        BoundingBox3 overlap = BoundingBox3.of(new CartesianPoint(10, 10, 10), new CartesianPoint(25, 25, 25));
        assertFalse(outer.contains(overlap));
    }

    @Test
    void boundingBox3Corners() {
        BoundingBox3 box = BoundingBox3.of(new CartesianPoint(0, 0, 0), new CartesianPoint(10, 10, 10));
        java.util.List<CartesianPoint> corners = box.corners();

        assertEquals(8, corners.size());

        // Check min corner
        assertTrue(corners.contains(new CartesianPoint(0, 0, 0)));

        // Check max corner
        assertTrue(corners.contains(new CartesianPoint(10, 10, 10)));

        // Check some other corners
        assertTrue(corners.contains(new CartesianPoint(10, 0, 0)));
        assertTrue(corners.contains(new CartesianPoint(0, 10, 0)));
        assertTrue(corners.contains(new CartesianPoint(0, 0, 10)));
    }

    @Test
    void emptyBoxCorners() {
        BoundingBox3 empty = BoundingBox3.empty();
        java.util.List<CartesianPoint> corners = empty.corners();
        assertTrue(corners.isEmpty());
    }

    @Test
    void boundingBox3Diagonal() {
        BoundingBox3 box = BoundingBox3.of(new CartesianPoint(0, 0, 0), new CartesianPoint(3, 4, 12));
        // Diagonal = sqrt(3^2 + 4^2 + 12^2) = sqrt(9 + 16 + 144) = sqrt(169) = 13
        assertEquals(13.0, box.diagonal(), 1e-10);
    }

    @Test
    void boundingBox3Center() {
        BoundingBox3 box = BoundingBox3.of(new CartesianPoint(0, 0, 0), new CartesianPoint(10, 20, 30));
        CartesianPoint center = box.center();
        assertEquals(5.0, center.x(), 1e-10);
        assertEquals(10.0, center.y(), 1e-10);
        assertEquals(15.0, center.z(), 1e-10);
    }

    @Test
    void boundingBox3Volume() {
        BoundingBox3 box = BoundingBox3.of(new CartesianPoint(0, 0, 0), new CartesianPoint(2, 3, 4));
        assertEquals(24.0, box.volume(), 1e-10);
    }
}