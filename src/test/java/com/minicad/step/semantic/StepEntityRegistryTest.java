package com.minicad.step.semantic;

import org.junit.jupiter.api.Test;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for StepEntityRegistry aggregator class.
 */
class StepEntityRegistryTest {

  @Test
  void testRegisterAllPopulatesRegistry() {
    Map<String, EntityFactory> registry = new HashMap<>();
    StepEntityRegistry.registerAll(registry);

    // Registry should be populated with entities from all sub-registries
    // GeometryRegistry provides ~80 entities, TopologyRegistry provides ~40 entities
    assertFalse(registry.isEmpty(), "Registry should not be empty after registration");
    assertTrue(registry.size() >= 100,
        "Expected at least 100 entities (Geometry + Topology), got " + registry.size());
  }

  @Test
  void testRegistryClassIsFinal() {
    // Verify class structure
    assertTrue(Modifier.isFinal(StepEntityRegistry.class.getModifiers()));
  }

  @Test
  void testPrivateConstructor() throws Exception {
    // Verify private constructor (utility class pattern)
    var constructor = StepEntityRegistry.class.getDeclaredConstructor();
    assertFalse(constructor.canAccess(null));
  }
}
