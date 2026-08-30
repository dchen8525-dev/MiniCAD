# MiniCAD

Industrial-oriented CAD kernel and STEP parser in Java.

A from-scratch, minimal CAD geometry/topology kernel that reads STEP files
(focus on STEP AP242), builds a semantic CAD model, and produces a preview
mesh usable by the bundled browser viewer.

## Features

- **STEP parser** — syntax parsing (ANTLR4) and semantic resolution for CAD
  entities: geometry, topology (shells, faces, loops, edges), B-rep, PMI,
  assemblies, and placements.
- **Geometry kernel** — 3D curves and surfaces: lines, circles, ellipses,
  B-spline / NURBS curves and surfaces (De Boor evaluation), trimmed curves,
  planes, ruled/extruded/revolution surfaces, bounding boxes.
- **Topology & validation** — solid/face/loop/edge model with bounding-box
  and validity checks.
- **Preview pipeline** — tessellation into a mesh with UV mapping, exported to
  JSON (fastjson2) for rendering.
- **Browser viewer** — embedded Jetty web app serving a Three.js viewer
  (`src/main/resources/static`).
- **EXPRESS code generation** — `tools` package generates Java model classes and
  resolver methods from an EXPRESS schema, plus a capability scanner/report.
- **Samples** — real-world `.step` files under `samples/` used as regression
  fixtures.

## Requirements

- JDK 17+ (Jetty 12 requires it; the build itself targets bytecode 17)
- Maven 3.9+ (or use the bundled wrapper: `./mvnw` / `mvnw.cmd`, pinned to 3.9.16)

## Build & test

```sh
mvn test            # compile + run the full test suite (2047 tests, includes the JaCoCo gate)
mvn verify          # additionally runs forbiddenapis and the Spotless check
mvn package         # build the jar
```

With the wrapper: `./mvnw test` (Linux/macOS) or `mvnw.cmd test` (Windows).

Formatting: `mvn spotless:apply` to fix; `spotless:check` runs in the
`verify` phase and fails the build on unformatted hand-written sources.

The test suite covers geometry evaluation, STEP parsing/semantics, topology,
preview, and the web apps. Coverage results land in `target/site/jacoco`.

## Running the apps

Every app is launched via `mvn exec:java`. Entry points live in
`src/main/java/com/minicad/app/`:

| Class | Purpose |
|-------|---------|
| `StepDumpApp` | Dump a parsed STEP model as text/JSON (default main class) |
| `StepViewerApp` | Serve the browser 3D viewer on an embedded Jetty server |
| `StepBenchmarkApp` | Benchmark parse/evaluate throughput |
| `StepCapabilityReportApp` | Report which STEP entity/capability types are supported |
| `Main` | Small entry-point used by tooling |

Example:

```sh
mvn -q exec:java -Dexec.mainClass=com.minicad.app.StepDumpApp -Dexec.args=samples/bspline-patch.step
```

## Project layout

```
src/main/java/com/minicad/
  app/          application entry points (dump, viewer, benchmark, report)
  builder/      semantic model builders from parsed STEP
  common/       shared primitives, epsilon, preconditions, exceptions
  export/       JSON / preview export
  geometry/     3D curves and surfaces (B-spline, NURBS, trimmed, …)
  geometry2d/   2D geometry used in P-curves and planar loops
  helper/       unit extraction, math utilities
  preview/      tessellation and UV mapping for preview mesh
  step/         STEP syntax (ANTLR) and semantic resolution
  tool/         EXPRESS schema → Java code generation (see below)
  topology/     solids, faces, loops, edges, validation
```

### The `tool` code-generation package

`com.minicad.tool` is a development-time CLI, not part of the runtime
pipeline: `ExpressSchemaParser` reads an EXPRESS schema catalog,
`CapabilityScanner`/`EntityPrioritizer` inventory entity coverage, and
`ModelClassGenerator`/`ResolverMethodGenerator` emit the 1265 generated
classes in `com.minicad.step.model` plus their resolver dispatch table.
The generated sources are checked in and excluded from coverage and
formatting gates; the generator itself has no unit tests (it is exercised
by regenerating and diffing). Entry points expose `main` methods and are
run ad hoc, e.g.:

```sh
mvn -q exec:java -Dexec.mainClass=com.minicad.tool.ModelClassGenerator
```

## Documentation

- `docs/superpowers/` — design docs and implementation plans.
- `docs/archive/` — archived refactoring / analysis reports (historical).

## Repository hygiene

- `.workbuddy/`, `target/` are developer-local and ignored.
- Reports from earlier refactoring sessions are archived under `docs/archive/`.