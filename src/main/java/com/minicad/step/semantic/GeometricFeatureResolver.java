package com.minicad.step.semantic;

import com.minicad.step.model.*;
import com.minicad.step.syntax.StepEntityDefinition;
import com.minicad.step.syntax.StepEntityInstance;

import java.util.List;

/**
 * Geometric feature resolver - handles geometric features and modifications.
 * Extracted from StepEntityResolver to reduce file size and improve maintainability.
 * Contains chamfer, fillet, blended surfaces, and other geometric features.
 */
final class GeometricFeatureResolver {

  private final StepEntityResolver resolver;

  GeometricFeatureResolver(StepEntityResolver resolver) {
    this.resolver = resolver;
  }

  // === Edge Features ===

  StepChamferEdge resolveChamferEdge(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CHAMFER_EDGE");
    StepEntityResolver.requireParameterCount(instance, definition, 6);
    List<StepEntity> adjacentFaces =
        resolver.entityReferenceList(
            instance,
            definition,
            4,
            "CHAMFER_EDGE adjacent_faces must contain entity references");
    return new StepChamferEdge(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.numberValue(instance, definition, 2),
        resolver.numberValue(instance, definition, 3),
        adjacentFaces,
        resolver.stringValue(instance, definition, 5));
  }

  StepFilletEdge resolveFilletEdge(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "FILLET_EDGE");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    List<StepEntity> adjacentFaces =
        resolver.entityReferenceList(
            instance,
            definition,
            3,
            "FILLET_EDGE adjacent_faces must contain entity references");
    return new StepFilletEdge(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.numberValue(instance, definition, 2),
        adjacentFaces,
        resolver.stringValue(instance, definition, 4));
  }

  // === Surface Features ===

  StepBlendedSurface resolveBlendedSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "BLENDED_SURFACE");
    StepEntityResolver.requireParameterCount(instance, definition, 7);
    return new StepBlendedSurface(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.resolve(resolver.referenceId(instance, definition, 3)),
        resolver.numberValue(instance, definition, 4),
        resolver.resolve(resolver.referenceId(instance, definition, 5)));
  }

  StepFreeFormSurface resolveFreeFormSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "FREE_FORM_SURFACE");
    StepEntityResolver.requireParameterCount(instance, definition, 8);
    List<List<StepEntity>> controlPoints = resolver.resolveFreeFormControlPoints(instance, definition, 2);
    List<Double> knotVectors = resolver.numberList(instance, definition, 6);
    List<Double> weights = resolver.numberList(instance, definition, 7);
    return new StepFreeFormSurface(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        controlPoints,
        (int) resolver.numberValue(instance, definition, 3),
        (int) resolver.numberValue(instance, definition, 4),
        knotVectors,
        weights);
  }

  // === Feature Definitions ===

  StepFilletDefinition resolveFilletDefinition(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "FILLET_DEFINITION");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    List<StepEntity> edges =
        resolver.entityReferenceList(
            instance, definition, 2,
            "FILLET_DEFINITION edges must contain entity references");
    return new StepFilletDefinition(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        edges,
        resolver.optionalNumberValue(instance, definition, 3));
  }

  StepChamferDefinition resolveChamferDefinition(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CHAMFER_DEFINITION");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    List<StepEntity> edges =
        resolver.entityReferenceList(
            instance, definition, 2,
            "CHAMFER_DEFINITION edges must contain entity references");
    return new StepChamferDefinition(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        edges,
        resolver.optionalNumberValue(instance, definition, 3),
        resolver.optionalNumberValue(instance, definition, 4));
  }

  StepChamfer resolveChamfer(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CHAMFER");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    List<StepEntity> edges =
        resolver.entityReferenceList(
            instance, definition, 2,
            "CHAMFER edges must contain entity references");
    return new StepChamfer(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        edges,
        resolver.optionalNumberValue(instance, definition, 3),
        resolver.optionalNumberValue(instance, definition, 4));
  }

  // === Quality and Tolerance ===

  StepCurvedToleranceZone resolveCurvedToleranceZone(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CURVED_TOLERANCE_ZONE");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepCurvedToleranceZone(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.resolve(resolver.referenceId(instance, definition, 3)));
  }

  StepSurfaceQuality resolveSurfaceQuality(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SURFACE_QUALITY");
    StepEntityResolver.requireParameterCount(instance, definition, 7);
    List<Double> roughnessValues = resolver.numberList(instance, definition, 3);
    return new StepSurfaceQuality(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        roughnessValues,
        resolver.stringValue(instance, definition, 4),
        resolver.stringValue(instance, definition, 5),
        resolver.resolve(resolver.referenceId(instance, definition, 6)));
  }
  // === Feature Definition Entities ===

  StepFeatureElementDefinition resolveFeatureElementDefinition(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "FEATURE_ELEMENT_DEFINITION");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepFeatureElementDefinition(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1));
  }

  StepMarkingFeature resolveMarkingFeature(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "MARKING_FEATURE");
    StepEntityResolver.requireParameterCount(instance, definition, 7);
    return new StepMarkingFeature(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.stringValue(instance, definition, 3),
        resolver.numberValue(instance, definition, 4),
        resolver.resolve(resolver.referenceId(instance, definition, 5)));
  }

  StepPlacedDatumTargetFeature resolvePlacedDatumTargetFeature(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "PLACED_DATUM_TARGET_FEATURE");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepPlacedDatumTargetFeature(
        instance.id(),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 0),
            StepPropertyDefinition.class,
            "PLACED_DATUM_TARGET_FEATURE definition must reference PROPERTY_DEFINITION"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepRepresentation.class,
            "PLACED_DATUM_TARGET_FEATURE used_representation must reference REPRESENTATION"));
  }

  StepStructuralFeature resolveStructuralFeature(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "STRUCTURAL_FEATURE");
    StepEntityResolver.requireParameterCount(instance, definition, 8);
    return new StepStructuralFeature(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.numberValue(instance, definition, 3),
        resolver.resolve(resolver.referenceId(instance, definition, 4)),
        resolver.entityReferenceList(instance, definition, 5,
            "STRUCTURAL_FEATURE end conditions must contain entity references"),
        resolver.entityReferenceList(instance, definition, 6,
            "STRUCTURAL_FEATURE load points must contain entity references"));
  }
}
