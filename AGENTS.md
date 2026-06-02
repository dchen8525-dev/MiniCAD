# MiniCAD Overnight Fix Queue

Repo: https://github.com/dchen8525-dev/MiniCAD

Goal: keep fixing until all high/medium/low items are resolved or explicitly documented as unsupported.

## Priority 0: 必须先跑基线

1. Run:
   - `mvn -B clean test`
   - `mvn -q exec:java -Dexec.args="examples/minimal-square.step"`
   - `mvn -q exec:java -Dexec.args="examples/engine.stp"`
2. Record current failures.
3. Do not remove tests to make build pass.
4. For every fix, add or update tests.

---

# A. Security / DoS / Web Viewer

## A01. `/api/preview` body 无大小限制

Problem: `StepViewerApp.PreviewServlet` reads multipart and normal request body with `readAllBytes()`, so huge uploads can cause OOM. The servlet also sets `MultipartConfigElement` without explicit size limits. :contentReference[oaicite:0]{index=0}

Fix:
- Add max upload size, default 50MB.
- Configurable via system property: `minicad.preview.maxUploadBytes`.
- Return HTTP 413 for oversized request.
- Add bounded stream reader.

Verify:
- Test normal small STEP succeeds.
- Test oversized raw body returns 413.
- Test oversized multipart file returns 413.

## A02. `/api/example?name=` 路径穿越

Problem: unknown example name maps to `Path.of("examples", name + ".step")`; names containing `../`, `/`, `\`, absolute paths, or encoded traversal should be rejected. :contentReference[oaicite:1]{index=1}

Fix:
- Allow only `[A-Za-z0-9._-]+`.
- Normalize and ensure path remains inside `examples`.
- Invalid name returns 400.

Verify:
- `../pom`, `../../etc/passwd`, `..\\pom`, `%2e%2e/pom` fail.
- `minimal-square` and `plate-with-round-hole` still work.

## A03. Preview cache 无上限，磁盘可被打满

Problem: `.minicad-cache/preview-glb-v1/{sha256}.glb` grows forever. :contentReference[oaicite:2]{index=2}

Fix:
- Add max cache bytes, default 1GB.
- Configurable via `minicad.preview.cache.maxBytes`.
- Use LRU by last modified time.
- Clean after writes and optionally at startup.

Verify:
- Generate many cache files beyond limit.
- Old files deleted.
- Cache hit still returns `X-MiniCAD-Cache: hit`.

## A04. Cache write 非原子

Problem: concurrent same STEP requests may partially write or race on same `.glb`.

Fix:
- Write to temp file.
- Atomic move to final path.
- Handle existing file safely.

Verify:
- Concurrent same input returns valid GLB every time.

## A05. Cache path 泄露

Problem: response exposes `X-MiniCAD-Cache-Path`, leaking local filesystem path. :contentReference[oaicite:3]{index=3}

Fix:
- Remove this header by default.
- Add debug-only property if needed.

Verify:
- Header absent in normal response.

## A06. Viewer 默认绑定不明确

Problem: startup message says `127.0.0.1`, but `new Server(port)` may bind more broadly depending Jetty behavior. :contentReference[oaicite:4]{index=4}

Fix:
- Explicitly bind `127.0.0.1`.
- Add optional `--host=` for external bind.
- Print warning for non-loopback host.

Verify:
- Default listens only on loopback.
- `--host=0.0.0.0` works intentionally.

## A07. StaticServlet 一次性 `readAllBytes()`

Problem: static resources are read fully into memory. Small now, but avoid bad pattern. :contentReference[oaicite:5]{index=5}

Fix:
- Stream static resources to response.
- Set content length only when available.

Verify:
- `/`, `/viewer.js`, `/vendor/*` still work.

## A08. 缺少 HTTP 安全头

Fix:
- Add:
  - `X-Content-Type-Options: nosniff`
  - `Referrer-Policy: no-referrer`
  - `Content-Security-Policy`
  - `Cross-Origin-Resource-Policy: same-origin`

Verify:
- Tests assert headers on static and API responses.

## A09. 错误信息可能泄露内部细节

Problem: parse/geometry exceptions are returned directly to browser. :contentReference[oaicite:6]{index=6}

Fix:
- Return safe message to client.
- Log detailed diagnostic server-side.
- Include request id in client error.

Verify:
- Bad STEP returns generic + requestId.
- Logs still include diagnostic context.

## A10. 请求日志可能泄露 STEP 内容

Problem: diagnostic context logs source excerpts around parse position. :contentReference[oaicite:7]{index=7}

Fix:
- Disable source excerpt logging by default.
- Enable only with debug property.

Verify:
- Parse failure logs no STEP content by default.

---

# B. STEP Text / Encoding / Parser

## B01. Tokenizer 是 restricted subset

Problem: `StepTokenizer` explicitly says it is a minimal tokenizer for a restricted STEP subset. :contentReference[oaicite:8]{index=8}

Fix:
- Either complete STEP lexical support or update docs to avoid overclaiming.
- Add tests for comments, strings, numbers, enums, typed params.

## B02. Parser 是 minimal DATA parser

Problem: `StepParser` says it is a minimal parser for STEP DATA section, while README describes a complete parse chain. :contentReference[oaicite:9]{index=9}

Fix:
- Expand parser support or document exact limitations.
- Add parser compatibility test suite.

## B03. STEP string escape 支持不足

Fix:
- Support:
  - doubled single quote `''`
  - `\S\`
  - `\P...\`
  - `\X\hh`
  - `\X2\hhhh...\X0\`
  - `\X4\hhhhhhhh...\X0\`
- Reject malformed escape with position.

Verify:
- Chinese, Japanese, German umlaut, emoji-like Unicode where valid.
- Invalid hex throws `StepParseException`.

## B04. HEADER 信息解析后利用不足

Problem: parser reads HEADER entries, but many downstream components likely ignore FILE_SCHEMA / FILE_NAME / units. :contentReference[oaicite:10]{index=10}

Fix:
- Preserve header metadata.
- Expose schema, author, organization, timestamp, originating system.
- Use schema in compatibility reporting.

Verify:
- AP214/AP242 header test.

## B05. `findKeywordOutsideStringsAndComments` 可能误匹配单词内部

Problem: it uses case-insensitive `regionMatches` for `HEADER;`, `DATA;`, `ENDSEC;` without full STEP section-state validation. :contentReference[oaicite:11]{index=11}

Fix:
- Require valid section tokens.
- Reject multiple DATA sections unless explicitly supported.
- Better errors for malformed section order.

Verify:
- `ENDSEC;` inside string/comment ignored.
- `SOMEDATA;` does not match `DATA;`.

## B06. 数字解析缺少边界检查

Fix:
- Reject NaN/Infinity.
- Handle huge exponents clearly.
- Preserve original literal.
- Better messages for invalid numbers.

Verify:
- `1E9999`, `NaN`, malformed exponent fail safely.

## B07. Entity id 使用 int 可能溢出

Problem: `parseInteger` uses `Integer.parseInt`. :contentReference[oaicite:12]{index=12}

Fix:
- Decide max supported id.
- Use long internally or reject with clear message.
- Add duplicate / overflow tests.

## B08. Complex entity 空循环风险

Problem: `parseComplexEntity()` loops until RPAREN. On malformed EOF, error quality may be poor. :contentReference[oaicite:13]{index=13}

Fix:
- Detect EOF inside complex entity.
- Include opening position.

## B09. Typed value only wraps single value

Problem: `parseTypedValue()` parses only one wrapped `StepValue`. :contentReference[oaicite:14]{index=14}

Fix:
- Confirm STEP typed parameter grammar.
- Support typed value parameter lists if required by real files.
- Add fixtures.

## B10. Missing support for multiple DATA sections

Fix:
- Either support `DATA; ... ENDSEC; DATA; ... ENDSEC;`
- Or explicitly reject with documented message.

---

# C. STEP Semantic Resolver / Model Coverage

## C01. README 与 AGENTS 统计冲突

Problem: README says 1175 model classes and 1324 registry calls, while AGENTS says 1062 model classes and ~1559 registry calls. :contentReference[oaicite:15]{index=15}

Fix:
- Add script to count:
  - model classes
  - registered entities
  - resolver-supported entities
  - builder-supported entities
  - exporter-supported entities
- README/AGENTS must use generated numbers only.

## C02. “注册”与“真正支持”混淆

Problem: AGENTS explicitly says distinguish resolver coverage from builder/exporter coverage. :contentReference[oaicite:16]{index=16}

Fix:
- Create capability matrix:
  - parsed
  - resolved
  - built
  - exported
  - tested
- Update README tables.

## C03. Unsupported entity behavior inconsistent

Fix:
- Every unsupported entity should produce structured warning or explicit exception.
- No silent geometry loss.

Verify:
- Unknown entity fixture.
- Known-but-unbuilt entity fixture.

## C04. Forward references

Fix:
- Ensure references to later ids resolve.
- Add tests.

## C05. Missing references

Fix:
- Error should include missing `#id` and referencing entity id.

## C06. Duplicate entity ids

Fix:
- Reject duplicates with exact id and position.

## C07. Wrong parameter count

Fix:
- Every entity factory should validate arity.
- Error includes entity type, id, expected, actual.

## C08. Wrong parameter type

Fix:
- Error includes entity type, id, parameter index/name, expected type, actual type.

## C09. `$` vs `*` semantics

Fix:
- Audit all factories for omitted `$` and not-provided `*`.
- Add tests per common entity.

## C10. Select type handling incomplete

Fix:
- Audit SELECT fields.
- Add typed select decoding helper.
- Add tests for representative AP242 selects.

---

# D. Geometry Correctness

## D01. Boolean operations correctness incomplete

Problem: AGENTS says CSG boolean operations are partially built/exported but still need correctness/completeness work. :contentReference[oaicite:17]{index=17}

Fix:
- Add correctness tests for union/difference/intersection.
- Validate bbox, shell count, face count.
- Return unsupported instead of wrong geometry where exact boolean not implemented.

## D02. Swept solids correctness incomplete

Problem: AGENTS lists EXTRUDED/REVOLVED/SURFACE_CURVE swept solids as needing correctness/completeness. :contentReference[oaicite:18]{index=18}

Fix:
- Test rectangle extrusion.
- Test circular profile extrusion.
- Test revolve 90/180/360 degrees.
- Handle negative depth / invalid axis.

## D03. Half-space clipping incomplete

Problem: AGENTS lists HALF_SPACE_SOLID / BOXED_HALF_SPACE / POLYGONAL_BOUNDED_HALF_SPACE as incomplete. :contentReference[oaicite:19]{index=19}

Fix:
- Implement robust plane clipping or explicitly reject unsupported cases.

## D04. Tessellated geometry correctness incomplete

Problem: AGENTS lists TESSELLATED_FACE_SET / TESSELLATED_FACE / TESSELLATED_TRIANGLE as incomplete. :contentReference[oaicite:20]{index=20}

Fix:
- Validate indices.
- Reject out-of-range indices.
- Handle orientation.
- Preserve normals/colors if present.

## D05. Advanced volumes incomplete

Problem: AGENTS lists CYLINDER_VOLUME, SPHERE_VOLUME, TORUS_VOLUME, cone/cylinder/prism volumes as incomplete. :contentReference[oaicite:21]{index=21}

Fix:
- Add triangulation/export tests.
- Validate radii > 0, dimensions > 0.

## D06. B-Spline knot validation

Fix:
- Validate degree, knot multiplicities, control point dimensions.
- Reject inconsistent arrays.

## D07. Rational B-Spline weights validation

Fix:
- Weight count must match control points.
- Reject zero/negative invalid weights if geometry requires.

## D08. Curve trimming orientation

Fix:
- Test TRIMMED_CURVE sense agreement.
- Handle parameter vs cartesian trim values.

## D09. Surface bounds orientation

Fix:
- Validate FACE_BOUND orientation.
- Ensure holes are wound opposite outer loop.

## D10. Degenerate geometry

Fix:
- Detect zero-length edges.
- Detect zero-area faces.
- Decide whether to skip with warning or fail.

---

# E. Topology / B-Rep

## E01. Closed shell validation

Fix:
- Every edge in closed shell should have matching opposite usage.
- Add shell validator.

## E02. Open shell handling

Fix:
- Preview/export open shell separately from solid.
- Do not label open shell as valid solid.

## E03. Oriented edge semantics

Fix:
- Ensure reversed oriented edge swaps start/end.
- Test loops with mixed orientation.

## E04. Edge loop closure

Fix:
- Validate consecutive edge endpoints connect within tolerance.
- Error includes loop id and edge ids.

## E05. Vertex tolerance

Fix:
- Centralize tolerance policy.
- Avoid random epsilon comparisons.

## E06. Manifold check

Fix:
- Detect non-manifold edges.
- Report warnings.

## E07. BREP_WITH_VOIDS

Fix:
- Validate inner void shells.
- Ensure void orientation is correct.

## E08. Units

Fix:
- Read STEP unit entities.
- Convert mm/inch/meter consistently.
- Add tests for inch STEP.

---

# F. Assembly / Transform

## F01. MAPPED_ITEM transform correctness

README says mapped items and transformations are fully supported, but this needs tests. :contentReference[oaicite:22]{index=22}

Fix:
- Add nested transform tests.
- Rotation + translation.
- Multiple instances of same part.

## F02. NEXT_ASSEMBLY_USAGE_OCCURRENCE metadata

Fix:
- Preserve assembly tree names.
- Export assembly hierarchy to preview metadata.

## F03. Transformation matrix validation

Fix:
- Reject non-orthogonal axes or normalize safely.
- Handle missing ref direction.

## F04. Unit transform interaction

Fix:
- Assembly transform must apply after unit conversion consistently.

---

# G. Preview / GLB / Viewer

## G01. GLB exporter robustness

Fix:
- Validate generated GLB header.
- Add snapshot/golden tests for tiny model.

## G02. Unsupported face count

AGENTS says preview tests should cover rendered counts and `unsupportedFaceCount`. :contentReference[oaicite:23]{index=23}

Fix:
- Add tests for unsupported face reporting.
- UI shows warning clearly.

## G03. Large model performance

Fix:
- Add benchmark-ish test for large tessellated model.
- Avoid O(n²) where possible.

## G04. Mesh normal generation

Fix:
- Ensure normals are generated consistently.
- Handle flipped faces.

## G05. Viewer memory cleanup

Fix:
- When loading new model, dispose old Three.js geometries/materials/textures.

## G06. Viewer error handling

Fix:
- Show parse/export errors in UI.
- Do not leave spinner forever.

## G07. Drag-and-drop validation

Fix:
- Accept `.step`, `.stp`, `.p21`.
- Reject obvious non-text/non-STEP.

## G08. Browser-side file size precheck

Fix:
- Warn before uploading huge files.
- Same limit as server.

---

# H. CLI / Apps

## H01. CLI exit codes

Fix:
- Success returns 0.
- Parse/resolution errors return non-zero.
- Add tests with process launcher if feasible.

## H02. CLI error messages

Fix:
- Include file path and cause.
- Avoid full stack trace unless `--debug`.

## H03. CLI supports multiple files

Fix:
- Allow multiple STEP paths.
- Print per-file summary.

## H04. Add `--json` summary output

Fix:
- Useful for automation.
- Include entity count, unsupported count, bbox.

## H05. Add `--validate-only`

Fix:
- Parse + resolve + topology validate, no export.

---

# I. Tests / Fixtures

## I01. Run all examples as regression tests

Problem: examples exist, AGENTS says examples folder has STEP sample files. :contentReference[oaicite:24]{index=24}

Fix:
- Parameterized test over `examples/*.step`, `*.stp`, `*.p21`.
- At least parse all.
- Resolve/export where supported.

## I02. Add real-world STEP corpus harness

Fix:
- `src/test/resources/step/realworld/README.md`
- Allow ignored large fixtures.
- Document where to place local proprietary files without committing them.

## I03. Negative syntax tests

Add tests for:
- missing DATA
- missing ENDSEC
- unterminated string
- unterminated comment
- bad number
- duplicate id
- missing reference
- bad entity arity

## I04. Golden bbox tests

Add small fixtures with expected bbox:
- cube
- cylinder
- sphere
- plate with hole
- assembly with two cubes

## I05. Property-like parser tests

Fix:
- Generate random simple entity lists.
- Parse and validate no crash.

## I06. Multipart servlet tests

Fix:
- Test upload through embedded Jetty or servlet harness.

## I07. Cache tests

Fix:
- temp cache dir property.
- hit/miss/eviction tests.

---

# J. CI / Build / Quality

## J01. Add GitHub Actions CI

Problem: repo has Actions tab, but no obvious workflow in root listing. :contentReference[oaicite:25]{index=25}

Fix:
- `.github/workflows/ci.yml`
- Java 21
- `mvn -B clean test`

## J02. Add dependency cache

Fix:
- Use `actions/setup-java` Maven cache.

## J03. Add CodeQL or dependency review

Fix:
- Enable Java CodeQL workflow if appropriate.

## J04. Maven dependency versions

Problem: pom uses Jetty 11.0.24, logback 1.5.18, fastjson2 2.0.56, JUnit 5.10.2. :contentReference[oaicite:26]{index=26}

Fix:
- Check for newer safe versions.
- Keep Java 21 compatibility.
- Run tests.

## J05. Add formatter/checkstyle

Fix:
- Add Spotless or Checkstyle.
- Do not reformat generated files blindly.

## J06. Add forbidden APIs check

Fix:
- Avoid accidental `readAllBytes()` on untrusted input.
- Avoid system path leaks.

## J07. Add Maven Enforcer

Fix:
- Require Java 21.
- Ban duplicate dependencies.

---

# K. Documentation

## K01. README overclaims industrial completeness

Problem: README says complete STEP parsing and industrial CAD compatibility, while AGENTS says many areas still need correctness/completeness. :contentReference[oaicite:27]{index=27}

Fix:
- Rewrite project status:
  - experimental
  - supported subset
  - known limitations
- Keep honest capability table.

## K02. Add SECURITY.md

Fix:
- Explain local viewer threat model.
- Reporting process.
- File upload limits.

## K03. Add CONTRIBUTING.md

Fix:
- Build/test commands.
- Test requirements.
- Entity support policy.

## K04. Add architecture diagram text

Fix:
- `syntax -> semantic model -> geometry/topology -> exporter`.
- Explain where to add new entity support.

## K05. Add troubleshooting

Fix:
- Java version.
- Maven commands.
- Common parse errors.
- Large file limitations.

---

# L. Internal Code Quality

## L01. Replace broad mutable lists with immutable outputs

Fix:
- Ensure parser/model returns `List.copyOf`.
- Audit places returning mutable internals.

## L02. Centralize diagnostics

Fix:
- `Diagnostic` or `MiniCadIssue` type:
  - severity
  - code
  - entity id
  - message

## L03. Centralize capability reporting

Fix:
- `CapabilityRegistry`.
- Used by CLI, viewer, docs generator.

## L04. Avoid catch-all geometry swallowing

Fix:
- Do not silently skip failed faces.
- Collect warning with reason.

## L05. Add request id MDC logging

Fix:
- Use SLF4J MDC for viewer request id.
- Cleaner logs.

## L06. Thread safety audit

Fix:
- Shared caches, registries, exporters must be immutable or synchronized.

## L07. Config object for viewer

Fix:
- Parse port/host/cache/upload/debug into `ViewerConfig`.

## L08. Better argument parser

Fix:
- Support:
  - `--port`
  - `--host`
  - `--cache-dir`
  - `--max-upload`
  - `--no-cache`
  - `--debug`

---

# M. Extra Long-Running Codex Tasks

## M01. Build capability scanner

Create `tools/scan-capabilities` or Maven exec class that outputs JSON:
- all model classes
- all registry names
- all entity factories
- all builder handlers
- all exporter handlers
- test coverage by entity if detectable

## M02. Generate markdown coverage report

Output:
- `doc/generated/coverage.md`
- `doc/generated/unsupported-ap242.md`

## M03. Add AP203/AP214/AP242 schema diff tooling

Use files in `schemas` directory.
Compare schema entity names with implementation registry.

## M04. Add fixture minimizer

Tool:
- input failing STEP
- output minimal subset preserving failure
Useful for debugging real CAD files.

## M05. Add fuzz target

Simple parser fuzz:
- random tokens
- must never hang or OOM
- must fail with `StepParseException`

---

# Final Verification

After every batch:

```bash
mvn -B clean test
mvn -q exec:java -Dexec.args="examples/minimal-square.step"
mvn -q exec:java -Dexec.args="examples/engine.stp"
mvn "-Dexec.mainClass=com.minicad.app.StepViewerApp" exec:java