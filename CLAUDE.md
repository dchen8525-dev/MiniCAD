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
- `step.model`: 1115+ entity model classes
- `step.semantic`: 9 resolver/builder classes
- `step.syntax`: 3 tokenizer/parser classes
- `geometry`: 30 curve/surface classes (13 Curve3 permits, 16 SurfaceGeometry permits)
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
- Parametric surface rebuild in viewer for: plane, sphere, cylinder, cone, torus, B-Spline, rational B-Spline, surface of revolution, surface of linear extrusion, paraboloid, hyperboloid, surface of translation, surface of projection

## Key Interfaces

- `Curve3` (sealed, 13 permits): Line3, Circle, Ellipse3, Parabola3, Hyperbola3, Clothoid3, BSplineCurve3, RationalBSplineCurve3, Polyline3, TrimmedCurve3, CompositeCurve3, SurfaceCurve3, DegenerateCurve3. Key methods: `pointAt()`, `sample()`, `tangentAt()`, `closestPointTo()`, `contains()`, `length()`, `boundingBox()`, `parameterAt()`.
- `SurfaceGeometry` (sealed, 16 permits): Plane, CylindricalSurface, ConicalSurface, SphericalSurface, ToroidalSurface, BSplineSurface3, RationalBSplineSurface3, SurfaceOfLinearExtrusion3, SurfaceOfRevolution3, RuledSurface3, OffsetSurface3, SurfaceOfConstantRadius3, ParaboloidSurface, HyperboloidSurface, SurfaceOfTranslation3, SurfaceOfProjection3. Key methods: `pointAt()`, `sampleGrid()`, `normalAt()`, `closestPointTo()`, `boundingBox()`.
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

The resolver registers ~24000+ entity types via ~1635+ direct registry.put() calls. Key categories:

**Geometry/Topology (fully parsed with B-Rep generation):**
- All basic curves (LINE, CIRCLE, ELLIPSE, HYPERBOLA, PARABOLA, POLYLINE, TRIMMED_CURVE, COMPOSITE_CURVE, B_SPLINE_CURVE_WITH_KNOTS, etc.) — 13 Curve3 types
- All basic surfaces (PLANE, CYLINDRICAL_SURFACE, CONICAL_SURFACE, TOROIDAL_SURFACE, SPHERICAL_SURFACE, B_SPLINE_SURFACE_WITH_KNOTS, etc.) — 16 SurfaceGeometry types
- All topology (VERTEX_POINT, EDGE_CURVE, ORIENTED_EDGE, EDGE_LOOP, ADVANCED_FACE, CLOSED_SHELL, MANIFOLD_SOLID_BREP, etc.)
- Advanced surfaces: PARABOLOID_SURFACE, HYPERBOLOID_SURFACE, SURFACE_OF_TRANSLATION, SURFACE_OF_PROJECTION

**CSG/Swept (full B-Rep generation):**
- Boolean operations: BOOLEAN_RESULT/BOOLEAN_CLIPPING_RESULT with half-space clipping (difference/intersection/union)
- Swept solids: EXTRUDED_AREA_SOLID, REVOLVED_AREA_SOLID, SURFACE_CURVE_SWEPT_AREA_SOLID
- Extruded/revolved tapered: EXTRUDED_AREA_SOLID_TAPERED, REVOLVED_AREA_SOLID_TAPERED
- Half space: HALF_SPACE_SOLID, BOXED_HALF_SPACE, POLYGONAL_BOUNDED_HALF_SPACE
- Swept disk solid: SWEPT_DISK_SOLID
- CSG volumes: CYLINDER_VOLUME, SPHERE_VOLUME, TORUS_VOLUME, PRISM_VOLUME, CSG_SOLID
- Face-based solids: EXTRUDED_FACE_SOLID, REVOLVED_FACE_SOLID, SWEPT_FACE_SOLID
- Tessellated: TESSELLATED_FACE_SET, TESSELLATED_FACE → triangular mesh B-Rep
- Profile definitions: CIRCLE_PROFILE_DEF, RECTANGLE_PROFILE_DEF, etc.

**Assembly/Product structure:**
- NEXT_ASSEMBLY_USAGE_OCCURRENCE, CONTEXT_DEPENDENT_SHAPE_REPRESENTATION
- PRODUCT, PRODUCT_DEFINITION, PRODUCT_DEFINITION_SHAPE

**PMI/Annotation (preview support):**
- ANNOTATION_FILL_AREA, ANNOTATION_FILL_AREA_REGION, FILL_AREA_WITH_OUTLINE
- DRAUGHTING_PRE_DEFINED_COLOUR, PRE_DEFINED_COLOUR
- TERMINATOR_SYMBOL, DIMENSIONAL_EXPONENT

**Kinematic (full support):**
- 14 specific kinematic pair types (PRISMATIC_PAIR, REVOLUTE_PAIR, etc.)
- MECHANISM_STATE_REPRESENTATION, KINEMATIC_PATH

**FEA (full support):**
- VOLUME_3D_ELEMENT_REPRESENTATION, FEA_MATERIAL_PROPERTY_REPRESENTATION
- ELEMENT_VOLUME_2D, ELEMENT_VOLUME_3D, NODE_SET, ELEMENT_SET

**GD&T (full support):**
- GEOMETRIC_TOLERANCE with variants (max tolerance, defined area unit, non-uniform zone)
- DATUM_REFERENCE_MODIFIER, DATUM_REFERENCE_MODIFIER_WITH_VALUE
- RUNOUT_ZONE_DEFINITION_ORIENTATION

## Current Limitations

### Geometry Evaluation Known Constraints

- **CSG solid-solid Boolean**: `BOOLEAN_RESULT` with two bounded solids (e.g., sphere - cylinder) requires a mesh Boolean kernel — not supported. Only half-space clipping is implemented (difference/intersection/union with HALF_SPACE_SOLID or BOXED_HALF_SPACE).
- **Degenerate edges**: Zero-length edges may fail during topology construction.
- **B-Spline surface trimming**: Trimming curves on B-Spline surfaces use UV projection; complex multi-loop trims may produce artifacts.

### STEP Entity Parsing Not Yet Supported

The following STEP AP214/AP242 entity types are registered but have no B-Rep generation:

**Advanced geometry surfaces**:
- `TOROIDAL_SURFACE_WITH_SPECIFIED_BENDS`

**Advanced PMI/tolerances**:
- `GEOMETRIC_TOLERANCE_RELATIONSHIP`
- `DATUM_SYSTEM` (multi-datum combination)

**Validation property framework**:
- `VALIDATION_RESULT_REPRESENTATION`

### Industrial File Import Status

| File | solids | unsupported faces | Notes |
|---|---|---|---|
| engine.stp | 31 | 0 | 93829 entities |
| fan.stp | 1 | 0 | 41707 entities |