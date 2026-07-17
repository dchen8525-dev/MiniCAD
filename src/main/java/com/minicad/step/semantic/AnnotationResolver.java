package com.minicad.step.semantic;

import com.minicad.common.StepResolutionException;
import com.minicad.common.UnsupportedStepEntityException;
import com.minicad.step.model.*;
import com.minicad.step.syntax.StepEntityDefinition;
import com.minicad.step.syntax.StepEntityInstance;
import com.minicad.step.syntax.StepValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Annotation resolver - handles PMI, annotation, datum, tolerance, and dimension entities.
 * Extracted from StepEntityResolver to reduce file size and improve maintainability.
 * Contains annotation occurrences/callouts, datum systems, tolerance zones, and
 * dimension representations.
 */
final class AnnotationResolver {

  private final StepEntityResolver resolver;

  AnnotationResolver(StepEntityResolver resolver) {
    this.resolver = resolver;
  }

  // === Annotation Entities ===

  StepAnnotationFillArea resolveAnnotationFillArea(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "ANNOTATION_FILL_AREA");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    List<StepEntity> boundaries =
        resolver.entityReferenceList(
            instance,
            definition,
            1,
            "ANNOTATION_FILL_AREA boundaries must contain curve references");
    for (StepEntity boundary : boundaries) {
      if (!resolver.isSupportedAnnotationCurveCarrier(boundary)) {
        throw new StepResolutionException(
            "ANNOTATION_FILL_AREA boundaries must reference supported curves, EDGE_CURVE, SUBEDGE, ORIENTED_EDGE, EDGE_LOOP, POLY_LOOP, PATH, OPEN_PATH, SUBPATH, ORIENTED_PATH, CONNECTED_EDGE_SET, WIRE_SHELL, wireframe model or GEOMETRIC_CURVE_SET");
      }
    }
    return new StepAnnotationFillArea(
        instance.id(), resolver.stringValue(instance, definition, 0), boundaries);
  }

  StepAnnotationFillAreaRegion resolveAnnotationFillAreaRegion(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "ANNOTATION_FILL_AREA_REGION");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepAnnotationFillAreaRegion(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.entityReferenceList(instance, definition, 1,
            "ANNOTATION_FILL_AREA_REGION regions must contain entity references"));
  }

  StepAnnotationPlane resolveAnnotationPlane(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "ANNOTATION_PLANE");
    StepEntityResolver.requireParameterCount(instance, definition, 1);
    List<StepEntity> elements = List.of();
    if (!resolver.isUnset(definition.parameters().get(0))) {
      elements =
          resolver.entityReferenceList(
              instance,
              definition,
              0,
              "ANNOTATION_PLANE elements must contain entity references");
      for (StepEntity element : elements) {
        if (!resolver.isSupportedAnnotationPlaneElement(element)) {
          throw new UnsupportedStepEntityException(
              "ANNOTATION_PLANE elements must reference supported point carriers or point-like annotation content/occurrences");
        }
      }
    }
    return new StepAnnotationPlane(
        instance.id(),
        resolver.inheritedRepresentationItemName(instance),
        resolver.referenceList(
            instance,
            resolver.definition(instance, "STYLED_ITEM"),
            1,
            StepPresentationStyleAssignment.class,
            "ANNOTATION_PLANE styles must contain PRESENTATION_STYLE_ASSIGNMENT references"),
        resolver.requireEntity(
            resolver.inheritedStyledItemTargetId(instance),
            StepPlane.class,
            "ANNOTATION_PLANE item must reference PLANE"),
        elements);
  }

  StepAnnotationRecord resolveAnnotationRecord(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "ANNOTATION_RECORD");
    StepEntityResolver.requireParameterCount(instance, definition, 8);
    return new StepAnnotationRecord(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.stringValue(instance, definition, 2),
        resolver.resolve(resolver.referenceId(instance, definition, 3)),
        resolver.resolve(resolver.referenceId(instance, definition, 4)),
        resolver.resolve(resolver.referenceId(instance, definition, 5)),
        resolver.stringValue(instance, definition, 6));
  }

  StepAnnotationSymbol resolveAnnotationSymbol(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "ANNOTATION_SYMBOL");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    StepEntity mappingTarget = resolver.resolve(resolver.referenceId(instance, definition, 2));
    if (!(mappingTarget instanceof StepAxis2Placement2D)
        && !(mappingTarget instanceof StepAxis2Placement3D)) {
      throw new UnsupportedStepEntityException(
          "ANNOTATION_SYMBOL mapping_target must reference AXIS2_PLACEMENT_2D or AXIS2_PLACEMENT_3D");
    }
    return new StepAnnotationSymbol(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepSymbolRepresentationMap.class,
            "ANNOTATION_SYMBOL mapping_source must reference SYMBOL_REPRESENTATION_MAP"),
        mappingTarget);
  }

  StepAnnotationText resolveAnnotationText(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "ANNOTATION_TEXT");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    StepEntity mappingTarget = resolver.resolve(resolver.referenceId(instance, definition, 2));
    if (!(mappingTarget instanceof StepAxis2Placement2D)
        && !(mappingTarget instanceof StepAxis2Placement3D)) {
      throw new UnsupportedStepEntityException(
          "ANNOTATION_TEXT mapping_target must reference AXIS2_PLACEMENT_2D or AXIS2_PLACEMENT_3D");
    }
    return new StepAnnotationText(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepRepresentationMap.class,
            "ANNOTATION_TEXT mapping_source must reference REPRESENTATION_MAP"),
        mappingTarget);
  }

  StepDimensionCurve resolveDimensionCurve(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "DIMENSION_CURVE");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    StepEntity item = resolver.resolve(resolver.referenceId(instance, definition, 2));
    if (!resolver.isSupportedAnnotationCurveCarrier(item)) {
      throw new StepResolutionException(
          "DIMENSION_CURVE item must reference a supported curve, EDGE_CURVE, SUBEDGE, ORIENTED_EDGE, EDGE_LOOP, POLY_LOOP, PATH, OPEN_PATH, SUBPATH, ORIENTED_PATH, CONNECTED_EDGE_SET, WIRE_SHELL, wireframe model or GEOMETRIC_CURVE_SET");
    }
    return new StepDimensionCurve(
        instance.id(),
        resolver.optionalStringValue(instance, definition, 0),
        resolver.referenceList(
            instance,
            definition,
            1,
            StepPresentationStyleAssignment.class,
            "DIMENSION_CURVE styles must contain PRESENTATION_STYLE_ASSIGNMENT references"),
        item);
  }

  StepDraughtingCallout resolveDraughtingCallout(StepEntityInstance instance) {
    return resolveDraughtingCallout(instance, "DRAUGHTING_CALLOUT");
  }

  StepDraughtingCallout resolveDraughtingCallout(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    List<StepEntity> contents =
        resolver.entityReferenceList(
            instance, definition, 1, entityName + " contents must contain entity references");
    for (StepEntity content : contents) {
      if (!(content instanceof StepAnnotationTextOccurrence)
          && !(content instanceof StepCartesianPoint)
          && !(content instanceof StepVertexPoint)
          && !resolver.isSupportedCurveReference(content)
          && !(content instanceof StepGeometricReplica
              && "POINT_REPLICA".equals(((StepGeometricReplica) content).entityName()))
          && !(content instanceof StepEdgeCurve)
          && !(content instanceof StepSubedge)
          && !(content instanceof StepOrientedEdge)
          && !(content instanceof StepManifoldSolidBrep)
          && !(content instanceof StepBrepWithVoids)
          && !(content instanceof StepSweptAreaSolid)
          && !(content instanceof StepSolidReplica)
          && !(content instanceof StepCsgSolid)
          && !(content instanceof StepCsgPrimitive)
          && !(content instanceof StepBooleanResult)
          && !(content instanceof StepBooleanClippingResult)
          && !(content instanceof StepAdvancedFace)
          && !(content instanceof StepFaceSurface)
          && !(content instanceof StepOrientedFace)
          && !(content instanceof StepOpenShell)
          && !(content instanceof StepSurfacedOpenShell)
          && !(content instanceof StepOrientedOpenShell)
          && !(content instanceof StepClosedShell)
          && !(content instanceof StepOrientedClosedShell)
          && !(content instanceof StepConnectedFaceSet)
          && !(content instanceof StepConnectedFaceSubSet)
          && !(content instanceof StepFaceBasedSurfaceModel)
          && !(content instanceof StepShellBasedSurfaceModel)
          && !(content instanceof StepGeometricSet)
          && !(content instanceof StepGeometricCurveSet)
          && !(content instanceof StepPointSet)
          && !(content instanceof StepPath)
          && !(content instanceof StepOpenPath)
          && !(content instanceof StepSubpath)
          && !(content instanceof StepOrientedPath)
          && !(content instanceof StepConnectedEdgeSet)
          && !(content instanceof StepPolyLoop)
          && !(content instanceof StepVertexLoop)
          && !(content instanceof StepVertexShell)
          && !(content instanceof StepWireShell)
          && !(content instanceof StepEdgeLoop)
          && !(content instanceof StepEdgeBasedWireframeModel)
          && !(content instanceof StepShellBasedWireframeModel)
          && !(content instanceof StepFaceBasedSurfaceModel)
          && !(content instanceof StepShellBasedSurfaceModel)
          && !(content instanceof StepAnnotationSymbol)
          && !(content instanceof StepAnnotationText)
          && !(content instanceof StepAnnotationTextCharacter)
          && !(content instanceof StepAnnotationFillArea)
          && !(content instanceof StepAnnotationSymbolOccurrence)
          && !(content instanceof StepAnnotationSubfigureOccurrence)
          && !(content instanceof StepAnnotationFillAreaOccurrence)
          && !(content instanceof StepAnnotationPlaceholderOccurrence)
          && !(content instanceof StepAnnotationPlane)
          && !(content instanceof StepAnnotationCurveOccurrence)
          && !(content instanceof StepAnnotationPointOccurrence)
          && !(content instanceof StepDraughtingAnnotationOccurrence)
          && !(content instanceof StepTerminatorSymbol)) {
        throw new UnsupportedStepEntityException(
            entityName
                + " contents must reference supported annotation content, direct point carriers, supported curves/edge carriers, TERMINATOR_SYMBOL, supported face/shell/path/wire/model containers, GEOMETRIC_SET, POINT_SET or GEOMETRIC_CURVE_SET");
      }
    }
    return new StepDraughtingCallout(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        contents,
        entityName);
  }

  StepDraughtingCalloutRelationship resolveDraughtingCalloutRelationship(StepEntityInstance instance) {
    return resolveDraughtingCalloutRelationship(instance, "DRAUGHTING_CALLOUT_RELATIONSHIP");
  }

  StepDraughtingCalloutRelationship resolveDraughtingCalloutRelationship(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepDraughtingCalloutRelationship(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 2),
            StepDraughtingCallout.class,
            entityName + " relating_callout must reference DRAUGHTING_CALLOUT"),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 3),
            StepDraughtingCallout.class,
            entityName + " related_callout must reference DRAUGHTING_CALLOUT"));
  }

  StepDraughtingPreDefinedTerminatorSymbol resolveDraughtingPreDefinedTerminatorSymbol(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "DRAUGHTING_PRE_DEFINED_TERMINATOR_SYMBOL");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepDraughtingPreDefinedTerminatorSymbol(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1));
  }

  StepLeaderCurve resolveLeaderCurve(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "LEADER_CURVE");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    StepEntity item = resolver.resolve(resolver.referenceId(instance, definition, 2));
    if (!resolver.isSupportedAnnotationCurveCarrier(item)) {
      throw new StepResolutionException(
          "LEADER_CURVE item must reference a supported curve, EDGE_CURVE, SUBEDGE, ORIENTED_EDGE, EDGE_LOOP, POLY_LOOP, PATH, OPEN_PATH, SUBPATH, ORIENTED_PATH, CONNECTED_EDGE_SET, WIRE_SHELL, wireframe model or GEOMETRIC_CURVE_SET");
    }
    return new StepLeaderCurve(
        instance.id(),
        resolver.optionalStringValue(instance, definition, 0),
        resolver.referenceList(
            instance,
            definition,
            1,
            StepPresentationStyleAssignment.class,
            "LEADER_CURVE styles must contain PRESENTATION_STYLE_ASSIGNMENT references"),
        item);
  }

  StepProjectionCurve resolveProjectionCurve(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "PROJECTION_CURVE");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    StepEntity item = resolver.resolve(resolver.referenceId(instance, definition, 2));
    if (!resolver.isSupportedAnnotationCurveCarrier(item)) {
      throw new StepResolutionException(
          "PROJECTION_CURVE item must reference a supported curve, EDGE_CURVE, SUBEDGE, ORIENTED_EDGE, EDGE_LOOP, POLY_LOOP, PATH, OPEN_PATH, SUBPATH, ORIENTED_PATH, CONNECTED_EDGE_SET, WIRE_SHELL, wireframe model or GEOMETRIC_CURVE_SET");
    }
    return new StepProjectionCurve(
        instance.id(),
        resolver.optionalStringValue(instance, definition, 0),
        resolver.referenceList(
            instance,
            definition,
            1,
            StepPresentationStyleAssignment.class,
            "PROJECTION_CURVE styles must contain PRESENTATION_STYLE_ASSIGNMENT references"),
        item);
  }

  StepTextLiteralWithDraughtingCallout resolveTextLiteralWithDraughtingCallout(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "TEXT_LITERAL_WITH_DRAUGHTING_CALLOUT");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepTextLiteralWithDraughtingCallout(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  // === Datum Entities ===

  StepCompositeDatumReference resolveCompositeDatumReference(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "COMPOSITE_DATUM_REFERENCE");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    List<StepEntity> datums =
        resolver.entityReferenceList(
            instance, definition, 2,
            "COMPOSITE_DATUM_REFERENCE datums must contain entity references");
    return new StepCompositeDatumReference(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        datums,
        resolver.stringValue(instance, definition, 3));
  }

  StepDatum resolveDatum(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "DATUM");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepDatum(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.booleanValue(instance, definition, 3));
  }

  StepDatumFeature resolveDatumFeature(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "DATUM_FEATURE");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepDatumFeature(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  StepDatumReference resolveDatumReference(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "DATUM_REFERENCE");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepDatumReference(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.integerValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  StepDatumReferenceCompartment resolveDatumReferenceCompartment(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "DATUM_REFERENCE_COMPARTMENT");
    StepEntityResolver.requireParameterCount(instance, definition, 6);
    return new StepDatumReferenceCompartment(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.integerValue(instance, definition, 3),
        resolver.resolve(resolver.referenceId(instance, definition, 4)));
  }

  StepDatumReferenceModifier resolveDatumReferenceModifier(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "DATUM_REFERENCE_MODIFIER");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepDatumReferenceModifier(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  StepDatumReferenceModifierWithSign resolveDatumReferenceModifierWithSign(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "DATUM_REFERENCE_MODIFIER_WITH_SIGN");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepDatumReferenceModifierWithSign(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.stringValue(instance, definition, 2));
  }

  StepDatumSystem resolveDatumSystem(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "DATUM_SYSTEM");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    StepValue datumValue = resolver.unwrapTyped(definition.parameters().get(2));
    List<StepEntity> datums;
    if (datumValue instanceof StepValue.ListValue) {
      StepValue.ListValue listValue = (StepValue.ListValue) datumValue;
      datums = new ArrayList<>(listValue.elements().size());
      for (StepValue element : listValue.elements()) {
        StepValue unwrapped = resolver.unwrapTyped(element);
        if (unwrapped instanceof StepValue.ReferenceValue) {
          StepValue.ReferenceValue ref = (StepValue.ReferenceValue) unwrapped;
          datums.add(resolver.resolve(ref.id()));
        }
      }
    } else if (datumValue instanceof StepValue.ReferenceValue) {
      StepValue.ReferenceValue ref = (StepValue.ReferenceValue) datumValue;
      datums = List.of(resolver.resolve(ref.id()));
    } else {
      throw new StepResolutionException(
          "DATUM_SYSTEM datums must contain entity references");
    }
    StepValue lastValue = resolver.unwrapTyped(definition.parameters().get(3));
    StepEntity tolerance;
    if (lastValue instanceof StepValue.ReferenceValue) {
      StepValue.ReferenceValue ref = (StepValue.ReferenceValue) lastValue;
      tolerance = resolver.resolve(ref.id());
    } else {
      tolerance = null;
    }
    return new StepDatumSystem(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        datums,
        resolver.stringValue(instance, definition, 1),
        tolerance);
  }

  StepDatumSystemReference resolveDatumSystemReference(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "DATUM_SYSTEM_REFERENCE");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepDatumSystemReference(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.integerValue(instance, definition, 2));
  }

  StepDatumTarget resolveDatumTarget(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "DATUM_TARGET");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepDatumTarget(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  StepGeneralizedDatum resolveGeneralizedDatum(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "GENERALIZED_DATUM");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepGeneralizedDatum(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  // === Tolerance Entities ===

  StepFeatureControlFrame resolveFeatureControlFrame(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "FEATURE_CONTROL_FRAME");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    List<StepEntity> datumSystem =
        resolver.entityReferenceList(
            instance, definition, 1,
            "FEATURE_CONTROL_FRAME datum_system must contain entity references");
    return new StepFeatureControlFrame(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        datumSystem,
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  StepCompositeGroupTolerance resolveCompositeGroupTolerance(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "COMPOSITE_GROUP_TOLERANCE");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepCompositeGroupTolerance(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.numberValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  StepGeometricTolerance resolveGeometricTolerance(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "GEOMETRIC_TOLERANCE");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepGeometricTolerance(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.numberValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  StepGeometricTolerance resolveGeometricTolerance(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepGeometricTolerance(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.numberValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  StepGeometricToleranceTarget resolveGeometricToleranceTarget(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "GEOMETRIC_TOLERANCE_TARGET");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepGeometricToleranceTarget(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.numberValue(instance, definition, 2));
  }

  StepLinearToleranceZone resolveLinearToleranceZone(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "LINEAR_TOLERANCE_ZONE");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepLinearToleranceZone(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.numberValue(instance, definition, 3));
  }

  StepPlusMinusTolerance resolvePlusMinusTolerance(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "PLUS_MINUS_TOLERANCE");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepPlusMinusTolerance(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  StepRadialToleranceZone resolveRadialToleranceZone(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "RADIAL_TOLERANCE_ZONE");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepRadialToleranceZone(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.numberValue(instance, definition, 3));
  }

  StepRectangularToleranceZone resolveRectangularToleranceZone(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "RECTANGULAR_TOLERANCE_ZONE");
    StepEntityResolver.requireParameterCount(instance, definition, 6);
    return new StepRectangularToleranceZone(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.optionalNumberValue(instance, definition, 3),
        resolver.optionalNumberValue(instance, definition, 4));
  }

  StepRunoutToleranceZone resolveRunoutToleranceZone(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "RUNOUT_TOLERANCE_ZONE");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepRunoutToleranceZone(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)));
  }

  StepToleranceModifier resolveToleranceModifier(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "TOLERANCE_MODIFIER");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepToleranceModifier(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.numberValue(instance, definition, 2),
        resolver.resolve(resolver.referenceId(instance, definition, 3)));
  }

  StepTolerancePair resolveTolerancePair(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "TOLERANCE_PAIR");
    StepEntityResolver.requireParameterCount(instance, definition, 6);
    return new StepTolerancePair(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.optionalNumberValue(instance, definition, 1),
        resolver.optionalNumberValue(instance, definition, 2),
        resolver.resolve(resolver.referenceId(instance, definition, 3)),
        resolver.stringValue(instance, definition, 4));
  }

  StepToleranceSet resolveToleranceSet(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "TOLERANCE_SET");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    List<StepEntity> tolerances =
        resolver.entityReferenceList(
            instance, definition, 1,
            "TOLERANCE_SET tolerances must contain entity references");
    return new StepToleranceSet(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        tolerances,
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.resolve(resolver.referenceId(instance, definition, 3)));
  }

  StepToleranceValue resolveToleranceValue(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "TOLERANCE_VALUE");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepToleranceValue(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.numberValue(instance, definition, 1),
        resolver.numberValue(instance, definition, 2));
  }

  StepToleranceZone resolveToleranceZone(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "TOLERANCE_ZONE");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepToleranceZone(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)));
  }

  StepToleranceZoneForm resolveToleranceZoneForm(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "TOLERANCE_ZONE_FORM");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepToleranceZoneForm(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.enumValue(instance, definition, 1));
  }

  // === Dimension Entities ===

  StepAngularDimensionRepresentation resolveAngularDimensionRepresentation(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "ANGULAR_DIMENSION_REPRESENTATION");
    StepEntityResolver.requireParameterCount(instance, definition, 6);
    return new StepAngularDimensionRepresentation(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.entityReferenceList(instance, definition, 1,
            "ANGULAR_DIMENSION_REPRESENTATION items must contain entity references"),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.optionalNumberValue(instance, definition, 3),
        resolver.resolve(resolver.referenceId(instance, definition, 4)));
  }

  StepChainDimensionRepresentation resolveChainDimensionRepresentation(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CHAIN_DIMENSION_REPRESENTATION");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepChainDimensionRepresentation(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.entityReferenceList(instance, definition, 1,
            "CHAIN_DIMENSION_REPRESENTATION items must contain entity references"),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.resolve(resolver.referenceId(instance, definition, 3)));
  }

  StepDimensionalLocation resolveDimensionalLocation(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "DIMENSIONAL_LOCATION");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepDimensionalLocation(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  StepDimensionalMeasurement resolveDimensionalMeasurement(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "DIMENSIONAL_MEASUREMENT");
    StepEntityResolver.requireParameterCount(instance, definition, 9);
    return new StepDimensionalMeasurement(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.resolve(resolver.referenceId(instance, definition, 1)),
        resolver.stringValue(instance, definition, 2),
        resolver.numberValue(instance, definition, 3),
        resolver.numberValue(instance, definition, 4),
        resolver.numberValue(instance, definition, 5),
        resolver.numberValue(instance, definition, 6),
        resolver.resolve(resolver.referenceId(instance, definition, 7)));
  }

  StepDimensionalSize resolveDimensionalSize(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "DIMENSIONAL_SIZE");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    return new StepDimensionalSize(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  StepDirectedDimensionalSize resolveDirectedDimensionalSize(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "DIRECTED_DIMENSIONAL_SIZE");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepDirectedDimensionalSize(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.numberValue(instance, definition, 1),
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

  StepLinearDimensionRepresentation resolveLinearDimensionRepresentation(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "LINEAR_DIMENSION_REPRESENTATION");
    StepEntityResolver.requireParameterCount(instance, definition, 6);
    return new StepLinearDimensionRepresentation(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.entityReferenceList(instance, definition, 1,
            "LINEAR_DIMENSION_REPRESENTATION items must contain entity references"),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.optionalNumberValue(instance, definition, 3),
        resolver.resolve(resolver.referenceId(instance, definition, 4)));
  }

  StepOrdinateDimensionRepresentation resolveOrdinateDimensionRepresentation(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "ORDINATE_DIMENSION_REPRESENTATION");
    StepEntityResolver.requireParameterCount(instance, definition, 6);
    return new StepOrdinateDimensionRepresentation(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.entityReferenceList(instance, definition, 1,
            "ORDINATE_DIMENSION_REPRESENTATION items must contain entity references"),
        resolver.resolve(resolver.referenceId(instance, definition, 2)),
        resolver.resolve(resolver.referenceId(instance, definition, 3)),
        resolver.resolve(resolver.referenceId(instance, definition, 4)));
  }

  StepRangeDimensionalSize resolveRangeDimensionalSize(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "RANGE_DIMENSIONAL_SIZE");
    StepEntityResolver.requireParameterCount(instance, definition, 5);
    return new StepRangeDimensionalSize(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.stringValue(instance, definition, 1),
        resolver.numberValue(instance, definition, 2),
        resolver.numberValue(instance, definition, 3));
  }

  StepShapeDimensionRepresentation resolveShapeDimensionRepresentation(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SHAPE_DIMENSION_REPRESENTATION");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    List<StepEntity> items =
        resolver.entityReferenceList(
            instance, definition, 1,
            "SHAPE_DIMENSION_REPRESENTATION items must contain entity references");
    return new StepShapeDimensionRepresentation(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        items,
        resolver.resolve(resolver.referenceId(instance, definition, 2)));
  }

}
