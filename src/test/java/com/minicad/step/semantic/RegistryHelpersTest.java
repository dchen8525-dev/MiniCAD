package com.minicad.step.semantic;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for RegistryHelpers utility class.
 * Verifies batch registration methods work correctly.
 */
class RegistryHelpersTest {

  @Test
  void testRegisterGeometricToleranceAliases() {
    Map<String, EntityFactory> registry = new HashMap<>();
    RegistryHelpers.registerGeometricToleranceAliases(
        registry, "ANGULARITY_TOLERANCE", "PARALLELISM_TOLERANCE", "PERPENDICULARITY_TOLERANCE");

    assertTrue(registry.containsKey("ANGULARITY_TOLERANCE"));
    assertTrue(registry.containsKey("PARALLELISM_TOLERANCE"));
    assertTrue(registry.containsKey("PERPENDICULARITY_TOLERANCE"));
    assertEquals(3, registry.size());
  }

  @Test
  void testRegisterShapeAspectAliases() {
    Map<String, EntityFactory> registry = new HashMap<>();
    RegistryHelpers.registerShapeAspectAliases(
        registry, "DATUM_FEATURE", "DATUM_TARGET");

    assertTrue(registry.containsKey("DATUM_FEATURE"));
    assertTrue(registry.containsKey("DATUM_TARGET"));
    assertEquals(2, registry.size());
  }

  @Test
  void testRegisterShapeAspectOccurrenceAliases() {
    Map<String, EntityFactory> registry = new HashMap<>();
    RegistryHelpers.registerShapeAspectOccurrenceAliases(
        registry, "HOLE_OCCURRENCE", "SLOT_OCCURRENCE");

    assertTrue(registry.containsKey("HOLE_OCCURRENCE"));
    assertTrue(registry.containsKey("SLOT_OCCURRENCE"));
    assertEquals(2, registry.size());
  }

  @Test
  void testRegisterCharacterizedObjectAliases() {
    Map<String, EntityFactory> registry = new HashMap<>();
    RegistryHelpers.registerCharacterizedObjectAliases(
        registry, "FEATURE_DEFINITION", "CHAMFER_DEFINITION");

    assertTrue(registry.containsKey("FEATURE_DEFINITION"));
    assertTrue(registry.containsKey("CHAMFER_DEFINITION"));
    assertEquals(2, registry.size());
  }

  @Test
  void testRegisterExternallyDefinedItemAliases() {
    Map<String, EntityFactory> registry = new HashMap<>();
    RegistryHelpers.registerExternallyDefinedItemAliases(
        registry, "EXTERNALLY_DEFINED_HATCH_STYLE", "EXTERNALLY_DEFINED_TILE_STYLE");

    assertTrue(registry.containsKey("EXTERNALLY_DEFINED_HATCH_STYLE"));
    assertTrue(registry.containsKey("EXTERNALLY_DEFINED_TILE_STYLE"));
    assertEquals(2, registry.size());
  }

  @Test
  void testRegisterRepresentationAliases() {
    Map<String, EntityFactory> registry = new HashMap<>();
    RegistryHelpers.registerRepresentationAliases(
        registry, true, "SHAPE_REPRESENTATION", "ADVANCED_BREP_SHAPE_REPRESENTATION");

    assertTrue(registry.containsKey("SHAPE_REPRESENTATION"));
    assertTrue(registry.containsKey("ADVANCED_BREP_SHAPE_REPRESENTATION"));
    assertEquals(2, registry.size());
  }

  @Test
  void testRegisterKinematicPairAliases() {
    Map<String, EntityFactory> registry = new HashMap<>();
    RegistryHelpers.registerKinematicPairAliases(
        registry, "REVOLUTE_PAIR", "PRISMATIC_PAIR", "CYLINDRICAL_PAIR");

    assertTrue(registry.containsKey("REVOLUTE_PAIR"));
    assertTrue(registry.containsKey("PRISMATIC_PAIR"));
    assertTrue(registry.containsKey("CYLINDRICAL_PAIR"));
    assertEquals(3, registry.size());
  }

  @Test
  void testRegisterFeaAliases() {
    Map<String, EntityFactory> registry = new HashMap<>();
    RegistryHelpers.registerFeaAliases(
        registry, "FEA_MODEL_3D", "FEA_MODEL_2D");

    assertTrue(registry.containsKey("FEA_MODEL_3D"));
    assertTrue(registry.containsKey("FEA_MODEL_2D"));
    assertEquals(2, registry.size());
  }

  @Test
  void testEmptyVarargs() {
    Map<String, EntityFactory> registry = new HashMap<>();
    RegistryHelpers.registerGeometricToleranceAliases(registry);

    assertTrue(registry.isEmpty());
  }

  @Test
  void testMultipleRegistrationsDifferentTypes() {
    Map<String, EntityFactory> registry = new HashMap<>();
    RegistryHelpers.registerGeometricToleranceAliases(registry, "TOLERANCE_1");
    RegistryHelpers.registerShapeAspectAliases(registry, "ASPECT_1", "ASPECT_2");
    RegistryHelpers.registerKinematicPairAliases(registry, "PAIR_1");

    assertTrue(registry.containsKey("TOLERANCE_1"));
    assertTrue(registry.containsKey("ASPECT_1"));
    assertTrue(registry.containsKey("ASPECT_2"));
    assertTrue(registry.containsKey("PAIR_1"));
    assertEquals(4, registry.size());
  }
}
