package com.minicad.preview.payload;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class UvBoundsTest {

    @Test
    void constructorExposesAccessors() {
        UvBounds b = new UvBounds(0.1, 0.2, 0.9, 0.8);
        assertEquals(0.1, b.getMinU());
        assertEquals(0.2, b.getMinV());
        assertEquals(0.9, b.getMaxU());
        assertEquals(0.8, b.getMaxV());
        // fluent accessors
        assertEquals(0.1, b.minU());
        assertEquals(0.2, b.minV());
        assertEquals(0.9, b.maxU());
        assertEquals(0.8, b.maxV());
    }

    @Test
    void spansComputeDifferences() {
        UvBounds b = new UvBounds(0.1, 0.2, 0.9, 0.8);
        assertEquals(0.8, b.uSpan(), 1e-12);
        assertEquals(0.6, b.vSpan(), 1e-12);
    }

    @Test
    void equalsAndHashCodeConsistent() {
        UvBounds a = new UvBounds(0.0, 0.0, 1.0, 1.0);
        UvBounds b = new UvBounds(0.0, 0.0, 1.0, 1.0);
        UvBounds c = new UvBounds(0.0, 0.0, 1.0, 0.99);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
        assertNotEquals(a, null);
        assertNotEquals(a, "not a bounds");
    }

    @Test
    void toStringIsNonNull() {
        assertNotNull(new UvBounds(0.0, 0.0, 1.0, 1.0).toString());
    }
}
