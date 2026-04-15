# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build and Test Commands

```bash
mvn test                                    # Run all tests
mvn -q test                                 # Quieter test run
mvn clean test                              # Rebuild from scratch
mvn exec:java -Dexec.args="examples/minimal-square.step"  # CLI demo on a STEP file
mvn exec:java -Dexec.mainClass=com.minicad.app.StepViewerApp exec:java  # Start web viewer at http://127.0.0.1:8080
```

## Architecture Overview

This is a teaching-oriented minimal CAD kernel and STEP parser. The code follows a strict layered architecture:

```
STEP text → syntax (StepTokenizer → StepParser → StepFile)
         → semantic model (StepEntityResolver → ~430 StepXxx model classes)
         → internal geometry/topology (Curve3, SurfaceGeometry, Vertex, Edge, Face, Shell, Solid)
         → preview/export (StepPreviewJsonExporter → browser Three.js)
```

**Key layers:**

- **syntax** (`step.syntax`): StepTokenizer and StepParser produce raw AST (StepFile, StepEntityInstance, StepValue). No semantic interpretation here.
- **semantic model** (`step.model`): ~430 immutable record classes representing resolved STEP entities. All implement `StepEntity` interface.
- **semantic resolution** (`step.semantic`): StepEntityResolver maps raw entity definitions to model classes via a large registry (~24000+ entity types registered).
- **geometry** (`geometry`, `geometry2d`): Minimal 3D/2D geometry types. `Curve3` and `SurfaceGeometry` are sealed interfaces with default sampling/bounding box methods.
- **topology** (`topology`): B-Rep topology (Vertex, Edge, OrientedEdge, EdgeLoop, FaceBound, Face, Shell, Solid). All validated on construction.

**Viewer architecture:**

- Java resolves STEP and exports preview JSON via StepPreviewJsonExporter
- Browser (`src/main/resources/static`) renders exported JSON with Three.js
- No server-side triangulation; all mesh generation is in browser JavaScript

## Key Interfaces

- `Curve3` (sealed): Line3, Circle, Ellipse3, BSplineCurve3, Polyline3, TrimmedCurve3, CompositeCurve3, etc. Default methods for `boundingBox()`, `sample()`, `length()`, `tangentAt()`.
- `SurfaceGeometry` (sealed): Plane, CylindricalSurface, ConicalSurface, SphericalSurface, ToroidalSurface, BSplineSurface3, etc. Default methods for `boundingBox()`, `sampleGrid()`, `normalAt()`.
- `StepEntity`: marker interface for all resolved STEP model classes.

## Testing Patterns

Tests mirror the main package layout under `src/test/java/`. Use inline STEP snippets for parser/resolver tests unless a reusable file in `examples/` is clearer. Cover:
- Forward references, missing references, illegal syntax
- Unsupported entities/forms, partial-support boundaries
- Both happy path and failure path

## Important Constraints

- This is **not** an industrial CAD kernel. Never claim full STEP support.
- Unsupported behavior must fail explicitly with clear exceptions (`UnsupportedStepEntityException`, `UnsupportedGeometryException`), not silently.
- If a feature approaches industrial CAD complexity, narrow scope and document the limitation.
- Keep classes small and explicit; avoid deep inheritance or generic frameworks.
- Use Java 21 `record` where immutability is natural.