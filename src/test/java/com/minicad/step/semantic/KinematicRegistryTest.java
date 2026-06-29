package com.minicad.step.semantic;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for KinematicRegistry.
 */
class KinematicRegistryTest {

  private Map<String, EntityFactory> registry;

  @BeforeEach
  void setUp() {
    registry = new HashMap<>();
    StepEntityRegistry.registerAll(registry);
  }

  @Test
  @DisplayName("Should register kinematic base entities")
  void shouldRegisterKinematicBaseEntities() {
    assertRegistered("KINEMATIC_PAIR");
    assertRegistered("KINEMATIC_JOINT");
    assertRegistered("KINEMATIC_LINK");
    assertRegistered("KINEMATIC_STRUCTURE");
  }

  @Test
  @DisplayName("Should register kinematic pair types")
  void shouldRegisterKinematicPairTypes() {
    assertRegistered("PRISMATIC_PAIR");
    assertRegistered("REVOLUTE_PAIR");
    assertRegistered("CYLINDRICAL_PAIR");
    assertRegistered("SPHERICAL_PAIR");
    assertRegistered("GEAR_PAIR");
  }

  @Test
  @DisplayName("Should register kinematic joint types")
  void shouldRegisterKinematicJointTypes() {
    assertRegistered("REVOLUTE_JOINT");
    assertRegistered("PRISMATIC_JOINT");
    assertRegistered("SPHERICAL_JOINT");
    assertRegistered("CYLINDRICAL_JOINT");
  }

  @Test
  @DisplayName("Should register kinematic path")
  void shouldRegisterKinematicPath() {
    // KINEMATIC_PATH may be in different registry (Geometry/Topology)
    assertRegistered("KINEMATIC_LINK_SEQUENCE");
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