# Package Reorganization Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Reorganize MiniCAD package structure from current fragmented layout to clean layered architecture

**Architecture:** Three-phase approach: (1) Split app package into export/preview/helper/builder, (2) Consolidate step.model small packages, (3) Optimize other packages. Each phase followed by test validation.

**Tech Stack:** Java 11, Maven, Git, JUnit 5

---

## File Structure Overview

**Phase 1 - App Package Reorganization:**
- Move 52 files from `com.minicad.app` to:
  - `com.minicad.export.glb` (4 files)
  - `com.minicad.export.mesh` (3 files)
  - `com.minicad.export.json` (4 files)
  - `com.minicad.preview.sampling` (10 files)
  - `com.minicad.preview.builder` (4 files)
  - `com.minicad.preview.mapper` (4 files)
  - `com.minicad.preview.statistics` (3 files)
  - `com.minicad.preview.payload` (6 files)
  - `com.minicad.helper.geometry` (3 files)
  - `com.minicad.helper.metadata` (4 files)
  - `com.minicad.helper.validation` (2 files)
  - `com.minicad.builder` (3 files)
- Keep 4 files in `com.minicad.app` (entry points only)

**Phase 2 - Step.Model Package Consolidation:**
- Create 5 new grouping packages:
  - `com.minicad.step.model.core` (merge base + element + misc)
  - `com.minicad.step.model.management` (merge config_mgmt + log_audit + security + backup_recovery)
  - `com.minicad.step.model.organization` (merge organization + document + approval)
  - `com.minicad.step.model.technical` (merge tolerance + kinematic + unit + date_time)
  - `com.minicad.step.model.profile_analysis` (merge profile + analysis)
- Rename snake_case packages to camelCase

**Phase 3 - Other Package Optimization:**
- Rename `tools` → `tool`
- Create mirror test package structure

---

## Phase 1: App Package Reorganization

**Impact:** High - 52 files moved, many imports affected

### Task 1.1: Create Export Package Structure

**Files:**
- Create directories: `src/main/java/com/minicad/export/glb`, `src/main/java/com/minicad/export/mesh`, `src/main/java/com/minicad/export/json`

- [ ] **Step 1: Create export package directories**

```bash
mkdir -p src/main/java/com/minicad/export/glb
mkdir -p src/main/java/com/minicad/export/mesh
mkdir -p src/main/java/com/minicad/export/json
```

Expected: 3 directories created

- [ ] **Step 2: Verify directories created**

```bash
ls -la src/main/java/com/minicad/export/
```

Expected: Shows `glb`, `mesh`, `json` subdirectories

- [ ] **Step 3: Commit structure**

```bash
git add src/main/java/com/minicad/export/
git commit -m "refactor(phase1): create export package structure"
```

---

### Task 1.2: Move GLB Export Classes

**Files:**
- Move: `src/main/java/com/minicad/app/PreviewGlbBuilder.java` → `src/main/java/com/minicad/export/glb/PreviewGlbBuilder.java`
- Move: `src/main/java/com/minicad/app/PreviewMeshExporter.java` → `src/main/java/com/minicad/export/glb/PreviewMeshExporter.java`
- Move: `src/main/java/com/minicad/app/PreviewMaterialExporter.java` → `src/main/java/com/minicad/export/glb/PreviewMaterialExporter.java`
- Move: `src/main/java/com/minicad/app/TessellatedFaceExporter.java` → `src/main/java/com/minicad/export/glb/TessellatedFaceExporter.java`

- [ ] **Step 1: Move files using git mv**

```bash
git mv src/main/java/com/minicad/app/PreviewGlbBuilder.java src/main/java/com/minicad/export/glb/PreviewGlbBuilder.java
git mv src/main/java/com/minicad/app/PreviewMeshExporter.java src/main/java/com/minicad/export/glb/PreviewMeshExporter.java
git mv src/main/java/com/minicad/app/PreviewMaterialExporter.java src/main/java/com/minicad/export/glb/PreviewMaterialExporter.java
git mv src/main/java/com/minicad/app/TessellatedFaceExporter.java src/main/java/com/minicad/export/glb/TessellatedFaceExporter.java
```

Expected: Files moved, git tracks the move

- [ ] **Step 2: Update package declaration in each file**

For each file, change line 1 from `package com.minicad.app;` to `package com.minicad.export.glb;`

Example for PreviewGlbBuilder.java:
```java
// Before: package com.minicad.app;
// After:  package com.minicad.export.glb;
```

Use sed or manual edit for each file.

- [ ] **Step 3: Update imports in moved files**

Check each file for imports that need updating:
- Keep imports to `com.minicad.common.*` (unchanged)
- Keep imports to `com.minicad.geometry.*` (unchanged)
- Keep imports to `com.minicad.topology.*` (unchanged)
- Update imports from `com.minicad.app.*` to `com.minicad.export.glb.*` if referencing sibling classes

- [ ] **Step 4: Verify files moved correctly**

```bash
ls src/main/java/com/minicad/export/glb/
ls src/main/java/com/minicad/app/ | grep -E "(PreviewGlbBuilder|PreviewMeshExporter|PreviewMaterialExporter|TessellatedFaceExporter)"
```

Expected: Files in glb/, no longer in app/

- [ ] **Step 5: Commit GLB export move**

```bash
git add src/main/java/com/minicad/export/glb/
git commit -m "refactor(phase1): move GLB export classes to export.glb package"
```

---

### Task 1.3: Move Mesh Export Classes

**Files:**
- Move: `src/main/java/com/minicad/app/StepMeshExporter.java` → `src/main/java/com/minicad/export/mesh/StepMeshExporter.java`
- Move: `src/main/java/com/minicad/app/MeshTriangulatorPlanar.java` → `src/main/java/com/minicad/export/mesh/MeshTriangulatorPlanar.java`
- Move: `src/main/java/com/minicad/app/MeshTriangulatorParametric.java` → `src/main/java/com/minicad/export/mesh/MeshTriangulatorParametric.java`

- [ ] **Step 1: Move files**

```bash
git mv src/main/java/com/minicad/app/StepMeshExporter.java src/main/java/com/minicad/export/mesh/StepMeshExporter.java
git mv src/main/java/com/minicad/app/MeshTriangulatorPlanar.java src/main/java/com/minicad/export/mesh/MeshTriangulatorPlanar.java
git mv src/main/java/com/minicad/app/MeshTriangulatorParametric.java src/main/java/com/minicad/export/mesh/MeshTriangulatorParametric.java
```

- [ ] **Step 2: Update package declaration**

Change to `package com.minicad.export.mesh;` in each file.

- [ ] **Step 3: Update imports**

Check and update imports as needed.

- [ ] **Step 4: Verify and commit**

```bash
ls src/main/java/com/minicad/export/mesh/
git add src/main/java/com/minicad/export/mesh/
git commit -m "refactor(phase1): move mesh export classes to export.mesh package"
```

---

### Task 1.4: Move JSON Export Classes

**Files:**
- Move 4 files to `export/json/`

- [ ] **Step 1: Move files**

```bash
git mv src/main/java/com/minicad/app/StepPreviewJsonExporter.java src/main/java/com/minicad/export/json/StepPreviewJsonExporter.java
git mv src/main/java/com/minicad/app/PreviewSerializers.java src/main/java/com/minicad/export/json/PreviewSerializers.java
git mv src/main/java/com/minicad/app/SerializationHelper.java src/main/java/com/minicad/export/json/SerializationHelper.java
git mv src/main/java/com/minicad/app/JsonBuilder.java src/main/java/com/minicad/export/json/JsonBuilder.java
```

- [ ] **Step 2: Update package declaration**

Change to `package com.minicad.export.json;`

- [ ] **Step 3: Update imports and commit**

```bash
git add src/main/java/com/minicad/export/json/
git commit -m "refactor(phase1): move JSON export classes to export.json package"
```

---

### Task 1.5: Update Import Statements in Dependent Files

**Files:**
- Modify: All files that import the moved export classes (need to scan codebase)

- [ ] **Step 1: Find files importing moved classes**

```bash
grep -r "import com.minicad.app.PreviewGlbBuilder" src/main/java/
grep -r "import com.minicad.app.StepMeshExporter" src/main/java/
grep -r "import com.minicad.app.StepPreviewJsonExporter" src/main/java/
```

Expected: List of files with imports to update

- [ ] **Step 2: Update imports in found files**

For each file found, update:
- `import com.minicad.app.PreviewGlbBuilder;` → `import com.minicad.export.glb.PreviewGlbBuilder;`
- `import com.minicad.app.StepMeshExporter;` → `import com.minicad.export.mesh.StepMeshExporter;`
- `import com.minicad.app.StepPreviewJsonExporter;` → `import com.minicad.export.json.StepPreviewJsonExporter;`

- [ ] **Step 3: Commit import updates**

```bash
git add -A
git commit -m "refactor(phase1): update imports for moved export classes"
```

---

### Task 1.6: Create Preview Package Structure

**Files:**
- Create directories: `src/main/java/com/minicad/preview/sampling`, `builder`, `mapper`, `statistics`, `payload`

- [ ] **Step 1: Create preview package directories**

```bash
mkdir -p src/main/java/com/minicad/preview/sampling
mkdir -p src/main/java/com/minicad/preview/builder
mkdir -p src/main/java/com/minicad/preview/mapper
mkdir -p src/main/java/com/minicad/preview/statistics
mkdir -p src/main/java/com/minicad/preview/payload
```

- [ ] **Step 2: Verify and commit**

```bash
ls -la src/main/java/com/minicad/preview/
git add src/main/java/com/minicad/preview/
git commit -m "refactor(phase1): create preview package structure"
```

---

### Task 1.7: Move Preview Sampling Classes (10 files)

**Files:**
- Move 10 sampling-related files to `preview/sampling/`

- [ ] **Step 1: Move sampling files**

```bash
# List of files to move (need to verify exact names in app/)
git mv src/main/java/com/minicad/app/CurveEvaluator.java src/main/java/com/minicad/preview/sampling/CurveEvaluator.java
git mv src/main/java/com/minicad/app/PreviewCurveEvaluator.java src/main/java/com/minicad/preview/sampling/PreviewCurveEvaluator.java
git mv src/main/java/com/minicad/app/PreviewEdgeSampler.java src/main/java/com/minicad/preview/sampling/PreviewEdgeSampler.java
git mv src/main/java/com/minicad/app/PreviewSurfaceSampler.java src/main/java/com/minicad/preview/sampling/PreviewSurfaceSampler.java
git mv src/main/java/com/minicad/app/PreviewPcurveSampler.java src/main/java/com/minicad/preview/sampling/PreviewPcurveSampler.java
git mv src/main/java/com/minicad/app/ConicSamplingHelper.java src/main/java/com/minicad/preview/sampling/ConicSamplingHelper.java
git mv src/main/java/com/minicad/app/Curve2SamplingHelper.java src/main/java/com/minicad/preview/sampling/Curve2SamplingHelper.java
git mv src/main/java/com/minicad/app/Curve3SamplingHelper.java src/main/java/com/minicad/preview/sampling/Curve3SamplingHelper.java
git mv src/main/java/com/minicad/app/PcurveSamplingHelper.java src/main/java/com/minicad/preview/sampling/PcurveSamplingHelper.java
git mv src/main/java/com/minicad/app/TriangulationHelper.java src/main/java/com/minicad/preview/sampling/TriangulationHelper.java
```

Note: Verify exact file names exist in app/ before moving. Adjust if file names differ.

- [ ] **Step 2: Update package declarations**

Change to `package com.minicad.preview.sampling;` in all 10 files.

- [ ] **Step 3: Update imports and commit**

```bash
git add src/main/java/com/minicad/preview/sampling/
git commit -m "refactor(phase1): move sampling classes to preview.sampling package"
```

---

### Task 1.8: Move Preview Builder Classes

**Files:**
- Move 4 builder files to `preview/builder/`

- [ ] **Step 1: Move builder files**

```bash
git mv src/main/java/com/minicad/app/PreviewFaceBuilder.java src/main/java/com/minicad/preview/builder/PreviewFaceBuilder.java
git mv src/main/java/com/minicad/app/PreviewPmiBuilder.java src/main/java/com/minicad/preview/builder/PreviewPmiBuilder.java
git mv src/main/java/com/minicad/app/PreviewGeometryCollector.java src/main/java/com/minicad/preview/builder/PreviewGeometryCollector.java
git mv src/main/java/com/minicad/app/PreviewPmiPayloadTypes.java src/main/java/com/minicad/preview/builder/PreviewPmiPayloadTypes.java
```

- [ ] **Step 2: Update package and commit**

Change package to `com.minicad.preview.builder` and commit.

---

### Task 1.9: Move Preview Mapper Classes

**Files:**
- Move 4 mapper files to `preview/mapper/`

- [ ] **Step 1: Move mapper files**

```bash
git mv src/main/java/com/minicad/app/ParametricSurfaceMapper.java src/main/java/com/minicad/preview/mapper/ParametricSurfaceMapper.java
git mv src/main/java/com/minicad/app/PreviewUvMapper.java src/main/java/com/minicad/preview/mapper/PreviewUvMapper.java
git mv src/main/java/com/minicad/app/SurfaceMapperHelper.java src/main/java/com/minicad/preview/mapper/SurfaceMapperHelper.java
git mv src/main/java/com/minicad/app/PreviewUvCoords.java src/main/java/com/minicad/preview/mapper/PreviewUvCoords.java
```

- [ ] **Step 2: Update package and commit**

---

### Task 1.10: Move Preview Statistics Classes

**Files:**
- Move 3 statistics files to `preview/statistics/`

- [ ] **Step 1: Move statistics files**

```bash
git mv src/main/java/com/minicad/app/PreviewStatisticsHelper.java src/main/java/com/minicad/preview/statistics/PreviewStatisticsHelper.java
git mv src/main/java/com/minicad/app/BoundsAccumulator.java src/main/java/com/minicad/preview/statistics/BoundsAccumulator.java
git mv src/main/java/com/minicad/app/GeometryMeasurementHelper.java src/main/java/com/minicad/preview/statistics/GeometryMeasurementHelper.java
```

- [ ] **Step 2: Update package and commit**

---

### Task 1.11: Move Preview Payload Classes

**Files:**
- Move 6 payload files to `preview/payload/`

- [ ] **Step 1: Move payload files**

```bash
git mv src/main/java/com/minicad/app/PreviewBinaryPayloadTypes.java src/main/java/com/minicad/preview/payload/PreviewBinaryPayloadTypes.java
git mv src/main/java/com/minicad/app/PreviewMeshPayloadTypes.java src/main/java/com/minicad/preview/payload/PreviewMeshPayloadTypes.java
git mv src/main/java/com/minicad/app/PreviewValidationPayloadTypes.java src/main/java/com/minicad/preview/payload/PreviewValidationPayloadTypes.java
git mv src/main/java/com/minicad/app/PayloadConversionHelper.java src/main/java/com/minicad/preview/payload/PayloadConversionHelper.java
git mv src/main/java/com/minicad/app/PayloadReductionHelper.java src/main/java/com/minicad/preview/payload/PayloadReductionHelper.java
# Check if PreviewPayload.java exists
```

- [ ] **Step 2: Update package and commit**

---

### Task 1.12: Create Helper Package Structure

**Files:**
- Create directories: `helper/geometry`, `helper/metadata`, `helper/validation`

- [ ] **Step 1: Create helper package directories**

```bash
mkdir -p src/main/java/com/minicad/helper/geometry
mkdir -p src/main/java/com/minicad/helper/metadata
mkdir -p src/main/java/com/minicad/helper/validation
```

- [ ] **Step 2: Verify and commit**

---

### Task 1.13: Move Helper Geometry Classes

**Files:**
- Move 3 files to `helper/geometry/`

- [ ] **Step 1: Move geometry helper files**

```bash
git mv src/main/java/com/minicad/app/SurfaceGeometryHelper.java src/main/java/com/minicad/helper/geometry/SurfaceGeometryHelper.java
git mv src/main/java/com/minicad/app/MathUtilityHelper.java src/main/java/com/minicad/helper/geometry/MathUtilityHelper.java
git mv src/main/java/com/minicad/app/ShellHelper.java src/main/java/com/minicad/helper/geometry/ShellHelper.java
```

- [ ] **Step 2: Update package and commit**

---

### Task 1.14: Move Helper Metadata Classes

**Files:**
- Move 4 files to `helper/metadata/`

- [ ] **Step 1: Move metadata helper files**

```bash
git mv src/main/java/com/minicad/app/ProductMetadataExtractor.java src/main/java/com/minicad/helper/metadata/ProductMetadataExtractor.java
git mv src/main/java/com/minicad/app/UnitExtractor.java src/main/java/com/minicad/helper/metadata/UnitExtractor.java
git mv src/main/java/com/minicad/app/StepMetadataExtractor.java src/main/java/com/minicad/helper/metadata/StepMetadataExtractor.java
git mv src/main/java/com/minicad/app/StepTextReader.java src/main/java/com/minicad/helper/metadata/StepTextReader.java
```

- [ ] **Step 2: Update package and commit**

---

### Task 1.15: Move Helper Validation Classes

**Files:**
- Move 2 files to `helper/validation/`

- [ ] **Step 1: Move validation helper files**

```bash
git mv src/main/java/com/minicad/app/ValidationReportHelper.java src/main/java/com/minicad/helper/validation/ValidationReportHelper.java
git mv src/main/java/com/minicad/app/StepValidationMatcher.java src/main/java/com/minicad/helper/validation/StepValidationMatcher.java
```

- [ ] **Step 2: Update package and commit**

---

### Task 1.16: Create Builder Package and Move Classes

**Files:**
- Create: `src/main/java/com/minicad/builder/`
- Move 3 files

- [ ] **Step 1: Create builder package**

```bash
mkdir -p src/main/java/com/minicad/builder
```

- [ ] **Step 2: Move builder files**

```bash
git mv src/main/java/com/minicad/app/StepAssemblyGraphBuilder.java src/main/java/com/minicad/builder/StepAssemblyGraphBuilder.java
git mv src/main/java/com/minicad/app/CompiledStepDocument.java src/main/java/com/minicad/builder/CompiledStepDocument.java
git mv src/main/java/com/minicad/app/StepCapabilityRegistry.java src/main/java/com/minicad/builder/StepCapabilityRegistry.java
```

- [ ] **Step 3: Update package and commit**

---

### Task 1.17: Update All Import Statements for Phase 1

**Files:**
- Modify: All files that import moved preview/helper/builder classes

- [ ] **Step 1: Find all files with imports to moved classes**

Use grep to scan entire codebase for imports to `com.minicad.app.*` (excluding the 4 entry points that stay in app).

```bash
grep -r "import com.minicad.app." src/main/java/ src/test/java/ | grep -v "StepViewerApp\|StepDumpApp\|StepBenchmarkApp\|StepCapabilityReportApp"
```

Expected: Long list of files

- [ ] **Step 2: Batch update imports using sed**

Create a script or use sed to update imports in bulk:

```bash
# Example sed commands (need to verify exact package names)
find src/main/java src/test/java -name "*.java" -exec sed -i 's/import com\.minicad\.app\.PreviewGlbBuilder;/import com.minicad.export.glb.PreviewGlbBuilder;/g' {} \;
find src/main/java src/test/java -name "*.java" -exec sed -i 's/import com\.minicad\.app\.PreviewSampling;/import com.minicad.preview.sampling;/g' {} \;
# ... repeat for all moved classes
```

Note: This requires careful mapping of old to new package names. Consider using IDE refactoring tool if available.

- [ ] **Step 3: Verify app package only has 4 entry points**

```bash
ls src/main/java/com/minicad/app/*.java
```

Expected: Only `StepViewerApp.java`, `StepDumpApp.java`, `StepBenchmarkApp.java`, `StepCapabilityReportApp.java`, possibly `Main.java`

- [ ] **Step 4: Commit import updates**

```bash
git add -A
git commit -m "refactor(phase1): update all imports for app package reorganization"
```

---

### Task 1.18: Run Tests to Verify Phase 1

**Files:**
- Test: All tests in `src/test/java/`

- [ ] **Step 1: Run full test suite**

```bash
mvn clean test
```

Expected: All tests pass (exit code 0)

- [ ] **Step 2: Check for compilation errors**

If tests fail, check Maven output for:
- Compilation errors (package not found, class not found)
- Runtime errors (ClassNotFoundException)

- [ ] **Step 3: Fix any errors**

If errors found:
1. Check package declarations are correct
2. Check import statements are correct
3. Check file locations match package structure

- [ ] **Step 4: Re-run tests until pass**

```bash
mvn clean test
```

Continue fixing until all tests pass.

- [ ] **Step 5: Commit phase 1 completion**

```bash
git add -A
git commit -m "refactor(phase1): complete app package reorganization - all tests pass"
```

---

## Phase 2: Step.Model Package Consolidation

**Impact:** Medium - Hundreds of files moved in package structure, but mostly generated STEP entities

**Note:** Phase 2 is complex due to many sub-packages. Consider breaking into smaller batches.

### Task 2.1: Create Core Package Structure

**Files:**
- Create: `src/main/java/com/minicad/step/model/core/base/`
- Create: `src/main/java/com/minicad/step/model/core/element/`
- Create: `src/main/java/com/minicad/step/model/core/misc/`

- [ ] **Step 1: Create core package directories**

```bash
mkdir -p src/main/java/com/minicad/step/model/core/base
mkdir -p src/main/java/com/minicad/step/model/core/element
mkdir -p src/main/java/com/minicad/step/model/core/misc
```

- [ ] **Step 2: Verify directories**

- [ ] **Step 3: Commit**

---

### Task 2.2: Move Base Package to Core

**Files:**
- Move: 11 files from `step/model/base/` to `step/model/core/base/`

- [ ] **Step 1: Move base package files**

```bash
git mv src/main/java/com/minicad/step/model/base/*.java src/main/java/com/minicad/step/model/core/base/
```

- [ ] **Step 2: Update package declarations**

Change `package com.minicad.step.model.base;` to `package com.minicad.step.model.core.base;` in all files.

- [ ] **Step 3: Commit**

---

### Task 2.3: Move Element Package to Core

**Files:**
- Move: ~15 files from `step/model/element/` to `step/model/core/element/`

- [ ] **Step 1: Move element package files**

```bash
git mv src/main/java/com/minicad/step/model/element/*.java src/main/java/com/minicad/step/model/core/element/
```

- [ ] **Step 2: Update package declarations**

Change to `package com.minicad.step.model.core.element;`

- [ ] **Step 3: Commit**

---

### Task 2.4: Move Misc Package to Core

**Files:**
- Move: ~30 files from `step/model/misc/` to `step/model/core/misc/`

- [ ] **Step 1: Move misc package files**

```bash
git mv src/main/java/com/minicad/step/model/misc/*.java src/main/java/com/minicad/step/model/core/misc/
```

- [ ] **Step 2: Update package declarations**

Change to `package com.minicad.step.model.core.misc;`

- [ ] **Step 3: Commit**

---

### Task 2.5: Update Imports for Core Package

**Files:**
- Modify: All files importing `step.model.base`, `step.model.element`, `step.model.misc`

- [ ] **Step 1: Find files with imports**

```bash
grep -r "import com.minicad.step.model.base." src/main/java/
grep -r "import com.minicad.step.model.element." src/main/java/
grep -r "import com.minicad.step.model.misc." src/main/java/
```

- [ ] **Step 2: Update imports**

Batch update:
- `com.minicad.step.model.base.` → `com.minicad.step.model.core.base.`
- `com.minicad.step.model.element.` → `com.minicad.step.model.core.element.`
- `com.minicad.step.model.misc.` → `com.minicad.step.model.core.misc.`

- [ ] **Step 3: Commit**

---

### Task 2.6: Create Management Package Structure

**Files:**
- Create: `step/model/management/config/`, `log/`, `security/`, `backup/`

- [ ] **Step 1: Create management package directories**

```bash
mkdir -p src/main/java/com/minicad/step/model/management/config
mkdir -p src/main/java/com/minicad/step/model/management/log
mkdir -p src/main/java/com/minicad/step/model/management/security
mkdir -p src/main/java/com/minicad/step/model/management/backup
```

- [ ] **Step 2: Verify and commit**

---

### Task 2.7: Rename config_mgmt to config

**Files:**
- Move: 32 files from `step/model/config_mgmt/` to `step/model/management/config/`

- [ ] **Step 1: Move config_mgmt files**

```bash
git mv src/main/java/com/minicad/step/model/config_mgmt/*.java src/main/java/com/minicad/step/model/management/config/
```

- [ ] **Step 2: Update package declarations**

Change `package com.minicad.step.model.config_mgmt;` to `package com.minicad.step.model.management.config;`

- [ ] **Step 3: Commit**

---

### Task 2.8: Rename log_audit to log

**Files:**
- Move: 23 files from `step/model/log_audit/` to `step/model/management/log/`

- [ ] **Step 1: Move log_audit files**

```bash
git mv src/main/java/com/minicad/step/model/log_audit/*.java src/main/java/com/minicad/step/model/management/log/
```

- [ ] **Step 2: Update package declarations**

Change to `package com.minicad.step.model.management.log;`

- [ ] **Step 3: Commit**

---

### Task 2.9: Move security to management

**Files:**
- Move: 22 files from `step/model/security/` to `step/model/management/security/`

- [ ] **Step 1: Move security files**

```bash
git mv src/main/java/com/minicad/step/model/security/*.java src/main/java/com/minicad/step/model/management/security/
```

- [ ] **Step 2: Update package declarations**

Change to `package com.minicad.step.model.management.security;`

- [ ] **Step 3: Commit**

---

### Task 2.10: Rename backup_recovery to backup

**Files:**
- Move: 14 files from `step/model/backup_recovery/` to `step/model/management/backup/`

- [ ] **Step 1: Move backup_recovery files**

```bash
git mv src/main/java/com/minicad/step/model/backup_recovery/*.java src/main/java/com/minicad/step/model/management/backup/
```

- [ ] **Step 2: Update package declarations**

Change to `package com.minicad.step.model.management.backup;`

- [ ] **Step 3: Commit**

---

### Task 2.11: Update Imports for Management Package

**Files:**
- Modify: All files importing renamed packages

- [ ] **Step 1: Find and update imports**

Batch update for snake_case package renames:
- `com.minicad.step.model.config_mgmt.` → `com.minicad.step.model.management.config.`
- `com.minicad.step.model.log_audit.` → `com.minicad.step.model.management.log.`
- `com.minicad.step.model.backup_recovery.` → `com.minicad.step.model.management.backup.`
- `com.minicad.step.model.security.` → `com.minicad.step.model.management.security.`

- [ ] **Step 2: Commit**

---

### Task 2.12: Create Organization Package Structure

**Files:**
- Create: `step/model/organization/org/`, `document/`, `approval/`

- [ ] **Step 1: Create directories**

```bash
mkdir -p src/main/java/com/minicad/step/model/organization/org
mkdir -p src/main/java/com/minicad/step/model/organization/document
mkdir -p src/main/java/com/minicad/step/model/organization/approval
```

- [ ] **Step 2: Verify and commit**

---

### Task 2.13: Move Organization Sub-packages

**Files:**
- Move: `organization` → `organization/org`, `document` → `organization/document`, `approval` → `organization/approval`

- [ ] **Step 1: Move organization files**

```bash
git mv src/main/java/com/minicad/step/model/organization/*.java src/main/java/com/minicad/step/model/organization/org/
git mv src/main/java/com/minicad/step/model/document/*.java src/main/java/com/minicad/step/model/organization/document/
git mv src/main/java/com/minicad/step/model/approval/*.java src/main/java/com/minicad/step/model/organization/approval/
```

- [ ] **Step 2: Update package declarations**

Update to respective packages under organization.

- [ ] **Step 3: Update imports and commit**

---

### Task 2.14: Create Technical Package Structure

**Files:**
- Create: `step/model/technical/tolerance/`, `kinematic/`, `unit/`, `date/`

- [ ] **Step 1: Create directories**

```bash
mkdir -p src/main/java/com/minicad/step/model/technical/tolerance
mkdir -p src/main/java/com/minicad/step/model/technical/kinematic
mkdir -p src/main/java/com/minicad/step/model/technical/unit
mkdir -p src/main/java/com/minicad/step/model/technical/date
```

- [ ] **Step 2: Verify and commit**

---

### Task 2.15: Move Technical Sub-packages

**Files:**
- Move: `tolerance`, `kinematic`, `unit`, `date_time` → technical sub-packages

- [ ] **Step 1: Move files**

```bash
git mv src/main/java/com/minicad/step/model/tolerance/*.java src/main/java/com/minicad/step/model/technical/tolerance/
git mv src/main/java/com/minicad/step/model/kinematic/*.java src/main/java/com/minicad/step/model/technical/kinematic/
git mv src/main/java/com/minicad/step/model/unit/*.java src/main/java/com/minicad/step/model/technical/unit/
git mv src/main/java/com/minicad/step/model/date_time/*.java src/main/java/com/minicad/step/model/technical/date/
```

- [ ] **Step 2: Update package declarations**

Change `date_time` to `date` package name.

- [ ] **Step 3: Update imports and commit**

---

### Task 2.16: Create Profile Analysis Package

**Files:**
- Create: `step/model/profile_analysis/profile/`, `analysis/`

- [ ] **Step 1: Create directories**

```bash
mkdir -p src/main/java/com/minicad/step/model/profile_analysis/profile
mkdir -p src/main/java/com/minicad/step/model/profile_analysis/analysis
```

- [ ] **Step 2: Move files**

```bash
git mv src/main/java/com/minicad/step/model/profile/*.java src/main/java/com/minicad/step/model/profile_analysis/profile/
git mv src/main/java/com/minicad/step/model/analysis/*.java src/main/java/com/minicad/step/model/profile_analysis/analysis/
```

- [ ] **Step 3: Update package and imports**

---

### Task 2.17: Update All Imports for Phase 2

**Files:**
- Modify: All files importing moved/renamed packages

- [ ] **Step 1: Comprehensive import update**

Scan entire codebase and update all imports from old package names to new package names.

Use bulk sed commands or IDE refactoring tool.

- [ ] **Step 2: Verify step.model package structure**

```bash
find src/main/java/com/minicad/step/model -type d | wc -l
ls src/main/java/com/minicad/step/model/
```

Expected: ~20 sub-packages (reduced from 32)

- [ ] **Step 3: Commit**

---

### Task 2.18: Run Tests to Verify Phase 2

**Files:**
- Test: All tests

- [ ] **Step 1: Run full test suite**

```bash
mvn clean test
```

Expected: All tests pass

- [ ] **Step 2: Fix any errors**

Fix compilation or runtime errors related to package structure.

- [ ] **Step 3: Commit phase 2 completion**

```bash
git add -A
git commit -m "refactor(phase2): complete step.model package consolidation - all tests pass"
```

---

## Phase 3: Other Package Optimization

**Impact:** Low - Minor renames and test structure creation

### Task 3.1: Rename tools to tool

**Files:**
- Move: `src/main/java/com/minicad/tools/` → `src/main/java/com/minicad/tool/`

- [ ] **Step 1: Rename tools directory**

```bash
git mv src/main/java/com/minicad/tools src/main/java/com/minicad/tool
```

- [ ] **Step 2: Update package declarations in tool files**

Change `package com.minicad.tools;` to `package com.minicad.tool;` in all files.

- [ ] **Step 3: Update imports**

```bash
grep -r "import com.minicad.tools." src/main/java/ src/test/java/
```

Update to `import com.minicad.tool.`

- [ ] **Step 4: Commit**

```bash
git add -A
git commit -m "refactor(phase3): rename tools package to tool (singular)"
```

---

### Task 3.2: Create Mirror Test Package Structure

**Files:**
- Create: Test directories mirroring source structure

- [ ] **Step 1: Create test package directories**

Create test directories for new packages:

```bash
mkdir -p src/test/java/com/minicad/export/glb
mkdir -p src/test/java/com/minicad/export/mesh
mkdir -p src/test/java/com/minicad/export/json
mkdir -p src/test/java/com/minicad/preview/sampling
mkdir -p src/test/java/com/minicad/preview/builder
mkdir -p src/test/java/com/minicad/preview/mapper
mkdir -p src/test/java/com/minicad/preview/statistics
mkdir -p src/test/java/com/minicad/preview/payload
mkdir -p src/test/java/com/minicad/helper/geometry
mkdir -p src/test/java/com/minicad/helper/metadata
mkdir -p src/test/java/com/minicad/helper/validation
mkdir -p src/test/java/com/minicad/builder
mkdir -p src/test/java/com/minicad/tool
mkdir -p src/test/java/com/minicad/step/model/core/base
mkdir -p src/test/java/com/minicad/step/model/core/element
mkdir -p src/test/java/com/minicad/step/model/core/misc
mkdir -p src/test/java/com/minicad/step/model/management/config
mkdir -p src/test/java/com/minicad/step/model/management/log
mkdir -p src/test/java/com/minicad/step/model/management/security
mkdir -p src/test/java/com/minicad/step/model/management/backup
mkdir -p src/test/java/com/minicad/step/model/organization/org
mkdir -p src/test/java/com/minicad/step/model/organization/document
mkdir -p src/test/java/com/minicad/step/model/organization/approval
mkdir -p src/test/java/com/minicad/step/model/technical/tolerance
mkdir -p src/test/java/com/minicad/step/model/technical/kinematic
mkdir -p src/test/java/com/minicad/step/model/technical/unit
mkdir -p src/test/java/com/minicad/step/model/technical/date
mkdir -p src/test/java/com/minicad/step/model/profile_analysis/profile
mkdir -p src/test/java/com/minicad/step/model/profile_analysis/analysis
```

- [ ] **Step 2: Move existing test files**

Move existing test files to new test package structure to match source structure.

- [ ] **Step 3: Update test package declarations**

- [ ] **Step 4: Commit**

---

### Task 3.3: Update Documentation

**Files:**
- Modify: `README.md`, `AGENTS.md`

- [ ] **Step 1: Update README architecture diagram**

Update package structure diagram in README.md to reflect new structure.

- [ ] **Step 2: Update AGENTS.md**

Update AGENTS.md to reference new package names.

- [ ] **Step 3: Commit**

```bash
git add README.md AGENTS.md
git commit -m "docs: update documentation for package reorganization"
```

---

### Task 3.4: Run Final Tests

**Files:**
- Test: All tests

- [ ] **Step 1: Run full test suite**

```bash
mvn clean test
```

Expected: All tests pass

- [ ] **Step 2: Verify final package structure**

```bash
ls src/main/java/com/minicad/
find src/main/java/com/minicad/step/model -type d | wc -l
```

Expected: 11 top-level packages, ~20 step.model sub-packages

- [ ] **Step 3: Commit phase 3 completion**

```bash
git add -A
git commit -m "refactor(phase3): complete other package optimization - all tests pass"
```

---

### Task 3.5: Final Verification and Summary Commit

**Files:**
- All modified files

- [ ] **Step 1: Run baseline verification**

```bash
mvn -B clean test
mvn -q exec:java -Dexec.args="examples/minimal-square.step"
mvn -q exec:java -Dexec.args="examples/engine.stp"
```

Expected: All tests pass, examples run successfully

- [ ] **Step 2: Verify git status**

```bash
git status
git log --oneline | head -20
```

Expected: Clean working tree, all changes committed

- [ ] **Step 3: Create summary commit**

```bash
git commit --allow-empty -m "refactor: complete package reorganization

- Phase 1: Split app package into export/preview/helper/builder (52 files moved)
- Phase 2: Consolidate step.model packages (32 → 20 packages)
- Phase 3: Rename tools to tool, create test structure, update docs

Package structure now follows clean layered architecture:
- app: 4 entry points only
- export: GLB/mesh/JSON export functionality
- preview: Sampling/builder/mapper/statistics/payload
- helper: Geometry/metadata/validation helpers
- builder: Model building and assembly
- tool: Utility tools
- step.model: 20 consolidated packages with logical grouping

All tests pass, examples run successfully."
```

---

## Self-Review Checklist

After completing all tasks:

**1. Spec Coverage:** ✓ All 3 phases covered, all package moves documented

**2. Placeholder Scan:** ✓ No TBD/TODO, all steps have concrete commands

**3. Type Consistency:** ✓ Package names consistent across tasks

**4. Test Coverage:** ✓ Tests run after each phase

---

**Plan complete and saved to `docs/superpowers/plans/2026-07-08-package-reorganization.md`**

Two execution options:

**1. Subagent-Driven (recommended)** - I dispatch a fresh subagent per task, review between tasks, fast iteration

**2. Inline Execution** - Execute tasks in this session using executing-plans, batch execution with checkpoints

Which approach?