package com.minicad.step.semantic;

import com.minicad.common.StepResolutionException;
import com.minicad.common.UnsupportedStepEntityException;

import com.minicad.step.model.StepAnnotationCurveOccurrence;
import com.minicad.step.model.StepAnnotationFillAreaOccurrence;
import com.minicad.step.model.StepAnnotationPlaceholderOccurrence;
import com.minicad.step.model.StepAnnotationPlane;
import com.minicad.step.model.StepAnnotationPointOccurrence;
import com.minicad.step.model.StepAnnotationSubfigureOccurrence;
import com.minicad.step.model.StepAnnotationSymbolOccurrence;
import com.minicad.step.model.StepAnnotationTextOccurrence;
import com.minicad.step.model.StepBSplineCurve;
import com.minicad.step.model.StepBSplineCurveWithKnots;
import com.minicad.step.model.StepBSplineSurface;
import com.minicad.step.model.StepBSplineSurfaceWithKnots;
import com.minicad.step.model.StepBezierCurve;
import com.minicad.step.model.StepBezierSurface;
import com.minicad.step.model.StepBoundedCurve;
import com.minicad.step.model.StepBoundedSurface;
import com.minicad.step.model.StepCircle;
import com.minicad.step.model.StepClothoid;
import com.minicad.step.model.StepCompositeCurve;
import com.minicad.step.model.StepCompositeCurveOnSurface3D;
import com.minicad.step.model.StepCompositeCurveOnSurface;
import com.minicad.step.model.StepConicCurve;
import com.minicad.step.model.StepConicalSurface;
import com.minicad.step.model.StepContextDependentUnit;
import com.minicad.step.model.StepConversionBasedUnit;
import com.minicad.step.model.StepConversionBasedUnitWithOffset;
import com.minicad.step.model.StepCurve2D;
import com.minicad.step.model.StepCurve;
import com.minicad.step.model.StepCurveBoundedSurface;
import com.minicad.step.model.StepCylindricalSurface;
import com.minicad.step.model.StepDegenerateCurve;
import com.minicad.step.model.StepDegeneratePcurve;
import com.minicad.step.model.StepDegenerateToroidalSurface;
import com.minicad.step.model.StepDerivedUnit;
import com.minicad.step.model.StepDimensionCurve;
import com.minicad.step.model.StepDraughtingAnnotationOccurrence;
import com.minicad.step.model.StepEdgeWire;
import com.minicad.step.model.StepEllipse;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepGeometricReplica;
import com.minicad.step.model.StepIndexedPolyCurve;
import com.minicad.step.model.StepLeaderCurve;
import com.minicad.step.model.StepLine;
import com.minicad.step.model.StepLineSegment;
import com.minicad.step.model.StepMappedItem;
import com.minicad.step.model.StepNamedUnit;
import com.minicad.step.model.StepOffsetCurve2D;
import com.minicad.step.model.StepOffsetCurve3D;
import com.minicad.step.model.StepOffsetSurface2;
import com.minicad.step.model.StepOffsetSurface;
import com.minicad.step.model.StepOrientedCurve;
import com.minicad.step.model.StepOrientedSurface;
import com.minicad.step.model.StepPcurve;
import com.minicad.step.model.StepPiecewiseBezierCurve;
import com.minicad.step.model.StepPiecewiseBezierSurface;
import com.minicad.step.model.StepPlane;
import com.minicad.step.model.StepPolyline;
import com.minicad.step.model.StepProjectionCurve;
import com.minicad.step.model.StepQuasiUniformCurve;
import com.minicad.step.model.StepQuasiUniformSurface;
import com.minicad.step.model.StepRationalBSplineCurve;
import com.minicad.step.model.StepRationalBSplineSurface;
import com.minicad.step.model.StepRectangularCompositeSurface;
import com.minicad.step.model.StepRectangularTrimmedSurface;
import com.minicad.step.model.StepSeamCurve;
import com.minicad.step.model.StepSiUnit;
import com.minicad.step.model.StepSphericalSurface;
import com.minicad.step.model.StepSubface;
import com.minicad.step.model.StepSurface;
import com.minicad.step.model.StepSurfaceCurve;
import com.minicad.step.model.StepSurfaceOfConstantRadius;
import com.minicad.step.model.StepSurfaceOfLinearExtrusion;
import com.minicad.step.model.StepSurfaceOfRevolution;
import com.minicad.step.model.StepSurfacePatch;
import com.minicad.step.model.StepSweptDiskSolid;
import com.minicad.step.model.StepTerminatorSymbol;
import com.minicad.step.model.StepToroidalSurface;
import com.minicad.step.model.StepTrimmedCurve;
import com.minicad.step.model.StepUniformCurve;
import com.minicad.step.model.StepUniformSurface;
import com.minicad.step.syntax.StepEntityDefinition;
import com.minicad.step.syntax.StepEntityInstance;
import com.minicad.step.syntax.StepValue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Stateless value helpers extracted from StepEntityResolver.
 *
 * <p>Provenance: every method body in this class was relocated byte-for-byte
 * (verbatim move of whole methods, including attached comments) from
 * {@link StepEntityResolver}; the only textual changes are the insertion of
 * the {@code static} modifier on formerly instance-side signatures and this
 * class header. These helpers read only their own parameters (plus static
 * {@link StepParameterReader} utilities) and never touch resolver instance
 * state such as {@code resolved}, {@code resolutionStack} or
 * {@code instancesById}.
 */
final class StepResolverValueHelpers {

  private StepResolverValueHelpers() {
  }

  static StepEntityDefinition definition(StepEntityInstance instance, String name) {
    return instance.requireDefinition(name);
  }

  static void requireParameterCount(
      StepEntityInstance instance, StepEntityDefinition definition, int expected) {
    StepParameterReader.requireParameterCount(instance, definition, expected);
  }

  static void requireParameterCountIn(
      StepEntityInstance instance, StepEntityDefinition definition, int... expectedCounts) {
    StepParameterReader.requireParameterCountIn(instance, definition, expectedCounts);
  }

  static String stringValue(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (value instanceof StepValue.StringValue) {
      StepValue.StringValue stringValue = (StepValue.StringValue) value;
      return stringValue.value();
    }
    throw StepParameterReader.parameterTypeMismatch(instance, definition, index, "string");
  }

  static String optionalStringValue(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = definition.parameters().get(index);
    if (isUnset(value)) {
      return "";
    }
    return stringValue(instance, definition, index);
  }

  static List<String> optionalStringListValue(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = definition.parameters().get(index);
    if (isUnset(value)) {
      return List.of();
    }
    StepValue unwrapped = unwrapTyped(value);
    if (!(unwrapped instanceof StepValue.ListValue)) {
      throw StepParameterReader.parameterTypeMismatch(instance, definition, index, "string list");
    }
    StepValue.ListValue listValue = (StepValue.ListValue) unwrapped;
    List<String> result = new ArrayList<>(listValue.elements().size());
    for (StepValue element : listValue.elements()) {
      StepValue unwrappedElement = unwrapTyped(element);
      if (!(unwrappedElement instanceof StepValue.StringValue)) {
        throw new StepResolutionException(
            definition.name() + " string list must contain only strings");
      }
      StepValue.StringValue stringValue = (StepValue.StringValue) unwrappedElement;
      result.add(stringValue.value());
    }
    return List.copyOf(result);
  }

  static double numberValue(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (value instanceof StepValue.NumberValue) {
      StepValue.NumberValue numberValue = (StepValue.NumberValue) value;
      return numberValue.value();
    }
    throw StepParameterReader.parameterTypeMismatch(instance, definition, index, "number");
  }

  static int integerValue(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    double value = numberValue(instance, definition, index);
    if (value != Math.rint(value)) {
      throw new StepResolutionException(
          "entity #"
              + instance.id()
              + " "
              + definition.name()
              + " parameter "
              + index
              + " type mismatch: expected integer, actual number");
    }
    return (int) value;
  }

  static Integer optionalIntegerValue(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = definition.parameters().get(index);
    if (isUnset(value)) {
      return null;
    }
    return integerValue(instance, definition, index);
  }

  static Double optionalNumberValue(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = definition.parameters().get(index);
    if (isUnset(value)) {
      return null;
    }
    return numberValue(instance, definition, index);
  }

  static String enumValue(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (value instanceof StepValue.EnumValue) {
      StepValue.EnumValue enumValue = (StepValue.EnumValue) value;
      return enumValue.value();
    }
    throw StepParameterReader.parameterTypeMismatch(instance, definition, index, "enum");
  }

  static boolean booleanValue(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    String enumVal = enumValue(instance, definition, index);
    if ("T".equals(enumVal)) {
      return true;
    }
    if ("F".equals(enumVal)) {
      return false;
    }
    throw new StepResolutionException(
        "entity #"
            + instance.id()
            + " "
            + definition.name()
            + " parameter "
            + index
            + " type mismatch: expected boolean .T. or .F., actual enum ."
            + enumVal
            + ".");
  }

  static int referenceId(StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (value instanceof StepValue.ReferenceValue) {
      StepValue.ReferenceValue referenceValue = (StepValue.ReferenceValue) value;
      return referenceValue.id();
    }
    throw StepParameterReader.parameterTypeMismatch(instance, definition, index, "reference");
  }

  static List<Double> coordinateTriple(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    return coordinateList(instance, definition, index, 3, 3);
  }

  static List<Double> doubleList(StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (!(value instanceof StepValue.ListValue)) {
      throw StepParameterReader.parameterTypeMismatch(instance, definition, index, "list");
    }
    StepValue.ListValue listValue = (StepValue.ListValue) value;
    return listValue.elements().stream()
        .map(v -> numberValueFrom(instance, v, definition, index))
        .collect(Collectors.toList());
  }

  static double numberValueFrom(
      StepEntityInstance instance, StepValue value, StepEntityDefinition definition, int index) {
    value = unwrapTyped(value);
    if (!(value instanceof StepValue.NumberValue)) {
      throw parameterElementTypeMismatch(instance, definition, index, "number", value);
    }
    StepValue.NumberValue numberValue = (StepValue.NumberValue) value;
    return numberValue.value();
  }

  static List<Double> coordinateList(
      StepEntityInstance instance,
      StepEntityDefinition definition,
      int index,
      int minSize,
      int maxSize) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (!(value instanceof StepValue.ListValue)) {
      throw StepParameterReader.parameterTypeMismatch(instance, definition, index, "list");
    }
    StepValue.ListValue listValue = (StepValue.ListValue) value;
    if (listValue.elements().size() < minSize || listValue.elements().size() > maxSize) {
      throw new UnsupportedStepEntityException(
          definition.name() + " only supports " + minSize + "D to " + maxSize + "D coordinates");
    }
    List<Double> result = new ArrayList<>(listValue.elements().size());
    for (StepValue element : listValue.elements()) {
      StepValue unwrapped = unwrapTyped(element);
      if (unwrapped instanceof StepValue.NumberValue) {
        StepValue.NumberValue numberValue = (StepValue.NumberValue) unwrapped;
        result.add(numberValue.value());
      } else {
        throw parameterElementTypeMismatch(instance, definition, index, "number", element);
      }
    }
    return List.copyOf(result);
  }

  static List<Double> numberList(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (!(value instanceof StepValue.ListValue)) {
      throw StepParameterReader.parameterTypeMismatch(instance, definition, index, "list");
    }
    StepValue.ListValue listValue = (StepValue.ListValue) value;
    List<Double> result = new ArrayList<>(listValue.elements().size());
    for (StepValue element : listValue.elements()) {
      StepValue unwrapped = unwrapTyped(element);
      if (!(unwrapped instanceof StepValue.NumberValue)) {
        throw parameterElementTypeMismatch(instance, definition, index, "number", element);
      }
      StepValue.NumberValue numberValue = (StepValue.NumberValue) unwrapped;
      result.add(numberValue.value());
    }
    return List.copyOf(result);
  }

  static List<Integer> intList(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (!(value instanceof StepValue.ListValue)) {
      throw StepParameterReader.parameterTypeMismatch(instance, definition, index, "list");
    }
    StepValue.ListValue listValue = (StepValue.ListValue) value;
    List<Integer> result = new ArrayList<>(listValue.elements().size());
    for (StepValue element : listValue.elements()) {
      StepValue unwrapped = unwrapTyped(element);
      if (!(unwrapped instanceof StepValue.NumberValue)) {
        throw parameterElementTypeMismatch(instance, definition, index, "number", element);
      }
      StepValue.NumberValue numberValue = (StepValue.NumberValue) unwrapped;
      result.add((int) numberValue.value());
    }
    return List.copyOf(result);
  }

  static List<String> stringList(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (!(value instanceof StepValue.ListValue)) {
      throw StepParameterReader.parameterTypeMismatch(instance, definition, index, "list");
    }
    StepValue.ListValue listValue = (StepValue.ListValue) value;
    List<String> result = new ArrayList<>(listValue.elements().size());
    for (StepValue element : listValue.elements()) {
      StepValue unwrapped = unwrapTyped(element);
      if (!(unwrapped instanceof StepValue.StringValue)) {
        throw parameterElementTypeMismatch(instance, definition, index, "string", element);
      }
      StepValue.StringValue strValue = (StepValue.StringValue) unwrapped;
      result.add(strValue.value());
    }
    return List.copyOf(result);
  }

  static String logicalValue(StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (value instanceof StepValue.EnumValue) {
      StepValue.EnumValue enumValue = (StepValue.EnumValue) value;
      return enumValue.value();
    }
    if (value instanceof StepValue.StringValue) {
      StepValue.StringValue strValue = (StepValue.StringValue) value;
      return strValue.value();
    }
    throw StepParameterReader.parameterTypeMismatch(
        instance, definition, index, "LOGICAL value (.T., .F., or .U.)");
  }

  /**
   * Extracts a list of numbers from a pre-unwrapped StepValue.
   * Useful when the caller has already handled nested list unwrapping.
   */
  static List<Double> extractNumberList(StepEntityDefinition definition, StepValue value, String paramName) {
    if (!(value instanceof StepValue.ListValue)) {
      throw new StepResolutionException(paramName + " parameter must be a list");
    }
    StepValue.ListValue listValue = (StepValue.ListValue) value;
    List<Double> result = new ArrayList<>(listValue.elements().size());
    for (StepValue element : listValue.elements()) {
      StepValue unwrapped = unwrapTyped(element);
      if (!(unwrapped instanceof StepValue.NumberValue)) {
        throw new StepResolutionException(paramName + " numeric list must contain only numbers");
      }
      StepValue.NumberValue numberValue = (StepValue.NumberValue) unwrapped;
      result.add(numberValue.value());
    }
    return List.copyOf(result);
  }

  static List<String> literalList(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (!(value instanceof StepValue.ListValue)) {
      throw new StepResolutionException(
          definition.name() + " parameter " + index + " must be a list");
    }
    StepValue.ListValue listValue = (StepValue.ListValue) value;
    List<String> result = new ArrayList<>(listValue.elements().size());
    for (StepValue element : listValue.elements()) {
      result.add(literalText(element));
    }
    return List.copyOf(result);
  }

  static List<List<Double>> numberGrid(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (!(value instanceof StepValue.ListValue)) {
      throw StepParameterReader.parameterTypeMismatch(instance, definition, index, "nested list");
    }
    StepValue.ListValue outerList = (StepValue.ListValue) value;
    List<List<Double>> grid = new ArrayList<>(outerList.elements().size());
    for (StepValue rowValue : outerList.elements()) {
      StepValue row = unwrapTyped(rowValue);
      if (!(row instanceof StepValue.ListValue)) {
        throw parameterElementTypeMismatch(instance, definition, index, "nested numeric list", rowValue);
      }
      StepValue.ListValue rowList = (StepValue.ListValue) row;
      List<Double> entries = new ArrayList<>(rowList.elements().size());
      for (StepValue element : rowList.elements()) {
        StepValue unwrapped = unwrapTyped(element);
        if (!(unwrapped instanceof StepValue.NumberValue)) {
          throw parameterElementTypeMismatch(instance, definition, index, "number", element);
        }
        StepValue.NumberValue numberValue = (StepValue.NumberValue) unwrapped;
        entries.add(numberValue.value());
      }
      grid.add(List.copyOf(entries));
    }
    return List.copyOf(grid);
  }

  static List<Integer> integerList(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    List<Double> values = numberList(instance, definition, index);
    List<Integer> result = new ArrayList<>(values.size());
    for (double value : values) {
      if (value != Math.rint(value)) {
        throw new StepResolutionException(
            "entity #"
                + instance.id()
                + " "
                + definition.name()
                + " parameter "
                + index
                + " element type mismatch: expected integer, actual number");
      }
      result.add((int) value);
    }
    return List.copyOf(result);
  }

  static StepResolutionException parameterElementTypeMismatch(
      StepEntityInstance instance,
      StepEntityDefinition definition,
      int index,
      String expected,
      StepValue actualValue) {
    return new StepResolutionException(
        "entity #"
            + instance.id()
            + " "
            + definition.name()
            + " parameter "
            + index
            + " element type mismatch: expected "
            + expected
            + ", actual "
            + StepParameterReader.valueType(actualValue));
  }

  static boolean isUnset(StepValue value) {
    return StepParameterReader.isUnset(value);
  }

  // ---------------------------------------------------------------------------
  // SELECT type handling helpers (C10)
  // ---------------------------------------------------------------------------

  /** Wrapper for StepParameterReader.typedSelection with entity ID context. */
  static StepParameterReader.TypedSelection typedSelection(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    return StepParameterReader.typedSelection(instance, definition, index);
  }

  /** Wrapper for StepParameterReader.optionalTypedSelection with entity ID context. */
  static StepParameterReader.TypedSelection optionalTypedSelection(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    return StepParameterReader.optionalTypedSelection(instance, definition, index);
  }

  /** Wrapper for StepParameterReader.validateSelectTypeName. */
  static void validateSelectTypeName(
      StepEntityInstance instance,
      StepEntityDefinition definition,
      int index,
      StepParameterReader.TypedSelection selection,
      java.util.Set<String> allowedTypes) {
    StepParameterReader.validateSelectTypeName(instance, definition, index, selection, allowedTypes);
  }

  /** Wrapper for StepParameterReader.validateSelectTypeKnown. */
  static void validateSelectTypeKnown(
      StepEntityInstance instance,
      StepEntityDefinition definition,
      int index,
      StepParameterReader.TypedSelection selection) {
    StepParameterReader.validateSelectTypeKnown(instance, definition, index, selection);
  }

  static StepValue unwrapTyped(StepValue value) {
    StepValue current = value;
    while (current instanceof StepValue.TypedValue) {
      StepValue.TypedValue typedValue = (StepValue.TypedValue) current;
      current = typedValue.value();
    }
    return current;
  }

  static String literalText(StepValue value) {
    if (value instanceof StepValue.StringValue) {
      StepValue.StringValue stringValue = (StepValue.StringValue) value;
      return stringValue.value();
    }
    if (value instanceof StepValue.NumberValue) {
      StepValue.NumberValue numberValue = (StepValue.NumberValue) value;
      return numberValue.raw();
    }
    if (value instanceof StepValue.EnumValue) {
      StepValue.EnumValue enumValue = (StepValue.EnumValue) value;
      return "." + enumValue.value() + ".";
    }
    if (value instanceof StepValue.ReferenceValue) {
      StepValue.ReferenceValue referenceValue = (StepValue.ReferenceValue) value;
      return "#" + referenceValue.id();
    }
    if (value instanceof StepValue.OmittedValue) {
      return "$";
    }
    if (value instanceof StepValue.NotProvidedValue) {
      return "*";
    }
    if (value instanceof StepValue.TypedValue) {
      StepValue.TypedValue typedValue = (StepValue.TypedValue) value;
      return typedValue.typeName() + "(" + literalText(typedValue.value()) + ")";
    }
    throw new IllegalArgumentException();
  }

  static boolean isAnnotationOccurrence(StepEntity entity) {
    return entity instanceof StepAnnotationTextOccurrence
        || entity instanceof StepAnnotationPointOccurrence
        || entity instanceof StepAnnotationCurveOccurrence
        || entity instanceof StepLeaderCurve
        || entity instanceof StepProjectionCurve
        || entity instanceof StepDimensionCurve
        || entity instanceof StepAnnotationFillAreaOccurrence
        || entity instanceof StepAnnotationPlaceholderOccurrence
        || entity instanceof StepAnnotationPlane
        || entity instanceof StepAnnotationSymbolOccurrence
        || entity instanceof StepAnnotationSubfigureOccurrence
        || entity instanceof StepDraughtingAnnotationOccurrence
        || entity instanceof StepTerminatorSymbol;
  }

  static boolean isSupportedCurveReference(StepEntity entity) {
    return entity instanceof StepLine
        || entity instanceof StepLineSegment
        || entity instanceof StepCircle
        || entity instanceof StepEllipse
        || entity instanceof StepConicCurve
        || entity instanceof StepPolyline
        || entity instanceof StepOffsetCurve2D
        || entity instanceof StepOffsetCurve3D
        || entity instanceof StepOrientedCurve
        || entity instanceof StepCompositeCurve
        || entity instanceof StepCompositeCurveOnSurface
        || entity instanceof StepCompositeCurveOnSurface3D
        || entity instanceof StepCurve
        || entity instanceof StepBoundedCurve
        || entity instanceof StepBSplineCurve
        || entity instanceof StepBSplineCurveWithKnots
        || entity instanceof StepRationalBSplineCurve
        || entity instanceof StepBezierCurve
        || entity instanceof StepPiecewiseBezierCurve
        || entity instanceof StepUniformCurve
        || entity instanceof StepQuasiUniformCurve
        || entity instanceof StepTrimmedCurve
        || entity instanceof StepPcurve
        || entity instanceof StepDegeneratePcurve
        || entity instanceof StepSurfaceCurve
        || entity instanceof StepSeamCurve
        || entity instanceof StepAnnotationCurveOccurrence
        || entity instanceof StepLeaderCurve
        || entity instanceof StepProjectionCurve
        || entity instanceof StepDimensionCurve
        || entity instanceof StepDraughtingAnnotationOccurrence
        || entity instanceof StepTerminatorSymbol
        || entity instanceof StepClothoid
        || entity instanceof StepIndexedPolyCurve
        || entity instanceof StepDegenerateCurve
        || entity instanceof StepEdgeWire
        || entity instanceof StepSweptDiskSolid
        || entity instanceof StepCurve2D
        || entity instanceof StepMappedItem
        || (entity instanceof StepGeometricReplica
            && "CURVE_REPLICA".equals(((StepGeometricReplica) entity).entityName()));
  }

  static boolean isSupportedSurfaceReference(StepEntity entity) {
    return entity instanceof StepPlane
        || entity instanceof StepSurface
        || entity instanceof StepBoundedSurface
        || entity instanceof StepOffsetSurface
        || entity instanceof StepOffsetSurface2
        || entity instanceof StepBSplineSurface
        || entity instanceof StepBSplineSurfaceWithKnots
        || entity instanceof StepRationalBSplineSurface
        || entity instanceof StepBezierSurface
        || entity instanceof StepPiecewiseBezierSurface
        || entity instanceof StepUniformSurface
        || entity instanceof StepQuasiUniformSurface
        || entity instanceof StepCylindricalSurface
        || entity instanceof StepConicalSurface
        || entity instanceof StepToroidalSurface
        || entity instanceof StepDegenerateToroidalSurface
        || entity instanceof StepSphericalSurface
        || entity instanceof StepSurfaceOfLinearExtrusion
        || entity instanceof StepSurfaceOfRevolution
        || entity instanceof StepSurfaceOfConstantRadius
        || entity instanceof StepRectangularTrimmedSurface
        || entity instanceof StepRectangularCompositeSurface
        || entity instanceof StepSurfacePatch
        || entity instanceof StepCurveBoundedSurface
        || entity instanceof StepOrientedSurface
        || entity instanceof StepSubface
        || entity instanceof StepMappedItem
        || (entity instanceof StepGeometricReplica
            && "SURFACE_REPLICA".equals(((StepGeometricReplica) entity).entityName()));
  }

  static String deriveUnitKind(StepEntityInstance instance) {
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
        "MASS_DENSITY_UNIT",
        "DYNAMIC_VISCOSITY_UNIT",
        "KINEMATIC_VISCOSITY_UNIT",
        "MOMENT_OF_INERTIA_UNIT",
        "THERMAL_CONDUCTIVITY_UNIT",
        "HEAT_FLUX_DENSITY_UNIT",
        "SPECIFIC_HEAT_CAPACITY_UNIT",
        "AREA_DENSITY_UNIT",
        "VOLUMETRIC_FLOW_RATE_UNIT",
        "MASS_FLOW_RATE_UNIT",
        "ROTATIONAL_FREQUENCY_UNIT",
        "ANGULAR_VELOCITY_UNIT",
        "ANGULAR_ACCELERATION_UNIT",
        "TORQUE_UNIT",
        "LINEAR_FORCE_UNIT",
        "LINEAR_STIFFNESS_UNIT",
        "ROTATIONAL_STIFFNESS_UNIT",
        "LINEAR_MOMENT_UNIT",
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

  static boolean matchesUnitKind(StepEntity entity, String expectedUnitKind) {
    if (entity instanceof StepNamedUnit) {
            StepNamedUnit namedUnit = (StepNamedUnit) entity;
      return expectedUnitKind.equals(namedUnit.unitKind());
    }
    if (entity instanceof StepSiUnit) {
            StepSiUnit siUnit = (StepSiUnit) entity;
      return expectedUnitKind.equals(siUnit.unitKind());
    }
    if (entity instanceof StepConversionBasedUnit) {
            StepConversionBasedUnit conversionBasedUnit = (StepConversionBasedUnit) entity;
      return expectedUnitKind.equals(conversionBasedUnit.unitKind());
    }
    if (entity instanceof StepConversionBasedUnitWithOffset) {
            StepConversionBasedUnitWithOffset conversionBasedUnitWithOffset = (StepConversionBasedUnitWithOffset) entity;
      return expectedUnitKind.equals(conversionBasedUnitWithOffset.unitKind());
    }
    if (entity instanceof StepContextDependentUnit) {
            StepContextDependentUnit contextDependentUnit = (StepContextDependentUnit) entity;
      return expectedUnitKind.equals(contextDependentUnit.unitKind());
    }
    if (entity instanceof StepDerivedUnit) {
            StepDerivedUnit derivedUnit = (StepDerivedUnit) entity;
      return expectedUnitKind.equals(derivedUnit.unitKind());
    }
    return false;
  }

  static String inheritedRepresentationItemName(StepEntityInstance instance) {
    return instance.hasDefinition("REPRESENTATION_ITEM")
        ? stringValue(instance, definition(instance, "REPRESENTATION_ITEM"), 0)
        : "";
  }

  static String inheritedTopologicalRepresentationItemName(StepEntityInstance instance) {
    return instance.hasDefinition("TOPOLOGICAL_REPRESENTATION_ITEM")
        ? stringValue(instance, definition(instance, "TOPOLOGICAL_REPRESENTATION_ITEM"), 0)
        : inheritedRepresentationItemName(instance);
  }

  static int inheritedStyledItemTargetId(StepEntityInstance instance) {
    if (!instance.hasDefinition("STYLED_ITEM")) {
      throw new StepResolutionException("complex entity is missing STYLED_ITEM definition");
    }
    StepEntityDefinition definition = definition(instance, "STYLED_ITEM");
    requireParameterCount(instance, definition, 3);
    return referenceId(instance, definition, 2);
  }
}
