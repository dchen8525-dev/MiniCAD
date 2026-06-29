# Registry Package

This package contains specialized registry classes for STEP entity type registration.

## Registry Classes

Each registry class handles a specific domain of STEP entities:

- GeometryRegistry - curves, surfaces, points, placements
- TopologyRegistry - shells, faces, edges, loops
- ProductRegistry - BREP, CSG, assemblies, swept solids
- RepresentationRegistry - all representation types
- ManufacturingRegistry - manufacturing features, operations
- ToleranceRegistry - GD&T, PMI, dimensions, datums
- UnitRegistry - units, measures, conversions
- AnnotationRegistry - annotations, styles, colors
- ClassificationRegistry - approvals, certifications, organizations
- KinematicRegistry - kinematic structures, pairs, mechanisms
- FeaRegistry - FEA, mesh, element properties
- ProfileRegistry - profile definitions
- ConfigManagementRegistry - configuration, change control
- MiscellaneousRegistry - catch-all for remaining entities

## Architecture

All registries are called by `StepEntityRegistry` (aggregator class in parent package).

Each registry follows the pattern:
```java
public final class XxxRegistry {
    private XxxRegistry() {}
    
    public static void register(Map<String, EntityFactory> registry) {
        // registry.put() calls for domain entities
    }
}
```