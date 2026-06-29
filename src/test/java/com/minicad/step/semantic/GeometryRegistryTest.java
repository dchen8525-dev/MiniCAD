package com.minicad.step.semantic;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for GeometryRegistry (split into GeometryRegistry1 and GeometryRegistry2).
 */
class GeometryRegistryTest {

  @Test
  void testGeometryRegistryRegistersExpectedEntities() {
    Map<String, EntityFactory> registry = new HashMap<>();
    GeometryRegistry1.register(registry);
    GeometryRegistry2.register(registry);

    // Verify key geometry entities
    assertTrue(registry.containsKey("CARTESIAN_POINT"));
    assertTrue(registry.containsKey("DIRECTION"));
    assertTrue(registry.containsKey("VECTOR"));
    assertTrue(registry.containsKey("AXIS1_PLACEMENT"));
    assertTrue(registry.containsKey("AXIS2_PLACEMENT_3D"));

    assertTrue(registry.containsKey("LINE"));
    assertTrue(registry.containsKey("CIRCLE"));
    assertTrue(registry.containsKey("ELLIPSE"));
    assertTrue(registry.containsKey("B_SPLINE_CURVE"));
    assertTrue(registry.containsKey("COMPOSITE_CURVE"));
    assertTrue(registry.containsKey("TRIMMED_CURVE"));

    assertTrue(registry.containsKey("PLANE"));
    assertTrue(registry.containsKey("CYLINDRICAL_SURFACE"));
    assertTrue(registry.containsKey("SPHERICAL_SURFACE"));
    assertTrue(registry.containsKey("TOROIDAL_SURFACE"));
    assertTrue(registry.containsKey("B_SPLINE_SURFACE"));

    assertTrue(registry.containsKey("CIRCLE_2D"));
    assertTrue(registry.containsKey("LINE_2D"));
    assertTrue(registry.containsKey("B_SPLINE_CURVE_2D"));

    // Verify count within expected range (~100-150 entities)
    assertTrue(registry.size() >= 80);
    assertTrue(registry.size() <= 400);
  }

  @Test
  void testRegistryClassesAreFinal() throws Exception {
    assertTrue(java.lang.reflect.Modifier.isFinal(GeometryRegistry1.class.getModifiers()));
    assertTrue(java.lang.reflect.Modifier.isFinal(GeometryRegistry2.class.getModifiers()));
  }
}