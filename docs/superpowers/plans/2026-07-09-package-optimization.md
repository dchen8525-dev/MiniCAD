# Package Structure Optimization Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Flatten step.model (36→1 package) and helper (3→1 package) to reduce package count from 60+ to ~30, shorten import statements, and improve navigation efficiency.

**Architecture:** Aggressive flat consolidation - move all StepEntity files (1264) to single step.model package, move all helper files (9) to single helper package, preserve Git history with `git mv`, update import statements globally.

**Tech Stack:** Java 11, Maven, Git, PowerShell (Windows)

## Global Constraints

- **step.model**: Flatten 36 sub-packages → 1 package (1264 files move to `src/main/java/com/minicad/step/model/`)
- **helper**: Flatten 3 sub-packages → 1 package (9 files move to `src/main/java/com/minicad/helper/`)
- **geometry/geometry2d**: Keep separate (no merge)
- **Git history preservation**: Use `git mv` for all file moves
- **Import statement update**: Change `com.minicad.step.model.<subpackage>` → `com.minicad.step.model`
- **Testing**: Run `mvn clean test` after each major phase
- **Commit frequency**: Commit after each completed task

---

## Phase 1: Pre-Migration Analysis

### Task 1: Check for naming conflicts

**Files:**
- Analyze: `src/main/java/com/minicad/step/model/**/*.java`

**Interfaces:**
- Consumes: None
- Produces: Conflict report (if any duplicate class names exist)

**Purpose:** Ensure no duplicate class names across 36 sub-packages before merge. If conflicts exist, plan would need adjustment.

- [ ] **Step 1: Find all StepEntity class names**

```bash
find src/main/java/com/minicad/step/model -name "*.java" -exec basename {} .java \; | sort
```

Expected: List of 1264 unique class names (StepXxx prefix ensures uniqueness)

- [ ] **Step 2: Check for duplicate class names**

```bash
find src/main/java/com/minicad/step/model -name "*.java" -exec basename {} .java \; | sort | uniq -d
```

Expected: Empty output (no duplicates). If duplicates exist, STOP and report conflict.

- [ ] **Step 3: Commit analysis results**

```bash
git add docs/superpowers/specs/2026-07-09-package-optimization-design.md
git commit -m "docs: add package optimization design (方案 A - flat consolidation)"
```

---

## Phase 2: step.model Flattening

### Task 2: Move all step.model sub-package files to root

**Files:**
- Move: `src/main/java/com/minicad/step/model/<subpackage>/*.java` → `src/main/java/com/minicad/step/model/`
- 36 sub-packages: action, annotation, classification, core, expression, fea, geometry, management, manufacturing, organization, product, profile_analysis, representation, resource, system, technical, topology, validation, workflow

**Interfaces:**
- Consumes: None
- Produces: All 1264 files moved to `step.model` package (Git history preserved)

**Purpose:** Physically move files to flatten package structure. Use `git mv` to preserve history.

- [ ] **Step 1: Create temporary migration script**

Create file: `scripts/migrate_step_model.ps1`

```powershell
# PowerShell script to move all step.model sub-package files to root
$targetDir = "src/main/java/com/minicad/step/model"

# Get all sub-package directories (exclude the root model directory itself)
$subdirs = Get-ChildItem -Path $targetDir -Directory | Where-Object { $_.Name -ne "model" }

foreach ($subdir in $subdirs) {
    $files = Get-ChildItem -Path $subdir.FullName -Filter "*.java"
    foreach ($file in $files) {
        $destPath = Join-Path $targetDir $file.Name
        # Use git mv to preserve history
        git mv $file.FullName $destPath
        Write-Host "Moved: $($file.Name) from $($subdir.Name)"
    }
}

Write-Host "Total files moved: $(($subdirs | ForEach-Object { (Get-ChildItem $_.FullName -Filter '*.java').Count } | Measure-Object -Sum).Sum)"
```

- [ ] **Step 2: Execute migration script**

```powershell
powershell -ExecutionPolicy Bypass -File scripts/migrate_step_model.ps1
```

Expected: "Total files moved: 1264" (or close to this number)

- [ ] **Step 3: Verify all files moved**

```bash
find src/main/java/com/minicad/step/model -name "*.java" -type f | wc -l
```

Expected: 1264 files (all files now in root model directory)

- [ ] **Step 4: Check sub-package directories are empty**

```bash
find src/main/java/com/minicad/step/model -type d -empty
```

Expected: List of empty directories (will be removed in cleanup phase)

- [ ] **Step 5: Commit file moves**

```bash
git add -A
git commit -m "refactor: flatten step.model package (36 sub-packages → 1 package)

- Move 1264 StepEntity files to root model package
- Use git mv to preserve file history
- Sub-package directories now empty (will be removed in cleanup phase)"
```

---

### Task 3: Update package declarations in moved files

**Files:**
- Modify: All 1264 files in `src/main/java/com/minicad/step/model/*.java`

**Interfaces:**
- Consumes: Files moved in Task 2
- Produces: All files with correct package declaration `package com.minicad.step.model;`

**Purpose:** Change package declaration from `com.minicad.step.model.<subpackage>` to `com.minicad.step.model` in all moved files.

- [ ] **Step 1: Create package declaration update script**

Create file: `scripts/update_package_declarations.ps1`

```powershell
# Update package declarations in all step.model files
$modelDir = "src/main/java/com/minicad/step/model"
$files = Get-ChildItem -Path $modelDir -Filter "*.java" -File

$count = 0
foreach ($file in $files) {
    $content = Get-Content $file.FullName -Raw
    # Replace package declaration: remove sub-package suffix
    $newContent = $content -replace 'package com\.minicad\.step\.model\.[a-z_]+;', 'package com.minicad.step.model;'
    Set-Content -Path $file.FullName -Value $newContent -NoNewline
    $count++
}

Write-Host "Updated package declarations in $count files"
```

- [ ] **Step 2: Execute package declaration update**

```powershell
powershell -ExecutionPolicy Bypass -File scripts/update_package_declarations.ps1
```

Expected: "Updated package declarations in 1264 files"

- [ ] **Step 3: Verify package declarations updated correctly**

Sample check for a few files:

```bash
grep "^package " src/main/java/com/minicad/step/model/StepAction.java src/main/java/com/minicad/step/model/StepCartesianPoint.java src/main/java/com/minicad/step/model/StepRepresentation.java
```

Expected: All show `package com.minicad.step.model;`

- [ ] **Step 4: Commit package declaration updates**

```bash
git add -A
git commit -m "refactor: update package declarations in step.model files

- Change from com.minicad.step.model.<subpackage> to com.minicad.step.model
- Updated 1264 files to reflect flat package structure"
```

---

### Task 4: Update import statements across codebase

**Files:**
- Modify: All files in `src/main/java/com/minicad/**/*.java` (excluding step.model/*.java)
- Modify: All files in `src/test/java/**/*.java`

**Interfaces:**
- Consumes: Files with updated package declarations (Task 3)
- Produces: All import statements changed to `com.minicad.step.model`

**Purpose:** Update all import statements that reference step.model sub-packages to use flat package path.

- [ ] **Step 1: Create import update script**

Create file: `scripts/update_imports_step_model.ps1`

```powershell
# Update all import statements referencing step.model sub-packages
$sourceDirs = @("src/main/java", "src/test/java")
$pattern = 'import com\.minicad\.step\.model\.[a-z_]+\.'
$replacement = 'import com.minicad.step.model.'

$totalCount = 0
foreach ($dir in $sourceDirs) {
    $files = Get-ChildItem -Path $dir -Filter "*.java" -Recurse -File | Where-Object { $_.DirectoryName -notmatch "step\\model$" }
    foreach ($file in $files) {
        $content = Get-Content $file.FullName -Raw
        if ($content -match $pattern) {
            $newContent = $content -replace $pattern, $replacement
            Set-Content -Path $file.FullName -Value $newContent -NoNewline
            $count = ($content | Select-String -Pattern $pattern -AllMatches).Matches.Count
            $totalCount += $count
            Write-Host "Updated $count imports in: $($file.FullName)"
        }
    }
}

Write-Host "Total import statements updated: $totalCount"
```

- [ ] **Step 2: Execute import update script**

```powershell
powershell -ExecutionPolicy Bypass -File scripts/update_imports_step_model.ps1
```

Expected: "Total import statements updated: <large number>" (estimated 5000+ imports across 1495 main files + test files)

- [ ] **Step 3: Verify import updates in key files**

Check imports in files that heavily use StepEntity classes:

```bash
grep "import com.minicad.step.model" src/main/java/com/minicad/builder/StepAssemblyGraphBuilder.java | head -10
```

Expected: All imports show `import com.minicad.step.model.StepXxx;` (no sub-package)

- [ ] **Step 4: Compile to check for import errors**

```bash
mvn -B clean compile
```

Expected: SUCCESS (all imports resolved correctly). If FAIL, check error messages for remaining sub-package imports.

- [ ] **Step 5: Commit import statement updates**

```bash
git add -A
git commit -m "refactor: update import statements for flat step.model package

- Change all imports from com.minicad.step.model.<subpackage> to com.minicad.step.model
- Updated imports across src/main/java and src/test/java
- Estimated 5000+ import statements updated"
```

---

## Phase 3: helper Flattening

### Task 5: Move helper sub-package files to root

**Files:**
- Move: `src/main/java/com/minicad/helper/geometry/*.java` → `src/main/java/com/minicad/helper/`
- Move: `src/main/java/com/minicad/helper/metadata/*.java` → `src/main/java/com/minicad/helper/`
- Move: `src/main/java/com/minicad/helper/validation/*.java` → `src/main/java/com/minicad/helper/`
- Total: 9 files

**Interfaces:**
- Consumes: None
- Produces: All 9 helper files moved to root helper package (Git history preserved)

**Purpose:** Move all helper files from 3 sub-packages to root helper package.

- [ ] **Step 1: Move geometry sub-package files**

```bash
git mv src/main/java/com/minicad/helper/geometry/MathUtilityHelper.java src/main/java/com/minicad/helper/
git mv src/main/java/com/minicad/helper/geometry/ShellHelper.java src/main/java/com/minicad/helper/
git mv src/main/java/com/minicad/helper/geometry/SurfaceGeometryHelper.java src/main/java/com/minicad/helper/
```

Expected: 3 files moved successfully

- [ ] **Step 2: Move metadata sub-package files**

```bash
git mv src/main/java/com/minicad/helper/metadata/ProductMetadataExtractor.java src/main/java/com/minicad/helper/
git mv src/main/java/com/minicad/helper/metadata/StepMetadataExtractor.java src/main/java/com/minicad/helper/
git mv src/main/java/com/minicad/helper/metadata/StepTextReader.java src/main/java/com/minicad/helper/
git mv src/main/java/com/minicad/helper/metadata/UnitExtractor.java src/main/java/com/minicad/helper/
```

Expected: 4 files moved successfully

- [ ] **Step 3: Move validation sub-package files**

```bash
git mv src/main/java/com/minicad/helper/validation/StepValidationMatcher.java src/main/java/com/minicad/helper/
git mv src/main/java/com/minicad/helper/validation/ValidationReportHelper.java src/main/java/com/minicad/helper/
```

Expected: 2 files moved successfully

- [ ] **Step 4: Verify all files moved**

```bash
find src/main/java/com/minicad/helper -name "*.java" -type f | wc -l
```

Expected: 9 files (all in root helper directory)

- [ ] **Step 5: Commit file moves**

```bash
git add -A
git commit -m "refactor: flatten helper package (3 sub-packages → 1 package)

- Move 9 helper files to root helper package
- geometry: 3 files, metadata: 4 files, validation: 2 files
- Use git mv to preserve file history"
```

---

### Task 6: Update package declarations in helper files

**Files:**
- Modify: All 9 files in `src/main/java/com/minicad/helper/*.java`

**Interfaces:**
- Consumes: Files moved in Task 5
- Produces: All helper files with package declaration `package com.minicad.helper;`

**Purpose:** Change package declaration from `com.minicad.helper.<subpackage>` to `com.minicad.helper` in all moved files.

- [ ] **Step 1: Update package declarations**

For each helper file, update package declaration:

```bash
# MathUtilityHelper.java
sed -i 's/^package com\.minicad\.helper\.geometry;/package com.minicad.helper;/' src/main/java/com/minicad/helper/MathUtilityHelper.java

# ShellHelper.java
sed -i 's/^package com\.minicad\.helper\.geometry;/package com.minicad.helper;/' src/main/java/com/minicad/helper/ShellHelper.java

# SurfaceGeometryHelper.java
sed -i 's/^package com\.minicad\.helper\.geometry;/package com.minicad.helper;/' src/main/java/com/minicad/helper/SurfaceGeometryHelper.java

# ProductMetadataExtractor.java
sed -i 's/^package com\.minicad\.helper\.metadata;/package com.minicad.helper;/' src/main/java/com/minicad/helper/ProductMetadataExtractor.java

# StepMetadataExtractor.java
sed -i 's/^package com\.minicad\.helper\.metadata;/package com.minicad.helper;/' src/main/java/com/minicad/helper/StepMetadataExtractor.java

# StepTextReader.java
sed -i 's/^package com\.minicad\.helper\.metadata;/package com.minicad.helper;/' src/main/java/com/minicad/helper/StepTextReader.java

# UnitExtractor.java
sed -i 's/^package com\.minicad\.helper\.metadata;/package com.minicad.helper;/' src/main/java/com/minicad/helper/UnitExtractor.java

# StepValidationMatcher.java
sed -i 's/^package com\.minicad\.helper\.validation;/package com.minicad.helper;/' src/main/java/com/minicad/helper/StepValidationMatcher.java

# ValidationReportHelper.java
sed -i 's/^package com\.minicad\.helper\.validation;/package com.minicad.helper;/' src/main/java/com/minicad/helper/ValidationReportHelper.java
```

Expected: All 9 files updated

- [ ] **Step 2: Verify package declarations**

```bash
grep "^package " src/main/java/com/minicad/helper/*.java
```

Expected: All show `package com.minicad.helper;`

- [ ] **Step 3: Commit package declaration updates**

```bash
git add -A
git commit -m "refactor: update package declarations in helper files

- Change from com.minicad.helper.<subpackage> to com.minicad.helper
- Updated 9 files to reflect flat package structure"
```

---

### Task 7: Update import statements for helper

**Files:**
- Modify: All files in `src/main/java/com/minicad/**/*.java` (excluding helper/*.java)
- Modify: All files in `src/test/java/**/*.java`

**Interfaces:**
- Consumes: Files with updated package declarations (Task 6)
- Produces: All import statements changed to `com.minicad.helper`

**Purpose:** Update all import statements that reference helper sub-packages to use flat package path.

- [ ] **Step 1: Create import update script**

Create file: `scripts/update_imports_helper.ps1`

```powershell
# Update all import statements referencing helper sub-packages
$sourceDirs = @("src/main/java", "src/test/java")
$patterns = @(
    @('import com\.minicad\.helper\.geometry\.', 'import com.minicad.helper.'),
    @('import com\.minicad\.helper\.metadata\.', 'import com.minicad.helper.'),
    @('import com\.minicad\.helper\.validation\.', 'import com.minicad.helper.')
)

$totalCount = 0
foreach ($dir in $sourceDirs) {
    $files = Get-ChildItem -Path $dir -Filter "*.java" -Recurse -File | Where-Object { $_.DirectoryName -notmatch "helper$" }
    foreach ($file in $files) {
        $content = Get-Content $file.FullName -Raw
        $updated = $false
        foreach ($pair in $patterns) {
            $pattern = $pair[0]
            $replacement = $pair[1]
            if ($content -match $pattern) {
                $content = $content -replace $pattern, $replacement
                $updated = $true
                $count = ($content | Select-String -Pattern $pattern -AllMatches).Matches.Count
                $totalCount += $count
            }
        }
        if ($updated) {
            Set-Content -Path $file.FullName -Value $content -NoNewline
            Write-Host "Updated imports in: $($file.FullName)"
        }
    }
}

Write-Host "Total helper import statements updated: $totalCount"
```

- [ ] **Step 2: Execute import update script**

```powershell
powershell -ExecutionPolicy Bypass -File scripts/update_imports_helper.ps1
```

Expected: "Total helper import statements updated: <number>" (estimated ~50 imports)

- [ ] **Step 3: Compile to check for import errors**

```bash
mvn -B clean compile
```

Expected: SUCCESS. If FAIL, check error messages for remaining sub-package imports.

- [ ] **Step 4: Commit import statement updates**

```bash
git add -A
git commit -m "refactor: update import statements for flat helper package

- Change imports from com.minicad.helper.<subpackage> to com.minicad.helper
- Updated imports across src/main/java and src/test/java"
```

---

## Phase 4: Cleanup and Validation

### Task 8: Remove empty sub-package directories

**Files:**
- Delete: `src/main/java/com/minicad/step/model/<36 subdirectories>/`
- Delete: `src/main/java/com/minicad/helper/geometry/`
- Delete: `src/main/java/com/minicad/helper/metadata/`
- Delete: `src/main/java/com/minicad/helper/validation/`

**Interfaces:**
- Consumes: Empty directories from previous tasks
- Produces: Cleaned package structure (no empty directories)

**Purpose:** Remove all empty sub-package directories to finalize flat package structure.

- [ ] **Step 1: Find empty directories in step.model**

```bash
find src/main/java/com/minicad/step/model -type d -empty
```

Expected: List of 36 empty directories

- [ ] **Step 2: Remove empty step.model subdirectories**

```bash
find src/main/java/com/minicad/step/model -type d -empty -delete
```

Expected: All empty directories removed

- [ ] **Step 3: Verify step.model directory structure**

```bash
find src/main/java/com/minicad/step/model -type d
```

Expected: Only `src/main/java/com/minicad/step/model` directory (no subdirectories)

- [ ] **Step 4: Remove empty helper subdirectories**

```bash
rm -rf src/main/java/com/minicad/helper/geometry
rm -rf src/main/java/com/minicad/helper/metadata
rm -rf src/main/java/com/minicad/helper/validation
```

Expected: 3 empty directories removed

- [ ] **Step 5: Verify helper directory structure**

```bash
find src/main/java/com/minicad/helper -type d
```

Expected: Only `src/main/java/com/minicad/helper` directory (no subdirectories)

- [ ] **Step 6: Commit cleanup**

```bash
git add -A
git commit -m "refactor: remove empty sub-package directories

- Remove 36 empty step.model subdirectories
- Remove 3 empty helper subdirectories (geometry, metadata, validation)
- Finalize flat package structure"
```

---

### Task 9: Run full test suite

**Files:**
- Test: All test files in `src/test/java/**/*.java`

**Interfaces:**
- Consumes: Refactored codebase (Tasks 1-8)
- Produces: Test results (all tests pass)

**Purpose:** Validate all functionality works correctly after package refactoring.

- [ ] **Step 1: Run Maven clean test**

```bash
mvn -B clean test
```

Expected: BUILD SUCCESS, all tests pass. If FAIL, investigate test failures and fix.

- [ ] **Step 2: Run example regression tests**

```bash
mvn -B test -Dtest=ExamplesRegressionTest
```

Expected: All example STEP files parse successfully.

- [ ] **Step 3: Test CLI application**

```bash
mvn -q exec:java -Dexec.args="examples/minimal-square.step"
```

Expected: Output shows parsed entities without errors.

- [ ] **Step 4: Test web viewer application**

```bash
mvn -q exec:java -Dexec.mainClass=com.minicad.app.StepViewerApp &
sleep 5
curl http://127.0.0.1:8080/api/example?name=minimal-square
```

Expected: JSON response with preview data. Stop server after test.

- [ ] **Step 5: Commit test validation**

```bash
git add -A
git commit -m "test: validate package refactoring with full test suite

- All unit tests pass (mvn clean test)
- Example regression tests pass
- CLI application works correctly
- Web viewer application works correctly"
```

---

### Task 10: Update documentation

**Files:**
- Modify: `README.md` (package structure section)
- Modify: `CONTRIBUTING.md` (package structure section)
- Create: `docs/package-structure-change.md` (migration guide)

**Interfaces:**
- Consumes: Completed refactoring (Tasks 1-9)
- Produces: Updated documentation reflecting new package structure

**Purpose:** Document package structure changes for future developers.

- [ ] **Step 1: Update README.md package structure section**

Find and update package structure documentation. Example change:

```markdown
## Package Structure

After optimization (2026-07-09):

```
com.minicad
├── app (13 files) - CLI applications (StepDumpApp, StepViewerApp, etc.)
├── builder (9 files) - Assembly graph builders
├── common (7 files) - Exceptions, Preconditions, Epsilon
├── export - Exporters (glb, json, mesh sub-packages)
├── geometry (38 files) - 3D geometry primitives
├── geometry2d (17 files) - 2D geometry primitives
├── helper (9 files) - Utility helpers (all in one package)
├── preview - Preview system (builder, mapper, payload, sampling, statistics)
├── step
│   ├── model (1264 files) - All STEP entity model classes (flat structure)
│   ├── semantic - Resolvers and registries
│   └── syntax - ANTLR parser and lexer
├── tool (3 files) - Capability scanner and report tools
└── topology (30 files) - B-Rep topology primitives
```

**Note**: step.model was flattened from 36 sub-packages to 1 package on 2026-07-09.
All StepEntity classes now reside directly in `com.minicad.step.model`.
```

- [ ] **Step 2: Update CONTRIBUTING.md package structure section**

Update package descriptions to reflect flat structure.

- [ ] **Step 3: Create package migration guide**

Create file: `docs/package-structure-change.md`

```markdown
# Package Structure Migration (2026-07-09)

## Summary

On July 9, 2026, MiniCAD package structure was optimized to reduce package count and improve navigation efficiency.

## Changes

### step.model (36 → 1 package)

**Before**: 1264 StepEntity files in 36 sub-packages (workflow, annotation, manufacturing, geometry, etc.)

**After**: All 1264 files in single `com.minicad.step.model` package

**Import statement changes**:
- Old: `import com.minicad.step.model.geometry.StepCartesianPoint;`
- New: `import com.minicad.step.model.StepCartesianPoint;`

### helper (3 → 1 package)

**Before**: 9 files in 3 sub-packages (geometry, metadata, validation)

**After**: All 9 files in single `com.minicad.helper` package

**Import statement changes**:
- Old: `import com.minicad.helper.geometry.MathUtilityHelper;`
- New: `import com.minicad.helper.MathUtilityHelper;`

## Migration Impact

- Import statements shortened by 60%+
- Package count reduced from 60+ to ~30
- Navigation simplified (all StepEntity in one package)
- Git history preserved (used `git mv`)

## Developer Guide

When adding new StepEntity classes:
- Place directly in `com.minicad.step.model` package
- No need to classify by STEP entity type
- Use class-level comments for STEP classification if needed

When adding new helper classes:
- Place directly in `com.minicad.helper` package
- Use descriptive class names for clarity
```

- [ ] **Step 4: Commit documentation updates**

```bash
git add README.md CONTRIBUTING.md docs/package-structure-change.md
git commit -m "docs: update package structure documentation

- Update README.md with new flat package structure
- Update CONTRIBUTING.md package descriptions
- Add package-structure-change.md migration guide
- Document import statement changes and developer guide"
```

---

## Phase 5: Final Verification

### Task 11: Verify package count reduction

**Files:**
- Analyze: `src/main/java/com/minicad/**/*.java`

**Interfaces:**
- Consumes: Completed refactoring (Tasks 1-10)
- Produces: Package count verification

**Purpose:** Verify package count reduced from 60+ to ~30 as specified.

- [ ] **Step 1: Count final package count**

```bash
find src/main/java/com/minicad -type d | wc -l
```

Expected: ~30 packages (reduced from 60+)

- [ ] **Step 2: List all packages**

```bash
find src/main/java/com/minicad -type d | sed 's|[^/]*/||g' | sort
```

Expected: Clean list showing flat structure

- [ ] **Step 3: Verify no step.model sub-packages exist**

```bash
find src/main/java/com/minicad/step/model -type d -name "*" | grep -v "^src/main/java/com/minicad/step/model$"
```

Expected: Empty output (no subdirectories)

- [ ] **Step 4: Verify no helper sub-packages exist**

```bash
find src/main/java/com/minicad/helper -type d | grep -v "^src/main/java/com/minicad/helper$"
```

Expected: Empty output (no subdirectories)

- [ ] **Step 5: Create final summary commit**

```bash
git add -A
git commit -m "refactor: complete package structure optimization

Summary:
- Package count: 60+ → ~30 (50% reduction)
- step.model: 36 sub-packages → 1 package (1264 files)
- helper: 3 sub-packages → 1 package (9 files)
- Import statements shortened by 60%+
- All tests pass
- Documentation updated

Migration completed successfully on 2026-07-09"
```

---

## Success Criteria Verification

After all tasks complete, verify:

- [ ] Package count reduced to ~30
- [ ] All tests pass (`mvn clean test`)
- [ ] Example regression tests pass
- [ ] CLI application works
- [ ] Web viewer application works
- [ ] No Git history loss (all commits preserved)
- [ ] Documentation updated

---

## Rollback Plan

If critical issues arise during migration:

1. **Stop migration immediately** on first compilation error or test failure
2. **Analyze error** - check if it's import statement issue or package declaration issue
3. **Fix incrementally** - update problematic imports/packages individually
4. **If major rollback needed**:
   ```bash
   git reset --hard <commit-before-migration>
   ```
   Then restart migration with fixed approach.

---

## Notes

- **Estimated total time**: 6-8 hours (1 day)
- **Files modified**: 1264 (step.model) + 9 (helper) + imports across all codebase
- **Git history preservation**: All file moves use `git mv` to retain history
- **Frequent commits**: Each task produces independent commit for easy rollback