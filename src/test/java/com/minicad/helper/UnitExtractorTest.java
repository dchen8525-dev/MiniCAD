package com.minicad.helper;

import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepSiUnit;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Tests for M4: SI length-unit scale resolution.
 *
 * <p>The bug was that MILLIMETRE was handled in a separate branch that dropped
 * the 1e-3 base factor, so millimetres were silently treated as metres.
 */
class UnitExtractorTest {

    private static UnitExtractor.UnitInfo extractLength(String prefix, String unitName) {
        Map<Integer, StepEntity> m = new HashMap<>();
        m.put(1, new StepSiUnit(1, "LENGTH_UNIT", prefix, unitName));
        return UnitExtractor.extract(m);
    }

    @Test
    void millimetreResolvesTo1eMinus3() {
        UnitExtractor.UnitInfo info = extractLength(null, "MILLIMETRE");
        assertEquals("millimetre", info.lengthUnit());
        assertEquals(1e-3, info.scaleToMeters(), 1e-15);
    }

    @Test
    void milliMillimetreResolvesTo1eMinus6() {
        UnitExtractor.UnitInfo info = extractLength("MILLI", "MILLIMETRE");
        assertEquals(1e-6, info.scaleToMeters(), 1e-18);
    }

    @Test
    void metreResolvesToUnity() {
        UnitExtractor.UnitInfo info = extractLength(null, "METRE");
        assertEquals(1.0, info.scaleToMeters(), 1e-15);
    }

    @Test
    void kilometreResolvesTo1e3() {
        UnitExtractor.UnitInfo info = extractLength("KILO", "METRE");
        assertEquals(1e3, info.scaleToMeters(), 1e-12);
    }

    @Test
    void centimetreResolvesTo1eMinus2() {
        UnitExtractor.UnitInfo info = extractLength("CENTI", "METRE");
        assertEquals(1e-2, info.scaleToMeters(), 1e-15);
    }

    @Test
    void inchResolvesTo0254() {
        UnitExtractor.UnitInfo info = extractLength(null, "INCH");
        assertEquals(0.0254, info.scaleToMeters(), 1e-12);
    }

    @Test
    void unknownUnitResolvesToNull() {
        UnitExtractor.UnitInfo info = extractLength(null, "PARSEC");
        assertNull(info.scaleToMeters());
    }
}
