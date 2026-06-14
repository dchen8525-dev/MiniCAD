package com.minicad.app;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UnitExtractorTest {

    @Test
    void extractsInchScaleFromConversionBasedLengthUnit() {
        CompiledStepDocument compiled = CompiledStepDocument.compile(
        "DATA;\n"
        + "#1=(LENGTH_UNIT() NAMED_UNIT(*) SI_UNIT(.MILLI.,.METRE.));\n"
        + "#2=MEASURE_WITH_UNIT(LENGTH_MEASURE(25.4),#1);\n"
        + "#3=(CONVERSION_BASED_UNIT('INCH',#2) NAMED_UNIT(*) LENGTH_UNIT());\n"
        + "#4=(GEOMETRIC_REPRESENTATION_CONTEXT(3)\n"
        + "    GLOBAL_UNIT_ASSIGNED_CONTEXT((#3))\n"
        + "    REPRESENTATION_CONTEXT('ID','MODEL'));\n"
        + "ENDSEC;"

        UnitExtractor.UnitInfo units = UnitExtractor.extract(compiled.resolved());

        assertEquals("INCH", units.lengthUnit());
        assertEquals(0.0254, units.scaleToMeters(), 1.0e-12);
    }

    @Test
    void extractsStandaloneInchScaleFromConversionBasedLengthUnit() {
        CompiledStepDocument compiled = CompiledStepDocument.compile(
        "DATA;\n"
        + "#1=(LENGTH_UNIT() NAMED_UNIT(*) SI_UNIT(.MILLI.,.METRE.));\n"
        + "#2=MEASURE_WITH_UNIT(LENGTH_MEASURE(25.4),#1);\n"
        + "#3=(CONVERSION_BASED_UNIT('INCH',#2) NAMED_UNIT(*) LENGTH_UNIT());\n"
        + "ENDSEC;"

        UnitExtractor.UnitInfo units = UnitExtractor.extract(compiled.resolved());

        assertEquals("INCH", units.lengthUnit());
        assertEquals(0.0254, units.scaleToMeters(), 1.0e-12);
    }
}
