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
                targets.stream().map(PmiTargetPayload::id).collect(Collectors.toList()),
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
        if (zone.form() instanceof StepToleranceZoneForm) {
            StepToleranceZoneForm form = (StepToleranceZoneForm) zone.form();
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
            if (element instanceof StepGeometricSet) {
            StepGeometricSet geometricSet = (StepGeometricSet) element;
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
        if (content instanceof StepGeometricSet) {
            StepGeometricSet geometricSet = (StepGeometricSet) content;
            for (StepEntity element : geometricSet.elements()) {
                appendPmiLeader(element, leader, builder);
            }
            return;
        }
        if (content instanceof StepGeometricCurveSet) {
            StepGeometricCurveSet curveSet = (StepGeometricCurveSet) content;
            for (StepEntity element : curveSet.elements()) {
                appendPmiLeader(element, leader, builder);
            }
            return;
        }
        if (content instanceof StepPointSet) {
            StepPointSet pointSet = (StepPointSet) content;
            for (StepEntity point : pointSet.points()) {
                appendPmiLeader(point, leader, builder);
            }
            return;
        }
        if (content instanceof StepAnnotationPlaceholderOccurrence) {
            StepAnnotationPlaceholderOccurrence placeholderOccurrence = (StepAnnotationPlaceholderOccurrence) content;
            appendPmiLeader(placeholderOccurrence.item(), leader, builder);
            return;
        }
        if (content instanceof StepAnnotationPlane) {
            StepAnnotationPlane annotationPlane = (StepAnnotationPlane) content;
            for (StepEntity element : annotationPlane.elements()) {
                appendPmiLeader(element, leader, builder);
            }
            return;
        }
        if (content instanceof StepFaceBasedSurfaceModel) {
            StepFaceBasedSurfaceModel surfaceModel = (StepFaceBasedSurfaceModel) content;
            for (StepEntity faceSet : surfaceModel.faceSets()) {
                appendPmiLeader(faceSet, leader, builder);
            }
            return;
        }
        if (content instanceof StepShellBasedSurfaceModel) {
            StepShellBasedSurfaceModel surfaceModel = (StepShellBasedSurfaceModel) content;
            for (StepEntity shell : surfaceModel.shells()) {
                appendPmiLeader(shell, leader, builder);
            }
            return;
        }
        if (content instanceof StepManifoldSolidBrep) {
            StepManifoldSolidBrep solid = (StepManifoldSolidBrep) content;
            appendPmiLeader(solid.outer(), leader, builder);
            return;
        }
        if (content instanceof StepBrepWithVoids) {
            StepBrepWithVoids solid = (StepBrepWithVoids) content;
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
        if (content instanceof StepAdvancedFace) {
            StepAdvancedFace face = (StepAdvancedFace) content;
            for (StepFaceBound bound : face.bounds()) {
                appendPmiLeader(bound, leader, builder);
            }
            return;
        }
        if (content instanceof StepFaceSurface) {
            StepFaceSurface face = (StepFaceSurface) content;
            for (StepFaceBound bound : face.bounds()) {
                appendPmiLeader(bound, leader, builder);
            }
            return;
        }
        if (content instanceof StepOrientedFace) {
            StepOrientedFace face = (StepOrientedFace) content;
            appendPmiLeader(face.faceElement(), leader, builder);
            return;
        }
        if (content instanceof StepFaceBound) {
            StepFaceBound faceBound = (StepFaceBound) content;
            appendPmiLeader(faceBound.loop(), leader, builder);
            return;
        }
        if (content instanceof StepOpenShell) {
            StepOpenShell shell = (StepOpenShell) content;
            for (StepFaceEntity face : shell.faces()) {
                appendPmiLeader(face, leader, builder);
            }
            return;
        }
        if (content instanceof StepSurfacedOpenShell) {
            StepSurfacedOpenShell shell = (StepSurfacedOpenShell) content;
            for (StepFaceEntity face : shell.faces()) {
                appendPmiLeader(face, leader, builder);
            }
            return;
        }
        if (content instanceof StepOrientedOpenShell) {
            StepOrientedOpenShell shell = (StepOrientedOpenShell) content;
            appendPmiLeader(shell.openShellElement(), leader, builder);
            return;
        }
        if (content instanceof StepClosedShell) {
            StepClosedShell shell = (StepClosedShell) content;
            for (StepFaceEntity face : shell.faces()) {
                appendPmiLeader(face, leader, builder);
            }
            return;
        }
        if (content instanceof StepOrientedClosedShell) {
            StepOrientedClosedShell shell = (StepOrientedClosedShell) content;
            appendPmiLeader(shell.closedShellElement(), leader, builder);
            return;
        }
        if (content instanceof StepConnectedFaceSet) {
            StepConnectedFaceSet faceSet = (StepConnectedFaceSet) content;
            for (StepFaceEntity face : faceSet.faces()) {
                appendPmiLeader(face, leader, builder);
            }
            return;
        }
        if (content instanceof StepConnectedFaceSubSet) {
            StepConnectedFaceSubSet faceSet = (StepConnectedFaceSubSet) content;
            for (StepFaceEntity face : faceSet.faces()) {
                appendPmiLeader(face, leader, builder);
            }
            return;
        }
        if (content instanceof StepAnnotationPointOccurrence) {
            StepAnnotationPointOccurrence pointOccurrence = (StepAnnotationPointOccurrence) content;
            appendPmiLeader(pointOccurrence.item(), leader, builder);
            return;
        }
        if (content instanceof StepAnnotationCurveOccurrence) {
            StepAnnotationCurveOccurrence occurrence = (StepAnnotationCurveOccurrence) content;
            appendPmiLeader(occurrence.item(), leader, builder);
            return;
        }
        if (content instanceof StepAnnotationFillArea) {
            StepAnnotationFillArea fillArea = (StepAnnotationFillArea) content;
            List<CartesianPoint> sampled = StepPreviewJsonExporter.sampleAnnotationFillAreaPoints(fillArea, builder);
            if (sampled != null) {
                for (CartesianPoint point : sampled) {
                    leader.add(StepPreviewJsonExporter.toPointPayload(point));
                }
            }
            return;
        }
        if (content instanceof StepAnnotationFillAreaOccurrence) {
            StepAnnotationFillAreaOccurrence fillAreaOccurrence = (StepAnnotationFillAreaOccurrence) content;
            appendPmiLeader(fillAreaOccurrence.item(), leader, builder);
            return;
        }
        if (content instanceof StepAnnotationSymbol) {
            StepAnnotationSymbol annotationSymbol = (StepAnnotationSymbol) content;
            List<CartesianPoint> sampled = StepPreviewJsonExporter.sampleLooseEdgePoints(annotationSymbol, builder);
            if (sampled != null) {
                for (CartesianPoint point : sampled) {
                    leader.add(StepPreviewJsonExporter.toPointPayload(point));
                }
            }
            return;
        }
        if (content instanceof StepAnnotationSymbolOccurrence) {
            StepAnnotationSymbolOccurrence symbolOccurrence = (StepAnnotationSymbolOccurrence) content;
            appendPmiLeader(symbolOccurrence.item(), leader, builder);
            return;
        }
        if (content instanceof StepAnnotationSubfigureOccurrence) {
            StepAnnotationSubfigureOccurrence subfigureOccurrence = (StepAnnotationSubfigureOccurrence) content;
            appendPmiLeader(subfigureOccurrence.item(), leader, builder);
            return;
        }
        if (content instanceof StepAnnotationText) {
            StepAnnotationText annotationText = (StepAnnotationText) content;
            List<CartesianPoint> sampled = StepPreviewJsonExporter.sampleLooseEdgePoints(annotationText, builder);
            if (sampled != null) {
                for (CartesianPoint point : sampled) {
                    leader.add(StepPreviewJsonExporter.toPointPayload(point));
                }
            }
            return;
        }
        if (content instanceof StepAnnotationTextCharacter) {
            StepAnnotationTextCharacter annotationTextCharacter = (StepAnnotationTextCharacter) content;
            List<CartesianPoint> sampled = StepPreviewJsonExporter.sampleLooseEdgePoints(annotationTextCharacter, builder);
            if (sampled != null) {
                for (CartesianPoint point : sampled) {
                    leader.add(StepPreviewJsonExporter.toPointPayload(point));
                }
            }
            return;
        }
        if (content instanceof StepDimensionCurve) {
            StepDimensionCurve dimensionCurve = (StepDimensionCurve) content;
            appendPmiLeader(dimensionCurve.item(), leader, builder);
            return;
        }
        if (content instanceof StepLeaderCurve) {
            StepLeaderCurve leaderCurve = (StepLeaderCurve) content;
            appendPmiLeader(leaderCurve.item(), leader, builder);
            return;
        }
        if (content instanceof StepProjectionCurve) {
            StepProjectionCurve projectionCurve = (StepProjectionCurve) content;
            appendPmiLeader(projectionCurve.item(), leader, builder);
            return;
        }
        if (content instanceof StepDraughtingAnnotationOccurrence) {
            StepDraughtingAnnotationOccurrence annotationOccurrence = (StepDraughtingAnnotationOccurrence) content;
            appendPmiLeader(annotationOccurrence.item(), leader, builder);
            return;
        }
        if (content instanceof StepTerminatorSymbol) {
            StepTerminatorSymbol terminatorSymbol = (StepTerminatorSymbol) content;
            appendPmiLeader(terminatorSymbol.annotatedCurve(), leader, builder);
            return;
        }
        if (content instanceof StepPath) {
            StepPath path = (StepPath) content;
            appendPmiPathLeader(path.edges(), leader, builder);
            return;
        }
        if (content instanceof StepOpenPath) {
            StepOpenPath path = (StepOpenPath) content;
            appendPmiPathLeader(path.edges(), leader, builder);
            return;
        }
        if (content instanceof StepSubpath) {
            StepSubpath subpath = (StepSubpath) content;
            appendPmiPathLeader(subpath.edges(), leader, builder);
            return;
        }
        if (content instanceof StepOrientedPath) {
            StepOrientedPath orientedPath = (StepOrientedPath) content;
            appendPmiPathLeader(orientedPath.edges(), leader, builder);
            return;
        }
        if (content instanceof StepConnectedEdgeSet) {
            StepConnectedEdgeSet connectedEdgeSet = (StepConnectedEdgeSet) content;
            for (StepEntity edge : connectedEdgeSet.edges()) {
                appendPmiLeader(edge, leader, builder);
            }
            return;
        }
        if (content instanceof StepEdgeBasedWireframeModel) {
            StepEdgeBasedWireframeModel wireframeModel = (StepEdgeBasedWireframeModel) content;
            for (StepConnectedEdgeSet boundary : wireframeModel.boundaries()) {
                appendPmiLeader(boundary, leader, builder);
            }
            return;
        }
        if (content instanceof StepShellBasedWireframeModel) {
            StepShellBasedWireframeModel wireframeModel = (StepShellBasedWireframeModel) content;
            for (StepEntity boundary : wireframeModel.boundaries()) {
                appendPmiLeader(boundary, leader, builder);
            }
            return;
        }
        if (content instanceof StepWireShell) {
            StepWireShell wireShell = (StepWireShell) content;
            for (StepLoop loop : wireShell.loops()) {
                appendPmiLeader(loop, leader, builder);
            }
            return;
        }
        if (content instanceof StepEdgeLoop) {
            StepEdgeLoop edgeLoop = (StepEdgeLoop) content;
            appendPmiPathLeader(edgeLoop.edges(), leader, builder);
            return;
        }
        if (content instanceof StepVertexLoop) {
            StepVertexLoop vertexLoop = (StepVertexLoop) content;
            leader.add(StepPreviewJsonExporter.toPointPayload(StepPreviewJsonExporter.pointFromStep(vertexLoop.loopVertex().point())));
            return;
        }
        if (content instanceof StepPolyLoop) {
            StepPolyLoop polyLoop = (StepPolyLoop) content;
            for (StepCartesianPoint point : polyLoop.polygon()) {
                leader.add(StepPreviewJsonExporter.toPointPayload(StepPreviewJsonExporter.pointFromStep(point)));
            }
            return;
        }
        if (content instanceof StepVertexShell) {
            StepVertexShell vertexShell = (StepVertexShell) content;
            leader.add(StepPreviewJsonExporter.toPointPayload(StepPreviewJsonExporter.pointFromStep(vertexShell.extent().loopVertex().point())));
            return;
        }
        if (content instanceof StepGeometricReplica && "POINT_REPLICA".equals(((StepGeometricReplica) content).entityName())) {
            StepGeometricReplica replica = (StepGeometricReplica) content;
            CartesianPoint point = pointFromReplica(replica, builder);
            if (point != null) {
                leader.add(StepPreviewJsonExporter.toPointPayload(point));
            }
            return;
        }
        if (content instanceof StepCartesianPoint) {
            StepCartesianPoint point = (StepCartesianPoint) content;
            leader.add(StepPreviewJsonExporter.toPointPayload(StepPreviewJsonExporter.pointFromStep(point)));
            return;
        }
        if (content instanceof StepVertexPoint) {
            StepVertexPoint vertexPoint = (StepVertexPoint) content;
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
        if (loop instanceof EdgeLoop) {
            EdgeLoop edgeLoop = (EdgeLoop) loop;
            for (OrientedEdge edge : edgeLoop.edges()) {
                appendTopologyEdgeLeader(edge, leader);
            }
            return;
        }
        if (loop instanceof VertexLoop) {
            VertexLoop vertexLoop = (VertexLoop) loop;
            leader.add(StepPreviewJsonExporter.toPointPayload(vertexLoop.vertex().point()));
            return;
        }
        if (loop instanceof PolyLoop) {
            PolyLoop polyLoop = (PolyLoop) loop;
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
        if (target instanceof StepFaceEntity) {
            StepFaceEntity face = (StepFaceEntity) target;
            return StepPreviewJsonExporter.faceDisplayName(face);
        }
        if (target instanceof StepEdgeCurve) {
            StepEdgeCurve edge = (StepEdgeCurve) target;
            return edge.name();
        }
        if (target instanceof StepSubedge) {
            StepSubedge subedge = (StepSubedge) target;
            return subedge.name();
        }
        if (target instanceof StepOrientedEdge) {
            StepOrientedEdge orientedEdge = (StepOrientedEdge) target;
            return orientedEdge.name();
        }
        if (target instanceof StepPath) {
            StepPath path = (StepPath) target;
            return path.name();
        }
        if (target instanceof StepOpenPath) {
            StepOpenPath path = (StepOpenPath) target;
            return path.name();
        }
        if (target instanceof StepSubpath) {
            StepSubpath subpath = (StepSubpath) target;
            return subpath.name();
        }
        if (target instanceof StepOrientedPath) {
            StepOrientedPath orientedPath = (StepOrientedPath) target;
            return orientedPath.name();
        }
        if (target instanceof StepConnectedEdgeSet) {
            StepConnectedEdgeSet edgeSet = (StepConnectedEdgeSet) target;
            return edgeSet.name();
        }
        if (target instanceof StepPointSet) {
            StepPointSet pointSet = (StepPointSet) target;
            return pointSet.name();
        }
        if (target instanceof StepAnnotationSymbol) {
            StepAnnotationSymbol annotationSymbol = (StepAnnotationSymbol) target;
            return annotationSymbol.name();
        }
        if (target instanceof StepAnnotationText) {
            StepAnnotationText annotationText = (StepAnnotationText) target;
            return annotationText.name();
        }
        if (target instanceof StepAnnotationTextCharacter) {
            StepAnnotationTextCharacter annotationTextCharacter = (StepAnnotationTextCharacter) target;
            return annotationTextCharacter.name();
        }
        if (target instanceof StepAnnotationFillArea) {
            StepAnnotationFillArea fillArea = (StepAnnotationFillArea) target;
            return fillArea.name();
        }
        if (target instanceof StepGeometricSet) {
            StepGeometricSet geometricSet = (StepGeometricSet) target;
            return geometricSet.name();
        }
        if (target instanceof StepGeometricCurveSet) {
            StepGeometricCurveSet curveSet = (StepGeometricCurveSet) target;
            return curveSet.name();
        }
        if (target instanceof StepOpenShell) {
            StepOpenShell openShell = (StepOpenShell) target;
            return openShell.name();
        }
        if (target instanceof StepSurfacedOpenShell) {
            StepSurfacedOpenShell openShell = (StepSurfacedOpenShell) target;
            return openShell.name();
        }
        if (target instanceof StepOrientedOpenShell) {
            StepOrientedOpenShell openShell = (StepOrientedOpenShell) target;
            return openShell.name();
        }
        if (target instanceof StepClosedShell) {
            StepClosedShell closedShell = (StepClosedShell) target;
            return closedShell.name();
        }
        if (target instanceof StepOrientedClosedShell) {
            StepOrientedClosedShell closedShell = (StepOrientedClosedShell) target;
            return closedShell.name();
        }
        if (target instanceof StepWireShell) {
            StepWireShell wireShell = (StepWireShell) target;
            return wireShell.name();
        }
        if (target instanceof StepVertexShell) {
            StepVertexShell vertexShell = (StepVertexShell) target;
            return vertexShell.name();
        }
        if (target instanceof StepEdgeLoop) {
            StepEdgeLoop edgeLoop = (StepEdgeLoop) target;
            return edgeLoop.name();
        }
        if (target instanceof StepVertexLoop) {
            StepVertexLoop vertexLoop = (StepVertexLoop) target;
            return vertexLoop.name();
        }
        if (target instanceof StepPolyLoop) {
            StepPolyLoop polyLoop = (StepPolyLoop) target;
            return polyLoop.name();
        }
        if (target instanceof StepConnectedFaceSet) {
            StepConnectedFaceSet faceSet = (StepConnectedFaceSet) target;
            return faceSet.name();
        }
        if (target instanceof StepConnectedFaceSubSet) {
            StepConnectedFaceSubSet faceSet = (StepConnectedFaceSubSet) target;
            return faceSet.name();
        }
        if (target instanceof StepFaceBasedSurfaceModel) {
            StepFaceBasedSurfaceModel surfaceModel = (StepFaceBasedSurfaceModel) target;
            return surfaceModel.name();
        }
        if (target instanceof StepShellBasedSurfaceModel) {
            StepShellBasedSurfaceModel surfaceModel = (StepShellBasedSurfaceModel) target;
            return surfaceModel.name();
        }
        if (target instanceof StepEdgeBasedWireframeModel) {
            StepEdgeBasedWireframeModel wireframeModel = (StepEdgeBasedWireframeModel) target;
            return wireframeModel.name();
        }
        if (target instanceof StepShellBasedWireframeModel) {
            StepShellBasedWireframeModel wireframeModel = (StepShellBasedWireframeModel) target;
            return wireframeModel.name();
        }
        if (target instanceof StepManifoldSolidBrep) {
            StepManifoldSolidBrep solid = (StepManifoldSolidBrep) target;
            return solid.name();
        }
        if (target instanceof StepBrepWithVoids) {
            StepBrepWithVoids solid = (StepBrepWithVoids) target;
            return solid.name();
        }
        if (target instanceof StepSweptAreaSolid) {
            StepSweptAreaSolid solid = (StepSweptAreaSolid) target;
            return solid.name();
        }
        if (target instanceof StepSolidReplica) {
            StepSolidReplica solid = (StepSolidReplica) target;
            return solid.name();
        }
        if (target instanceof StepCsgSolid) {
            StepCsgSolid solid = (StepCsgSolid) target;
            return solid.name();
        }
        if (target instanceof StepCsgPrimitive) {
            StepCsgPrimitive solid = (StepCsgPrimitive) target;
            return solid.name();
        }
        if (target instanceof StepBooleanResult) {
            StepBooleanResult solid = (StepBooleanResult) target;
            return solid.name();
        }
        if (target instanceof StepBooleanClippingResult) {
            StepBooleanClippingResult solid = (StepBooleanClippingResult) target;
            return solid.name();
        }
        if (target instanceof StepSweptDiskSolid) {
            StepSweptDiskSolid solid = (StepSweptDiskSolid) target;
            return solid.name();
        }
        if (target instanceof StepComplexClippingResult) {
            StepComplexClippingResult solid = (StepComplexClippingResult) target;
            return solid.name();
        }
        if (target instanceof StepRepresentation) {
            StepRepresentation representation = (StepRepresentation) target;
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
        if (definition instanceof StepAnnotationOccurrenceRelationship) {
            StepAnnotationOccurrenceRelationship relationship = (StepAnnotationOccurrenceRelationship) definition;
            appendPmiTarget(
                    targetsByUsageId,
                    identifiedItem.id(),
                    representation,
                    instanceIdsByTargetId,
                    relationship.entityName(),
                    relationship.id()
            );
        } else if (definition instanceof StepDraughtingCalloutRelationship) {
            StepDraughtingCalloutRelationship relationship = (StepDraughtingCalloutRelationship) definition;
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
        if (item instanceof StepCartesianPoint) {
            StepCartesianPoint point = (StepCartesianPoint) item;
            return StepPreviewJsonExporter.pointFromStep(point);
        }
        if (item instanceof StepVertexPoint) {
            StepVertexPoint vertexPoint = (StepVertexPoint) item;
            return StepPreviewJsonExporter.pointFromStep(vertexPoint.point());
        }
        if (item instanceof StepVertexShell) {
            StepVertexShell vertexShell = (StepVertexShell) item;
            return StepPreviewJsonExporter.pointFromStep(vertexShell.extent().loopVertex().point());
        }
        if (item instanceof StepPointSet) {
            StepPointSet pointSet = (StepPointSet) item;
            return pointFromPointSet(pointSet, builder);
        }
        if (item instanceof StepGeometricSet) {
            StepGeometricSet geometricSet = (StepGeometricSet) item;
            return pointFromGeometricSet(geometricSet, builder);
        }
        if (item instanceof StepGeometricCurveSet) {
            StepGeometricCurveSet curveSet = (StepGeometricCurveSet) item;
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
        if (builder != null && item instanceof StepGeometricReplica && "POINT_REPLICA".equals(replica.entityName())) {
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
        return sampled.get(0);
    }

    static CartesianPoint pointFromAnnotationOccurrence(StepEntity occurrence, StepCadBuilder builder) {
            switch (occurrence) {
      case StepAnnotationPointOccurrence __:
        return pointFromAnnotationPoint(pointOccurrence.item(), builder);
      case StepAnnotationCurveOccurrence __:
        return pointFromCurveCarrier(curveOccurrence.item(), builder);
      case StepLeaderCurve __:
        return pointFromCurveCarrier(leaderCurve.item(), builder);
      case StepDimensionCurve __:
        return pointFromCurveCarrier(dimensionCurve.item(), builder);
      case StepProjectionCurve __:
        return pointFromCurveCarrier(projectionCurve.item(), builder);
      case StepAnnotationFillAreaOccurrence __:
        return pointFromAnnotationPoint(fillAreaOccurrence.fillStyleTarget(), builder);
      case StepAnnotationFillArea __:
        return pointFromAnnotationFillArea(fillArea, builder);
      case StepAnnotationSymbol __:
        return pointFromAnnotationSymbol(annotationSymbol);
      case StepAnnotationSymbolOccurrence __:
        return pointFromAnnotationOccurrence(symbolOccurrence.item(), builder);
      case StepAnnotationSubfigureOccurrence __:
        return pointFromAnnotationOccurrence(subfigureOccurrence.item(), builder);
      case StepAnnotationPlaceholderOccurrence __:
        return pointFromPlaceholderItem(placeholderOccurrence.item(), builder);
      case StepAnnotationPlane __:
        return pointFromAnnotationPlane(annotationPlane, builder);
      case StepAnnotationText __:
        return pointFromPlacement(annotationText.mappingTarget());
      case StepAnnotationTextCharacter __:
        return pointFromPlacement(annotationTextCharacter.mappingTarget());
      case StepAnnotationTextOccurrence __:
        return pointFromAnnotationPoint(textOccurrence.position(), builder);
      case StepDraughtingAnnotationOccurrence __:
        return pointFromAnnotationOccurrence(annotationOccurrence.item(), builder);
      case StepPointSet __:
        return pointFromPointSet(pointSet, builder);
      case StepGeometricSet __:
        return pointFromGeometricSet(geometricSet, builder);
      case StepGeometricCurveSet __:
        return pointFromGeometricCurveSet(curveSet, builder);
      case StepVertexShell __:
        return StepPreviewJsonExporter.pointFromStep(vertexShell.extent().loopVertex().point());
      default:
        throw new IllegalArgumentException("Unknown value type: " + occurrence);
    }
    }

    public static CartesianPoint pointFromCurveCarrier(StepEntity item, StepCadBuilder builder) {
        List<CartesianPoint> sampled = StepPreviewJsonExporter.sampleLooseEdgePoints(item, builder);
        if (sampled == null || sampled.isEmpty()) {
            return null;
        }
        return sampled.get(0);
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
        return pointFromAnnotationPoint(item, builder);
    }

    static void collectPlaceholderPositions(
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
            point = pointFromAnnotationPoint(item, builder);
        }
        if (point != null) {
            positions.add(point);
        }
    }

    public static CartesianPoint pointFromPlacement(StepEntity placement) {
        if (placement instanceof StepAxis2Placement3D) {
            StepAxis2Placement3D placement3D = (StepAxis2Placement3D) placement;
            return StepPreviewJsonExporter.pointFromStep(placement3D.location());
        }
        if (placement instanceof StepAxis2Placement2D) {
            StepAxis2Placement2D placement2D = (StepAxis2Placement2D) placement;
            StepCartesianPoint point = placement2D.location();
            return new CartesianPoint(point.coordinates().get(0), point.coordinates().get(1), 0.0);
        }
        return null;
    }

    public static CartesianPoint pointFromReplica(StepGeometricReplica replica, StepCadBuilder builder) {
        if (replica.parent() instanceof StepCartesianPoint) {
            StepCartesianPoint point = (StepCartesianPoint) replica.parent();
            return transformPoint(StepPreviewJsonExporter.pointFromStep(point), replica.transformation(), builder);
        }
        if (replica.parent() instanceof StepVertexPoint) {
            StepVertexPoint vertexPoint = (StepVertexPoint) replica.parent();
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
        CartesianPoint position = pointFromAnnotationPoint(text.position(), builder);
        if (position == null) {
            return null;
        }
        return new PmiPayload(
                callout.name(),
                text.text(),
                StepPreviewJsonExporter.toPointPayload(position),
                List.copyOf(leader),
                targets.stream().map(PmiTargetPayload::id).collect(Collectors.toList()),
                List.copyOf(targets)
        );
    }
}
