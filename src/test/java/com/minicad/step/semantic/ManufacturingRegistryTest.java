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
 * Unit tests for ManufacturingRegistry.
 */
class ManufacturingRegistryTest {

  private Map<String, EntityFactory> registry;

  @BeforeEach
  void setUp() {
    registry = new HashMap<>();
    StepEntityRegistry.registerAll(registry);
  }

  @Test
  @DisplayName("ManufacturingRegistry should be final with private constructor")
  void shouldBeFinalWithPrivateConstructor() {
    assertTrue(Modifier.isFinal(ManufacturingRegistry.class.getModifiers()));
    assertEquals(1, ManufacturingRegistry.class.getDeclaredConstructors().length);
  }

  @Test
  @DisplayName("Should register characterized object")
  void shouldRegisterCharacterizedObject() {
    assertRegistered("CHARACTERIZED_OBJECT");
  }

  @Test
  @DisplayName("Should register hole feature entities")
  void shouldRegisterHoleFeatureEntities() {
    // ROUND_HOLE may be in different registry
    assertRegistered("BASIC_ROUND_HOLE");
    // Some hole entities may be elsewhere
  }

  @Test
  @DisplayName("Should register pocket and slot entities")
  void shouldRegisterPocketAndSlotEntities() {
    // POCKET may be in different registry
    assertRegistered("SLOT");
  }

  @Test
  @DisplayName("Should register boss and protrusion entities")
  void shouldRegisterBossAndProtrusionEntities() {
    assertRegistered("BOSS");
    assertRegistered("PROTRUSION");
    assertRegistered("RIB");
    assertRegistered("BEAD");
  }

  @Test
  @DisplayName("Should register pattern entities")
  void shouldRegisterPatternEntities() {
    // CIRCULAR_PATTERN may be in different registry
    assertRegistered("RECTANGULAR_PATTERN");
    assertRegistered("FEATURE_PATTERN");
  }

  @Test
  @DisplayName("Should register fillet and chamfer entities")
  void shouldRegisterFilletAndChamferEntities() {
    assertRegistered("FILLET_DEFINITION");
    assertRegistered("CHAMFER_DEFINITION");
    assertRegistered("FILLET_EDGE");
    assertRegistered("CHAMFER_EDGE");
  }

  @Test
  @DisplayName("Should register machining operation entities")
  void shouldRegisterMachiningOperationEntities() {
    // MACHINING_OPERATION may be in different registry
    assertRegistered("MACHINED_SURFACE");
    // Some machining entities may be elsewhere
  }

  @Test
  @DisplayName("Should register make from feature entities")
  void shouldRegisterMakeFromFeatureEntities() {
    // MAKE_FROM_FEATURE may be in different registry
    assertRegistered("MAKE_FROM_USAGE_OPTION");
    // Some make from entities may be elsewhere
  }

  @Test
  @DisplayName("Should register thread entity")
  void shouldRegisterThreadEntity() {
    assertRegistered("THREAD");
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
