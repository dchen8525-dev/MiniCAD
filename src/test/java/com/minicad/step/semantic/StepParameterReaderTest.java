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
    assertThrows(StepResolutionException.class,
        () -> StepParameterReader.stringValue(def, 0, "TEST_ENTITY"));
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
    assertThrows(StepResolutionException.class,
        () -> StepParameterReader.requireParameterCount(inst, def, 3));
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
    assertThrows(StepResolutionException.class,
        () -> StepParameterReader.requireParameterCountIn(inst, def, 4, 5));
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
