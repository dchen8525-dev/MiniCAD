package com.minicad.step.semantic;

import java.util.Map;

/**
 * Main registry that aggregates all entity type registries.
 * Replaces the monolithic MiscRegistry (8444 lines) with modular registries.
 *
 * All registries are now < 1000 lines each, extracted from MiscRegistry.java.
 */
public final class StepEntityRegistry {

  private StepEntityRegistry() {}

  /**
   * Registers all STEP entity factories by calling specialized registries.
   *
   * @param registry the entity factory registry to populate
   */
  public static void registerAll(Map<String, EntityFactory> registry) {
    // Geometry entities (split into 2 files)
    GeometryRegistry1.register(registry);
    GeometryRegistry2.register(registry);

    // Topology entities
    TopologyRegistry.register(registry);

    // Product entities (BREP, CSG, assemblies)
    ProductRegistry.register(registry);

    // Representation entities (split into 2 files)
    RepresentationRegistry1.register(registry);
    RepresentationRegistry2.register(registry);

    // Profile entities
    ProfileRegistry.register(registry);

    // Tolerance/GD&T entities
    ToleranceRegistry.register(registry);

    // Annotation/Presentation entities
    AnnotationRegistry.register(registry);

    // Unit/Measure entities
    UnitRegistry.register(registry);

    // Manufacturing features
    ManufacturingRegistry.register(registry);

    // Kinematic entities
    KinematicRegistry.register(registry);

    // FEA entities
    FeaRegistry.register(registry);

    // Configuration management entities
    ConfigManagementRegistry.register(registry);

    // Classification entities
    ClassificationRegistry.register(registry);

    // Miscellaneous entities (split into 4 files)
    MiscellaneousRegistry1.register(registry);
    MiscellaneousRegistry2.register(registry);
    MiscellaneousRegistry3.register(registry);
    MiscellaneousRegistry4.register(registry);
  }
}