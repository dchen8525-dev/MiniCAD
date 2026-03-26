# Repository Guidelines

## Project Structure & Module Organization

- `src/main/java/com/minicad/common`: shared exceptions, epsilon, validation helpers.
- `src/main/java/com/minicad/geometry`: minimal geometry types such as `CartesianPoint`, `Curve3`, `Line3`, `Plane`, `Circle`, `CylindricalSurface`.
- `src/main/java/com/minicad/topology`: minimal B-Rep topology such as `Vertex`, `Edge`, `Face`, `Shell`.
- `src/main/java/com/minicad/step/syntax`: STEP tokenizer, parser, and raw AST.
- `src/main/java/com/minicad/step/model`: resolved STEP semantic records.
- `src/main/java/com/minicad/step/semantic`: resolver and CAD object builder.
- `src/main/java/com/minicad/app`: CLI entry points, preview exporter, and Jetty-based local viewer.
- `src/main/resources/static`: viewer HTML/JS and vendored Three.js assets.
- `src/test/java`: JUnit 5 tests mirroring the main package layout.
- `examples`: small STEP files for manual CLI runs and browser preview demos.

## Build, Test, and Development Commands

- `mvn test`: compile and run the full JUnit 5 suite.
- `mvn -q test`: quieter test run for quick checks.
- `mvn exec:java -Dexec.args="examples/minimal-square.step"`: run the CLI demo on the example STEP file.
- `mvn exec:java -Dexec.args="examples/plate-with-round-hole.step"`: run the CLI demo on the richer planar example.
- `mvn "-Dexec.mainClass=com.minicad.app.StepViewerApp" exec:java`: start the local Jetty viewer on `http://127.0.0.1:8080`.
- `mvn clean test`: rebuild from scratch when changing parser/model code.

## Coding Style & Naming Conventions

- Use Java 21 features already present in the repo, especially `record` where immutability is natural.
- Keep indentation at 4 spaces and use standard Java brace style.
- Prefer small, explicit classes over generic frameworks or deep inheritance.
- Name tests after the class under test, for example `StepParserTest`, `FaceTest`.
- Use clear exception messages; unsupported behavior must fail explicitly, not silently.

## Testing Guidelines

- Framework: JUnit 5 only.
- Add tests for both happy path and failure path.
- For STEP work, cover forward references, missing references, illegal syntax, unsupported entities/forms, and partial-support boundaries.
- Keep tests deterministic and small; prefer inline STEP snippets unless a reusable file under `examples/` is clearer.
- When adding preview/export behavior, test both rendered counts and explicitly skipped `unsupportedFaceCount` behavior.

## Commit & Pull Request Guidelines

- This repository currently has no commit history, so use a simple conventional style such as:
  - `feat: add STEP resolver for EDGE_LOOP`
  - `test: cover duplicate entity ids`
  - `docs: translate README to Chinese`
- Keep each commit focused on one logical change.
- PRs should include: purpose, scope, commands run (`mvn test`), and any intentional unsupported behavior.

## Architecture Notes

- Preserve the current layering: `syntax -> semantic model -> internal geometry/topology`.
- Preserve the current viewer split: Java resolves and exports preview data; the browser only renders exported JSON.
- Do not claim full STEP support.
- `ADVANCED_FACE` semantic resolution currently accepts `PLANE` and `CYLINDRICAL_SURFACE`, but only planar faces are actually built/rendered.
- `CYLINDRICAL_SURFACE` is currently parse/build-only geometry. Do not present it as meshed, triangulated, or rendered support.
- `EDGE_CURVE` topology currently supports `LINE` and `CIRCLE`; if you add more curves, wire them through `Curve3` and update preview/export sampling explicitly.
- If a feature approaches industrial CAD complexity, narrow scope and document the limitation instead of faking support.
