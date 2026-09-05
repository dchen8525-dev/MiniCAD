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
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
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
        SummaryAccumulator accumulator = new SummaryAccumulator(
                builder,
                lines,
                StepEntityIdCollector.collectShellFaceIds(resolved.values()),
                StepEntityIdCollector.collectLoopOrientedEdgeIds(resolved.values()),
                StepEntityIdCollector.collectOrientedEdgeElementIds(resolved.values()),
                StepEntityIdCollector.collectFaceBoundLoopIds(resolved.values())
        );
        for (StepEntity entity : resolved.values()) {
            accumulator.summarize(entity);
        }
        accumulator.appendTotals();
    }

    @FunctionalInterface
    private interface SummaryAction {
        String run() throws UnsupportedGeometryException, GeometryException, TopologyException, StepResolutionException;
    }

    private record SummaryRule(
            Class<?> type,
            BiPredicate<StepEntity, SummaryAccumulator> guard,
            BiConsumer<StepEntity, SummaryAccumulator> handler
    ) {
        boolean matches(StepEntity entity, SummaryAccumulator accumulator) {
            return type.isInstance(entity) && (guard == null || guard.test(entity, accumulator));
        }
    }

    private static SummaryRule summaryRule(Class<?> type, BiConsumer<StepEntity, SummaryAccumulator> handler) {
        return new SummaryRule(type, null, handler);
    }

    private static SummaryRule summaryRule(
            Class<?> type,
            BiPredicate<StepEntity, SummaryAccumulator> guard,
            BiConsumer<StepEntity, SummaryAccumulator> handler
    ) {
        return new SummaryRule(type, guard, handler);
    }

    /**
     * Build-summary rules keyed by exact concrete type, replacing the former
     * 45-branch else-if chain. The generated STEP model hierarchy is flat
     * (every class directly implements StepEntity), so rule order cannot
     * change matching; the guarded rules keep the standalone-only filters
     * that used to sit in the branch conditions.
     */
    private static final List<SummaryRule> SUMMARY_RULES = List.of(
            summaryRule(StepOpenShell.class, (entity, acc) -> {
                acc.openShells++;
                StepOpenShell openShell = (StepOpenShell) entity;
                acc.summarizeShellEntity(openShell, openShell.faces(), "faces");
            }),
            summaryRule(StepSurfacedOpenShell.class, (entity, acc) -> {
                acc.openShells++;
                StepSurfacedOpenShell openShell = (StepSurfacedOpenShell) entity;
                acc.summarizeShellEntity(openShell, openShell.faces(), "faces");
            }),
            summaryRule(StepClosedShell.class, (entity, acc) -> {
                acc.closedShells++;
                StepClosedShell closedShell = (StepClosedShell) entity;
                acc.summarizeShellEntity(closedShell, closedShell.faces(), "faces");
            }),
            summaryRule(StepOrientedOpenShell.class, (entity, acc) -> {
                acc.openShells++;
                StepOrientedOpenShell openShell = (StepOrientedOpenShell) entity;
                acc.summarizeShellEntity(openShell, openShell.faces(), "faces");
            }),
            summaryRule(StepOrientedClosedShell.class, (entity, acc) -> {
                acc.closedShells++;
                StepOrientedClosedShell closedShell = (StepOrientedClosedShell) entity;
                acc.summarizeShellEntity(closedShell, closedShell.faces(), "faces");
            }),
            summaryRule(StepManifoldSolidBrep.class, (entity, acc) -> {
                acc.solids++;
                StepManifoldSolidBrep solidBrep = (StepManifoldSolidBrep) entity;
                acc.summarizeShellEntity(solidBrep, shellFaces(solidBrep.outer()), "shellFaces");
            }),
            summaryRule(StepBrepWithVoids.class, (entity, acc) -> {
                acc.solids++;
                StepBrepWithVoids brepWithVoids = (StepBrepWithVoids) entity;
                FaceBuildCounts counts = summarizeShell(shellFaces(brepWithVoids.outer()), acc.builder);
                for (StepEntity voidShell : brepWithVoids.voids()) {
                    counts = counts.plus(summarizeShell(shellFaces(voidShell), acc.builder));
                }
                acc.reportCounts(brepWithVoids, "shellFaces", counts);
            }),
            summaryRule(StepSweptAreaSolid.class, (entity, acc) -> {
                acc.solids++;
                acc.summarizeBuiltSolid(entity, "shellFaces", "unsupported_solid.swept_area");
            }),
            summaryRule(StepExtrudedFaceSolid.class, (entity, acc) -> {
                acc.solids++;
                acc.summarizeBuiltSolid(entity, "shellFaces", "unsupported_solid.extruded_face");
            }),
            summaryRule(StepRevolvedFaceSolid.class, (entity, acc) -> {
                acc.solids++;
                acc.summarizeBuiltSolid(entity, "shellFaces", "unsupported_solid.revolved_face");
            }),
            summaryRule(StepSolidReplica.class, (entity, acc) -> {
                acc.solids++;
                acc.summarizeBuiltSolid(entity, "shellFaces", "unsupported_solid.replica");
            }),
            summaryRule(StepCsgSolid.class, (entity, acc) -> {
                acc.solids++;
                acc.summarizeBuiltSolid(entity, "shellFaces", "unsupported_solid.csg");
            }),
            summaryRule(StepCsgPrimitive.class, (entity, acc) -> {
                acc.solids++;
                acc.summarizeBuiltSolid(entity, "shellFaces", "unsupported_solid.csg_primitive");
            }),
            summaryRule(StepBooleanClippingResult.class, (entity, acc) -> {
                acc.booleanResults++;
                acc.summarizeBuiltSolid(entity, "faces", "unsupported_boolean.clipping_result");
            }),
            summaryRule(StepBooleanResult.class, (entity, acc) -> {
                acc.booleanResults++;
                acc.summarizeBuiltSolid(entity, "faces", "unsupported_boolean.result");
            }),
            summaryRule(StepFaceEntity.class,
                    (entity, acc) -> !acc.shellFaceIds.contains(entity.id()),
                    (entity, acc) -> {
                        acc.standaloneFaceEntities++;
                        acc.summarizeStandalone(entity, "built=false", () -> {
                            acc.builder.buildFace(entity.id());
                            return "built=true";
                        });
                    }),
            summaryRule(StepOrientedEdge.class,
                    (entity, acc) -> !acc.loopOrientedEdgeIds.contains(entity.id()),
                    (entity, acc) -> {
                        acc.standaloneEdgeEntities++;
                        acc.summarizeStandalone(entity, "built=false", () -> {
                            acc.builder.buildOrientedEdge(entity.id());
                            return "built=true";
                        });
                    }),
            summaryRule(StepEdgeCurve.class,
                    (entity, acc) -> !acc.orientedEdgeElementIds.contains(entity.id()),
                    (entity, acc) -> {
                        acc.standaloneEdgeEntities++;
                        acc.summarizeStandalone(entity, "built=false", () -> {
                            acc.builder.buildEdge(entity.id());
                            return "built=true";
                        });
                    }),
            summaryRule(StepSubedge.class,
                    (entity, acc) -> !acc.orientedEdgeElementIds.contains(entity.id()),
                    (entity, acc) -> {
                        acc.standaloneEdgeEntities++;
                        acc.summarizeStandalone(entity, "built=false", () -> {
                            acc.builder.buildEdge(entity.id());
                            return "built=true";
                        });
                    }),
            summaryRule(StepEdgeLoop.class,
                    (entity, acc) -> !acc.faceBoundLoopIds.contains(entity.id()),
                    (entity, acc) -> {
                        acc.standaloneLoopEntities++;
                        acc.summarizeStandalone(entity, "built=false", () -> {
                            acc.builder.buildEdgeLoop(entity.id());
                            return "built=true";
                        });
                    }),
            summaryRule(StepVertexLoop.class,
                    (entity, acc) -> !acc.faceBoundLoopIds.contains(entity.id()),
                    (entity, acc) -> {
                        acc.standaloneLoopEntities++;
                        acc.summarizeStandalone(entity, "built=false", () -> {
                            acc.builder.buildVertexLoop(entity.id());
                            return "built=true";
                        });
                    }),
            summaryRule(StepPolyLoop.class,
                    (entity, acc) -> !acc.faceBoundLoopIds.contains(entity.id()),
                    (entity, acc) -> {
                        acc.standaloneLoopEntities++;
                        acc.summarizeStandalone(entity, "built=false", () -> {
                            validatePolyLoop((StepPolyLoop) entity, acc.builder);
                            return "built=true";
                        });
                    }),
            summaryRule(StepPath.class, (entity, acc) -> {
                acc.standalonePathEntities++;
                acc.summarizeStandalone(entity, "built=false", () -> {
                    validatePathEdges(((StepPath) entity).edges(), acc.builder);
                    return "built=true";
                });
            }),
            summaryRule(StepOpenPath.class, (entity, acc) -> {
                acc.standalonePathEntities++;
                acc.summarizeStandalone(entity, "built=false", () -> {
                    validatePathEdges(((StepOpenPath) entity).edges(), acc.builder);
                    return "built=true";
                });
            }),
            summaryRule(StepSubpath.class, (entity, acc) -> {
                acc.standalonePathEntities++;
                acc.summarizeStandalone(entity, "built=false", () -> {
                    validatePathEdges(((StepSubpath) entity).edges(), acc.builder);
                    return "built=true";
                });
            }),
            summaryRule(StepOrientedPath.class, (entity, acc) -> {
                acc.standalonePathEntities++;
                acc.summarizeStandalone(entity, "built=false", () -> {
                    validatePathEdges(((StepOrientedPath) entity).edges(), acc.builder);
                    return "built=true";
                });
            }),
            summaryRule(StepConnectedEdgeSet.class, (entity, acc) -> {
                acc.standaloneContainerEntities++;
                acc.summarizeStandalone(entity, "builtEdges=0", () ->
                        "builtEdges=" + validateConnectedEdgeSet((StepConnectedEdgeSet) entity, acc.builder));
            }),
            summaryRule(StepWireShell.class, (entity, acc) -> {
                acc.standaloneContainerEntities++;
                acc.summarizeStandalone(entity, "builtLoops=0", () ->
                        "builtLoops=" + validateWireShell((StepWireShell) entity, acc.builder));
            }),
            summaryRule(StepVertexShell.class, (entity, acc) -> {
                acc.standaloneContainerEntities++;
                acc.summarizeStandalone(entity, "builtVertices=0", () -> {
                    acc.builder.buildVertexLoop(((StepVertexShell) entity).extent().id());
                    return "builtVertices=1";
                });
            }),
            summaryRule(StepEdgeBasedWireframeModel.class, (entity, acc) -> {
                acc.standaloneContainerEntities++;
                acc.summarizeStandalone(entity, "builtEdges=0", () -> {
                    int edgeCount = 0;
                    for (StepConnectedEdgeSet boundary : ((StepEdgeBasedWireframeModel) entity).boundaries()) {
                        edgeCount += validateConnectedEdgeSet(boundary, acc.builder);
                    }
                    return "builtEdges=" + edgeCount;
                });
            }),
            summaryRule(StepShellBasedWireframeModel.class, (entity, acc) -> {
                acc.standaloneContainerEntities++;
                acc.summarizeStandalone(entity, "builtBoundaries=0", () ->
                        "builtBoundaries=" + validateShellBasedWireframeModel((StepShellBasedWireframeModel) entity, acc.builder));
            }),
            summaryRule(StepFaceBasedSurfaceModel.class, (entity, acc) -> {
                acc.standaloneContainerEntities++;
                try {
                    FaceBuildCounts counts = validateFaceBasedSurfaceModel((StepFaceBasedSurfaceModel) entity, acc.builder);
                    acc.reportCounts(entity, "faces", counts);
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    acc.summarizeFailedEntity(entity, "faces=0", ex);
                }
            }),
            summaryRule(StepShellBasedSurfaceModel.class, (entity, acc) -> {
                acc.standaloneContainerEntities++;
                try {
                    FaceBuildCounts counts = validateShellBasedSurfaceModel((StepShellBasedSurfaceModel) entity, acc.builder);
                    acc.reportCounts(entity, "faces", counts);
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    acc.summarizeFailedEntity(entity, "faces=0", ex);
                }
            }),
            summaryRule(StepGeometricCurveSet.class, (entity, acc) -> {
                acc.standaloneContainerEntities++;
                acc.summarizeStandalone(entity, "builtMembers=0", () ->
                        "builtMembers=" + validateGeometricCurveSet((StepGeometricCurveSet) entity, acc.builder));
            }),
            summaryRule(StepPointSet.class, (entity, acc) -> {
                acc.standaloneContainerEntities++;
                acc.summarizeStandalone(entity, "builtMembers=0", () ->
                        "builtMembers=" + validatePointSet((StepPointSet) entity, acc.builder));
            }),
            summaryRule(StepGeometricSet.class, (entity, acc) -> {
                acc.standaloneContainerEntities++;
                acc.summarizeStandalone(entity, "builtMembers=0", () ->
                        "builtMembers=" + validateGeometricSet((StepGeometricSet) entity, acc.builder));
            }),
            summaryRule(StepRepresentation.class, (entity, acc) -> {
                acc.standaloneContainerEntities++;
                try {
                    int itemCount = validateRepresentation((StepRepresentation) entity, acc.builder);
                    acc.line(entity, "builtItems=" + itemCount + ", unsupportedFaces=0");
                } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                    if (is2DPcurveEntity(entity)) {
                        acc.standaloneContainerEntities--;
                        acc.skipped2DEntities++;
                        return;
                    }
                    acc.summarizeFailedEntity(entity, "builtItems=0", ex);
                }
            }),
            summaryRule(StepRepresentationMap.class, (entity, acc) -> {
                acc.standaloneContainerEntities++;
                acc.summarizeStandalone(entity, "builtItems=0", () ->
                        "builtItems=" + validateRepresentationMap((StepRepresentationMap) entity, acc.builder));
            }),
            summaryRule(StepMappedItem.class, (entity, acc) -> {
                acc.standaloneContainerEntities++;
                acc.summarizeStandalone(entity, "builtItems=0", () ->
                        "builtItems=" + validateMappedItem((StepMappedItem) entity, acc.builder));
            }),
            summaryRule(StepStyledItem.class, (entity, acc) -> {
                acc.standaloneContainerEntities++;
                acc.summarizeStandalone(entity, "builtItems=0", () ->
                        "builtItems=" + validateStyledItem((StepStyledItem) entity, acc.builder));
            }),
            summaryRule(StepOverRidingStyledItem.class, (entity, acc) -> {
                acc.standaloneContainerEntities++;
                acc.summarizeStandalone(entity, "builtItems=0", () ->
                        "builtItems=" + validateOverridingStyledItem((StepOverRidingStyledItem) entity, acc.builder));
            }),
            summaryRule(StepRepresentationRelationship.class, (entity, acc) -> {
                acc.standaloneContainerEntities++;
                acc.summarizeStandalone(entity, "builtItems=0", () ->
                        "builtItems=" + validateRepresentationRelationship((StepRepresentationRelationship) entity, acc.builder));
            }),
            summaryRule(StepRepresentationRelationshipWithTransformation.class, (entity, acc) -> {
                acc.standaloneContainerEntities++;
                acc.summarizeStandalone(entity, "builtItems=0", () ->
                        "builtItems=" + validateRepresentationRelationshipWithTransformation((StepRepresentationRelationshipWithTransformation) entity, acc.builder));
            }),
            summaryRule(StepShapeRepresentationRelationship.class, (entity, acc) -> {
                acc.standaloneContainerEntities++;
                acc.summarizeStandalone(entity, "builtItems=0", () ->
                        "builtItems=" + validateShapeRepresentationRelationship((StepShapeRepresentationRelationship) entity, acc.builder));
            })
    );

    /**
     * Mutable state for one appendBuildSummary pass. Owns the running totals
     * and the shared failure bookkeeping the per-type rules reuse.
     */
    private static final class SummaryAccumulator {
        private final StepCadBuilder builder;
        private final List<String> lines;
        private final Set<Integer> shellFaceIds;
        private final Set<Integer> loopOrientedEdgeIds;
        private final Set<Integer> orientedEdgeElementIds;
        private final Set<Integer> faceBoundLoopIds;
        private int openShells;
        private int closedShells;
        private int solids;
        private int booleanResults;
        private int standaloneFaceEntities;
        private int standaloneEdgeEntities;
        private int standaloneLoopEntities;
        private int standalonePathEntities;
        private int standaloneContainerEntities;
        private int skipped2DEntities;
        private int unsupportedFaces;
        private final Map<String, Integer> unsupportedReasons = new LinkedHashMap<>();
        private final Map<String, Integer> unsupportedReasonCodes = new LinkedHashMap<>();

        private SummaryAccumulator(
                StepCadBuilder builder,
                List<String> lines,
                Set<Integer> shellFaceIds,
                Set<Integer> loopOrientedEdgeIds,
                Set<Integer> orientedEdgeElementIds,
                Set<Integer> faceBoundLoopIds
        ) {
            this.builder = builder;
            this.lines = lines;
            this.shellFaceIds = shellFaceIds;
            this.loopOrientedEdgeIds = loopOrientedEdgeIds;
            this.orientedEdgeElementIds = orientedEdgeElementIds;
            this.faceBoundLoopIds = faceBoundLoopIds;
        }

        void summarize(StepEntity entity) {
            for (SummaryRule rule : SUMMARY_RULES) {
                if (rule.matches(entity, this)) {
                    rule.handler().accept(entity, this);
                    return;
                }
            }
            summarizeGenericEntity(entity);
        }

        private void summarizeGenericEntity(StepEntity entity) {
            standaloneContainerEntities++;
            try {
                int itemCount = validateSummaryEntity(entity, builder);
                line(entity, "builtItems=" + itemCount + ", unsupportedFaces=0");
            } catch (UnsupportedGeometryException ex) {
                String reason = normalizeReason(ex.getMessage());
                if (isGenericDumpUnsupported(entity, reason)) {
                    standaloneContainerEntities--;
                    return;
                }
                if (is2DPcurveEntity(entity)) {
                    standaloneContainerEntities--;
                    skipped2DEntities++;
                    return;
                }
                summarizeFailedEntity(entity, "builtItems=0", ex);
            } catch (GeometryException | TopologyException | StepResolutionException ex) {
                if (is2DPcurveEntity(entity)) {
                    standaloneContainerEntities--;
                    skipped2DEntities++;
                    return;
                }
                summarizeFailedEntity(entity, "builtItems=0", ex);
            }
        }

        private void appendTotals() {
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

        private void line(StepEntity entity, String detail) {
            lines.add("  " + stepEntityTypeName(entity) + " #" + entity.id() + ": " + detail);
        }

        /** Per-face build counts of a shell-typed entity: line plus reason maps appended and merged into totals. */
        private void summarizeShellEntity(StepEntity entity, Iterable<StepFaceEntity> faces, String countLabel) {
            reportCounts(entity, countLabel, summarizeShell(faces, builder));
        }

        private void reportCounts(StepEntity entity, String countLabel, FaceBuildCounts counts) {
            line(entity, countLabel + "=" + counts.supportedFaces() + ", unsupportedFaces=" + counts.unsupportedFaces());
            appendUnsupportedReasons(lines, counts.unsupportedReasons());
            appendUnsupportedReasonCodes(lines, counts.unsupportedReasonCodes());
            unsupportedFaces += counts.unsupportedFaces();
            mergeReasonCounts(unsupportedReasons, counts.unsupportedReasons());
            mergeReasonCounts(unsupportedReasonCodes, counts.unsupportedReasonCodes());
        }

        /** Solids and boolean results summarized through builder.buildSolid; a failed build counts as one unsupported face. */
        private void summarizeBuiltSolid(StepEntity entity, String countLabel, String reasonCode) {
            try {
                int faceCount = builder.buildSolid(entity.id()).outerShell().faces().size();
                line(entity, countLabel + "=" + faceCount + ", unsupportedFaces=0");
            } catch (UnsupportedGeometryException ex) {
                Map<String, Integer> reasonCounts = Map.of(ex.getMessage(), 1);
                Map<String, Integer> reasonCodeCounts = Map.of(reasonCode, 1);
                line(entity, countLabel + "=0, unsupportedFaces=1");
                appendUnsupportedReasons(lines, reasonCounts);
                appendUnsupportedReasonCodes(lines, reasonCodeCounts);
                unsupportedFaces++;
                mergeReasonCounts(unsupportedReasons, reasonCounts);
                mergeReasonCounts(unsupportedReasonCodes, reasonCodeCounts);
            }
        }

        private void summarizeStandalone(StepEntity entity, String failureText, SummaryAction action) {
            try {
                line(entity, action.run() + ", unsupportedFaces=0");
            } catch (UnsupportedGeometryException | GeometryException | TopologyException | StepResolutionException ex) {
                summarizeFailedEntity(entity, failureText, ex);
            }
        }

        private void summarizeFailedEntity(StepEntity entity, String failureText, Exception ex) {
            String reason = normalizeReason(ex.getMessage());
            String reasonCode = classifyReasonCode(ex, reason);
            line(entity, failureText + ", unsupportedFaces=1");
            appendUnsupportedReasons(lines, Map.of(reason, 1));
            appendUnsupportedReasonCodes(lines, Map.of(reasonCode, 1));
            unsupportedFaces++;
            unsupportedReasons.merge(reason, 1, Integer::sum);
            unsupportedReasonCodes.merge(reasonCode, 1, Integer::sum);
        }
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

    // validateSurfaceEntity dispatch table (first-match-return,
    // mirrors the original sequential ifs).
    private record SurfaceRule(
            Class<? extends StepEntity> type, SurfaceHandler handler) {}

    private interface SurfaceHandler {
        Integer validate(StepEntity entity, StepCadBuilder builder);
    }

    private static SurfaceRule surfaceRule(
            Class<? extends StepEntity> type, SurfaceHandler handler) {
        return new SurfaceRule(type, handler);
    }

    private static final List<SurfaceRule> SURFACE_RULES = List.of(
        surfaceRule(StepPlane.class, (entity, builder) -> {
            StepPlane plane = (StepPlane) entity;
            builder.buildPlane(plane.id());
            return 1;
        }),
        surfaceRule(StepCylindricalSurface.class, (entity, builder) -> {
            StepCylindricalSurface cylindricalSurface = (StepCylindricalSurface) entity;
            builder.buildCylindricalSurface(cylindricalSurface.id());
            return 1;
        }),
        surfaceRule(StepConicalSurface.class, (entity, builder) -> {
            StepConicalSurface conicalSurface = (StepConicalSurface) entity;
            builder.buildConicalSurface(conicalSurface.id());
            return 1;
        }),
        surfaceRule(StepToroidalSurface.class, (entity, builder) -> {
            StepToroidalSurface toroidalSurface = (StepToroidalSurface) entity;
            builder.buildToroidalSurface(toroidalSurface.id());
            return 1;
        }),
        surfaceRule(StepSphericalSurface.class, (entity, builder) -> {
            StepSphericalSurface sphericalSurface = (StepSphericalSurface) entity;
            builder.buildSphericalSurface(sphericalSurface.id());
            return 1;
        }),
        surfaceRule(StepDegenerateToroidalSurface.class, (entity, builder) -> {
            StepDegenerateToroidalSurface degenerateToroidalSurface = (StepDegenerateToroidalSurface) entity;
            builder.buildDegenerateToroidalSurface(degenerateToroidalSurface.id());
            return 1;
        }),
        surfaceRule(StepBSplineSurfaceWithKnots.class, (entity, builder) -> {
            StepBSplineSurfaceWithKnots splineSurface = (StepBSplineSurfaceWithKnots) entity;
            builder.buildBSplineSurface(splineSurface.id());
            return 1;
        }),
        surfaceRule(StepRationalBSplineSurface.class, (entity, builder) -> {
            StepRationalBSplineSurface rationalSplineSurface = (StepRationalBSplineSurface) entity;
            builder.buildRationalBSplineSurface(rationalSplineSurface.id());
            return 1;
        }),
        surfaceRule(StepSurfaceOfLinearExtrusion.class, (entity, builder) -> {
            StepSurfaceOfLinearExtrusion extrusionSurface = (StepSurfaceOfLinearExtrusion) entity;
            builder.buildSurfaceOfLinearExtrusion(extrusionSurface.id());
            return validateSummaryEntity(extrusionSurface.sweptCurve(), builder)
                    + validateSummaryEntity(extrusionSurface.extrusionAxis(), builder);
        }),
        surfaceRule(StepSurfaceOfRevolution.class, (entity, builder) -> {
            StepSurfaceOfRevolution revolutionSurface = (StepSurfaceOfRevolution) entity;
            builder.buildSurfaceOfRevolution(revolutionSurface.id());
            return validateSummaryEntity(revolutionSurface.sweptCurve(), builder)
                    + validateSummaryEntity(revolutionSurface.axisPosition(), builder);
        }),
        surfaceRule(StepRectangularTrimmedSurface.class, (entity, builder) -> {
            StepRectangularTrimmedSurface trimmedSurface = (StepRectangularTrimmedSurface) entity;
            builder.buildRectangularTrimmedSurface(trimmedSurface.id());
            return validateSummaryEntity(trimmedSurface.basisSurface(), builder);
        }),
        surfaceRule(StepCurveBoundedSurface.class, (entity, builder) -> {
            StepCurveBoundedSurface boundedSurface = (StepCurveBoundedSurface) entity;
            builder.buildCurveBoundedSurface(boundedSurface.id());
            return validateSummaryEntity(boundedSurface.basisSurface(), builder)
                    + validateSummaryItems(boundedSurface.boundaries(), builder);
        }),
        surfaceRule(StepOrientedSurface.class, (entity, builder) -> {
            StepOrientedSurface orientedSurface = (StepOrientedSurface) entity;
            builder.buildOrientedSurface(orientedSurface.id());
            return validateSummaryEntity(orientedSurface.surfaceElement(), builder);
        }),
        surfaceRule(StepOffsetSurface.class, (entity, builder) -> {
            StepOffsetSurface offsetSurface = (StepOffsetSurface) entity;
            builder.buildOffsetSurface(offsetSurface.id());
            return validateSummaryEntity(offsetSurface.basisSurface(), builder);
        }),
        surfaceRule(StepAxis2Placement3D.class, (entity, builder) -> {
            StepAxis2Placement3D placement3D = (StepAxis2Placement3D) entity;
            builder.buildPlacement(placement3D.id());
            return 1;
        }),
        surfaceRule(StepAxis1Placement.class, (entity, builder) -> {
            StepAxis1Placement axis1Placement = (StepAxis1Placement) entity;
            builder.buildAxis1Placement(axis1Placement.id());
            return 1;
        }),
        surfaceRule(StepAxis2Placement2D.class, (entity, builder) -> {
            StepAxis2Placement2D placement2D = (StepAxis2Placement2D) entity;
            validatePoint(placement2D.location(), builder);
            validateDirection(placement2D.refDirection(), builder);
            return 1;
        }),
        surfaceRule(StepCartesianTransformationOperator.class, (entity, builder) -> {
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
        }),
        surfaceRule(StepItemDefinedTransformation.class, (entity, builder) -> {
            StepItemDefinedTransformation transformation = (StepItemDefinedTransformation) entity;
            builder.buildPlacement(transformation.transformItem1().id());
            builder.buildPlacement(transformation.transformItem2().id());
            return 1;
        })
    );

    private static Integer validateSurfaceEntity(StepEntity entity, StepCadBuilder builder) {
        for (SurfaceRule rule : SURFACE_RULES) {
            if (rule.type().isInstance(entity)) {
                return rule.handler().validate(entity, builder);
            }
        }

        return null;
    }

    // validateTopologyEntity dispatch table (first-match-return,
    // mirrors the original sequential ifs).
    private record TopologyRule(
            Class<? extends StepEntity> type, TopologyHandler handler) {}

    private interface TopologyHandler {
        Integer validate(StepEntity entity, StepCadBuilder builder);
    }

    private static TopologyRule topologyRule(
            Class<? extends StepEntity> type, TopologyHandler handler) {
        return new TopologyRule(type, handler);
    }

    private static final List<TopologyRule> TOPOLOGY_RULES = List.of(
        topologyRule(StepEdgeCurve.class, (entity, builder) -> {
            StepEdgeCurve edgeCurve = (StepEdgeCurve) entity;
            builder.buildEdge(edgeCurve.id());
            return 1;
        }),
        topologyRule(StepSubedge.class, (entity, builder) -> {
            StepSubedge subedge = (StepSubedge) entity;
            builder.buildEdge(subedge.id());
            return 1;
        }),
        topologyRule(StepOrientedEdge.class, (entity, builder) -> {
            StepOrientedEdge orientedEdge = (StepOrientedEdge) entity;
            builder.buildOrientedEdge(orientedEdge.id());
            return 1;
        }),
        topologyRule(StepEdgeLoop.class, (entity, builder) -> {
            StepEdgeLoop edgeLoop = (StepEdgeLoop) entity;
            builder.buildEdgeLoop(edgeLoop.id());
            return 1;
        }),
        topologyRule(StepVertexLoop.class, (entity, builder) -> {
            StepVertexLoop vertexLoop = (StepVertexLoop) entity;
            builder.buildVertexLoop(vertexLoop.id());
            return 1;
        }),
        topologyRule(StepPolyLoop.class, (entity, builder) -> {
            StepPolyLoop polyLoop = (StepPolyLoop) entity;
            validatePolyLoop(polyLoop, builder);
            return 1;
        }),
        topologyRule(StepPath.class, (entity, builder) -> {
            StepPath path = (StepPath) entity;
            validatePathEdges(path.edges(), builder);
            return 1;
        }),
        topologyRule(StepOpenPath.class, (entity, builder) -> {
            StepOpenPath openPath = (StepOpenPath) entity;
            validatePathEdges(openPath.edges(), builder);
            return 1;
        }),
        topologyRule(StepSubpath.class, (entity, builder) -> {
            StepSubpath subpath = (StepSubpath) entity;
            validatePathEdges(subpath.edges(), builder);
            return 1;
        }),
        topologyRule(StepOrientedPath.class, (entity, builder) -> {
            StepOrientedPath orientedPath = (StepOrientedPath) entity;
            validatePathEdges(orientedPath.edges(), builder);
            return 1;
        }),
        topologyRule(StepConnectedEdgeSet.class, (entity, builder) -> {
            StepConnectedEdgeSet edgeSet = (StepConnectedEdgeSet) entity;
            return validateConnectedEdgeSet(edgeSet, builder);
        }),
        topologyRule(StepWireShell.class, (entity, builder) -> {
            StepWireShell wireShell = (StepWireShell) entity;
            return validateWireShell(wireShell, builder);
        }),
        topologyRule(StepVertexShell.class, (entity, builder) -> {
            StepVertexShell vertexShell = (StepVertexShell) entity;
            builder.buildVertexLoop(vertexShell.extent().id());
            return 1;
        }),
        topologyRule(StepEdgeBasedWireframeModel.class, (entity, builder) -> {
            StepEdgeBasedWireframeModel wireframeModel = (StepEdgeBasedWireframeModel) entity;
            int count = 0;
            for (StepConnectedEdgeSet boundary : wireframeModel.boundaries()) {
                count += validateConnectedEdgeSet(boundary, builder);
            }
            return count;
        }),
        topologyRule(StepShellBasedWireframeModel.class, (entity, builder) -> {
            StepShellBasedWireframeModel wireframeModel = (StepShellBasedWireframeModel) entity;
            return validateShellBasedWireframeModel(wireframeModel, builder);
        }),
        topologyRule(StepFaceEntity.class, (entity, builder) -> {
            StepFaceEntity face = (StepFaceEntity) entity;
            builder.buildFace(face.id());
            return 1;
        }),
        topologyRule(StepFaceBasedSurfaceModel.class, (entity, builder) -> {
            StepFaceBasedSurfaceModel surfaceModel = (StepFaceBasedSurfaceModel) entity;
            return validateFaceBasedSurfaceModel(surfaceModel, builder).supportedFaces();
        }),
        topologyRule(StepShellBasedSurfaceModel.class, (entity, builder) -> {
            StepShellBasedSurfaceModel surfaceModel = (StepShellBasedSurfaceModel) entity;
            return validateShellBasedSurfaceModel(surfaceModel, builder).supportedFaces();
        }),
        topologyRule(StepConnectedFaceSet.class, (entity, builder) -> {
            StepConnectedFaceSet connectedFaceSet = (StepConnectedFaceSet) entity;
            return summarizeShell(connectedFaceSet.faces(), builder).supportedFaces();
        }),
        topologyRule(StepConnectedFaceSubSet.class, (entity, builder) -> {
            StepConnectedFaceSubSet connectedFaceSubSet = (StepConnectedFaceSubSet) entity;
            return summarizeShell(connectedFaceSubSet.faces(), builder).supportedFaces();
        }),
        topologyRule(StepOpenShell.class, (entity, builder) -> {
            return builder.buildShell(entity.id()).faces().size();
        }),
        topologyRule(StepSurfacedOpenShell.class, (entity, builder) -> {
            return builder.buildShell(entity.id()).faces().size();
        }),
        topologyRule(StepOrientedOpenShell.class, (entity, builder) -> {
            return builder.buildShell(entity.id()).faces().size();
        }),
        topologyRule(StepClosedShell.class, (entity, builder) -> {
            return builder.buildShell(entity.id()).faces().size();
        }),
        topologyRule(StepOrientedClosedShell.class, (entity, builder) -> {
            return builder.buildShell(entity.id()).faces().size();
        }),
        topologyRule(StepManifoldSolidBrep.class, (entity, builder) -> {
            return builder.buildSolid(entity.id()).outerShell().faces().size();
        }),
        topologyRule(StepBrepWithVoids.class, (entity, builder) -> {
            return builder.buildSolid(entity.id()).outerShell().faces().size();
        }),
        topologyRule(StepSweptAreaSolid.class, (entity, builder) -> {
            return builder.buildSolid(entity.id()).outerShell().faces().size();
        }),
        topologyRule(StepExtrudedFaceSolid.class, (entity, builder) -> {
            return builder.buildSolid(entity.id()).outerShell().faces().size();
        }),
        topologyRule(StepRevolvedFaceSolid.class, (entity, builder) -> {
            return builder.buildSolid(entity.id()).outerShell().faces().size();
        }),
        topologyRule(StepSolidReplica.class, (entity, builder) -> {
            return builder.buildSolid(entity.id()).outerShell().faces().size();
        }),
        topologyRule(StepCsgSolid.class, (entity, builder) -> {
            return builder.buildSolid(entity.id()).outerShell().faces().size();
        }),
        topologyRule(StepCsgPrimitive.class, (entity, builder) -> {
            return builder.buildSolid(entity.id()).outerShell().faces().size();
        }),
        topologyRule(StepBooleanResult.class, (entity, builder) -> {
            return builder.buildSolid(entity.id()).outerShell().faces().size();
        }),
        topologyRule(StepBooleanClippingResult.class, (entity, builder) -> {
            return builder.buildSolid(entity.id()).outerShell().faces().size();
        }),
        topologyRule(StepPointSet.class, (entity, builder) -> {
            StepPointSet pointSet = (StepPointSet) entity;
            return validatePointSet(pointSet, builder);
        }),
        topologyRule(StepGeometricCurveSet.class, (entity, builder) -> {
            StepGeometricCurveSet curveSet = (StepGeometricCurveSet) entity;
            return validateGeometricCurveSet(curveSet, builder);
        }),
        topologyRule(StepGeometricSet.class, (entity, builder) -> {
            StepGeometricSet geometricSet = (StepGeometricSet) entity;
            return validateGeometricSet(geometricSet, builder);
        }),
        topologyRule(StepBoxDomain.class, (entity, builder) -> {
            StepBoxDomain boxDomain = (StepBoxDomain) entity;
            return validateSummaryEntity(boxDomain.corner(), builder);
        }),
        topologyRule(StepHalfSpaceSolid.class, (entity, builder) -> {
            StepHalfSpaceSolid halfSpaceSolid = (StepHalfSpaceSolid) entity;
            int count = validateSummaryEntity(halfSpaceSolid.baseSurface(), builder);
            if (halfSpaceSolid.enclosure() != null) {
                count += validateSummaryEntity(halfSpaceSolid.enclosure(), builder);
            }
            return count;
        }),
        topologyRule(StepProfileDef.class, (entity, builder) -> {
            StepProfileDef profileDef = (StepProfileDef) entity;
            int count = 0;
            if (profileDef.position() != null) {
                count += validateSummaryEntity(profileDef.position(), builder);
            }
            count += validateSummaryItems(profileDef.curves(), builder);
            return Math.max(1, count);
        }),
        topologyRule(StepRepresentation.class, (entity, builder) -> {
            StepRepresentation representation = (StepRepresentation) entity;
            return validateRepresentation(representation, builder);
        }),
        topologyRule(StepRepresentationMap.class, (entity, builder) -> {
            StepRepresentationMap representationMap = (StepRepresentationMap) entity;
            return validateRepresentationMap(representationMap, builder);
        }),
        topologyRule(StepMappedItem.class, (entity, builder) -> {
            StepMappedItem mappedItem = (StepMappedItem) entity;
            return validateMappedItem(mappedItem, builder);
        }),
        topologyRule(StepStyledItem.class, (entity, builder) -> {
            StepStyledItem styledItem = (StepStyledItem) entity;
            return validateStyledItem(styledItem, builder);
        }),
        topologyRule(StepOverRidingStyledItem.class, (entity, builder) -> {
            StepOverRidingStyledItem styledItem = (StepOverRidingStyledItem) entity;
            return validateOverridingStyledItem(styledItem, builder);
        }),
        topologyRule(StepRepresentationRelationship.class, (entity, builder) -> {
            StepRepresentationRelationship relationship = (StepRepresentationRelationship) entity;
            return validateRepresentationRelationship(relationship, builder);
        }),
        topologyRule(StepRepresentationRelationshipWithTransformation.class, (entity, builder) -> {
            StepRepresentationRelationshipWithTransformation relationship = (StepRepresentationRelationshipWithTransformation) entity;
            return validateRepresentationRelationshipWithTransformation(relationship, builder);
        }),
        topologyRule(StepShapeRepresentationRelationship.class, (entity, builder) -> {
            StepShapeRepresentationRelationship relationship = (StepShapeRepresentationRelationship) entity;
            return validateShapeRepresentationRelationship(relationship, builder);
        })
    );

    private static Integer validateTopologyEntity(StepEntity entity, StepCadBuilder builder) {
        for (TopologyRule rule : TOPOLOGY_RULES) {
            if (rule.type().isInstance(entity)) {
                return rule.handler().validate(entity, builder);
            }
        }

        return null;
    }

    // validateAnnotationEntity dispatch table (first-match-return,
    // mirrors the original sequential ifs).
    private record AnnotationRule(
            Class<? extends StepEntity> type, AnnotationHandler handler) {}

    private interface AnnotationHandler {
        Integer validate(StepEntity entity, StepCadBuilder builder);
    }

    private static AnnotationRule annotationRule(
            Class<? extends StepEntity> type, AnnotationHandler handler) {
        return new AnnotationRule(type, handler);
    }

    private static final List<AnnotationRule> ANNOTATION_RULES = List.of(
        annotationRule(StepAnnotationCurveOccurrence.class, (entity, builder) -> {
            StepAnnotationCurveOccurrence annotationCurveOccurrence = (StepAnnotationCurveOccurrence) entity;
            return validateAnnotationCurveOccurrence(annotationCurveOccurrence.item(), builder);
        }),
        annotationRule(StepDraughtingAnnotationOccurrence.class, (entity, builder) -> {
            StepDraughtingAnnotationOccurrence annotationOccurrence = (StepDraughtingAnnotationOccurrence) entity;
            return validateSummaryEntity(annotationOccurrence.item(), builder);
        }),
        annotationRule(StepLeaderCurve.class, (entity, builder) -> {
            StepLeaderCurve leaderCurve = (StepLeaderCurve) entity;
            return validateAnnotationCurveOccurrence(leaderCurve.item(), builder);
        }),
        annotationRule(StepDimensionCurve.class, (entity, builder) -> {
            StepDimensionCurve dimensionCurve = (StepDimensionCurve) entity;
            return validateAnnotationCurveOccurrence(dimensionCurve.item(), builder);
        }),
        annotationRule(StepProjectionCurve.class, (entity, builder) -> {
            StepProjectionCurve projectionCurve = (StepProjectionCurve) entity;
            return validateAnnotationCurveOccurrence(projectionCurve.item(), builder);
        }),
        annotationRule(StepAnnotationFillArea.class, (entity, builder) -> {
            StepAnnotationFillArea fillArea = (StepAnnotationFillArea) entity;
            return validateAnnotationFillArea(fillArea, builder);
        }),
        annotationRule(StepAnnotationFillAreaOccurrence.class, (entity, builder) -> {
            StepAnnotationFillAreaOccurrence fillAreaOccurrence = (StepAnnotationFillAreaOccurrence) entity;
            return validateAnnotationFillArea(fillAreaOccurrence.item(), builder)
                    + validateSummaryEntity(fillAreaOccurrence.fillStyleTarget(), builder);
        }),
        annotationRule(StepAnnotationPlaceholderOccurrence.class, (entity, builder) -> {
            StepAnnotationPlaceholderOccurrence placeholderOccurrence = (StepAnnotationPlaceholderOccurrence) entity;
            return validateSummaryEntity(placeholderOccurrence.item(), builder);
        }),
        annotationRule(StepAnnotationPointOccurrence.class, (entity, builder) -> {
            StepAnnotationPointOccurrence pointOccurrence = (StepAnnotationPointOccurrence) entity;
            return validateSummaryEntity(pointOccurrence.item(), builder);
        }),
        annotationRule(StepAnnotationTextOccurrence.class, (entity, builder) -> {
            StepAnnotationTextOccurrence textOccurrence = (StepAnnotationTextOccurrence) entity;
            validateSummaryEntity(textOccurrence.position(), builder);
            return 1;
        }),
        annotationRule(StepAnnotationSymbolOccurrence.class, (entity, builder) -> {
            StepAnnotationSymbolOccurrence symbolOccurrence = (StepAnnotationSymbolOccurrence) entity;
            return validateSummaryEntity(symbolOccurrence.item(), builder);
        }),
        annotationRule(StepAnnotationSubfigureOccurrence.class, (entity, builder) -> {
            StepAnnotationSubfigureOccurrence subfigureOccurrence = (StepAnnotationSubfigureOccurrence) entity;
            return validateSummaryEntity(subfigureOccurrence.item(), builder);
        }),
        annotationRule(StepTerminatorSymbol.class, (entity, builder) -> {
            StepTerminatorSymbol terminatorSymbol = (StepTerminatorSymbol) entity;
            return validateSummaryEntity(terminatorSymbol.item(), builder)
                    + validateSummaryEntity(terminatorSymbol.annotatedCurve(), builder);
        }),
        annotationRule(StepAnnotationPlane.class, (entity, builder) -> {
            StepAnnotationPlane annotationPlane = (StepAnnotationPlane) entity;
            return validateAnnotationPlane(annotationPlane, builder);
        }),
        annotationRule(StepDraughtingCallout.class, (entity, builder) -> {
            StepDraughtingCallout callout = (StepDraughtingCallout) entity;
            return validateDraughtingCallout(callout, builder);
        }),
        annotationRule(StepDraughtingCalloutRelationship.class, (entity, builder) -> {
            StepDraughtingCalloutRelationship relationship = (StepDraughtingCalloutRelationship) entity;
            return validateDraughtingCallout(relationship.relatingCallout(), builder)
                    + validateDraughtingCallout(relationship.relatedCallout(), builder);
        }),
        annotationRule(StepAnnotationOccurrenceRelationship.class, (entity, builder) -> {
            StepAnnotationOccurrenceRelationship relationship = (StepAnnotationOccurrenceRelationship) entity;
            return validateSummaryEntity(relationship.relatingAnnotationOccurrence(), builder)
                    + validateSummaryEntity(relationship.relatedAnnotationOccurrence(), builder);
        }),
        annotationRule(StepSymbolRepresentationMap.class, (entity, builder) -> {
            StepSymbolRepresentationMap representationMap = (StepSymbolRepresentationMap) entity;
            validateSummaryEntity(representationMap.mappedOrigin(), builder);
            return validateRepresentation(representationMap.mappedRepresentation(), builder);
        }),
        annotationRule(StepAnnotationSymbol.class, (entity, builder) -> {
            StepAnnotationSymbol annotationSymbol = (StepAnnotationSymbol) entity;
            int count = validateSummaryEntity(annotationSymbol.mappingSource(), builder);
            return count + validateSummaryEntity(annotationSymbol.mappingTarget(), builder);
        }),
        annotationRule(StepAnnotationText.class, (entity, builder) -> {
            StepAnnotationText annotationText = (StepAnnotationText) entity;
            int count = validateSummaryEntity(annotationText.mappingSource(), builder);
            return count + validateSummaryEntity(annotationText.mappingTarget(), builder);
        }),
        annotationRule(StepAnnotationTextCharacter.class, (entity, builder) -> {
            StepAnnotationTextCharacter annotationTextCharacter = (StepAnnotationTextCharacter) entity;
            int count = validateSummaryEntity(annotationTextCharacter.mappingSource(), builder);
            return count + validateSummaryEntity(annotationTextCharacter.mappingTarget(), builder);
        })
    );

    private static Integer validateAnnotationEntity(StepEntity entity, StepCadBuilder builder) {
        for (AnnotationRule rule : ANNOTATION_RULES) {
            if (rule.type().isInstance(entity)) {
                return rule.handler().validate(entity, builder);
            }
        }

        return null;
    }

    // validatePresentationStyleEntity dispatch table (first-match-return,
    // mirrors the original sequential ifs).
    private record PresentationStyleRule(
            Class<? extends StepEntity> type, PresentationStyleHandler handler) {}

    private interface PresentationStyleHandler {
        Integer validate(StepEntity entity, StepCadBuilder builder);
    }

    private static PresentationStyleRule presentationStyleRule(
            Class<? extends StepEntity> type, PresentationStyleHandler handler) {
        return new PresentationStyleRule(type, handler);
    }

    private static final List<PresentationStyleRule> PRESENTATION_STYLE_RULES = List.of(
        presentationStyleRule(StepPresentationLayerAssignment.class, (entity, builder) -> {
            StepPresentationLayerAssignment layerAssignment = (StepPresentationLayerAssignment) entity;
            return validateSummaryItems(layerAssignment.assignedItems(), builder);
        }),
        presentationStyleRule(StepPresentationStyleAssignment.class, (entity, builder) -> {
            StepPresentationStyleAssignment assignment = (StepPresentationStyleAssignment) entity;
            return validatePresentationStyleAssignment(assignment, builder);
        }),
        presentationStyleRule(StepCurveStyle.class, (entity, builder) -> {
            StepCurveStyle curveStyle = (StepCurveStyle) entity;
            return validateCurveStyle(curveStyle, builder);
        }),
        presentationStyleRule(StepPointStyle.class, (entity, builder) -> {
            StepPointStyle pointStyle = (StepPointStyle) entity;
            return validateSummaryEntity(pointStyle.marker(), builder)
                    + validateSummaryEntity(pointStyle.colour(), builder);
        }),
        presentationStyleRule(StepSymbolStyle.class, (entity, builder) -> {
            StepSymbolStyle symbolStyle = (StepSymbolStyle) entity;
            return validateSummaryEntity(symbolStyle.styleOfSymbol(), builder);
        }),
        presentationStyleRule(StepFillAreaStyleColour.class, (entity, builder) -> {
            StepFillAreaStyleColour fillAreaStyleColour = (StepFillAreaStyleColour) entity;
            return validateSummaryEntity(fillAreaStyleColour.colour(), builder);
        }),
        presentationStyleRule(StepFillAreaStyle.class, (entity, builder) -> {
            StepFillAreaStyle fillAreaStyle = (StepFillAreaStyle) entity;
            return validateFillAreaStyle(fillAreaStyle, builder);
        }),
        presentationStyleRule(StepSurfaceStyleFillArea.class, (entity, builder) -> {
            StepSurfaceStyleFillArea surfaceStyleFillArea = (StepSurfaceStyleFillArea) entity;
            return validateSummaryEntity(surfaceStyleFillArea.fillStyle(), builder);
        }),
        presentationStyleRule(StepSurfaceStyleBoundary.class, (entity, builder) -> {
            StepSurfaceStyleBoundary surfaceStyleBoundary = (StepSurfaceStyleBoundary) entity;
            return validateCurveStyle(surfaceStyleBoundary.style(), builder);
        }),
        presentationStyleRule(StepSurfaceStyleParameterLine.class, (entity, builder) -> {
            StepSurfaceStyleParameterLine surfaceStyleParameterLine = (StepSurfaceStyleParameterLine) entity;
            return validateCurveStyle(surfaceStyleParameterLine.style(), builder);
        }),
        presentationStyleRule(StepSurfaceStyleControlGrid.class, (entity, builder) -> {
            StepSurfaceStyleControlGrid surfaceStyleControlGrid = (StepSurfaceStyleControlGrid) entity;
            return validateCurveStyle(surfaceStyleControlGrid.style(), builder);
        }),
        presentationStyleRule(StepSurfaceStyleSegmentationCurve.class, (entity, builder) -> {
            StepSurfaceStyleSegmentationCurve surfaceStyleSegmentationCurve = (StepSurfaceStyleSegmentationCurve) entity;
            return validateCurveStyle(surfaceStyleSegmentationCurve.style(), builder);
        }),
        presentationStyleRule(StepSurfaceStyleSilhouette.class, (entity, builder) -> {
            StepSurfaceStyleSilhouette surfaceStyleSilhouette = (StepSurfaceStyleSilhouette) entity;
            return validateCurveStyle(surfaceStyleSilhouette.style(), builder);
        }),
        presentationStyleRule(StepSurfaceStyleTransparent.class, (entity, builder) -> {
            return 1;
        }),
        presentationStyleRule(StepSurfaceStyleReflectanceAmbient.class, (entity, builder) -> {
            return 1;
        }),
        presentationStyleRule(StepSurfaceStyleReflectanceAmbientDiffuse.class, (entity, builder) -> {
            return 1;
        }),
        presentationStyleRule(StepSurfaceStyleReflectanceAmbientDiffuseSpecular.class, (entity, builder) -> {
            StepSurfaceStyleReflectanceAmbientDiffuseSpecular specular = (StepSurfaceStyleReflectanceAmbientDiffuseSpecular) entity;
            return 1 + validateSummaryEntity(specular.specularColour(), builder);
        }),
        presentationStyleRule(StepSurfaceSideStyle.class, (entity, builder) -> {
            StepSurfaceSideStyle surfaceSideStyle = (StepSurfaceSideStyle) entity;
            return validateSurfaceSideStyle(surfaceSideStyle, builder);
        }),
        presentationStyleRule(StepSurfaceStyleUsage.class, (entity, builder) -> {
            StepSurfaceStyleUsage surfaceStyleUsage = (StepSurfaceStyleUsage) entity;
            return validateSurfaceSideStyle(surfaceStyleUsage.style(), builder);
        }),
        presentationStyleRule(StepTextStyleForDefinedFont.class, (entity, builder) -> {
            StepTextStyleForDefinedFont textStyleForDefinedFont = (StepTextStyleForDefinedFont) entity;
            return validateSummaryEntity(textStyleForDefinedFont.textColour(), builder);
        }),
        presentationStyleRule(StepTextStyle.class, (entity, builder) -> {
            StepTextStyle textStyle = (StepTextStyle) entity;
            return validateSummaryEntity(textStyle.characterAppearance(), builder);
        }),
        presentationStyleRule(StepTextStyleWithSpacing.class, (entity, builder) -> {
            StepTextStyleWithSpacing textStyleWithSpacing = (StepTextStyleWithSpacing) entity;
            return validateSummaryEntity(textStyleWithSpacing.characterAppearance(), builder);
        }),
        presentationStyleRule(StepTextStyleWithJustification.class, (entity, builder) -> {
            StepTextStyleWithJustification textStyleWithJustification = (StepTextStyleWithJustification) entity;
            return validateSummaryEntity(textStyleWithJustification.characterAppearance(), builder);
        }),
        presentationStyleRule(StepTextStyleWithMirror.class, (entity, builder) -> {
            StepTextStyleWithMirror textStyleWithMirror = (StepTextStyleWithMirror) entity;
            return validateSummaryEntity(textStyleWithMirror.characterAppearance(), builder)
                    + validateSummaryEntity(textStyleWithMirror.mirrorPlacement(), builder);
        }),
        presentationStyleRule(StepTextStyleWithBoxCharacteristics.class, (entity, builder) -> {
            StepTextStyleWithBoxCharacteristics textStyleWithBoxCharacteristics = (StepTextStyleWithBoxCharacteristics) entity;
            return validateSummaryEntity(textStyleWithBoxCharacteristics.characterAppearance(), builder);
        }),
        presentationStyleRule(StepSymbolColour.class, (entity, builder) -> {
            StepSymbolColour symbolColour = (StepSymbolColour) entity;
            return validateSummaryEntity(symbolColour.colour(), builder);
        }),
        presentationStyleRule(StepCharacterGlyphStyleStroke.class, (entity, builder) -> {
            StepCharacterGlyphStyleStroke glyphStyleStroke = (StepCharacterGlyphStyleStroke) entity;
            return validateCurveStyle(glyphStyleStroke.strokeStyle(), builder);
        }),
        presentationStyleRule(StepCharacterGlyphStyleOutline.class, (entity, builder) -> {
            StepCharacterGlyphStyleOutline glyphStyleOutline = (StepCharacterGlyphStyleOutline) entity;
            return validateCurveStyle(glyphStyleOutline.outlineStyle(), builder);
        }),
        presentationStyleRule(StepCharacterGlyphStyleOutlineWithCharacteristics.class, (entity, builder) -> {
            StepCharacterGlyphStyleOutlineWithCharacteristics glyphStyleOutline = (StepCharacterGlyphStyleOutlineWithCharacteristics) entity;
            return validateCurveStyle(glyphStyleOutline.outlineStyle(), builder)
                    + validateFillAreaStyle(glyphStyleOutline.characteristics(), builder);
        }),
        presentationStyleRule(StepUserDefinedCurveFont.class, (entity, builder) -> {
            StepUserDefinedCurveFont userDefinedCurveFont = (StepUserDefinedCurveFont) entity;
            return validateRepresentationMap(userDefinedCurveFont.mappingSource(), builder)
                    + validateSummaryEntity(userDefinedCurveFont.mappingTarget(), builder);
        }),
        presentationStyleRule(StepUserDefinedMarker.class, (entity, builder) -> {
            StepUserDefinedMarker userDefinedMarker = (StepUserDefinedMarker) entity;
            return validateRepresentationMap(userDefinedMarker.mappingSource(), builder)
                    + validateSummaryEntity(userDefinedMarker.mappingTarget(), builder);
        }),
        presentationStyleRule(StepUserDefinedTerminatorSymbol.class, (entity, builder) -> {
            StepUserDefinedTerminatorSymbol userDefinedTerminatorSymbol = (StepUserDefinedTerminatorSymbol) entity;
            return validateRepresentationMap(userDefinedTerminatorSymbol.mappingSource(), builder)
                    + validateSummaryEntity(userDefinedTerminatorSymbol.mappingTarget(), builder);
        })
    );

    private static Integer validatePresentationStyleEntity(StepEntity entity, StepCadBuilder builder) {
        for (PresentationStyleRule rule : PRESENTATION_STYLE_RULES) {
            if (rule.type().isInstance(entity)) {
                return rule.handler().validate(entity, builder);
            }
        }

        return null;
    }

    // validateContextUnitEntity dispatch table (first-match-return,
    // mirrors the original sequential ifs).
    private record ContextUnitRule(
            Class<? extends StepEntity> type, ContextUnitHandler handler) {}

    private interface ContextUnitHandler {
        Integer validate(StepEntity entity, StepCadBuilder builder);
    }

    private static ContextUnitRule contextUnitRule(
            Class<? extends StepEntity> type, ContextUnitHandler handler) {
        return new ContextUnitRule(type, handler);
    }

    private static final List<ContextUnitRule> CONTEXT_UNIT_RULES = List.of(
        contextUnitRule(StepGeometricRepresentationContext.class, (entity, builder) -> {
            StepGeometricRepresentationContext geometricRepresentationContext = (StepGeometricRepresentationContext) entity;
            int count = 1;
            if (geometricRepresentationContext.globalUnitAssignedContext() != null) {
                count += validateSummaryEntity(geometricRepresentationContext.globalUnitAssignedContext(), builder);
            }
            if (geometricRepresentationContext.globalUncertaintyAssignedContext() != null) {
                count += validateSummaryEntity(geometricRepresentationContext.globalUncertaintyAssignedContext(), builder);
            }
            return count;
        }),
        contextUnitRule(StepGlobalUnitAssignedContext.class, (entity, builder) -> {
            StepGlobalUnitAssignedContext globalUnitAssignedContext = (StepGlobalUnitAssignedContext) entity;
            return validateSummaryItems(globalUnitAssignedContext.units(), builder);
        }),
        contextUnitRule(StepGlobalUncertaintyAssignedContext.class, (entity, builder) -> {
            StepGlobalUncertaintyAssignedContext globalUncertaintyAssignedContext = (StepGlobalUncertaintyAssignedContext) entity;
            int count = 0;
            for (StepUncertaintyMeasureWithUnit uncertainty : globalUncertaintyAssignedContext.uncertainties()) {
                count += validateSummaryEntity(uncertainty, builder);
            }
            return count;
        }),
        contextUnitRule(StepMeasureWithUnit.class, (entity, builder) -> {
            StepMeasureWithUnit measureWithUnit = (StepMeasureWithUnit) entity;
            return validateSummaryEntity(measureWithUnit.unitComponent(), builder);
        }),
        contextUnitRule(StepTypedMeasureWithUnit.class, (entity, builder) -> {
            StepTypedMeasureWithUnit typedMeasureWithUnit = (StepTypedMeasureWithUnit) entity;
            return validateSummaryEntity(typedMeasureWithUnit.unitComponent(), builder);
        }),
        contextUnitRule(StepUncertaintyMeasureWithUnit.class, (entity, builder) -> {
            StepUncertaintyMeasureWithUnit uncertaintyMeasureWithUnit = (StepUncertaintyMeasureWithUnit) entity;
            return validateSummaryEntity(uncertaintyMeasureWithUnit.unitComponent(), builder);
        }),
        contextUnitRule(StepConversionBasedUnit.class, (entity, builder) -> {
            StepConversionBasedUnit conversionBasedUnit = (StepConversionBasedUnit) entity;
            return validateSummaryEntity(conversionBasedUnit.conversionFactor(), builder);
        }),
        contextUnitRule(StepConversionBasedUnitWithOffset.class, (entity, builder) -> {
            StepConversionBasedUnitWithOffset conversionBasedUnitWithOffset = (StepConversionBasedUnitWithOffset) entity;
            return validateSummaryEntity(conversionBasedUnitWithOffset.conversionFactor(), builder);
        }),
        contextUnitRule(StepDerivedUnit.class, (entity, builder) -> {
            StepDerivedUnit derivedUnit = (StepDerivedUnit) entity;
            int count = 0;
            for (StepDerivedUnitElement element : derivedUnit.elements()) {
                count += validateSummaryEntity(element, builder);
            }
            return count;
        }),
        contextUnitRule(StepDerivedUnitElement.class, (entity, builder) -> {
            StepDerivedUnitElement derivedUnitElement = (StepDerivedUnitElement) entity;
            return validateSummaryEntity(derivedUnitElement.unit(), builder);
        }),
        contextUnitRule(StepPreDefinedColour.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepColourSpecification.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepDraughtingPreDefinedColour.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepColour.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepColourRgb.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepPreDefinedCurveFont.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepDraughtingPreDefinedCurveFont.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepPreDefinedMarker.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepPreDefinedTextFont.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepPreDefinedItem.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepPreDefinedSymbol.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepPreDefinedPointMarkerSymbol.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepPreDefinedDimensionSymbol.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepPreDefinedGeometricalToleranceSymbol.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepPreDefinedTerminatorSymbol.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepPreDefinedSurfaceSideStyle.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepDraughtingPreDefinedTextFont.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepExternalSource.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepExternallyDefinedItem.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepAddress.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepGeneralProperty.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepCharacterizedObject.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepProductCategory.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepProductRelatedProductCategory.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepEffectivity.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepLanguage.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepIdentificationRole.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepDescriptionAttribute.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepNameAttribute.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepIdAttribute.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepDescriptiveRepresentationItem.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepValueRepresentationItem.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepMeasureRepresentationItem.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepRepresentationItem.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepGeometricRepresentationItem.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepTopologicalRepresentationItem.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepPoint.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepCurve.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepSurface.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepSurfaceModel.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepSolidModel.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepBoundedCurve.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepBSplineCurve.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepPiecewiseBezierCurve.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepBezierCurve.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepUniformCurve.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepQuasiUniformCurve.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepBoundedSurface.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepBSplineSurface.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepPiecewiseBezierSurface.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepBezierSurface.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepUniformSurface.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepQuasiUniformSurface.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepVertex.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepEdge.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepFace.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepDocumentType.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepRepresentationContext.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepNamedUnit.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepSiUnit.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepContextDependentUnit.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepDimensionalExponents.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepGroup.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepClassificationRole.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepOrganization.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepOrganizationRole.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepNameAssignment.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepApprovalStatus.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepApprovalRole.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepContractType.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepCertificationType.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepSecurityClassificationLevel.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepPerson.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepPersonAndOrganizationRole.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepCalendarDate.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepCoordinatedUniversalTimeOffset.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepDateRole.class, (entity, builder) -> {
            return 1;
        }),
        contextUnitRule(StepDateTimeRole.class, (entity, builder) -> {
            return 1;
        })
    );

    private static Integer validateContextUnitEntity(StepEntity entity, StepCadBuilder builder) {
        for (ContextUnitRule rule : CONTEXT_UNIT_RULES) {
            if (rule.type().isInstance(entity)) {
                return rule.handler().validate(entity, builder);
            }
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

    // validateRepresentationUsageEntity dispatch table (first-match-return,
    // mirrors the original sequential ifs).
    private record RepresentationUsageRule(
            Class<? extends StepEntity> type, RepresentationUsageHandler handler) {}

    private interface RepresentationUsageHandler {
        Integer validate(StepEntity entity, StepCadBuilder builder);
    }

    private static RepresentationUsageRule representationUsageRule(
            Class<? extends StepEntity> type, RepresentationUsageHandler handler) {
        return new RepresentationUsageRule(type, handler);
    }

    private static final List<RepresentationUsageRule> REPRESENTATION_USAGE_RULES = List.of(
        representationUsageRule(StepItemIdentifiedRepresentationUsage.class, (entity, builder) -> {
            StepItemIdentifiedRepresentationUsage itemIdentifiedRepresentationUsage = (StepItemIdentifiedRepresentationUsage) entity;
            return validateRepresentationUsage(itemIdentifiedRepresentationUsage.definition(),
                    itemIdentifiedRepresentationUsage.usedRepresentation(),
                    itemIdentifiedRepresentationUsage.identifiedItem(),
                    builder);
        }),
        representationUsageRule(StepChainBasedItemIdentifiedRepresentationUsage.class, (entity, builder) -> {
            StepChainBasedItemIdentifiedRepresentationUsage chainBasedItemIdentifiedRepresentationUsage = (StepChainBasedItemIdentifiedRepresentationUsage) entity;
            return validateChainBasedRepresentationUsage(chainBasedItemIdentifiedRepresentationUsage.definition(),
                    chainBasedItemIdentifiedRepresentationUsage.nodes(),
                    chainBasedItemIdentifiedRepresentationUsage.undirectedLinks(),
                    chainBasedItemIdentifiedRepresentationUsage.identifiedItem(),
                    builder);
        }),
        representationUsageRule(StepPlacedTarget.class, (entity, builder) -> {
            StepPlacedTarget placedTarget = (StepPlacedTarget) entity;
            return validateRepresentationUsage(placedTarget.definition(),
                    placedTarget.usedRepresentation(),
                    placedTarget.identifiedItem(),
                    builder);
        }),
        representationUsageRule(StepDraughtingModelItemAssociation.class, (entity, builder) -> {
            StepDraughtingModelItemAssociation draughtingModelItemAssociation = (StepDraughtingModelItemAssociation) entity;
            return validateRepresentationUsage(draughtingModelItemAssociation.definition(),
                    draughtingModelItemAssociation.usedRepresentation(),
                    draughtingModelItemAssociation.identifiedItem(),
                    builder);
        }),
        representationUsageRule(StepDraughtingModelItemAssociationWithPlaceholder.class, (entity, builder) -> {
            StepDraughtingModelItemAssociationWithPlaceholder associationWithPlaceholder = (StepDraughtingModelItemAssociationWithPlaceholder) entity;
            return validateRepresentationUsage(associationWithPlaceholder.definition(),
                    associationWithPlaceholder.usedRepresentation(),
                    associationWithPlaceholder.identifiedItem(),
                    builder) + validateSummaryEntity(associationWithPlaceholder.annotationPlaceholder(), builder);
        }),
        representationUsageRule(StepPmiRequirementItemAssociation.class, (entity, builder) -> {
            StepPmiRequirementItemAssociation pmiRequirementItemAssociation = (StepPmiRequirementItemAssociation) entity;
            return validateRepresentationUsage(pmiRequirementItemAssociation.definition(),
                    pmiRequirementItemAssociation.usedRepresentation(),
                    pmiRequirementItemAssociation.identifiedItem(),
                    builder) + validateSummaryEntity(pmiRequirementItemAssociation.requirement(), builder);
        }),
        representationUsageRule(StepMechanicalDesignRequirementItemAssociation.class, (entity, builder) -> {
            StepMechanicalDesignRequirementItemAssociation requirementItemAssociation = (StepMechanicalDesignRequirementItemAssociation) entity;
            return validateRepresentationUsage(requirementItemAssociation.definition(),
                    requirementItemAssociation.usedRepresentation(),
                    requirementItemAssociation.identifiedItem(),
                    builder) + validateSummaryEntity(requirementItemAssociation.requirement(), builder);
        }),
        representationUsageRule(StepGeometricItemSpecificUsage.class, (entity, builder) -> {
            StepGeometricItemSpecificUsage geometricItemSpecificUsage = (StepGeometricItemSpecificUsage) entity;
            return validateSummaryEntity(geometricItemSpecificUsage.usage(), builder)
                    + validateSummaryEntity(geometricItemSpecificUsage.identifiedItem(), builder);
        }),
        representationUsageRule(StepChainBasedGeometricItemSpecificUsage.class, (entity, builder) -> {
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
        })
    );

    private static Integer validateRepresentationUsageEntity(StepEntity entity, StepCadBuilder builder) {
        for (RepresentationUsageRule rule : REPRESENTATION_USAGE_RULES) {
            if (rule.type().isInstance(entity)) {
                return rule.handler().validate(entity, builder);
            }
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
