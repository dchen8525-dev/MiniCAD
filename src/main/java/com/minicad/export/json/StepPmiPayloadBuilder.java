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
            if (entity instanceof StepDraughtingCallout) {
            StepDraughtingCallout callout = (StepDraughtingCallout) entity;
                PmiPayload payload = toPmiPayload(callout, targetsByUsageId.getOrDefault(callout.id(), List.of()), builder);
                if (payload != null) {
                    pmi.add(payload);
                }
            } else if (entity instanceof StepAnnotationTextOccurrence) {
            StepAnnotationTextOccurrence textOccurrence = (StepAnnotationTextOccurrence) entity;
                CartesianPoint position = StepPmiPayloadBuilder.pointFromAnnotationPoint(textOccurrence.position(), builder);
                if (position == null) {
                    continue;
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
            } else if (entity instanceof StepAnnotationPointOccurrence) {
            StepAnnotationPointOccurrence pointOccurrence = (StepAnnotationPointOccurrence) entity;
                CartesianPoint position = StepPmiPayloadBuilder.pointFromAnnotationPoint(pointOccurrence.item(), builder);
                if (position != null) {
                    List<PmiTargetPayload> targets = targetsByUsageId.getOrDefault(pointOccurrence.id(), List.of());
                    pmi.add(toStandalonePointPmi(pointOccurrence.id(), pointOccurrence.name(), position, targets));
                }
            } else if (entity instanceof StepAnnotationFillAreaOccurrence) {
            StepAnnotationFillAreaOccurrence fillAreaOccurrence = (StepAnnotationFillAreaOccurrence) entity;
                CartesianPoint position = StepPmiPayloadBuilder.pointFromAnnotationPoint(fillAreaOccurrence.fillStyleTarget(), builder);
                if (position != null) {
                    List<PmiTargetPayload> targets = targetsByUsageId.getOrDefault(fillAreaOccurrence.id(), List.of());
                    pmi.add(toStandalonePointPmi(fillAreaOccurrence.id(), fillAreaOccurrence.name(), position, targets));
                }
            } else if (entity instanceof StepAnnotationPlaceholderOccurrence) {
            StepAnnotationPlaceholderOccurrence placeholderOccurrence = (StepAnnotationPlaceholderOccurrence) entity;
                appendPlaceholderPmi(
                        placeholderOccurrence,
                        pmi,
                        builder,
                        targetsByUsageId.getOrDefault(placeholderOccurrence.id(), List.of()));
            } else if (entity instanceof StepAnnotationSymbolOccurrence) {
            StepAnnotationSymbolOccurrence symbolOccurrence = (StepAnnotationSymbolOccurrence) entity;
                CartesianPoint position = pointFromAnnotationOccurrence(symbolOccurrence.item(), builder);
                if (position != null) {
                    List<PmiTargetPayload> targets = targetsByUsageId.getOrDefault(symbolOccurrence.id(), List.of());
                    pmi.add(toStandalonePointPmi(symbolOccurrence.id(), symbolOccurrence.name(), position, targets));
                }
            } else if (entity instanceof StepAnnotationSymbol) {
            StepAnnotationSymbol annotationSymbol = (StepAnnotationSymbol) entity;
                CartesianPoint position = pointFromAnnotationSymbol(annotationSymbol);
                if (position != null) {
                    List<PmiTargetPayload> targets = targetsByUsageId.getOrDefault(annotationSymbol.id(), List.of());
                    pmi.add(toStandalonePointPmi(annotationSymbol.id(), annotationSymbol.name(), position, targets));
                }
            } else if (entity instanceof StepAnnotationText) {
            StepAnnotationText annotationText = (StepAnnotationText) entity;
                CartesianPoint position = pointFromPlacement(annotationText.mappingTarget());
                if (position != null) {
                    List<PmiTargetPayload> targets = targetsByUsageId.getOrDefault(annotationText.id(), List.of());
                    pmi.add(toStandalonePointPmi(annotationText.id(), annotationText.name(), position, targets));
                }
            } else if (entity instanceof StepAnnotationTextCharacter) {
            StepAnnotationTextCharacter annotationTextCharacter = (StepAnnotationTextCharacter) entity;
                CartesianPoint position = pointFromPlacement(annotationTextCharacter.mappingTarget());
                if (position != null) {
                    List<PmiTargetPayload> targets = targetsByUsageId.getOrDefault(annotationTextCharacter.id(), List.of());
                    pmi.add(toStandalonePointPmi(annotationTextCharacter.id(), annotationTextCharacter.name(), position, targets));
                }
            } else if (entity instanceof StepAnnotationFillArea) {
            StepAnnotationFillArea fillArea = (StepAnnotationFillArea) entity;
                CartesianPoint position = pointFromAnnotationFillArea(fillArea, builder);
                if (position != null) {
                    List<PmiTargetPayload> targets = targetsByUsageId.getOrDefault(fillArea.id(), List.of());
                    pmi.add(toStandalonePointPmi(fillArea.id(), fillArea.name(), position, targets));
                }
            } else if (entity instanceof StepAnnotationSubfigureOccurrence) {
            StepAnnotationSubfigureOccurrence subfigureOccurrence = (StepAnnotationSubfigureOccurrence) entity;
                CartesianPoint position = pointFromAnnotationOccurrence(subfigureOccurrence.item(), builder);
                if (position != null) {
                    List<PmiTargetPayload> targets = targetsByUsageId.getOrDefault(subfigureOccurrence.id(), List.of());
                    pmi.add(toStandalonePointPmi(subfigureOccurrence.id(), subfigureOccurrence.name(), position, targets));
                }
            } else if (entity instanceof StepAnnotationPlane) {
            StepAnnotationPlane annotationPlane = (StepAnnotationPlane) entity;
                appendAnnotationPlanePmi(
                        annotationPlane,
                        pmi,
                        builder,
                        targetsByUsageId.getOrDefault(annotationPlane.id(), List.of()));
            } else if (entity instanceof StepDraughtingAnnotationOccurrence) {
            StepDraughtingAnnotationOccurrence annotationOccurrence = (StepDraughtingAnnotationOccurrence) entity;
                appendDraughtingAnnotationPmi(
                        annotationOccurrence,
                        pmi,
                        builder,
                        targetsByUsageId.getOrDefault(annotationOccurrence.id(), List.of()));
            } else if (entity instanceof StepAnnotationOccurrenceRelationship) {
            StepAnnotationOccurrenceRelationship relationship = (StepAnnotationOccurrenceRelationship) entity;
                appendAnnotationOccurrenceRelationshipPmi(relationship, pmi, builder);
            } else if (entity instanceof StepPointSet) {
            StepPointSet pointSet = (StepPointSet) entity;
                appendPointSetPmi(pointSet, pmi, builder);
            } else if (entity instanceof StepGeometricMeasurement) {
            StepGeometricMeasurement measurement = (StepGeometricMeasurement) entity;
                appendGeometricMeasurementPmi(measurement, pmi, builder);
            } else if (entity instanceof StepVertexShell) {
            StepVertexShell vertexShell = (StepVertexShell) entity;
                pmi.add(toStandalonePointPmi(
                        vertexShell.id(),
                        vertexShell.name(),
                        StepPointExtractor.pointFromStep(vertexShell.extent().loopVertex().point())
                ));
            } else if (entity instanceof StepGeometricReplica && "POINT_REPLICA".equals(((StepGeometricReplica) entity).entityName())) {
            StepGeometricReplica replica = (StepGeometricReplica) entity;
                CartesianPoint position = StepPmiPayloadBuilder.pointFromReplica(replica, builder);
                if (position != null) {
                    pmi.add(toStandalonePointPmi(replica.id(), replica.name(), position));
                }
            } else if (entity instanceof StepFillAreaWithOutline) {
            StepFillAreaWithOutline fillArea = (StepFillAreaWithOutline) entity;
                appendFillAreaWithOutlinePmi(fillArea, pmi, builder);
            } else if (entity instanceof StepGeometricTolerance) {
            StepGeometricTolerance tolerance = (StepGeometricTolerance) entity;
                appendGeometricTolerancePmi(tolerance, pmi, builder);
            } else if (entity instanceof StepGeometricToleranceWithDatumReference) {
            StepGeometricToleranceWithDatumReference tolerance = (StepGeometricToleranceWithDatumReference) entity;
                appendGeometricToleranceWithDatumPmi(tolerance, pmi, builder);
            } else if (entity instanceof StepGeometricToleranceWithDefinedAreaUnit) {
            StepGeometricToleranceWithDefinedAreaUnit tolerance = (StepGeometricToleranceWithDefinedAreaUnit) entity;
                appendGeometricToleranceWithAreaUnitPmi(tolerance, pmi, builder);
            } else if (entity instanceof StepGeometricToleranceWithMaximumTolerance) {
            StepGeometricToleranceWithMaximumTolerance tolerance = (StepGeometricToleranceWithMaximumTolerance) entity;
                appendGeometricToleranceWithMaxPmi(tolerance, pmi, builder);
            } else if (entity instanceof StepDimensionalLocation) {
            StepDimensionalLocation location = (StepDimensionalLocation) entity;
                appendDimensionalLocationPmi(location, pmi, builder);
            } else if (entity instanceof StepToleranceZone) {
            StepToleranceZone zone = (StepToleranceZone) entity;
                appendToleranceZonePmi(zone, pmi, builder);
            } else if (entity instanceof StepDatum) {
            StepDatum datum = (StepDatum) entity;
                appendDatumPmi(datum, pmi, builder);
            } else if (entity instanceof StepDatumTarget) {
            StepDatumTarget datumTarget = (StepDatumTarget) entity;
                appendDatumTargetPmi(datumTarget, pmi, builder);
            }
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

    private static void appendPmiLeader(
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
            List<CartesianPoint> sampled = sampleAnnotationFillAreaPoints(fillArea, builder);
            if (sampled != null) {
                for (CartesianPoint point : sampled) {
                    leader.add(PayloadConversionHelper.toPointPayload(point));
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
            List<CartesianPoint> sampled = sampleLooseEdgePoints(annotationSymbol, builder);
            if (sampled != null) {
                for (CartesianPoint point : sampled) {
                    leader.add(PayloadConversionHelper.toPointPayload(point));
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
            List<CartesianPoint> sampled = sampleLooseEdgePoints(annotationText, builder);
            if (sampled != null) {
                for (CartesianPoint point : sampled) {
                    leader.add(PayloadConversionHelper.toPointPayload(point));
                }
            }
            return;
        }
        if (content instanceof StepAnnotationTextCharacter) {
            StepAnnotationTextCharacter annotationTextCharacter = (StepAnnotationTextCharacter) content;
            List<CartesianPoint> sampled = sampleLooseEdgePoints(annotationTextCharacter, builder);
            if (sampled != null) {
                for (CartesianPoint point : sampled) {
                    leader.add(PayloadConversionHelper.toPointPayload(point));
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
            for (StepEntity loop : wireShell.loops()) {
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
            leader.add(PayloadConversionHelper.toPointPayload(StepPointExtractor.pointFromStep(vertexLoop.loopVertex().point())));
            return;
        }
        if (content instanceof StepPolyLoop) {
            StepPolyLoop polyLoop = (StepPolyLoop) content;
            for (StepCartesianPoint point : polyLoop.polygon()) {
                leader.add(PayloadConversionHelper.toPointPayload(StepPointExtractor.pointFromStep(point)));
            }
            return;
        }
        if (content instanceof StepVertexShell) {
            StepVertexShell vertexShell = (StepVertexShell) content;
            leader.add(PayloadConversionHelper.toPointPayload(StepPointExtractor.pointFromStep(vertexShell.extent().loopVertex().point())));
            return;
        }
        if (content instanceof StepGeometricReplica && "POINT_REPLICA".equals(((StepGeometricReplica) content).entityName())) {
            StepGeometricReplica replica = (StepGeometricReplica) content;
            CartesianPoint point = StepPmiPayloadBuilder.pointFromReplica(replica, builder);
            if (point != null) {
                leader.add(PayloadConversionHelper.toPointPayload(point));
            }
            return;
        }
        if (content instanceof StepCartesianPoint) {
            StepCartesianPoint point = (StepCartesianPoint) content;
            leader.add(PayloadConversionHelper.toPointPayload(StepPointExtractor.pointFromStep(point)));
            return;
        }
        if (content instanceof StepVertexPoint) {
            StepVertexPoint vertexPoint = (StepVertexPoint) content;
            leader.add(PayloadConversionHelper.toPointPayload(StepPointExtractor.pointFromStep(vertexPoint.point())));
            return;
        }
        List<CartesianPoint> sampled = sampleLooseEdgePoints(content, builder);
        if (sampled == null) {
            return;
        }
        for (CartesianPoint point : sampled) {
            leader.add(PayloadConversionHelper.toPointPayload(point));
        }
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

    private static CartesianPoint pointFromAnnotationOccurrence(StepEntity occurrence, StepCadBuilder builder) {
        if (occurrence instanceof StepAnnotationPointOccurrence) {
            StepAnnotationPointOccurrence pointOccurrence = (StepAnnotationPointOccurrence) occurrence;
            return StepPmiPayloadBuilder.pointFromAnnotationPoint(pointOccurrence.item(), builder);
        } else if (occurrence instanceof StepAnnotationCurveOccurrence) {
            StepAnnotationCurveOccurrence curveOccurrence = (StepAnnotationCurveOccurrence) occurrence;
            return pointFromCurveCarrier(curveOccurrence.item(), builder);
        } else if (occurrence instanceof StepLeaderCurve) {
            StepLeaderCurve leaderCurve = (StepLeaderCurve) occurrence;
            return pointFromCurveCarrier(leaderCurve.item(), builder);
        } else if (occurrence instanceof StepDimensionCurve) {
            StepDimensionCurve dimensionCurve = (StepDimensionCurve) occurrence;
            return pointFromCurveCarrier(dimensionCurve.item(), builder);
        } else if (occurrence instanceof StepProjectionCurve) {
            StepProjectionCurve projectionCurve = (StepProjectionCurve) occurrence;
            return pointFromCurveCarrier(projectionCurve.item(), builder);
        } else if (occurrence instanceof StepAnnotationFillAreaOccurrence) {
            StepAnnotationFillAreaOccurrence fillAreaOccurrence = (StepAnnotationFillAreaOccurrence) occurrence;
            return StepPmiPayloadBuilder.pointFromAnnotationPoint(fillAreaOccurrence.fillStyleTarget(), builder);
        } else if (occurrence instanceof StepAnnotationFillArea) {
            StepAnnotationFillArea fillArea = (StepAnnotationFillArea) occurrence;
            return pointFromAnnotationFillArea(fillArea, builder);
        } else if (occurrence instanceof StepAnnotationSymbol) {
            StepAnnotationSymbol annotationSymbol = (StepAnnotationSymbol) occurrence;
            return pointFromAnnotationSymbol(annotationSymbol);
        } else if (occurrence instanceof StepAnnotationSymbolOccurrence) {
            StepAnnotationSymbolOccurrence symbolOccurrence = (StepAnnotationSymbolOccurrence) occurrence;
            return pointFromAnnotationOccurrence(symbolOccurrence.item(), builder);
        } else if (occurrence instanceof StepAnnotationSubfigureOccurrence) {
            StepAnnotationSubfigureOccurrence subfigureOccurrence = (StepAnnotationSubfigureOccurrence) occurrence;
            return pointFromAnnotationOccurrence(subfigureOccurrence.item(), builder);
        } else if (occurrence instanceof StepAnnotationPlaceholderOccurrence) {
            StepAnnotationPlaceholderOccurrence placeholderOccurrence = (StepAnnotationPlaceholderOccurrence) occurrence;
            return pointFromPlaceholderItem(placeholderOccurrence.item(), builder);
        } else if (occurrence instanceof StepAnnotationPlane) {
            StepAnnotationPlane annotationPlane = (StepAnnotationPlane) occurrence;
            return pointFromAnnotationPlane(annotationPlane, builder);
        } else if (occurrence instanceof StepAnnotationText) {
            StepAnnotationText annotationText = (StepAnnotationText) occurrence;
            return pointFromPlacement(annotationText.mappingTarget());
        } else if (occurrence instanceof StepAnnotationTextCharacter) {
            StepAnnotationTextCharacter annotationTextCharacter = (StepAnnotationTextCharacter) occurrence;
            return pointFromPlacement(annotationTextCharacter.mappingTarget());
        } else if (occurrence instanceof StepAnnotationTextOccurrence) {
            StepAnnotationTextOccurrence textOccurrence = (StepAnnotationTextOccurrence) occurrence;
            return StepPmiPayloadBuilder.pointFromAnnotationPoint(textOccurrence.position(), builder);
        } else if (occurrence instanceof StepDraughtingAnnotationOccurrence) {
            StepDraughtingAnnotationOccurrence annotationOccurrence = (StepDraughtingAnnotationOccurrence) occurrence;
            return pointFromAnnotationOccurrence(annotationOccurrence.item(), builder);
        } else if (occurrence instanceof StepTerminatorSymbol) {
            StepTerminatorSymbol terminatorSymbol = (StepTerminatorSymbol) occurrence;
            CartesianPoint position = pointFromAnnotationOccurrence(terminatorSymbol.item(), builder);
            if (position == null) {
                position = pointFromAnnotationOccurrence(terminatorSymbol.annotatedCurve(), builder);
            }
            return position;
        } else if (occurrence instanceof StepPointSet) {
            StepPointSet pointSet = (StepPointSet) occurrence;
            return pointFromPointSet(pointSet, builder);
        } else if (occurrence instanceof StepGeometricSet) {
            StepGeometricSet geometricSet = (StepGeometricSet) occurrence;
            return pointFromGeometricSet(geometricSet, builder);
        } else if (occurrence instanceof StepGeometricCurveSet) {
            StepGeometricCurveSet curveSet = (StepGeometricCurveSet) occurrence;
            return pointFromGeometricCurveSet(curveSet, builder);
        } else if (occurrence instanceof StepVertexShell) {
            StepVertexShell vertexShell = (StepVertexShell) occurrence;
            return StepPointExtractor.pointFromStep(vertexShell.extent().loopVertex().point());
        } else if (occurrence instanceof StepGeometricReplica
                && "POINT_REPLICA".equals(((StepGeometricReplica) occurrence).entityName())) {
            StepGeometricReplica replica = (StepGeometricReplica) occurrence;
            return builder == null ? null : StepPmiPayloadBuilder.pointFromReplica(replica, builder);
        } else {
            return null;
        }
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

}
