package com.minicad.preview.builder;

import com.minicad.helper.StepMetadataExtractor;
import com.minicad.geometry.CartesianPoint;
import com.minicad.helper.StepTextReader;
import com.minicad.preview.payload.FacePayload;
import com.minicad.preview.sampling.PreviewCurveEvaluator;
import com.minicad.step.semantic.StepCadBuilder;
import com.minicad.step.semantic.StepEntityResolver;
import com.minicad.step.syntax.StepParser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.minicad.step.model.StepAdvancedFace;
import com.minicad.step.model.StepCylindricalSurface;
import com.minicad.step.model.StepEdgeCurve;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepFaceEntity;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pipeline-level tests for the preview building trio the coverage gate calls
 * out (PreviewCurveEvaluator / PreviewFaceBuilder / PreviewGeometryCollector):
 * real sample faces and edges are driven through the public entries and the
 * produced geometry is asserted, not just executed.
 */
class PreviewPipelineTest {

    private static final Path SAMPLE = Path.of("samples", "plate-with-round-hole.step");

    private static Map<Integer, StepEntity> resolved;
    private static StepCadBuilder builder;

    @BeforeAll
    static void buildFixture() throws Exception {
        String text = StepTextReader.readDecoded(Files.readAllBytes(SAMPLE)).text();
        resolved = StepEntityResolver.resolveAll(StepParser.parse(text));
        builder = StepCadBuilder.fromResolved(resolved);
    }

    @Test
    void legacyGeometryCollectorBuildsFacesAndEdgesFromTheSample() {
        com.minicad.preview.payload.GeometryCollection geometry =
                PreviewGeometryCollector.buildLegacyGeometry(
                        resolved, builder, StepMetadataExtractor.fromResolved(resolved));

        // plate-with-round-hole: the legacy collector emits the shell-backed
        // planar face; the hole boundary arrives through its bounds.
        assertTrue(geometry.faces().size() >= 1,
                () -> "expected at least 1 face, got " + geometry.faces().size());
        assertTrue(geometry.edges().size() >= 3,
                () -> "expected at least 3 edges, got " + geometry.edges().size());
        for (FacePayload face : geometry.faces()) {
            assertTrue(face.triangles() != null && face.triangles().size() >= 3,
                    () -> "face " + face.stepId() + " has no triangles");
        }

        com.minicad.preview.payload.GeometryCollection merged =
                PreviewGeometryCollector.mergeGeometry(geometry, geometry);
        assertEquals(geometry.faces().size() * 2, merged.faces().size());
        assertEquals(geometry.edges().size() * 2, merged.edges().size());
    }

    @Test
    void curveEvaluatorSamplesEveryResolvedEdgeCurveEndToEnd() {
        List<StepEdgeCurve> edgeCurves = new ArrayList<>();
        for (StepEntity entity : resolved.values()) {
            if (entity instanceof StepEdgeCurve) {
                edgeCurves.add((StepEdgeCurve) entity);
            }
        }
        assertTrue(edgeCurves.size() >= 4, "fixture should contain edge curves");

        for (StepEdgeCurve edge : edgeCurves) {
            PreviewCurveEvaluator.CurveEvaluator evaluator =
                    PreviewCurveEvaluator.curveEvaluator(edge, builder);
            assertNotNull(evaluator, () -> "no evaluator for edge #" + edge.id());
            assertTrue(evaluator.end() > evaluator.start(), () -> "degenerate domain on edge #" + edge.id());
            CartesianPoint startPoint = evaluator.pointAt(evaluator.start());
            CartesianPoint endPoint = evaluator.pointAt(evaluator.end());
            assertNotNull(startPoint);
            assertNotNull(endPoint);
        }
    }

    @Test
    void cylindricalFacePayloadIsBuiltAcrossTheSampleCorpus() throws Exception {
        // The payload builder guards on exact loop shapes (one outer 4-edge
        // EdgeLoop); instead of betting on one fixture, assert that the corpus
        // contains at least one cylindrical face the builder accepts.
        int cylindersSeen = 0;
        try (var walk = Files.walk(Path.of("samples"))) {
            for (Path sample : walk.filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().toLowerCase().endsWith(".step"))
                    .collect(java.util.stream.Collectors.toList())) {
                String text = StepTextReader.readDecoded(Files.readAllBytes(sample)).text();
                Map<Integer, StepEntity> sampleResolved = StepEntityResolver.resolveAll(StepParser.parse(text));
                StepCadBuilder sampleBuilder = StepCadBuilder.fromResolved(sampleResolved);

                for (StepEntity surface : sampleResolved.values()) {
                    if (!(surface instanceof StepCylindricalSurface)) {
                        continue;
                    }
                    cylindersSeen++;
                    for (StepEntity entity : sampleResolved.values()) {
                        if (entity instanceof StepAdvancedFace
                                && PreviewFaceBuilder.faceGeometry((StepFaceEntity) entity).id() == surface.id()) {
                            FacePayload payload = PreviewFaceBuilder.toCylindricalFacePayload(
                                    (StepFaceEntity) entity,
                                    (StepCylindricalSurface) surface,
                                    sampleBuilder,
                                    StepMetadataExtractor.DisplayMetadata.EMPTY);
                            if (payload != null) {
                                assertNotNull(payload.surface());
                                assertTrue(payload.surface().type().toLowerCase().contains("cylindr"),
                                        () -> "unexpected surface type " + payload.surface().type());
                                assertTrue(payload.triangles().size() >= 9,
                                        "accepted cylindrical payload should be tessellated");
                                return; // corpus proven
                            }
                        }
                    }
                }
            }
        }
        org.junit.jupiter.api.Assumptions.assumeTrue(cylindersSeen > 0, "no cylindrical surfaces in corpus");
        org.junit.jupiter.api.Assertions.fail(
                "no cylindrical face in the corpus satisfied the payload builder guards");
    }
}
