package com.minicad.app;

import com.minicad.common.*;
import com.minicad.geometry.*;
import com.minicad.geometry2d.*;
import com.minicad.export.json.JsonBuilder;
import com.minicad.helper.StepTextReader;
import com.minicad.helper.UnitExtractor;
import com.minicad.step.model.*;
import com.minicad.step.model.*;
import com.minicad.step.model.*;
import com.minicad.step.model.*;
import com.minicad.step.model.*;
import com.minicad.step.model.*;
import com.minicad.step.model.*;
import com.minicad.step.model.*;
import com.minicad.step.model.*;
import com.minicad.step.model.*;
import com.minicad.step.model.*;
import com.minicad.step.model.*;
import com.minicad.step.model.*;
import com.minicad.step.model.*;
import com.minicad.step.model.*;
import com.minicad.step.model.*;
import com.minicad.step.model.*;
import com.minicad.step.model.*;
import com.minicad.step.semantic.*;
import com.minicad.step.syntax.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Minimal CLI demo that reads a STEP file and prints a structural summary.
 */
public final class StepDumpApp {
    private static final Logger log = LoggerFactory.getLogger(StepDumpApp.class);

    private StepDumpApp() {
    }

    /**
     * CLI entry point.
     *
     * @param args command-line arguments
     * @throws IOException if reading the file fails
     */
    public static void main(String[] args) throws IOException {
        int exitCode = run(args, log::info, log::error);
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }

    static int run(String[] args, Consumer<String> out, Consumer<String> err) throws IOException {
        boolean debug = false;
        boolean validateOnly = false;
        boolean jsonOutput = false;
        List<String> files = new ArrayList<>();

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if ("--debug".equals(arg)) {
                debug = true;
            } else if ("--validate-only".equals(arg)) {
                validateOnly = true;
            } else if ("--json".equals(arg)) {
                jsonOutput = true;
            } else if (!arg.startsWith("--")) {
                files.add(arg);
            }
        }

        if (files.isEmpty()) {
            err.accept("Usage: StepDumpApp [--debug] [--validate-only] [--json] <step-file>...");
            return 2;
        }

        if (jsonOutput) {
            return runJson(files, debug, out, err);
        }

        int overallExitCode = 0;
        for (String filePath : files) {
            int exitCode = runSingleFile(filePath, debug, validateOnly, out, err);
            if (exitCode != 0) {
                overallExitCode = exitCode;
            }
        }
        return overallExitCode;
    }

    private static int runSingleFile(String filePath, boolean debug, boolean validateOnly,
                                     Consumer<String> out, Consumer<String> err) throws IOException {
        Path path = Path.of(filePath);
        String text = StepTextReader.read(path);

        try {
            StepFile stepFile = StepParser.parse(text);
            Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(stepFile);
            StepCadBuilder builder = validateOnly ? null : StepCadBuilder.fromResolved(resolved);

            List<String> lines = new ArrayList<>();
            lines.add("File: " + path);
            lines.add("");
            appendSyntaxSummary(stepFile, lines);
            if (!validateOnly) {
                lines.add("");
                appendSemanticSummary(resolved, lines);
                lines.add("");
                appendBuildSummary(resolved, builder, lines);
            } else {
                lines.add("");
                lines.add("Validation Summary");
                lines.add("  status: ok");
                lines.add("  entityCount: " + stepFile.entities().size());
                lines.add("  resolvedCount: " + resolved.size());
            }
            lines.forEach(out);
            return 0;
        } catch (StepParseException | StepResolutionException | UnsupportedGeometryException | TopologyException | GeometryException ex) {
            String errorMsg = "STEP processing failed for " + path + ": " + ex.getMessage();
            err.accept(errorMsg);
            if (debug) {
                StringWriter sw = new StringWriter();
                ex.printStackTrace(new PrintWriter(sw));
                err.accept(sw.toString());
            }
            return 1;
        }
    }

    private static int runJson(List<String> files, boolean debug, Consumer<String> out, Consumer<String> err) throws IOException {
        List<Map<String, Object>> results = new ArrayList<>();
        int overallExitCode = 0;

        for (String filePath : files) {
            Map<String, Object> fileResult = new LinkedHashMap<>();
            fileResult.put("path", filePath);
            Path path = Path.of(filePath);

            try {
                String text = StepTextReader.read(path);
                StepFile stepFile = StepParser.parse(text);
                Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(stepFile);
                StepCadBuilder builder = StepCadBuilder.fromResolved(resolved);
                UnitExtractor.UnitInfo units = UnitExtractor.extract(resolved);

                fileResult.put("status", "ok");
                fileResult.put("exitCode", 0);
                fileResult.put("entityCount", stepFile.entities().size());
                fileResult.put("resolvedCount", resolved.size());
                fileResult.put("unsupportedCount", countUnsupportedFaces(resolved, builder));

                // Collect issues
                List<Map<String, Object>> issues = new ArrayList<>();
                if (units != null && units.scaleToMeters() != null
                        && Math.abs(units.scaleToMeters() - 1.0) > 1.0e-12) {
                    Map<String, Object> unitWarning = new LinkedHashMap<>();
                    unitWarning.put("severity", "WARNING");
                    unitWarning.put("code", "units.coordinates_not_normalized");
                    unitWarning.put("message", "geometry coordinates are emitted in source STEP units; scaleToMeters is metadata only");
                    issues.add(unitWarning);
                }
                fileResult.put("issues", issues);

                // Calculate bbox
                BoundingBox3 bbox = computeBoundingBox(resolved, builder);
                Map<String, Object> bboxMap = new LinkedHashMap<>();
                if (bbox != null) {
                    bboxMap.put("min", new double[]{bbox.minX(), bbox.minY(), bbox.minZ()});
                    bboxMap.put("max", new double[]{bbox.maxX(), bbox.maxY(), bbox.maxZ()});
                } else {
                    bboxMap.put("min", new double[]{0.0, 0.0, 0.0});
                    bboxMap.put("max", new double[]{0.0, 0.0, 0.0});
                }
                fileResult.put("bbox", bboxMap);
            } catch (StepParseException | StepResolutionException | UnsupportedGeometryException | TopologyException | GeometryException ex) {
                fileResult.put("status", "failed");
                fileResult.put("exitCode", 1);
                fileResult.put("error", ex.getMessage());
                Map<String, Object> issue = new LinkedHashMap<>();
                issue.put("severity", "ERROR");
                issue.put("code", "step.parse");
                issue.put("message", ex.getMessage());
                fileResult.put("issues", List.of(issue));
                overallExitCode = 1;
            }

            results.add(fileResult);
        }

        // Output JSON
        out.accept(JsonBuilder.toJson(results));
        return overallExitCode;
    }

    private static int countUnsupportedFaces(Map<Integer, StepEntity> resolved, StepCadBuilder builder) {
        int unsupportedFaces = 0;
        for (StepEntity entity : resolved.values()) {
            if (entity instanceof StepOpenShell) {
                StepOpenShell openShell = (StepOpenShell) entity;
                unsupportedFaces += summarizeShell(openShell.faces(), builder).unsupportedFaces();
            } else if (entity instanceof StepClosedShell) {
                StepClosedShell closedShell = (StepClosedShell) entity;
                unsupportedFaces += summarizeShell(closedShell.faces(), builder).unsupportedFaces();
            }
        }
        return unsupportedFaces;
    }

    private static BoundingBox3 computeBoundingBox(Map<Integer, StepEntity> resolved, StepCadBuilder builder) {
        BoundingBox3 bbox = null;
        for (StepEntity entity : resolved.values()) {
            if (entity instanceof StepCartesianPoint) {
                StepCartesianPoint point = (StepCartesianPoint) entity;
                List<Double> coords = point.coordinates();
                if (coords != null && coords.size() >= 2) {
                    double x = coords.get(0);
                    double y = coords.get(1);
                    double z = coords.size() >= 3 ? coords.get(2) : 0.0;
                    CartesianPoint cp = new CartesianPoint(x, y, z);
                    if (bbox == null) {
                        bbox = BoundingBox3.of(cp);
                    } else {
                        bbox = bbox.expand(cp);
                    }
                }
            }
        }
        return bbox;
    }

    private static void appendSyntaxSummary(StepFile file, List<String> lines) {
        lines.add("Syntax Summary");
        lines.add("  entityCount: " + file.entities().size());
        if (!file.entities().isEmpty()) {
            lines.add("  firstId: #" + file.entities().get(0).id());
            lines.add("  lastId: #" + file.entities().get(file.entities().size() - 1).id());
        }
    }

    private static void appendSemanticSummary(Map<Integer, StepEntity> resolved, List<String> lines) {
        lines.add("Semantic Summary");
        Map<String, Integer> counts = new TreeMap<>();
        for (StepEntity entity : resolved.values()) {
            counts.merge(stepEntityTypeName(entity), 1, Integer::sum);
        }
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            lines.add("  " + entry.getKey() + ": " + entry.getValue());
        }
    }

    private static String stepEntityTypeName(StepEntity entity) {
        if (entity instanceof com.minicad.step.model.StepFaceBound) {
            com.minicad.step.model.StepFaceBound faceBound = (com.minicad.step.model.StepFaceBound) entity;
            return faceBound.outer() ? "FACE_OUTER_BOUND" : "FACE_BOUND";
        }
        if (entity instanceof StepAxis2Placement2D) {
            return "AXIS2_PLACEMENT_2D";
        }
        if (entity instanceof StepAxis2Placement3D) {
            return "AXIS2_PLACEMENT_3D";
        }
        if (entity instanceof StepOffsetCurve2D) {
            return "OFFSET_CURVE_2D";
        }
        if (entity instanceof StepOffsetCurve3D) {
            return "OFFSET_CURVE_3D";
        }
        if (entity instanceof com.minicad.step.model.StepRepresentation) {
            com.minicad.step.model.StepRepresentation representation = (com.minicad.step.model.StepRepresentation) entity;
            if (representation.entityName() != null
                    && !representation.entityName().isBlank()
                    && !"REPRESENTATION".equals(representation.entityName())
                    && !"SHAPE_REPRESENTATION".equals(representation.entityName())) {
                return representation.entityName();
            }
            if (representation.shapeRepresentation()) {
                return "SHAPE_REPRESENTATION";
            }
            return "REPRESENTATION";
        }
        // Attempt to get entityName via reflection; if the method doesn't exist, fall back to class name
        try {
            var method = entity.getClass().getMethod("entityName");
            Object value = method.invoke(entity);
            if (value instanceof String && !((String) value).isBlank()) {
                String name = (String) value;
                return name;
            }
        } catch (ReflectiveOperationException ignored) {
            // entityName method not present or not accessible, use class name fallback below
        }
        String simpleName = entity.getClass().getSimpleName();
        if (simpleName.startsWith("Step")) {
            simpleName = simpleName.substring(4);
        }
        return camelToUpperSnake(simpleName);
    }

    private static String camelToUpperSnake(String value) {
        if (value.isEmpty()) {
            return value;
        }
        String normalized = value
                .replaceAll("([A-Z]+)([A-Z][a-z])", "$1_$2")
                .replaceAll("([a-z0-9])([A-Z])", "$1_$2");
        return normalized.toUpperCase(java.util.Locale.ROOT);
    }

    private static void appendBuildSummary(Map<Integer, StepEntity> resolved, StepCadBuilder builder, List<String> lines) {
        lines.add("Build Summary");

        int openShells = 0;
        int closedShells = 0;
        int solids = 0;
        int booleanResults = 0;
        int standaloneFaceEntities = 0;
        int standaloneEdgeEntities = 0;
        int standaloneLoopEntities = 0;
        int standalonePathEntities = 0;
        int standaloneContainerEntities = 0;
        int unsupportedFaces = 0;
        int skipped2DEntities = 0;
        Map<String, Integer> unsupportedReasons = new LinkedHashMap<>();
        Map<String, Integer> unsupportedReasonCodes = new LinkedHashMap<>();
        Set<Integer> shellFaceIds = StepEntityIdCollector.collectShellFaceIds(resolved.values());
        Set<Integer> loopOrientedEdgeIds = StepEntityIdCollector.collectLoopOrientedEdgeIds(resolved.values());
        Set<Integer> orientedEdgeElementIds = StepEntityIdCollector.collectOrientedEdgeElementIds(resolved.values());
        Set<Integer> faceBoundLoopIds = StepEntityIdCollector.collectFaceBoundLoopIds(resolved.values());

        for (StepEntity entity : resolved.values()) {
            if (entity instanceof StepOpenShell) {
                StepOpenShell openShell = (StepOpenShell) entity;
                FaceBuildCounts counts = summarizeShell(openShell.faces(), builder);
                lines.add("  " + stepEntityTypeName(openShell) + " #" + openShell.id() + ": faces=" + counts.supportedFaces()
                        + ", unsupportedFaces=" + counts.unsupportedFaces());
                appendUnsupportedReasons(lines, counts.unsupportedReasons());
                appendUnsupportedReasonCodes(lines, counts.unsupportedReasonCodes());
                openShells++;
                unsupportedFaces += counts.unsupportedFaces();
                mergeReasonCounts(unsupportedReasons, counts.unsupportedReasons());
                mergeReasonCounts(unsupportedReasonCodes, counts.unsupportedReasonCodes());
            } else if (entity instanceof StepSurfacedOpenShell) {
                StepSurfacedOpenShell surfacedOpenShell = (StepSurfacedOpenShell) entity;
                FaceBuildCounts counts = summarizeShell(surfacedOpenShell.faces(), builder);
                lines.add("  " + stepEntityTypeName(surfacedOpenShell) + " #" + surfacedOpenShell.id() + ": faces=" + counts.supportedFaces()
                        + ", unsupportedFaces=" + counts.unsupportedFaces());
                appendUnsupportedReasons(lines, counts.unsupportedReasons());
                appendUnsupportedReasonCodes(lines, counts.unsupportedReasonCodes());
                openShells++;
                unsupportedFaces += counts.unsupportedFaces();
                mergeReasonCounts(unsupportedReasons, counts.unsupportedReasons());
                mergeReasonCounts(unsupportedReasonCodes, counts.unsupportedReasonCodes());
            } else if (entity instanceof StepClosedShell) {
                StepClosedShell closedShell = (StepClosedShell) entity;
                FaceBuildCounts counts = summarizeShell(closedShell.faces(), builder);
                lines.add("  " + stepEntityTypeName(closedShell) + " #" + closedShell.id() + ": faces=" + counts.supportedFaces()
                        + ", unsupportedFaces=" + counts.unsupportedFaces());
                appendUnsupportedReasons(lines, counts.unsupportedReasons());
                appendUnsupportedReasonCodes(lines, counts.unsupportedReasonCodes());
                closedShells++;
                unsupportedFaces += counts.unsupportedFaces();
                mergeReasonCounts(unsupportedReasons, counts.unsupportedReasons());
                mergeReasonCounts(unsupportedReasonCodes, counts.unsupportedReasonCodes());
            } else if (entity instanceof StepOrientedOpenShell) {
                StepOrientedOpenShell orientedOpenShell = (StepOrientedOpenShell) entity;
                FaceBuildCounts counts = summarizeShell(orientedOpenShell.faces(), builder);
                lines.add("  " + stepEntityTypeName(orientedOpenShell) + " #" + orientedOpenShell.id() + ": faces=" + counts.supportedFaces()
                        + ", unsupportedFaces=" + counts.unsupportedFaces());
                appendUnsupportedReasons(lines, counts.unsupportedReasons());
                appendUnsupportedReasonCodes(lines, counts.unsupportedReasonCodes());
                openShells++;
                unsupportedFaces += counts.unsupportedFaces();
                mergeReasonCounts(unsupportedReasons, counts.unsupportedReasons());
                mergeReasonCounts(unsupportedReasonCodes, counts.unsupportedReasonCodes());
            } else if (entity instanceof StepOrientedClosedShell) {
                StepOrientedClosedShell orientedClosedShell = (StepOrientedClosedShell) entity;
                FaceBuildCounts counts = summarizeShell(orientedClosedShell.faces(), builder);
                lines.add("  " + stepEntityTypeName(orientedClosedShell) + " #" + orientedClosedShell.id() + ": faces=" + counts.supportedFaces()
                        + ", unsupportedFaces=" + counts.unsupportedFaces());
                appendUnsupportedReasons(lines, counts.unsupportedReasons());
                appendUnsupportedReasonCodes(lines, counts.unsupportedReasonCodes());
                closedShells++;
                unsupportedFaces += counts.unsupportedFaces();
                mergeReasonCounts(unsupportedReasons, counts.unsupportedReasons());
                mergeReasonCounts(unsupportedReasonCodes, counts.unsupportedReasonCodes());
            } else if (entity instanceof StepManifoldSolidBrep) {
                StepManifoldSolidBrep solidBrep = (StepManifoldSolidBrep) entity;
                FaceBuildCounts counts = summarizeShell(shellFaces(solidBrep.outer()), builder);
                lines.add("  " + stepEntityTypeName(solidBrep) + " #" + solidBrep.id() + ": shellFaces=" + counts.supportedFaces()
                        + ", unsupportedFaces=" + counts.unsupportedFaces());
                appendUnsupportedReasons(lines, counts.unsupportedReasons());
                appendUnsupportedReasonCodes(lines, counts.unsupportedReasonCodes());
                solids++;
                unsupportedFaces += counts.unsupportedFaces();
                mergeReasonCounts(unsupportedReasons, counts.unsupportedReasons());
                mergeReasonCounts(unsupportedReasonCodes, counts.unsupportedReasonCodes());
            } else if (entity instanceof StepBrepWithVoids) {
                StepBrepWithVoids brepWithVoids = (StepBrepWithVoids) entity;
                FaceBuildCounts counts = summarizeShell(shellFaces(brepWithVoids.outer()), builder);
                for (StepEntity voidShell : brepWithVoids.voids()) {
                    counts = counts.plus(summarizeShell(shellFaces(voidShell), builder));
                }
                lines.add("  " + stepEntityTypeName(brepWithVoids) + " #" + brepWithVoids.id() + ": shellFaces=" + counts.supportedFaces()
                        + ", unsupportedFaces=" + counts.unsupportedFaces());
                appendUnsupportedReasons(lines, counts.unsupportedReasons());
                appendUnsupportedReasonCodes(lines, counts.unsupportedReasonCodes());
                solids++;
                unsupportedFaces += counts.unsupportedFaces();
                mergeReasonCounts(unsupportedReasons, counts.unsupportedReasons());
                mergeReasonCounts(unsupportedReasonCodes, counts.unsupportedReasonCodes());
            } else if (entity instanceof StepSweptAreaSolid) {
                StepSweptAreaSolid sweptAreaSolid = (StepSweptAreaSolid) entity;
                try {
                    int faceCount = builder.buildSolid(sweptAreaSolid.id()).outerShell().faces().size();
                    lines.add("  " + stepEntityTypeName(sweptAreaSolid) + " #" + sweptAreaSolid.id() + ": shellFaces=" + faceCount + ", unsupportedFaces=0");
                } catch (UnsupportedGeometryException ex) {
                    Map<String, Integer> reasonCounts = Map.of(ex.getMessage(), 1);
                    Map<String, Integer> reasonCodeCounts = Map.of("unsupported_solid.swept_area", 1);
                    lines.add("  " + stepEntityTypeName(sweptAreaSolid) + " #" + sweptAreaSolid.id() + ": shellFaces=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, reasonCounts);
                    appendUnsupportedReasonCodes(lines, reasonCodeCounts);
                    unsupportedFaces++;
                    mergeReasonCounts(unsupportedReasons, reasonCounts);
                    mergeReasonCounts(unsupportedReasonCodes, reasonCodeCounts);
                }
                solids++;
            } else if (entity instanceof StepExtrudedFaceSolid) {
                StepExtrudedFaceSolid extrudedFaceSolid = (StepExtrudedFaceSolid) entity;
                try {
                    int faceCount = builder.buildSolid(extrudedFaceSolid.id()).outerShell().faces().size();
                    lines.add("  " + stepEntityTypeName(extrudedFaceSolid) + " #" + extrudedFaceSolid.id() + ": shellFaces=" + faceCount + ", unsupportedFaces=0");
                } catch (UnsupportedGeometryException ex) {
                    Map<String, Integer> reasonCounts = Map.of(ex.getMessage(), 1);
                    Map<String, Integer> reasonCodeCounts = Map.of("unsupported_solid.extruded_face", 1);
                    lines.add("  " + stepEntityTypeName(extrudedFaceSolid) + " #" + extrudedFaceSolid.id() + ": shellFaces=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, reasonCounts);
                    appendUnsupportedReasonCodes(lines, reasonCodeCounts);
                    unsupportedFaces++;
                    mergeReasonCounts(unsupportedReasons, reasonCounts);
                    mergeReasonCounts(unsupportedReasonCodes, reasonCodeCounts);
                }
                solids++;
            } else if (entity instanceof StepRevolvedFaceSolid) {
                StepRevolvedFaceSolid revolvedFaceSolid = (StepRevolvedFaceSolid) entity;
                try {
                    int faceCount = builder.buildSolid(revolvedFaceSolid.id()).outerShell().faces().size();
                    lines.add("  " + stepEntityTypeName(revolvedFaceSolid) + " #" + revolvedFaceSolid.id() + ": shellFaces=" + faceCount + ", unsupportedFaces=0");
                } catch (UnsupportedGeometryException ex) {
                    Map<String, Integer> reasonCounts = Map.of(ex.getMessage(), 1);
                    Map<String, Integer> reasonCodeCounts = Map.of("unsupported_solid.revolved_face", 1);
                    lines.add("  " + stepEntityTypeName(revolvedFaceSolid) + " #" + revolvedFaceSolid.id() + ": shellFaces=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, reasonCounts);
                    appendUnsupportedReasonCodes(lines, reasonCodeCounts);
                    unsupportedFaces++;
                    mergeReasonCounts(unsupportedReasons, reasonCounts);
                    mergeReasonCounts(unsupportedReasonCodes, reasonCodeCounts);
                }
                solids++;
            } else if (entity instanceof StepSolidReplica) {
                StepSolidReplica solidReplica = (StepSolidReplica) entity;
                try {
                    int faceCount = builder.buildSolid(solidReplica.id()).outerShell().faces().size();
                    lines.add("  " + stepEntityTypeName(solidReplica) + " #" + solidReplica.id() + ": shellFaces=" + faceCount + ", unsupportedFaces=0");
                } catch (UnsupportedGeometryException ex) {
                    Map<String, Integer> reasonCounts = Map.of(ex.getMessage(), 1);
                    Map<String, Integer> reasonCodeCounts = Map.of("unsupported_solid.replica", 1);
                    lines.add("  " + stepEntityTypeName(solidReplica) + " #" + solidReplica.id() + ": shellFaces=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, reasonCounts);
                    appendUnsupportedReasonCodes(lines, reasonCodeCounts);
                    unsupportedFaces++;
                    mergeReasonCounts(unsupportedReasons, reasonCounts);
                    mergeReasonCounts(unsupportedReasonCodes, reasonCodeCounts);
                }
                solids++;
            } else if (entity instanceof StepCsgSolid) {
                StepCsgSolid csgSolid = (StepCsgSolid) entity;
                try {
                    int faceCount = builder.buildSolid(csgSolid.id()).outerShell().faces().size();
                    lines.add("  " + stepEntityTypeName(csgSolid) + " #" + csgSolid.id() + ": shellFaces=" + faceCount + ", unsupportedFaces=0");
                } catch (UnsupportedGeometryException ex) {
                    Map<String, Integer> reasonCounts = Map.of(ex.getMessage(), 1);
                    Map<String, Integer> reasonCodeCounts = Map.of("unsupported_solid.csg", 1);
                    lines.add("  " + stepEntityTypeName(csgSolid) + " #" + csgSolid.id() + ": shellFaces=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, reasonCounts);
                    appendUnsupportedReasonCodes(lines, reasonCodeCounts);
                    unsupportedFaces++;
                    mergeReasonCounts(unsupportedReasons, reasonCounts);
                    mergeReasonCounts(unsupportedReasonCodes, reasonCodeCounts);
                }
                solids++;
            } else if (entity instanceof StepCsgPrimitive) {
                StepCsgPrimitive csgPrimitive = (StepCsgPrimitive) entity;
                try {
                    int faceCount = builder.buildSolid(csgPrimitive.id()).outerShell().faces().size();
                    lines.add("  " + stepEntityTypeName(csgPrimitive) + " #" + csgPrimitive.id() + ": shellFaces=" + faceCount + ", unsupportedFaces=0");
                } catch (UnsupportedGeometryException ex) {
                    Map<String, Integer> reasonCounts = Map.of(ex.getMessage(), 1);
                    Map<String, Integer> reasonCodeCounts = Map.of("unsupported_solid.csg_primitive", 1);
                    lines.add("  " + stepEntityTypeName(csgPrimitive) + " #" + csgPrimitive.id() + ": shellFaces=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, reasonCounts);
                    appendUnsupportedReasonCodes(lines, reasonCodeCounts);
                    unsupportedFaces++;
                    mergeReasonCounts(unsupportedReasons, reasonCounts);
                    mergeReasonCounts(unsupportedReasonCodes, reasonCodeCounts);
                }
                solids++;
            } else if (entity instanceof StepBooleanClippingResult) {
                StepBooleanClippingResult clippingResult = (StepBooleanClippingResult) entity;
                booleanResults++;
                try {
                    int faceCount = builder.buildSolid(clippingResult.id()).outerShell().faces().size();
                    lines.add("  " + stepEntityTypeName(clippingResult) + " #" + clippingResult.id() + ": faces=" + faceCount + ", unsupportedFaces=0");
                } catch (UnsupportedGeometryException ex) {
                    Map<String, Integer> reasonCounts = Map.of(ex.getMessage(), 1);
                    Map<String, Integer> reasonCodeCounts = Map.of("unsupported_boolean.clipping_result", 1);
                    lines.add("  " + stepEntityTypeName(clippingResult) + " #" + clippingResult.id() + ": faces=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, reasonCounts);
                    appendUnsupportedReasonCodes(lines, reasonCodeCounts);
                    unsupportedFaces++;
                    mergeReasonCounts(unsupportedReasons, reasonCounts);
                    mergeReasonCounts(unsupportedReasonCodes, reasonCodeCounts);
                }
            } else if (entity instanceof StepBooleanResult) {
                StepBooleanResult booleanResult = (StepBooleanResult) entity;
                booleanResults++;
                try {
                    int faceCount = builder.buildSolid(booleanResult.id()).outerShell().faces().size();
                    lines.add("  " + stepEntityTypeName(booleanResult) + " #" + booleanResult.id() + ": faces=" + faceCount + ", unsupportedFaces=0");
                } catch (UnsupportedGeometryException ex) {
                    Map<String, Integer> reasonCounts = Map.of(ex.getMessage(), 1);
                    Map<String, Integer> reasonCodeCounts = Map.of("unsupported_boolean.result", 1);
                    lines.add("  " + stepEntityTypeName(booleanResult) + " #" + booleanResult.id() + ": faces=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, reasonCounts);
                    appendUnsupportedReasonCodes(lines, reasonCodeCounts);
                    unsupportedFaces++;
                    mergeReasonCounts(unsupportedReasons, reasonCounts);
                    mergeReasonCounts(unsupportedReasonCodes, reasonCodeCounts);
                }
            } else if (entity instanceof StepFaceEntity && !shellFaceIds.contains(((StepFaceEntity) entity).id())) {
                StepFaceEntity face = (StepFaceEntity) entity;
                standaloneFaceEntities++;
                try {
                    builder.buildFace(face.id());
                    lines.add("  " + stepEntityTypeName(face) + " #" + face.id() + ": built=true, unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(face) + " #" + face.id() + ": built=false, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepOrientedEdge && !loopOrientedEdgeIds.contains(((StepOrientedEdge) entity).id())) {
                StepOrientedEdge orientedEdge = (StepOrientedEdge) entity;
                standaloneEdgeEntities++;
                try {
                    builder.buildOrientedEdge(orientedEdge.id());
                    lines.add("  " + stepEntityTypeName(orientedEdge) + " #" + orientedEdge.id() + ": built=true, unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(orientedEdge) + " #" + orientedEdge.id() + ": built=false, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepEdgeCurve && !orientedEdgeElementIds.contains(((StepEdgeCurve) entity).id())) {
                StepEdgeCurve edgeCurve = (StepEdgeCurve) entity;
                standaloneEdgeEntities++;
                try {
                    builder.buildEdge(edgeCurve.id());
                    lines.add("  " + stepEntityTypeName(edgeCurve) + " #" + edgeCurve.id() + ": built=true, unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(edgeCurve) + " #" + edgeCurve.id() + ": built=false, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepSubedge && !orientedEdgeElementIds.contains(((StepSubedge) entity).id())) {
                StepSubedge subedge = (StepSubedge) entity;
                standaloneEdgeEntities++;
                try {
                    builder.buildEdge(subedge.id());
                    lines.add("  " + stepEntityTypeName(subedge) + " #" + subedge.id() + ": built=true, unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(subedge) + " #" + subedge.id() + ": built=false, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepEdgeLoop && !faceBoundLoopIds.contains(((StepEdgeLoop) entity).id())) {
                StepEdgeLoop edgeLoop = (StepEdgeLoop) entity;
                standaloneLoopEntities++;
                try {
                    builder.buildEdgeLoop(edgeLoop.id());
                    lines.add("  " + stepEntityTypeName(edgeLoop) + " #" + edgeLoop.id() + ": built=true, unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(edgeLoop) + " #" + edgeLoop.id() + ": built=false, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepVertexLoop && !faceBoundLoopIds.contains(((StepVertexLoop) entity).id())) {
                StepVertexLoop vertexLoop = (StepVertexLoop) entity;
                standaloneLoopEntities++;
                try {
                    builder.buildVertexLoop(vertexLoop.id());
                    lines.add("  " + stepEntityTypeName(vertexLoop) + " #" + vertexLoop.id() + ": built=true, unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(vertexLoop) + " #" + vertexLoop.id() + ": built=false, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepPolyLoop && !faceBoundLoopIds.contains(((StepPolyLoop) entity).id())) {
                StepPolyLoop polyLoop = (StepPolyLoop) entity;
                standaloneLoopEntities++;
                try {
                    validatePolyLoop(polyLoop, builder);
                    lines.add("  " + stepEntityTypeName(polyLoop) + " #" + polyLoop.id() + ": built=true, unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(polyLoop) + " #" + polyLoop.id() + ": built=false, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepPath) {
                StepPath path = (StepPath) entity;
                standalonePathEntities++;
                try {
                    validatePathEdges(path.edges(), builder);
                    lines.add("  " + stepEntityTypeName(path) + " #" + path.id() + ": built=true, unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(path) + " #" + path.id() + ": built=false, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepOpenPath) {
                StepOpenPath openPath = (StepOpenPath) entity;
                standalonePathEntities++;
                try {
                    validatePathEdges(openPath.edges(), builder);
                    lines.add("  " + stepEntityTypeName(openPath) + " #" + openPath.id() + ": built=true, unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(openPath) + " #" + openPath.id() + ": built=false, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepSubpath) {
                StepSubpath subpath = (StepSubpath) entity;
                standalonePathEntities++;
                try {
                    validatePathEdges(subpath.edges(), builder);
                    lines.add("  " + stepEntityTypeName(subpath) + " #" + subpath.id() + ": built=true, unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(subpath) + " #" + subpath.id() + ": built=false, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepOrientedPath) {
                StepOrientedPath orientedPath = (StepOrientedPath) entity;
                standalonePathEntities++;
                try {
                    validatePathEdges(orientedPath.edges(), builder);
                    lines.add("  " + stepEntityTypeName(orientedPath) + " #" + orientedPath.id() + ": built=true, unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(orientedPath) + " #" + orientedPath.id() + ": built=false, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepConnectedEdgeSet) {
                StepConnectedEdgeSet edgeSet = (StepConnectedEdgeSet) entity;
                standaloneContainerEntities++;
                try {
                    int edgeCount = validateConnectedEdgeSet(edgeSet, builder);
                    lines.add("  " + stepEntityTypeName(edgeSet) + " #" + edgeSet.id() + ": builtEdges=" + edgeCount + ", unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(edgeSet) + " #" + edgeSet.id() + ": builtEdges=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepWireShell) {
                StepWireShell wireShell = (StepWireShell) entity;
                standaloneContainerEntities++;
                try {
                    int loopCount = validateWireShell(wireShell, builder);
                    lines.add("  " + stepEntityTypeName(wireShell) + " #" + wireShell.id() + ": builtLoops=" + loopCount + ", unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(wireShell) + " #" + wireShell.id() + ": builtLoops=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepVertexShell) {
                StepVertexShell vertexShell = (StepVertexShell) entity;
                standaloneContainerEntities++;
                try {
                    builder.buildVertexLoop(vertexShell.extent().id());
                    lines.add("  " + stepEntityTypeName(vertexShell) + " #" + vertexShell.id() + ": builtVertices=1, unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(vertexShell) + " #" + vertexShell.id() + ": builtVertices=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepEdgeBasedWireframeModel) {
                StepEdgeBasedWireframeModel wireframeModel = (StepEdgeBasedWireframeModel) entity;
                standaloneContainerEntities++;
                try {
                    int edgeCount = 0;
                    for (StepConnectedEdgeSet boundary : wireframeModel.boundaries()) {
                        edgeCount += validateConnectedEdgeSet(boundary, builder);
                    }
                    lines.add("  " + stepEntityTypeName(wireframeModel) + " #" + wireframeModel.id() + ": builtEdges=" + edgeCount + ", unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(wireframeModel) + " #" + wireframeModel.id() + ": builtEdges=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepShellBasedWireframeModel) {
                StepShellBasedWireframeModel wireframeModel = (StepShellBasedWireframeModel) entity;
                standaloneContainerEntities++;
                try {
                    int memberCount = validateShellBasedWireframeModel(wireframeModel, builder);
                    lines.add("  " + stepEntityTypeName(wireframeModel) + " #" + wireframeModel.id() + ": builtBoundaries=" + memberCount + ", unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(wireframeModel) + " #" + wireframeModel.id() + ": builtBoundaries=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepFaceBasedSurfaceModel) {
                StepFaceBasedSurfaceModel surfaceModel = (StepFaceBasedSurfaceModel) entity;
                standaloneContainerEntities++;
                try {
                    FaceBuildCounts counts = validateFaceBasedSurfaceModel(surfaceModel, builder);
                    lines.add("  " + stepEntityTypeName(surfaceModel) + " #" + surfaceModel.id() + ": faces=" + counts.supportedFaces()
                            + ", unsupportedFaces=" + counts.unsupportedFaces());
                    appendUnsupportedReasons(lines, counts.unsupportedReasons());
                    appendUnsupportedReasonCodes(lines, counts.unsupportedReasonCodes());
                    unsupportedFaces += counts.unsupportedFaces();
                    mergeReasonCounts(unsupportedReasons, counts.unsupportedReasons());
                    mergeReasonCounts(unsupportedReasonCodes, counts.unsupportedReasonCodes());
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(surfaceModel) + " #" + surfaceModel.id() + ": faces=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepShellBasedSurfaceModel) {
                StepShellBasedSurfaceModel surfaceModel = (StepShellBasedSurfaceModel) entity;
                standaloneContainerEntities++;
                try {
                    FaceBuildCounts counts = validateShellBasedSurfaceModel(surfaceModel, builder);
                    lines.add("  " + stepEntityTypeName(surfaceModel) + " #" + surfaceModel.id() + ": faces=" + counts.supportedFaces()
                            + ", unsupportedFaces=" + counts.unsupportedFaces());
                    appendUnsupportedReasons(lines, counts.unsupportedReasons());
                    appendUnsupportedReasonCodes(lines, counts.unsupportedReasonCodes());
                    unsupportedFaces += counts.unsupportedFaces();
                    mergeReasonCounts(unsupportedReasons, counts.unsupportedReasons());
                    mergeReasonCounts(unsupportedReasonCodes, counts.unsupportedReasonCodes());
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(surfaceModel) + " #" + surfaceModel.id() + ": faces=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepGeometricCurveSet) {
                StepGeometricCurveSet curveSet = (StepGeometricCurveSet) entity;
                standaloneContainerEntities++;
                try {
                    int memberCount = validateGeometricCurveSet(curveSet, builder);
                    lines.add("  " + stepEntityTypeName(curveSet) + " #" + curveSet.id() + ": builtMembers=" + memberCount + ", unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(curveSet) + " #" + curveSet.id() + ": builtMembers=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepPointSet) {
                StepPointSet pointSet = (StepPointSet) entity;
                standaloneContainerEntities++;
                try {
                    int memberCount = validatePointSet(pointSet, builder);
                    lines.add("  " + stepEntityTypeName(pointSet) + " #" + pointSet.id() + ": builtMembers=" + memberCount + ", unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(pointSet) + " #" + pointSet.id() + ": builtMembers=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepGeometricSet) {
                StepGeometricSet geometricSet = (StepGeometricSet) entity;
                standaloneContainerEntities++;
                try {
                    int memberCount = validateGeometricSet(geometricSet, builder);
                    lines.add("  " + stepEntityTypeName(geometricSet) + " #" + geometricSet.id() + ": builtMembers=" + memberCount + ", unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(geometricSet) + " #" + geometricSet.id() + ": builtMembers=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepRepresentation) {
                StepRepresentation representation = (StepRepresentation) entity;
                standaloneContainerEntities++;
                try {
                    int itemCount = validateRepresentation(representation, builder);
                    lines.add("  " + stepEntityTypeName(representation) + " #" + representation.id() + ": builtItems=" + itemCount + ", unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    if (is2DPcurveEntity(entity)) {
                        standaloneContainerEntities--;
                        skipped2DEntities++;
                        continue;
                    }
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(representation) + " #" + representation.id() + ": builtItems=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepRepresentationMap) {
                StepRepresentationMap representationMap = (StepRepresentationMap) entity;
                standaloneContainerEntities++;
                try {
                    int itemCount = validateRepresentationMap(representationMap, builder);
                    lines.add("  " + stepEntityTypeName(representationMap) + " #" + representationMap.id() + ": builtItems=" + itemCount + ", unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(representationMap) + " #" + representationMap.id() + ": builtItems=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepMappedItem) {
                StepMappedItem mappedItem = (StepMappedItem) entity;
                standaloneContainerEntities++;
                try {
                    int itemCount = validateMappedItem(mappedItem, builder);
                    lines.add("  " + stepEntityTypeName(mappedItem) + " #" + mappedItem.id() + ": builtItems=" + itemCount + ", unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(mappedItem) + " #" + mappedItem.id() + ": builtItems=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepStyledItem) {
                StepStyledItem styledItem = (StepStyledItem) entity;
                standaloneContainerEntities++;
                try {
                    int itemCount = validateStyledItem(styledItem, builder);
                    lines.add("  " + stepEntityTypeName(styledItem) + " #" + styledItem.id() + ": builtItems=" + itemCount + ", unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(styledItem) + " #" + styledItem.id() + ": builtItems=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepOverRidingStyledItem) {
                StepOverRidingStyledItem styledItem = (StepOverRidingStyledItem) entity;
                standaloneContainerEntities++;
                try {
                    int itemCount = validateOverridingStyledItem(styledItem, builder);
                    lines.add("  " + stepEntityTypeName(styledItem) + " #" + styledItem.id() + ": builtItems=" + itemCount + ", unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(styledItem) + " #" + styledItem.id() + ": builtItems=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepRepresentationRelationship) {
                StepRepresentationRelationship relationship = (StepRepresentationRelationship) entity;
                standaloneContainerEntities++;
                try {
                    int itemCount = validateRepresentationRelationship(relationship, builder);
                    lines.add("  " + stepEntityTypeName(relationship) + " #" + relationship.id() + ": builtItems=" + itemCount + ", unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(relationship) + " #" + relationship.id() + ": builtItems=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepRepresentationRelationshipWithTransformation) {
                StepRepresentationRelationshipWithTransformation relationship = (StepRepresentationRelationshipWithTransformation) entity;
                standaloneContainerEntities++;
                try {
                    int itemCount = validateRepresentationRelationshipWithTransformation(relationship, builder);
                    lines.add("  " + stepEntityTypeName(relationship) + " #" + relationship.id() + ": builtItems=" + itemCount + ", unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(relationship) + " #" + relationship.id() + ": builtItems=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else if (entity instanceof StepShapeRepresentationRelationship) {
                StepShapeRepresentationRelationship relationship = (StepShapeRepresentationRelationship) entity;
                standaloneContainerEntities++;
                try {
                    int itemCount = validateShapeRepresentationRelationship(relationship, builder);
                    lines.add("  " + stepEntityTypeName(relationship) + " #" + relationship.id() + ": builtItems=" + itemCount + ", unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(relationship) + " #" + relationship.id() + ": builtItems=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            } else {
                standaloneContainerEntities++;
                try {
                    int itemCount = validateSummaryEntity(entity, builder);
                    lines.add("  " + stepEntityTypeName(entity) + " #" + entity.id() + ": builtItems=" + itemCount + ", unsupportedFaces=0");
                } catch (UnsupportedGeometryException ex) {
                    String reason = normalizeReason(ex.getMessage());
                    if (isGenericDumpUnsupported(entity, reason)) {
                        standaloneContainerEntities--;
                        continue;
                    }
                    if (is2DPcurveEntity(entity)) {
                        standaloneContainerEntities--;
                        skipped2DEntities++;
                        continue;
                    }
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(entity) + " #" + entity.id() + ": builtItems=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                } catch (GeometryException | TopologyException | StepResolutionException ex) {
                    if (is2DPcurveEntity(entity)) {
                        standaloneContainerEntities--;
                        skipped2DEntities++;
                        continue;
                    }
                    String reason = normalizeReason(ex.getMessage());
                    String reasonCode = classifyReasonCode(ex, reason);
                    lines.add("  " + stepEntityTypeName(entity) + " #" + entity.id() + ": builtItems=0, unsupportedFaces=1");
                    appendUnsupportedReasons(lines, Map.of(reason, 1));
                    appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
                    unsupportedFaces++;
                    unsupportedReasons.merge(reason, 1, Integer::sum);
                    unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
                }
            }
        }

        lines.add("  totals: openShells=" + openShells + ", closedShells=" + closedShells
                + ", solids=" + solids + ", booleanResults=" + booleanResults
                + ", standaloneFaceEntities=" + standaloneFaceEntities
                + ", standaloneEdgeEntities=" + standaloneEdgeEntities
                + ", standaloneLoopEntities=" + standaloneLoopEntities
                + ", standalonePathEntities=" + standalonePathEntities
                + ", standaloneContainerEntities=" + standaloneContainerEntities
                + ", skipped2DEntities=" + skipped2DEntities
                + ", unsupportedFaces=" + unsupportedFaces);
        appendUnsupportedReasons(lines, unsupportedReasons);
        appendUnsupportedReasonCodes(lines, unsupportedReasonCodes);
    }

    private static void validatePolyLoop(StepPolyLoop polyLoop, StepCadBuilder builder) {
        for (var point : polyLoop.polygon()) {
            builder.buildPoint(point.id());
        }
    }

    private static void validatePathEdges(List<StepOrientedEdge> edges, StepCadBuilder builder) {
        for (StepOrientedEdge edge : edges) {
            builder.buildOrientedEdge(edge.id());
        }
    }

    private static int validateConnectedEdgeSet(StepConnectedEdgeSet edgeSet, StepCadBuilder builder) {
        int count = 0;
        for (StepEntity edge : edgeSet.edges()) {
            if (edge instanceof StepEdgeCurve) {
                StepEdgeCurve edgeCurve = (StepEdgeCurve) edge;
                builder.buildEdge(edgeCurve.id());
                count++;
            } else if (edge instanceof StepSubedge) {
                StepSubedge subedge = (StepSubedge) edge;
                builder.buildEdge(subedge.id());
                count++;
            } else if (edge instanceof StepOrientedEdge) {
                StepOrientedEdge orientedEdge = (StepOrientedEdge) edge;
                builder.buildOrientedEdge(orientedEdge.id());
                count++;
            } else {
                throw new UnsupportedGeometryException("CONNECTED_EDGE_SET requires EDGE_CURVE, SUBEDGE or ORIENTED_EDGE members");
            }
        }
        return count;
    }

    private static int validateWireShell(StepWireShell wireShell, StepCadBuilder builder) {
        int count = 0;
        for (var loop : wireShell.loops()) {
            if (loop instanceof StepEdgeLoop) {
                StepEdgeLoop edgeLoop = (StepEdgeLoop) loop;
                builder.buildEdgeLoop(edgeLoop.id());
            } else if (loop instanceof StepVertexLoop) {
                StepVertexLoop vertexLoop = (StepVertexLoop) loop;
                builder.buildVertexLoop(vertexLoop.id());
            } else if (loop instanceof StepPolyLoop) {
                StepPolyLoop polyLoop = (StepPolyLoop) loop;
                validatePolyLoop(polyLoop, builder);
            } else {
                throw new UnsupportedGeometryException("WIRE_SHELL requires EDGE_LOOP, VERTEX_LOOP or POLY_LOOP members");
            }
            count++;
        }
        return count;
    }

    private static int validateShellBasedWireframeModel(StepShellBasedWireframeModel wireframeModel, StepCadBuilder builder) {
        int count = 0;
        for (StepEntity boundary : wireframeModel.boundaries()) {
            if (boundary instanceof StepWireShell) {
                StepWireShell wireShell = (StepWireShell) boundary;
                validateWireShell(wireShell, builder);
            } else if (boundary instanceof StepVertexShell) {
                StepVertexShell vertexShell = (StepVertexShell) boundary;
                builder.buildVertexLoop(vertexShell.extent().id());
            } else {
                throw new UnsupportedGeometryException("SHELL_BASED_WIREFRAME_MODEL requires WIRE_SHELL or VERTEX_SHELL boundaries");
            }
            count++;
        }
        return count;
    }

    private static FaceBuildCounts validateFaceBasedSurfaceModel(StepFaceBasedSurfaceModel surfaceModel, StepCadBuilder builder) {
        FaceBuildCounts counts = new FaceBuildCounts(0, 0, Map.of(), Map.of());
        for (StepEntity faceSet : surfaceModel.faceSets()) {
            if (faceSet instanceof StepConnectedFaceSet) {
                StepConnectedFaceSet connectedFaceSet = (StepConnectedFaceSet) faceSet;
                counts = counts.plus(summarizeShell(connectedFaceSet.faces(), builder));
            } else if (faceSet instanceof StepConnectedFaceSubSet) {
                StepConnectedFaceSubSet connectedFaceSubSet = (StepConnectedFaceSubSet) faceSet;
                counts = counts.plus(summarizeShell(connectedFaceSubSet.faces(), builder));
            } else if (faceSet instanceof StepOpenShell
                    || faceSet instanceof StepSurfacedOpenShell
                    || faceSet instanceof StepOrientedOpenShell
                    || faceSet instanceof StepClosedShell
                    || faceSet instanceof StepOrientedClosedShell) {
                counts = counts.plus(summarizeShell(shellFaces(faceSet), builder));
            } else {
                throw new UnsupportedGeometryException(
                        "FACE_BASED_SURFACE_MODEL requires CONNECTED_FACE_SET, CONNECTED_FACE_SUB_SET or shell members");
            }
        }
        return counts;
    }

    private static FaceBuildCounts validateShellBasedSurfaceModel(StepShellBasedSurfaceModel surfaceModel, StepCadBuilder builder) {
        FaceBuildCounts counts = new FaceBuildCounts(0, 0, Map.of(), Map.of());
        for (StepEntity shell : surfaceModel.shells()) {
            counts = counts.plus(summarizeShell(shellFaces(shell), builder));
        }
        return counts;
    }

    private static int validateGeometricCurveSet(StepGeometricCurveSet curveSet, StepCadBuilder builder) {
        int count = 0;
        for (StepEntity element : curveSet.elements()) {
            if (element instanceof StepCartesianPoint) {
                StepCartesianPoint point = (StepCartesianPoint) element;
                builder.buildPoint(point.id());
            } else if (element instanceof StepVertexPoint) {
                StepVertexPoint vertexPoint = (StepVertexPoint) element;
                builder.buildVertex(vertexPoint.id());
            } else if (element instanceof StepGeometricReplica && "POINT_REPLICA".equals(((StepGeometricReplica) element).entityName())) {
                StepGeometricReplica replica = (StepGeometricReplica) element;
                builder.buildPointReference(replica.id());
            } else if (element instanceof StepLine) {
                StepLine line = (StepLine) element;
                builder.buildLine(line.id());
            } else if (element instanceof StepCircle) {
                StepCircle circle = (StepCircle) element;
                builder.buildCircle(circle.id());
            } else if (element instanceof StepEllipse) {
                StepEllipse ellipse = (StepEllipse) element;
                builder.buildEllipse(ellipse.id());
            } else if (element instanceof StepPolyline) {
                StepPolyline polyline = (StepPolyline) element;
                builder.buildPolyline(polyline.id());
            } else if (element instanceof StepEdgeCurve) {
                StepEdgeCurve edgeCurve = (StepEdgeCurve) element;
                builder.buildEdge(edgeCurve.id());
            } else if (element instanceof StepSubedge) {
                StepSubedge subedge = (StepSubedge) element;
                builder.buildEdge(subedge.id());
            } else if (element instanceof StepOrientedEdge) {
                StepOrientedEdge orientedEdge = (StepOrientedEdge) element;
                builder.buildOrientedEdge(orientedEdge.id());
            } else if (element instanceof StepConnectedEdgeSet) {
                StepConnectedEdgeSet edgeSet = (StepConnectedEdgeSet) element;
                validateConnectedEdgeSet(edgeSet, builder);
            } else if (element instanceof StepEdgeLoop) {
                StepEdgeLoop edgeLoop = (StepEdgeLoop) element;
                validatePathEdges(edgeLoop.edges(), builder);
            } else if (element instanceof StepVertexLoop) {
                StepVertexLoop vertexLoop = (StepVertexLoop) element;
                builder.buildVertexLoop(vertexLoop.id());
            } else if (element instanceof StepPath) {
                StepPath path = (StepPath) element;
                validatePathEdges(path.edges(), builder);
            } else if (element instanceof StepOpenPath) {
                StepOpenPath openPath = (StepOpenPath) element;
                validatePathEdges(openPath.edges(), builder);
            } else if (element instanceof StepSubpath) {
                StepSubpath subpath = (StepSubpath) element;
                validatePathEdges(subpath.edges(), builder);
            } else if (element instanceof StepOrientedPath) {
                StepOrientedPath orientedPath = (StepOrientedPath) element;
                validatePathEdges(orientedPath.edges(), builder);
            } else if (element instanceof StepPolyLoop) {
                StepPolyLoop polyLoop = (StepPolyLoop) element;
                validatePolyLoop(polyLoop, builder);
            } else if (element instanceof StepWireShell) {
                StepWireShell wireShell = (StepWireShell) element;
                validateWireShell(wireShell, builder);
            } else if (element instanceof StepVertexShell
                    || element instanceof StepEdgeBasedWireframeModel
                    || element instanceof StepShellBasedWireframeModel) {
                validateSummaryEntity(element, builder);
            } else if (element instanceof StepPointSet) {
                StepPointSet pointSet = (StepPointSet) element;
                validatePointSet(pointSet, builder);
            } else if (element instanceof StepGeometricSet) {
                StepGeometricSet geometricSet = (StepGeometricSet) element;
                validateGeometricSet(geometricSet, builder);
            } else if (element instanceof StepGeometricCurveSet) {
                StepGeometricCurveSet nestedCurveSet = (StepGeometricCurveSet) element;
                validateGeometricCurveSet(nestedCurveSet, builder);
            } else {
                throw new UnsupportedGeometryException(
                        "GEOMETRIC_CURVE_SET requires supported curve, point, path, wire, topology or nested set members");
            }
            count++;
        }
        return count;
    }

    private static int validatePointSet(StepPointSet pointSet, StepCadBuilder builder) {
        int count = 0;
        for (StepEntity point : pointSet.points()) {
            if (point instanceof StepCartesianPoint) {
                StepCartesianPoint cartesianPoint = (StepCartesianPoint) point;
                builder.buildPoint(cartesianPoint.id());
            } else if (point instanceof StepGeometricReplica && "POINT_REPLICA".equals(((StepGeometricReplica) point).entityName())) {
                StepGeometricReplica replica = (StepGeometricReplica) point;
                builder.buildPointReference(replica.id());
            } else if (point instanceof StepVertexPoint) {
                StepVertexPoint vertexPoint = (StepVertexPoint) point;
                builder.buildVertex(vertexPoint.id());
            } else if (point instanceof StepVertexShell
                    || point instanceof StepAnnotationSymbol
                    || point instanceof StepAnnotationText
                    || point instanceof StepAnnotationTextCharacter
                    || point instanceof StepAnnotationFillArea
                    || point instanceof StepAnnotationPointOccurrence
                    || point instanceof StepAnnotationFillAreaOccurrence
                    || point instanceof StepAnnotationTextOccurrence
                    || point instanceof StepAnnotationPlaceholderOccurrence
                    || point instanceof StepAnnotationSymbolOccurrence
                    || point instanceof StepAnnotationSubfigureOccurrence
                    || point instanceof StepDraughtingAnnotationOccurrence
                    || point instanceof StepAnnotationPlane) {
                validateSummaryEntity(point, builder);
            } else if (point instanceof StepPointSet) {
                StepPointSet nestedPointSet = (StepPointSet) point;
                validatePointSet(nestedPointSet, builder);
            } else if (point instanceof StepGeometricSet) {
                StepGeometricSet geometricSet = (StepGeometricSet) point;
                validateGeometricSet(geometricSet, builder);
            } else if (point instanceof StepGeometricCurveSet) {
                StepGeometricCurveSet curveSet = (StepGeometricCurveSet) point;
                validateGeometricCurveSet(curveSet, builder);
            } else {
                throw new UnsupportedGeometryException(
                        "POINT_SET requires supported point carriers, point-like annotation content/occurrences or nested point containers");
            }
            count++;
        }
        return count;
    }

    private static int validateGeometricSet(StepGeometricSet geometricSet, StepCadBuilder builder) {
        int count = 0;
        for (StepEntity element : geometricSet.elements()) {
            if (element instanceof StepCartesianPoint) {
                StepCartesianPoint cartesianPoint = (StepCartesianPoint) element;
                builder.buildPoint(cartesianPoint.id());
            } else if (element instanceof StepGeometricReplica && "POINT_REPLICA".equals(((StepGeometricReplica) element).entityName())) {
                StepGeometricReplica replica = (StepGeometricReplica) element;
                builder.buildPointReference(replica.id());
            } else if (element instanceof StepVertexPoint) {
                StepVertexPoint vertexPoint = (StepVertexPoint) element;
                builder.buildVertex(vertexPoint.id());
            } else if (element instanceof StepLine) {
                StepLine line = (StepLine) element;
                builder.buildLine(line.id());
            } else if (element instanceof StepCircle) {
                StepCircle circle = (StepCircle) element;
                builder.buildCircle(circle.id());
            } else if (element instanceof StepEllipse) {
                StepEllipse ellipse = (StepEllipse) element;
                builder.buildEllipse(ellipse.id());
            } else if (element instanceof StepPolyline) {
                StepPolyline polyline = (StepPolyline) element;
                builder.buildPolyline(polyline.id());
            } else if (element instanceof StepEdgeCurve) {
                StepEdgeCurve edgeCurve = (StepEdgeCurve) element;
                builder.buildEdge(edgeCurve.id());
            } else if (element instanceof StepSubedge) {
                StepSubedge subedge = (StepSubedge) element;
                builder.buildEdge(subedge.id());
            } else if (element instanceof StepOrientedEdge) {
                StepOrientedEdge orientedEdge = (StepOrientedEdge) element;
                builder.buildOrientedEdge(orientedEdge.id());
            } else if (element instanceof StepConnectedEdgeSet) {
                StepConnectedEdgeSet edgeSet = (StepConnectedEdgeSet) element;
                validateConnectedEdgeSet(edgeSet, builder);
            } else if (element instanceof StepEdgeLoop) {
                StepEdgeLoop edgeLoop = (StepEdgeLoop) element;
                validatePathEdges(edgeLoop.edges(), builder);
            } else if (element instanceof StepVertexLoop) {
                StepVertexLoop vertexLoop = (StepVertexLoop) element;
                builder.buildVertexLoop(vertexLoop.id());
            } else if (element instanceof StepWireShell) {
                StepWireShell wireShell = (StepWireShell) element;
                validateWireShell(wireShell, builder);
            } else if (element instanceof StepOpenShell
                    || element instanceof StepSurfacedOpenShell
                    || element instanceof StepOrientedOpenShell
                    || element instanceof StepClosedShell
                    || element instanceof StepOrientedClosedShell
                    || element instanceof StepConnectedFaceSet
                    || element instanceof StepConnectedFaceSubSet
                    || element instanceof StepFaceBasedSurfaceModel
                    || element instanceof StepShellBasedSurfaceModel
                    || element instanceof StepEdgeBasedWireframeModel
                    || element instanceof StepShellBasedWireframeModel
                    || element instanceof StepManifoldSolidBrep
                    || element instanceof StepBrepWithVoids
                    || element instanceof StepSweptAreaSolid
                    || element instanceof StepExtrudedFaceSolid
                    || element instanceof StepRevolvedFaceSolid
                    || element instanceof StepSolidReplica
                    || element instanceof StepCsgSolid
                    || element instanceof StepCsgPrimitive
                    || element instanceof StepBooleanResult
                    || element instanceof StepBooleanClippingResult) {
                validateSummaryEntity(element, builder);
            } else if (element instanceof StepPath) {
                StepPath path = (StepPath) element;
                validatePathEdges(path.edges(), builder);
            } else if (element instanceof StepOpenPath) {
                StepOpenPath openPath = (StepOpenPath) element;
                validatePathEdges(openPath.edges(), builder);
            } else if (element instanceof StepSubpath) {
                StepSubpath subpath = (StepSubpath) element;
                validatePathEdges(subpath.edges(), builder);
            } else if (element instanceof StepOrientedPath) {
                StepOrientedPath orientedPath = (StepOrientedPath) element;
                validatePathEdges(orientedPath.edges(), builder);
            } else if (element instanceof StepPolyLoop) {
                StepPolyLoop polyLoop = (StepPolyLoop) element;
                validatePolyLoop(polyLoop, builder);
            } else if (isSupportedGeometricSetSurface(element)) {
                validateSupportedSurfaceReference(element, builder);
            } else if (element instanceof StepPointSet) {
                StepPointSet pointSet = (StepPointSet) element;
                validatePointSet(pointSet, builder);
            } else if (element instanceof StepGeometricSet) {
                StepGeometricSet nestedGeometricSet = (StepGeometricSet) element;
                validateGeometricSet(nestedGeometricSet, builder);
            } else if (element instanceof StepGeometricCurveSet) {
                StepGeometricCurveSet curveSet = (StepGeometricCurveSet) element;
                validateGeometricCurveSet(curveSet, builder);
            } else {
                throw new UnsupportedGeometryException(
                        "GEOMETRIC_SET requires supported point, curve, surface, path, topology, shell/model/solid container or nested set members");
            }
            count++;
        }
        return count;
    }

    private static void validateSupportedSurfaceReference(StepEntity surface, StepCadBuilder builder) {
        if (surface instanceof StepPlane) {
            StepPlane plane = (StepPlane) surface;
            builder.buildPlane(plane.id());
        } else if (surface instanceof StepCylindricalSurface) {
            StepCylindricalSurface cylindricalSurface = (StepCylindricalSurface) surface;
            builder.buildCylindricalSurface(cylindricalSurface.id());
        } else if (surface instanceof StepConicalSurface) {
            StepConicalSurface conicalSurface = (StepConicalSurface) surface;
            builder.buildConicalSurface(conicalSurface.id());
        } else if (surface instanceof StepSphericalSurface) {
            StepSphericalSurface sphericalSurface = (StepSphericalSurface) surface;
            builder.buildSphericalSurface(sphericalSurface.id());
        } else if (surface instanceof StepToroidalSurface) {
            StepToroidalSurface toroidalSurface = (StepToroidalSurface) surface;
            builder.buildToroidalSurface(toroidalSurface.id());
        } else if (surface instanceof StepDegenerateToroidalSurface) {
            StepDegenerateToroidalSurface degenerateToroidalSurface = (StepDegenerateToroidalSurface) surface;
            builder.buildDegenerateToroidalSurface(degenerateToroidalSurface.id());
        } else if (surface instanceof StepSurfaceOfLinearExtrusion) {
            StepSurfaceOfLinearExtrusion extrusionSurface = (StepSurfaceOfLinearExtrusion) surface;
            builder.buildSurfaceOfLinearExtrusion(extrusionSurface.id());
        } else if (surface instanceof StepSurfaceOfRevolution) {
            StepSurfaceOfRevolution revolutionSurface = (StepSurfaceOfRevolution) surface;
            builder.buildSurfaceOfRevolution(revolutionSurface.id());
        } else if (surface instanceof StepBSplineSurfaceWithKnots) {
            StepBSplineSurfaceWithKnots splineSurface = (StepBSplineSurfaceWithKnots) surface;
            builder.buildBSplineSurface(splineSurface.id());
        } else if (surface instanceof StepRationalBSplineSurface) {
            StepRationalBSplineSurface rationalSplineSurface = (StepRationalBSplineSurface) surface;
            builder.buildRationalBSplineSurface(rationalSplineSurface.id());
        } else if (surface instanceof StepRectangularTrimmedSurface) {
            StepRectangularTrimmedSurface trimmedSurface = (StepRectangularTrimmedSurface) surface;
            builder.buildRectangularTrimmedSurface(trimmedSurface.id());
        } else if (surface instanceof StepCurveBoundedSurface) {
            StepCurveBoundedSurface boundedSurface = (StepCurveBoundedSurface) surface;
            builder.buildCurveBoundedSurface(boundedSurface.id());
        } else if (surface instanceof StepOrientedSurface) {
            StepOrientedSurface orientedSurface = (StepOrientedSurface) surface;
            builder.buildOrientedSurface(orientedSurface.id());
        } else if (surface instanceof StepOffsetSurface) {
            StepOffsetSurface offsetSurface = (StepOffsetSurface) surface;
            builder.buildOffsetSurface(offsetSurface.id());
        } else if (surface instanceof StepGeometricReplica && "SURFACE_REPLICA".equals(((StepGeometricReplica) surface).entityName())) {
            StepGeometricReplica replica = (StepGeometricReplica) surface;
            builder.buildSurfaceReplica(replica.id());
        } else {
            throw new UnsupportedGeometryException(
                    "GEOMETRIC_SET requires supported point, curve, surface, path, topology, shell/model/solid container or nested set members");
        }
    }

    private static boolean isSupportedGeometricSetSurface(StepEntity surface) {
        return surface instanceof StepPlane
                || surface instanceof StepCylindricalSurface
                || surface instanceof StepConicalSurface
                || surface instanceof StepSphericalSurface
                || surface instanceof StepToroidalSurface
                || surface instanceof StepDegenerateToroidalSurface
                || surface instanceof StepSurfaceOfLinearExtrusion
                || surface instanceof StepSurfaceOfRevolution
                || surface instanceof StepBSplineSurfaceWithKnots
                || surface instanceof StepRationalBSplineSurface
                || surface instanceof StepRectangularTrimmedSurface
                || surface instanceof StepCurveBoundedSurface
                || surface instanceof StepOrientedSurface
                || surface instanceof StepOffsetSurface
                || surface instanceof StepGeometricReplica && "SURFACE_REPLICA".equals(((StepGeometricReplica) surface).entityName());
    }

    private static int validateRepresentation(StepRepresentation representation, StepCadBuilder builder) {
        int count = 0;
        for (StepEntity item : representation.items()) {
            count += validateSummaryEntity(item, builder);
        }
        return count;
    }

    private static int validateRepresentationMap(StepRepresentationMap representationMap, StepCadBuilder builder) {
        validateSummaryEntity(representationMap.mappedOrigin(), builder);
        return validateRepresentation(representationMap.mappedRepresentation(), builder);
    }

    private static int validateMappedItem(StepMappedItem mappedItem, StepCadBuilder builder) {
        int count = validateRepresentationMap(mappedItem.mappingSource(), builder);
        validateSummaryEntity(mappedItem.mappingTarget(), builder);
        return count;
    }

    private static int validateStyledItem(StepStyledItem styledItem, StepCadBuilder builder) {
        return validateSummaryEntity(styledItem.item(), builder);
    }

    private static int validateOverridingStyledItem(StepOverRidingStyledItem styledItem, StepCadBuilder builder) {
        int count = validateSummaryEntity(styledItem.item(), builder);
        validateStyledItem(styledItem.overRiddenStyle(), builder);
        return count;
    }

    private static int validateRepresentationRelationship(StepRepresentationRelationship relationship, StepCadBuilder builder) {
        return validateRepresentation(relationship.rep1(), builder)
                + validateRepresentation(relationship.rep2(), builder);
    }

    private static int validateRepresentationRelationshipWithTransformation(
            StepRepresentationRelationshipWithTransformation relationship,
            StepCadBuilder builder
    ) {
        int count = validateRepresentation(relationship.rep1(), builder)
                + validateRepresentation(relationship.rep2(), builder);
        validateSummaryEntity(relationship.transformationOperator(), builder);
        return count;
    }

    private static int validateShapeRepresentationRelationship(
            StepShapeRepresentationRelationship relationship,
            StepCadBuilder builder
    ) {
        return validateRepresentation(relationship.rep1(), builder)
                + validateRepresentation(relationship.rep2(), builder);
    }

    private static int validateAnnotationCurveOccurrence(StepEntity item, StepCadBuilder builder) {
        return validateSummaryEntity(item, builder);
    }

    private static int validateAnnotationFillArea(StepAnnotationFillArea fillArea, StepCadBuilder builder) {
        int count = 0;
        for (StepEntity boundary : fillArea.boundaries()) {
            count += validateSummaryEntity(boundary, builder);
        }
        return count;
    }

    private static int validateAnnotationPlane(StepAnnotationPlane plane, StepCadBuilder builder) {
        int count = validateSummaryEntity(plane.item(), builder);
        for (StepEntity element : plane.elements()) {
            count += validateSummaryEntity(element, builder);
        }
        return count;
    }

    private static int validateDraughtingCallout(StepDraughtingCallout callout, StepCadBuilder builder) {
        int count = 0;
        for (StepEntity content : callout.contents()) {
            count += validateSummaryEntity(content, builder);
        }
        return count;
    }

    private static int validatePresentationStyleAssignment(StepPresentationStyleAssignment assignment, StepCadBuilder builder) {
        int count = 0;
        for (StepEntity style : assignment.styles()) {
            count += validateSummaryEntity(style, builder);
        }
        return count;
    }

    private static int validateCurveStyle(StepCurveStyle curveStyle, StepCadBuilder builder) {
        return validateSummaryEntity(curveStyle.curveFont(), builder)
                + validateSummaryEntity(curveStyle.colour(), builder);
    }

    private static int validateFillAreaStyle(StepFillAreaStyle fillAreaStyle, StepCadBuilder builder) {
        int count = 0;
        for (StepFillAreaStyleColour style : fillAreaStyle.styles()) {
            count += validateSummaryEntity(style, builder);
        }
        return count;
    }

    private static int validateSurfaceSideStyle(StepSurfaceSideStyle style, StepCadBuilder builder) {
        int count = 0;
        for (StepEntity item : style.styles()) {
            count += validateSummaryEntity(item, builder);
        }
        return count;
    }

    private static int validateSummaryItems(List<StepEntity> items, StepCadBuilder builder) {
        int count = 0;
        for (StepEntity item : items) {
            count += validateSummaryEntity(item, builder);
        }
        return count;
    }

    private static int validateRepresentationUsage(
            StepEntity definition,
            StepRepresentation usedRepresentation,
            StepEntity identifiedItem,
            StepCadBuilder builder
    ) {
        return validateSummaryEntity(definition, builder)
                + validateSummaryEntity(usedRepresentation, builder)
                + validateSummaryEntity(identifiedItem, builder);
    }

    private static int validateChainBasedRepresentationUsage(
            StepEntity definition,
            List<StepRepresentation> nodes,
            List<StepRepresentationRelationship> undirectedLinks,
            StepEntity identifiedItem,
            StepCadBuilder builder
    ) {
        int count = validateSummaryEntity(definition, builder)
                + validateSummaryEntity(identifiedItem, builder);
        for (StepRepresentation node : nodes) {
            count += validateSummaryEntity(node, builder);
        }
        for (StepRepresentationRelationship link : undirectedLinks) {
            count += validateSummaryEntity(link, builder);
        }
        return count;
    }

    private static boolean isGenericDumpUnsupported(StepEntity entity, String reason) {
        return reason.equals(stepEntityTypeName(entity) + " dump validation is unsupported");
    }

    /**
     * Returns true if the entity is a 2D/pcurve/semantic support type that should
     * not count toward unsupportedFaces when validation fails. These include 2D
     * curves in surface parameter space, pcurves, and non-geometry support
     * entities (directions, vectors, representation contexts).
     */
    private static boolean is2DPcurveEntity(StepEntity entity) {
        return entity instanceof StepPcurve
                || entity instanceof StepDegeneratePcurve
                || entity instanceof StepOffsetCurve2D
                || entity instanceof StepAxis2Placement2D
                || entity instanceof com.minicad.step.model.StepCurve2D
                || entity instanceof com.minicad.step.model.StepCircle2D
                || entity instanceof com.minicad.step.model.StepEllipse2D
                || entity instanceof com.minicad.step.model.StepHyperbola2D
                || entity instanceof com.minicad.step.model.StepParabola2D
                || entity instanceof com.minicad.step.model.StepTrimmedCurve2D
                || entity instanceof StepTrimmedCurve
                || entity instanceof StepRepresentation
                || entity instanceof StepDirection
                || entity instanceof StepVector
                || entity instanceof StepRepresentationContext;
    }

    private static int validateSummaryEntity(StepEntity entity, StepCadBuilder builder) {
        Integer count = validateGeometricPrimitiveEntity(entity, builder);
        if (count != null) {
            return count;
        }
        count = validateSurfaceEntity(entity, builder);
        if (count != null) {
            return count;
        }
        count = validateTopologyEntity(entity, builder);
        if (count != null) {
            return count;
        }
        count = validateAnnotationEntity(entity, builder);
        if (count != null) {
            return count;
        }
        count = validatePresentationStyleEntity(entity, builder);
        if (count != null) {
            return count;
        }
        count = validateContextUnitEntity(entity, builder);
        if (count != null) {
            return count;
        }
        count = validateAssignmentEntity(entity, builder);
        if (count != null) {
            return count;
        }
        count = validateProductStructureEntity(entity, builder);
        if (count != null) {
            return count;
        }
        count = validateRepresentationUsageEntity(entity, builder);
        if (count != null) {
            return count;
        }
        count = validateManagementAssignmentEntity(entity, builder);
        if (count != null) {
            return count;
        }
        throw new UnsupportedGeometryException(stepEntityTypeName(entity) + " dump validation is unsupported");
    }

    // validateGeometricPrimitiveEntity dispatch table (first-match-return,
    // mirrors the original sequential ifs).
    private record GeometricPrimitiveRule(
            Class<? extends StepEntity> type, GeometricPrimitiveHandler handler) {}

    private interface GeometricPrimitiveHandler {
        Integer validate(StepEntity entity, StepCadBuilder builder);
    }

    private static GeometricPrimitiveRule geometricPrimitiveRule(
            Class<? extends StepEntity> type, GeometricPrimitiveHandler handler) {
        return new GeometricPrimitiveRule(type, handler);
    }

    private static final List<GeometricPrimitiveRule> GEOMETRIC_PRIMITIVE_RULES = List.of(
        geometricPrimitiveRule(StepCartesianPoint.class, (entity, builder) -> {
            StepCartesianPoint point = (StepCartesianPoint) entity;
            validatePoint(point, builder);
            return 1;
        }),
        geometricPrimitiveRule(StepDirection.class, (entity, builder) -> {
            StepDirection direction = (StepDirection) entity;
            validateDirection(direction, builder);
            return 1;
        }),
        geometricPrimitiveRule(StepVector.class, (entity, builder) -> {
            StepVector vector = (StepVector) entity;
            builder.buildVector(vector.id());
            return 1;
        }),
        geometricPrimitiveRule(StepVertexPoint.class, (entity, builder) -> {
            StepVertexPoint vertexPoint = (StepVertexPoint) entity;
            builder.buildVertex(vertexPoint.id());
            return 1;
        }),
        geometricPrimitiveRule(StepConicCurve.class, (entity, builder) -> {
            StepConicCurve conicCurve = (StepConicCurve) entity;
            return validateSummaryEntity(conicCurve.position(), builder);
        }),
        geometricPrimitiveRule(StepLine.class, (entity, builder) -> {
            StepLine line = (StepLine) entity;
            validateLine(line, builder);
            return 1;
        }),
        geometricPrimitiveRule(StepCircle.class, (entity, builder) -> {
            StepCircle circle = (StepCircle) entity;
            validateCircle(circle, builder);
            return 1;
        }),
        geometricPrimitiveRule(StepEllipse.class, (entity, builder) -> {
            StepEllipse ellipse = (StepEllipse) entity;
            validateEllipse(ellipse, builder);
            return 1;
        }),
        geometricPrimitiveRule(StepPolyline.class, (entity, builder) -> {
            StepPolyline polyline = (StepPolyline) entity;
            validatePolyline(polyline, builder);
            return 1;
        }),
        geometricPrimitiveRule(StepBSplineCurveWithKnots.class, (entity, builder) -> {
            StepBSplineCurveWithKnots splineCurve = (StepBSplineCurveWithKnots) entity;
            builder.buildBSplineCurve(splineCurve.id());
            return 1;
        }),
        geometricPrimitiveRule(StepRationalBSplineCurve.class, (entity, builder) -> {
            StepRationalBSplineCurve splineCurve = (StepRationalBSplineCurve) entity;
            builder.buildRationalBSplineCurve(splineCurve.id());
            return 1;
        }),
        geometricPrimitiveRule(StepTrimmedCurve.class, (entity, builder) -> {
            StepTrimmedCurve trimmedCurve = (StepTrimmedCurve) entity;
            builder.buildTrimmedCurve(trimmedCurve.id());
            return 1;
        }),
        geometricPrimitiveRule(StepSurfaceCurve.class, (entity, builder) -> {
            StepSurfaceCurve surfaceCurve = (StepSurfaceCurve) entity;
            builder.buildSurfaceCurve(surfaceCurve.id());
            return 1;
        }),
        geometricPrimitiveRule(StepSeamCurve.class, (entity, builder) -> {
            StepSeamCurve seamCurve = (StepSeamCurve) entity;
            builder.buildSeamCurve(seamCurve.id());
            return 1;
        }),
        geometricPrimitiveRule(StepCompositeCurve.class, (entity, builder) -> {
            StepCompositeCurve compositeCurve = (StepCompositeCurve) entity;
            builder.buildCompositeCurve(compositeCurve.id());
            return 1;
        }),
        geometricPrimitiveRule(StepCompositeCurveOnSurface.class, (entity, builder) -> {
            StepCompositeCurveOnSurface compositeCurveOnSurface = (StepCompositeCurveOnSurface) entity;
            builder.buildCompositeCurve(compositeCurveOnSurface.id());
            return 1;
        }),
        geometricPrimitiveRule(StepCompositeCurveSegment.class, (entity, builder) -> {
            StepCompositeCurveSegment segment = (StepCompositeCurveSegment) entity;
            return validateSummaryEntity(segment.parentCurve(), builder);
        }),
        geometricPrimitiveRule(StepOffsetCurve2D.class, (entity, builder) -> {
            StepOffsetCurve2D offsetCurve2D = (StepOffsetCurve2D) entity;
            builder.buildOffsetCurve2(offsetCurve2D.id());
            return 1;
        }),
        geometricPrimitiveRule(StepOffsetCurve3D.class, (entity, builder) -> {
            StepOffsetCurve3D offsetCurve3D = (StepOffsetCurve3D) entity;
            builder.buildOffsetCurve3(offsetCurve3D.id());
            return 1;
        }),
        geometricPrimitiveRule(StepOrientedCurve.class, (entity, builder) -> {
            StepOrientedCurve orientedCurve = (StepOrientedCurve) entity;
            builder.buildCurveReference3(orientedCurve.id());
            return validateSummaryEntity(orientedCurve.curveElement(), builder);
        }),
        geometricPrimitiveRule(StepPcurve.class, (entity, builder) -> {
            StepPcurve pcurve = (StepPcurve) entity;
            builder.buildPcurve2(pcurve.id());
            return 1;
        }),
        geometricPrimitiveRule(StepDegeneratePcurve.class, (entity, builder) -> {
            StepDegeneratePcurve degeneratePcurve = (StepDegeneratePcurve) entity;
            builder.buildPcurve2(degeneratePcurve.id());
            return validateSummaryEntity(degeneratePcurve.basisSurface(), builder)
                    + validateSummaryEntity(degeneratePcurve.referenceToCurve(), builder);
        }),
        geometricPrimitiveRule(StepGeometricReplica.class, (entity, builder) -> {
            StepGeometricReplica replica = (StepGeometricReplica) entity;
            if ("POINT_REPLICA".equals(replica.entityName())) {
                builder.buildPointReference(replica.id());
            }
            if ("CURVE_REPLICA".equals(replica.entityName())) {
                builder.buildCurveReference3(replica.id());
            }
            if ("SURFACE_REPLICA".equals(replica.entityName())) {
                builder.buildSurfaceReplica(replica.id());
            }
            return validateSummaryEntity(replica.parent(), builder)
                    + validateSummaryEntity(replica.transformation(), builder);
        })
    );

    private static Integer validateGeometricPrimitiveEntity(StepEntity entity, StepCadBuilder builder) {
        for (GeometricPrimitiveRule rule : GEOMETRIC_PRIMITIVE_RULES) {
            if (rule.type().isInstance(entity)) {
                return rule.handler().validate(entity, builder);
            }
        }

        return null;
    }

    private static Integer validateSurfaceEntity(StepEntity entity, StepCadBuilder builder) {
        if (entity instanceof StepPlane) {
            StepPlane plane = (StepPlane) entity;
            builder.buildPlane(plane.id());
            return 1;
        }
        if (entity instanceof StepCylindricalSurface) {
            StepCylindricalSurface cylindricalSurface = (StepCylindricalSurface) entity;
            builder.buildCylindricalSurface(cylindricalSurface.id());
            return 1;
        }
        if (entity instanceof StepConicalSurface) {
            StepConicalSurface conicalSurface = (StepConicalSurface) entity;
            builder.buildConicalSurface(conicalSurface.id());
            return 1;
        }
        if (entity instanceof StepToroidalSurface) {
            StepToroidalSurface toroidalSurface = (StepToroidalSurface) entity;
            builder.buildToroidalSurface(toroidalSurface.id());
            return 1;
        }
        if (entity instanceof StepSphericalSurface) {
            StepSphericalSurface sphericalSurface = (StepSphericalSurface) entity;
            builder.buildSphericalSurface(sphericalSurface.id());
            return 1;
        }
        if (entity instanceof StepDegenerateToroidalSurface) {
            StepDegenerateToroidalSurface degenerateToroidalSurface = (StepDegenerateToroidalSurface) entity;
            builder.buildDegenerateToroidalSurface(degenerateToroidalSurface.id());
            return 1;
        }
        if (entity instanceof StepBSplineSurfaceWithKnots) {
            StepBSplineSurfaceWithKnots splineSurface = (StepBSplineSurfaceWithKnots) entity;
            builder.buildBSplineSurface(splineSurface.id());
            return 1;
        }
        if (entity instanceof StepRationalBSplineSurface) {
            StepRationalBSplineSurface rationalSplineSurface = (StepRationalBSplineSurface) entity;
            builder.buildRationalBSplineSurface(rationalSplineSurface.id());
            return 1;
        }
        if (entity instanceof StepSurfaceOfLinearExtrusion) {
            StepSurfaceOfLinearExtrusion extrusionSurface = (StepSurfaceOfLinearExtrusion) entity;
            builder.buildSurfaceOfLinearExtrusion(extrusionSurface.id());
            return validateSummaryEntity(extrusionSurface.sweptCurve(), builder)
                    + validateSummaryEntity(extrusionSurface.extrusionAxis(), builder);
        }
        if (entity instanceof StepSurfaceOfRevolution) {
            StepSurfaceOfRevolution revolutionSurface = (StepSurfaceOfRevolution) entity;
            builder.buildSurfaceOfRevolution(revolutionSurface.id());
            return validateSummaryEntity(revolutionSurface.sweptCurve(), builder)
                    + validateSummaryEntity(revolutionSurface.axisPosition(), builder);
        }
        if (entity instanceof StepRectangularTrimmedSurface) {
            StepRectangularTrimmedSurface trimmedSurface = (StepRectangularTrimmedSurface) entity;
            builder.buildRectangularTrimmedSurface(trimmedSurface.id());
            return validateSummaryEntity(trimmedSurface.basisSurface(), builder);
        }
        if (entity instanceof StepCurveBoundedSurface) {
            StepCurveBoundedSurface boundedSurface = (StepCurveBoundedSurface) entity;
            builder.buildCurveBoundedSurface(boundedSurface.id());
            return validateSummaryEntity(boundedSurface.basisSurface(), builder)
                    + validateSummaryItems(boundedSurface.boundaries(), builder);
        }
        if (entity instanceof StepOrientedSurface) {
            StepOrientedSurface orientedSurface = (StepOrientedSurface) entity;
            builder.buildOrientedSurface(orientedSurface.id());
            return validateSummaryEntity(orientedSurface.surfaceElement(), builder);
        }
        if (entity instanceof StepOffsetSurface) {
            StepOffsetSurface offsetSurface = (StepOffsetSurface) entity;
            builder.buildOffsetSurface(offsetSurface.id());
            return validateSummaryEntity(offsetSurface.basisSurface(), builder);
        }
        if (entity instanceof StepAxis2Placement3D) {
            StepAxis2Placement3D placement3D = (StepAxis2Placement3D) entity;
            builder.buildPlacement(placement3D.id());
            return 1;
        }
        if (entity instanceof StepAxis1Placement) {
            StepAxis1Placement axis1Placement = (StepAxis1Placement) entity;
            builder.buildAxis1Placement(axis1Placement.id());
            return 1;
        }
        if (entity instanceof StepAxis2Placement2D) {
            StepAxis2Placement2D placement2D = (StepAxis2Placement2D) entity;
            validatePoint(placement2D.location(), builder);
            validateDirection(placement2D.refDirection(), builder);
            return 1;
        }
        if (entity instanceof StepCartesianTransformationOperator) {
            StepCartesianTransformationOperator transformation = (StepCartesianTransformationOperator) entity;
            if (transformation.axis1() != null) {
                validateDirection(transformation.axis1(), builder);
            }
            if (transformation.axis2() != null) {
                validateDirection(transformation.axis2(), builder);
            }
            if (transformation.axis3() != null) {
                validateDirection(transformation.axis3(), builder);
            }
            validatePoint(transformation.localOrigin(), builder);
            return 1;
        }
        if (entity instanceof StepItemDefinedTransformation) {
            StepItemDefinedTransformation transformation = (StepItemDefinedTransformation) entity;
            builder.buildPlacement(transformation.transformItem1().id());
            builder.buildPlacement(transformation.transformItem2().id());
            return 1;
        }
        return null;
    }

    private static Integer validateTopologyEntity(StepEntity entity, StepCadBuilder builder) {
        if (entity instanceof StepEdgeCurve) {
            StepEdgeCurve edgeCurve = (StepEdgeCurve) entity;
            builder.buildEdge(edgeCurve.id());
            return 1;
        }
        if (entity instanceof StepSubedge) {
            StepSubedge subedge = (StepSubedge) entity;
            builder.buildEdge(subedge.id());
            return 1;
        }
        if (entity instanceof StepOrientedEdge) {
            StepOrientedEdge orientedEdge = (StepOrientedEdge) entity;
            builder.buildOrientedEdge(orientedEdge.id());
            return 1;
        }
        if (entity instanceof StepEdgeLoop) {
            StepEdgeLoop edgeLoop = (StepEdgeLoop) entity;
            builder.buildEdgeLoop(edgeLoop.id());
            return 1;
        }
        if (entity instanceof StepVertexLoop) {
            StepVertexLoop vertexLoop = (StepVertexLoop) entity;
            builder.buildVertexLoop(vertexLoop.id());
            return 1;
        }
        if (entity instanceof StepPolyLoop) {
            StepPolyLoop polyLoop = (StepPolyLoop) entity;
            validatePolyLoop(polyLoop, builder);
            return 1;
        }
        if (entity instanceof StepPath) {
            StepPath path = (StepPath) entity;
            validatePathEdges(path.edges(), builder);
            return 1;
        }
        if (entity instanceof StepOpenPath) {
            StepOpenPath openPath = (StepOpenPath) entity;
            validatePathEdges(openPath.edges(), builder);
            return 1;
        }
        if (entity instanceof StepSubpath) {
            StepSubpath subpath = (StepSubpath) entity;
            validatePathEdges(subpath.edges(), builder);
            return 1;
        }
        if (entity instanceof StepOrientedPath) {
            StepOrientedPath orientedPath = (StepOrientedPath) entity;
            validatePathEdges(orientedPath.edges(), builder);
            return 1;
        }
        if (entity instanceof StepConnectedEdgeSet) {
            StepConnectedEdgeSet edgeSet = (StepConnectedEdgeSet) entity;
            return validateConnectedEdgeSet(edgeSet, builder);
        }
        if (entity instanceof StepWireShell) {
            StepWireShell wireShell = (StepWireShell) entity;
            return validateWireShell(wireShell, builder);
        }
        if (entity instanceof StepVertexShell) {
            StepVertexShell vertexShell = (StepVertexShell) entity;
            builder.buildVertexLoop(vertexShell.extent().id());
            return 1;
        }
        if (entity instanceof StepEdgeBasedWireframeModel) {
            StepEdgeBasedWireframeModel wireframeModel = (StepEdgeBasedWireframeModel) entity;
            int count = 0;
            for (StepConnectedEdgeSet boundary : wireframeModel.boundaries()) {
                count += validateConnectedEdgeSet(boundary, builder);
            }
            return count;
        }
        if (entity instanceof StepShellBasedWireframeModel) {
            StepShellBasedWireframeModel wireframeModel = (StepShellBasedWireframeModel) entity;
            return validateShellBasedWireframeModel(wireframeModel, builder);
        }
        if (entity instanceof StepFaceEntity) {
            StepFaceEntity face = (StepFaceEntity) entity;
            builder.buildFace(face.id());
            return 1;
        }
        if (entity instanceof StepFaceBasedSurfaceModel) {
            StepFaceBasedSurfaceModel surfaceModel = (StepFaceBasedSurfaceModel) entity;
            return validateFaceBasedSurfaceModel(surfaceModel, builder).supportedFaces();
        }
        if (entity instanceof StepShellBasedSurfaceModel) {
            StepShellBasedSurfaceModel surfaceModel = (StepShellBasedSurfaceModel) entity;
            return validateShellBasedSurfaceModel(surfaceModel, builder).supportedFaces();
        }
        if (entity instanceof StepConnectedFaceSet) {
            StepConnectedFaceSet connectedFaceSet = (StepConnectedFaceSet) entity;
            return summarizeShell(connectedFaceSet.faces(), builder).supportedFaces();
        }
        if (entity instanceof StepConnectedFaceSubSet) {
            StepConnectedFaceSubSet connectedFaceSubSet = (StepConnectedFaceSubSet) entity;
            return summarizeShell(connectedFaceSubSet.faces(), builder).supportedFaces();
        }
        if (entity instanceof StepOpenShell
                || entity instanceof StepSurfacedOpenShell
                || entity instanceof StepOrientedOpenShell
                || entity instanceof StepClosedShell
                || entity instanceof StepOrientedClosedShell) {
            return builder.buildShell(entity.id()).faces().size();
        }
        if (entity instanceof StepManifoldSolidBrep
                || entity instanceof StepBrepWithVoids
                || entity instanceof StepSweptAreaSolid
                || entity instanceof StepExtrudedFaceSolid
                || entity instanceof StepRevolvedFaceSolid
                || entity instanceof StepSolidReplica
                || entity instanceof StepCsgSolid
                || entity instanceof StepCsgPrimitive
                || entity instanceof StepBooleanResult
                || entity instanceof StepBooleanClippingResult) {
            return builder.buildSolid(entity.id()).outerShell().faces().size();
        }
        if (entity instanceof StepPointSet) {
            StepPointSet pointSet = (StepPointSet) entity;
            return validatePointSet(pointSet, builder);
        }
        if (entity instanceof StepGeometricCurveSet) {
            StepGeometricCurveSet curveSet = (StepGeometricCurveSet) entity;
            return validateGeometricCurveSet(curveSet, builder);
        }
        if (entity instanceof StepGeometricSet) {
            StepGeometricSet geometricSet = (StepGeometricSet) entity;
            return validateGeometricSet(geometricSet, builder);
        }
        if (entity instanceof StepBoxDomain) {
            StepBoxDomain boxDomain = (StepBoxDomain) entity;
            return validateSummaryEntity(boxDomain.corner(), builder);
        }
        if (entity instanceof StepHalfSpaceSolid) {
            StepHalfSpaceSolid halfSpaceSolid = (StepHalfSpaceSolid) entity;
            int count = validateSummaryEntity(halfSpaceSolid.baseSurface(), builder);
            if (halfSpaceSolid.enclosure() != null) {
                count += validateSummaryEntity(halfSpaceSolid.enclosure(), builder);
            }
            return count;
        }
        if (entity instanceof StepProfileDef) {
            StepProfileDef profileDef = (StepProfileDef) entity;
            int count = 0;
            if (profileDef.position() != null) {
                count += validateSummaryEntity(profileDef.position(), builder);
            }
            count += validateSummaryItems(profileDef.curves(), builder);
            return Math.max(1, count);
        }
        if (entity instanceof StepRepresentation) {
            StepRepresentation representation = (StepRepresentation) entity;
            return validateRepresentation(representation, builder);
        }
        if (entity instanceof StepRepresentationMap) {
            StepRepresentationMap representationMap = (StepRepresentationMap) entity;
            return validateRepresentationMap(representationMap, builder);
        }
        if (entity instanceof StepMappedItem) {
            StepMappedItem mappedItem = (StepMappedItem) entity;
            return validateMappedItem(mappedItem, builder);
        }
        if (entity instanceof StepStyledItem) {
            StepStyledItem styledItem = (StepStyledItem) entity;
            return validateStyledItem(styledItem, builder);
        }
        if (entity instanceof StepOverRidingStyledItem) {
            StepOverRidingStyledItem styledItem = (StepOverRidingStyledItem) entity;
            return validateOverridingStyledItem(styledItem, builder);
        }
        if (entity instanceof StepRepresentationRelationship) {
            StepRepresentationRelationship relationship = (StepRepresentationRelationship) entity;
            return validateRepresentationRelationship(relationship, builder);
        }
        if (entity instanceof StepRepresentationRelationshipWithTransformation) {
            StepRepresentationRelationshipWithTransformation relationship = (StepRepresentationRelationshipWithTransformation) entity;
            return validateRepresentationRelationshipWithTransformation(relationship, builder);
        }
        if (entity instanceof StepShapeRepresentationRelationship) {
            StepShapeRepresentationRelationship relationship = (StepShapeRepresentationRelationship) entity;
            return validateShapeRepresentationRelationship(relationship, builder);
        }
        return null;
    }

    private static Integer validateAnnotationEntity(StepEntity entity, StepCadBuilder builder) {
        if (entity instanceof StepAnnotationCurveOccurrence) {
            StepAnnotationCurveOccurrence annotationCurveOccurrence = (StepAnnotationCurveOccurrence) entity;
            return validateAnnotationCurveOccurrence(annotationCurveOccurrence.item(), builder);
        }
        if (entity instanceof StepDraughtingAnnotationOccurrence) {
            StepDraughtingAnnotationOccurrence annotationOccurrence = (StepDraughtingAnnotationOccurrence) entity;
            return validateSummaryEntity(annotationOccurrence.item(), builder);
        }
        if (entity instanceof StepLeaderCurve) {
            StepLeaderCurve leaderCurve = (StepLeaderCurve) entity;
            return validateAnnotationCurveOccurrence(leaderCurve.item(), builder);
        }
        if (entity instanceof StepDimensionCurve) {
            StepDimensionCurve dimensionCurve = (StepDimensionCurve) entity;
            return validateAnnotationCurveOccurrence(dimensionCurve.item(), builder);
        }
        if (entity instanceof StepProjectionCurve) {
            StepProjectionCurve projectionCurve = (StepProjectionCurve) entity;
            return validateAnnotationCurveOccurrence(projectionCurve.item(), builder);
        }
        if (entity instanceof StepAnnotationFillArea) {
            StepAnnotationFillArea fillArea = (StepAnnotationFillArea) entity;
            return validateAnnotationFillArea(fillArea, builder);
        }
        if (entity instanceof StepAnnotationFillAreaOccurrence) {
            StepAnnotationFillAreaOccurrence fillAreaOccurrence = (StepAnnotationFillAreaOccurrence) entity;
            return validateAnnotationFillArea(fillAreaOccurrence.item(), builder)
                    + validateSummaryEntity(fillAreaOccurrence.fillStyleTarget(), builder);
        }
        if (entity instanceof StepAnnotationPlaceholderOccurrence) {
            StepAnnotationPlaceholderOccurrence placeholderOccurrence = (StepAnnotationPlaceholderOccurrence) entity;
            return validateSummaryEntity(placeholderOccurrence.item(), builder);
        }
        if (entity instanceof StepAnnotationPointOccurrence) {
            StepAnnotationPointOccurrence pointOccurrence = (StepAnnotationPointOccurrence) entity;
            return validateSummaryEntity(pointOccurrence.item(), builder);
        }
        if (entity instanceof StepAnnotationTextOccurrence) {
            StepAnnotationTextOccurrence textOccurrence = (StepAnnotationTextOccurrence) entity;
            validateSummaryEntity(textOccurrence.position(), builder);
            return 1;
        }
        if (entity instanceof StepAnnotationSymbolOccurrence) {
            StepAnnotationSymbolOccurrence symbolOccurrence = (StepAnnotationSymbolOccurrence) entity;
            return validateSummaryEntity(symbolOccurrence.item(), builder);
        }
        if (entity instanceof StepAnnotationSubfigureOccurrence) {
            StepAnnotationSubfigureOccurrence subfigureOccurrence = (StepAnnotationSubfigureOccurrence) entity;
            return validateSummaryEntity(subfigureOccurrence.item(), builder);
        }
        if (entity instanceof StepTerminatorSymbol) {
            StepTerminatorSymbol terminatorSymbol = (StepTerminatorSymbol) entity;
            return validateSummaryEntity(terminatorSymbol.item(), builder)
                    + validateSummaryEntity(terminatorSymbol.annotatedCurve(), builder);
        }
        if (entity instanceof StepAnnotationPlane) {
            StepAnnotationPlane annotationPlane = (StepAnnotationPlane) entity;
            return validateAnnotationPlane(annotationPlane, builder);
        }
        if (entity instanceof StepDraughtingCallout) {
            StepDraughtingCallout callout = (StepDraughtingCallout) entity;
            return validateDraughtingCallout(callout, builder);
        }
        if (entity instanceof StepDraughtingCalloutRelationship) {
            StepDraughtingCalloutRelationship relationship = (StepDraughtingCalloutRelationship) entity;
            return validateDraughtingCallout(relationship.relatingCallout(), builder)
                    + validateDraughtingCallout(relationship.relatedCallout(), builder);
        }
        if (entity instanceof StepAnnotationOccurrenceRelationship) {
            StepAnnotationOccurrenceRelationship relationship = (StepAnnotationOccurrenceRelationship) entity;
            return validateSummaryEntity(relationship.relatingAnnotationOccurrence(), builder)
                    + validateSummaryEntity(relationship.relatedAnnotationOccurrence(), builder);
        }
        if (entity instanceof StepSymbolRepresentationMap) {
            StepSymbolRepresentationMap representationMap = (StepSymbolRepresentationMap) entity;
            validateSummaryEntity(representationMap.mappedOrigin(), builder);
            return validateRepresentation(representationMap.mappedRepresentation(), builder);
        }
        if (entity instanceof StepAnnotationSymbol) {
            StepAnnotationSymbol annotationSymbol = (StepAnnotationSymbol) entity;
            int count = validateSummaryEntity(annotationSymbol.mappingSource(), builder);
            return count + validateSummaryEntity(annotationSymbol.mappingTarget(), builder);
        }
        if (entity instanceof StepAnnotationText) {
            StepAnnotationText annotationText = (StepAnnotationText) entity;
            int count = validateSummaryEntity(annotationText.mappingSource(), builder);
            return count + validateSummaryEntity(annotationText.mappingTarget(), builder);
        }
        if (entity instanceof StepAnnotationTextCharacter) {
            StepAnnotationTextCharacter annotationTextCharacter = (StepAnnotationTextCharacter) entity;
            int count = validateSummaryEntity(annotationTextCharacter.mappingSource(), builder);
            return count + validateSummaryEntity(annotationTextCharacter.mappingTarget(), builder);
        }
        return null;
    }

    private static Integer validatePresentationStyleEntity(StepEntity entity, StepCadBuilder builder) {
        if (entity instanceof StepPresentationLayerAssignment) {
            StepPresentationLayerAssignment layerAssignment = (StepPresentationLayerAssignment) entity;
            return validateSummaryItems(layerAssignment.assignedItems(), builder);
        }
        if (entity instanceof StepPresentationStyleAssignment) {
            StepPresentationStyleAssignment assignment = (StepPresentationStyleAssignment) entity;
            return validatePresentationStyleAssignment(assignment, builder);
        }
        if (entity instanceof StepCurveStyle) {
            StepCurveStyle curveStyle = (StepCurveStyle) entity;
            return validateCurveStyle(curveStyle, builder);
        }
        if (entity instanceof StepPointStyle) {
            StepPointStyle pointStyle = (StepPointStyle) entity;
            return validateSummaryEntity(pointStyle.marker(), builder)
                    + validateSummaryEntity(pointStyle.colour(), builder);
        }
        if (entity instanceof StepSymbolStyle) {
            StepSymbolStyle symbolStyle = (StepSymbolStyle) entity;
            return validateSummaryEntity(symbolStyle.styleOfSymbol(), builder);
        }
        if (entity instanceof StepFillAreaStyleColour) {
            StepFillAreaStyleColour fillAreaStyleColour = (StepFillAreaStyleColour) entity;
            return validateSummaryEntity(fillAreaStyleColour.colour(), builder);
        }
        if (entity instanceof StepFillAreaStyle) {
            StepFillAreaStyle fillAreaStyle = (StepFillAreaStyle) entity;
            return validateFillAreaStyle(fillAreaStyle, builder);
        }
        if (entity instanceof StepSurfaceStyleFillArea) {
            StepSurfaceStyleFillArea surfaceStyleFillArea = (StepSurfaceStyleFillArea) entity;
            return validateSummaryEntity(surfaceStyleFillArea.fillStyle(), builder);
        }
        if (entity instanceof StepSurfaceStyleBoundary) {
            StepSurfaceStyleBoundary surfaceStyleBoundary = (StepSurfaceStyleBoundary) entity;
            return validateCurveStyle(surfaceStyleBoundary.style(), builder);
        }
        if (entity instanceof StepSurfaceStyleParameterLine) {
            StepSurfaceStyleParameterLine surfaceStyleParameterLine = (StepSurfaceStyleParameterLine) entity;
            return validateCurveStyle(surfaceStyleParameterLine.style(), builder);
        }
        if (entity instanceof StepSurfaceStyleControlGrid) {
            StepSurfaceStyleControlGrid surfaceStyleControlGrid = (StepSurfaceStyleControlGrid) entity;
            return validateCurveStyle(surfaceStyleControlGrid.style(), builder);
        }
        if (entity instanceof StepSurfaceStyleSegmentationCurve) {
            StepSurfaceStyleSegmentationCurve surfaceStyleSegmentationCurve = (StepSurfaceStyleSegmentationCurve) entity;
            return validateCurveStyle(surfaceStyleSegmentationCurve.style(), builder);
        }
        if (entity instanceof StepSurfaceStyleSilhouette) {
            StepSurfaceStyleSilhouette surfaceStyleSilhouette = (StepSurfaceStyleSilhouette) entity;
            return validateCurveStyle(surfaceStyleSilhouette.style(), builder);
        }
        if (entity instanceof StepSurfaceStyleTransparent
                || entity instanceof StepSurfaceStyleReflectanceAmbient
                || entity instanceof StepSurfaceStyleReflectanceAmbientDiffuse) {
            return 1;
        }
        if (entity instanceof StepSurfaceStyleReflectanceAmbientDiffuseSpecular) {
            StepSurfaceStyleReflectanceAmbientDiffuseSpecular specular = (StepSurfaceStyleReflectanceAmbientDiffuseSpecular) entity;
            return 1 + validateSummaryEntity(specular.specularColour(), builder);
        }
        if (entity instanceof StepSurfaceSideStyle) {
            StepSurfaceSideStyle surfaceSideStyle = (StepSurfaceSideStyle) entity;
            return validateSurfaceSideStyle(surfaceSideStyle, builder);
        }
        if (entity instanceof StepSurfaceStyleUsage) {
            StepSurfaceStyleUsage surfaceStyleUsage = (StepSurfaceStyleUsage) entity;
            return validateSurfaceSideStyle(surfaceStyleUsage.style(), builder);
        }
        if (entity instanceof StepTextStyleForDefinedFont) {
            StepTextStyleForDefinedFont textStyleForDefinedFont = (StepTextStyleForDefinedFont) entity;
            return validateSummaryEntity(textStyleForDefinedFont.textColour(), builder);
        }
        if (entity instanceof StepTextStyle) {
            StepTextStyle textStyle = (StepTextStyle) entity;
            return validateSummaryEntity(textStyle.characterAppearance(), builder);
        }
        if (entity instanceof StepTextStyleWithSpacing) {
            StepTextStyleWithSpacing textStyleWithSpacing = (StepTextStyleWithSpacing) entity;
            return validateSummaryEntity(textStyleWithSpacing.characterAppearance(), builder);
        }
        if (entity instanceof StepTextStyleWithJustification) {
            StepTextStyleWithJustification textStyleWithJustification = (StepTextStyleWithJustification) entity;
            return validateSummaryEntity(textStyleWithJustification.characterAppearance(), builder);
        }
        if (entity instanceof StepTextStyleWithMirror) {
            StepTextStyleWithMirror textStyleWithMirror = (StepTextStyleWithMirror) entity;
            return validateSummaryEntity(textStyleWithMirror.characterAppearance(), builder)
                    + validateSummaryEntity(textStyleWithMirror.mirrorPlacement(), builder);
        }
        if (entity instanceof StepTextStyleWithBoxCharacteristics) {
            StepTextStyleWithBoxCharacteristics textStyleWithBoxCharacteristics = (StepTextStyleWithBoxCharacteristics) entity;
            return validateSummaryEntity(textStyleWithBoxCharacteristics.characterAppearance(), builder);
        }
        if (entity instanceof StepSymbolColour) {
            StepSymbolColour symbolColour = (StepSymbolColour) entity;
            return validateSummaryEntity(symbolColour.colour(), builder);
        }
        if (entity instanceof StepCharacterGlyphStyleStroke) {
            StepCharacterGlyphStyleStroke glyphStyleStroke = (StepCharacterGlyphStyleStroke) entity;
            return validateCurveStyle(glyphStyleStroke.strokeStyle(), builder);
        }
        if (entity instanceof StepCharacterGlyphStyleOutline) {
            StepCharacterGlyphStyleOutline glyphStyleOutline = (StepCharacterGlyphStyleOutline) entity;
            return validateCurveStyle(glyphStyleOutline.outlineStyle(), builder);
        }
        if (entity instanceof StepCharacterGlyphStyleOutlineWithCharacteristics) {
            StepCharacterGlyphStyleOutlineWithCharacteristics glyphStyleOutline = (StepCharacterGlyphStyleOutlineWithCharacteristics) entity;
            return validateCurveStyle(glyphStyleOutline.outlineStyle(), builder)
                    + validateFillAreaStyle(glyphStyleOutline.characteristics(), builder);
        }
        if (entity instanceof StepUserDefinedCurveFont) {
            StepUserDefinedCurveFont userDefinedCurveFont = (StepUserDefinedCurveFont) entity;
            return validateRepresentationMap(userDefinedCurveFont.mappingSource(), builder)
                    + validateSummaryEntity(userDefinedCurveFont.mappingTarget(), builder);
        }
        if (entity instanceof StepUserDefinedMarker) {
            StepUserDefinedMarker userDefinedMarker = (StepUserDefinedMarker) entity;
            return validateRepresentationMap(userDefinedMarker.mappingSource(), builder)
                    + validateSummaryEntity(userDefinedMarker.mappingTarget(), builder);
        }
        if (entity instanceof StepUserDefinedTerminatorSymbol) {
            StepUserDefinedTerminatorSymbol userDefinedTerminatorSymbol = (StepUserDefinedTerminatorSymbol) entity;
            return validateRepresentationMap(userDefinedTerminatorSymbol.mappingSource(), builder)
                    + validateSummaryEntity(userDefinedTerminatorSymbol.mappingTarget(), builder);
        }
        return null;
    }

    private static Integer validateContextUnitEntity(StepEntity entity, StepCadBuilder builder) {
        if (entity instanceof StepGeometricRepresentationContext) {
            StepGeometricRepresentationContext geometricRepresentationContext = (StepGeometricRepresentationContext) entity;
            int count = 1;
            if (geometricRepresentationContext.globalUnitAssignedContext() != null) {
                count += validateSummaryEntity(geometricRepresentationContext.globalUnitAssignedContext(), builder);
            }
            if (geometricRepresentationContext.globalUncertaintyAssignedContext() != null) {
                count += validateSummaryEntity(geometricRepresentationContext.globalUncertaintyAssignedContext(), builder);
            }
            return count;
        }
        if (entity instanceof StepGlobalUnitAssignedContext) {
            StepGlobalUnitAssignedContext globalUnitAssignedContext = (StepGlobalUnitAssignedContext) entity;
            return validateSummaryItems(globalUnitAssignedContext.units(), builder);
        }
        if (entity instanceof StepGlobalUncertaintyAssignedContext) {
            StepGlobalUncertaintyAssignedContext globalUncertaintyAssignedContext = (StepGlobalUncertaintyAssignedContext) entity;
            int count = 0;
            for (StepUncertaintyMeasureWithUnit uncertainty : globalUncertaintyAssignedContext.uncertainties()) {
                count += validateSummaryEntity(uncertainty, builder);
            }
            return count;
        }
        if (entity instanceof StepMeasureWithUnit) {
            StepMeasureWithUnit measureWithUnit = (StepMeasureWithUnit) entity;
            return validateSummaryEntity(measureWithUnit.unitComponent(), builder);
        }
        if (entity instanceof StepTypedMeasureWithUnit) {
            StepTypedMeasureWithUnit typedMeasureWithUnit = (StepTypedMeasureWithUnit) entity;
            return validateSummaryEntity(typedMeasureWithUnit.unitComponent(), builder);
        }
        if (entity instanceof StepUncertaintyMeasureWithUnit) {
            StepUncertaintyMeasureWithUnit uncertaintyMeasureWithUnit = (StepUncertaintyMeasureWithUnit) entity;
            return validateSummaryEntity(uncertaintyMeasureWithUnit.unitComponent(), builder);
        }
        if (entity instanceof StepConversionBasedUnit) {
            StepConversionBasedUnit conversionBasedUnit = (StepConversionBasedUnit) entity;
            return validateSummaryEntity(conversionBasedUnit.conversionFactor(), builder);
        }
        if (entity instanceof StepConversionBasedUnitWithOffset) {
            StepConversionBasedUnitWithOffset conversionBasedUnitWithOffset = (StepConversionBasedUnitWithOffset) entity;
            return validateSummaryEntity(conversionBasedUnitWithOffset.conversionFactor(), builder);
        }
        if (entity instanceof StepDerivedUnit) {
            StepDerivedUnit derivedUnit = (StepDerivedUnit) entity;
            int count = 0;
            for (StepDerivedUnitElement element : derivedUnit.elements()) {
                count += validateSummaryEntity(element, builder);
            }
            return count;
        }
        if (entity instanceof StepDerivedUnitElement) {
            StepDerivedUnitElement derivedUnitElement = (StepDerivedUnitElement) entity;
            return validateSummaryEntity(derivedUnitElement.unit(), builder);
        }
        if (entity instanceof StepPreDefinedColour
                || entity instanceof StepColourSpecification
                || entity instanceof StepDraughtingPreDefinedColour
                || entity instanceof StepColour
                || entity instanceof StepColourRgb
                || entity instanceof StepPreDefinedCurveFont
                || entity instanceof StepDraughtingPreDefinedCurveFont
                || entity instanceof StepPreDefinedMarker
                || entity instanceof StepPreDefinedTextFont
                || entity instanceof StepPreDefinedItem
                || entity instanceof StepPreDefinedSymbol
                || entity instanceof StepPreDefinedPointMarkerSymbol
                || entity instanceof StepPreDefinedDimensionSymbol
                || entity instanceof StepPreDefinedGeometricalToleranceSymbol
                || entity instanceof StepPreDefinedTerminatorSymbol
                || entity instanceof StepPreDefinedSurfaceSideStyle
                || entity instanceof StepDraughtingPreDefinedTextFont
                || entity instanceof StepExternalSource
                || entity instanceof StepExternallyDefinedItem
                || entity instanceof StepAddress
                || entity instanceof StepGeneralProperty
                || entity instanceof StepCharacterizedObject
                || entity instanceof StepProductCategory
                || entity instanceof StepProductRelatedProductCategory
                || entity instanceof StepEffectivity
                || entity instanceof StepLanguage
                || entity instanceof StepIdentificationRole
                || entity instanceof StepDescriptionAttribute
                || entity instanceof StepNameAttribute
                || entity instanceof StepIdAttribute
                || entity instanceof StepDescriptiveRepresentationItem
                || entity instanceof StepValueRepresentationItem
                || entity instanceof StepMeasureRepresentationItem
                || entity instanceof StepRepresentationItem
                || entity instanceof StepGeometricRepresentationItem
                || entity instanceof StepTopologicalRepresentationItem
                || entity instanceof StepPoint
                || entity instanceof StepCurve
                || entity instanceof StepSurface
                || entity instanceof StepSurfaceModel
                || entity instanceof StepSolidModel
                || entity instanceof StepBoundedCurve
                || entity instanceof StepBSplineCurve
                || entity instanceof StepPiecewiseBezierCurve
                || entity instanceof StepBezierCurve
                || entity instanceof StepUniformCurve
                || entity instanceof StepQuasiUniformCurve
                || entity instanceof StepBoundedSurface
                || entity instanceof StepBSplineSurface
                || entity instanceof StepPiecewiseBezierSurface
                || entity instanceof StepBezierSurface
                || entity instanceof StepUniformSurface
                || entity instanceof StepQuasiUniformSurface
                || entity instanceof StepVertex
                || entity instanceof StepEdge
                || entity instanceof StepFace
                || entity instanceof StepDocumentType
                || entity instanceof StepRepresentationContext
                || entity instanceof StepNamedUnit
                || entity instanceof StepSiUnit
                || entity instanceof StepContextDependentUnit
                || entity instanceof StepDimensionalExponents
                || entity instanceof StepGroup
                || entity instanceof StepClassificationRole
                || entity instanceof StepOrganization
                || entity instanceof StepOrganizationRole
                || entity instanceof StepNameAssignment
                || entity instanceof StepApprovalStatus
                || entity instanceof StepApprovalRole
                || entity instanceof StepContractType
                || entity instanceof StepCertificationType
                || entity instanceof StepSecurityClassificationLevel
                || entity instanceof StepPerson
                || entity instanceof StepPersonAndOrganizationRole
                || entity instanceof StepCalendarDate
                || entity instanceof StepCoordinatedUniversalTimeOffset
                || entity instanceof StepDateRole
                || entity instanceof StepDateTimeRole) {
            return 1;
        }
        return null;
    }

    // validateAssignmentEntity dispatch table (first-match-return,
    // mirrors the original sequential ifs).
    private record AssignmentRule(
            Class<? extends StepEntity> type, AssignmentHandler handler) {}

    private interface AssignmentHandler {
        Integer validate(StepEntity entity, StepCadBuilder builder);
    }

    private static AssignmentRule assignmentRule(
            Class<? extends StepEntity> type, AssignmentHandler handler) {
        return new AssignmentRule(type, handler);
    }

    private static final List<AssignmentRule> ASSIGNMENT_RULES = List.of(
        assignmentRule(StepIdentificationAssignment.class, (entity, builder) -> {
            StepIdentificationAssignment identificationAssignment = (StepIdentificationAssignment) entity;
            return validateSummaryEntity(identificationAssignment.role(), builder);
        }),
        assignmentRule(StepAppliedIdentificationAssignment.class, (entity, builder) -> {
            StepAppliedIdentificationAssignment appliedIdentificationAssignment = (StepAppliedIdentificationAssignment) entity;
            return validateSummaryEntity(appliedIdentificationAssignment.role(), builder)
                    + validateSummaryItems(appliedIdentificationAssignment.items(), builder);
        }),
        assignmentRule(StepPersonAndOrganization.class, (entity, builder) -> {
            StepPersonAndOrganization personAndOrganization = (StepPersonAndOrganization) entity;
            return validateSummaryEntity(personAndOrganization.person(), builder)
                    + validateSummaryEntity(personAndOrganization.organization(), builder);
        }),
        assignmentRule(StepPersonAndOrganizationAssignment.class, (entity, builder) -> {
            StepPersonAndOrganizationAssignment personAndOrganizationAssignment = (StepPersonAndOrganizationAssignment) entity;
            return validateSummaryEntity(personAndOrganizationAssignment.assignedPersonAndOrganization(), builder)
                    + validateSummaryEntity(personAndOrganizationAssignment.role(), builder);
        }),
        assignmentRule(StepAppliedPersonAndOrganizationAssignment.class, (entity, builder) -> {
            StepAppliedPersonAndOrganizationAssignment appliedPersonAndOrganizationAssignment = (StepAppliedPersonAndOrganizationAssignment) entity;
            return validateSummaryEntity(appliedPersonAndOrganizationAssignment.assignedPersonAndOrganization(), builder)
                    + validateSummaryEntity(appliedPersonAndOrganizationAssignment.role(), builder)
                    + validateSummaryItems(appliedPersonAndOrganizationAssignment.items(), builder);
        }),
        assignmentRule(StepLocalTime.class, (entity, builder) -> {
            StepLocalTime localTime = (StepLocalTime) entity;
            return 1 + validateSummaryEntity(localTime.zone(), builder);
        }),
        assignmentRule(StepDateAndTime.class, (entity, builder) -> {
            StepDateAndTime dateAndTime = (StepDateAndTime) entity;
            return validateSummaryEntity(dateAndTime.dateComponent(), builder)
                    + validateSummaryEntity(dateAndTime.timeComponent(), builder);
        }),
        assignmentRule(StepDateAssignment.class, (entity, builder) -> {
            StepDateAssignment dateAssignment = (StepDateAssignment) entity;
            return validateSummaryEntity(dateAssignment.assignedDate(), builder)
                    + validateSummaryEntity(dateAssignment.role(), builder);
        }),
        assignmentRule(StepAppliedDateAssignment.class, (entity, builder) -> {
            StepAppliedDateAssignment appliedDateAssignment = (StepAppliedDateAssignment) entity;
            return validateSummaryEntity(appliedDateAssignment.assignedDate(), builder)
                    + validateSummaryEntity(appliedDateAssignment.role(), builder)
                    + validateSummaryItems(appliedDateAssignment.items(), builder);
        }),
        assignmentRule(StepDateTimeAssignment.class, (entity, builder) -> {
            StepDateTimeAssignment dateTimeAssignment = (StepDateTimeAssignment) entity;
            return validateSummaryEntity(dateTimeAssignment.assignedDateAndTime(), builder)
                    + validateSummaryEntity(dateTimeAssignment.role(), builder);
        }),
        assignmentRule(StepAppliedDateTimeAssignment.class, (entity, builder) -> {
            StepAppliedDateTimeAssignment appliedDateTimeAssignment = (StepAppliedDateTimeAssignment) entity;
            return validateSummaryEntity(appliedDateTimeAssignment.assignedDateAndTime(), builder)
                    + validateSummaryEntity(appliedDateTimeAssignment.role(), builder)
                    + validateSummaryItems(appliedDateTimeAssignment.items(), builder);
        }),
        assignmentRule(StepDocumentReference.class, (entity, builder) -> {
            StepDocumentReference documentReference = (StepDocumentReference) entity;
            return validateSummaryEntity(documentReference.assignedDocument(), builder);
        }),
        assignmentRule(StepAppliedDocumentReference.class, (entity, builder) -> {
            StepAppliedDocumentReference appliedDocumentReference = (StepAppliedDocumentReference) entity;
            return validateSummaryEntity(appliedDocumentReference.assignedDocument(), builder)
                    + validateSummaryItems(appliedDocumentReference.items(), builder);
        }),
        assignmentRule(StepDocumentRelationship.class, (entity, builder) -> {
            StepDocumentRelationship documentRelationship = (StepDocumentRelationship) entity;
            return validateSummaryEntity(documentRelationship.relatingDocument(), builder)
                    + validateSummaryEntity(documentRelationship.relatedDocument(), builder);
        }),
        assignmentRule(StepPropertyDefinitionRelationship.class, (entity, builder) -> {
            StepPropertyDefinitionRelationship propertyDefinitionRelationship = (StepPropertyDefinitionRelationship) entity;
            return 2;
        }),
        assignmentRule(StepAbstractVariable.class, (entity, builder) -> {
            StepAbstractVariable abstractVariable = (StepAbstractVariable) entity;
            return validateSummaryEntity(abstractVariable.definition(), builder)
                    + validateSummaryEntity(abstractVariable.usedRepresentation(), builder);
        }),
        assignmentRule(StepRowVariable.class, (entity, builder) -> {
            StepRowVariable rowVariable = (StepRowVariable) entity;
            return validateSummaryEntity(rowVariable.definition(), builder)
                    + validateSummaryEntity(rowVariable.usedRepresentation(), builder);
        }),
        assignmentRule(StepScalarVariable.class, (entity, builder) -> {
            StepScalarVariable scalarVariable = (StepScalarVariable) entity;
            return validateSummaryEntity(scalarVariable.definition(), builder)
                    + validateSummaryEntity(scalarVariable.usedRepresentation(), builder);
        }),
        assignmentRule(StepForwardChainingRulePremise.class, (entity, builder) -> {
            StepForwardChainingRulePremise rulePremise = (StepForwardChainingRulePremise) entity;
            return validateSummaryEntity(rulePremise.definition(), builder)
                    + validateSummaryEntity(rulePremise.usedRepresentation(), builder);
        }),
        assignmentRule(StepBackChainingRuleBody.class, (entity, builder) -> {
            StepBackChainingRuleBody ruleBody = (StepBackChainingRuleBody) entity;
            return validateSummaryEntity(ruleBody.definition(), builder)
                    + validateSummaryEntity(ruleBody.usedRepresentation(), builder);
        }),
        assignmentRule(StepAttributeAssertion.class, (entity, builder) -> {
            StepAttributeAssertion attributeAssertion = (StepAttributeAssertion) entity;
            return validateSummaryEntity(attributeAssertion.usedRepresentation(), builder);
        }),
        assignmentRule(StepApprovalPersonOrganization.class, (entity, builder) -> {
            StepApprovalPersonOrganization approvalPersonOrganization = (StepApprovalPersonOrganization) entity;
            return validateSummaryEntity(approvalPersonOrganization.personOrganization(), builder)
                    + validateSummaryEntity(approvalPersonOrganization.authorizedApproval(), builder)
                    + validateSummaryEntity(approvalPersonOrganization.role(), builder);
        }),
        assignmentRule(StepApprovalDateTime.class, (entity, builder) -> {
            StepApprovalDateTime approvalDateTime = (StepApprovalDateTime) entity;
            return validateSummaryEntity(approvalDateTime.dateTime(), builder)
                    + validateSummaryEntity(approvalDateTime.datedApproval(), builder);
        }),
        assignmentRule(StepGroupRelationship.class, (entity, builder) -> {
            StepGroupRelationship groupRelationship = (StepGroupRelationship) entity;
            return validateSummaryEntity(groupRelationship.relatingGroup(), builder)
                    + validateSummaryEntity(groupRelationship.relatedGroup(), builder);
        }),
        assignmentRule(StepOrganizationRelationship.class, (entity, builder) -> {
            StepOrganizationRelationship organizationRelationship = (StepOrganizationRelationship) entity;
            return validateSummaryEntity(organizationRelationship.relatingOrganization(), builder)
                    + validateSummaryEntity(organizationRelationship.relatedOrganization(), builder);
        })
    );

    private static Integer validateAssignmentEntity(StepEntity entity, StepCadBuilder builder) {
        for (AssignmentRule rule : ASSIGNMENT_RULES) {
            if (rule.type().isInstance(entity)) {
                return rule.handler().validate(entity, builder);
            }
        }

        return null;
    }

    // validateProductStructureEntity dispatch table (first-match-return,
    // mirrors the original sequential ifs).
    private record ProductStructureRule(
            Class<? extends StepEntity> type, ProductStructureHandler handler) {}

    private interface ProductStructureHandler {
        Integer validate(StepEntity entity, StepCadBuilder builder);
    }

    private static ProductStructureRule productStructureRule(
            Class<? extends StepEntity> type, ProductStructureHandler handler) {
        return new ProductStructureRule(type, handler);
    }

    private static final List<ProductStructureRule> PRODUCT_STRUCTURE_RULES = List.of(
        productStructureRule(StepApplicationContext.class, (entity, builder) -> {
            return 1;
        }),
        productStructureRule(StepApplicationProtocolDefinition.class, (entity, builder) -> {
            StepApplicationProtocolDefinition applicationProtocolDefinition = (StepApplicationProtocolDefinition) entity;
            return validateSummaryEntity(applicationProtocolDefinition.application(), builder);
        }),
        productStructureRule(StepProduct.class, (entity, builder) -> {
            StepProduct product = (StepProduct) entity;
            return 1;
        }),
        productStructureRule(StepProductContext.class, (entity, builder) -> {
            StepProductContext productContext = (StepProductContext) entity;
            return validateSummaryEntity(productContext.frameOfReference(), builder);
        }),
        productStructureRule(StepProductDefinitionContext.class, (entity, builder) -> {
            StepProductDefinitionContext productDefinitionContext = (StepProductDefinitionContext) entity;
            return validateSummaryEntity(productDefinitionContext.frameOfReference(), builder);
        }),
        productStructureRule(StepProductDefinitionFormation.class, (entity, builder) -> {
            StepProductDefinitionFormation formation = (StepProductDefinitionFormation) entity;
            return validateSummaryEntity(formation.ofProduct(), builder);
        }),
        productStructureRule(StepProductDefinition.class, (entity, builder) -> {
            StepProductDefinition definition = (StepProductDefinition) entity;
            return validateSummaryEntity(definition.formation(), builder)
                    + validateSummaryEntity(definition.frameOfReference(), builder);
        }),
        productStructureRule(StepProductDefinitionShape.class, (entity, builder) -> {
            StepProductDefinitionShape productDefinitionShape = (StepProductDefinitionShape) entity;
            return validateSummaryEntity(productDefinitionShape.definition(), builder);
        }),
        productStructureRule(StepProductDefinitionEffectivity.class, (entity, builder) -> {
            StepProductDefinitionEffectivity productDefinitionEffectivity = (StepProductDefinitionEffectivity) entity;
            return validateSummaryEntity(productDefinitionEffectivity.productDefinition(), builder);
        }),
        productStructureRule(StepProductRelationship.class, (entity, builder) -> {
            StepProductRelationship productRelationship = (StepProductRelationship) entity;
            return validateSummaryEntity(productRelationship.relatingProduct(), builder)
                    + validateSummaryEntity(productRelationship.relatedProduct(), builder);
        }),
        productStructureRule(StepProductDefinitionRelationship.class, (entity, builder) -> {
            StepProductDefinitionRelationship productDefinitionRelationship = (StepProductDefinitionRelationship) entity;
            return validateSummaryEntity(productDefinitionRelationship.relatingProductDefinition(), builder)
                    + validateSummaryEntity(productDefinitionRelationship.relatedProductDefinition(), builder);
        }),
        productStructureRule(StepProductDefinitionFormationRelationship.class, (entity, builder) -> {
            StepProductDefinitionFormationRelationship productDefinitionFormationRelationship = (StepProductDefinitionFormationRelationship) entity;
            return validateSummaryEntity(productDefinitionFormationRelationship.relatingFormation(), builder)
                    + validateSummaryEntity(productDefinitionFormationRelationship.relatedFormation(), builder);
        }),
        productStructureRule(StepProductDefinitionRelationshipRelationship.class, (entity, builder) -> {
            StepProductDefinitionRelationshipRelationship relationshipRelationship = (StepProductDefinitionRelationshipRelationship) entity;
            return validateSummaryEntity(relationshipRelationship.relating(), builder)
                    + validateSummaryEntity(relationshipRelationship.related(), builder);
        }),
        productStructureRule(StepPropertyDefinition.class, (entity, builder) -> {
            StepPropertyDefinition propertyDefinition = (StepPropertyDefinition) entity;
            return validateSummaryEntity(propertyDefinition.definition(), builder);
        }),
        productStructureRule(StepPropertyDefinitionRepresentation.class, (entity, builder) -> {
            StepPropertyDefinitionRepresentation propertyDefinitionRepresentation = (StepPropertyDefinitionRepresentation) entity;
            return validateSummaryEntity(propertyDefinitionRepresentation.definition(), builder)
                    + validateSummaryEntity(propertyDefinitionRepresentation.usedRepresentation(), builder);
        }),
        productStructureRule(StepActionPropertyRepresentation.class, (entity, builder) -> {
            StepActionPropertyRepresentation actionPropertyRepresentation = (StepActionPropertyRepresentation) entity;
            return validateSummaryEntity(actionPropertyRepresentation.definition(), builder)
                    + validateSummaryEntity(actionPropertyRepresentation.usedRepresentation(), builder);
        }),
        productStructureRule(StepContactRatioRepresentation.class, (entity, builder) -> {
            StepContactRatioRepresentation contactRatioRepresentation = (StepContactRatioRepresentation) entity;
            return validateSummaryEntity(contactRatioRepresentation.definition(), builder)
                    + validateSummaryEntity(contactRatioRepresentation.usedRepresentation(), builder);
        }),
        productStructureRule(StepKinematicPropertyDefinitionRepresentation.class, (entity, builder) -> {
            StepKinematicPropertyDefinitionRepresentation kinematicPropertyDefinitionRepresentation = (StepKinematicPropertyDefinitionRepresentation) entity;
            return validateSummaryEntity(kinematicPropertyDefinitionRepresentation.definition(), builder)
                    + validateSummaryEntity(kinematicPropertyDefinitionRepresentation.usedRepresentation(), builder);
        }),
        productStructureRule(StepKinematicPropertyMechanismRepresentation.class, (entity, builder) -> {
            StepKinematicPropertyMechanismRepresentation kinematicPropertyMechanismRepresentation = (StepKinematicPropertyMechanismRepresentation) entity;
            return validateSummaryEntity(kinematicPropertyMechanismRepresentation.definition(), builder)
                    + validateSummaryEntity(kinematicPropertyMechanismRepresentation.usedRepresentation(), builder);
        }),
        productStructureRule(StepKinematicPropertyRepresentationRelation.class, (entity, builder) -> {
            StepKinematicPropertyRepresentationRelation kinematicPropertyRepresentationRelation = (StepKinematicPropertyRepresentationRelation) entity;
            return validateSummaryEntity(kinematicPropertyRepresentationRelation.definition(), builder)
                    + validateSummaryEntity(kinematicPropertyRepresentationRelation.usedRepresentation(), builder);
        }),
        productStructureRule(StepKinematicPropertyTopologyRepresentation.class, (entity, builder) -> {
            StepKinematicPropertyTopologyRepresentation kinematicPropertyTopologyRepresentation = (StepKinematicPropertyTopologyRepresentation) entity;
            return validateSummaryEntity(kinematicPropertyTopologyRepresentation.definition(), builder)
                    + validateSummaryEntity(kinematicPropertyTopologyRepresentation.usedRepresentation(), builder);
        }),
        productStructureRule(StepResourcePropertyRepresentation.class, (entity, builder) -> {
            StepResourcePropertyRepresentation resourcePropertyRepresentation = (StepResourcePropertyRepresentation) entity;
            return validateSummaryEntity(resourcePropertyRepresentation.definition(), builder)
                    + validateSummaryEntity(resourcePropertyRepresentation.usedRepresentation(), builder);
        }),
        productStructureRule(StepShapeDefinitionRepresentation.class, (entity, builder) -> {
            StepShapeDefinitionRepresentation shapeDefinitionRepresentation = (StepShapeDefinitionRepresentation) entity;
            return validateSummaryEntity(shapeDefinitionRepresentation.definition(), builder)
                    + validateSummaryEntity(shapeDefinitionRepresentation.usedRepresentation(), builder);
        }),
        productStructureRule(StepContextDependentShapeRepresentation.class, (entity, builder) -> {
            StepContextDependentShapeRepresentation contextDependentShapeRepresentation = (StepContextDependentShapeRepresentation) entity;
            return validateSummaryEntity(contextDependentShapeRepresentation.representationRelationship(), builder)
                    + validateSummaryEntity(contextDependentShapeRepresentation.representedProductRelation(), builder);
        }),
        productStructureRule(StepNextAssemblyUsageOccurrence.class, (entity, builder) -> {
            StepNextAssemblyUsageOccurrence nextAssemblyUsageOccurrence = (StepNextAssemblyUsageOccurrence) entity;
            return validateSummaryEntity(nextAssemblyUsageOccurrence.relatingProductDefinition(), builder)
                    + validateSummaryEntity(nextAssemblyUsageOccurrence.relatedProductDefinition(), builder);
        }),
        productStructureRule(StepPlacedDatumTargetFeature.class, (entity, builder) -> {
            StepPlacedDatumTargetFeature placedDatumTargetFeature = (StepPlacedDatumTargetFeature) entity;
            return validateSummaryEntity(placedDatumTargetFeature.usedRepresentation(), builder);
        }),
        productStructureRule(StepShapeAspect.class, (entity, builder) -> {
            StepShapeAspect shapeAspect = (StepShapeAspect) entity;
            return validateSummaryEntity(shapeAspect.ofShape(), builder);
        }),
        productStructureRule(StepShapeAspectOccurrence.class, (entity, builder) -> {
            StepShapeAspectOccurrence shapeAspectOccurrence = (StepShapeAspectOccurrence) entity;
            return validateSummaryEntity(shapeAspectOccurrence.ofShape(), builder)
                    + validateSummaryEntity(shapeAspectOccurrence.definition(), builder);
        }),
        productStructureRule(StepShapeAspectRelationship.class, (entity, builder) -> {
            StepShapeAspectRelationship shapeAspectRelationship = (StepShapeAspectRelationship) entity;
            return validateSummaryEntity(shapeAspectRelationship.relatingShapeAspect(), builder)
                    + validateSummaryEntity(shapeAspectRelationship.relatedShapeAspect(), builder);
        })
    );

    private static Integer validateProductStructureEntity(StepEntity entity, StepCadBuilder builder) {
        for (ProductStructureRule rule : PRODUCT_STRUCTURE_RULES) {
            if (rule.type().isInstance(entity)) {
                return rule.handler().validate(entity, builder);
            }
        }

        return null;
    }

    private static Integer validateRepresentationUsageEntity(StepEntity entity, StepCadBuilder builder) {
        if (entity instanceof StepItemIdentifiedRepresentationUsage) {
            StepItemIdentifiedRepresentationUsage itemIdentifiedRepresentationUsage = (StepItemIdentifiedRepresentationUsage) entity;
            return validateRepresentationUsage(itemIdentifiedRepresentationUsage.definition(),
                    itemIdentifiedRepresentationUsage.usedRepresentation(),
                    itemIdentifiedRepresentationUsage.identifiedItem(),
                    builder);
        }
        if (entity instanceof StepChainBasedItemIdentifiedRepresentationUsage) {
            StepChainBasedItemIdentifiedRepresentationUsage chainBasedItemIdentifiedRepresentationUsage = (StepChainBasedItemIdentifiedRepresentationUsage) entity;
            return validateChainBasedRepresentationUsage(chainBasedItemIdentifiedRepresentationUsage.definition(),
                    chainBasedItemIdentifiedRepresentationUsage.nodes(),
                    chainBasedItemIdentifiedRepresentationUsage.undirectedLinks(),
                    chainBasedItemIdentifiedRepresentationUsage.identifiedItem(),
                    builder);
        }
        if (entity instanceof StepPlacedTarget) {
            StepPlacedTarget placedTarget = (StepPlacedTarget) entity;
            return validateRepresentationUsage(placedTarget.definition(),
                    placedTarget.usedRepresentation(),
                    placedTarget.identifiedItem(),
                    builder);
        }
        if (entity instanceof StepDraughtingModelItemAssociation) {
            StepDraughtingModelItemAssociation draughtingModelItemAssociation = (StepDraughtingModelItemAssociation) entity;
            return validateRepresentationUsage(draughtingModelItemAssociation.definition(),
                    draughtingModelItemAssociation.usedRepresentation(),
                    draughtingModelItemAssociation.identifiedItem(),
                    builder);
        }
        if (entity instanceof StepDraughtingModelItemAssociationWithPlaceholder) {
            StepDraughtingModelItemAssociationWithPlaceholder associationWithPlaceholder = (StepDraughtingModelItemAssociationWithPlaceholder) entity;
            return validateRepresentationUsage(associationWithPlaceholder.definition(),
                    associationWithPlaceholder.usedRepresentation(),
                    associationWithPlaceholder.identifiedItem(),
                    builder) + validateSummaryEntity(associationWithPlaceholder.annotationPlaceholder(), builder);
        }
        if (entity instanceof StepPmiRequirementItemAssociation) {
            StepPmiRequirementItemAssociation pmiRequirementItemAssociation = (StepPmiRequirementItemAssociation) entity;
            return validateRepresentationUsage(pmiRequirementItemAssociation.definition(),
                    pmiRequirementItemAssociation.usedRepresentation(),
                    pmiRequirementItemAssociation.identifiedItem(),
                    builder) + validateSummaryEntity(pmiRequirementItemAssociation.requirement(), builder);
        }
        if (entity instanceof StepMechanicalDesignRequirementItemAssociation) {
            StepMechanicalDesignRequirementItemAssociation requirementItemAssociation = (StepMechanicalDesignRequirementItemAssociation) entity;
            return validateRepresentationUsage(requirementItemAssociation.definition(),
                    requirementItemAssociation.usedRepresentation(),
                    requirementItemAssociation.identifiedItem(),
                    builder) + validateSummaryEntity(requirementItemAssociation.requirement(), builder);
        }
        if (entity instanceof StepGeometricItemSpecificUsage) {
            StepGeometricItemSpecificUsage geometricItemSpecificUsage = (StepGeometricItemSpecificUsage) entity;
            return validateSummaryEntity(geometricItemSpecificUsage.usage(), builder)
                    + validateSummaryEntity(geometricItemSpecificUsage.identifiedItem(), builder);
        }
        if (entity instanceof StepChainBasedGeometricItemSpecificUsage) {
            StepChainBasedGeometricItemSpecificUsage chainBasedGeometricItemSpecificUsage = (StepChainBasedGeometricItemSpecificUsage) entity;
            int count = validateSummaryEntity(chainBasedGeometricItemSpecificUsage.usage(), builder)
                    + validateSummaryEntity(chainBasedGeometricItemSpecificUsage.identifiedItem(), builder);
            for (StepRepresentation node : chainBasedGeometricItemSpecificUsage.nodes()) {
                count += validateSummaryEntity(node, builder);
            }
            for (StepRepresentationRelationship link : chainBasedGeometricItemSpecificUsage.undirectedLinks()) {
                count += validateSummaryEntity(link, builder);
            }
            return count;
        }
        return null;
    }

    // validateManagementAssignmentEntity dispatch table (first-match-return,
    // mirrors the original sequential ifs).
    private record ManagementAssignmentRule(
            Class<? extends StepEntity> type, ManagementAssignmentHandler handler) {}

    private interface ManagementAssignmentHandler {
        Integer validate(StepEntity entity, StepCadBuilder builder);
    }

    private static ManagementAssignmentRule managementAssignmentRule(
            Class<? extends StepEntity> type, ManagementAssignmentHandler handler) {
        return new ManagementAssignmentRule(type, handler);
    }

    private static final List<ManagementAssignmentRule> MANAGEMENT_ASSIGNMENT_RULES = List.of(
        managementAssignmentRule(StepGroupAssignment.class, (entity, builder) -> {
            StepGroupAssignment groupAssignment = (StepGroupAssignment) entity;
            return validateSummaryEntity(groupAssignment.assignedGroup(), builder);
        }),
        managementAssignmentRule(StepAppliedGroupAssignment.class, (entity, builder) -> {
            StepAppliedGroupAssignment appliedGroupAssignment = (StepAppliedGroupAssignment) entity;
            return validateSummaryEntity(appliedGroupAssignment.assignedGroup(), builder)
                    + validateSummaryItems(appliedGroupAssignment.items(), builder);
        }),
        managementAssignmentRule(StepClassificationAssignment.class, (entity, builder) -> {
            StepClassificationAssignment classificationAssignment = (StepClassificationAssignment) entity;
            return validateSummaryEntity(classificationAssignment.assignedClass(), builder)
                    + validateSummaryEntity(classificationAssignment.role(), builder);
        }),
        managementAssignmentRule(StepAppliedClassificationAssignment.class, (entity, builder) -> {
            StepAppliedClassificationAssignment appliedClassificationAssignment = (StepAppliedClassificationAssignment) entity;
            return validateSummaryEntity(appliedClassificationAssignment.assignedClass(), builder)
                    + validateSummaryEntity(appliedClassificationAssignment.role(), builder)
                    + validateSummaryItems(appliedClassificationAssignment.items(), builder);
        }),
        managementAssignmentRule(StepOrganizationAssignment.class, (entity, builder) -> {
            StepOrganizationAssignment organizationAssignment = (StepOrganizationAssignment) entity;
            return validateSummaryEntity(organizationAssignment.assignedOrganization(), builder)
                    + validateSummaryEntity(organizationAssignment.role(), builder);
        }),
        managementAssignmentRule(StepAppliedOrganizationAssignment.class, (entity, builder) -> {
            StepAppliedOrganizationAssignment appliedOrganizationAssignment = (StepAppliedOrganizationAssignment) entity;
            return validateSummaryEntity(appliedOrganizationAssignment.assignedOrganization(), builder)
                    + validateSummaryEntity(appliedOrganizationAssignment.role(), builder)
                    + validateSummaryItems(appliedOrganizationAssignment.items(), builder);
        }),
        managementAssignmentRule(StepAppliedNameAssignment.class, (entity, builder) -> {
            StepAppliedNameAssignment appliedNameAssignment = (StepAppliedNameAssignment) entity;
            return validateSummaryItems(appliedNameAssignment.items(), builder);
        }),
        managementAssignmentRule(StepApproval.class, (entity, builder) -> {
            StepApproval approval = (StepApproval) entity;
            return 1 + validateSummaryEntity(approval.status(), builder);
        }),
        managementAssignmentRule(StepApprovalAssignment.class, (entity, builder) -> {
            StepApprovalAssignment approvalAssignment = (StepApprovalAssignment) entity;
            return validateSummaryEntity(approvalAssignment.assignedApproval(), builder);
        }),
        managementAssignmentRule(StepAppliedApprovalAssignment.class, (entity, builder) -> {
            StepAppliedApprovalAssignment appliedApprovalAssignment = (StepAppliedApprovalAssignment) entity;
            return validateSummaryEntity(appliedApprovalAssignment.assignedApproval(), builder)
                    + validateSummaryItems(appliedApprovalAssignment.items(), builder);
        }),
        managementAssignmentRule(StepContract.class, (entity, builder) -> {
            StepContract contract = (StepContract) entity;
            return 1 + validateSummaryEntity(contract.kind(), builder);
        }),
        managementAssignmentRule(StepContractAssignment.class, (entity, builder) -> {
            StepContractAssignment contractAssignment = (StepContractAssignment) entity;
            return validateSummaryEntity(contractAssignment.assignedContract(), builder);
        }),
        managementAssignmentRule(StepAppliedContractAssignment.class, (entity, builder) -> {
            StepAppliedContractAssignment appliedContractAssignment = (StepAppliedContractAssignment) entity;
            return validateSummaryEntity(appliedContractAssignment.assignedContract(), builder)
                    + validateSummaryItems(appliedContractAssignment.items(), builder);
        }),
        managementAssignmentRule(StepCertification.class, (entity, builder) -> {
            StepCertification certification = (StepCertification) entity;
            return 1 + validateSummaryEntity(certification.kind(), builder);
        }),
        managementAssignmentRule(StepCertificationAssignment.class, (entity, builder) -> {
            StepCertificationAssignment certificationAssignment = (StepCertificationAssignment) entity;
            return validateSummaryEntity(certificationAssignment.assignedCertification(), builder);
        }),
        managementAssignmentRule(StepAppliedCertificationAssignment.class, (entity, builder) -> {
            StepAppliedCertificationAssignment appliedCertificationAssignment = (StepAppliedCertificationAssignment) entity;
            return validateSummaryEntity(appliedCertificationAssignment.assignedCertification(), builder)
                    + validateSummaryItems(appliedCertificationAssignment.items(), builder);
        }),
        managementAssignmentRule(StepSecurityClassification.class, (entity, builder) -> {
            StepSecurityClassification securityClassification = (StepSecurityClassification) entity;
            return 1 + validateSummaryEntity(securityClassification.securityLevel(), builder);
        }),
        managementAssignmentRule(StepSecurityClassificationAssignment.class, (entity, builder) -> {
            StepSecurityClassificationAssignment securityClassificationAssignment = (StepSecurityClassificationAssignment) entity;
            return validateSummaryEntity(securityClassificationAssignment.assignedSecurityClassification(), builder);
        }),
        managementAssignmentRule(StepAppliedSecurityClassificationAssignment.class, (entity, builder) -> {
            StepAppliedSecurityClassificationAssignment appliedSecurityClassificationAssignment = (StepAppliedSecurityClassificationAssignment) entity;
            return validateSummaryEntity(appliedSecurityClassificationAssignment.assignedSecurityClassification(), builder)
                    + validateSummaryItems(appliedSecurityClassificationAssignment.items(), builder);
        }),
        managementAssignmentRule(StepExternalSourceRelationship.class, (entity, builder) -> {
            StepExternalSourceRelationship externalSourceRelationship = (StepExternalSourceRelationship) entity;
            return validateSummaryEntity(externalSourceRelationship.relatingSource(), builder)
                    + validateSummaryEntity(externalSourceRelationship.relatedSource(), builder);
        }),
        managementAssignmentRule(StepGeneralPropertyRelationship.class, (entity, builder) -> {
            StepGeneralPropertyRelationship generalPropertyRelationship = (StepGeneralPropertyRelationship) entity;
            return validateSummaryEntity(generalPropertyRelationship.relatingGeneralProperty(), builder)
                    + validateSummaryEntity(generalPropertyRelationship.relatedGeneralProperty(), builder);
        }),
        managementAssignmentRule(StepProductCategoryRelationship.class, (entity, builder) -> {
            StepProductCategoryRelationship productCategoryRelationship = (StepProductCategoryRelationship) entity;
            return validateSummaryEntity(productCategoryRelationship.category(), builder)
                    + validateSummaryEntity(productCategoryRelationship.subCategory(), builder);
        }),
        managementAssignmentRule(StepProductRelatedProductCategory.class, (entity, builder) -> {
            StepProductRelatedProductCategory productRelatedCategory = (StepProductRelatedProductCategory) entity;
            return validateSummaryItems(List.copyOf(productRelatedCategory.products()), builder);
        }),
        managementAssignmentRule(StepDocument.class, (entity, builder) -> {
            StepDocument document = (StepDocument) entity;
            return 1 + validateSummaryEntity(document.kind(), builder);
        }),
        managementAssignmentRule(StepDocumentUsageConstraint.class, (entity, builder) -> {
            StepDocumentUsageConstraint documentUsageConstraint = (StepDocumentUsageConstraint) entity;
            return validateSummaryEntity(documentUsageConstraint.source(), builder);
        }),
        managementAssignmentRule(StepEffectivityRelationship.class, (entity, builder) -> {
            StepEffectivityRelationship effectivityRelationship = (StepEffectivityRelationship) entity;
            return validateSummaryEntity(effectivityRelationship.relatingEffectivity(), builder)
                    + validateSummaryEntity(effectivityRelationship.relatedEffectivity(), builder);
        }),
        managementAssignmentRule(StepLanguageAssignment.class, (entity, builder) -> {
            StepLanguageAssignment languageAssignment = (StepLanguageAssignment) entity;
            return validateSummaryEntity(languageAssignment.assignedLanguage(), builder);
        }),
        managementAssignmentRule(StepAppliedLanguageAssignment.class, (entity, builder) -> {
            StepAppliedLanguageAssignment appliedLanguageAssignment = (StepAppliedLanguageAssignment) entity;
            int count = validateSummaryEntity(appliedLanguageAssignment.assignedLanguage(), builder);
            for (StepEntity item : appliedLanguageAssignment.items()) {
                count += validateSummaryEntity(item, builder);
            }
            return count;
        }),
        managementAssignmentRule(StepExternalIdentificationAssignment.class, (entity, builder) -> {
            StepExternalIdentificationAssignment externalIdentificationAssignment = (StepExternalIdentificationAssignment) entity;
            return validateSummaryEntity(externalIdentificationAssignment.role(), builder)
                    + validateSummaryEntity(externalIdentificationAssignment.source(), builder);
        }),
        managementAssignmentRule(StepAppliedExternalIdentificationAssignment.class, (entity, builder) -> {
            StepAppliedExternalIdentificationAssignment appliedExternalIdentificationAssignment = (StepAppliedExternalIdentificationAssignment) entity;
            int count = validateSummaryEntity(appliedExternalIdentificationAssignment.role(), builder)
                    + validateSummaryEntity(appliedExternalIdentificationAssignment.source(), builder);
            for (StepEntity item : appliedExternalIdentificationAssignment.items()) {
                count += validateSummaryEntity(item, builder);
            }
            return count;
        })
    );

    private static Integer validateManagementAssignmentEntity(StepEntity entity, StepCadBuilder builder) {
        for (ManagementAssignmentRule rule : MANAGEMENT_ASSIGNMENT_RULES) {
            if (rule.type().isInstance(entity)) {
                return rule.handler().validate(entity, builder);
            }
        }

        return null;
    }

    private static void validatePoint(StepCartesianPoint point, StepCadBuilder builder) {
        if (point.coordinates().size() == 2) {
            builder.buildPoint2(point.id());
        } else {
            builder.buildPoint(point.id());
        }
    }

    private static void validateDirection(StepDirection direction, StepCadBuilder builder) {
        if (direction.directionRatios().size() == 2) {
            builder.buildDirection2(direction.id());
        } else {
            builder.buildDirection(direction.id());
        }
    }

    private static void validateLine(StepLine line, StepCadBuilder builder) {
        if (line.point().coordinates().size() == 2) {
            builder.buildLine2(line.id());
        } else {
            builder.buildLine(line.id());
        }
    }

    private static void validateCircle(StepCircle circle, StepCadBuilder builder) {
        if (is2dPlacement(circle.position())) {
            builder.buildCircle2(circle.id());
        } else {
            builder.buildCircle(circle.id());
        }
    }

    private static void validateEllipse(StepEllipse ellipse, StepCadBuilder builder) {
        if (is2dPlacement(ellipse.position())) {
            builder.buildEllipse2(ellipse.id());
        } else {
            builder.buildEllipse(ellipse.id());
        }
    }

    private static void validatePolyline(StepPolyline polyline, StepCadBuilder builder) {
        if (!polyline.points().isEmpty() && polyline.points().get(0).coordinates().size() == 2) {
            builder.buildPolyline2(polyline.id());
        } else {
            builder.buildPolyline(polyline.id());
        }
    }

    private static boolean is2dPlacement(StepEntity placement) {
        if (placement instanceof StepAxis2Placement2D) {
            return true;
        }
        if (placement instanceof StepCartesianPoint) {
            StepCartesianPoint point = (StepCartesianPoint) placement;
            return point.coordinates().size() == 2;
        }
        return false;
    }

    private static Iterable<StepFaceEntity> shellFaces(StepEntity entity) {
        if (entity instanceof StepOpenShell) {
            StepOpenShell openShell = (StepOpenShell) entity;
            return openShell.faces();
        }
        if (entity instanceof StepSurfacedOpenShell) {
            StepSurfacedOpenShell surfacedOpenShell = (StepSurfacedOpenShell) entity;
            return surfacedOpenShell.faces();
        }
        if (entity instanceof StepOrientedOpenShell) {
            StepOrientedOpenShell orientedOpenShell = (StepOrientedOpenShell) entity;
            return orientedOpenShell.faces();
        }
        if (entity instanceof StepClosedShell) {
            StepClosedShell closedShell = (StepClosedShell) entity;
            return closedShell.faces();
        }
        if (entity instanceof StepOrientedClosedShell) {
            StepOrientedClosedShell orientedClosedShell = (StepOrientedClosedShell) entity;
            return orientedClosedShell.faces();
        }
        throw new StepResolutionException("entity #" + entity.id() + " is not a supported shell");
    }

    private static FaceBuildCounts summarizeShell(Iterable<StepFaceEntity> faces, StepCadBuilder builder) {
        int supported = 0;
        int unsupported = 0;
        Map<String, Integer> unsupportedReasons = new LinkedHashMap<>();
        Map<String, Integer> unsupportedReasonCodes = new LinkedHashMap<>();
        for (StepFaceEntity face : faces) {
            try {
                builder.buildFace(face.id());
                supported++;
            } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                unsupported++;
                String reason = normalizeReason(ex.getMessage());
                unsupportedReasons.merge(reason, 1, Integer::sum);
                unsupportedReasonCodes.merge(classifyReasonCode(ex, reason), 1, Integer::sum);
            }
        }
        return new FaceBuildCounts(
                supported,
                unsupported,
                Map.copyOf(unsupportedReasons),
                Map.copyOf(unsupportedReasonCodes)
        );
    }

    private static void appendUnsupportedReasons(List<String> lines, Map<String, Integer> unsupportedReasons) {
        if (unsupportedReasons.isEmpty()) {
            return;
        }
        lines.add("    unsupportedReasons: " + formatReasonCounts(unsupportedReasons));
    }

    private static void appendUnsupportedReasonCodes(List<String> lines, Map<String, Integer> unsupportedReasonCodes) {
        if (unsupportedReasonCodes.isEmpty()) {
            return;
        }
        lines.add("    unsupportedReasonCodes: " + formatReasonCounts(unsupportedReasonCodes));
    }

    private static void mergeReasonCounts(Map<String, Integer> target, Map<String, Integer> source) {
        for (Map.Entry<String, Integer> entry : source.entrySet()) {
            target.merge(entry.getKey(), entry.getValue(), Integer::sum);
        }
    }

    private static String formatReasonCounts(Map<String, Integer> reasonCounts) {
        return reasonCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
                .map(entry -> entry.getKey() + ":" + entry.getValue())
                .collect(Collectors.joining("|"));
    }

    // Delegate to StepReasonCodeClassifier - extracted utility class
    private static String normalizeReason(String message) {
        return StepReasonCodeClassifier.normalizeReason(message);
    }

    // Delegate to StepReasonCodeClassifier - extracted utility class
    private static String classifyReasonCode(Exception ex, String reason) {
        return StepReasonCodeClassifier.classifyReasonCode(ex, reason);
    }

    private static final class FaceBuildCounts {
        private final int supportedFaces;
        private final int unsupportedFaces;
        private final Map<String, Integer> unsupportedReasons;
        private final Map<String, Integer> unsupportedReasonCodes;

        FaceBuildCounts(int supportedFaces, int unsupportedFaces,
                Map<String, Integer> unsupportedReasons, Map<String, Integer> unsupportedReasonCodes) {
            this.supportedFaces = supportedFaces;
            this.unsupportedFaces = unsupportedFaces;
            this.unsupportedReasons = unsupportedReasons;
            this.unsupportedReasonCodes = unsupportedReasonCodes;
        }

        int supportedFaces() { return supportedFaces; }
        int unsupportedFaces() { return unsupportedFaces; }
        Map<String, Integer> unsupportedReasons() { return unsupportedReasons; }
        Map<String, Integer> unsupportedReasonCodes() { return unsupportedReasonCodes; }

        private FaceBuildCounts plus(FaceBuildCounts other) {
            Map<String, Integer> reasons = new LinkedHashMap<>(unsupportedReasons);
            mergeReasonCounts(reasons, other.unsupportedReasons);
            Map<String, Integer> reasonCodes = new LinkedHashMap<>(unsupportedReasonCodes);
            mergeReasonCounts(reasonCodes, other.unsupportedReasonCodes);
            return new FaceBuildCounts(
                    supportedFaces + other.supportedFaces,
                    unsupportedFaces + other.unsupportedFaces,
                    reasons,
                    reasonCodes
            );
        }
    }
}
