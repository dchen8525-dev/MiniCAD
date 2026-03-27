package com.minicad.app;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.StepParseException;
import com.minicad.common.StepResolutionException;
import com.minicad.common.TopologyException;
import com.minicad.common.UnsupportedGeometryException;
import com.minicad.app.StepAssemblyGraphBuilder.AssemblyGraph;
import com.minicad.app.StepAssemblyGraphBuilder.AssemblyNode;
import com.minicad.app.StepAssemblyGraphBuilder.AssemblyRepresentation;
import com.minicad.geometry.Axis2Placement3D;
import com.minicad.geometry.BSplineCurve3;
import com.minicad.geometry.BSplineSurface3;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Circle;
import com.minicad.geometry.ConicalSurface;
import com.minicad.geometry.CylindricalSurface;
import com.minicad.geometry.Curve3;
import com.minicad.geometry.Direction3;
import com.minicad.geometry.Ellipse3;
import com.minicad.geometry.Line3;
import com.minicad.geometry.SurfaceCurve3;
import com.minicad.geometry.ToroidalSurface;
import com.minicad.geometry.TrimmedCurve3;
import com.minicad.geometry.Vector3;
import com.minicad.geometry2d.Line2;
import com.minicad.geometry2d.BSplineCurve2;
import com.minicad.geometry2d.Circle2;
import com.minicad.geometry2d.Ellipse2;
import com.minicad.geometry2d.TrimmedCurve2;
import com.minicad.step.model.StepAdvancedFace;
import com.minicad.step.model.StepClosedShell;
import com.minicad.step.model.StepConicalSurface;
import com.minicad.step.model.StepCartesianPoint;
import com.minicad.step.model.StepCylindricalSurface;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepFaceEntity;
import com.minicad.step.model.StepFaceSurface;
import com.minicad.step.model.StepGeometricCurveSet;
import com.minicad.step.model.StepGeometricItemSpecificUsage;
import com.minicad.step.model.StepManifoldSolidBrep;
import com.minicad.step.model.StepAnnotationTextOccurrence;
import com.minicad.step.model.StepBSplineCurveWithKnots;
import com.minicad.step.model.StepBSplineSurfaceWithKnots;
import com.minicad.step.model.StepCircle;
import com.minicad.step.model.StepDraughtingCallout;
import com.minicad.step.model.StepEdgeCurve;
import com.minicad.step.model.StepEllipse;
import com.minicad.step.model.StepMeasureRepresentationItem;
import com.minicad.step.model.StepOpenShell;
import com.minicad.step.model.StepOrientedFace;
import com.minicad.step.model.StepRepresentation;
import com.minicad.step.model.StepPlane;
import com.minicad.step.model.StepLine;
import com.minicad.step.model.StepPcurve;
import com.minicad.step.model.StepSeamCurve;
import com.minicad.step.model.StepSurfaceCurve;
import com.minicad.step.model.StepToroidalSurface;
import com.minicad.step.model.StepTrimmedCurve;
import com.minicad.step.model.StepVertexPoint;
import com.minicad.step.semantic.StepCadBuilder;
import com.minicad.step.semantic.StepEntityResolver;
import com.minicad.step.syntax.StepFile;
import com.minicad.step.syntax.StepParser;
import com.minicad.topology.Edge;
import com.minicad.topology.EdgeLoop;
import com.minicad.topology.Face;
import com.minicad.topology.FaceBound;
import com.minicad.topology.Loop;
import com.minicad.topology.OrientedEdge;
import com.minicad.topology.VertexLoop;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Converts supported STEP topology into a JSON payload for the browser viewer.
 */
public final class StepPreviewJsonExporter {

    private StepPreviewJsonExporter() {
    }

    public static String export(String stepText) {
        StepFile stepFile = StepParser.parse(stepText);
        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(stepFile);
        StepCadBuilder builder = StepCadBuilder.fromResolved(resolved);

        PreviewPayload payload = buildPayload(stepFile, resolved, builder);
        return toJson(payload);
    }

    private static PreviewPayload buildPayload(
            StepFile stepFile,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder
    ) {
        StepMetadataExtractor metadata = StepMetadataExtractor.fromResolved(resolved);
        GeometryCollection legacyGeometry = buildLegacyGeometry(resolved, builder, metadata);
        AssemblyData assembly = buildAssemblyData(resolved, builder, metadata);

        BoundsAccumulator geometryBounds = new BoundsAccumulator();
        includeGeometry(geometryBounds, legacyGeometry);
        includeAssembly(geometryBounds, assembly);
        List<PmiPayload> pmi = buildPmiPayloads(resolved, assembly);
        BoundsAccumulator bounds = copyBounds(geometryBounds);
        includePmi(bounds, pmi);
        ValidationPayload validation = buildValidationPayload(legacyGeometry, assembly, geometryBounds, resolved);

        PreviewStats stats = new PreviewStats(
                stepFile.entities().size(),
                countEntities(resolved, StepManifoldSolidBrep.class),
                countShells(resolved),
                legacyGeometry.faces().size(),
                legacyGeometry.edges().size(),
                countUnsupportedFaces(resolved, builder)
        );

        return new PreviewPayload(
                stats,
                bounds.toPayload(),
                validation,
                pmi,
                legacyGeometry.unsupportedFaces(),
                legacyGeometry.edges(),
                legacyGeometry.faces(),
                assembly.representations(),
                assembly.instances()
        );
    }

    private static GeometryCollection buildLegacyGeometry(
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata
    ) {
        Set<Integer> shellIds = new TreeSet<>();
        for (StepEntity entity : resolved.values()) {
            if (entity instanceof StepOpenShell openShell) {
                shellIds.add(openShell.id());
            } else if (entity instanceof StepClosedShell closedShell) {
                shellIds.add(closedShell.id());
            } else if (entity instanceof StepManifoldSolidBrep solidBrep) {
                shellIds.add(solidBrep.outer().id());
            }
        }
        return buildGeometryForShells(shellIds, resolved, builder, metadata, Map.of());
    }

    private static GeometryCollection buildGeometryForShells(
            Set<Integer> shellIds,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata,
            Map<Integer, StepMetadataExtractor.DisplayMetadata> inheritedShellMetadata
    ) {
        List<FacePayload> faces = new ArrayList<>();
        List<UnsupportedFacePayload> unsupportedFaces = new ArrayList<>();
        Set<Integer> uniqueEdgeIds = new LinkedHashSet<>();

        for (Integer shellId : shellIds) {
            for (StepFaceEntity stepFace : shellFaces(resolved.get(shellId))) {
                PreviewFaceResult previewFace = buildPreviewFaceResult(
                        stepFace,
                        builder,
                        mergeMetadata(inheritedShellMetadata.get(shellId), metadata.forItem(stepFace.id()))
                );
                if (previewFace.face() == null) {
                    unsupportedFaces.add(previewFace.unsupportedFace());
                    continue;
                }
                faces.add(previewFace.face());
                for (com.minicad.step.model.StepFaceBound bound : stepFace.bounds()) {
                    if (bound.loop() instanceof com.minicad.step.model.StepEdgeLoop edgeLoop) {
                        uniqueEdgeIds.addAll(edgeLoop.edges().stream().map(edge -> edge.edgeElement().id()).toList());
                    }
                }
            }
        }

        List<EdgePayload> edges = new ArrayList<>();
        for (Integer edgeId : uniqueEdgeIds) {
            List<CartesianPoint> polyline = sampleEdgePreview(edgeId, resolved, builder);
            edges.add(new EdgePayload(edgeId, toPointPayloads(polyline)));
        }
        return new GeometryCollection(List.copyOf(edges), List.copyOf(faces), List.copyOf(unsupportedFaces));
    }

    private static PreviewFaceResult buildPreviewFaceResult(
            StepFaceEntity stepFace,
            StepCadBuilder builder,
            StepMetadataExtractor.DisplayMetadata metadata
    ) {
        if (stepFace instanceof StepOrientedFace orientedFace) {
            PreviewFaceResult base = buildPreviewFaceResult(orientedFace.faceElement(), builder, metadata);
            if (base.face() == null) {
                return new PreviewFaceResult(
                        null,
                        toUnsupportedFacePayload(stepFace, base.unsupportedFace() == null ? null : base.unsupportedFace().reason())
                );
            }
            if (orientedFace.orientation()) {
                return new PreviewFaceResult(base.face(), null);
            }
            return new PreviewFaceResult(reverseFacePayload(base.face()), null);
        }

        StepEntity geometry = faceGeometry(stepFace);
        if (geometry instanceof StepPlane) {
            try {
                return new PreviewFaceResult(
                        toPlanarFacePayload(stepFace.id(), builder.buildFace(stepFace.id()), faceDisplayName(stepFace), metadata),
                        null
                );
            } catch (TopologyException | StepResolutionException | UnsupportedGeometryException ex) {
                return new PreviewFaceResult(null, toUnsupportedFacePayload(stepFace, "planar face build failed"));
            }
        }
        if (geometry instanceof StepCylindricalSurface cylindricalSurface) {
            try {
                FacePayload payload = toCylindricalFacePayload(stepFace, cylindricalSurface, builder, metadata);
                if (payload != null) {
                    return new PreviewFaceResult(payload, null);
                }
            } catch (TopologyException | StepResolutionException | UnsupportedGeometryException ex) {
            }
        }
        if (geometry instanceof StepConicalSurface conicalSurface) {
            try {
                FacePayload payload = toConicalFacePayload(stepFace, conicalSurface, builder, metadata);
                if (payload != null) {
                    return new PreviewFaceResult(payload, null);
                }
            } catch (TopologyException | StepResolutionException | UnsupportedGeometryException ex) {
            }
        }
        if (geometry instanceof StepBSplineSurfaceWithKnots splineSurface) {
            try {
                return new PreviewFaceResult(toBSplineSurfaceFacePayload(stepFace, splineSurface, builder, metadata), null);
            } catch (TopologyException | StepResolutionException | UnsupportedGeometryException ex) {
                return new PreviewFaceResult(null, toUnsupportedFacePayload(stepFace, "b-spline surface preview failed"));
            }
        }
        if (geometry instanceof StepToroidalSurface toroidalSurface) {
            try {
                FacePayload payload = toToroidalFacePayload(stepFace, toroidalSurface, builder, metadata);
                if (payload != null) {
                    return new PreviewFaceResult(payload, null);
                }
            } catch (TopologyException | StepResolutionException | UnsupportedGeometryException ex) {
            }
        }
        if (geometry instanceof StepCylindricalSurface
                || geometry instanceof StepConicalSurface
                || geometry instanceof StepToroidalSurface) {
            return toParametricTrimmedFaceResult(stepFace, geometry, metadata, builder);
        }
        return new PreviewFaceResult(null, toUnsupportedFacePayload(stepFace, "surface type not previewable"));
    }

    private static AssemblyData buildAssemblyData(
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata
    ) {
        AssemblyGraph graph = StepAssemblyGraphBuilder.build(resolved);
        Map<Integer, RepresentationPayload> representations = new LinkedHashMap<>();
        for (AssemblyRepresentation assemblyRepresentation : graph.representations()) {
            StepEntity entity = resolved.get(assemblyRepresentation.representationId());
            if (entity instanceof StepRepresentation representation && representation.shapeRepresentation()) {
                Set<Integer> shellIds = collectRepresentationShells(representation);
                StepMetadataExtractor.DisplayMetadata representationMetadata = metadata.forItem(representation.id());
                GeometryCollection geometry = buildGeometryForShells(
                        shellIds,
                        resolved,
                        builder,
                        metadata,
                        collectInheritedShellMetadata(representation, metadata)
                );
                representations.put(
                        representation.id(),
                        new RepresentationPayload(
                                representation.id(),
                                assemblyRepresentation.name(),
                                representationMetadata.layers(),
                                toColorPayload(representationMetadata.rgb()),
                                geometry.edges(),
                                geometry.faces()
                        )
                );
            }
        }

        List<InstancePayload> instances = new ArrayList<>();
        for (AssemblyNode node : graph.nodes()) {
            instances.add(new InstancePayload(
                    node.id(),
                    node.parentId(),
                    node.productDefinitionId(),
                    node.occurrenceId(),
                    node.representationIds().isEmpty() ? null : node.representationIds().getFirst(),
                    node.representationIds(),
                    node.label(),
                    node.description(),
                    node.localMatrix(),
                    node.worldMatrix(),
                    node.depth()
                ));
        }

        return new AssemblyData(List.copyOf(representations.values()), List.copyOf(instances));
    }

    private static Set<Integer> collectRepresentationShells(StepRepresentation representation) {
        Set<Integer> shellIds = new TreeSet<>();
        for (StepEntity item : representation.items()) {
            if (item instanceof StepOpenShell openShell) {
                shellIds.add(openShell.id());
            } else if (item instanceof StepClosedShell closedShell) {
                shellIds.add(closedShell.id());
            } else if (item instanceof StepManifoldSolidBrep solidBrep) {
                shellIds.add(solidBrep.outer().id());
            }
        }
        return shellIds;
    }

    private static Map<Integer, StepMetadataExtractor.DisplayMetadata> collectInheritedShellMetadata(
            StepRepresentation representation,
            StepMetadataExtractor metadata
    ) {
        Map<Integer, StepMetadataExtractor.DisplayMetadata> metadataByShellId = new LinkedHashMap<>();
        for (StepEntity item : representation.items()) {
            StepMetadataExtractor.DisplayMetadata itemMetadata = metadata.forItem(item.id());
            if (item instanceof StepOpenShell openShell) {
                metadataByShellId.put(openShell.id(), mergeMetadata(metadataByShellId.get(openShell.id()), itemMetadata));
            } else if (item instanceof StepClosedShell closedShell) {
                metadataByShellId.put(closedShell.id(), mergeMetadata(metadataByShellId.get(closedShell.id()), itemMetadata));
            } else if (item instanceof StepManifoldSolidBrep solidBrep) {
                int shellId = solidBrep.outer().id();
                metadataByShellId.put(shellId, mergeMetadata(metadataByShellId.get(shellId), itemMetadata));
            }
        }
        return Map.copyOf(metadataByShellId);
    }

    private static StepMetadataExtractor.DisplayMetadata mergeMetadata(
            StepMetadataExtractor.DisplayMetadata inherited,
            StepMetadataExtractor.DisplayMetadata direct
    ) {
        StepMetadataExtractor.DisplayMetadata left = inherited == null ? StepMetadataExtractor.DisplayMetadata.EMPTY : inherited;
        StepMetadataExtractor.DisplayMetadata right = direct == null ? StepMetadataExtractor.DisplayMetadata.EMPTY : direct;
        int[] rgb = right.rgb() != null ? right.rgb() : left.rgb();
        Set<String> layers = new LinkedHashSet<>(left.layers());
        layers.addAll(right.layers());
        return new StepMetadataExtractor.DisplayMetadata(rgb, List.copyOf(layers));
    }

    private static String faceDisplayName(StepFaceEntity stepFace) {
        if (stepFace instanceof StepOrientedFace orientedFace) {
            return faceDisplayName(orientedFace.faceElement());
        }
        return stepFace.name();
    }

    private static UnsupportedFacePayload toUnsupportedFacePayload(StepFaceEntity stepFace, String reason) {
        StepEntity geometry = faceGeometry(stepFace);
        return new UnsupportedFacePayload(
                stepFace.id(),
                faceDisplayName(stepFace),
                surfaceTypeName(geometry),
                reason == null ? "preview export returned no mesh" : reason
        );
    }

    private static ColorPayload toColorPayload(int[] rgb) {
        if (rgb == null) {
            return null;
        }
        return new ColorPayload(rgb[0], rgb[1], rgb[2]);
    }

    private static int countEntities(Map<Integer, StepEntity> resolved, Class<? extends StepEntity> type) {
        int count = 0;
        for (StepEntity entity : resolved.values()) {
            if (type.isInstance(entity)) {
                count++;
            }
        }
        return count;
    }

    private static int countShells(Map<Integer, StepEntity> resolved) {
        int count = 0;
        for (StepEntity entity : resolved.values()) {
            if (entity instanceof StepOpenShell || entity instanceof StepClosedShell) {
                count++;
            }
        }
        return count;
    }

    private static int countUnsupportedFaces(Map<Integer, StepEntity> resolved, StepCadBuilder builder) {
        int unsupported = 0;
        for (StepEntity entity : resolved.values()) {
            if (entity instanceof StepFaceEntity stepFace
                    && buildPreviewFaceResult(stepFace, builder, StepMetadataExtractor.DisplayMetadata.EMPTY).face() == null) {
                unsupported++;
            }
        }
        return unsupported;
    }

    private static List<StepFaceEntity> shellFaces(StepEntity entity) {
        if (entity instanceof StepOpenShell openShell) {
            return openShell.faces();
        }
        if (entity instanceof StepClosedShell closedShell) {
            return closedShell.faces();
        }
        throw new UnsupportedGeometryException("preview export requires OPEN_SHELL or CLOSED_SHELL");
    }

    private static FacePayload toPlanarFacePayload(
            int stepId,
            Face face,
            String name,
            StepMetadataExtractor.DisplayMetadata metadata
    ) {
        List<LoopPayload> loops = new ArrayList<>();
        for (FaceBound bound : face.bounds()) {
            loops.add(new LoopPayload(bound.outer(), toPointPayloads(sampleLoop(bound))));
        }
        Direction3 normal = face.surface().normal();
        return new FacePayload(
                stepId,
                name,
                "PLANE",
                toPointPayload(face.surface().origin()),
                new VectorPayload(normal.x(), normal.y(), normal.z()),
                face.sameSense(),
                toColorPayload(metadata.rgb()),
                metadata.layers(),
                loops,
                List.of()
        );
    }

    private static FacePayload toCylindricalFacePayload(
            StepFaceEntity stepFace,
            StepCylindricalSurface stepSurface,
            StepCadBuilder builder,
            StepMetadataExtractor.DisplayMetadata metadata
    ) {
        List<FaceBound> bounds = buildFaceBounds(stepFace, builder);
        if (bounds.size() != 1 || !bounds.getFirst().outer()) {
            return null;
        }

        if (!(bounds.getFirst().loop() instanceof EdgeLoop outerLoop)) {
            return null;
        }
        if (outerLoop.edges().size() != 4) {
            return null;
        }

        List<OrientedEdge> circleEdges = outerLoop.edges().stream()
                .filter(edge -> edge.edge().curve() instanceof Circle)
                .toList();
        List<OrientedEdge> lineEdges = outerLoop.edges().stream()
                .filter(edge -> edge.edge().curve() instanceof Line3)
                .toList();
        if (circleEdges.size() != 2 || lineEdges.size() != 2) {
            return null;
        }

        CylindricalSurface surface = builder.buildCylindricalSurface(stepSurface.id());
        OrientedEdge lowerArc = circleEdges.getFirst();
        OrientedEdge upperArc = circleEdges.getLast();
        if (averageAxialHeight(surface, sampleOrientedEdge(lowerArc)) > averageAxialHeight(surface, sampleOrientedEdge(upperArc))) {
            lowerArc = circleEdges.getLast();
            upperArc = circleEdges.getFirst();
        }

        List<CartesianPoint> lowerArcPoints = sampleOrientedEdge(lowerArc);
        List<CartesianPoint> upperArcPoints = sampleOrientedEdge(upperArc);
        double lowerHeight = averageAxialHeight(surface, lowerArcPoints);
        double upperHeight = averageAxialHeight(surface, upperArcPoints);
        if (Math.abs(upperHeight - lowerHeight) <= Epsilon.EPS) {
            return null;
        }

        List<Double> angles = unwrapAngles(surface, lowerArcPoints);
        if (angles.size() < 2) {
            return null;
        }

        boolean sameSense = faceSameSense(stepFace);
        List<PointPayload> triangles = triangulateCylindricalStrip(surface, lowerHeight, upperHeight, angles, sameSense);
        if (triangles.isEmpty()) {
            return null;
        }

        Vector3 startNormal = cylindricalNormal(surface, angles.getFirst(), sameSense);
        return new FacePayload(
                stepFace.id(),
                faceDisplayName(stepFace),
                "CYLINDRICAL_SURFACE",
                toPointPayload(surfacePoint(surface, angles.getFirst(), lowerHeight)),
                new VectorPayload(startNormal.x(), startNormal.y(), startNormal.z()),
                sameSense,
                toColorPayload(metadata.rgb()),
                metadata.layers(),
                List.of(new LoopPayload(true, toPointPayloads(sampleLoop(bounds.getFirst())))),
                triangles
        );
    }

    private static FacePayload toConicalFacePayload(
            StepFaceEntity stepFace,
            StepConicalSurface stepSurface,
            StepCadBuilder builder,
            StepMetadataExtractor.DisplayMetadata metadata
    ) {
        List<FaceBound> bounds = buildFaceBounds(stepFace, builder);
        if (bounds.size() != 1 || !bounds.getFirst().outer()) {
            return null;
        }
        if (!(bounds.getFirst().loop() instanceof EdgeLoop outerLoop) || outerLoop.edges().size() != 4) {
            return null;
        }

        List<OrientedEdge> circleEdges = outerLoop.edges().stream()
                .filter(edge -> edge.edge().curve() instanceof Circle)
                .toList();
        List<OrientedEdge> lineEdges = outerLoop.edges().stream()
                .filter(edge -> edge.edge().curve() instanceof Line3)
                .toList();
        if (circleEdges.size() != 2 || lineEdges.size() != 2) {
            return null;
        }

        ConicalSurface surface = builder.buildConicalSurface(stepSurface.id());
        OrientedEdge lowerArc = circleEdges.getFirst();
        OrientedEdge upperArc = circleEdges.getLast();
        if (averageAxialHeight(surface.position(), sampleOrientedEdge(lowerArc)) > averageAxialHeight(surface.position(), sampleOrientedEdge(upperArc))) {
            lowerArc = circleEdges.getLast();
            upperArc = circleEdges.getFirst();
        }

        List<CartesianPoint> lowerArcPoints = sampleOrientedEdge(lowerArc);
        List<CartesianPoint> upperArcPoints = sampleOrientedEdge(upperArc);
        double lowerHeight = averageAxialHeight(surface.position(), lowerArcPoints);
        double upperHeight = averageAxialHeight(surface.position(), upperArcPoints);
        if (Math.abs(upperHeight - lowerHeight) <= Epsilon.EPS) {
            return null;
        }

        List<Double> angles = unwrapAngles(surface.position(), lowerArcPoints);
        if (angles.size() < 2) {
            return null;
        }

        boolean sameSense = faceSameSense(stepFace);
        List<PointPayload> triangles = triangulateConicalStrip(surface, lowerHeight, upperHeight, angles, sameSense);
        if (triangles.isEmpty()) {
            return null;
        }

        Vector3 startNormal = conicalNormal(surface, angles.getFirst(), sameSense);
        return new FacePayload(
                stepFace.id(),
                faceDisplayName(stepFace),
                "CONICAL_SURFACE",
                toPointPayload(conicalSurfacePoint(surface, angles.getFirst(), lowerHeight)),
                new VectorPayload(startNormal.x(), startNormal.y(), startNormal.z()),
                sameSense,
                toColorPayload(metadata.rgb()),
                metadata.layers(),
                List.of(new LoopPayload(true, toPointPayloads(sampleLoop(bounds.getFirst())))),
                triangles
        );
    }

    private static FacePayload toToroidalFacePayload(
            StepFaceEntity stepFace,
            StepToroidalSurface stepSurface,
            StepCadBuilder builder,
            StepMetadataExtractor.DisplayMetadata metadata
    ) {
        List<FaceBound> bounds = buildFaceBounds(stepFace, builder);
        if (bounds.size() != 1 || !bounds.getFirst().outer()) {
            return null;
        }
        if (!(bounds.getFirst().loop() instanceof EdgeLoop outerLoop) || outerLoop.edges().size() != 4) {
            return null;
        }

        List<OrientedEdge> circleEdges = outerLoop.edges().stream()
                .filter(edge -> edge.edge().curve() instanceof Circle)
                .toList();
        if (circleEdges.size() != 4) {
            return null;
        }

        ToroidalSurface surface = builder.buildToroidalSurface(stepSurface.id());
        List<OrientedEdge> varyingUEdges = new ArrayList<>();
        List<OrientedEdge> varyingVEdges = new ArrayList<>();
        for (OrientedEdge edge : circleEdges) {
            List<CartesianPoint> points = sampleOrientedEdge(edge);
            List<Double> uValues = unwrapToroidalU(surface, points);
            List<Double> vValues = unwrapToroidalV(surface, points);
            double uRange = Math.abs(uValues.getLast() - uValues.getFirst());
            double vRange = Math.abs(vValues.getLast() - vValues.getFirst());
            if (uRange >= vRange) {
                varyingUEdges.add(edge);
            } else {
                varyingVEdges.add(edge);
            }
        }
        if (varyingUEdges.size() != 2 || varyingVEdges.size() != 2) {
            return null;
        }

        OrientedEdge lowerVEdge = varyingUEdges.getFirst();
        OrientedEdge upperVEdge = varyingUEdges.getLast();
        if (averageToroidalV(surface, sampleOrientedEdge(lowerVEdge)) > averageToroidalV(surface, sampleOrientedEdge(upperVEdge))) {
            lowerVEdge = varyingUEdges.getLast();
            upperVEdge = varyingUEdges.getFirst();
        }

        List<CartesianPoint> lowerPoints = sampleOrientedEdge(lowerVEdge);
        List<Double> uValues = unwrapToroidalU(surface, lowerPoints);
        double lowerV = averageToroidalV(surface, lowerPoints);
        double upperV = averageToroidalV(surface, sampleOrientedEdge(upperVEdge));
        if (Math.abs(upperV - lowerV) <= Epsilon.EPS || uValues.size() < 2) {
            return null;
        }

        boolean sameSense = faceSameSense(stepFace);
        List<PointPayload> triangles = triangulateToroidalStrip(surface, lowerV, upperV, uValues, sameSense);
        if (triangles.isEmpty()) {
            return null;
        }

        Vector3 startNormal = toroidalNormal(surface, uValues.getFirst(), lowerV, sameSense);
        return new FacePayload(
                stepFace.id(),
                faceDisplayName(stepFace),
                "TOROIDAL_SURFACE",
                toPointPayload(toroidalSurfacePoint(surface, uValues.getFirst(), lowerV)),
                new VectorPayload(startNormal.x(), startNormal.y(), startNormal.z()),
                sameSense,
                toColorPayload(metadata.rgb()),
                metadata.layers(),
                List.of(new LoopPayload(true, toPointPayloads(sampleLoop(bounds.getFirst())))),
                triangles
        );
    }

    private static FacePayload toBSplineSurfaceFacePayload(
            StepFaceEntity stepFace,
            StepBSplineSurfaceWithKnots stepSurface,
            StepCadBuilder builder,
            StepMetadataExtractor.DisplayMetadata metadata
    ) {
        BSplineSurface3 surface = builder.buildBSplineSurface(stepSurface.id());
        List<FaceBound> bounds = buildFaceBounds(stepFace, builder);
        if (bounds.size() != 1 || !bounds.getFirst().outer()) {
            return null;
        }
        if (!(bounds.getFirst().loop() instanceof EdgeLoop outerLoop) || outerLoop.edges().size() != 4) {
            return null;
        }

        SurfacePatch patch = buildFourSidedPatch(outerLoop);
        if (patch == null) {
            return null;
        }
        int uSegments = Math.max(patch.uSegments(), 10);
        int vSegments = Math.max(patch.vSegments(), 10);
        List<PointPayload> triangles = triangulateSurfaceGrid(
                sampleSurfaceGrid(surface, uSegments, vSegments),
                faceSameSense(stepFace)
        );
        if (triangles.isEmpty()) {
            return null;
        }
        Vector3 normal = surface.normalAt((surface.uStart() + surface.uEnd()) * 0.5, (surface.vStart() + surface.vEnd()) * 0.5);
        if (!faceSameSense(stepFace)) {
            normal = normal.scale(-1.0);
        }
        return new FacePayload(
                stepFace.id(),
                faceDisplayName(stepFace),
                "B_SPLINE_SURFACE_WITH_KNOTS",
                toPointPayload(surface.pointAt(surface.uStart(), surface.vStart())),
                new VectorPayload(normal.x(), normal.y(), normal.z()),
                faceSameSense(stepFace),
                toColorPayload(metadata.rgb()),
                metadata.layers(),
                List.of(new LoopPayload(true, toPointPayloads(sampleLoop(bounds.getFirst())))),
                triangles
        );
    }

    private static FacePayload toFourSidedPatchFacePayload(
            StepFaceEntity stepFace,
            StepEntity geometry,
            StepMetadataExtractor.DisplayMetadata metadata,
            StepCadBuilder builder
    ) {
        List<FaceBound> bounds = buildFaceBounds(stepFace, builder);
        if (bounds.size() != 1 || !bounds.getFirst().outer()) {
            return null;
        }
        if (!(bounds.getFirst().loop() instanceof EdgeLoop outerLoop) || outerLoop.edges().size() != 4) {
            return null;
        }
        SurfacePatch patch = buildFourSidedPatch(outerLoop);
        if (patch == null) {
            return null;
        }
        List<PointPayload> triangles = triangulatePatch(patch, faceSameSense(stepFace));
        if (triangles.isEmpty()) {
            return null;
        }
        Vector3 normal = patch.normalAt(0.5, 0.5);
        if (!faceSameSense(stepFace)) {
            normal = normal.scale(-1.0);
        }
        return new FacePayload(
                stepFace.id(),
                faceDisplayName(stepFace),
                surfaceTypeName(geometry),
                toPointPayload(patch.pointAt(0.0, 0.0)),
                new VectorPayload(normal.x(), normal.y(), normal.z()),
                faceSameSense(stepFace),
                toColorPayload(metadata.rgb()),
                metadata.layers(),
                List.of(new LoopPayload(true, toPointPayloads(sampleLoop(bounds.getFirst())))),
                triangles
        );
    }

    private static PreviewFaceResult toParametricTrimmedFaceResult(
            StepFaceEntity stepFace,
            StepEntity geometry,
            StepMetadataExtractor.DisplayMetadata metadata,
            StepCadBuilder builder
    ) {
        if (stepFace.bounds().isEmpty() || stepFace.bounds().stream().noneMatch(com.minicad.step.model.StepFaceBound::outer)) {
            return new PreviewFaceResult(null, toUnsupportedFacePayload(stepFace, "missing outer bound"));
        }
        ParametricSurfaceMapper mapper = mapperForSurface(geometry, builder);
        if (mapper == null) {
            return new PreviewFaceResult(null, toUnsupportedFacePayload(stepFace, "no parametric mapper for surface"));
        }
        List<ParametricLoopPayload> loops = buildParametricLoops(stepFace, geometry, mapper, builder);
        if (loops.isEmpty()) {
            try {
                loops = buildParametricLoops(buildFaceBounds(stepFace, builder), mapper);
            } catch (TopologyException | StepResolutionException | UnsupportedGeometryException ex) {
                return new PreviewFaceResult(null, toUnsupportedFacePayload(stepFace, "failed to derive face bounds"));
            }
        }
        if (loops.isEmpty()) {
            return new PreviewFaceResult(null, toUnsupportedFacePayload(stepFace, "failed to build parametric loops"));
        }
        UvBounds uvBounds = boundsOf(loops);
        if (uvBounds == null || uvBounds.uSpan() <= Epsilon.EPS || uvBounds.vSpan() <= Epsilon.EPS) {
            return new PreviewFaceResult(null, toUnsupportedFacePayload(stepFace, "degenerate parametric bounds"));
        }

        int sampleCount = loops.stream().mapToInt(loop -> loop.points().size()).max().orElse(0);
        // Start with a denser grid so small curved patches do not show visibly faceted silhouettes.
        int baseUSegments = Math.max(32, Math.min(128, sampleCount * 3));
        int baseVSegments = Math.max(20, Math.min(72, sampleCount * 2));
        List<PointPayload> triangles = triangulateParametricFaceAdaptive(
                mapper,
                loops,
                uvBounds,
                baseUSegments,
                baseVSegments,
                faceSameSense(stepFace)
        );
        if (triangles.isEmpty()) {
            return new PreviewFaceResult(null, toUnsupportedFacePayload(stepFace, "parametric triangulation produced no cells"));
        }

        double centerU = (uvBounds.minU() + uvBounds.maxU()) * 0.5;
        double centerV = (uvBounds.minV() + uvBounds.maxV()) * 0.5;
        Vector3 normal = mapper.normalAt(centerU, centerV);
        if (!faceSameSense(stepFace)) {
            normal = normal.scale(-1.0);
        }
        return new PreviewFaceResult(
                new FacePayload(
                        stepFace.id(),
                        faceDisplayName(stepFace),
                        surfaceTypeName(geometry),
                        toPointPayload(mapper.pointAt(centerU, centerV)),
                        new VectorPayload(normal.x(), normal.y(), normal.z()),
                        faceSameSense(stepFace),
                        toColorPayload(metadata.rgb()),
                        metadata.layers(),
                        toParametricLoopPayloads(loops, mapper),
                        triangles
                ),
                null
        );
    }

    private static List<PointPayload> triangulateCylindricalStrip(
            CylindricalSurface surface,
            double lowerHeight,
            double upperHeight,
            List<Double> angles,
            boolean sameSense
    ) {
        List<PointPayload> triangles = new ArrayList<>();
        for (int index = 0; index < angles.size() - 1; index++) {
            double angle0 = angles.get(index);
            double angle1 = angles.get(index + 1);
            if (Math.abs(angle1 - angle0) <= Epsilon.EPS) {
                continue;
            }

            CartesianPoint lower0 = surfacePoint(surface, angle0, lowerHeight);
            CartesianPoint lower1 = surfacePoint(surface, angle1, lowerHeight);
            CartesianPoint upper0 = surfacePoint(surface, angle0, upperHeight);
            CartesianPoint upper1 = surfacePoint(surface, angle1, upperHeight);

            Vector3 targetNormal = cylindricalNormal(surface, (angle0 + angle1) * 0.5, sameSense);
            appendOrientedTriangle(triangles, lower0, lower1, upper1, targetNormal);
            appendOrientedTriangle(triangles, lower0, upper1, upper0, targetNormal);
        }
        return List.copyOf(triangles);
    }

    private static List<PointPayload> triangulateConicalStrip(
            ConicalSurface surface,
            double lowerHeight,
            double upperHeight,
            List<Double> angles,
            boolean sameSense
    ) {
        List<PointPayload> triangles = new ArrayList<>();
        for (int index = 0; index < angles.size() - 1; index++) {
            double angle0 = angles.get(index);
            double angle1 = angles.get(index + 1);
            if (Math.abs(angle1 - angle0) <= Epsilon.EPS) {
                continue;
            }

            CartesianPoint lower0 = conicalSurfacePoint(surface, angle0, lowerHeight);
            CartesianPoint lower1 = conicalSurfacePoint(surface, angle1, lowerHeight);
            CartesianPoint upper0 = conicalSurfacePoint(surface, angle0, upperHeight);
            CartesianPoint upper1 = conicalSurfacePoint(surface, angle1, upperHeight);

            Vector3 targetNormal = conicalNormal(surface, (angle0 + angle1) * 0.5, sameSense);
            appendOrientedTriangle(triangles, lower0, lower1, upper1, targetNormal);
            appendOrientedTriangle(triangles, lower0, upper1, upper0, targetNormal);
        }
        return List.copyOf(triangles);
    }

    private static List<PointPayload> triangulateToroidalStrip(
            ToroidalSurface surface,
            double lowerV,
            double upperV,
            List<Double> uValues,
            boolean sameSense
    ) {
        List<PointPayload> triangles = new ArrayList<>();
        for (int index = 0; index < uValues.size() - 1; index++) {
            double u0 = uValues.get(index);
            double u1 = uValues.get(index + 1);
            if (Math.abs(u1 - u0) <= Epsilon.EPS) {
                continue;
            }
            CartesianPoint p00 = toroidalSurfacePoint(surface, u0, lowerV);
            CartesianPoint p10 = toroidalSurfacePoint(surface, u1, lowerV);
            CartesianPoint p01 = toroidalSurfacePoint(surface, u0, upperV);
            CartesianPoint p11 = toroidalSurfacePoint(surface, u1, upperV);
            Vector3 targetNormal = toroidalNormal(surface, (u0 + u1) * 0.5, (lowerV + upperV) * 0.5, sameSense);
            appendOrientedTriangle(triangles, p00, p10, p11, targetNormal);
            appendOrientedTriangle(triangles, p00, p11, p01, targetNormal);
        }
        return List.copyOf(triangles);
    }

    private static List<PointPayload> triangulatePatch(SurfacePatch patch, boolean sameSense) {
        List<PointPayload> triangles = new ArrayList<>();
        for (int u = 0; u < patch.uSegments(); u++) {
            for (int v = 0; v < patch.vSegments(); v++) {
                CartesianPoint p00 = patch.pointAt((double) u / patch.uSegments(), (double) v / patch.vSegments());
                CartesianPoint p10 = patch.pointAt((double) (u + 1) / patch.uSegments(), (double) v / patch.vSegments());
                CartesianPoint p01 = patch.pointAt((double) u / patch.uSegments(), (double) (v + 1) / patch.vSegments());
                CartesianPoint p11 = patch.pointAt((double) (u + 1) / patch.uSegments(), (double) (v + 1) / patch.vSegments());
                Vector3 targetNormal = patch.normalAt((u + 0.5) / patch.uSegments(), (v + 0.5) / patch.vSegments());
                if (!sameSense) {
                    targetNormal = targetNormal.scale(-1.0);
                }
                appendOrientedTriangle(triangles, p00, p10, p11, targetNormal);
                appendOrientedTriangle(triangles, p00, p11, p01, targetNormal);
            }
        }
        return List.copyOf(triangles);
    }

    private static List<PointPayload> triangulateSurfaceGrid(List<List<CartesianPoint>> grid, boolean sameSense) {
        List<PointPayload> triangles = new ArrayList<>();
        if (grid.size() < 2 || grid.getFirst().size() < 2) {
            return List.of();
        }
        for (int u = 0; u + 1 < grid.size(); u++) {
            for (int v = 0; v + 1 < grid.get(u).size(); v++) {
                CartesianPoint p00 = grid.get(u).get(v);
                CartesianPoint p10 = grid.get(u + 1).get(v);
                CartesianPoint p01 = grid.get(u).get(v + 1);
                CartesianPoint p11 = grid.get(u + 1).get(v + 1);
                Vector3 targetNormal = p10.subtract(p00).cross(p01.subtract(p00));
                if (targetNormal.norm() <= Epsilon.EPS) {
                    continue;
                }
                if (!sameSense) {
                    targetNormal = targetNormal.scale(-1.0);
                }
                appendOrientedTriangle(triangles, p00, p10, p11, targetNormal);
                appendOrientedTriangle(triangles, p00, p11, p01, targetNormal);
            }
        }
        return List.copyOf(triangles);
    }

    private static List<List<CartesianPoint>> sampleSurfaceGrid(BSplineSurface3 surface, int uSegments, int vSegments) {
        return surface.sampleGrid(Math.max(uSegments, 2), Math.max(vSegments, 2));
    }

    private static List<ParametricLoopPayload> buildParametricLoops(List<FaceBound> bounds, ParametricSurfaceMapper mapper) {
        List<ParametricLoopPayload> loops = new ArrayList<>();
        for (FaceBound bound : bounds) {
            if (bound.loop() instanceof VertexLoop) {
                return List.of();
            }
            List<CartesianPoint> points3d = sampleLoop(bound);
            if (points3d.size() < 4) {
                return List.of();
            }
            List<UvPoint> uvPoints = new ArrayList<>(points3d.size());
            UvPoint previous = null;
            for (CartesianPoint point : points3d) {
                UvPoint uv = mapper.project(point, previous);
                if (uv == null) {
                    return List.of();
                }
                uvPoints.add(uv);
                previous = uv;
            }
            uvPoints = normalizePeriodicLoop(uvPoints, mapper);
            uvPoints.set(0, uvPoints.getFirst());
            uvPoints.set(uvPoints.size() - 1, uvPoints.getFirst());
            loops.add(new ParametricLoopPayload(bound.outer(), List.copyOf(uvPoints)));
        }
        return List.copyOf(loops);
    }

    private static List<ParametricLoopPayload> buildParametricLoops(
            StepFaceEntity stepFace,
            StepEntity geometry,
            ParametricSurfaceMapper mapper,
            StepCadBuilder builder
    ) {
        List<ParametricLoopPayload> loops = new ArrayList<>();
        for (com.minicad.step.model.StepFaceBound bound : stepFace.bounds()) {
            if (!(bound.loop() instanceof com.minicad.step.model.StepEdgeLoop edgeLoop)) {
                return List.of();
            }
            List<UvPoint> loopPoints = new ArrayList<>();
            boolean firstEdge = true;
            for (com.minicad.step.model.StepOrientedEdge orientedEdge : edgeLoop.edges()) {
                List<UvPoint> edgePoints = sampleParametricOrientedEdge(orientedEdge, geometry, mapper, builder);
                if (edgePoints == null || edgePoints.size() < 2) {
                    return List.of();
                }
                int startIndex = firstEdge ? 0 : 1;
                for (int index = startIndex; index < edgePoints.size(); index++) {
                    loopPoints.add(edgePoints.get(index));
                }
                firstEdge = false;
            }
            if (loopPoints.size() < 4) {
                return List.of();
            }
            if (!bound.orientation()) {
                loopPoints = reverseClosedLoop(loopPoints);
            }
            loopPoints = normalizePeriodicLoop(loopPoints, mapper);
            if (!sameUv(loopPoints.getFirst(), loopPoints.getLast())) {
                loopPoints.add(loopPoints.getFirst());
            }
            loops.add(new ParametricLoopPayload(bound.outer(), List.copyOf(loopPoints)));
        }
        return List.copyOf(loops);
    }

    private static List<UvPoint> normalizePeriodicLoop(List<UvPoint> points, ParametricSurfaceMapper mapper) {
        if (points.size() < 2) {
            return points;
        }
        Double uPeriod = mapper.uPeriod();
        Double vPeriod = mapper.vPeriod();
        List<UvPoint> normalized = new ArrayList<>(points.size());
        UvPoint previous = null;
        for (UvPoint point : points) {
            double u = point.u();
            double v = point.v();
            if (previous != null) {
                if (uPeriod != null) {
                    u = unwrapPeriodic(u, previous.u(), uPeriod);
                }
                if (vPeriod != null) {
                    v = unwrapPeriodic(v, previous.v(), vPeriod);
                }
            }
            UvPoint normalizedPoint = new UvPoint(u, v);
            normalized.add(normalizedPoint);
            previous = normalizedPoint;
        }
        if (normalized.size() >= 2) {
            UvPoint first = normalized.getFirst();
            UvPoint last = normalized.getLast();
            double u = last.u();
            double v = last.v();
            if (uPeriod != null) {
                u = unwrapPeriodic(u, first.u(), uPeriod);
            }
            if (vPeriod != null) {
                v = unwrapPeriodic(v, first.v(), vPeriod);
            }
            normalized.set(normalized.size() - 1, new UvPoint(u, v));
        }
        return normalized;
    }

    private static UvBounds boundsOf(List<ParametricLoopPayload> loops) {
        double minU = Double.POSITIVE_INFINITY;
        double minV = Double.POSITIVE_INFINITY;
        double maxU = Double.NEGATIVE_INFINITY;
        double maxV = Double.NEGATIVE_INFINITY;
        for (ParametricLoopPayload loop : loops) {
            for (UvPoint point : loop.points()) {
                minU = Math.min(minU, point.u());
                minV = Math.min(minV, point.v());
                maxU = Math.max(maxU, point.u());
                maxV = Math.max(maxV, point.v());
            }
        }
        if (!Double.isFinite(minU) || !Double.isFinite(minV) || !Double.isFinite(maxU) || !Double.isFinite(maxV)) {
            return null;
        }
        return new UvBounds(minU, minV, maxU, maxV);
    }

    private static List<UvPoint> sampleParametricOrientedEdge(
            com.minicad.step.model.StepOrientedEdge orientedEdge,
            StepEntity faceGeometry,
            ParametricSurfaceMapper mapper,
            StepCadBuilder builder
    ) {
        StepVertexPoint startVertex = orientedEdge.orientation()
                ? orientedEdge.edgeElement().start()
                : orientedEdge.edgeElement().end();
        StepVertexPoint endVertex = orientedEdge.orientation()
                ? orientedEdge.edgeElement().end()
                : orientedEdge.edgeElement().start();
        StepEntity edgeGeometry = orientedEdge.edgeElement().edgeGeometry();
        List<StepPcurve> pcurves = switch (edgeGeometry) {
            case StepSurfaceCurve surfaceCurve -> matchingPcurves(surfaceCurve.associatedGeometry(), faceGeometry);
            case StepSeamCurve seamCurve -> matchingPcurves(seamCurve.associatedGeometry(), faceGeometry);
            default -> List.of();
        };
        if (pcurves.isEmpty()) {
            return null;
        }
        UvPoint projectedStart = mapper.project(pointFromStep(startVertex.point()), null);
        UvPoint projectedEnd = mapper.project(pointFromStep(endVertex.point()), projectedStart);
        List<UvPoint> best = null;
        double bestScore = Double.POSITIVE_INFINITY;
        for (StepPcurve pcurve : pcurves) {
            Object built;
            try {
                built = builder.buildPcurve2(pcurve.id());
            } catch (UnsupportedGeometryException ex) {
                continue;
            }
            if (built instanceof Line2 line) {
                UvPoint start = snapToLine(projectedStart, line);
                UvPoint end = snapToLine(projectedEnd, line);
                double score = distanceSquared(projectedStart, start) + distanceSquared(projectedEnd, end);
                List<UvPoint> samples = sampleLinePcurve(line, start, end);
                if (best == null || score < bestScore) {
                    best = samples;
                    bestScore = score;
                }
                continue;
            }
            if (built instanceof BSplineCurve2 spline) {
                List<UvPoint> samples = sampleSplinePcurve(spline, projectedStart, projectedEnd);
                if (!samples.isEmpty()) {
                    double score = distanceSquared(projectedStart, samples.getFirst()) + distanceSquared(projectedEnd, samples.getLast());
                    if (best == null || score < bestScore) {
                        best = samples;
                        bestScore = score;
                    }
                }
                continue;
            }
            if (built instanceof Circle2 circle) {
                UvPoint start = snapToCircle(projectedStart, circle);
                UvPoint end = snapToCircle(projectedEnd, circle);
                double score = distanceSquared(projectedStart, start) + distanceSquared(projectedEnd, end);
                List<UvPoint> samples = sampleCirclePcurve(circle, start, end);
                if (!samples.isEmpty() && (best == null || score < bestScore)) {
                    best = samples;
                    bestScore = score;
                }
                continue;
            }
            if (built instanceof Ellipse2 ellipse) {
                UvPoint start = snapToEllipse(projectedStart, ellipse);
                UvPoint end = snapToEllipse(projectedEnd, ellipse);
                double score = distanceSquared(projectedStart, start) + distanceSquared(projectedEnd, end);
                List<UvPoint> samples = sampleEllipsePcurve(ellipse, start, end);
                if (!samples.isEmpty() && (best == null || score < bestScore)) {
                    best = samples;
                    bestScore = score;
                }
                continue;
            }
            if (built instanceof TrimmedCurve2 trimmed) {
                List<UvPoint> samples = sampleTrimmedPcurve(trimmed, projectedStart, projectedEnd);
                if (!samples.isEmpty()) {
                    double score = distanceSquared(projectedStart, samples.getFirst()) + distanceSquared(projectedEnd, samples.getLast());
                    if (best == null || score < bestScore) {
                        best = samples;
                        bestScore = score;
                    }
                }
            }
        }
        return best;
    }

    private static List<StepPcurve> matchingPcurves(List<StepEntity> associatedGeometry, StepEntity faceGeometry) {
        List<StepPcurve> matches = new ArrayList<>();
        for (StepEntity associated : associatedGeometry) {
            if (associated instanceof StepPcurve pcurve && pcurve.basisSurface().id() == faceGeometry.id()) {
                matches.add(pcurve);
            }
        }
        return List.copyOf(matches);
    }

    private static UvPoint snapToLine(UvPoint point, Line2 line) {
        com.minicad.geometry2d.Point2 snapped = line.closestPoint(new com.minicad.geometry2d.Point2(point.u(), point.v()));
        return new UvPoint(snapped.x(), snapped.y());
    }

    private static UvPoint snapToCircle(UvPoint point, Circle2 circle) {
        com.minicad.geometry2d.Vector2 offset = new com.minicad.geometry2d.Point2(point.u(), point.v()).subtract(circle.center());
        double norm = offset.norm();
        if (norm <= Epsilon.EPS) {
            com.minicad.geometry2d.Point2 fallback = circle.pointAt(0.0);
            return new UvPoint(fallback.x(), fallback.y());
        }
        com.minicad.geometry2d.Point2 snapped = circle.center().add(offset.scale(circle.radius() / norm));
        return new UvPoint(snapped.x(), snapped.y());
    }

    private static UvPoint snapToEllipse(UvPoint point, Ellipse2 ellipse) {
        double angle = ellipse.angleOf(ellipse.pointAt(ellipse.angleOf(snapEllipseSeed(point, ellipse))));
        com.minicad.geometry2d.Point2 snapped = ellipse.pointAt(angle);
        return new UvPoint(snapped.x(), snapped.y());
    }

    private static List<UvPoint> sampleLinePcurve(Line2 line, UvPoint start, UvPoint end) {
        com.minicad.geometry2d.Point2 startPoint = new com.minicad.geometry2d.Point2(start.u(), start.v());
        com.minicad.geometry2d.Point2 endPoint = new com.minicad.geometry2d.Point2(end.u(), end.v());
        double startParameter = line.parameterOf(startPoint);
        double endParameter = line.parameterOf(endPoint);
        int segments = Math.max(8, (int) Math.ceil(Math.abs(endParameter - startParameter) * 4.0));
        List<UvPoint> points = new ArrayList<>(segments + 1);
        for (int index = 0; index <= segments; index++) {
            double parameter = startParameter + (endParameter - startParameter) * index / segments;
            com.minicad.geometry2d.Point2 point = line.pointAt(parameter);
            points.add(new UvPoint(point.x(), point.y()));
        }
        points.set(0, start);
        points.set(points.size() - 1, end);
        return List.copyOf(points);
    }

    private static List<UvPoint> sampleSplinePcurve(BSplineCurve2 spline, UvPoint projectedStart, UvPoint projectedEnd) {
        List<com.minicad.geometry2d.Point2> sampled = spline.sample(48);
        if (sampled.size() < 2) {
            return List.of();
        }
        int startIndex = closestPointIndex(sampled, projectedStart);
        int endIndex = closestPointIndex(sampled, projectedEnd);
        if (startIndex == endIndex) {
            return List.of(projectedStart, projectedEnd);
        }
        List<UvPoint> points = new ArrayList<>();
        int step = startIndex <= endIndex ? 1 : -1;
        for (int index = startIndex; index != endIndex + step; index += step) {
            com.minicad.geometry2d.Point2 point = sampled.get(index);
            points.add(new UvPoint(point.x(), point.y()));
        }
        points.set(0, projectedStart);
        points.set(points.size() - 1, projectedEnd);
        return List.copyOf(points);
    }

    private static List<UvPoint> sampleCirclePcurve(Circle2 circle, UvPoint start, UvPoint end) {
        com.minicad.geometry2d.Point2 startPoint = new com.minicad.geometry2d.Point2(start.u(), start.v());
        com.minicad.geometry2d.Point2 endPoint = new com.minicad.geometry2d.Point2(end.u(), end.v());
        double startAngle = circle.angleOf(startPoint);
        double endAngle = circle.angleOf(endPoint);
        double delta = endAngle - startAngle;
        if (delta > Math.PI) {
            delta -= Math.PI * 2.0;
        } else if (delta < -Math.PI) {
            delta += Math.PI * 2.0;
        }
        int segments = Math.max(12, (int) Math.ceil(Math.abs(delta) * 12.0));
        List<UvPoint> points = new ArrayList<>(segments + 1);
        for (int index = 0; index <= segments; index++) {
            double angle = startAngle + delta * index / segments;
            com.minicad.geometry2d.Point2 point = circle.pointAt(angle);
            points.add(new UvPoint(point.x(), point.y()));
        }
        points.set(0, start);
        points.set(points.size() - 1, end);
        return List.copyOf(points);
    }

    private static List<UvPoint> sampleEllipsePcurve(Ellipse2 ellipse, UvPoint start, UvPoint end) {
        com.minicad.geometry2d.Point2 startPoint = new com.minicad.geometry2d.Point2(start.u(), start.v());
        com.minicad.geometry2d.Point2 endPoint = new com.minicad.geometry2d.Point2(end.u(), end.v());
        double startAngle = ellipse.angleOf(startPoint);
        double endAngle = ellipse.angleOf(endPoint);
        double delta = endAngle - startAngle;
        if (delta > Math.PI) {
            delta -= Math.PI * 2.0;
        } else if (delta < -Math.PI) {
            delta += Math.PI * 2.0;
        }
        int segments = Math.max(12, (int) Math.ceil(Math.abs(delta) * 12.0));
        List<UvPoint> points = new ArrayList<>(segments + 1);
        for (int index = 0; index <= segments; index++) {
            double angle = startAngle + delta * index / segments;
            com.minicad.geometry2d.Point2 point = ellipse.pointAt(angle);
            points.add(new UvPoint(point.x(), point.y()));
        }
        points.set(0, start);
        points.set(points.size() - 1, end);
        return List.copyOf(points);
    }

    private static List<UvPoint> sampleTrimmedPcurve(TrimmedCurve2 trimmed, UvPoint projectedStart, UvPoint projectedEnd) {
        UvPoint trimStart = new UvPoint(trimmed.trimStart().x(), trimmed.trimStart().y());
        UvPoint trimEnd = new UvPoint(trimmed.trimEnd().x(), trimmed.trimEnd().y());
        List<UvPoint> forward = sampleCurve2(trimmed.basisCurve(), trimStart, trimEnd);
        List<UvPoint> reverse = sampleCurve2(trimmed.basisCurve(), trimEnd, trimStart);
        if (forward.isEmpty() && reverse.isEmpty()) {
            return List.of();
        }
        List<UvPoint> preferred;
        if (!trimmed.senseAgreement()) {
            preferred = reverse.isEmpty() ? forward : reverse;
        } else {
            preferred = score(projectedStart, projectedEnd, forward) <= score(projectedStart, projectedEnd, reverse)
                    ? forward
                    : reverse;
        }
        return alignTrimmedSamples(preferred, projectedStart, projectedEnd);
    }

    private static List<UvPoint> sampleCurve2(com.minicad.geometry2d.Curve2 curve, UvPoint start, UvPoint end) {
        if (curve instanceof Line2 line) {
            return sampleLinePcurve(line, start, end);
        }
        if (curve instanceof Circle2 circle) {
            return sampleCirclePcurve(circle, start, end);
        }
        if (curve instanceof Ellipse2 ellipse) {
            return sampleEllipsePcurve(ellipse, start, end);
        }
        if (curve instanceof BSplineCurve2 spline) {
            return sampleSplinePcurve(spline, start, end);
        }
        if (curve instanceof TrimmedCurve2 trimmed) {
            return sampleTrimmedPcurve(trimmed, start, end);
        }
        return List.of();
    }

    private static double score(UvPoint start, UvPoint end, List<UvPoint> samples) {
        if (samples.isEmpty()) {
            return Double.POSITIVE_INFINITY;
        }
        return distanceSquared(start, samples.getFirst()) + distanceSquared(end, samples.getLast());
    }

    private static List<UvPoint> alignTrimmedSamples(List<UvPoint> samples, UvPoint projectedStart, UvPoint projectedEnd) {
        if (samples.isEmpty()) {
            return samples;
        }
        List<UvPoint> aligned = new ArrayList<>(samples);
        double forwardScore = distanceSquared(projectedStart, aligned.getFirst()) + distanceSquared(projectedEnd, aligned.getLast());
        double reverseScore = distanceSquared(projectedStart, aligned.getLast()) + distanceSquared(projectedEnd, aligned.getFirst());
        if (reverseScore < forwardScore) {
            java.util.Collections.reverse(aligned);
        }
        aligned.set(0, projectedStart);
        aligned.set(aligned.size() - 1, projectedEnd);
        return List.copyOf(aligned);
    }

    private static com.minicad.geometry2d.Point2 snapEllipseSeed(UvPoint point, Ellipse2 ellipse) {
        com.minicad.geometry2d.Vector2 offset = new com.minicad.geometry2d.Point2(point.u(), point.v()).subtract(ellipse.center());
        if (offset.norm() <= Epsilon.EPS) {
            return ellipse.pointAt(0.0);
        }
        com.minicad.geometry2d.Vector2 x = ellipse.xDirection().asVector();
        com.minicad.geometry2d.Vector2 y = new com.minicad.geometry2d.Vector2(-x.y(), x.x());
        double nx = offset.dot(x) / ellipse.semiAxis1();
        double ny = offset.dot(y) / ellipse.semiAxis2();
        double norm = Math.hypot(nx, ny);
        if (norm <= Epsilon.EPS) {
            return ellipse.pointAt(0.0);
        }
        double angle = Math.atan2(ny / norm, nx / norm);
        return ellipse.pointAt(angle);
    }

    private static List<LoopPayload> toParametricLoopPayloads(List<ParametricLoopPayload> loops, ParametricSurfaceMapper mapper) {
        List<LoopPayload> payloads = new ArrayList<>(loops.size());
        for (ParametricLoopPayload loop : loops) {
            List<PointPayload> points = new ArrayList<>(loop.points().size());
            for (UvPoint point : loop.points()) {
                points.add(toPointPayload(mapper.pointAt(point.u(), point.v())));
            }
            payloads.add(new LoopPayload(loop.outer(), List.copyOf(points)));
        }
        return List.copyOf(payloads);
    }

    private static int closestPointIndex(List<com.minicad.geometry2d.Point2> points, UvPoint target) {
        int bestIndex = 0;
        double bestDistance = Double.POSITIVE_INFINITY;
        for (int index = 0; index < points.size(); index++) {
            com.minicad.geometry2d.Point2 point = points.get(index);
            double du = point.x() - target.u();
            double dv = point.y() - target.v();
            double distance = du * du + dv * dv;
            if (distance < bestDistance) {
                bestDistance = distance;
                bestIndex = index;
            }
        }
        return bestIndex;
    }

    private static boolean sameUv(UvPoint left, UvPoint right) {
        return distanceSquared(left, right) <= 1.0e-12;
    }

    private static double distanceSquared(UvPoint left, UvPoint right) {
        double du = left.u() - right.u();
        double dv = left.v() - right.v();
        return du * du + dv * dv;
    }

    private static List<PointPayload> triangulateParametricFace(
            ParametricSurfaceMapper mapper,
            List<ParametricLoopPayload> loops,
            UvBounds bounds,
            int uSegments,
            int vSegments,
            boolean sameSense
    ) {
        ParametricLoopPayload outer = loops.stream().filter(ParametricLoopPayload::outer).findFirst().orElse(null);
        if (outer == null) {
            return List.of();
        }
        List<ParametricLoopPayload> holes = loops.stream().filter(loop -> !loop.outer()).toList();
        List<PointPayload> triangles = new ArrayList<>();
        for (int ui = 0; ui < uSegments; ui++) {
            double u0 = bounds.minU() + bounds.uSpan() * ui / uSegments;
            double u1 = bounds.minU() + bounds.uSpan() * (ui + 1) / uSegments;
            for (int vi = 0; vi < vSegments; vi++) {
                double v0 = bounds.minV() + bounds.vSpan() * vi / vSegments;
                double v1 = bounds.minV() + bounds.vSpan() * (vi + 1) / vSegments;
                UvPoint center = new UvPoint((u0 + u1) * 0.5, (v0 + v1) * 0.5);
                if (!contains(outer.points(), center)) {
                    continue;
                }
                boolean insideHole = false;
                for (ParametricLoopPayload hole : holes) {
                    if (contains(hole.points(), center)) {
                        insideHole = true;
                        break;
                    }
                }
                if (insideHole) {
                    continue;
                }
                CartesianPoint p00 = mapper.pointAt(u0, v0);
                CartesianPoint p10 = mapper.pointAt(u1, v0);
                CartesianPoint p01 = mapper.pointAt(u0, v1);
                CartesianPoint p11 = mapper.pointAt(u1, v1);
                Vector3 normal = mapper.normalAt(center.u(), center.v());
                if (!sameSense) {
                    normal = normal.scale(-1.0);
                }
                appendOrientedTriangle(triangles, p00, p10, p11, normal);
                appendOrientedTriangle(triangles, p00, p11, p01, normal);
            }
        }
        return List.copyOf(triangles);
    }

    private static List<PointPayload> triangulateParametricFaceAdaptive(
            ParametricSurfaceMapper mapper,
            List<ParametricLoopPayload> loops,
            UvBounds bounds,
            int baseUSegments,
            int baseVSegments,
            boolean sameSense
    ) {
        int uSegments = baseUSegments;
        int vSegments = baseVSegments;
        for (int attempt = 0; attempt < 4; attempt++) {
            List<PointPayload> triangles = triangulateParametricFace(mapper, loops, bounds, uSegments, vSegments, sameSense);
            if (!triangles.isEmpty()) {
                return triangles;
            }
            uSegments = Math.min(uSegments * 2, 288);
            vSegments = Math.min(vSegments * 2, 192);
        }
        return List.of();
    }

    private static boolean contains(List<UvPoint> polygon, UvPoint point) {
        if (polygon.size() < 3) {
            return false;
        }
        if (isOnPolygonBoundary(polygon, point)) {
            return true;
        }
        boolean inside = false;
        for (int i = 0, j = polygon.size() - 1; i < polygon.size(); j = i++) {
            UvPoint a = polygon.get(i);
            UvPoint b = polygon.get(j);
            boolean intersects = ((a.v() > point.v()) != (b.v() > point.v()))
                    && (point.u() < (b.u() - a.u()) * (point.v() - a.v()) / ((b.v() - a.v()) + 1.0e-12) + a.u());
            if (intersects) {
                inside = !inside;
            }
        }
        return inside;
    }

    private static boolean isOnPolygonBoundary(List<UvPoint> polygon, UvPoint point) {
        for (int index = 0; index + 1 < polygon.size(); index++) {
            if (isOnSegment(polygon.get(index), polygon.get(index + 1), point)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isOnSegment(UvPoint a, UvPoint b, UvPoint point) {
        double abU = b.u() - a.u();
        double abV = b.v() - a.v();
        double lengthSquared = abU * abU + abV * abV;
        if (lengthSquared <= 1.0e-18) {
            return distanceSquared(a, point) <= 1.0e-18;
        }
        double apU = point.u() - a.u();
        double apV = point.v() - a.v();
        double cross = abU * apV - abV * apU;
        if (Math.abs(cross) > 1.0e-9) {
            return false;
        }
        double dot = apU * abU + apV * abV;
        if (dot < -1.0e-9) {
            return false;
        }
        return dot <= lengthSquared + 1.0e-9;
    }

    private static ParametricSurfaceMapper mapperForSurface(StepEntity geometry, StepCadBuilder builder) {
        if (geometry instanceof StepCylindricalSurface cylindricalSurface) {
            CylindricalSurface surface = builder.buildCylindricalSurface(cylindricalSurface.id());
            return new ParametricSurfaceMapper() {
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    double u = unwrapPeriodic(cylindricalAngle(surface, point), previous == null ? null : previous.u(), Math.PI * 2.0);
                    return new UvPoint(u, axialHeight(surface, point));
                }

                @Override
                public CartesianPoint pointAt(double u, double v) {
                    return surfacePoint(surface, u, v);
                }

                @Override
                public Vector3 normalAt(double u, double v) {
                    return cylindricalNormal(surface, u, true);
                }

                @Override
                public Double uPeriod() {
                    return Math.PI * 2.0;
                }
            };
        }
        if (geometry instanceof StepConicalSurface conicalSurface) {
            ConicalSurface surface = builder.buildConicalSurface(conicalSurface.id());
            return new ParametricSurfaceMapper() {
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    double u = unwrapPeriodic(cylindricalAngle(surface.position(), point), previous == null ? null : previous.u(), Math.PI * 2.0);
                    return new UvPoint(u, axialHeight(surface.position(), point));
                }

                @Override
                public CartesianPoint pointAt(double u, double v) {
                    return conicalSurfacePoint(surface, u, v);
                }

                @Override
                public Vector3 normalAt(double u, double v) {
                    return conicalNormal(surface, u, true);
                }

                @Override
                public Double uPeriod() {
                    return Math.PI * 2.0;
                }
            };
        }
        if (geometry instanceof StepToroidalSurface toroidalSurface) {
            ToroidalSurface surface = builder.buildToroidalSurface(toroidalSurface.id());
            return new ParametricSurfaceMapper() {
                @Override
                public UvPoint project(CartesianPoint point, UvPoint previous) {
                    Double previousU = previous == null ? null : previous.u();
                    Double previousV = previous == null ? null : previous.v();
                    double u = unwrapPeriodic(toroidalU(surface, point), previousU, Math.PI * 2.0);
                    double v = unwrapPeriodic(toroidalV(surface, point), previousV, Math.PI * 2.0);
                    return new UvPoint(u, v);
                }

                @Override
                public CartesianPoint pointAt(double u, double v) {
                    return toroidalSurfacePoint(surface, u, v);
                }

                @Override
                public Vector3 normalAt(double u, double v) {
                    return toroidalNormal(surface, u, v, true);
                }

                @Override
                public Double uPeriod() {
                    return Math.PI * 2.0;
                }

                @Override
                public Double vPeriod() {
                    return Math.PI * 2.0;
                }
            };
        }
        return null;
    }

    private static double unwrapPeriodic(double value, Double previous, double period) {
        if (previous == null) {
            return value;
        }
        while (value - previous > period * 0.5) {
            value -= period;
        }
        while (value - previous < -period * 0.5) {
            value += period;
        }
        return value;
    }

    private static SurfacePatch buildFourSidedPatch(EdgeLoop outerLoop) {
        if (outerLoop.edges().size() != 4) {
            return null;
        }
        List<CartesianPoint> bottom = sampleOrientedEdge(outerLoop.edges().get(0));
        List<CartesianPoint> right = sampleOrientedEdge(outerLoop.edges().get(1));
        List<CartesianPoint> top = reversed(sampleOrientedEdge(outerLoop.edges().get(2)));
        List<CartesianPoint> left = reversed(sampleOrientedEdge(outerLoop.edges().get(3)));
        if (!cornersMatch(bottom, right, top, left)) {
            return null;
        }
        int uSegments = Math.max(Math.max(bottom.size(), top.size()) - 1, 8);
        int vSegments = Math.max(Math.max(left.size(), right.size()) - 1, 8);
        return new SurfacePatch(
                resamplePolyline(bottom, uSegments),
                resamplePolyline(top, uSegments),
                resamplePolyline(left, vSegments),
                resamplePolyline(right, vSegments)
        );
    }

    private static boolean cornersMatch(
            List<CartesianPoint> bottom,
            List<CartesianPoint> right,
            List<CartesianPoint> top,
            List<CartesianPoint> left
    ) {
        return close(bottom.getFirst(), left.getFirst())
                && close(bottom.getLast(), right.getFirst())
                && close(top.getFirst(), left.getLast())
                && close(top.getLast(), right.getLast());
    }

    private static boolean close(CartesianPoint left, CartesianPoint right) {
        return left.distanceTo(right) <= 1.0e-6;
    }

    private static List<CartesianPoint> reversed(List<CartesianPoint> points) {
        List<CartesianPoint> copy = new ArrayList<>(points);
        java.util.Collections.reverse(copy);
        return List.copyOf(copy);
    }

    private static List<CartesianPoint> resamplePolyline(List<CartesianPoint> points, int segments) {
        if (points.size() < 2) {
            return List.of(points.getFirst());
        }
        List<Double> lengths = new ArrayList<>(points.size());
        lengths.add(0.0);
        for (int i = 1; i < points.size(); i++) {
            lengths.add(lengths.get(i - 1) + points.get(i - 1).distanceTo(points.get(i)));
        }
        double total = lengths.getLast();
        if (total <= Epsilon.EPS) {
            return java.util.Collections.nCopies(segments + 1, points.getFirst());
        }
        List<CartesianPoint> result = new ArrayList<>(segments + 1);
        for (int i = 0; i <= segments; i++) {
            double target = total * i / segments;
            result.add(pointAtDistance(points, lengths, target));
        }
        result.set(0, points.getFirst());
        result.set(result.size() - 1, points.getLast());
        return List.copyOf(result);
    }

    private static CartesianPoint pointAtDistance(List<CartesianPoint> points, List<Double> lengths, double target) {
        for (int i = 1; i < lengths.size(); i++) {
            if (target <= lengths.get(i)) {
                double start = lengths.get(i - 1);
                double segment = lengths.get(i) - start;
                double alpha = segment <= Epsilon.EPS ? 0.0 : (target - start) / segment;
                return interpolate(points.get(i - 1), points.get(i), alpha);
            }
        }
        return points.getLast();
    }

    private static CartesianPoint interpolate(CartesianPoint a, CartesianPoint b, double alpha) {
        return new CartesianPoint(
                a.x() * (1.0 - alpha) + b.x() * alpha,
                a.y() * (1.0 - alpha) + b.y() * alpha,
                a.z() * (1.0 - alpha) + b.z() * alpha
        );
    }

    private static void appendOrientedTriangle(
            List<PointPayload> triangles,
            CartesianPoint a,
            CartesianPoint b,
            CartesianPoint c,
            Vector3 targetNormal
    ) {
        Vector3 normal = b.subtract(a).cross(c.subtract(a));
        if (normal.dot(targetNormal) < 0.0) {
            triangles.add(toPointPayload(a));
            triangles.add(toPointPayload(c));
            triangles.add(toPointPayload(b));
            return;
        }
        triangles.add(toPointPayload(a));
        triangles.add(toPointPayload(b));
        triangles.add(toPointPayload(c));
    }

    private static List<Double> unwrapAngles(CylindricalSurface surface, List<CartesianPoint> points) {
        return unwrapAngles(surface.position(), points);
    }

    private static List<Double> unwrapAngles(Axis2Placement3D placement, List<CartesianPoint> points) {
        List<Double> angles = new ArrayList<>(points.size());
        for (CartesianPoint point : points) {
            double angle = cylindricalAngle(placement, point);
            if (!angles.isEmpty()) {
                double previous = angles.getLast();
                while (angle - previous > Math.PI) {
                    angle -= Math.PI * 2.0;
                }
                while (angle - previous < -Math.PI) {
                    angle += Math.PI * 2.0;
                }
            }
            angles.add(angle);
        }
        return List.copyOf(angles);
    }

    private static double averageAxialHeight(CylindricalSurface surface, List<CartesianPoint> points) {
        return averageAxialHeight(surface.position(), points);
    }

    private static double averageAxialHeight(Axis2Placement3D placement, List<CartesianPoint> points) {
        double total = 0.0;
        for (CartesianPoint point : points) {
            total += axialHeight(placement, point);
        }
        return total / points.size();
    }

    private static double axialHeight(CylindricalSurface surface, CartesianPoint point) {
        return axialHeight(surface.position(), point);
    }

    private static double axialHeight(Axis2Placement3D placement, CartesianPoint point) {
        return point.subtract(placement.location()).dot(placement.axis().asVector());
    }

    private static double cylindricalAngle(CylindricalSurface surface, CartesianPoint point) {
        return cylindricalAngle(surface.position(), point);
    }

    private static double cylindricalAngle(Axis2Placement3D placement, CartesianPoint point) {
        Vector3 offset = point.subtract(placement.location());
        double x = offset.dot(placement.xDirection().asVector());
        double y = offset.dot(placement.yDirection().asVector());
        return Math.atan2(y, x);
    }

    private static CartesianPoint surfacePoint(CylindricalSurface surface, double angle, double height) {
        Axis2Placement3D placement = surface.position();
        Vector3 radial = placement.xDirection().asVector().scale(Math.cos(angle) * surface.radius())
                .add(placement.yDirection().asVector().scale(Math.sin(angle) * surface.radius()));
        Vector3 axial = placement.axis().asVector().scale(height);
        return placement.location().add(radial.add(axial));
    }

    private static Vector3 cylindricalNormal(CylindricalSurface surface, double angle, boolean sameSense) {
        Axis2Placement3D placement = surface.position();
        Vector3 radial = placement.xDirection().asVector().scale(Math.cos(angle))
                .add(placement.yDirection().asVector().scale(Math.sin(angle)));
        return sameSense ? radial : radial.scale(-1.0);
    }

    private static CartesianPoint conicalSurfacePoint(ConicalSurface surface, double angle, double height) {
        Axis2Placement3D placement = surface.position();
        double radius = surface.radius() + height * Math.tan(surface.semiAngle());
        Vector3 radial = placement.xDirection().asVector().scale(Math.cos(angle) * radius)
                .add(placement.yDirection().asVector().scale(Math.sin(angle) * radius));
        Vector3 axial = placement.axis().asVector().scale(height);
        return placement.location().add(radial.add(axial));
    }

    private static Vector3 conicalNormal(ConicalSurface surface, double angle, boolean sameSense) {
        Axis2Placement3D placement = surface.position();
        double slope = Math.tan(surface.semiAngle());
        Vector3 radial = placement.xDirection().asVector().scale(Math.cos(angle))
                .add(placement.yDirection().asVector().scale(Math.sin(angle)));
        Vector3 normal = radial.subtract(placement.axis().asVector().scale(slope));
        return sameSense ? normal.normalize().asVector() : normal.normalize().reverse().asVector();
    }

    private static CartesianPoint toroidalSurfacePoint(ToroidalSurface surface, double u, double v) {
        Axis2Placement3D placement = surface.position();
        double radial = surface.majorRadius() + surface.minorRadius() * Math.cos(v);
        Vector3 xy = placement.xDirection().asVector().scale(Math.cos(u) * radial)
                .add(placement.yDirection().asVector().scale(Math.sin(u) * radial));
        Vector3 z = placement.axis().asVector().scale(surface.minorRadius() * Math.sin(v));
        return placement.location().add(xy.add(z));
    }

    private static Vector3 toroidalNormal(ToroidalSurface surface, double u, double v, boolean sameSense) {
        Axis2Placement3D placement = surface.position();
        Vector3 normal = placement.xDirection().asVector().scale(Math.cos(u) * Math.cos(v))
                .add(placement.yDirection().asVector().scale(Math.sin(u) * Math.cos(v)))
                .add(placement.axis().asVector().scale(Math.sin(v)));
        return sameSense ? normal.normalize().asVector() : normal.normalize().reverse().asVector();
    }

    private static List<Double> unwrapToroidalU(ToroidalSurface surface, List<CartesianPoint> points) {
        List<Double> values = new ArrayList<>(points.size());
        for (CartesianPoint point : points) {
            double value = toroidalU(surface, point);
            if (!values.isEmpty()) {
                double previous = values.getLast();
                while (value - previous > Math.PI) {
                    value -= Math.PI * 2.0;
                }
                while (value - previous < -Math.PI) {
                    value += Math.PI * 2.0;
                }
            }
            values.add(value);
        }
        return List.copyOf(values);
    }

    private static List<Double> unwrapToroidalV(ToroidalSurface surface, List<CartesianPoint> points) {
        List<Double> values = new ArrayList<>(points.size());
        for (CartesianPoint point : points) {
            double value = toroidalV(surface, point);
            if (!values.isEmpty()) {
                double previous = values.getLast();
                while (value - previous > Math.PI) {
                    value -= Math.PI * 2.0;
                }
                while (value - previous < -Math.PI) {
                    value += Math.PI * 2.0;
                }
            }
            values.add(value);
        }
        return List.copyOf(values);
    }

    private static double averageToroidalV(ToroidalSurface surface, List<CartesianPoint> points) {
        double total = 0.0;
        for (CartesianPoint point : points) {
            total += toroidalV(surface, point);
        }
        return total / points.size();
    }

    private static double toroidalU(ToroidalSurface surface, CartesianPoint point) {
        Axis2Placement3D placement = surface.position();
        Vector3 offset = point.subtract(placement.location());
        double x = offset.dot(placement.xDirection().asVector());
        double y = offset.dot(placement.yDirection().asVector());
        return Math.atan2(y, x);
    }

    private static double toroidalV(ToroidalSurface surface, CartesianPoint point) {
        Axis2Placement3D placement = surface.position();
        Vector3 offset = point.subtract(placement.location());
        double x = offset.dot(placement.xDirection().asVector());
        double y = offset.dot(placement.yDirection().asVector());
        double z = offset.dot(placement.axis().asVector());
        double rho = Math.sqrt(x * x + y * y);
        return Math.atan2(z, rho - surface.majorRadius());
    }

    private static List<FaceBound> buildFaceBounds(StepFaceEntity stepFace, StepCadBuilder builder) {
        return stepFace.bounds().stream().map(bound -> builder.buildFaceBound(bound.id())).toList();
    }

    private static StepEntity faceGeometry(StepFaceEntity stepFace) {
        if (stepFace instanceof StepAdvancedFace advancedFace) {
            return advancedFace.faceGeometry();
        }
        if (stepFace instanceof StepFaceSurface faceSurface) {
            return faceSurface.faceGeometry();
        }
        if (stepFace instanceof StepOrientedFace orientedFace) {
            return faceGeometry(orientedFace.faceElement());
        }
        return null;
    }

    private static String surfaceTypeName(StepEntity geometry) {
        if (geometry instanceof StepCylindricalSurface) {
            return "CYLINDRICAL_SURFACE";
        }
        if (geometry instanceof StepConicalSurface) {
            return "CONICAL_SURFACE";
        }
        if (geometry instanceof StepToroidalSurface) {
            return "TOROIDAL_SURFACE";
        }
        if (geometry instanceof StepBSplineSurfaceWithKnots) {
            return "B_SPLINE_SURFACE_WITH_KNOTS";
        }
        return geometry.getClass().getSimpleName();
    }

    private static boolean faceSameSense(StepFaceEntity stepFace) {
        if (stepFace instanceof StepAdvancedFace advancedFace) {
            return advancedFace.sameSense();
        }
        if (stepFace instanceof StepFaceSurface faceSurface) {
            return faceSurface.sameSense();
        }
        if (stepFace instanceof StepOrientedFace orientedFace) {
            boolean base = faceSameSense(orientedFace.faceElement());
            return orientedFace.orientation() ? base : !base;
        }
        return true;
    }

    private static FacePayload reverseFacePayload(FacePayload base) {
        List<PointPayload> reversedTriangles = new ArrayList<>(base.triangles().size());
        for (int index = 0; index + 2 < base.triangles().size(); index += 3) {
            reversedTriangles.add(base.triangles().get(index));
            reversedTriangles.add(base.triangles().get(index + 2));
            reversedTriangles.add(base.triangles().get(index + 1));
        }
        return new FacePayload(
                base.stepId(),
                base.name(),
                base.surfaceType(),
                base.origin(),
                new VectorPayload(-base.normal().x(), -base.normal().y(), -base.normal().z()),
                !base.sameSense(),
                base.color(),
                base.layers(),
                base.loops(),
                List.copyOf(reversedTriangles)
        );
    }

    private static List<CartesianPoint> sampleLoop(FaceBound bound) {
        if (bound.loop() instanceof VertexLoop vertexLoop) {
            return List.of(vertexLoop.vertex().point());
        }
        if (!(bound.loop() instanceof EdgeLoop edgeLoop)) {
            throw new UnsupportedGeometryException("preview export requires EDGE_LOOP or VERTEX_LOOP");
        }
        List<CartesianPoint> sampled = new ArrayList<>();
        boolean firstEdge = true;
        for (OrientedEdge orientedEdge : edgeLoop.edges()) {
            List<CartesianPoint> edgePoints = sampleOrientedEdge(orientedEdge);
            int startIndex = firstEdge ? 0 : 1;
            for (int i = startIndex; i < edgePoints.size(); i++) {
                sampled.add(edgePoints.get(i));
            }
            firstEdge = false;
        }
        if (!sampled.isEmpty() && sampled.getFirst().distanceTo(sampled.getLast()) > 1.0e-9) {
            sampled.add(sampled.getFirst());
        }
        return bound.orientation() ? sampled : reverseClosedLoop(sampled);
    }

    private static <T> List<T> reverseClosedLoop(List<T> points) {
        if (points.size() < 2) {
            return points;
        }
        List<T> reversed = new ArrayList<>(points);
        if (reversed.getFirst().equals(reversed.getLast())) {
            T start = reversed.removeLast();
            java.util.Collections.reverse(reversed);
            reversed.add(reversed.getFirst());
            reversed.set(0, start);
            reversed.set(reversed.size() - 1, start);
            return reversed;
        }
        java.util.Collections.reverse(reversed);
        return reversed;
    }

    private static List<CartesianPoint> sampleOrientedEdge(OrientedEdge orientedEdge) {
        Edge edge = orientedEdge.edge();
        boolean naturalForward = orientedEdge.orientation() ? edge.sameSense() : !edge.sameSense();
        return sampleEdge(orientedEdge.startVertex().point(), orientedEdge.endVertex().point(), edge.curve(), naturalForward);
    }

    private static List<CartesianPoint> sampleEdgePreview(
            int edgeId,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder
    ) {
        try {
            Edge edge = builder.buildEdge(edgeId);
            return sampleEdge(edge.start().point(), edge.end().point(), edge.curve(), edge.sameSense());
        } catch (TopologyException ex) {
            StepEdgeCurve edge = (StepEdgeCurve) resolved.get(edgeId);
            CartesianPoint start = pointFromStep(edge.start().point());
            CartesianPoint end = pointFromStep(edge.end().point());
            StepEntity edgeGeometry = edge.edgeGeometry();
            Curve3 curve;
            if (edgeGeometry instanceof StepLine line) {
                curve = builder.buildLine(line.id());
            } else if (edgeGeometry instanceof StepCircle circle) {
                curve = builder.buildCircle(circle.id());
            } else if (edgeGeometry instanceof StepEllipse ellipse) {
                curve = builder.buildEllipse(ellipse.id());
            } else if (edgeGeometry instanceof StepBSplineCurveWithKnots spline) {
                curve = builder.buildBSplineCurve(spline.id());
            } else if (edgeGeometry instanceof StepSurfaceCurve surfaceCurve) {
                curve = builder.buildSurfaceCurve(surfaceCurve.id());
            } else if (edgeGeometry instanceof StepTrimmedCurve trimmedCurve) {
                curve = builder.buildTrimmedCurve(trimmedCurve.id());
            } else {
                throw ex;
            }
            try {
                return sampleEdge(start, end, curve, edge.sameSense());
            } catch (GeometryException geometryException) {
                return List.of(start, end);
            }
        }
    }

    private static List<CartesianPoint> sampleEdge(CartesianPoint start, CartesianPoint end, Curve3 curve, boolean naturalForward) {
        if (curve instanceof TrimmedCurve3 trimmedCurve) {
            return sampleEdge(start, end, trimmedCurve.basisCurve(), naturalForward);
        }
        if (curve instanceof SurfaceCurve3 surfaceCurve) {
            return sampleEdge(start, end, surfaceCurve.curve3d(), naturalForward);
        }
        if (curve instanceof BSplineCurve3 splineCurve) {
            List<CartesianPoint> points = new ArrayList<>(splineCurve.sample(48));
            if (!naturalForward) {
                java.util.Collections.reverse(points);
            }
            points.set(0, start);
            points.set(points.size() - 1, end);
            return List.copyOf(points);
        }
        if (curve instanceof Line3) {
            return List.of(start, end);
        }
        if (curve instanceof Circle circle) {
            return sampleCircleArc(circle, start, end, naturalForward);
        }
        if (curve instanceof Ellipse3 ellipse) {
            return sampleEllipseArc(ellipse, start, end, naturalForward);
        }
        throw new UnsupportedGeometryException("preview export requires LINE, CIRCLE, ELLIPSE, SURFACE_CURVE, SEAM_CURVE or TRIMMED_CURVE topology");
    }

    private static List<CartesianPoint> sampleCircleArc(Circle circle, CartesianPoint start, CartesianPoint end, boolean naturalForward) {
        double startAngle = circle.angleOf(start);
        double endAngle = circle.angleOf(end);
        double delta = endAngle - startAngle;
        if (naturalForward) {
            if (delta < 0.0) {
                delta += Math.PI * 2.0;
            }
        } else if (delta > 0.0) {
            delta -= Math.PI * 2.0;
        }

        int segments = Math.max(8, (int) Math.ceil(Math.abs(delta) / (Math.PI / 12.0)));
        List<CartesianPoint> points = new ArrayList<>(segments + 1);
        for (int i = 0; i <= segments; i++) {
            double angle = startAngle + delta * i / segments;
            points.add(circle.pointAt(angle));
        }
        points.set(0, start);
        points.set(points.size() - 1, end);
        return points;
    }

    private static List<CartesianPoint> sampleEllipseArc(Ellipse3 ellipse, CartesianPoint start, CartesianPoint end, boolean naturalForward) {
        double startAngle = ellipse.angleOf(start);
        double endAngle = ellipse.angleOf(end);
        double delta = endAngle - startAngle;
        if (naturalForward) {
            if (delta < 0.0) {
                delta += Math.PI * 2.0;
            }
        } else if (delta > 0.0) {
            delta -= Math.PI * 2.0;
        }

        int segments = Math.max(12, (int) Math.ceil(Math.abs(delta) / (Math.PI / 18.0)));
        List<CartesianPoint> points = new ArrayList<>(segments + 1);
        for (int i = 0; i <= segments; i++) {
            double angle = startAngle + delta * i / segments;
            points.add(ellipse.pointAt(angle));
        }
        points.set(0, start);
        points.set(points.size() - 1, end);
        return points;
    }

    private static PointPayload toPointPayload(CartesianPoint point) {
        return new PointPayload(point.x(), point.y(), point.z());
    }

    private static List<PointPayload> toPointPayloads(List<CartesianPoint> points) {
        return points.stream().map(StepPreviewJsonExporter::toPointPayload).toList();
    }

    private static void includeGeometry(BoundsAccumulator bounds, GeometryCollection geometry) {
        for (FacePayload face : geometry.faces()) {
            for (LoopPayload loop : face.loops()) {
                for (PointPayload point : loop.points()) {
                    bounds.include(point);
                }
            }
        }
        for (EdgePayload edge : geometry.edges()) {
            for (PointPayload point : edge.points()) {
                bounds.include(point);
            }
        }
    }

    private static void includeAssembly(BoundsAccumulator bounds, AssemblyData assembly) {
        Map<Integer, RepresentationPayload> byId = assembly.representations().stream()
                .collect(Collectors.toMap(RepresentationPayload::id, representation -> representation, (left, right) -> left, LinkedHashMap::new));
        for (InstancePayload instance : assembly.instances()) {
            for (Integer representationId : instance.representationIds()) {
                RepresentationPayload representation = byId.get(representationId);
                if (representation == null) {
                    continue;
                }
                for (FacePayload face : representation.faces()) {
                    for (LoopPayload loop : face.loops()) {
                        for (PointPayload point : loop.points()) {
                            bounds.include(transform(point, instance.worldMatrix()));
                        }
                    }
                }
                for (EdgePayload edge : representation.edges()) {
                    for (PointPayload point : edge.points()) {
                        bounds.include(transform(point, instance.worldMatrix()));
                    }
                }
            }
        }
    }

    private static BoundsAccumulator copyBounds(BoundsAccumulator source) {
        BoundsAccumulator copy = new BoundsAccumulator();
        if (!source.isEmpty()) {
            copy.minX = source.minX;
            copy.minY = source.minY;
            copy.minZ = source.minZ;
            copy.maxX = source.maxX;
            copy.maxY = source.maxY;
            copy.maxZ = source.maxZ;
        }
        return copy;
    }

    private static ValidationPayload buildValidationPayload(
            GeometryCollection legacyGeometry,
            AssemblyData assembly,
            BoundsAccumulator bounds,
            Map<Integer, StepEntity> resolved
    ) {
        GeometrySummary summary = assembly.instances().isEmpty()
                ? summarizeGeometry(legacyGeometry)
                : summarizeAssembly(assembly);
        PointPayload center = bounds.isEmpty()
                ? new PointPayload(0.0, 0.0, 0.0)
                : new PointPayload(
                        (bounds.minX + bounds.maxX) * 0.5,
                        (bounds.minY + bounds.maxY) * 0.5,
                        (bounds.minZ + bounds.maxZ) * 0.5
                );
        double sizeX = bounds.isEmpty() ? 0.0 : bounds.maxX - bounds.minX;
        double sizeY = bounds.isEmpty() ? 0.0 : bounds.maxY - bounds.minY;
        double sizeZ = bounds.isEmpty() ? 0.0 : bounds.maxZ - bounds.minZ;
        return new ValidationPayload(
                assembly.representations().size(),
                assembly.instances().size(),
                summary.faceCount(),
                summary.edgeCount(),
                summary.approxSurfaceArea(),
                summary.approxEdgeLength(),
                center,
                buildValidationReport(
                        resolved,
                        summary,
                        new ValidationContext(
                                assembly.representations().size(),
                                assembly.instances().size(),
                                center,
                                sizeX,
                                sizeY,
                                sizeZ
                        )
                )
        );
    }

    private static GeometrySummary summarizeGeometry(GeometryCollection geometry) {
        return new GeometrySummary(
                geometry.faces().size(),
                geometry.edges().size(),
                approximateSurfaceArea(geometry.faces()),
                approximateEdgeLength(geometry.edges())
        );
    }

    private static GeometrySummary summarizeAssembly(AssemblyData assembly) {
        Map<Integer, RepresentationPayload> byId = assembly.representations().stream()
                .collect(Collectors.toMap(RepresentationPayload::id, representation -> representation, (left, right) -> left, LinkedHashMap::new));
        int faceCount = 0;
        int edgeCount = 0;
        double area = 0.0;
        double edgeLength = 0.0;
        for (InstancePayload instance : assembly.instances()) {
            for (Integer representationId : instance.representationIds()) {
                RepresentationPayload representation = byId.get(representationId);
                if (representation == null) {
                    continue;
                }
                faceCount += representation.faces().size();
                edgeCount += representation.edges().size();
                area += approximateSurfaceArea(transformedFaces(representation.faces(), instance.worldMatrix()));
                edgeLength += approximateEdgeLength(transformedEdges(representation.edges(), instance.worldMatrix()));
            }
        }
        return new GeometrySummary(faceCount, edgeCount, area, edgeLength);
    }

    private static List<FacePayload> transformedFaces(List<FacePayload> faces, double[] matrix) {
        List<FacePayload> transformed = new ArrayList<>(faces.size());
        for (FacePayload face : faces) {
            List<LoopPayload> loops = new ArrayList<>(face.loops().size());
            for (LoopPayload loop : face.loops()) {
                List<PointPayload> points = new ArrayList<>(loop.points().size());
                for (PointPayload point : loop.points()) {
                    points.add(transform(point, matrix));
                }
                loops.add(new LoopPayload(loop.outer(), List.copyOf(points)));
            }
            List<PointPayload> triangles = new ArrayList<>(face.triangles().size());
            for (PointPayload point : face.triangles()) {
                triangles.add(transform(point, matrix));
            }
            transformed.add(new FacePayload(
                    face.stepId(),
                    face.name(),
                    face.surfaceType(),
                    transform(face.origin(), matrix),
                    face.normal(),
                    face.sameSense(),
                    face.color(),
                    face.layers(),
                    List.copyOf(loops),
                    List.copyOf(triangles)
            ));
        }
        return List.copyOf(transformed);
    }

    private static List<EdgePayload> transformedEdges(List<EdgePayload> edges, double[] matrix) {
        List<EdgePayload> transformed = new ArrayList<>(edges.size());
        for (EdgePayload edge : edges) {
            List<PointPayload> points = new ArrayList<>(edge.points().size());
            for (PointPayload point : edge.points()) {
                points.add(transform(point, matrix));
            }
            transformed.add(new EdgePayload(edge.stepId(), List.copyOf(points)));
        }
        return List.copyOf(transformed);
    }

    private static double approximateSurfaceArea(List<FacePayload> faces) {
        double total = 0.0;
        for (FacePayload face : faces) {
            if (!face.triangles().isEmpty()) {
                total += triangleArea(face.triangles());
            } else {
                total += loopArea(face);
            }
        }
        return total;
    }

    private static double approximateEdgeLength(List<EdgePayload> edges) {
        double total = 0.0;
        for (EdgePayload edge : edges) {
            for (int i = 0; i + 1 < edge.points().size(); i++) {
                total += distance(edge.points().get(i), edge.points().get(i + 1));
            }
        }
        return total;
    }

    private static double triangleArea(List<PointPayload> triangles) {
        double total = 0.0;
        for (int i = 0; i + 2 < triangles.size(); i += 3) {
            PointPayload a = triangles.get(i);
            PointPayload b = triangles.get(i + 1);
            PointPayload c = triangles.get(i + 2);
            double abx = b.x() - a.x();
            double aby = b.y() - a.y();
            double abz = b.z() - a.z();
            double acx = c.x() - a.x();
            double acy = c.y() - a.y();
            double acz = c.z() - a.z();
            double cx = aby * acz - abz * acy;
            double cy = abz * acx - abx * acz;
            double cz = abx * acy - aby * acx;
            total += 0.5 * Math.sqrt(cx * cx + cy * cy + cz * cz);
        }
        return total;
    }

    private static double loopArea(FacePayload face) {
        double total = 0.0;
        for (LoopPayload loop : face.loops()) {
            double area = polygonArea(loop.points(), face.normal());
            total += loop.outer() ? area : -area;
        }
        return Math.abs(total);
    }

    private static double polygonArea(List<PointPayload> points, VectorPayload normal) {
        if (points.size() < 3) {
            return 0.0;
        }
        double nx = normal.x();
        double ny = normal.y();
        double nz = normal.z();
        double length = Math.sqrt(nx * nx + ny * ny + nz * nz);
        if (length <= Epsilon.EPS) {
            return 0.0;
        }
        nx /= length;
        ny /= length;
        nz /= length;
        double areaVectorX = 0.0;
        double areaVectorY = 0.0;
        double areaVectorZ = 0.0;
        for (int i = 0; i < points.size(); i++) {
            PointPayload current = points.get(i);
            PointPayload next = points.get((i + 1) % points.size());
            areaVectorX += current.y() * next.z() - current.z() * next.y();
            areaVectorY += current.z() * next.x() - current.x() * next.z();
            areaVectorZ += current.x() * next.y() - current.y() * next.x();
        }
        return Math.abs((areaVectorX * nx + areaVectorY * ny + areaVectorZ * nz) * 0.5);
    }

    private static double distance(PointPayload a, PointPayload b) {
        double dx = b.x() - a.x();
        double dy = b.y() - a.y();
        double dz = b.z() - a.z();
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    private static List<PmiPayload> buildPmiPayloads(Map<Integer, StepEntity> resolved, AssemblyData assembly) {
        Map<Integer, List<PmiTargetPayload>> targetsByUsageId = new LinkedHashMap<>();
        Map<Integer, List<String>> instanceIdsByTargetId = buildInstanceIdsByTargetId(assembly);
        for (StepEntity entity : resolved.values()) {
            if (entity instanceof StepGeometricItemSpecificUsage usage) {
                targetsByUsageId.computeIfAbsent(usage.usage().id(), ignored -> new ArrayList<>()).add(
                        new PmiTargetPayload(
                                usage.identifiedItem().id(),
                                pmiTargetType(usage.identifiedItem()),
                                pmiTargetName(usage.identifiedItem()),
                                List.copyOf(instanceIdsByTargetId.getOrDefault(usage.identifiedItem().id(), List.of()))
                        )
                );
            }
        }
        List<PmiPayload> pmi = new ArrayList<>();
        for (StepEntity entity : resolved.values()) {
            if (entity instanceof StepDraughtingCallout callout) {
                PmiPayload payload = toPmiPayload(callout, targetsByUsageId.getOrDefault(callout.id(), List.of()));
                if (payload != null) {
                    pmi.add(payload);
                }
            } else if (entity instanceof StepAnnotationTextOccurrence textOccurrence) {
                List<PmiTargetPayload> targets = targetsByUsageId.getOrDefault(textOccurrence.id(), List.of());
                pmi.add(new PmiPayload(
                        textOccurrence.name(),
                        textOccurrence.text(),
                        toPointPayload(pointFromStep(textOccurrence.position())),
                        List.of(),
                        targets.stream().map(PmiTargetPayload::id).toList(),
                        targets
                ));
            }
        }
        return List.copyOf(pmi);
    }

    private static Map<Integer, List<String>> buildInstanceIdsByTargetId(AssemblyData assembly) {
        Map<Integer, RepresentationPayload> representationsById = assembly.representations().stream()
                .collect(Collectors.toMap(RepresentationPayload::id, representation -> representation, (left, right) -> left, LinkedHashMap::new));
        Map<Integer, Set<String>> targetInstances = new LinkedHashMap<>();
        for (InstancePayload instance : assembly.instances()) {
            for (Integer representationId : instance.representationIds()) {
                targetInstances.computeIfAbsent(representationId, ignored -> new LinkedHashSet<>()).add(instance.id());
                RepresentationPayload representation = representationsById.get(representationId);
                if (representation == null) {
                    continue;
                }
                for (FacePayload face : representation.faces()) {
                    targetInstances.computeIfAbsent(face.stepId(), ignored -> new LinkedHashSet<>()).add(instance.id());
                }
                for (EdgePayload edge : representation.edges()) {
                    targetInstances.computeIfAbsent(edge.stepId(), ignored -> new LinkedHashSet<>()).add(instance.id());
                }
            }
        }
        Map<Integer, List<String>> byTargetId = new LinkedHashMap<>();
        for (Map.Entry<Integer, Set<String>> entry : targetInstances.entrySet()) {
            byTargetId.put(entry.getKey(), List.copyOf(entry.getValue()));
        }
        return Map.copyOf(byTargetId);
    }

    private static PmiPayload toPmiPayload(StepDraughtingCallout callout, List<PmiTargetPayload> targets) {
        StepAnnotationTextOccurrence text = null;
        List<PointPayload> leader = new ArrayList<>();
        for (StepEntity content : callout.contents()) {
            if (content instanceof StepAnnotationTextOccurrence annotationText) {
                text = annotationText;
            } else if (content instanceof StepGeometricCurveSet curveSet) {
                leader.addAll(samplePmiCurveSet(curveSet));
            }
        }
        if (text == null) {
            return null;
        }
        return new PmiPayload(
                callout.name(),
                text.text(),
                toPointPayload(pointFromStep(text.position())),
                List.copyOf(leader),
                targets.stream().map(PmiTargetPayload::id).toList(),
                List.copyOf(targets)
        );
    }

    private static String pmiTargetType(StepEntity target) {
        if (target instanceof StepFaceEntity) {
            return "face";
        }
        if (target instanceof com.minicad.step.model.StepEdgeCurve) {
            return "edge";
        }
        if (target instanceof StepRepresentation) {
            return "representation";
        }
        return "entity";
    }

    private static String pmiTargetName(StepEntity target) {
        if (target instanceof StepFaceEntity face) {
            return faceDisplayName(face);
        }
        if (target instanceof com.minicad.step.model.StepEdgeCurve edge) {
            return edge.name();
        }
        if (target instanceof StepRepresentation representation) {
            return representation.name();
        }
        return "";
    }

    private static List<PointPayload> samplePmiCurveSet(StepGeometricCurveSet curveSet) {
        List<PointPayload> points = new ArrayList<>();
        for (StepEntity element : curveSet.elements()) {
            if (element instanceof StepCartesianPoint point) {
                points.add(toPointPayload(new CartesianPoint(
                        point.coordinates().get(0),
                        point.coordinates().get(1),
                        point.coordinates().get(2)
                )));
            } else if (element instanceof StepLine
                    || element instanceof StepCircle
                    || element instanceof StepEllipse
                    || element instanceof StepTrimmedCurve
                    || element instanceof StepSurfaceCurve
                    || element instanceof StepBSplineCurveWithKnots) {
                // PMI sampling only uses explicit point leaders in this minimal subset.
            }
        }
        return List.copyOf(points);
    }

    private static CartesianPoint pointFromStep(StepCartesianPoint point) {
        return new CartesianPoint(point.coordinates().get(0), point.coordinates().get(1), point.coordinates().get(2));
    }

    private static void includePmi(BoundsAccumulator bounds, List<PmiPayload> pmi) {
        for (PmiPayload item : pmi) {
            bounds.include(item.position());
            for (PointPayload point : item.leader()) {
                bounds.include(point);
            }
        }
    }

    private static ValidationReportPayload buildValidationReport(
            Map<Integer, StepEntity> resolved,
            GeometrySummary summary,
            ValidationContext context
    ) {
        List<ValidationCheckPayload> checks = new ArrayList<>();
        int okCount = 0;
        int warnCount = 0;
        for (StepEntity entity : resolved.values()) {
            if (!(entity instanceof StepMeasureRepresentationItem item)) {
                continue;
            }
            String propertyId = StepValidationMatcher.matchPropertyId(item.name(), item.measureType());
            Double actual = actualValidationValue(propertyId, summary, context);
            if (actual == null) {
                continue;
            }
            double delta = actual - item.value();
            boolean matches = Math.abs(delta) <= 1.0e-6;
            if (matches) {
                okCount++;
            } else {
                warnCount++;
            }
            checks.add(new ValidationCheckPayload(
                    propertyId,
                    item.name(),
                    item.measureType(),
                    item.value(),
                    actual,
                    delta,
                    matches ? "ok" : "warn",
                    matches
            ));
        }
        return new ValidationReportPayload(
                checks.isEmpty() ? "empty" : warnCount == 0 ? "ok" : "warn",
                okCount,
                warnCount,
                List.copyOf(checks)
        );
    }

    private static Double actualValidationValue(String propertyId, GeometrySummary summary, ValidationContext context) {
        if ("surface_area".equals(propertyId)) {
            return summary.approxSurfaceArea();
        }
        if ("edge_length".equals(propertyId)) {
            return summary.approxEdgeLength();
        }
        if ("center_x".equals(propertyId)) {
            return context.center().x();
        }
        if ("center_y".equals(propertyId)) {
            return context.center().y();
        }
        if ("center_z".equals(propertyId)) {
            return context.center().z();
        }
        if ("bbox_x".equals(propertyId)) {
            return context.sizeX();
        }
        if ("bbox_y".equals(propertyId)) {
            return context.sizeY();
        }
        if ("bbox_z".equals(propertyId)) {
            return context.sizeZ();
        }
        if ("face_count".equals(propertyId)) {
            return (double) summary.faceCount();
        }
        if ("edge_count".equals(propertyId)) {
            return (double) summary.edgeCount();
        }
        if ("representation_count".equals(propertyId)) {
            return (double) context.representationCount();
        }
        if ("instance_count".equals(propertyId)) {
            return (double) context.instanceCount();
        }
        return null;
    }

    private static PointPayload transform(PointPayload point, double[] matrix) {
        double x = point.x();
        double y = point.y();
        double z = point.z();
        return new PointPayload(
                matrix[0] * x + matrix[1] * y + matrix[2] * z + matrix[3],
                matrix[4] * x + matrix[5] * y + matrix[6] * z + matrix[7],
                matrix[8] * x + matrix[9] * y + matrix[10] * z + matrix[11]
        );
    }

    private static String toJson(PreviewPayload payload) {
        StringBuilder json = new StringBuilder(4096);
        json.append('{');
        json.append("\"stats\":");
        appendStats(json, payload.stats());
        json.append(",\"bounds\":");
        appendBounds(json, payload.bounds());
        json.append(",\"validation\":");
        appendValidation(json, payload.validation());
        json.append(",\"pmi\":");
        appendPmi(json, payload.pmi());
        json.append(",\"unsupportedFaces\":");
        appendUnsupportedFaces(json, payload.unsupportedFaces());
        json.append(",\"edges\":");
        appendEdges(json, payload.edges());
        json.append(",\"faces\":");
        appendFaces(json, payload.faces());
        json.append(",\"representations\":");
        appendRepresentations(json, payload.representations());
        json.append(",\"instances\":");
        appendInstances(json, payload.instances());
        json.append('}');
        return json.toString();
    }

    private static void appendStats(StringBuilder json, PreviewStats stats) {
        json.append('{');
        json.append("\"entityCount\":").append(stats.entityCount());
        json.append(",\"solidCount\":").append(stats.solidCount());
        json.append(",\"shellCount\":").append(stats.shellCount());
        json.append(",\"faceCount\":").append(stats.faceCount());
        json.append(",\"edgeCount\":").append(stats.edgeCount());
        json.append(",\"unsupportedFaceCount\":").append(stats.unsupportedFaceCount());
        json.append('}');
    }

    private static void appendBounds(StringBuilder json, BoundsPayload bounds) {
        json.append('{');
        json.append("\"min\":");
        appendPoint(json, bounds.min());
        json.append(",\"max\":");
        appendPoint(json, bounds.max());
        json.append('}');
    }

    private static void appendValidation(StringBuilder json, ValidationPayload validation) {
        json.append('{');
        json.append("\"representationCount\":").append(validation.representationCount());
        json.append(",\"instanceCount\":").append(validation.instanceCount());
        json.append(",\"renderedFaceCount\":").append(validation.renderedFaceCount());
        json.append(",\"renderedEdgeCount\":").append(validation.renderedEdgeCount());
        json.append(",\"approxSurfaceArea\":").append(format(validation.approxSurfaceArea()));
        json.append(",\"approxEdgeLength\":").append(format(validation.approxEdgeLength()));
        json.append(",\"center\":");
        appendPoint(json, validation.center());
        json.append(",\"report\":");
        appendValidationReport(json, validation.report());
        json.append(",\"nativeChecks\":");
        appendValidationChecks(json, validation.report().checks());
        json.append('}');
    }

    private static void appendValidationReport(StringBuilder json, ValidationReportPayload report) {
        json.append('{');
        json.append("\"status\":").append(quote(report.status()));
        json.append(",\"okCount\":").append(report.okCount());
        json.append(",\"warnCount\":").append(report.warnCount());
        json.append(",\"checks\":");
        appendValidationChecks(json, report.checks());
        json.append('}');
    }

    private static void appendValidationChecks(StringBuilder json, List<ValidationCheckPayload> checks) {
        json.append('[');
        for (int i = 0; i < checks.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            ValidationCheckPayload check = checks.get(i);
            json.append('{');
            json.append("\"propertyId\":").append(quote(check.propertyId()));
            json.append(',');
            json.append("\"name\":").append(quote(check.name()));
            json.append(",\"measureType\":").append(quote(check.measureType()));
            json.append(",\"expected\":").append(format(check.expected()));
            json.append(",\"actual\":").append(format(check.actual()));
            json.append(",\"delta\":").append(format(check.delta()));
            json.append(",\"status\":").append(quote(check.status()));
            json.append(",\"matches\":").append(check.matches());
            json.append('}');
        }
        json.append(']');
    }

    private static void appendPmi(StringBuilder json, List<PmiPayload> pmi) {
        json.append('[');
        for (int i = 0; i < pmi.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            PmiPayload item = pmi.get(i);
            json.append('{');
            json.append("\"name\":").append(quote(item.name()));
            json.append(",\"text\":").append(quote(item.text()));
            json.append(",\"position\":");
            appendPoint(json, item.position());
            json.append(",\"leader\":");
            appendPoints(json, item.leader());
            json.append(",\"targetIds\":");
            appendIntegerList(json, item.targetIds());
            json.append(",\"targets\":");
            appendPmiTargets(json, item.targets());
            json.append('}');
        }
        json.append(']');
    }

    private static void appendUnsupportedFaces(StringBuilder json, List<UnsupportedFacePayload> unsupportedFaces) {
        json.append('[');
        for (int i = 0; i < unsupportedFaces.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            UnsupportedFacePayload face = unsupportedFaces.get(i);
            json.append('{');
            json.append("\"id\":").append(face.stepId());
            json.append(",\"name\":").append(quote(face.name()));
            json.append(",\"surfaceType\":").append(quote(face.surfaceType()));
            json.append(",\"reason\":").append(quote(face.reason()));
            json.append('}');
        }
        json.append(']');
    }

    private static void appendPmiTargets(StringBuilder json, List<PmiTargetPayload> targets) {
        json.append('[');
        for (int i = 0; i < targets.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            PmiTargetPayload target = targets.get(i);
            json.append('{');
            json.append("\"id\":").append(target.id());
            json.append(",\"type\":").append(quote(target.type()));
            json.append(",\"name\":").append(quote(target.name()));
            json.append(",\"instanceIds\":");
            appendQuotedList(json, target.instanceIds());
            json.append('}');
        }
        json.append(']');
    }

    private static void appendEdges(StringBuilder json, List<EdgePayload> edges) {
        json.append('[');
        for (int i = 0; i < edges.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            json.append('{');
            json.append("\"id\":").append(edges.get(i).stepId());
            json.append(",\"points\":");
            appendPoints(json, edges.get(i).points());
            json.append('}');
        }
        json.append(']');
    }

    private static void appendFaces(StringBuilder json, List<FacePayload> faces) {
        json.append('[');
        for (int i = 0; i < faces.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            appendFace(json, faces.get(i));
        }
        json.append(']');
    }

    private static void appendRepresentations(StringBuilder json, List<RepresentationPayload> representations) {
        json.append('[');
        for (int i = 0; i < representations.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            RepresentationPayload representation = representations.get(i);
            json.append('{');
            json.append("\"id\":").append(representation.id());
            json.append(",\"name\":").append(quote(representation.name()));
            json.append(",\"layers\":");
            appendStringList(json, representation.layers());
            json.append(",\"color\":");
            appendColor(json, representation.color());
            json.append(",\"edges\":");
            appendEdges(json, representation.edges());
            json.append(",\"faces\":");
            appendFaces(json, representation.faces());
            json.append('}');
        }
        json.append(']');
    }

    private static void appendInstances(StringBuilder json, List<InstancePayload> instances) {
        json.append('[');
        for (int i = 0; i < instances.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            InstancePayload instance = instances.get(i);
            json.append('{');
            json.append("\"id\":").append(quote(instance.id()));
            json.append(",\"parentId\":").append(instance.parentId() == null ? "null" : quote(instance.parentId()));
            json.append(",\"productDefinitionId\":").append(instance.productDefinitionId());
            json.append(",\"occurrenceId\":").append(instance.occurrenceId() == null ? "null" : instance.occurrenceId());
            json.append(",\"representationId\":").append(instance.representationId() == null ? "null" : instance.representationId());
            json.append(",\"representationIds\":");
            appendIntegerList(json, instance.representationIds());
            json.append(",\"label\":").append(quote(instance.label()));
            json.append(",\"description\":").append(quote(instance.description()));
            json.append(",\"localMatrix\":");
            appendMatrix(json, instance.localMatrix());
            json.append(",\"matrix\":");
            appendMatrix(json, instance.worldMatrix());
            json.append(",\"depth\":").append(instance.depth());
            json.append('}');
        }
        json.append(']');
    }

    private static void appendIntegerList(StringBuilder json, List<Integer> values) {
        json.append('[');
        for (int i = 0; i < values.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            json.append(values.get(i));
        }
        json.append(']');
    }

    private static void appendStringList(StringBuilder json, List<String> values) {
        appendQuotedList(json, values);
    }

    private static void appendQuotedList(StringBuilder json, List<String> values) {
        json.append('[');
        for (int i = 0; i < values.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            json.append(quote(values.get(i)));
        }
        json.append(']');
    }

    private static void appendFace(StringBuilder json, FacePayload face) {
        json.append('{');
        json.append("\"id\":").append(face.stepId());
        json.append(",\"name\":").append(quote(face.name()));
        json.append(',');
        json.append("\"surfaceType\":").append(quote(face.surfaceType()));
        json.append(',');
        json.append("\"origin\":");
        appendPoint(json, face.origin());
        json.append(",\"normal\":");
        appendVector(json, face.normal());
        json.append(",\"sameSense\":").append(face.sameSense());
        json.append(",\"color\":");
        appendColor(json, face.color());
        json.append(",\"layers\":");
        appendStringList(json, face.layers());
        json.append(",\"loops\":");
        appendLoops(json, face.loops());
        json.append(",\"triangles\":");
        appendPoints(json, face.triangles());
        json.append('}');
    }

    private static void appendColor(StringBuilder json, ColorPayload color) {
        if (color == null) {
            json.append("null");
            return;
        }
        json.append('[')
                .append(color.red())
                .append(',')
                .append(color.green())
                .append(',')
                .append(color.blue())
                .append(']');
    }

    private static void appendLoops(StringBuilder json, List<LoopPayload> loops) {
        json.append('[');
        for (int i = 0; i < loops.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            LoopPayload loop = loops.get(i);
            json.append('{');
            json.append("\"outer\":").append(loop.outer());
            json.append(",\"points\":");
            appendPoints(json, loop.points());
            json.append('}');
        }
        json.append(']');
    }

    private static void appendPoints(StringBuilder json, List<PointPayload> points) {
        json.append('[');
        for (int i = 0; i < points.size(); i++) {
            if (i > 0) {
                json.append(',');
            }
            appendPoint(json, points.get(i));
        }
        json.append(']');
    }

    private static void appendPoint(StringBuilder json, PointPayload point) {
        json.append('[')
                .append(format(point.x()))
                .append(',')
                .append(format(point.y()))
                .append(',')
                .append(format(point.z()))
                .append(']');
    }

    private static void appendVector(StringBuilder json, VectorPayload vector) {
        json.append('[')
                .append(format(vector.x()))
                .append(',')
                .append(format(vector.y()))
                .append(',')
                .append(format(vector.z()))
                .append(']');
    }

    private static void appendMatrix(StringBuilder json, double[] matrix) {
        json.append('[');
        for (int i = 0; i < matrix.length; i++) {
            if (i > 0) {
                json.append(',');
            }
            json.append(format(matrix[i]));
        }
        json.append(']');
    }

    private static String quote(String text) {
        String escaped = text.replace("\\", "\\\\").replace("\"", "\\\"");
        return "\"" + escaped + "\"";
    }

    private static String format(double value) {
        return Double.toString(value);
    }

    private record PreviewPayload(
            PreviewStats stats,
            BoundsPayload bounds,
            ValidationPayload validation,
            List<PmiPayload> pmi,
            List<UnsupportedFacePayload> unsupportedFaces,
            List<EdgePayload> edges,
            List<FacePayload> faces,
            List<RepresentationPayload> representations,
            List<InstancePayload> instances
    ) {
    }

    private record AssemblyData(List<RepresentationPayload> representations, List<InstancePayload> instances) {
    }

    private record GeometryCollection(
            List<EdgePayload> edges,
            List<FacePayload> faces,
            List<UnsupportedFacePayload> unsupportedFaces
    ) {
    }

    private record PreviewStats(
            int entityCount,
            int solidCount,
            int shellCount,
            int faceCount,
            int edgeCount,
            int unsupportedFaceCount
    ) {
    }

    private record BoundsPayload(PointPayload min, PointPayload max) {
    }

    private record ValidationPayload(
            int representationCount,
            int instanceCount,
            int renderedFaceCount,
            int renderedEdgeCount,
            double approxSurfaceArea,
            double approxEdgeLength,
            PointPayload center,
            ValidationReportPayload report
    ) {
    }

    private record ValidationReportPayload(
            String status,
            int okCount,
            int warnCount,
            List<ValidationCheckPayload> checks
    ) {
    }

    private record ValidationCheckPayload(
            String propertyId,
            String name,
            String measureType,
            double expected,
            double actual,
            double delta,
            String status,
            boolean matches
    ) {
    }

    private record ValidationContext(
            int representationCount,
            int instanceCount,
            PointPayload center,
            double sizeX,
            double sizeY,
            double sizeZ
    ) {
    }

    private record PmiPayload(
            String name,
            String text,
            PointPayload position,
            List<PointPayload> leader,
            List<Integer> targetIds,
            List<PmiTargetPayload> targets
    ) {
    }

    private record PmiTargetPayload(
            int id,
            String type,
            String name,
            List<String> instanceIds
    ) {
    }

    private record RepresentationPayload(
            int id,
            String name,
            List<String> layers,
            ColorPayload color,
            List<EdgePayload> edges,
            List<FacePayload> faces
    ) {
    }

    private record InstancePayload(
            String id,
            String parentId,
            int productDefinitionId,
            Integer occurrenceId,
            Integer representationId,
            List<Integer> representationIds,
            String label,
            String description,
            double[] localMatrix,
            double[] worldMatrix,
            int depth
    ) {
    }

    private interface ParametricSurfaceMapper {
        UvPoint project(CartesianPoint point, UvPoint previous);

        CartesianPoint pointAt(double u, double v);

        Vector3 normalAt(double u, double v);

        default Double uPeriod() {
            return null;
        }

        default Double vPeriod() {
            return null;
        }
    }

    private record UvPoint(double u, double v) {
    }

    private record ParametricLoopPayload(boolean outer, List<UvPoint> points) {
    }

    private record UvBounds(double minU, double minV, double maxU, double maxV) {
        double uSpan() {
            return maxU - minU;
        }

        double vSpan() {
            return maxV - minV;
        }
    }

    private record EdgePayload(int stepId, List<PointPayload> points) {
    }

    private record FacePayload(
            int stepId,
            String name,
            String surfaceType,
            PointPayload origin,
            VectorPayload normal,
            boolean sameSense,
            ColorPayload color,
            List<String> layers,
            List<LoopPayload> loops,
            List<PointPayload> triangles
    ) {
    }

    private record UnsupportedFacePayload(
            int stepId,
            String name,
            String surfaceType,
            String reason
    ) {
    }

    private record PreviewFaceResult(FacePayload face, UnsupportedFacePayload unsupportedFace) {
    }

    private record SurfacePatch(
            List<CartesianPoint> bottom,
            List<CartesianPoint> top,
            List<CartesianPoint> left,
            List<CartesianPoint> right
    ) {

        int uSegments() {
            return bottom.size() - 1;
        }

        int vSegments() {
            return left.size() - 1;
        }

        CartesianPoint pointAt(double u, double v) {
            CartesianPoint c0 = sample(bottom, u);
            CartesianPoint c1 = sample(top, u);
            CartesianPoint d0 = sample(left, v);
            CartesianPoint d1 = sample(right, v);
            CartesianPoint p00 = bottom.getFirst();
            CartesianPoint p10 = bottom.getLast();
            CartesianPoint p01 = top.getFirst();
            CartesianPoint p11 = top.getLast();
            return bilinearBlend(c0, c1, d0, d1, p00, p10, p01, p11, u, v);
        }

        Vector3 normalAt(double u, double v) {
            double du = Math.max(1.0 / Math.max(uSegments(), 1), 1.0e-3);
            double dv = Math.max(1.0 / Math.max(vSegments(), 1), 1.0e-3);
            CartesianPoint p = pointAt(u, v);
            CartesianPoint pu = pointAt(Math.min(1.0, u + du), v);
            CartesianPoint pv = pointAt(u, Math.min(1.0, v + dv));
            Vector3 normal = pu.subtract(p).cross(pv.subtract(p));
            if (normal.norm() <= Epsilon.EPS) {
                return new Vector3(0.0, 0.0, 1.0);
            }
            return normal.normalize().asVector();
        }

        private static CartesianPoint sample(List<CartesianPoint> polyline, double t) {
            double clamped = Math.max(0.0, Math.min(1.0, t));
            double scaled = clamped * (polyline.size() - 1);
            int low = Math.min((int) Math.floor(scaled), polyline.size() - 1);
            int high = Math.min(low + 1, polyline.size() - 1);
            double alpha = scaled - low;
            return interpolate(polyline.get(low), polyline.get(high), alpha);
        }

        private static CartesianPoint bilinearBlend(
                CartesianPoint c0,
                CartesianPoint c1,
                CartesianPoint d0,
                CartesianPoint d1,
                CartesianPoint p00,
                CartesianPoint p10,
                CartesianPoint p01,
                CartesianPoint p11,
                double u,
                double v
        ) {
            double x = (1.0 - v) * c0.x() + v * c1.x() + (1.0 - u) * d0.x() + u * d1.x()
                    - ((1.0 - u) * (1.0 - v) * p00.x() + u * (1.0 - v) * p10.x()
                    + (1.0 - u) * v * p01.x() + u * v * p11.x());
            double y = (1.0 - v) * c0.y() + v * c1.y() + (1.0 - u) * d0.y() + u * d1.y()
                    - ((1.0 - u) * (1.0 - v) * p00.y() + u * (1.0 - v) * p10.y()
                    + (1.0 - u) * v * p01.y() + u * v * p11.y());
            double z = (1.0 - v) * c0.z() + v * c1.z() + (1.0 - u) * d0.z() + u * d1.z()
                    - ((1.0 - u) * (1.0 - v) * p00.z() + u * (1.0 - v) * p10.z()
                    + (1.0 - u) * v * p01.z() + u * v * p11.z());
            return new CartesianPoint(x, y, z);
        }
    }

    private record LoopPayload(boolean outer, List<PointPayload> points) {
    }

    private record PointPayload(double x, double y, double z) {
    }

    private record VectorPayload(double x, double y, double z) {
    }

    private record ColorPayload(int red, int green, int blue) {
    }

    private record GeometrySummary(int faceCount, int edgeCount, double approxSurfaceArea, double approxEdgeLength) {
    }

    private static final class BoundsAccumulator {
        private double minX = Double.POSITIVE_INFINITY;
        private double minY = Double.POSITIVE_INFINITY;
        private double minZ = Double.POSITIVE_INFINITY;
        private double maxX = Double.NEGATIVE_INFINITY;
        private double maxY = Double.NEGATIVE_INFINITY;
        private double maxZ = Double.NEGATIVE_INFINITY;

        void include(CartesianPoint point) {
            include(new PointPayload(point.x(), point.y(), point.z()));
        }

        void include(PointPayload point) {
            minX = Math.min(minX, point.x());
            minY = Math.min(minY, point.y());
            minZ = Math.min(minZ, point.z());
            maxX = Math.max(maxX, point.x());
            maxY = Math.max(maxY, point.y());
            maxZ = Math.max(maxZ, point.z());
        }

        boolean isEmpty() {
            return !Double.isFinite(minX);
        }

        BoundsPayload toPayload() {
            if (!Double.isFinite(minX)) {
                PointPayload zero = new PointPayload(0.0, 0.0, 0.0);
                return new BoundsPayload(zero, zero);
            }
            return new BoundsPayload(new PointPayload(minX, minY, minZ), new PointPayload(maxX, maxY, maxZ));
        }
    }
}
