package com.minicad.step.semantic;

import com.minicad.common.StepResolutionException;
import com.minicad.step.model.core.base.StepEntity;
import com.minicad.step.syntax.StepEntityDefinition;
import com.minicad.step.syntax.StepEntityInstance;
import com.minicad.step.syntax.StepValue;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.Objects;

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
          "entity #"
              + instance.id()
              + " "
              + definition.name()
              + " parameter count mismatch: expected "
              + expected
              + ", actual "
              + definition.parameters().size());
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
        "entity #"
            + instance.id()
            + " "
            + definition.name()
            + " parameter count mismatch: expected "
            + expectedText
            + ", actual "
            + actual
        );
  }

  /**
   * Creates a consistent diagnostic for a parameter whose STEP value has the wrong type.
   */
  public static StepResolutionException parameterTypeMismatch(
      StepEntityInstance instance, StepEntityDefinition definition, int index, String expected) {
    return new StepResolutionException(
        "entity #"
            + instance.id()
            + " "
            + definition.name()
            + " parameter "
            + index
            + " type mismatch: expected "
            + expected
            + ", actual "
            + valueType(definition.parameters().get(index)));
  }

  private static StepResolutionException parameterTypeMismatch(
      StepEntityDefinition definition, int index, String entityName, String expected) {
    return new StepResolutionException(
        entityName
            + " parameter "
            + index
            + " type mismatch: expected "
            + expected
            + ", actual "
            + valueType(definition.parameters().get(index)));
  }

  private static StepResolutionException parameterElementTypeMismatch(
      String entityName, int index, String elementRole, String expected, StepValue actual) {
    return new StepResolutionException(
        entityName
            + " parameter "
            + index
            + " "
            + elementRole
            + " type mismatch: expected "
            + expected
            + ", actual "
            + valueType(actual));
  }

  public static String valueType(StepValue value) {
    StepValue unwrapped = unwrapTyped(value);
    if (unwrapped instanceof StepValue.StringValue) return "string";
    if (unwrapped instanceof StepValue.NumberValue) return "number";
    if (unwrapped instanceof StepValue.EnumValue) return "enum";
    if (unwrapped instanceof StepValue.ReferenceValue) return "reference";
    if (unwrapped instanceof StepValue.OmittedValue) return "omitted";
    if (unwrapped instanceof StepValue.NotProvidedValue) return "not-provided";
    if (unwrapped instanceof StepValue.ListValue) return "list";
    if (unwrapped instanceof StepValue.TypedValue) {
      StepValue.TypedValue typedValue = (StepValue.TypedValue) unwrapped;
      return "typed " + typedValue.typeName();
    }
    throw new IllegalArgumentException("Unknown value type: " + unwrapped);
  }

  // ---------------------------------------------------------------------------
  // Core value helpers
  // ---------------------------------------------------------------------------

  /**
   * Returns true if the value is omitted ($) or not-provided (*).
   */
  public static boolean isUnset(StepValue value) {
    return isOmitted(value) || isNotProvided(value);
  }

  /**
   * Returns true if the value is explicitly omitted ($).
   */
  public static boolean isOmitted(StepValue value) {
    return unwrapTyped(value) instanceof StepValue.OmittedValue;
  }

  /**
   * Returns true if the value is explicitly not provided (*).
   */
  public static boolean isNotProvided(StepValue value) {
    return unwrapTyped(value) instanceof StepValue.NotProvidedValue;
  }

  /**
   * Unwraps all {@link StepValue.TypedValue} layers to reach the leaf value.
   */
  public static StepValue unwrapTyped(StepValue value) {
    StepValue current = value;
    while (current instanceof StepValue.TypedValue) {
      StepValue.TypedValue typedValue = (StepValue.TypedValue) current;
      current = typedValue.value();
    }
    return current;
  }

  /**
   * Reads a typed SELECT-style parameter while preserving the outer wrapper name.
   * Enhanced version with entity ID context in error messages.
   */
  public static TypedSelection typedSelection(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = definition.parameters().get(index);
    if (value instanceof StepValue.TypedValue) {
      StepValue.TypedValue typedValue = (StepValue.TypedValue) value;
      return new TypedSelection(typedValue.typeName(), unwrapTyped(typedValue.value()));
    }
    throw new StepResolutionException(
        "entity #" + instance.id() + " " + definition.name() +
        " parameter " + index + " must be a typed SELECT value, actual: " + valueType(value));
  }

  /**
   * Reads a typed SELECT-style parameter while preserving the outer wrapper name.
   * Legacy version without entity ID (deprecated - use version with instance parameter).
   */
  public static TypedSelection typedSelection(
      StepEntityDefinition definition, int index, String entityName) {
    StepValue value = definition.parameters().get(index);
    if (value instanceof StepValue.TypedValue) {
      StepValue.TypedValue typedValue = (StepValue.TypedValue) value;
      return new TypedSelection(typedValue.typeName(), unwrapTyped(typedValue.value()));
    }
    throw new StepResolutionException(
        entityName + " parameter " + index + " must be a typed SELECT value");
  }

  /**
   * Reads an optional typed SELECT-style parameter with entity ID context.
   */
  public static TypedSelection optionalTypedSelection(
      StepEntityInstance instance, StepEntityDefinition definition, int index) {
    StepValue value = definition.parameters().get(index);
    if (isUnset(value)) {
      return null;
    }
    return typedSelection(instance, definition, index);
  }

  /**
   * Reads an optional typed SELECT-style parameter (legacy version).
   */
  public static TypedSelection optionalTypedSelection(
      StepEntityDefinition definition, int index, String entityName) {
    StepValue value = definition.parameters().get(index);
    if (isUnset(value)) {
      return null;
    }
    return typedSelection(definition, index, entityName);
  }

  /**
   * Validates that the SELECT type name matches one of the allowed types.
   *
   * @param instance the entity instance (for error context)
   * @param definition the entity definition (for error context)
   * @param index the parameter index (for error context)
   * @param selection the typed selection to validate
   * @param allowedTypes the set of allowed SELECT type names
   * @throws StepResolutionException if the type name is not in the allowed set
   */
  public static void validateSelectTypeName(
      StepEntityInstance instance,
      StepEntityDefinition definition,
      int index,
      TypedSelection selection,
      java.util.Set<String> allowedTypes) {
    if (!allowedTypes.contains(selection.typeName())) {
      throw new StepResolutionException(
          "entity #" + instance.id() + " " + definition.name() +
          " parameter " + index + " SELECT type name must be one of " + allowedTypes +
          ", actual: " + selection.typeName());
    }
  }

  /**
   * Validates that the SELECT type name is a known AP242 SELECT type.
   *
   * @param instance the entity instance (for error context)
   * @param definition the entity definition (for error context)
   * @param index the parameter index (for error context)
   * @param selection the typed selection to validate
   * @throws StepResolutionException if the type name is not a known SELECT type
   */
  public static void validateSelectTypeKnown(
      StepEntityInstance instance,
      StepEntityDefinition definition,
      int index,
      TypedSelection selection) {
    if (!SelectTypeRegistry.isValidSelectType(selection.typeName())) {
      throw new StepResolutionException(
          "entity #" + instance.id() + " " + definition.name() +
          " parameter " + index + " SELECT type name '" + selection.typeName() +
          "' is not a known AP242 SELECT type");
    }
  }

  /**
   * SELECT wrapper name plus the unwrapped payload value.
   *
   * @param typeName wrapper type name
   * @param value unwrapped payload
   */
  public static final class TypedSelection {
    private final String typeName;
    private final StepValue value;

    public TypedSelection(String typeName, StepValue value) {
      this.typeName = typeName;
      this.value = value;
    }

    public String getTypeName() {
      return typeName;
    }

    public StepValue getValue() {
      return value;
    }

    // Record-style accessors
    public String typeName() { return getTypeName(); }
    public StepValue value() { return getValue(); }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      TypedSelection that = (TypedSelection) o;
      return Objects.equals(typeName, that.typeName) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
      return Objects.hash(typeName, value);
    }

    @Override
    public String toString() {
      return "TypedSelection{typeName=" + typeName + ", value=" + value + "}";
    }
  }

  /**
   * Converts a StepValue back to its STEP literal text representation.
   */
  public static String literalText(StepValue value) {
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
    if (value instanceof StepValue.OmittedValue) return "$";
    if (value instanceof StepValue.NotProvidedValue) return "*";
    if (value instanceof StepValue.TypedValue) {
      StepValue.TypedValue typedValue = (StepValue.TypedValue) value;
      return typedValue.typeName() + "(" + literalText(typedValue.value()) + ")";
    }
    if (value instanceof StepValue.ListValue) {
      StepValue.ListValue listValue = (StepValue.ListValue) value;
      return "(" + listValue.elements().stream()
          .map(StepParameterReader::literalText)
          .collect(java.util.stream.Collectors.joining(",")) + ")";
    }
    throw new IllegalArgumentException();
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
    if (value instanceof StepValue.StringValue) {
      StepValue.StringValue stringValue = (StepValue.StringValue) value;
      return stringValue.value();
    }
    throw parameterTypeMismatch(definition, index, entityName, "string");
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
    if (value instanceof StepValue.NumberValue) {
      StepValue.NumberValue numberValue = (StepValue.NumberValue) value;
      return numberValue.value();
    }
    throw parameterTypeMismatch(definition, index, entityName, "number");
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
          entityName
              + " parameter "
              + index
              + " type mismatch: expected integer, actual non-integral number");
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
    if (value instanceof StepValue.EnumValue) {
      StepValue.EnumValue enumValue = (StepValue.EnumValue) value;
      return enumValue.value();
    }
    throw parameterTypeMismatch(definition, index, entityName, "enum");
  }

  /**
   * Reads a boolean parameter encoded as .T. or .F.
   */
  public static boolean booleanValue(
      StepEntityDefinition definition, int index, String entityName) {
    String enumVal = enumValue(definition, index, entityName);
    switch (enumVal) {
      case "T": return true;
      case "F": return false;
      default:
          throw new StepResolutionException(
              entityName
                  + " parameter "
                  + index
                  + " type mismatch: expected boolean enum .T. or .F., actual enum ."
                  + enumVal
                  + ".");
    }
  }

  /**
   * Reads a LOGICAL parameter (.T., .F., or .U.). Returns the raw value
   * string so the caller can decide how to handle UNKNOWN.
   */
  public static String logicalValue(
      StepEntityDefinition definition, int index, String entityName) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (value instanceof StepValue.EnumValue) {
      StepValue.EnumValue enumValue = (StepValue.EnumValue) value;
      return enumValue.value();
    }
    if (value instanceof StepValue.StringValue) {
      StepValue.StringValue strValue = (StepValue.StringValue) value;
      return strValue.value();
    }
    throw parameterTypeMismatch(definition, index, entityName, "LOGICAL value (.T., .F., or .U.)");
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
    if (value instanceof StepValue.ReferenceValue) {
      StepValue.ReferenceValue referenceValue = (StepValue.ReferenceValue) value;
      return referenceValue.id();
    }
    throw parameterTypeMismatch(definition, index, entityName, "reference");
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
    if (value instanceof StepValue.ReferenceValue) {
      StepValue.ReferenceValue referenceValue = (StepValue.ReferenceValue) value;
      return resolver.apply(referenceValue.id());
    }
    throw new StepResolutionException(
        "parameter type mismatch: expected reference, omitted, or not-provided, actual "
            + valueType(value));
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
    if (!(value instanceof StepValue.ListValue)) {
      throw parameterTypeMismatch(definition, index, entityName, "list");
    }
    StepValue.ListValue listValue = (StepValue.ListValue) value;
    if (listValue.elements().size() < minSize || listValue.elements().size() > maxSize) {
      throw new StepResolutionException(
          entityName + " only supports " + minSize + "D to " + maxSize + "D coordinates");
    }
    List<Double> result = new ArrayList<>(listValue.elements().size());
    for (StepValue element : listValue.elements()) {
      StepValue unwrapped = unwrapTyped(element);
      if (unwrapped instanceof StepValue.NumberValue) {
        StepValue.NumberValue numberValue = (StepValue.NumberValue) unwrapped;
        result.add(numberValue.value());
      } else {
        throw parameterElementTypeMismatch(entityName, index, "coordinate element", "number", element);
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
    if (!(value instanceof StepValue.ListValue)) {
      throw parameterTypeMismatch(definition, index, entityName, "list");
    }
    StepValue.ListValue listValue = (StepValue.ListValue) value;
    List<Double> result = new ArrayList<>(listValue.elements().size());
    for (StepValue element : listValue.elements()) {
      StepValue unwrapped = unwrapTyped(element);
      if (!(unwrapped instanceof StepValue.NumberValue)) {
        throw parameterElementTypeMismatch(entityName, index, "numeric list element", "number", element);
      }
      StepValue.NumberValue numberValue = (StepValue.NumberValue) unwrapped;
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
    if (!(value instanceof StepValue.ListValue)) {
      throw parameterTypeMismatch(definition, index, entityName, "list");
    }
    StepValue.ListValue listValue = (StepValue.ListValue) value;
    List<Integer> result = new ArrayList<>(listValue.elements().size());
    for (StepValue element : listValue.elements()) {
      StepValue unwrapped = unwrapTyped(element);
      if (!(unwrapped instanceof StepValue.NumberValue)) {
        throw parameterElementTypeMismatch(entityName, index, "integer list element", "number", element);
      }
      StepValue.NumberValue numberValue = (StepValue.NumberValue) unwrapped;
      double dv = numberValue.value();
      if (dv != Math.rint(dv)) {
        throw new StepResolutionException(
            entityName
                + " parameter "
                + index
                + " integer list element type mismatch: expected integer, actual non-integral number");
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
    if (!(value instanceof StepValue.ListValue)) {
      throw parameterTypeMismatch(definition, index, entityName, "list");
    }
    StepValue.ListValue listValue = (StepValue.ListValue) value;
    List<String> result = new ArrayList<>(listValue.elements().size());
    for (StepValue element : listValue.elements()) {
      StepValue unwrapped = unwrapTyped(element);
      if (!(unwrapped instanceof StepValue.StringValue)) {
        throw parameterElementTypeMismatch(entityName, index, "string list element", "string", element);
      }
      StepValue.StringValue strValue = (StepValue.StringValue) unwrapped;
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
    if (!(unwrapped instanceof StepValue.ListValue)) {
      throw parameterTypeMismatch(definition, index, entityName, "string list");
    }
    StepValue.ListValue listValue = (StepValue.ListValue) unwrapped;
    List<String> result = new ArrayList<>(listValue.elements().size());
    for (StepValue element : listValue.elements()) {
      StepValue unwrappedElement = unwrapTyped(element);
      if (!(unwrappedElement instanceof StepValue.StringValue)) {
        throw parameterElementTypeMismatch(entityName, index, "string list element", "string", element);
      }
      StepValue.StringValue stringValue = (StepValue.StringValue) unwrappedElement;
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
            entityName
                + " integer list element type mismatch: expected integer, actual non-integral number");
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
    if (!(value instanceof StepValue.ListValue)) {
      throw new StepResolutionException(
          paramName + " parameter type mismatch: expected list, actual " + valueType(value));
    }
    StepValue.ListValue listValue = (StepValue.ListValue) value;
    List<Double> result = new ArrayList<>(listValue.elements().size());
    for (StepValue element : listValue.elements()) {
      StepValue unwrapped = unwrapTyped(element);
      if (!(unwrapped instanceof StepValue.NumberValue)) {
        throw new StepResolutionException(
            paramName
                + " numeric list element type mismatch: expected number, actual "
                + valueType(element));
      }
      StepValue.NumberValue numberValue = (StepValue.NumberValue) unwrapped;
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
    if (!(value instanceof StepValue.ListValue)) {
      throw parameterTypeMismatch(definition, index, entityName, "list");
    }
    StepValue.ListValue listValue = (StepValue.ListValue) value;
    return List.copyOf(listValue.elements());
  }

  /**
   * Reads a list of literals, converting each element to its STEP text form.
   */
  public static List<String> literalList(
      StepEntityDefinition definition, int index, String entityName) {
    StepValue value = unwrapTyped(definition.parameters().get(index));
    if (!(value instanceof StepValue.ListValue)) {
      throw parameterTypeMismatch(definition, index, entityName, "list");
    }
    StepValue.ListValue listValue = (StepValue.ListValue) value;
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
    if (!(value instanceof StepValue.ListValue)) {
      throw parameterTypeMismatch(definition, index, entityName, "nested list");
    }
    StepValue.ListValue outerList = (StepValue.ListValue) value;
    List<List<Double>> grid = new ArrayList<>(outerList.elements().size());
    for (StepValue rowValue : outerList.elements()) {
      StepValue row = unwrapTyped(rowValue);
      if (!(row instanceof StepValue.ListValue)) {
        throw parameterElementTypeMismatch(entityName, index, "grid row", "list", rowValue);
      }
      StepValue.ListValue rowList = (StepValue.ListValue) row;
      List<Double> entries = new ArrayList<>(rowList.elements().size());
      for (StepValue element : rowList.elements()) {
        StepValue unwrapped = unwrapTyped(element);
        if (!(unwrapped instanceof StepValue.NumberValue)) {
          throw parameterElementTypeMismatch(entityName, index, "grid element", "number", element);
        }
        StepValue.NumberValue numberValue = (StepValue.NumberValue) unwrapped;
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
    if (!(value instanceof StepValue.ListValue)) {
      throw new StepResolutionException(
          definition.name() + " parameter " + index + " must be a list");
    }
    StepValue.ListValue listValue = (StepValue.ListValue) value;
    List<StepEntity> result = new ArrayList<>();
    for (StepValue element : listValue.elements()) {
      StepValue unwrapped = unwrapTyped(element);
      if (!(unwrapped instanceof StepValue.ReferenceValue)) {
        throw new StepResolutionException(message);
      }
      StepValue.ReferenceValue referenceValue = (StepValue.ReferenceValue) unwrapped;
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
    if (!(value instanceof StepValue.ListValue)) {
      throw new StepResolutionException(
          definition.name() + " parameter " + index + " must be a nested list");
    }
    StepValue.ListValue outerList = (StepValue.ListValue) value;
    List<List<T>> grid = new ArrayList<>(outerList.elements().size());
    for (StepValue rowValue : outerList.elements()) {
      StepValue row = unwrapTyped(rowValue);
      if (!(row instanceof StepValue.ListValue)) {
        throw new StepResolutionException(message);
      }
      StepValue.ListValue rowList = (StepValue.ListValue) row;
      List<T> entries = new ArrayList<>(rowList.elements().size());
      for (StepValue element : rowList.elements()) {
        StepValue unwrapped = unwrapTyped(element);
        if (!(unwrapped instanceof StepValue.ReferenceValue)) {
          throw new StepResolutionException(message);
        }
        StepValue.ReferenceValue referenceValue = (StepValue.ReferenceValue) unwrapped;
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
    if (!(value instanceof StepValue.ListValue)) {
      throw new StepResolutionException(
          definition.name() + " parameter " + index + " must be a nested list");
    }
    StepValue.ListValue outerList = (StepValue.ListValue) value;
    List<List<StepEntity>> result = new ArrayList<>();
    for (StepValue outerElement : outerList.elements()) {
      StepValue unwrappedOuter = unwrapTyped(outerElement);
      if (!(unwrappedOuter instanceof StepValue.ListValue)) {
        throw new StepResolutionException(
            definition.name() + " parameter " + index + " must contain nested lists");
      }
      StepValue.ListValue innerList = (StepValue.ListValue) unwrappedOuter;
      List<StepEntity> row = new ArrayList<>();
      for (StepValue innerElement : innerList.elements()) {
        StepValue unwrappedInner = unwrapTyped(innerElement);
        if (!(unwrappedInner instanceof StepValue.ReferenceValue)) {
          throw new StepResolutionException(
              definition.name() + " parameter " + index + " inner elements must be references");
        }
        StepValue.ReferenceValue referenceValue = (StepValue.ReferenceValue) unwrappedInner;
        row.add(resolver.apply(referenceValue.id()));
      }
      result.add(List.copyOf(row));
    }
    return List.copyOf(result);
  }
}
