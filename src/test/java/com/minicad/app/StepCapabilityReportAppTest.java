package com.minicad.app;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StepCapabilityReportAppTest {

    @TempDir
    Path tempDir;

    @Test
    void convertsStepClassNamesToStepEntityNames() {
        assertEquals("MANIFOLD_SOLID_BREP", StepCapabilityReportApp.camelToUpperUnderscore("ManifoldSolidBrep"));
        assertEquals("B_SPLINE_CURVE_2D", StepCapabilityReportApp.camelToUpperUnderscore("BSplineCurve2D"));
        assertEquals("AXIS2_PLACEMENT_3D", StepCapabilityReportApp.camelToUpperUnderscore("Axis2Placement3D"));
    }

    @Test
    void scansCapabilitySignalsFromSourceTree() throws Exception {
        StepCapabilityReportApp.CapabilityReport report = StepCapabilityReportApp.scan(Path.of("."));

        assertTrue(report.modelEntities().contains("CARTESIAN_POINT"));
        assertTrue(report.registeredEntities().contains("CARTESIAN_POINT"));
        assertTrue(report.builderEntities().contains("CARTESIAN_POINT"));
        assertTrue(report.exporterEntities().contains("CARTESIAN_POINT"));
        assertTrue(report.testedEntities().contains("CARTESIAN_POINT"));
        assertTrue(report.rows().stream().anyMatch(row -> row.entity().equals("MANIFOLD_SOLID_BREP")));
    }

    @Test
    void rendersMarkdownAndJsonReports() throws Exception {
        StepCapabilityReportApp.CapabilityReport report = StepCapabilityReportApp.scan(Path.of("."));

        String markdown = StepCapabilityReportApp.toMarkdown(report);
        String json = StepCapabilityReportApp.toJson(report);

        assertTrue(markdown.contains("## Summary"));
        assertTrue(markdown.contains("| Entity | Quality | Model | Registered | Builder | Exporter | Tested | Declared | Limitations |"));
        assertTrue(json.contains("\"summary\""));
        assertTrue(json.contains("\"rows\""));
        assertTrue(json.contains("\"entity\":\"CARTESIAN_POINT\""));
        assertTrue(json.contains("\"qualityLevel\":\""));
        assertTrue(json.contains("\"declaredCapabilityEntries\""));
    }

    @Test
    void loadsDeclarativeCapabilityRegistry() throws Exception {
        String registry = 
        "# comment\n"
        + "entity\tlevel\tparsed\tresolved\tbuilt\texported\ttested\tlimitations\n"
        + "CARTESIAN_POINT\tL4\ttrue\ttrue\ttrue\ttrue\ttrue\tcommon path"

        Map<String, StepCapabilityRegistry.Capability> capabilities = StepCapabilityRegistry.load(
                new ByteArrayInputStream(registry.getBytes(StandardCharsets.UTF_8)));

        StepCapabilityRegistry.Capability capability = capabilities.get("CARTESIAN_POINT");
        assertEquals("L4", capability.level());
        assertTrue(capability.parsed());
        assertEquals("common path", capability.limitations());
    }

    @Test
    void scansExpressSchemaEntities() throws Exception {
        Path schema = tempDir.resolve("sample.exp");
        Files.writeString(schema, 
        "SCHEMA sample_schema;\n"
        + "  ENTITY cartesian_point;\n"
        + "  END_ENTITY;\n"
        + "\n"
        + "  ENTITY custom_entity\n"
        + "    ABSTRACT SUPERTYPE;\n"
        + "  END_ENTITY;\n"
        + "END_SCHEMA;"

        assertEquals(
                java.util.Set.of("CARTESIAN_POINT", "CUSTOM_ENTITY"),
                StepCapabilityReportApp.scanExpressSchemaEntities(schema));
    }

    @Test
    void scansCuratedEntityLists() throws Exception {
        Path schema = tempDir.resolve("ap214-curated-entities.lst");
        Files.writeString(schema, 
        "# Curated entity list\n"
        + "cartesian_point\n"
        + "ADVANCED_FACE # inline note\n"
        + "// ignored comment"

        assertEquals(
                java.util.Set.of("ADVANCED_FACE", "CARTESIAN_POINT"),
                StepCapabilityReportApp.scanExpressSchemaEntities(schema));
    }

    @Test
    void rendersSchemaCoverageReports() throws Exception {
        Path schema = tempDir.resolve("sample.exp");
        Files.writeString(schema, 
        "SCHEMA sample_schema;\n"
        + "  ENTITY cartesian_point;\n"
        + "  END_ENTITY;\n"
        + "  ENTITY schema_only_entity;\n"
        + "  END_ENTITY;\n"
        + "END_SCHEMA;"
        StepCapabilityReportApp.CapabilityReport report = StepCapabilityReportApp.scan(Path.of("."));

        StepCapabilityReportApp.SchemaCoverageReport schemaReport =
                StepCapabilityReportApp.scanSchemaCoverage(report, "Sample", schema);
        String markdown = StepCapabilityReportApp.toMarkdown(schemaReport);
        String json = StepCapabilityReportApp.toJson(schemaReport);

        assertEquals(2, schemaReport.schemaEntities().size());
        assertTrue(schemaReport.rows().stream().anyMatch(row ->
                row.entity().equals("CARTESIAN_POINT") && row.registered()));
        assertTrue(schemaReport.rows().stream().anyMatch(row ->
                row.entity().equals("SCHEMA_ONLY_ENTITY") && !row.registered()));
        assertTrue(markdown.contains("# MiniCAD Sample Schema Coverage Report"));
        assertTrue(markdown.contains("| Schema entities | 2 |"));
        assertTrue(markdown.contains("| Entity | Quality | Model | Registered | Builder | Exporter | Tested | Declared | Limitations |"));
        assertTrue(json.contains("\"schemaName\": \"Sample\""));
        assertTrue(json.contains("\"entity\":\"SCHEMA_ONLY_ENTITY\""));
        assertTrue(json.contains("\"qualityLevel\":\""));
    }
}
