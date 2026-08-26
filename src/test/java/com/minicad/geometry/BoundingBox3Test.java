package com.minicad.geometry;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for BoundingBox3.contains, including the empty-box guard that prevents
 * a +Inf/-Inf empty box from being wrongly reported as contained.
 */
class BoundingBox3Test {

    @Test
    void emptyBoxContainsNoPoint() {
        BoundingBox3 empty = BoundingBox3.empty();
        assertFalse(empty.containsPoint(new CartesianPoint(0, 0, 0)));
    }

    @Test
    void emptyBoxIsNotContainedByNonEmpty() {
        // Regression: the +/-Inf bounds of an empty box previously satisfied
        // every comparison and were wrongly reported as contained.
        BoundingBox3 empty = BoundingBox3.empty();
        BoundingBox3 solid = new BoundingBox3(0, 0, 0, 10, 10, 10);
        assertFalse(solid.contains(empty));
        assertFalse(empty.contains(solid));
        assertFalse(empty.contains(empty));
    }

    @Test
    void nonEmptyBoxContainsSubBox() {
        BoundingBox3 outer = new BoundingBox3(0, 0, 0, 10, 10, 10);
        BoundingBox3 inner = new BoundingBox3(2, 2, 2, 8, 8, 8);
        assertTrue(outer.contains(inner));
        assertTrue(outer.containsPoint(new CartesianPoint(5, 5, 5)));
        assertFalse(outer.containsPoint(new CartesianPoint(11, 5, 5)));
    }

    @Test
    void boundaryPointIsContained() {
        BoundingBox3 box = new BoundingBox3(0, 0, 0, 10, 10, 10);
        assertTrue(box.containsPoint(new CartesianPoint(0, 10, 5)));
    }
}
