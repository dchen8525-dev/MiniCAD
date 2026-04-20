package com.minicad.app;

import com.minicad.step.model.base.StepEntity;
import com.minicad.step.semantic.StepEntityResolver;
import com.minicad.step.syntax.StepFile;
import com.minicad.step.syntax.StepParser;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ProductMetadataExtractorTest {

    private static final String STEP_WITH_HEADER = """
            ISO-10303-21;
            HEADER;
            FILE_DESCRIPTION(('Test Model Description'),'2;1');
            FILE_NAME('bracket-v2.step','2026-04-17T10:00:00',('Developer'),(''),'MiniCAD','FreeCAD','');
            FILE_SCHEMA(('AP203'));
            ENDSEC;
            DATA;
            #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
            ENDSEC;
            END-ISO-10303-21;
            """;

    @Test
    void shouldExtractHeaderMetadata() {
        StepFile stepFile = StepParser.parse(STEP_WITH_HEADER);
        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(stepFile);
        ProductMetadataExtractor.ProductMetadata metadata = ProductMetadataExtractor.extract(stepFile, resolved);

        assertEquals("bracket-v2.step", metadata.fileName());
        assertEquals("Test Model Description", metadata.fileDescription());
        assertEquals(1, metadata.schemaNames().size());
        assertEquals("AP203", metadata.schemaNames().get(0));
    }

    @Test
    void shouldHandleEmptyHeader() {
        String stepText = """
                ISO-10303-21;
                HEADER;
                ENDSEC;
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                ENDSEC;
                END-ISO-10303-21;
                """;

        StepFile stepFile = StepParser.parse(stepText);
        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(stepFile);
        ProductMetadataExtractor.ProductMetadata metadata = ProductMetadataExtractor.extract(stepFile, resolved);

        assertTrue(metadata.isEmpty());
    }

    @Test
    void shouldExtractMultipleSchemas() {
        String stepText = """
                ISO-10303-21;
                HEADER;
                FILE_SCHEMA(('AP203','AP214'));
                ENDSEC;
                DATA;
                #1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));
                ENDSEC;
                END-ISO-10303-21;
                """;

        StepFile stepFile = StepParser.parse(stepText);
        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(stepFile);
        ProductMetadataExtractor.ProductMetadata metadata = ProductMetadataExtractor.extract(stepFile, resolved);

        assertEquals(2, metadata.schemaNames().size());
        assertTrue(metadata.schemaNames().contains("AP203"));
        assertTrue(metadata.schemaNames().contains("AP214"));
    }
}
