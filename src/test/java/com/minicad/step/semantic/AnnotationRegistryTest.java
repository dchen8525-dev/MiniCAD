package com.minicad.step.semantic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for AnnotationRegistry.
 */
class AnnotationRegistryTest {

  private Map<String, EntityFactory> registry;

  @BeforeEach
  void setUp() {
    registry = new HashMap<>();
    AnnotationRegistry.register(registry);
  }

  @Test
  @DisplayName("AnnotationRegistry should be final with private constructor")
  void shouldBeFinalWithPrivateConstructor() {
    assertTrue(Modifier.isFinal(AnnotationRegistry.class.getModifiers()));
    assertEquals(1, AnnotationRegistry.class.getDeclaredConstructors().length);
  }

  @Test
  @DisplayName("Should register annotation occurrence entities")
  void shouldRegisterAnnotationOccurrenceEntities() {
    assertRegistered("ANNOTATION_FILL_AREA");
    assertRegistered("ANNOTATION_PLANE");
    assertRegistered("ANNOTATION_CURVE_OCCURRENCE");
    assertRegistered("ANNOTATION_TEXT");
    assertRegistered("ANNOTATION_SYMBOL");
    assertRegistered("ANNOTATION_TEXT_OCCURRENCE");
  }

  @Test
  @DisplayName("Should register colour entities")
  void shouldRegisterColourEntities() {
    assertRegistered("COLOUR_RGB");
    assertRegistered("COLOUR_SPECIFICATION");
    assertRegistered("COLOUR");
    assertRegistered("PRE_DEFINED_COLOUR");
  }

  @Test
  @DisplayName("Should register curve style entities")
  void shouldRegisterCurveStyleEntities() {
    assertRegistered("CURVE_STYLE");
    assertRegistered("CURVE_STYLE_FONT");
    assertRegistered("CURVE_STYLE_RENDERING");
    assertRegistered("PRE_DEFINED_CURVE_FONT");
  }

  @Test
  @DisplayName("Should register text style entities")
  void shouldRegisterTextStyleEntities() {
    assertRegistered("TEXT_STYLE");
    assertRegistered("TEXT_STYLE_WITH_SPACING");
    assertRegistered("TEXT_STYLE_WITH_JUSTIFICATION");
    assertRegistered("PRE_DEFINED_TEXT_FONT");
  }

  @Test
  @DisplayName("Should register fill area style entities")
  void shouldRegisterFillAreaStyleEntities() {
    assertRegistered("FILL_AREA_STYLE_COLOUR");
    assertRegistered("FILL_AREA_STYLE");
  }

  @Test
  @DisplayName("Should register surface style entities")
  void shouldRegisterSurfaceStyleEntities() {
    assertRegistered("SURFACE_STYLE_FILL_AREA");
    assertRegistered("SURFACE_SIDE_STYLE");
    assertRegistered("SURFACE_STYLE_USAGE");
  }

  @Test
  @DisplayName("Should register styled item entities")
  void shouldRegisterStyledItemEntities() {
    assertRegistered("STYLED_ITEM");
    assertRegistered("OVER_RIDING_STYLED_ITEM");
  }

  @Test
  @DisplayName("Should register presentation style entities")
  void shouldRegisterPresentationStyleEntities() {
    assertRegistered("PRESENTATION_STYLE_ASSIGNMENT");
    assertRegistered("PRESENTATION_LAYER_ASSIGNMENT");
  }

  @Test
  @DisplayName("Total annotation entity count should be reasonable")
  void totalEntityCountShouldBeReasonable() {
    // Expecting approximately 60 annotation entities
    assertTrue(registry.size() >= 50,
        "Expected at least 50 annotation entities, got " + registry.size());
    assertTrue(registry.size() <= 80,
        "Expected at most 80 annotation entities, got " + registry.size());
  }

  private void assertRegistered(String entityName) {
    EntityFactory factory = registry.get(entityName);
    assertNotNull(factory, entityName + " should be registered");
  }
}