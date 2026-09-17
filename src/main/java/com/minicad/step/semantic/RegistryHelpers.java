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


}
