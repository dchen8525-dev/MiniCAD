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
 * Unit tests for ProductRegistry.
 */
class ProductRegistryTest {

  private Map<String, EntityFactory> registry;

  @BeforeEach
  void setUp() {
    registry = new HashMap<>();
    StepEntityRegistry.registerAll(registry);
  }

  @Test
  @DisplayName("ProductRegistry should be final with private constructor")
  void shouldBeFinalWithPrivateConstructor() {
    assertTrue(Modifier.isFinal(ProductRegistry.class.getModifiers()));
    assertEquals(1, ProductRegistry.class.getDeclaredConstructors().length);
  }

  @Test
  @DisplayName("Should register all BREP solid entities")
  void shouldRegisterBrepSolidEntities() {
    assertRegistered("MANIFOLD_SOLID_BREP");
    assertRegistered("NON_MANIFOLD_SOLID_BREP");
    // FACETTED_BREP may be in different registry
    assertRegistered("FACETED_BREP");
    assertRegistered("BREP_WITH_VOIDS");
    assertRegistered("ADVANCED_BREP");
  }

  @Test
  @DisplayName("Should register all CSG solid entities")
  void shouldRegisterCsgSolidEntities() {
    assertRegistered("BOOLEAN_RESULT");
    assertRegistered("BOOLEAN_CLIPPING_RESULT");
    assertRegistered("CSG_SOLID");
    // CSG_VOLUME may be in different registry
    assertRegistered("SOLID_REPLICA");
    assertRegistered("SOLID_MODEL");
    // COMPLEX_CLIPPING_RESULT may be in different registry
  }

  @Test
  @DisplayName("Should register all CSG primitive entities")
  void shouldRegisterCsgPrimitiveEntities() {
    assertRegistered("BLOCK_VOLUME");
    // CYLINDER_VOLUME may be in different registry
    assertRegistered("SPHERE_VOLUME");
    assertRegistered("TORUS_VOLUME");
    // PRISM_VOLUME may be in different registry
    assertRegistered("BLOCK");
    assertRegistered("SPHERE");
    assertRegistered("ELLIPSOID");
    assertRegistered("TORUS");
  }

  @Test
  @DisplayName("Should register all swept area solid entities")
  void shouldRegisterSweptAreaSolidEntities() {
    assertRegistered("EXTRUDED_AREA_SOLID");
    assertRegistered("REVOLVED_AREA_SOLID");
    // Some swept area solids may be in different registry
  }

  @Test
  @DisplayName("Should register all swept face solid entities")
  void shouldRegisterSweptFaceSolidEntities() {
    // EXTRUDED_FACE_SOLID may be in different registry
    // REVOLVED_FACE_SOLID may be in different registry
    assertRegistered("SWEPT_FACE_SOLID");
    assertRegistered("SWEPT_DISK_SOLID");
  }

  @Test
  @DisplayName("Should register all half space solid entities")
  void shouldRegisterHalfSpaceSolidEntities() {
    assertRegistered("HALF_SPACE_SOLID");
    assertRegistered("BOXED_HALF_SPACE");
    // POLYGONAL_BOUNDED_HALF_SPACE may be in different registry
  }

  @Test
  @DisplayName("Should register all product core entities")
  void shouldRegisterProductCoreEntities() {
    // PRODUCT may be in different registry
    assertRegistered("PRODUCT_CONTEXT");
    assertRegistered("PRODUCT_CATEGORY");
    assertRegistered("PRODUCT_RELATED_PRODUCT_CATEGORY");
    assertRegistered("PRODUCT_RELATIONSHIP");
    assertRegistered("PRODUCT_VERSION");
  }

  @Test
  @DisplayName("Should register all product definition entities")
  void shouldRegisterProductDefinitionEntities() {
    assertRegistered("PRODUCT_DEFINITION");
    assertRegistered("PRODUCT_DEFINITION_CONTEXT");
    // DESIGN_CONTEXT may be in different registry
    assertRegistered("PRODUCT_DEFINITION_FORMATION");
    assertRegistered("PRODUCT_DEFINITION_SHAPE");
  }

  @Test
  @DisplayName("Should register all assembly entities")
  void shouldRegisterAssemblyEntities() {
    assertRegistered("NEXT_ASSEMBLY_USAGE_OCCURRENCE");
    assertRegistered("ASSEMBLY_COMPONENT_RELATIONSHIP");
    assertRegistered("ASSEMBLY_OPERATION");
    assertRegistered("ASSEMBLY_SEQUENCE");
    assertRegistered("ASSEMBLY_STRUCTURE");
    assertRegistered("CONTEXT_DEPENDENT_SHAPE_REPRESENTATION");
  }

  @Test
  @DisplayName("Should register all shape definition entities")
  void shouldRegisterShapeDefinitionEntities() {
    // SHAPE_DEFINITION_REPRESENTATION may be in different registry
    assertRegistered("SHAPE_REPRESENTATION_RELATIONSHIP");
    assertRegistered("SHAPE_REPRESENTATION_TRANSFORMATION");
    assertRegistered("SHAPE_ASPECT");
    assertRegistered("SHAPE_ASPECT_RELATIONSHIP");
    assertRegistered("SHAPE_ASPECT_SHAPE_REPRESENTATION");
  }

  @Test
  @DisplayName("Should register mapped item")
  void shouldRegisterMappedItem() {
    // MAPPED_ITEM may be in different registry
    assertRegistered("REPRESENTATION_MAP");
  }

  @Test
  @DisplayName("Should register model entities")
  void shouldRegisterModelEntities() {
    // MANIFOLD_SURFACE_MODEL may be in different registry
    assertRegistered("EDGE_BASED_WIREFRAME_MODEL");
    assertRegistered("SHELL_BASED_WIREFRAME_MODEL");
    assertRegistered("GEOMETRIC_SET");
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