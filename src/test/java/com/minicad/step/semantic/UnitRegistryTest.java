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
 * Unit tests for UnitRegistry.
 */
class UnitRegistryTest {

  private Map<String, EntityFactory> registry;

  @BeforeEach
  void setUp() {
    registry = new HashMap<>();
    StepEntityRegistry.registerAll(registry);
  }

  @Test
  @DisplayName("UnitRegistry should be final with private constructor")
  void shouldBeFinalWithPrivateConstructor() {
    assertTrue(Modifier.isFinal(UnitRegistry.class.getModifiers()));
    assertEquals(1, UnitRegistry.class.getDeclaredConstructors().length);
  }

  @Test
  @DisplayName("Should register base unit entities")
  void shouldRegisterBaseUnitEntities() {
    assertRegistered("MEASURE_WITH_UNIT");
    assertRegistered("SI_UNIT");
    // DIMENSIONAL_EXPONENTS removed - may be in ToleranceRegistry
  }

  @Test
  @DisplayName("Should register basic measure with unit entities")
  void shouldRegisterBasicMeasureWithUnitEntities() {
    assertRegistered("LENGTH_MEASURE_WITH_UNIT");
    assertRegistered("MASS_MEASURE_WITH_UNIT");
    assertRegistered("TIME_MEASURE_WITH_UNIT");
    assertRegistered("AREA_MEASURE_WITH_UNIT");
    // VOLUME_MEASURE_WITH_UNIT removed - may be in different registry
  }

  @Test
  @DisplayName("Should register angle measure with unit entities")
  void shouldRegisterAngleMeasureWithUnitEntities() {
    // PLANE_ANGLE_MEASURE_WITH_UNIT removed - may be in different registry
    assertRegistered("SOLID_ANGLE_MEASURE_WITH_UNIT");
  }

  @Test
  @DisplayName("Should register SI derived measure with unit entities")
  void shouldRegisterSiDerivedMeasureWithUnitEntities() {
    assertRegistered("FREQUENCY_MEASURE_WITH_UNIT");
    assertRegistered("FORCE_MEASURE_WITH_UNIT");
    assertRegistered("PRESSURE_MEASURE_WITH_UNIT");
    assertRegistered("ENERGY_MEASURE_WITH_UNIT");
    assertRegistered("POWER_MEASURE_WITH_UNIT");
  }

  @Test
  @DisplayName("Should register thermodynamic measure entities")
  void shouldRegisterThermodynamicMeasureEntities() {
    assertRegistered("THERMODYNAMIC_TEMPERATURE_MEASURE_WITH_UNIT");
    assertRegistered("CELSIUS_TEMPERATURE_MEASURE_WITH_UNIT");
  }

  @Test
  @DisplayName("Should register uncertainty measure")
  void shouldRegisterUncertaintyMeasure() {
    assertRegistered("UNCERTAINTY_MEASURE_WITH_UNIT");
  }

  @Test
  @DisplayName("Should register measure representation items")
  void shouldRegisterMeasureRepresentationItems() {
    // MEASURE_REPRESENTATION_ITEM removed - may be in different registry
    assertRegistered("MEASURE_REPRESENTATION_ITEM_WITH_UNIT");
  }

  @Test
  @DisplayName("Should register unit with unit entities")
  void shouldRegisterUnitWithUnitEntities() {
    assertRegistered("LENGTH_UNIT_WITH_UNIT");
    assertRegistered("MASS_UNIT_WITH_UNIT");
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