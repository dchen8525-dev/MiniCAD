package com.minicad.step.semantic;

import com.minicad.common.StepResolutionException;
import com.minicad.step.model.base.StepEntity;
import com.minicad.step.syntax.StepEntityDefinition;
import com.minicad.step.syntax.StepEntityInstance;
import com.minicad.step.syntax.StepValue;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Utility class for reading typed parameter values from STEP entity instances.
 *
 * Extracts parameter reading logic from {@link StepEntityResolver} so that
 * resolve methods can be shorter and more focused on entity assembly.
 */
public final class StepParameterReader {

  private StepParameterReader() {}

  // ---------------------------------------------------------------------------
  // Definition access
  // ---------------------------------------------------------------------------

  /**
   * Returns the named definition from the instance.
   */
  public static StepEntityDefinition definition(StepEntityInstance instance, String name) {
    return instance.requireDefinition(name);
  }

  /**
   * Validates that the definition has exactly {@code expected} parameters.
   */
  public static void requireParameterCount(
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

  /**
   * Validates that the definition has one of the given parameter counts.
   */
  public static void requireParameterCountIn(
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

  // ---------------------------------------------------------------------------
  // Core value helpers
  // ---------------------------------------------------------------------------

  /**
   * Returns true if the value is omitted ($) or not-provided (*).
   */
  public static boolean isUnset(StepValue value) {
    StepValue unwrapped = unwrapTyped(value);
    return unwrapped instanceof StepValue.OmittedValue
        || unwrapped instanceof StepValue.NotProvidedValue;
  }

  /**
   * Unwraps all {@link StepValue.TypedValue} layers to reach the leaf value.
   */
  public static StepValue unwrapTyped(StepValue value) {
    StepValue current = value;
    while (current instanceof StepValue.TypedValue typedValue) {
      current = typedValue.value();
    }
    return current;
  }

  /**
   * Converts a StepValue back to its STEP literal text representation.
   */
  public static String literalText(StepValue value) {
    return switch (value) {
      case StepValue.StringValue stringValue -> stringValue.value();
      case StepValue.NumberValue numberValue -> numberValue.raw();
      case StepValue.EnumValue enumValue -> "." + enumValue.value() + ".";
      case StepValue.ReferenceValue referenceValue -> "#" + referenceValue.id();
      case StepValue.OmittedValue ignored -> "$";
      case StepValue.NotProvidedValue ignored -> "*";
      case StepValue.ListValue listValue ->
          listValue.elements().stream()
              .map(StepParameterReader::literalText)
              .collect(java.util.stream.Collectors.joining(",", "(", ")"));
      case StepValue.TypedValue typedValue ->
          typedValue.typeName() + "(" + literalText(typedValue.value()) + ")";
    };
  }

  // ---------------------------------------------------------------------------
  // Scalar value readers
  // ---------------------------------------------------------------------------

  /**
   * Reads a required string parameter at the given index.
   *
   * @param entityName human-readable entity name for error messages
   */
  public static String stringValue(
      StepEntityDefinition definition, int index, String entityName) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (value instanceof StepValue.StringValue stringValue) {
      return stringValue.value();
    }
    throw new StepResolutionException(
        entityName + " parameter " + index + " must be a string");
  }

  /**
   * Reads an optional string parameter; returns empty string if omitted.
   */
  public static String optionalStringValue(
      StepEntityDefinition definition, int index, String entityName) {
    StepValue value = definition.parameters().get(index);
    if (isUnset(value)) {
      return "";
    }
    return stringValue(definition, index, entityName);
  }

  /**
   * Reads a required numeric (double) parameter at the given index.
   */
  public static double numberValue(
      StepEntityDefinition definition, int index, String entityName) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (value instanceof StepValue.NumberValue numberValue) {
      return numberValue.value();
    }
    throw new StepResolutionException(
        entityName + " parameter " + index + " must be a number");
  }

  /**
   * Reads an optional numeric parameter; returns null if omitted.
   */
  public static Double optionalNumberValue(
      StepEntityDefinition definition, int index, String entityName) {
    StepValue value = definition.parameters().get(index);
    if (isUnset(value)) {
      return null;
    }
    return numberValue(definition, index, entityName);
  }

  /**
   * Reads a required integer parameter. The underlying STEP value must be
   * a number with an integral value.
   */
  public static int integerValue(
      StepEntityDefinition definition, int index, String entityName) {
    double value = numberValue(definition, index, entityName);
    if (value != Math.rint(value)) {
      throw new StepResolutionException(
          entityName + " parameter " + index + " must be an integer");
    }
    return (int) value;
  }

  /**
   * Reads an optional integer parameter; returns null if omitted.
   */
  public static Integer optionalIntegerValue(
      StepEntityDefinition definition, int index, String entityName) {
    StepValue value = definition.parameters().get(index);
    if (isUnset(value)) {
      return null;
    }
    return integerValue(definition, index, entityName);
  }

  /**
   * Reads a required enum parameter. Returns the enum value without the
   * surrounding dots.
   */
  public static String enumValue(
      StepEntityDefinition definition, int index, String entityName) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (value instanceof StepValue.EnumValue enumValue) {
      return enumValue.value();
    }
    throw new StepResolutionException(
        entityName + " parameter " + index + " must be an enum");
  }

  /**
   * Reads a boolean parameter encoded as .T. or .F.
   */
  public static boolean booleanValue(
      StepEntityDefinition definition, int index, String entityName) {
    return switch (enumValue(definition, index, entityName)) {
      case "T" -> true;
      case "F" -> false;
      default ->
          throw new StepResolutionException(
              entityName + " parameter " + index + " must be .T. or .F.");
    };
  }

  /**
   * Reads a LOGICAL parameter (.T., .F., or .U.). Returns the raw value
   * string so the caller can decide how to handle UNKNOWN.
   */
  public static String logicalValue(
      StepEntityDefinition definition, int index, String entityName) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (value instanceof StepValue.EnumValue enumValue) {
      return enumValue.value();
    }
    if (value instanceof StepValue.StringValue strValue) {
      return strValue.value();
    }
    throw new StepResolutionException(
        entityName + " parameter " + index + " must be a LOGICAL value (.T., .F., or .U.)");
  }

  // ---------------------------------------------------------------------------
  // Reference readers
  // ---------------------------------------------------------------------------

  /**
   * Reads a required reference and returns the referenced entity id.
   */
  public static int referenceId(
      StepEntityDefinition definition, int index, String entityName) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (value instanceof StepValue.ReferenceValue referenceValue) {
      return referenceValue.id();
    }
    throw new StepResolutionException(
        entityName + " parameter " + index + " must be a reference");
  }

  /**
   * Resolves a reference value to a StepEntity using the provided resolver.
   * Returns null if the value is omitted or not-provided.
   *
   * @param resolver function that maps an entity id to a resolved StepEntity
   */
  public static StepEntity tryResolveReference(
      StepValue value, Function<Integer, StepEntity> resolver) {
    value = unwrapTyped(value);
    if (value instanceof StepValue.OmittedValue || value instanceof StepValue.NotProvidedValue) {
      return null;
    }
    if (value instanceof StepValue.ReferenceValue referenceValue) {
      return resolver.apply(referenceValue.id());
    }
    throw new StepResolutionException("parameter must be a reference or omit/not-provided");
  }

  /**
   * Resolves a required reference parameter to a StepEntity.
   */
  public static StepEntity resolveReference(
      StepEntityDefinition definition, int index, String entityName,
      Function<Integer, StepEntity> resolver) {
    int refId = referenceId(definition, index, entityName);
    return resolver.apply(refId);
  }

  /**
   * Resolves an optional reference parameter; returns null if omitted.
   */
  public static StepEntity optionalResolveReference(
      StepEntityDefinition definition, int index, String entityName,
      Function<Integer, StepEntity> resolver) {
    StepValue value = definition.parameters().get(index);
    if (isUnset(value)) {
      return null;
    }
    return resolveReference(definition, index, entityName, resolver);
  }

  /**
   * Reads a required reference and casts it to the expected type.
   */
  public static <T extends StepEntity> T requireEntity(
      StepEntityDefinition definition, int index, String entityName,
      Class<T> type, String message, Function<Integer, StepEntity> resolver) {
    int refId = referenceId(definition, index, entityName);
    StepEntity entity = resolver.apply(refId);
    if (!type.isInstance(entity)) {
      throw new StepResolutionException(message + " but got " + entity.getClass().getSimpleName());
    }
    return type.cast(entity);
  }

  /**
   * Reads an optional reference and casts it to the expected type;
   * returns null if omitted.
   */
  public static <T extends StepEntity> T optionalRequireEntity(
      StepEntityDefinition definition, int index, String entityName,
      Class<T> type, String message, Function<Integer, StepEntity> resolver) {
    StepValue value = definition.parameters().get(index);
    if (isUnset(value)) {
      return null;
    }
    int refId = referenceId(definition, index, entityName);
    StepEntity entity = resolver.apply(refId);
    if (!type.isInstance(entity)) {
      throw new StepResolutionException(message + " but got " + entity.getClass().getSimpleName());
    }
    return type.cast(entity);
  }

  /**
   * Reads a required reference that must be a vertex-like entity
   * (StepVertex or StepVertexPoint).
   */
  public static StepEntity requireVertexLike(
      StepEntityDefinition definition, int index, String entityName,
      String message, Function<Integer, StepEntity> resolver) {
    int refId = referenceId(definition, index, entityName);
    StepEntity entity = resolver.apply(refId);
    String entityClassName = entity.getClass().getSimpleName();
    // Check by simple name since we don't want to import specific model classes here
    if (!entityClassName.equals("StepVertex") && !entityClassName.equals("StepVertexPoint")) {
      throw new StepResolutionException(message + " but got " + entityClassName);
    }
    return entity;
  }

  // ---------------------------------------------------------------------------
  // List readers
  // ---------------------------------------------------------------------------

  /**
   * Reads a coordinate list with size validation (minSize to maxSize elements).
   */
  public static List<Double> coordinateList(
      StepEntityDefinition definition, int index,
      int minSize, int maxSize, String entityName) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (!(value instanceof StepValue.ListValue listValue)) {
      throw new StepResolutionException(
          entityName + " parameter " + index + " must be a list");
    }
    if (listValue.elements().size() < minSize || listValue.elements().size() > maxSize) {
      throw new StepResolutionException(
          entityName + " only supports " + minSize + "D to " + maxSize + "D coordinates");
    }
    List<Double> result = new ArrayList<>(listValue.elements().size());
    for (StepValue element : listValue.elements()) {
      StepValue unwrapped = unwrapTyped(element);
      if (unwrapped instanceof StepValue.NumberValue numberValue) {
        result.add(numberValue.value());
      } else {
        throw new StepResolutionException(
            entityName + " coordinate list must contain only numbers");
      }
    }
    return List.copyOf(result);
  }

  /**
   * Reads a coordinate triple (exactly 3 numeric values).
   */
  public static List<Double> coordinateTriple(
      StepEntityDefinition definition, int index, String entityName) {
    return coordinateList(definition, index, 3, 3, entityName);
  }

  /**
   * Reads a list of doubles.
   */
  public static List<Double> doubleList(
      StepEntityDefinition definition, int index, String entityName) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (!(value instanceof StepValue.ListValue listValue)) {
      throw new StepResolutionException(
          entityName + " parameter " + index + " must be a list");
    }
    List<Double> result = new ArrayList<>(listValue.elements().size());
    for (StepValue element : listValue.elements()) {
      StepValue unwrapped = unwrapTyped(element);
      if (!(unwrapped instanceof StepValue.NumberValue numberValue)) {
        throw new StepResolutionException(
            entityName + " numeric list must contain only numbers");
      }
      result.add(numberValue.value());
    }
    return List.copyOf(result);
  }

  /**
   * Reads a list of integers.
   */
  public static List<Integer> intList(
      StepEntityDefinition definition, int index, String entityName) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (!(value instanceof StepValue.ListValue listValue)) {
      throw new StepResolutionException(
          entityName + " parameter " + index + " must be a list");
    }
    List<Integer> result = new ArrayList<>(listValue.elements().size());
    for (StepValue element : listValue.elements()) {
      StepValue unwrapped = unwrapTyped(element);
      if (!(unwrapped instanceof StepValue.NumberValue numberValue)) {
        throw new StepResolutionException(
            entityName + " integer list must contain only numbers");
      }
      double dv = numberValue.value();
      if (dv != Math.rint(dv)) {
        throw new StepResolutionException(
            entityName + " integer list must contain only integers");
      }
      result.add((int) dv);
    }
    return List.copyOf(result);
  }

  /**
   * Reads a list of strings.
   */
  public static List<String> stringList(
      StepEntityDefinition definition, int index, String entityName) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (!(value instanceof StepValue.ListValue listValue)) {
      throw new StepResolutionException(
          entityName + " parameter " + index + " must be a list");
    }
    List<String> result = new ArrayList<>(listValue.elements().size());
    for (StepValue element : listValue.elements()) {
      StepValue unwrapped = unwrapTyped(element);
      if (!(unwrapped instanceof StepValue.StringValue strValue)) {
        throw new StepResolutionException(
            entityName + " string list must contain only strings");
      }
      result.add(strValue.value());
    }
    return List.copyOf(result);
  }

  /**
   * Reads an optional list of strings; returns empty list if omitted.
   */
  public static List<String> optionalStringListValue(
      StepEntityDefinition definition, int index, String entityName) {
    StepValue value = definition.parameters().get(index);
    if (isUnset(value)) {
      return List.of();
    }
    StepValue unwrapped = unwrapTyped(value);
    if (!(unwrapped instanceof StepValue.ListValue listValue)) {
      throw new StepResolutionException(
          entityName + " parameter " + index + " must be a string list");
    }
    List<String> result = new ArrayList<>(listValue.elements().size());
    for (StepValue element : listValue.elements()) {
      StepValue unwrappedElement = unwrapTyped(element);
      if (!(unwrappedElement instanceof StepValue.StringValue stringValue)) {
        throw new StepResolutionException(
            entityName + " string list must contain only strings");
      }
      result.add(stringValue.value());
    }
    return List.copyOf(result);
  }

  /**
   * Reads a list of numbers (alias for doubleList, preserves original name).
   */
  public static List<Double> numberList(
      StepEntityDefinition definition, int index, String entityName) {
    return doubleList(definition, index, entityName);
  }

  /**
   * Reads a list of integers, validating each is a whole number.
   */
  public static List<Integer> integerList(
      StepEntityDefinition definition, int index, String entityName) {
    List<Double> values = doubleList(definition, index, entityName);
    List<Integer> result = new ArrayList<>(values.size());
    for (double value : values) {
      if (value != Math.rint(value)) {
        throw new StepResolutionException(
            entityName + " integer list must contain only integers");
      }
      result.add((int) value);
    }
    return List.copyOf(result);
  }

  /**
   * Extracts a number list from a pre-unwrapped StepValue.
   * Useful when the caller has already handled nested list unwrapping.
   */
  public static List<Double> extractNumberList(StepValue value, String paramName) {
    if (!(value instanceof StepValue.ListValue listValue)) {
      throw new StepResolutionException(paramName + " parameter must be a list");
    }
    List<Double> result = new ArrayList<>(listValue.elements().size());
    for (StepValue element : listValue.elements()) {
      StepValue unwrapped = unwrapTyped(element);
      if (!(unwrapped instanceof StepValue.NumberValue numberValue)) {
        throw new StepResolutionException(paramName + " numeric list must contain only numbers");
      }
      result.add(numberValue.value());
    }
    return List.copyOf(result);
  }

  /**
   * Reads a list of parameter values as raw StepValue elements (for trim curves etc.).
   */
  public static List<StepValue> listElements(
      StepEntityDefinition definition, int index, String entityName) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (!(value instanceof StepValue.ListValue listValue)) {
      throw new StepResolutionException(
          entityName + " parameter " + index + " must be a list");
    }
    return List.copyOf(listValue.elements());
  }

  /**
   * Reads a list of literals, converting each element to its STEP text form.
   */
  public static List<String> literalList(
      StepEntityDefinition definition, int index, String entityName) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (!(value instanceof StepValue.ListValue listValue)) {
      throw new StepResolutionException(
          entityName + " parameter " + index + " must be a list");
    }
    List<String> result = new ArrayList<>(listValue.elements().size());
    for (StepValue element : listValue.elements()) {
      result.add(literalText(element));
    }
    return List.copyOf(result);
  }

  /**
   * Reads a nested list of doubles (grid/matrix).
   */
  public static List<List<Double>> numberGrid(
      StepEntityDefinition definition, int index, String entityName) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (!(value instanceof StepValue.ListValue outerList)) {
      throw new StepResolutionException(
          entityName + " parameter " + index + " must be a nested list");
    }
    List<List<Double>> grid = new ArrayList<>(outerList.elements().size());
    for (StepValue rowValue : outerList.elements()) {
      StepValue row = unwrapTyped(rowValue);
      if (!(row instanceof StepValue.ListValue rowList)) {
        throw new StepResolutionException(
            entityName + " numeric grid must contain only nested numeric lists");
      }
      List<Double> entries = new ArrayList<>(rowList.elements().size());
      for (StepValue element : rowList.elements()) {
        StepValue unwrapped = unwrapTyped(element);
        if (!(unwrapped instanceof StepValue.NumberValue numberValue)) {
          throw new StepResolutionException(
              entityName + " numeric grid must contain only numbers");
        }
        entries.add(numberValue.value());
      }
      grid.add(List.copyOf(entries));
    }
    return List.copyOf(grid);
  }

  // ---------------------------------------------------------------------------
  // Entity reference list readers
  // ---------------------------------------------------------------------------

  /**
   * Reads a list of references and resolves them to StepEntities.
   */
  public static List<StepEntity> entityReferenceList(
      StepEntityDefinition definition, int index, String message,
      Function<Integer, StepEntity> resolver) {
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
      result.add(resolver.apply(referenceValue.id()));
    }
    return List.copyOf(result);
  }

  /**
   * Reads a nested list of references, resolving each to the expected type.
   */
  public static <T extends StepEntity> List<List<T>> referenceGrid(
      StepEntityDefinition definition, int index,
      Class<T> type, String message, Function<Integer, StepEntity> resolver) {
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
        StepEntity entity = resolver.apply(referenceValue.id());
        if (!type.isInstance(entity)) {
          throw new StepResolutionException(
              message + " but got " + entity.getClass().getSimpleName());
        }
        entries.add(type.cast(entity));
      }
      grid.add(List.copyOf(entries));
    }
    return List.copyOf(grid);
  }

  /**
   * Reads a nested list of references and resolves them without type checking.
   */
  public static List<List<StepEntity>> entityReferenceGrid(
      StepEntityDefinition definition, int index, String message,
      Function<Integer, StepEntity> resolver) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (!(value instanceof StepValue.ListValue outerList)) {
      throw new StepResolutionException(
          definition.name() + " parameter " + index + " must be a nested list");
    }
    List<List<StepEntity>> result = new ArrayList<>();
    for (StepValue outerElement : outerList.elements()) {
      StepValue unwrappedOuter = unwrapTyped(outerElement);
      if (!(unwrappedOuter instanceof StepValue.ListValue innerList)) {
        throw new StepResolutionException(
            definition.name() + " parameter " + index + " must contain nested lists");
      }
      List<StepEntity> row = new ArrayList<>();
      for (StepValue innerElement : innerList.elements()) {
        StepValue unwrappedInner = unwrapTyped(innerElement);
        if (!(unwrappedInner instanceof StepValue.ReferenceValue referenceValue)) {
          throw new StepResolutionException(
              definition.name() + " parameter " + index + " inner elements must be references");
        }
        row.add(resolver.apply(referenceValue.id()));
      }
      result.add(List.copyOf(row));
    }
    return List.copyOf(result);
  }
}
