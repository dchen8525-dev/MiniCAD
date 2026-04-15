package com.minicad.geometry2d;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for BoundingBox2 class.
 */
class BoundingBox2Test {

    @Test
    void emptyBoundingBox() {
        BoundingBox2 empty = BoundingBox2.empty();
        assertTrue(empty.isEmpty());
        assertEquals(0.0, empty.width(), 0.001);
        assertEquals(0.0, empty.height(), 0.001);
    }

    @Test
    void boundingBoxOfTwoPoints() {
        Point2 p1 = new Point2(0, 0);
        Point2 p2 = new Point2(10, 20);
        BoundingBox2 box = BoundingBox2.of(p1, p2);
        assertEquals(0.0, box.minX(), 0.001);
        assertEquals(0.0, box.minY(), 0.001);
        assertEquals(10.0, box.maxX(), 0.001);
        assertEquals(20.0, box.maxY(), 0.001);
    }

    @Test
    void boundingBoxWidthHeight() {
        BoundingBox2 box = new BoundingBox2(0, 0, 10, 20);
        assertEquals(10.0, box.width(), 0.001);
        assertEquals(20.0, box.height(), 0.001);
    }

    @Test
    void boundingBoxCenter() {
        BoundingBox2 box = new BoundingBox2(0, 0, 10, 20);
        Point2 center = box.center();
        assertEquals(5.0, center.x(), 0.001);
        assertEquals(10.0, center.y(), 0.001);
    }

    @Test
    void boundingBoxArea() {
        BoundingBox2 box = new BoundingBox2(0, 0, 10, 20);
        assertEquals(200.0, box.area(), 0.001);
    }

    @Test
    void boundingBoxDiagonal() {
        BoundingBox2 box = new BoundingBox2(0, 0, 3, 4);
        assertEquals(5.0, box.diagonal(), 0.001);
    }

    @Test
    void boundingBoxContains() {
        BoundingBox2 box = new BoundingBox2(0, 0, 10, 10);
        assertTrue(box.contains(new Point2(5, 5)));
        assertTrue(box.contains(new Point2(0, 0)));
        assertTrue(box.contains(new Point2(10, 10)));
        assertFalse(box.contains(new Point2(11, 5)));
        assertFalse(box.contains(new Point2(-1, 5)));
    }

    @Test
    void boundingBoxIntersects() {
        BoundingBox2 box1 = new BoundingBox2(0, 0, 10, 10);
        BoundingBox2 box2 = new BoundingBox2(5, 5, 15, 15);
        assertTrue(box1.intersects(box2));

        BoundingBox2 box3 = new BoundingBox2(15, 15, 20, 20);
        assertFalse(box1.intersects(box3));
    }

    @Test
    void boundingBoxUnion() {
        BoundingBox2 box1 = new BoundingBox2(0, 0, 10, 10);
        BoundingBox2 box2 = new BoundingBox2(5, 5, 15, 15);
        BoundingBox2 union = box1.union(box2);
        assertEquals(0.0, union.minX(), 0.001);
        assertEquals(0.0, union.minY(), 0.001);
        assertEquals(15.0, union.maxX(), 0.001);
        assertEquals(15.0, union.maxY(), 0.001);
    }

    @Test
    void boundingBoxUnionWithPoint() {
        BoundingBox2 box = new BoundingBox2(0, 0, 10, 10);
        BoundingBox2 expanded = box.union(new Point2(15, 5));
        assertEquals(15.0, expanded.maxX(), 0.001);
        assertEquals(10.0, expanded.maxY(), 0.001);
    }

    @Test
    void boundingBoxIntersection() {
        BoundingBox2 box1 = new BoundingBox2(0, 0, 10, 10);
        BoundingBox2 box2 = new BoundingBox2(5, 5, 15, 15);
        BoundingBox2 intersection = box1.intersection(box2);
        assertEquals(5.0, intersection.minX(), 0.001);
        assertEquals(5.0, intersection.minY(), 0.001);
        assertEquals(10.0, intersection.maxX(), 0.001);
        assertEquals(10.0, intersection.maxY(), 0.001);
    }

    @Test
    void boundingBoxIntersectionEmpty() {
        BoundingBox2 box1 = new BoundingBox2(0, 0, 10, 10);
        BoundingBox2 box2 = new BoundingBox2(15, 15, 20, 20);
        BoundingBox2 intersection = box1.intersection(box2);
        assertTrue(intersection.isEmpty());
    }

    @Test
    void boundingBoxExpand() {
        BoundingBox2 box = new BoundingBox2(0, 0, 10, 10);
        BoundingBox2 expanded = box.expand(2);
        assertEquals(-2.0, expanded.minX(), 0.001);
        assertEquals(-2.0, expanded.minY(), 0.001);
        assertEquals(12.0, expanded.maxX(), 0.001);
        assertEquals(12.0, expanded.maxY(), 0.001);
    }

    @Test
    void boundingBoxScale() {
        BoundingBox2 box = new BoundingBox2(0, 0, 10, 10);
        BoundingBox2 scaled = box.scale(2);
        assertEquals(-5.0, scaled.minX(), 0.001);
        assertEquals(-5.0, scaled.minY(), 0.001);
        assertEquals(15.0, scaled.maxX(), 0.001);
        assertEquals(15.0, scaled.maxY(), 0.001);
    }

    @Test
    void boundingBoxOfCollection() {
        java.util.List<Point2> points = java.util.List.of(
            new Point2(0, 0),
            new Point2(10, 20),
            new Point2(5, 30)
        );
        BoundingBox2 box = BoundingBox2.of(points);
        assertEquals(0.0, box.minX(), 0.001);
        assertEquals(0.0, box.minY(), 0.001);
        assertEquals(10.0, box.maxX(), 0.001);
        assertEquals(30.0, box.maxY(), 0.001);
    }

    @Test
    void boundingBoxMinCorner() {
        BoundingBox2 box = new BoundingBox2(0, 5, 10, 20);
        Point2 min = box.minCorner();
        assertEquals(0.0, min.x(), 0.001);
        assertEquals(5.0, min.y(), 0.001);
    }

    @Test
    void boundingBoxMaxCorner() {
        BoundingBox2 box = new BoundingBox2(0, 5, 10, 20);
        Point2 max = box.maxCorner();
        assertEquals(10.0, max.x(), 0.001);
        assertEquals(20.0, max.y(), 0.001);
    }

    @Test
    void emptyBoxCenterThrows() {
        BoundingBox2 empty = BoundingBox2.empty();
        assertThrows(com.minicad.common.GeometryException.class, () -> empty.center());
    }

    @Test
    void emptyBoxMinCornerThrows() {
        BoundingBox2 empty = BoundingBox2.empty();
        assertThrows(com.minicad.common.GeometryException.class, () -> empty.minCorner());
    }

    @Test
    void emptyBoxMaxCornerThrows() {
        BoundingBox2 empty = BoundingBox2.empty();
        assertThrows(com.minicad.common.GeometryException.class, () -> empty.maxCorner());
    }

    @Test
    void boundingBoxContainsBox() {
        BoundingBox2 outer = new BoundingBox2(0, 0, 20, 20);
        BoundingBox2 inner = new BoundingBox2(5, 5, 15, 15);

        // Check that inner is fully within outer
        assertTrue(outer.contains(inner.minCorner()));
        assertTrue(outer.contains(inner.maxCorner()));
    }

    @Test
    void boundingBoxIntersectsTouching() {
        BoundingBox2 box1 = new BoundingBox2(0, 0, 10, 10);
        BoundingBox2 box2 = new BoundingBox2(10, 0, 20, 10);  // Touches at edge

        assertTrue(box1.intersects(box2));
    }

    @Test
    void boundingBoxUnionEmpty() {
        BoundingBox2 box = new BoundingBox2(0, 0, 10, 10);
        BoundingBox2 empty = BoundingBox2.empty();

        BoundingBox2 union1 = box.union(empty);
        assertEquals(box, union1);

        BoundingBox2 union2 = empty.union(box);
        assertEquals(box, union2);
    }

    @Test
    void boundingBoxIntersectionWithEmpty() {
        BoundingBox2 box = new BoundingBox2(0, 0, 10, 10);
        BoundingBox2 empty = BoundingBox2.empty();

        BoundingBox2 intersection1 = box.intersection(empty);
        assertTrue(intersection1.isEmpty());

        BoundingBox2 intersection2 = empty.intersection(box);
        assertTrue(intersection2.isEmpty());
    }
}