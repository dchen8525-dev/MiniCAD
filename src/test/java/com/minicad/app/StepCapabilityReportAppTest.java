package com.minicad.app;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StepCapabilityReportAppTest {

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
        assertTrue(markdown.contains("| Entity | Model | Registered | Builder | Exporter | Tested |"));
        assertTrue(json.contains("\"summary\""));
        assertTrue(json.contains("\"rows\""));
        assertTrue(json.contains("\"entity\":\"CARTESIAN_POINT\""));
    }
}
