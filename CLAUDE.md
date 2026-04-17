# CLAUDE.md

This file provides guidance to Claude Code when working with code in this repository.

## Build and Test Commands

```bash
mvn test                                    # Run all tests
mvn -q test                                 # Quieter test run
mvn clean test                              # Rebuild from scratch
mvn exec:java -Dexec.args="examples/minimal-square.step"  # CLI demo on a STEP file
mvn exec:java -Dexec.mainClass=com.minicad.app.StepViewerApp exec:java  # Start web viewer at http://127.0.0.1:8080
```

## Architecture Overview

Industrial-oriented CAD kernel and STEP parser with comprehensive AP214/AP242 entity type coverage.

**Project goals**:
- Complete STEP (ISO 10303) file parsing for all mainstream entity types
- Full B-Rep (Boundary Representation) topology support
- Web-based 3D model preview capability
- Industrial-grade compatibility with STEP files from CATIA, NX, SolidWorks, Creo, etc.

```
STEP text → syntax (StepTokenizer → StepParser → StepFile)
         → semantic model (StepEntityResolver → 1062 StepXxx model classes)
         → internal geometry/topology (Curve3, SurfaceGeometry, Vertex, Edge, Face, Shell, Solid)
         → preview/export (StepPreviewJsonExporter → browser Three.js)
```

**Layer sizes:**
- `step.model`: 1062 entity model classes
- `step.semantic`: 9 resolver/builder classes
- `step.syntax`: 3 tokenizer/parser classes
- `geometry`: 30 curve/surface classes
- `geometry2d`: 16 2D parametric domain classes
- `topology`: 11 B-Rep classes

**Key packages:**
- `step.syntax`: StepTokenizer and StepParser produce raw AST. No semantic interpretation.
- `step.model`: ~1062 immutable record classes for resolved STEP entities. All implement `StepEntity`.
- `step.semantic`: StepEntityResolver maps raw definitions to model classes (~24000+ entity types registered).
- `geometry`/`geometry2d`: 3D/2D geometry types with sealed interfaces and default sampling methods.
- `topology`: B-Rep topology (Vertex, Edge, OrientedEdge, EdgeLoop, FaceBound, Face, Shell, Solid).

**Viewer architecture:**
- Java resolves STEP and exports preview JSON via StepPreviewJsonExporter
- Browser (`src/main/resources/static`) renders with Three.js
- No server-side triangulation; mesh generation in browser JavaScript

## Key Interfaces

- `Curve3` (sealed): Line3, Circle, Ellipse3, BSplineCurve3, Polyline3, TrimmedCurve3, CompositeCurve3, etc. Default methods for `boundingBox()`, `sample()`, `length()`, `tangentAt()`.
- `SurfaceGeometry` (sealed): Plane, CylindricalSurface, ConicalSurface, SphericalSurface, ToroidalSurface, BSplineSurface3, etc. Default methods for `boundingBox()`, `sampleGrid()`, `normalAt()`.
- `StepEntity`: marker interface for all resolved STEP model classes.

## Testing Patterns

Tests mirror main package layout under `src/test/java/`. Use inline STEP snippets for parser/resolver tests unless a reusable file in `examples/` is clearer. Cover:
- Forward references, missing references, illegal syntax
- Unsupported entities/forms, partial-support boundaries
- Both happy path and failure path

## Important Constraints

- Do NOT claim full STEP/AP242 support for all entity types.
- Unsupported behavior must fail explicitly with clear exceptions (`UnsupportedStepEntityException`, `UnsupportedGeometryException`), not silently.
- If a feature approaches industrial CAD kernel complexity (Boolean operations, healing, tolerance propagation), document limitations explicitly.
- Keep classes small and explicit; avoid deep inheritance or generic frameworks.
- Use Java 21 `record` where immutability is natural.

## STEP Entity Coverage

The resolver registers ~24000+ entity types via ~1559 direct registry.put() calls. Key categories:

**Geometry/Topology (fully parsed):**
- All basic curves (LINE, CIRCLE, ELLIPSE, HYPERBOLA, PARABOLA, POLYLINE, TRIMMED_CURVE, COMPOSITE_CURVE, B_SPLINE_CURVE_WITH_KNOTS, etc.)
- All basic surfaces (PLANE, CYLINDRICAL_SURFACE, CONICAL_SURFACE, TOROIDAL_SURFACE, SPHERICAL_SURFACE, B_SPLINE_SURFACE_WITH_KNOTS, etc.)
- All topology (VERTEX_POINT, EDGE_CURVE, ORIENTED_EDGE, EDGE_LOOP, ADVANCED_FACE, CLOSED_SHELL, MANIFOLD_SOLID_BREP, etc.)

**Assembly/Product structure:**
- NEXT_ASSEMBLY_USAGE_OCCURRENCE, CONTEXT_DEPENDENT_SHAPE_REPRESENTATION
- PRODUCT, PRODUCT_DEFINITION, PRODUCT_DEFINITION_SHAPE

**CSG/Swept (parsed but limited geometric evaluation):**
- BOOLEAN_RESULT, CSG_SOLID, EXTRUDED_AREA_SOLID, REVOLVED_AREA_SOLID
- Profile definitions (CIRCLE_PROFILE_DEF, RECTANGLE_PROFILE_DEF, etc.)

## Current Limitations

### Geometry Evaluation Not Implemented

These entities are fully parsed but geometric evaluation is not performed:
- **CSG Boolean operations**: `BOOLEAN_RESULT`, `BOOLEAN_CLIPPING_RESULT`, `COMPLEX_CLIPPING_RESULT` parsed but no geometric boolean evaluation
- **Swept solids**: `EXTRUDED_AREA_SOLID`, `REVOLVED_AREA_SOLID`, `SURFACE_CURVE_SWEPT_AREA_SOLID` parsed but no B-Rep generation
- **Half space**: `HALF_SPACE_SOLID`, `BOXED_HALF_SPACE`, `POLYGONAL_BOUNDED_HALF_SPACE` parsed but no clipping evaluation
- **Tessellated geometry**: `TESSELLATED_FACE_SET`, `TESSELLATED_FACE`, `TESSELLATED_TRIANGLE` parsed but not converted to analytical geometry

### STEP Entity Parsing Not Yet Supported

The following STEP AP214/AP242 entity types are not yet implemented:

**Advanced geometry surfaces**:
- `SURFACE_OF_TRANSLATION`, `SURFACE_OF_PROJECTION`
- `PARABOLOID_SURFACE`, `HYPERBOLOID_SURFACE`
- `TOROIDAL_SURFACE_WITH_SPECIFIED_BENDS`

**Advanced swept solids**:
- `EXTRUDED_FACE_SOLID`, `REVOLVED_FACE_SOLID`
- `SURFACE_CURVE_SWEPT_FACE_SOLID`

**Advanced CSG primitives**:
- `CYLINDER_VOLUME`, `SPHERE_VOLUME`, `TORUS_VOLUME`
- `RIGHT_CIRCULAR_CYLINDER_VOLUME`, `RIGHT_CIRCULAR_CONE_VOLUME`
- `PRISM_VOLUME`

**Advanced PMI/tolerances**:
- `GEOMETRIC_TOLERANCE_RELATIONSHIP`
- `DATUM_SYSTEM` (multi-datum combination)
- `PROJECTED_ZONE_DEFINITION`, `NON_UNIFORM_ZONE_DEFINITION`

**Validation property framework**:
- `VALIDATION_PROPERTY_REPRESENTATION`, `VALIDATION_RESULT_REPRESENTATION`
- `CALCULATED_GEOMETRIC_REPRESENTATION_ITEM`

**Kinematic (partial support, these not implemented)**:
- `KINEMATIC_PATH`, `KINEMATIC_FRAME_BASED_TRANSFORMATION`

**Finite element/mesh (partial support, these not implemented)**:
- `ELEMENT_VOLUME_2D`, `ELEMENT_VOLUME_3D`
- `NODE_SET`, `ELEMENT_SET`