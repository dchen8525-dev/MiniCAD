package com.minicad.app;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UnitExtractorTest {

    @Test
    void extractsInchScaleFromConversionBasedLengthUnit() {
        CompiledStepDocument compiled = CompiledStepDocument.compile("""
                DATA;
                #1=(LENGTH_UNIT() NAMED_UNIT(*) SI_UNIT(.MILLI.,.METRE.));
                #2=MEASURE_WITH_UNIT(LENGTH_MEASURE(25.4),#1);
                #3=(CONVERSION_BASED_UNIT('INCH',#2) NAMED_UNIT(*) LENGTH_UNIT());
                #4=(GEOMETRIC_REPRESENTATION_CONTEXT(3)
                    GLOBAL_UNIT_ASSIGNED_CONTEXT((#3))
                    REPRESENTATION_CONTEXT('ID','MODEL'));
                ENDSEC;
                """);

        UnitExtractor.UnitInfo units = UnitExtractor.extract(compiled.resolved());

        assertEquals("INCH", units.lengthUnit());
        assertEquals(0.0254, units.scaleToMeters(), 1.0e-12);
    }

    @Test
    void extractsStandaloneInchScaleFromConversionBasedLengthUnit() {
        CompiledStepDocument compiled = CompiledStepDocument.compile("""
                DATA;
                #1=(LENGTH_UNIT() NAMED_UNIT(*) SI_UNIT(.MILLI.,.METRE.));
                #2=MEASURE_WITH_UNIT(LENGTH_MEASURE(25.4),#1);
                #3=(CONVERSION_BASED_UNIT('INCH',#2) NAMED_UNIT(*) LENGTH_UNIT());
                ENDSEC;
                """);

        UnitExtractor.UnitInfo units = UnitExtractor.extract(compiled.resolved());

        assertEquals("INCH", units.lengthUnit());
        assertEquals(0.0254, units.scaleToMeters(), 1.0e-12);
    }
}
