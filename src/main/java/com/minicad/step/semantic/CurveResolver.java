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
 * Curve resolver - handles curve-on-surface, pcurve, trimmed, composite,
 * indexed poly, and 2D/degenerate curve entities.
 * Extracted from StepEntityResolver to reduce file size.
 */
final class CurveResolver {

  private final StepEntityResolver resolver;

  CurveResolver(StepEntityResolver resolver) {
    this.resolver = resolver;
  }

  // === Curve On Surface Entities ===

  StepCompositeCurveOnSurface resolveCompositeCurveOnSurface(StepEntityInstance instance) {
    return resolveCompositeCurveOnSurface(instance, "COMPOSITE_CURVE_ON_SURFACE");
  }

  StepCompositeCurveOnSurface resolveCompositeCurveOnSurface(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepCompositeCurveOnSurface(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.referenceList(
            instance,
            definition,
            1,
            StepCompositeCurveSegment.class,
            entityName + " segments must contain COMPOSITE_CURVE_SEGMENT references"),
        resolver.booleanValue(instance, definition, 2));
  }

  StepCompositeCurveOnSurface3D resolveCompositeCurveOnSurface3D(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "COMPOSITE_CURVE_ON_SURFACE_3D");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    List<StepCompositeCurveSegment> segments =
        resolver.referenceList(
            instance,
            definition,
            1,
            StepCompositeCurveSegment.class,
            "COMPOSITE_CURVE_ON_SURFACE_3D segments must reference COMPOSITE_CURVE_SEGMENT");
    StepEntity surface = resolver.resolve(resolver.referenceId(instance, definition, 2));
    if (!StepResolverValueHelpers.isSupportedSurfaceReference(surface)) {
      throw new UnsupportedStepEntityException(
          "COMPOSITE_CURVE_ON_SURFACE_3D surface must reference a supported surface");
    }
    return new StepCompositeCurveOnSurface3D(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        segments,
        surface,
        resolver.booleanValue(instance, definition, 3));
  }

  StepSeamCurve resolveSeamCurve(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "SEAM_CURVE");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    StepEntity curve3d = resolver.resolve(resolver.referenceId(instance, definition, 1));
    if (!StepResolverValueHelpers.isSupportedCurveReference(curve3d)) {
      throw new UnsupportedStepEntityException(
          "SEAM_CURVE curve_3d must reference a supported curve");
    }
    List<StepEntity> associatedGeometry =
        resolver.entityReferenceList(
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
        resolver.stringValue(instance, definition, 0),
        curve3d,
        associatedGeometry,
        resolver.enumValue(instance, definition, 3));
  }

  StepSurfaceCurve resolveSurfaceCurve(StepEntityInstance instance) {
    return resolveSurfaceCurve(instance, "SURFACE_CURVE");
  }

  StepSurfaceCurve resolveSurfaceCurve(StepEntityInstance instance, String entityName) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    StepEntity curve3d = resolver.resolve(resolver.referenceId(instance, definition, 1));
    if (!StepResolverValueHelpers.isSupportedCurveReference(curve3d)) {
      throw new UnsupportedStepEntityException(
          entityName + " curve_3d must reference a supported curve");
    }
    List<StepEntity> associatedGeometry =
        resolver.entityReferenceList(
            instance,
            definition,
            2,
            entityName + " associated_geometry must contain entity references");
    for (StepEntity associated : associatedGeometry) {
      if (!(associated instanceof StepPcurve) && !(associated instanceof StepDegeneratePcurve)) {
        throw new UnsupportedStepEntityException(
            entityName + " associated_geometry currently supports PCURVE or DEGENERATE_PCURVE references");
      }
    }
    return new StepSurfaceCurve(
        instance.id(),
        entityName,
        resolver.stringValue(instance, definition, 0),
        curve3d,
        associatedGeometry,
        resolver.enumValue(instance, definition, 3));
  }

  // === Pcurve Entities ===

  StepDegeneratePcurve resolveDegeneratePcurve(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "DEGENERATE_PCURVE");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    StepEntity basisSurface = resolver.resolve(resolver.referenceId(instance, definition, 1));
    if (!StepResolverValueHelpers.isSupportedSurfaceReference(basisSurface)) {
      throw new UnsupportedStepEntityException(
          "DEGENERATE_PCURVE basis surface must reference a supported surface");
    }
    StepRepresentation representation =
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 2),
            StepRepresentation.class,
            "DEGENERATE_PCURVE reference_to_curve must reference REPRESENTATION");
    if (representation.items().size() != 1) {
      throw new UnsupportedStepEntityException(
          "DEGENERATE_PCURVE reference_to_curve must contain exactly one 2D curve item");
    }
    StepEntity item = representation.items().get(0);
    if (!StepResolverValueHelpers.isSupportedCurveReference(item)) {
      throw new UnsupportedStepEntityException(
          "DEGENERATE_PCURVE reference_to_curve must contain a supported curve item");
    }
    return new StepDegeneratePcurve(
        instance.id(), resolver.stringValue(instance, definition, 0), basisSurface, representation);
  }

  StepPcurve resolvePcurve(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "PCURVE");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    StepEntity basisSurface = resolver.resolve(resolver.referenceId(instance, definition, 1));
    if (!StepResolverValueHelpers.isSupportedSurfaceReference(basisSurface)) {
      throw new UnsupportedStepEntityException(
          "PCURVE basis surface must reference a supported surface");
    }
    StepRepresentation representation =
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 2),
            StepRepresentation.class,
            "PCURVE reference_to_curve must reference REPRESENTATION");
    if (representation.items().size() != 1) {
      throw new UnsupportedStepEntityException(
          "PCURVE reference_to_curve must contain exactly one 2D curve item");
    }
    StepEntity item = representation.items().get(0);
    if (!StepResolverValueHelpers.isSupportedCurveReference(item)) {
      throw new UnsupportedStepEntityException(
          "PCURVE reference_to_curve must contain a supported curve item");
    }
    return new StepPcurve(
        instance.id(), resolver.stringValue(instance, definition, 0), basisSurface, representation);
  }

  // === Trimmed / Conic / Free-Form Entities ===

  StepConicCurve resolveConicCurve(StepEntityInstance instance, String entityName, int parameterCount) {
    StepEntityDefinition definition = resolver.definition(instance, entityName);
    StepEntityResolver.requireParameterCount(instance, definition, parameterCount + 2);
    StepEntity position = resolver.resolve(resolver.referenceId(instance, definition, 1));
    if (!(position instanceof StepAxis2Placement3D)
        && !(position instanceof StepAxis2Placement2D)) {
      throw new StepResolutionException(
          entityName + " position must reference AXIS2_PLACEMENT_3D or AXIS2_PLACEMENT_2D");
    }
    List<Double> parameters = new ArrayList<>(parameterCount);
    for (int index = 0; index < parameterCount; index++) {
      parameters.add(resolver.numberValue(instance, definition, index + 2));
    }
    return new StepConicCurve(
        instance.id(), resolver.stringValue(instance, definition, 0), position, parameters, entityName);
  }

  List<List<StepEntity>> resolveFreeFormControlPoints(StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = resolver.unwrapTyped(definition.parameters().get(index));
    if (!(value instanceof StepValue.ListValue)) {
      throw new StepResolutionException(
          definition.name() + " parameter " + index + " must be a nested list");
    }
    StepValue.ListValue outerList = (StepValue.ListValue) value;
    List<List<StepEntity>> result = new ArrayList<>();
    for (StepValue outerElement : outerList.elements()) {
      StepValue unwrappedOuter = resolver.unwrapTyped(outerElement);
      if (!(unwrappedOuter instanceof StepValue.ListValue)) {
        throw new StepResolutionException(
            definition.name() + " control_points must contain nested lists");
      }
      StepValue.ListValue innerList = (StepValue.ListValue) unwrappedOuter;
      List<StepEntity> row = new ArrayList<>();
      for (StepValue innerElement : innerList.elements()) {
        StepValue unwrappedInner = resolver.unwrapTyped(innerElement);
        if (!(unwrappedInner instanceof StepValue.ReferenceValue)) {
          throw new StepResolutionException(
              definition.name() + " control_points inner elements must be references");
        }
        StepValue.ReferenceValue referenceValue = (StepValue.ReferenceValue) unwrappedInner;
        row.add(resolver.resolve(referenceValue.id()));
      }
      result.add(List.copyOf(row));
    }
    return List.copyOf(result);
  }

  StepTrimmedCurve resolveTrimmedCurve(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "TRIMMED_CURVE");
    StepEntityResolver.requireParameterCount(instance, definition, 6);
    StepEntity basisCurve = resolver.resolve(resolver.referenceId(instance, definition, 1));
    if (!StepResolverValueHelpers.isSupportedCurveReference(basisCurve)) {
      throw new UnsupportedStepEntityException(
          "TRIMMED_CURVE basis curve must reference a supported curve");
    }
    // Trims can be entity references (e.g., #5) or parameter values (e.g., 0.0).
    // Pass through raw StepValues so the builder can handle both.
    List<StepValue> trim1 = resolver.trimValues(instance, definition, 2, "TRIMMED_CURVE trim_1");
    List<StepValue> trim2 = resolver.trimValues(instance, definition, 3, "TRIMMED_CURVE trim_2");
    return new StepTrimmedCurve(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        basisCurve,
        trim1,
        trim2,
        resolver.booleanValue(instance, definition, 4),
        resolver.enumValue(instance, definition, 5));
  }

  // === Indexed Poly / 2D / Degenerate Curve Entities ===

  StepCurve2D resolveCurve2D(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "CURVE_2D");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    // Equation parameter may be wrapped in nested list: ((a,b,c,d)) -> ListValue containing ListValue
    StepValue eqParam = resolver.unwrapTyped(definition.parameters().get(2));
    if (eqParam instanceof StepValue.ListValue) {
      StepValue.ListValue outerList = (StepValue.ListValue) eqParam;
      if (outerList.elements().size() == 1 && outerList.elements().get(0) instanceof StepValue.ListValue) {
        StepValue.ListValue innerList = (StepValue.ListValue) outerList.elements().get(0);
        eqParam = innerList;
      }
    }
    List<Double> eqList = StepResolverValueHelpers.extractNumberList(definition, eqParam, "CURVE_2D");
    double[] equation = new double[eqList.size()];
    for (int i = 0; i < eqList.size(); i++) {
      equation[i] = eqList.get(i);
    }
    return new StepCurve2D(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepAxis2Placement2D.class,
            "CURVE_2D position must reference AXIS2_PLACEMENT_2D"),
        equation);
  }

  StepDegenerateCurve resolveDegenerateCurve(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "DEGENERATE_CURVE");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    StepEntity basisCurve = resolver.resolve(resolver.referenceId(instance, definition, 1));
    if (!StepResolverValueHelpers.isSupportedCurveReference(basisCurve)) {
      throw new UnsupportedStepEntityException(
          "DEGENERATE_CURVE basis_curve must reference a supported curve");
    }
    return new StepDegenerateCurve(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        basisCurve);
  }

  StepDegenerateCurve2D resolveDegenerateCurve2D(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "DEGENERATE_CURVE_2D");
    StepEntityResolver.requireParameterCount(instance, definition, 2);
    return new StepDegenerateCurve2D(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.requireEntity(
            resolver.referenceId(instance, definition, 1),
            StepCartesianPoint.class,
            "DEGENERATE_CURVE_2D point must reference CARTESIAN_POINT"));
  }

  StepIndexedPolyCurve resolveIndexedPolyCurve(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "INDEXED_POLY_CURVE");
    StepEntityResolver.requireParameterCount(instance, definition, 4);
    List<StepCartesianPoint> points =
        resolver.referenceList(
            instance,
            definition,
            1,
            StepCartesianPoint.class,
            "INDEXED_POLY_CURVE points must reference CARTESIAN_POINT");
    List<Integer> indices =
        resolver.integerList(instance, definition, 2);
    return new StepIndexedPolyCurve(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        points,
        indices,
        resolver.booleanValue(instance, definition, 3));
  }

  StepIndexedPolyCurve2D resolveIndexedPolyCurve2D(StepEntityInstance instance) {
    StepEntityDefinition definition = resolver.definition(instance, "INDEXED_POLY_CURVE_2D");
    StepEntityResolver.requireParameterCount(instance, definition, 3);
    return new StepIndexedPolyCurve2D(
        instance.id(),
        resolver.stringValue(instance, definition, 0),
        resolver.referenceList(
            instance,
            definition,
            1,
            StepCartesianPoint.class,
            "INDEXED_POLY_CURVE_2D points must reference CARTESIAN_POINT"),
        resolver.integerList(instance, definition, 2));
  }
}
