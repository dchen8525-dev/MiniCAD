package com.minicad.step.semantic;

import java.util.Map;

/**
 * Helper methods for batch registering entity type aliases.
 * Extracted from MiscRegistry for reuse across all registry classes.
 * Provides batch registration methods for common entity patterns.
 */
public final class RegistryHelpers {

  private RegistryHelpers() {}

  /**
   * Registers geometric tolerance entity aliases.
   * Each entity uses resolveGeometricTolerance resolver method.
   */
  public static void registerGeometricToleranceAliases(
      Map<String, EntityFactory> registry, String... entityNames) {
    for (String entityName : entityNames) {
      registry.put(
          entityName, (resolver, instance) -> resolver.resolveGeometricTolerance(instance, entityName));
    }
  }

  /**
   * Registers shape aspect entity aliases.
   * Each entity uses resolveShapeAspect resolver method.
   */
  public static void registerShapeAspectAliases(
      Map<String, EntityFactory> registry, String... entityNames) {
    for (String entityName : entityNames) {
      registry.put(
          entityName, (resolver, instance) -> resolver.resolveShapeAspect(instance, entityName));
    }
  }

  /**
   * Registers shape aspect occurrence entity aliases.
   * Each entity uses resolveShapeAspectOccurrence resolver method.
   */
  public static void registerShapeAspectOccurrenceAliases(
      Map<String, EntityFactory> registry, String... entityNames) {
    for (String entityName : entityNames) {
      registry.put(
          entityName,
          (resolver, instance) -> resolver.resolveShapeAspectOccurrence(instance, entityName));
    }
  }

  /**
   * Registers characterized object entity aliases.
   * Each entity uses resolveCharacterizedObject resolver method.
   */
  public static void registerCharacterizedObjectAliases(
      Map<String, EntityFactory> registry, String... entityNames) {
    for (String entityName : entityNames) {
      registry.put(
          entityName,
          (resolver, instance) -> resolver.resolveCharacterizedObject(instance, entityName));
    }
  }

  /**
   * Registers externally defined item entity aliases.
   * Each entity uses resolveExternallyDefinedItem resolver method.
   */
  public static void registerExternallyDefinedItemAliases(
      Map<String, EntityFactory> registry, String... entityNames) {
    for (String entityName : entityNames) {
      registry.put(
          entityName,
          (resolver, instance) -> resolver.resolveExternallyDefinedItem(instance, entityName));
    }
  }

  /**
   * Registers shape aspect relationship entity aliases.
   * Each entity uses resolveShapeAspectRelationship resolver method.
   */
  public static void registerShapeAspectRelationshipAliases(
      Map<String, EntityFactory> registry, String... entityNames) {
    for (String entityName : entityNames) {
      registry.put(
          entityName,
          (resolver, instance) ->
              resolver.resolveShapeAspectRelationship(instance, entityName));
    }
  }

  /**
   * Registers representation relationship entity aliases.
   * Each entity uses resolveRepresentationRelationship resolver method.
   */
  public static void registerRepresentationRelationshipAliases(
      Map<String, EntityFactory> registry, String... entityNames) {
    for (String entityName : entityNames) {
      registry.put(
          entityName,
          (resolver, instance) ->
              resolver.resolveRepresentationRelationship(instance, entityName));
    }
  }

  /**
   * Registers a typed measure with unit entity.
   * Uses resolveTypedMeasureWithUnit resolver method with expected unit kind.
   */
  public static void registerTypedMeasureWithUnit(
      Map<String, EntityFactory> registry, String entityName, String expectedUnitKind) {
    registry.put(
        entityName,
        (resolver, instance) ->
            resolver.resolveTypedMeasureWithUnit(instance, entityName, expectedUnitKind));
  }

  /**
   * Registers typed measure with unit pairs.
   * For each unit kind, derives measure name and registers pair.
   */
  public static void registerTypedMeasureWithUnitPairs(
      Map<String, EntityFactory> registry, String... unitKinds) {
    for (String unitKind : unitKinds) {
      String measureName = unitKind.replace("_UNIT", "_MEASURE_WITH_UNIT");
      registerTypedMeasureWithUnit(registry, measureName, unitKind);
    }
  }

  /**
   * Registers standalone derived unit kind entities.
   * Each entity uses resolveStandaloneDerivedUnitKind resolver method.
   */
  public static void registerStandaloneDerivedUnitKinds(
      Map<String, EntityFactory> registry, String... unitKinds) {
    for (String unitKind : unitKinds) {
      registry.put(
          unitKind,
          (resolver, instance) -> resolver.resolveStandaloneDerivedUnitKind(instance, unitKind));
    }
  }

  /**
   * Registers kinematic pair entity aliases.
   * Each entity uses resolveKinematicPair resolver method.
   */
  public static void registerKinematicPairAliases(
      Map<String, EntityFactory> registry, String... entityNames) {
    for (String entityName : entityNames) {
      registry.put(entityName,
          (resolver, instance) -> resolver.resolveKinematicPair(instance, entityName));
    }
  }

  /**
   * Registers FEA entity aliases.
   * Each entity uses resolveRepresentation resolver method (not shape representation).
   */
  public static void registerFeaAliases(
      Map<String, EntityFactory> registry, String... entityNames) {
    for (String entityName : entityNames) {
      registry.put(entityName,
          (resolver, instance) -> resolver.resolveRepresentation(instance, entityName, false));
    }
  }

  /**
   * Registers representation entity aliases.
   * Each entity uses resolveRepresentation resolver method.
   */
  public static void registerRepresentationAliases(
      Map<String, EntityFactory> registry, boolean shapeRepresentation, String... entityNames) {
    for (String entityName : entityNames) {
      registry.put(entityName,
          (resolver, instance) ->
              resolver.resolveRepresentation(instance, entityName, shapeRepresentation));
    }
  }

  /**
   * Registers product definition relationship entity aliases.
   * Each entity uses resolveProductDefinitionRelationship resolver method.
   */
  public static void registerProductDefinitionRelationshipAliases(
      Map<String, EntityFactory> registry, String... entityNames) {
    for (String entityName : entityNames) {
      registry.put(entityName,
          (resolver, instance) ->
              resolver.resolveProductDefinitionRelationship(instance, entityName));
    }
  }

  /**
   * Registers product definition relationship relationship entity aliases.
   * Each entity uses resolveProductDefinitionRelationshipRelationship resolver method.
   */
  public static void registerProductDefinitionRelationshipRelationshipAliases(
      Map<String, EntityFactory> registry, String... entityNames) {
    for (String entityName : entityNames) {
      registry.put(entityName,
          (resolver, instance) ->
              resolver.resolveProductDefinitionRelationshipRelationship(instance, entityName));
    }
  }
}