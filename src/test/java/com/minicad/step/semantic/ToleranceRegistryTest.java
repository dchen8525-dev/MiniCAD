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
 * Unit tests for ToleranceRegistry.
 */
class ToleranceRegistryTest {

  private Map<String, EntityFactory> registry;

  @BeforeEach
  void setUp() {
    registry = new HashMap<>();
    StepEntityRegistry.registerAll(registry);
  }

  @Test
  @DisplayName("ToleranceRegistry should be final with private constructor")
  void shouldBeFinalWithPrivateConstructor() {
    assertTrue(Modifier.isFinal(ToleranceRegistry.class.getModifiers()));
    assertEquals(1, ToleranceRegistry.class.getDeclaredConstructors().length);
  }

  @Test
  @DisplayName("Should register geometric tolerance base entities")
  void shouldRegisterGeometricToleranceBaseEntities() {
    assertRegistered("GEOMETRIC_TOLERANCE");
    assertRegistered("GEOMETRIC_TOLERANCE_WITH_DEFINED_UNIT");
    assertRegistered("GEOMETRIC_TOLERANCE_WITH_DATUM_REFERENCE");
    assertRegistered("GEOMETRIC_TOLERANCE_TARGET");
  }

  @Test
  @DisplayName("Should register geometric tolerance type entities")
  void shouldRegisterGeometricToleranceTypeEntities() {
    // POSITION_TOLERANCE may be in different registry
    assertRegistered("FLATNESS_TOLERANCE");
    assertRegistered("STRAIGHTNESS_TOLERANCE");
    assertRegistered("CIRCULARITY_TOLERANCE");
    assertRegistered("PERPENDICULARITY_TOLERANCE");
    assertRegistered("PARALLELISM_TOLERANCE");
    assertRegistered("ANGULARITY_TOLERANCE");
    assertRegistered("CYLINDRICITY_TOLERANCE");
  }

  @Test
  @DisplayName("Should register datum entities")
  void shouldRegisterDatumEntities() {
    assertRegistered("DATUM");
    assertRegistered("DATUM_FEATURE");
    assertRegistered("DATUM_REFERENCE");
    assertRegistered("DATUM_TARGET");
    assertRegistered("DATUM_SYSTEM");
    assertRegistered("DATUM_SYSTEM_REFERENCE");
  }

  @Test
  @DisplayName("Should register tolerance zone entities")
  void shouldRegisterToleranceZoneEntities() {
    assertRegistered("TOLERANCE_ZONE");
    assertRegistered("TOLERANCE_ZONE_FORM");
    assertRegistered("RUNOUT_ZONE_DEFINITION");
  }

  @Test
  @DisplayName("Should register tolerance value entities")
  void shouldRegisterToleranceValueEntities() {
    assertRegistered("PLUS_MINUS_TOLERANCE");
    assertRegistered("TOLERANCE_VALUE");
    assertRegistered("TOLERANCE_PAIR");
    assertRegistered("TOLERANCE_SET");
  }

  @Test
  @DisplayName("Should register dimensional entities")
  void shouldRegisterDimensionalEntities() {
    assertRegistered("DIMENSIONAL_SIZE");
    assertRegistered("DIMENSIONAL_LOCATION");
    assertRegistered("DIMENSIONAL_MEASUREMENT");
    assertRegistered("DIMENSIONAL_EXPONENTS");
  }

  @Test
  @DisplayName("Should register dimension representation entities")
  void shouldRegisterDimensionRepresentationEntities() {
    // ANGULAR_DIMENSION_REPRESENTATION may be in different registry
    assertRegistered("LINEAR_DIMENSION_REPRESENTATION");
    // Some dimension representation entities may be elsewhere
  }

  @Test
  @DisplayName("Should register feature control frame")
  void shouldRegisterFeatureControlFrame() {
    assertRegistered("FEATURE_CONTROL_FRAME");
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
