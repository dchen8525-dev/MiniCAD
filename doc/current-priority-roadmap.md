# MiniCAD Current Priority Roadmap

This roadmap tracks the most important near-term work for MiniCAD. Work should proceed from the highest priority downward unless a lower-priority item is required to unblock a higher-priority fix.

## Execution Rules

- Always run and record the baseline before a fix batch:
  - `mvn -B clean test`
  - `mvn -q exec:java -Dexec.args="examples/minimal-square.step"`
  - `mvn -q exec:java -Dexec.args="examples/engine.stp"`
- Do not remove tests to make the build pass.
- Every fix should add or update focused tests.
- Unsupported behavior should be documented or reported explicitly. Do not silently drop geometry.
- Prefer generated capability numbers over hand-maintained counts.

## Baseline: 2026-06-08

Environment note:

- Default `java -version`: OpenJDK 11.0.31.
- Default `mvn -version`: Maven 3.9.14 using Java 11.0.31.
- The project requires Java 21. Baseline was run with `JAVA_HOME=C:\Users\admin\.jdks\ms-21.0.11`.

Results:

- `mvn -B clean test`: passed with Java 21.
  - Tests run: 1503
  - Failures: 0
  - Errors: 0
  - Skipped: 0
- `mvn -q exec:java '-Dexec.args=examples/minimal-square.step'`: passed with Java 21.
  - Syntax entity count: 37
  - Build totals include closedShells=1, solids=1, unsupportedFaces=0
- `mvn -q exec:java '-Dexec.args=examples/engine.stp'`: passed with Java 21.
  - Syntax entity count: 93829
  - Semantic summary includes MANIFOLD_SOLID_BREP=31, ADVANCED_FACE=2387, CLOSED_SHELL=31

Initial environment failure:

- Running `mvn -B clean test` under the default Java 11 failed during compilation with `不支持发行版本 21`.

## Progress: 2026-06-08 P0 Viewer Security Batch

Completed:

- Added upload size configuration through `minicad.preview.maxUploadBytes`, default 50 MB.
- Added bounded stream reading for preview request bodies.
- Configured multipart size limits and return HTTP 413 for oversized raw or multipart uploads.
- Added `/api/example` name validation with `[A-Za-z0-9._-]+`.
- Normalized example paths and ensured resolved paths stay inside `examples`.
- Added preview cache size configuration through `minicad.preview.cache.maxBytes`, default 1 GB.
- Added optional preview cache dir override through `minicad.preview.cache.dir`.
- Added LRU cache eviction by last-modified time.
- Changed preview cache writes to temporary file plus atomic move when supported.
- Removed `X-MiniCAD-Cache-Path` from normal responses.
- Bound the viewer explicitly to `127.0.0.1` by default.
- Added `--host=` and warning logs for non-loopback binds.
- Streamed static resources instead of reading them fully into memory.
- Added HTTP security headers to static and API responses.
- Returned safe preview error messages with request ids.
- Disabled STEP source excerpt logging by default. It is now gated by `minicad.preview.debugSourceExcerpt`.

Tests added:

- Valid examples still work.
- Traversal names such as `../pom`, `../../etc/passwd`, `..\pom`, and `%2e%2e/pom` return 400.
- Oversized raw preview body returns 413.
- Oversized multipart preview body returns 413.
- Small STEP preview succeeds.
- Cache miss then hit works.
- `X-MiniCAD-Cache-Path` is absent.
- Static and API responses include security headers.
- Cache eviction removes the oldest files first.

Verification:

- `mvn -B clean test`: passed with Java 21.
  - Tests run: 1511
  - Failures: 0
  - Errors: 0
  - Skipped: 0
- `mvn -q exec:java '-Dexec.args=examples/minimal-square.step'`: passed with Java 21.
- `mvn -q exec:java '-Dexec.args=examples/engine.stp'`: passed with Java 21.

## Progress: 2026-06-08 P0 Capability Matrix Batch

Completed:

- Added `StepCapabilityReportApp`, a source-derived capability scanner.
- The scanner separates:
  - model classes
  - resolver-registered entities
  - builder-referenced entities
  - exporter-referenced entities
  - test-referenced entities
- Added `--json`, `--out=<path>`, `--root=<path>`, and `--write-docs` options.
- Generated:
  - `doc/generated/coverage.md`
  - `doc/generated/coverage.json`
- Added focused tests for scanner naming, scanning, Markdown rendering, and JSON rendering.

Current generated summary:

- Model classes: 1246
- Registered entities: 1293
- Builder-referenced entities: 734
- Exporter-referenced entities: 425
- Test-referenced entities: 495
- Matrix rows: 1844

Notes:

- The report is a capability signal, not a proof of geometric correctness.
- Matrix rows are limited to known model or registered entities to reduce noise from test aliases and helper class names.

Verification:

- `mvn -B clean test`: passed with Java 21.
  - Tests run: 1514
  - Failures: 0
  - Errors: 0
  - Skipped: 0
- `mvn -q exec:java '-Dexec.args=examples/minimal-square.step'`: passed with Java 21.
- `mvn -q exec:java '-Dexec.args=examples/engine.stp'`: passed with Java 21.

## P0: Immediate

1. Run and record the baseline commands.
2. Build a capability matrix that distinguishes:
   - parsed
   - resolved
   - built
   - exported
   - tested
3. Fix Web Viewer security issues:
   - Add `/api/preview` upload size limits, default 50 MB.
   - Make the max upload size configurable through `minicad.preview.maxUploadBytes`.
   - Reject oversized raw and multipart uploads with HTTP 413.
   - Add a bounded stream reader for untrusted input.
   - Reject `/api/example?name=` path traversal and allow only `[A-Za-z0-9._-]+`.
   - Normalize example paths and ensure they remain inside `examples`.
   - Add preview cache size limit, default 1 GB.
   - Make cache size configurable through `minicad.preview.cache.maxBytes`.
   - Evict preview cache by least-recently-used last-modified time.
   - Write cache files through a temporary file and atomic move.
   - Remove `X-MiniCAD-Cache-Path` by default.
   - Bind the viewer to `127.0.0.1` by default.
   - Add explicit `--host=` for external binds.
   - Warn when binding to a non-loopback host.
   - Stream static resources instead of reading them fully into memory.
   - Add HTTP security headers.
   - Return safe client error messages with request ids.
   - Avoid logging STEP source excerpts by default.
4. Add Web/API tests:
   - Small STEP preview succeeds.
   - Oversized raw body returns 413.
   - Oversized multipart upload returns 413.
   - Invalid example names return 400.
   - Valid example names still work.
   - Cache hit and eviction work.
   - Static and API responses include security headers.

## P1: Parser And Semantic Correctness

1. Complete STEP string escape support:
   - doubled single quote `''`
   - `\S\`
   - `\P...\`
   - `\X\hh`
   - `\X2\hhhh...\X0\`
   - `\X4\hhhhhhhh...\X0\`
2. Improve syntax boundary handling:
   - Reject `NaN`, `Infinity`, and unsafe huge exponents.
   - Preserve original numeric literals.
   - Reject entity id overflow with clear messages.
   - Reject duplicate ids with exact id and position.
   - Improve EOF errors inside complex entities.
   - Support or explicitly reject multiple DATA sections.
3. Unify semantic resolver errors:
   - Missing references include the missing `#id` and referencing entity id.
   - Wrong arity includes entity type, id, expected, and actual.
   - Wrong parameter type includes entity type, id, parameter index or name, expected type, and actual type.
   - Unsupported entities produce structured warnings or explicit exceptions.
4. Preserve and use HEADER metadata:
   - schema
   - author
   - organization
   - timestamp
   - originating system
   - compatibility reporting

## P2: Geometry And Topology Reliability

1. Add a topology validator:
   - Closed shell edge usages must be paired with opposite usages.
   - Edge loops must close within tolerance.
   - Reversed oriented edges must swap start and end semantics.
   - Non-manifold edges must be detected and reported.
   - Open shells must not be labeled as valid solids.
2. Implement a consistent unit system:
   - Read STEP unit entities.
   - Convert mm, inch, and meter consistently.
   - Add inch STEP tests.
3. Add golden geometry tests:
   - cube
   - cylinder
   - sphere
   - plate with hole
   - assembly with two cubes
   - assert bbox, face count, shell count, and unsupported count
4. Fail or report unsupported geometry instead of returning wrong geometry:
   - boolean operations
   - half-space clipping
   - complex swept solids
   - incomplete tessellated geometry
   - invalid B-spline knots or weights
   - degenerate edges and faces

## P3: Preview And GLB

1. Validate generated GLB files:
   - GLB magic/header
   - version
   - chunk lengths
   - tiny model golden output
2. Improve viewer error handling:
   - Show parse/export errors in the UI.
   - Do not leave the loading spinner active after failure.
3. Dispose previous Three.js resources when loading a new model:
   - geometries
   - materials
   - textures
4. Add performance regression coverage:
   - `examples/engine.stp`
   - large tessellated model
   - avoid obvious quadratic behavior

## P4: Engineering Quality And Documentation

1. Add GitHub Actions CI:
   - Java 21
   - Maven cache
   - `mvn -B clean test`
2. Improve CLI behavior:
   - correct exit codes
   - `--json`
   - `--validate-only`
   - multiple input files
   - no full stack trace unless debug mode is enabled
3. Rewrite project status documentation:
   - experimental status
   - supported subset
   - known limitations
   - honest capability table
4. Add project docs:
   - `SECURITY.md`
   - `CONTRIBUTING.md`
   - architecture overview
   - troubleshooting

## Progress: P0 Viewer Verification Hardening

Date: 2026-06-08

Completed:

1. Added regression coverage for concurrent `/api/preview` requests with identical STEP input.
2. Fixed a Windows cache race where concurrent temp-to-final cache moves could surface as `AccessDeniedException`.
3. Added regression coverage for safe preview error responses:
   - client receives a generic message
   - response includes `requestId`
   - raw STEP details are not returned
4. Added host binding coverage for default loopback and explicit external host configuration.
5. Disabled preview request body prefix logging by default.
   - `minicad.preview.debugBodyPrefix=true` re-enables it for local debugging.
   - This keeps request metadata in logs without leaking STEP content fragments by default.

Verification:

1. `mvn -B clean test`
   - Result: pass
   - Tests: 1563 run, 0 failures, 0 errors, 0 skipped
2. `mvn -q exec:java -Dexec.args="examples/minimal-square.step"`
   - Result: pass
   - Entity count: 37
3. `mvn -q exec:java -Dexec.args="examples/engine.stp"`
   - Result: pass
   - Entity count: 93829

## Progress: I01 Example Regression Coverage

Date: 2026-06-08

Completed:

1. Added a parameterized regression test over all `examples/*.step`, `examples/*.stp`, and `examples/*.p21`.
2. The test uses the project STEP text reader before parser execution, so non-default encodings follow the same path as the app layer.
3. Current example coverage:
   - Files discovered: 45
   - Minimum assertion: every example parses and contains DATA entities

Verification:

1. `mvn -q -Dtest=StepExampleRegressionTest test`
   - Result: pass
2. `mvn -B clean test`
   - Result: pass
   - Tests: 1563 run, 0 failures, 0 errors, 0 skipped
3. `mvn -q exec:java -Dexec.args="examples/minimal-square.step"`
   - Result: pass
   - Entity count: 37
4. `mvn -q exec:java -Dexec.args="examples/engine.stp"`
   - Result: pass
   - Entity count: 93829

## Progress: H03 CLI Multiple Files

Date: 2026-06-08

Completed:

1. Updated `StepDumpApp` usage to accept one or more STEP paths: `StepDumpApp [--debug] <step-file>...`.
2. Split single-file processing into a reusable path so multi-file runs preserve the existing per-file summary output.
3. Multi-file CLI runs now continue after a file read, parse, resolution, topology, geometry, or unsupported-geometry failure.
4. Aggregate exit codes now return:
   - `0` when every requested file succeeds.
   - `1` when one or more requested files fail but argument parsing is valid.
   - `2` for invalid CLI arguments.
5. Added tests for all-success multi-file runs and mixed bad/good multi-file runs, including continued processing after the bad file.

Verification:

1. `mvn -q -Dtest=StepDumpAppTest test`
   - Result: pass
2. `mvn -B clean test`
   - Result: pass
   - Tests: 1596 run, 0 failures, 0 errors, 0 skipped
3. `mvn -q exec:java -Dexec.args="examples/minimal-square.step"`
   - Result: pass
   - Entity count: 37
4. `mvn -q exec:java -Dexec.args="examples/engine.stp"`
   - Result: pass
   - Entity count: 93829

## Progress: H05 CLI Validate Only

Date: 2026-06-08

Completed:

1. Added `--validate-only` to `StepDumpApp`.
2. Validate-only mode reads the STEP file, parses syntax, resolves semantic entities, and runs the existing build-summary validation path to exercise topology/geometry validation without printing the full build report.
3. Validate-only output now emits a concise `Validation Summary` with status, parsed entity count, resolved entity count, and aggregate build totals.
4. Updated CLI usage to `StepDumpApp [--debug] [--validate-only] <step-file>...`.
5. Added a focused test that verifies validate-only success output and confirms `Build Summary` / `Semantic Summary` are not printed in validate-only mode.

Verification:

1. `mvn -q -Dtest=StepDumpAppTest test`
   - Result: pass
2. `mvn -B clean test`
   - Result: pass
   - Tests: 1597 run, 0 failures, 0 errors, 0 skipped
3. `mvn -q exec:java -Dexec.args="examples/minimal-square.step"`
   - Result: pass
   - Entity count: 37
4. `mvn -q exec:java -Dexec.args="examples/engine.stp"`
   - Result: pass
   - Entity count: 93829

## Progress: H04 CLI JSON Summary Output

Date: 2026-06-08

Completed:

1. Added `--json` to `StepDumpApp`.
2. JSON mode emits one machine-readable document with:
   - top-level `status` and `exitCode`.
   - per-file `path`, `status`, `entityCount`, `resolvedCount`, `unsupportedCount`, and `bbox`.
   - per-file `error` for failed files.
3. JSON mode supports multiple input files and reports failures in JSON without writing the normal text error line to stderr.
4. Refactored CLI processing so text, validate-only, and JSON modes share the same parse/resolve/build-validation result.
5. CLI `main` now writes normal output to stdout and errors to stderr, which makes JSON mode usable for automation.
6. Added tests for successful JSON output and JSON failure output.

Verification:

1. `mvn -q -Dtest=StepDumpAppTest test`
   - Result: pass
2. `mvn -B clean test`
   - Result: pass
   - Tests: 1599 run, 0 failures, 0 errors, 0 skipped
3. `mvn -q exec:java -Dexec.args="examples/minimal-square.step"`
   - Result: pass
   - Entity count: 37
4. `mvn -q exec:java -Dexec.args="examples/engine.stp"`
   - Result: pass
   - Entity count: 93829
5. `mvn -q exec:java -Dexec.args="--json examples/minimal-square.step"`
   - Result: pass
   - JSON includes entityCount 37, unsupportedCount 0, bbox min `[0.0,0.0,0.0]`, bbox max `[1.0,1.0,0.0]`

## Progress: G05/G06/G07/G08 Viewer Frontend Hardening

Date: 2026-06-09

Completed:

1. Added browser-side file validation before upload:
   - accepts `.step`, `.stp`, and `.p21`.
   - rejects other extensions before calling `/api/preview`.
   - rejects files over the default 50 MB preview upload limit before calling `/api/preview`.
2. Added drag-and-drop support on the scene area using the same validation path as the file input.
3. Tightened viewer cleanup when loading a new model or handling an error:
   - clears stale product metadata.
   - clears stale unit metadata.
   - clears stale unsupported boolean/faces state.
4. Improved Three.js resource disposal:
   - disposes old geometries.
   - disposes old materials.
   - disposes material textures.
   - disposes source materials before replacing GLTF materials with viewer materials.
5. Fixed viewer module loading under CSP:
   - kept `script-src 'self'`.
   - allowed only the fixed inline import map by SHA-256 hash.
   - verified the browser creates a canvas and reports no module resolution errors.

Verification:

1. `mvn -q -Dtest=StepViewerStaticResourcesTest,StepViewerAppSecurityTest test`
   - Result: pass
2. Browser smoke test at `http://127.0.0.1:8081/?v=4`
   - Result: pass
   - Canvas count: 1
   - Recent console errors: 0

## Progress: G01 GLB Export Robustness

Date: 2026-06-09

Completed:

1. Added reusable GLB validation in `PreviewSerializers.validateGlb`.
2. Validation now checks:
   - non-null payload.
   - minimum length.
   - 4-byte total alignment.
   - magic `glTF`.
   - version `2`.
   - header length equals actual payload length.
   - JSON chunk type and bounds.
   - BIN chunk type and bounds when present.
   - no trailing bytes after the final chunk.
3. `PreviewSerializers.toGlb` validates generated output before returning it.
4. `StepPreviewJsonExporter.exportGlb` validates the final GLB before caching or sending it.
5. Added regression tests for valid tiny GLB output and malformed length/chunk headers.

Verification:

1. `mvn -q -Dtest=StepPreviewJsonExporterTest#shouldExportGlbPreviewPacketForMinimalSquare+glbValidatorShouldRejectMalformedHeadersAndChunks test`
   - Result: pass

## Progress: G04 Mesh Normal Generation

Date: 2026-06-09

Completed:

1. Added GLB mesh consistency validation before face meshes are written:
   - position buffer length must match vertex count.
   - normal buffer count and length must match position vertices.
   - indices must be non-empty triangle indices when a mesh has vertices.
   - indices must stay within the vertex range.
   - position and normal values must be finite.
   - generated normals must be unit length within tolerance.
2. Preserved compatibility with parameterized STEP faces that intentionally carry no pre-triangulated vertices and are rebuilt by the viewer from GLB extras.
3. Added a non-empty triangle GLB regression test that reads the GLB JSON/BIN chunks and verifies:
   - every mesh primitive includes a `NORMAL` accessor.
   - normal accessor count matches the `POSITION` accessor count.
   - exported normal vectors are normalized.
   - index accessor count remains a multiple of 3.

Verification:

1. `mvn -q -Dtest=StepPreviewJsonExporterTest#glbMeshesShouldIncludeNormalizedNormalsMatchingPositions+shouldExportGlbPreviewPacketForMinimalSquare test`
   - Result: pass

## Progress: G02 Unsupported Face Reporting

Date: 2026-06-09

Completed:

1. Added regression coverage for unsupported face reporting in GLB preview metadata:
   - `scene.extras.preview.stats.unsupportedFaceCount` carries the skipped face count.
   - `scene.extras.preview.unsupportedFaces` carries id, surface type, and reason.
   - `scene.extras.preview.issues` carries a structured warning with code `step.unsupported`.
2. Added static viewer coverage to ensure the UI warning path remains present:
   - unsupported face stat tile.
   - Unsupported Faces panel.
   - summary toggle.
   - surface-type and reason summaries.
   - GLB preview render path calls `updateUnsupportedFaces(preview.unsupportedFaces)`.

Verification:

1. `mvn -q -Dtest=PreviewSerializersIssueTest,StepViewerStaticResourcesTest test`
   - Result: pass

## Progress: J07 Maven Enforcer

Date: 2026-06-09

Completed:

1. Added `maven-enforcer-plugin` bound to the Maven `validate` phase.
2. Enforced Java version `[21,22)` so builds fail clearly outside the supported Java 21 runtime.
3. Enabled `dependencyConvergence` to catch conflicting transitive dependency versions.
4. Enabled `banDuplicatePomDependencyVersions` to prevent duplicate dependency declarations.
5. Resolved the convergence issue exposed by the new rule:
   - Jetty paths were resolving `org.slf4j:slf4j-api:2.0.9`.
   - Logback 1.5.18 was resolving `org.slf4j:slf4j-api:2.0.17`.
   - Added `slf4j.version` and dependency management to converge on `2.0.17`.

Verification:

1. `mvn -B validate`
   - Result: pass
   - Enforcer rules passed: Java version, dependency convergence, duplicate dependency versions
2. `mvn -B clean test`
   - Result: pass
   - Tests: 1599 run, 0 failures, 0 errors, 0 skipped
3. `mvn -q exec:java -Dexec.args="examples/minimal-square.step"`
   - Result: pass
   - Entity count: 37
4. `mvn -q exec:java -Dexec.args="examples/engine.stp"`
   - Result: pass
   - Entity count: 93829

## Progress: J03/K02/K03/K04/K05 Documentation And Security Batch

Date: 2026-06-09

Completed:

1. J03: Added `.github/workflows/codeql.yml` for Java CodeQL analysis on pushes, pull requests, and a weekly schedule.
2. K02: Added `SECURITY.md` covering the local viewer threat model, vulnerability reporting, upload/cache expectations, and safe operational guidance.
3. K03: Added `CONTRIBUTING.md` with Java 21 prerequisites, baseline commands, test expectations, entity support levels, and local fixture guidance.
4. K04: Added `doc/architecture.md` with the pipeline `STEP text -> syntax -> semantic model -> geometry/topology -> export` and guidance for adding entity support.
5. K05: Added `doc/troubleshooting.md` covering Java version errors, Maven validation, CLI usage, viewer startup, common STEP parse/resolution errors, and large-file guidance.

Verification:

1. `mvn -B clean test`
   - Result: pass
   - Tests run: 1599
   - Failures: 0
   - Errors: 0
   - Skipped: 0
2. `mvn -q exec:java -Dexec.args="examples/minimal-square.step"`
   - Result: pass
   - Entity count: 37
3. `mvn -q exec:java -Dexec.args="examples/engine.stp"`
   - Result: pass
   - Entity count: 93829

## Progress: I03 Negative Syntax Tests

Date: 2026-06-08

Completed:

1. Added negative syntax regression tests for missing DATA `ENDSEC`, unterminated STEP strings, and unterminated block comments.
2. Hardened section keyword scanning so an unterminated string before or inside DATA fails as `StepParseException` instead of being silently skipped into a misleading section error.
3. Confirmed the I03 set now covers missing DATA, missing ENDSEC, unterminated string, unterminated comment, bad number, duplicate id, missing reference, and bad entity arity through parser and resolver tests.

Verification:

1. `mvn -q -Dtest=StepParserTest test`
   - Result: pass
2. `mvn -B clean test`
   - Result: pass
   - Tests: 1584 run, 0 failures, 0 errors, 0 skipped
3. `mvn -q exec:java -Dexec.args="examples/minimal-square.step"`
   - Result: pass
   - Entity count: 37
4. `mvn -q exec:java -Dexec.args="examples/engine.stp"`
   - Result: pass
   - Entity count: 93829

## Progress: C04 Forward References

Date: 2026-06-08

Completed:

1. Confirmed the resolver supports references to entities declared later in the DATA section.
2. Added regression coverage for forward references inside reference lists, using an `EDGE_LOOP` that references later `ORIENTED_EDGE` entities.
3. Existing direct forward-reference coverage remains in `shouldResolveSupportedEntitiesWithForwardReferences`.

Verification:

1. `mvn -q -Dtest=StepEntityResolverTest test`
   - Result: pass
2. `mvn -B clean test`
   - Result: pass
   - Tests: 1581 run, 0 failures, 0 errors, 0 skipped
3. `mvn -q exec:java -Dexec.args="examples/minimal-square.step"`
   - Result: pass
   - Entity count: 37
4. `mvn -q exec:java -Dexec.args="examples/engine.stp"`
   - Result: pass
   - Entity count: 93829

## Progress: C05 Missing References

Date: 2026-06-08

Completed:

1. Resolver now tracks the entity currently being resolved.
2. Missing reference errors include both the missing `#id` and the referring entity `#id`.
3. Added semantic resolver tests for direct missing references and missing references inside reference lists.

Verification:

1. `mvn -q -Dtest=StepEntityResolverTest test`
   - Result: pass
2. `mvn -B clean test`
   - Result: pass
   - Tests: 1574 run, 0 failures, 0 errors, 0 skipped
3. `mvn -q exec:java -Dexec.args="examples/minimal-square.step"`
   - Result: pass
   - Entity count: 37
4. `mvn -q exec:java -Dexec.args="examples/engine.stp"`
   - Result: pass
   - Entity count: 93829

## Progress: C07 Wrong Parameter Count

Date: 2026-06-08

Completed:

1. Standardized parameter-count diagnostics through `StepParameterReader`.
2. `StepEntityResolver` now delegates its parameter-count wrappers to the shared reader, avoiding duplicate message formats.
3. Wrong arity errors now include entity id, entity type, expected count, and actual count.
4. Added tests for exact-count validation, one-of-count validation, and a real `CARTESIAN_POINT` resolver failure.

Verification:

1. `mvn -q -Dtest=StepParameterReaderTest,StepEntityResolverTest test`
   - Result: pass
2. `mvn -B clean test`
   - Result: pass
   - Tests: 1575 run, 0 failures, 0 errors, 0 skipped
3. `mvn -q exec:java -Dexec.args="examples/minimal-square.step"`
   - Result: pass
   - Entity count: 37
4. `mvn -q exec:java -Dexec.args="examples/engine.stp"`
   - Result: pass
   - Entity count: 93829

## Progress: C08 Wrong Parameter Type

Date: 2026-06-08

Completed:

1. Added a shared parameter type mismatch diagnostic helper in `StepParameterReader`.
2. Updated core `StepEntityResolver` scalar/reference/list readers to report entity id, entity type, parameter index, expected type, and actual STEP value type.
3. Added element-level diagnostics for list/grid parameters where the container type is correct but an inner value has the wrong type.
4. Added regression coverage for wrong string, list, and reference parameter types through real resolver paths.

Verification:

1. `mvn -q -Dtest=StepParameterReaderTest,StepEntityResolverTest test`
   - Result: pass
2. `mvn -B clean test`
   - Result: pass
   - Tests: 1579 run, 0 failures, 0 errors, 0 skipped
3. `mvn -q exec:java -Dexec.args="examples/minimal-square.step"`
   - Result: pass
   - Entity count: 37
4. `mvn -q exec:java -Dexec.args="examples/engine.stp"`
   - Result: pass
   - Entity count: 93829

## Progress: C03 Unsupported Entity Behavior

Date: 2026-06-08

Completed:

1. Unknown STEP entities now fail with an explicit `UnsupportedStepEntityException`.
2. The unsupported-entity diagnostic now includes the entity id and entity type, so silent loss is avoided and the source entity is directly traceable.
3. Added an unknown entity fixture to assert the exact unsupported behavior.

Verification:

1. `mvn -q -Dtest=StepEntityResolverTest test`
   - Result: pass
2. `mvn -B clean test`
   - Result: pass
   - Tests: 1580 run, 0 failures, 0 errors, 0 skipped
3. `mvn -q exec:java -Dexec.args="examples/minimal-square.step"`
   - Result: pass
   - Entity count: 37
4. `mvn -q exec:java -Dexec.args="examples/engine.stp"`
   - Result: pass
   - Entity count: 93829

## Progress: J01/J02 Continuous Integration

Date: 2026-06-08

Completed:

1. Added GitHub Actions workflow at `.github/workflows/ci.yml`.
2. CI uses Java 21 via `actions/setup-java@v4`.
3. CI enables Maven dependency caching through `cache: maven`.
4. CI runs `mvn -B clean test` on pull requests and pushes to `main` or `master`.

Verification:

1. `mvn -B clean test`
   - Result: pass
   - Tests: 1563 run, 0 failures, 0 errors, 0 skipped

## Progress: B06 Number Parsing Boundaries

Date: 2026-06-08

Completed:

1. Reject non-finite numeric literals produced by overflow, such as `1E9999`.
2. Reject bare special floating literals such as `NaN`, `INF`, and `Infinity` with clear parse errors.
3. Preserve the original numeric literal text in `StepValue.NumberValue.raw()`.

Verification:

1. `mvn -q -Dtest=StepParserTest test`
   - Result: pass
2. `mvn -B clean test`
   - Result: pass
   - Tests: 1566 run, 0 failures, 0 errors, 0 skipped
3. `mvn -q exec:java -Dexec.args="examples/minimal-square.step"`
   - Result: pass
   - Entity count: 37
4. `mvn -q exec:java -Dexec.args="examples/engine.stp"`
   - Result: pass
   - Entity count: 93829

## Progress: B05 Section Keyword Matching

Date: 2026-06-08

Completed:

1. Tightened `HEADER;`, `DATA;`, and `ENDSEC;` detection so keywords embedded inside longer words are not matched.
2. Added regression coverage for `SOMEDATA;` not being treated as `DATA;`.
3. Kept existing behavior that section keywords inside comments and strings are ignored.

Verification:

1. `mvn -q -Dtest=StepParserTest test`
   - Result: pass
2. `mvn -B clean test`
   - Result: pass
   - Tests: 1568 run, 0 failures, 0 errors, 0 skipped
3. `mvn -q exec:java -Dexec.args="examples/minimal-square.step"`
   - Result: pass
   - Entity count: 37
4. `mvn -q exec:java -Dexec.args="examples/engine.stp"`
   - Result: pass
   - Entity count: 93829

## Progress: B08/B10 Complex Entity EOF And DATA Section Policy

Date: 2026-06-08

Completed:

1. Added explicit EOF detection while parsing complex entity instances.
2. Complex entity EOF errors now include the opening parenthesis position.
3. Multiple `DATA` sections are explicitly rejected with a documented parser error instead of silently ignoring later sections.

Verification:

1. `mvn -q -Dtest=StepParserTest test`
   - Result: pass
2. `mvn -B clean test`
   - Result: pass
   - Tests: 1568 run, 0 failures, 0 errors, 0 skipped
3. `mvn -q exec:java -Dexec.args="examples/minimal-square.step"`
   - Result: pass
   - Entity count: 37
4. `mvn -q exec:java -Dexec.args="examples/engine.stp"`
   - Result: pass
   - Entity count: 93829

## Progress: B03 STEP String Escapes

Date: 2026-06-08

Completed:

1. Added STEP string escape decoding for doubled quotes, `\S\`, `\P...\`, `\X\hh`, `\X2\hhhh...\X0\`, and `\X4\hhhhhhhh...\X0\`.
2. Malformed string escapes now fail with `StepParseException` and source positions instead of being silently preserved.
3. Updated semantic regression expectations so escaped STEP metadata resolves to Unicode text.

Verification:

1. `mvn -q -Dtest=StepParserTest test`
   - Result: pass
2. `mvn -q -Dtest=StepParserTest,StepEntityResolverTest test`
   - Result: pass
3. `mvn -B clean test`
   - Result: pass
   - Tests: 1570 run, 0 failures, 0 errors, 0 skipped
4. `mvn -q exec:java -Dexec.args="examples/minimal-square.step"`
   - Result: pass
   - Entity count: 37
5. `mvn -q exec:java -Dexec.args="examples/engine.stp"`
   - Result: pass
   - Entity count: 93829

## Progress: B07 Entity Id Bounds

Date: 2026-06-08

Completed:

1. Defined the supported STEP entity/reference id range as `1..2147483647`, matching the current `int`-based syntax and semantic model.
2. Replaced direct `Integer.parseInt` id parsing with bounded `BigInteger` validation so huge ids fail with clear messages.
3. Rejected zero, negative, explicit-plus, and overflow entity ids; rejected zero and overflow reference ids.

Verification:

1. `mvn -q -Dtest=StepParserTest test`
   - Result: pass
2. `mvn -B clean test`
   - Result: pass
   - Tests: 1572 run, 0 failures, 0 errors, 0 skipped
3. `mvn -q exec:java -Dexec.args="examples/minimal-square.step"`
   - Result: pass
   - Entity count: 37
4. `mvn -q exec:java -Dexec.args="examples/engine.stp"`
   - Result: pass
   - Entity count: 93829

## Progress: C06 Duplicate Entity Ids

Date: 2026-06-08

Completed:

1. Moved duplicate entity id detection into `StepParser.parseFile()` so malformed STEP input is rejected during parsing.
2. Duplicate id errors now include the duplicate declaration position and the first declaration position.
3. Kept `StepFile.entitiesById()` duplicate checking as a defensive fallback for manually constructed `StepFile` instances.

Verification:

1. `mvn -q -Dtest=StepParserTest,StepEntityResolverTest test`
   - Result: pass
2. `mvn -B clean test`
   - Result: pass
   - Tests: 1573 run, 0 failures, 0 errors, 0 skipped
3. `mvn -q exec:java -Dexec.args="examples/minimal-square.step"`
   - Result: pass
   - Entity count: 37
4. `mvn -q exec:java -Dexec.args="examples/engine.stp"`
   - Result: pass
   - Entity count: 93829

## Progress: C09 Dollar Vs Star Semantics

Date: 2026-06-08

Completed:

1. Added `StepParameterReader.isOmitted()` and `StepParameterReader.isNotProvided()` so callers can distinguish `$` from `*` instead of only using the combined unset predicate.
2. Kept `isUnset()` as the intentional combined predicate for optional fields, now implemented through the explicit omitted/not-provided helpers.
3. Delegated `StepEntityResolver.isUnset()` to `StepParameterReader` to keep `$/*` handling centralized.
4. Added unit validation for complex entities that include `NAMED_UNIT`; `NAMED_UNIT` dimensions now accept `$`, `*`, or `#DIMENSIONAL_EXPONENTS`, and reject other value types even when resolved through `SI_UNIT`, `CONVERSION_BASED_UNIT`, or `CONTEXT_DEPENDENT_UNIT`.
5. Added representative tests for helper predicates, inherited `ORIENTED_EDGE` endpoints using `$` and `*`, omitted `NAMED_UNIT` dimensions, referenced dimensions, and invalid dimension value types.

Verification:

1. `mvn -q -Dtest=StepParameterReaderTest,StepEntityResolverTest test`
   - Result: pass
2. `mvn -B clean test`
   - Result: pass
   - Tests: 1590 run, 0 failures, 0 errors, 0 skipped
3. `mvn -q exec:java -Dexec.args="examples/minimal-square.step"`
   - Result: pass
   - Entity count: 37
4. `mvn -q exec:java -Dexec.args="examples/engine.stp"`
   - Result: pass
   - Entity count: 93829

## Progress: H01/H02 CLI Exit Codes And Errors

Date: 2026-06-08

Completed:

1. `StepDumpApp.main()` no longer declares `IOException`; file-read failures are handled through the normal CLI error path instead of falling through to a Java stack trace.
2. `StepDumpApp.run()` returns:
   - `0` for successful processing.
   - `1` for file read, parse, resolution, topology, geometry, and unsupported-geometry processing errors.
   - `2` for invalid CLI arguments.
3. CLI error messages now include the input file path and concise cause.
4. Added `--debug`; stack traces are suppressed by default and emitted only when debug is explicitly enabled.
5. Added direct `run()` tests and a `ProcessBuilder` test that launches `com.minicad.app.StepDumpApp` and verifies the actual process exit code for a parse failure.

Verification:

1. `mvn -q -Dtest=StepDumpAppTest test`
   - Result: pass
2. `mvn -B clean test`
   - Result: pass
   - Tests: 1594 run, 0 failures, 0 errors, 0 skipped
3. `mvn -q exec:java -Dexec.args="examples/minimal-square.step"`
   - Result: pass
   - Entity count: 37
4. `mvn -q exec:java -Dexec.args="examples/engine.stp"`
   - Result: pass
   - Entity count: 93829
