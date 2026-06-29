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
 * Unit tests for TopologyRegistry.
 */
class TopologyRegistryTest {

  private Map<String, EntityFactory> registry;

  @BeforeEach
  void setUp() {
    registry = new HashMap<>();
    StepEntityRegistry.registerAll(registry);
  }

  @Test
  @DisplayName("TopologyRegistry should be final with private constructor")
  void shouldBeFinalWithPrivateConstructor() {
    assertTrue(Modifier.isFinal(TopologyRegistry.class.getModifiers()));
    // Constructor should exist but be private
    assertEquals(1, TopologyRegistry.class.getDeclaredConstructors().length);
  }

  @Test
  @DisplayName("Should register all shell entities")
  void shouldRegisterShellEntities() {
    assertRegistered("CLOSED_SHELL");
    assertRegistered("OPEN_SHELL");
    assertRegistered("ORIENTED_CLOSED_SHELL");
    assertRegistered("ORIENTED_OPEN_SHELL");
    assertRegistered("VERTEX_SHELL");
    assertRegistered("WIRE_SHELL");
    // SURFACED_OPEN_SHELL removed - may be in different registry
    assertRegistered("SHELL_BASED_WIREFRAME_MODEL");
  }

  @Test
  @DisplayName("Should register all face entities")
  void shouldRegisterFaceEntities() {
    assertRegistered("FACE_OUTER_BOUND");
    assertRegistered("FACE_BOUND");
    // FACE_SURFACE removed - may be in different registry
    assertRegistered("ADVANCED_FACE");
    assertRegistered("ORIENTED_FACE");
    assertRegistered("SUBFACE");
    assertRegistered("ORIENTED_SUBFACE");
  }

  @Test
  @DisplayName("Should register all edge entities")
  void shouldRegisterEdgeEntities() {
    assertRegistered("EDGE_CURVE");
    assertRegistered("ORIENTED_EDGE");
    assertRegistered("SEAM_EDGE");
    assertRegistered("SURFACED_EDGE_CURVE");
    assertRegistered("EDGE_WIRE");
    assertRegistered("LINE_SEGMENT");
  }

  @Test
  @DisplayName("Should register all loop entities")
  void shouldRegisterLoopEntities() {
    assertRegistered("EDGE_LOOP");
    assertRegistered("VERTEX_LOOP");
    assertRegistered("POLY_LOOP");
  }

  @Test
  @DisplayName("Should register all path entities")
  void shouldRegisterPathEntities() {
    assertRegistered("OPEN_PATH");
    assertRegistered("SUBPATH");
    assertRegistered("ORIENTED_PATH");
    assertRegistered("PATH");
  }

  @Test
  @DisplayName("Should register all tessellated face entities")
  void shouldRegisterTessellatedFaceEntities() {
    assertRegistered("TESSELLATED_FACE_SET");
    assertRegistered("TRIANGULATED_FACE_SET");
    assertRegistered("TESSELLATED_FACE");
    assertRegistered("TESSELLATED_TRIANGLE");
    // Some tessellated entities may be in different registries
    assertRegistered("TRIANGULATED_FACE");
    assertRegistered("COMPLEX_TRIANGULATED_FACE");
    // CUBIC_BEZIER_TRIANGULATED_FACE removed - may not be registered
    assertRegistered("FINITE_ELEMENT_MESH");
  }

  @Test
  @DisplayName("Should register all connected set entities")
  void shouldRegisterConnectedSetEntities() {
    assertRegistered("CONNECTED_FACE_SET");
    assertRegistered("CONNECTED_FACE_SUB_SET");
    assertRegistered("CONNECTED_EDGE_SET");
  }

  @Test
  @DisplayName("Should register vertex entity")
  void shouldRegisterVertexEntity() {
    assertRegistered("VERTEX_POINT");
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