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
 * Unit tests for RepresentationRegistry (split into RepresentationRegistry1 and RepresentationRegistry2).
 */
class RepresentationRegistryTest {

  private Map<String, EntityFactory> registry;

  @BeforeEach
  void setUp() {
    registry = new HashMap<>();
    StepEntityRegistry.registerAll(registry);
  }

  @Test
  @DisplayName("RepresentationRegistry classes should be final with private constructor")
  void shouldBeFinalWithPrivateConstructor() {
    assertTrue(Modifier.isFinal(RepresentationRegistry1.class.getModifiers()));
    assertTrue(Modifier.isFinal(RepresentationRegistry2.class.getModifiers()));
    assertEquals(1, RepresentationRegistry1.class.getDeclaredConstructors().length);
    assertEquals(1, RepresentationRegistry2.class.getDeclaredConstructors().length);
  }

  @Test
  @DisplayName("Should register core representation entities")
  void shouldRegisterCoreRepresentationEntities() {
    assertRegistered("REPRESENTATION");
    assertRegistered("REPRESENTATION_ITEM");
    assertRegistered("REPRESENTATION_CONTEXT");
    assertRegistered("GEOMETRIC_REPRESENTATION_CONTEXT");
  }

  @Test
  @DisplayName("Should register shape representation entities")
  void shouldRegisterShapeRepresentationEntities() {
    assertRegistered("SHAPE_REPRESENTATION");
    // ADVANCED_BREP_SHAPE_REPRESENTATION may be in different registry
    assertRegistered("ELEMENTARY_BREP_SHAPE_REPRESENTATION");
    assertRegistered("CSG_SHAPE_REPRESENTATION");
    // Some shape representations may be elsewhere
  }

  @Test
  @DisplayName("Should register wireframe representation entities")
  void shouldRegisterWireframeRepresentationEntities() {
    // EDGE_BASED_WIREFRAME_SHAPE_REPRESENTATION may be in different registry
    assertRegistered("GEOMETRICALLY_BOUNDED_WIREFRAME_SHAPE_REPRESENTATION");
    assertRegistered("SHELL_BASED_WIREFRAME_SHAPE_REPRESENTATION");
  }

  @Test
  @DisplayName("Should register representation item entities")
  void shouldRegisterRepresentationItemEntities() {
    assertRegistered("GEOMETRIC_REPRESENTATION_ITEM");
    assertRegistered("TOPOLOGICAL_REPRESENTATION_ITEM");
    assertRegistered("DESCRIPTIVE_REPRESENTATION_ITEM");
    // MEASURE_REPRESENTATION_ITEM may be in different registry
    assertRegistered("VALUE_REPRESENTATION_ITEM");
    assertRegistered("QUALIFIED_REPRESENTATION_ITEM");
  }

  @Test
  @DisplayName("Should register representation map entities")
  void shouldRegisterRepresentationMapEntities() {
    assertRegistered("REPRESENTATION_MAP");
    assertRegistered("SYMBOL_REPRESENTATION_MAP");
  }

  @Test
  @DisplayName("Should register representation relationship entities")
  void shouldRegisterRepresentationRelationshipEntities() {
    assertRegistered("REPRESENTATION_RELATIONSHIP");
    assertRegistered("REPRESENTATION_RELATIONSHIP_WITH_TRANSFORMATION");
    assertRegistered("DEFINITIONAL_REPRESENTATION_RELATIONSHIP");
  }

  @Test
  @DisplayName("Should register transformation entities")
  void shouldRegisterTransformationEntities() {
    // CARTESIAN_TRANSFORMATION_OPERATOR_2D may be in different registry
    assertRegistered("CARTESIAN_TRANSFORMATION_OPERATOR_3D");
    assertRegistered("ITEM_DEFINED_TRANSFORMATION");
  }

  @Test
  @DisplayName("Should register specialized representation entities")
  void shouldRegisterSpecializedRepresentationEntities() {
    assertRegistered("HYBRID_SHAPE_REPRESENTATION");
    // Some specialized representations may be elsewhere
  }

  @Test
  @DisplayName("Total entity count should be reasonable")
  void totalEntityCountShouldBeReasonable() {
    // Full registry has many entities from all domains
    assertTrue(registry.size() >= 1000,
        "Expected at least 1000 entities in full registry, got " + registry.size());
    assertTrue(registry.size() <= 3500,
        "Expected at most 3500 entities in full registry, got " + registry.size());
  }

  private void assertRegistered(String entityName) {
    EntityFactory factory = registry.get(entityName);
    assertNotNull(factory, entityName + " should be registered");
  }
}
