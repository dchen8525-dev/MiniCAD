package com.minicad.preview.builder;

import com.minicad.export.json.StepFacePayloadBuilder;
import com.minicad.export.json.StepGeometryHelper;
import com.minicad.export.json.StepLegacyGeometryBuilder;
import com.minicad.helper.StepMetadataExtractor;
import com.minicad.geometry.CartesianPoint;
import com.minicad.helper.StepTextReader;
import com.minicad.preview.payload.FacePayload;
import com.minicad.preview.payload.GeometryCollection;
import com.minicad.preview.payload.LoopPayload;
import com.minicad.preview.payload.PointPayload;
import com.minicad.preview.sampling.CurveEvaluator;
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
 * Pipeline-level tests for the preview builders the coverage gate calls out
 * (PreviewCurveEvaluator / PreviewFaceBuilder): real sample faces and edges are
 * driven through the public entries and the produced geometry is asserted, not
 * just executed. The legacy-geometry leg drives StepLegacyGeometryBuilder, which
 * is now the single home of that orchestration.
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
                StepLegacyGeometryBuilder.buildLegacyGeometry(
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
                StepLegacyGeometryBuilder.mergeGeometry(geometry, geometry);
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
            CurveEvaluator evaluator =
                    PreviewCurveEvaluator.curveEvaluator(edge, builder);
            assertNotNull(evaluator, () -> "no evaluator for edge #" + edge.id());
            assertTrue(evaluator.end() > evaluator.start(), () -> "degenerate domain on edge #" + edge.id());
            assertNotNull(evaluator.pointAt(evaluator.start()));
            assertNotNull(evaluator.pointAt(evaluator.end()));
        }
    }

    @Test
    void solidFacesAreTrimmedToTheirOwnBounds() throws Exception {
        // test.step wraps its geometry in a MANIFOLD_SOLID_BREP, so every face
        // reaches the exporter through the solid bucket rather than through a
        // shell. That bucket used to grid each carrier surface over its whole
        // parameter domain, which drew each of the nine cylindrical bands as a
        // full-height cylinder instead of the sliver the trim loops enclose.
        String text = StepTextReader.readDecoded(
                Files.readAllBytes(Path.of("samples", "test.step"))).text();
        Map<Integer, StepEntity> sampleResolved = StepEntityResolver.resolveAll(StepParser.parse(text));
        StepCadBuilder sampleBuilder = StepCadBuilder.fromResolved(sampleResolved);
        GeometryCollection geometry =
                StepLegacyGeometryBuilder.buildLegacyGeometry(
                        sampleResolved, sampleBuilder, StepMetadataExtractor.fromResolved(sampleResolved));

        assertEquals(15, geometry.faces().size(), "test.step has 9 cylindrical faces and 6 planes");
        int cylindricalTriangles = 0;
        for (FacePayload face : geometry.faces()) {
            if (!"CYLINDRICAL_SURFACE".equals(face.surfaceType())) {
                continue;
            }
            cylindricalTriangles += face.triangles().size() / 3;
            double triangleDiagonal = diagonal(triangleBounds(face));
            double loopDiagonal = diagonal(loopBounds(face));
            assertTrue(triangleDiagonal <= loopDiagonal * 1.05,
                    () -> "face " + face.stepId() + " draws triangles past its trim loops: "
                            + triangleDiagonal + " vs " + loopDiagonal);
        }
        assertEquals(1_952, cylindricalTriangles,
                "the cylindrical bands must be tessellated in their own parametric window");
    }

    private static double[] triangleBounds(FacePayload face) {
        return pointBounds(face.triangles());
    }

    private static double[] loopBounds(FacePayload face) {
        List<PointPayload> points = new ArrayList<>();
        for (LoopPayload loop : face.loops()) {
            points.addAll(loop.points());
        }
        return pointBounds(points);
    }

    private static double[] pointBounds(List<PointPayload> points) {
        double[] bounds = {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY,
                Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY};
        for (PointPayload point : points) {
            bounds[0] = Math.min(bounds[0], point.x());
            bounds[1] = Math.min(bounds[1], point.y());
            bounds[2] = Math.min(bounds[2], point.z());
            bounds[3] = Math.max(bounds[3], point.x());
            bounds[4] = Math.max(bounds[4], point.y());
            bounds[5] = Math.max(bounds[5], point.z());
        }
        return bounds;
    }

    private static double diagonal(double[] bounds) {
        double dx = bounds[3] - bounds[0];
        double dy = bounds[4] - bounds[1];
        double dz = bounds[5] - bounds[2];
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    @Test
    void curveEvaluatorHandlesEveryCurveFamilyAcrossTheCorpus() throws Exception {
        // Walking the whole corpus drives the line/circle/ellipse/bspline/
        // trimmed/... evaluator branches on production-shaped data.
        int evaluatorsBuilt = 0;
        try (var walk = Files.walk(Path.of("samples"))) {
            for (Path sample : walk.filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().toLowerCase().endsWith(".step"))
                    .collect(java.util.stream.Collectors.toList())) {
                String text = StepTextReader.readDecoded(Files.readAllBytes(sample)).text();
                Map<Integer, StepEntity> sampleResolved =
                        StepEntityResolver.resolveAll(StepParser.parse(text));
                StepCadBuilder sampleBuilder = StepCadBuilder.fromResolved(sampleResolved);
                for (StepEntity entity : sampleResolved.values()) {
                    if (!(entity instanceof StepEdgeCurve)) {
                        continue;
                    }
                    CurveEvaluator evaluator =
                            PreviewCurveEvaluator.curveEvaluator((StepEdgeCurve) entity, sampleBuilder);
                    if (evaluator == null) {
                        continue;
                    }
                    evaluatorsBuilt++;
                    assertTrue(evaluator.end() >= evaluator.start(),
                            () -> sample + " edge #" + entity.id() + " domain");
                    evaluator.pointAt(evaluator.start());
                    evaluator.pointAt((evaluator.start() + evaluator.end()) / 2.0);
                    evaluator.pointAt(evaluator.end());
                }
            }
        }
        assertTrue(evaluatorsBuilt >= 100,
                "corpus should exercise many evaluators, got " + evaluatorsBuilt);
    }

    @Test
    void cylindricalFacePayloadIsBuiltAcrossTheSampleCorpus() throws Exception {
        // The payload builder guards on exact loop shapes (one outer 4-edge
        // EdgeLoop); instead of betting on one fixture, assert that the corpus
        // contains at least one cylindrical face the builder accepts. The
        // builder is the export-side rule table: PreviewFaceBuilder carried a
        // copy of this handler for a while, but its only caller was itself, and
        // it went with the rest of that dead handler family. Driving
        // buildPreviewFaceResult here keeps the corpus assertion pointed at the
        // path the app actually uses.
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
                                && StepGeometryHelper.faceGeometry((StepFaceEntity) entity).id() == surface.id()) {
                            FacePayload payload = StepFacePayloadBuilder.buildPreviewFaceResult(
                                    (StepFaceEntity) entity,
                                    sampleBuilder,
                                    StepMetadataExtractor.DisplayMetadata.EMPTY).face();
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
