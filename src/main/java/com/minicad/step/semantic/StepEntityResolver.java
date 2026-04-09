package com.minicad.step.semantic;

import com.minicad.common.StepResolutionException;
import com.minicad.common.UnsupportedStepEntityException;
import com.minicad.step.model.StepAdvancedFace;
import com.minicad.step.model.StepAnnotationTextOccurrence;
import com.minicad.step.model.StepApplicationContext;
import com.minicad.step.model.StepApplicationProtocolDefinition;
import com.minicad.step.model.StepAxis1Placement;
import com.minicad.step.model.StepAxis2Placement2D;
import com.minicad.step.model.StepAxis2Placement3D;
import com.minicad.step.model.StepBoundedCurve;
import com.minicad.step.model.StepBoundedSurface;
import com.minicad.step.model.StepBezierCurve;
import com.minicad.step.model.StepBezierSurface;
import com.minicad.step.model.StepBooleanClippingResult;
import com.minicad.step.model.StepBooleanResult;
import com.minicad.step.model.StepBrepWithVoids;
import com.minicad.step.model.StepBSplineCurve;
import com.minicad.step.model.StepCartesianPoint;
import com.minicad.step.model.StepBSplineCurveWithKnots;
import com.minicad.step.model.StepBSplineSurface;
import com.minicad.step.model.StepBSplineSurfaceWithKnots;
import com.minicad.step.model.StepRationalBSplineCurve;
import com.minicad.step.model.StepRationalBSplineSurface;
import com.minicad.step.model.StepCircle;
import com.minicad.step.model.StepClosedShell;
import com.minicad.step.model.StepColourRgb;
import com.minicad.step.model.StepConnectedEdgeSet;
import com.minicad.step.model.StepConnectedFaceSet;
import com.minicad.step.model.StepConnectedFaceSubSet;
import com.minicad.step.model.StepCompositeCurve;
import com.minicad.step.model.StepCompositeCurveOnSurface;
import com.minicad.step.model.StepCompositeCurveSegment;
import com.minicad.step.model.StepConicalSurface;
import com.minicad.step.model.StepConversionBasedUnit;
import com.minicad.step.model.StepConversionBasedUnitWithOffset;
import com.minicad.step.model.StepContextDependentUnit;
import com.minicad.step.model.StepCylindricalSurface;
import com.minicad.step.model.StepCurveStyle;
import com.minicad.step.model.StepDegeneratePcurve;
import com.minicad.step.model.StepDirection;
import com.minicad.step.model.StepDerivedUnit;
import com.minicad.step.model.StepDerivedUnitElement;
import com.minicad.step.model.StepDescriptiveRepresentationItem;
import com.minicad.step.model.StepDraughtingPreDefinedColour;
import com.minicad.step.model.StepDraughtingPreDefinedCurveFont;
import com.minicad.step.model.StepDraughtingCallout;
import com.minicad.step.model.StepEdgeCurve;
import com.minicad.step.model.StepEdgeBasedWireframeModel;
import com.minicad.step.model.StepEdgeLoop;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepFaceEntity;
import com.minicad.step.model.StepFaceBound;
import com.minicad.step.model.StepFaceBasedSurfaceModel;
import com.minicad.step.model.StepFaceSurface;
import com.minicad.step.model.StepFillAreaStyle;
import com.minicad.step.model.StepFillAreaStyleColour;
import com.minicad.step.model.StepGeometricCurveSet;
import com.minicad.step.model.StepGeometricSet;
import com.minicad.step.model.StepGeometricItemSpecificUsage;
import com.minicad.step.model.StepGeometricRepresentationContext;
import com.minicad.step.model.StepGlobalUncertaintyAssignedContext;
import com.minicad.step.model.StepGlobalUnitAssignedContext;
import com.minicad.step.model.StepItemDefinedTransformation;
import com.minicad.step.model.StepLine;
import com.minicad.step.model.StepManifoldSolidBrep;
import com.minicad.step.model.StepMeasureWithUnit;
import com.minicad.step.model.StepMeasureRepresentationItem;
import com.minicad.step.model.StepNamedUnit;
import com.minicad.step.model.StepOpenShell;
import com.minicad.step.model.StepOpenPath;
import com.minicad.step.model.StepOverRidingStyledItem;
import com.minicad.step.model.StepOrientedEdge;
import com.minicad.step.model.StepOrientedFace;
import com.minicad.step.model.StepOrientedClosedShell;
import com.minicad.step.model.StepOrientedOpenShell;
import com.minicad.step.model.StepOrientedPath;
import com.minicad.step.model.StepPath;
import com.minicad.step.model.StepPlane;
import com.minicad.step.model.StepPolyLoop;
import com.minicad.step.model.StepPolyline;
import com.minicad.step.model.StepPcurve;
import com.minicad.step.model.StepEllipse;
import com.minicad.step.model.StepProduct;
import com.minicad.step.model.StepProductContext;
import com.minicad.step.model.StepProductDefinition;
import com.minicad.step.model.StepProductDefinitionContext;
import com.minicad.step.model.StepProductDefinitionFormation;
import com.minicad.step.model.StepProductDefinitionShape;
import com.minicad.step.model.StepProductRelatedProductCategory;
import com.minicad.step.model.StepPropertyDefinition;
import com.minicad.step.model.StepPropertyDefinitionRepresentation;
import com.minicad.step.model.StepPresentationLayerAssignment;
import com.minicad.step.model.StepPresentationStyleAssignment;
import com.minicad.step.model.StepPoint;
import com.minicad.step.model.StepPointSet;
import com.minicad.step.model.StepPiecewiseBezierCurve;
import com.minicad.step.model.StepPiecewiseBezierSurface;
import com.minicad.step.model.StepRepresentation;
import com.minicad.step.model.StepRepresentationItem;
import com.minicad.step.model.StepRepresentationRelationship;
import com.minicad.step.model.StepRepresentationContext;
import com.minicad.step.model.StepRepresentationRelationshipWithTransformation;
import com.minicad.step.model.StepCurve;
import com.minicad.step.model.StepGeometricRepresentationItem;
import com.minicad.step.model.StepShapeRepresentationRelationship;
import com.minicad.step.model.StepShapeDefinitionRepresentation;
import com.minicad.step.model.StepShellBasedSurfaceModel;
import com.minicad.step.model.StepSeamCurve;
import com.minicad.step.model.StepSiUnit;
import com.minicad.step.model.StepSolidModel;
import com.minicad.step.model.StepSurface;
import com.minicad.step.model.StepSurfaceCurve;
import com.minicad.step.model.StepSurfacedOpenShell;
import com.minicad.step.model.StepSurfaceModel;
import com.minicad.step.model.StepSurfaceOfLinearExtrusion;
import com.minicad.step.model.StepSurfaceOfRevolution;
import com.minicad.step.model.StepSphericalSurface;
import com.minicad.step.model.StepSurfaceSideStyle;
import com.minicad.step.model.StepSurfaceStyleFillArea;
import com.minicad.step.model.StepSurfaceStyleUsage;
import com.minicad.step.model.StepStyledItem;
import com.minicad.step.model.StepSubedge;
import com.minicad.step.model.StepSubpath;
import com.minicad.step.model.StepTypedMeasureWithUnit;
import com.minicad.step.model.StepTopologicalRepresentationItem;
import com.minicad.step.model.StepTrimmedCurve;
import com.minicad.step.model.StepToroidalSurface;
import com.minicad.step.model.StepUniformCurve;
import com.minicad.step.model.StepQuasiUniformCurve;
import com.minicad.step.model.StepUniformSurface;
import com.minicad.step.model.StepQuasiUniformSurface;
import com.minicad.step.model.StepUncertaintyMeasureWithUnit;
import com.minicad.step.model.StepNextAssemblyUsageOccurrence;
import com.minicad.step.model.StepOffsetCurve3D;
import com.minicad.step.model.StepOffsetSurface;
import com.minicad.step.model.StepContextDependentShapeRepresentation;
import com.minicad.step.model.StepFace;
import com.minicad.step.model.StepEdge;
import com.minicad.step.model.StepLoop;
import com.minicad.step.model.StepVector;
import com.minicad.step.model.StepVertex;
import com.minicad.step.model.StepVertexLoop;
import com.minicad.step.model.StepVertexPoint;
import com.minicad.step.model.StepVertexShell;
import com.minicad.step.model.StepValueRepresentationItem;
import com.minicad.step.model.StepWireShell;
import com.minicad.step.model.StepShellBasedWireframeModel;
import com.minicad.step.syntax.StepEntityDefinition;
import com.minicad.step.syntax.StepEntityInstance;
import com.minicad.step.syntax.StepFile;
import com.minicad.step.syntax.StepValue;

import java.util.*;
import java.util.stream.Collectors;

/** Resolves raw STEP AST entities into a minimal semantic model. */
public final class StepEntityResolver {

  private static final Map<String, EntityFactory> REGISTRY = createRegistry();

  private final Map<Integer, StepEntityInstance> instancesById;
  private final Map<Integer, StepEntity> resolved = new LinkedHashMap<>();

  private StepEntityResolver(StepFile file) {
    this.instancesById = file.entitiesById();
  }

  /**
   * Resolves all supported entities in the file.
   *
   * @param file parsed STEP file
   * @return resolved entities indexed by id
   */
  public static Map<Integer, StepEntity> resolveAll(StepFile file) {
    return new StepEntityResolver(file).resolveAll();
  }

  private Map<Integer, StepEntity> resolveAll() {
    for (Integer id : instancesById.keySet()) {
      resolve(id);
    }
    return Map.copyOf(resolved);
  }

  private StepEntity resolve(int id) {
    StepEntity existing = resolved.get(id);
    if (existing != null) {
      return existing;
    }

    StepEntityInstance instance = instancesById.get(id);
    if (instance == null) {
      throw new StepResolutionException("missing referenced entity #" + id);
    }

    for (Map.Entry<String, EntityFactory> entry : REGISTRY.entrySet()) {
      if (!instance.hasDefinition(entry.getKey())) {
        continue;
      }
      StepEntity entity = entry.getValue().create(this, instance);
      resolved.put(id, entity);
      return entity;
    }

    throw new UnsupportedStepEntityException("unsupported STEP entity " + instance.name());
  }

  private StepCartesianPoint resolveCartesianPoint(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CARTESIAN_POINT");
    requireParameterCount(instance, definition, 2);
    return new StepCartesianPoint(
        instance.id(),
        stringValue(instance, definition, 0),
        coordinateList(instance, definition, 1, 2, 3));
  }

  private StepDirection resolveDirection(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DIRECTION");
    requireParameterCount(instance, definition, 2);
    return new StepDirection(
        instance.id(),
        stringValue(instance, definition, 0),
        coordinateList(instance, definition, 1, 2, 3));
  }

  private StepVector resolveVector(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "VECTOR");
    requireParameterCount(instance, definition, 3);
    return new StepVector(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepDirection.class,
            "VECTOR orientation must reference DIRECTION"),
        numberValue(instance, definition, 2));
  }

  private StepAxis2Placement3D resolveAxis2Placement3D(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "AXIS2_PLACEMENT_3D");
    requireParameterCount(instance, definition, 4);
    if (isUnset(definition.parameters().get(2)) || isUnset(definition.parameters().get(3))) {
      throw new UnsupportedStepEntityException(
          "AXIS2_PLACEMENT_3D requires explicit axis and ref direction");
    }
    return new StepAxis2Placement3D(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepCartesianPoint.class,
            "AXIS2_PLACEMENT_3D location must reference CARTESIAN_POINT"),
        requireEntity(
            referenceId(instance, definition, 2),
            StepDirection.class,
            "AXIS2_PLACEMENT_3D axis must reference DIRECTION"),
        requireEntity(
            referenceId(instance, definition, 3),
            StepDirection.class,
            "AXIS2_PLACEMENT_3D ref direction must reference DIRECTION"));
  }

  private StepAxis1Placement resolveAxis1Placement(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "AXIS1_PLACEMENT");
    requireParameterCount(instance, definition, 3);
    return new StepAxis1Placement(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepCartesianPoint.class,
            "AXIS1_PLACEMENT location must reference CARTESIAN_POINT"),
        requireEntity(
            referenceId(instance, definition, 2),
            StepDirection.class,
            "AXIS1_PLACEMENT axis must reference DIRECTION"));
  }

  private StepAxis2Placement2D resolveAxis2Placement2D(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "AXIS2_PLACEMENT_2D");
    requireParameterCount(instance, definition, 3);
    return new StepAxis2Placement2D(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepCartesianPoint.class,
            "AXIS2_PLACEMENT_2D location must reference CARTESIAN_POINT"),
        requireEntity(
            referenceId(instance, definition, 2),
            StepDirection.class,
            "AXIS2_PLACEMENT_2D ref direction must reference DIRECTION"));
  }

  private StepLine resolveLine(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "LINE");
    requireParameterCount(instance, definition, 3);
    return new StepLine(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepCartesianPoint.class,
            "LINE point must reference CARTESIAN_POINT"),
        requireEntity(
            referenceId(instance, definition, 2),
            StepVector.class,
            "LINE vector must reference VECTOR"));
  }

  private StepPolyline resolvePolyline(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "POLYLINE");
    requireParameterCount(instance, definition, 2);
    return new StepPolyline(
        instance.id(),
        stringValue(instance, definition, 0),
        referenceList(
            instance,
            definition,
            1,
            StepCartesianPoint.class,
            "POLYLINE points must reference CARTESIAN_POINT"));
  }

  private StepPlane resolvePlane(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PLANE");
    requireParameterCount(instance, definition, 2);
    return new StepPlane(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepAxis2Placement3D.class,
            "PLANE position must reference AXIS2_PLACEMENT_3D"));
  }

  private StepCircle resolveCircle(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CIRCLE");
    requireParameterCount(instance, definition, 3);
    StepEntity position = resolve(referenceId(instance, definition, 1));
    if (!(position instanceof StepAxis2Placement3D)
        && !(position instanceof StepAxis2Placement2D)) {
      throw new StepResolutionException(
          "CIRCLE position must reference AXIS2_PLACEMENT_3D or AXIS2_PLACEMENT_2D");
    }
    return new StepCircle(
        instance.id(),
        stringValue(instance, definition, 0),
        position,
        numberValue(instance, definition, 2));
  }

  private StepEllipse resolveEllipse(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ELLIPSE");
    requireParameterCount(instance, definition, 4);
    StepEntity position = resolve(referenceId(instance, definition, 1));
    if (!(position instanceof StepAxis2Placement3D)
        && !(position instanceof StepAxis2Placement2D)) {
      throw new StepResolutionException(
          "ELLIPSE position must reference AXIS2_PLACEMENT_3D or AXIS2_PLACEMENT_2D");
    }
    return new StepEllipse(
        instance.id(),
        stringValue(instance, definition, 0),
        position,
        numberValue(instance, definition, 2),
        numberValue(instance, definition, 3));
  }

  private StepCylindricalSurface resolveCylindricalSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CYLINDRICAL_SURFACE");
    requireParameterCount(instance, definition, 3);
    return new StepCylindricalSurface(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepAxis2Placement3D.class,
            "CYLINDRICAL_SURFACE position must reference AXIS2_PLACEMENT_3D"),
        numberValue(instance, definition, 2));
  }

  private StepConicalSurface resolveConicalSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CONICAL_SURFACE");
    requireParameterCount(instance, definition, 4);
    return new StepConicalSurface(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepAxis2Placement3D.class,
            "CONICAL_SURFACE position must reference AXIS2_PLACEMENT_3D"),
        numberValue(instance, definition, 2),
        numberValue(instance, definition, 3));
  }

  private StepToroidalSurface resolveToroidalSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "TOROIDAL_SURFACE");
    requireParameterCount(instance, definition, 4);
    return new StepToroidalSurface(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepAxis2Placement3D.class,
            "TOROIDAL_SURFACE position must reference AXIS2_PLACEMENT_3D"),
        numberValue(instance, definition, 2),
        numberValue(instance, definition, 3));
  }

  private StepSphericalSurface resolveSphericalSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SPHERICAL_SURFACE");
    requireParameterCount(instance, definition, 3);
    return new StepSphericalSurface(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepAxis2Placement3D.class,
            "SPHERICAL_SURFACE position must reference AXIS2_PLACEMENT_3D"),
        numberValue(instance, definition, 2));
  }

  private StepSurfaceOfLinearExtrusion resolveSurfaceOfLinearExtrusion(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SURFACE_OF_LINEAR_EXTRUSION");
    requireParameterCount(instance, definition, 3);
    StepEntity sweptCurve = resolve(referenceId(instance, definition, 1));
    if (!(sweptCurve instanceof StepLine)
        && !(sweptCurve instanceof StepCircle)
        && !(sweptCurve instanceof StepEllipse)
        && !(sweptCurve instanceof StepBSplineCurveWithKnots)
        && !(sweptCurve instanceof StepTrimmedCurve)
        && !(sweptCurve instanceof StepSurfaceCurve)) {
      throw new UnsupportedStepEntityException(
          "SURFACE_OF_LINEAR_EXTRUSION swept_curve must be LINE, CIRCLE, ELLIPSE, B_SPLINE_CURVE_WITH_KNOTS, TRIMMED_CURVE or SURFACE_CURVE");
    }
    return new StepSurfaceOfLinearExtrusion(
        instance.id(),
        stringValue(instance, definition, 0),
        sweptCurve,
        requireEntity(
            referenceId(instance, definition, 2),
            StepVector.class,
            "SURFACE_OF_LINEAR_EXTRUSION extrusion_axis must reference VECTOR"));
  }

  private StepSurfaceOfRevolution resolveSurfaceOfRevolution(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SURFACE_OF_REVOLUTION");
    requireParameterCount(instance, definition, 3);
    StepEntity sweptCurve = resolve(referenceId(instance, definition, 1));
    if (!(sweptCurve instanceof StepLine)
        && !(sweptCurve instanceof StepCircle)
        && !(sweptCurve instanceof StepEllipse)
        && !(sweptCurve instanceof StepBSplineCurveWithKnots)
        && !(sweptCurve instanceof StepTrimmedCurve)
        && !(sweptCurve instanceof StepSurfaceCurve)) {
      throw new UnsupportedStepEntityException(
          "SURFACE_OF_REVOLUTION swept_curve must be LINE, CIRCLE, ELLIPSE, B_SPLINE_CURVE_WITH_KNOTS, TRIMMED_CURVE or SURFACE_CURVE");
    }
    return new StepSurfaceOfRevolution(
        instance.id(),
        stringValue(instance, definition, 0),
        sweptCurve,
        requireEntity(
            referenceId(instance, definition, 2),
            StepAxis1Placement.class,
            "SURFACE_OF_REVOLUTION axis_position must reference AXIS1_PLACEMENT"));
  }

  private StepOffsetCurve3D resolveOffsetCurve3D(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "OFFSET_CURVE_3D");
    requireParameterCount(instance, definition, 5);
    StepEntity basisCurve = resolve(referenceId(instance, definition, 1));
    if (!(basisCurve instanceof StepLine)
        && !(basisCurve instanceof StepCircle)
        && !(basisCurve instanceof StepEllipse)
        && !(basisCurve instanceof StepCurve)
        && !(basisCurve instanceof StepBoundedCurve)
        && !(basisCurve instanceof StepBSplineCurve)
        && !(basisCurve instanceof StepBSplineCurveWithKnots)
        && !(basisCurve instanceof StepRationalBSplineCurve)
        && !(basisCurve instanceof StepBezierCurve)
        && !(basisCurve instanceof StepPiecewiseBezierCurve)
        && !(basisCurve instanceof StepUniformCurve)
        && !(basisCurve instanceof StepQuasiUniformCurve)
        && !(basisCurve instanceof StepTrimmedCurve)
        && !(basisCurve instanceof StepSurfaceCurve)
        && !(basisCurve instanceof StepSeamCurve)) {
      throw new UnsupportedStepEntityException(
          "OFFSET_CURVE_3D basis_curve must reference a supported curve");
    }
    return new StepOffsetCurve3D(
        instance.id(),
        stringValue(instance, definition, 0),
        basisCurve,
        numberValue(instance, definition, 2),
        booleanValue(instance, definition, 3),
        requireEntity(
            referenceId(instance, definition, 4),
            StepDirection.class,
            "OFFSET_CURVE_3D ref_direction must reference DIRECTION"));
  }

  private StepOffsetSurface resolveOffsetSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "OFFSET_SURFACE");
    requireParameterCount(instance, definition, 4);
    StepEntity basisSurface = resolve(referenceId(instance, definition, 1));
    if (!(basisSurface instanceof StepPlane)
        && !(basisSurface instanceof StepSurface)
        && !(basisSurface instanceof StepBoundedSurface)
        && !(basisSurface instanceof StepBSplineSurface)
        && !(basisSurface instanceof StepBSplineSurfaceWithKnots)
        && !(basisSurface instanceof StepRationalBSplineSurface)
        && !(basisSurface instanceof StepBezierSurface)
        && !(basisSurface instanceof StepPiecewiseBezierSurface)
        && !(basisSurface instanceof StepUniformSurface)
        && !(basisSurface instanceof StepQuasiUniformSurface)
        && !(basisSurface instanceof StepCylindricalSurface)
        && !(basisSurface instanceof StepConicalSurface)
        && !(basisSurface instanceof StepToroidalSurface)
        && !(basisSurface instanceof StepSphericalSurface)
        && !(basisSurface instanceof StepSurfaceOfLinearExtrusion)
        && !(basisSurface instanceof StepSurfaceOfRevolution)) {
      throw new UnsupportedStepEntityException(
          "OFFSET_SURFACE basis_surface must reference a supported surface");
    }
    return new StepOffsetSurface(
        instance.id(),
        stringValue(instance, definition, 0),
        basisSurface,
        numberValue(instance, definition, 2),
        booleanValue(instance, definition, 3));
  }

  private StepCompositeCurveSegment resolveCompositeCurveSegment(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "COMPOSITE_CURVE_SEGMENT");
    requireParameterCount(instance, definition, 3);
    StepEntity parentCurve = resolve(referenceId(instance, definition, 2));
    if (!(parentCurve instanceof StepLine)
        && !(parentCurve instanceof StepCircle)
        && !(parentCurve instanceof StepEllipse)
        && !(parentCurve instanceof StepOffsetCurve3D)
        && !(parentCurve instanceof StepCompositeCurve)
        && !(parentCurve instanceof StepCompositeCurveOnSurface)
        && !(parentCurve instanceof StepCurve)
        && !(parentCurve instanceof StepBoundedCurve)
        && !(parentCurve instanceof StepBSplineCurve)
        && !(parentCurve instanceof StepBSplineCurveWithKnots)
        && !(parentCurve instanceof StepRationalBSplineCurve)
        && !(parentCurve instanceof StepBezierCurve)
        && !(parentCurve instanceof StepPiecewiseBezierCurve)
        && !(parentCurve instanceof StepUniformCurve)
        && !(parentCurve instanceof StepQuasiUniformCurve)
        && !(parentCurve instanceof StepTrimmedCurve)
        && !(parentCurve instanceof StepPcurve)
        && !(parentCurve instanceof StepSurfaceCurve)
        && !(parentCurve instanceof StepSeamCurve)) {
      throw new UnsupportedStepEntityException(
          "COMPOSITE_CURVE_SEGMENT parent_curve must reference a supported curve");
    }
    return new StepCompositeCurveSegment(
        instance.id(),
        enumValue(instance, definition, 0),
        booleanValue(instance, definition, 1),
        parentCurve);
  }

  private StepCompositeCurve resolveCompositeCurve(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "COMPOSITE_CURVE");
    requireParameterCount(instance, definition, 3);
    return new StepCompositeCurve(
        instance.id(),
        stringValue(instance, definition, 0),
        referenceList(
            instance,
            definition,
            1,
            StepCompositeCurveSegment.class,
            "COMPOSITE_CURVE segments must contain COMPOSITE_CURVE_SEGMENT references"),
        booleanValue(instance, definition, 2));
  }

  private StepCompositeCurveOnSurface resolveCompositeCurveOnSurface(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "COMPOSITE_CURVE_ON_SURFACE");
    requireParameterCount(instance, definition, 3);
    return new StepCompositeCurveOnSurface(
        instance.id(),
        stringValue(instance, definition, 0),
        referenceList(
            instance,
            definition,
            1,
            StepCompositeCurveSegment.class,
            "COMPOSITE_CURVE_ON_SURFACE segments must contain COMPOSITE_CURVE_SEGMENT references"),
        booleanValue(instance, definition, 2));
  }

  private StepTrimmedCurve resolveTrimmedCurve(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "TRIMMED_CURVE");
    requireParameterCount(instance, definition, 6);
    StepEntity basisCurve = resolve(referenceId(instance, definition, 1));
    if (!(basisCurve instanceof StepLine)
        && !(basisCurve instanceof StepCircle)
        && !(basisCurve instanceof StepEllipse)
        && !(basisCurve instanceof StepSurfaceCurve)
        && !(basisCurve instanceof StepBSplineCurveWithKnots)) {
      throw new UnsupportedStepEntityException(
          "TRIMMED_CURVE basis curve must be LINE, CIRCLE, ELLIPSE, SURFACE_CURVE or B_SPLINE_CURVE_WITH_KNOTS");
    }
    return new StepTrimmedCurve(
        instance.id(),
        stringValue(instance, definition, 0),
        basisCurve,
        entityReferenceList(
            instance, definition, 2, "TRIMMED_CURVE trim_1 must contain entity references"),
        entityReferenceList(
            instance, definition, 3, "TRIMMED_CURVE trim_2 must contain entity references"),
        booleanValue(instance, definition, 4),
        enumValue(instance, definition, 5));
  }

  private StepSurfaceCurve resolveSurfaceCurve(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SURFACE_CURVE");
    requireParameterCount(instance, definition, 4);
    StepEntity curve3d = resolve(referenceId(instance, definition, 1));
    if (!(curve3d instanceof StepLine)
        && !(curve3d instanceof StepCircle)
        && !(curve3d instanceof StepEllipse)
        && !(curve3d instanceof StepBSplineCurveWithKnots)) {
      throw new UnsupportedStepEntityException(
          "SURFACE_CURVE curve_3d must be LINE, CIRCLE, ELLIPSE or B_SPLINE_CURVE_WITH_KNOTS");
    }
    List<StepEntity> associatedGeometry =
        entityReferenceList(
            instance,
            definition,
            2,
            "SURFACE_CURVE associated_geometry must contain entity references");
    for (StepEntity associated : associatedGeometry) {
      if (!(associated instanceof StepPcurve) && !(associated instanceof StepDegeneratePcurve)) {
        throw new UnsupportedStepEntityException(
            "SURFACE_CURVE associated_geometry currently supports PCURVE or DEGENERATE_PCURVE references");
      }
    }
    return new StepSurfaceCurve(
        instance.id(),
        stringValue(instance, definition, 0),
        curve3d,
        associatedGeometry,
        enumValue(instance, definition, 3));
  }

  private StepSeamCurve resolveSeamCurve(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SEAM_CURVE");
    requireParameterCount(instance, definition, 4);
    StepEntity curve3d = resolve(referenceId(instance, definition, 1));
    if (!(curve3d instanceof StepLine)
        && !(curve3d instanceof StepCircle)
        && !(curve3d instanceof StepEllipse)
        && !(curve3d instanceof StepBSplineCurveWithKnots)) {
      throw new UnsupportedStepEntityException(
          "SEAM_CURVE curve_3d must be LINE, CIRCLE, ELLIPSE or B_SPLINE_CURVE_WITH_KNOTS");
    }
    List<StepEntity> associatedGeometry =
        entityReferenceList(
            instance,
            definition,
            2,
            "SEAM_CURVE associated_geometry must contain entity references");
    if (associatedGeometry.size() != 2) {
      throw new UnsupportedStepEntityException(
          "SEAM_CURVE associated_geometry must contain exactly two PCURVE references");
    }
    for (StepEntity associated : associatedGeometry) {
      if (!(associated instanceof StepPcurve) && !(associated instanceof StepDegeneratePcurve)) {
        throw new UnsupportedStepEntityException(
            "SEAM_CURVE associated_geometry currently supports PCURVE or DEGENERATE_PCURVE references");
      }
    }
    return new StepSeamCurve(
        instance.id(),
        stringValue(instance, definition, 0),
        curve3d,
        associatedGeometry,
        enumValue(instance, definition, 3));
  }

  private StepPcurve resolvePcurve(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PCURVE");
    requireParameterCount(instance, definition, 3);
    StepEntity basisSurface = resolve(referenceId(instance, definition, 1));
    if (!(basisSurface instanceof StepPlane)
        && !(basisSurface instanceof StepCylindricalSurface)
        && !(basisSurface instanceof StepConicalSurface)
        && !(basisSurface instanceof StepToroidalSurface)
        && !(basisSurface instanceof StepSurfaceOfLinearExtrusion)
        && !(basisSurface instanceof StepSurfaceOfRevolution)
        && !(basisSurface instanceof StepBSplineSurfaceWithKnots)) {
      throw new UnsupportedStepEntityException(
          "PCURVE basis surface must reference a supported surface");
    }
    StepRepresentation representation =
        requireEntity(
            referenceId(instance, definition, 2),
            StepRepresentation.class,
            "PCURVE reference_to_curve must reference REPRESENTATION");
    if (representation.items().size() != 1) {
      throw new UnsupportedStepEntityException(
          "PCURVE reference_to_curve must contain exactly one 2D curve item");
    }
    StepEntity item = representation.items().getFirst();
    if (!(item instanceof StepLine)
        && !(item instanceof StepCircle)
        && !(item instanceof StepEllipse)
        && !(item instanceof StepBSplineCurveWithKnots)
        && !(item instanceof StepTrimmedCurve)) {
      throw new UnsupportedStepEntityException(
          "PCURVE currently supports 2D LINE, CIRCLE, ELLIPSE, B_SPLINE_CURVE_WITH_KNOTS or TRIMMED_CURVE items");
    }
    return new StepPcurve(
        instance.id(), stringValue(instance, definition, 0), basisSurface, representation);
  }

  private StepDegeneratePcurve resolveDegeneratePcurve(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DEGENERATE_PCURVE");
    requireParameterCount(instance, definition, 3);
    StepEntity basisSurface = resolve(referenceId(instance, definition, 1));
    if (!(basisSurface instanceof StepPlane)
        && !(basisSurface instanceof StepCylindricalSurface)
        && !(basisSurface instanceof StepConicalSurface)
        && !(basisSurface instanceof StepToroidalSurface)
        && !(basisSurface instanceof StepSurfaceOfLinearExtrusion)
        && !(basisSurface instanceof StepSurfaceOfRevolution)
        && !(basisSurface instanceof StepBSplineSurfaceWithKnots)) {
      throw new UnsupportedStepEntityException(
          "DEGENERATE_PCURVE basis surface must reference a supported surface");
    }
    StepRepresentation representation =
        requireEntity(
            referenceId(instance, definition, 2),
            StepRepresentation.class,
            "DEGENERATE_PCURVE reference_to_curve must reference REPRESENTATION");
    if (representation.items().size() != 1) {
      throw new UnsupportedStepEntityException(
          "DEGENERATE_PCURVE reference_to_curve must contain exactly one 2D curve item");
    }
    StepEntity item = representation.items().getFirst();
    if (!(item instanceof StepLine)
        && !(item instanceof StepCircle)
        && !(item instanceof StepEllipse)
        && !(item instanceof StepBSplineCurveWithKnots)
        && !(item instanceof StepTrimmedCurve)) {
      throw new UnsupportedStepEntityException(
          "DEGENERATE_PCURVE currently supports 2D LINE, CIRCLE, ELLIPSE, B_SPLINE_CURVE_WITH_KNOTS or TRIMMED_CURVE items");
    }
    return new StepDegeneratePcurve(
        instance.id(), stringValue(instance, definition, 0), basisSurface, representation);
  }

  private StepBSplineCurveWithKnots resolveBSplineCurveWithKnots(StepEntityInstance instance) {
    StepEntityDefinition spline = definition(instance, "B_SPLINE_CURVE_WITH_KNOTS");
    if (instance.hasDefinition("B_SPLINE_CURVE")) {
      requireParameterCount(instance, spline, 3);
      StepEntityDefinition base = definition(instance, "B_SPLINE_CURVE");
      requireParameterCountIn(instance, base, 5, 6);
      boolean hasName = base.parameters().size() == 6;
      return new StepBSplineCurveWithKnots(
          instance.id(),
          hasName ? stringValue(instance, base, 0) : "",
          integerValue(instance, base, hasName ? 1 : 0),
          referenceList(
              instance,
              base,
              hasName ? 2 : 1,
              StepCartesianPoint.class,
              "B_SPLINE_CURVE control points must reference CARTESIAN_POINT"),
          enumValue(instance, base, hasName ? 3 : 2),
          booleanValue(instance, base, hasName ? 4 : 3),
          booleanValue(instance, base, hasName ? 5 : 4),
          integerList(instance, spline, 0),
          numberList(instance, spline, 1),
          enumValue(instance, spline, 2));
    }
    requireParameterCount(instance, spline, 9);
    return new StepBSplineCurveWithKnots(
        instance.id(),
        stringValue(instance, spline, 0),
        integerValue(instance, spline, 1),
        referenceList(
            instance,
            spline,
            2,
            StepCartesianPoint.class,
            "B_SPLINE_CURVE control points must reference CARTESIAN_POINT"),
        enumValue(instance, spline, 3),
        booleanValue(instance, spline, 4),
        booleanValue(instance, spline, 5),
        integerList(instance, spline, 6),
        numberList(instance, spline, 7),
        enumValue(instance, spline, 8));
  }

  private StepBSplineCurve resolveBSplineCurve(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "B_SPLINE_CURVE");
    requireParameterCountIn(instance, definition, 5, 6);
    boolean hasName = definition.parameters().size() == 6;
    return new StepBSplineCurve(
        instance.id(),
        hasName ? stringValue(instance, definition, 0) : "",
        integerValue(instance, definition, hasName ? 1 : 0),
        referenceList(
            instance,
            definition,
            hasName ? 2 : 1,
            StepCartesianPoint.class,
            "B_SPLINE_CURVE control points must reference CARTESIAN_POINT"),
        enumValue(instance, definition, hasName ? 3 : 2),
        booleanValue(instance, definition, hasName ? 4 : 3),
        booleanValue(instance, definition, hasName ? 5 : 4));
  }

  private StepRationalBSplineCurve resolveRationalBSplineCurve(StepEntityInstance instance) {
    StepEntityDefinition rational = definition(instance, "RATIONAL_B_SPLINE_CURVE");
    requireParameterCount(instance, rational, 1);
    StepEntityDefinition base = definition(instance, "B_SPLINE_CURVE");
    requireParameterCountIn(instance, base, 5, 6);
    boolean hasName = base.parameters().size() == 6;
    List<Integer> knotMultiplicities = List.of();
    List<Double> knots = List.of();
    String knotSpec = "";
    if (instance.hasDefinition("B_SPLINE_CURVE_WITH_KNOTS")) {
      StepEntityDefinition knotDefinition = definition(instance, "B_SPLINE_CURVE_WITH_KNOTS");
      requireParameterCount(instance, knotDefinition, 3);
      knotMultiplicities = integerList(instance, knotDefinition, 0);
      knots = numberList(instance, knotDefinition, 1);
      knotSpec = enumValue(instance, knotDefinition, 2);
    }
    return new StepRationalBSplineCurve(
        instance.id(),
        hasName ? stringValue(instance, base, 0) : "",
        integerValue(instance, base, hasName ? 1 : 0),
        referenceList(
            instance,
            base,
            hasName ? 2 : 1,
            StepCartesianPoint.class,
            "B_SPLINE_CURVE control points must reference CARTESIAN_POINT"),
        enumValue(instance, base, hasName ? 3 : 2),
        booleanValue(instance, base, hasName ? 4 : 3),
        booleanValue(instance, base, hasName ? 5 : 4),
        numberList(instance, rational, 0),
        knotMultiplicities,
        knots,
        knotSpec);
  }

  private StepBSplineSurfaceWithKnots resolveBSplineSurfaceWithKnots(StepEntityInstance instance) {
    StepEntityDefinition knots = definition(instance, "B_SPLINE_SURFACE_WITH_KNOTS");
    if (instance.hasDefinition("B_SPLINE_SURFACE")) {
      requireParameterCount(instance, knots, 5);
      StepEntityDefinition base = definition(instance, "B_SPLINE_SURFACE");
      requireParameterCount(instance, base, 7);
      return new StepBSplineSurfaceWithKnots(
          instance.id(),
          "",
          integerValue(instance, base, 0),
          integerValue(instance, base, 1),
          referenceGrid(
              instance,
              base,
              2,
              StepCartesianPoint.class,
              "B_SPLINE_SURFACE control points must reference CARTESIAN_POINT"),
          enumValue(instance, base, 3),
          booleanValue(instance, base, 4),
          booleanValue(instance, base, 5),
          booleanValue(instance, base, 6),
          integerList(instance, knots, 0),
          integerList(instance, knots, 1),
          numberList(instance, knots, 2),
          numberList(instance, knots, 3),
          enumValue(instance, knots, 4));
    }
    requireParameterCount(instance, knots, 13);
    return new StepBSplineSurfaceWithKnots(
        instance.id(),
        stringValue(instance, knots, 0),
        integerValue(instance, knots, 1),
        integerValue(instance, knots, 2),
        referenceGrid(
            instance,
            knots,
            3,
            StepCartesianPoint.class,
            "B_SPLINE_SURFACE control points must reference CARTESIAN_POINT"),
        enumValue(instance, knots, 4),
        booleanValue(instance, knots, 5),
        booleanValue(instance, knots, 6),
        booleanValue(instance, knots, 7),
        integerList(instance, knots, 8),
        integerList(instance, knots, 9),
        numberList(instance, knots, 10),
        numberList(instance, knots, 11),
        enumValue(instance, knots, 12));
  }

  private StepBSplineSurface resolveBSplineSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "B_SPLINE_SURFACE");
    requireParameterCount(instance, definition, 7);
    return new StepBSplineSurface(
        instance.id(),
        "",
        integerValue(instance, definition, 0),
        integerValue(instance, definition, 1),
        referenceGrid(
            instance,
            definition,
            2,
            StepCartesianPoint.class,
            "B_SPLINE_SURFACE control points must reference CARTESIAN_POINT"),
        enumValue(instance, definition, 3),
        booleanValue(instance, definition, 4),
        booleanValue(instance, definition, 5),
        booleanValue(instance, definition, 6));
  }

  private StepRationalBSplineSurface resolveRationalBSplineSurface(StepEntityInstance instance) {
    StepEntityDefinition rational = definition(instance, "RATIONAL_B_SPLINE_SURFACE");
    requireParameterCount(instance, rational, 1);
    StepEntityDefinition base = definition(instance, "B_SPLINE_SURFACE");
    requireParameterCount(instance, base, 7);
    List<Integer> uMultiplicities = List.of();
    List<Integer> vMultiplicities = List.of();
    List<Double> uKnots = List.of();
    List<Double> vKnots = List.of();
    String knotSpec = "";
    if (instance.hasDefinition("B_SPLINE_SURFACE_WITH_KNOTS")) {
      StepEntityDefinition knotDefinition = definition(instance, "B_SPLINE_SURFACE_WITH_KNOTS");
      requireParameterCount(instance, knotDefinition, 5);
      uMultiplicities = integerList(instance, knotDefinition, 0);
      vMultiplicities = integerList(instance, knotDefinition, 1);
      uKnots = numberList(instance, knotDefinition, 2);
      vKnots = numberList(instance, knotDefinition, 3);
      knotSpec = enumValue(instance, knotDefinition, 4);
    }
    return new StepRationalBSplineSurface(
        instance.id(),
        "",
        integerValue(instance, base, 0),
        integerValue(instance, base, 1),
        referenceGrid(
            instance,
            base,
            2,
            StepCartesianPoint.class,
            "B_SPLINE_SURFACE control points must reference CARTESIAN_POINT"),
        enumValue(instance, base, 3),
        booleanValue(instance, base, 4),
        booleanValue(instance, base, 5),
        booleanValue(instance, base, 6),
        numberGrid(instance, rational, 0),
        uMultiplicities,
        vMultiplicities,
        uKnots,
        vKnots,
        knotSpec);
  }

  private StepVertexPoint resolveVertexPoint(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "VERTEX_POINT");
    requireParameterCount(instance, definition, 2);
    return new StepVertexPoint(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepCartesianPoint.class,
            "VERTEX_POINT geometry must reference CARTESIAN_POINT"));
  }

  private StepEdgeCurve resolveEdgeCurve(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "EDGE_CURVE");
    requireParameterCount(instance, definition, 5);
    StepEntity edgeGeometry = resolve(referenceId(instance, definition, 3));
    if (!(edgeGeometry instanceof StepLine)
        && !(edgeGeometry instanceof StepCircle)
        && !(edgeGeometry instanceof StepEllipse)
        && !(edgeGeometry instanceof StepBSplineCurveWithKnots)
        && !(edgeGeometry instanceof StepSurfaceCurve)
        && !(edgeGeometry instanceof StepSeamCurve)
        && !(edgeGeometry instanceof StepTrimmedCurve)) {
      throw new UnsupportedStepEntityException(
          "EDGE_CURVE geometry must be LINE, CIRCLE, ELLIPSE, B_SPLINE_CURVE_WITH_KNOTS, SURFACE_CURVE, SEAM_CURVE or TRIMMED_CURVE");
    }
    return new StepEdgeCurve(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepVertexPoint.class,
            "EDGE_CURVE start must reference VERTEX_POINT"),
        requireEntity(
            referenceId(instance, definition, 2),
            StepVertexPoint.class,
            "EDGE_CURVE end must reference VERTEX_POINT"),
        edgeGeometry,
        booleanValue(instance, definition, 4));
  }

  private StepOrientedEdge resolveOrientedEdge(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ORIENTED_EDGE");
    requireParameterCount(instance, definition, 5);
    if (!isUnset(definition.parameters().get(1)) || !isUnset(definition.parameters().get(2))) {
      throw new UnsupportedStepEntityException(
          "ORIENTED_EDGE explicit edge_start/edge_end is unsupported");
    }
    return new StepOrientedEdge(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 3),
            StepEdgeCurve.class,
            "ORIENTED_EDGE edge_element must reference EDGE_CURVE"),
        booleanValue(instance, definition, 4));
  }

  private StepSubedge resolveSubedge(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SUBEDGE");
    requireParameterCount(instance, definition, 4);
    StepEntity parentEdge = resolve(referenceId(instance, definition, 3));
    if (!(parentEdge instanceof StepEdgeCurve) && !(parentEdge instanceof StepSubedge)) {
      throw new UnsupportedStepEntityException(
          "SUBEDGE parent_edge must reference EDGE_CURVE or SUBEDGE");
    }
    if (parentEdge.id() == instance.id()) {
      throw new UnsupportedStepEntityException("SUBEDGE parent_edge must not self-reference");
    }
    return new StepSubedge(
        instance.id(),
        stringValue(instance, definition, 0),
        requireVertexLike(
            referenceId(instance, definition, 1),
            "SUBEDGE edge_start must reference VERTEX or VERTEX_POINT"),
        requireVertexLike(
            referenceId(instance, definition, 2),
            "SUBEDGE edge_end must reference VERTEX or VERTEX_POINT"),
        parentEdge);
  }

  private StepConnectedEdgeSet resolveConnectedEdgeSet(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CONNECTED_EDGE_SET");
    requireParameterCount(instance, definition, 2);
    List<StepEntity> edges = entityReferenceList(
        instance,
        definition,
        1,
        "CONNECTED_EDGE_SET edges must contain edge references");
    for (StepEntity edge : edges) {
      if (!(edge instanceof StepEdgeCurve)
          && !(edge instanceof StepOrientedEdge)
          && !(edge instanceof StepSubedge)
          && !(edge instanceof StepEdge)) {
        throw new UnsupportedStepEntityException(
            "CONNECTED_EDGE_SET edges must reference EDGE subtypes");
      }
    }
    return new StepConnectedEdgeSet(instance.id(), stringValue(instance, definition, 0), edges);
  }

  private StepEdgeBasedWireframeModel resolveEdgeBasedWireframeModel(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "EDGE_BASED_WIREFRAME_MODEL");
    requireParameterCount(instance, definition, 2);
    return new StepEdgeBasedWireframeModel(
        instance.id(),
        stringValue(instance, definition, 0),
        referenceList(
            instance,
            definition,
            1,
            StepConnectedEdgeSet.class,
            "EDGE_BASED_WIREFRAME_MODEL ebwm_boundary must contain CONNECTED_EDGE_SET references"));
  }

  private StepEdgeLoop resolveEdgeLoop(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "EDGE_LOOP");
    requireParameterCount(instance, definition, 2);
    List<StepOrientedEdge> edges =
        referenceList(
            instance,
            definition,
            1,
            StepOrientedEdge.class,
            "EDGE_LOOP edge list must contain ORIENTED_EDGE references");
    return new StepEdgeLoop(instance.id(), stringValue(instance, definition, 0), edges);
  }

  private StepPath resolvePath(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PATH");
    requireParameterCount(instance, definition, 2);
    List<StepOrientedEdge> edges =
        referenceList(
            instance,
            definition,
            1,
            StepOrientedEdge.class,
            "PATH edge list must contain ORIENTED_EDGE references");
    return new StepPath(instance.id(), stringValue(instance, definition, 0), edges);
  }

  private StepOpenPath resolveOpenPath(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "OPEN_PATH");
    requireParameterCount(instance, definition, 2);
    List<StepOrientedEdge> edges =
        referenceList(
            instance,
            definition,
            1,
            StepOrientedEdge.class,
            "OPEN_PATH edge list must contain ORIENTED_EDGE references");
    if (!edges.isEmpty()) {
      StepOrientedEdge first = edges.getFirst();
      StepOrientedEdge last = edges.getLast();
      if (first.edgeElement().start().id() == last.edgeElement().end().id()) {
        throw new UnsupportedStepEntityException(
            "OPEN_PATH start vertex must differ from end vertex");
      }
    }
    return new StepOpenPath(instance.id(), stringValue(instance, definition, 0), edges);
  }

  private StepSubpath resolveSubpath(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SUBPATH");
    requireParameterCount(instance, definition, 3);
    List<StepOrientedEdge> edges =
        referenceList(
            instance,
            definition,
            1,
            StepOrientedEdge.class,
            "SUBPATH edge list must contain ORIENTED_EDGE references");
    StepEntity parentPath = resolve(referenceId(instance, definition, 2));
    if (!isPathEntity(parentPath)) {
      throw new UnsupportedStepEntityException(
          "SUBPATH parent_path must reference PATH, OPEN_PATH, SUBPATH, ORIENTED_PATH or EDGE_LOOP");
    }
    if (parentPath.id() == instance.id()) {
      throw new UnsupportedStepEntityException("SUBPATH parent_path must not reference itself");
    }
    return new StepSubpath(instance.id(), stringValue(instance, definition, 0), edges, parentPath);
  }

  private StepOrientedPath resolveOrientedPath(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ORIENTED_PATH");
    requireParameterCountIn(instance, definition, 2, 3);
    boolean hasStandaloneName = definition.parameters().size() == 3;
    String name =
        hasStandaloneName
            ? stringValue(instance, definition, 0)
            : instance.hasDefinition("PATH") ? stringValue(instance, definition(instance, "PATH"), 0) : "";
    int pathIndex = hasStandaloneName ? 1 : 0;
    StepEntity pathElement = resolve(referenceId(instance, definition, pathIndex));
    if (!isPathEntity(pathElement)) {
      throw new UnsupportedStepEntityException(
          "ORIENTED_PATH path_element must reference PATH, OPEN_PATH, SUBPATH, ORIENTED_PATH or EDGE_LOOP");
    }
    if (pathElement instanceof StepOrientedPath) {
      throw new UnsupportedStepEntityException(
          "ORIENTED_PATH path_element must not reference ORIENTED_PATH");
    }
    boolean orientation = booleanValue(instance, definition, pathIndex + 1);
    List<StepOrientedEdge> sourceEdges = pathEdges(pathElement);
    List<StepOrientedEdge> edges =
        orientation ? sourceEdges : sourceEdges.reversed();
    return new StepOrientedPath(instance.id(), name, pathElement, orientation, edges);
  }

  private static boolean isPathEntity(StepEntity entity) {
    return entity instanceof StepPath
        || entity instanceof StepOpenPath
        || entity instanceof StepSubpath
        || entity instanceof StepOrientedPath
        || entity instanceof StepEdgeLoop;
  }

  private static List<StepOrientedEdge> pathEdges(StepEntity entity) {
    return switch (entity) {
      case StepPath path -> path.edges();
      case StepOpenPath openPath -> openPath.edges();
      case StepSubpath subpath -> subpath.edges();
      case StepOrientedPath orientedPath -> orientedPath.edges();
      case StepEdgeLoop edgeLoop -> edgeLoop.edges();
      default -> throw new UnsupportedStepEntityException("entity is not a supported path");
    };
  }

  private StepVertexLoop resolveVertexLoop(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "VERTEX_LOOP");
    requireParameterCount(instance, definition, 2);
    return new StepVertexLoop(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepVertexPoint.class,
            "VERTEX_LOOP loop_vertex must reference VERTEX_POINT"));
  }

  private StepPolyLoop resolvePolyLoop(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "POLY_LOOP");
    requireParameterCount(instance, definition, 2);
    List<StepCartesianPoint> polygon =
        referenceList(
            instance,
            definition,
            1,
            StepCartesianPoint.class,
            "POLY_LOOP polygon must contain CARTESIAN_POINT references");
    if (polygon.size() < 3) {
      throw new UnsupportedStepEntityException("POLY_LOOP requires at least 3 points");
    }
    return new StepPolyLoop(instance.id(), stringValue(instance, definition, 0), polygon);
  }

  private StepFaceBound resolveFaceBound(StepEntityInstance instance, boolean outer) {
    StepEntityDefinition definition =
        definition(instance, outer ? "FACE_OUTER_BOUND" : "FACE_BOUND");
    requireParameterCount(instance, definition, 3);
    return new StepFaceBound(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepLoop.class,
            "FACE_BOUND loop must reference LOOP subtype"),
        booleanValue(instance, definition, 2),
        outer);
  }

  private StepAdvancedFace resolveAdvancedFace(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ADVANCED_FACE");
    requireParameterCount(instance, definition, 4);
    StepEntity faceGeometry = resolve(referenceId(instance, definition, 2));
    if (!(faceGeometry instanceof StepPlane)
        && !(faceGeometry instanceof StepCylindricalSurface)
        && !(faceGeometry instanceof StepConicalSurface)
        && !(faceGeometry instanceof StepSphericalSurface)
        && !(faceGeometry instanceof StepSurfaceOfLinearExtrusion)
        && !(faceGeometry instanceof StepSurfaceOfRevolution)
        && !(faceGeometry instanceof StepBSplineSurfaceWithKnots)
        && !(faceGeometry instanceof StepToroidalSurface)) {
      throw new UnsupportedStepEntityException(
          "ADVANCED_FACE geometry must be PLANE, CYLINDRICAL_SURFACE, CONICAL_SURFACE, SURFACE_OF_LINEAR_EXTRUSION, SURFACE_OF_REVOLUTION, B_SPLINE_SURFACE_WITH_KNOTS or TOROIDAL_SURFACE");
    }
    return new StepAdvancedFace(
        instance.id(),
        stringValue(instance, definition, 0),
        referenceList(
            instance,
            definition,
            1,
            StepFaceBound.class,
            "ADVANCED_FACE bounds must contain FACE_BOUND references"),
        faceGeometry,
        booleanValue(instance, definition, 3));
  }

  private StepFaceSurface resolveFaceSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FACE_SURFACE");
    requireParameterCount(instance, definition, 4);
    StepEntity faceGeometry = resolve(referenceId(instance, definition, 2));
    if (!(faceGeometry instanceof StepPlane)
        && !(faceGeometry instanceof StepCylindricalSurface)
        && !(faceGeometry instanceof StepConicalSurface)
        && !(faceGeometry instanceof StepSphericalSurface)
        && !(faceGeometry instanceof StepSurfaceOfLinearExtrusion)
        && !(faceGeometry instanceof StepSurfaceOfRevolution)
        && !(faceGeometry instanceof StepBSplineSurfaceWithKnots)
        && !(faceGeometry instanceof StepToroidalSurface)) {
      throw new UnsupportedStepEntityException(
          "FACE_SURFACE geometry must be PLANE, CYLINDRICAL_SURFACE, CONICAL_SURFACE, SURFACE_OF_LINEAR_EXTRUSION, SURFACE_OF_REVOLUTION, B_SPLINE_SURFACE_WITH_KNOTS or TOROIDAL_SURFACE");
    }
    return new StepFaceSurface(
        instance.id(),
        stringValue(instance, definition, 0),
        referenceList(
            instance,
            definition,
            1,
            StepFaceBound.class,
            "FACE_SURFACE bounds must contain FACE_BOUND references"),
        faceGeometry,
        booleanValue(instance, definition, 3));
  }

  private StepOrientedFace resolveOrientedFace(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ORIENTED_FACE");
    requireParameterCount(instance, definition, 3);
    StepFaceEntity faceElement =
        requireEntity(
            referenceId(instance, definition, 1),
            StepFaceEntity.class,
            "ORIENTED_FACE face_element must reference FACE subtype");
    if (faceElement instanceof StepOrientedFace) {
      throw new UnsupportedStepEntityException("ORIENTED_FACE nesting is unsupported");
    }
    return new StepOrientedFace(
        instance.id(),
        stringValue(instance, definition, 0),
        faceElement,
        booleanValue(instance, definition, 2));
  }

  private StepOpenShell resolveOpenShell(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "OPEN_SHELL");
    requireParameterCount(instance, definition, 2);
    return new StepOpenShell(
        instance.id(),
        stringValue(instance, definition, 0),
        referenceList(
            instance,
            definition,
            1,
            StepFaceEntity.class,
            "OPEN_SHELL faces must contain FACE subtype references"));
  }

  private StepClosedShell resolveClosedShell(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CLOSED_SHELL");
    requireParameterCount(instance, definition, 2);
    return new StepClosedShell(
        instance.id(),
        stringValue(instance, definition, 0),
        referenceList(
            instance,
            definition,
            1,
            StepFaceEntity.class,
            "CLOSED_SHELL faces must contain FACE subtype references"));
  }

  private StepSurfacedOpenShell resolveSurfacedOpenShell(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SURFACED_OPEN_SHELL");
    requireParameterCount(instance, definition, 2);
    List<StepFaceEntity> faces =
        referenceList(
            instance,
            definition,
            1,
            StepFaceEntity.class,
            "SURFACED_OPEN_SHELL faces must contain FACE subtype references");
    for (StepFaceEntity face : faces) {
      if (!(face instanceof StepFaceSurface)) {
        throw new StepResolutionException(
            "SURFACED_OPEN_SHELL faces must reference FACE_SURFACE or subtype");
      }
    }
    return new StepSurfacedOpenShell(instance.id(), stringValue(instance, definition, 0), faces);
  }

  private StepOrientedOpenShell resolveOrientedOpenShell(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ORIENTED_OPEN_SHELL");
    requireParameterCount(instance, definition, 3);
    StepEntity openShellElement = resolve(referenceId(instance, definition, 1));
    if (!(openShellElement instanceof StepOpenShell)
        && !(openShellElement instanceof StepSurfacedOpenShell)
        && !(openShellElement instanceof StepOrientedOpenShell)) {
      throw new StepResolutionException(
          "ORIENTED_OPEN_SHELL open_shell_element must reference OPEN_SHELL");
    }
    if (openShellElement instanceof StepOrientedOpenShell) {
      throw new UnsupportedStepEntityException("ORIENTED_OPEN_SHELL nesting is unsupported");
    }
    return new StepOrientedOpenShell(
        instance.id(),
        stringValue(instance, definition, 0),
        openShellElement,
        booleanValue(instance, definition, 2));
  }

  private StepOrientedClosedShell resolveOrientedClosedShell(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ORIENTED_CLOSED_SHELL");
    requireParameterCount(instance, definition, 3);
    StepEntity closedShellElement = resolve(referenceId(instance, definition, 1));
    if (!(closedShellElement instanceof StepClosedShell)
        && !(closedShellElement instanceof StepOrientedClosedShell)) {
      throw new StepResolutionException(
          "ORIENTED_CLOSED_SHELL closed_shell_element must reference CLOSED_SHELL");
    }
    if (closedShellElement instanceof StepOrientedClosedShell) {
      throw new UnsupportedStepEntityException("ORIENTED_CLOSED_SHELL nesting is unsupported");
    }
    return new StepOrientedClosedShell(
        instance.id(),
        stringValue(instance, definition, 0),
        closedShellElement,
        booleanValue(instance, definition, 2));
  }

  private StepConnectedFaceSet resolveConnectedFaceSet(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CONNECTED_FACE_SET");
    requireParameterCount(instance, definition, 2);
    return new StepConnectedFaceSet(
        instance.id(),
        stringValue(instance, definition, 0),
        referenceList(
            instance,
            definition,
            1,
            StepFaceEntity.class,
            "CONNECTED_FACE_SET cfs_faces must contain FACE subtype references"));
  }

  private StepConnectedFaceSubSet resolveConnectedFaceSubSet(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CONNECTED_FACE_SUB_SET");
    requireParameterCount(instance, definition, 3);
    StepEntity parentFaceSet = resolve(referenceId(instance, definition, 2));
    if (!isConnectedFaceSetEntity(parentFaceSet)) {
      throw new StepResolutionException(
          "CONNECTED_FACE_SUB_SET parent_face_set must reference CONNECTED_FACE_SET or CONNECTED_FACE_SUB_SET");
    }
    if (parentFaceSet.id() == instance.id()) {
      throw new StepResolutionException(
          "CONNECTED_FACE_SUB_SET parent_face_set cannot reference itself");
    }
    return new StepConnectedFaceSubSet(
        instance.id(),
        stringValue(instance, definition, 0),
        referenceList(
            instance,
            definition,
            1,
            StepFaceEntity.class,
            "CONNECTED_FACE_SUB_SET cfs_faces must contain FACE subtype references"),
        parentFaceSet);
  }

  private StepVertexShell resolveVertexShell(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "VERTEX_SHELL");
    requireParameterCount(instance, definition, 2);
    return new StepVertexShell(
        instance.id(),
        stringValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepVertexLoop.class,
            "VERTEX_SHELL vertex_shell_extent must reference VERTEX_LOOP"));
  }

  private StepWireShell resolveWireShell(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "WIRE_SHELL");
    requireParameterCount(instance, definition, 2);
    List<StepLoop> loops =
        referenceList(
            instance,
            definition,
            1,
            StepLoop.class,
            "WIRE_SHELL wire_shell_extent must contain LOOP references");
    boolean hasPolyLoop = loops.stream().anyMatch(loop -> loop instanceof StepPolyLoop);
    boolean hasNonPolyLoop = loops.stream().anyMatch(loop -> !(loop instanceof StepPolyLoop));
    if (hasPolyLoop && hasNonPolyLoop) {
      throw new UnsupportedStepEntityException(
          "WIRE_SHELL mixed poly_loop and non-poly_loop sets are unsupported");
    }
    return new StepWireShell(instance.id(), stringValue(instance, definition, 0), loops);
  }

  private StepManifoldSolidBrep resolveManifoldSolidBrep(StepEntityInstance instance) {
    return resolveManifoldSolidBrep(instance, "MANIFOLD_SOLID_BREP");
  }

  private StepManifoldSolidBrep resolveManifoldSolidBrep(
      StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 2);
    StepEntity outer = resolve(referenceId(instance, definition, 1));
    if (!isClosedShellEntity(outer)) {
      throw new StepResolutionException(entityName + " outer must reference CLOSED_SHELL");
    }
    return new StepManifoldSolidBrep(
        instance.id(),
        stringValue(instance, definition, 0),
        outer);
  }

  private StepShellBasedSurfaceModel resolveShellBasedSurfaceModel(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SHELL_BASED_SURFACE_MODEL");
    requireParameterCount(instance, definition, 2);
    List<StepEntity> shells =
        entityReferenceList(
            instance,
            definition,
            1,
            "SHELL_BASED_SURFACE_MODEL shells must contain shell references");
    for (StepEntity shell : shells) {
      if (!isShellEntity(shell)) {
        throw new StepResolutionException(
            "SHELL_BASED_SURFACE_MODEL shells must reference OPEN_SHELL, ORIENTED_OPEN_SHELL, CLOSED_SHELL or ORIENTED_CLOSED_SHELL");
      }
    }
    return new StepShellBasedSurfaceModel(
        instance.id(), stringValue(instance, definition, 0), shells);
  }

  private StepFaceBasedSurfaceModel resolveFaceBasedSurfaceModel(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FACE_BASED_SURFACE_MODEL");
    requireParameterCount(instance, definition, 2);
    List<StepEntity> faceSets =
        entityReferenceList(
            instance,
            definition,
            1,
            "FACE_BASED_SURFACE_MODEL fbsm_faces must contain connected face sets");
    for (StepEntity faceSet : faceSets) {
      if (!isConnectedFaceSetEntity(faceSet)
          && !isShellEntity(faceSet)) {
        throw new StepResolutionException(
            "FACE_BASED_SURFACE_MODEL fbsm_faces must reference CONNECTED_FACE_SET, CONNECTED_FACE_SUB_SET, OPEN_SHELL, ORIENTED_OPEN_SHELL, CLOSED_SHELL or ORIENTED_CLOSED_SHELL");
      }
    }
    return new StepFaceBasedSurfaceModel(
        instance.id(), stringValue(instance, definition, 0), faceSets);
  }

  private boolean isConnectedFaceSetEntity(StepEntity entity) {
    return entity instanceof StepConnectedFaceSet || entity instanceof StepConnectedFaceSubSet;
  }

  private boolean isOpenShellEntity(StepEntity entity) {
    return entity instanceof StepOpenShell
        || entity instanceof StepSurfacedOpenShell
        || entity instanceof StepOrientedOpenShell;
  }

  private boolean isClosedShellEntity(StepEntity entity) {
    return entity instanceof StepClosedShell || entity instanceof StepOrientedClosedShell;
  }

  private boolean isShellEntity(StepEntity entity) {
    return isOpenShellEntity(entity) || isClosedShellEntity(entity);
  }

  private boolean isBooleanOperandEntity(StepEntity entity) {
    return entity instanceof StepManifoldSolidBrep
        || entity instanceof StepBrepWithVoids
        || entity instanceof StepBooleanResult
        || entity instanceof StepBooleanClippingResult;
  }

  private StepShellBasedWireframeModel resolveShellBasedWireframeModel(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SHELL_BASED_WIREFRAME_MODEL");
    requireParameterCount(instance, definition, 2);
    List<StepEntity> shells =
        entityReferenceList(
            instance,
            definition,
            1,
            "SHELL_BASED_WIREFRAME_MODEL sbwm_boundary must contain shell references");
    for (StepEntity shell : shells) {
      if (!(shell instanceof StepVertexShell) && !(shell instanceof StepWireShell)) {
        throw new StepResolutionException(
            "SHELL_BASED_WIREFRAME_MODEL sbwm_boundary must reference VERTEX_SHELL or WIRE_SHELL");
      }
    }
    return new StepShellBasedWireframeModel(
        instance.id(), stringValue(instance, definition, 0), shells);
  }

  private StepBrepWithVoids resolveBrepWithVoids(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "BREP_WITH_VOIDS");
    requireParameterCount(instance, definition, 3);
    return new StepBrepWithVoids(
        instance.id(),
        stringValue(instance, definition, 0),
        requireClosedShellEntity(instance, definition, 1, "BREP_WITH_VOIDS outer must reference CLOSED_SHELL"),
        requireClosedShellEntities(instance, definition, 2, "BREP_WITH_VOIDS voids must contain CLOSED_SHELL references"));
  }

  private StepBooleanResult resolveBooleanResult(StepEntityInstance instance) {
    return resolveBooleanResult(instance, "BOOLEAN_RESULT");
  }

  private StepBooleanResult resolveBooleanResult(
      StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 3);
    StepEntity firstOperand = resolve(referenceId(instance, definition, 1));
    StepEntity secondOperand = resolve(referenceId(instance, definition, 2));
    if (!isBooleanOperandEntity(firstOperand) || !isBooleanOperandEntity(secondOperand)) {
      throw new StepResolutionException(
          entityName
              + " operands must reference MANIFOLD_SOLID_BREP, FACETED_BREP, BREP_WITH_VOIDS, BOOLEAN_RESULT or BOOLEAN_CLIPPING_RESULT");
    }
    return new StepBooleanResult(
        instance.id(),
        inheritedRepresentationItemName(instance),
        enumValue(instance, definition, 0),
        firstOperand,
        secondOperand);
  }

  private StepBooleanClippingResult resolveBooleanClippingResult(StepEntityInstance instance) {
    StepBooleanResult base = resolveBooleanResult(instance, "BOOLEAN_CLIPPING_RESULT");
    return new StepBooleanClippingResult(
        base.id(), base.name(), base.operator(), base.firstOperand(), base.secondOperand());
  }

  private StepEntity requireClosedShellEntity(
      StepEntityInstance instance, StepEntityDefinition definition, int parameterIndex, String message) {
    StepEntity shell = resolve(referenceId(instance, definition, parameterIndex));
    if (!isClosedShellEntity(shell)) {
      throw new StepResolutionException(message);
    }
    return shell;
  }

  private List<StepEntity> requireClosedShellEntities(
      StepEntityInstance instance, StepEntityDefinition definition, int parameterIndex, String message) {
    List<StepEntity> shells = entityReferenceList(instance, definition, parameterIndex, message);
    for (StepEntity shell : shells) {
      if (!isClosedShellEntity(shell)) {
        throw new StepResolutionException(message);
      }
    }
    return shells;
  }

  private StepRepresentationContext resolveRepresentationContext(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "REPRESENTATION_CONTEXT");
    requireParameterCount(instance, definition, 2);
    return new StepRepresentationContext(
        instance.id(), stringValue(instance, definition, 0), stringValue(instance, definition, 1));
  }

  private StepApplicationContext resolveApplicationContext(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "APPLICATION_CONTEXT");
    requireParameterCount(instance, definition, 1);
    return new StepApplicationContext(instance.id(), stringValue(instance, definition, 0));
  }

  private StepApplicationProtocolDefinition resolveApplicationProtocolDefinition(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "APPLICATION_PROTOCOL_DEFINITION");
    requireParameterCount(instance, definition, 4);
    return new StepApplicationProtocolDefinition(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        integerValue(instance, definition, 2),
        requireEntity(
            referenceId(instance, definition, 3),
            StepApplicationContext.class,
            "APPLICATION_PROTOCOL_DEFINITION application must reference APPLICATION_CONTEXT"));
  }

  private StepProductContext resolveProductContext(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PRODUCT_CONTEXT");
    requireParameterCount(instance, definition, 3);
    boolean stepOrder =
        unwrapTyped(definition.parameters().get(1)) instanceof StepValue.ReferenceValue;
    return new StepProductContext(
        instance.id(),
        stringValue(instance, definition, 0),
        stepOrder ? stringValue(instance, definition, 2) : stringValue(instance, definition, 1),
        requireEntity(
            referenceId(instance, definition, stepOrder ? 1 : 2),
            StepApplicationContext.class,
            "PRODUCT_CONTEXT frame_of_reference must reference APPLICATION_CONTEXT"));
  }

  private StepProduct resolveProduct(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PRODUCT");
    requireParameterCount(instance, definition, 4);
    return new StepProduct(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        optionalStringValue(instance, definition, 2),
        referenceList(
            instance,
            definition,
            3,
            StepProductContext.class,
            "PRODUCT frame_of_reference must contain PRODUCT_CONTEXT references"));
  }

  private StepProductRelatedProductCategory resolveProductRelatedProductCategory(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PRODUCT_RELATED_PRODUCT_CATEGORY");
    requireParameterCount(instance, definition, 3);
    return new StepProductRelatedProductCategory(
        instance.id(),
        stringValue(instance, definition, 0),
        optionalStringValue(instance, definition, 1),
        referenceList(
            instance,
            definition,
            2,
            StepProduct.class,
            "PRODUCT_RELATED_PRODUCT_CATEGORY products must contain PRODUCT references"));
  }

  private StepProductDefinitionFormation resolveProductDefinitionFormation(
      StepEntityInstance instance) {
    StepEntityDefinition definition;
    if (instance.hasDefinition("PRODUCT_DEFINITION_FORMATION_WITH_SPECIFIED_SOURCE")) {
      definition = definition(instance, "PRODUCT_DEFINITION_FORMATION_WITH_SPECIFIED_SOURCE");
      requireParameterCount(instance, definition, 4);
    } else {
      definition = definition(instance, "PRODUCT_DEFINITION_FORMATION");
      requireParameterCount(instance, definition, 3);
    }
    return new StepProductDefinitionFormation(
        instance.id(),
        stringValue(instance, definition, 0),
        optionalStringValue(instance, definition, 1),
        requireEntity(
            referenceId(instance, definition, 2),
            StepProduct.class,
            "PRODUCT_DEFINITION_FORMATION of_product must reference PRODUCT"));
  }

  private StepProductDefinitionContext resolveProductDefinitionContext(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PRODUCT_DEFINITION_CONTEXT");
    requireParameterCount(instance, definition, 3);
    boolean stepOrder =
        unwrapTyped(definition.parameters().get(1)) instanceof StepValue.ReferenceValue;
    return new StepProductDefinitionContext(
        instance.id(),
        stringValue(instance, definition, 0),
        stepOrder ? stringValue(instance, definition, 2) : stringValue(instance, definition, 1),
        requireEntity(
            referenceId(instance, definition, stepOrder ? 1 : 2),
            StepApplicationContext.class,
            "PRODUCT_DEFINITION_CONTEXT frame_of_reference must reference APPLICATION_CONTEXT"));
  }

  private StepProductDefinition resolveProductDefinition(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PRODUCT_DEFINITION");
    requireParameterCount(instance, definition, 4);
    return new StepProductDefinition(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        requireEntity(
            referenceId(instance, definition, 2),
            StepProductDefinitionFormation.class,
            "PRODUCT_DEFINITION formation must reference PRODUCT_DEFINITION_FORMATION"),
        requireEntity(
            referenceId(instance, definition, 3),
            StepProductDefinitionContext.class,
            "PRODUCT_DEFINITION frame_of_reference must reference PRODUCT_DEFINITION_CONTEXT"));
  }

  private StepProductDefinitionShape resolveProductDefinitionShape(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PRODUCT_DEFINITION_SHAPE");
    requireParameterCount(instance, definition, 3);
    StepEntity resolvedDefinition = resolve(referenceId(instance, definition, 2));
    if (!(resolvedDefinition instanceof StepProductDefinition)
        && !(resolvedDefinition instanceof StepNextAssemblyUsageOccurrence)) {
      throw new StepResolutionException(
          "PRODUCT_DEFINITION_SHAPE definition must reference PRODUCT_DEFINITION or NEXT_ASSEMBLY_USAGE_OCCURRENCE"
              + " but got "
              + resolvedDefinition.getClass().getSimpleName());
    }
    return new StepProductDefinitionShape(
        instance.id(),
        stringValue(instance, definition, 0),
        optionalStringValue(instance, definition, 1),
        resolvedDefinition);
  }

  private StepPropertyDefinition resolvePropertyDefinition(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PROPERTY_DEFINITION");
    requireParameterCount(instance, definition, 3);
    return new StepPropertyDefinition(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        resolve(referenceId(instance, definition, 2)));
  }

  private StepShapeDefinitionRepresentation resolveShapeDefinitionRepresentation(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SHAPE_DEFINITION_REPRESENTATION");
    requireParameterCount(instance, definition, 2);
    return new StepShapeDefinitionRepresentation(
        instance.id(),
        requireEntity(
            referenceId(instance, definition, 0),
            StepProductDefinitionShape.class,
            "SHAPE_DEFINITION_REPRESENTATION definition must reference PRODUCT_DEFINITION_SHAPE"),
        requireEntity(
            referenceId(instance, definition, 1),
            StepRepresentation.class,
            "SHAPE_DEFINITION_REPRESENTATION used_representation must reference SHAPE_REPRESENTATION"));
  }

  private StepPropertyDefinitionRepresentation resolvePropertyDefinitionRepresentation(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PROPERTY_DEFINITION_REPRESENTATION");
    requireParameterCount(instance, definition, 2);
    return new StepPropertyDefinitionRepresentation(
        instance.id(),
        requireEntity(
            referenceId(instance, definition, 0),
            StepPropertyDefinition.class,
            "PROPERTY_DEFINITION_REPRESENTATION definition must reference PROPERTY_DEFINITION"),
        requireEntity(
            referenceId(instance, definition, 1),
            StepRepresentation.class,
            "PROPERTY_DEFINITION_REPRESENTATION used_representation must reference REPRESENTATION"));
  }

  private StepItemDefinedTransformation resolveItemDefinedTransformation(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ITEM_DEFINED_TRANSFORMATION");
    requireParameterCount(instance, definition, 4);
    return new StepItemDefinedTransformation(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        requireEntity(
            referenceId(instance, definition, 2),
            StepAxis2Placement3D.class,
            "ITEM_DEFINED_TRANSFORMATION transform_item_1 must reference AXIS2_PLACEMENT_3D"),
        requireEntity(
            referenceId(instance, definition, 3),
            StepAxis2Placement3D.class,
            "ITEM_DEFINED_TRANSFORMATION transform_item_2 must reference AXIS2_PLACEMENT_3D"));
  }

  private StepRepresentationRelationshipWithTransformation
      resolveRepresentationRelationshipWithTransformation(StepEntityInstance instance) {
    StepEntityDefinition relationship = definition(instance, "REPRESENTATION_RELATIONSHIP");
    StepEntityDefinition transformation =
        definition(instance, "REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION");
    requireParameterCount(instance, relationship, 4);
    requireParameterCount(instance, transformation, 1);
    return new StepRepresentationRelationshipWithTransformation(
        instance.id(),
        stringValue(instance, relationship, 0),
        stringValue(instance, relationship, 1),
        requireEntity(
            referenceId(instance, relationship, 2),
            StepRepresentation.class,
            "REPRESENTATION_RELATIONSHIP rep_1 must reference REPRESENTATION"),
        requireEntity(
            referenceId(instance, relationship, 3),
            StepRepresentation.class,
            "REPRESENTATION_RELATIONSHIP rep_2 must reference REPRESENTATION"),
        requireEntity(
            referenceId(instance, transformation, 0),
            StepItemDefinedTransformation.class,
            "REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION transformation_operator must reference ITEM_DEFINED_TRANSFORMATION"));
  }

  private StepRepresentationRelationship resolveRepresentationRelationship(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "REPRESENTATION_RELATIONSHIP");
    requireParameterCount(instance, definition, 4);
    return new StepRepresentationRelationship(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        requireEntity(
            referenceId(instance, definition, 2),
            StepRepresentation.class,
            "REPRESENTATION_RELATIONSHIP rep_1 must reference REPRESENTATION"),
        requireEntity(
            referenceId(instance, definition, 3),
            StepRepresentation.class,
            "REPRESENTATION_RELATIONSHIP rep_2 must reference REPRESENTATION"));
  }

  private StepShapeRepresentationRelationship resolveShapeRepresentationRelationship(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SHAPE_REPRESENTATION_RELATIONSHIP");
    requireParameterCount(instance, definition, 4);
    return new StepShapeRepresentationRelationship(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        requireEntity(
            referenceId(instance, definition, 2),
            StepRepresentation.class,
            "SHAPE_REPRESENTATION_RELATIONSHIP rep_1 must reference REPRESENTATION"),
        requireEntity(
            referenceId(instance, definition, 3),
            StepRepresentation.class,
            "SHAPE_REPRESENTATION_RELATIONSHIP rep_2 must reference REPRESENTATION"));
  }

  private StepUncertaintyMeasureWithUnit resolveUncertaintyMeasureWithUnit(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "UNCERTAINTY_MEASURE_WITH_UNIT");
    requireParameterCount(instance, definition, 4);
    return new StepUncertaintyMeasureWithUnit(
        instance.id(),
        numberValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)),
        stringValue(instance, definition, 2),
        stringValue(instance, definition, 3));
  }

  private StepGlobalUnitAssignedContext resolveGlobalUnitAssignedContext(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "GLOBAL_UNIT_ASSIGNED_CONTEXT");
    requireParameterCount(instance, definition, 1);
    return new StepGlobalUnitAssignedContext(
        instance.id(),
        entityReferenceList(
            instance,
            definition,
            0,
            "GLOBAL_UNIT_ASSIGNED_CONTEXT units must contain entity references"));
  }

  private StepGlobalUncertaintyAssignedContext resolveGlobalUncertaintyAssignedContext(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "GLOBAL_UNCERTAINTY_ASSIGNED_CONTEXT");
    requireParameterCount(instance, definition, 1);
    return new StepGlobalUncertaintyAssignedContext(
        instance.id(),
        referenceList(
            instance,
            definition,
            0,
            StepUncertaintyMeasureWithUnit.class,
            "GLOBAL_UNCERTAINTY_ASSIGNED_CONTEXT uncertainties must contain UNCERTAINTY_MEASURE_WITH_UNIT references"));
  }

  private StepNextAssemblyUsageOccurrence resolveNextAssemblyUsageOccurrence(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "NEXT_ASSEMBLY_USAGE_OCCURRENCE");
    requireParameterCountIn(instance, definition, 5, 6);
    return new StepNextAssemblyUsageOccurrence(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        stringValue(instance, definition, 2),
        requireEntity(
            referenceId(instance, definition, 3),
            StepProductDefinition.class,
            "NEXT_ASSEMBLY_USAGE_OCCURRENCE relating_product_definition must reference PRODUCT_DEFINITION"),
        requireEntity(
            referenceId(instance, definition, 4),
            StepProductDefinition.class,
            "NEXT_ASSEMBLY_USAGE_OCCURRENCE related_product_definition must reference PRODUCT_DEFINITION"),
        definition.parameters().size() > 5 ? optionalStringValue(instance, definition, 5) : null);
  }

  private StepContextDependentShapeRepresentation resolveContextDependentShapeRepresentation(
      StepEntityInstance instance) {
    StepEntityDefinition definition =
        definition(instance, "CONTEXT_DEPENDENT_SHAPE_REPRESENTATION");
    requireParameterCount(instance, definition, 2);
    StepEntity relationship = resolve(referenceId(instance, definition, 0));
    if (!(relationship instanceof StepShapeRepresentationRelationship)
        && !(relationship instanceof StepRepresentationRelationship)
        && !(relationship instanceof StepRepresentationRelationshipWithTransformation)) {
      throw new StepResolutionException(
          "CONTEXT_DEPENDENT_SHAPE_REPRESENTATION representation_relation must reference a representation relationship"
              + " but got "
              + relationship.getClass().getSimpleName());
    }
    StepEntity representedProductRelation = resolve(referenceId(instance, definition, 1));
    if (!(representedProductRelation instanceof StepNextAssemblyUsageOccurrence)
        && !(representedProductRelation instanceof StepProductDefinitionShape)) {
      throw new StepResolutionException(
          "CONTEXT_DEPENDENT_SHAPE_REPRESENTATION represented_product_relation must reference"
              + " NEXT_ASSEMBLY_USAGE_OCCURRENCE or PRODUCT_DEFINITION_SHAPE but got "
              + representedProductRelation.getClass().getSimpleName());
    }
    return new StepContextDependentShapeRepresentation(
        instance.id(), relationship, representedProductRelation);
  }

  private StepMeasureWithUnit resolveMeasureWithUnit(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "MEASURE_WITH_UNIT");
    requireParameterCount(instance, definition, 2);
    return new StepMeasureWithUnit(
        instance.id(),
        numberValue(instance, definition, 0),
        resolve(referenceId(instance, definition, 1)));
  }

  private StepTypedMeasureWithUnit resolveTypedMeasureWithUnit(
      StepEntityInstance instance, String entityName, String expectedUnitKind) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 2);
    StepEntity unitComponent = resolve(referenceId(instance, definition, 1));
    if (!matchesUnitKind(unitComponent, expectedUnitKind)) {
      throw new StepResolutionException(
          entityName + " unit_component must reference " + expectedUnitKind);
    }
    return new StepTypedMeasureWithUnit(
        instance.id(),
        entityName,
        numberValue(instance, definition, 0),
        unitComponent);
  }

  private StepDerivedUnitElement resolveDerivedUnitElement(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DERIVED_UNIT_ELEMENT");
    requireParameterCount(instance, definition, 2);
    return new StepDerivedUnitElement(
        instance.id(),
        resolve(referenceId(instance, definition, 0)),
        numberValue(instance, definition, 1));
  }

  private StepDerivedUnit resolveDerivedUnit(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DERIVED_UNIT");
    requireParameterCount(instance, definition, 1);
    return new StepDerivedUnit(
        instance.id(),
        referenceList(
            instance,
            definition,
            0,
            StepDerivedUnitElement.class,
            "DERIVED_UNIT elements must contain DERIVED_UNIT_ELEMENT references"),
        "DERIVED_UNIT");
  }

  private StepGeometricRepresentationContext resolveGeometricRepresentationContext(
      StepEntityInstance instance) {
    StepEntityDefinition geometric = definition(instance, "GEOMETRIC_REPRESENTATION_CONTEXT");
    StepEntityDefinition representation = definition(instance, "REPRESENTATION_CONTEXT");
    requireParameterCount(instance, geometric, 1);
    requireParameterCount(instance, representation, 2);
    StepGlobalUnitAssignedContext globalUnits =
        instance.hasDefinition("GLOBAL_UNIT_ASSIGNED_CONTEXT")
            ? resolveGlobalUnitAssignedContext(instance)
            : null;
    StepGlobalUncertaintyAssignedContext globalUncertainty =
        instance.hasDefinition("GLOBAL_UNCERTAINTY_ASSIGNED_CONTEXT")
            ? resolveGlobalUncertaintyAssignedContext(instance)
            : null;
    return new StepGeometricRepresentationContext(
        instance.id(),
        integerValue(instance, geometric, 0),
        stringValue(instance, representation, 0),
        stringValue(instance, representation, 1),
        globalUnits,
        globalUncertainty);
  }

  private StepNamedUnit resolveNamedUnit(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "NAMED_UNIT");
    requireParameterCount(instance, definition, 1);
    if (!isUnset(definition.parameters().getFirst())) {
      throw new UnsupportedStepEntityException(
          "NAMED_UNIT dimensions must be omitted or not provided");
    }
    return new StepNamedUnit(instance.id(), deriveUnitKind(instance));
  }

  private StepNamedUnit resolveStandaloneUnitKind(
      StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 0);
    return new StepNamedUnit(instance.id(), entityName);
  }

  private StepContextDependentUnit resolveContextDependentUnit(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CONTEXT_DEPENDENT_UNIT");
    requireParameterCount(instance, definition, 1);
    return new StepContextDependentUnit(
        instance.id(),
        stringValue(instance, definition, 0),
        deriveUnitKind(instance));
  }

  private StepConversionBasedUnit resolveConversionBasedUnit(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CONVERSION_BASED_UNIT");
    requireParameterCount(instance, definition, 2);
    StepEntity conversionFactor = resolve(referenceId(instance, definition, 1));
    if (!(conversionFactor instanceof StepMeasureWithUnit measureWithUnit)) {
      throw new StepResolutionException(
          "CONVERSION_BASED_UNIT conversion_factor must reference MEASURE_WITH_UNIT");
    }
    return new StepConversionBasedUnit(
        instance.id(),
        stringValue(instance, definition, 0),
        deriveUnitKind(instance),
        measureWithUnit);
  }

  private StepConversionBasedUnitWithOffset resolveConversionBasedUnitWithOffset(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CONVERSION_BASED_UNIT_WITH_OFFSET");
    requireParameterCount(instance, definition, 1);
    StepConversionBasedUnit base = resolveConversionBasedUnit(instance);
    return new StepConversionBasedUnitWithOffset(
        instance.id(),
        base.name(),
        base.unitKind(),
        base.conversionFactor(),
        numberValue(instance, definition, 0));
  }

  private StepDerivedUnit resolveStandaloneDerivedUnitKind(
      StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 0);
    return new StepDerivedUnit(instance.id(), List.of(), entityName);
  }

  private StepSiUnit resolveSiUnit(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SI_UNIT");
    requireParameterCount(instance, definition, 2);
    String prefix = null;
    if (!isUnset(definition.parameters().get(0))) {
      prefix = enumValue(instance, definition, 0);
    }
    return new StepSiUnit(
        instance.id(), deriveUnitKind(instance), prefix, enumValue(instance, definition, 1));
  }

  private StepRepresentation resolveRepresentation(
      StepEntityInstance instance, boolean shapeRepresentation) {
    String entityName = shapeRepresentation ? "SHAPE_REPRESENTATION" : "REPRESENTATION";
    return resolveRepresentation(instance, entityName, shapeRepresentation);
  }

  private StepRepresentation resolveRepresentation(
      StepEntityInstance instance, String entityName, boolean shapeRepresentation) {
    StepEntityDefinition definition = definition(instance, entityName);
    requireParameterCount(instance, definition, 3);
    List<StepEntity> items =
        entityReferenceList(
            instance, definition, 1, entityName + " items must contain entity references");
    StepEntity context = resolve(referenceId(instance, definition, 2));
    if (!(context instanceof StepRepresentationContext)
        && !(context instanceof StepGeometricRepresentationContext)) {
      throw new StepResolutionException(
          entityName + " context must reference a representation context");
    }
    return new StepRepresentation(
        instance.id(), stringValue(instance, definition, 0), items, context, shapeRepresentation);
  }

  private StepRepresentationItem resolveRepresentationItem(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "REPRESENTATION_ITEM");
    requireParameterCount(instance, definition, 1);
    return new StepRepresentationItem(instance.id(), stringValue(instance, definition, 0));
  }

  private StepGeometricRepresentationItem resolveGeometricRepresentationItem(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "GEOMETRIC_REPRESENTATION_ITEM");
    requireParameterCount(instance, definition, 0);
    return new StepGeometricRepresentationItem(instance.id(), inheritedRepresentationItemName(instance));
  }

  private StepPoint resolvePoint(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "POINT");
    requireParameterCount(instance, definition, 0);
    return new StepPoint(instance.id(), inheritedRepresentationItemName(instance));
  }

  private StepCurve resolveCurve(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CURVE");
    requireParameterCount(instance, definition, 0);
    return new StepCurve(instance.id(), inheritedRepresentationItemName(instance));
  }

  private StepSurface resolveSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SURFACE");
    requireParameterCount(instance, definition, 0);
    return new StepSurface(instance.id(), inheritedRepresentationItemName(instance));
  }

  private StepBoundedCurve resolveBoundedCurve(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "BOUNDED_CURVE");
    requireParameterCount(instance, definition, 0);
    return new StepBoundedCurve(instance.id(), inheritedRepresentationItemName(instance));
  }

  private StepUniformCurve resolveUniformCurve(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "UNIFORM_CURVE");
    requireParameterCount(instance, definition, 0);
    return new StepUniformCurve(instance.id(), inheritedRepresentationItemName(instance));
  }

  private StepBezierCurve resolveBezierCurve(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "BEZIER_CURVE");
    requireParameterCount(instance, definition, 0);
    return new StepBezierCurve(instance.id(), inheritedRepresentationItemName(instance));
  }

  private StepPiecewiseBezierCurve resolvePiecewiseBezierCurve(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PIECEWISE_BEZIER_CURVE");
    requireParameterCount(instance, definition, 0);
    return new StepPiecewiseBezierCurve(instance.id(), inheritedRepresentationItemName(instance));
  }

  private StepQuasiUniformCurve resolveQuasiUniformCurve(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "QUASI_UNIFORM_CURVE");
    requireParameterCount(instance, definition, 0);
    return new StepQuasiUniformCurve(instance.id(), inheritedRepresentationItemName(instance));
  }

  private StepBoundedSurface resolveBoundedSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "BOUNDED_SURFACE");
    requireParameterCount(instance, definition, 0);
    return new StepBoundedSurface(instance.id(), inheritedRepresentationItemName(instance));
  }

  private StepUniformSurface resolveUniformSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "UNIFORM_SURFACE");
    requireParameterCount(instance, definition, 0);
    return new StepUniformSurface(instance.id(), inheritedRepresentationItemName(instance));
  }

  private StepBezierSurface resolveBezierSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "BEZIER_SURFACE");
    requireParameterCount(instance, definition, 0);
    return new StepBezierSurface(instance.id(), inheritedRepresentationItemName(instance));
  }

  private StepPiecewiseBezierSurface resolvePiecewiseBezierSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PIECEWISE_BEZIER_SURFACE");
    requireParameterCount(instance, definition, 0);
    return new StepPiecewiseBezierSurface(instance.id(), inheritedRepresentationItemName(instance));
  }

  private StepQuasiUniformSurface resolveQuasiUniformSurface(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "QUASI_UNIFORM_SURFACE");
    requireParameterCount(instance, definition, 0);
    return new StepQuasiUniformSurface(instance.id(), inheritedRepresentationItemName(instance));
  }

  private StepSurfaceModel resolveSurfaceModel(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SURFACE_MODEL");
    requireParameterCount(instance, definition, 0);
    return new StepSurfaceModel(instance.id(), inheritedRepresentationItemName(instance));
  }

  private StepSolidModel resolveSolidModel(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SOLID_MODEL");
    requireParameterCount(instance, definition, 0);
    return new StepSolidModel(instance.id(), inheritedRepresentationItemName(instance));
  }

  private StepTopologicalRepresentationItem resolveTopologicalRepresentationItem(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "TOPOLOGICAL_REPRESENTATION_ITEM");
    requireParameterCount(instance, definition, 1);
    return new StepTopologicalRepresentationItem(
        instance.id(), stringValue(instance, definition, 0));
  }

  private StepVertex resolveVertex(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "VERTEX");
    requireParameterCount(instance, definition, 0);
    return new StepVertex(instance.id(), inheritedTopologicalRepresentationItemName(instance));
  }

  private StepEdge resolveEdge(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "EDGE");
    requireParameterCount(instance, definition, 0);
    return new StepEdge(instance.id(), inheritedTopologicalRepresentationItemName(instance));
  }

  private StepFace resolveFace(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FACE");
    requireParameterCount(instance, definition, 0);
    return new StepFace(instance.id(), inheritedTopologicalRepresentationItemName(instance));
  }

  private StepColourRgb resolveColourRgb(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "COLOUR_RGB");
    requireParameterCount(instance, definition, 4);
    return new StepColourRgb(
        instance.id(),
        stringValue(instance, definition, 0),
        numberValue(instance, definition, 1),
        numberValue(instance, definition, 2),
        numberValue(instance, definition, 3));
  }

  private StepDraughtingPreDefinedCurveFont resolveDraughtingPreDefinedCurveFont(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DRAUGHTING_PRE_DEFINED_CURVE_FONT");
    requireParameterCount(instance, definition, 1);
    return new StepDraughtingPreDefinedCurveFont(
        instance.id(), stringValue(instance, definition, 0));
  }

  private StepDraughtingPreDefinedColour resolveDraughtingPreDefinedColour(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DRAUGHTING_PRE_DEFINED_COLOUR");
    requireParameterCount(instance, definition, 1);
    return new StepDraughtingPreDefinedColour(instance.id(), stringValue(instance, definition, 0));
  }

  private StepCurveStyle resolveCurveStyle(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "CURVE_STYLE");
    requireParameterCount(instance, definition, 4);
    StepEntity font = resolve(referenceId(instance, definition, 1));
    if (!(font instanceof StepDraughtingPreDefinedCurveFont)) {
      throw new UnsupportedStepEntityException(
          "CURVE_STYLE font must reference DRAUGHTING_PRE_DEFINED_CURVE_FONT");
    }
    StepEntity colour = resolve(referenceId(instance, definition, 3));
    if (!(colour instanceof StepColourRgb) && !(colour instanceof StepDraughtingPreDefinedColour)) {
      throw new UnsupportedStepEntityException(
          "CURVE_STYLE colour must reference COLOUR_RGB or DRAUGHTING_PRE_DEFINED_COLOUR");
    }
    return new StepCurveStyle(
        instance.id(),
        optionalStringValue(instance, definition, 0),
        font,
        numberValue(instance, definition, 2),
        colour);
  }

  private StepFillAreaStyleColour resolveFillAreaStyleColour(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FILL_AREA_STYLE_COLOUR");
    requireParameterCount(instance, definition, 2);
    StepEntity colour = resolve(referenceId(instance, definition, 1));
    if (!(colour instanceof StepColourRgb) && !(colour instanceof StepDraughtingPreDefinedColour)) {
      throw new UnsupportedStepEntityException(
          "FILL_AREA_STYLE_COLOUR colour must reference COLOUR_RGB or DRAUGHTING_PRE_DEFINED_COLOUR");
    }
    return new StepFillAreaStyleColour(
        instance.id(), optionalStringValue(instance, definition, 0), colour);
  }

  private StepFillAreaStyle resolveFillAreaStyle(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "FILL_AREA_STYLE");
    requireParameterCount(instance, definition, 2);
    return new StepFillAreaStyle(
        instance.id(),
        optionalStringValue(instance, definition, 0),
        referenceList(
            instance,
            definition,
            1,
            StepFillAreaStyleColour.class,
            "FILL_AREA_STYLE styles must contain FILL_AREA_STYLE_COLOUR references"));
  }

  private StepSurfaceStyleFillArea resolveSurfaceStyleFillArea(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SURFACE_STYLE_FILL_AREA");
    requireParameterCount(instance, definition, 1);
    return new StepSurfaceStyleFillArea(
        instance.id(),
        requireEntity(
            referenceId(instance, definition, 0),
            StepFillAreaStyle.class,
            "SURFACE_STYLE_FILL_AREA fill style must reference FILL_AREA_STYLE"));
  }

  private StepSurfaceSideStyle resolveSurfaceSideStyle(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SURFACE_SIDE_STYLE");
    requireParameterCount(instance, definition, 2);
    return new StepSurfaceSideStyle(
        instance.id(),
        optionalStringValue(instance, definition, 0),
        referenceList(
            instance,
            definition,
            1,
            StepSurfaceStyleFillArea.class,
            "SURFACE_SIDE_STYLE styles must contain SURFACE_STYLE_FILL_AREA references"));
  }

  private StepSurfaceStyleUsage resolveSurfaceStyleUsage(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "SURFACE_STYLE_USAGE");
    requireParameterCount(instance, definition, 2);
    return new StepSurfaceStyleUsage(
        instance.id(),
        enumValue(instance, definition, 0),
        requireEntity(
            referenceId(instance, definition, 1),
            StepSurfaceSideStyle.class,
            "SURFACE_STYLE_USAGE style must reference SURFACE_SIDE_STYLE"));
  }

  private StepPresentationStyleAssignment resolvePresentationStyleAssignment(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PRESENTATION_STYLE_ASSIGNMENT");
    requireParameterCount(instance, definition, 1);
    return new StepPresentationStyleAssignment(
        instance.id(),
        entityReferenceList(
            instance,
            definition,
            0,
            "PRESENTATION_STYLE_ASSIGNMENT styles must contain entity references"));
  }

  private StepStyledItem resolveStyledItem(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "STYLED_ITEM");
    requireParameterCount(instance, definition, 3);
    return new StepStyledItem(
        instance.id(),
        optionalStringValue(instance, definition, 0),
        referenceList(
            instance,
            definition,
            1,
            StepPresentationStyleAssignment.class,
            "STYLED_ITEM styles must contain PRESENTATION_STYLE_ASSIGNMENT references"),
        resolve(referenceId(instance, definition, 2)));
  }

  private StepOverRidingStyledItem resolveOverRidingStyledItem(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "OVER_RIDING_STYLED_ITEM");
    requireParameterCount(instance, definition, 4);
    return new StepOverRidingStyledItem(
        instance.id(),
        optionalStringValue(instance, definition, 0),
        referenceList(
            instance,
            definition,
            1,
            StepPresentationStyleAssignment.class,
            "OVER_RIDING_STYLED_ITEM styles must contain PRESENTATION_STYLE_ASSIGNMENT references"),
        resolve(referenceId(instance, definition, 2)),
        requireEntity(
            referenceId(instance, definition, 3),
            StepStyledItem.class,
            "OVER_RIDING_STYLED_ITEM over_ridden_style must reference STYLED_ITEM"));
  }

  private StepPresentationLayerAssignment resolvePresentationLayerAssignment(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "PRESENTATION_LAYER_ASSIGNMENT");
    requireParameterCount(instance, definition, 3);
    return new StepPresentationLayerAssignment(
        instance.id(),
        stringValue(instance, definition, 0),
        optionalStringValue(instance, definition, 1),
        entityReferenceList(
            instance,
            definition,
            2,
            "PRESENTATION_LAYER_ASSIGNMENT assigned items must contain entity references"));
  }

  private StepAnnotationTextOccurrence resolveAnnotationTextOccurrence(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "ANNOTATION_TEXT_OCCURRENCE");
    requireParameterCount(instance, definition, 3);
    return new StepAnnotationTextOccurrence(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        requireEntity(
            referenceId(instance, definition, 2),
            StepCartesianPoint.class,
            "ANNOTATION_TEXT_OCCURRENCE position must reference CARTESIAN_POINT"));
  }

  private StepGeometricCurveSet resolveGeometricCurveSet(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "GEOMETRIC_CURVE_SET");
    requireParameterCount(instance, definition, 2);
    List<StepEntity> elements =
        entityReferenceList(
            instance, definition, 1, "GEOMETRIC_CURVE_SET elements must contain entity references");
    for (StepEntity element : elements) {
      if (!(element instanceof StepLine)
          && !(element instanceof StepCircle)
          && !(element instanceof StepEllipse)
          && !(element instanceof StepPolyline)
          && !(element instanceof StepTrimmedCurve)
          && !(element instanceof StepSurfaceCurve)
          && !(element instanceof StepBSplineCurveWithKnots)
          && !(element instanceof StepCartesianPoint)) {
        throw new UnsupportedStepEntityException(
            "GEOMETRIC_CURVE_SET elements must be supported curves or points");
      }
    }
    return new StepGeometricCurveSet(instance.id(), stringValue(instance, definition, 0), elements);
  }

  private StepGeometricSet resolveGeometricSet(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "GEOMETRIC_SET");
    requireParameterCount(instance, definition, 2);
    List<StepEntity> elements =
        entityReferenceList(
            instance, definition, 1, "GEOMETRIC_SET elements must contain entity references");
    for (StepEntity element : elements) {
      if (!(element instanceof StepLine)
          && !(element instanceof StepCircle)
          && !(element instanceof StepEllipse)
          && !(element instanceof StepPolyline)
          && !(element instanceof StepTrimmedCurve)
          && !(element instanceof StepSurfaceCurve)
          && !(element instanceof StepBSplineCurveWithKnots)
          && !(element instanceof StepCartesianPoint)
          && !(element instanceof StepPlane)
          && !(element instanceof StepCylindricalSurface)
          && !(element instanceof StepConicalSurface)
          && !(element instanceof StepToroidalSurface)
          && !(element instanceof StepSphericalSurface)
          && !(element instanceof StepSurfaceOfLinearExtrusion)
          && !(element instanceof StepSurfaceOfRevolution)
          && !(element instanceof StepBSplineSurfaceWithKnots)) {
        throw new UnsupportedStepEntityException(
            "GEOMETRIC_SET elements must be supported curves, surfaces or points");
      }
    }
    return new StepGeometricSet(instance.id(), stringValue(instance, definition, 0), elements);
  }

  private StepPointSet resolvePointSet(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "POINT_SET");
    requireParameterCount(instance, definition, 2);
    List<StepEntity> points =
        entityReferenceList(
            instance, definition, 1, "POINT_SET points must contain entity references");
    for (StepEntity point : points) {
      if (!(point instanceof StepCartesianPoint) && !(point instanceof StepPoint)) {
        throw new UnsupportedStepEntityException(
            "POINT_SET points must reference CARTESIAN_POINT or POINT");
      }
    }
    return new StepPointSet(instance.id(), stringValue(instance, definition, 0), points);
  }

  private StepDraughtingCallout resolveDraughtingCallout(StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DRAUGHTING_CALLOUT");
    requireParameterCount(instance, definition, 2);
    List<StepEntity> contents =
        entityReferenceList(
            instance, definition, 1, "DRAUGHTING_CALLOUT contents must contain entity references");
    for (StepEntity content : contents) {
      if (!(content instanceof StepAnnotationTextOccurrence)
          && !(content instanceof StepGeometricCurveSet)) {
        throw new UnsupportedStepEntityException(
            "DRAUGHTING_CALLOUT contents must reference ANNOTATION_TEXT_OCCURRENCE or GEOMETRIC_CURVE_SET");
      }
    }
    return new StepDraughtingCallout(instance.id(), stringValue(instance, definition, 0), contents);
  }

  private StepMeasureRepresentationItem resolveMeasureRepresentationItem(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "MEASURE_REPRESENTATION_ITEM");
    requireParameterCount(instance, definition, 3);
    StepValue value = definition.parameters().get(1);
    if (!(value instanceof StepValue.TypedValue typedValue)) {
      throw new StepResolutionException(
          "MEASURE_REPRESENTATION_ITEM parameter 1 must be a typed measure value");
    }
    StepValue unwrapped = unwrapTyped(typedValue.value());
    if (!(unwrapped instanceof StepValue.NumberValue numberValue)) {
      throw new StepResolutionException(
          "MEASURE_REPRESENTATION_ITEM typed measure must wrap a number");
    }
    return new StepMeasureRepresentationItem(
        instance.id(),
        stringValue(instance, definition, 0),
        typedValue.typeName(),
        numberValue.value(),
        resolve(referenceId(instance, definition, 2)));
  }

  private StepDescriptiveRepresentationItem resolveDescriptiveRepresentationItem(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "DESCRIPTIVE_REPRESENTATION_ITEM");
    requireParameterCount(instance, definition, 2);
    return new StepDescriptiveRepresentationItem(
        instance.id(), stringValue(instance, definition, 0), stringValue(instance, definition, 1));
  }

  private StepValueRepresentationItem resolveValueRepresentationItem(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "VALUE_REPRESENTATION_ITEM");
    requireParameterCount(instance, definition, 2);
    StepValue value = definition.parameters().get(1);
    if (!(value instanceof StepValue.TypedValue typedValue)) {
      throw new StepResolutionException(
          "VALUE_REPRESENTATION_ITEM parameter 1 must be a typed value");
    }
    return new StepValueRepresentationItem(
        instance.id(),
        stringValue(instance, definition, 0),
        typedValue.typeName(),
        literalText(typedValue.value()));
  }

  private StepGeometricItemSpecificUsage resolveGeometricItemSpecificUsage(
      StepEntityInstance instance) {
    StepEntityDefinition definition = definition(instance, "GEOMETRIC_ITEM_SPECIFIC_USAGE");
    requireParameterCount(instance, definition, 4);
    StepEntity usage = resolve(referenceId(instance, definition, 2));
    if (!(usage instanceof StepDraughtingCallout)
        && !(usage instanceof StepAnnotationTextOccurrence)) {
      throw new UnsupportedStepEntityException(
          "GEOMETRIC_ITEM_SPECIFIC_USAGE usage must reference DRAUGHTING_CALLOUT or ANNOTATION_TEXT_OCCURRENCE");
    }
    StepEntity identifiedItem = resolve(referenceId(instance, definition, 3));
    if (!(identifiedItem instanceof StepFaceEntity)
        && !(identifiedItem instanceof StepEdgeCurve)
        && !(identifiedItem instanceof StepRepresentation)) {
      throw new UnsupportedStepEntityException(
          "GEOMETRIC_ITEM_SPECIFIC_USAGE identified item must reference FACE, EDGE_CURVE or REPRESENTATION");
    }
    return new StepGeometricItemSpecificUsage(
        instance.id(),
        stringValue(instance, definition, 0),
        stringValue(instance, definition, 1),
        usage,
        identifiedItem);
  }

  private StepEntityDefinition definition(StepEntityInstance instance, String name) {
    return instance.requireDefinition(name);
  }

  private static void requireParameterCount(
      StepEntityInstance instance, StepEntityDefinition definition, int expected) {
    if (definition.parameters().size() != expected) {
      throw new StepResolutionException(
          definition.name()
              + " expects "
              + expected
              + " parameters but got "
              + definition.parameters().size()
              + " in entity #"
              + instance.id());
    }
  }

  private static void requireParameterCountIn(
      StepEntityInstance instance, StepEntityDefinition definition, int... expectedCounts) {
    int actual = definition.parameters().size();
    for (int expected : expectedCounts) {
      if (actual == expected) {
        return;
      }
    }
    StringBuilder expectedText = new StringBuilder();
    for (int i = 0; i < expectedCounts.length; i++) {
      if (i > 0) {
        expectedText.append(i == expectedCounts.length - 1 ? " or " : ", ");
      }
      expectedText.append(expectedCounts[i]);
    }
    throw new StepResolutionException(
        definition.name()
            + " expects "
            + expectedText
            + " parameters but got "
            + actual
            + " in entity #"
            + instance.id());
  }

  private String stringValue(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (value instanceof StepValue.StringValue stringValue) {
      return stringValue.value();
    }
    throw new StepResolutionException(
        definition.name() + " parameter " + index + " must be a string");
  }

  private String optionalStringValue(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = definition.parameters().get(index);
    if (isUnset(value)) {
      return "";
    }
    return stringValue(instance, definition, index);
  }

  private double numberValue(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (value instanceof StepValue.NumberValue numberValue) {
      return numberValue.value();
    }
    throw new StepResolutionException(
        definition.name() + " parameter " + index + " must be a number");
  }

  private int integerValue(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    double value = numberValue(instance, definition, index);
    if (value != Math.rint(value)) {
      throw new StepResolutionException(
          definition.name() + " parameter " + index + " must be an integer");
    }
    return (int) value;
  }

  private String enumValue(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (value instanceof StepValue.EnumValue enumValue) {
      return enumValue.value();
    }
    throw new StepResolutionException(
        definition.name() + " parameter " + index + " must be an enum");
  }

  private boolean booleanValue(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    return switch (enumValue(instance, definition, index)) {
      case "T" -> true;
      case "F" -> false;
      default ->
          throw new StepResolutionException(
              definition.name() + " parameter " + index + " must be .T. or .F.");
    };
  }

  private int referenceId(StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (value instanceof StepValue.ReferenceValue referenceValue) {
      return referenceValue.id();
    }
    throw new StepResolutionException(
        definition.name() + " parameter " + index + " must be a reference");
  }

  private List<Double> coordinateTriple(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    return coordinateList(instance, definition, index, 3, 3);
  }

  private List<Double> coordinateList(
      StepEntityInstance instance,
      StepEntityDefinition definition,
      int index,
      int minSize,
      int maxSize) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (!(value instanceof StepValue.ListValue listValue)) {
      throw new StepResolutionException(
          definition.name() + " parameter " + index + " must be a list");
    }
    if (listValue.elements().size() < minSize || listValue.elements().size() > maxSize) {
      throw new UnsupportedStepEntityException(
          definition.name() + " only supports " + minSize + "D to " + maxSize + "D coordinates");
    }
    List<Double> result = new ArrayList<>(listValue.elements().size());
    for (StepValue element : listValue.elements()) {
      StepValue unwrapped = unwrapTyped(element);
      if (unwrapped instanceof StepValue.NumberValue numberValue) {
        result.add(numberValue.value());
      } else {
        throw new StepResolutionException(
            definition.name() + " coordinate list must contain only numbers");
      }
    }
    return List.copyOf(result);
  }

  private List<Double> numberList(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (!(value instanceof StepValue.ListValue listValue)) {
      throw new StepResolutionException(
          definition.name() + " parameter " + index + " must be a list");
    }
    List<Double> result = new ArrayList<>(listValue.elements().size());
    for (StepValue element : listValue.elements()) {
      StepValue unwrapped = unwrapTyped(element);
      if (!(unwrapped instanceof StepValue.NumberValue numberValue)) {
        throw new StepResolutionException(
            definition.name() + " numeric list must contain only numbers");
      }
      result.add(numberValue.value());
    }
    return List.copyOf(result);
  }

  private List<List<Double>> numberGrid(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (!(value instanceof StepValue.ListValue outerList)) {
      throw new StepResolutionException(
          definition.name() + " parameter " + index + " must be a nested list");
    }
    List<List<Double>> grid = new ArrayList<>(outerList.elements().size());
    for (StepValue rowValue : outerList.elements()) {
      StepValue row = unwrapTyped(rowValue);
      if (!(row instanceof StepValue.ListValue rowList)) {
        throw new StepResolutionException(
            definition.name() + " numeric grid must contain only nested numeric lists");
      }
      List<Double> entries = new ArrayList<>(rowList.elements().size());
      for (StepValue element : rowList.elements()) {
        StepValue unwrapped = unwrapTyped(element);
        if (!(unwrapped instanceof StepValue.NumberValue numberValue)) {
          throw new StepResolutionException(
              definition.name() + " numeric grid must contain only numbers");
        }
        entries.add(numberValue.value());
      }
      grid.add(List.copyOf(entries));
    }
    return List.copyOf(grid);
  }

  private <T extends StepEntity> List<List<T>> referenceGrid(
      StepEntityInstance instance,
      StepEntityDefinition definition,
      int index,
      Class<T> type,
      String message) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (!(value instanceof StepValue.ListValue outerList)) {
      throw new StepResolutionException(
          definition.name() + " parameter " + index + " must be a nested list");
    }
    List<List<T>> grid = new ArrayList<>(outerList.elements().size());
    for (StepValue rowValue : outerList.elements()) {
      StepValue row = unwrapTyped(rowValue);
      if (!(row instanceof StepValue.ListValue rowList)) {
        throw new StepResolutionException(message);
      }
      List<T> entries = new ArrayList<>(rowList.elements().size());
      for (StepValue element : rowList.elements()) {
        StepValue unwrapped = unwrapTyped(element);
        if (!(unwrapped instanceof StepValue.ReferenceValue referenceValue)) {
          throw new StepResolutionException(message);
        }
        entries.add(requireEntity(referenceValue.id(), type, message));
      }
      grid.add(List.copyOf(entries));
    }
    return List.copyOf(grid);
  }

  private List<Integer> integerList(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    List<Double> values = numberList(instance, definition, index);
    List<Integer> result = new ArrayList<>(values.size());
    for (double value : values) {
      if (value != Math.rint(value)) {
        throw new StepResolutionException(
            definition.name() + " integer list must contain only integers");
      }
      result.add((int) value);
    }
    return List.copyOf(result);
  }

  private boolean isUnset(StepValue value) {
    StepValue unwrapped = unwrapTyped(value);
    return unwrapped instanceof StepValue.OmittedValue
        || unwrapped instanceof StepValue.NotProvidedValue;
  }

  private StepValue unwrapTyped(StepValue value) {
    StepValue current = value;
    while (current instanceof StepValue.TypedValue typedValue) {
      current = typedValue.value();
    }
    return current;
  }

  private String literalText(StepValue value) {
    return switch (value) {
      case StepValue.StringValue stringValue -> stringValue.value();
      case StepValue.NumberValue numberValue -> numberValue.raw();
      case StepValue.EnumValue enumValue -> "." + enumValue.value() + ".";
      case StepValue.ReferenceValue referenceValue -> "#" + referenceValue.id();
      case StepValue.OmittedValue ignored -> "$";
      case StepValue.NotProvidedValue ignored -> "*";
      case StepValue.ListValue listValue ->
          listValue.elements().stream()
              .map(this::literalText)
              .collect(Collectors.joining(",", "(", ")"));
      case StepValue.TypedValue typedValue ->
          typedValue.typeName() + "(" + literalText(typedValue.value()) + ")";
    };
  }

  private <T extends StepEntity> T requireEntity(int id, Class<T> type, String message) {
    StepEntity entity = resolve(id);
    if (!type.isInstance(entity)) {
      throw new StepResolutionException(message + " but got " + entity.getClass().getSimpleName());
    }
    return type.cast(entity);
  }

  private StepEntity requireVertexLike(int id, String message) {
    StepEntity entity = resolve(id);
    if (!(entity instanceof StepVertex) && !(entity instanceof StepVertexPoint)) {
      throw new StepResolutionException(message + " but got " + entity.getClass().getSimpleName());
    }
    return entity;
  }

  private <T extends StepEntity> List<T> referenceList(
      StepEntityInstance instance,
      StepEntityDefinition definition,
      int index,
      Class<T> type,
      String message) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (!(value instanceof StepValue.ListValue listValue)) {
      throw new StepResolutionException(
          definition.name() + " parameter " + index + " must be a list");
    }
    List<T> result = new ArrayList<>();
    for (StepValue element : listValue.elements()) {
      StepValue unwrapped = unwrapTyped(element);
      if (!(unwrapped instanceof StepValue.ReferenceValue referenceValue)) {
        throw new StepResolutionException(message);
      }
      result.add(requireEntity(referenceValue.id(), type, message));
    }
    return List.copyOf(result);
  }

  private List<StepEntity> entityReferenceList(
      StepEntityInstance instance, StepEntityDefinition definition, int index, String message) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (!(value instanceof StepValue.ListValue listValue)) {
      throw new StepResolutionException(
          definition.name() + " parameter " + index + " must be a list");
    }
    List<StepEntity> result = new ArrayList<>();
    for (StepValue element : listValue.elements()) {
      StepValue unwrapped = unwrapTyped(element);
      if (!(unwrapped instanceof StepValue.ReferenceValue referenceValue)) {
        throw new StepResolutionException(message);
      }
      result.add(resolve(referenceValue.id()));
    }
    return List.copyOf(result);
  }

  private String deriveUnitKind(StepEntityInstance instance) {
    for (String candidate : List.of(
        "LENGTH_UNIT",
        "PLANE_ANGLE_UNIT",
        "SOLID_ANGLE_UNIT",
        "RATIO_UNIT",
        "AREA_UNIT",
        "VOLUME_UNIT",
        "TIME_UNIT",
        "THERMODYNAMIC_TEMPERATURE_UNIT",
        "ELECTRIC_CURRENT_UNIT",
        "AMOUNT_OF_SUBSTANCE_UNIT",
        "LUMINOUS_FLUX_UNIT",
        "LUMINOUS_INTENSITY_UNIT",
        "ACCELERATION_UNIT",
        "VELOCITY_UNIT",
        "THERMAL_RESISTANCE_UNIT",
        "FREQUENCY_UNIT",
        "FORCE_UNIT",
        "PRESSURE_UNIT",
        "ENERGY_UNIT",
        "POWER_UNIT",
        "ELECTRIC_CHARGE_UNIT",
        "ELECTRIC_POTENTIAL_UNIT",
        "CAPACITANCE_UNIT",
        "RESISTANCE_UNIT",
        "CONDUCTANCE_UNIT",
        "MAGNETIC_FLUX_UNIT",
        "MAGNETIC_FLUX_DENSITY_UNIT",
        "INDUCTANCE_UNIT",
        "ILLUMINANCE_UNIT",
        "RADIOACTIVITY_UNIT",
        "ABSORBED_DOSE_UNIT",
        "DOSE_EQUIVALENT_UNIT")) {
      if (instance.hasDefinition(candidate)) {
        return candidate;
      }
    }
    if (instance.hasDefinition("MASS_UNIT")) {
      return "MASS_UNIT";
    }
    return "NAMED_UNIT";
  }

  private boolean matchesUnitKind(StepEntity entity, String expectedUnitKind) {
    if (entity instanceof StepNamedUnit namedUnit) {
      return expectedUnitKind.equals(namedUnit.unitKind());
    }
    if (entity instanceof StepSiUnit siUnit) {
      return expectedUnitKind.equals(siUnit.unitKind());
    }
    if (entity instanceof StepConversionBasedUnit conversionBasedUnit) {
      return expectedUnitKind.equals(conversionBasedUnit.unitKind());
    }
    if (entity instanceof StepConversionBasedUnitWithOffset conversionBasedUnitWithOffset) {
      return expectedUnitKind.equals(conversionBasedUnitWithOffset.unitKind());
    }
    if (entity instanceof StepContextDependentUnit contextDependentUnit) {
      return expectedUnitKind.equals(contextDependentUnit.unitKind());
    }
    if (entity instanceof StepDerivedUnit derivedUnit) {
      return expectedUnitKind.equals(derivedUnit.unitKind());
    }
    return false;
  }

  private String inheritedRepresentationItemName(StepEntityInstance instance) {
    return instance.hasDefinition("REPRESENTATION_ITEM")
        ? stringValue(instance, definition(instance, "REPRESENTATION_ITEM"), 0)
        : "";
  }

  private String inheritedTopologicalRepresentationItemName(StepEntityInstance instance) {
    return instance.hasDefinition("TOPOLOGICAL_REPRESENTATION_ITEM")
        ? stringValue(instance, definition(instance, "TOPOLOGICAL_REPRESENTATION_ITEM"), 0)
        : inheritedRepresentationItemName(instance);
  }

  private static Map<String, EntityFactory> createRegistry() {
    // Resolution order matters for complex entities such as
    // (LENGTH_UNIT() NAMED_UNIT(*) SI_UNIT(...)).
    // Prefer the more specific entity factories inserted earlier here.
    Map<String, EntityFactory> registry = new LinkedHashMap<>();
    registry.put(
        "GEOMETRIC_REPRESENTATION_CONTEXT",
        StepEntityResolver::resolveGeometricRepresentationContext);
    registry.put(
        "SHAPE_REPRESENTATION",
        (resolver, instance) -> resolver.resolveRepresentation(instance, true));
    registry.put(
        "ADVANCED_BREP_SHAPE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "ADVANCED_BREP_SHAPE_REPRESENTATION", true));
    registry.put(
        "ELEMENTARY_BREP_SHAPE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "ELEMENTARY_BREP_SHAPE_REPRESENTATION", true));
    registry.put(
        "FACETED_BREP_SHAPE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "FACETED_BREP_SHAPE_REPRESENTATION", true));
    registry.put(
        "CSG_SHAPE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "CSG_SHAPE_REPRESENTATION", true));
    registry.put("BOOLEAN_CLIPPING_RESULT", StepEntityResolver::resolveBooleanClippingResult);
    registry.put("BOOLEAN_RESULT", StepEntityResolver::resolveBooleanResult);
    registry.put(
        "FACETED_BREP",
        (resolver, instance) -> resolver.resolveManifoldSolidBrep(instance, "FACETED_BREP"));
    registry.put("BREP_WITH_VOIDS", StepEntityResolver::resolveBrepWithVoids);
    registry.put(
        "EDGE_BASED_WIREFRAME_SHAPE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "EDGE_BASED_WIREFRAME_SHAPE_REPRESENTATION", true));
    registry.put(
        "GEOMETRICALLY_BOUNDED_WIREFRAME_SHAPE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "GEOMETRICALLY_BOUNDED_WIREFRAME_SHAPE_REPRESENTATION", true));
    registry.put(
        "GEOMETRICALLY_BOUNDED_2D_WIREFRAME_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "GEOMETRICALLY_BOUNDED_2D_WIREFRAME_REPRESENTATION", true));
    registry.put(
        "SHELL_BASED_WIREFRAME_SHAPE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "SHELL_BASED_WIREFRAME_SHAPE_REPRESENTATION", true));
    registry.put(
        "MANIFOLD_SURFACE_SHAPE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "MANIFOLD_SURFACE_SHAPE_REPRESENTATION", true));
    registry.put(
        "SURFACE_SHAPE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "SURFACE_SHAPE_REPRESENTATION", true));
    registry.put(
        "GEOMETRICALLY_BOUNDED_SURFACE_SHAPE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "GEOMETRICALLY_BOUNDED_SURFACE_SHAPE_REPRESENTATION", true));
    registry.put(
        "PATH_SHAPE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "PATH_SHAPE_REPRESENTATION", true));
    registry.put(
        "WIREFRAME_SHAPE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "WIREFRAME_SHAPE_REPRESENTATION", true));
    registry.put(
        "FACE_SHAPE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "FACE_SHAPE_REPRESENTATION", true));
    registry.put(
        "NON_MANIFOLD_SURFACE_SHAPE_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "NON_MANIFOLD_SURFACE_SHAPE_REPRESENTATION", true));
    registry.put(
        "MECHANICAL_DESIGN_GEOMETRIC_PRESENTATION_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(
                instance, "MECHANICAL_DESIGN_GEOMETRIC_PRESENTATION_REPRESENTATION", false));
    registry.put(
        "REPRESENTATION", (resolver, instance) -> resolver.resolveRepresentation(instance, false));
    registry.put("APPLICATION_CONTEXT", StepEntityResolver::resolveApplicationContext);
    registry.put(
        "APPLICATION_PROTOCOL_DEFINITION",
        StepEntityResolver::resolveApplicationProtocolDefinition);
    registry.put("PRODUCT_CONTEXT", StepEntityResolver::resolveProductContext);
    registry.put("PRODUCT", StepEntityResolver::resolveProduct);
    registry.put(
        "PRODUCT_RELATED_PRODUCT_CATEGORY",
        StepEntityResolver::resolveProductRelatedProductCategory);
    registry.put(
        "PRODUCT_DEFINITION_FORMATION", StepEntityResolver::resolveProductDefinitionFormation);
    registry.put(
        "PRODUCT_DEFINITION_FORMATION_WITH_SPECIFIED_SOURCE",
        StepEntityResolver::resolveProductDefinitionFormation);
    registry.put("PRODUCT_DEFINITION_CONTEXT", StepEntityResolver::resolveProductDefinitionContext);
    registry.put("PRODUCT_DEFINITION", StepEntityResolver::resolveProductDefinition);
    registry.put("PRODUCT_DEFINITION_SHAPE", StepEntityResolver::resolveProductDefinitionShape);
    registry.put("PROPERTY_DEFINITION", StepEntityResolver::resolvePropertyDefinition);
    registry.put(
        "SHAPE_DEFINITION_REPRESENTATION",
        StepEntityResolver::resolveShapeDefinitionRepresentation);
    registry.put(
        "PROPERTY_DEFINITION_REPRESENTATION",
        StepEntityResolver::resolvePropertyDefinitionRepresentation);
    registry.put(
        "ITEM_DEFINED_TRANSFORMATION", StepEntityResolver::resolveItemDefinedTransformation);
    registry.put(
        "REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION",
        StepEntityResolver::resolveRepresentationRelationshipWithTransformation);
    registry.put(
        "REPRESENTATION_RELATIONSHIP", StepEntityResolver::resolveRepresentationRelationship);
    registry.put(
        "SHAPE_REPRESENTATION_RELATIONSHIP",
        StepEntityResolver::resolveShapeRepresentationRelationship);
    registry.put(
        "NEXT_ASSEMBLY_USAGE_OCCURRENCE", StepEntityResolver::resolveNextAssemblyUsageOccurrence);
    registry.put(
        "CONTEXT_DEPENDENT_SHAPE_REPRESENTATION",
        StepEntityResolver::resolveContextDependentShapeRepresentation);
    registry.put(
        "UNCERTAINTY_MEASURE_WITH_UNIT", StepEntityResolver::resolveUncertaintyMeasureWithUnit);
    registry.put(
        "LENGTH_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(instance, "LENGTH_MEASURE_WITH_UNIT", "LENGTH_UNIT"));
    registry.put(
        "MASS_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(instance, "MASS_MEASURE_WITH_UNIT", "MASS_UNIT"));
    registry.put(
        "TIME_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(instance, "TIME_MEASURE_WITH_UNIT", "TIME_UNIT"));
    registry.put(
        "PLANE_ANGLE_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(
                instance, "PLANE_ANGLE_MEASURE_WITH_UNIT", "PLANE_ANGLE_UNIT"));
    registry.put(
        "SOLID_ANGLE_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(
                instance, "SOLID_ANGLE_MEASURE_WITH_UNIT", "SOLID_ANGLE_UNIT"));
    registry.put(
        "AREA_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(instance, "AREA_MEASURE_WITH_UNIT", "AREA_UNIT"));
    registry.put(
        "VOLUME_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(instance, "VOLUME_MEASURE_WITH_UNIT", "VOLUME_UNIT"));
    registry.put(
        "RATIO_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(instance, "RATIO_MEASURE_WITH_UNIT", "RATIO_UNIT"));
    registry.put(
        "THERMODYNAMIC_TEMPERATURE_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(
                instance,
                "THERMODYNAMIC_TEMPERATURE_MEASURE_WITH_UNIT",
                "THERMODYNAMIC_TEMPERATURE_UNIT"));
    registry.put(
        "ELECTRIC_CURRENT_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(
                instance, "ELECTRIC_CURRENT_MEASURE_WITH_UNIT", "ELECTRIC_CURRENT_UNIT"));
    registry.put(
        "AMOUNT_OF_SUBSTANCE_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(
                instance,
                "AMOUNT_OF_SUBSTANCE_MEASURE_WITH_UNIT",
                "AMOUNT_OF_SUBSTANCE_UNIT"));
    registry.put(
        "FREQUENCY_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(
                instance, "FREQUENCY_MEASURE_WITH_UNIT", "FREQUENCY_UNIT"));
    registry.put(
        "FORCE_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(instance, "FORCE_MEASURE_WITH_UNIT", "FORCE_UNIT"));
    registry.put(
        "PRESSURE_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(
                instance, "PRESSURE_MEASURE_WITH_UNIT", "PRESSURE_UNIT"));
    registry.put(
        "ENERGY_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(
                instance, "ENERGY_MEASURE_WITH_UNIT", "ENERGY_UNIT"));
    registry.put(
        "POWER_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(instance, "POWER_MEASURE_WITH_UNIT", "POWER_UNIT"));
    registry.put(
        "ELECTRIC_CHARGE_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(
                instance, "ELECTRIC_CHARGE_MEASURE_WITH_UNIT", "ELECTRIC_CHARGE_UNIT"));
    registry.put(
        "ELECTRIC_POTENTIAL_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(
                instance,
                "ELECTRIC_POTENTIAL_MEASURE_WITH_UNIT",
                "ELECTRIC_POTENTIAL_UNIT"));
    registry.put(
        "CAPACITANCE_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(
                instance, "CAPACITANCE_MEASURE_WITH_UNIT", "CAPACITANCE_UNIT"));
    registry.put(
        "RESISTANCE_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(
                instance, "RESISTANCE_MEASURE_WITH_UNIT", "RESISTANCE_UNIT"));
    registry.put(
        "CONDUCTANCE_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(
                instance, "CONDUCTANCE_MEASURE_WITH_UNIT", "CONDUCTANCE_UNIT"));
    registry.put(
        "MAGNETIC_FLUX_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(
                instance, "MAGNETIC_FLUX_MEASURE_WITH_UNIT", "MAGNETIC_FLUX_UNIT"));
    registry.put(
        "MAGNETIC_FLUX_DENSITY_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(
                instance,
                "MAGNETIC_FLUX_DENSITY_MEASURE_WITH_UNIT",
                "MAGNETIC_FLUX_DENSITY_UNIT"));
    registry.put(
        "INDUCTANCE_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(
                instance, "INDUCTANCE_MEASURE_WITH_UNIT", "INDUCTANCE_UNIT"));
    registry.put(
        "ILLUMINANCE_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(
                instance, "ILLUMINANCE_MEASURE_WITH_UNIT", "ILLUMINANCE_UNIT"));
    registry.put(
        "LUMINOUS_FLUX_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(
                instance, "LUMINOUS_FLUX_MEASURE_WITH_UNIT", "LUMINOUS_FLUX_UNIT"));
    registry.put(
        "LUMINOUS_INTENSITY_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(
                instance,
                "LUMINOUS_INTENSITY_MEASURE_WITH_UNIT",
                "LUMINOUS_INTENSITY_UNIT"));
    registry.put(
        "RADIOACTIVITY_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(
                instance, "RADIOACTIVITY_MEASURE_WITH_UNIT", "RADIOACTIVITY_UNIT"));
    registry.put(
        "ABSORBED_DOSE_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(
                instance, "ABSORBED_DOSE_MEASURE_WITH_UNIT", "ABSORBED_DOSE_UNIT"));
    registry.put(
        "DOSE_EQUIVALENT_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(
                instance, "DOSE_EQUIVALENT_MEASURE_WITH_UNIT", "DOSE_EQUIVALENT_UNIT"));
    registry.put(
        "ACCELERATION_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(
                instance, "ACCELERATION_MEASURE_WITH_UNIT", "ACCELERATION_UNIT"));
    registry.put(
        "VELOCITY_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(
                instance, "VELOCITY_MEASURE_WITH_UNIT", "VELOCITY_UNIT"));
    registry.put(
        "THERMAL_RESISTANCE_MEASURE_WITH_UNIT",
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(
                instance,
                "THERMAL_RESISTANCE_MEASURE_WITH_UNIT",
                "THERMAL_RESISTANCE_UNIT"));
    registry.put(
        "GLOBAL_UNIT_ASSIGNED_CONTEXT", StepEntityResolver::resolveGlobalUnitAssignedContext);
    registry.put(
        "GLOBAL_UNCERTAINTY_ASSIGNED_CONTEXT",
        StepEntityResolver::resolveGlobalUncertaintyAssignedContext);
    registry.put("MEASURE_WITH_UNIT", StepEntityResolver::resolveMeasureWithUnit);
    registry.put("DERIVED_UNIT_ELEMENT", StepEntityResolver::resolveDerivedUnitElement);
    registry.put("DERIVED_UNIT", StepEntityResolver::resolveDerivedUnit);
    registry.put("SI_UNIT", StepEntityResolver::resolveSiUnit);
    registry.put(
        "CONVERSION_BASED_UNIT_WITH_OFFSET",
        StepEntityResolver::resolveConversionBasedUnitWithOffset);
    registry.put("CONVERSION_BASED_UNIT", StepEntityResolver::resolveConversionBasedUnit);
    registry.put("CONTEXT_DEPENDENT_UNIT", StepEntityResolver::resolveContextDependentUnit);
    registry.put("NAMED_UNIT", StepEntityResolver::resolveNamedUnit);
    registry.put(
        "LENGTH_UNIT",
        (resolver, instance) -> resolver.resolveStandaloneUnitKind(instance, "LENGTH_UNIT"));
    registry.put(
        "MASS_UNIT",
        (resolver, instance) -> resolver.resolveStandaloneUnitKind(instance, "MASS_UNIT"));
    registry.put(
        "PLANE_ANGLE_UNIT",
        (resolver, instance) -> resolver.resolveStandaloneUnitKind(instance, "PLANE_ANGLE_UNIT"));
    registry.put(
        "SOLID_ANGLE_UNIT",
        (resolver, instance) -> resolver.resolveStandaloneUnitKind(instance, "SOLID_ANGLE_UNIT"));
    registry.put(
        "RATIO_UNIT",
        (resolver, instance) -> resolver.resolveStandaloneUnitKind(instance, "RATIO_UNIT"));
    registry.put(
        "AREA_UNIT",
        (resolver, instance) -> resolver.resolveStandaloneUnitKind(instance, "AREA_UNIT"));
    registry.put(
        "VOLUME_UNIT",
        (resolver, instance) -> resolver.resolveStandaloneUnitKind(instance, "VOLUME_UNIT"));
    registry.put(
        "TIME_UNIT",
        (resolver, instance) -> resolver.resolveStandaloneUnitKind(instance, "TIME_UNIT"));
    registry.put(
        "THERMODYNAMIC_TEMPERATURE_UNIT",
        (resolver, instance) ->
            resolver.resolveStandaloneUnitKind(instance, "THERMODYNAMIC_TEMPERATURE_UNIT"));
    registry.put(
        "ELECTRIC_CURRENT_UNIT",
        (resolver, instance) ->
            resolver.resolveStandaloneUnitKind(instance, "ELECTRIC_CURRENT_UNIT"));
    registry.put(
        "AMOUNT_OF_SUBSTANCE_UNIT",
        (resolver, instance) ->
            resolver.resolveStandaloneUnitKind(instance, "AMOUNT_OF_SUBSTANCE_UNIT"));
    registry.put(
        "LUMINOUS_FLUX_UNIT",
        (resolver, instance) -> resolver.resolveStandaloneUnitKind(instance, "LUMINOUS_FLUX_UNIT"));
    registry.put(
        "LUMINOUS_INTENSITY_UNIT",
        (resolver, instance) ->
            resolver.resolveStandaloneUnitKind(instance, "LUMINOUS_INTENSITY_UNIT"));
    registry.put(
        "ACCELERATION_UNIT",
        (resolver, instance) ->
            resolver.resolveStandaloneDerivedUnitKind(instance, "ACCELERATION_UNIT"));
    registry.put(
        "VELOCITY_UNIT",
        (resolver, instance) -> resolver.resolveStandaloneDerivedUnitKind(instance, "VELOCITY_UNIT"));
    registry.put(
        "THERMAL_RESISTANCE_UNIT",
        (resolver, instance) ->
            resolver.resolveStandaloneDerivedUnitKind(instance, "THERMAL_RESISTANCE_UNIT"));
    registry.put(
        "FREQUENCY_UNIT",
        (resolver, instance) -> resolver.resolveStandaloneDerivedUnitKind(instance, "FREQUENCY_UNIT"));
    registry.put(
        "FORCE_UNIT",
        (resolver, instance) -> resolver.resolveStandaloneDerivedUnitKind(instance, "FORCE_UNIT"));
    registry.put(
        "PRESSURE_UNIT",
        (resolver, instance) -> resolver.resolveStandaloneDerivedUnitKind(instance, "PRESSURE_UNIT"));
    registry.put(
        "ENERGY_UNIT",
        (resolver, instance) -> resolver.resolveStandaloneDerivedUnitKind(instance, "ENERGY_UNIT"));
    registry.put(
        "POWER_UNIT",
        (resolver, instance) -> resolver.resolveStandaloneDerivedUnitKind(instance, "POWER_UNIT"));
    registry.put(
        "ELECTRIC_CHARGE_UNIT",
        (resolver, instance) ->
            resolver.resolveStandaloneDerivedUnitKind(instance, "ELECTRIC_CHARGE_UNIT"));
    registry.put(
        "ELECTRIC_POTENTIAL_UNIT",
        (resolver, instance) ->
            resolver.resolveStandaloneDerivedUnitKind(instance, "ELECTRIC_POTENTIAL_UNIT"));
    registry.put(
        "CAPACITANCE_UNIT",
        (resolver, instance) -> resolver.resolveStandaloneDerivedUnitKind(instance, "CAPACITANCE_UNIT"));
    registry.put(
        "RESISTANCE_UNIT",
        (resolver, instance) -> resolver.resolveStandaloneDerivedUnitKind(instance, "RESISTANCE_UNIT"));
    registry.put(
        "CONDUCTANCE_UNIT",
        (resolver, instance) -> resolver.resolveStandaloneDerivedUnitKind(instance, "CONDUCTANCE_UNIT"));
    registry.put(
        "MAGNETIC_FLUX_UNIT",
        (resolver, instance) ->
            resolver.resolveStandaloneDerivedUnitKind(instance, "MAGNETIC_FLUX_UNIT"));
    registry.put(
        "MAGNETIC_FLUX_DENSITY_UNIT",
        (resolver, instance) ->
            resolver.resolveStandaloneDerivedUnitKind(instance, "MAGNETIC_FLUX_DENSITY_UNIT"));
    registry.put(
        "INDUCTANCE_UNIT",
        (resolver, instance) -> resolver.resolveStandaloneDerivedUnitKind(instance, "INDUCTANCE_UNIT"));
    registry.put(
        "ILLUMINANCE_UNIT",
        (resolver, instance) -> resolver.resolveStandaloneDerivedUnitKind(instance, "ILLUMINANCE_UNIT"));
    registry.put(
        "RADIOACTIVITY_UNIT",
        (resolver, instance) ->
            resolver.resolveStandaloneDerivedUnitKind(instance, "RADIOACTIVITY_UNIT"));
    registry.put(
        "ABSORBED_DOSE_UNIT",
        (resolver, instance) ->
            resolver.resolveStandaloneDerivedUnitKind(instance, "ABSORBED_DOSE_UNIT"));
    registry.put(
        "DOSE_EQUIVALENT_UNIT",
        (resolver, instance) ->
            resolver.resolveStandaloneDerivedUnitKind(instance, "DOSE_EQUIVALENT_UNIT"));
    registry.put("POINT", StepEntityResolver::resolvePoint);
    registry.put("RATIONAL_B_SPLINE_CURVE", StepEntityResolver::resolveRationalBSplineCurve);
    registry.put("RATIONAL_B_SPLINE_SURFACE", StepEntityResolver::resolveRationalBSplineSurface);
    registry.put("B_SPLINE_CURVE_WITH_KNOTS", StepEntityResolver::resolveBSplineCurveWithKnots);
    registry.put("B_SPLINE_SURFACE_WITH_KNOTS", StepEntityResolver::resolveBSplineSurfaceWithKnots);
    registry.put("PIECEWISE_BEZIER_CURVE", StepEntityResolver::resolvePiecewiseBezierCurve);
    registry.put("PIECEWISE_BEZIER_SURFACE", StepEntityResolver::resolvePiecewiseBezierSurface);
    registry.put("B_SPLINE_CURVE", StepEntityResolver::resolveBSplineCurve);
    registry.put("B_SPLINE_SURFACE", StepEntityResolver::resolveBSplineSurface);
    registry.put("BEZIER_CURVE", StepEntityResolver::resolveBezierCurve);
    registry.put("BEZIER_SURFACE", StepEntityResolver::resolveBezierSurface);
    registry.put("UNIFORM_CURVE", StepEntityResolver::resolveUniformCurve);
    registry.put("QUASI_UNIFORM_CURVE", StepEntityResolver::resolveQuasiUniformCurve);
    registry.put("FACE_BASED_SURFACE_MODEL", StepEntityResolver::resolveFaceBasedSurfaceModel);
    registry.put("SHELL_BASED_SURFACE_MODEL", StepEntityResolver::resolveShellBasedSurfaceModel);
    registry.put("SURFACE_MODEL", StepEntityResolver::resolveSurfaceModel);
    registry.put("UNIFORM_SURFACE", StepEntityResolver::resolveUniformSurface);
    registry.put("QUASI_UNIFORM_SURFACE", StepEntityResolver::resolveQuasiUniformSurface);
    registry.put("COMPOSITE_CURVE_SEGMENT", StepEntityResolver::resolveCompositeCurveSegment);
    registry.put(
        "COMPOSITE_CURVE_ON_SURFACE", StepEntityResolver::resolveCompositeCurveOnSurface);
    registry.put("COMPOSITE_CURVE", StepEntityResolver::resolveCompositeCurve);
    registry.put("POLYLINE", StepEntityResolver::resolvePolyline);
    registry.put("BOUNDED_CURVE", StepEntityResolver::resolveBoundedCurve);
    registry.put("BOUNDED_SURFACE", StepEntityResolver::resolveBoundedSurface);
    registry.put("CURVE", StepEntityResolver::resolveCurve);
    registry.put("SURFACE", StepEntityResolver::resolveSurface);
    registry.put("OFFSET_CURVE_3D", StepEntityResolver::resolveOffsetCurve3D);
    registry.put("OFFSET_SURFACE", StepEntityResolver::resolveOffsetSurface);
    registry.put("VERTEX", StepEntityResolver::resolveVertex);
    registry.put("EDGE_BASED_WIREFRAME_MODEL", StepEntityResolver::resolveEdgeBasedWireframeModel);
    registry.put("CONNECTED_EDGE_SET", StepEntityResolver::resolveConnectedEdgeSet);
    registry.put("SUBEDGE", StepEntityResolver::resolveSubedge);
    registry.put("EDGE", StepEntityResolver::resolveEdge);
    registry.put("FACE", StepEntityResolver::resolveFace);
    registry.put("MANIFOLD_SOLID_BREP", StepEntityResolver::resolveManifoldSolidBrep);
    registry.put("SOLID_MODEL", StepEntityResolver::resolveSolidModel);
    registry.put(
        "GEOMETRIC_REPRESENTATION_ITEM",
        StepEntityResolver::resolveGeometricRepresentationItem);
    registry.put(
        "TOPOLOGICAL_REPRESENTATION_ITEM",
        StepEntityResolver::resolveTopologicalRepresentationItem);
    registry.put("REPRESENTATION_ITEM", StepEntityResolver::resolveRepresentationItem);
    registry.put("REPRESENTATION_CONTEXT", StepEntityResolver::resolveRepresentationContext);
    registry.put(
        "DEFINITIONAL_REPRESENTATION",
        (resolver, instance) ->
            resolver.resolveRepresentation(instance, "DEFINITIONAL_REPRESENTATION", false));
    registry.put("COLOUR_RGB", StepEntityResolver::resolveColourRgb);
    registry.put(
        "DRAUGHTING_PRE_DEFINED_CURVE_FONT",
        StepEntityResolver::resolveDraughtingPreDefinedCurveFont);
    registry.put(
        "DRAUGHTING_PRE_DEFINED_COLOUR", StepEntityResolver::resolveDraughtingPreDefinedColour);
    registry.put("CURVE_STYLE", StepEntityResolver::resolveCurveStyle);
    registry.put("FILL_AREA_STYLE_COLOUR", StepEntityResolver::resolveFillAreaStyleColour);
    registry.put("FILL_AREA_STYLE", StepEntityResolver::resolveFillAreaStyle);
    registry.put("SURFACE_STYLE_FILL_AREA", StepEntityResolver::resolveSurfaceStyleFillArea);
    registry.put("SURFACE_SIDE_STYLE", StepEntityResolver::resolveSurfaceSideStyle);
    registry.put("SURFACE_STYLE_USAGE", StepEntityResolver::resolveSurfaceStyleUsage);
    registry.put(
        "PRESENTATION_STYLE_ASSIGNMENT", StepEntityResolver::resolvePresentationStyleAssignment);
    registry.put("STYLED_ITEM", StepEntityResolver::resolveStyledItem);
    registry.put("OVER_RIDING_STYLED_ITEM", StepEntityResolver::resolveOverRidingStyledItem);
    registry.put(
        "PRESENTATION_LAYER_ASSIGNMENT", StepEntityResolver::resolvePresentationLayerAssignment);
    registry.put("ANNOTATION_TEXT_OCCURRENCE", StepEntityResolver::resolveAnnotationTextOccurrence);
    registry.put("GEOMETRIC_CURVE_SET", StepEntityResolver::resolveGeometricCurveSet);
    registry.put("GEOMETRIC_SET", StepEntityResolver::resolveGeometricSet);
    registry.put("POINT_SET", StepEntityResolver::resolvePointSet);
    registry.put("DRAUGHTING_CALLOUT", StepEntityResolver::resolveDraughtingCallout);
    registry.put(
        "GEOMETRIC_ITEM_SPECIFIC_USAGE", StepEntityResolver::resolveGeometricItemSpecificUsage);
    registry.put(
        "MEASURE_REPRESENTATION_ITEM", StepEntityResolver::resolveMeasureRepresentationItem);
    registry.put(
        "DESCRIPTIVE_REPRESENTATION_ITEM",
        StepEntityResolver::resolveDescriptiveRepresentationItem);
    registry.put(
        "VALUE_REPRESENTATION_ITEM", StepEntityResolver::resolveValueRepresentationItem);
    registry.put("CARTESIAN_POINT", StepEntityResolver::resolveCartesianPoint);
    registry.put("DIRECTION", StepEntityResolver::resolveDirection);
    registry.put("VECTOR", StepEntityResolver::resolveVector);
    registry.put("AXIS1_PLACEMENT", StepEntityResolver::resolveAxis1Placement);
    registry.put("AXIS2_PLACEMENT_2D", StepEntityResolver::resolveAxis2Placement2D);
    registry.put("AXIS2_PLACEMENT_3D", StepEntityResolver::resolveAxis2Placement3D);
    registry.put("LINE", StepEntityResolver::resolveLine);
    registry.put("PLANE", StepEntityResolver::resolvePlane);
    registry.put("CIRCLE", StepEntityResolver::resolveCircle);
    registry.put("ELLIPSE", StepEntityResolver::resolveEllipse);
    registry.put("SURFACE_CURVE", StepEntityResolver::resolveSurfaceCurve);
    registry.put("SEAM_CURVE", StepEntityResolver::resolveSeamCurve);
    registry.put("DEGENERATE_PCURVE", StepEntityResolver::resolveDegeneratePcurve);
    registry.put("PCURVE", StepEntityResolver::resolvePcurve);
    registry.put("CYLINDRICAL_SURFACE", StepEntityResolver::resolveCylindricalSurface);
    registry.put("CONICAL_SURFACE", StepEntityResolver::resolveConicalSurface);
    registry.put("TOROIDAL_SURFACE", StepEntityResolver::resolveToroidalSurface);
    registry.put("SPHERICAL_SURFACE", StepEntityResolver::resolveSphericalSurface);
    registry.put(
        "SURFACE_OF_LINEAR_EXTRUSION", StepEntityResolver::resolveSurfaceOfLinearExtrusion);
    registry.put("SURFACE_OF_REVOLUTION", StepEntityResolver::resolveSurfaceOfRevolution);
    registry.put("TRIMMED_CURVE", StepEntityResolver::resolveTrimmedCurve);
    registry.put("VERTEX_POINT", StepEntityResolver::resolveVertexPoint);
    registry.put("EDGE_CURVE", StepEntityResolver::resolveEdgeCurve);
    registry.put("ORIENTED_EDGE", StepEntityResolver::resolveOrientedEdge);
    registry.put("VERTEX_LOOP", StepEntityResolver::resolveVertexLoop);
    registry.put("POLY_LOOP", StepEntityResolver::resolvePolyLoop);
    registry.put("OPEN_PATH", StepEntityResolver::resolveOpenPath);
    registry.put("SUBPATH", StepEntityResolver::resolveSubpath);
    registry.put("ORIENTED_PATH", StepEntityResolver::resolveOrientedPath);
    registry.put("PATH", StepEntityResolver::resolvePath);
    registry.put("EDGE_LOOP", StepEntityResolver::resolveEdgeLoop);
    registry.put(
        "FACE_OUTER_BOUND", (resolver, instance) -> resolver.resolveFaceBound(instance, true));
    registry.put("FACE_BOUND", (resolver, instance) -> resolver.resolveFaceBound(instance, false));
    registry.put("FACE_SURFACE", StepEntityResolver::resolveFaceSurface);
    registry.put("ADVANCED_FACE", StepEntityResolver::resolveAdvancedFace);
    registry.put("ORIENTED_FACE", StepEntityResolver::resolveOrientedFace);
    registry.put("VERTEX_SHELL", StepEntityResolver::resolveVertexShell);
    registry.put("WIRE_SHELL", StepEntityResolver::resolveWireShell);
    registry.put("CONNECTED_FACE_SUB_SET", StepEntityResolver::resolveConnectedFaceSubSet);
    registry.put("CONNECTED_FACE_SET", StepEntityResolver::resolveConnectedFaceSet);
    registry.put("SURFACED_OPEN_SHELL", StepEntityResolver::resolveSurfacedOpenShell);
    registry.put("ORIENTED_OPEN_SHELL", StepEntityResolver::resolveOrientedOpenShell);
    registry.put("ORIENTED_CLOSED_SHELL", StepEntityResolver::resolveOrientedClosedShell);
    registry.put("SHELL_BASED_WIREFRAME_MODEL", StepEntityResolver::resolveShellBasedWireframeModel);
    registry.put("OPEN_SHELL", StepEntityResolver::resolveOpenShell);
    registry.put("CLOSED_SHELL", StepEntityResolver::resolveClosedShell);
    return registry;
  }

  private interface EntityFactory {
    StepEntity create(StepEntityResolver resolver, StepEntityInstance instance);
  }
}
