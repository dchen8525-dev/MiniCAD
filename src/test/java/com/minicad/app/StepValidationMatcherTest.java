package com.minicad.app;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StepValidationMatcherTest {

    @Test
    void shouldMatchAreaMeasureWithoutExactAreaName() {
        assertEquals("surface_area", StepValidationMatcher.matchPropertyId("projected value", "AREA_MEASURE"));
    }

    @Test
    void shouldMatchLengthMeasureCentroidAndBounds() {
        assertEquals("center_x", StepValidationMatcher.matchPropertyId("centroid x", "LENGTH_MEASURE"));
        assertEquals("bbox_z", StepValidationMatcher.matchPropertyId("height", "LENGTH_MEASURE"));
        assertEquals("edge_length", StepValidationMatcher.matchPropertyId("wire metric", "LENGTH_MEASURE"));
    }
}
