package com.minicad.export.json;

import com.minicad.common.StepResolutionException;
import com.minicad.common.TopologyException;
import com.minicad.common.UnsupportedGeometryException;
import com.minicad.export.glb.PreviewMaterialExporter;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Curve3;
import com.minicad.geometry.Vector3;
import com.minicad.preview.builder.PmiPayload;
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
        Map<Integer, List<String>> instanceIdsByTargetId = buildInstanceIdsByTargetId(assembly);
        for (StepEntity entity : resolved.values()) {
            if (entity instanceof StepGeometricItemSpecificUsage) {
            StepGeometricItemSpecificUsage usage = (StepGeometricItemSpecificUsage) entity;
                appendPmiTarget(
                        targetsByUsageId,
                        usage.usage().id(),
                        usage.identifiedItem(),
                        instanceIdsByTargetId,
                        null,
                        null,
                        "GEOMETRIC_ITEM_SPECIFIC_USAGE",
                        usage.id()
                );
            } else if (entity instanceof StepChainBasedGeometricItemSpecificUsage) {
            StepChainBasedGeometricItemSpecificUsage usage = (StepChainBasedGeometricItemSpecificUsage) entity;
                appendPmiTarget(
                        targetsByUsageId,
                        usage.usage().id(),
                        usage.identifiedItem(),
                        instanceIdsByTargetId,
                        null,
                        null,
                        "CHAIN_BASED_GEOMETRIC_ITEM_SPECIFIC_USAGE",
                        usage.id()
                );
            } else if (entity instanceof StepItemIdentifiedRepresentationUsage) {
            StepItemIdentifiedRepresentationUsage usage = (StepItemIdentifiedRepresentationUsage) entity;
                appendRepresentationBacklinkTarget(
                        targetsByUsageId,
                        usage.identifiedItem(),
                        usage.usedRepresentation(),
                        instanceIdsByTargetId,
                        "ITEM_IDENTIFIED_REPRESENTATION_USAGE",
                        usage.id()
                );
                appendAttachedRepresentationRelationshipTargets(
                        targetsByUsageId,
                        usage.identifiedItem(),
                        usage.usedRepresentation(),
                        resolved,
                        instanceIdsByTargetId
                );
            } else if (entity instanceof StepChainBasedItemIdentifiedRepresentationUsage) {
            StepChainBasedItemIdentifiedRepresentationUsage usage = (StepChainBasedItemIdentifiedRepresentationUsage) entity;
                appendRepresentationBacklinkTarget(
                        targetsByUsageId,
                        usage.identifiedItem(),
                        usage.leaf(),
                        instanceIdsByTargetId,
                        "CHAIN_BASED_ITEM_IDENTIFIED_REPRESENTATION_USAGE",
                        usage.id()
                );
                appendAttachedRepresentationRelationshipTargets(
                        targetsByUsageId,
                        usage.identifiedItem(),
                        usage.leaf(),
                        resolved,
                        instanceIdsByTargetId
                );
            } else if (entity instanceof StepPlacedTarget) {
            StepPlacedTarget usage = (StepPlacedTarget) entity;
                appendRepresentationBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), instanceIdsByTargetId, "PLACED_TARGET", usage.id());
                appendAttachedRepresentationRelationshipTargets(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), resolved, instanceIdsByTargetId);
                appendDefinitionBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), usage.definition(), instanceIdsByTargetId);
                appendRelationshipBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), usage.definition(), instanceIdsByTargetId);
                appendSemanticDefinitionTargets(targetsByUsageId, usage.identifiedItem(), usage.definition(), resolved, instanceIdsByTargetId);
            } else if (entity instanceof StepDraughtingModelItemAssociation) {
            StepDraughtingModelItemAssociation usage = (StepDraughtingModelItemAssociation) entity;
                appendRepresentationBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), instanceIdsByTargetId, "DRAUGHTING_MODEL_ITEM_ASSOCIATION", usage.id());
                appendAttachedRepresentationRelationshipTargets(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), resolved, instanceIdsByTargetId);
                appendDefinitionBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), usage.definition(), instanceIdsByTargetId);
                appendRelationshipBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), usage.definition(), instanceIdsByTargetId);
                appendSemanticDefinitionTargets(targetsByUsageId, usage.identifiedItem(), usage.definition(), resolved, instanceIdsByTargetId);
            } else if (entity instanceof StepDraughtingModelItemAssociationWithPlaceholder) {
            StepDraughtingModelItemAssociationWithPlaceholder usage = (StepDraughtingModelItemAssociationWithPlaceholder) entity;
                appendRepresentationBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), instanceIdsByTargetId, "DRAUGHTING_MODEL_ITEM_ASSOCIATION_WITH_PLACEHOLDER", usage.id());
                appendAttachedRepresentationRelationshipTargets(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), resolved, instanceIdsByTargetId);
                appendDefinitionBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), usage.definition(), instanceIdsByTargetId);
                appendRelationshipBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), usage.definition(), instanceIdsByTargetId);
                appendSemanticDefinitionTargets(targetsByUsageId, usage.identifiedItem(), usage.definition(), resolved, instanceIdsByTargetId);
            } else if (entity instanceof StepPmiRequirementItemAssociation) {
            StepPmiRequirementItemAssociation usage = (StepPmiRequirementItemAssociation) entity;
                appendRepresentationBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), instanceIdsByTargetId, "PMI_REQUIREMENT_ITEM_ASSOCIATION", usage.id());
                appendAttachedRepresentationRelationshipTargets(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), resolved, instanceIdsByTargetId);
                appendDefinitionBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), usage.definition(), instanceIdsByTargetId);
                appendDefinitionBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), usage.requirement(), instanceIdsByTargetId);
                appendRelationshipBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), usage.definition(), instanceIdsByTargetId);
                appendRelationshipBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), usage.requirement(), instanceIdsByTargetId);
                appendSemanticDefinitionTargets(targetsByUsageId, usage.identifiedItem(), usage.definition(), resolved, instanceIdsByTargetId);
                appendSemanticDefinitionTargets(targetsByUsageId, usage.identifiedItem(), usage.requirement(), resolved, instanceIdsByTargetId);
            } else if (entity instanceof StepMechanicalDesignRequirementItemAssociation) {
            StepMechanicalDesignRequirementItemAssociation usage = (StepMechanicalDesignRequirementItemAssociation) entity;
                appendRepresentationBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), instanceIdsByTargetId, "MECHANICAL_DESIGN_REQUIREMENT_ITEM_ASSOCIATION", usage.id());
                appendAttachedRepresentationRelationshipTargets(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), resolved, instanceIdsByTargetId);
                appendDefinitionBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), usage.definition(), instanceIdsByTargetId);
                appendDefinitionBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), usage.requirement(), instanceIdsByTargetId);
                appendRelationshipBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), usage.definition(), instanceIdsByTargetId);
                appendRelationshipBacklinkTarget(targetsByUsageId, usage.identifiedItem(), usage.usedRepresentation(), usage.requirement(), instanceIdsByTargetId);
                appendSemanticDefinitionTargets(targetsByUsageId, usage.identifiedItem(), usage.definition(), resolved, instanceIdsByTargetId);
                appendSemanticDefinitionTargets(targetsByUsageId, usage.identifiedItem(), usage.requirement(), resolved, instanceIdsByTargetId);
            }
        }
        for (StepEntity entity : resolved.values()) {
            if (entity instanceof StepDraughtingCalloutRelationship) {
            StepDraughtingCalloutRelationship relationship = (StepDraughtingCalloutRelationship) entity;
                propagateCalloutTargets(targetsByUsageId, relationship);
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
                CartesianPoint position = StepPreviewJsonExporter.pointFromAnnotationPoint(textOccurrence.position(), builder);
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
                CartesianPoint position = StepPreviewJsonExporter.pointFromAnnotationPoint(pointOccurrence.item(), builder);
                if (position != null) {
                    List<PmiTargetPayload> targets = targetsByUsageId.getOrDefault(pointOccurrence.id(), List.of());
                    pmi.add(toStandalonePointPmi(pointOccurrence.id(), pointOccurrence.name(), position, targets));
                }
            } else if (entity instanceof StepAnnotationFillAreaOccurrence) {
            StepAnnotationFillAreaOccurrence fillAreaOccurrence = (StepAnnotationFillAreaOccurrence) entity;
                CartesianPoint position = StepPreviewJsonExporter.pointFromAnnotationPoint(fillAreaOccurrence.fillStyleTarget(), builder);
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
                        StepPreviewJsonExporter.pointFromStep(vertexShell.extent().loopVertex().point())
                ));
            } else if (entity instanceof StepGeometricReplica && "POINT_REPLICA".equals(((StepGeometricReplica) entity).entityName())) {
            StepGeometricReplica replica = (StepGeometricReplica) entity;
                CartesianPoint position = StepPreviewJsonExporter.pointFromReplica(replica, builder);
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
                position = StepPreviewJsonExporter.pointFromAnnotationPoint(element, builder);
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
            CartesianPoint position = StepPreviewJsonExporter.pointFromAnnotationPoint(item, builder);
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
        CartesianPoint position = StepPreviewJsonExporter.pointFromAnnotationPoint(measurement.measurementGeometry(), builder);
        if (position != null) {
            String label = measurement.name() + " (" + measurement.geometricType() + ")";
            if (measurement.measuredValue() != 0.0) {
                label += ": " + String.format("%.3f", measurement.measuredValue());
            }
            pmi.add(toStandalonePointPmi(measurement.id(), label, position));
        }
        for (StepEntity pt : measurement.measurementPoints()) {
            CartesianPoint mp = StepPreviewJsonExporter.pointFromAnnotationPoint(pt, builder);
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
        CartesianPoint position = StepPreviewJsonExporter.pointFromAnnotationPoint(tolerance.toleratedShape(), builder);
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
        CartesianPoint position = StepPreviewJsonExporter.pointFromAnnotationPoint(tolerance.tolerancedFeature(), builder);
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
        CartesianPoint position = StepPreviewJsonExporter.pointFromAnnotationPoint(tolerance.tolerancedFeature(), builder);
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
        CartesianPoint position = StepPreviewJsonExporter.pointFromAnnotationPoint(tolerance.tolerancedFeature(), builder);
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
        CartesianPoint position = StepPreviewJsonExporter.pointFromAnnotationPoint(location.relatedShape(), builder);
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
        CartesianPoint position = StepPreviewJsonExporter.pointFromAnnotationPoint(zone.form(), builder);
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
        CartesianPoint position = StepPreviewJsonExporter.pointFromAnnotationPoint(datum.target(), builder);
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
        CartesianPoint position = StepPreviewJsonExporter.pointFromAnnotationPoint(datumTarget.targetShape(), builder);
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

    private static boolean isSupportedPmiUsageCarrier(StepEntity entity) {
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
        CartesianPoint position = StepPreviewJsonExporter.pointFromAnnotationPoint(text.position(), builder);
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
            leader.add(PayloadConversionHelper.toPointPayload(StepPreviewJsonExporter.pointFromStep(vertexLoop.loopVertex().point())));
            return;
        }
        if (content instanceof StepPolyLoop) {
            StepPolyLoop polyLoop = (StepPolyLoop) content;
            for (StepCartesianPoint point : polyLoop.polygon()) {
                leader.add(PayloadConversionHelper.toPointPayload(StepPreviewJsonExporter.pointFromStep(point)));
            }
            return;
        }
        if (content instanceof StepVertexShell) {
            StepVertexShell vertexShell = (StepVertexShell) content;
            leader.add(PayloadConversionHelper.toPointPayload(StepPreviewJsonExporter.pointFromStep(vertexShell.extent().loopVertex().point())));
            return;
        }
        if (content instanceof StepGeometricReplica && "POINT_REPLICA".equals(((StepGeometricReplica) content).entityName())) {
            StepGeometricReplica replica = (StepGeometricReplica) content;
            CartesianPoint point = StepPreviewJsonExporter.pointFromReplica(replica, builder);
            if (point != null) {
                leader.add(PayloadConversionHelper.toPointPayload(point));
            }
            return;
        }
        if (content instanceof StepCartesianPoint) {
            StepCartesianPoint point = (StepCartesianPoint) content;
            leader.add(PayloadConversionHelper.toPointPayload(StepPreviewJsonExporter.pointFromStep(point)));
            return;
        }
        if (content instanceof StepVertexPoint) {
            StepVertexPoint vertexPoint = (StepVertexPoint) content;
            leader.add(PayloadConversionHelper.toPointPayload(StepPreviewJsonExporter.pointFromStep(vertexPoint.point())));
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

    private static void appendPmiTarget(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepEntity target,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        appendPmiTarget(targetsByUsageId, usageId, target, instanceIdsByTargetId, null, null, null, null);
    }

    private static void appendPmiTarget(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepEntity target,
            Map<Integer, List<String>> instanceIdsByTargetId,
            String viaRelationshipType,
            Integer viaRelationshipId
    ) {
        appendPmiTarget(targetsByUsageId, usageId, target, instanceIdsByTargetId, viaRelationshipType, viaRelationshipId, null, null);
    }

    private static void appendPmiTarget(
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

    private static void appendPmiTarget(
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
                PmiTargetHelper.pmiTargetType(target),
                PmiTargetHelper.pmiTargetName(target),
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

    private static void appendRepresentationBacklinkTarget(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepRepresentation representation,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        appendRepresentationBacklinkTarget(targetsByUsageId, identifiedItem, representation, instanceIdsByTargetId, null, null);
    }

    private static void appendRepresentationBacklinkTarget(
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

    private static void appendDefinitionBacklinkTarget(
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
                definitionTypeName(definition),
                definition.id()
        );
    }

    private static void appendExistingRepresentationDefinitionTargets(
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
                    definitionTypeName(definition),
                    definition.id()
            );
            List<PmiTargetPayload> targets = targetsByUsageId.computeIfAbsent(identifiedItem.id(), ignored -> new ArrayList<>());
            if (!targets.contains(payload)) {
                targets.add(payload);
            }
        }
    }

    private static void appendRelationshipBacklinkTarget(
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

    public static void appendSemanticDefinitionTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (!isSupportedPmiUsageCarrier(identifiedItem)) {
            return;
        }
        if (definition instanceof StepAnnotationOccurrenceRelationship) {
            StepAnnotationOccurrenceRelationship relationship = (StepAnnotationOccurrenceRelationship) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            appendRelationshipSemanticTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.entityName(),
                    relationship.id(),
                    relationship.relatingAnnotationOccurrence(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendRelationshipSemanticTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.entityName(),
                    relationship.id(),
                    relationship.relatedAnnotationOccurrence(),
                    resolved,
                    instanceIdsByTargetId
            );
            return;
        }
        if (definition instanceof StepDraughtingCalloutRelationship) {
            StepDraughtingCalloutRelationship relationship = (StepDraughtingCalloutRelationship) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            appendRelationshipSemanticTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    "DRAUGHTING_CALLOUT_RELATIONSHIP",
                    relationship.id(),
                    relationship.relatingCallout(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendRelationshipSemanticTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    "DRAUGHTING_CALLOUT_RELATIONSHIP",
                    relationship.id(),
                    relationship.relatedCallout(),
                    resolved,
                    instanceIdsByTargetId
            );
            return;
        }
        if (definition instanceof StepPropertyDefinitionRelationship) {
            StepPropertyDefinitionRelationship relationship = (StepPropertyDefinitionRelationship) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            appendPropertyRepresentationLinkTargets(
                    targetsByUsageId,
                    identifiedItem,
                    relationship.relatingPropertyDefinition(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendPropertyRepresentationLinkTargets(
                    targetsByUsageId,
                    identifiedItem,
                    relationship.relatedPropertyDefinition(),
                    resolved,
                    instanceIdsByTargetId
            );
        }
        if (definition instanceof StepPropertyDefinition) {
            StepPropertyDefinition propertyDefinition = (StepPropertyDefinition) definition;
            appendPropertyDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    propertyDefinition,
                    resolved,
                    instanceIdsByTargetId
            );
            appendPropertyRepresentationLinkTargets(
                    targetsByUsageId,
                    identifiedItem,
                    propertyDefinition,
                    resolved,
                    instanceIdsByTargetId
            );
            appendNestedDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    propertyDefinition.definition(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepGeneralPropertyRelationship) {
            StepGeneralPropertyRelationship relationship = (StepGeneralPropertyRelationship) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.relatingGeneralProperty(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.relatedGeneralProperty(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepShapeAspectRelationship) {
            StepShapeAspectRelationship relationship = (StepShapeAspectRelationship) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.relatingShapeAspect(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.relatedShapeAspect(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepGeneralProperty) {
            StepGeneralProperty generalProperty = (StepGeneralProperty) definition;
            appendGeneralPropertyRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    generalProperty,
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepShapeAspect) {
            StepShapeAspect shapeAspect = (StepShapeAspect) definition;
            appendShapeAspectRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    shapeAspect,
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepProduct) {
            StepProduct product = (StepProduct) definition;
            appendProductRelationshipTargets(targetsByUsageId, identifiedItem.id(), product, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepProductDefinitionFormation) {
            StepProductDefinitionFormation formation = (StepProductDefinitionFormation) definition;
            appendProductDefinitionFormationRelationshipTargets(targetsByUsageId, identifiedItem.id(), formation, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepProductDefinition) {
            StepProductDefinition productDefinition = (StepProductDefinition) definition;
            appendProductDefinitionRelationshipTargets(targetsByUsageId, identifiedItem.id(), productDefinition, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepProductRelationship) {
            StepProductRelationship relationship = (StepProductRelationship) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, relationship, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.relatingProduct(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.relatedProduct(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepProductDefinitionFormationRelationship) {
            StepProductDefinitionFormationRelationship relationship = (StepProductDefinitionFormationRelationship) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, relationship, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.relatingFormation(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.relatedFormation(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepGroupRelationship) {
            StepGroupRelationship relationship = (StepGroupRelationship) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, relationship, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.relatingGroup(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.relatedGroup(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepDocumentRelationship) {
            StepDocumentRelationship relationship = (StepDocumentRelationship) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, relationship, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.relatingDocument(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.relatedDocument(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepOrganizationRelationship) {
            StepOrganizationRelationship relationship = (StepOrganizationRelationship) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, relationship, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.relatingOrganization(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.relatedOrganization(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepEffectivityRelationship) {
            StepEffectivityRelationship relationship = (StepEffectivityRelationship) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, relationship, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.relatingEffectivity(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.relatedEffectivity(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepProductCategoryRelationship) {
            StepProductCategoryRelationship relationship = (StepProductCategoryRelationship) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, relationship, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.category(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.subCategory(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepGroup) {
            StepGroup group = (StepGroup) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, group, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, group, resolved, instanceIdsByTargetId);
            appendGroupRelationshipTargets(targetsByUsageId, identifiedItem.id(), group, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDocument) {
            StepDocument document = (StepDocument) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, document, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, document, resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    document.kind(),
                    instanceIdsByTargetId
            );
            appendDocumentRelationshipTargets(targetsByUsageId, identifiedItem.id(), document, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDocumentReference) {
            StepDocumentReference reference = (StepDocumentReference) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, reference, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, reference, resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    reference.assignedDocument(),
                    instanceIdsByTargetId
            );
            appendDocumentRelationshipTargets(targetsByUsageId, identifiedItem.id(), reference.assignedDocument(), resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    reference.assignedDocument().kind(),
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepAppliedDocumentReference) {
            StepAppliedDocumentReference reference = (StepAppliedDocumentReference) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, reference, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, reference, resolved, instanceIdsByTargetId);
            appendDocumentRelationshipTargets(targetsByUsageId, identifiedItem.id(), reference.assignedDocument(), resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    reference.assignedDocument(),
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    reference.assignedDocument().kind(),
                    definitionTypeName(reference.assignedDocument().kind()),
                    reference.assignedDocument().kind().id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    reference.assignedDocument().kind(),
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepApprovalAssignment) {
            StepApprovalAssignment assignment = (StepApprovalAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(targetsByUsageId, identifiedItem.id(), assignment.assignedApproval(), relationshipTypeName(assignment), assignment.id(), resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedApproval(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedApproval().status(), instanceIdsByTargetId);
            appendApprovalDecorationTargets(targetsByUsageId, identifiedItem, assignment.assignedApproval(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSecurityClassificationAssignment) {
            StepSecurityClassificationAssignment assignment = (StepSecurityClassificationAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(targetsByUsageId, identifiedItem.id(), assignment.assignedSecurityClassification(), relationshipTypeName(assignment), assignment.id(), resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedSecurityClassification(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedSecurityClassification().securityLevel(), instanceIdsByTargetId);
        } else if (definition instanceof StepContractAssignment) {
            StepContractAssignment assignment = (StepContractAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(targetsByUsageId, identifiedItem.id(), assignment.assignedContract(), relationshipTypeName(assignment), assignment.id(), resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedContract(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedContract().kind(), instanceIdsByTargetId);
        } else if (definition instanceof StepCertificationAssignment) {
            StepCertificationAssignment assignment = (StepCertificationAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(targetsByUsageId, identifiedItem.id(), assignment.assignedCertification(), relationshipTypeName(assignment), assignment.id(), resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedCertification(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedCertification().kind(), instanceIdsByTargetId);
        } else if (definition instanceof StepPersonAndOrganizationAssignment) {
            StepPersonAndOrganizationAssignment assignment = (StepPersonAndOrganizationAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(targetsByUsageId, identifiedItem.id(), assignment.assignedPersonAndOrganization(), relationshipTypeName(assignment), assignment.id(), resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedPersonAndOrganization(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedPersonAndOrganization().person(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedPersonAndOrganization().organization(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.role(), instanceIdsByTargetId);
        } else if (definition instanceof StepOrganizationAssignment) {
            StepOrganizationAssignment assignment = (StepOrganizationAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(targetsByUsageId, identifiedItem.id(), assignment.assignedOrganization(), relationshipTypeName(assignment), assignment.id(), resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedOrganization(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.role(), instanceIdsByTargetId);
        } else if (definition instanceof StepLanguageAssignment) {
            StepLanguageAssignment assignment = (StepLanguageAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(targetsByUsageId, identifiedItem.id(), assignment.assignedLanguage(), relationshipTypeName(assignment), assignment.id(), resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedLanguage(), instanceIdsByTargetId);
        } else if (definition instanceof StepGroupAssignment) {
            StepGroupAssignment assignment = (StepGroupAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(targetsByUsageId, identifiedItem.id(), assignment.assignedGroup(), relationshipTypeName(assignment), assignment.id(), resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedGroup(), instanceIdsByTargetId);
        } else if (definition instanceof StepClassificationAssignment) {
            StepClassificationAssignment assignment = (StepClassificationAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(targetsByUsageId, identifiedItem.id(), assignment.assignedClass(), relationshipTypeName(assignment), assignment.id(), resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedClass(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.role(), instanceIdsByTargetId);
        } else if (definition instanceof StepDateAssignment) {
            StepDateAssignment assignment = (StepDateAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(targetsByUsageId, identifiedItem.id(), assignment.assignedDate(), relationshipTypeName(assignment), assignment.id(), resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedDate(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.role(), instanceIdsByTargetId);
        } else if (definition instanceof StepDateTimeAssignment) {
            StepDateTimeAssignment assignment = (StepDateTimeAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(targetsByUsageId, identifiedItem.id(), assignment.assignedDateAndTime(), relationshipTypeName(assignment), assignment.id(), resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedDateAndTime(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedDateAndTime().dateComponent(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedDateAndTime().timeComponent(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.assignedDateAndTime().timeComponent().zone(), instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, assignment.role(), instanceIdsByTargetId);
        } else if (definition instanceof StepIdentificationAssignment) {
            StepIdentificationAssignment assignment = (StepIdentificationAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, assignment.role(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepExternalIdentificationAssignment) {
            StepExternalIdentificationAssignment assignment = (StepExternalIdentificationAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, assignment.role(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, assignment.source(), resolved, instanceIdsByTargetId);
            appendExternalSourceRelationshipTargets(targetsByUsageId, identifiedItem, assignment.source(), resolved, instanceIdsByTargetId);
            appendExternallyDefinedItemTargets(targetsByUsageId, identifiedItem, assignment.source(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepOrganization) {
            StepOrganization organization = (StepOrganization) definition;
            appendOrganizationRelationshipTargets(targetsByUsageId, identifiedItem.id(), organization, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepEffectivity) {
            StepEffectivity effectivity = (StepEffectivity) definition;
            appendProductDefinitionEffectivityTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    effectivity,
                    resolved,
                    instanceIdsByTargetId
            );
            appendEffectivityRelationshipTargets(targetsByUsageId, identifiedItem.id(), effectivity, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepProductCategory) {
            StepProductCategory category = (StepProductCategory) definition;
            appendProductCategoryRelationshipTargets(targetsByUsageId, identifiedItem.id(), category, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepExternalSource) {
            StepExternalSource source = (StepExternalSource) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, source, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, source, resolved, instanceIdsByTargetId);
            appendExternallyDefinedItemTargets(
                    targetsByUsageId,
                    identifiedItem,
                    source,
                    resolved,
                    instanceIdsByTargetId
            );
            appendExternalSourceRelationshipTargets(targetsByUsageId, identifiedItem, source, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepExternallyDefinedItem) {
            StepExternallyDefinedItem item = (StepExternallyDefinedItem) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, item, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, item.source(), instanceIdsByTargetId);
            appendExternalSourceRelationshipTargets(targetsByUsageId, identifiedItem, item.source(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDocumentUsageConstraint) {
            StepDocumentUsageConstraint documentUsageConstraint = (StepDocumentUsageConstraint) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, documentUsageConstraint, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, documentUsageConstraint, resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    documentUsageConstraint.source(),
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    documentUsageConstraint.source().kind(),
                    instanceIdsByTargetId
            );
            appendDocumentRelationshipTargets(targetsByUsageId, identifiedItem.id(), documentUsageConstraint.source(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepRepresentation) {
            StepRepresentation representation = (StepRepresentation) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, representation, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    representation,
                    resolved,
                    instanceIdsByTargetId
            );
            if (representation.context() != null) {
                appendNestedDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        representation.context(),
                        resolved,
                        instanceIdsByTargetId
                );
            }
            for (StepEntity item : representation.items()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
            }
        } else if (definition instanceof StepProductDefinitionShape) {
            StepProductDefinitionShape productDefinitionShape = (StepProductDefinitionShape) definition;
            appendProductDefinitionShapeRepresentationTargets(
                    targetsByUsageId,
                    identifiedItem,
                    productDefinitionShape,
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepProductDefinition) {
            StepProductDefinition productDefinition = (StepProductDefinition) definition;
            appendProductDefinitionRepresentationTargets(
                    targetsByUsageId,
                    identifiedItem,
                    productDefinition,
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepNextAssemblyUsageOccurrence) {
            StepNextAssemblyUsageOccurrence occurrence = (StepNextAssemblyUsageOccurrence) definition;
            appendOccurrenceRepresentationTargets(
                    targetsByUsageId,
                    identifiedItem,
                    occurrence,
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepShapeAspectOccurrence) {
            StepShapeAspectOccurrence occurrence = (StepShapeAspectOccurrence) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, occurrence, instanceIdsByTargetId);
            appendNestedDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    occurrence.definition(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepProductDefinitionRelationshipRelationship) {
            StepProductDefinitionRelationshipRelationship relationship = (StepProductDefinitionRelationshipRelationship) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.relating(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    relationship.related(),
                    relationshipTypeName(relationship),
                    relationship.id(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepApprovalPersonOrganization) {
            StepApprovalPersonOrganization assignment = (StepApprovalPersonOrganization) definition;
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.personOrganization(),
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.personOrganization().person(),
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.personOrganization().organization(),
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.authorizedApproval(),
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.authorizedApproval().status(),
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    assignment.role(),
                    definitionTypeName(assignment.role()),
                    assignment.role().id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.role(),
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepApprovalDateTime) {
            StepApprovalDateTime assignment = (StepApprovalDateTime) definition;
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.dateTime(),
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.dateTime().dateComponent(),
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.dateTime().timeComponent(),
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.dateTime().timeComponent().zone(),
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.datedApproval(),
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.datedApproval().status(),
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepCalendarDate) {
            StepCalendarDate calendarDate = (StepCalendarDate) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, calendarDate, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, calendarDate, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepNameAttribute) {
            StepNameAttribute attribute = (StepNameAttribute) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, attribute, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, attribute, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, attribute.namedItem(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDescriptionAttribute) {
            StepDescriptionAttribute attribute = (StepDescriptionAttribute) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, attribute, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, attribute, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, attribute.describedItem(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepIdAttribute) {
            StepIdAttribute attribute = (StepIdAttribute) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, attribute, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, attribute, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, attribute.identifiedItem(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepNameAssignment) {
            StepNameAssignment assignment = (StepNameAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepAppliedNameAssignment) {
            StepAppliedNameAssignment assignment = (StepAppliedNameAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            for (StepEntity item : assignment.items()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
            }
        } else if (definition instanceof StepDateAndTime) {
            StepDateAndTime dateAndTime = (StepDateAndTime) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, dateAndTime, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, dateAndTime, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, dateAndTime.dateComponent(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, dateAndTime.timeComponent(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepLocalTime) {
            StepLocalTime localTime = (StepLocalTime) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, localTime, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, localTime, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, localTime.zone(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCoordinatedUniversalTimeOffset) {
            StepCoordinatedUniversalTimeOffset zone = (StepCoordinatedUniversalTimeOffset) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, zone, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, zone, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepApprovalStatus) {
            StepApprovalStatus status = (StepApprovalStatus) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, status, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, status, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSecurityClassificationLevel) {
            StepSecurityClassificationLevel level = (StepSecurityClassificationLevel) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, level, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, level, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepContractType) {
            StepContractType kind = (StepContractType) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, kind, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, kind, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCertificationType) {
            StepCertificationType kind = (StepCertificationType) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, kind, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, kind, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepApprovalRole) {
            StepApprovalRole role = (StepApprovalRole) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, role, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, role, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepOrganizationRole) {
            StepOrganizationRole role = (StepOrganizationRole) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, role, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, role, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPersonAndOrganizationRole) {
            StepPersonAndOrganizationRole role = (StepPersonAndOrganizationRole) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, role, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, role, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepClassificationRole) {
            StepClassificationRole role = (StepClassificationRole) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, role, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, role, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDateRole) {
            StepDateRole role = (StepDateRole) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, role, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, role, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDateTimeRole) {
            StepDateTimeRole role = (StepDateTimeRole) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, role, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, role, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepIdentificationRole) {
            StepIdentificationRole role = (StepIdentificationRole) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, role, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, role, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDocumentType) {
            StepDocumentType kind = (StepDocumentType) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, kind, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, kind, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepApproval) {
            StepApproval approval = (StepApproval) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, approval, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, approval, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, approval.status(), resolved, instanceIdsByTargetId);
            appendApprovalDecorationTargets(targetsByUsageId, identifiedItem, approval, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSecurityClassification) {
            StepSecurityClassification classification = (StepSecurityClassification) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, classification, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, classification, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, classification.securityLevel(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepContract) {
            StepContract contract = (StepContract) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, contract, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, contract, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, contract.kind(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCertification) {
            StepCertification certification = (StepCertification) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, certification, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, certification, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, certification.kind(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPerson) {
            StepPerson person = (StepPerson) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, person, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, person, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPersonAndOrganization) {
            StepPersonAndOrganization personAndOrganization = (StepPersonAndOrganization) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, personAndOrganization, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, personAndOrganization, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, personAndOrganization.person(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, personAndOrganization.organization(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepLanguage) {
            StepLanguage language = (StepLanguage) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, language, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, language, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepAppliedClassificationAssignment) {
            StepAppliedClassificationAssignment assignment = (StepAppliedClassificationAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    assignment.role(),
                    definitionTypeName(assignment.role()),
                    assignment.role().id(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepAppliedDateAssignment) {
            StepAppliedDateAssignment assignment = (StepAppliedDateAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedDate(),
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    assignment.role(),
                    definitionTypeName(assignment.role()),
                    assignment.role().id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.role(),
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepAppliedDateTimeAssignment) {
            StepAppliedDateTimeAssignment assignment = (StepAppliedDateTimeAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedDateAndTime(),
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedDateAndTime().timeComponent(),
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedDateAndTime().timeComponent().zone(),
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    assignment.role(),
                    definitionTypeName(assignment.role()),
                    assignment.role().id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.role(),
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepAppliedApprovalAssignment) {
            StepAppliedApprovalAssignment assignment = (StepAppliedApprovalAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedApproval(),
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    assignment.assignedApproval().status(),
                    definitionTypeName(assignment.assignedApproval().status()),
                    assignment.assignedApproval().status().id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedApproval().status(),
                    instanceIdsByTargetId
            );
            appendApprovalDecorationTargets(targetsByUsageId, identifiedItem, assignment.assignedApproval(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepAppliedSecurityClassificationAssignment) {
            StepAppliedSecurityClassificationAssignment assignment = (StepAppliedSecurityClassificationAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedSecurityClassification(),
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    assignment.assignedSecurityClassification().securityLevel(),
                    definitionTypeName(assignment.assignedSecurityClassification().securityLevel()),
                    assignment.assignedSecurityClassification().securityLevel().id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedSecurityClassification().securityLevel(),
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepAppliedContractAssignment) {
            StepAppliedContractAssignment assignment = (StepAppliedContractAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedContract(),
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    assignment.assignedContract().kind(),
                    definitionTypeName(assignment.assignedContract().kind()),
                    assignment.assignedContract().kind().id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedContract().kind(),
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepAppliedCertificationAssignment) {
            StepAppliedCertificationAssignment assignment = (StepAppliedCertificationAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedCertification(),
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    assignment.assignedCertification().kind(),
                    definitionTypeName(assignment.assignedCertification().kind()),
                    assignment.assignedCertification().kind().id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedCertification().kind(),
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepAppliedPersonAndOrganizationAssignment) {
            StepAppliedPersonAndOrganizationAssignment assignment = (StepAppliedPersonAndOrganizationAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedPersonAndOrganization(),
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedPersonAndOrganization().person(),
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedPersonAndOrganization().organization(),
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    assignment.role(),
                    definitionTypeName(assignment.role()),
                    assignment.role().id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.role(),
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepAppliedOrganizationAssignment) {
            StepAppliedOrganizationAssignment assignment = (StepAppliedOrganizationAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedOrganization(),
                    instanceIdsByTargetId
            );
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    assignment.role(),
                    definitionTypeName(assignment.role()),
                    assignment.role().id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.role(),
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepAppliedLanguageAssignment) {
            StepAppliedLanguageAssignment assignment = (StepAppliedLanguageAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendDefinitionRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem.id(),
                    assignment.assignedLanguage(),
                    definitionTypeName(assignment.assignedLanguage()),
                    assignment.assignedLanguage().id(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedLanguage(),
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepAppliedGroupAssignment) {
            StepAppliedGroupAssignment assignment = (StepAppliedGroupAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.assignedGroup(),
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepAppliedIdentificationAssignment) {
            StepAppliedIdentificationAssignment assignment = (StepAppliedIdentificationAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, assignment.role(), resolved, instanceIdsByTargetId);
            for (StepEntity item : assignment.items()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
            }
        } else if (definition instanceof StepAppliedExternalIdentificationAssignment) {
            StepAppliedExternalIdentificationAssignment assignment = (StepAppliedExternalIdentificationAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, assignment, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, assignment.role(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, assignment.source(), resolved, instanceIdsByTargetId);
            appendExternalSourceRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.source(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendExternallyDefinedItemTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assignment.source(),
                    resolved,
                    instanceIdsByTargetId
            );
            for (StepEntity item : assignment.items()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
            }
        } else if (definition instanceof StepAnnotationCurveOccurrence) {
            StepAnnotationCurveOccurrence occurrence = (StepAnnotationCurveOccurrence) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, occurrence, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, occurrence.item(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepAnnotationFillArea) {
            StepAnnotationFillArea fillArea = (StepAnnotationFillArea) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, fillArea, instanceIdsByTargetId);
            for (StepEntity boundary : fillArea.boundaries()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, boundary, resolved, instanceIdsByTargetId);
            }
        } else if (definition instanceof StepAnnotationFillAreaOccurrence) {
            StepAnnotationFillAreaOccurrence occurrence = (StepAnnotationFillAreaOccurrence) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, occurrence, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, occurrence.item(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, occurrence.fillStyleTarget(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepAnnotationPlaceholderOccurrence) {
            StepAnnotationPlaceholderOccurrence occurrence = (StepAnnotationPlaceholderOccurrence) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, occurrence, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, occurrence.item(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepAnnotationPointOccurrence) {
            StepAnnotationPointOccurrence occurrence = (StepAnnotationPointOccurrence) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, occurrence, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, occurrence.item(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepAnnotationSymbolOccurrence) {
            StepAnnotationSymbolOccurrence occurrence = (StepAnnotationSymbolOccurrence) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, occurrence, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, occurrence.item(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepAnnotationSubfigureOccurrence) {
            StepAnnotationSubfigureOccurrence occurrence = (StepAnnotationSubfigureOccurrence) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, occurrence, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, occurrence.item(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepAnnotationTextOccurrence) {
            StepAnnotationTextOccurrence occurrence = (StepAnnotationTextOccurrence) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, occurrence, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, occurrence.position(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDraughtingAnnotationOccurrence) {
            StepDraughtingAnnotationOccurrence occurrence = (StepDraughtingAnnotationOccurrence) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, occurrence, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, occurrence.item(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepTerminatorSymbol) {
            StepTerminatorSymbol symbol = (StepTerminatorSymbol) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, symbol, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, symbol.item(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, symbol.annotatedCurve(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPresentationStyleAssignment) {
            StepPresentationStyleAssignment assignment = (StepPresentationStyleAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            for (StepEntity style : assignment.styles()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style, resolved, instanceIdsByTargetId);
            }
        } else if (definition instanceof StepSurfaceStyleUsage) {
            StepSurfaceStyleUsage usage = (StepSurfaceStyleUsage) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, usage, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, usage.style(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSurfaceSideStyle) {
            StepSurfaceSideStyle style = (StepSurfaceSideStyle) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            for (StepEntity component : style.styles()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, component, resolved, instanceIdsByTargetId);
            }
        } else if (definition instanceof StepSurfaceStyleFillArea) {
            StepSurfaceStyleFillArea style = (StepSurfaceStyleFillArea) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.fillStyle(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepFillAreaStyle) {
            StepFillAreaStyle style = (StepFillAreaStyle) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            for (StepFillAreaStyleColour component : style.styles()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, component, resolved, instanceIdsByTargetId);
            }
        } else if (definition instanceof StepFillAreaStyleColour) {
            StepFillAreaStyleColour style = (StepFillAreaStyleColour) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.colour(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCurveStyle) {
            StepCurveStyle style = (StepCurveStyle) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.curveFont(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.colour(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSurfaceStyleBoundary) {
            StepSurfaceStyleBoundary style = (StepSurfaceStyleBoundary) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.style(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSurfaceStyleParameterLine) {
            StepSurfaceStyleParameterLine style = (StepSurfaceStyleParameterLine) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.style(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSurfaceStyleControlGrid) {
            StepSurfaceStyleControlGrid style = (StepSurfaceStyleControlGrid) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.style(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSurfaceStyleSegmentationCurve) {
            StepSurfaceStyleSegmentationCurve style = (StepSurfaceStyleSegmentationCurve) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.style(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSurfaceStyleSilhouette) {
            StepSurfaceStyleSilhouette style = (StepSurfaceStyleSilhouette) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.style(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCharacterGlyphStyleStroke) {
            StepCharacterGlyphStyleStroke style = (StepCharacterGlyphStyleStroke) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.strokeStyle(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCharacterGlyphStyleOutline) {
            StepCharacterGlyphStyleOutline style = (StepCharacterGlyphStyleOutline) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.outlineStyle(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCharacterGlyphStyleOutlineWithCharacteristics) {
            StepCharacterGlyphStyleOutlineWithCharacteristics style = (StepCharacterGlyphStyleOutlineWithCharacteristics) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.outlineStyle(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.characteristics(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepTextStyle) {
            StepTextStyle style = (StepTextStyle) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.characterAppearance(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepTextStyleWithSpacing) {
            StepTextStyleWithSpacing style = (StepTextStyleWithSpacing) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.characterAppearance(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepTextStyleWithBoxCharacteristics) {
            StepTextStyleWithBoxCharacteristics style = (StepTextStyleWithBoxCharacteristics) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.characterAppearance(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepTextStyleWithJustification) {
            StepTextStyleWithJustification style = (StepTextStyleWithJustification) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.characterAppearance(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepTextStyleWithMirror) {
            StepTextStyleWithMirror style = (StepTextStyleWithMirror) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.characterAppearance(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.mirrorPlacement(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepTextStyleForDefinedFont) {
            StepTextStyleForDefinedFont style = (StepTextStyleForDefinedFont) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.textColour(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPointStyle) {
            StepPointStyle style = (StepPointStyle) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.marker(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.colour(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSymbolColour) {
            StepSymbolColour style = (StepSymbolColour) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.colour(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSymbolStyle) {
            StepSymbolStyle style = (StepSymbolStyle) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.styleOfSymbol(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSurfaceStyleTransparent) {
            StepSurfaceStyleTransparent style = (StepSurfaceStyleTransparent) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
        } else if (definition instanceof StepSurfaceStyleReflectanceAmbient) {
            StepSurfaceStyleReflectanceAmbient style = (StepSurfaceStyleReflectanceAmbient) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
        } else if (definition instanceof StepSurfaceStyleReflectanceAmbientDiffuse) {
            StepSurfaceStyleReflectanceAmbientDiffuse style = (StepSurfaceStyleReflectanceAmbientDiffuse) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
        } else if (definition instanceof StepSurfaceStyleReflectanceAmbientDiffuseSpecular) {
            StepSurfaceStyleReflectanceAmbientDiffuseSpecular style = (StepSurfaceStyleReflectanceAmbientDiffuseSpecular) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style.specularColour(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPreDefinedSurfaceSideStyle) {
            StepPreDefinedSurfaceSideStyle style = (StepPreDefinedSurfaceSideStyle) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, style, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, style, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPreDefinedColour) {
            StepPreDefinedColour colour = (StepPreDefinedColour) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, colour, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, colour, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDraughtingPreDefinedColour) {
            StepDraughtingPreDefinedColour colour = (StepDraughtingPreDefinedColour) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, colour, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, colour, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepColourRgb) {
            StepColourRgb colour = (StepColourRgb) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, colour, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, colour, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepColourSpecification) {
            StepColourSpecification colour = (StepColourSpecification) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, colour, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, colour, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepColour) {
            StepColour colour = (StepColour) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, colour, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, colour, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPreDefinedCurveFont) {
            StepPreDefinedCurveFont font = (StepPreDefinedCurveFont) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, font, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, font, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDraughtingPreDefinedCurveFont) {
            StepDraughtingPreDefinedCurveFont font = (StepDraughtingPreDefinedCurveFont) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, font, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, font, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPreDefinedTextFont) {
            StepPreDefinedTextFont font = (StepPreDefinedTextFont) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, font, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, font, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDraughtingPreDefinedTextFont) {
            StepDraughtingPreDefinedTextFont font = (StepDraughtingPreDefinedTextFont) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, font, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, font, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPreDefinedTerminatorSymbol) {
            StepPreDefinedTerminatorSymbol symbol = (StepPreDefinedTerminatorSymbol) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, symbol, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, symbol, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPreDefinedSymbol) {
            StepPreDefinedSymbol symbol = (StepPreDefinedSymbol) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, symbol, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, symbol, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPreDefinedDimensionSymbol) {
            StepPreDefinedDimensionSymbol symbol = (StepPreDefinedDimensionSymbol) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, symbol, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, symbol, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPreDefinedGeometricalToleranceSymbol) {
            StepPreDefinedGeometricalToleranceSymbol symbol = (StepPreDefinedGeometricalToleranceSymbol) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, symbol, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, symbol, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPreDefinedItem) {
            StepPreDefinedItem item = (StepPreDefinedItem) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, item, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepAnnotationPlane) {
            StepAnnotationPlane plane = (StepAnnotationPlane) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, plane, instanceIdsByTargetId);
            for (StepPresentationStyleAssignment style : plane.styles()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style, resolved, instanceIdsByTargetId);
            }
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, plane.item(), resolved, instanceIdsByTargetId);
            for (StepEntity element : plane.elements()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, element, resolved, instanceIdsByTargetId);
            }
        } else if (definition instanceof StepDraughtingCallout) {
            StepDraughtingCallout callout = (StepDraughtingCallout) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, callout, instanceIdsByTargetId);
            for (StepEntity content : callout.contents()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, content, resolved, instanceIdsByTargetId);
            }
        } else if (definition instanceof StepPresentationLayerAssignment) {
            StepPresentationLayerAssignment assignment = (StepPresentationLayerAssignment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assignment, instanceIdsByTargetId);
            for (StepEntity item : assignment.assignedItems()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
            }
        } else if (definition instanceof StepStyledItem) {
            StepStyledItem styledItem = (StepStyledItem) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, styledItem, instanceIdsByTargetId);
            for (StepPresentationStyleAssignment style : styledItem.styles()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style, resolved, instanceIdsByTargetId);
            }
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, styledItem.item(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepOverRidingStyledItem) {
            StepOverRidingStyledItem styledItem = (StepOverRidingStyledItem) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, styledItem, instanceIdsByTargetId);
            for (StepPresentationStyleAssignment style : styledItem.styles()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, style, resolved, instanceIdsByTargetId);
            }
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, styledItem.item(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, styledItem.overRiddenStyle(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepRepresentationMap) {
            StepRepresentationMap representationMap = (StepRepresentationMap) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, representationMap, instanceIdsByTargetId);
            appendRepresentationMapDefinitionTargets(targetsByUsageId, identifiedItem, representationMap, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, representationMap, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSymbolRepresentationMap) {
            StepSymbolRepresentationMap representationMap = (StepSymbolRepresentationMap) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, representationMap, instanceIdsByTargetId);
            appendRepresentationMapDefinitionTargets(targetsByUsageId, identifiedItem, representationMap, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, representationMap, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepMappedItem) {
            StepMappedItem mappedItem = (StepMappedItem) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, mappedItem, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, mappedItem.mappingSource(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, mappedItem.mappingTarget(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, mappedItem, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepGeometricReplica) {
            StepGeometricReplica replica = (StepGeometricReplica) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, replica, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, replica.parent(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, replica.transformation(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, replica, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepItemDefinedTransformation) {
            StepItemDefinedTransformation transformation = (StepItemDefinedTransformation) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, transformation, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, transformation.transformItem1(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, transformation.transformItem2(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, transformation, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCartesianTransformationOperator) {
            StepCartesianTransformationOperator transformation = (StepCartesianTransformationOperator) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, transformation, instanceIdsByTargetId);
            if (transformation.axis1() != null) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, transformation.axis1(), resolved, instanceIdsByTargetId);
            }
            if (transformation.axis2() != null) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, transformation.axis2(), resolved, instanceIdsByTargetId);
            }
            if (transformation.axis3() != null) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, transformation.axis3(), resolved, instanceIdsByTargetId);
            }
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, transformation.localOrigin(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, transformation, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepAxis1Placement) {
            StepAxis1Placement placement = (StepAxis1Placement) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, placement, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, placement.location(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, placement.axis(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepAxis2Placement2D) {
            StepAxis2Placement2D placement = (StepAxis2Placement2D) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, placement, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, placement.location(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, placement.refDirection(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCartesianPoint) {
            StepCartesianPoint point = (StepCartesianPoint) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, point, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, point, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPoint) {
            StepPoint point = (StepPoint) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, point, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, point, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDirection) {
            StepDirection direction = (StepDirection) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, direction, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, direction, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepVector) {
            StepVector vector = (StepVector) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, vector, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, vector.orientation(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, vector, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepAxis2Placement3D) {
            StepAxis2Placement3D placement = (StepAxis2Placement3D) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, placement, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, placement.location(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, placement.axis(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, placement.refDirection(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, placement, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPlane) {
            StepPlane plane = (StepPlane) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, plane, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, plane.position(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, plane, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepGeometricSet) {
            StepGeometricSet set = (StepGeometricSet) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, set, instanceIdsByTargetId);
            for (StepEntity element : set.elements()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, element, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, set, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepGeometricCurveSet) {
            StepGeometricCurveSet set = (StepGeometricCurveSet) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, set, instanceIdsByTargetId);
            for (StepEntity element : set.elements()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, element, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, set, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPointSet) {
            StepPointSet set = (StepPointSet) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, set, instanceIdsByTargetId);
            for (StepEntity point : set.points()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, point, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, set, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPath) {
            StepPath path = (StepPath) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, path, instanceIdsByTargetId);
            for (StepOrientedEdge edge : path.edges()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, edge, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, path, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepOpenPath) {
            StepOpenPath path = (StepOpenPath) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, path, instanceIdsByTargetId);
            for (StepOrientedEdge edge : path.edges()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, edge, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, path, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepOrientedPath) {
            StepOrientedPath path = (StepOrientedPath) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, path, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, path.pathElement(), resolved, instanceIdsByTargetId);
            for (StepOrientedEdge edge : path.edges()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, edge, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, path, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSubpath) {
            StepSubpath path = (StepSubpath) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, path, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, path.parentPath(), resolved, instanceIdsByTargetId);
            for (StepOrientedEdge edge : path.edges()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, edge, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, path, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepEdgeLoop) {
            StepEdgeLoop loop = (StepEdgeLoop) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, loop, instanceIdsByTargetId);
            for (StepOrientedEdge edge : loop.edges()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, edge, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, loop, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPolyLoop) {
            StepPolyLoop loop = (StepPolyLoop) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, loop, instanceIdsByTargetId);
            for (StepCartesianPoint point : loop.polygon()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, point, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, loop, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepConnectedEdgeSet) {
            StepConnectedEdgeSet set = (StepConnectedEdgeSet) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, set, instanceIdsByTargetId);
            for (StepEntity edge : set.edges()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, edge, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, set, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepEdgeBasedWireframeModel) {
            StepEdgeBasedWireframeModel model = (StepEdgeBasedWireframeModel) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, model, instanceIdsByTargetId);
            for (StepConnectedEdgeSet boundary : model.boundaries()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, boundary, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, model, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepShellBasedWireframeModel) {
            StepShellBasedWireframeModel model = (StepShellBasedWireframeModel) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, model, instanceIdsByTargetId);
            for (StepEntity boundary : model.boundaries()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, boundary, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, model, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepWireShell) {
            StepWireShell shell = (StepWireShell) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, shell, instanceIdsByTargetId);
            for (StepLoop loop : shell.loops()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, loop, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, shell, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepVertexShell) {
            StepVertexShell shell = (StepVertexShell) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, shell, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, shell.extent(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, shell, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepVertexLoop) {
            StepVertexLoop loop = (StepVertexLoop) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, loop, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, loop.loopVertex(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, loop, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepOrientedEdge) {
            StepOrientedEdge edge = (StepOrientedEdge) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, edge, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, edge.edgeElement(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, edge, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepEdgeCurve) {
            StepEdgeCurve edge = (StepEdgeCurve) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, edge, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, edge.start(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, edge.end(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, edge.edgeGeometry(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, edge, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepVertexPoint) {
            StepVertexPoint vertex = (StepVertexPoint) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, vertex, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, vertex.point(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, vertex, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepAdvancedFace) {
            StepAdvancedFace face = (StepAdvancedFace) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, face, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, face.faceGeometry(), resolved, instanceIdsByTargetId);
            for (StepFaceBound bound : face.bounds()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, bound, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, face, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepFaceSurface) {
            StepFaceSurface face = (StepFaceSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, face, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, face.faceGeometry(), resolved, instanceIdsByTargetId);
            for (StepFaceBound bound : face.bounds()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, bound, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, face, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepOrientedFace) {
            StepOrientedFace face = (StepOrientedFace) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, face, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, face.faceElement(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, face, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepConnectedFaceSet) {
            StepConnectedFaceSet faceSet = (StepConnectedFaceSet) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, faceSet, instanceIdsByTargetId);
            for (StepFaceEntity face : faceSet.faces()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, face, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, faceSet, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepConnectedFaceSubSet) {
            StepConnectedFaceSubSet faceSet = (StepConnectedFaceSubSet) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, faceSet, instanceIdsByTargetId);
            for (StepFaceEntity face : faceSet.faces()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, face, resolved, instanceIdsByTargetId);
            }
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, faceSet.parentFaceSet(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, faceSet, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepOpenShell) {
            StepOpenShell shell = (StepOpenShell) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, shell, instanceIdsByTargetId);
            for (StepFaceEntity face : shell.faces()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, face, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, shell, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSurfacedOpenShell) {
            StepSurfacedOpenShell shell = (StepSurfacedOpenShell) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, shell, instanceIdsByTargetId);
            for (StepFaceEntity face : shell.faces()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, face, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, shell, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepClosedShell) {
            StepClosedShell shell = (StepClosedShell) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, shell, instanceIdsByTargetId);
            for (StepFaceEntity face : shell.faces()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, face, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, shell, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepOrientedOpenShell) {
            StepOrientedOpenShell shell = (StepOrientedOpenShell) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, shell, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, shell.openShellElement(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, shell, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepOrientedClosedShell) {
            StepOrientedClosedShell shell = (StepOrientedClosedShell) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, shell, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, shell.closedShellElement(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, shell, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepFaceBasedSurfaceModel) {
            StepFaceBasedSurfaceModel model = (StepFaceBasedSurfaceModel) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, model, instanceIdsByTargetId);
            for (StepEntity faceSet : model.faceSets()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, faceSet, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, model, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepShellBasedSurfaceModel) {
            StepShellBasedSurfaceModel model = (StepShellBasedSurfaceModel) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, model, instanceIdsByTargetId);
            for (StepEntity shell : model.shells()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, shell, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, model, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepManifoldSolidBrep) {
            StepManifoldSolidBrep solid = (StepManifoldSolidBrep) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, solid, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, solid.outer(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, solid, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepBrepWithVoids) {
            StepBrepWithVoids solid = (StepBrepWithVoids) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, solid, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, solid.outer(), resolved, instanceIdsByTargetId);
            for (StepEntity voidShell : solid.voids()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, voidShell, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, solid, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSweptAreaSolid) {
            StepSweptAreaSolid solid = (StepSweptAreaSolid) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, solid, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, solid.sweptArea(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, solid.position(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, solid.sweepReference(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, solid, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSweptDiskSolid) {
            StepSweptDiskSolid solid = (StepSweptDiskSolid) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, solid, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, solid.sweptCurve(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, solid, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepComplexClippingResult) {
            StepComplexClippingResult solid = (StepComplexClippingResult) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, solid, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, solid.firstOperand(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, solid.secondOperand(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, solid, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSolidReplica) {
            StepSolidReplica solid = (StepSolidReplica) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, solid, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, solid.parentSolid(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, solid.transformation(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, solid, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepHalfSpaceSolid) {
            StepHalfSpaceSolid solid = (StepHalfSpaceSolid) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, solid, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, solid.baseSurface(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, solid.enclosure(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, solid, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCsgSolid) {
            StepCsgSolid solid = (StepCsgSolid) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, solid, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, solid.treeRootExpression(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, solid, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCsgPrimitive) {
            StepCsgPrimitive primitive = (StepCsgPrimitive) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, primitive, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, primitive.position(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, primitive, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepProfileDef) {
            StepProfileDef profile = (StepProfileDef) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, profile, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, profile.position(), resolved, instanceIdsByTargetId);
            for (StepEntity curve : profile.curves()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, profile, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepConicCurve) {
            StepConicCurve curve = (StepConicCurve) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, curve.position(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepBSplineCurve) {
            StepBSplineCurve curve = (StepBSplineCurve) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendSplineCurveControlPointTargets(targetsByUsageId, identifiedItem, curve.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepBSplineCurveWithKnots) {
            StepBSplineCurveWithKnots curve = (StepBSplineCurveWithKnots) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendSplineCurveControlPointTargets(targetsByUsageId, identifiedItem, curve.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepRationalBSplineCurve) {
            StepRationalBSplineCurve curve = (StepRationalBSplineCurve) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendSplineCurveControlPointTargets(targetsByUsageId, identifiedItem, curve.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepBezierCurve) {
            StepBezierCurve curve = (StepBezierCurve) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendSplineCurveControlPointTargets(targetsByUsageId, identifiedItem, curve.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepUniformCurve) {
            StepUniformCurve curve = (StepUniformCurve) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendSplineCurveControlPointTargets(targetsByUsageId, identifiedItem, curve.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepQuasiUniformCurve) {
            StepQuasiUniformCurve curve = (StepQuasiUniformCurve) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendSplineCurveControlPointTargets(targetsByUsageId, identifiedItem, curve.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPiecewiseBezierCurve) {
            StepPiecewiseBezierCurve curve = (StepPiecewiseBezierCurve) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendSplineCurveControlPointTargets(targetsByUsageId, identifiedItem, curve.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepLine) {
            StepLine line = (StepLine) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, line, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, line.point(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, line.vector(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, line, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCircle) {
            StepCircle circle = (StepCircle) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, circle, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, circle.position(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, circle, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepEllipse) {
            StepEllipse ellipse = (StepEllipse) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, ellipse, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, ellipse.position(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, ellipse, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCurve) {
            StepCurve curve = (StepCurve) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPolyline) {
            StepPolyline polyline = (StepPolyline) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, polyline, instanceIdsByTargetId);
            for (StepCartesianPoint point : polyline.points()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, point, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, polyline, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepTrimmedCurve) {
            StepTrimmedCurve curve = (StepTrimmedCurve) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, curve.basisCurve(), resolved, instanceIdsByTargetId);
            for (StepValue trim : curve.trim1()) {
                if (trim instanceof StepValue.ReferenceValue) {
                    StepValue.ReferenceValue ref = (StepValue.ReferenceValue) trim;
                    appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, resolved.get(ref.id()), resolved, instanceIdsByTargetId);
                }
            }
            for (StepValue trim : curve.trim2()) {
                if (trim instanceof StepValue.ReferenceValue) {
                    StepValue.ReferenceValue ref = (StepValue.ReferenceValue) trim;
                    appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, resolved.get(ref.id()), resolved, instanceIdsByTargetId);
                }
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepOffsetCurve2D) {
            StepOffsetCurve2D curve = (StepOffsetCurve2D) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, curve.basisCurve(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepOffsetCurve3D) {
            StepOffsetCurve3D curve = (StepOffsetCurve3D) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, curve.basisCurve(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, curve.refDirection(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPcurve) {
            StepPcurve curve = (StepPcurve) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, curve.basisSurface(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, curve.referenceToCurve(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDegeneratePcurve) {
            StepDegeneratePcurve curve = (StepDegeneratePcurve) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, curve.basisSurface(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, curve.referenceToCurve(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSurfaceCurve) {
            StepSurfaceCurve curve = (StepSurfaceCurve) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, curve.curve3d(), resolved, instanceIdsByTargetId);
            for (StepEntity associated : curve.associatedGeometry()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, associated, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSeamCurve) {
            StepSeamCurve curve = (StepSeamCurve) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, curve.curve3d(), resolved, instanceIdsByTargetId);
            for (StepEntity associated : curve.associatedGeometry()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, associated, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCompositeCurve) {
            StepCompositeCurve curve = (StepCompositeCurve) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            for (StepCompositeCurveSegment segment : curve.segments()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, segment, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCompositeCurveOnSurface) {
            StepCompositeCurveOnSurface curve = (StepCompositeCurveOnSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            for (StepCompositeCurveSegment segment : curve.segments()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, segment, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCompositeCurveSegment) {
            StepCompositeCurveSegment segment = (StepCompositeCurveSegment) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, segment, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, segment.parentCurve(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, segment, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCylindricalSurface) {
            StepCylindricalSurface surface = (StepCylindricalSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, surface.position(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepConicalSurface) {
            StepConicalSurface surface = (StepConicalSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, surface.position(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSphericalSurface) {
            StepSphericalSurface surface = (StepSphericalSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, surface.position(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepToroidalSurface) {
            StepToroidalSurface surface = (StepToroidalSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, surface.position(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSurfaceOfLinearExtrusion) {
            StepSurfaceOfLinearExtrusion surface = (StepSurfaceOfLinearExtrusion) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, surface.sweptCurve(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, surface.extrusionAxis(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSurfaceOfRevolution) {
            StepSurfaceOfRevolution surface = (StepSurfaceOfRevolution) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, surface.sweptCurve(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, surface.axisPosition(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepRectangularTrimmedSurface) {
            StepRectangularTrimmedSurface surface = (StepRectangularTrimmedSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, surface.basisSurface(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCurveBoundedSurface) {
            StepCurveBoundedSurface surface = (StepCurveBoundedSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, surface.basisSurface(), resolved, instanceIdsByTargetId);
            for (StepEntity boundary : surface.boundaries()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, boundary, resolved, instanceIdsByTargetId);
            }
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepOrientedSurface) {
            StepOrientedSurface surface = (StepOrientedSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, surface.surfaceElement(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepOffsetSurface) {
            StepOffsetSurface surface = (StepOffsetSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, surface.basisSurface(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepBSplineSurface) {
            StepBSplineSurface surface = (StepBSplineSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendSplineSurfaceControlPointTargets(targetsByUsageId, identifiedItem, surface.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepBSplineSurfaceWithKnots) {
            StepBSplineSurfaceWithKnots surface = (StepBSplineSurfaceWithKnots) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendSplineSurfaceControlPointTargets(targetsByUsageId, identifiedItem, surface.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepRationalBSplineSurface) {
            StepRationalBSplineSurface surface = (StepRationalBSplineSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendSplineSurfaceControlPointTargets(targetsByUsageId, identifiedItem, surface.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepBezierSurface) {
            StepBezierSurface surface = (StepBezierSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendSplineSurfaceControlPointTargets(targetsByUsageId, identifiedItem, surface.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepUniformSurface) {
            StepUniformSurface surface = (StepUniformSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendSplineSurfaceControlPointTargets(targetsByUsageId, identifiedItem, surface.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepQuasiUniformSurface) {
            StepQuasiUniformSurface surface = (StepQuasiUniformSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendSplineSurfaceControlPointTargets(targetsByUsageId, identifiedItem, surface.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPiecewiseBezierSurface) {
            StepPiecewiseBezierSurface surface = (StepPiecewiseBezierSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendSplineSurfaceControlPointTargets(targetsByUsageId, identifiedItem, surface.controlPoints(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepFace) {
            StepFace face = (StepFace) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, face, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, face, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepBoundedCurve) {
            StepBoundedCurve curve = (StepBoundedCurve) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, curve, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, curve, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepBoundedSurface) {
            StepBoundedSurface surface = (StepBoundedSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSurface) {
            StepSurface surface = (StepSurface) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, surface, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, surface, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepMeasureRepresentationItem) {
            StepMeasureRepresentationItem item = (StepMeasureRepresentationItem) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, item, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, item.unit(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDescriptiveRepresentationItem) {
            StepDescriptiveRepresentationItem item = (StepDescriptiveRepresentationItem) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, item, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepValueRepresentationItem) {
            StepValueRepresentationItem item = (StepValueRepresentationItem) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, item, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSurfaceModel) {
            StepSurfaceModel model = (StepSurfaceModel) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, model, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, model, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepSolidModel) {
            StepSolidModel model = (StepSolidModel) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, model, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, model, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepRepresentationItem) {
            StepRepresentationItem item = (StepRepresentationItem) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, item, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepGeometricRepresentationItem) {
            StepGeometricRepresentationItem item = (StepGeometricRepresentationItem) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, item, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepTopologicalRepresentationItem) {
            StepTopologicalRepresentationItem item = (StepTopologicalRepresentationItem) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, item, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepMeasureWithUnit) {
            StepMeasureWithUnit measure = (StepMeasureWithUnit) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, measure, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, measure.unitComponent(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, measure, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepTypedMeasureWithUnit) {
            StepTypedMeasureWithUnit measure = (StepTypedMeasureWithUnit) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, measure, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, measure.unitComponent(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, measure, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepUncertaintyMeasureWithUnit) {
            StepUncertaintyMeasureWithUnit measure = (StepUncertaintyMeasureWithUnit) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, measure, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, measure.unitComponent(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, measure, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepConversionBasedUnit) {
            StepConversionBasedUnit unit = (StepConversionBasedUnit) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, unit, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, unit.conversionFactor(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepConversionBasedUnitWithOffset) {
            StepConversionBasedUnitWithOffset unit = (StepConversionBasedUnitWithOffset) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, unit, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, unit.conversionFactor(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDerivedUnit) {
            StepDerivedUnit unit = (StepDerivedUnit) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, unit, instanceIdsByTargetId);
            for (StepDerivedUnitElement element : unit.elements()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, element, resolved, instanceIdsByTargetId);
            }
        } else if (definition instanceof StepDerivedUnitElement) {
            StepDerivedUnitElement element = (StepDerivedUnitElement) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, element, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, element.unit(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepNamedUnit) {
            StepNamedUnit unit = (StepNamedUnit) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, unit, instanceIdsByTargetId);
        } else if (definition instanceof StepSiUnit) {
            StepSiUnit unit = (StepSiUnit) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, unit, instanceIdsByTargetId);
        } else if (definition instanceof StepContextDependentUnit) {
            StepContextDependentUnit unit = (StepContextDependentUnit) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, unit, instanceIdsByTargetId);
        } else if (definition instanceof StepRepresentationContext) {
            StepRepresentationContext context = (StepRepresentationContext) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, context, instanceIdsByTargetId);
        } else if (definition instanceof StepGeometricRepresentationContext) {
            StepGeometricRepresentationContext context = (StepGeometricRepresentationContext) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, context, instanceIdsByTargetId);
            if (context.globalUnitAssignedContext() != null) {
                appendNestedDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        context.globalUnitAssignedContext(),
                        resolved,
                        instanceIdsByTargetId
                );
            }
            if (context.globalUncertaintyAssignedContext() != null) {
                appendNestedDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        context.globalUncertaintyAssignedContext(),
                        resolved,
                        instanceIdsByTargetId
                );
            }
        } else if (definition instanceof StepGlobalUnitAssignedContext) {
            StepGlobalUnitAssignedContext context = (StepGlobalUnitAssignedContext) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, context, instanceIdsByTargetId);
            for (StepEntity unit : context.units()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, unit, resolved, instanceIdsByTargetId);
            }
        } else if (definition instanceof StepGlobalUncertaintyAssignedContext) {
            StepGlobalUncertaintyAssignedContext context = (StepGlobalUncertaintyAssignedContext) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, context, instanceIdsByTargetId);
            for (StepUncertaintyMeasureWithUnit uncertainty : context.uncertainties()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, uncertainty, resolved, instanceIdsByTargetId);
            }
        } else if (definition instanceof StepAddress) {
            StepAddress address = (StepAddress) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, address, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, address, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepCharacterizedObject) {
            StepCharacterizedObject characterizedObject = (StepCharacterizedObject) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, characterizedObject, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, characterizedObject, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDimensionalExponents) {
            StepDimensionalExponents exponents = (StepDimensionalExponents) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, exponents, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, exponents, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepVertex) {
            StepVertex vertex = (StepVertex) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, vertex, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, vertex, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepEdge) {
            StepEdge edge = (StepEdge) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, edge, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, edge, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepAbstractVariable) {
            StepAbstractVariable variable = (StepAbstractVariable) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, variable, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    variable.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, variable.definition(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepRowVariable) {
            StepRowVariable variable = (StepRowVariable) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, variable, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    variable.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, variable.definition(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepScalarVariable) {
            StepScalarVariable variable = (StepScalarVariable) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, variable, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    variable.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, variable.definition(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepForwardChainingRulePremise) {
            StepForwardChainingRulePremise variable = (StepForwardChainingRulePremise) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, variable, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    variable.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, variable.definition(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepBackChainingRuleBody) {
            StepBackChainingRuleBody variable = (StepBackChainingRuleBody) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, variable, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    variable.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, variable.definition(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPropertyDefinitionRepresentation) {
            StepPropertyDefinitionRepresentation link = (StepPropertyDefinitionRepresentation) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, link, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, link.definition(), resolved, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    link.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepActionPropertyRepresentation) {
            StepActionPropertyRepresentation link = (StepActionPropertyRepresentation) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, link, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, link.definition(), resolved, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    link.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepContactRatioRepresentation) {
            StepContactRatioRepresentation link = (StepContactRatioRepresentation) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, link, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, link.definition(), resolved, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    link.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepKinematicPropertyDefinitionRepresentation) {
            StepKinematicPropertyDefinitionRepresentation link = (StepKinematicPropertyDefinitionRepresentation) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, link, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, link.definition(), resolved, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    link.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepKinematicPropertyMechanismRepresentation) {
            StepKinematicPropertyMechanismRepresentation link = (StepKinematicPropertyMechanismRepresentation) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, link, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, link.definition(), resolved, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    link.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepKinematicPropertyRepresentationRelation) {
            StepKinematicPropertyRepresentationRelation link = (StepKinematicPropertyRepresentationRelation) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, link, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, link.definition(), resolved, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    link.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepKinematicPropertyTopologyRepresentation) {
            StepKinematicPropertyTopologyRepresentation link = (StepKinematicPropertyTopologyRepresentation) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, link, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, link.definition(), resolved, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    link.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepResourcePropertyRepresentation) {
            StepResourcePropertyRepresentation link = (StepResourcePropertyRepresentation) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, link, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, link.definition(), resolved, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    link.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepAttributeAssertion) {
            StepAttributeAssertion assertion = (StepAttributeAssertion) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, assertion, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, assertion.definition(), resolved, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    assertion.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepShapeDefinitionRepresentation) {
            StepShapeDefinitionRepresentation link = (StepShapeDefinitionRepresentation) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, link, instanceIdsByTargetId);
            appendAttachedRepresentationRelationshipTargets(
                    targetsByUsageId,
                    identifiedItem,
                    link.usedRepresentation(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepApplicationProtocolDefinition) {
            StepApplicationProtocolDefinition protocolDefinition = (StepApplicationProtocolDefinition) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, protocolDefinition, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, protocolDefinition.application(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepProduct) {
            StepProduct product = (StepProduct) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, product, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, product, resolved, instanceIdsByTargetId);
            for (StepProductContext context : product.frameOfReference()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, context, resolved, instanceIdsByTargetId);
            }
        } else if (definition instanceof StepProductDefinitionFormation) {
            StepProductDefinitionFormation formation = (StepProductDefinitionFormation) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, formation, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, formation, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, formation.ofProduct(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepProductDefinition) {
            StepProductDefinition productDefinition = (StepProductDefinition) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, productDefinition, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, productDefinition, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, productDefinition.formation(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, productDefinition.frameOfReference(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepProductDefinitionShape) {
            StepProductDefinitionShape productDefinitionShape = (StepProductDefinitionShape) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, productDefinitionShape, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, productDefinitionShape, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, productDefinitionShape.definition(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepProductContext) {
            StepProductContext productContext = (StepProductContext) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, productContext, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, productContext, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, productContext.frameOfReference(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepProductDefinitionContext) {
            StepProductDefinitionContext productDefinitionContext = (StepProductDefinitionContext) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, productDefinitionContext, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, productDefinitionContext, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, productDefinitionContext.frameOfReference(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepApplicationContext) {
            StepApplicationContext applicationContext = (StepApplicationContext) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, applicationContext, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, applicationContext, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepGroup) {
            StepGroup group = (StepGroup) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, group, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, group, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDocument) {
            StepDocument document = (StepDocument) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, document, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, document, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, document.kind(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDocumentReference) {
            StepDocumentReference reference = (StepDocumentReference) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, reference, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, reference, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, reference.assignedDocument(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, reference.assignedDocument().kind(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepAppliedDocumentReference) {
            StepAppliedDocumentReference reference = (StepAppliedDocumentReference) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, reference, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, reference, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, reference.assignedDocument(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, reference.assignedDocument().kind(), resolved, instanceIdsByTargetId);
            for (StepEntity item : reference.items()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
            }
        } else if (definition instanceof StepExternalSource) {
            StepExternalSource source = (StepExternalSource) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, source, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, source, resolved, instanceIdsByTargetId);
            appendExternalSourceRelationshipTargets(targetsByUsageId, identifiedItem, source, resolved, instanceIdsByTargetId);
            appendExternallyDefinedItemTargets(targetsByUsageId, identifiedItem, source, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepExternallyDefinedItem) {
            StepExternallyDefinedItem item = (StepExternallyDefinedItem) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, item, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, item, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, item.source(), resolved, instanceIdsByTargetId);
            appendExternalSourceRelationshipTargets(targetsByUsageId, identifiedItem, item.source(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepDocumentUsageConstraint) {
            StepDocumentUsageConstraint documentUsageConstraint = (StepDocumentUsageConstraint) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, documentUsageConstraint, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, documentUsageConstraint, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, documentUsageConstraint.source(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, documentUsageConstraint.source().kind(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepProductCategory) {
            StepProductCategory category = (StepProductCategory) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, category, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, category, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepProductRelatedProductCategory) {
            StepProductRelatedProductCategory relatedCategory = (StepProductRelatedProductCategory) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relatedCategory, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, relatedCategory, resolved, instanceIdsByTargetId);
            for (StepProduct product : relatedCategory.products()) {
                appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, product, resolved, instanceIdsByTargetId);
            }
        } else if (definition instanceof StepGeneralProperty) {
            StepGeneralProperty generalProperty = (StepGeneralProperty) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, generalProperty, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, generalProperty, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepProductDefinitionEffectivity) {
            StepProductDefinitionEffectivity effectivity = (StepProductDefinitionEffectivity) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, effectivity, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, effectivity, resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, effectivity.productDefinition(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepEffectivity) {
            StepEffectivity effectivity = (StepEffectivity) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, effectivity, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, effectivity, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepRepresentationRelationship) {
            StepRepresentationRelationship relationship = (StepRepresentationRelationship) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            for (StepEntity target : collectRepresentationTargetsFromRelationship(relationship)) {
                appendPmiTarget(
                        targetsByUsageId,
                        identifiedItem.id(),
                        target,
                        instanceIdsByTargetId,
                        null,
                        null,
                        null,
                        null,
                        definitionTypeName(relationship),
                        relationship.id()
                );
            }
        } else if (definition instanceof StepShapeRepresentationRelationship) {
            StepShapeRepresentationRelationship relationship = (StepShapeRepresentationRelationship) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            for (StepEntity target : collectRepresentationTargetsFromRelationship(relationship)) {
                appendPmiTarget(
                        targetsByUsageId,
                        identifiedItem.id(),
                        target,
                        instanceIdsByTargetId,
                        null,
                        null,
                        null,
                        null,
                        definitionTypeName(relationship),
                        relationship.id()
                );
            }
        } else if (definition instanceof StepContextDependentShapeRepresentation) {
            StepContextDependentShapeRepresentation shapeRepresentation = (StepContextDependentShapeRepresentation) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, shapeRepresentation, instanceIdsByTargetId);
            appendNestedDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    shapeRepresentation.representationRelationship(),
                    resolved,
                    instanceIdsByTargetId
            );
            appendNestedDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    shapeRepresentation.representedProductRelation(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepRepresentationRelationshipWithTransformation) {
            StepRepresentationRelationshipWithTransformation relationship = (StepRepresentationRelationshipWithTransformation) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            appendNestedDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    relationship.transformationOperator(),
                    resolved,
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepBoxDomain) {
            StepBoxDomain boxDomain = (StepBoxDomain) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, boxDomain, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, boxDomain.corner(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepBooleanClippingResult) {
            StepBooleanClippingResult result = (StepBooleanClippingResult) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, result, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, result.firstOperand(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, result.secondOperand(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepBooleanResult) {
            StepBooleanResult result = (StepBooleanResult) definition;
            appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, result, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, result.firstOperand(), resolved, instanceIdsByTargetId);
            appendNestedDefinitionTargets(targetsByUsageId, identifiedItem, result.secondOperand(), resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPreDefinedMarker) {
            StepPreDefinedMarker marker = (StepPreDefinedMarker) definition;
            appendPointMarkerStyleTargets(targetsByUsageId, identifiedItem, marker.id(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, marker, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepPreDefinedPointMarkerSymbol) {
            StepPreDefinedPointMarkerSymbol marker = (StepPreDefinedPointMarkerSymbol) definition;
            appendPointMarkerStyleTargets(targetsByUsageId, identifiedItem, marker.id(), resolved, instanceIdsByTargetId);
            appendIndirectPropertyRepresentationTargets(targetsByUsageId, identifiedItem, marker, resolved, instanceIdsByTargetId);
        } else if (definition instanceof StepAnnotationSymbol) {
            StepAnnotationSymbol annotationSymbol = (StepAnnotationSymbol) definition;
            appendMappedDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    annotationSymbol.mappingSource(),
                    annotationSymbol.mappingTarget(),
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepAnnotationText) {
            StepAnnotationText annotationText = (StepAnnotationText) definition;
            appendMappedDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    annotationText.mappingSource(),
                    annotationText.mappingTarget(),
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepAnnotationTextCharacter) {
            StepAnnotationTextCharacter annotationTextCharacter = (StepAnnotationTextCharacter) definition;
            appendMappedDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    annotationTextCharacter.mappingSource(),
                    annotationTextCharacter.mappingTarget(),
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepUserDefinedCurveFont) {
            StepUserDefinedCurveFont curveFont = (StepUserDefinedCurveFont) definition;
            appendMappedDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    curveFont.mappingSource(),
                    curveFont.mappingTarget(),
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepUserDefinedMarker) {
            StepUserDefinedMarker marker = (StepUserDefinedMarker) definition;
            appendMappedDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    marker.mappingSource(),
                    marker.mappingTarget(),
                    instanceIdsByTargetId
            );
        } else if (definition instanceof StepUserDefinedTerminatorSymbol) {
            StepUserDefinedTerminatorSymbol symbol = (StepUserDefinedTerminatorSymbol) definition;
            appendMappedDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    symbol.mappingSource(),
                    symbol.mappingTarget(),
                    instanceIdsByTargetId
            );
        }
        for (StepEntity target : collectSemanticTargets(definition, resolved, new LinkedHashSet<>())) {
            appendPmiTarget(
                    targetsByUsageId,
                    identifiedItem.id(),
                    target,
                    instanceIdsByTargetId,
                    null,
                    null,
                    null,
                    null,
                    definitionTypeName(definition),
                    definition.id()
            );
        }
    }

    private static void appendCarrierDefinitionTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity carrier,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        appendExistingRepresentationDefinitionTargets(
                targetsByUsageId,
                identifiedItem,
                carrier,
                instanceIdsByTargetId
        );
    }

    private static void appendDefinitionRelationshipTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepEntity relatedDefinition,
            String viaDefinitionType,
            int viaDefinitionId,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (relatedDefinition instanceof StepPropertyDefinition) {
            StepPropertyDefinition propertyDefinition = (StepPropertyDefinition) relatedDefinition;
            appendPropertyRepresentationLinkTargets(
                    targetsByUsageId,
                    usageId,
                    propertyDefinition,
                    resolved,
                    instanceIdsByTargetId
            );
        }
        for (StepEntity target : collectSemanticTargets(relatedDefinition, resolved, new LinkedHashSet<>())) {
            appendPmiTarget(
                    targetsByUsageId,
                    usageId,
                    target,
                    instanceIdsByTargetId,
                    null,
                    null,
                    null,
                    null,
                    viaDefinitionType,
                    viaDefinitionId
            );
        }
    }

    private static void appendNestedDefinitionTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity nestedDefinition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        if (nestedDefinition == null) {
            return;
        }
        appendExistingRepresentationDefinitionTargets(
                targetsByUsageId,
                identifiedItem,
                nestedDefinition,
                instanceIdsByTargetId
        );
        appendSemanticDefinitionTargets(
                targetsByUsageId,
                identifiedItem,
                nestedDefinition,
                resolved,
                instanceIdsByTargetId
        );
    }

    private static void appendRelationshipSemanticTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            String relationshipType,
            int relationshipId,
            StepEntity source,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity target : collectSemanticTargets(source, resolved, new LinkedHashSet<>())) {
            appendPmiTarget(
                    targetsByUsageId,
                    usageId,
                    target,
                    instanceIdsByTargetId,
                    relationshipType,
                    relationshipId
            );
        }
    }

    private static void propagateCalloutTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepDraughtingCalloutRelationship relationship
    ) {
        List<PmiTargetPayload> relatingTargets = targetsByUsageId.get(relationship.relatingCallout().id());
        if (relatingTargets == null || relatingTargets.isEmpty()) {
            return;
        }
        List<PmiTargetPayload> relatedTargets = targetsByUsageId.computeIfAbsent(
                relationship.relatedCallout().id(),
                ignored -> new ArrayList<>()
        );
        for (PmiTargetPayload target : relatingTargets) {
            PmiTargetPayload propagated = new PmiTargetPayload(
                    target.id(),
                    target.type(),
                    target.name(),
                    target.instanceIds(),
                    "DRAUGHTING_CALLOUT_RELATIONSHIP",
                    relationship.id(),
                    target.viaUsageType(),
                    target.viaUsageId(),
                    target.viaDefinitionType(),
                    target.viaDefinitionId()
            );
            if (!relatedTargets.contains(propagated)) {
                relatedTargets.add(propagated);
            }
        }
    }

    // ========================================================================
    // Point Extraction Methods
    // ========================================================================

    private static CartesianPoint pointFromAnnotationOccurrence(StepEntity occurrence, StepCadBuilder builder) {
        if (occurrence instanceof StepAnnotationPointOccurrence) {
            StepAnnotationPointOccurrence pointOccurrence = (StepAnnotationPointOccurrence) occurrence;
            return StepPreviewJsonExporter.pointFromAnnotationPoint(pointOccurrence.item(), builder);
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
            return StepPreviewJsonExporter.pointFromAnnotationPoint(fillAreaOccurrence.fillStyleTarget(), builder);
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
            return StepPreviewJsonExporter.pointFromAnnotationPoint(textOccurrence.position(), builder);
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
            return StepPreviewJsonExporter.pointFromStep(vertexShell.extent().loopVertex().point());
        } else if (occurrence instanceof StepGeometricReplica
                && "POINT_REPLICA".equals(((StepGeometricReplica) occurrence).entityName())) {
            StepGeometricReplica replica = (StepGeometricReplica) occurrence;
            return builder == null ? null : StepPreviewJsonExporter.pointFromReplica(replica, builder);
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
            point = StepPreviewJsonExporter.pointFromAnnotationPoint(element, builder);
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
            point = StepPreviewJsonExporter.pointFromAnnotationPoint(element, builder);
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
            point = StepPreviewJsonExporter.pointFromAnnotationPoint(element, builder);
            if (point != null) {
                return point;
            }
        }
        return null;
    }

    private static CartesianPoint pointFromPointSet(StepPointSet pointSet, StepCadBuilder builder) {
        for (StepEntity item : pointSet.points()) {
            CartesianPoint point = StepPreviewJsonExporter.pointFromAnnotationPoint(item, builder);
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
        return StepPreviewJsonExporter.pointFromAnnotationPoint(item, builder);
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
            point = StepPreviewJsonExporter.pointFromAnnotationPoint(item, builder);
        }
        if (point != null) {
            positions.add(point);
        }
    }

    private static CartesianPoint pointFromPlacement(StepEntity placement) {
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
        return StepEdgePayloadBuilder.sampleLooseCurve(curve);
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

    private static void appendIndirectPropertyRepresentationTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepPropertyDefinition
                    && ((StepPropertyDefinition) candidate).definition().id() == definition.id()) {
                StepPropertyDefinition propertyDefinition = (StepPropertyDefinition) candidate;
                appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, propertyDefinition, instanceIdsByTargetId);
                appendPropertyRepresentationLinkTargets(
                        targetsByUsageId,
                        identifiedItem,
                        propertyDefinition,
                        resolved,
                        instanceIdsByTargetId
                );
            }
        }
    }

    private static void appendProductRelationshipTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepProduct product,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepProductRelationship) {
            StepProductRelationship relationship = (StepProductRelationship) candidate;
                if (relationship.relatingProduct().id() == product.id()) {
                    appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relationship.relatedProduct(), relationshipTypeName(relationship), relationship.id(), resolved, instanceIdsByTargetId);
                }
                if (relationship.relatedProduct().id() == product.id()) {
                    appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relationship.relatingProduct(), relationshipTypeName(relationship), relationship.id(), resolved, instanceIdsByTargetId);
                }
            } else if (candidate instanceof StepProductRelatedProductCategory
                    && ((StepProductRelatedProductCategory) candidate).products().stream().anyMatch(related -> related.id() == product.id())) {
                StepProductRelatedProductCategory relatedCategory = (StepProductRelatedProductCategory) candidate;
                appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relatedCategory, relationshipTypeName(relatedCategory), relatedCategory.id(), resolved, instanceIdsByTargetId);
            }
        }
    }

    private static void appendProductDefinitionFormationRelationshipTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepProductDefinitionFormation formation,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (!(candidate instanceof StepProductDefinitionFormationRelationship)) {
                continue;
            }
            StepProductDefinitionFormationRelationship relationship = (StepProductDefinitionFormationRelationship) candidate;
            if (relationship.relatingFormation().id() == formation.id()) {
                appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relationship.relatedFormation(), relationshipTypeName(relationship), relationship.id(), resolved, instanceIdsByTargetId);
            }
            if (relationship.relatedFormation().id() == formation.id()) {
                appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relationship.relatingFormation(), relationshipTypeName(relationship), relationship.id(), resolved, instanceIdsByTargetId);
            }
        }
    }

    private static void appendProductDefinitionRelationshipTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepProductDefinition productDefinition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (!(candidate instanceof StepProductDefinitionRelationship
                    || candidate instanceof StepNextAssemblyUsageOccurrence)) {
                continue;
            }
            if (candidate instanceof StepProductDefinitionRelationship) {
                StepProductDefinitionRelationship relationship = (StepProductDefinitionRelationship) candidate;
                if (relationship.relatingProductDefinition().id() == productDefinition.id()) {
                    appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relationship.relatedProductDefinition(), relationshipTypeName(relationship), relationship.id(), resolved, instanceIdsByTargetId);
                }
                if (relationship.relatedProductDefinition().id() == productDefinition.id()) {
                    appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relationship.relatingProductDefinition(), relationshipTypeName(relationship), relationship.id(), resolved, instanceIdsByTargetId);
                }
            } else {
                StepNextAssemblyUsageOccurrence occurrence = (StepNextAssemblyUsageOccurrence) candidate;
                if (occurrence.relatingProductDefinition().id() == productDefinition.id()
                        || occurrence.relatedProductDefinition().id() == productDefinition.id()) {
                    appendDefinitionRelationshipTargets(targetsByUsageId, usageId, occurrence, relationshipTypeName(occurrence), occurrence.id(), resolved, instanceIdsByTargetId);
                }
            }
        }
    }

    private static void appendAttachedRepresentationRelationshipTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepRepresentation representation,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepRepresentationRelationship
                    && referencesRepresentation(((StepRepresentationRelationship) candidate).rep1(),
                            ((StepRepresentationRelationship) candidate).rep2(), representation.id())) {
                StepRepresentationRelationship relationship = (StepRepresentationRelationship) candidate;
                appendDefinitionRelationshipTargets(
                        targetsByUsageId,
                        identifiedItem.id(),
                        relationship,
                        relationshipTypeName(relationship),
                        relationship.id(),
                        resolved,
                        instanceIdsByTargetId
                );
            } else if (candidate instanceof StepRepresentationRelationshipWithTransformation
                    && referencesRepresentation(((StepRepresentationRelationshipWithTransformation) candidate).rep1(),
                            ((StepRepresentationRelationshipWithTransformation) candidate).rep2(), representation.id())) {
                StepRepresentationRelationshipWithTransformation transformed = (StepRepresentationRelationshipWithTransformation) candidate;
                appendDefinitionRelationshipTargets(
                        targetsByUsageId,
                        identifiedItem.id(),
                        transformed,
                        relationshipTypeName(transformed),
                        transformed.id(),
                        resolved,
                        instanceIdsByTargetId
                );
                appendDefinitionRelationshipTargets(
                        targetsByUsageId,
                        identifiedItem.id(),
                        transformed.transformationOperator(),
                        definitionTypeName(transformed.transformationOperator()),
                        transformed.transformationOperator().id(),
                        resolved,
                        instanceIdsByTargetId
                );
                appendNestedDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        transformed.transformationOperator().transformItem1(),
                        resolved,
                        instanceIdsByTargetId
                );
                appendNestedDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        transformed.transformationOperator().transformItem2(),
                        resolved,
                        instanceIdsByTargetId
                );
            } else if (candidate instanceof StepShapeRepresentationRelationship
                    && referencesRepresentation(((StepShapeRepresentationRelationship) candidate).rep1(),
                            ((StepShapeRepresentationRelationship) candidate).rep2(), representation.id())) {
                StepShapeRepresentationRelationship relationship = (StepShapeRepresentationRelationship) candidate;
                appendDefinitionRelationshipTargets(
                        targetsByUsageId,
                        identifiedItem.id(),
                        relationship,
                        relationshipTypeName(relationship),
                        relationship.id(),
                        resolved,
                        instanceIdsByTargetId
                );
            }
        }
    }

    private static boolean referencesRepresentation(
            StepRepresentation rep1,
            StepRepresentation rep2,
            int representationId
    ) {
        return rep1.id() == representationId || rep2.id() == representationId;
    }

    private static void appendPropertyDefinitionRelationshipTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepPropertyDefinition propertyDefinition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (!(candidate instanceof StepPropertyDefinitionRelationship)) {
                continue;
            }
            StepPropertyDefinitionRelationship relationship = (StepPropertyDefinitionRelationship) candidate;
            if (relationship.relatingPropertyDefinition().id() == propertyDefinition.id()) {
                appendDefinitionRelationshipTargets(
                        targetsByUsageId,
                        usageId,
                        relationship.relatedPropertyDefinition(),
                        relationshipTypeName(relationship),
                        relationship.id(),
                        resolved,
                        instanceIdsByTargetId
                );
            }
            if (relationship.relatedPropertyDefinition().id() == propertyDefinition.id()) {
                appendDefinitionRelationshipTargets(
                        targetsByUsageId,
                        usageId,
                        relationship.relatingPropertyDefinition(),
                        relationshipTypeName(relationship),
                        relationship.id(),
                        resolved,
                        instanceIdsByTargetId
                );
            }
        }
    }

    private static StepRepresentation propertyRepresentationLinkRepresentation(StepEntity candidate, int propertyDefinitionId) {
        if (candidate instanceof StepPropertyDefinitionRepresentation
                && ((StepPropertyDefinitionRepresentation) candidate).definition().id() == propertyDefinitionId) {
            StepPropertyDefinitionRepresentation representationLink = (StepPropertyDefinitionRepresentation) candidate;
            return representationLink.usedRepresentation();
        }
        if (candidate instanceof StepAttributeAssertion
                && ((StepAttributeAssertion) candidate).definition().id() == propertyDefinitionId) {
            StepAttributeAssertion representationLink = (StepAttributeAssertion) candidate;
            return representationLink.usedRepresentation();
        }
        if (candidate instanceof StepActionPropertyRepresentation
                && ((StepActionPropertyRepresentation) candidate).definition().id() == propertyDefinitionId) {
            StepActionPropertyRepresentation representationLink = (StepActionPropertyRepresentation) candidate;
            return representationLink.usedRepresentation();
        }
        if (candidate instanceof StepContactRatioRepresentation
                && ((StepContactRatioRepresentation) candidate).definition().id() == propertyDefinitionId) {
            StepContactRatioRepresentation representationLink = (StepContactRatioRepresentation) candidate;
            return representationLink.usedRepresentation();
        }
        if (candidate instanceof StepKinematicPropertyDefinitionRepresentation
                && ((StepKinematicPropertyDefinitionRepresentation) candidate).definition().id() == propertyDefinitionId) {
            StepKinematicPropertyDefinitionRepresentation representationLink = (StepKinematicPropertyDefinitionRepresentation) candidate;
            return representationLink.usedRepresentation();
        }
        if (candidate instanceof StepKinematicPropertyMechanismRepresentation
                && ((StepKinematicPropertyMechanismRepresentation) candidate).definition().id() == propertyDefinitionId) {
            StepKinematicPropertyMechanismRepresentation representationLink = (StepKinematicPropertyMechanismRepresentation) candidate;
            return representationLink.usedRepresentation();
        }
        if (candidate instanceof StepKinematicPropertyRepresentationRelation
                && ((StepKinematicPropertyRepresentationRelation) candidate).definition().id() == propertyDefinitionId) {
            StepKinematicPropertyRepresentationRelation representationLink = (StepKinematicPropertyRepresentationRelation) candidate;
            return representationLink.usedRepresentation();
        }
        if (candidate instanceof StepKinematicPropertyTopologyRepresentation
                && ((StepKinematicPropertyTopologyRepresentation) candidate).definition().id() == propertyDefinitionId) {
            StepKinematicPropertyTopologyRepresentation representationLink = (StepKinematicPropertyTopologyRepresentation) candidate;
            return representationLink.usedRepresentation();
        }
        if (candidate instanceof StepResourcePropertyRepresentation
                && ((StepResourcePropertyRepresentation) candidate).definition().id() == propertyDefinitionId) {
            StepResourcePropertyRepresentation representationLink = (StepResourcePropertyRepresentation) candidate;
            return representationLink.usedRepresentation();
        }
        if (candidate instanceof StepForwardChainingRulePremise
                && ((StepForwardChainingRulePremise) candidate).definition().id() == propertyDefinitionId) {
            StepForwardChainingRulePremise representationLink = (StepForwardChainingRulePremise) candidate;
            return representationLink.usedRepresentation();
        }
        if (candidate instanceof StepBackChainingRuleBody
                && ((StepBackChainingRuleBody) candidate).definition().id() == propertyDefinitionId) {
            StepBackChainingRuleBody representationLink = (StepBackChainingRuleBody) candidate;
            return representationLink.usedRepresentation();
        }
        if (candidate instanceof StepPlacedDatumTargetFeature
                && ((StepPlacedDatumTargetFeature) candidate).definition().id() == propertyDefinitionId) {
            StepPlacedDatumTargetFeature representationLink = (StepPlacedDatumTargetFeature) candidate;
            return representationLink.usedRepresentation();
        }
        return null;
    }

    private static void appendGeneralPropertyRelationshipTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepGeneralProperty generalProperty,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (!(candidate instanceof StepGeneralPropertyRelationship)) {
                continue;
            }
            StepGeneralPropertyRelationship relationship = (StepGeneralPropertyRelationship) candidate;
            if (relationship.relatingGeneralProperty().id() == generalProperty.id()) {
                appendDefinitionRelationshipTargets(
                        targetsByUsageId,
                        usageId,
                        relationship.relatedGeneralProperty(),
                        relationshipTypeName(relationship),
                        relationship.id(),
                        resolved,
                        instanceIdsByTargetId
                );
            }
            if (relationship.relatedGeneralProperty().id() == generalProperty.id()) {
                appendDefinitionRelationshipTargets(
                        targetsByUsageId,
                        usageId,
                        relationship.relatingGeneralProperty(),
                        relationshipTypeName(relationship),
                        relationship.id(),
                        resolved,
                        instanceIdsByTargetId
                );
            }
        }
    }

    private static void appendShapeAspectRelationshipTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepShapeAspect shapeAspect,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (!(candidate instanceof StepShapeAspectRelationship)) {
                continue;
            }
            StepShapeAspectRelationship relationship = (StepShapeAspectRelationship) candidate;
            if (relationship.relatingShapeAspect().id() == shapeAspect.id()) {
                appendDefinitionRelationshipTargets(
                        targetsByUsageId,
                        usageId,
                        relationship.relatedShapeAspect(),
                        relationshipTypeName(relationship),
                        relationship.id(),
                        resolved,
                        instanceIdsByTargetId
                );
            }
            if (relationship.relatedShapeAspect().id() == shapeAspect.id()) {
                appendDefinitionRelationshipTargets(
                        targetsByUsageId,
                        usageId,
                        relationship.relatingShapeAspect(),
                        relationshipTypeName(relationship),
                        relationship.id(),
                        resolved,
                        instanceIdsByTargetId
                );
            }
        }
    }

    private static void appendGroupRelationshipTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepGroup group,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (!(candidate instanceof StepGroupRelationship)) {
                continue;
            }
            StepGroupRelationship relationship = (StepGroupRelationship) candidate;
            if (relationship.relatingGroup().id() == group.id()) {
                appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relationship.relatedGroup(), relationshipTypeName(relationship), relationship.id(), resolved, instanceIdsByTargetId);
            }
            if (relationship.relatedGroup().id() == group.id()) {
                appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relationship.relatingGroup(), relationshipTypeName(relationship), relationship.id(), resolved, instanceIdsByTargetId);
            }
        }
    }

    private static void appendDocumentRelationshipTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepDocument document,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (!(candidate instanceof StepDocumentRelationship)) {
                continue;
            }
            StepDocumentRelationship relationship = (StepDocumentRelationship) candidate;
            if (relationship.relatingDocument().id() == document.id()) {
                appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relationship.relatedDocument(), relationshipTypeName(relationship), relationship.id(), resolved, instanceIdsByTargetId);
            }
            if (relationship.relatedDocument().id() == document.id()) {
                appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relationship.relatingDocument(), relationshipTypeName(relationship), relationship.id(), resolved, instanceIdsByTargetId);
            }
        }
    }

    // ========================================================================
    // Semantic Target Collection
    // ========================================================================

    private static Set<StepEntity> collectTargetsReferencingEntity(
            int referencedId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepPropertyDefinition
                    && ((StepPropertyDefinition) candidate).definition().id() == referencedId) {
                StepPropertyDefinition propertyDefinition = (StepPropertyDefinition) candidate;
                targets.addAll(collectSemanticTargets(propertyDefinition, resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }

    private static Set<StepEntity> collectRepresentationTargetsFromRelationship(StepEntity relationship) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        if (relationship instanceof StepRepresentationRelationship) {
            StepRepresentationRelationship representationRelationship = (StepRepresentationRelationship) relationship;
            targets.add(representationRelationship.rep1());
            targets.add(representationRelationship.rep2());
        } else if (relationship instanceof StepRepresentationRelationshipWithTransformation) {
            StepRepresentationRelationshipWithTransformation representationRelationship = (StepRepresentationRelationshipWithTransformation) relationship;
            targets.add(representationRelationship.rep1());
            targets.add(representationRelationship.rep2());
        } else if (relationship instanceof StepShapeRepresentationRelationship) {
            StepShapeRepresentationRelationship representationRelationship = (StepShapeRepresentationRelationship) relationship;
            targets.add(representationRelationship.rep1());
            targets.add(representationRelationship.rep2());
        }
        return Set.copyOf(targets);
    }

    // ========================================================================
    // Utility Methods
    // ========================================================================

    public static String definitionTypeName(StepEntity definition) {
        return StepTypeNameResolver.definitionTypeName(definition);
    }

    private static String relationshipTypeName(StepEntity relationship) {
        return StepTypeNameResolver.relationshipTypeName(relationship);
    }
    private static void appendApprovalDecorationTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepApproval approval,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepApprovalPersonOrganization
                    && ((StepApprovalPersonOrganization) candidate).authorizedApproval().id() == approval.id()) {
                StepApprovalPersonOrganization personOrganization = (StepApprovalPersonOrganization) candidate;
                appendExistingRepresentationDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        personOrganization,
                        instanceIdsByTargetId
                );
                appendExistingRepresentationDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        personOrganization.personOrganization(),
                        instanceIdsByTargetId
                );
                appendExistingRepresentationDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        personOrganization.personOrganization().person(),
                        instanceIdsByTargetId
                );
                appendExistingRepresentationDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        personOrganization.personOrganization().organization(),
                        instanceIdsByTargetId
                );
                appendExistingRepresentationDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        personOrganization.role(),
                        instanceIdsByTargetId
                );
            } else if (candidate instanceof StepApprovalDateTime
                    && ((StepApprovalDateTime) candidate).datedApproval().id() == approval.id()) {
                StepApprovalDateTime approvalDateTime = (StepApprovalDateTime) candidate;
                appendExistingRepresentationDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        approvalDateTime,
                        instanceIdsByTargetId
                );
                appendExistingRepresentationDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        approvalDateTime.dateTime(),
                        instanceIdsByTargetId
                );
                appendExistingRepresentationDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        approvalDateTime.dateTime().dateComponent(),
                        instanceIdsByTargetId
                );
                appendExistingRepresentationDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        approvalDateTime.dateTime().timeComponent(),
                        instanceIdsByTargetId
                );
                appendExistingRepresentationDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        approvalDateTime.dateTime().timeComponent().zone(),
                        instanceIdsByTargetId
                );
            }
        }
    }

    private static void appendEffectivityRelationshipTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepEffectivity effectivity,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (!(candidate instanceof StepEffectivityRelationship)) {
                continue;
            }
            StepEffectivityRelationship relationship = (StepEffectivityRelationship) candidate;
            if (relationship.relatingEffectivity().id() == effectivity.id()) {
                appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relationship.relatedEffectivity(), relationshipTypeName(relationship), relationship.id(), resolved, instanceIdsByTargetId);
            }
            if (relationship.relatedEffectivity().id() == effectivity.id()) {
                appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relationship.relatingEffectivity(), relationshipTypeName(relationship), relationship.id(), resolved, instanceIdsByTargetId);
            }
        }
    }

    private static void appendExternallyDefinedItemTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepExternalSource source,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        Set<Integer> linkedSourceIds = new LinkedHashSet<>();
        linkedSourceIds.add(source.id());
        boolean changed;
        do {
            changed = false;
            for (StepEntity candidate : resolved.values()) {
                if (!(candidate instanceof StepExternalSourceRelationship)) {
                    continue;
                }
                StepExternalSourceRelationship relationship = (StepExternalSourceRelationship) candidate;
                int relatingId = relationship.relatingSource().id();
                int relatedId = relationship.relatedSource().id();
                if (linkedSourceIds.contains(relatingId) && linkedSourceIds.add(relatedId)) {
                    changed = true;
                }
                if (linkedSourceIds.contains(relatedId) && linkedSourceIds.add(relatingId)) {
                    changed = true;
                }
            }
        } while (changed);
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepExternallyDefinedItem
                    && linkedSourceIds.contains(((StepExternallyDefinedItem) candidate).source().id())) {
                StepExternallyDefinedItem item = (StepExternallyDefinedItem) candidate;
                appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, item, instanceIdsByTargetId);
            }
        }
    }

    private static void appendExternalSourceRelationshipTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepExternalSource source,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (!(candidate instanceof StepExternalSourceRelationship)) {
                continue;
            }
            StepExternalSourceRelationship relationship = (StepExternalSourceRelationship) candidate;
            if (relationship.relatingSource().id() == source.id()) {
                appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            }
            if (relationship.relatedSource().id() == source.id()) {
                appendExistingRepresentationDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
            }
        }
    }

    private static void appendMappedDefinitionTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity mappingSource,
            StepEntity mappingTarget,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        appendExistingRepresentationDefinitionTargets(
                targetsByUsageId,
                identifiedItem,
                mappingSource,
                instanceIdsByTargetId
        );
        if (mappingSource instanceof StepRepresentationMap) {
            StepRepresentationMap representationMap = (StepRepresentationMap) mappingSource;
            appendRepresentationMapDefinitionTargets(targetsByUsageId, identifiedItem, representationMap, instanceIdsByTargetId);
        } else if (mappingSource instanceof StepSymbolRepresentationMap) {
            StepSymbolRepresentationMap representationMap = (StepSymbolRepresentationMap) mappingSource;
            appendRepresentationMapDefinitionTargets(targetsByUsageId, identifiedItem, representationMap, instanceIdsByTargetId);
        }
        appendPlacementDefinitionTargets(targetsByUsageId, identifiedItem, mappingTarget, instanceIdsByTargetId);
    }

    private static void appendOccurrenceRepresentationTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepNextAssemblyUsageOccurrence occurrence,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepProductDefinitionShape
                    && ((StepProductDefinitionShape) candidate).definition().id() == occurrence.id()) {
                StepProductDefinitionShape shape = (StepProductDefinitionShape) candidate;
                appendProductDefinitionShapeRepresentationTargets(
                        targetsByUsageId,
                        identifiedItem,
                        shape,
                        resolved,
                        instanceIdsByTargetId
                );
            }
        }
    }

    private static void appendOrganizationRelationshipTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepOrganization organization,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (!(candidate instanceof StepOrganizationRelationship)) {
                continue;
            }
            StepOrganizationRelationship relationship = (StepOrganizationRelationship) candidate;
            if (relationship.relatingOrganization().id() == organization.id()) {
                appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relationship.relatedOrganization(), relationshipTypeName(relationship), relationship.id(), resolved, instanceIdsByTargetId);
            }
            if (relationship.relatedOrganization().id() == organization.id()) {
                appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relationship.relatingOrganization(), relationshipTypeName(relationship), relationship.id(), resolved, instanceIdsByTargetId);
            }
        }
    }

    private static void appendPointMarkerStyleTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            int markerId,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepPointStyle && ((StepPointStyle) candidate).marker().id() == markerId) {
            StepPointStyle pointStyle = (StepPointStyle) candidate;
                appendExistingRepresentationDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        pointStyle,
                        instanceIdsByTargetId
                );
                appendExistingRepresentationDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        pointStyle.colour(),
                        instanceIdsByTargetId
                );
            }
        }
    }

    private static void appendProductCategoryRelationshipTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepProductCategory category,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (!(candidate instanceof StepProductCategoryRelationship)) {
                continue;
            }
            StepProductCategoryRelationship relationship = (StepProductCategoryRelationship) candidate;
            if (relationship.category().id() == category.id()) {
                appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relationship.subCategory(), relationshipTypeName(relationship), relationship.id(), resolved, instanceIdsByTargetId);
            }
            if (relationship.subCategory().id() == category.id()) {
                appendDefinitionRelationshipTargets(targetsByUsageId, usageId, relationship.category(), relationshipTypeName(relationship), relationship.id(), resolved, instanceIdsByTargetId);
            }
        }
    }

    private static void appendProductDefinitionEffectivityTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepEffectivity effectivity,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        Set<String> linkedEffectivityNames = new LinkedHashSet<>();
        linkedEffectivityNames.add(effectivity.name());
        boolean changed;
        do {
            changed = false;
            for (StepEntity candidate : resolved.values()) {
                if (!(candidate instanceof StepEffectivityRelationship)) {
                    continue;
                }
                StepEffectivityRelationship relationship = (StepEffectivityRelationship) candidate;
                String relatingName = relationship.relatingEffectivity().name();
                String relatedName = relationship.relatedEffectivity().name();
                if (linkedEffectivityNames.contains(relatingName) && linkedEffectivityNames.add(relatedName)) {
                    changed = true;
                }
                if (linkedEffectivityNames.contains(relatedName) && linkedEffectivityNames.add(relatingName)) {
                    changed = true;
                }
            }
        } while (changed);
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepProductDefinitionEffectivity
                    && linkedEffectivityNames.contains(((StepProductDefinitionEffectivity) candidate).effectivityId())) {
                StepProductDefinitionEffectivity productDefinitionEffectivity = (StepProductDefinitionEffectivity) candidate;
                appendDefinitionRelationshipTargets(
                        targetsByUsageId,
                        usageId,
                        productDefinitionEffectivity,
                        definitionTypeName(productDefinitionEffectivity),
                        productDefinitionEffectivity.id(),
                        resolved,
                        instanceIdsByTargetId
                );
            }
        }
    }

    private static void appendProductDefinitionRepresentationTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepProductDefinition productDefinition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepProductDefinitionShape
                    && ((StepProductDefinitionShape) candidate).definition().id() == productDefinition.id()) {
                StepProductDefinitionShape shape = (StepProductDefinitionShape) candidate;
                appendProductDefinitionShapeRepresentationTargets(
                        targetsByUsageId,
                        identifiedItem,
                        shape,
                        resolved,
                        instanceIdsByTargetId
                );
            }
        }
    }

    private static void appendProductDefinitionShapeRepresentationTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepProductDefinitionShape productDefinitionShape,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepShapeDefinitionRepresentation
                    && ((StepShapeDefinitionRepresentation) candidate).definition().id() == productDefinitionShape.id()) {
                StepShapeDefinitionRepresentation link = (StepShapeDefinitionRepresentation) candidate;
                appendDefinitionRelationshipTargets(
                        targetsByUsageId,
                        identifiedItem.id(),
                        link,
                        relationshipTypeName(link),
                        link.id(),
                        resolved,
                        instanceIdsByTargetId
                );
                appendAttachedRepresentationRelationshipTargets(
                        targetsByUsageId,
                        identifiedItem,
                        link.usedRepresentation(),
                        resolved,
                        instanceIdsByTargetId
                );
            } else if (candidate instanceof StepContextDependentShapeRepresentation
                    && ((StepContextDependentShapeRepresentation) candidate).representedProductRelation().id() == productDefinitionShape.id()) {
                StepContextDependentShapeRepresentation contextDependent = (StepContextDependentShapeRepresentation) candidate;
                appendDefinitionRelationshipTargets(
                        targetsByUsageId,
                        identifiedItem.id(),
                        contextDependent,
                        relationshipTypeName(contextDependent),
                        contextDependent.id(),
                        resolved,
                        instanceIdsByTargetId
                );
                if (contextDependent.representationRelationship() != null) {
                    appendDefinitionRelationshipTargets(
                        targetsByUsageId,
                            identifiedItem.id(),
                            contextDependent.representationRelationship(),
                            relationshipTypeName(contextDependent.representationRelationship()),
                            contextDependent.representationRelationship().id(),
                            resolved,
                            instanceIdsByTargetId
                    );
                    if (contextDependent.representationRelationship() instanceof StepRepresentationRelationshipWithTransformation) {
                        StepRepresentationRelationshipWithTransformation transformed = (StepRepresentationRelationshipWithTransformation) contextDependent.representationRelationship();
                        appendDefinitionRelationshipTargets(
                                targetsByUsageId,
                                identifiedItem.id(),
                                transformed.transformationOperator(),
                                definitionTypeName(transformed.transformationOperator()),
                                transformed.transformationOperator().id(),
                                resolved,
                                instanceIdsByTargetId
                        );
                        appendNestedDefinitionTargets(
                                targetsByUsageId,
                                identifiedItem,
                                transformed.transformationOperator().transformItem1(),
                                resolved,
                                instanceIdsByTargetId
                        );
                        appendNestedDefinitionTargets(
                                targetsByUsageId,
                                identifiedItem,
                                transformed.transformationOperator().transformItem2(),
                                resolved,
                                instanceIdsByTargetId
                        );
                    }
                }
            }
        }
    }

    private static void appendSplineCurveControlPointTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            List<StepCartesianPoint> controlPoints,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepCartesianPoint controlPoint : controlPoints) {
            appendNestedDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    controlPoint,
                    resolved,
                    instanceIdsByTargetId
            );
        }
    }

    private static void appendSplineSurfaceControlPointTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            List<List<StepCartesianPoint>> controlPoints,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (List<StepCartesianPoint> row : controlPoints) {
            appendSplineCurveControlPointTargets(
                    targetsByUsageId,
                    identifiedItem,
                    row,
                    resolved,
                    instanceIdsByTargetId
            );
        }
    }

    private static Set<StepEntity> collectTargetsForApprovalStatus(
            int statusId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepApproval
                    && ((StepApproval) candidate).status().id() == statusId) {
                StepApproval approval = (StepApproval) candidate;
                targets.addAll(collectSemanticTargets(approval, resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }

    private static Set<StepEntity> collectTargetsForAssignedUncertainty(
            int uncertaintyId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepGlobalUncertaintyAssignedContext
                    && ((StepGlobalUncertaintyAssignedContext) candidate).uncertainties().stream().anyMatch(uncertainty -> uncertainty.id() == uncertaintyId)) {
                StepGlobalUncertaintyAssignedContext context = (StepGlobalUncertaintyAssignedContext) candidate;
                targets.addAll(collectSemanticTargets(context, resolved, visiting));
            } else if (candidate instanceof StepGeometricRepresentationContext
                    && ((StepGeometricRepresentationContext) candidate).globalUncertaintyAssignedContext() != null
                    && ((StepGeometricRepresentationContext) candidate).globalUncertaintyAssignedContext().uncertainties().stream()
                            .anyMatch(uncertainty -> uncertainty.id() == uncertaintyId)) {
                StepGeometricRepresentationContext context = (StepGeometricRepresentationContext) candidate;
                targets.addAll(collectSemanticTargets(context, resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }

    private static Set<StepEntity> collectTargetsForDateRole(
            int roleId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepAppliedDateAssignment
                    && ((StepAppliedDateAssignment) candidate).role().id() == roleId) {
                StepAppliedDateAssignment assignment = (StepAppliedDateAssignment) candidate;
                targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }

    private static Set<StepEntity> collectTargetsForDateTimeRole(
            int roleId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepAppliedDateTimeAssignment
                    && ((StepAppliedDateTimeAssignment) candidate).role().id() == roleId) {
                StepAppliedDateTimeAssignment assignment = (StepAppliedDateTimeAssignment) candidate;
                targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }

    private static Set<StepEntity> collectTargetsForRepresentationContext(
            int contextId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepRepresentation
                    && ((StepRepresentation) candidate).context() != null
                    && ((StepRepresentation) candidate).context().id() == contextId) {
                StepRepresentation representation = (StepRepresentation) candidate;
                targets.add(representation);
            } else if (candidate instanceof StepPropertyDefinition
                    && ((StepPropertyDefinition) candidate).definition().id() == contextId) {
                StepPropertyDefinition propertyDefinition = (StepPropertyDefinition) candidate;
                targets.addAll(collectSemanticTargets(propertyDefinition, resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }

    private static Set<StepEntity> collectTargetsForApprovalRole(
            int roleId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepApprovalPersonOrganization
                    && ((StepApprovalPersonOrganization) candidate).role().id() == roleId) {
                StepApprovalPersonOrganization assignment = (StepApprovalPersonOrganization) candidate;
                targets.addAll(collectSemanticTargets(assignment, resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }

    private static Set<StepEntity> collectTargetsForAssignedUnit(
            int unitId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepGlobalUnitAssignedContext
                    && ((StepGlobalUnitAssignedContext) candidate).units().stream().anyMatch(unit -> unit.id() == unitId)) {
                StepGlobalUnitAssignedContext context = (StepGlobalUnitAssignedContext) candidate;
                targets.addAll(collectSemanticTargets(context, resolved, visiting));
            } else if (candidate instanceof StepGeometricRepresentationContext
                    && ((StepGeometricRepresentationContext) candidate).globalUnitAssignedContext() != null
                    && ((StepGeometricRepresentationContext) candidate).globalUnitAssignedContext().units().stream().anyMatch(unit -> unit.id() == unitId)) {
                StepGeometricRepresentationContext context = (StepGeometricRepresentationContext) candidate;
                targets.addAll(collectSemanticTargets(context, resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }

    private static Set<StepEntity> collectTargetsForCertificationType(
            int kindId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepCertification
                    && ((StepCertification) candidate).kind().id() == kindId) {
                StepCertification certification = (StepCertification) candidate;
                targets.addAll(collectSemanticTargets(certification, resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }

    private static Set<StepEntity> collectTargetsForClassificationRole(
            int roleId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepClassificationAssignment
                    && ((StepClassificationAssignment) candidate).role().id() == roleId) {
                StepClassificationAssignment assignment = (StepClassificationAssignment) candidate;
                targets.addAll(collectSemanticTargets(assignment, resolved, visiting));
            } else if (candidate instanceof StepAppliedClassificationAssignment
                    && ((StepAppliedClassificationAssignment) candidate).role().id() == roleId) {
                StepAppliedClassificationAssignment assignment = (StepAppliedClassificationAssignment) candidate;
                targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }

    private static Set<StepEntity> collectTargetsForContractType(
            int kindId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepContract
                    && ((StepContract) candidate).kind().id() == kindId) {
                StepContract contract = (StepContract) candidate;
                targets.addAll(collectSemanticTargets(contract, resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }

    private static Set<StepEntity> collectTargetsForCurveFont(
            int curveFontId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        return PreviewMaterialExporter.collectTargetsForCurveFont(curveFontId, resolved, visiting, StepPmiPayloadBuilder::collectSemanticTargets);
    }

    private static Set<StepEntity> collectTargetsForDocumentType(
            int kindId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepDocument
                    && ((StepDocument) candidate).kind().id() == kindId) {
                StepDocument document = (StepDocument) candidate;
                targets.addAll(collectSemanticTargets(document, resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }

    private static Set<StepEntity> collectTargetsForGlobalUncertaintyContext(
            int contextId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepGeometricRepresentationContext
                    && ((StepGeometricRepresentationContext) candidate).globalUncertaintyAssignedContext() != null
                    && ((StepGeometricRepresentationContext) candidate).globalUncertaintyAssignedContext().id() == contextId) {
                StepGeometricRepresentationContext context = (StepGeometricRepresentationContext) candidate;
                targets.addAll(collectSemanticTargets(context, resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }

    private static Set<StepEntity> collectTargetsForGlobalUnitContext(
            int contextId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepGeometricRepresentationContext
                    && ((StepGeometricRepresentationContext) candidate).globalUnitAssignedContext() != null
                    && ((StepGeometricRepresentationContext) candidate).globalUnitAssignedContext().id() == contextId) {
                StepGeometricRepresentationContext context = (StepGeometricRepresentationContext) candidate;
                targets.addAll(collectSemanticTargets(context, resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }

    private static Set<StepEntity> collectTargetsForIdentificationRole(
            int roleId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepIdentificationAssignment
                    && ((StepIdentificationAssignment) candidate).role().id() == roleId) {
                StepIdentificationAssignment assignment = (StepIdentificationAssignment) candidate;
                targets.addAll(collectSemanticTargets(assignment, resolved, visiting));
            } else if (candidate instanceof StepAppliedIdentificationAssignment
                    && ((StepAppliedIdentificationAssignment) candidate).role().id() == roleId) {
                StepAppliedIdentificationAssignment assignment = (StepAppliedIdentificationAssignment) candidate;
                targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
            } else if (candidate instanceof StepExternalIdentificationAssignment
                    && ((StepExternalIdentificationAssignment) candidate).role().id() == roleId) {
                StepExternalIdentificationAssignment assignment = (StepExternalIdentificationAssignment) candidate;
                targets.addAll(collectSemanticTargets(assignment, resolved, visiting));
            } else if (candidate instanceof StepAppliedExternalIdentificationAssignment
                    && ((StepAppliedExternalIdentificationAssignment) candidate).role().id() == roleId) {
                StepAppliedExternalIdentificationAssignment assignment = (StepAppliedExternalIdentificationAssignment) candidate;
                targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }

    private static Set<StepEntity> collectTargetsForItemDefinedTransformation(
            int transformationId,
            Map<Integer, StepEntity> resolved
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepRepresentationRelationshipWithTransformation
                    && ((StepRepresentationRelationshipWithTransformation) candidate).transformationOperator().id() == transformationId) {
                StepRepresentationRelationshipWithTransformation relationship = (StepRepresentationRelationshipWithTransformation) candidate;
                targets.addAll(collectRepresentationTargetsFromRelationship(relationship));
            }
        }
        return Set.copyOf(targets);
    }

    private static Set<StepEntity> collectTargetsForOccurrence(
            int occurrenceId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepProductDefinitionShape
                    && ((StepProductDefinitionShape) candidate).definition().id() == occurrenceId) {
                StepProductDefinitionShape shape = (StepProductDefinitionShape) candidate;
                targets.addAll(collectSemanticTargets(shape, resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }

    private static Set<StepEntity> collectTargetsForOrganizationRole(
            int roleId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepOrganizationAssignment
                    && ((StepOrganizationAssignment) candidate).role().id() == roleId) {
                StepOrganizationAssignment assignment = (StepOrganizationAssignment) candidate;
                targets.addAll(collectSemanticTargets(assignment, resolved, visiting));
            } else if (candidate instanceof StepAppliedOrganizationAssignment
                    && ((StepAppliedOrganizationAssignment) candidate).role().id() == roleId) {
                StepAppliedOrganizationAssignment assignment = (StepAppliedOrganizationAssignment) candidate;
                targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }

    private static Set<StepEntity> collectTargetsForPersonAndOrganizationRole(
            int roleId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepPersonAndOrganizationAssignment
                    && ((StepPersonAndOrganizationAssignment) candidate).role().id() == roleId) {
                StepPersonAndOrganizationAssignment assignment = (StepPersonAndOrganizationAssignment) candidate;
                targets.addAll(collectSemanticTargets(assignment, resolved, visiting));
            } else if (candidate instanceof StepAppliedPersonAndOrganizationAssignment
                    && ((StepAppliedPersonAndOrganizationAssignment) candidate).role().id() == roleId) {
                StepAppliedPersonAndOrganizationAssignment assignment = (StepAppliedPersonAndOrganizationAssignment) candidate;
                targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }

    private static Set<StepEntity> collectTargetsForPointMarker(
            int markerId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        return PreviewMaterialExporter.collectTargetsForPointMarker(markerId, resolved, visiting, StepPmiPayloadBuilder::collectSemanticTargets);
    }

    private static Set<StepEntity> collectTargetsForProductDefinition(
            int productDefinitionId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepProductDefinitionShape
                    && ((StepProductDefinitionShape) candidate).definition().id() == productDefinitionId) {
                StepProductDefinitionShape shape = (StepProductDefinitionShape) candidate;
                targets.addAll(collectSemanticTargets(shape, resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }

    private static Set<StepEntity> collectTargetsForSecurityLevel(
            int levelId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepSecurityClassification
                    && ((StepSecurityClassification) candidate).securityLevel().id() == levelId) {
                StepSecurityClassification classification = (StepSecurityClassification) candidate;
                targets.addAll(collectSemanticTargets(classification, resolved, visiting));
            }
        }
        return Set.copyOf(targets);
    }

    private static Set<StepEntity> collectTargetsForStyleColour(
            int colourId,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        return PreviewMaterialExporter.collectTargetsForStyleColour(colourId, resolved, visiting, StepPmiPayloadBuilder::collectSemanticTargets);
    }

    private static void appendPlacementDefinitionTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity placement,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        appendExistingRepresentationDefinitionTargets(
                targetsByUsageId,
                identifiedItem,
                placement,
                instanceIdsByTargetId
        );
        if (placement instanceof StepAxis1Placement) {
            StepAxis1Placement axis1Placement = (StepAxis1Placement) placement;
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    axis1Placement.location(),
                    instanceIdsByTargetId
            );
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    axis1Placement.axis(),
                    instanceIdsByTargetId
            );
        } else if (placement instanceof StepAxis2Placement2D) {
            StepAxis2Placement2D axis2Placement2D = (StepAxis2Placement2D) placement;
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    axis2Placement2D.location(),
                    instanceIdsByTargetId
            );
            if (axis2Placement2D.refDirection() != null) {
                appendExistingRepresentationDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        axis2Placement2D.refDirection(),
                        instanceIdsByTargetId
                );
            }
        } else if (placement instanceof StepAxis2Placement3D) {
            StepAxis2Placement3D axis2Placement3D = (StepAxis2Placement3D) placement;
            appendExistingRepresentationDefinitionTargets(
                    targetsByUsageId,
                    identifiedItem,
                    axis2Placement3D.location(),
                    instanceIdsByTargetId
            );
            if (axis2Placement3D.axis() != null) {
                appendExistingRepresentationDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        axis2Placement3D.axis(),
                        instanceIdsByTargetId
                );
            }
            if (axis2Placement3D.refDirection() != null) {
                appendExistingRepresentationDefinitionTargets(
                        targetsByUsageId,
                        identifiedItem,
                        axis2Placement3D.refDirection(),
                        instanceIdsByTargetId
                );
            }
        }
    }


    private static void appendPropertyRepresentationLinkTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepPropertyDefinition propertyDefinition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        appendPropertyRepresentationLinkTargets(
                targetsByUsageId,
                identifiedItem.id(),
                propertyDefinition,
                resolved,
                instanceIdsByTargetId
        );
        for (StepEntity candidate : resolved.values()) {
            StepRepresentation usedRepresentation = propertyRepresentationLinkRepresentation(candidate, propertyDefinition.id());
            if (usedRepresentation != null) {
                appendAttachedRepresentationRelationshipTargets(
                        targetsByUsageId,
                        identifiedItem,
                        usedRepresentation,
                        resolved,
                        instanceIdsByTargetId
                );
            }
        }
    }

    private static void appendPropertyRepresentationLinkTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            int usageId,
            StepPropertyDefinition propertyDefinition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        for (StepEntity candidate : resolved.values()) {
            if (candidate instanceof StepPropertyDefinitionRepresentation
                    && ((StepPropertyDefinitionRepresentation) candidate).definition().id() == propertyDefinition.id()) {
                StepPropertyDefinitionRepresentation representationLink = (StepPropertyDefinitionRepresentation) candidate;
                appendPmiTarget(
                        targetsByUsageId,
                        usageId,
                        representationLink.usedRepresentation(),
                        instanceIdsByTargetId,
                        null,
                        null,
                        null,
                        null,
                        definitionTypeName(representationLink),
                        representationLink.id()
                );
            } else if (candidate instanceof StepAttributeAssertion
                    && ((StepAttributeAssertion) candidate).definition().id() == propertyDefinition.id()) {
                StepAttributeAssertion representationLink = (StepAttributeAssertion) candidate;
                appendPmiTarget(
                        targetsByUsageId,
                        usageId,
                        representationLink.usedRepresentation(),
                        instanceIdsByTargetId,
                        null,
                        null,
                        null,
                        null,
                        definitionTypeName(representationLink),
                        representationLink.id()
                );
            } else if (candidate instanceof StepActionPropertyRepresentation
                    && ((StepActionPropertyRepresentation) candidate).definition().id() == propertyDefinition.id()) {
                StepActionPropertyRepresentation representationLink = (StepActionPropertyRepresentation) candidate;
                appendPmiTarget(
                        targetsByUsageId,
                        usageId,
                        representationLink.usedRepresentation(),
                        instanceIdsByTargetId,
                        null,
                        null,
                        null,
                        null,
                        definitionTypeName(representationLink),
                        representationLink.id()
                );
            } else if (candidate instanceof StepContactRatioRepresentation
                    && ((StepContactRatioRepresentation) candidate).definition().id() == propertyDefinition.id()) {
                StepContactRatioRepresentation representationLink = (StepContactRatioRepresentation) candidate;
                appendPmiTarget(
                        targetsByUsageId,
                        usageId,
                        representationLink.usedRepresentation(),
                        instanceIdsByTargetId,
                        null,
                        null,
                        null,
                        null,
                        definitionTypeName(representationLink),
                        representationLink.id()
                );
            } else if (candidate instanceof StepKinematicPropertyDefinitionRepresentation
                    && ((StepKinematicPropertyDefinitionRepresentation) candidate).definition().id() == propertyDefinition.id()) {
                StepKinematicPropertyDefinitionRepresentation representationLink = (StepKinematicPropertyDefinitionRepresentation) candidate;
                appendPmiTarget(
                        targetsByUsageId,
                        usageId,
                        representationLink.usedRepresentation(),
                        instanceIdsByTargetId,
                        null,
                        null,
                        null,
                        null,
                        definitionTypeName(representationLink),
                        representationLink.id()
                );
            } else if (candidate instanceof StepKinematicPropertyMechanismRepresentation
                    && ((StepKinematicPropertyMechanismRepresentation) candidate).definition().id() == propertyDefinition.id()) {
                StepKinematicPropertyMechanismRepresentation representationLink = (StepKinematicPropertyMechanismRepresentation) candidate;
                appendPmiTarget(
                        targetsByUsageId,
                        usageId,
                        representationLink.usedRepresentation(),
                        instanceIdsByTargetId,
                        null,
                        null,
                        null,
                        null,
                        definitionTypeName(representationLink),
                        representationLink.id()
                );
            } else if (candidate instanceof StepKinematicPropertyRepresentationRelation
                    && ((StepKinematicPropertyRepresentationRelation) candidate).definition().id() == propertyDefinition.id()) {
                StepKinematicPropertyRepresentationRelation representationLink = (StepKinematicPropertyRepresentationRelation) candidate;
                appendPmiTarget(
                        targetsByUsageId,
                        usageId,
                        representationLink.usedRepresentation(),
                        instanceIdsByTargetId,
                        null,
                        null,
                        null,
                        null,
                        definitionTypeName(representationLink),
                        representationLink.id()
                );
            } else if (candidate instanceof StepKinematicPropertyTopologyRepresentation
                    && ((StepKinematicPropertyTopologyRepresentation) candidate).definition().id() == propertyDefinition.id()) {
                StepKinematicPropertyTopologyRepresentation representationLink = (StepKinematicPropertyTopologyRepresentation) candidate;
                appendPmiTarget(
                        targetsByUsageId,
                        usageId,
                        representationLink.usedRepresentation(),
                        instanceIdsByTargetId,
                        null,
                        null,
                        null,
                        null,
                        definitionTypeName(representationLink),
                        representationLink.id()
                );
            } else if (candidate instanceof StepResourcePropertyRepresentation
                    && ((StepResourcePropertyRepresentation) candidate).definition().id() == propertyDefinition.id()) {
                StepResourcePropertyRepresentation representationLink = (StepResourcePropertyRepresentation) candidate;
                appendPmiTarget(
                        targetsByUsageId,
                        usageId,
                        representationLink.usedRepresentation(),
                        instanceIdsByTargetId,
                        null,
                        null,
                        null,
                        null,
                        definitionTypeName(representationLink),
                        representationLink.id()
                );
            } else if (candidate instanceof StepForwardChainingRulePremise
                    && ((StepForwardChainingRulePremise) candidate).definition().id() == propertyDefinition.id()) {
                StepForwardChainingRulePremise representationLink = (StepForwardChainingRulePremise) candidate;
                appendPmiTarget(
                        targetsByUsageId,
                        usageId,
                        representationLink.usedRepresentation(),
                        instanceIdsByTargetId,
                        null,
                        null,
                        null,
                        null,
                        definitionTypeName(representationLink),
                        representationLink.id()
                );
            } else if (candidate instanceof StepBackChainingRuleBody
                    && ((StepBackChainingRuleBody) candidate).definition().id() == propertyDefinition.id()) {
                StepBackChainingRuleBody representationLink = (StepBackChainingRuleBody) candidate;
                appendPmiTarget(
                        targetsByUsageId,
                        usageId,
                        representationLink.usedRepresentation(),
                        instanceIdsByTargetId,
                        null,
                        null,
                        null,
                        null,
                        definitionTypeName(representationLink),
                        representationLink.id()
                );
            } else if (candidate instanceof StepPlacedDatumTargetFeature
                    && ((StepPlacedDatumTargetFeature) candidate).definition().id() == propertyDefinition.id()) {
                StepPlacedDatumTargetFeature representationLink = (StepPlacedDatumTargetFeature) candidate;
                appendPmiTarget(
                        targetsByUsageId,
                        usageId,
                        representationLink.usedRepresentation(),
                        instanceIdsByTargetId,
                        null,
                        null,
                        null,
                        null,
                        definitionTypeName(representationLink),
                        representationLink.id()
                );
            }
        }
    }

    private static Set<StepEntity> collectSemanticTargets(
            StepEntity entity,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        if (entity == null || !visiting.add(entity.id())) {
            return Set.of();
        }
        Set<StepEntity> targets = new LinkedHashSet<>();
        if (entity instanceof StepFaceEntity
                || entity instanceof StepEdgeCurve
                || entity instanceof StepPath
                || entity instanceof StepOpenPath
                || entity instanceof StepSubpath
                || entity instanceof StepOrientedPath
                || entity instanceof StepConnectedEdgeSet
                || entity instanceof StepPointSet
                || entity instanceof StepGeometricSet
                || entity instanceof StepGeometricCurveSet
                || entity instanceof StepOpenShell
                || entity instanceof StepSurfacedOpenShell
                || entity instanceof StepOrientedOpenShell
                || entity instanceof StepClosedShell
                || entity instanceof StepOrientedClosedShell
                || entity instanceof StepWireShell
                || entity instanceof StepVertexShell
                || entity instanceof StepEdgeLoop
                || entity instanceof StepPolyLoop
                || entity instanceof StepConnectedFaceSet
                || entity instanceof StepConnectedFaceSubSet
                || entity instanceof StepFaceBasedSurfaceModel
                || entity instanceof StepShellBasedSurfaceModel
                || entity instanceof StepEdgeBasedWireframeModel
                || entity instanceof StepShellBasedWireframeModel
                || entity instanceof StepManifoldSolidBrep
                || entity instanceof StepBrepWithVoids
                || entity instanceof StepSweptAreaSolid
                || entity instanceof StepSolidReplica
                || entity instanceof StepCsgSolid
                || entity instanceof StepCsgPrimitive
                || entity instanceof StepBooleanResult
                || entity instanceof StepBooleanClippingResult
                || entity instanceof StepRepresentation) {
            targets.add(entity);
        }
        if (entity instanceof StepPropertyDefinition) {
            StepPropertyDefinition propertyDefinition = (StepPropertyDefinition) entity;
            targets.addAll(collectSemanticTargets(propertyDefinition.definition(), resolved, visiting));
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepPropertyDefinitionRepresentation
                    && ((StepPropertyDefinitionRepresentation) candidate).definition().id() == propertyDefinition.id()) {
                StepPropertyDefinitionRepresentation representationLink = (StepPropertyDefinitionRepresentation) candidate;
                targets.add(representationLink.usedRepresentation());
            } else if (candidate instanceof StepActionPropertyRepresentation
                    && ((StepActionPropertyRepresentation) candidate).definition().id() == propertyDefinition.id()) {
                StepActionPropertyRepresentation representationLink = (StepActionPropertyRepresentation) candidate;
                targets.add(representationLink.usedRepresentation());
            } else if (candidate instanceof StepContactRatioRepresentation
                    && ((StepContactRatioRepresentation) candidate).definition().id() == propertyDefinition.id()) {
                StepContactRatioRepresentation representationLink = (StepContactRatioRepresentation) candidate;
                targets.add(representationLink.usedRepresentation());
            } else if (candidate instanceof StepKinematicPropertyDefinitionRepresentation
                    && ((StepKinematicPropertyDefinitionRepresentation) candidate).definition().id() == propertyDefinition.id()) {
                StepKinematicPropertyDefinitionRepresentation representationLink = (StepKinematicPropertyDefinitionRepresentation) candidate;
                targets.add(representationLink.usedRepresentation());
            } else if (candidate instanceof StepKinematicPropertyMechanismRepresentation
                    && ((StepKinematicPropertyMechanismRepresentation) candidate).definition().id() == propertyDefinition.id()) {
                StepKinematicPropertyMechanismRepresentation representationLink = (StepKinematicPropertyMechanismRepresentation) candidate;
                targets.add(representationLink.usedRepresentation());
            } else if (candidate instanceof StepKinematicPropertyRepresentationRelation
                    && ((StepKinematicPropertyRepresentationRelation) candidate).definition().id() == propertyDefinition.id()) {
                StepKinematicPropertyRepresentationRelation representationLink = (StepKinematicPropertyRepresentationRelation) candidate;
                targets.add(representationLink.usedRepresentation());
            } else if (candidate instanceof StepKinematicPropertyTopologyRepresentation
                    && ((StepKinematicPropertyTopologyRepresentation) candidate).definition().id() == propertyDefinition.id()) {
                StepKinematicPropertyTopologyRepresentation representationLink = (StepKinematicPropertyTopologyRepresentation) candidate;
                targets.add(representationLink.usedRepresentation());
            } else if (candidate instanceof StepResourcePropertyRepresentation
                    && ((StepResourcePropertyRepresentation) candidate).definition().id() == propertyDefinition.id()) {
                StepResourcePropertyRepresentation representationLink = (StepResourcePropertyRepresentation) candidate;
                targets.add(representationLink.usedRepresentation());
            } else if (candidate instanceof StepForwardChainingRulePremise
                    && ((StepForwardChainingRulePremise) candidate).definition().id() == propertyDefinition.id()) {
                StepForwardChainingRulePremise representationLink = (StepForwardChainingRulePremise) candidate;
                targets.add(representationLink.usedRepresentation());
            } else if (candidate instanceof StepBackChainingRuleBody
                    && ((StepBackChainingRuleBody) candidate).definition().id() == propertyDefinition.id()) {
                StepBackChainingRuleBody representationLink = (StepBackChainingRuleBody) candidate;
                targets.add(representationLink.usedRepresentation());
            } else if (candidate instanceof StepPlacedDatumTargetFeature
                    && ((StepPlacedDatumTargetFeature) candidate).definition().id() == propertyDefinition.id()) {
                StepPlacedDatumTargetFeature datumTargetFeature = (StepPlacedDatumTargetFeature) candidate;
                targets.add(datumTargetFeature.usedRepresentation());
            } else if (candidate instanceof StepPropertyDefinitionRelationship) {
                StepPropertyDefinitionRelationship relationship = (StepPropertyDefinitionRelationship) candidate;
                if (relationship.relatingPropertyDefinition().id() == propertyDefinition.id()) {
                    targets.addAll(collectSemanticTargets(relationship.relatedPropertyDefinition(), resolved, visiting));
                }
                if (relationship.relatedPropertyDefinition().id() == propertyDefinition.id()) {
                    targets.addAll(collectSemanticTargets(relationship.relatingPropertyDefinition(), resolved, visiting));
                }
            }
        }
    } else if (entity instanceof StepDescriptiveRepresentationItem) {
            StepDescriptiveRepresentationItem item = (StepDescriptiveRepresentationItem) entity;
            targets.addAll(collectTargetsReferencingEntity(item.id(), resolved, visiting));
        } else if (entity instanceof StepValueRepresentationItem) {
            StepValueRepresentationItem item = (StepValueRepresentationItem) entity;
            targets.addAll(collectTargetsReferencingEntity(item.id(), resolved, visiting));
        } else if (entity instanceof StepMeasureRepresentationItem) {
            StepMeasureRepresentationItem item = (StepMeasureRepresentationItem) entity;
            targets.addAll(collectTargetsReferencingEntity(item.id(), resolved, visiting));
            targets.addAll(collectSemanticTargets(item.unit(), resolved, visiting));
        } else if (entity instanceof StepMeasureWithUnit) {
            StepMeasureWithUnit measure = (StepMeasureWithUnit) entity;
            targets.addAll(collectSemanticTargets(measure.unitComponent(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(measure.id(), resolved, visiting));
        } else if (entity instanceof StepTypedMeasureWithUnit) {
            StepTypedMeasureWithUnit measure = (StepTypedMeasureWithUnit) entity;
            targets.addAll(collectSemanticTargets(measure.unitComponent(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(measure.id(), resolved, visiting));
        } else if (entity instanceof StepUncertaintyMeasureWithUnit) {
            StepUncertaintyMeasureWithUnit measure = (StepUncertaintyMeasureWithUnit) entity;
            targets.addAll(collectSemanticTargets(measure.unitComponent(), resolved, visiting));
            targets.addAll(collectTargetsForAssignedUncertainty(measure.id(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(measure.id(), resolved, visiting));
        } else if (entity instanceof StepCartesianPoint) {
            StepCartesianPoint point = (StepCartesianPoint) entity;
            targets.addAll(collectTargetsReferencingEntity(point.id(), resolved, visiting));
        } else if (entity instanceof StepDirection) {
            StepDirection direction = (StepDirection) entity;
            targets.addAll(collectTargetsReferencingEntity(direction.id(), resolved, visiting));
        } else if (entity instanceof StepVector) {
            StepVector vector = (StepVector) entity;
            targets.addAll(collectSemanticTargets(vector.orientation(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(vector.id(), resolved, visiting));
        } else if (entity instanceof StepAxis1Placement) {
            StepAxis1Placement placement = (StepAxis1Placement) entity;
            targets.addAll(collectSemanticTargets(placement.location(), resolved, visiting));
            targets.addAll(collectSemanticTargets(placement.axis(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(placement.id(), resolved, visiting));
        } else if (entity instanceof StepAxis2Placement2D) {
            StepAxis2Placement2D placement = (StepAxis2Placement2D) entity;
            targets.addAll(collectSemanticTargets(placement.location(), resolved, visiting));
            if (placement.refDirection() != null) {
                targets.addAll(collectSemanticTargets(placement.refDirection(), resolved, visiting));
            }
            targets.addAll(collectTargetsReferencingEntity(placement.id(), resolved, visiting));
        } else if (entity instanceof StepAxis2Placement3D) {
            StepAxis2Placement3D placement = (StepAxis2Placement3D) entity;
            targets.addAll(collectSemanticTargets(placement.location(), resolved, visiting));
            if (placement.axis() != null) {
                targets.addAll(collectSemanticTargets(placement.axis(), resolved, visiting));
            }
            if (placement.refDirection() != null) {
                targets.addAll(collectSemanticTargets(placement.refDirection(), resolved, visiting));
            }
            targets.addAll(collectTargetsReferencingEntity(placement.id(), resolved, visiting));
        } else if (entity instanceof StepAddress) {
            StepAddress address = (StepAddress) entity;
            targets.addAll(collectTargetsReferencingEntity(address.id(), resolved, visiting));
        } else if (entity instanceof StepCharacterizedObject) {
            StepCharacterizedObject characterizedObject = (StepCharacterizedObject) entity;
            targets.addAll(collectTargetsReferencingEntity(characterizedObject.id(), resolved, visiting));
        } else if (entity instanceof StepPoint) {
            StepPoint point = (StepPoint) entity;
            targets.addAll(collectTargetsReferencingEntity(point.id(), resolved, visiting));
        } else if (entity instanceof StepPointSet) {
            StepPointSet pointSet = (StepPointSet) entity;
            targets.addAll(collectSemanticTargets(pointSet.points(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(pointSet.id(), resolved, visiting));
        } else if (entity instanceof StepPolyline) {
            StepPolyline polyline = (StepPolyline) entity;
            targets.addAll(collectSemanticTargets(polyline.points(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(polyline.id(), resolved, visiting));
        } else if (entity instanceof StepProfileDef) {
            StepProfileDef profile = (StepProfileDef) entity;
            if (profile.position() != null) {
                targets.addAll(collectSemanticTargets(profile.position(), resolved, visiting));
            }
            targets.addAll(collectSemanticTargets(profile.curves(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(profile.id(), resolved, visiting));
        } else if (entity instanceof StepLine) {
            StepLine line = (StepLine) entity;
            targets.addAll(collectSemanticTargets(line.point(), resolved, visiting));
            targets.addAll(collectSemanticTargets(line.vector(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(line.id(), resolved, visiting));
        } else if (entity instanceof StepCircle) {
            StepCircle circle = (StepCircle) entity;
            targets.addAll(collectSemanticTargets(circle.position(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(circle.id(), resolved, visiting));
        } else if (entity instanceof StepEllipse) {
            StepEllipse ellipse = (StepEllipse) entity;
            targets.addAll(collectSemanticTargets(ellipse.position(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(ellipse.id(), resolved, visiting));
        } else if (entity instanceof StepCurve) {
            StepCurve curve = (StepCurve) entity;
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepBoundedCurve) {
            StepBoundedCurve curve = (StepBoundedCurve) entity;
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepConicCurve) {
            StepConicCurve curve = (StepConicCurve) entity;
            targets.addAll(collectSemanticTargets(curve.position(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepBSplineCurve) {
            StepBSplineCurve curve = (StepBSplineCurve) entity;
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepBezierCurve) {
            StepBezierCurve curve = (StepBezierCurve) entity;
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepBSplineCurveWithKnots) {
            StepBSplineCurveWithKnots curve = (StepBSplineCurveWithKnots) entity;
            targets.addAll(collectSemanticTargets(curve.controlPoints(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepRationalBSplineCurve) {
            StepRationalBSplineCurve curve = (StepRationalBSplineCurve) entity;
            targets.addAll(collectSemanticTargets(curve.controlPoints(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepPiecewiseBezierCurve) {
            StepPiecewiseBezierCurve curve = (StepPiecewiseBezierCurve) entity;
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepUniformCurve) {
            StepUniformCurve curve = (StepUniformCurve) entity;
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepQuasiUniformCurve) {
            StepQuasiUniformCurve curve = (StepQuasiUniformCurve) entity;
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepOffsetCurve2D) {
            StepOffsetCurve2D curve = (StepOffsetCurve2D) entity;
            targets.addAll(collectSemanticTargets(curve.basisCurve(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepOffsetCurve3D) {
            StepOffsetCurve3D curve = (StepOffsetCurve3D) entity;
            targets.addAll(collectSemanticTargets(curve.basisCurve(), resolved, visiting));
            targets.addAll(collectSemanticTargets(curve.refDirection(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepOrientedCurve) {
            StepOrientedCurve curve = (StepOrientedCurve) entity;
            targets.addAll(collectSemanticTargets(curve.curveElement(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepTrimmedCurve) {
            StepTrimmedCurve curve = (StepTrimmedCurve) entity;
            targets.addAll(collectSemanticTargets(curve.basisCurve(), resolved, visiting));
            for (StepValue trim : curve.trim1()) {
                if (trim instanceof StepValue.ReferenceValue && resolved.containsKey(((StepValue.ReferenceValue) trim).id())) {
                    StepValue.ReferenceValue ref = (StepValue.ReferenceValue) trim;
                    targets.addAll(collectSemanticTargets(resolved.get(ref.id()), resolved, visiting));
                }
            }
            for (StepValue trim : curve.trim2()) {
                if (trim instanceof StepValue.ReferenceValue && resolved.containsKey(((StepValue.ReferenceValue) trim).id())) {
                    StepValue.ReferenceValue ref = (StepValue.ReferenceValue) trim;
                    targets.addAll(collectSemanticTargets(resolved.get(ref.id()), resolved, visiting));
                }
            }
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepSurfaceCurve) {
            StepSurfaceCurve curve = (StepSurfaceCurve) entity;
            targets.addAll(collectSemanticTargets(curve.curve3d(), resolved, visiting));
            targets.addAll(collectSemanticTargets(curve.associatedGeometry(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepSeamCurve) {
            StepSeamCurve curve = (StepSeamCurve) entity;
            targets.addAll(collectSemanticTargets(curve.curve3d(), resolved, visiting));
            targets.addAll(collectSemanticTargets(curve.associatedGeometry(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepPcurve) {
            StepPcurve curve = (StepPcurve) entity;
            targets.addAll(collectSemanticTargets(curve.basisSurface(), resolved, visiting));
            targets.addAll(collectSemanticTargets(curve.referenceToCurve(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepCompositeCurve) {
            StepCompositeCurve curve = (StepCompositeCurve) entity;
            targets.addAll(collectSemanticTargets(curve.segments(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepCompositeCurveOnSurface) {
            StepCompositeCurveOnSurface curve = (StepCompositeCurveOnSurface) entity;
            targets.addAll(collectSemanticTargets(curve.segments(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepCompositeCurveSegment) {
            StepCompositeCurveSegment segment = (StepCompositeCurveSegment) entity;
            targets.addAll(collectSemanticTargets(segment.parentCurve(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(segment.id(), resolved, visiting));
        } else if (entity instanceof StepSurface) {
            StepSurface surface = (StepSurface) entity;
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepBoundedSurface) {
            StepBoundedSurface surface = (StepBoundedSurface) entity;
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepBSplineSurface) {
            StepBSplineSurface surface = (StepBSplineSurface) entity;
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepBezierSurface) {
            StepBezierSurface surface = (StepBezierSurface) entity;
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepBSplineSurfaceWithKnots) {
            StepBSplineSurfaceWithKnots surface = (StepBSplineSurfaceWithKnots) entity;
            targets.addAll(collectSemanticTargets(surface.controlPoints().stream().flatMap(List::stream).collect(Collectors.toList()),
                    resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepRationalBSplineSurface) {
            StepRationalBSplineSurface surface = (StepRationalBSplineSurface) entity;
            targets.addAll(collectSemanticTargets(surface.controlPoints().stream().flatMap(List::stream).collect(Collectors.toList()),
                    resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepPiecewiseBezierSurface) {
            StepPiecewiseBezierSurface surface = (StepPiecewiseBezierSurface) entity;
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepUniformSurface) {
            StepUniformSurface surface = (StepUniformSurface) entity;
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepQuasiUniformSurface) {
            StepQuasiUniformSurface surface = (StepQuasiUniformSurface) entity;
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepPlane) {
            StepPlane plane = (StepPlane) entity;
            targets.addAll(collectSemanticTargets(plane.position(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(plane.id(), resolved, visiting));
        } else if (entity instanceof StepCylindricalSurface) {
            StepCylindricalSurface surface = (StepCylindricalSurface) entity;
            targets.addAll(collectSemanticTargets(surface.position(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepConicalSurface) {
            StepConicalSurface surface = (StepConicalSurface) entity;
            targets.addAll(collectSemanticTargets(surface.position(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepToroidalSurface) {
            StepToroidalSurface surface = (StepToroidalSurface) entity;
            targets.addAll(collectSemanticTargets(surface.position(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepSurfaceOfLinearExtrusion) {
            StepSurfaceOfLinearExtrusion surface = (StepSurfaceOfLinearExtrusion) entity;
            targets.addAll(collectSemanticTargets(surface.sweptCurve(), resolved, visiting));
            targets.addAll(collectSemanticTargets(surface.extrusionAxis(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepSurfaceOfRevolution) {
            StepSurfaceOfRevolution surface = (StepSurfaceOfRevolution) entity;
            targets.addAll(collectSemanticTargets(surface.sweptCurve(), resolved, visiting));
            targets.addAll(collectSemanticTargets(surface.axisPosition(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepRectangularTrimmedSurface) {
            StepRectangularTrimmedSurface surface = (StepRectangularTrimmedSurface) entity;
            targets.addAll(collectSemanticTargets(surface.basisSurface(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepCurveBoundedSurface) {
            StepCurveBoundedSurface surface = (StepCurveBoundedSurface) entity;
            targets.addAll(collectSemanticTargets(surface.basisSurface(), resolved, visiting));
            targets.addAll(collectSemanticTargets(surface.boundaries(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepOrientedSurface) {
            StepOrientedSurface surface = (StepOrientedSurface) entity;
            targets.addAll(collectSemanticTargets(surface.surfaceElement(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepOffsetSurface) {
            StepOffsetSurface surface = (StepOffsetSurface) entity;
            targets.addAll(collectSemanticTargets(surface.basisSurface(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepSphericalSurface) {
            StepSphericalSurface surface = (StepSphericalSurface) entity;
            targets.addAll(collectSemanticTargets(surface.position(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepDegenerateToroidalSurface) {
            StepDegenerateToroidalSurface surface = (StepDegenerateToroidalSurface) entity;
            targets.addAll(collectSemanticTargets(surface.position(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(surface.id(), resolved, visiting));
        } else if (entity instanceof StepShellBasedSurfaceModel) {
            StepShellBasedSurfaceModel model = (StepShellBasedSurfaceModel) entity;
            targets.addAll(collectSemanticTargets(model.shells(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(model.id(), resolved, visiting));
        } else if (entity instanceof StepFaceBasedSurfaceModel) {
            StepFaceBasedSurfaceModel model = (StepFaceBasedSurfaceModel) entity;
            targets.addAll(collectSemanticTargets(model.faceSets(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(model.id(), resolved, visiting));
        } else if (entity instanceof StepSurfaceModel) {
            StepSurfaceModel model = (StepSurfaceModel) entity;
            targets.addAll(collectTargetsReferencingEntity(model.id(), resolved, visiting));
        } else if (entity instanceof StepSolidModel) {
            StepSolidModel model = (StepSolidModel) entity;
            targets.addAll(collectTargetsReferencingEntity(model.id(), resolved, visiting));
        } else if (entity instanceof StepGeometricCurveSet) {
            StepGeometricCurveSet set = (StepGeometricCurveSet) entity;
            targets.addAll(collectSemanticTargets(set.elements(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(set.id(), resolved, visiting));
        } else if (entity instanceof StepGeometricSet) {
            StepGeometricSet set = (StepGeometricSet) entity;
            targets.addAll(collectSemanticTargets(set.elements(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(set.id(), resolved, visiting));
        } else if (entity instanceof StepBoxDomain) {
            StepBoxDomain boxDomain = (StepBoxDomain) entity;
            targets.addAll(collectSemanticTargets(boxDomain.corner(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(boxDomain.id(), resolved, visiting));
        } else if (entity instanceof StepDimensionalExponents) {
            StepDimensionalExponents exponents = (StepDimensionalExponents) entity;
            targets.addAll(collectTargetsReferencingEntity(exponents.id(), resolved, visiting));
        } else if (entity instanceof StepDegeneratePcurve) {
            StepDegeneratePcurve curve = (StepDegeneratePcurve) entity;
            targets.addAll(collectSemanticTargets(curve.basisSurface(), resolved, visiting));
            targets.addAll(collectSemanticTargets(curve.referenceToCurve(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(curve.id(), resolved, visiting));
        } else if (entity instanceof StepHalfSpaceSolid) {
            StepHalfSpaceSolid halfSpaceSolid = (StepHalfSpaceSolid) entity;
            targets.addAll(collectSemanticTargets(halfSpaceSolid.baseSurface(), resolved, visiting));
            if (halfSpaceSolid.enclosure() != null) {
                targets.addAll(collectSemanticTargets(halfSpaceSolid.enclosure(), resolved, visiting));
            }
            targets.addAll(collectTargetsReferencingEntity(halfSpaceSolid.id(), resolved, visiting));
        } else if (entity instanceof StepVertex) {
            StepVertex vertex = (StepVertex) entity;
            targets.addAll(collectTargetsReferencingEntity(vertex.id(), resolved, visiting));
        } else if (entity instanceof StepVertexPoint) {
            StepVertexPoint vertexPoint = (StepVertexPoint) entity;
            targets.addAll(collectSemanticTargets(vertexPoint.point(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(vertexPoint.id(), resolved, visiting));
        } else if (entity instanceof StepEdge) {
            StepEdge edge = (StepEdge) entity;
            targets.addAll(collectTargetsReferencingEntity(edge.id(), resolved, visiting));
        } else if (entity instanceof StepConnectedEdgeSet) {
            StepConnectedEdgeSet edgeSet = (StepConnectedEdgeSet) entity;
            targets.addAll(collectSemanticTargets(edgeSet.edges(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(edgeSet.id(), resolved, visiting));
        } else if (entity instanceof StepEdgeBasedWireframeModel) {
            StepEdgeBasedWireframeModel model = (StepEdgeBasedWireframeModel) entity;
            targets.addAll(collectSemanticTargets(model.boundaries(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(model.id(), resolved, visiting));
        } else if (entity instanceof StepPolyLoop) {
            StepPolyLoop loop = (StepPolyLoop) entity;
            targets.addAll(collectSemanticTargets(loop.polygon(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(loop.id(), resolved, visiting));
        } else if (entity instanceof StepLoop) {
            StepLoop loop = (StepLoop) entity;
            targets.addAll(collectTargetsReferencingEntity(loop.id(), resolved, visiting));
        } else if (entity instanceof StepEdgeLoop) {
            StepEdgeLoop loop = (StepEdgeLoop) entity;
            targets.addAll(collectSemanticTargets(loop.edges(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(loop.id(), resolved, visiting));
        } else if (entity instanceof StepVertexLoop) {
            StepVertexLoop loop = (StepVertexLoop) entity;
            targets.addAll(collectSemanticTargets(loop.loopVertex(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(loop.id(), resolved, visiting));
        } else if (entity instanceof com.minicad.step.model.StepFaceBound) {
            com.minicad.step.model.StepFaceBound faceBound = (com.minicad.step.model.StepFaceBound) entity;
            targets.addAll(collectSemanticTargets(faceBound.loop(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(faceBound.id(), resolved, visiting));
        } else if (entity instanceof StepFace) {
            StepFace face = (StepFace) entity;
            targets.addAll(collectTargetsReferencingEntity(face.id(), resolved, visiting));
        } else if (entity instanceof StepAdvancedFace) {
            StepAdvancedFace face = (StepAdvancedFace) entity;
            targets.addAll(collectSemanticTargets(face.bounds(), resolved, visiting));
            targets.addAll(collectSemanticTargets(face.faceGeometry(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(face.id(), resolved, visiting));
        } else if (entity instanceof StepFaceSurface) {
            StepFaceSurface face = (StepFaceSurface) entity;
            targets.addAll(collectSemanticTargets(face.bounds(), resolved, visiting));
            targets.addAll(collectSemanticTargets(face.faceGeometry(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(face.id(), resolved, visiting));
        } else if (entity instanceof StepOpenShell) {
            StepOpenShell shell = (StepOpenShell) entity;
            targets.addAll(collectSemanticTargets(shell.faces(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(shell.id(), resolved, visiting));
        } else if (entity instanceof StepSurfacedOpenShell) {
            StepSurfacedOpenShell shell = (StepSurfacedOpenShell) entity;
            targets.addAll(collectSemanticTargets(shell.faces(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(shell.id(), resolved, visiting));
        } else if (entity instanceof StepOrientedOpenShell) {
            StepOrientedOpenShell shell = (StepOrientedOpenShell) entity;
            targets.addAll(collectSemanticTargets(shell.openShellElement(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(shell.id(), resolved, visiting));
        } else if (entity instanceof StepClosedShell) {
            StepClosedShell shell = (StepClosedShell) entity;
            targets.addAll(collectSemanticTargets(shell.faces(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(shell.id(), resolved, visiting));
        } else if (entity instanceof StepOrientedClosedShell) {
            StepOrientedClosedShell shell = (StepOrientedClosedShell) entity;
            targets.addAll(collectSemanticTargets(shell.closedShellElement(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(shell.id(), resolved, visiting));
        } else if (entity instanceof StepConnectedFaceSet) {
            StepConnectedFaceSet faceSet = (StepConnectedFaceSet) entity;
            targets.addAll(collectSemanticTargets(faceSet.faces(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(faceSet.id(), resolved, visiting));
        } else if (entity instanceof StepConnectedFaceSubSet) {
            StepConnectedFaceSubSet faceSet = (StepConnectedFaceSubSet) entity;
            targets.addAll(collectSemanticTargets(faceSet.faces(), resolved, visiting));
            targets.addAll(collectSemanticTargets(faceSet.parentFaceSet(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(faceSet.id(), resolved, visiting));
        } else if (entity instanceof StepOrientedEdge) {
            StepOrientedEdge edge = (StepOrientedEdge) entity;
            targets.addAll(collectSemanticTargets(edge.edgeElement(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(edge.id(), resolved, visiting));
        } else if (entity instanceof StepOrientedFace) {
            StepOrientedFace face = (StepOrientedFace) entity;
            targets.addAll(collectSemanticTargets(face.faceElement(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(face.id(), resolved, visiting));
        } else if (entity instanceof StepPath) {
            StepPath path = (StepPath) entity;
            targets.addAll(collectSemanticTargets(path.edges(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(path.id(), resolved, visiting));
        } else if (entity instanceof StepOpenPath) {
            StepOpenPath path = (StepOpenPath) entity;
            targets.addAll(collectSemanticTargets(path.edges(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(path.id(), resolved, visiting));
        } else if (entity instanceof StepSubpath) {
            StepSubpath subpath = (StepSubpath) entity;
            targets.addAll(collectSemanticTargets(subpath.edges(), resolved, visiting));
            targets.addAll(collectSemanticTargets(subpath.parentPath(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(subpath.id(), resolved, visiting));
        } else if (entity instanceof StepOrientedPath) {
            StepOrientedPath path = (StepOrientedPath) entity;
            targets.addAll(collectSemanticTargets(path.pathElement(), resolved, visiting));
            targets.addAll(collectSemanticTargets(path.edges(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(path.id(), resolved, visiting));
        } else if (entity instanceof StepWireShell) {
            StepWireShell wireShell = (StepWireShell) entity;
            targets.addAll(collectSemanticTargets(wireShell.loops(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(wireShell.id(), resolved, visiting));
        } else if (entity instanceof StepVertexShell) {
            StepVertexShell vertexShell = (StepVertexShell) entity;
            targets.addAll(collectSemanticTargets(vertexShell.extent(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(vertexShell.id(), resolved, visiting));
        } else if (entity instanceof StepShellBasedWireframeModel) {
            StepShellBasedWireframeModel model = (StepShellBasedWireframeModel) entity;
            targets.addAll(collectSemanticTargets(model.boundaries(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(model.id(), resolved, visiting));
        } else if (entity instanceof StepSubedge) {
            StepSubedge subedge = (StepSubedge) entity;
            targets.addAll(collectSemanticTargets(subedge.start(), resolved, visiting));
            targets.addAll(collectSemanticTargets(subedge.end(), resolved, visiting));
            targets.addAll(collectSemanticTargets(subedge.parentEdge(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(subedge.id(), resolved, visiting));
        } else if (entity instanceof StepCartesianTransformationOperator) {
            StepCartesianTransformationOperator transformation = (StepCartesianTransformationOperator) entity;
            if (transformation.axis1() != null) {
                targets.addAll(collectSemanticTargets(transformation.axis1(), resolved, visiting));
            }
            if (transformation.axis2() != null) {
                targets.addAll(collectSemanticTargets(transformation.axis2(), resolved, visiting));
            }
            if (transformation.axis3() != null) {
                targets.addAll(collectSemanticTargets(transformation.axis3(), resolved, visiting));
            }
            targets.addAll(collectSemanticTargets(transformation.localOrigin(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(transformation.id(), resolved, visiting));
        } else if (entity instanceof StepGeometricReplica) {
            StepGeometricReplica replica = (StepGeometricReplica) entity;
            targets.addAll(collectSemanticTargets(replica.parent(), resolved, visiting));
            targets.addAll(collectSemanticTargets(replica.transformation(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(replica.id(), resolved, visiting));
        } else if (entity instanceof StepSweptAreaSolid) {
            StepSweptAreaSolid solid = (StepSweptAreaSolid) entity;
            targets.addAll(collectSemanticTargets(solid.sweptArea(), resolved, visiting));
            targets.addAll(collectSemanticTargets(solid.position(), resolved, visiting));
            targets.addAll(collectSemanticTargets(solid.sweepReference(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(solid.id(), resolved, visiting));
        } else if (entity instanceof StepSweptDiskSolid) {
            StepSweptDiskSolid solid = (StepSweptDiskSolid) entity;
            targets.addAll(collectSemanticTargets(solid.sweptCurve(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(solid.id(), resolved, visiting));
        } else if (entity instanceof StepComplexClippingResult) {
            StepComplexClippingResult solid = (StepComplexClippingResult) entity;
            targets.addAll(collectSemanticTargets(solid.firstOperand(), resolved, visiting));
            targets.addAll(collectSemanticTargets(solid.secondOperand(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(solid.id(), resolved, visiting));
        } else if (entity instanceof StepSolidReplica) {
            StepSolidReplica solid = (StepSolidReplica) entity;
            targets.addAll(collectSemanticTargets(solid.parentSolid(), resolved, visiting));
            targets.addAll(collectSemanticTargets(solid.transformation(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(solid.id(), resolved, visiting));
        } else if (entity instanceof StepManifoldSolidBrep) {
            StepManifoldSolidBrep solid = (StepManifoldSolidBrep) entity;
            targets.addAll(collectSemanticTargets(solid.outer(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(solid.id(), resolved, visiting));
        } else if (entity instanceof StepBrepWithVoids) {
            StepBrepWithVoids solid = (StepBrepWithVoids) entity;
            targets.addAll(collectSemanticTargets(solid.outer(), resolved, visiting));
            targets.addAll(collectSemanticTargets(solid.voids(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(solid.id(), resolved, visiting));
        } else if (entity instanceof StepBooleanClippingResult) {
            StepBooleanClippingResult result = (StepBooleanClippingResult) entity;
            targets.addAll(collectSemanticTargets(result.firstOperand(), resolved, visiting));
            targets.addAll(collectSemanticTargets(result.secondOperand(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(result.id(), resolved, visiting));
        } else if (entity instanceof StepBooleanResult) {
            StepBooleanResult result = (StepBooleanResult) entity;
            targets.addAll(collectSemanticTargets(result.firstOperand(), resolved, visiting));
            targets.addAll(collectSemanticTargets(result.secondOperand(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(result.id(), resolved, visiting));
        } else if (entity instanceof StepCsgSolid) {
            StepCsgSolid solid = (StepCsgSolid) entity;
            targets.addAll(collectSemanticTargets(solid.treeRootExpression(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(solid.id(), resolved, visiting));
        } else if (entity instanceof StepCsgPrimitive) {
            StepCsgPrimitive primitive = (StepCsgPrimitive) entity;
            targets.addAll(collectSemanticTargets(primitive.position(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(primitive.id(), resolved, visiting));
        } else if (entity instanceof StepRepresentationContext) {
            StepRepresentationContext context = (StepRepresentationContext) entity;
            targets.addAll(collectTargetsForRepresentationContext(context.id(), resolved, visiting));
        } else if (entity instanceof StepGeometricRepresentationContext) {
            StepGeometricRepresentationContext context = (StepGeometricRepresentationContext) entity;
            if (context.globalUnitAssignedContext() != null) {
                targets.addAll(collectSemanticTargets(context.globalUnitAssignedContext(), resolved, visiting));
            }
            if (context.globalUncertaintyAssignedContext() != null) {
                targets.addAll(collectSemanticTargets(context.globalUncertaintyAssignedContext(), resolved, visiting));
            }
            targets.addAll(collectTargetsForRepresentationContext(context.id(), resolved, visiting));
        } else if (entity instanceof StepAbstractVariable) {
            StepAbstractVariable variable = (StepAbstractVariable) entity;
            targets.add(variable.usedRepresentation());
            targets.addAll(collectSemanticTargets(variable.definition(), resolved, visiting));
        } else if (entity instanceof StepScalarVariable) {
            StepScalarVariable variable = (StepScalarVariable) entity;
            targets.add(variable.usedRepresentation());
            targets.addAll(collectSemanticTargets(variable.definition(), resolved, visiting));
        } else if (entity instanceof StepRowVariable) {
            StepRowVariable variable = (StepRowVariable) entity;
            targets.add(variable.usedRepresentation());
            targets.addAll(collectSemanticTargets(variable.definition(), resolved, visiting));
        } else if (entity instanceof StepForwardChainingRulePremise) {
            StepForwardChainingRulePremise variable = (StepForwardChainingRulePremise) entity;
            targets.add(variable.usedRepresentation());
            targets.addAll(collectSemanticTargets(variable.definition(), resolved, visiting));
        } else if (entity instanceof StepBackChainingRuleBody) {
            StepBackChainingRuleBody variable = (StepBackChainingRuleBody) entity;
            targets.add(variable.usedRepresentation());
            targets.addAll(collectSemanticTargets(variable.definition(), resolved, visiting));
        } else if (entity instanceof StepApplicationContext) {
            StepApplicationContext applicationContext = (StepApplicationContext) entity;
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepApplicationProtocolDefinition
                        && ((StepApplicationProtocolDefinition) candidate).application().id() == applicationContext.id()) {
                    StepApplicationProtocolDefinition protocolDefinition = (StepApplicationProtocolDefinition) candidate;
                    targets.addAll(collectSemanticTargets(protocolDefinition, resolved, visiting));
                } else if (candidate instanceof StepProductContext
                        && ((StepProductContext) candidate).frameOfReference().id() == applicationContext.id()) {
                    StepProductContext productContext = (StepProductContext) candidate;
                    targets.addAll(collectSemanticTargets(productContext, resolved, visiting));
                } else if (candidate instanceof StepProductDefinitionContext
                        && ((StepProductDefinitionContext) candidate).frameOfReference().id() == applicationContext.id()) {
                    StepProductDefinitionContext productDefinitionContext = (StepProductDefinitionContext) candidate;
                    targets.addAll(collectSemanticTargets(productDefinitionContext, resolved, visiting));
                }
            }
        } else if (entity instanceof StepApplicationProtocolDefinition) {
            StepApplicationProtocolDefinition protocolDefinition = (StepApplicationProtocolDefinition) entity;
            targets.addAll(collectSemanticTargets(protocolDefinition.application(), resolved, visiting));
        } else if (entity instanceof StepProductContext) {
            StepProductContext productContext = (StepProductContext) entity;
            targets.addAll(collectSemanticTargets(productContext.frameOfReference(), resolved, visiting));
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepProduct
                        && ((StepProduct) candidate).frameOfReference().stream().anyMatch(context -> context.id() == productContext.id())) {
                    StepProduct product = (StepProduct) candidate;
                    targets.addAll(collectSemanticTargets(product, resolved, visiting));
                }
            }
        } else if (entity instanceof StepProductDefinitionContext) {
            StepProductDefinitionContext productDefinitionContext = (StepProductDefinitionContext) entity;
            targets.addAll(collectSemanticTargets(productDefinitionContext.frameOfReference(), resolved, visiting));
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepProductDefinition
                        && ((StepProductDefinition) candidate).frameOfReference().id() == productDefinitionContext.id()) {
                    StepProductDefinition productDefinition = (StepProductDefinition) candidate;
                    targets.addAll(collectSemanticTargets(productDefinition, resolved, visiting));
                }
            }
        } else if (entity instanceof StepGeneralProperty) {
            StepGeneralProperty generalProperty = (StepGeneralProperty) entity;
            targets.addAll(collectTargetsReferencingEntity(generalProperty.id(), resolved, visiting));
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepGeneralPropertyRelationship) {
                    StepGeneralPropertyRelationship relationship = (StepGeneralPropertyRelationship) candidate;
                    if (relationship.relatingGeneralProperty().id() == generalProperty.id()) {
                        targets.addAll(collectSemanticTargets(relationship.relatedGeneralProperty(), resolved, visiting));
                    }
                    if (relationship.relatedGeneralProperty().id() == generalProperty.id()) {
                        targets.addAll(collectSemanticTargets(relationship.relatingGeneralProperty(), resolved, visiting));
                    }
                }
            }
        } else if (entity instanceof StepDocument) {
            StepDocument document = (StepDocument) entity;
            targets.addAll(collectTargetsReferencingEntity(document.id(), resolved, visiting));
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepDocumentReference
                        && ((StepDocumentReference) candidate).assignedDocument().id() == document.id()) {
                    StepDocumentReference reference = (StepDocumentReference) candidate;
                    targets.addAll(collectSemanticTargets(reference, resolved, visiting));
                } else if (candidate instanceof StepAppliedDocumentReference
                        && ((StepAppliedDocumentReference) candidate).assignedDocument().id() == document.id()) {
                    StepAppliedDocumentReference reference = (StepAppliedDocumentReference) candidate;
                    targets.addAll(collectSemanticTargets(reference, resolved, visiting));
                } else if (candidate instanceof StepDocumentRelationship) {
                    StepDocumentRelationship relationship = (StepDocumentRelationship) candidate;
                    if (relationship.relatingDocument().id() == document.id()) {
                        targets.addAll(collectSemanticTargets(relationship.relatedDocument(), resolved, visiting));
                    }
                    if (relationship.relatedDocument().id() == document.id()) {
                        targets.addAll(collectSemanticTargets(relationship.relatingDocument(), resolved, visiting));
                    }
                }
            }
        } else if (entity instanceof StepDocumentUsageConstraint) {
            StepDocumentUsageConstraint documentUsageConstraint = (StepDocumentUsageConstraint) entity;
            targets.addAll(collectSemanticTargets(documentUsageConstraint.source(), resolved, visiting));
        } else if (entity instanceof StepGroup) {
            StepGroup group = (StepGroup) entity;
            targets.addAll(collectTargetsReferencingEntity(group.id(), resolved, visiting));
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepGroupAssignment
                    && ((StepGroupAssignment) candidate).assignedGroup().id() == group.id()) {
                StepGroupAssignment assignment = (StepGroupAssignment) candidate;
                targets.addAll(collectSemanticTargets(assignment.assignedGroup(), resolved, visiting));
            } else if (candidate instanceof StepAppliedGroupAssignment
                    && ((StepAppliedGroupAssignment) candidate).assignedGroup().id() == group.id()) {
                StepAppliedGroupAssignment assignment = (StepAppliedGroupAssignment) candidate;
                targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
            } else if (candidate instanceof StepClassificationAssignment
                    && ((StepClassificationAssignment) candidate).assignedClass().id() == group.id()) {
                StepClassificationAssignment assignment = (StepClassificationAssignment) candidate;
                targets.addAll(collectSemanticTargets(assignment.assignedClass(), resolved, visiting));
            } else if (candidate instanceof StepAppliedClassificationAssignment
                    && ((StepAppliedClassificationAssignment) candidate).assignedClass().id() == group.id()) {
                StepAppliedClassificationAssignment assignment = (StepAppliedClassificationAssignment) candidate;
                targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
                } else if (candidate instanceof StepGroupRelationship) {
            StepGroupRelationship relationship = (StepGroupRelationship) candidate;
                    if (relationship.relatingGroup().id() == group.id()) {
                        targets.addAll(collectSemanticTargets(relationship.relatedGroup(), resolved, visiting));
                    }
                    if (relationship.relatedGroup().id() == group.id()) {
                        targets.addAll(collectSemanticTargets(relationship.relatingGroup(), resolved, visiting));
                    }
                }
            }
        } else if (entity instanceof StepOrganization) {
            StepOrganization organization = (StepOrganization) entity;
            targets.addAll(collectTargetsReferencingEntity(organization.id(), resolved, visiting));
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepAppliedOrganizationAssignment
                        && ((StepAppliedOrganizationAssignment) candidate).assignedOrganization().id() == organization.id()) {
                    StepAppliedOrganizationAssignment assignment = (StepAppliedOrganizationAssignment) candidate;
                    targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
                } else if (candidate instanceof StepOrganizationAssignment
                        && ((StepOrganizationAssignment) candidate).assignedOrganization().id() == organization.id()) {
                    StepOrganizationAssignment assignment = (StepOrganizationAssignment) candidate;
                    targets.addAll(collectSemanticTargets(assignment.assignedOrganization(), resolved, visiting));
                } else if (candidate instanceof StepOrganizationRelationship) {
                    StepOrganizationRelationship relationship = (StepOrganizationRelationship) candidate;
                    if (relationship.relatingOrganization().id() == organization.id()) {
                        targets.addAll(collectSemanticTargets(relationship.relatedOrganization(), resolved, visiting));
                    }
                    if (relationship.relatedOrganization().id() == organization.id()) {
                        targets.addAll(collectSemanticTargets(relationship.relatingOrganization(), resolved, visiting));
                    }
                }
            }
        } else if (entity instanceof StepProductCategory) {
            StepProductCategory productCategory = (StepProductCategory) entity;
            targets.addAll(collectTargetsReferencingEntity(productCategory.id(), resolved, visiting));
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepProductCategoryRelationship) {
                    StepProductCategoryRelationship relationship = (StepProductCategoryRelationship) candidate;
                    if (relationship.category().id() == productCategory.id()) {
                        targets.addAll(collectSemanticTargets(relationship.subCategory(), resolved, visiting));
                    }
                    if (relationship.subCategory().id() == productCategory.id()) {
                        targets.addAll(collectSemanticTargets(relationship.category(), resolved, visiting));
                    }
                } else if (candidate instanceof StepProductRelatedProductCategory
                        && ((StepProductRelatedProductCategory) candidate).id() == productCategory.id()) {
                    StepProductRelatedProductCategory relatedCategory = (StepProductRelatedProductCategory) candidate;
                    targets.addAll(collectSemanticTargets(relatedCategory.products(), resolved, visiting));
                }
            }
        } else if (entity instanceof StepProductRelatedProductCategory) {
            StepProductRelatedProductCategory relatedCategory = (StepProductRelatedProductCategory) entity;
            targets.addAll(collectSemanticTargets(relatedCategory.products(), resolved, visiting));
        } else if (entity instanceof StepProduct) {
            StepProduct product = (StepProduct) entity;
            targets.addAll(collectTargetsReferencingEntity(product.id(), resolved, visiting));
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepProductDefinitionFormation
                        && ((StepProductDefinitionFormation) candidate).ofProduct().id() == product.id()) {
                    StepProductDefinitionFormation formation = (StepProductDefinitionFormation) candidate;
                    targets.addAll(collectSemanticTargets(formation, resolved, visiting));
                } else if (candidate instanceof StepProductRelatedProductCategory
                        && ((StepProductRelatedProductCategory) candidate).products().stream().anyMatch(related -> related.id() == product.id())) {
                    StepProductRelatedProductCategory relatedCategory = (StepProductRelatedProductCategory) candidate;
                    targets.addAll(collectSemanticTargets(relatedCategory, resolved, visiting));
                } else if (candidate instanceof StepProductRelationship) {
                    StepProductRelationship relationship = (StepProductRelationship) candidate;
                    if (relationship.relatingProduct().id() == product.id()) {
                        targets.addAll(collectSemanticTargets(relationship.relatedProduct(), resolved, visiting));
                    }
                    if (relationship.relatedProduct().id() == product.id()) {
                        targets.addAll(collectSemanticTargets(relationship.relatingProduct(), resolved, visiting));
                    }
                }
            }
        } else if (entity instanceof StepProductDefinitionFormation) {
            StepProductDefinitionFormation formation = (StepProductDefinitionFormation) entity;
            targets.addAll(collectTargetsReferencingEntity(formation.id(), resolved, visiting));
            targets.addAll(collectSemanticTargets(formation.ofProduct(), resolved, visiting));
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepProductDefinition
                        && ((StepProductDefinition) candidate).formation().id() == formation.id()) {
                    StepProductDefinition productDefinition = (StepProductDefinition) candidate;
                    targets.addAll(collectSemanticTargets(productDefinition, resolved, visiting));
                } else if (candidate instanceof StepProductDefinitionFormationRelationship) {
                    StepProductDefinitionFormationRelationship relationship = (StepProductDefinitionFormationRelationship) candidate;
                    if (relationship.relatingFormation().id() == formation.id()) {
                        targets.addAll(collectSemanticTargets(relationship.relatedFormation(), resolved, visiting));
                    }
                    if (relationship.relatedFormation().id() == formation.id()) {
                        targets.addAll(collectSemanticTargets(relationship.relatingFormation(), resolved, visiting));
                    }
                }
            }
        } else if (entity instanceof StepProductDefinitionEffectivity) {
            StepProductDefinitionEffectivity effectivity = (StepProductDefinitionEffectivity) entity;
            targets.addAll(collectSemanticTargets(effectivity.productDefinition(), resolved, visiting));
        } else if (entity instanceof StepEffectivity) {
            StepEffectivity effectivity = (StepEffectivity) entity;
            targets.addAll(collectTargetsReferencingEntity(effectivity.id(), resolved, visiting));
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepEffectivityRelationship) {
            StepEffectivityRelationship relationship = (StepEffectivityRelationship) candidate;
                    if (relationship.relatingEffectivity().id() == effectivity.id()) {
                        targets.addAll(collectSemanticTargets(relationship.relatedEffectivity(), resolved, visiting));
                    }
                    if (relationship.relatedEffectivity().id() == effectivity.id()) {
                        targets.addAll(collectSemanticTargets(relationship.relatingEffectivity(), resolved, visiting));
                    }
                }
            }
        } else if (entity instanceof StepCalendarDate) {
            StepCalendarDate calendarDate = (StepCalendarDate) entity;
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepDateAssignment
                        && ((StepDateAssignment) candidate).assignedDate().id() == calendarDate.id()) {
                    StepDateAssignment assignment = (StepDateAssignment) candidate;
                    targets.addAll(collectSemanticTargets(assignment, resolved, visiting));
                } else if (candidate instanceof StepAppliedDateAssignment
                        && ((StepAppliedDateAssignment) candidate).assignedDate().id() == calendarDate.id()) {
                    StepAppliedDateAssignment assignment = (StepAppliedDateAssignment) candidate;
                    targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
                } else if (candidate instanceof StepDateAndTime
                        && ((StepDateAndTime) candidate).dateComponent().id() == calendarDate.id()) {
                    StepDateAndTime dateAndTime = (StepDateAndTime) candidate;
                    targets.addAll(collectSemanticTargets(dateAndTime, resolved, visiting));
                }
            }
        } else if (entity instanceof StepDateAndTime) {
            StepDateAndTime dateAndTime = (StepDateAndTime) entity;
            targets.addAll(collectSemanticTargets(dateAndTime.dateComponent(), resolved, visiting));
            targets.addAll(collectSemanticTargets(dateAndTime.timeComponent(), resolved, visiting));
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepDateTimeAssignment
                        && ((StepDateTimeAssignment) candidate).assignedDateAndTime().id() == dateAndTime.id()) {
                    StepDateTimeAssignment assignment = (StepDateTimeAssignment) candidate;
                    targets.addAll(collectSemanticTargets(assignment, resolved, visiting));
                } else if (candidate instanceof StepAppliedDateTimeAssignment
                        && ((StepAppliedDateTimeAssignment) candidate).assignedDateAndTime().id() == dateAndTime.id()) {
                    StepAppliedDateTimeAssignment assignment = (StepAppliedDateTimeAssignment) candidate;
                    targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
                } else if (candidate instanceof StepApprovalDateTime
                        && ((StepApprovalDateTime) candidate).dateTime().id() == dateAndTime.id()) {
                    StepApprovalDateTime approvalDateTime = (StepApprovalDateTime) candidate;
                    targets.addAll(collectSemanticTargets(approvalDateTime, resolved, visiting));
                }
            }
        } else if (entity instanceof StepLocalTime) {
            StepLocalTime localTime = (StepLocalTime) entity;
            targets.addAll(collectSemanticTargets(localTime.zone(), resolved, visiting));
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepDateAndTime
                        && ((StepDateAndTime) candidate).timeComponent().id() == localTime.id()) {
                    StepDateAndTime dateAndTime = (StepDateAndTime) candidate;
                    targets.addAll(collectSemanticTargets(dateAndTime, resolved, visiting));
                }
            }
        } else if (entity instanceof StepCoordinatedUniversalTimeOffset) {
            StepCoordinatedUniversalTimeOffset zone = (StepCoordinatedUniversalTimeOffset) entity;
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepLocalTime
                        && ((StepLocalTime) candidate).zone().id() == zone.id()) {
                    StepLocalTime localTime = (StepLocalTime) candidate;
                    targets.addAll(collectSemanticTargets(localTime, resolved, visiting));
                }
            }
        } else if (entity instanceof StepDateAssignment) {
            StepDateAssignment assignment = (StepDateAssignment) entity;
            targets.addAll(collectTargetsForDateRole(assignment.role().id(), resolved, visiting));
        } else if (entity instanceof StepDateTimeAssignment) {
            StepDateTimeAssignment assignment = (StepDateTimeAssignment) entity;
            targets.addAll(collectTargetsForDateTimeRole(assignment.role().id(), resolved, visiting));
        } else if (entity instanceof StepPerson) {
            StepPerson person = (StepPerson) entity;
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepPersonAndOrganization
                        && ((StepPersonAndOrganization) candidate).person().id() == person.id()) {
                    StepPersonAndOrganization personAndOrganization = (StepPersonAndOrganization) candidate;
                    targets.addAll(collectSemanticTargets(personAndOrganization, resolved, visiting));
                }
            }
        } else if (entity instanceof StepApprovalStatus) {
            StepApprovalStatus status = (StepApprovalStatus) entity;
            targets.addAll(collectTargetsForApprovalStatus(status.id(), resolved, visiting));
        } else if (entity instanceof StepSecurityClassificationLevel) {
            StepSecurityClassificationLevel level = (StepSecurityClassificationLevel) entity;
            targets.addAll(collectTargetsForSecurityLevel(level.id(), resolved, visiting));
        } else if (entity instanceof StepContractType) {
            StepContractType kind = (StepContractType) entity;
            targets.addAll(collectTargetsForContractType(kind.id(), resolved, visiting));
        } else if (entity instanceof StepCertificationType) {
            StepCertificationType kind = (StepCertificationType) entity;
            targets.addAll(collectTargetsForCertificationType(kind.id(), resolved, visiting));
        } else if (entity instanceof StepApprovalRole) {
            StepApprovalRole role = (StepApprovalRole) entity;
            targets.addAll(collectTargetsForApprovalRole(role.id(), resolved, visiting));
        } else if (entity instanceof StepOrganizationRole) {
            StepOrganizationRole role = (StepOrganizationRole) entity;
            targets.addAll(collectTargetsForOrganizationRole(role.id(), resolved, visiting));
        } else if (entity instanceof StepPersonAndOrganizationRole) {
            StepPersonAndOrganizationRole role = (StepPersonAndOrganizationRole) entity;
            targets.addAll(collectTargetsForPersonAndOrganizationRole(role.id(), resolved, visiting));
        } else if (entity instanceof StepClassificationRole) {
            StepClassificationRole role = (StepClassificationRole) entity;
            targets.addAll(collectTargetsForClassificationRole(role.id(), resolved, visiting));
        } else if (entity instanceof StepDateRole) {
            StepDateRole role = (StepDateRole) entity;
            targets.addAll(collectTargetsForDateRole(role.id(), resolved, visiting));
        } else if (entity instanceof StepDateTimeRole) {
            StepDateTimeRole role = (StepDateTimeRole) entity;
            targets.addAll(collectTargetsForDateTimeRole(role.id(), resolved, visiting));
        } else if (entity instanceof StepIdentificationRole) {
            StepIdentificationRole role = (StepIdentificationRole) entity;
            targets.addAll(collectTargetsForIdentificationRole(role.id(), resolved, visiting));
        } else if (entity instanceof StepDocumentType) {
            StepDocumentType kind = (StepDocumentType) entity;
            targets.addAll(collectTargetsForDocumentType(kind.id(), resolved, visiting));
        } else if (entity instanceof StepApproval) {
            StepApproval approval = (StepApproval) entity;
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepAppliedApprovalAssignment
                        && ((StepAppliedApprovalAssignment) candidate).assignedApproval().id() == approval.id()) {
                    StepAppliedApprovalAssignment assignment = (StepAppliedApprovalAssignment) candidate;
                    targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
                } else if (candidate instanceof StepApprovalAssignment
                        && ((StepApprovalAssignment) candidate).assignedApproval().id() == approval.id()) {
                    StepApprovalAssignment assignment = (StepApprovalAssignment) candidate;
                    targets.addAll(collectSemanticTargets(assignment.assignedApproval(), resolved, visiting));
                } else if (candidate instanceof StepApprovalPersonOrganization
                        && ((StepApprovalPersonOrganization) candidate).authorizedApproval().id() == approval.id()) {
                    StepApprovalPersonOrganization personOrganization = (StepApprovalPersonOrganization) candidate;
                    targets.addAll(collectSemanticTargets(personOrganization.personOrganization(), resolved, visiting));
                } else if (candidate instanceof StepApprovalDateTime
                        && ((StepApprovalDateTime) candidate).datedApproval().id() == approval.id()) {
                    StepApprovalDateTime approvalDateTime = (StepApprovalDateTime) candidate;
                    targets.addAll(collectSemanticTargets(approvalDateTime.dateTime(), resolved, visiting));
                }
            }
        } else if (entity instanceof StepSecurityClassification) {
            StepSecurityClassification classification = (StepSecurityClassification) entity;
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepAppliedSecurityClassificationAssignment
                        && ((StepAppliedSecurityClassificationAssignment) candidate).assignedSecurityClassification().id() == classification.id()) {
                    StepAppliedSecurityClassificationAssignment assignment = (StepAppliedSecurityClassificationAssignment) candidate;
                    targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
                } else if (candidate instanceof StepSecurityClassificationAssignment
                        && ((StepSecurityClassificationAssignment) candidate).assignedSecurityClassification().id() == classification.id()) {
                    StepSecurityClassificationAssignment assignment = (StepSecurityClassificationAssignment) candidate;
                    targets.addAll(collectSemanticTargets(assignment.assignedSecurityClassification(), resolved, visiting));
                }
            }
        } else if (entity instanceof StepContract) {
            StepContract contract = (StepContract) entity;
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepAppliedContractAssignment
                        && ((StepAppliedContractAssignment) candidate).assignedContract().id() == contract.id()) {
                    StepAppliedContractAssignment assignment = (StepAppliedContractAssignment) candidate;
                    targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
                } else if (candidate instanceof StepContractAssignment
                        && ((StepContractAssignment) candidate).assignedContract().id() == contract.id()) {
                    StepContractAssignment assignment = (StepContractAssignment) candidate;
                    targets.addAll(collectSemanticTargets(assignment.assignedContract(), resolved, visiting));
                }
            }
        } else if (entity instanceof StepCertification) {
            StepCertification certification = (StepCertification) entity;
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepAppliedCertificationAssignment
                        && ((StepAppliedCertificationAssignment) candidate).assignedCertification().id() == certification.id()) {
                    StepAppliedCertificationAssignment assignment = (StepAppliedCertificationAssignment) candidate;
                    targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
                } else if (candidate instanceof StepCertificationAssignment
                        && ((StepCertificationAssignment) candidate).assignedCertification().id() == certification.id()) {
                    StepCertificationAssignment assignment = (StepCertificationAssignment) candidate;
                    targets.addAll(collectSemanticTargets(assignment.assignedCertification(), resolved, visiting));
                }
            }
        } else if (entity instanceof StepPersonAndOrganization) {
            StepPersonAndOrganization personAndOrganization = (StepPersonAndOrganization) entity;
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepAppliedPersonAndOrganizationAssignment
                        && ((StepAppliedPersonAndOrganizationAssignment) candidate).assignedPersonAndOrganization().id() == personAndOrganization.id()) {
                    StepAppliedPersonAndOrganizationAssignment assignment = (StepAppliedPersonAndOrganizationAssignment) candidate;
                    targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
                } else if (candidate instanceof StepPersonAndOrganizationAssignment
                        && ((StepPersonAndOrganizationAssignment) candidate).assignedPersonAndOrganization().id() == personAndOrganization.id()) {
                    StepPersonAndOrganizationAssignment assignment = (StepPersonAndOrganizationAssignment) candidate;
                    targets.addAll(collectSemanticTargets(assignment.assignedPersonAndOrganization(), resolved, visiting));
                }
            }
        } else if (entity instanceof StepLanguage) {
            StepLanguage language = (StepLanguage) entity;
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepAppliedLanguageAssignment
                        && ((StepAppliedLanguageAssignment) candidate).assignedLanguage().id() == language.id()) {
                    StepAppliedLanguageAssignment assignment = (StepAppliedLanguageAssignment) candidate;
                    targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
                } else if (candidate instanceof StepLanguageAssignment
                        && ((StepLanguageAssignment) candidate).assignedLanguage().id() == language.id()) {
                    StepLanguageAssignment assignment = (StepLanguageAssignment) candidate;
                    targets.addAll(collectSemanticTargets(assignment.assignedLanguage(), resolved, visiting));
                }
            }
        } else if (entity instanceof StepExternalIdentificationAssignment) {
            StepExternalIdentificationAssignment assignment = (StepExternalIdentificationAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.source(), resolved, visiting));
        } else if (entity instanceof StepExternalSource) {
            StepExternalSource source = (StepExternalSource) entity;
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepExternallyDefinedItem
                        && ((StepExternallyDefinedItem) candidate).source().id() == source.id()) {
                    StepExternallyDefinedItem item = (StepExternallyDefinedItem) candidate;
                    targets.addAll(collectSemanticTargets(item, resolved, visiting));
                } else if (candidate instanceof StepAppliedExternalIdentificationAssignment
                        && ((StepAppliedExternalIdentificationAssignment) candidate).source().id() == source.id()) {
                    StepAppliedExternalIdentificationAssignment assignment = (StepAppliedExternalIdentificationAssignment) candidate;
                    targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
                } else if (candidate instanceof StepExternalSourceRelationship) {
                    StepExternalSourceRelationship relationship = (StepExternalSourceRelationship) candidate;
                    if (relationship.relatingSource().id() == source.id()) {
                        targets.addAll(collectSemanticTargets(relationship.relatedSource(), resolved, visiting));
                    }
                    if (relationship.relatedSource().id() == source.id()) {
                        targets.addAll(collectSemanticTargets(relationship.relatingSource(), resolved, visiting));
                    }
                }
            }
        } else if (entity instanceof StepExternallyDefinedItem) {
            StepExternallyDefinedItem item = (StepExternallyDefinedItem) entity;
            targets.addAll(collectTargetsReferencingEntity(item.id(), resolved, visiting));
            targets.addAll(collectSemanticTargets(item.source(), resolved, visiting));
        } else if (entity instanceof StepGeneralPropertyRelationship) {
            StepGeneralPropertyRelationship relationship = (StepGeneralPropertyRelationship) entity;
            targets.addAll(collectSemanticTargets(relationship.relatingGeneralProperty(), resolved, visiting));
            targets.addAll(collectSemanticTargets(relationship.relatedGeneralProperty(), resolved, visiting));
        } else if (entity instanceof StepApprovalAssignment) {
            StepApprovalAssignment assignment = (StepApprovalAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.assignedApproval(), resolved, visiting));
        } else if (entity instanceof StepClassificationAssignment) {
            StepClassificationAssignment assignment = (StepClassificationAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.assignedClass(), resolved, visiting));
        } else if (entity instanceof StepGroupAssignment) {
            StepGroupAssignment assignment = (StepGroupAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.assignedGroup(), resolved, visiting));
        } else if (entity instanceof StepSecurityClassificationAssignment) {
            StepSecurityClassificationAssignment assignment = (StepSecurityClassificationAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.assignedSecurityClassification(), resolved, visiting));
        } else if (entity instanceof StepContractAssignment) {
            StepContractAssignment assignment = (StepContractAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.assignedContract(), resolved, visiting));
        } else if (entity instanceof StepCertificationAssignment) {
            StepCertificationAssignment assignment = (StepCertificationAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.assignedCertification(), resolved, visiting));
        } else if (entity instanceof StepPersonAndOrganizationAssignment) {
            StepPersonAndOrganizationAssignment assignment = (StepPersonAndOrganizationAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.assignedPersonAndOrganization(), resolved, visiting));
        } else if (entity instanceof StepOrganizationAssignment) {
            StepOrganizationAssignment assignment = (StepOrganizationAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.assignedOrganization(), resolved, visiting));
        } else if (entity instanceof StepLanguageAssignment) {
            StepLanguageAssignment assignment = (StepLanguageAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.assignedLanguage(), resolved, visiting));
        } else if (entity instanceof StepDocumentReference) {
            StepDocumentReference reference = (StepDocumentReference) entity;
            targets.addAll(collectSemanticTargets(reference.assignedDocument(), resolved, visiting));
        } else if (entity instanceof StepPresentationLayerAssignment) {
            StepPresentationLayerAssignment assignment = (StepPresentationLayerAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.assignedItems(), resolved, visiting));
        } else if (entity instanceof StepApprovalPersonOrganization) {
            StepApprovalPersonOrganization approvalPersonOrganization = (StepApprovalPersonOrganization) entity;
            targets.addAll(collectSemanticTargets(approvalPersonOrganization.authorizedApproval(), resolved, visiting));
        } else if (entity instanceof StepApprovalDateTime) {
            StepApprovalDateTime approvalDateTime = (StepApprovalDateTime) entity;
            targets.addAll(collectSemanticTargets(approvalDateTime.datedApproval(), resolved, visiting));
        } else if (entity instanceof StepItemDefinedTransformation) {
            StepItemDefinedTransformation transformation = (StepItemDefinedTransformation) entity;
            targets.addAll(collectTargetsForItemDefinedTransformation(transformation.id(), resolved));
        } else if (entity instanceof StepExternalSourceRelationship) {
            StepExternalSourceRelationship relationship = (StepExternalSourceRelationship) entity;
            targets.addAll(collectSemanticTargets(relationship.relatingSource(), resolved, visiting));
            targets.addAll(collectSemanticTargets(relationship.relatedSource(), resolved, visiting));
        } else if (entity instanceof StepDocumentRelationship) {
            StepDocumentRelationship relationship = (StepDocumentRelationship) entity;
            targets.addAll(collectSemanticTargets(relationship.relatingDocument(), resolved, visiting));
            targets.addAll(collectSemanticTargets(relationship.relatedDocument(), resolved, visiting));
        } else if (entity instanceof StepGroupRelationship) {
            StepGroupRelationship relationship = (StepGroupRelationship) entity;
            targets.addAll(collectSemanticTargets(relationship.relatingGroup(), resolved, visiting));
            targets.addAll(collectSemanticTargets(relationship.relatedGroup(), resolved, visiting));
        } else if (entity instanceof StepOrganizationRelationship) {
            StepOrganizationRelationship relationship = (StepOrganizationRelationship) entity;
            targets.addAll(collectSemanticTargets(relationship.relatingOrganization(), resolved, visiting));
            targets.addAll(collectSemanticTargets(relationship.relatedOrganization(), resolved, visiting));
        } else if (entity instanceof StepProductCategoryRelationship) {
            StepProductCategoryRelationship relationship = (StepProductCategoryRelationship) entity;
            targets.addAll(collectSemanticTargets(relationship.category(), resolved, visiting));
            targets.addAll(collectSemanticTargets(relationship.subCategory(), resolved, visiting));
        } else if (entity instanceof StepProductRelationship) {
            StepProductRelationship relationship = (StepProductRelationship) entity;
            targets.addAll(collectSemanticTargets(relationship.relatingProduct(), resolved, visiting));
            targets.addAll(collectSemanticTargets(relationship.relatedProduct(), resolved, visiting));
        } else if (entity instanceof StepProductDefinitionFormationRelationship) {
            StepProductDefinitionFormationRelationship relationship = (StepProductDefinitionFormationRelationship) entity;
            targets.addAll(collectSemanticTargets(relationship.relatingFormation(), resolved, visiting));
            targets.addAll(collectSemanticTargets(relationship.relatedFormation(), resolved, visiting));
        } else if (entity instanceof StepEffectivityRelationship) {
            StepEffectivityRelationship relationship = (StepEffectivityRelationship) entity;
            targets.addAll(collectSemanticTargets(relationship.relatingEffectivity(), resolved, visiting));
            targets.addAll(collectSemanticTargets(relationship.relatedEffectivity(), resolved, visiting));
        } else if (entity instanceof StepRepresentationRelationship) {
            StepRepresentationRelationship relationship = (StepRepresentationRelationship) entity;
            targets.addAll(collectRepresentationTargetsFromRelationship(relationship));
        } else if (entity instanceof StepRepresentationRelationshipWithTransformation) {
            StepRepresentationRelationshipWithTransformation relationship = (StepRepresentationRelationshipWithTransformation) entity;
            targets.addAll(collectRepresentationTargetsFromRelationship(relationship));
        } else if (entity instanceof StepShapeRepresentationRelationship) {
            StepShapeRepresentationRelationship relationship = (StepShapeRepresentationRelationship) entity;
            targets.addAll(collectRepresentationTargetsFromRelationship(relationship));
        } else if (entity instanceof StepGeometricItemSpecificUsage) {
            StepGeometricItemSpecificUsage usage = (StepGeometricItemSpecificUsage) entity;
            targets.addAll(collectSemanticTargets(usage.usage(), resolved, visiting));
            targets.addAll(collectSemanticTargets(usage.identifiedItem(), resolved, visiting));
        } else if (entity instanceof StepChainBasedGeometricItemSpecificUsage) {
            StepChainBasedGeometricItemSpecificUsage usage = (StepChainBasedGeometricItemSpecificUsage) entity;
            targets.addAll(collectSemanticTargets(usage.usage(), resolved, visiting));
            targets.addAll(collectSemanticTargets(usage.identifiedItem(), resolved, visiting));
            targets.addAll(collectSemanticTargets(usage.nodes(), resolved, visiting));
            for (StepRepresentationRelationship relationship : usage.undirectedLinks()) {
                targets.addAll(collectRepresentationTargetsFromRelationship(relationship));
            }
        } else if (entity instanceof StepItemIdentifiedRepresentationUsage) {
            StepItemIdentifiedRepresentationUsage usage = (StepItemIdentifiedRepresentationUsage) entity;
            targets.add(usage.usedRepresentation());
            targets.addAll(collectSemanticTargets(usage.definition(), resolved, visiting));
            targets.addAll(collectSemanticTargets(usage.identifiedItem(), resolved, visiting));
        } else if (entity instanceof StepChainBasedItemIdentifiedRepresentationUsage) {
            StepChainBasedItemIdentifiedRepresentationUsage usage = (StepChainBasedItemIdentifiedRepresentationUsage) entity;
            targets.addAll(collectSemanticTargets(usage.definition(), resolved, visiting));
            targets.addAll(collectSemanticTargets(usage.identifiedItem(), resolved, visiting));
            targets.addAll(collectSemanticTargets(usage.nodes(), resolved, visiting));
            for (StepRepresentationRelationship relationship : usage.undirectedLinks()) {
                targets.addAll(collectRepresentationTargetsFromRelationship(relationship));
            }
        } else if (entity instanceof StepPlacedTarget) {
            StepPlacedTarget usage = (StepPlacedTarget) entity;
            targets.add(usage.usedRepresentation());
            targets.addAll(collectSemanticTargets(usage.definition(), resolved, visiting));
            targets.addAll(collectSemanticTargets(usage.identifiedItem(), resolved, visiting));
        } else if (entity instanceof StepDraughtingModelItemAssociation) {
            StepDraughtingModelItemAssociation usage = (StepDraughtingModelItemAssociation) entity;
            targets.add(usage.usedRepresentation());
            targets.addAll(collectSemanticTargets(usage.definition(), resolved, visiting));
            targets.addAll(collectSemanticTargets(usage.identifiedItem(), resolved, visiting));
        } else if (entity instanceof StepDraughtingModelItemAssociationWithPlaceholder) {
            StepDraughtingModelItemAssociationWithPlaceholder usage = (StepDraughtingModelItemAssociationWithPlaceholder) entity;
            targets.add(usage.usedRepresentation());
            targets.addAll(collectSemanticTargets(usage.definition(), resolved, visiting));
            targets.addAll(collectSemanticTargets(usage.identifiedItem(), resolved, visiting));
            targets.addAll(collectSemanticTargets(usage.annotationPlaceholder(), resolved, visiting));
        } else if (entity instanceof StepPmiRequirementItemAssociation) {
            StepPmiRequirementItemAssociation usage = (StepPmiRequirementItemAssociation) entity;
            targets.add(usage.usedRepresentation());
            targets.addAll(collectSemanticTargets(usage.definition(), resolved, visiting));
            targets.addAll(collectSemanticTargets(usage.identifiedItem(), resolved, visiting));
            targets.addAll(collectSemanticTargets(usage.requirement(), resolved, visiting));
        } else if (entity instanceof StepMechanicalDesignRequirementItemAssociation) {
            StepMechanicalDesignRequirementItemAssociation usage = (StepMechanicalDesignRequirementItemAssociation) entity;
            targets.add(usage.usedRepresentation());
            targets.addAll(collectSemanticTargets(usage.definition(), resolved, visiting));
            targets.addAll(collectSemanticTargets(usage.identifiedItem(), resolved, visiting));
            targets.addAll(collectSemanticTargets(usage.requirement(), resolved, visiting));
        } else if (entity instanceof StepStyledItem) {
            StepStyledItem styledItem = (StepStyledItem) entity;
            targets.addAll(collectSemanticTargets(styledItem.styles(), resolved, visiting));
            targets.addAll(collectSemanticTargets(styledItem.item(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(styledItem.id(), resolved, visiting));
        } else if (entity instanceof StepOverRidingStyledItem) {
            StepOverRidingStyledItem styledItem = (StepOverRidingStyledItem) entity;
            targets.addAll(collectSemanticTargets(styledItem.styles(), resolved, visiting));
            targets.addAll(collectSemanticTargets(styledItem.item(), resolved, visiting));
            targets.addAll(collectSemanticTargets(styledItem.overRiddenStyle(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(styledItem.id(), resolved, visiting));
        } else if (entity instanceof StepMappedItem) {
            StepMappedItem mappedItem = (StepMappedItem) entity;
            targets.addAll(collectSemanticTargets(mappedItem.mappingSource(), resolved, visiting));
            targets.addAll(collectSemanticTargets(mappedItem.mappingTarget(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(mappedItem.id(), resolved, visiting));
        } else if (entity instanceof StepAnnotationCurveOccurrence) {
            StepAnnotationCurveOccurrence occurrence = (StepAnnotationCurveOccurrence) entity;
            targets.addAll(collectSemanticTargets(occurrence.styles(), resolved, visiting));
            targets.addAll(collectSemanticTargets(occurrence.item(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(occurrence.id(), resolved, visiting));
        } else if (entity instanceof StepAnnotationFillArea) {
            StepAnnotationFillArea fillArea = (StepAnnotationFillArea) entity;
            targets.addAll(collectSemanticTargets(fillArea.boundaries(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(fillArea.id(), resolved, visiting));
        } else if (entity instanceof StepAnnotationFillAreaOccurrence) {
            StepAnnotationFillAreaOccurrence occurrence = (StepAnnotationFillAreaOccurrence) entity;
            targets.addAll(collectSemanticTargets(occurrence.styles(), resolved, visiting));
            targets.addAll(collectSemanticTargets(occurrence.item(), resolved, visiting));
            targets.addAll(collectSemanticTargets(occurrence.fillStyleTarget(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(occurrence.id(), resolved, visiting));
        } else if (entity instanceof StepAnnotationPlaceholderOccurrence) {
            StepAnnotationPlaceholderOccurrence occurrence = (StepAnnotationPlaceholderOccurrence) entity;
            targets.addAll(collectSemanticTargets(occurrence.styles(), resolved, visiting));
            targets.addAll(collectSemanticTargets(occurrence.item(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(occurrence.id(), resolved, visiting));
        } else if (entity instanceof StepAnnotationPlane) {
            StepAnnotationPlane plane = (StepAnnotationPlane) entity;
            targets.addAll(collectSemanticTargets(plane.styles(), resolved, visiting));
            targets.addAll(collectSemanticTargets(plane.item(), resolved, visiting));
            targets.addAll(collectSemanticTargets(plane.elements(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(plane.id(), resolved, visiting));
        } else if (entity instanceof StepAnnotationPointOccurrence) {
            StepAnnotationPointOccurrence occurrence = (StepAnnotationPointOccurrence) entity;
            targets.addAll(collectSemanticTargets(occurrence.styles(), resolved, visiting));
            targets.addAll(collectSemanticTargets(occurrence.item(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(occurrence.id(), resolved, visiting));
        } else if (entity instanceof StepAnnotationSymbolOccurrence) {
            StepAnnotationSymbolOccurrence occurrence = (StepAnnotationSymbolOccurrence) entity;
            targets.addAll(collectSemanticTargets(occurrence.styles(), resolved, visiting));
            targets.addAll(collectSemanticTargets(occurrence.item(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(occurrence.id(), resolved, visiting));
        } else if (entity instanceof StepAnnotationSubfigureOccurrence) {
            StepAnnotationSubfigureOccurrence occurrence = (StepAnnotationSubfigureOccurrence) entity;
            targets.addAll(collectSemanticTargets(occurrence.styles(), resolved, visiting));
            targets.addAll(collectSemanticTargets(occurrence.item(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(occurrence.id(), resolved, visiting));
        } else if (entity instanceof StepAnnotationTextOccurrence) {
            StepAnnotationTextOccurrence occurrence = (StepAnnotationTextOccurrence) entity;
            targets.addAll(collectSemanticTargets(occurrence.position(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(occurrence.id(), resolved, visiting));
        } else if (entity instanceof StepDraughtingAnnotationOccurrence) {
            StepDraughtingAnnotationOccurrence occurrence = (StepDraughtingAnnotationOccurrence) entity;
            targets.addAll(collectSemanticTargets(occurrence.styles(), resolved, visiting));
            targets.addAll(collectSemanticTargets(occurrence.item(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(occurrence.id(), resolved, visiting));
        } else if (entity instanceof StepDimensionCurve) {
            StepDimensionCurve occurrence = (StepDimensionCurve) entity;
            targets.addAll(collectSemanticTargets(occurrence.styles(), resolved, visiting));
            targets.addAll(collectSemanticTargets(occurrence.item(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(occurrence.id(), resolved, visiting));
        } else if (entity instanceof StepLeaderCurve) {
            StepLeaderCurve occurrence = (StepLeaderCurve) entity;
            targets.addAll(collectSemanticTargets(occurrence.styles(), resolved, visiting));
            targets.addAll(collectSemanticTargets(occurrence.item(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(occurrence.id(), resolved, visiting));
        } else if (entity instanceof StepProjectionCurve) {
            StepProjectionCurve occurrence = (StepProjectionCurve) entity;
            targets.addAll(collectSemanticTargets(occurrence.styles(), resolved, visiting));
            targets.addAll(collectSemanticTargets(occurrence.item(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(occurrence.id(), resolved, visiting));
        } else if (entity instanceof StepTerminatorSymbol) {
            StepTerminatorSymbol symbol = (StepTerminatorSymbol) entity;
            targets.addAll(collectSemanticTargets(symbol.styles(), resolved, visiting));
            targets.addAll(collectSemanticTargets(symbol.item(), resolved, visiting));
            targets.addAll(collectSemanticTargets(symbol.annotatedCurve(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(symbol.id(), resolved, visiting));
        } else if (entity instanceof StepDraughtingCallout) {
            StepDraughtingCallout callout = (StepDraughtingCallout) entity;
            targets.addAll(collectSemanticTargets(callout.contents(), resolved, visiting));
        } else if (entity instanceof StepDraughtingCalloutRelationship) {
            StepDraughtingCalloutRelationship relationship = (StepDraughtingCalloutRelationship) entity;
            targets.addAll(collectSemanticTargets(relationship.relatingCallout(), resolved, visiting));
            targets.addAll(collectSemanticTargets(relationship.relatedCallout(), resolved, visiting));
        } else if (entity instanceof StepAnnotationOccurrenceRelationship) {
            StepAnnotationOccurrenceRelationship relationship = (StepAnnotationOccurrenceRelationship) entity;
            targets.addAll(collectSemanticTargets(relationship.relatingAnnotationOccurrence(), resolved, visiting));
            targets.addAll(collectSemanticTargets(relationship.relatedAnnotationOccurrence(), resolved, visiting));
        } else if (entity instanceof StepRepresentationMap) {
            StepRepresentationMap mapping = (StepRepresentationMap) entity;
            targets.add(mapping.mappedRepresentation());
            targets.addAll(collectSemanticTargets(mapping.mappedOrigin(), resolved, visiting));
        } else if (entity instanceof StepSymbolRepresentationMap) {
            StepSymbolRepresentationMap mapping = (StepSymbolRepresentationMap) entity;
            targets.add(mapping.mappedRepresentation());
            targets.addAll(collectSemanticTargets(mapping.mappedOrigin(), resolved, visiting));
        } else if (entity instanceof StepAnnotationSymbol) {
            StepAnnotationSymbol annotationSymbol = (StepAnnotationSymbol) entity;
            targets.addAll(collectSemanticTargets(annotationSymbol.mappingSource(), resolved, visiting));
            targets.addAll(collectSemanticTargets(annotationSymbol.mappingTarget(), resolved, visiting));
        } else if (entity instanceof StepAnnotationText) {
            StepAnnotationText annotationText = (StepAnnotationText) entity;
            targets.addAll(collectSemanticTargets(annotationText.mappingSource(), resolved, visiting));
            targets.addAll(collectSemanticTargets(annotationText.mappingTarget(), resolved, visiting));
        } else if (entity instanceof StepAnnotationTextCharacter) {
            StepAnnotationTextCharacter annotationTextCharacter = (StepAnnotationTextCharacter) entity;
            targets.addAll(collectSemanticTargets(annotationTextCharacter.mappingSource(), resolved, visiting));
            targets.addAll(collectSemanticTargets(annotationTextCharacter.mappingTarget(), resolved, visiting));
        } else if (entity instanceof StepUserDefinedCurveFont) {
            StepUserDefinedCurveFont curveFont = (StepUserDefinedCurveFont) entity;
            targets.addAll(collectSemanticTargets(curveFont.mappingSource(), resolved, visiting));
            targets.addAll(collectSemanticTargets(curveFont.mappingTarget(), resolved, visiting));
        } else if (entity instanceof StepUserDefinedMarker) {
            StepUserDefinedMarker marker = (StepUserDefinedMarker) entity;
            targets.addAll(collectSemanticTargets(marker.mappingSource(), resolved, visiting));
            targets.addAll(collectSemanticTargets(marker.mappingTarget(), resolved, visiting));
        } else if (entity instanceof StepUserDefinedTerminatorSymbol) {
            StepUserDefinedTerminatorSymbol symbol = (StepUserDefinedTerminatorSymbol) entity;
            targets.addAll(collectSemanticTargets(symbol.mappingSource(), resolved, visiting));
            targets.addAll(collectSemanticTargets(symbol.mappingTarget(), resolved, visiting));
        } else if (entity instanceof StepPresentationStyleAssignment) {
            StepPresentationStyleAssignment assignment = (StepPresentationStyleAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.styles(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(assignment.id(), resolved, visiting));
        } else if (entity instanceof StepFillAreaStyle) {
            StepFillAreaStyle fillAreaStyle = (StepFillAreaStyle) entity;
            targets.addAll(collectSemanticTargets(fillAreaStyle.styles(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(fillAreaStyle.id(), resolved, visiting));
        } else if (entity instanceof StepFillAreaStyleColour) {
            StepFillAreaStyleColour fillAreaStyleColour = (StepFillAreaStyleColour) entity;
            targets.addAll(collectSemanticTargets(fillAreaStyleColour.colour(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(fillAreaStyleColour.id(), resolved, visiting));
        } else if (entity instanceof StepSurfaceStyleFillArea) {
            StepSurfaceStyleFillArea style = (StepSurfaceStyleFillArea) entity;
            targets.addAll(collectSemanticTargets(style.fillStyle(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting));
        } else if (entity instanceof StepCharacterGlyphStyleStroke) {
            StepCharacterGlyphStyleStroke style = (StepCharacterGlyphStyleStroke) entity;
            targets.addAll(collectSemanticTargets(style.strokeStyle(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting));
        } else if (entity instanceof StepCharacterGlyphStyleOutline) {
            StepCharacterGlyphStyleOutline style = (StepCharacterGlyphStyleOutline) entity;
            targets.addAll(collectSemanticTargets(style.outlineStyle(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting));
        } else if (entity instanceof StepCharacterGlyphStyleOutlineWithCharacteristics) {
            StepCharacterGlyphStyleOutlineWithCharacteristics style = (StepCharacterGlyphStyleOutlineWithCharacteristics) entity;
            targets.addAll(collectSemanticTargets(style.outlineStyle(), resolved, visiting));
            targets.addAll(collectSemanticTargets(style.characteristics(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting));
        } else if (entity instanceof StepPreDefinedCurveFont) {
            StepPreDefinedCurveFont curveFont = (StepPreDefinedCurveFont) entity;
            targets.addAll(collectTargetsForCurveFont(curveFont.id(), resolved, visiting));
        } else if (entity instanceof StepDraughtingPreDefinedCurveFont) {
            StepDraughtingPreDefinedCurveFont curveFont = (StepDraughtingPreDefinedCurveFont) entity;
            targets.addAll(collectTargetsForCurveFont(curveFont.id(), resolved, visiting));
        } else if (entity instanceof StepPreDefinedMarker) {
            StepPreDefinedMarker marker = (StepPreDefinedMarker) entity;
            targets.addAll(collectTargetsForPointMarker(marker.id(), resolved, visiting));
        } else if (entity instanceof StepPreDefinedPointMarkerSymbol) {
            StepPreDefinedPointMarkerSymbol marker = (StepPreDefinedPointMarkerSymbol) entity;
            targets.addAll(collectTargetsForPointMarker(marker.id(), resolved, visiting));
        } else if (entity instanceof StepColour) {
            StepColour colour = (StepColour) entity;
            targets.addAll(collectTargetsForStyleColour(colour.id(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(colour.id(), resolved, visiting));
        } else if (entity instanceof StepColourSpecification) {
            StepColourSpecification colour = (StepColourSpecification) entity;
            targets.addAll(collectTargetsForStyleColour(colour.id(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(colour.id(), resolved, visiting));
        } else if (entity instanceof StepColourRgb) {
            StepColourRgb colour = (StepColourRgb) entity;
            targets.addAll(collectTargetsForStyleColour(colour.id(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(colour.id(), resolved, visiting));
        } else if (entity instanceof StepConversionBasedUnit) {
            StepConversionBasedUnit unit = (StepConversionBasedUnit) entity;
            targets.addAll(collectSemanticTargets(unit.conversionFactor(), resolved, visiting));
            targets.addAll(collectTargetsForAssignedUnit(unit.id(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(unit.id(), resolved, visiting));
        } else if (entity instanceof StepConversionBasedUnitWithOffset) {
            StepConversionBasedUnitWithOffset unit = (StepConversionBasedUnitWithOffset) entity;
            targets.addAll(collectSemanticTargets(unit.conversionFactor(), resolved, visiting));
            targets.addAll(collectTargetsForAssignedUnit(unit.id(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(unit.id(), resolved, visiting));
        } else if (entity instanceof StepDerivedUnit) {
            StepDerivedUnit unit = (StepDerivedUnit) entity;
            targets.addAll(collectSemanticTargets(unit.elements(), resolved, visiting));
            targets.addAll(collectTargetsForAssignedUnit(unit.id(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(unit.id(), resolved, visiting));
        } else if (entity instanceof StepDerivedUnitElement) {
            StepDerivedUnitElement element = (StepDerivedUnitElement) entity;
            targets.addAll(collectSemanticTargets(element.unit(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(element.id(), resolved, visiting));
        } else if (entity instanceof StepNamedUnit) {
            StepNamedUnit unit = (StepNamedUnit) entity;
            targets.addAll(collectTargetsForAssignedUnit(unit.id(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(unit.id(), resolved, visiting));
        } else if (entity instanceof StepSiUnit) {
            StepSiUnit unit = (StepSiUnit) entity;
            targets.addAll(collectTargetsForAssignedUnit(unit.id(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(unit.id(), resolved, visiting));
        } else if (entity instanceof StepContextDependentUnit) {
            StepContextDependentUnit unit = (StepContextDependentUnit) entity;
            targets.addAll(collectTargetsForAssignedUnit(unit.id(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(unit.id(), resolved, visiting));
        } else if (entity instanceof StepGlobalUncertaintyAssignedContext) {
            StepGlobalUncertaintyAssignedContext context = (StepGlobalUncertaintyAssignedContext) entity;
            targets.addAll(collectSemanticTargets(context.uncertainties(), resolved, visiting));
            targets.addAll(collectTargetsForGlobalUncertaintyContext(context.id(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(context.id(), resolved, visiting));
        } else if (entity instanceof StepGlobalUnitAssignedContext) {
            StepGlobalUnitAssignedContext context = (StepGlobalUnitAssignedContext) entity;
            targets.addAll(collectSemanticTargets(context.units(), resolved, visiting));
            targets.addAll(collectTargetsForGlobalUnitContext(context.id(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(context.id(), resolved, visiting));
        } else if (entity instanceof StepRepresentationItem) {
            StepRepresentationItem item = (StepRepresentationItem) entity;
            targets.addAll(collectTargetsReferencingEntity(item.id(), resolved, visiting));
        } else if (entity instanceof StepGeometricRepresentationItem) {
            StepGeometricRepresentationItem item = (StepGeometricRepresentationItem) entity;
            targets.addAll(collectTargetsReferencingEntity(item.id(), resolved, visiting));
        } else if (entity instanceof StepTopologicalRepresentationItem) {
            StepTopologicalRepresentationItem item = (StepTopologicalRepresentationItem) entity;
            targets.addAll(collectTargetsReferencingEntity(item.id(), resolved, visiting));
        } else if (entity instanceof StepPreDefinedColour) {
            StepPreDefinedColour colour = (StepPreDefinedColour) entity;
            targets.addAll(collectTargetsForStyleColour(colour.id(), resolved, visiting));
        } else if (entity instanceof StepDraughtingPreDefinedColour) {
            StepDraughtingPreDefinedColour colour = (StepDraughtingPreDefinedColour) entity;
            targets.addAll(collectTargetsForStyleColour(colour.id(), resolved, visiting));
        } else if (entity instanceof StepCurveStyle) {
            StepCurveStyle curveStyle = (StepCurveStyle) entity;
            targets.addAll(collectSemanticTargets(curveStyle.curveFont(), resolved, visiting));
            targets.addAll(collectSemanticTargets(curveStyle.colour(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(curveStyle.id(), resolved, visiting));
        } else if (entity instanceof StepPointStyle) {
            StepPointStyle pointStyle = (StepPointStyle) entity;
            targets.addAll(collectSemanticTargets(pointStyle.marker(), resolved, visiting));
            targets.addAll(collectSemanticTargets(pointStyle.colour(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(pointStyle.id(), resolved, visiting));
        } else if (entity instanceof StepSymbolColour) {
            StepSymbolColour symbolColour = (StepSymbolColour) entity;
            targets.addAll(collectSemanticTargets(symbolColour.colour(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(symbolColour.id(), resolved, visiting));
        } else if (entity instanceof StepSymbolStyle) {
            StepSymbolStyle symbolStyle = (StepSymbolStyle) entity;
            targets.addAll(collectSemanticTargets(symbolStyle.styleOfSymbol(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(symbolStyle.id(), resolved, visiting));
        } else if (entity instanceof StepTextStyleForDefinedFont) {
            StepTextStyleForDefinedFont textStyle = (StepTextStyleForDefinedFont) entity;
            targets.addAll(collectSemanticTargets(textStyle.textColour(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(textStyle.id(), resolved, visiting));
        } else if (entity instanceof StepTextStyle) {
            StepTextStyle textStyle = (StepTextStyle) entity;
            targets.addAll(collectSemanticTargets(textStyle.characterAppearance(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(textStyle.id(), resolved, visiting));
        } else if (entity instanceof StepTextStyleWithSpacing) {
            StepTextStyleWithSpacing textStyle = (StepTextStyleWithSpacing) entity;
            targets.addAll(collectSemanticTargets(textStyle.characterAppearance(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(textStyle.id(), resolved, visiting));
        } else if (entity instanceof StepTextStyleWithJustification) {
            StepTextStyleWithJustification textStyle = (StepTextStyleWithJustification) entity;
            targets.addAll(collectSemanticTargets(textStyle.characterAppearance(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(textStyle.id(), resolved, visiting));
        } else if (entity instanceof StepTextStyleWithBoxCharacteristics) {
            StepTextStyleWithBoxCharacteristics textStyle = (StepTextStyleWithBoxCharacteristics) entity;
            targets.addAll(collectSemanticTargets(textStyle.characterAppearance(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(textStyle.id(), resolved, visiting));
        } else if (entity instanceof StepTextStyleWithMirror) {
            StepTextStyleWithMirror textStyle = (StepTextStyleWithMirror) entity;
            targets.addAll(collectSemanticTargets(textStyle.characterAppearance(), resolved, visiting));
            targets.addAll(collectSemanticTargets(textStyle.mirrorPlacement(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(textStyle.id(), resolved, visiting));
        } else if (entity instanceof StepSurfaceStyleBoundary) {
            StepSurfaceStyleBoundary style = (StepSurfaceStyleBoundary) entity;
            targets.addAll(collectSemanticTargets(style.style(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting));
        } else if (entity instanceof StepSurfaceStyleParameterLine) {
            StepSurfaceStyleParameterLine style = (StepSurfaceStyleParameterLine) entity;
            targets.addAll(collectSemanticTargets(style.style(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting));
        } else if (entity instanceof StepSurfaceStyleSegmentationCurve) {
            StepSurfaceStyleSegmentationCurve style = (StepSurfaceStyleSegmentationCurve) entity;
            targets.addAll(collectSemanticTargets(style.style(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting));
        } else if (entity instanceof StepSurfaceStyleSilhouette) {
            StepSurfaceStyleSilhouette style = (StepSurfaceStyleSilhouette) entity;
            targets.addAll(collectSemanticTargets(style.style(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting));
        } else if (entity instanceof StepSurfaceStyleControlGrid) {
            StepSurfaceStyleControlGrid style = (StepSurfaceStyleControlGrid) entity;
            targets.addAll(collectSemanticTargets(style.style(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting));
        } else if (entity instanceof StepSurfaceSideStyle) {
            StepSurfaceSideStyle sideStyle = (StepSurfaceSideStyle) entity;
            targets.addAll(collectSemanticTargets(sideStyle.styles(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(sideStyle.id(), resolved, visiting));
        } else if (entity instanceof StepSurfaceStyleUsage) {
            StepSurfaceStyleUsage usage = (StepSurfaceStyleUsage) entity;
            targets.addAll(collectSemanticTargets(usage.style(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(usage.id(), resolved, visiting));
        } else if (entity instanceof StepSurfaceStyleTransparent) {
            StepSurfaceStyleTransparent style = (StepSurfaceStyleTransparent) entity;
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting));
        } else if (entity instanceof StepSurfaceStyleReflectanceAmbient) {
            StepSurfaceStyleReflectanceAmbient style = (StepSurfaceStyleReflectanceAmbient) entity;
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting));
        } else if (entity instanceof StepSurfaceStyleReflectanceAmbientDiffuse) {
            StepSurfaceStyleReflectanceAmbientDiffuse style = (StepSurfaceStyleReflectanceAmbientDiffuse) entity;
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting));
        } else if (entity instanceof StepSurfaceStyleReflectanceAmbientDiffuseSpecular) {
            StepSurfaceStyleReflectanceAmbientDiffuseSpecular style = (StepSurfaceStyleReflectanceAmbientDiffuseSpecular) entity;
            targets.addAll(collectSemanticTargets(style.specularColour(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting));
        } else if (entity instanceof StepPreDefinedSurfaceSideStyle) {
            StepPreDefinedSurfaceSideStyle style = (StepPreDefinedSurfaceSideStyle) entity;
            targets.addAll(collectTargetsReferencingEntity(style.id(), resolved, visiting));
        } else if (entity instanceof StepPreDefinedTextFont) {
            StepPreDefinedTextFont textFont = (StepPreDefinedTextFont) entity;
            targets.addAll(collectTargetsReferencingEntity(textFont.id(), resolved, visiting));
        } else if (entity instanceof StepDraughtingPreDefinedTextFont) {
            StepDraughtingPreDefinedTextFont textFont = (StepDraughtingPreDefinedTextFont) entity;
            targets.addAll(collectTargetsReferencingEntity(textFont.id(), resolved, visiting));
        } else if (entity instanceof StepPreDefinedTerminatorSymbol) {
            StepPreDefinedTerminatorSymbol symbol = (StepPreDefinedTerminatorSymbol) entity;
            targets.addAll(collectTargetsReferencingEntity(symbol.id(), resolved, visiting));
        } else if (entity instanceof StepPreDefinedSymbol) {
            StepPreDefinedSymbol symbol = (StepPreDefinedSymbol) entity;
            targets.addAll(collectTargetsReferencingEntity(symbol.id(), resolved, visiting));
        } else if (entity instanceof StepPreDefinedDimensionSymbol) {
            StepPreDefinedDimensionSymbol symbol = (StepPreDefinedDimensionSymbol) entity;
            targets.addAll(collectTargetsReferencingEntity(symbol.id(), resolved, visiting));
        } else if (entity instanceof StepPreDefinedGeometricalToleranceSymbol) {
            StepPreDefinedGeometricalToleranceSymbol symbol = (StepPreDefinedGeometricalToleranceSymbol) entity;
            targets.addAll(collectTargetsReferencingEntity(symbol.id(), resolved, visiting));
        } else if (entity instanceof StepPreDefinedItem) {
            StepPreDefinedItem item = (StepPreDefinedItem) entity;
            targets.addAll(collectTargetsReferencingEntity(item.id(), resolved, visiting));
        } else if (entity instanceof StepDescriptionAttribute) {
            StepDescriptionAttribute descriptionAttribute = (StepDescriptionAttribute) entity;
            targets.addAll(collectSemanticTargets(descriptionAttribute.describedItem(), resolved, visiting));
        } else if (entity instanceof StepNameAttribute) {
            StepNameAttribute nameAttribute = (StepNameAttribute) entity;
            targets.addAll(collectSemanticTargets(nameAttribute.namedItem(), resolved, visiting));
        } else if (entity instanceof StepIdAttribute) {
            StepIdAttribute idAttribute = (StepIdAttribute) entity;
            targets.addAll(collectSemanticTargets(idAttribute.identifiedItem(), resolved, visiting));
        } else if (entity instanceof StepAppliedNameAssignment) {
            StepAppliedNameAssignment assignment = (StepAppliedNameAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
        } else if (entity instanceof StepAppliedIdentificationAssignment) {
            StepAppliedIdentificationAssignment assignment = (StepAppliedIdentificationAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
        } else if (entity instanceof StepAppliedExternalIdentificationAssignment) {
            StepAppliedExternalIdentificationAssignment assignment = (StepAppliedExternalIdentificationAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
        } else if (entity instanceof StepAppliedGroupAssignment) {
            StepAppliedGroupAssignment assignment = (StepAppliedGroupAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
        } else if (entity instanceof StepAppliedClassificationAssignment) {
            StepAppliedClassificationAssignment assignment = (StepAppliedClassificationAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
        } else if (entity instanceof StepAppliedDateAssignment) {
            StepAppliedDateAssignment assignment = (StepAppliedDateAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
        } else if (entity instanceof StepAppliedDateTimeAssignment) {
            StepAppliedDateTimeAssignment assignment = (StepAppliedDateTimeAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
        } else if (entity instanceof StepAppliedApprovalAssignment) {
            StepAppliedApprovalAssignment assignment = (StepAppliedApprovalAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
        } else if (entity instanceof StepAppliedSecurityClassificationAssignment) {
            StepAppliedSecurityClassificationAssignment assignment = (StepAppliedSecurityClassificationAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
        } else if (entity instanceof StepAppliedDocumentReference) {
            StepAppliedDocumentReference assignment = (StepAppliedDocumentReference) entity;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
        } else if (entity instanceof StepAppliedContractAssignment) {
            StepAppliedContractAssignment assignment = (StepAppliedContractAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
        } else if (entity instanceof StepAppliedCertificationAssignment) {
            StepAppliedCertificationAssignment assignment = (StepAppliedCertificationAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
        } else if (entity instanceof StepAppliedPersonAndOrganizationAssignment) {
            StepAppliedPersonAndOrganizationAssignment assignment = (StepAppliedPersonAndOrganizationAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
        } else if (entity instanceof StepAppliedOrganizationAssignment) {
            StepAppliedOrganizationAssignment assignment = (StepAppliedOrganizationAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
        } else if (entity instanceof StepAppliedLanguageAssignment) {
            StepAppliedLanguageAssignment assignment = (StepAppliedLanguageAssignment) entity;
            targets.addAll(collectSemanticTargets(assignment.items(), resolved, visiting));
        } else if (entity instanceof StepAttributeAssertion) {
            StepAttributeAssertion attributeAssertion = (StepAttributeAssertion) entity;
            targets.add(attributeAssertion.usedRepresentation());
            targets.addAll(collectSemanticTargets(attributeAssertion.definition(), resolved, visiting));
        } else if (entity instanceof StepIdentificationAssignment
                || entity instanceof StepNameAssignment) {
            // Pure assignment metadata without item references contributes no target by itself.
        } else if (entity instanceof StepShapeDefinitionRepresentation) {
            StepShapeDefinitionRepresentation shapeDefinitionRepresentation = (StepShapeDefinitionRepresentation) entity;
            targets.add(shapeDefinitionRepresentation.usedRepresentation());
            targets.addAll(collectSemanticTargets(shapeDefinitionRepresentation.definition(), resolved, visiting));
        } else if (entity instanceof StepContextDependentShapeRepresentation) {
            StepContextDependentShapeRepresentation contextDependent = (StepContextDependentShapeRepresentation) entity;
            targets.addAll(collectRepresentationTargetsFromRelationship(contextDependent.representationRelationship()));
            targets.addAll(collectSemanticTargets(contextDependent.representedProductRelation(), resolved, visiting));
        } else if (entity instanceof StepProductDefinitionShape) {
            StepProductDefinitionShape productDefinitionShape = (StepProductDefinitionShape) entity;
            targets.addAll(collectSemanticTargets(productDefinitionShape.definition(), resolved, visiting));
            for (StepEntity candidate : resolved.values()) {
                if (candidate instanceof StepShapeDefinitionRepresentation
                        && ((StepShapeDefinitionRepresentation) candidate).definition().id() == productDefinitionShape.id()) {
                    StepShapeDefinitionRepresentation link = (StepShapeDefinitionRepresentation) candidate;
                    targets.add(link.usedRepresentation());
                } else if (candidate instanceof StepContextDependentShapeRepresentation
                        && ((StepContextDependentShapeRepresentation) candidate).representedProductRelation().id() == productDefinitionShape.id()) {
                    StepContextDependentShapeRepresentation contextDependent = (StepContextDependentShapeRepresentation) candidate;
                    targets.addAll(collectSemanticTargets(contextDependent, resolved, visiting));
                } else if (candidate instanceof StepShapeAspect
                        && ((StepShapeAspect) candidate).ofShape().id() == productDefinitionShape.id()) {
                    StepShapeAspect shapeAspect = (StepShapeAspect) candidate;
                    targets.addAll(collectSemanticTargets(shapeAspect, resolved, visiting));
                } else if (candidate instanceof StepShapeAspectOccurrence
                        && ((StepShapeAspectOccurrence) candidate).ofShape().id() == productDefinitionShape.id()) {
                    StepShapeAspectOccurrence occurrence = (StepShapeAspectOccurrence) candidate;
                    targets.addAll(collectSemanticTargets(occurrence, resolved, visiting));
                }
            }
        } else if (entity instanceof StepProductDefinition) {
            StepProductDefinition productDefinition = (StepProductDefinition) entity;
            targets.addAll(collectTargetsForProductDefinition(productDefinition.id(), resolved, visiting));
        } else if (entity instanceof StepNextAssemblyUsageOccurrence) {
            StepNextAssemblyUsageOccurrence occurrence = (StepNextAssemblyUsageOccurrence) entity;
            targets.addAll(collectSemanticTargets(occurrence.relatedProductDefinition(), resolved, visiting));
            targets.addAll(collectTargetsForOccurrence(occurrence.id(), resolved, visiting));
        } else if (entity instanceof StepProductDefinitionRelationship) {
            StepProductDefinitionRelationship relationship = (StepProductDefinitionRelationship) entity;
            targets.addAll(collectSemanticTargets(relationship.relatingProductDefinition(), resolved, visiting));
            targets.addAll(collectSemanticTargets(relationship.relatedProductDefinition(), resolved, visiting));
        } else if (entity instanceof StepProductDefinitionRelationshipRelationship) {
            StepProductDefinitionRelationshipRelationship relationship = (StepProductDefinitionRelationshipRelationship) entity;
            targets.addAll(collectSemanticTargets(relationship.relating(), resolved, visiting));
            targets.addAll(collectSemanticTargets(relationship.related(), resolved, visiting));
        } else if (entity instanceof StepPropertyDefinitionRelationship) {
            StepPropertyDefinitionRelationship relationship = (StepPropertyDefinitionRelationship) entity;
            targets.addAll(collectSemanticTargets(relationship.relatingPropertyDefinition(), resolved, visiting));
            targets.addAll(collectSemanticTargets(relationship.relatedPropertyDefinition(), resolved, visiting));
        } else if (entity instanceof StepShapeAspectOccurrence) {
            StepShapeAspectOccurrence occurrence = (StepShapeAspectOccurrence) entity;
            targets.addAll(collectSemanticTargets(occurrence.definition(), resolved, visiting));
            targets.addAll(collectTargetsReferencingEntity(occurrence.id(), resolved, visiting));
        } else if (entity instanceof StepShapeAspect) {
            StepShapeAspect shapeAspect = (StepShapeAspect) entity;
            targets.addAll(collectTargetsReferencingEntity(shapeAspect.id(), resolved, visiting));
        } else if (entity instanceof StepShapeAspectRelationship) {
            StepShapeAspectRelationship relationship = (StepShapeAspectRelationship) entity;
            targets.addAll(collectSemanticTargets(relationship.relatingShapeAspect(), resolved, visiting));
            targets.addAll(collectSemanticTargets(relationship.relatedShapeAspect(), resolved, visiting));
        }
        visiting.remove(entity.id());
        return Set.copyOf(targets);
    }

    private static Set<StepEntity> collectSemanticTargets(
            List<? extends StepEntity> entities,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting
    ) {
        Set<StepEntity> targets = new LinkedHashSet<>();
        for (StepEntity entity : entities) {
            targets.addAll(collectSemanticTargets(entity, resolved, visiting));
        }
        return Set.copyOf(targets);
    }

    private static void appendRepresentationMapDefinitionTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepRepresentationMap representationMap,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        appendPlacementDefinitionTargets(
                targetsByUsageId,
                identifiedItem,
                representationMap.mappedOrigin(),
                instanceIdsByTargetId
        );
        appendExistingRepresentationDefinitionTargets(
                targetsByUsageId,
                identifiedItem,
                representationMap.mappedRepresentation(),
                instanceIdsByTargetId
        );
    }

    private static void appendRepresentationMapDefinitionTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepSymbolRepresentationMap representationMap,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {
        appendPlacementDefinitionTargets(
                targetsByUsageId,
                identifiedItem,
                representationMap.mappedOrigin(),
                instanceIdsByTargetId
        );
        appendExistingRepresentationDefinitionTargets(
                targetsByUsageId,
                identifiedItem,
                representationMap.mappedRepresentation(),
                instanceIdsByTargetId
        );
    }
}