package com.minicad.export.json;

import com.minicad.common.StepResolutionException;
import com.minicad.common.TopologyException;
import com.minicad.common.UnsupportedGeometryException;
import com.minicad.export.glb.PreviewMaterialExporter;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Curve3;
import com.minicad.geometry.Vector3;
import com.minicad.preview.builder.PmiPayload;
import com.minicad.preview.sampling.Curve3SamplingHelper;
import com.minicad.preview.builder.PmiTargetHelper;
import com.minicad.preview.builder.PmiTargetPayload;
import com.minicad.preview.payload.AssemblyData;
import com.minicad.preview.payload.EdgePayload;
import com.minicad.preview.payload.FacePayload;
import com.minicad.preview.payload.InstancePayload;
import com.minicad.preview.payload.PayloadConversionHelper;
import com.minicad.preview.payload.PointPayload;
import com.minicad.preview.payload.RepresentationPayload;
import com.minicad.step.model.*;
import com.minicad.step.semantic.StepCadBuilder;
import com.minicad.topology.EdgeLoop;
import com.minicad.topology.Face;
import com.minicad.topology.FaceBound;
import com.minicad.topology.Loop;
import com.minicad.topology.OrientedEdge;
import com.minicad.topology.PolyLoop;
import com.minicad.topology.Shell;
import com.minicad.topology.Solid;
import com.minicad.topology.VertexLoop;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import com.minicad.step.syntax.StepValue;

/**
 * Builds PMI (Product Manufacturing Information) payloads from STEP entities.
 *
 * <p>Handles annotations, dimensions, tolerances, datum features, and other
 * manufacturing information that annotates geometry in CAD models.</p>
 *
 * <p>This class extracts PMI information from various STEP entity types:</p>
 * <ul>
 *   <li>Draughting callouts (dimensions, notes)</li>
 *   <li>Geometric tolerances (GD&T)</li>
 *   <li>Datum features and targets</li>
 *   <li>Annotation occurrences (text, symbols)</li>
 *   <li>PMI requirement associations</li>
 * </ul>
 *
 * @since 1.0
 */
public final class StepPmiPayloadBuilder {

    private StepPmiPayloadBuilder() {
    }

    // ========================================================================
    // Main Entry Point
    // ========================================================================

    /**
     * Builds PMI payloads from resolved STEP entities.
     *
     * @param resolved the resolved STEP entities by ID
     * @param assembly the assembly data
     * @param builder the STEP CAD builder for geometry extraction
     * @return list of PMI payloads
     */
    public static List<PmiPayload> buildPmiPayloads(
            Map<Integer, StepEntity> resolved,
            AssemblyData assembly,
            StepCadBuilder builder
    ) {
        Map<Integer, List<PmiTargetPayload>> targetsByUsageId = new LinkedHashMap<>();
        Map<Integer, List<String>> instanceIdsByTargetId = StepPmiTargetBuilder.buildInstanceIdsByTargetId(assembly);
        for (StepEntity entity : resolved.values()) {
            dispatchPmiUsageTargets(entity, resolved, targetsByUsageId, instanceIdsByTargetId);
        }
        for (StepEntity entity : resolved.values()) {
            if (entity instanceof StepDraughtingCalloutRelationship) {
            StepDraughtingCalloutRelationship relationship = (StepDraughtingCalloutRelationship) entity;
                StepPmiTargetBuilder.propagateCalloutTargets(targetsByUsageId, relationship);
            }
        }
        List<PmiPayload> pmi = new ArrayList<>();
        for (StepEntity entity : resolved.values()) {
            dispatchPmiPayloads(pmi, entity, targetsByUsageId, builder);
        }
        return List.copyOf(pmi);
    }

    // ========================================================================
    // PMI Payload Construction Methods
    // ========================================================================

    private static void appendPlaceholderPmi(
            StepAnnotationPlaceholderOccurrence placeholderOccurrence,
            List<PmiPayload> pmi,
            StepCadBuilder builder,
            List<PmiTargetPayload> targets
    ) {
        List<CartesianPoint> positions = new ArrayList<>();
        collectPlaceholderPositions(placeholderOccurrence.item(), positions, builder);
        int pointIndex = 0;
        for (CartesianPoint position : positions) {
            String name = placeholderOccurrence.name();
            if (positions.size() > 1) {
                name = name + "[" + pointIndex + "]";
            }
            pmi.add(toStandalonePointPmi(
                    placeholderOccurrence.id() * 1000 + pointIndex,
                    name,
                    position,
                    targets));
            pointIndex++;
        }
    }

    private static void appendAnnotationPlanePmi(
            StepAnnotationPlane annotationPlane,
            List<PmiPayload> pmi,
            StepCadBuilder builder,
            List<PmiTargetPayload> targets
    ) {
        int pointIndex = 0;
        for (StepEntity element : annotationPlane.elements()) {
            CartesianPoint position = null;
            if (element instanceof StepGeometricSet) {
            StepGeometricSet geometricSet = (StepGeometricSet) element;
                position = pointFromGeometricSet(geometricSet, builder);
            }
            if (position == null) {
                position = pointFromAnnotationOccurrence(element, builder);
            }
            if (position == null) {
                position = StepPmiPayloadBuilder.pointFromAnnotationPoint(element, builder);
            }
            if (position == null) {
                continue;
            }
            String name = annotationPlane.elements().size() == 1
                    ? annotationPlane.name()
                    : annotationPlane.name() + "[" + pointIndex + "]";
            pmi.add(toStandalonePointPmi(
                    annotationPlane.id() * 1000 + pointIndex,
                    name,
                    position,
                    targets));
            pointIndex++;
        }
    }

    private static void appendAnnotationOccurrenceRelationshipPmi(
            StepAnnotationOccurrenceRelationship relationship,
            List<PmiPayload> pmi,
            StepCadBuilder builder
    ) {
        CartesianPoint position = pointFromAnnotationOccurrence(relationship.relatedAnnotationOccurrence(), builder);
        if (position == null) {
            position = pointFromAnnotationOccurrence(relationship.relatingAnnotationOccurrence(), builder);
        }
        if (position != null) {
            pmi.add(toStandalonePointPmi(relationship.id(), relationship.name(), position));
        }
    }

    private static void appendDraughtingAnnotationPmi(
            StepDraughtingAnnotationOccurrence annotationOccurrence,
            List<PmiPayload> pmi,
            StepCadBuilder builder,
            List<PmiTargetPayload> targets
    ) {
        CartesianPoint position = pointFromAnnotationOccurrence(annotationOccurrence.item(), builder);
        if (position != null) {
            pmi.add(toStandalonePointPmi(annotationOccurrence.id(), annotationOccurrence.name(), position, targets));
        }
    }

    private static void appendPointSetPmi(StepPointSet pointSet, List<PmiPayload> pmi, StepCadBuilder builder) {
        int pointIndex = 0;
        for (StepEntity item : pointSet.points()) {
            CartesianPoint position = StepPmiPayloadBuilder.pointFromAnnotationPoint(item, builder);
            if (position == null) {
                continue;
            }
            String pointName = pointSet.points().size() == 1
                    ? pointSet.name()
                    : pointSet.name() + "[" + pointIndex + "]";
            pmi.add(toStandalonePointPmi(pointSet.id() * 1000 + pointIndex, pointName, position));
            pointIndex++;
        }
    }

    private static void appendGeometricMeasurementPmi(
            StepGeometricMeasurement measurement,
            List<PmiPayload> pmi,
            StepCadBuilder builder
    ) {
        CartesianPoint position = StepPmiPayloadBuilder.pointFromAnnotationPoint(measurement.measurementGeometry(), builder);
        if (position != null) {
            String label = measurement.name() + " (" + measurement.geometricType() + ")";
            if (measurement.measuredValue() != 0.0) {
                label += ": " + String.format("%.3f", measurement.measuredValue());
            }
            pmi.add(toStandalonePointPmi(measurement.id(), label, position));
        }
        for (StepEntity pt : measurement.measurementPoints()) {
            CartesianPoint mp = StepPmiPayloadBuilder.pointFromAnnotationPoint(pt, builder);
            if (mp != null) {
                pmi.add(toStandalonePointPmi(measurement.id() * 1000 + measurement.measurementPoints().indexOf(pt),
                        measurement.name() + " point", mp));
            }
        }
    }

    private static void appendFillAreaWithOutlinePmi(
            StepFillAreaWithOutline fillArea,
            List<PmiPayload> pmi,
            StepCadBuilder builder
    ) {
        List<CartesianPoint> points = new ArrayList<>();
        for (StepEntity outline : fillArea.outlines()) {
            List<CartesianPoint> sampled = sampleLooseEdgePoints(outline, builder);
            if (sampled != null && !sampled.isEmpty()) {
                points.addAll(sampled);
            }
        }
        if (!points.isEmpty()) {
            CartesianPoint center = points.get(points.size() / 2);
            pmi.add(toStandalonePointPmi(fillArea.id(), fillArea.name(), center));
        }
    }

    private static void appendGeometricTolerancePmi(
            StepGeometricTolerance tolerance,
            List<PmiPayload> pmi,
            StepCadBuilder builder
    ) {
        CartesianPoint position = StepPmiPayloadBuilder.pointFromAnnotationPoint(tolerance.toleratedShape(), builder);
        if (position != null) {
            String label = tolerance.name() != null ? tolerance.name() : "GEOMETRIC_TOLERANCE";
            if (tolerance.magnitude() != 0.0) {
                label += ": " + String.format("%.3f", tolerance.magnitude());
            }
            pmi.add(toStandalonePointPmi(tolerance.id(), label, position));
        }
    }

    private static void appendGeometricToleranceWithDatumPmi(
            StepGeometricToleranceWithDatumReference tolerance,
            List<PmiPayload> pmi,
            StepCadBuilder builder
    ) {
        CartesianPoint position = StepPmiPayloadBuilder.pointFromAnnotationPoint(tolerance.tolerancedFeature(), builder);
        if (position != null) {
            String label = tolerance.name() != null ? tolerance.name() : tolerance.toleranceType();
            if (tolerance.magnitude() != null && tolerance.magnitude() != 0.0) {
                label += ": " + String.format("%.3f", tolerance.magnitude());
            }
            pmi.add(toStandalonePointPmi(tolerance.id(), label, position));
        }
    }

    private static void appendGeometricToleranceWithAreaUnitPmi(
            StepGeometricToleranceWithDefinedAreaUnit tolerance,
            List<PmiPayload> pmi,
            StepCadBuilder builder
    ) {
        CartesianPoint position = StepPmiPayloadBuilder.pointFromAnnotationPoint(tolerance.tolerancedFeature(), builder);
        if (position != null) {
            String label = tolerance.name() != null ? tolerance.name() : tolerance.toleranceType();
            if (tolerance.magnitude() != null && tolerance.magnitude() != 0.0) {
                label += ": " + String.format("%.3f", tolerance.magnitude());
            }
            pmi.add(toStandalonePointPmi(tolerance.id(), label, position));
        }
    }

    private static void appendGeometricToleranceWithMaxPmi(
            StepGeometricToleranceWithMaximumTolerance tolerance,
            List<PmiPayload> pmi,
            StepCadBuilder builder
    ) {
        CartesianPoint position = StepPmiPayloadBuilder.pointFromAnnotationPoint(tolerance.tolerancedFeature(), builder);
        if (position != null) {
            String label = tolerance.name() != null ? tolerance.name() : tolerance.toleranceType();
            if (tolerance.magnitude() != null && tolerance.magnitude() != 0.0) {
                label += ": " + String.format("%.3f", tolerance.magnitude());
            }
            if (tolerance.maximumTolerance() != null) {
                label += " / " + String.format("%.3f", tolerance.maximumTolerance());
            }
            pmi.add(toStandalonePointPmi(tolerance.id(), label, position));
        }
    }

    private static void appendDimensionalLocationPmi(
            StepDimensionalLocation location,
            List<PmiPayload> pmi,
            StepCadBuilder builder
    ) {
        CartesianPoint position = StepPmiPayloadBuilder.pointFromAnnotationPoint(location.relatedShape(), builder);
        if (position != null) {
            String label = location.name() != null ? location.name() : "DIMENSIONAL_LOCATION";
            pmi.add(toStandalonePointPmi(location.id(), label, position));
        }
    }

    private static void appendToleranceZonePmi(
            StepToleranceZone zone,
            List<PmiPayload> pmi,
            StepCadBuilder builder
    ) {
        String zoneShape = null;
        if (zone.form() instanceof StepToleranceZoneForm) {
            StepToleranceZoneForm form = (StepToleranceZoneForm) zone.form();
            zoneShape = form.zoneShape();
        }
        CartesianPoint position = StepPmiPayloadBuilder.pointFromAnnotationPoint(zone.form(), builder);
        if (position != null) {
            String label = zone.name() != null ? zone.name() : "TOLERANCE_ZONE";
            if (zoneShape != null) {
                label += " (" + zoneShape + ")";
            }
            pmi.add(toStandalonePointPmi(zone.id(), label, position));
        }
    }

    private static void appendDatumPmi(
            StepDatum datum,
            List<PmiPayload> pmi,
            StepCadBuilder builder
    ) {
        CartesianPoint position = StepPmiPayloadBuilder.pointFromAnnotationPoint(datum.target(), builder);
        if (position != null) {
            String label = datum.name() != null ? datum.name() : "DATUM";
            pmi.add(toStandalonePointPmi(datum.id(), label, position));
        }
    }

    private static void appendDatumTargetPmi(
            StepDatumTarget datumTarget,
            List<PmiPayload> pmi,
            StepCadBuilder builder
    ) {
        CartesianPoint position = StepPmiPayloadBuilder.pointFromAnnotationPoint(datumTarget.targetShape(), builder);
        if (position != null) {
            String label = datumTarget.name() != null ? datumTarget.name() : "DATUM_TARGET";
            pmi.add(toStandalonePointPmi(datumTarget.id(), label, position));
        }
    }

    private static PmiPayload toStandalonePointPmi(int id, String name, CartesianPoint position) {
        return toStandalonePointPmi(id, name, position, List.of());
    }

    private static PmiPayload toStandalonePointPmi(
            int id,
            String name,
            CartesianPoint position,
            List<PmiTargetPayload> targets
    ) {
        return new PmiPayload(
                name == null || name.isBlank() ? "POINT_" + id : name,
                "",
                PayloadConversionHelper.toPointPayload(position),
                List.of(),
                targets.stream().map(PmiTargetPayload::id).collect(Collectors.toList()),
                targets
        );
    }

    static boolean isSupportedPmiUsageCarrier(StepEntity entity) {
        return entity instanceof StepDraughtingCallout
                || entity instanceof StepAnnotationSymbol
                || entity instanceof StepAnnotationText
                || entity instanceof StepAnnotationTextCharacter
                || entity instanceof StepAnnotationFillArea
                || entity instanceof StepAnnotationTextOccurrence
                || entity instanceof StepAnnotationPointOccurrence
                || entity instanceof StepAnnotationFillAreaOccurrence
                || entity instanceof StepAnnotationPlaceholderOccurrence
                || entity instanceof StepAnnotationPlane
                || entity instanceof StepAnnotationSymbolOccurrence
                || entity instanceof StepAnnotationSubfigureOccurrence
                || entity instanceof StepDraughtingAnnotationOccurrence
                || entity instanceof StepTerminatorSymbol;
    }

    private static PmiPayload toPmiPayload(
            StepDraughtingCallout callout,
            List<PmiTargetPayload> targets,
            StepCadBuilder builder
    ) {
        StepAnnotationTextOccurrence text = null;
        List<PointPayload> leader = new ArrayList<>();
        for (StepEntity content : callout.contents()) {
            if (content instanceof StepAnnotationTextOccurrence) {
            StepAnnotationTextOccurrence annotationText = (StepAnnotationTextOccurrence) content;
                text = annotationText;
            } else {
                appendPmiLeader(content, leader, builder);
            }
        }
        if (text == null) {
            return null;
        }
        CartesianPoint position = StepPmiPayloadBuilder.pointFromAnnotationPoint(text.position(), builder);
        if (position == null) {
            return null;
        }
        return new PmiPayload(
                callout.name(),
                text.text(),
                PayloadConversionHelper.toPointPayload(position),
                List.copyOf(leader),
                targets.stream().map(PmiTargetPayload::id).collect(Collectors.toList()),
                List.copyOf(targets)
        );
    }

    // ========================================================================
    // PMI Leader Methods
    // ========================================================================

    @FunctionalInterface
    private interface PmiLeaderHandler {
        void handle(StepEntity content, List<PointPayload> leader, StepCadBuilder builder);
    }

    private record PmiLeaderRule(Class<?> type, Predicate<StepEntity> guard, PmiLeaderHandler handler) {
        boolean matches(StepEntity content) {
            return type.isInstance(content) && (guard == null || guard.test(content));
        }
    }

    private static PmiLeaderRule leaderRule(Class<?> type, PmiLeaderHandler handler) {
        return new PmiLeaderRule(type, null, handler);
    }

    /** Delegate to the leader of a single referenced entity. */
    private static PmiLeaderRule delegateRule(Class<?> type, Function<StepEntity, StepEntity> next) {
        return leaderRule(type, (content, leader, builder) -> appendPmiLeader(next.apply(content), leader, builder));
    }

    /** Recurse over each member of a contained entity list. */
    private static PmiLeaderRule membersRule(Class<?> type, Function<StepEntity, List<? extends StepEntity>> members) {
        return leaderRule(type, (content, leader, builder) -> {
            for (StepEntity member : members.apply(content)) {
                appendPmiLeader(member, leader, builder);
            }
        });
    }

    /** Path/loop families whose leader is built from oriented-edge lists. */
    private static PmiLeaderRule pathLeaderRule(Class<?> type, Function<StepEntity, List<StepOrientedEdge>> edges) {
        return leaderRule(type, (content, leader, builder) -> appendPmiPathLeader(edges.apply(content), leader, builder));
    }

    /** Content types whose leader points come from loose-edge sampling. */
    private static PmiLeaderRule sampledPointsRule(Class<?> type) {
        return leaderRule(type, (content, leader, builder) -> addSampledPoints(content, leader, builder));
    }

    /** Leader consisting of one extracted point. */
    private static PmiLeaderRule pointRule(Class<?> type, Function<StepEntity, CartesianPoint> point) {
        return leaderRule(type, (content, leader, builder) ->
                leader.add(PayloadConversionHelper.toPointPayload(point.apply(content))));
    }

    private static void addSampledPoints(StepEntity content, List<PointPayload> leader, StepCadBuilder builder) {
        List<CartesianPoint> sampled = sampleLooseEdgePoints(content, builder);
        addSampledPoints(sampled, leader);
    }

    private static void addSampledPoints(List<CartesianPoint> sampled, List<PointPayload> leader) {
        if (sampled != null) {
            for (CartesianPoint point : sampled) {
                leader.add(PayloadConversionHelper.toPointPayload(point));
            }
        }
    }

    /** Solid types summarized through builder.buildSolid; unsupported solids contribute no leader points. */
    private static final List<Class<?>> SOLID_LEADER_TYPES = List.of(
            StepSweptAreaSolid.class,
            StepSolidReplica.class,
            StepCsgSolid.class,
            StepCsgPrimitive.class,
            StepBooleanResult.class,
            StepBooleanClippingResult.class,
            StepSweptDiskSolid.class,
            StepExtrudedAreaSolidTapered.class,
            StepRevolvedAreaSolidTapered.class,
            StepSurfaceCurveSweptAreaSolid.class,
            StepPolygonalBoundedHalfSpace.class,
            StepComplexClippingResult.class
    );

    /**
     * PMI leader rules keyed by concrete type, replacing the former 50-branch
     * if/else-if chain. Order mirrors the original chain (first match wins);
     * any content that matches no rule falls back to loose-edge sampling, as
     * the trailing branch of the old chain did.
     */
    private static final List<PmiLeaderRule> PMI_LEADER_RULES = List.of(
            membersRule(StepGeometricSet.class, content -> ((StepGeometricSet) content).elements()),
            membersRule(StepGeometricCurveSet.class, content -> ((StepGeometricCurveSet) content).elements()),
            membersRule(StepPointSet.class, content -> ((StepPointSet) content).points()),
            delegateRule(StepAnnotationPlaceholderOccurrence.class, content -> ((StepAnnotationPlaceholderOccurrence) content).item()),
            membersRule(StepAnnotationPlane.class, content -> ((StepAnnotationPlane) content).elements()),
            membersRule(StepFaceBasedSurfaceModel.class, content -> ((StepFaceBasedSurfaceModel) content).faceSets()),
            membersRule(StepShellBasedSurfaceModel.class, content -> ((StepShellBasedSurfaceModel) content).shells()),
            delegateRule(StepManifoldSolidBrep.class, content -> ((StepManifoldSolidBrep) content).outer()),
            leaderRule(StepBrepWithVoids.class, (content, leader, builder) -> {
                StepBrepWithVoids solid = (StepBrepWithVoids) content;
                appendPmiLeader(solid.outer(), leader, builder);
                for (StepEntity voidShell : solid.voids()) {
                    appendPmiLeader(voidShell, leader, builder);
                }
            }),
            new PmiLeaderRule(StepEntity.class,
                    content -> SOLID_LEADER_TYPES.stream().anyMatch(type -> type.isInstance(content)),
                    (content, leader, builder) -> appendPmiLeaderForSolid(content, leader, builder)),
            membersRule(StepAdvancedFace.class, content -> ((StepAdvancedFace) content).bounds()),
            membersRule(StepFaceSurface.class, content -> ((StepFaceSurface) content).bounds()),
            delegateRule(StepOrientedFace.class, content -> ((StepOrientedFace) content).faceElement()),
            delegateRule(StepFaceBound.class, content -> ((StepFaceBound) content).loop()),
            membersRule(StepOpenShell.class, content -> ((StepOpenShell) content).faces()),
            membersRule(StepSurfacedOpenShell.class, content -> ((StepSurfacedOpenShell) content).faces()),
            delegateRule(StepOrientedOpenShell.class, content -> ((StepOrientedOpenShell) content).openShellElement()),
            membersRule(StepClosedShell.class, content -> ((StepClosedShell) content).faces()),
            delegateRule(StepOrientedClosedShell.class, content -> ((StepOrientedClosedShell) content).closedShellElement()),
            membersRule(StepConnectedFaceSet.class, content -> ((StepConnectedFaceSet) content).faces()),
            membersRule(StepConnectedFaceSubSet.class, content -> ((StepConnectedFaceSubSet) content).faces()),
            delegateRule(StepAnnotationPointOccurrence.class, content -> ((StepAnnotationPointOccurrence) content).item()),
            delegateRule(StepAnnotationCurveOccurrence.class, content -> ((StepAnnotationCurveOccurrence) content).item()),
            leaderRule(StepAnnotationFillArea.class, (content, leader, builder) ->
                    addSampledPoints(sampleAnnotationFillAreaPoints((StepAnnotationFillArea) content, builder), leader)),
            delegateRule(StepAnnotationFillAreaOccurrence.class, content -> ((StepAnnotationFillAreaOccurrence) content).item()),
            sampledPointsRule(StepAnnotationSymbol.class),
            delegateRule(StepAnnotationSymbolOccurrence.class, content -> ((StepAnnotationSymbolOccurrence) content).item()),
            delegateRule(StepAnnotationSubfigureOccurrence.class, content -> ((StepAnnotationSubfigureOccurrence) content).item()),
            sampledPointsRule(StepAnnotationText.class),
            sampledPointsRule(StepAnnotationTextCharacter.class),
            delegateRule(StepDimensionCurve.class, content -> ((StepDimensionCurve) content).item()),
            delegateRule(StepLeaderCurve.class, content -> ((StepLeaderCurve) content).item()),
            delegateRule(StepProjectionCurve.class, content -> ((StepProjectionCurve) content).item()),
            delegateRule(StepDraughtingAnnotationOccurrence.class, content -> ((StepDraughtingAnnotationOccurrence) content).item()),
            delegateRule(StepTerminatorSymbol.class, content -> ((StepTerminatorSymbol) content).annotatedCurve()),
            pathLeaderRule(StepPath.class, content -> ((StepPath) content).edges()),
            pathLeaderRule(StepOpenPath.class, content -> ((StepOpenPath) content).edges()),
            pathLeaderRule(StepSubpath.class, content -> ((StepSubpath) content).edges()),
            pathLeaderRule(StepOrientedPath.class, content -> ((StepOrientedPath) content).edges()),
            membersRule(StepConnectedEdgeSet.class, content -> ((StepConnectedEdgeSet) content).edges()),
            membersRule(StepEdgeBasedWireframeModel.class, content -> ((StepEdgeBasedWireframeModel) content).boundaries()),
            membersRule(StepShellBasedWireframeModel.class, content -> ((StepShellBasedWireframeModel) content).boundaries()),
            membersRule(StepWireShell.class, content -> ((StepWireShell) content).loops()),
            pathLeaderRule(StepEdgeLoop.class, content -> ((StepEdgeLoop) content).edges()),
            pointRule(StepVertexLoop.class, content ->
                    StepPointExtractor.pointFromStep(((StepVertexLoop) content).loopVertex().point())),
            leaderRule(StepPolyLoop.class, (content, leader, builder) -> {
                for (StepCartesianPoint point : ((StepPolyLoop) content).polygon()) {
                    leader.add(PayloadConversionHelper.toPointPayload(StepPointExtractor.pointFromStep(point)));
                }
            }),
            pointRule(StepVertexShell.class, content ->
                    StepPointExtractor.pointFromStep(((StepVertexShell) content).extent().loopVertex().point())),
            new PmiLeaderRule(StepGeometricReplica.class,
                    content -> "POINT_REPLICA".equals(((StepGeometricReplica) content).entityName()),
                    (content, leader, builder) -> {
                        CartesianPoint point = pointFromReplica((StepGeometricReplica) content, builder);
                        if (point != null) {
                            leader.add(PayloadConversionHelper.toPointPayload(point));
                        }
                    }),
            pointRule(StepCartesianPoint.class, content ->
                    StepPointExtractor.pointFromStep((StepCartesianPoint) content)),
            pointRule(StepVertexPoint.class, content ->
                    StepPointExtractor.pointFromStep(((StepVertexPoint) content).point()))
    );

    private static void appendPmiLeader(
            StepEntity content,
            List<PointPayload> leader,
            StepCadBuilder builder
    ) {
        for (PmiLeaderRule rule : PMI_LEADER_RULES) {
            if (rule.matches(content)) {
                rule.handler().handle(content, leader, builder);
                return;
            }
        }
        addSampledPoints(content, leader, builder);
    }

    private static void appendPmiLeaderForSolid(
            StepEntity solidEntity,
            List<PointPayload> leader,
            StepCadBuilder builder
    ) {
        try {
            Solid solid = builder.buildSolid(solidEntity.id());
            for (Face face : solid.outerShell().faces()) {
                appendPmiLeader(face, leader, builder);
            }
            for (Shell voidShell : solid.voidShells()) {
                for (Face face : voidShell.faces()) {
                    appendPmiLeader(face, leader, builder);
                }
            }
        } catch (UnsupportedGeometryException | StepResolutionException | TopologyException ex) {
            // Callout leader extraction is best-effort; unsupported solid content should not fail export.
        }
    }

    private static void appendPmiLeader(
            Face face,
            List<PointPayload> leader,
            StepCadBuilder builder
    ) {
        for (FaceBound bound : face.bounds()) {
            appendPmiLeader(bound.loop(), leader, builder);
        }
    }

    private static void appendPmiLeader(
            Loop loop,
            List<PointPayload> leader,
            StepCadBuilder builder
    ) {
        if (loop instanceof EdgeLoop) {
            EdgeLoop edgeLoop = (EdgeLoop) loop;
            for (OrientedEdge edge : edgeLoop.edges()) {
                appendTopologyEdgeLeader(edge, leader);
            }
            return;
        }
        if (loop instanceof VertexLoop) {
            VertexLoop vertexLoop = (VertexLoop) loop;
            leader.add(PayloadConversionHelper.toPointPayload(vertexLoop.vertex().point()));
            return;
        }
        if (loop instanceof PolyLoop) {
            PolyLoop polyLoop = (PolyLoop) loop;
            for (CartesianPoint point : polyLoop.points()) {
                leader.add(PayloadConversionHelper.toPointPayload(point));
            }
        }
    }

    private static void appendTopologyEdgeLeader(
            OrientedEdge orientedEdge,
            List<PointPayload> leader
    ) {
        List<CartesianPoint> points = sampleLooseCurve(orientedEdge.edge().curve());
        if (!orientedEdge.orientation()) {
            List<CartesianPoint> reversed = new ArrayList<>(points);
            Collections.reverse(reversed);
            points = reversed;
        }
        for (CartesianPoint point : points) {
            leader.add(PayloadConversionHelper.toPointPayload(point));
        }
    }

    private static void appendPmiPathLeader(
            List<StepOrientedEdge> edges,
            List<PointPayload> leader,
            StepCadBuilder builder
    ) {
        for (StepOrientedEdge edge : edges) {
            List<CartesianPoint> points = sampleLooseEdgePoints(edge.edgeElement(), builder);
            if (points == null) {
                continue;
            }
            for (CartesianPoint point : points) {
                leader.add(PayloadConversionHelper.toPointPayload(point));
            }
        }
    }

    // ========================================================================
    // PMI Target Methods
    // ========================================================================

    // ========================================================================
    // Point Extraction Methods
    // ========================================================================

    @FunctionalInterface
    private interface OccurrencePointHandler {
        CartesianPoint point(StepEntity occurrence, StepCadBuilder builder);
    }

    private record OccurrencePointRule(Class<?> type, Predicate<StepEntity> guard, OccurrencePointHandler handler) {
        boolean matches(StepEntity occurrence) {
            return type.isInstance(occurrence) && (guard == null || guard.test(occurrence));
        }
    }

    private static OccurrencePointRule pointRule(Class<?> type, OccurrencePointHandler handler) {
        return new OccurrencePointRule(type, null, handler);
    }

    /** Recurse on a referenced occurrence entity. */
    private static OccurrencePointRule recursePointRule(Class<?> type, Function<StepEntity, StepEntity> next) {
        return pointRule(type, (occurrence, builder) -> pointFromAnnotationOccurrence(next.apply(occurrence), builder));
    }

    /** First point of the loose-edge sampling of a referenced entity. */
    private static OccurrencePointRule curveCarrierPointRule(Class<?> type, Function<StepEntity, StepEntity> next) {
        return pointRule(type, (occurrence, builder) -> pointFromCurveCarrier(next.apply(occurrence), builder));
    }

    /** Point of a directly carried annotation point entity. */
    private static OccurrencePointRule annotationPointRule(Class<?> type, Function<StepEntity, StepEntity> next) {
        return pointRule(type, (occurrence, builder) -> pointFromAnnotationPoint(next.apply(occurrence), builder));
    }

    /** Point read off a referenced placement entity. */
    private static OccurrencePointRule placementPointRule(Class<?> type, Function<StepEntity, StepEntity> next) {
        return pointRule(type, (occurrence, builder) -> pointFromPlacement(next.apply(occurrence)));
    }

    /**
     * Annotation-occurrence point rules keyed by concrete type, replacing the
     * former 22-branch if/else-if chain. Order mirrors the original chain
     * (first match wins); unmatched occurrences yield null.
     */
    private static final List<OccurrencePointRule> ANNOTATION_POINT_RULES = List.of(
            annotationPointRule(StepAnnotationPointOccurrence.class, occurrence -> ((StepAnnotationPointOccurrence) occurrence).item()),
            curveCarrierPointRule(StepAnnotationCurveOccurrence.class, occurrence -> ((StepAnnotationCurveOccurrence) occurrence).item()),
            curveCarrierPointRule(StepLeaderCurve.class, occurrence -> ((StepLeaderCurve) occurrence).item()),
            curveCarrierPointRule(StepDimensionCurve.class, occurrence -> ((StepDimensionCurve) occurrence).item()),
            curveCarrierPointRule(StepProjectionCurve.class, occurrence -> ((StepProjectionCurve) occurrence).item()),
            annotationPointRule(StepAnnotationFillAreaOccurrence.class, occurrence -> ((StepAnnotationFillAreaOccurrence) occurrence).fillStyleTarget()),
            pointRule(StepAnnotationFillArea.class, (occurrence, builder) ->
                    pointFromAnnotationFillArea((StepAnnotationFillArea) occurrence, builder)),
            pointRule(StepAnnotationSymbol.class, (occurrence, builder) ->
                    pointFromAnnotationSymbol((StepAnnotationSymbol) occurrence)),
            recursePointRule(StepAnnotationSymbolOccurrence.class, occurrence -> ((StepAnnotationSymbolOccurrence) occurrence).item()),
            recursePointRule(StepAnnotationSubfigureOccurrence.class, occurrence -> ((StepAnnotationSubfigureOccurrence) occurrence).item()),
            pointRule(StepAnnotationPlaceholderOccurrence.class, (occurrence, builder) ->
                    pointFromPlaceholderItem(((StepAnnotationPlaceholderOccurrence) occurrence).item(), builder)),
            pointRule(StepAnnotationPlane.class, (occurrence, builder) ->
                    pointFromAnnotationPlane((StepAnnotationPlane) occurrence, builder)),
            placementPointRule(StepAnnotationText.class, occurrence -> ((StepAnnotationText) occurrence).mappingTarget()),
            placementPointRule(StepAnnotationTextCharacter.class, occurrence -> ((StepAnnotationTextCharacter) occurrence).mappingTarget()),
            annotationPointRule(StepAnnotationTextOccurrence.class, occurrence -> ((StepAnnotationTextOccurrence) occurrence).position()),
            recursePointRule(StepDraughtingAnnotationOccurrence.class, occurrence -> ((StepDraughtingAnnotationOccurrence) occurrence).item()),
            pointRule(StepTerminatorSymbol.class, (occurrence, builder) -> {
                StepTerminatorSymbol terminatorSymbol = (StepTerminatorSymbol) occurrence;
                CartesianPoint position = pointFromAnnotationOccurrence(terminatorSymbol.item(), builder);
                if (position == null) {
                    position = pointFromAnnotationOccurrence(terminatorSymbol.annotatedCurve(), builder);
                }
                return position;
            }),
            pointRule(StepPointSet.class, (occurrence, builder) ->
                    pointFromPointSet((StepPointSet) occurrence, builder)),
            pointRule(StepGeometricSet.class, (occurrence, builder) ->
                    pointFromGeometricSet((StepGeometricSet) occurrence, builder)),
            pointRule(StepGeometricCurveSet.class, (occurrence, builder) ->
                    pointFromGeometricCurveSet((StepGeometricCurveSet) occurrence, builder)),
            pointRule(StepVertexShell.class, (occurrence, builder) ->
                    StepPointExtractor.pointFromStep(((StepVertexShell) occurrence).extent().loopVertex().point())),
            new OccurrencePointRule(StepGeometricReplica.class,
                    occurrence -> "POINT_REPLICA".equals(((StepGeometricReplica) occurrence).entityName()),
                    (occurrence, builder) -> builder == null ? null : pointFromReplica((StepGeometricReplica) occurrence, builder))
    );

    private static CartesianPoint pointFromAnnotationOccurrence(StepEntity occurrence, StepCadBuilder builder) {
        for (OccurrencePointRule rule : ANNOTATION_POINT_RULES) {
            if (rule.matches(occurrence)) {
                return rule.handler().point(occurrence, builder);
            }
        }
        return null;
    }

    private static CartesianPoint pointFromCurveCarrier(StepEntity item, StepCadBuilder builder) {
        List<CartesianPoint> sampled = sampleLooseEdgePoints(item, builder);
        if (sampled == null || sampled.isEmpty()) {
            return null;
        }
        return sampled.get(0);
    }

    private static CartesianPoint pointFromGeometricSet(StepGeometricSet geometricSet, StepCadBuilder builder) {
        for (StepEntity element : geometricSet.elements()) {
            CartesianPoint point = pointFromAnnotationOccurrence(element, builder);
            if (point != null) {
                return point;
            }
            point = StepPmiPayloadBuilder.pointFromAnnotationPoint(element, builder);
            if (point != null) {
                return point;
            }
            point = pointFromCurveCarrier(element, builder);
            if (point != null) {
                return point;
            }
        }
        return null;
    }

    private static CartesianPoint pointFromGeometricCurveSet(StepGeometricCurveSet curveSet, StepCadBuilder builder) {
        for (StepEntity element : curveSet.elements()) {
            CartesianPoint point = pointFromAnnotationOccurrence(element, builder);
            if (point != null) {
                return point;
            }
            point = StepPmiPayloadBuilder.pointFromAnnotationPoint(element, builder);
            if (point != null) {
                return point;
            }
            point = pointFromCurveCarrier(element, builder);
            if (point != null) {
                return point;
            }
        }
        return null;
    }

    private static CartesianPoint pointFromAnnotationPlane(StepAnnotationPlane annotationPlane, StepCadBuilder builder) {
        for (StepEntity element : annotationPlane.elements()) {
            if (element instanceof StepGeometricSet) {
            StepGeometricSet geometricSet = (StepGeometricSet) element;
                CartesianPoint point = pointFromGeometricSet(geometricSet, builder);
                if (point != null) {
                    return point;
                }
            }
            CartesianPoint point = pointFromAnnotationOccurrence(element, builder);
            if (point != null) {
                return point;
            }
            point = StepPmiPayloadBuilder.pointFromAnnotationPoint(element, builder);
            if (point != null) {
                return point;
            }
        }
        return null;
    }

    private static CartesianPoint pointFromPointSet(StepPointSet pointSet, StepCadBuilder builder) {
        for (StepEntity item : pointSet.points()) {
            CartesianPoint point = StepPmiPayloadBuilder.pointFromAnnotationPoint(item, builder);
            if (point != null) {
                return point;
            }
        }
        return null;
    }

    private static CartesianPoint pointFromPlaceholderItem(StepEntity item, StepCadBuilder builder) {
        if (item instanceof StepGeometricSet) {
            StepGeometricSet geometricSet = (StepGeometricSet) item;
            return pointFromGeometricSet(geometricSet, builder);
        }
        if (item instanceof StepGeometricCurveSet) {
            StepGeometricCurveSet curveSet = (StepGeometricCurveSet) item;
            return pointFromGeometricCurveSet(curveSet, builder);
        }
        if (item instanceof StepPointSet) {
            StepPointSet pointSet = (StepPointSet) item;
            return pointFromPointSet(pointSet, builder);
        }
        if (item instanceof StepAnnotationPlane) {
            StepAnnotationPlane annotationPlane = (StepAnnotationPlane) item;
            return pointFromAnnotationPlane(annotationPlane, builder);
        }
        CartesianPoint point = pointFromAnnotationOccurrence(item, builder);
        if (point != null) {
            return point;
        }
        return StepPmiPayloadBuilder.pointFromAnnotationPoint(item, builder);
    }

    private static void collectPlaceholderPositions(
            StepEntity item,
            List<CartesianPoint> positions,
            StepCadBuilder builder
    ) {
        if (item instanceof StepPointSet) {
            StepPointSet pointSet = (StepPointSet) item;
            for (StepEntity point : pointSet.points()) {
                collectPlaceholderPositions(point, positions, builder);
            }
            return;
        }
        if (item instanceof StepGeometricSet) {
            StepGeometricSet geometricSet = (StepGeometricSet) item;
            for (StepEntity element : geometricSet.elements()) {
                collectPlaceholderPositions(element, positions, builder);
            }
            return;
        }
        if (item instanceof StepGeometricCurveSet) {
            StepGeometricCurveSet curveSet = (StepGeometricCurveSet) item;
            for (StepEntity element : curveSet.elements()) {
                collectPlaceholderPositions(element, positions, builder);
            }
            return;
        }
        if (item instanceof StepAnnotationPlane) {
            StepAnnotationPlane annotationPlane = (StepAnnotationPlane) item;
            for (StepEntity element : annotationPlane.elements()) {
                collectPlaceholderPositions(element, positions, builder);
            }
            return;
        }
        CartesianPoint point = pointFromAnnotationOccurrence(item, builder);
        if (point == null) {
            point = StepPmiPayloadBuilder.pointFromAnnotationPoint(item, builder);
        }
        if (point != null) {
            positions.add(point);
        }
    }

    private static CartesianPoint pointFromPlacement(StepEntity placement) {
        if (placement instanceof StepAxis2Placement3D) {
            StepAxis2Placement3D placement3D = (StepAxis2Placement3D) placement;
            return StepPointExtractor.pointFromStep(placement3D.location());
        }
        if (placement instanceof StepAxis2Placement2D) {
            StepAxis2Placement2D placement2D = (StepAxis2Placement2D) placement;
            StepCartesianPoint point = placement2D.location();
            return new CartesianPoint(point.coordinates().get(0), point.coordinates().get(1), 0.0);
        }
        return null;
    }

    private static CartesianPoint pointFromAnnotationSymbol(StepAnnotationSymbol annotationSymbol) {
        return pointFromPlacement(annotationSymbol.mappingTarget());
    }

    private static CartesianPoint pointFromAnnotationFillArea(
            StepAnnotationFillArea fillArea,
            StepCadBuilder builder
    ) {
        List<CartesianPoint> sampled = sampleAnnotationFillAreaPoints(fillArea, builder);
        if (sampled == null || sampled.isEmpty()) {
            return null;
        }
        return sampled.get(0);
    }

    // ========================================================================
    // Sample Methods
    // ========================================================================

    private static List<CartesianPoint> sampleLooseEdgePoints(StepEntity entity, StepCadBuilder builder) {
        return StepEdgePayloadBuilder.sampleLooseEdgePoints(entity, builder);
    }

    private static List<CartesianPoint> sampleLooseCurve(Curve3 curve) {
        return Curve3SamplingHelper.sampleLooseCurve(curve);
    }

    private static List<CartesianPoint> sampleAnnotationFillAreaPoints(
            StepAnnotationFillArea fillArea,
            StepCadBuilder builder
    ) {
        return StepEdgePayloadBuilder.sampleAnnotationFillAreaPoints(fillArea, builder);
    }

    // ========================================================================
    // Helper Methods for Target Collection
    // ========================================================================

    // ========================================================================
    // Semantic Target Collection
    // ========================================================================

    // ========================================================================
    // Utility Methods


    public static CartesianPoint pointFromAnnotationPoint(StepEntity item, StepCadBuilder builder) {
        if (item instanceof StepCartesianPoint) {
            StepCartesianPoint point = (StepCartesianPoint) item;
            return StepPointExtractor.pointFromStep(point);
        }
        if (item instanceof StepVertexPoint) {
            StepVertexPoint vertexPoint = (StepVertexPoint) item;
            return StepPointExtractor.pointFromStep(vertexPoint.point());
        }
        if (item instanceof StepVertexShell) {
            StepVertexShell vertexShell = (StepVertexShell) item;
            return StepPointExtractor.pointFromStep(vertexShell.extent().loopVertex().point());
        }
        if (item instanceof StepPointSet) {
            StepPointSet pointSet = (StepPointSet) item;
            return StepPmiPayloadBuilder.pointFromPointSet(pointSet, builder);
        }
        if (item instanceof StepGeometricSet) {
            StepGeometricSet geometricSet = (StepGeometricSet) item;
            return StepPmiPayloadBuilder.pointFromGeometricSet(geometricSet, builder);
        }
        if (item instanceof StepGeometricCurveSet) {
            StepGeometricCurveSet curveSet = (StepGeometricCurveSet) item;
            return StepPmiPayloadBuilder.pointFromGeometricCurveSet(curveSet, builder);
        }
        if (item instanceof StepAnnotationSymbol
                || item instanceof StepAnnotationText
                || item instanceof StepAnnotationTextCharacter
                || item instanceof StepAnnotationFillArea) {
            return StepPmiPayloadBuilder.pointFromAnnotationOccurrence(item, builder);
        }
        if (item instanceof StepAnnotationPointOccurrence
                || item instanceof StepAnnotationFillAreaOccurrence
                || item instanceof StepAnnotationTextOccurrence
                || item instanceof StepAnnotationPlaceholderOccurrence
                || item instanceof StepAnnotationSymbolOccurrence
                || item instanceof StepAnnotationSubfigureOccurrence
                || item instanceof StepDraughtingAnnotationOccurrence
                || item instanceof StepAnnotationPlane) {
            return StepPmiPayloadBuilder.pointFromAnnotationOccurrence(item, builder);
        }
        if (builder != null && item instanceof StepGeometricReplica) {
            StepGeometricReplica replica = (StepGeometricReplica) item;
            if ("POINT_REPLICA".equals(replica.entityName())) {
                return pointFromReplica(replica, builder);
            }
        }
        return null;
    }


    public static CartesianPoint pointFromReplica(StepGeometricReplica replica, StepCadBuilder builder) {
        if (replica.parent() instanceof StepCartesianPoint) {
            StepCartesianPoint point = (StepCartesianPoint) replica.parent();
            return StepPointExtractor.transformPoint(StepPointExtractor.pointFromStep(point), replica.transformation(), builder);
        }
        if (replica.parent() instanceof StepVertexPoint) {
            StepVertexPoint vertexPoint = (StepVertexPoint) replica.parent();
            return StepPointExtractor.transformPoint(StepPointExtractor.pointFromStep(vertexPoint.point()), replica.transformation(), builder);
        }
        return null;
    }


    /**
     * Dispatch table behind the Region A usage-target collection in
     * buildPmiPayloads.
     *
     * Replaces a 9-branch if/else-if {@code instanceof} chain (lines 78..177 of
     * the original). The order below is load bearing: {@code instanceof} also
     * matches subtypes and the original chain was "first match wins", so entries
     * keep their original relative order.
     *
     * Each branch body was moved verbatim into a handler; the handlers mutate the
     * caller's targetsByUsageId map (exactly what the branches did), so behaviour
     * is unchanged. Region B (a 1-branch chain) and Region C (which builds the
     * pmi list and contains continue) are intentionally NOT part of this table.
     */
    @FunctionalInterface
    private interface PmiUsageTargetHandler {
        void handle(
                StepEntity entity,
                Map<Integer, StepEntity> resolved,
                Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
                Map<Integer, List<String>> instanceIdsByTargetId
        );
    }

    private record PmiUsageTargetRule(Class<?> type, PmiUsageTargetHandler handler) {
        boolean matches(StepEntity entity) {
            return type.isInstance(entity);
        }
    }

    private static PmiUsageTargetRule pmiUsageTargetRule(Class<?> type, PmiUsageTargetHandler handler) {
        return new PmiUsageTargetRule(type, handler);
    }

    private static final List<PmiUsageTargetRule> PMI_USAGE_TARGET_RULES = List.of(
            pmiUsageTargetRule(StepGeometricItemSpecificUsage.class, (
                    entity,
                    resolved,
                    targetsByUsageId,
                    instanceIdsByTargetId
            ) -> {
            StepGeometricItemSpecificUsage usage = (StepGeometricItemSpecificUsage) entity;
            StepPmiTargetBuilder.appendPmiTarget(
            targetsByUsageId,
            usage.usage().id(),
            usage.identifiedItem(),
            instanceIdsByTargetId,
            null,
            null,
            "GEOMETRIC_ITEM_SPECIFIC_USAGE",
            usage.id()
            );
            }),
            pmiUsageTargetRule(StepChainBasedGeometricItemSpecificUsage.class, (
                    entity,
                    resolved,
                    targetsByUsageId,
                    instanceIdsByTargetId
            ) -> {
            StepChainBasedGeometricItemSpecificUsage usage = (StepChainBasedGeometricItemSpecificUsage) entity;
            StepPmiTargetBuilder.appendPmiTarget(
            targetsByUsageId,
            usage.usage().id(),
            usage.identifiedItem(),
            instanceIdsByTargetId,
            null,
            null,
            "CHAIN_BASED_GEOMETRIC_ITEM_SPECIFIC_USAGE",
            usage.id()
            );
            }),
            pmiUsageTargetRule(StepItemIdentifiedRepresentationUsage.class, (
                    entity,
                    resolved,
                    targetsByUsageId,
                    instanceIdsByTargetId
            ) -> {
            StepItemIdentifiedRepresentationUsage usage = (StepItemIdentifiedRepresentationUsage) entity;
            StepPmiTargetBuilder.appendRepresentationBacklinkTarget(
            targetsByUsageId,
            usage.identifiedItem(),
            usage.usedRepresentation(),
            instanceIdsByTargetId,
            "ITEM_IDENTIFIED_REPRESENTATION_USAGE",
            usage.id()
            );
            StepPmiTargetBuilder.appendAttachedRepresentationRelationshipTargets(
            targetsByUsageId,
            usage.identifiedItem(),
            usage.usedRepresentation(),
            resolved,
            instanceIdsByTargetId
            );
            }),
            pmiUsageTargetRule(StepChainBasedItemIdentifiedRepresentationUsage.class, (
                    entity,
                    resolved,
                    targetsByUsageId,
                    instanceIdsByTargetId
            ) -> {
            StepChainBasedItemIdentifiedRepresentationUsage usage = (StepChainBasedItemIdentifiedRepresentationUsage) entity;
            StepPmiTargetBuilder.appendRepresentationBacklinkTarget(
            targetsByUsageId,
            usage.identifiedItem(),
            usage.leaf(),
            instanceIdsByTargetId,
            "CHAIN_BASED_ITEM_IDENTIFIED_REPRESENTATION_USAGE",
            usage.id()
            );
            StepPmiTargetBuilder.appendAttachedRepresentationRelationshipTargets(
            targetsByUsageId,
            usage.identifiedItem(),
            usage.leaf(),
            resolved,
            instanceIdsByTargetId
            );
            }),
            pmiUsageTargetRule(StepPlacedTarget.class, (entity, resolved, targetsByUsageId, instanceIdsByTargetId) -> {
            StepPlacedTarget usage = (StepPlacedTarget) entity;
            StepPmiTargetBuilder.appendRepresentationBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), instanceIdsByTargetId, "PLACED_TARGET", usage.id());
            StepPmiTargetBuilder.appendAttachedRepresentationRelationshipTargets(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), resolved, instanceIdsByTargetId);
            StepPmiTargetBuilder.appendDefinitionBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), usage.definition(), instanceIdsByTargetId);
            StepPmiTargetBuilder.appendRelationshipBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), usage.definition(), instanceIdsByTargetId);
            StepPmiTargetBuilder.appendSemanticDefinitionTargets(targetsByUsageId, usage.identifiedItem(), usage.definition(), resolved, instanceIdsByTargetId);
            }),
            pmiUsageTargetRule(StepDraughtingModelItemAssociation.class, (
                    entity,
                    resolved,
                    targetsByUsageId,
                    instanceIdsByTargetId
            ) -> {
            StepDraughtingModelItemAssociation usage = (StepDraughtingModelItemAssociation) entity;
            StepPmiTargetBuilder.appendRepresentationBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), instanceIdsByTargetId, "DRAUGHTING_MODEL_ITEM_ASSOCIATION", usage.id());
            StepPmiTargetBuilder.appendAttachedRepresentationRelationshipTargets(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), resolved, instanceIdsByTargetId);
            StepPmiTargetBuilder.appendDefinitionBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), usage.definition(), instanceIdsByTargetId);
            StepPmiTargetBuilder.appendRelationshipBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), usage.definition(), instanceIdsByTargetId);
            StepPmiTargetBuilder.appendSemanticDefinitionTargets(targetsByUsageId, usage.identifiedItem(), usage.definition(), resolved, instanceIdsByTargetId);
            }),
            pmiUsageTargetRule(StepDraughtingModelItemAssociationWithPlaceholder.class, (
                    entity,
                    resolved,
                    targetsByUsageId,
                    instanceIdsByTargetId
            ) -> {
            StepDraughtingModelItemAssociationWithPlaceholder usage = (StepDraughtingModelItemAssociationWithPlaceholder) entity;
            StepPmiTargetBuilder.appendRepresentationBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), instanceIdsByTargetId, "DRAUGHTING_MODEL_ITEM_ASSOCIATION_WITH_PLACEHOLDER", usage.id());
            StepPmiTargetBuilder.appendAttachedRepresentationRelationshipTargets(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), resolved, instanceIdsByTargetId);
            StepPmiTargetBuilder.appendDefinitionBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), usage.definition(), instanceIdsByTargetId);
            StepPmiTargetBuilder.appendRelationshipBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), usage.definition(), instanceIdsByTargetId);
            StepPmiTargetBuilder.appendSemanticDefinitionTargets(targetsByUsageId, usage.identifiedItem(), usage.definition(), resolved, instanceIdsByTargetId);
            }),
            pmiUsageTargetRule(StepPmiRequirementItemAssociation.class, (
                    entity,
                    resolved,
                    targetsByUsageId,
                    instanceIdsByTargetId
            ) -> {
            StepPmiRequirementItemAssociation usage = (StepPmiRequirementItemAssociation) entity;
            StepPmiTargetBuilder.appendRepresentationBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), instanceIdsByTargetId, "PMI_REQUIREMENT_ITEM_ASSOCIATION", usage.id());
            StepPmiTargetBuilder.appendAttachedRepresentationRelationshipTargets(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), resolved, instanceIdsByTargetId);
            StepPmiTargetBuilder.appendDefinitionBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), usage.definition(), instanceIdsByTargetId);
            StepPmiTargetBuilder.appendDefinitionBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), usage.requirement(), instanceIdsByTargetId);
            StepPmiTargetBuilder.appendRelationshipBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), usage.definition(), instanceIdsByTargetId);
            StepPmiTargetBuilder.appendRelationshipBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), usage.requirement(), instanceIdsByTargetId);
            StepPmiTargetBuilder.appendSemanticDefinitionTargets(targetsByUsageId, usage.identifiedItem(), usage.definition(), resolved, instanceIdsByTargetId);
            StepPmiTargetBuilder.appendSemanticDefinitionTargets(targetsByUsageId, usage.identifiedItem(), usage.requirement(), resolved, instanceIdsByTargetId);
            }),
            pmiUsageTargetRule(StepMechanicalDesignRequirementItemAssociation.class, (
                    entity,
                    resolved,
                    targetsByUsageId,
                    instanceIdsByTargetId
            ) -> {
            StepMechanicalDesignRequirementItemAssociation usage = (StepMechanicalDesignRequirementItemAssociation) entity;
            StepPmiTargetBuilder.appendRepresentationBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), instanceIdsByTargetId, "MECHANICAL_DESIGN_REQUIREMENT_ITEM_ASSOCIATION", usage.id());
            StepPmiTargetBuilder.appendAttachedRepresentationRelationshipTargets(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), resolved, instanceIdsByTargetId);
            StepPmiTargetBuilder.appendDefinitionBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), usage.definition(), instanceIdsByTargetId);
            StepPmiTargetBuilder.appendDefinitionBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), usage.requirement(), instanceIdsByTargetId);
            StepPmiTargetBuilder.appendRelationshipBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), usage.definition(), instanceIdsByTargetId);
            StepPmiTargetBuilder.appendRelationshipBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), usage.requirement(), instanceIdsByTargetId);
            StepPmiTargetBuilder.appendSemanticDefinitionTargets(targetsByUsageId, usage.identifiedItem(), usage.definition(), resolved, instanceIdsByTargetId);
            StepPmiTargetBuilder.appendSemanticDefinitionTargets(targetsByUsageId, usage.identifiedItem(), usage.requirement(), resolved, instanceIdsByTargetId);
            })
    );

    private static void dispatchPmiUsageTargets(
            StepEntity entity,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (PmiUsageTargetRule rule : PMI_USAGE_TARGET_RULES) {
            if (rule.matches(entity)) {
                rule.handler().handle(entity, resolved, targetsByUsageId, instanceIdsByTargetId);
                return;
            }
        }
    }

    /**
     * Dispatch table behind the Region C pmi-building chain in buildPmiPayloads.
     *
     * Replaces a ~27-branch if/else-if {@code instanceof} chain that built the
     * pmi list. The order below is load bearing: {@code instanceof} also matches
     * subtypes and the original chain was "first match wins", so entries keep
     * their original relative order. Each branch body was moved verbatim into a
     * handler that mutates the caller's pmi list (exactly what the branches did),
     * so behaviour is unchanged.
     *
     * The {@code StepGeometricReplica} branch had a compound condition
     * (instanceof AND an entityName() check) which a plain Class-based rule cannot
     * express, so it is a predicate rule. Region A (usage-target collection) and
     * Region B (the 1-branch callout propagation) are NOT part of this table.
     */
    @FunctionalInterface
    private interface PmiPayloadHandler {
        void handle(
                List<PmiPayload> pmi,
                StepEntity entity,
                Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
                StepCadBuilder builder
        );
    }

    private record PmiPayloadRule(Class<?> type, Predicate<StepEntity> predicate, PmiPayloadHandler handler) {
        boolean matches(StepEntity entity) {
            return type.isInstance(entity) && (predicate == null || predicate.test(entity));
        }
    }

    private static PmiPayloadRule pmiPayloadRule(Class<?> type, PmiPayloadHandler handler) {
        return new PmiPayloadRule(type, null, handler);
    }

    private static PmiPayloadRule pmiPayloadRule(Class<?> type, Predicate<StepEntity> predicate, PmiPayloadHandler handler) {
        return new PmiPayloadRule(type, predicate, handler);
    }

    private static final List<PmiPayloadRule> PMI_PAYLOAD_RULES = List.of(
            pmiPayloadRule(StepDraughtingCallout.class, (pmi, entity, targetsByUsageId, builder) -> {
            StepDraughtingCallout callout = (StepDraughtingCallout) entity;
                PmiPayload payload = toPmiPayload(callout, targetsByUsageId.getOrDefault(callout.id(), List.of()), builder);
                if (payload != null) {
                    pmi.add(payload);
                }
            }),
            pmiPayloadRule(StepAnnotationTextOccurrence.class, (pmi, entity, targetsByUsageId, builder) -> {
            StepAnnotationTextOccurrence textOccurrence = (StepAnnotationTextOccurrence) entity;
                CartesianPoint position = StepPmiPayloadBuilder.pointFromAnnotationPoint(textOccurrence.position(), builder);
                if (position == null) {
                    return;
                }
                List<PmiTargetPayload> targets = targetsByUsageId.getOrDefault(textOccurrence.id(), List.of());
                pmi.add(new PmiPayload(
                        textOccurrence.name(),
                        textOccurrence.text(),
                        PayloadConversionHelper.toPointPayload(position),
                        List.of(),
                        targets.stream().map(PmiTargetPayload::id).collect(Collectors.toList()),
                        targets
                ));
            }),
            pmiPayloadRule(StepAnnotationPointOccurrence.class, (pmi, entity, targetsByUsageId, builder) -> {
            StepAnnotationPointOccurrence pointOccurrence = (StepAnnotationPointOccurrence) entity;
                CartesianPoint position = StepPmiPayloadBuilder.pointFromAnnotationPoint(pointOccurrence.item(), builder);
                if (position != null) {
                    List<PmiTargetPayload> targets = targetsByUsageId.getOrDefault(pointOccurrence.id(), List.of());
                    pmi.add(toStandalonePointPmi(pointOccurrence.id(), pointOccurrence.name(), position, targets));
                }
            }),
            pmiPayloadRule(StepAnnotationFillAreaOccurrence.class, (pmi, entity, targetsByUsageId, builder) -> {
            StepAnnotationFillAreaOccurrence fillAreaOccurrence = (StepAnnotationFillAreaOccurrence) entity;
                CartesianPoint position = StepPmiPayloadBuilder.pointFromAnnotationPoint(fillAreaOccurrence.fillStyleTarget(), builder);
                if (position != null) {
                    List<PmiTargetPayload> targets = targetsByUsageId.getOrDefault(fillAreaOccurrence.id(), List.of());
                    pmi.add(toStandalonePointPmi(fillAreaOccurrence.id(), fillAreaOccurrence.name(), position, targets));
                }
            }),
            pmiPayloadRule(StepAnnotationPlaceholderOccurrence.class, (pmi, entity, targetsByUsageId, builder) -> {
            StepAnnotationPlaceholderOccurrence placeholderOccurrence = (StepAnnotationPlaceholderOccurrence) entity;
                appendPlaceholderPmi(
                        placeholderOccurrence,
                        pmi,
                        builder,
                        targetsByUsageId.getOrDefault(placeholderOccurrence.id(), List.of()));
            }),
            pmiPayloadRule(StepAnnotationSymbolOccurrence.class, (pmi, entity, targetsByUsageId, builder) -> {
            StepAnnotationSymbolOccurrence symbolOccurrence = (StepAnnotationSymbolOccurrence) entity;
                CartesianPoint position = pointFromAnnotationOccurrence(symbolOccurrence.item(), builder);
                if (position != null) {
                    List<PmiTargetPayload> targets = targetsByUsageId.getOrDefault(symbolOccurrence.id(), List.of());
                    pmi.add(toStandalonePointPmi(symbolOccurrence.id(), symbolOccurrence.name(), position, targets));
                }
            }),
            pmiPayloadRule(StepAnnotationSymbol.class, (pmi, entity, targetsByUsageId, builder) -> {
            StepAnnotationSymbol annotationSymbol = (StepAnnotationSymbol) entity;
                CartesianPoint position = pointFromAnnotationSymbol(annotationSymbol);
                if (position != null) {
                    List<PmiTargetPayload> targets = targetsByUsageId.getOrDefault(annotationSymbol.id(), List.of());
                    pmi.add(toStandalonePointPmi(annotationSymbol.id(), annotationSymbol.name(), position, targets));
                }
            }),
            pmiPayloadRule(StepAnnotationText.class, (pmi, entity, targetsByUsageId, builder) -> {
            StepAnnotationText annotationText = (StepAnnotationText) entity;
                CartesianPoint position = pointFromPlacement(annotationText.mappingTarget());
                if (position != null) {
                    List<PmiTargetPayload> targets = targetsByUsageId.getOrDefault(annotationText.id(), List.of());
                    pmi.add(toStandalonePointPmi(annotationText.id(), annotationText.name(), position, targets));
                }
            }),
            pmiPayloadRule(StepAnnotationTextCharacter.class, (pmi, entity, targetsByUsageId, builder) -> {
            StepAnnotationTextCharacter annotationTextCharacter = (StepAnnotationTextCharacter) entity;
                CartesianPoint position = pointFromPlacement(annotationTextCharacter.mappingTarget());
                if (position != null) {
                    List<PmiTargetPayload> targets = targetsByUsageId.getOrDefault(annotationTextCharacter.id(), List.of());
                    pmi.add(toStandalonePointPmi(annotationTextCharacter.id(), annotationTextCharacter.name(), position, targets));
                }
            }),
            pmiPayloadRule(StepAnnotationFillArea.class, (pmi, entity, targetsByUsageId, builder) -> {
            StepAnnotationFillArea fillArea = (StepAnnotationFillArea) entity;
                CartesianPoint position = pointFromAnnotationFillArea(fillArea, builder);
                if (position != null) {
                    List<PmiTargetPayload> targets = targetsByUsageId.getOrDefault(fillArea.id(), List.of());
                    pmi.add(toStandalonePointPmi(fillArea.id(), fillArea.name(), position, targets));
                }
            }),
            pmiPayloadRule(StepAnnotationSubfigureOccurrence.class, (pmi, entity, targetsByUsageId, builder) -> {
            StepAnnotationSubfigureOccurrence subfigureOccurrence = (StepAnnotationSubfigureOccurrence) entity;
                CartesianPoint position = pointFromAnnotationOccurrence(subfigureOccurrence.item(), builder);
                if (position != null) {
                    List<PmiTargetPayload> targets = targetsByUsageId.getOrDefault(subfigureOccurrence.id(), List.of());
                    pmi.add(toStandalonePointPmi(subfigureOccurrence.id(), subfigureOccurrence.name(), position, targets));
                }
            }),
            pmiPayloadRule(StepAnnotationPlane.class, (pmi, entity, targetsByUsageId, builder) -> {
            StepAnnotationPlane annotationPlane = (StepAnnotationPlane) entity;
                appendAnnotationPlanePmi(
                        annotationPlane,
                        pmi,
                        builder,
                        targetsByUsageId.getOrDefault(annotationPlane.id(), List.of()));
            }),
            pmiPayloadRule(StepDraughtingAnnotationOccurrence.class, (pmi, entity, targetsByUsageId, builder) -> {
            StepDraughtingAnnotationOccurrence annotationOccurrence = (StepDraughtingAnnotationOccurrence) entity;
                appendDraughtingAnnotationPmi(
                        annotationOccurrence,
                        pmi,
                        builder,
                        targetsByUsageId.getOrDefault(annotationOccurrence.id(), List.of()));
            }),
            pmiPayloadRule(StepAnnotationOccurrenceRelationship.class, (pmi, entity, targetsByUsageId, builder) -> {
            StepAnnotationOccurrenceRelationship relationship = (StepAnnotationOccurrenceRelationship) entity;
                appendAnnotationOccurrenceRelationshipPmi(relationship, pmi, builder);
            }),
            pmiPayloadRule(StepPointSet.class, (pmi, entity, targetsByUsageId, builder) -> {
            StepPointSet pointSet = (StepPointSet) entity;
                appendPointSetPmi(pointSet, pmi, builder);
            }),
            pmiPayloadRule(StepGeometricMeasurement.class, (pmi, entity, targetsByUsageId, builder) -> {
            StepGeometricMeasurement measurement = (StepGeometricMeasurement) entity;
                appendGeometricMeasurementPmi(measurement, pmi, builder);
            }),
            pmiPayloadRule(StepVertexShell.class, (pmi, entity, targetsByUsageId, builder) -> {
            StepVertexShell vertexShell = (StepVertexShell) entity;
                pmi.add(toStandalonePointPmi(
                        vertexShell.id(),
                        vertexShell.name(),
                        StepPointExtractor.pointFromStep(vertexShell.extent().loopVertex().point())
                ));
            }),
            pmiPayloadRule(
                    StepGeometricReplica.class,
                    e -> "POINT_REPLICA".equals(((StepGeometricReplica) e).entityName()),
                    (pmi, entity, targetsByUsageId, builder) -> {
            StepGeometricReplica replica = (StepGeometricReplica) entity;
                CartesianPoint position = StepPmiPayloadBuilder.pointFromReplica(replica, builder);
                if (position != null) {
                    pmi.add(toStandalonePointPmi(replica.id(), replica.name(), position));
                }
            }),
            pmiPayloadRule(StepFillAreaWithOutline.class, (pmi, entity, targetsByUsageId, builder) -> {
            StepFillAreaWithOutline fillArea = (StepFillAreaWithOutline) entity;
                appendFillAreaWithOutlinePmi(fillArea, pmi, builder);
            }),
            pmiPayloadRule(StepGeometricTolerance.class, (pmi, entity, targetsByUsageId, builder) -> {
            StepGeometricTolerance tolerance = (StepGeometricTolerance) entity;
                appendGeometricTolerancePmi(tolerance, pmi, builder);
            }),
            pmiPayloadRule(StepGeometricToleranceWithDatumReference.class, (pmi, entity, targetsByUsageId, builder) -> {
            StepGeometricToleranceWithDatumReference tolerance = (StepGeometricToleranceWithDatumReference) entity;
                appendGeometricToleranceWithDatumPmi(tolerance, pmi, builder);
            }),
            pmiPayloadRule(StepGeometricToleranceWithDefinedAreaUnit.class, (pmi, entity, targetsByUsageId, builder) -> {
            StepGeometricToleranceWithDefinedAreaUnit tolerance = (StepGeometricToleranceWithDefinedAreaUnit) entity;
                appendGeometricToleranceWithAreaUnitPmi(tolerance, pmi, builder);
            }),
            pmiPayloadRule(StepGeometricToleranceWithMaximumTolerance.class, (pmi, entity, targetsByUsageId, builder) -> {
            StepGeometricToleranceWithMaximumTolerance tolerance = (StepGeometricToleranceWithMaximumTolerance) entity;
                appendGeometricToleranceWithMaxPmi(tolerance, pmi, builder);
            }),
            pmiPayloadRule(StepDimensionalLocation.class, (pmi, entity, targetsByUsageId, builder) -> {
            StepDimensionalLocation location = (StepDimensionalLocation) entity;
                appendDimensionalLocationPmi(location, pmi, builder);
            }),
            pmiPayloadRule(StepToleranceZone.class, (pmi, entity, targetsByUsageId, builder) -> {
            StepToleranceZone zone = (StepToleranceZone) entity;
                appendToleranceZonePmi(zone, pmi, builder);
            }),
            pmiPayloadRule(StepDatum.class, (pmi, entity, targetsByUsageId, builder) -> {
            StepDatum datum = (StepDatum) entity;
                appendDatumPmi(datum, pmi, builder);
            }),
            pmiPayloadRule(StepDatumTarget.class, (pmi, entity, targetsByUsageId, builder) -> {
            StepDatumTarget datumTarget = (StepDatumTarget) entity;
                appendDatumTargetPmi(datumTarget, pmi, builder);
            })
    );

    private static void dispatchPmiPayloads(
            List<PmiPayload> pmi,
            StepEntity entity,
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepCadBuilder builder
    ) {
        for (PmiPayloadRule rule : PMI_PAYLOAD_RULES) {
            if (rule.matches(entity)) {
                rule.handler().handle(pmi, entity, targetsByUsageId, builder);
                return;
            }
        }
    }

}
