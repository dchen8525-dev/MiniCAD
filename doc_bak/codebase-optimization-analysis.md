# MiniCAD Codebase Optimization Analysis

## Summary

After reviewing the repository, the most valuable optimizations are in the STEP resolver and export pipeline rather than the syntax parser. Current benchmark evidence shows that for medium and large files, `resolve` and preview payload/export stages dominate runtime.

## Highest Priority

### 1. Replace linear registry scans in `StepEntityResolver`

Current implementation in [StepEntityResolver.java](/root/work/MiniCAD/src/main/java/com/minicad/step/semantic/StepEntityResolver.java:611) normalizes the current entity definition names and then linearly scans the entire `REGISTRY` to find a match.

Current pattern:

- Build `defNames[]` for the current instance
- Iterate every `REGISTRY` entry
- Compare each registry key against each definition name

This gives unnecessary overhead proportional to:

- entity count
- registry size
- number of definitions in complex entities

Recommended changes:

- Cache normalized definition names earlier, ideally in parse-time or instance construction
- Replace registry full-scan with direct `Map.get(definitionName)`
- For complex entities, iterate the instance definitions and do direct lookups

Why this is first:

- This is the clearest runtime hotspot in core semantic resolution
- Benchmark evidence already shows `resolve` is a dominant stage on large files

Observed benchmark on `examples/fan.stp`:

- `parse`: about `0.74s`
- `resolve`: about `4.05s`
- preview payload: about `2.01s`
- preview JSON encoding: about `0.78s`

That makes resolver optimization the best first move.

### 2. Eliminate duplicate `parse -> resolve -> build` work across exporters

`StepPreviewJsonExporter` currently runs the full pipeline separately for:

- `export`
- `exportBinary`
- `exportGlb`

See [StepPreviewJsonExporter.java](/root/work/MiniCAD/src/main/java/com/minicad/app/StepPreviewJsonExporter.java:499).

Each entry point repeats:

- `StepParser.parse(stepText)`
- `StepEntityResolver.resolveAll(stepFile)`
- `StepCadBuilder.fromResolved(resolved)`

Recommended changes:

- Introduce a reusable intermediate object such as `ResolvedStepDocument` or `CompiledStepDocument`
- Reuse `StepFile`, `resolved`, and `StepCadBuilder`
- If practical, also reuse `PreviewPayload` between JSON/binary/GLB outputs

Expected benefits:

- Lower total CPU
- Lower peak memory
- Cleaner API boundaries
- Better support for server-side caching in `StepViewerApp`

### 3. Remove unsafe parallel use of `StepCadBuilder` in `StepMeshExporter`

`StepMeshExporter` uses `parallelStream()` for face triangulation while sharing one `StepCadBuilder` instance across workers. See [StepMeshExporter.java](/root/work/MiniCAD/src/main/java/com/minicad/app/StepMeshExporter.java:80).

`StepCadBuilder` is not thread-safe. It maintains many mutable `LinkedHashMap` caches, as shown in [StepCadBuilder.java](/root/work/MiniCAD/src/main/java/com/minicad/step/semantic/StepCadBuilder.java:274).

This is a correctness risk, not just a performance issue.

Recommended options:

- Remove parallelism first and make behavior deterministic
- Or redesign builder state so workers do not share mutable caches
- Or split immutable resolved data from per-thread build caches

Until that is addressed, current parallel triangulation has race potential.

## Medium Priority

### 4. Split `StepCadBuilder` by responsibility

`StepCadBuilder` is currently doing too much:

- 2D geometry construction
- 3D geometry construction
- surface construction
- topology construction
- cache management for many object types

The file is about `8460` lines and maintains many specialized caches.

Recommended decomposition:

- `Geometry2Builder`
- `Geometry3Builder`
- `SurfaceBuilder`
- `TopologyBuilder`

Benefits:

- Lower maintenance burden
- Easier testing
- Easier future parallelization
- Clearer ownership of cache lifecycles

### 5. Split `StepPreviewJsonExporter` into multiple components

`StepPreviewJsonExporter` is about `18370` lines and currently mixes:

- payload construction
- assembly extraction
- PMI extraction
- mesh generation
- JSON encoding
- binary encoding
- GLB encoding

This creates a very high regression surface.

Recommended decomposition:

- preview payload builder
- assembly/metadata extractor
- JSON serializer
- binary packet writer
- GLB writer

This is mainly a maintainability optimization, but it will also make profiling and targeted performance work much easier.

## Lower Priority Runtime Work

### 6. Continue syntax-layer micro-optimizations only after resolver/export changes

`StepParser` and `StepTokenizer` are relatively small and already cleaner than the resolver/export layers:

- [StepParser.java](/root/work/MiniCAD/src/main/java/com/minicad/step/syntax/StepParser.java:1)
- [StepTokenizer.java](/root/work/MiniCAD/src/main/java/com/minicad/step/syntax/StepTokenizer.java:1)

Further possible improvements:

- better `ArrayList` capacity estimation
- fewer small object allocations in `StepValue`
- tighter numeric and enum token handling

But based on current measurements, syntax is not the first bottleneck to attack.

## Developer Productivity Improvements

### 7. Split oversized test files

Several test classes are now very large:

- `src/test/java/com/minicad/app/StepPreviewJsonExporterTest.java`: about `10506` lines
- `src/test/java/com/minicad/step/semantic/StepEntityResolverTest.java`: about `9659` lines
- `src/test/java/com/minicad/app/StepDumpAppTest.java`: about `7857` lines

Recommended changes:

- Split tests by feature area
- Extract helper builders and STEP snippet fixtures
- Keep focused test classes for specific entity groups or export features

Benefits:

- Faster failure localization
- Easier selective test runs
- Lower merge conflict frequency
- Lower cognitive load during maintenance

### 8. Make benchmark pipeline reuse intermediate results

`StepBenchmarkApp` first benchmarks `parse/resolve/build`, then separately calls:

- `StepPreviewJsonExporter.export(stepText)`
- `StepMeshExporter.exportObj(stepText)`

Those calls repeat parsing and resolution again. See [StepBenchmarkApp.java](/root/work/MiniCAD/src/main/java/com/minicad/app/StepBenchmarkApp.java:50).

Recommended changes:

- Benchmark from a reusable resolved representation
- Separate "fresh pipeline" metrics from "export from resolved state" metrics

This will produce more useful performance data for regression tracking.

## Suggested Execution Order

Recommended implementation sequence:

1. Refactor `StepEntityResolver` to use direct registry lookup
2. Introduce a reusable resolved/compiled document object
3. Rework exporter entry points to reuse intermediate pipeline stages
4. Remove or redesign unsafe parallel builder access in mesh export
5. Split `StepCadBuilder` and `StepPreviewJsonExporter` by responsibility
6. Split oversized test classes
7. Return to syntax-layer micro-optimizations if benchmarks still justify it

## Conclusion

The biggest gains are not in the parser anymore. The resolver and export pipeline are the real optimization targets:

- resolver is a clear CPU hotspot
- exporters repeat expensive work
- mesh export has unsafe shared mutable state
- builder/export classes are too large for safe iteration speed

If only two changes are made first, they should be:

1. direct registry lookup in `StepEntityResolver`
2. shared reusable `ResolvedStepDocument`-style pipeline state for all exporters
