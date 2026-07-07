package com.minicad.step.semantic;

import com.minicad.common.StepResolutionException;
import com.minicad.step.model.base.StepEntity;
import com.minicad.step.model.geometry.StepCartesianPoint;
import com.minicad.step.model.geometry.StepDirection;
import com.minicad.step.syntax.StepEntityDefinition;
import com.minicad.step.syntax.StepEntityInstance;
import com.minicad.step.syntax.StepValue;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class StepParameterReaderTest {

  // ---------------------------------------------------------------------------
  // Helpers to build test instances quickly
  // ---------------------------------------------------------------------------

  private static StepEntityInstance instance(String name, List<StepValue> params) {
    return new StepEntityInstance(1, name, params);
  }

  private static StepEntityDefinition def(StepEntityInstance inst, String name) {
    return inst.requireDefinition(name);
  }

  private static StepEntityInstance instanceWithDef(String defName, List<StepValue> params) {
    return new StepEntityInstance(1, List.of(new StepEntityDefinition(defName, params)));
  }

  // ---------------------------------------------------------------------------
  // unwrapTyped / isUnset
  // ---------------------------------------------------------------------------

  @Test
  void unwrapTypedPassesThroughPlainValue() {
    StepValue plain = new StepValue.NumberValue(1.5, "1.5");
    assertEquals(plain, StepParameterReader.unwrapTyped(plain));
  }

  @Test
  void unwrapTypedUnwrapsTypedValue() {
    StepValue wrapped = new StepValue.TypedValue("LENGTH_MEASURE",
        new StepValue.NumberValue(1.5, "1.5"));
    StepValue result = StepParameterReader.unwrapTyped(wrapped);
    assertTrue(result instanceof StepValue.NumberValue);
    assertEquals(1.5, ((StepValue.NumberValue) result).value());
  }

  @Test
  void unwrapTypedUnwrapsMultipleLayers() {
    StepValue doubleWrapped = new StepValue.TypedValue("OUTER",
        new StepValue.TypedValue("INNER",
            new StepValue.NumberValue(42.0, "42")));
    StepValue result = StepParameterReader.unwrapTyped(doubleWrapped);
    assertTrue(result instanceof StepValue.NumberValue);
  }

  @Test
  void typedSelectionPreservesOuterTypeAndUnwrapsPayload() {
    var inst = instanceWithDef("TEST", List.of(
        new StepValue.TypedValue("AP242_SELECT",
            new StepValue.TypedValue("INNER", new StepValue.ReferenceValue(7)))));
    var def = def(inst, "TEST");

    StepParameterReader.TypedSelection selection =
        StepParameterReader.typedSelection(def, 0, "TEST");

    assertEquals("AP242_SELECT", selection.typeName());
    assertInstanceOf(StepValue.ReferenceValue.class, selection.value());
  }

  @Test
  void typedSelectionRejectsPlainPayload() {
    var inst = instanceWithDef("TEST", List.of(new StepValue.ReferenceValue(7)));
    var def = def(inst, "TEST");

    assertThrows(StepResolutionException.class,
        () -> StepParameterReader.typedSelection(def, 0, "TEST"));
  }

  @Test
  void isUnsetOmittedValue() {
    assertTrue(StepParameterReader.isUnset(new StepValue.OmittedValue()));
  }

  @Test
  void isUnsetNotProvidedValue() {
    assertTrue(StepParameterReader.isUnset(new StepValue.NotProvidedValue()));
  }

  @Test
  void isUnsetRegularValue() {
    assertFalse(StepParameterReader.isUnset(new StepValue.NumberValue(1.0, "1")));
  }

  @Test
  void isUnsetTypedOmittedValue() {
    StepValue typedOmitted = new StepValue.TypedValue("X", new StepValue.OmittedValue());
    assertTrue(StepParameterReader.isUnset(typedOmitted));
  }

  @Test
  void isOmittedDistinguishesDollarFromStar() {
    StepValue omitted = new StepValue.OmittedValue();
    StepValue notProvided = new StepValue.NotProvidedValue();

    assertTrue(StepParameterReader.isOmitted(omitted));
    assertFalse(StepParameterReader.isOmitted(notProvided));
  }

  @Test
  void isNotProvidedDistinguishesStarFromDollar() {
    StepValue omitted = new StepValue.OmittedValue();
    StepValue notProvided = new StepValue.NotProvidedValue();

    assertTrue(StepParameterReader.isNotProvided(notProvided));
    assertFalse(StepParameterReader.isNotProvided(omitted));
  }

  @Test
  void unsetPredicatesUnwrapTypedValues() {
    StepValue typedOmitted = new StepValue.TypedValue("WRAP", new StepValue.OmittedValue());
    StepValue typedNotProvided = new StepValue.TypedValue("WRAP", new StepValue.NotProvidedValue());

    assertTrue(StepParameterReader.isOmitted(typedOmitted));
    assertTrue(StepParameterReader.isNotProvided(typedNotProvided));
  }

  // ---------------------------------------------------------------------------
  // literalText
  // ---------------------------------------------------------------------------

  @Test
  void literalTextString() {
    assertEquals("hello", StepParameterReader.literalText(new StepValue.StringValue("hello")));
  }

  @Test
  void literalTextNumber() {
    assertEquals("3.14", StepParameterReader.literalText(new StepValue.NumberValue(3.14, "3.14")));
  }

  @Test
  void literalTextEnum() {
    assertEquals(".FOO.", StepParameterReader.literalText(new StepValue.EnumValue("FOO")));
  }

  @Test
  void literalTextReference() {
    assertEquals("#42", StepParameterReader.literalText(new StepValue.ReferenceValue(42)));
  }

  @Test
  void literalTextOmitted() {
    assertEquals("$", StepParameterReader.literalText(new StepValue.OmittedValue()));
  }

  @Test
  void literalTextNotProvided() {
    assertEquals("*", StepParameterReader.literalText(new StepValue.NotProvidedValue()));
  }

  @Test
  void literalTextList() {
    List<StepValue> elements = List.of(
        new StepValue.NumberValue(1.0, "1"),
        new StepValue.NumberValue(2.0, "2"));
    assertEquals("(1,2)", StepParameterReader.literalText(new StepValue.ListValue(elements)));
  }

  @Test
  void literalTextTypedValue() {
    StepValue typed = new StepValue.TypedValue("FOO", new StepValue.EnumValue("BAR"));
    assertEquals("FOO(.BAR.)", StepParameterReader.literalText(typed));
  }

  // ---------------------------------------------------------------------------
  // Scalar readers
  // ---------------------------------------------------------------------------

  @Test
  void stringValueReadsPlainString() {
    var inst = instanceWithDef("TEST_ENTITY", List.of(new StepValue.StringValue("hello")));
    var def = def(inst, "TEST_ENTITY");
    assertEquals("hello", StepParameterReader.stringValue(def, 0, "TEST_ENTITY"));
  }

  @Test
  void stringValueReadsTypedString() {
    var inst = instanceWithDef("TEST_ENTITY", List.of(
        new StepValue.TypedValue("LABEL", new StepValue.StringValue("world"))));
    var def = def(inst, "TEST_ENTITY");
    assertEquals("world", StepParameterReader.stringValue(def, 0, "TEST_ENTITY"));
  }

  @Test
  void stringValueThrowsOnNonString() {
    var inst = instanceWithDef("TEST_ENTITY", List.of(new StepValue.NumberValue(1.0, "1")));
    var def = def(inst, "TEST_ENTITY");
    StepResolutionException exception = assertThrows(StepResolutionException.class,
        () -> StepParameterReader.stringValue(def, 0, "TEST_ENTITY"));

    assertEquals(
        "TEST_ENTITY parameter 0 type mismatch: expected string, actual number",
        exception.getMessage());
  }

  @Test
  void optionalStringValueReturnsEmptyWhenOmitted() {
    var inst = instanceWithDef("TEST", List.of(new StepValue.OmittedValue()));
    var def = def(inst, "TEST");
    assertEquals("", StepParameterReader.optionalStringValue(def, 0, "TEST"));
  }

  @Test
  void optionalStringValueReturnsValueWhenPresent() {
    var inst = instanceWithDef("TEST", List.of(new StepValue.StringValue("ok")));
    var def = def(inst, "TEST");
    assertEquals("ok", StepParameterReader.optionalStringValue(def, 0, "TEST"));
  }

  @Test
  void numberValueReadsPlainNumber() {
    var inst = instanceWithDef("TEST", List.of(new StepValue.NumberValue(3.14, "3.14")));
    var def = def(inst, "TEST");
    assertEquals(3.14, StepParameterReader.numberValue(def, 0, "TEST"));
  }

  @Test
  void numberValueReadsTypedNumber() {
    var inst = instanceWithDef("TEST", List.of(
        new StepValue.TypedValue("LENGTH_MEASURE", new StepValue.NumberValue(5.0, "5"))));
    var def = def(inst, "TEST");
    assertEquals(5.0, StepParameterReader.numberValue(def, 0, "TEST"));
  }

  @Test
  void referenceIdReportsExpectedAndActualType() {
    var inst = instanceWithDef("TEST", List.of(new StepValue.StringValue("not-ref")));
    var def = def(inst, "TEST");

    StepResolutionException exception = assertThrows(StepResolutionException.class,
        () -> StepParameterReader.referenceId(def, 0, "TEST"));

    assertEquals(
        "TEST parameter 0 type mismatch: expected reference, actual string",
        exception.getMessage());
  }

  @Test
  void doubleListReportsExpectedAndActualParameterType() {
    var inst = instanceWithDef("TEST", List.of(new StepValue.ReferenceValue(7)));
    var def = def(inst, "TEST");

    StepResolutionException exception = assertThrows(StepResolutionException.class,
        () -> StepParameterReader.doubleList(def, 0, "TEST"));

    assertEquals(
        "TEST parameter 0 type mismatch: expected list, actual reference",
        exception.getMessage());
  }

  @Test
  void doubleListReportsExpectedAndActualElementType() {
    var inst = instanceWithDef("TEST", List.of(
        new StepValue.ListValue(List.of(
            new StepValue.NumberValue(1.0, "1.0"),
            new StepValue.StringValue("bad")))));
    var def = def(inst, "TEST");

    StepResolutionException exception = assertThrows(StepResolutionException.class,
        () -> StepParameterReader.doubleList(def, 0, "TEST"));

    assertEquals(
        "TEST parameter 0 numeric list element type mismatch: expected number, actual string",
        exception.getMessage());
  }

  @Test
  void optionalNumberValueReturnsNullWhenOmitted() {
    var inst = instanceWithDef("TEST", List.of(new StepValue.OmittedValue()));
    var def = def(inst, "TEST");
    assertNull(StepParameterReader.optionalNumberValue(def, 0, "TEST"));
  }

  @Test
  void integerValueReadsWholeNumber() {
    var inst = instanceWithDef("TEST", List.of(new StepValue.NumberValue(42.0, "42")));
    var def = def(inst, "TEST");
    assertEquals(42, StepParameterReader.integerValue(def, 0, "TEST"));
  }

  @Test
  void integerValueThrowsOnFractional() {
    var inst = instanceWithDef("TEST", List.of(new StepValue.NumberValue(3.14, "3.14")));
    var def = def(inst, "TEST");
    assertThrows(StepResolutionException.class,
        () -> StepParameterReader.integerValue(def, 0, "TEST"));
  }

  @Test
  void optionalIntegerValueReturnsNullWhenOmitted() {
    var inst = instanceWithDef("TEST", List.of(new StepValue.NotProvidedValue()));
    var def = def(inst, "TEST");
    assertNull(StepParameterReader.optionalIntegerValue(def, 0, "TEST"));
  }

  @Test
  void enumValueReadsEnum() {
    var inst = instanceWithDef("TEST", List.of(new StepValue.EnumValue("CLOCKWISE")));
    var def = def(inst, "TEST");
    assertEquals("CLOCKWISE", StepParameterReader.enumValue(def, 0, "TEST"));
  }

  @Test
  void enumValueThrowsOnNonEnum() {
    var inst = instanceWithDef("TEST", List.of(new StepValue.NumberValue(1.0, "1")));
    var def = def(inst, "TEST");
    assertThrows(StepResolutionException.class,
        () -> StepParameterReader.enumValue(def, 0, "TEST"));
  }

  @Test
  void booleanValueReadsTrue() {
    var inst = instanceWithDef("TEST", List.of(new StepValue.EnumValue("T")));
    var def = def(inst, "TEST");
    assertTrue(StepParameterReader.booleanValue(def, 0, "TEST"));
  }

  @Test
  void booleanValueReadsFalse() {
    var inst = instanceWithDef("TEST", List.of(new StepValue.EnumValue("F")));
    var def = def(inst, "TEST");
    assertFalse(StepParameterReader.booleanValue(def, 0, "TEST"));
  }

  @Test
  void booleanValueThrowsOnUnknown() {
    var inst = instanceWithDef("TEST", List.of(new StepValue.EnumValue("U")));
    var def = def(inst, "TEST");
    assertThrows(StepResolutionException.class,
        () -> StepParameterReader.booleanValue(def, 0, "TEST"));
  }

  @Test
  void logicalValueReadsEnumLogical() {
    var inst = instanceWithDef("TEST", List.of(new StepValue.EnumValue("T")));
    var def = def(inst, "TEST");
    assertEquals("T", StepParameterReader.logicalValue(def, 0, "TEST"));
  }

  @Test
  void logicalValueReadsStringLogical() {
    var inst = instanceWithDef("TEST", List.of(new StepValue.StringValue("UNKNOWN")));
    var def = def(inst, "TEST");
    assertEquals("UNKNOWN", StepParameterReader.logicalValue(def, 0, "TEST"));
  }

  // ---------------------------------------------------------------------------
  // Reference readers
  // ---------------------------------------------------------------------------

  @Test
  void referenceIdReadsReference() {
    var inst = instanceWithDef("TEST", List.of(new StepValue.ReferenceValue(99)));
    var def = def(inst, "TEST");
    assertEquals(99, StepParameterReader.referenceId(def, 0, "TEST"));
  }

  @Test
  void referenceIdThrowsOnNonReference() {
    var inst = instanceWithDef("TEST", List.of(new StepValue.NumberValue(1.0, "1")));
    var def = def(inst, "TEST");
    assertThrows(StepResolutionException.class,
        () -> StepParameterReader.referenceId(def, 0, "TEST"));
  }

  @Test
  void tryResolveReferenceResolvesReference() {
    var resolver = java.util.Map.<Integer, StepEntity>of(
        5, new StepCartesianPoint(5, "P", List.of(1.0, 2.0, 3.0)));
    StepValue ref = new StepValue.ReferenceValue(5);
    StepEntity result = StepParameterReader.tryResolveReference(ref, resolver::get);
    assertInstanceOf(StepCartesianPoint.class, result);
  }

  @Test
  void tryResolveReferenceReturnsNullOnOmitted() {
    StepValue omitted = new StepValue.OmittedValue();
    assertNull(StepParameterReader.tryResolveReference(omitted, id -> null));
  }

  @Test
  void tryResolveReferenceReturnsNullOnNotProvided() {
    StepValue notProvided = new StepValue.NotProvidedValue();
    assertNull(StepParameterReader.tryResolveReference(notProvided, id -> null));
  }

  @Test
  void tryResolveReferenceThrowsOnNonReference() {
    StepValue num = new StepValue.NumberValue(1.0, "1");
    assertThrows(StepResolutionException.class,
        () -> StepParameterReader.tryResolveReference(num, id -> null));
  }

  @Test
  void tryResolveReferenceUnwrapsTypedReference() {
    var resolver = java.util.Map.<Integer, StepEntity>of(
        7, new StepDirection(7, "D", List.of(1.0, 0.0, 0.0)));
    StepValue typedRef = new StepValue.TypedValue("X", new StepValue.ReferenceValue(7));
    StepEntity result = StepParameterReader.tryResolveReference(typedRef, resolver::get);
    assertInstanceOf(StepDirection.class, result);
  }

  // ---------------------------------------------------------------------------
  // List readers
  // ---------------------------------------------------------------------------

  @Test
  void coordinateTripleReadsThreeNumbers() {
    var inst = instanceWithDef("TEST", List.of(doubleList(1.0, 2.0, 3.0)));
    var def = def(inst, "TEST");
    List<Double> result = StepParameterReader.coordinateTriple(def, 0, "TEST");
    assertEquals(List.of(1.0, 2.0, 3.0), result);
  }

  @Test
  void coordinateListReadsTwoNumbers() {
    var inst = instanceWithDef("TEST", List.of(doubleList(1.0, 2.0)));
    var def = def(inst, "TEST");
    List<Double> result = StepParameterReader.coordinateList(def, 0, 2, 3, "TEST");
    assertEquals(List.of(1.0, 2.0), result);
  }

  @Test
  void coordinateListRejectsTooFewElements() {
    var inst = instanceWithDef("TEST", List.of(doubleList(1.0)));
    var def = def(inst, "TEST");
    assertThrows(StepResolutionException.class,
        () -> StepParameterReader.coordinateList(def, 0, 2, 3, "TEST"));
  }

  @Test
  void coordinateListRejectsTooManyElements() {
    var inst = instanceWithDef("TEST", List.of(doubleList(1.0, 2.0, 3.0, 4.0)));
    var def = def(inst, "TEST");
    assertThrows(StepResolutionException.class,
        () -> StepParameterReader.coordinateList(def, 0, 2, 3, "TEST"));
  }

  @Test
  void doubleListReadsNumbers() {
    var inst = instanceWithDef("TEST", List.of(doubleList(1.0, 2.5, 3.0)));
    var def = def(inst, "TEST");
    assertEquals(List.of(1.0, 2.5, 3.0), StepParameterReader.doubleList(def, 0, "TEST"));
  }

  @Test
  void intListReadsIntegers() {
    var inst = instanceWithDef("TEST", List.of(doubleList(1.0, 2.0, 3.0)));
    var def = def(inst, "TEST");
    assertEquals(List.of(1, 2, 3), StepParameterReader.intList(def, 0, "TEST"));
  }

  @Test
  void intListThrowsOnNonIntegers() {
    var inst = instanceWithDef("TEST", List.of(doubleList(1.5, 2.0)));
    var def = def(inst, "TEST");
    assertThrows(StepResolutionException.class,
        () -> StepParameterReader.intList(def, 0, "TEST"));
  }

  @Test
  void stringListReadsStrings() {
    var inst = instanceWithDef("TEST", List.of(
        new StepValue.ListValue(List.of(
            new StepValue.StringValue("a"),
            new StepValue.StringValue("b")))));
    var def = def(inst, "TEST");
    assertEquals(List.of("a", "b"), StepParameterReader.stringList(def, 0, "TEST"));
  }

  @Test
  void optionalStringListValueReturnsEmptyWhenOmitted() {
    var inst = instanceWithDef("TEST", List.of(new StepValue.OmittedValue()));
    var def = def(inst, "TEST");
    assertEquals(List.of(), StepParameterReader.optionalStringListValue(def, 0, "TEST"));
  }

  @Test
  void extractNumberListReadsPreUnwrapped() {
    StepValue list = doubleList(1.0, 2.0, 3.0);
    StepValue unwrapped = StepParameterReader.unwrapTyped(list);
    assertEquals(List.of(1.0, 2.0, 3.0),
        StepParameterReader.extractNumberList(unwrapped, "TEST"));
  }

  @Test
  void extractNumberListThrowsOnNonList() {
    assertThrows(StepResolutionException.class,
        () -> StepParameterReader.extractNumberList(
            new StepValue.NumberValue(1.0, "1"), "TEST"));
  }

  @Test
  void literalListReadsMixedValues() {
    var inst = instanceWithDef("TEST", List.of(
        new StepValue.ListValue(List.of(
            new StepValue.NumberValue(1.0, "1"),
            new StepValue.EnumValue("FOO"),
            new StepValue.ReferenceValue(5)))));
    var def = def(inst, "TEST");
    assertEquals(List.of("1", ".FOO.", "#5"),
        StepParameterReader.literalList(def, 0, "TEST"));
  }

  @Test
  void numberGridReadsNestedNumbers() {
    var inst = instanceWithDef("TEST", List.of(
        new StepValue.ListValue(List.of(
            doubleList(1.0, 2.0),
            doubleList(3.0, 4.0)))));
    var def = def(inst, "TEST");
    List<List<Double>> grid = StepParameterReader.numberGrid(def, 0, "TEST");
    assertEquals(List.of(List.of(1.0, 2.0), List.of(3.0, 4.0)), grid);
  }

  // ---------------------------------------------------------------------------
  // Entity reference list
  // ---------------------------------------------------------------------------

  @Test
  void entityReferenceListResolvesReferences() {
    var inst = instanceWithDef("TEST", List.of(
        new StepValue.ListValue(List.of(
            new StepValue.ReferenceValue(10),
            new StepValue.ReferenceValue(20)))));
    var def = def(inst, "TEST");
    var resolver = java.util.Map.<Integer, StepEntity>of(
        10, new StepCartesianPoint(10, "A", List.of(0.0, 0.0, 0.0)),
        20, new StepCartesianPoint(20, "B", List.of(1.0, 0.0, 0.0)));
    List<StepEntity> result = StepParameterReader.entityReferenceList(
        def, 0, "must be references", resolver::get);
    assertEquals(2, result.size());
    assertInstanceOf(StepCartesianPoint.class, result.get(0));
    assertInstanceOf(StepCartesianPoint.class, result.get(1));
  }

  @Test
  void entityReferenceListThrowsOnNonReference() {
    var inst = instanceWithDef("TEST", List.of(
        new StepValue.ListValue(List.of(
            new StepValue.NumberValue(1.0, "1")))));
    var def = def(inst, "TEST");
    assertThrows(StepResolutionException.class,
        () -> StepParameterReader.entityReferenceList(
            def, 0, "must be references", id -> null));
  }

  // ---------------------------------------------------------------------------
  // requireParameterCount
  // ---------------------------------------------------------------------------

  @Test
  void requireParameterCountPassesExact() {
    var inst = instanceWithDef("TEST", List.of(
        new StepValue.NumberValue(1.0, "1"),
        new StepValue.NumberValue(2.0, "2")));
    var def = def(inst, "TEST");
    assertDoesNotThrow(() -> StepParameterReader.requireParameterCount(inst, def, 2));
  }

  @Test
  void requireParameterCountFailsWrongCount() {
    var inst = instanceWithDef("TEST", List.of(
        new StepValue.NumberValue(1.0, "1")));
    var def = def(inst, "TEST");
    StepResolutionException exception = assertThrows(StepResolutionException.class,
        () -> StepParameterReader.requireParameterCount(inst, def, 3));
    assertEquals(
        "entity #1 TEST parameter count mismatch: expected 3, actual 1",
        exception.getMessage());
  }

  @Test
  void requireParameterCountInPassesOneOfOptions() {
    var inst = instanceWithDef("TEST", List.of(
        new StepValue.NumberValue(1.0, "1")));
    var def = def(inst, "TEST");
    assertDoesNotThrow(() -> StepParameterReader.requireParameterCountIn(inst, def, 1, 2, 3));
  }

  @Test
  void requireParameterCountInFailsNoneMatch() {
    var inst = instanceWithDef("TEST", List.of(
        new StepValue.NumberValue(1.0, "1"),
        new StepValue.NumberValue(2.0, "2")));
    var def = def(inst, "TEST");
    StepResolutionException exception = assertThrows(StepResolutionException.class,
        () -> StepParameterReader.requireParameterCountIn(inst, def, 4, 5));
    assertEquals(
        "entity #1 TEST parameter count mismatch: expected 4 or 5, actual 2",
        exception.getMessage());
  }

  @Test
  void parameterTypeMismatchIncludesEntityAndActualType() {
    var inst = instanceWithDef("TEST", List.of(new StepValue.StringValue("bad")));
    var def = def(inst, "TEST");

    StepResolutionException exception =
        StepParameterReader.parameterTypeMismatch(inst, def, 0, "number");

    assertEquals(
        "entity #1 TEST parameter 0 type mismatch: expected number, actual string",
        exception.getMessage());
  }

  // ---------------------------------------------------------------------------
  // SELECT type validation tests (C10)
  // ---------------------------------------------------------------------------

  @Test
  void typedSelectionWithEntityIdIncludesEntityIdInError() {
    var inst = instanceWithDef("TEST_ENTITY", List.of(new StepValue.NumberValue(1.0, "1")));
    var def = def(inst, "TEST_ENTITY");

    StepResolutionException exception = assertThrows(StepResolutionException.class,
        () -> StepParameterReader.typedSelection(inst, def, 0));

    assertTrue(exception.getMessage().contains("entity #1"));
    assertTrue(exception.getMessage().contains("TEST_ENTITY"));
    assertTrue(exception.getMessage().contains("must be a typed SELECT value"));
    assertTrue(exception.getMessage().contains("actual: number"));
  }

  @Test
  void optionalTypedSelectionReturnsNullWhenOmitted() {
    var inst = instanceWithDef("TEST", List.of(new StepValue.OmittedValue()));
    var def = def(inst, "TEST");

    assertNull(StepParameterReader.optionalTypedSelection(inst, def, 0));
  }

  @Test
  void optionalTypedSelectionReturnsSelectionWhenPresent() {
    var inst = instanceWithDef("TEST", List.of(
        new StepValue.TypedValue("LENGTH_MEASURE", new StepValue.NumberValue(5.0, "5"))));
    var def = def(inst, "TEST");

    StepParameterReader.TypedSelection selection =
        StepParameterReader.optionalTypedSelection(inst, def, 0);

    assertNotNull(selection);
    assertEquals("LENGTH_MEASURE", selection.typeName());
  }

  @Test
  void validateSelectTypeNameAcceptsValidType() {
    var inst = instanceWithDef("TEST", List.of(
        new StepValue.TypedValue("LENGTH_MEASURE", new StepValue.NumberValue(5.0, "5"))));
    var def = def(inst, "TEST");
    StepParameterReader.TypedSelection selection =
        StepParameterReader.typedSelection(inst, def, 0);

    assertDoesNotThrow(() ->
        StepParameterReader.validateSelectTypeName(inst, def, 0, selection,
            SelectTypeRegistry.MEASURE_SELECT_TYPES));
  }

  @Test
  void validateSelectTypeNameRejectsInvalidType() {
    var inst = instanceWithDef("TEST", List.of(
        new StepValue.TypedValue("INVALID_TYPE", new StepValue.NumberValue(5.0, "5"))));
    var def = def(inst, "TEST");
    StepParameterReader.TypedSelection selection =
        StepParameterReader.typedSelection(inst, def, 0);

    StepResolutionException exception = assertThrows(StepResolutionException.class,
        () -> StepParameterReader.validateSelectTypeName(inst, def, 0, selection,
            SelectTypeRegistry.MEASURE_SELECT_TYPES));

    assertTrue(exception.getMessage().contains("entity #1"));
    assertTrue(exception.getMessage().contains("TEST"));
    assertTrue(exception.getMessage().contains("SELECT type name must be one of"));
    assertTrue(exception.getMessage().contains("INVALID_TYPE"));
  }

  @Test
  void validateSelectTypeKnownAcceptsKnownType() {
    var inst = instanceWithDef("TEST", List.of(
        new StepValue.TypedValue("LENGTH_MEASURE", new StepValue.NumberValue(5.0, "5"))));
    var def = def(inst, "TEST");
    StepParameterReader.TypedSelection selection =
        StepParameterReader.typedSelection(inst, def, 0);

    assertDoesNotThrow(() ->
        StepParameterReader.validateSelectTypeKnown(inst, def, 0, selection));
  }

  @Test
  void validateSelectTypeKnownRejectsUnknownType() {
    var inst = instanceWithDef("TEST", List.of(
        new StepValue.TypedValue("UNKNOWN_SELECT", new StepValue.NumberValue(5.0, "5"))));
    var def = def(inst, "TEST");
    StepParameterReader.TypedSelection selection =
        StepParameterReader.typedSelection(inst, def, 0);

    StepResolutionException exception = assertThrows(StepResolutionException.class,
        () -> StepParameterReader.validateSelectTypeKnown(inst, def, 0, selection));

    assertTrue(exception.getMessage().contains("entity #1"));
    assertTrue(exception.getMessage().contains("TEST"));
    assertTrue(exception.getMessage().contains("is not a known AP242 SELECT type"));
    assertTrue(exception.getMessage().contains("UNKNOWN_SELECT"));
  }

  // ---------------------------------------------------------------------------
  // Helper
  // ---------------------------------------------------------------------------

  private static StepValue doubleList(double... values) {
    List<StepValue> elements = new java.util.ArrayList<>();
    for (double v : values) {
      elements.add(new StepValue.NumberValue(v, String.valueOf(v)));
    }
    return new StepValue.ListValue(elements);
  }
}
