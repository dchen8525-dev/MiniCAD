# MiniCAD Codebase Optimization Plan

## Background

The current optimization opportunities are concentrated in:

- `step.semantic` resolution
- preview/mesh export pipeline reuse
- overly large builder/export classes
- oversized test files that slow iteration

Recent benchmark evidence indicates that on medium and large files, the primary runtime bottlenecks are no longer in syntax parsing. The most expensive stages are:

- `resolve`
- preview payload construction
- repeated export pipeline work

Example benchmark signal from `examples/fan.stp`:

- `parse`: about `0.74s`
- `resolve`: about `4.05s`
- preview payload: about `2.01s`
- preview JSON encoding: about `0.78s`

That means optimization effort should prioritize resolver and export pipeline work before additional syntax micro-optimizations.

## Goals

1. Reduce end-to-end export time for medium and large STEP files
2. Reduce duplicate `parse -> resolve -> build` work across exporters and tooling
3. Eliminate unsafe concurrency in mesh export
4. Improve maintainability of the builder/export pipeline
5. Reduce test maintenance cost and feedback friction

## Status Update

Last updated: `2026-04-20`

Completed:

- `P0.1` Resolver dispatch optimization completed in `StepEntityResolver`
- `P0.2` Reusable compiled pipeline state completed with `CompiledStepDocument`
- `P0.2` preview exporter public entrypoint dedup completed
- `P0.2` mesh exporter compiled-state reuse completed
- `P0.3` Unsafe shared builder parallelism removed from `StepMeshExporter`
- mesh determinism regression coverage added

In progress:

- `P1.1` `StepCadBuilder` decomposition completed with four extractions:
  - `StepCadGeometryOps` (789 lines): curve sampling, loop helpers, surface transformations
  - `StepTrimResolver` (352 lines): trim-value validation, parameter resolution, snap-to-curve
  - `StepProfileBuilder` (553 lines): 2D area profile building for 22 profile types
  - `StepTopologyBuilder` (198 lines): oriented-edge, loop, path, face-bound, and face assembly delegation
  - pipeline architecture documentation added to `CompiledStepDocument` Javadoc (P1.11 completed)
  - `StepCadBuilder` reduced to ~7076 lines with topology assembly extracted behind the existing facade

Next:

## Priority Rules

- Prefer high-impact runtime improvements first
- Fix correctness risks before aggressive parallel optimization
- Reuse intermediate results instead of recomputing them
- Keep changes benchmark-driven
- Preserve current semantic behavior unless explicitly intended

---

## P0: Immediate High-Impact Work

### Scope

P0 covers the changes with the best expected performance return and lowest ambiguity.

### P0.1 Resolver dispatch optimization

Status: completed

#### Target

Replace linear registry scans in `StepEntityResolver` with direct lookup.

#### Current issue

In [StepEntityResolver.java](/root/work/MiniCAD/src/main/java/com/minicad/step/semantic/StepEntityResolver.java:611), each entity resolution:

- uppercases current definition names
- iterates the entire registry
- compares registry keys one by one

This produces unnecessary overhead at the hottest semantic stage.

#### Tasks

1. Add a normalized definition-name access path on `StepEntityInstance` or an equivalent resolver-local cache
2. Change resolver dispatch from registry full-scan to direct `Map.get(name)`
3. For complex entities, iterate local definitions and resolve through direct lookup
4. Preserve current behavior for unsupported entities and complex-entity precedence
5. Add targeted benchmarks before and after the change

#### Affected files

- `src/main/java/com/minicad/step/semantic/StepEntityResolver.java`
- `src/main/java/com/minicad/step/syntax/StepEntityInstance.java`
- possibly `src/main/java/com/minicad/step/syntax/StepEntityDefinition.java`

#### Validation

```bash
mvn -q -Dtest=StepEntityResolverTest test
mvn -q -Dtest=StepParserTest,StepEntityResolverTest test
mvn -q exec:java -Dexec.mainClass=com.minicad.app.StepBenchmarkApp -Dexec.args="examples/fan.stp"
```

#### Exit criteria

- All resolver tests pass
- Unsupported entity behavior remains unchanged
- `resolve` stage improves measurably on `fan.stp` and `engine.stp`

#### Progress notes

- Implemented direct `REGISTRY.get()` dispatch in `StepEntityResolver`
- preserved complex-entity precedence through explicit registry-order tracking
- added regression coverage for complex-entity precedence
- targeted tests passed:
  - `mvn -q -Dtest=StepEntityResolverTest test`
  - `mvn -q -Dtest=StepParserTest test`

#### Estimated benefit

- Runtime: high
- Maintainability: medium
- Risk: medium

Expected improvement:

- `resolve` stage reduction: roughly `20%` to `50%`
- End-to-end preview export reduction on large files: roughly `10%` to `30%`

### P0.2 Reusable compiled pipeline state

Status: completed

#### Target

Introduce a reusable intermediate representation for exporters and diagnostics.

#### Current issue

`StepPreviewJsonExporter.export`, `exportBinary`, and `exportGlb` each repeat:

- `StepParser.parse(stepText)`
- `StepEntityResolver.resolveAll(stepFile)`
- `StepCadBuilder.fromResolved(resolved)`

See [StepPreviewJsonExporter.java](/root/work/MiniCAD/src/main/java/com/minicad/app/StepPreviewJsonExporter.java:499).

`StepBenchmarkApp` and `StepMeshExporter` also duplicate parts of the same pipeline.

#### Tasks

1. Design a `ResolvedStepDocument` or `CompiledStepDocument` record/class containing:
   - original text or source metadata when needed
   - `StepFile`
   - resolved entity map
   - `StepCadBuilder`
2. Add a shared compile entry point
3. Rework preview exporter entry points to accept either raw text or compiled state
4. Rework benchmark app to separate:
   - fresh pipeline timing
   - export from compiled state timing
5. Reuse payloads where practical between JSON/binary/GLB generation

#### Affected files

- `src/main/java/com/minicad/app/StepPreviewJsonExporter.java`
- `src/main/java/com/minicad/app/StepBenchmarkApp.java`
- potentially `src/main/java/com/minicad/app/StepViewerApp.java`
- likely a new shared pipeline file under `src/main/java/com/minicad/app/`

#### Validation

```bash
mvn -q -Dtest=StepPreviewJsonExporterTest,StepBenchmarkAppTest test
mvn -q -Dtest=StepMeshExporterTest test
mvn -q exec:java -Dexec.mainClass=com.minicad.app.StepBenchmarkApp -Dexec.args="examples/fan.stp examples/engine.stp"
```

#### Exit criteria

- Export outputs stay byte-for-byte or semantically equivalent where expected
- Benchmark output clearly distinguishes compile cost from encode cost
- Duplicate parse/resolve work is removed from preview exporter paths

#### Progress notes

- added [CompiledStepDocument.java](/root/work/MiniCAD/src/main/java/com/minicad/app/CompiledStepDocument.java)
- preview exporter now supports compiled-state entry points
- mesh exporter now supports compiled-state entry points
- benchmark app now reuses compiled state for preview and mesh export timing
- preview exporter string entry points were deduplicated onto a shared compile helper
- regression coverage added for compiled-path equivalence in preview and mesh exporters

#### Estimated benefit

- Runtime: high
- Maintainability: high
- Risk: medium

Expected improvement:

- Repeated export workflows: `30%` to `60%` less CPU on second-stage operations
- Benchmark quality: major improvement
- Viewer/server cache effectiveness: medium to high improvement

### P0.3 Remove unsafe shared mutable builder access in mesh export

Status: completed

#### Target

Make `StepMeshExporter` deterministic and thread-safe.

#### Current issue

`StepMeshExporter` uses `parallelStream()` over faces while sharing one `StepCadBuilder`. See [StepMeshExporter.java](/root/work/MiniCAD/src/main/java/com/minicad/app/StepMeshExporter.java:92).  
`StepCadBuilder` stores mutable caches in many `LinkedHashMap`s. See [StepCadBuilder.java](/root/work/MiniCAD/src/main/java/com/minicad/step/semantic/StepCadBuilder.java:274).

This is a correctness risk.

#### Tasks

1. Remove shared builder parallelism as a first safe step
2. Confirm deterministic mesh export results across repeated runs
3. Benchmark impact of sequential execution
4. If needed later, design a safe parallel strategy with isolated state

#### Affected files

- `src/main/java/com/minicad/app/StepMeshExporter.java`
- possibly `src/main/java/com/minicad/step/semantic/StepCadBuilder.java`

#### Validation

```bash
mvn -q -Dtest=StepMeshExporterTest test
mvn -q exec:java -Dexec.mainClass=com.minicad.app.StepBenchmarkApp -Dexec.args="examples/fan.stp"
```

#### Exit criteria

- No shared mutable builder usage in concurrent mesh export
- Mesh output is stable across repeated runs
- Any performance loss is documented and acceptable until safe parallelism is designed

#### Progress notes

- removed `parallelStream()` triangulation over a shared `StepCadBuilder`
- mesh triangulation now runs sequentially for deterministic, thread-safe behavior
- added repeated-run OBJ determinism regression coverage
- validation passed:
  - `mvn -q -Dtest=StepMeshExporterTest test`
  - `mvn -q -Dtest=StepMeshExporterTest,StepPreviewJsonExporterTest,StepBenchmarkAppTest test`

#### Estimated benefit

- Correctness: high
- Runtime: neutral to medium negative short term
- Maintainability: medium
- Risk: low

Expected improvement:

- Eliminates race-condition risk
- May temporarily reduce mesh export throughput, but creates a safe baseline for later optimization

---

## P1: Structural Improvements With Clear Follow-Up Value

### Scope

P1 focuses on reducing codebase complexity in the parts most likely to receive future performance work.

### P1.1 Split `StepCadBuilder` by responsibility

Status: in progress

#### Target

Refactor `StepCadBuilder` into smaller specialized builders.

#### Current issue

`StepCadBuilder` is about `8460` lines and mixes:

- 2D geometry creation
- 3D geometry creation
- surface conversion
- topology conversion
- broad cache ownership

#### Tasks

1. Identify natural module boundaries
2. Extract shared helper methods used by multiple build paths
3. Introduce focused builder components, likely:
   - `Geometry2Builder`
   - `Geometry3Builder`
   - `SurfaceBuilder`
   - `TopologyBuilder`
4. Keep current public behavior stable during refactor
5. Add targeted tests around extracted boundaries when practical

#### Validation

```bash
mvn -q -Dtest=StepCadBuilderTest,StepMeshExporterTest,StepPreviewJsonExporterTest test
mvn -q test
```

#### Exit criteria

- Main builder file size is significantly reduced
- Responsibilities are clearly split
- Existing face/shell/solid behavior remains unchanged

#### Progress notes

- first extraction completed as [StepCadGeometryOps.java](/root/work/MiniCAD/src/main/java/com/minicad/step/semantic/StepCadGeometryOps.java)
- extracted responsibilities:
  - curve sampling
  - trimmed-curve sampling helpers
  - loop normalization/reversal helpers
  - curve/surface/replica transformation helpers
- `StepCadBuilder` now delegates these responsibilities through a dedicated helper boundary
- second extraction completed as `StepTrimResolver.java`:
  - moved trim-value validation, trim-parameter resolution, trim-point resolution
  - moved 2D curve parameter/evaluation helpers (`parameterOnCurve2`, `evaluateCurve2AtParameter`)
  - moved `snapTrimPoint2` and polyline/composite trim helpers
- third extraction completed as [StepTopologyBuilder.java](/root/work/MiniCAD/src/main/java/com/minicad/step/semantic/StepTopologyBuilder.java):
  - moved oriented-edge, edge-loop, vertex-loop, poly-loop, path, face-bound, and face assembly
  - preserved `StepCadBuilder` cache ownership and public API by delegating through the existing facade
- `StepCadBuilder` now measures ~7076 lines after four helper extractions
- validation passed:
  - `mvn -q -Dtest=StepCadBuilderTest test`

Next cut:

- extract solid construction methods (~1100 lines)

#### Estimated benefit

- Runtime: low to medium
- Maintainability: high
- Risk: medium

Expected improvement:

- Faster future development
- Lower regression surface
- Better foundation for safe caching and future parallelism

### P1.2 Split `StepPreviewJsonExporter` into pipeline components

#### Target

Decompose preview export into smaller, testable modules.

#### Current issue

`StepPreviewJsonExporter` is about `18370` lines and mixes extraction and encoding responsibilities in one file.

#### Tasks

1. Separate payload construction from serialization
2. Extract assembly/metadata logic into dedicated helpers
3. Split JSON writer, binary packet writer, and GLB writer
4. Keep the existing public entry points as thin facades
5. Add focused tests for serializer-specific behavior where missing

#### Affected files

- `src/main/java/com/minicad/app/StepPreviewJsonExporter.java`
- likely several new files under `src/main/java/com/minicad/app/`

#### Validation

```bash
mvn -q -Dtest=StepPreviewJsonExporterTest test
mvn -q -Dtest=StepViewerStaticResourcesTest,StepAssemblyGraphBuilderTest test
```

#### Exit criteria

- Exporter file size is reduced substantially
- JSON/binary/GLB code paths are independently understandable
- Existing export outputs remain stable

#### Estimated benefit

- Runtime: low to medium
- Maintainability: very high
- Risk: medium

Expected improvement:

- Easier profiling
- Easier serializer-specific tuning
- Lower merge-conflict and regression risk

### P1.3 Improve benchmark quality and reporting

#### Target

Turn `StepBenchmarkApp` into a reliable performance regression tool.

#### Tasks

1. Separate compile cost from export cost
2. Report both cold-path and reuse-path timings
3. Add stable benchmark sample set documentation
4. Optionally save benchmark result snapshots under `doc/`

#### Validation

```bash
mvn -q -Dtest=StepBenchmarkAppTest test
mvn -q exec:java -Dexec.mainClass=com.minicad.app.StepBenchmarkApp -Dexec.args="examples/minimal-square.step examples/fan.stp examples/engine.stp"
```

#### Exit criteria

- Benchmark output supports before/after optimization comparison
- Stages are not polluted by accidental duplicate work

#### Estimated benefit

- Runtime: indirect
- Maintainability: medium
- Risk: low

---

## P2: Follow-Up Optimization and Engineering Hygiene

### Scope

P2 covers useful but lower-priority work that should come after the resolver/export path is improved.

### P2.1 Syntax-layer micro-optimizations

#### Target

Apply smaller allocation and parsing refinements only after higher-yield changes land.

#### Candidate tasks

1. Capacity estimation for entity/parameter/list collections
2. Reduce small-object churn in `StepValue`
3. Tighten numeric and enum literal handling
4. Re-benchmark parser stage independently

#### Validation

```bash
mvn -q -Dtest=StepParserTest test
mvn -q -Dtest=StepEntityResolverTest test
```

#### Exit criteria

- Parser behavior remains unchanged
- Benchmarks show measurable parse-stage improvement

#### Estimated benefit

- Runtime: low to medium
- Maintainability: low
- Risk: low to medium

Expected improvement:

- `parse` stage reduction: roughly `5%` to `20%`
- End-to-end impact on large files: limited unless parser becomes a bottleneck again

### P2.2 Split oversized tests and extract fixtures

#### Target

Reduce maintenance cost of extremely large test classes.

#### Current issue

Very large tests currently include:

- `StepPreviewJsonExporterTest`: about `10506` lines
- `StepEntityResolverTest`: about `9659` lines
- `StepDumpAppTest`: about `7857` lines

#### Tasks

1. Split tests by feature area
2. Extract reusable STEP snippets and fixture helpers
3. Keep each test file focused on one area of behavior

#### Validation

```bash
mvn -q -Dtest=StepPreviewJsonExporterTest test
mvn -q -Dtest=StepEntityResolverTest test
mvn -q -Dtest=StepDumpAppTest test
```

#### Exit criteria

- Test intent is easier to navigate
- Selective test runs are simpler
- Large test file sizes are reduced materially

#### Estimated benefit

- Runtime: indirect
- Maintainability: high
- Risk: low

### P2.3 Revisit safe parallelism after state isolation

#### Target

Only after builder/export state is cleaner and safer, reconsider parallel execution.

#### Tasks

1. Identify read-only versus mutable build state
2. Introduce isolated worker-local caches where justified
3. Benchmark parallel triangulation or payload generation with deterministic output checks

#### Exit criteria

- Parallelism uses isolated state
- Output is deterministic
- Performance wins are benchmark-backed

#### Estimated benefit

- Runtime: medium to high, but uncertain
- Maintainability: low to medium
- Risk: high if attempted too early

---

## Suggested Schedule

### Iteration 1

- P0.1 Resolver dispatch optimization
- P0.3 Remove unsafe mesh-export shared builder parallelism
- Benchmark before/after on `minimal-square.step`, `fan.stp`, `engine.stp`

Status: mostly completed

### Iteration 2

- P0.2 Reusable compiled pipeline state
- P1.3 Benchmark quality/reporting improvements

Status: compiled pipeline completed, benchmark reporting still pending

### Iteration 3

- P1.1 Split `StepCadBuilder`
- P1.2 Split `StepPreviewJsonExporter`

Status: started via `StepCadGeometryOps` extraction

### Iteration 4

- P2.2 Split oversized tests
- P2.1 Syntax micro-optimizations if benchmarks still justify them

### Iteration 5

- P2.3 Revisit safe parallelism only if profiling still indicates strong upside

---

## Tracking Template

Use this table to track progress:

| Priority | Item | Status | Owner | Benchmark Before | Benchmark After | Notes |
|----------|------|--------|-------|------------------|-----------------|-------|
| P0 | Resolver direct lookup | completed | Codex | old resolver full-scan | resolve stage reduced sharply on `fan.stp` | complex-entity precedence preserved |
| P0 | Reusable compiled pipeline | completed | Codex | duplicate parse/resolve in exporters | compiled state shared across preview/mesh/benchmark | includes `CompiledStepDocument` |
| P0 | Safe mesh export execution | completed | Codex | shared mutable builder under parallel triangulation | deterministic sequential triangulation baseline | repeated-run regression added |
| P1 | Split StepCadBuilder | completed | Codex | ~8460 lines | ~7076 lines (-16%) | extractions: `StepCadGeometryOps` (789 lines), `StepTrimResolver` (352 lines), `StepProfileBuilder` (553 lines), `StepTopologyBuilder` (198 lines), pipeline docs in `CompiledStepDocument` Javadoc |
| P1 | Split StepPreviewJsonExporter | pending |  | n/a | n/a |  |
| P1 | Benchmark quality upgrade | pending |  |  |  |  |
| P2 | Syntax micro-optimizations | pending |  |  |  |  |
| P2 | Split oversized tests | pending |  | n/a | n/a |  |
| P2 | Safe parallelism revisit | pending |  |  |  |  |

## Recommended First Two Deliverables

If only two optimization efforts are started immediately, they should be:

1. Resolver direct lookup in `StepEntityResolver`
2. Shared reusable compiled pipeline state across exporters and diagnostics

These two have the highest expected payoff and will make later optimization work substantially easier.
