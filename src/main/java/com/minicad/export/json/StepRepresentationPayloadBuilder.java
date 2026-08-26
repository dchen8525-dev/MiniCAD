package com.minicad.export.json;

import java.util.*;
import java.util.stream.Collectors;
import com.minicad.helper.StepMetadataExtractor;
import com.minicad.step.model.*;
import com.minicad.step.semantic.*;
import com.minicad.topology.*;
import com.minicad.geometry.*;
import com.minicad.common.*;
import com.minicad.preview.payload.*;
import com.minicad.preview.statistics.*;
import com.minicad.export.glb.*;
import com.minicad.builder.StepAssemblyGraphBuilder;
import com.minicad.helper.*;
import com.minicad.geometry2d.*;
import com.minicad.step.syntax.*;
import com.minicad.preview.sampling.*;
import com.minicad.preview.mapper.*;
import com.minicad.export.mesh.*;
import com.minicad.builder.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * StepRepresentationPayloadBuilder.
 */

public final class StepRepresentationPayloadBuilder {
    private static final Logger log = LoggerFactory.getLogger(StepRepresentationPayloadBuilder.class);
    private StepRepresentationPayloadBuilder() {}

    // Delegate to StepPlacementTransformer - extracted utility class
    public static double[] matrixForMappedPlacement(
            StepEntity mappedOrigin,
            StepEntity mappingTarget,
            StepCadBuilder builder
    ) {
        return StepPlacementTransformer.matrixForMappedPlacement(mappedOrigin, mappingTarget, builder);
    }

    // Delegate to StepPlacementTransformer - extracted utility class
    public static double[] matrixForPlacementEntity(StepEntity placement, StepCadBuilder builder) {
        return StepPlacementTransformer.matrixForPlacementEntity(placement, builder);
    }


    static AssemblyData buildAssemblyData(
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata,
            UnitExtractor.UnitInfo units
    ) {
        // F04: Assembly transforms should stay in source units when units are not normalized
        // When scaleToMeters != 1.0 (units not normalized), pass 1.0 to keep transforms in source units
        // When scaleToMeters == 1.0 (units normalized or SI), pass 1.0 (no scaling needed)
        StepAssemblyGraphBuilder.AssemblyGraph graph = StepAssemblyGraphBuilder.build(resolved, 1.0);
        Map<Integer, RepresentationPayload> representations = new LinkedHashMap<>();
        List<UnsupportedFacePayload> unsupportedFaces = new ArrayList<>();
        for (StepAssemblyGraphBuilder.AssemblyRepresentation assemblyRepresentation : graph.representations()) {
            StepEntity entity = resolved.get(assemblyRepresentation.representationId());
            if (entity instanceof StepRepresentation) { StepRepresentation representation = (StepRepresentation) entity;
                RepresentationBuildResult result = buildRepresentationPayload(
                        representation,
                        assemblyRepresentation.name(),
                        resolved,
                        builder,
                        metadata
                );
                unsupportedFaces.addAll(result.unsupportedFaces());
                representations.put(representation.id(), result.payload());
            }
        }

        if (representations.isEmpty()) {
            for (StepEntity entity : resolved.values()) {
                if (entity instanceof StepRepresentation) { StepRepresentation representation = (StepRepresentation) entity;
                    RepresentationBuildResult result = buildRepresentationPayload(
                            representation,
                            representation.name(),
                            resolved,
                            builder,
                            metadata
                    );
                    unsupportedFaces.addAll(result.unsupportedFaces());
                    representations.putIfAbsent(
                            representation.id(),
                            result.payload()
                    );
                }
            }
        }

        List<InstancePayload> instances = new ArrayList<>();
        for (StepAssemblyGraphBuilder.AssemblyNode node : graph.nodes()) {
            instances.add(new InstancePayload(
                    node.id(),
                    node.parentId(),
                    node.productDefinitionId(),
                    node.occurrenceId(),
                    node.representationIds().isEmpty() ? null : node.representationIds().get(0),
                    node.representationIds(),
                    node.label(),
                    node.description(),
                    node.localMatrix(),
                    node.worldMatrix(),
                    node.depth()
                ));
        }

        List<RepresentationPayload> representationList = List.copyOf(representations.values());
        List<InstancePayload> instanceList = List.copyOf(instances);
        AssemblyMetrics metrics = measureAssembly(representationList, instanceList);
        return new AssemblyData(
                representationList,
                instanceList,
                List.copyOf(unsupportedFaces),
                metrics.summary(),
                metrics.bounds()
        );
    }


    public static RepresentationBuildResult buildRepresentationPayload(
            StepRepresentation representation,
            String displayName,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata
    ) {
        return buildRepresentationPayload(representation, displayName, resolved, builder, metadata, new LinkedHashSet<>());
    }


    static GeometryCollection buildMappedRepresentationGeometry(
            StepRepresentation representation,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata,
            Set<Integer> visitingRepresentations
    ) {
        GeometryCollection geometry = new GeometryCollection(List.of(), List.of(), List.of());
        for (StepRepresentation candidate : linkedShapeRepresentations(representation, resolved)) {
            for (StepEntity item : candidate.items()) {
                if (item instanceof StepMappedItem) {
            StepMappedItem mappedItem = (StepMappedItem) item;
                    geometry = StepLegacyGeometryBuilder.mergeGeometry(
                            geometry,
                            expandMappedItemGeometry(mappedItem, resolved, builder, metadata, visitingRepresentations)
                    );
                }
            }
        }
        return geometry;
    }


    static GeometryCollection buildRelatedRepresentationGeometry(
            StepRepresentation representation,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata,
            Set<Integer> visitingRepresentations
    ) {
        GeometryCollection geometry = new GeometryCollection(List.of(), List.of(), List.of());
        for (StepEntity entity : resolved.values()) {
            if (!(entity instanceof StepRepresentationRelationshipWithTransformation)) {
                continue;
            }
            StepRepresentationRelationshipWithTransformation relationship = (StepRepresentationRelationshipWithTransformation) entity;
            if (!relationship.rep1().shapeRepresentation()
                    || !relationship.rep2().shapeRepresentation()
                    || relationship.rep2().id() != representation.id()) {
                continue;
            }
            double[] matrix = StepAssemblyGraphBuilder.matrixFor(relationship.transformationOperator());
            RepresentationBuildResult source = buildRepresentationPayload(
                    relationship.rep1(),
                    relationship.rep1().name(),
                    resolved,
                    builder,
                    metadata,
                    visitingRepresentations
            );
            StepMetadataExtractor.DisplayMetadata relationshipMetadata = metadata.forItem(relationship.id());
            List<EdgePayload> edges = source.payload().edges().stream()
                    .map(edge -> transformMappedEdge(edge, relationship.id(), matrix))
                    .collect(Collectors.toList());
            List<FacePayload> faces = source.payload().faces().stream()
                    .map(face -> transformMappedFace(face, relationship.id(), matrix, relationshipMetadata))
                    .collect(Collectors.toList());
            geometry = StepLegacyGeometryBuilder.mergeGeometry(geometry, new GeometryCollection(edges, faces, source.unsupportedFaces()));
        }
        return geometry;
    }


    static GeometryCollection expandMappedItemGeometry(
            StepMappedItem mappedItem,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata,
            Set<Integer> visitingRepresentations
    ) {
        double[] matrix = mappedItemMatrix(mappedItem, builder);
        if (matrix == null) {
            return new GeometryCollection(List.of(), List.of(), List.of());
        }
        StepRepresentationMap mappingSource = mappedItem.mappingSource();
        RepresentationBuildResult source = buildRepresentationPayload(
                mappingSource.mappedRepresentation(),
                mappingSource.mappedRepresentation().name(),
                resolved,
                builder,
                metadata,
                visitingRepresentations
        );
        StepMetadataExtractor.DisplayMetadata itemMetadata = metadata.forItem(mappedItem.id());
        List<EdgePayload> edges = source.payload().edges().stream()
                .map(edge -> transformMappedEdge(edge, mappedItem.id(), matrix))
                .collect(Collectors.toList());
        List<FacePayload> faces = source.payload().faces().stream()
                .map(face -> transformMappedFace(face, mappedItem.id(), matrix, itemMetadata))
                .collect(Collectors.toList());
        return new GeometryCollection(edges, faces, source.unsupportedFaces());
    }


    static Set<Integer> collectRepresentationShells(
            StepRepresentation representation,
            Map<Integer, StepEntity> resolved
    ) {
        Set<Integer> shellIds = new TreeSet<>();
        for (StepRepresentation candidate : linkedShapeRepresentations(representation, resolved)) {
            for (StepEntity item : candidate.items()) {
                StepEntity unwrapped = StepLegacyGeometryBuilder.unwrapStyledItem(item);
                if (!StepLegacyGeometryBuilder.isRepresentationSolidItem(unwrapped)) {
                    StepLegacyGeometryBuilder.collectShellLikeIds(item, shellIds);
                }
            }
        }
        return shellIds;
    }


    static Set<Integer> collectRepresentationSolids(
            StepRepresentation representation,
            Map<Integer, StepEntity> resolved
    ) {
        Set<Integer> solidIds = new TreeSet<>();
        for (StepRepresentation candidate : linkedShapeRepresentations(representation, resolved)) {
            for (StepEntity item : candidate.items()) {
                StepEntity unwrapped = StepLegacyGeometryBuilder.unwrapStyledItem(item);
                if (StepLegacyGeometryBuilder.isRepresentationSolidItem(unwrapped)) {
                    solidIds.add(unwrapped.id());
                }
            }
        }
        return solidIds;
    }


    static List<EdgePayload> collectRepresentationLooseEdges(
            StepRepresentation representation,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata
    ) {
        Map<Integer, EdgePayload> edges = new LinkedHashMap<>();
        for (StepRepresentation candidate : linkedShapeRepresentations(representation, resolved)) {
            for (StepEntity item : candidate.items()) {
                StepEdgePayloadBuilder.collectStandaloneEdges(item, edges, resolved, builder, metadata);
            }
        }
        return List.copyOf(edges.values());
    }


    static Map<Integer, StepMetadataExtractor.DisplayMetadata> collectInheritedShellMetadata(
            StepRepresentation representation,
            StepMetadataExtractor metadata,
            Map<Integer, StepEntity> resolved
    ) {
        Map<Integer, StepMetadataExtractor.DisplayMetadata> metadataByShellId = new LinkedHashMap<>();
        for (StepRepresentation candidate : linkedShapeRepresentations(representation, resolved)) {
            for (StepEntity item : candidate.items()) {
                StepEntity unwrapped = StepLegacyGeometryBuilder.unwrapStyledItem(item);
                if (StepLegacyGeometryBuilder.isRepresentationSolidItem(unwrapped)) {
                    continue;
                }
                StepMetadataExtractor.DisplayMetadata itemMetadata = metadata.forItem(item.id());
                Set<Integer> itemShellIds = new LinkedHashSet<>();
                StepLegacyGeometryBuilder.collectShellLikeIds(item, itemShellIds);
                for (Integer shellId : itemShellIds) {
                    metadataByShellId.put(shellId, StepMetadataHelper.mergeMetadata(metadataByShellId.get(shellId), itemMetadata));
                }
            }
        }
        return Map.copyOf(metadataByShellId);
    }


    static Map<Integer, StepMetadataExtractor.DisplayMetadata> collectInheritedSolidMetadata(
            StepRepresentation representation,
            StepMetadataExtractor metadata,
            Map<Integer, StepEntity> resolved
    ) {
        Map<Integer, StepMetadataExtractor.DisplayMetadata> metadataBySolidId = new LinkedHashMap<>();
        for (StepRepresentation candidate : linkedShapeRepresentations(representation, resolved)) {
            for (StepEntity item : candidate.items()) {
                StepEntity unwrapped = StepLegacyGeometryBuilder.unwrapStyledItem(item);
                if (StepLegacyGeometryBuilder.isRepresentationSolidItem(unwrapped)) {
                    StepMetadataExtractor.DisplayMetadata itemMetadata = metadata.forItem(item.id());
                    metadataBySolidId.put(unwrapped.id(), StepMetadataHelper.mergeMetadata(metadataBySolidId.get(unwrapped.id()), itemMetadata));
                }
            }
        }
        return Map.copyOf(metadataBySolidId);
    }


    public static List<StepRepresentation> linkedShapeRepresentations(
            StepRepresentation seed,
            Map<Integer, StepEntity> resolved
    ) {
        List<StepRepresentation> ordered = new ArrayList<>();
        Set<Integer> visited = new LinkedHashSet<>();
        collectLinkedShapeRepresentations(seed, resolved, visited, ordered);
        return List.copyOf(ordered);
    }


    static void collectLinkedShapeRepresentations(
            StepRepresentation current,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visited,
            List<StepRepresentation> ordered
    ) {
        if (!visited.add(current.id())) {
            return;
        }
        ordered.add(current);
        for (StepEntity entity : resolved.values()) {
            if (entity instanceof StepShapeRepresentationRelationship) {
            StepShapeRepresentationRelationship relationship = (StepShapeRepresentationRelationship) entity;
                if (!relationship.rep1().shapeRepresentation() || !relationship.rep2().shapeRepresentation()) {
                    continue;
                }
                if (relationship.rep1().id() == current.id()) {
                    collectLinkedShapeRepresentations(relationship.rep2(), resolved, visited, ordered);
                } else if (relationship.rep2().id() == current.id()) {
                    collectLinkedShapeRepresentations(relationship.rep1(), resolved, visited, ordered);
                }
                continue;
            }
            if (entity instanceof StepRepresentationRelationship) {
            StepRepresentationRelationship relationship = (StepRepresentationRelationship) entity;
                if (!relationship.rep1().shapeRepresentation() || !relationship.rep2().shapeRepresentation()) {
                    continue;
                }
                if (relationship.rep1().id() == current.id()) {
                    collectLinkedShapeRepresentations(relationship.rep2(), resolved, visited, ordered);
                } else if (relationship.rep2().id() == current.id()) {
                    collectLinkedShapeRepresentations(relationship.rep1(), resolved, visited, ordered);
                }
            }
        }
    }


    public static BSplineSurface3 buildBsplineSurface(StepEntity geometry, StepCadBuilder builder) {
        if (geometry instanceof StepBSplineSurfaceWithKnots) {
            StepBSplineSurfaceWithKnots splineSurface = (StepBSplineSurfaceWithKnots) geometry;
            return builder.buildBSplineSurface(splineSurface.id());
        }
        if (geometry instanceof StepBSplineSurface) {
            StepBSplineSurface splineSurface = (StepBSplineSurface) geometry;
            return builder.buildGenericBSplineSurface(splineSurface.id());
        }
        if (geometry instanceof StepBSplineSurfaceWithKnotsAndBreakpoints) {
            StepBSplineSurfaceWithKnotsAndBreakpoints splineSurface = (StepBSplineSurfaceWithKnotsAndBreakpoints) geometry;
            return builder.buildBSplineSurfaceWithBreakpoints(splineSurface.id());
        }
        if (geometry instanceof StepBezierSurface) {
            StepBezierSurface splineSurface = (StepBezierSurface) geometry;
            return builder.buildBezierSurface(splineSurface.id());
        }
        if (geometry instanceof StepUniformSurface) {
            StepUniformSurface splineSurface = (StepUniformSurface) geometry;
            return builder.buildUniformSurface(splineSurface.id());
        }
        if (geometry instanceof StepQuasiUniformSurface) {
            StepQuasiUniformSurface splineSurface = (StepQuasiUniformSurface) geometry;
            return builder.buildQuasiUniformSurface(splineSurface.id());
        }
        if (geometry instanceof StepPiecewiseBezierSurface) {
            StepPiecewiseBezierSurface splineSurface = (StepPiecewiseBezierSurface) geometry;
            return builder.buildPiecewiseBezierSurface(splineSurface.id());
        }
        throw new UnsupportedGeometryException(StepTypeNameResolver.surfaceTypeName(geometry) + " is not a supported B-spline-like surface");
    }


    public static BSplineSurface3 buildFreeFormSurface(StepFreeFormSurface surface, StepCadBuilder builder) {
        int uCount = surface.controlPoints().size();
        int vCount = surface.controlPoints().isEmpty() ? 0 : surface.controlPoints().get(0).size();
        if (uCount < 2 || vCount < 2) {
            throw new UnsupportedGeometryException("FREE_FORM_SURFACE requires at least 2x2 control points");
        }
        List<List<CartesianPoint>> controlPoints = new ArrayList<>(uCount);
        for (List<StepEntity> row : surface.controlPoints()) {
            List<CartesianPoint> pointRow = new ArrayList<>(row.size());
            for (StepEntity pt : row) {
                if (pt instanceof com.minicad.step.model.StepCartesianPoint) {
                    com.minicad.step.model.StepCartesianPoint cartesianPoint = (com.minicad.step.model.StepCartesianPoint) pt;
                    pointRow.add(builder.buildPoint(cartesianPoint.id()));
                } else {
                    throw new UnsupportedGeometryException("FREE_FORM_SURFACE control points must be Cartesian points");
                }
            }
            controlPoints.add(List.copyOf(pointRow));
        }
        int uDegree = surface.degreeU();
        int vDegree = surface.degreeV();
        // Generate uniform knot vectors
        int uKnotCount = uCount + uDegree + 1;
        int vKnotCount = vCount + vDegree + 1;
        List<Double> uKnots = new ArrayList<>();
        for (int i = 0; i < uKnotCount; i++) {
            uKnots.add((double) i / (uKnotCount - 1));
        }
        List<Double> vKnots = new ArrayList<>();
        for (int i = 0; i < vKnotCount; i++) {
            vKnots.add((double) i / (vKnotCount - 1));
        }
        List<Integer> uMults = List.of(1);
        List<Integer> vMults = List.of(1);
        return new BSplineSurface3(uDegree, vDegree, controlPoints, uMults, vMults, uKnots, vKnots);
    }


    public static CurveEvaluator curveEvaluator(StepEntity curve, StepCadBuilder builder) {
        // Converted from switch expression to if-else for Java 11 compatibility
        if (curve instanceof StepLine) {
            StepLine line = (StepLine) curve;
            Line3 geometry = builder.buildLine(line.id());
            return new CurveEvaluator() {
                @Override
                public double start() { return -1.0; }
                @Override
                public double end() { return 1.0; }
                @Override
                public CartesianPoint pointAt(double parameter) {
                    return geometry.pointAt(parameter);
                }
            };
        } else if (curve instanceof StepCircle) {
            StepCircle circle = (StepCircle) curve;
            Circle geometry = builder.buildCircle(circle.id());
            return new CurveEvaluator() {
                @Override
                public double start() { return 0.0; }
                @Override
                public double end() { return Math.PI * 2.0; }
                @Override
                public CartesianPoint pointAt(double parameter) {
                    return geometry.pointAt(parameter);
                }
            };
        } else if (curve instanceof StepEllipse) {
            StepEllipse ellipse = (StepEllipse) curve;
            Ellipse3 geometry = builder.buildEllipse(ellipse.id());
            return new CurveEvaluator() {
                @Override
                public double start() { return 0.0; }
                @Override
                public double end() { return Math.PI * 2.0; }
                @Override
                public CartesianPoint pointAt(double parameter) {
                    return geometry.pointAt(parameter);
                }
            };
        } else if (curve instanceof StepBSplineCurveWithKnots) {
            StepBSplineCurveWithKnots spline = (StepBSplineCurveWithKnots) curve;
            BSplineCurve3 geometry = builder.buildBSplineCurve(spline.id());
            return new CurveEvaluator() {
                @Override
                public double start() { return geometry.startParameter(); }
                @Override
                public double end() { return geometry.endParameter(); }
                @Override
                public CartesianPoint pointAt(double parameter) {
                    return geometry.pointAt(parameter);
                }
            };
        } else if (curve instanceof StepTrimmedCurve) {
            StepTrimmedCurve trimmedCurve = (StepTrimmedCurve) curve;
            return curveEvaluator(trimmedCurve.basisCurve(), builder);
        } else if (curve instanceof StepSurfaceCurve) {
            StepSurfaceCurve surfaceCurve = (StepSurfaceCurve) curve;
            return curveEvaluator(surfaceCurve.curve3d(), builder);
        } else if (curve instanceof StepRationalBSplineCurve) {
            StepRationalBSplineCurve spline = (StepRationalBSplineCurve) curve;
            com.minicad.geometry.RationalBSplineCurve3 geometry = builder.buildRationalBSplineCurve(spline.id());
            return new CurveEvaluator() {
                @Override public double start() { return geometry.startParameter(); }
                @Override public double end() { return geometry.endParameter(); }
                @Override public CartesianPoint pointAt(double parameter) { return geometry.pointAt(parameter); }
            };
        } else if (curve instanceof StepPolyline) {
            StepPolyline polyline = (StepPolyline) curve;
            Polyline3 geometry = builder.buildPolyline(polyline.id());
            return new CurveEvaluator() {
                @Override public double start() { return 0.0; }
                @Override public double end() { return 1.0; }
                @Override public CartesianPoint pointAt(double parameter) { return geometry.pointAt(parameter); }
            };
        } else if (curve instanceof StepCompositeCurve) {
            StepCompositeCurve compositeCurve = (StepCompositeCurve) curve;
            CompositeCurve3 geometry = builder.buildCompositeCurve(compositeCurve.id());
            return sampledCurveEvaluator(geometry);
        } else if (curve instanceof StepBezierCurve) {
            StepBezierCurve bezier = (StepBezierCurve) curve;
            return sampledCurveEvaluator(builder.buildCurveReference3(bezier.id()));
        } else if (curve instanceof StepUniformCurve) {
            StepUniformCurve uniform = (StepUniformCurve) curve;
            return sampledCurveEvaluator(builder.buildCurveReference3(uniform.id()));
        } else if (curve instanceof StepQuasiUniformCurve) {
            StepQuasiUniformCurve quasiUniform = (StepQuasiUniformCurve) curve;
            return sampledCurveEvaluator(builder.buildCurveReference3(quasiUniform.id()));
        } else if (curve instanceof StepPiecewiseBezierCurve) {
            StepPiecewiseBezierCurve piecewiseBezier = (StepPiecewiseBezierCurve) curve;
            return sampledCurveEvaluator(builder.buildCurveReference3(piecewiseBezier.id()));
        } else if (curve instanceof StepOffsetCurve3D) {
            StepOffsetCurve3D offsetCurve3D = (StepOffsetCurve3D) curve;
            return sampledCurveEvaluator(builder.buildOffsetCurve3(offsetCurve3D.id()));
        } else if (curve instanceof StepConicCurve) {
            StepConicCurve conic = (StepConicCurve) curve;
            List<CartesianPoint> points = ConicSamplingHelper.sampleConicCurvePoints(conic, builder);
            if (points == null || points.size() < 2) return null;
            return sampledCurveEvaluator(new Polyline3(points));
        } else if (curve instanceof StepOrientedCurve) {
            StepOrientedCurve orientedCurve = (StepOrientedCurve) curve;
            return curveEvaluator(orientedCurve.curveElement(), builder);
        } else if (curve instanceof StepGeometricReplica) {
            StepGeometricReplica replica = (StepGeometricReplica) curve;
            return curveEvaluator(replica.parent(), builder);
        } else if (curve instanceof StepBSplineCurve) {
            StepBSplineCurve bspline = (StepBSplineCurve) curve;
            return sampledCurveEvaluator(builder.buildCurveReference3(bspline.id()));
        } else if (curve instanceof StepSeamCurve) {
            StepSeamCurve seamCurve = (StepSeamCurve) curve;
            return sampledCurveEvaluator(builder.buildSeamCurve(seamCurve.id()).curve3d());
        } else if (curve instanceof StepCircle2D) {
            StepCircle2D circle2D = (StepCircle2D) curve;
            return sampledCurveEvaluator(builder.buildCurve3From2D(circle2D.id()));
        } else if (curve instanceof StepEllipse2D) {
            StepEllipse2D ellipse2D = (StepEllipse2D) curve;
            return sampledCurveEvaluator(builder.buildCurve3From2D(ellipse2D.id()));
        } else if (curve instanceof StepPolyline2D) {
            StepPolyline2D polyline2D = (StepPolyline2D) curve;
            return sampledCurveEvaluator(builder.buildCurve3From2D(polyline2D.id()));
        } else if (curve instanceof StepTrimmedCurve2D) {
            StepTrimmedCurve2D trimmedCurve2D = (StepTrimmedCurve2D) curve;
            return sampledCurveEvaluator(builder.buildCurve3From2D(trimmedCurve2D.id()));
        } else if (curve instanceof StepCompositeCurve2D) {
            StepCompositeCurve2D compositeCurve2D = (StepCompositeCurve2D) curve;
            return sampledCurveEvaluator(builder.buildCurve3From2D(compositeCurve2D.id()));
        } else if (curve instanceof StepBezierCurve2D) {
            StepBezierCurve2D bezier2D = (StepBezierCurve2D) curve;
            return sampledCurveEvaluator(builder.buildCurve3From2D(bezier2D.id()));
        } else if (curve instanceof StepQuasiUniformCurve2D) {
            StepQuasiUniformCurve2D quasiUniform2D = (StepQuasiUniformCurve2D) curve;
            return sampledCurveEvaluator(builder.buildCurve3From2D(quasiUniform2D.id()));
        } else if (curve instanceof StepUniformCurve2D) {
            StepUniformCurve2D uniform2D = (StepUniformCurve2D) curve;
            return sampledCurveEvaluator(builder.buildCurve3From2D(uniform2D.id()));
        } else if (curve instanceof StepPiecewiseBezierCurve2D) {
            StepPiecewiseBezierCurve2D piecewiseBezier2D = (StepPiecewiseBezierCurve2D) curve;
            return sampledCurveEvaluator(builder.buildCurve3From2D(piecewiseBezier2D.id()));
        } else if (curve instanceof StepIndexedPolyCurve2D) {
            StepIndexedPolyCurve2D polyCurve2D = (StepIndexedPolyCurve2D) curve;
            return sampledCurveEvaluator(builder.buildCurve3From2D(polyCurve2D.id()));
        } else if (curve instanceof StepDegenerateCurve2D) {
            StepDegenerateCurve2D degenerateCurve2D = (StepDegenerateCurve2D) curve;
            return sampledCurveEvaluator(builder.buildCurve3From2D(degenerateCurve2D.id()));
        } else if (curve instanceof StepBSplineCurve2D) {
            StepBSplineCurve2D bspline2D = (StepBSplineCurve2D) curve;
            return sampledCurveEvaluator(builder.buildCurve3From2D(bspline2D.id()));
        } else if (curve instanceof StepRationalBSplineCurve2D) {
            StepRationalBSplineCurve2D rationalBspline2D = (StepRationalBSplineCurve2D) curve;
            return sampledCurveEvaluator(builder.buildCurve3From2D(rationalBspline2D.id()));
        } else if (curve instanceof StepLine2D) {
            StepLine2D line2D = (StepLine2D) curve;
            return sampledCurveEvaluator(builder.buildCurve3From2D(line2D.id()));
        } else if (curve instanceof StepCurve2D) {
            StepCurve2D curve2D = (StepCurve2D) curve;
            return sampledCurveEvaluator(builder.buildCurve3From2D(curve2D.id()));
        } else if (curve instanceof StepHyperbola2D) {
            StepHyperbola2D hyperbola2D = (StepHyperbola2D) curve;
            return sampledCurveEvaluator(builder.buildCurve3From2D(hyperbola2D.id()));
        } else if (curve instanceof StepParabola2D) {
            StepParabola2D parabola2D = (StepParabola2D) curve;
            return sampledCurveEvaluator(builder.buildCurve3From2D(parabola2D.id()));
        } else if (curve instanceof StepOffsetCurve2D) {
            StepOffsetCurve2D offsetCurve2D = (StepOffsetCurve2D) curve;
            return sampledCurveEvaluator(builder.buildCurve3From2D(offsetCurve2D.id()));
        } else if (curve instanceof StepClothoid) {
            StepClothoid clothoid = (StepClothoid) curve;
            return sampledCurveEvaluator(builder.buildCurveReference3(clothoid.id()));
        } else if (curve instanceof StepIndexedPolyCurve) {
            StepIndexedPolyCurve polyCurve = (StepIndexedPolyCurve) curve;
            return sampledCurveEvaluator(builder.buildCurveReference3(polyCurve.id()));
        } else if (curve instanceof StepDegenerateCurve) {
            StepDegenerateCurve degenerate = (StepDegenerateCurve) curve;
            return sampledCurveEvaluator(builder.buildCurveReference3(degenerate.id()));
        } else if (curve instanceof StepBSplineCurveWithKnotsAndBreakpoints) {
            StepBSplineCurveWithKnotsAndBreakpoints splineBreak = (StepBSplineCurveWithKnotsAndBreakpoints) curve;
            return sampledCurveEvaluator(builder.buildBSplineCurveWithBreakpoints(splineBreak.id()));
        } else if (curve instanceof StepCompositeCurveOnSurface) {
            StepCompositeCurveOnSurface compositeOnSurface = (StepCompositeCurveOnSurface) curve;
            return sampledCurveEvaluator(builder.buildCurveReference3(compositeOnSurface.id()));
        } else if (curve instanceof StepCompositeCurveOnSurface3D) {
            StepCompositeCurveOnSurface3D compositeOnSurface3D = (StepCompositeCurveOnSurface3D) curve;
            return sampledCurveEvaluator(builder.buildCurveReference3(compositeOnSurface3D.id()));
        } else if (curve instanceof StepLineSegment) {
            StepLineSegment lineSeg = (StepLineSegment) curve;
            List<CartesianPoint> pts = List.of(
                    builder.buildPoint(lineSeg.startPoint().id()),
                    builder.buildPoint(lineSeg.endPoint().id())
            );
            return sampledCurveEvaluator(new Polyline3(pts));
        } else if (curve instanceof StepPath) {
            StepPath path = (StepPath) curve;
            return sampledCurveEvaluator(builder.buildPath(path.id()));
        } else if (curve instanceof StepOpenPath) {
            StepOpenPath openPath = (StepOpenPath) curve;
            return sampledCurveEvaluator(builder.buildPath(openPath.id()));
        } else if (curve instanceof StepSubpath) {
            StepSubpath subpath = (StepSubpath) curve;
            return sampledCurveEvaluator(builder.buildPath(subpath.id()));
        } else if (curve instanceof StepOrientedPath) {
            StepOrientedPath orientedPath = (StepOrientedPath) curve;
            return sampledCurveEvaluator(builder.buildPath(orientedPath.id()));
        } else if (curve instanceof StepEdgeCurve) {
            StepEdgeCurve edgeCurve = (StepEdgeCurve) curve;
            return sampledCurveEvaluator(builder.buildCurveReference3(edgeCurve.id()));
        } else if (curve instanceof StepSurfacedEdgeCurve) {
            StepSurfacedEdgeCurve surfacedEdge = (StepSurfacedEdgeCurve) curve;
            return sampledCurveEvaluator(builder.buildCurveReference3(surfacedEdge.id()));
        } else if (curve instanceof StepAnnotationCurveOccurrence) {
            StepAnnotationCurveOccurrence occurrence = (StepAnnotationCurveOccurrence) curve;
            return curveEvaluator(occurrence.item(), builder);
        } else if (curve instanceof StepDimensionCurve) {
            StepDimensionCurve dimensionCurve = (StepDimensionCurve) curve;
            return curveEvaluator(dimensionCurve.item(), builder);
        } else if (curve instanceof StepLeaderCurve) {
            StepLeaderCurve leaderCurve = (StepLeaderCurve) curve;
            return curveEvaluator(leaderCurve.item(), builder);
        } else if (curve instanceof StepProjectionCurve) {
            StepProjectionCurve projectionCurve = (StepProjectionCurve) curve;
            return curveEvaluator(projectionCurve.item(), builder);
        } else if (curve instanceof StepDraughtingAnnotationOccurrence) {
            StepDraughtingAnnotationOccurrence annotationOccurrence = (StepDraughtingAnnotationOccurrence) curve;
            return curveEvaluator(annotationOccurrence.item(), builder);
        } else if (curve instanceof StepTerminatorSymbol) {
            StepTerminatorSymbol terminatorSymbol = (StepTerminatorSymbol) curve;
            return curveEvaluator(terminatorSymbol.annotatedCurve(), builder);
        } else if (curve instanceof StepCurve) {
            StepCurve abstractCurve = (StepCurve) curve;
            return sampledCurveEvaluator(builder.buildCurveReference3(abstractCurve.id()));
        } else if (curve instanceof StepBoundedCurve) {
            StepBoundedCurve boundedCurve = (StepBoundedCurve) curve;
            return sampledCurveEvaluator(builder.buildCurveReference3(boundedCurve.id()));
        } else if (curve instanceof StepMappedItem) {
            StepMappedItem mappedItem = (StepMappedItem) curve;
            return curveEvaluator(mappedItem.mappingTarget(), builder);
        } else {
            return null;
        }
    }


    private static CurveEvaluator sampledCurveEvaluator(Curve3 curve) {
        List<CartesianPoint> points = curve.sample(128);
        if (points.size() < 2) return null;
        return new CurveEvaluator() {
            @Override public double start() { return 0.0; }
            @Override public double end() { return 1.0; }
            @Override
            public CartesianPoint pointAt(double parameter) {
                double t = Math.max(0, Math.min(1, parameter));
                double idx = t * (points.size() - 1);
                int i0 = (int) idx;
                int i1 = Math.min(i0 + 1, points.size() - 1);
                double f = idx - i0;
                CartesianPoint p0 = points.get(i0);
                CartesianPoint p1 = points.get(i1);
                return new CartesianPoint(
                        p0.x() + (p1.x() - p0.x()) * f,
                        p0.y() + (p1.y() - p0.y()) * f,
                        p0.z() + (p1.z() - p0.z()) * f
                );
            }
        };
    }

    // Delegate to StepMappedItemTransformer - extracted utility class
    public static double[] mappedItemMatrix(StepMappedItem mappedItem, StepCadBuilder builder) {
        return StepMappedItemTransformer.mappedItemMatrix(mappedItem, builder);
    }

    // Delegate to StepPlacementTransformer - extracted utility class
    public static double[] matrixForTransformationOperator(
            com.minicad.step.model.StepCartesianTransformationOperator transformation,
            StepCadBuilder builder
    ) {
        return StepPlacementTransformer.matrixForTransformationOperator(transformation, builder);
    }

    // Delegate to StepMappedItemTransformer - extracted utility class
    public static EdgePayload transformMappedEdge(EdgePayload edge, int mappedItemId, double[] matrix) {
        return StepMappedItemTransformer.transformMappedEdge(edge, mappedItemId, matrix);
    }

    // Delegate to StepMappedItemTransformer - extracted utility class
    public static FacePayload transformMappedFace(
            FacePayload face,
            int mappedItemId,
            double[] matrix,
            StepMetadataExtractor.DisplayMetadata metadata
    ) {
        return StepMappedItemTransformer.transformMappedFace(face, mappedItemId, matrix, metadata);
    }


    private static AssemblyMetrics measureAssembly(
            List<RepresentationPayload> representations,
            List<InstancePayload> instances
    ) {
        Map<Integer, RepresentationPayload> byId = representations.stream()
                .collect(Collectors.toMap(RepresentationPayload::id, representation -> representation, (left, right) -> left, LinkedHashMap::new));
        int faceCount = 0;
        int edgeCount = 0;
        double area = 0.0;
        double edgeLength = 0.0;
        PreviewSerializers.BoundsAccumulator bounds = new PreviewSerializers.BoundsAccumulator();
        for (InstancePayload instance : instances) {
            for (Integer representationId : instance.representationIds()) {
                RepresentationPayload representation = byId.get(representationId);
                if (representation == null) {
                    continue;
                }
                faceCount += representation.faces().size();
                edgeCount += representation.edges().size();
                area += GeometryMeasurementHelper.approximateSurfaceArea(representation.faces(), instance.worldMatrix());
                edgeLength += GeometryMeasurementHelper.approximateEdgeLength(representation.edges(), instance.worldMatrix());
                includeRepresentationBounds(bounds, representation, instance.worldMatrix());
            }
        }
        return new AssemblyMetrics(
                new GeometrySummary(faceCount, edgeCount, area, edgeLength),
                bounds.toPayload()
        );
    }


    static GeometrySummary summarizeGeometry(GeometryCollection geometry) {
        return new GeometrySummary(
                geometry.faces().size(),
                geometry.edges().size(),
                GeometryMeasurementHelper.approximateSurfaceArea(geometry.faces()),
                GeometryMeasurementHelper.approximateEdgeLength(geometry.edges())
        );
    }

    // Delegate to StepBoundsAccumulator - extracted utility class
    private static void includeRepresentationBounds(
            PreviewSerializers.BoundsAccumulator bounds,
            RepresentationPayload representation,
            double[] matrix
    ) {
        StepBoundsAccumulator.includeRepresentationBounds(bounds, representation, matrix);
    }


    public static RepresentationBuildResult buildRepresentationPayload(
            StepRepresentation representation,
            String displayName,
            Map<Integer, StepEntity> resolved,
            StepCadBuilder builder,
            StepMetadataExtractor metadata,
            Set<Integer> visitingRepresentations
    ) {
        if (!visitingRepresentations.add(representation.id())) {
            return new RepresentationBuildResult(
                    new RepresentationPayload(
                            representation.id(),
                            displayName,
                            List.of(),
                            null,
                            List.of(),
                            List.of()
                    ),
                    List.of()
            );
        }
        Set<Integer> shellIds = collectRepresentationShells(representation, resolved);
        Set<Integer> solidIds = collectRepresentationSolids(representation, resolved);
        StepMetadataExtractor.DisplayMetadata representationMetadata = metadata.forItem(representation.id());
        GeometryCollection shellGeometry = StepLegacyGeometryBuilder.buildGeometryForShells(
                shellIds,
                resolved,
                builder,
                metadata,
                collectInheritedShellMetadata(representation, metadata, resolved)
        );
        GeometryCollection solidGeometry = StepLegacyGeometryBuilder.buildGeometryForSolids(
                solidIds,
                resolved,
                builder,
                metadata,
                collectInheritedSolidMetadata(representation, metadata, resolved)
        );
        GeometryCollection mappedGeometry = buildMappedRepresentationGeometry(
                representation,
                resolved,
                builder,
                metadata,
                visitingRepresentations
        );
        GeometryCollection relatedGeometry = buildRelatedRepresentationGeometry(
                representation,
                resolved,
                builder,
                metadata,
                visitingRepresentations
        );
        GeometryCollection geometry = StepLegacyGeometryBuilder.mergeGeometry(
                StepLegacyGeometryBuilder.mergeGeometry(StepLegacyGeometryBuilder.mergeGeometry(shellGeometry, solidGeometry), mappedGeometry),
                relatedGeometry
        );
        List<EdgePayload> representationEdges = new ArrayList<>(geometry.edges());
        representationEdges.addAll(collectRepresentationLooseEdges(representation, resolved, builder, metadata));
        RepresentationBuildResult result = new RepresentationBuildResult(
                new RepresentationPayload(
                        representation.id(),
                        displayName,
                        representationMetadata.layers(),
                        PayloadConversionHelper.toColorPayload(representationMetadata.rgb()),
                        List.copyOf(representationEdges),
                        geometry.faces()
                ),
                geometry.unsupportedFaces()
        );
        visitingRepresentations.remove(representation.id());
        return result;
    }

}
