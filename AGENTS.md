# Repository Guidelines

## Project Structure & Module Organization

- `src/main/java/com/minicad/common`: shared exceptions, epsilon, validation helpers (7 classes)
- `src/main/java/com/minicad/geometry`: 3D geometry types - `Curve3`, `SurfaceGeometry`, `CartesianPoint`, `Vector3`, `Direction3`, `BoundingBox3`, `Transformation3`, `Axis2Placement3D` and concrete implementations (30 classes)
- `src/main/java/com/minicad/geometry2d`: 2D parametric domain geometry - `Point2`, `Vector2`, `Direction2`, `BoundingBox2`, `Curve2` and concrete implementations (16 classes)
- `src/main/java/com/minicad/topology`: B-Rep topology - `Vertex`, `Edge`, `OrientedEdge`, `EdgeLoop`, `VertexLoop`, `PolyLoop`, `FaceBound`, `Face`, `Shell`, `Solid` (11 classes)
- `src/main/java/com/minicad/step/syntax`: STEP tokenizer, parser, and raw AST (3 classes)
- `src/main/java/com/minicad/step/model`: resolved STEP semantic records (1062 classes)
- `src/main/java/com/minicad/step/semantic`: resolver and CAD object builder (9 classes)
- `src/main/java/com/minicad/app`: CLI entry points, preview exporter, and Jetty-based local viewer (5 classes)
- `src/main/resources/static`: viewer HTML/JS and vendored Three.js assets
- `src/test/java`: JUnit 5 tests mirroring the main package layout
- `examples`: STEP sample files (44 files)

## Build, Test, and Development Commands

- `mvn test`: compile and run the full JUnit 5 suite
- `mvn -q test`: quieter test run for quick checks
- `mvn exec:java -Dexec.args="examples/minimal-square.step"`: run CLI demo on example STEP file
- `mvn exec:java -Dexec.args="examples/engine.stp"`: run CLI demo on complex model
- `mvn "-Dexec.mainClass=com.minicad.app.StepViewerApp" exec:java`: start local Jetty viewer on `http://127.0.0.1:8080`
- `mvn clean test`: rebuild from scratch when changing parser/model code

## Coding Style & Naming Conventions

- Use Java 21 features, especially `record` where immutability is natural
- Keep indentation at 4 spaces with standard Java brace style
- Prefer small, explicit classes over generic frameworks or deep inheritance
- Name tests after the class under test (e.g., `StepParserTest`, `FaceTest`)
- Use clear exception messages; unsupported behavior must fail explicitly

## Testing Guidelines

- Framework: JUnit 5 only
- Cover both happy path and failure path
- For STEP work: forward references, missing references, illegal syntax, unsupported entities/forms
- Keep tests deterministic and small; prefer inline STEP snippets unless reusable files under `examples/` are clearer
- When adding preview/export behavior, test both rendered counts and `unsupportedFaceCount`

## Commit & Pull Request Guidelines

- Use conventional commit style:
  - `feat: add STEP resolver for EDGE_LOOP`
  - `test: cover duplicate entity ids`
  - `docs: update README`
- Keep each commit focused on one logical change
- PRs should include: purpose, scope, commands run (`mvn test`), and intentional unsupported behavior

## Architecture Notes

- Preserve layering: `syntax -> semantic model -> internal geometry/topology`
- Preserve viewer split: Java resolves and exports preview data; browser only renders exported JSON
- ~24000+ STEP entity types registered via ~1559 registry.put() calls
- 1062 `StepXxx` model classes in `step.model`
- `ADVANCED_FACE` resolution accepts PLANE, CYLINDRICAL_SURFACE, CONICAL_SURFACE, TOROIDAL_SURFACE, SPHERICAL_SURFACE, B_SPLINE_SURFACE_WITH_KNOTS
- Web preview supports planar faces, cylindrical/conical/toroidal/spherical patches, and some B-spline surfaces
- CSG/Swept entities are parsed but geometric evaluation is limited

## Current STEP Entity Coverage

The resolver handles these categories with full parsing support:

**Geometry (1062 model classes):**
- Basic curves: LINE, CIRCLE, ELLIPSE, HYPERBOLA, PARABOLA, POLYLINE, TRIMMED_CURVE, COMPOSITE_CURVE, B_SPLINE_CURVE_WITH_KNOTS, RATIONAL_B_SPLINE_CURVE, SURFACE_CURVE, SEAM_CURVE, PCURVE, OFFSET_CURVE_3D
- Basic surfaces: PLANE, CYLINDRICAL_SURFACE, CONICAL_SURFACE, TOROIDAL_SURFACE, SPHERICAL_SURFACE, B_SPLINE_SURFACE_WITH_KNOTS, RATIONAL_B_SPLINE_SURFACE, SURFACE_OF_LINEAR_EXTRUSION, SURFACE_OF_REVOLUTION, OFFSET_SURFACE

**Topology:**
- VERTEX_POINT, EDGE_CURVE, ORIENTED_EDGE, EDGE_LOOP, VERTEX_LOOP, POLY_LOOP, FACE_BOUND, ADVANCED_FACE, CLOSED_SHELL, OPEN_SHELL, MANIFOLD_SOLID_BREP, BREP_WITH_VOIDS

**Assembly:**
- NEXT_ASSEMBLY_USAGE_OCCURRENCE, CONTEXT_DEPENDENT_SHAPE_REPRESENTATION, MAPPED_ITEM, ITEM_DEFINED_TRANSFORMATION

**Product/Metadata:**
- PRODUCT, PRODUCT_DEFINITION, PERSON, ORGANIZATION, APPROVAL, CERTIFICATION

## Limitations to Document

### Geometry Evaluation Not Implemented

These entities are fully parsed but geometric evaluation is not performed:
- CSG Boolean operations: `BOOLEAN_RESULT`, `BOOLEAN_CLIPPING_RESULT`, `COMPLEX_CLIPPING_RESULT`
- Swept solids: `EXTRUDED_AREA_SOLID`, `REVOLVED_AREA_SOLID`, `SURFACE_CURVE_SWEPT_AREA_SOLID`
- Half space: `HALF_SPACE_SOLID`, `BOXED_HALF_SPACE`, `POLYGONAL_BOUNDED_HALF_SPACE`
- Tessellated geometry: `TESSELLATED_FACE_SET`, `TESSELLATED_FACE`, `TESSELLATED_TRIANGLE`

### STEP Entity Parsing Not Yet Supported

**Advanced geometry surfaces**:
- `SURFACE_OF_TRANSLATION`, `SURFACE_OF_PROJECTION`
- `PARABOLOID_SURFACE`, `HYPERBOLOID_SURFACE`
- `TOROIDAL_SURFACE_WITH_SPECIFIED_BENDS`

**Advanced swept solids**:
- `EXTRUDED_FACE_SOLID`, `REVOLVED_FACE_SOLID`
- `SURFACE_CURVE_SWEPT_FACE_SOLID`

**Advanced CSG primitives**:
- `CYLINDER_VOLUME`, `SPHERE_VOLUME`, `TORUS_VOLUME`
- `RIGHT_CIRCULAR_CYLINDER_VOLUME`, `RIGHT_CIRCULAR_CONE_VOLUME`, `PRISM_VOLUME`

**Advanced PMI/tolerances**:
- `GEOMETRIC_TOLERANCE_RELATIONSHIP`, `DATUM_SYSTEM`
- `PROJECTED_ZONE_DEFINITION`, `NON_UNIFORM_ZONE_DEFINITION`

**Validation property framework**:
- `VALIDATION_PROPERTY_REPRESENTATION`, `VALIDATION_RESULT_REPRESENTATION`
- `CALCULATED_GEOMETRIC_REPRESENTATION_ITEM`

**Kinematic (partial)**: `KINEMATIC_PATH`, `KINEMATIC_FRAME_BASED_TRANSFORMATION`

**Finite element (partial)**: `ELEMENT_VOLUME_2D`, `ELEMENT_VOLUME_3D`, `NODE_SET`, `ELEMENT_SET`