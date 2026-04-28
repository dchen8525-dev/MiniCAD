package com.minicad.app;

import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Curve3;
import com.minicad.geometry.Vector3;
import com.minicad.geometry2d.Curve2;
import com.minicad.topology.Face;
import com.minicad.topology.FaceBound;
import com.minicad.topology.Loop;
import com.minicad.topology.EdgeLoop;
import com.minicad.topology.OrientedEdge;
import com.minicad.topology.VertexLoop;
import com.minicad.topology.PolyLoop;
import com.minicad.topology.Shell;
import com.minicad.topology.Solid;
import com.minicad.common.UnsupportedGeometryException;
import com.minicad.common.StepResolutionException;
import com.minicad.common.TopologyException;
import com.minicad.step.model.base.StepEntity;
import com.minicad.step.model.base.StepFaceEntity;
import com.minicad.step.model.base.StepGeometricRepresentationItem;
import com.minicad.step.model.base.StepPreDefinedItem;
import com.minicad.step.model.base.StepRepresentationItem;
import com.minicad.step.model.base.StepTopologicalRepresentationItem;
import com.minicad.step.model.base.StepCharacterizedObject;
import com.minicad.step.model.base.StepMeasureRepresentationItem;
import com.minicad.step.model.base.StepDescriptiveRepresentationItem;
import com.minicad.step.model.base.StepValueRepresentationItem;
import com.minicad.step.model.geometry.*;
import com.minicad.step.model.tolerance.*;
import com.minicad.step.model.topology.*;
import com.minicad.step.model.annotation.*;
import com.minicad.step.model.product.*;
import com.minicad.step.model.workflow.*;
import com.minicad.step.model.action.StepActionPropertyRepresentation;
import com.minicad.step.model.action.StepForwardChainingRulePremise;
import com.minicad.step.model.action.StepBackChainingRuleBody;
import com.minicad.step.model.approval.*;
import com.minicad.step.model.classification.*;
import com.minicad.step.model.document.*;
import com.minicad.step.model.organization.*;
import com.minicad.step.model.resource.StepAppliedContractAssignment;
import com.minicad.step.model.resource.StepResourcePropertyRepresentation;
import com.minicad.step.model.resource.StepContract;
import com.minicad.step.model.resource.StepContractType;
import com.minicad.step.model.date_time.*;
import com.minicad.step.model.security.StepAppliedSecurityClassificationAssignment;
import com.minicad.step.model.security.StepSecurityClassification;
import com.minicad.step.model.security.StepSecurityClassificationLevel;
import com.minicad.step.model.kinematic.StepKinematicPropertyDefinitionRepresentation;
import com.minicad.step.model.kinematic.StepKinematicPropertyMechanismRepresentation;
import com.minicad.step.model.kinematic.StepKinematicPropertyRepresentationRelation;
import com.minicad.step.model.kinematic.StepKinematicPropertyTopologyRepresentation;
import com.minicad.step.model.unit.*;
import com.minicad.step.semantic.StepCadBuilder;
import com.minicad.step.syntax.StepValue;

import java.util.*;
import java.util.stream.Collectors;

/**
 * PMI/Annotation extraction methods extracted from StepPreviewJsonExporter.
 */
public final class PreviewPmiBuilder {

    private PreviewPmiBuilder() {}

    // =========================================================================
    // PMI carrier support check
    // =========================================================================

    public static boolean isSupportedPmiUsageCarrier(StepEntity entity) {
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

    // =========================================================================
    // Standalone point PMI helpers
    // =========================================================================

    public static PmiPayload toStandalonePointPmi(int id, String name, CartesianPoint position) {
        return toStandalonePointPmi(id, name, position, List.of());
    }

    public static PmiPayload toStandalonePointPmi(
            int id,
            String name,
            CartesianPoint position,
            List<PmiTargetPayload> targets
    ) {
        return new PmiPayload(
                name == null || name.isBlank() ? "POINT_" + id : name,
                "",
                StepPreviewJsonExporter.toPointPayload(position),
                List.of(),
                targets.stream().map(PmiTargetPayload::id).toList(),
                targets
        );
    }

    // =========================================================================
    // PMI append methods
    // =========================================================================

    public static void appendPointSetPmi(StepPointSet pointSet, List<PmiPayload> pmi, StepCadBuilder builder) {
        int pointIndex = 0;
        for (StepEntity item : pointSet.points()) {
            CartesianPoint position = pointFromAnnotationPoint(item, builder);
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

    public static void appendGeometricMeasurementPmi(
            StepGeometricMeasurement measurement,
            List<PmiPayload> pmi,
            StepCadBuilder builder
    ) {
        CartesianPoint position = pointFromAnnotationPoint(measurement.measurementGeometry(), builder);
        if (position != null) {
            String label = measurement.name() + " (" + measurement.geometricType() + ")";
            if (measurement.measuredValue() != 0.0) {
                label += ": " + String.format("%.3f", measurement.measuredValue());
            }
            pmi.add(toStandalonePointPmi(measurement.id(), label, position));
        }
        for (StepEntity pt : measurement.measurementPoints()) {
            CartesianPoint mp = pointFromAnnotationPoint(pt, builder);
            if (mp != null) {
                pmi.add(toStandalonePointPmi(measurement.id() * 1000 + measurement.measurementPoints().indexOf(pt),
                        measurement.name() + " point", mp));
            }
        }
    }

    public static void appendFillAreaWithOutlinePmi(
            StepFillAreaWithOutline fillArea,
            List<PmiPayload> pmi,
            StepCadBuilder builder
    ) {
        List<CartesianPoint> points = new ArrayList<>();
        for (StepEntity outline : fillArea.outlines()) {
            List<CartesianPoint> sampled = StepPreviewJsonExporter.sampleLooseEdgePoints(outline, builder);
            if (sampled != null && !sampled.isEmpty()) {
                points.addAll(sampled);
            }
        }
        if (!points.isEmpty()) {
            CartesianPoint center = points.get(points.size() / 2);
            pmi.add(toStandalonePointPmi(fillArea.id(), fillArea.name(), center));
        }
    }

    public static void appendGeometricTolerancePmi(
            StepGeometricTolerance tolerance,
            List<PmiPayload> pmi,
            StepCadBuilder builder
    ) {
        CartesianPoint position = pointFromAnnotationPoint(tolerance.toleratedShape(), builder);
        if (position != null) {
            String label = tolerance.name() != null ? tolerance.name() : "GEOMETRIC_TOLERANCE";
            if (tolerance.magnitude() != 0.0) {
                label += ": " + String.format("%.3f", tolerance.magnitude());
            }
            pmi.add(toStandalonePointPmi(tolerance.id(), label, position));
        }
    }

    public static void appendGeometricToleranceWithDatumPmi(
            StepGeometricToleranceWithDatumReference tolerance,
            List<PmiPayload> pmi,
            StepCadBuilder builder
    ) {
        CartesianPoint position = pointFromAnnotationPoint(tolerance.tolerancedFeature(), builder);
        if (position != null) {
            String label = tolerance.name() != null ? tolerance.name() : tolerance.toleranceType();
            if (tolerance.magnitude() != null && tolerance.magnitude() != 0.0) {
                label += ": " + String.format("%.3f", tolerance.magnitude());
            }
            pmi.add(toStandalonePointPmi(tolerance.id(), label, position));
        }
    }

    public static void appendGeometricToleranceWithAreaUnitPmi(
            StepGeometricToleranceWithDefinedAreaUnit tolerance,
            List<PmiPayload> pmi,
            StepCadBuilder builder
    ) {
        CartesianPoint position = pointFromAnnotationPoint(tolerance.tolerancedFeature(), builder);
        if (position != null) {
            String label = tolerance.name() != null ? tolerance.name() : tolerance.toleranceType();
            if (tolerance.magnitude() != null && tolerance.magnitude() != 0.0) {
                label += ": " + String.format("%.3f", tolerance.magnitude());
            }
            pmi.add(toStandalonePointPmi(tolerance.id(), label, position));
        }
    }

    public static void appendGeometricToleranceWithMaxPmi(
            StepGeometricToleranceWithMaximumTolerance tolerance,
            List<PmiPayload> pmi,
            StepCadBuilder builder
    ) {
        CartesianPoint position = pointFromAnnotationPoint(tolerance.tolerancedFeature(), builder);
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

    public static void appendDimensionalLocationPmi(
            StepDimensionalLocation location,
            List<PmiPayload> pmi,
            StepCadBuilder builder
    ) {
        CartesianPoint position = pointFromAnnotationPoint(location.relatedShape(), builder);
        if (position != null) {
            String label = location.name() != null ? location.name() : "DIMENSIONAL_LOCATION";
            pmi.add(toStandalonePointPmi(location.id(), label, position));
        }
    }

    public static void appendToleranceZonePmi(
            StepToleranceZone zone,
            List<PmiPayload> pmi,
            StepCadBuilder builder
    ) {
        String zoneShape = null;
        if (zone.form() instanceof StepToleranceZoneForm form) {
            zoneShape = form.zoneShape();
        }
        CartesianPoint position = pointFromAnnotationPoint(zone.form(), builder);
        if (position != null) {
            String label = zone.name() != null ? zone.name() : "TOLERANCE_ZONE";
            if (zoneShape != null) {
                label += " (" + zoneShape + ")";
            }
            pmi.add(toStandalonePointPmi(zone.id(), label, position));
        }
    }

    public static void appendDatumPmi(
            StepDatum datum,
            List<PmiPayload> pmi,
            StepCadBuilder builder
    ) {
        CartesianPoint position = pointFromAnnotationPoint(datum.target(), builder);
        if (position != null) {
            String label = datum.name() != null ? datum.name() : "DATUM";
            pmi.add(toStandalonePointPmi(datum.id(), label, position));
        }
    }

    public static void appendDatumTargetPmi(
            StepDatumTarget datumTarget,
            List<PmiPayload> pmi,
            StepCadBuilder builder
    ) {
        CartesianPoint position = pointFromAnnotationPoint(datumTarget.targetShape(), builder);
        if (position != null) {
            String label = datumTarget.name() != null ? datumTarget.name() : "DATUM_TARGET";
            pmi.add(toStandalonePointPmi(datumTarget.id(), label, position));
        }
    }

    public static void appendPlaceholderPmi(
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

    public static void appendAnnotationPlanePmi(
            StepAnnotationPlane annotationPlane,
            List<PmiPayload> pmi,
            StepCadBuilder builder,
            List<PmiTargetPayload> targets
    ) {
        int pointIndex = 0;
        for (StepEntity element : annotationPlane.elements()) {
            CartesianPoint position = null;
            if (element instanceof StepGeometricSet geometricSet) {
                position = pointFromGeometricSet(geometricSet, builder);
            }
            if (position == null) {
                position = pointFromAnnotationOccurrence(element, builder);
            }
            if (position == null) {
                position = pointFromAnnotationPoint(element, builder);
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

    public static void appendAnnotationOccurrenceRelationshipPmi(
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

    public static void appendDraughtingAnnotationPmi(
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

    // =========================================================================
    // PMI leader methods
    // =========================================================================

    public static void appendPmiLeader(
            StepEntity content,
            List<PointPayload> leader,
            StepCadBuilder builder
    ) {
        if (content instanceof StepGeometricSet geometricSet) {
            for (StepEntity element : geometricSet.elements()) {
                appendPmiLeader(element, leader, builder);
            }
            return;
        }
        if (content instanceof StepGeometricCurveSet curveSet) {
            for (StepEntity element : curveSet.elements()) {
                appendPmiLeader(element, leader, builder);
            }
            return;
        }
        if (content instanceof StepPointSet pointSet) {
            for (StepEntity point : pointSet.points()) {
                appendPmiLeader(point, leader, builder);
            }
            return;
        }
        if (content instanceof StepAnnotationPlaceholderOccurrence placeholderOccurrence) {
            appendPmiLeader(placeholderOccurrence.item(), leader, builder);
            return;
        }
        if (content instanceof StepAnnotationPlane annotationPlane) {
            for (StepEntity element : annotationPlane.elements()) {
                appendPmiLeader(element, leader, builder);
            }
            return;
        }
        if (content instanceof StepFaceBasedSurfaceModel surfaceModel) {
            for (StepEntity faceSet : surfaceModel.faceSets()) {
                appendPmiLeader(faceSet, leader, builder);
            }
            return;
        }
        if (content instanceof StepShellBasedSurfaceModel surfaceModel) {
            for (StepEntity shell : surfaceModel.shells()) {
                appendPmiLeader(shell, leader, builder);
            }
            return;
        }
        if (content instanceof StepManifoldSolidBrep solid) {
            appendPmiLeader(solid.outer(), leader, builder);
            return;
        }
        if (content instanceof StepBrepWithVoids solid) {
            appendPmiLeader(solid.outer(), leader, builder);
            for (StepEntity voidShell : solid.voids()) {
                appendPmiLeader(voidShell, leader, builder);
            }
            return;
        }
        if (content instanceof StepSweptAreaSolid
                || content instanceof StepSolidReplica
                || content instanceof StepCsgSolid
                || content instanceof StepCsgPrimitive
                || content instanceof StepBooleanResult
                || content instanceof StepBooleanClippingResult
                || content instanceof StepSweptDiskSolid
                || content instanceof StepExtrudedAreaSolidTapered
                || content instanceof StepRevolvedAreaSolidTapered
                || content instanceof StepSurfaceCurveSweptAreaSolid
                || content instanceof StepPolygonalBoundedHalfSpace
                || content instanceof StepComplexClippingResult) {
            appendPmiLeaderForSolid(content, leader, builder);
            return;
        }
        if (content instanceof StepAdvancedFace face) {
            for (StepFaceBound bound : face.bounds()) {
                appendPmiLeader(bound, leader, builder);
            }
            return;
        }
        if (content instanceof StepFaceSurface face) {
            for (StepFaceBound bound : face.bounds()) {
                appendPmiLeader(bound, leader, builder);
            }
            return;
        }
        if (content instanceof StepOrientedFace face) {
            appendPmiLeader(face.faceElement(), leader, builder);
            return;
        }
        if (content instanceof StepFaceBound faceBound) {
            appendPmiLeader(faceBound.loop(), leader, builder);
            return;
        }
        if (content instanceof StepOpenShell shell) {
            for (StepFaceEntity face : shell.faces()) {
                appendPmiLeader(face, leader, builder);
            }
            return;
        }
        if (content instanceof StepSurfacedOpenShell shell) {
            for (StepFaceEntity face : shell.faces()) {
                appendPmiLeader(face, leader, builder);
            }
            return;
        }
        if (content instanceof StepOrientedOpenShell shell) {
            appendPmiLeader(shell.openShellElement(), leader, builder);
            return;
        }
        if (content instanceof StepClosedShell shell) {
            for (StepFaceEntity face : shell.faces()) {
                appendPmiLeader(face, leader, builder);
            }
            return;
        }
        if (content instanceof StepOrientedClosedShell shell) {
            appendPmiLeader(shell.closedShellElement(), leader, builder);
            return;
        }
        if (content instanceof StepConnectedFaceSet faceSet) {
            for (StepFaceEntity face : faceSet.faces()) {
                appendPmiLeader(face, leader, builder);
            }
            return;
        }
        if (content instanceof StepConnectedFaceSubSet faceSet) {
            for (StepFaceEntity face : faceSet.faces()) {
                appendPmiLeader(face, leader, builder);
            }
            return;
        }
        if (content instanceof StepAnnotationPointOccurrence pointOccurrence) {
            appendPmiLeader(pointOccurrence.item(), leader, builder);
            return;
        }
        if (content instanceof StepAnnotationCurveOccurrence occurrence) {
            appendPmiLeader(occurrence.item(), leader, builder);
            return;
        }
        if (content instanceof StepAnnotationFillArea fillArea) {
            List<CartesianPoint> sampled = StepPreviewJsonExporter.sampleAnnotationFillAreaPoints(fillArea, builder);
            if (sampled != null) {
                for (CartesianPoint point : sampled) {
                    leader.add(StepPreviewJsonExporter.toPointPayload(point));
                }
            }
            return;
        }
        if (content instanceof StepAnnotationFillAreaOccurrence fillAreaOccurrence) {
            appendPmiLeader(fillAreaOccurrence.item(), leader, builder);
            return;
        }
        if (content instanceof StepAnnotationSymbol annotationSymbol) {
            List<CartesianPoint> sampled = StepPreviewJsonExporter.sampleLooseEdgePoints(annotationSymbol, builder);
            if (sampled != null) {
                for (CartesianPoint point : sampled) {
                    leader.add(StepPreviewJsonExporter.toPointPayload(point));
                }
            }
            return;
        }
        if (content instanceof StepAnnotationSymbolOccurrence symbolOccurrence) {
            appendPmiLeader(symbolOccurrence.item(), leader, builder);
            return;
        }
        if (content instanceof StepAnnotationSubfigureOccurrence subfigureOccurrence) {
            appendPmiLeader(subfigureOccurrence.item(), leader, builder);
            return;
        }
        if (content instanceof StepAnnotationText annotationText) {
            List<CartesianPoint> sampled = StepPreviewJsonExporter.sampleLooseEdgePoints(annotationText, builder);
            if (sampled != null) {
                for (CartesianPoint point : sampled) {
                    leader.add(StepPreviewJsonExporter.toPointPayload(point));
                }
            }
            return;
        }
        if (content instanceof StepAnnotationTextCharacter annotationTextCharacter) {
            List<CartesianPoint> sampled = StepPreviewJsonExporter.sampleLooseEdgePoints(annotationTextCharacter, builder);
            if (sampled != null) {
                for (CartesianPoint point : sampled) {
                    leader.add(StepPreviewJsonExporter.toPointPayload(point));
                }
            }
            return;
        }
        if (content instanceof StepDimensionCurve dimensionCurve) {
            appendPmiLeader(dimensionCurve.item(), leader, builder);
            return;
        }
        if (content instanceof StepLeaderCurve leaderCurve) {
            appendPmiLeader(leaderCurve.item(), leader, builder);
            return;
        }
        if (content instanceof StepProjectionCurve projectionCurve) {
            appendPmiLeader(projectionCurve.item(), leader, builder);
            return;
        }
        if (content instanceof StepDraughtingAnnotationOccurrence annotationOccurrence) {
            appendPmiLeader(annotationOccurrence.item(), leader, builder);
            return;
        }
        if (content instanceof StepTerminatorSymbol terminatorSymbol) {
            appendPmiLeader(terminatorSymbol.annotatedCurve(), leader, builder);
            return;
        }
        if (content instanceof StepPath path) {
            appendPmiPathLeader(path.edges(), leader, builder);
            return;
        }
        if (content instanceof StepOpenPath path) {
            appendPmiPathLeader(path.edges(), leader, builder);
            return;
        }
        if (content instanceof StepSubpath subpath) {
            appendPmiPathLeader(subpath.edges(), leader, builder);
            return;
        }
        if (content instanceof StepOrientedPath orientedPath) {
            appendPmiPathLeader(orientedPath.edges(), leader, builder);
            return;
        }
        if (content instanceof StepConnectedEdgeSet connectedEdgeSet) {
            for (StepEntity edge : connectedEdgeSet.edges()) {
                appendPmiLeader(edge, leader, builder);
            }
            return;
        }
        if (content instanceof StepEdgeBasedWireframeModel wireframeModel) {
            for (StepConnectedEdgeSet boundary : wireframeModel.boundaries()) {
                appendPmiLeader(boundary, leader, builder);
            }
            return;
        }
        if (content instanceof StepShellBasedWireframeModel wireframeModel) {
            for (StepEntity boundary : wireframeModel.boundaries()) {
                appendPmiLeader(boundary, leader, builder);
            }
            return;
        }
        if (content instanceof StepWireShell wireShell) {
            for (StepLoop loop : wireShell.loops()) {
                appendPmiLeader(loop, leader, builder);
            }
            return;
        }
        if (content instanceof StepEdgeLoop edgeLoop) {
            appendPmiPathLeader(edgeLoop.edges(), leader, builder);
            return;
        }
        if (content instanceof StepVertexLoop vertexLoop) {
            leader.add(StepPreviewJsonExporter.toPointPayload(StepPreviewJsonExporter.pointFromStep(vertexLoop.loopVertex().point())));
            return;
        }
        if (content instanceof StepPolyLoop polyLoop) {
            for (StepCartesianPoint point : polyLoop.polygon()) {
                leader.add(StepPreviewJsonExporter.toPointPayload(StepPreviewJsonExporter.pointFromStep(point)));
            }
            return;
        }
        if (content instanceof StepVertexShell vertexShell) {
            leader.add(StepPreviewJsonExporter.toPointPayload(StepPreviewJsonExporter.pointFromStep(vertexShell.extent().loopVertex().point())));
            return;
        }
        if (content instanceof StepGeometricReplica replica && "POINT_REPLICA".equals(replica.entityName())) {
            CartesianPoint point = pointFromReplica(replica, builder);
            if (point != null) {
                leader.add(StepPreviewJsonExporter.toPointPayload(point));
            }
            return;
        }
        if (content instanceof StepCartesianPoint point) {
            leader.add(StepPreviewJsonExporter.toPointPayload(StepPreviewJsonExporter.pointFromStep(point)));
            return;
        }
        if (content instanceof StepVertexPoint vertexPoint) {
            leader.add(StepPreviewJsonExporter.toPointPayload(StepPreviewJsonExporter.pointFromStep(vertexPoint.point())));
            return;
        }
        List<CartesianPoint> sampled = StepPreviewJsonExporter.sampleLooseEdgePoints(content, builder);
        if (sampled == null) {
            return;
        }
        for (CartesianPoint point : sampled) {
            leader.add(StepPreviewJsonExporter.toPointPayload(point));
        }
    }

    public static void appendPmiLeaderForSolid(
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
            // Callout leader extraction is best-effort
        }
    }

    public static void appendPmiLeader(
            Face face,
            List<PointPayload> leader,
            StepCadBuilder builder
    ) {
        for (FaceBound bound : face.bounds()) {
            appendPmiLeader(bound.loop(), leader, builder);
        }
    }

    public static void appendPmiLeader(
            Loop loop,
            List<PointPayload> leader,
            StepCadBuilder builder
    ) {
        if (loop instanceof EdgeLoop edgeLoop) {
            for (OrientedEdge edge : edgeLoop.edges()) {
                appendTopologyEdgeLeader(edge, leader);
            }
            return;
        }
        if (loop instanceof VertexLoop vertexLoop) {
            leader.add(StepPreviewJsonExporter.toPointPayload(vertexLoop.vertex().point()));
            return;
        }
        if (loop instanceof PolyLoop polyLoop) {
            for (CartesianPoint point : polyLoop.points()) {
                leader.add(StepPreviewJsonExporter.toPointPayload(point));
            }
        }
    }

    public static void appendTopologyEdgeLeader(
            OrientedEdge orientedEdge,
            List<PointPayload> leader
    ) {
        List<CartesianPoint> points = StepPreviewJsonExporter.sampleLooseCurve(orientedEdge.edge().curve());
        if (!orientedEdge.orientation()) {
            List<CartesianPoint> reversed = new ArrayList<>(points);
            Collections.reverse(reversed);
            points = reversed;
        }
        for (CartesianPoint point : points) {
            leader.add(StepPreviewJsonExporter.toPointPayload(point));
        }
    }

    public static void appendPmiPathLeader(
            List<StepOrientedEdge> edges,
            List<PointPayload> leader,
            StepCadBuilder builder
    ) {
        for (StepOrientedEdge edge : edges) {
            List<CartesianPoint> points = StepPreviewJsonExporter.sampleLooseEdgePoints(edge.edgeElement(), builder);
            if (points == null) {
                continue;
            }
            for (CartesianPoint point : points) {
                leader.add(StepPreviewJsonExporter.toPointPayload(point));
            }
        }
    }

    // =========================================================================
    // PMI target helpers
    // =========================================================================

    public static String pmiTargetType(StepEntity target) {
        if (target instanceof StepFaceEntity) {
            return "face";
        }
        if (target instanceof StepEdgeCurve
                || target instanceof StepSubedge
                || target instanceof StepOrientedEdge) {
            return "edge";
        }
        if (target instanceof StepPath
                || target instanceof StepOpenPath
                || target instanceof StepSubpath
                || target instanceof StepOrientedPath) {
            return "path";
        }
        if (target instanceof StepConnectedEdgeSet) {
            return "edge_set";
        }
        if (target instanceof StepPointSet) {
            return "point_set";
        }
        if (target instanceof StepAnnotationSymbol) {
            return "annotation_symbol";
        }
        if (target instanceof StepAnnotationText) {
            return "annotation_text";
        }
        if (target instanceof StepAnnotationTextCharacter) {
            return "annotation_text_character";
        }
        if (target instanceof StepAnnotationFillArea) {
            return "annotation_fill_area";
        }
        if (target instanceof StepGeometricSet) {
            return "geometric_set";
        }
        if (target instanceof StepGeometricCurveSet) {
            return "curve_set";
        }
        if (target instanceof StepOpenShell
                || target instanceof StepSurfacedOpenShell
                || target instanceof StepOrientedOpenShell
                || target instanceof StepClosedShell
                || target instanceof StepOrientedClosedShell) {
            return "shell";
        }
        if (target instanceof StepWireShell) {
            return "wire_shell";
        }
        if (target instanceof StepVertexShell) {
            return "vertex_shell";
        }
        if (target instanceof StepEdgeLoop
                || target instanceof StepVertexLoop
                || target instanceof StepPolyLoop) {
            return "loop";
        }
        if (target instanceof StepConnectedFaceSet || target instanceof StepConnectedFaceSubSet) {
            return "face_set";
        }
        if (target instanceof StepFaceBasedSurfaceModel || target instanceof StepShellBasedSurfaceModel) {
            return "surface_model";
        }
        if (target instanceof StepEdgeBasedWireframeModel || target instanceof StepShellBasedWireframeModel) {
            return "wireframe_model";
        }
        if (target instanceof StepManifoldSolidBrep
                || target instanceof StepBrepWithVoids
                || target instanceof StepSweptAreaSolid
                || target instanceof StepSolidReplica
                || target instanceof StepCsgSolid
                || target instanceof StepCsgPrimitive
                || target instanceof StepBooleanResult
                || target instanceof StepBooleanClippingResult
                || target instanceof StepSweptDiskSolid
                || target instanceof StepExtrudedAreaSolidTapered
                || target instanceof StepRevolvedAreaSolidTapered
                || target instanceof StepSurfaceCurveSweptAreaSolid
                || target instanceof StepPolygonalBoundedHalfSpace
                || target instanceof StepComplexClippingResult) {
            return "solid";
        }
        if (target instanceof StepRepresentation) {
            return "representation";
        }
        return "entity";
    }

    public static String pmiTargetName(StepEntity target) {
        if (target instanceof StepFaceEntity face) {
            return StepPreviewJsonExporter.faceDisplayName(face);
        }
        if (target instanceof StepEdgeCurve edge) {
            return edge.name();
        }
        if (target instanceof StepSubedge subedge) {
            return subedge.name();
        }
        if (target instanceof StepOrientedEdge orientedEdge) {
            return orientedEdge.name();
        }
        if (target instanceof StepPath path) {
            return path.name();
        }
        if (target instanceof StepOpenPath path) {
            return path.name();
        }
        if (target instanceof StepSubpath subpath) {
            return subpath.name();
        }
        if (target instanceof StepOrientedPath orientedPath) {
            return orientedPath.name();
        }
        if (target instanceof StepConnectedEdgeSet edgeSet) {
            return edgeSet.name();
        }
        if (target instanceof StepPointSet pointSet) {
            return pointSet.name();
        }
        if (target instanceof StepAnnotationSymbol annotationSymbol) {
            return annotationSymbol.name();
        }
        if (target instanceof StepAnnotationText annotationText) {
            return annotationText.name();
        }
        if (target instanceof StepAnnotationTextCharacter annotationTextCharacter) {
            return annotationTextCharacter.name();
        }
        if (target instanceof StepAnnotationFillArea fillArea) {
            return fillArea.name();
        }
        if (target instanceof StepGeometricSet geometricSet) {
            return geometricSet.name();
        }
        if (target instanceof StepGeometricCurveSet curveSet) {
            return curveSet.name();
        }
        if (target instanceof StepOpenShell openShell) {
            return openShell.name();
        }
        if (target instanceof StepSurfacedOpenShell openShell) {
            return openShell.name();
        }
        if (target instanceof StepOrientedOpenShell openShell) {
            return openShell.name();
        }
        if (target instanceof StepClosedShell closedShell) {
            return closedShell.name();
        }
        if (target instanceof StepOrientedClosedShell closedShell) {
            return closedShell.name();
        }
        if (target instanceof StepWireShell wireShell) {
            return wireShell.name();
        }
        if (target instanceof StepVertexShell vertexShell) {
            return vertexShell.name();
        }
        if (target instanceof StepEdgeLoop edgeLoop) {
            return edgeLoop.name();
        }
        if (target instanceof StepVertexLoop vertexLoop) {
            return vertexLoop.name();
        }
        if (target instanceof StepPolyLoop polyLoop) {
            return polyLoop.name();
        }
        if (target instanceof StepConnectedFaceSet faceSet) {
            return faceSet.name();
        }
        if (target instanceof StepConnectedFaceSubSet faceSet) {
            return faceSet.name();
        }
        if (target instanceof StepFaceBasedSurfaceModel surfaceModel) {
            return surfaceModel.name();
        }
        if (target instanceof StepShellBasedSurfaceModel surfaceModel) {
            return surfaceModel.name();
        }
        if (target instanceof StepEdgeBasedWireframeModel wireframeModel) {
            return wireframeModel.name();
        }
        if (target instanceof StepShellBasedWireframeModel wireframeModel) {
            return wireframeModel.name();
        }
        if (target instanceof StepManifoldSolidBrep solid) {
            return solid.name();
        }
        if (target instanceof StepBrepWithVoids solid) {
            return solid.name();
        }
        if (target instanceof StepSweptAreaSolid solid) {
            return solid.name();
        }
        if (target instanceof StepSolidReplica solid) {
            return solid.name();
        }
        if (target instanceof StepCsgSolid solid) {
            return solid.name();
        }
        if (target instanceof StepCsgPrimitive solid) {
            return solid.name();
        }
        if (target instanceof StepBooleanResult solid) {
            return solid.name();
        }
        if (target instanceof StepBooleanClippingResult solid) {
            return solid.name();
        }
        if (target instanceof StepSweptDiskSolid solid) {
            return solid.name();
        }
        if (target instanceof StepComplexClippingResult solid) {
            return solid.name();
        }
        if (target instanceof StepRepresentation representation) {
            return representation.name();
        }
        return "";
    }

    public static void appendPmiTarget(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepEntity target,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        appendPmiTarget(targetsByUsageId, usageId, target, instanceIdsByTargetId, null, null, null, null);
    }

    public static void appendPmiTarget(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepEntity target,
            Map<Integer, List<String>> instanceIdsByTargetId,
            String viaRelationshipType,
            Integer viaRelationshipId
    ) {
        appendPmiTarget(targetsByUsageId, usageId, target, instanceIdsByTargetId, viaRelationshipType, viaRelationshipId, null, null);
    }

    public static void appendPmiTarget(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepEntity target,
            Map<Integer, List<String>> instanceIdsByTargetId,
            String viaRelationshipType,
            Integer viaRelationshipId,
            String viaUsageType,
            Integer viaUsageId
    ) {
        appendPmiTarget(
                targetsByUsageId,
                usageId,
                target,
                instanceIdsByTargetId,
                viaRelationshipType,
                viaRelationshipId,
                viaUsageType,
                viaUsageId,
                null,
                null
        );
    }

    public static void appendPmiTarget(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepEntity target,
            Map<Integer, List<String>> instanceIdsByTargetId,
            String viaRelationshipType,
            Integer viaRelationshipId,
            String viaUsageType,
            Integer viaUsageId,
            String viaDefinitionType,
            Integer viaDefinitionId
    ) {
        PmiTargetPayload payload = new PmiTargetPayload(
                target.id(),
                pmiTargetType(target),
                pmiTargetName(target),
                List.copyOf(instanceIdsByTargetId.getOrDefault(target.id(), List.of())),
                viaRelationshipType,
                viaRelationshipId,
                viaUsageType,
                viaUsageId,
                viaDefinitionType,
                viaDefinitionId
        );
        List<PmiTargetPayload> targets = targetsByUsageId.computeIfAbsent(usageId, ignored -> new ArrayList<>());
        if (!targets.contains(payload)) {
            targets.add(payload);
        }
    }

    // =========================================================================
    // Backlink target methods
    // =========================================================================

    public static void appendRepresentationBacklinkTarget(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepRepresentation representation,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        appendRepresentationBacklinkTarget(targetsByUsageId, identifiedItem, representation, instanceIdsByTargetId, null, null);
    }

    public static void appendRepresentationBacklinkTarget(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepRepresentation representation,
            Map<Integer, List<String>> instanceIdsByTargetId,
            String viaUsageType,
            Integer viaUsageId
    ) {
        if (!isSupportedPmiUsageCarrier(identifiedItem)) {
            return;
        }
        appendPmiTarget(targetsByUsageId, identifiedItem.id(), representation, instanceIdsByTargetId, null, null, viaUsageType, viaUsageId);
    }

    public static void appendDefinitionBacklinkTarget(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepRepresentation representation,
            StepEntity definition,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!isSupportedPmiUsageCarrier(identifiedItem) || definition == null) {
            return;
        }
        appendPmiTarget(
                targetsByUsageId,
                identifiedItem.id(),
                representation,
                instanceIdsByTargetId,
                null,
                null,
                null,
                null,
                StepPreviewJsonExporter.definitionTypeName(definition),
                definition.id()
        );
    }

    public static void appendExistingRepresentationDefinitionTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!isSupportedPmiUsageCarrier(identifiedItem) || definition == null) {
            return;
        }
        List<PmiTargetPayload> existingTargets = List.copyOf(targetsByUsageId.getOrDefault(identifiedItem.id(), List.of()));
        for (PmiTargetPayload target : existingTargets) {
            if (!"representation".equals(target.type())) {
                continue;
            }
            PmiTargetPayload payload = new PmiTargetPayload(
                    target.id(),
                    target.type(),
                    target.name(),
                    List.copyOf(instanceIdsByTargetId.getOrDefault(target.id(), target.instanceIds())),
                    null,
                    null,
                    null,
                    null,
                    StepPreviewJsonExporter.definitionTypeName(definition),
                    definition.id()
            );
            List<PmiTargetPayload> targets = targetsByUsageId.computeIfAbsent(identifiedItem.id(), ignored -> new ArrayList<>());
            if (!targets.contains(payload)) {
                targets.add(payload);
            }
        }
    }

    public static void appendRelationshipBacklinkTarget(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepRepresentation representation,
            StepEntity definition,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!isSupportedPmiUsageCarrier(identifiedItem)) {
            return;
        }
        if (definition instanceof StepAnnotationOccurrenceRelationship relationship) {
            appendPmiTarget(
                    targetsByUsageId,
                    identifiedItem.id(),
                    representation,
                    instanceIdsByTargetId,
                    relationship.entityName(),
                    relationship.id()
            );
        } else if (definition instanceof StepDraughtingCalloutRelationship relationship) {
            appendPmiTarget(
                    targetsByUsageId,
                    identifiedItem.id(),
                    representation,
                    instanceIdsByTargetId,
                    "DRAUGHTING_CALLOUT_RELATIONSHIP",
                    relationship.id()
            );
        }
    }

    // =========================================================================
    // Point extraction methods
    // =========================================================================

    public static CartesianPoint pointFromAnnotationPoint(StepEntity item, StepCadBuilder builder) {
        if (item instanceof StepCartesianPoint point) {
            return StepPreviewJsonExporter.pointFromStep(point);
        }
        if (item instanceof StepVertexPoint vertexPoint) {
            return StepPreviewJsonExporter.pointFromStep(vertexPoint.point());
        }
        if (item instanceof StepVertexShell vertexShell) {
            return StepPreviewJsonExporter.pointFromStep(vertexShell.extent().loopVertex().point());
        }
        if (item instanceof StepPointSet pointSet) {
            return pointFromPointSet(pointSet, builder);
        }
        if (item instanceof StepGeometricSet geometricSet) {
            return pointFromGeometricSet(geometricSet, builder);
        }
        if (item instanceof StepGeometricCurveSet curveSet) {
            return pointFromGeometricCurveSet(curveSet, builder);
        }
        if (item instanceof StepAnnotationSymbol
                || item instanceof StepAnnotationText
                || item instanceof StepAnnotationTextCharacter
                || item instanceof StepAnnotationFillArea) {
            return pointFromAnnotationOccurrence(item, builder);
        }
        if (item instanceof StepAnnotationPointOccurrence
                || item instanceof StepAnnotationFillAreaOccurrence
                || item instanceof StepAnnotationTextOccurrence
                || item instanceof StepAnnotationPlaceholderOccurrence
                || item instanceof StepAnnotationSymbolOccurrence
                || item instanceof StepAnnotationSubfigureOccurrence
                || item instanceof StepDraughtingAnnotationOccurrence
                || item instanceof StepAnnotationPlane) {
            return pointFromAnnotationOccurrence(item, builder);
        }
        if (builder != null && item instanceof StepGeometricReplica replica && "POINT_REPLICA".equals(replica.entityName())) {
            return pointFromReplica(replica, builder);
        }
        return null;
    }

    public static CartesianPoint pointFromAnnotationSymbol(StepAnnotationSymbol annotationSymbol) {
        return pointFromPlacement(annotationSymbol.mappingTarget());
    }

    static CartesianPoint pointFromAnnotationFillArea(
            StepAnnotationFillArea fillArea,
            StepCadBuilder builder
    ) {
        List<CartesianPoint> sampled = StepPreviewJsonExporter.sampleAnnotationFillAreaPoints(fillArea, builder);
        if (sampled == null || sampled.isEmpty()) {
            return null;
        }
        return sampled.getFirst();
    }

    static CartesianPoint pointFromAnnotationOccurrence(StepEntity occurrence, StepCadBuilder builder) {
        return switch (occurrence) {
            case StepAnnotationPointOccurrence pointOccurrence -> pointFromAnnotationPoint(pointOccurrence.item(), builder);
            case StepAnnotationCurveOccurrence curveOccurrence -> pointFromCurveCarrier(curveOccurrence.item(), builder);
            case StepLeaderCurve leaderCurve -> pointFromCurveCarrier(leaderCurve.item(), builder);
            case StepDimensionCurve dimensionCurve -> pointFromCurveCarrier(dimensionCurve.item(), builder);
            case StepProjectionCurve projectionCurve -> pointFromCurveCarrier(projectionCurve.item(), builder);
            case StepAnnotationFillAreaOccurrence fillAreaOccurrence -> pointFromAnnotationPoint(fillAreaOccurrence.fillStyleTarget(), builder);
            case StepAnnotationFillArea fillArea -> pointFromAnnotationFillArea(fillArea, builder);
            case StepAnnotationSymbol annotationSymbol -> pointFromAnnotationSymbol(annotationSymbol);
            case StepAnnotationSymbolOccurrence symbolOccurrence -> pointFromAnnotationOccurrence(symbolOccurrence.item(), builder);
            case StepAnnotationSubfigureOccurrence subfigureOccurrence -> pointFromAnnotationOccurrence(subfigureOccurrence.item(), builder);
            case StepAnnotationPlaceholderOccurrence placeholderOccurrence -> pointFromPlaceholderItem(placeholderOccurrence.item(), builder);
            case StepAnnotationPlane annotationPlane -> pointFromAnnotationPlane(annotationPlane, builder);
            case StepAnnotationText annotationText -> pointFromPlacement(annotationText.mappingTarget());
            case StepAnnotationTextCharacter annotationTextCharacter -> pointFromPlacement(annotationTextCharacter.mappingTarget());
            case StepAnnotationTextOccurrence textOccurrence -> pointFromAnnotationPoint(textOccurrence.position(), builder);
            case StepDraughtingAnnotationOccurrence annotationOccurrence -> pointFromAnnotationOccurrence(annotationOccurrence.item(), builder);
            case StepTerminatorSymbol terminatorSymbol -> {
                CartesianPoint position = pointFromAnnotationOccurrence(terminatorSymbol.item(), builder);
                if (position == null) {
                    position = pointFromAnnotationOccurrence(terminatorSymbol.annotatedCurve(), builder);
                }
                yield position;
            }
            case StepPointSet pointSet -> pointFromPointSet(pointSet, builder);
            case StepGeometricSet geometricSet -> pointFromGeometricSet(geometricSet, builder);
            case StepGeometricCurveSet curveSet -> pointFromGeometricCurveSet(curveSet, builder);
            case StepVertexShell vertexShell -> StepPreviewJsonExporter.pointFromStep(vertexShell.extent().loopVertex().point());
            case StepGeometricReplica replica when "POINT_REPLICA".equals(replica.entityName()) -> builder == null ? null : pointFromReplica(replica, builder);
            default -> null;
        };
    }

    public static CartesianPoint pointFromCurveCarrier(StepEntity item, StepCadBuilder builder) {
        List<CartesianPoint> sampled = StepPreviewJsonExporter.sampleLooseEdgePoints(item, builder);
        if (sampled == null || sampled.isEmpty()) {
            return null;
        }
        return sampled.getFirst();
    }

    public static CartesianPoint pointFromGeometricSet(StepGeometricSet geometricSet, StepCadBuilder builder) {
        for (StepEntity element : geometricSet.elements()) {
            CartesianPoint point = pointFromAnnotationOccurrence(element, builder);
            if (point != null) {
                return point;
            }
            point = pointFromAnnotationPoint(element, builder);
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

    public static CartesianPoint pointFromGeometricCurveSet(StepGeometricCurveSet curveSet, StepCadBuilder builder) {
        for (StepEntity element : curveSet.elements()) {
            CartesianPoint point = pointFromAnnotationOccurrence(element, builder);
            if (point != null) {
                return point;
            }
            point = pointFromAnnotationPoint(element, builder);
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

    public static CartesianPoint pointFromAnnotationPlane(StepAnnotationPlane annotationPlane, StepCadBuilder builder) {
        for (StepEntity element : annotationPlane.elements()) {
            if (element instanceof StepGeometricSet geometricSet) {
                CartesianPoint point = pointFromGeometricSet(geometricSet, builder);
                if (point != null) {
                    return point;
                }
            }
            CartesianPoint point = pointFromAnnotationOccurrence(element, builder);
            if (point != null) {
                return point;
            }
            point = pointFromAnnotationPoint(element, builder);
            if (point != null) {
                return point;
            }
        }
        return null;
    }

    public static CartesianPoint pointFromPointSet(StepPointSet pointSet, StepCadBuilder builder) {
        for (StepEntity item : pointSet.points()) {
            CartesianPoint point = pointFromAnnotationPoint(item, builder);
            if (point != null) {
                return point;
            }
        }
        return null;
    }

    public static CartesianPoint pointFromPlaceholderItem(StepEntity item, StepCadBuilder builder) {
        if (item instanceof StepGeometricSet geometricSet) {
            return pointFromGeometricSet(geometricSet, builder);
        }
        if (item instanceof StepGeometricCurveSet curveSet) {
            return pointFromGeometricCurveSet(curveSet, builder);
        }
        if (item instanceof StepPointSet pointSet) {
            return pointFromPointSet(pointSet, builder);
        }
        if (item instanceof StepAnnotationPlane annotationPlane) {
            return pointFromAnnotationPlane(annotationPlane, builder);
        }
        CartesianPoint point = pointFromAnnotationOccurrence(item, builder);
        if (point != null) {
            return point;
        }
        return pointFromAnnotationPoint(item, builder);
    }

    static void collectPlaceholderPositions(
            StepEntity item,
            List<CartesianPoint> positions,
            StepCadBuilder builder
    ) {
        if (item instanceof StepPointSet pointSet) {
            for (StepEntity point : pointSet.points()) {
                collectPlaceholderPositions(point, positions, builder);
            }
            return;
        }
        if (item instanceof StepGeometricSet geometricSet) {
            for (StepEntity element : geometricSet.elements()) {
                collectPlaceholderPositions(element, positions, builder);
            }
            return;
        }
        if (item instanceof StepGeometricCurveSet curveSet) {
            for (StepEntity element : curveSet.elements()) {
                collectPlaceholderPositions(element, positions, builder);
            }
            return;
        }
        if (item instanceof StepAnnotationPlane annotationPlane) {
            for (StepEntity element : annotationPlane.elements()) {
                collectPlaceholderPositions(element, positions, builder);
            }
            return;
        }
        CartesianPoint point = pointFromAnnotationOccurrence(item, builder);
        if (point == null) {
            point = pointFromAnnotationPoint(item, builder);
        }
        if (point != null) {
            positions.add(point);
        }
    }

    public static CartesianPoint pointFromPlacement(StepEntity placement) {
        if (placement instanceof StepAxis2Placement3D placement3D) {
            return StepPreviewJsonExporter.pointFromStep(placement3D.location());
        }
        if (placement instanceof StepAxis2Placement2D placement2D) {
            StepCartesianPoint point = placement2D.location();
            return new CartesianPoint(point.coordinates().get(0), point.coordinates().get(1), 0.0);
        }
        return null;
    }

    public static CartesianPoint pointFromReplica(StepGeometricReplica replica, StepCadBuilder builder) {
        if (replica.parent() instanceof StepCartesianPoint point) {
            return transformPoint(StepPreviewJsonExporter.pointFromStep(point), replica.transformation(), builder);
        }
        if (replica.parent() instanceof StepVertexPoint vertexPoint) {
            return transformPoint(StepPreviewJsonExporter.pointFromStep(vertexPoint.point()), replica.transformation(), builder);
        }
        return null;
    }

    static CartesianPoint transformPoint(
            CartesianPoint point,
            StepCartesianTransformationOperator transformation,
            StepCadBuilder builder
    ) {
        Vector3 axis1 = transformation.axis1() == null
                ? new Vector3(1.0, 0.0, 0.0)
                : builder.buildDirection(transformation.axis1().id()).asVector();
        Vector3 axis2;
        if (transformation.axis2() != null) {
            axis2 = builder.buildDirection(transformation.axis2().id()).asVector();
        } else {
            Vector3 fallback = new Vector3(0.0, 1.0, 0.0);
            axis2 = axis1.cross(fallback).isZero() ? new Vector3(0.0, 0.0, 1.0) : fallback;
        }
        Vector3 axis3;
        if (transformation.axis3() != null) {
            axis3 = builder.buildDirection(transformation.axis3().id()).asVector();
        } else {
            Vector3 cross = axis1.cross(axis2);
            axis3 = cross.isZero() ? new Vector3(0.0, 0.0, 1.0) : cross.normalize().asVector();
        }
        double scale = transformation.scale() == null ? 1.0 : transformation.scale();
        CartesianPoint origin = builder.buildPoint(transformation.localOrigin().id());
        Vector3 offset = axis1.scale(point.x() * scale)
                .add(axis2.scale(point.y() * scale))
                .add(axis3.scale(point.z() * scale));
        return origin.add(offset);
    }

    // =========================================================================
    // Assembly/instance ID mapping
    // =========================================================================

    public static Map<Integer, List<String>> buildInstanceIdsByTargetId(AssemblyData assembly) {
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

    // =========================================================================
    // Callout PMI payload builder
    // =========================================================================

    public static PmiPayload toPmiPayload(
            StepDraughtingCallout callout,
            List<PmiTargetPayload> targets,
            StepCadBuilder builder
    ) {
        StepAnnotationTextOccurrence text = null;
        List<PointPayload> leader = new ArrayList<>();
        for (StepEntity content : callout.contents()) {
            if (content instanceof StepAnnotationTextOccurrence annotationText) {
                text = annotationText;
            } else {
                appendPmiLeader(content, leader, builder);
            }
        }
        if (text == null) {
            return null;
        }
        CartesianPoint position = pointFromAnnotationPoint(text.position(), builder);
        if (position == null) {
            return null;
        }
        return new PmiPayload(
                callout.name(),
                text.text(),
                StepPreviewJsonExporter.toPointPayload(position),
                List.copyOf(leader),
                targets.stream().map(PmiTargetPayload::id).toList(),
                List.copyOf(targets)
        );
    }
}
