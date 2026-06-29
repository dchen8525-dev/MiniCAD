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
 * Unit tests for ClassificationRegistry.
 */
class ClassificationRegistryTest {

  private Map<String, EntityFactory> registry;

  @BeforeEach
  void setUp() {
    registry = new HashMap<>();
    ClassificationRegistry.register(registry);
  }

  @Test
  @DisplayName("ClassificationRegistry should be final with private constructor")
  void shouldBeFinalWithPrivateConstructor() {
    assertTrue(Modifier.isFinal(ClassificationRegistry.class.getModifiers()));
    assertEquals(1, ClassificationRegistry.class.getDeclaredConstructors().length);
  }

  @Test
  @DisplayName("Should register address entities")
  void shouldRegisterAddressEntities() {
    assertRegistered("ADDRESS");
    assertRegistered("ORGANIZATION_ADDRESS");
    assertRegistered("PERSON_ADDRESS");
  }

  @Test
  @DisplayName("Should register person and organization entities")
  void shouldRegisterPersonAndOrganizationEntities() {
    assertRegistered("PERSON");
    assertRegistered("ORGANIZATION");
    assertRegistered("PERSON_AND_ORGANIZATION");
  }

  @Test
  @DisplayName("Should register date entities")
  void shouldRegisterDateEntities() {
    assertRegistered("CALENDAR_DATE");
    assertRegistered("DATE_AND_TIME");
    assertRegistered("DATE_ASSIGNMENT");
  }

  @Test
  @DisplayName("Should register approval entities")
  void shouldRegisterApprovalEntities() {
    assertRegistered("APPROVAL");
    assertRegistered("APPROVAL_STATUS");
    assertRegistered("APPROVAL_ROLE");
    assertRegistered("APPROVAL_ASSIGNMENT");
  }

  @Test
  @DisplayName("Should register certification entities")
  void shouldRegisterCertificationEntities() {
    assertRegistered("CERTIFICATION");
    assertRegistered("CERTIFICATION_TYPE");
  }

  @Test
  @DisplayName("Should register security classification entities")
  void shouldRegisterSecurityClassificationEntities() {
    assertRegistered("SECURITY_CLASSIFICATION");
    assertRegistered("SECURITY_CLASSIFICATION_LEVEL");
  }

  @Test
  @DisplayName("Should register document entities")
  void shouldRegisterDocumentEntities() {
    assertRegistered("DOCUMENT");
    assertRegistered("DOCUMENT_RELATIONSHIP");
    assertRegistered("EXTERNAL_SOURCE");
  }

  @Test
  @DisplayName("Total classification entity count should be reasonable")
  void totalEntityCountShouldBeReasonable() {
    assertTrue(registry.size() >= 30,
        "Expected at least 30 classification entities, got " + registry.size());
    assertTrue(registry.size() <= 50,
        "Expected at most 50 classification entities, got " + registry.size());
  }

  private void assertRegistered(String entityName) {
    EntityFactory factory = registry.get(entityName);
    assertNotNull(factory, entityName + " should be registered");
  }
}