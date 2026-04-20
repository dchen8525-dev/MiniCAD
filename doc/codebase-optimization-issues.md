# MiniCAD Codebase Optimization Issue Breakdown

## Usage

This document expands the optimization plan into issue-sized work items. Each issue is designed to fit roughly within `1-2` days of focused work.

Each issue includes:

- objective
- concrete change scope
- refactoring approach
- implementation boundaries
- validation steps
- expected payoff

The issues are grouped by `P0 / P1 / P2`, but within each priority they are also ordered for execution.

## Current Progress

Last updated: `2026-04-20`

Completed:

- `P0-2` resolver direct lookup
- `P0-5` compiled pipeline object
- `P0-6` preview exporter compiled-state reuse
- `P0-7` mesh exporter compiled-state reuse
- `P0-8` unsafe mesh parallelism removal
- `P0-9` deterministic mesh regression coverage

In progress:

- `P1` `StepCadBuilder` split has started with a first helper extraction:
  - [StepCadGeometryOps.java](/root/work/MiniCAD/src/main/java/com/minicad/step/semantic/StepCadGeometryOps.java)
  - scope extracted so far: curve sampling, trimmed-curve helpers, loop helpers, replica/surface transformation helpers

Recommended next issue:

- extract trim-value and trim-parameter resolution from `StepCadBuilder`
- then continue toward topology-focused extraction

---

## P0 Issues

## P0-1: Add benchmark baselines for resolver/export hotspots

### Objective

Create a stable before/after baseline for the optimization work that follows.

### Scope

- benchmark `examples/minimal-square.step`
- benchmark `examples/fan.stp`
- benchmark `examples/engine.stp`
- capture `parse`, `resolve`, `build`, `preview export`, `mesh export`

### Refactoring approach

Do not change runtime behavior yet. Tighten the diagnostic workflow first.

Use the existing `StepBenchmarkApp` as the source of truth, but document one fixed baseline run format so later issues can compare against the same numbers.

### Implementation notes

- Confirm current benchmark output format is sufficient
- If needed, add a short markdown section in `doc/` with baseline commands and sample outputs
- Keep this issue purely diagnostic

### Boundaries

- no production logic changes
- no parser/resolver changes
- no exporter refactor yet

### Validation

```bash
mvn -q exec:java -Dexec.mainClass=com.minicad.app.StepBenchmarkApp -Dexec.args="examples/minimal-square.step examples/fan.stp examples/engine.stp"
```

### Expected payoff

- Enables measurable optimization
- Prevents "feels faster" changes without proof

---

## P0-2: Replace resolver registry full-scan with direct lookup

Status: completed

### Objective

Remove the hottest unnecessary loop in `StepEntityResolver`.

### Current problem

At [StepEntityResolver.java](/root/work/MiniCAD/src/main/java/com/minicad/step/semantic/StepEntityResolver.java:611), resolver dispatch currently:

- extracts definition names
- normalizes them per resolve call
- scans the entire registry

This is the main avoidable CPU hotspot.

### Refactoring approach

Convert dispatch to:

1. normalize local definition names once per instance
2. probe `REGISTRY.get(normalizedName)` directly
3. for complex entities, iterate local definitions in current order and select the first matching registered handler

This keeps existing semantics while removing full-table scanning.

### Implementation notes

- add a helper on `StepEntityInstance` or resolver-local utility to expose normalized definition names
- avoid changing external parser APIs more than necessary
- preserve current unsupported-entity exception behavior
- preserve current complex-entity matching order

### Boundaries

- no change to entity model classes
- no behavior change for supported/unsupported entities
- no attempt to redesign all resolver helpers in this issue

### Files

- `src/main/java/com/minicad/step/semantic/StepEntityResolver.java`
- optionally `src/main/java/com/minicad/step/syntax/StepEntityInstance.java`

### Validation

```bash
mvn -q -Dtest=StepEntityResolverTest test
mvn -q -Dtest=StepParserTest,StepEntityResolverTest test
mvn -q exec:java -Dexec.mainClass=com.minicad.app.StepBenchmarkApp -Dexec.args="examples/fan.stp"
```

### Expected payoff

- high runtime gain
- low semantic risk if ordering is preserved

### Progress result

- implemented direct map lookup dispatch in `StepEntityResolver`
- preserved complex-entity match precedence with explicit registry ordering
- added regression coverage for precedence stability
- validation passed:

```bash
mvn -q -Dtest=StepEntityResolverTest test
mvn -q -Dtest=StepParserTest,StepEntityResolverTest test
```

---

## P0-3: Cache normalized definition names on syntax instances

### Objective

Avoid repeating ASCII uppercasing and repeated per-entity string normalization.

### Current problem

Even after direct lookup is introduced, resolver may still repeatedly normalize definition names on hot paths.

### Refactoring approach

Push name normalization closer to syntax objects:

- add a cached normalized-name list or accessor on `StepEntityInstance`
- compute lazily
- store immutable/cached result

This keeps the resolver focused on lookup instead of string work.

### Implementation notes

- use ASCII-only normalization consistent with current STEP handling
- do not change user-visible `name()` or definition text
- keep memory overhead modest

### Boundaries

- no change to raw parsed names returned publicly
- no broad syntax redesign

### Files

- `src/main/java/com/minicad/step/syntax/StepEntityInstance.java`
- possibly `src/main/java/com/minicad/step/syntax/StepEntityDefinition.java`

### Validation

```bash
mvn -q -Dtest=StepParserTest,StepEntityResolverTest test
```

### Expected payoff

- medium runtime gain
- cleaner resolver code

---

## P0-4: Split benchmark timing into cold-path and compiled-path metrics

### Objective

Make benchmark numbers useful for pipeline reuse work.

### Current problem

`StepBenchmarkApp` currently measures raw stages and then calls exporters that internally re-parse and re-resolve, which mixes costs.

### Refactoring approach

Introduce two benchmark modes in one run:

- cold path: parse -> resolve -> build -> export
- compiled path: export from reusable compiled state

This gives clean visibility into what each future optimization actually improves.

### Implementation notes

- keep output human-readable
- do not over-engineer benchmark framework
- store stage names consistently

### Boundaries

- benchmark-only change
- no exporter API break required yet

### Files

- `src/main/java/com/minicad/app/StepBenchmarkApp.java`

### Validation

```bash
mvn -q -Dtest=StepBenchmarkAppTest test
mvn -q exec:java -Dexec.mainClass=com.minicad.app.StepBenchmarkApp -Dexec.args="examples/fan.stp"
```

### Expected payoff

- indirect runtime payoff
- high measurement quality improvement

---

## P0-5: Introduce `CompiledStepDocument` shared pipeline object

Status: completed

### Objective

Create one reusable container for the expensive front half of the STEP pipeline.

### Refactoring approach

Add a new internal type, for example:

- `CompiledStepDocument`

Containing:

- source text or metadata as needed
- `StepFile`
- `Map<Integer, StepEntity> resolved`
- `StepCadBuilder`

Construction should happen in one place and be callable from:

- preview exporter
- mesh exporter
- benchmark app
- potentially viewer app later

### Implementation notes

- prefer immutable fields
- keep constructor/factory narrow
- avoid stuffing unrelated export payloads into it in this first issue

### Boundaries

- this issue only introduces the shared compiled front-half state
- payload reuse comes later

### Files

- new file under `src/main/java/com/minicad/app/`
- `src/main/java/com/minicad/app/StepPreviewJsonExporter.java`
- `src/main/java/com/minicad/app/StepMeshExporter.java`
- `src/main/java/com/minicad/app/StepBenchmarkApp.java`

### Validation

```bash
mvn -q -Dtest=StepPreviewJsonExporterTest,StepMeshExporterTest,StepBenchmarkAppTest test
```

### Expected payoff

- high foundation value
- medium immediate runtime gain

### Progress result

- added [CompiledStepDocument.java](/root/work/MiniCAD/src/main/java/com/minicad/app/CompiledStepDocument.java)
- preview exporter, mesh exporter, and benchmark app now share compiled front-half pipeline state
- validation passed:

```bash
mvn -q -Dtest=StepPreviewJsonExporterTest,StepMeshExporterTest,StepBenchmarkAppTest test
```

---

## P0-6: Convert preview exporter entry points to use compiled state

Status: completed

### Objective

Stop repeating parse/resolve/build work across `export`, `exportBinary`, and `exportGlb`.

### Refactoring approach

Add overloads or internal entry points that accept `CompiledStepDocument`.

Then make the public string-based APIs do:

1. compile once
2. dispatch to output-specific encoder path

This keeps public API compatibility while collapsing duplicate work.

### Implementation notes

- keep current external methods for compatibility
- avoid mixing payload reuse in this issue unless it is trivial
- focus on front-half reuse first

### Boundaries

- no major payload builder redesign yet
- no JSON/binary/GLB writer split yet

### Files

- `src/main/java/com/minicad/app/StepPreviewJsonExporter.java`

### Validation

```bash
mvn -q -Dtest=StepPreviewJsonExporterTest test
mvn -q exec:java -Dexec.mainClass=com.minicad.app.StepBenchmarkApp -Dexec.args="examples/fan.stp"
```

### Expected payoff

- high runtime gain for repeated export paths
- lower memory churn

### Progress result

- `export`, `exportBinary`, and `exportGlb` now route through a shared compile helper
- public API remained unchanged
- compiled-path equivalence tests added for preview export

---

## P0-7: Convert mesh exporter to use compiled state

Status: completed

### Objective

Remove duplicated pipeline work in `StepMeshExporter`.

### Refactoring approach

Add an internal mesh-export path that accepts `CompiledStepDocument`.

This aligns mesh export with preview export and makes benchmark comparisons cleaner.

### Implementation notes

- keep existing string-based public API
- use compiled state only internally first

### Boundaries

- do not redesign triangulator here
- do not tackle thread-safety in the same patch unless trivial

### Files

- `src/main/java/com/minicad/app/StepMeshExporter.java`

### Validation

```bash
mvn -q -Dtest=StepMeshExporterTest test
```

### Expected payoff

- medium runtime gain
- cleaner exporter architecture

### Progress result

- mesh exporter now has compiled-state entry points alongside existing string APIs
- benchmark app reuses compiled state for mesh export timing
- compiled-path equivalence coverage added in `StepMeshExporterTest`

---

## P0-8: Remove unsafe parallel triangulation over shared `StepCadBuilder`

Status: completed

### Objective

Eliminate concurrency risk in mesh export.

### Refactoring approach

Take the safe path first:

- replace `parallelStream()` with sequential processing
- verify determinism

This intentionally prioritizes correctness over speculative throughput.

### Implementation notes

- document why parallelism is removed
- if performance drops, record it rather than compensating prematurely

### Boundaries

- no attempt to reintroduce safe parallelism here

### Files

- `src/main/java/com/minicad/app/StepMeshExporter.java`

### Validation

```bash
mvn -q -Dtest=StepMeshExporterTest test
```

### Expected payoff

- correctness and determinism improvement
- creates a safe baseline for later optimization

### Progress result

- replaced face-level `parallelStream()` triangulation with sequential traversal
- removed shared mutable builder access from mesh triangulation hot path
- validation passed:

```bash
mvn -q -Dtest=StepMeshExporterTest test
mvn -q -Dtest=StepMeshExporterTest,StepPreviewJsonExporterTest,StepBenchmarkAppTest test
```

---

## P0-9: Add deterministic mesh-export regression coverage

Status: completed

### Objective

Lock in behavior after removing unsafe parallelism.

### Refactoring approach

Add tests that verify:

- repeated exports of the same model produce stable output
- no ordering-dependent triangle/index instability appears

### Implementation notes

- prefer small deterministic samples
- if exact full output matching is too brittle, assert stable counts and stable normalized output

### Files

- `src/test/java/com/minicad/app/StepMeshExporterTest.java`

### Validation

```bash
mvn -q -Dtest=StepMeshExporterTest test
```

### Expected payoff

- protects correctness during later re-optimization

### Progress result

- added repeated-run deterministic OBJ regression coverage
- mesh exporter now verifies stable output across multiple runs on the same sample

---

## P1 Issues

## P1-0: Extract geometry helper boundary from `StepCadBuilder`

Status: completed

### Objective

Create a first low-risk structural cut inside `StepCadBuilder` without changing public behavior.

### Refactoring approach

Extract the most self-contained utility logic first into a package-private helper and keep `StepCadBuilder` as the facade.

This de-risks later extraction of geometry/topology-specific builders by proving the delegation pattern on a narrower slice.

### Implementation notes

- added [StepCadGeometryOps.java](/root/work/MiniCAD/src/main/java/com/minicad/step/semantic/StepCadGeometryOps.java)
- moved:
  - curve sampling helpers
  - trimmed-curve sampling helpers
  - closed-loop normalize/reverse helpers
  - curve and surface transformation helpers
  - replica transformation helpers
- `StepCadBuilder` now delegates these responsibilities through `geometryOps`

### Boundaries

- no caller-facing API change
- no topology extraction yet
- no cache redesign yet

### Validation

```bash
mvn -q -Dtest=StepMeshExporterTest,StepPreviewJsonExporterTest,StepEntityResolverTest test
mvn -q -Dtest=StepMeshExporterTest,StepBenchmarkAppTest test
```

### Expected payoff

- lowers `StepCadBuilder` local complexity
- establishes the first stable extraction seam for later `P1` work

## P1-1: Extract preview exporter compile stage from payload stage

### Objective

Make preview export code easier to reason about before splitting serializers.

### Refactoring approach

Inside `StepPreviewJsonExporter`, explicitly separate:

- compile/load stage
- payload-building stage
- encoding stage

This is an internal structural cleanup before bigger extraction.

### Implementation notes

- convert large methods into clear stage helpers
- do not move files yet unless the split is obvious

### Boundaries

- no serializer extraction yet
- no payload format changes

### Validation

```bash
mvn -q -Dtest=StepPreviewJsonExporterTest test
```

### Expected payoff

- lower local complexity
- easier next-step refactors

---

## P1-2: Extract JSON serialization from `StepPreviewJsonExporter`

### Objective

Separate payload construction from JSON encoding.

### Refactoring approach

Move generic JSON/payload-to-JSON logic into a dedicated helper or writer class.

This reduces the size of the main exporter and isolates serialization behavior.

### Implementation notes

- preserve exact JSON field semantics
- prefer internal package-private helper first

### Files

- `src/main/java/com/minicad/app/StepPreviewJsonExporter.java`
- new serializer file under `src/main/java/com/minicad/app/`

### Validation

```bash
mvn -q -Dtest=StepPreviewJsonExporterTest test
```

### Expected payoff

- maintainability gain
- easier future performance tuning for JSON writing

---

## P1-3: Extract binary packet writer from preview exporter

### Objective

Isolate binary export logic from payload construction.

### Refactoring approach

Move binary-specific encoding into a dedicated helper that consumes an already-built payload.

### Implementation notes

- keep public API unchanged
- preserve binary compatibility expected by current tests

### Files

- `src/main/java/com/minicad/app/StepPreviewJsonExporter.java`
- new binary writer file

### Validation

```bash
mvn -q -Dtest=StepPreviewJsonExporterTest test
```

### Expected payoff

- lower exporter complexity
- easier debugging of binary-only issues

---

## P1-4: Extract GLB writer from preview exporter

### Objective

Isolate GLB assembly and encoding.

### Refactoring approach

Move GLB-specific mesh/material/node/buffer writing into a dedicated helper class.

### Implementation notes

- keep current output semantics
- do not mix with payload extraction

### Files

- `src/main/java/com/minicad/app/StepPreviewJsonExporter.java`
- new GLB writer file

### Validation

```bash
mvn -q -Dtest=StepPreviewJsonExporterTest test
```

### Expected payoff

- major maintainability gain
- cleaner future GPU/export tuning path

---

## P1-5: Extract assembly-data build logic from preview exporter

### Objective

Separate assembly graph and instance logic from face/edge payload generation.

### Refactoring approach

Move assembly-centric logic into a dedicated helper that builds:

- representations
- instances
- assembly metadata

This keeps exporter payload logic narrower.

### Files

- `src/main/java/com/minicad/app/StepPreviewJsonExporter.java`
- potentially `src/main/java/com/minicad/app/StepAssemblyGraphBuilder.java`
- new helper file if needed

### Validation

```bash
mvn -q -Dtest=StepPreviewJsonExporterTest,StepAssemblyGraphBuilderTest test
```

### Expected payoff

- medium maintainability gain
- clearer ownership between assembly extraction and geometry extraction

---

## P1-6: Extract metadata/unit/product info stage from preview exporter

### Objective

Reduce coupling between geometry export and metadata extraction.

### Refactoring approach

Move orchestration around:

- `StepMetadataExtractor`
- `ProductMetadataExtractor`
- `UnitExtractor`

into a focused metadata stage helper.

### Boundaries

- no metadata schema changes

### Validation

```bash
mvn -q -Dtest=StepPreviewJsonExporterTest,ProductMetadataExtractorTest test
```

### Expected payoff

- lower cognitive load
- easier metadata-only bug fixing

---

## P1-7: Extract `Geometry2Builder` from `StepCadBuilder`

Status: not started

### Objective

Start shrinking `StepCadBuilder` with the lowest-risk split.

### Refactoring approach

Extract 2D-only build logic and caches first:

- points2d
- directions2d
- lines2d
- circle/ellipse/polyline/spline/trimmed/composite 2D

This is a good first slice because it is more self-contained than topology.

### Implementation notes

- keep orchestration in `StepCadBuilder` initially
- delegate to the extracted helper rather than rewriting callers

### Validation

```bash
mvn -q -Dtest=Curve2DTest,StepPreviewJsonExporterTest,StepMeshExporterTest test
```

### Expected payoff

- medium maintainability gain
- lower future refactor risk

---

## P1-8: Extract `Geometry3Builder` from `StepCadBuilder`

Status: not started

### Objective

Split 3D curve and point construction out of the central builder.

### Refactoring approach

After the 2D extraction pattern is proven, apply the same approach to:

- point/direction/vector/placement
- line/circle/ellipse/polyline/spline/composite/trimmed 3D

### Boundaries

- do not move topology or shell/solid logic yet

### Validation

```bash
mvn -q -Dtest=CurveUtilityTest,SurfaceGeometryTest,StepMeshExporterTest test
```

### Expected payoff

- medium to high maintainability gain

---

## P1-9: Extract `SurfaceBuilder` from `StepCadBuilder`

Status: not started

### Objective

Separate surface conversion logic from curve/topology logic.

### Refactoring approach

Move supported surface-building paths into a dedicated component:

- plane
- cylindrical
- conical
- toroidal
- spherical
- B-spline and rational B-spline surfaces
- offset/swept/advanced supported surfaces

### Validation

```bash
mvn -q -Dtest=SurfaceUtilityTest,StepPreviewJsonExporterTest,FaceTest test
```

### Expected payoff

- high maintainability gain
- clearer future surface optimization path

---

## P1-10: Extract `TopologyBuilder` from `StepCadBuilder`

Status: not started

### Objective

Move vertex/edge/loop/face/shell/solid build logic into a dedicated layer.

### Refactoring approach

After geometry and surface extraction are stable, move topology construction into a separate component that depends on the geometry builders.

### Boundaries

- preserve public `StepCadBuilder` facade initially
- avoid API churn for callers

### Validation

```bash
mvn -q -Dtest=FaceTest,SolidTest,TopologyBoundingBoxTest,StepMeshExporterTest test
```

### Expected payoff

- high maintainability gain
- much clearer ownership of caches and recursion

---

## P1-11: Add package-level or class-level documentation for compiled pipeline flow

### Objective

Make the new architecture discoverable once compiled-state reuse and builder splits land.

### Refactoring approach

Add a concise developer-facing markdown or class Javadoc explaining:

- raw STEP text
- parsed `StepFile`
- resolved semantic map
- built geometry/topology
- payload/export encoders

### Validation

- documentation review

### Expected payoff

- lowers onboarding and refactor friction

---

## P2 Issues

## P2-1: Add list capacity estimation in `StepParser`

### Objective

Reduce unnecessary array resizing in hot syntax collections.

### Refactoring approach

Introduce low-risk capacity hints where obvious:

- top-level entity list
- parameter lists
- nested list values

Use conservative estimates only.

### Boundaries

- no parser semantic changes
- no custom collection types

### Validation

```bash
mvn -q -Dtest=StepParserTest test
```

### Expected payoff

- small parse-stage gain

---

## P2-2: Reduce `StepValue` allocation churn on hot syntax paths

### Objective

Trim small-object pressure in the parser.

### Refactoring approach

Investigate safe cases for reducing allocation churn, for example:

- shared singleton instances for omitted/not-provided values
- avoid needless wrapper creation where semantics are identical

### Boundaries

- no semantic model changes
- no zero-copy rewrite

### Validation

```bash
mvn -q -Dtest=StepParserTest,StepEntityResolverTest test
```

### Expected payoff

- low to medium parse-stage gain

---

## P2-3: Tighten number and enum token handling

### Objective

Reduce overhead in token creation on very large files.

### Refactoring approach

Audit `StepTokenizer` and `StepParser` for:

- redundant substring creation
- repeated branching that can be simplified
- unnecessary temporary objects around number parsing

### Validation

```bash
mvn -q -Dtest=StepParserTest test
```

### Expected payoff

- small parser improvement

---

## P2-4: Split `StepPreviewJsonExporterTest` by feature area

### Objective

Turn one extremely large test file into focused suites.

### Refactoring approach

Split into topics such as:

- basic geometry export
- assembly export
- metadata export
- binary encoding
- GLB encoding
- advanced surfaces and trims
- PMI/callout export

Keep helper methods centralized where useful.

### Validation

```bash
mvn -q -Dtest=StepPreviewJsonExporterTest test
```

Then switch to the new test class names once split.

### Expected payoff

- high developer productivity gain

---

## P2-5: Split `StepEntityResolverTest` by entity category

### Objective

Reduce resolver test maintenance friction.

### Refactoring approach

Split by category:

- base primitives
- topology
- surfaces
- products/assembly
- metadata/approval/document
- tolerances/PMI
- advanced/edge cases

### Validation

```bash
mvn -q -Dtest=StepEntityResolverTest test
```

Then run the new split classes.

### Expected payoff

- easier selective runs
- easier failure localization

---

## P2-6: Split `StepDumpAppTest` by command/output feature

### Objective

Shrink a broad integration-heavy test file into smaller units.

### Refactoring approach

Split by concern:

- topology dump
- metadata dump
- assembly dump
- validation dump
- unsupported-entity reporting

### Validation

```bash
mvn -q -Dtest=StepDumpAppTest test
```

Then run the new split classes.

### Expected payoff

- lower merge conflicts
- easier maintenance

---

## P2-7: Extract shared STEP snippet fixtures for tests

### Objective

Reduce duplication across large test suites.

### Refactoring approach

Create shared fixture helpers for:

- minimal square
- simple shells
- trimmed cylindrical/conical/toroidal samples
- simple assembly samples
- metadata/approval/product samples

### Boundaries

- keep fixture helpers readable
- avoid over-abstracting tiny tests

### Validation

```bash
mvn -q test
```

### Expected payoff

- medium maintainability gain

---

## P2-8: Revisit safe parallelism with isolated builder state

### Objective

Reintroduce concurrency only after state ownership is clean.

### Refactoring approach

Only after P0 and P1 are complete:

1. classify immutable versus mutable builder data
2. move mutable caches to worker-local state
3. parallelize only isolated units
4. add deterministic-output regression tests

### Boundaries

- do not start this before builder/export refactors stabilize

### Validation

```bash
mvn -q -Dtest=StepMeshExporterTest,StepPreviewJsonExporterTest test
mvn -q exec:java -Dexec.mainClass=com.minicad.app.StepBenchmarkApp -Dexec.args="examples/fan.stp examples/engine.stp"
```

### Expected payoff

- potentially high runtime gain
- high implementation risk if attempted too early

---

## Suggested Execution Sequence

### Wave 1

- P0-1 benchmark baselines
- P0-2 resolver direct lookup
- P0-3 normalized definition cache

### Wave 2

- P0-4 benchmark cold/compiled split
- P0-5 compiled pipeline object
- P0-6 preview exporter compiled-state reuse
- P0-7 mesh exporter compiled-state reuse

### Wave 3

- P0-8 remove unsafe mesh parallelism
- P0-9 deterministic mesh regression coverage

### Wave 4

- P1-1 exporter stage separation
- P1-2 JSON writer extraction
- P1-3 binary writer extraction
- P1-4 GLB writer extraction

### Wave 5

- P1-5 assembly stage extraction
- P1-6 metadata stage extraction
- P1-7 geometry2 builder extraction
- P1-8 geometry3 builder extraction

### Wave 6

- P1-9 surface builder extraction
- P1-10 topology builder extraction
- P1-11 pipeline architecture documentation

### Wave 7

- P2-1 parser capacity estimation
- P2-2 `StepValue` allocation cleanup
- P2-3 tokenizer/parser micro-optimizations

### Wave 8

- P2-4 split preview exporter tests
- P2-5 split resolver tests
- P2-6 split dump app tests
- P2-7 shared test fixtures

### Wave 9

- P2-8 safe parallelism revisit

---

## Recommended Starting Set

If starting immediately, the best first three issues are:

1. `P0-2` Replace resolver registry full-scan with direct lookup
2. `P0-5` Introduce `CompiledStepDocument` shared pipeline object
3. `P0-8` Remove unsafe parallel triangulation over shared `StepCadBuilder`

That combination addresses:

- the clearest CPU hotspot
- the most obvious duplicate work
- the main concurrency correctness risk
