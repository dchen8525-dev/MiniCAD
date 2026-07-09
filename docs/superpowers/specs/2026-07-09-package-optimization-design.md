# Package Structure Optimization Design - Flat Consolidation (方案 A)

**Date**: 2026-07-09
**Status**: Approved
**Approach**: Aggressive flat consolidation to minimize package count and maximize developer navigation efficiency

---

## Problem Statement

MiniCAD project has **60+ packages** with significant navigation and conceptual boundary issues:

1. **step.model over-segmented**: 1264 StepEntity files spread across 36 sub-packages (workflow: 201, annotation: 118, manufacturing: 117, etc.)
2. **helper over-segmented**: 9 files split into 3 sub-packages (geometry, metadata, validation)
3. **Long import statements**: `import com.minicad.step.model.geometry.StepCartesianPoint;` (excessive path depth)
4. **Code location confusion**: Developers don't know which sub-package an entity belongs to (CARTESIAN_POINT in geometry or topology?)

---

## Decision

**Chosen Approach**: **方案 A (Aggressive Flat Consolidation)**

- Flatten `step.model`: 36 sub-packages → 1 package
- Flatten `helper`: 3 sub-packages → 1 package
- Keep `geometry` and `geometry2d` separate (user preference)
- Keep other packages unchanged (already well-organized)

---

## Architecture Changes

### Before Optimization (60+ packages)

```
com.minicad
├── app (13 files)
├── builder (9 files)
├── common (7 files)
├── export (主包 + 3子包: glb, json, mesh)
├── geometry (38 files)
├── geometry2d (17 files)
├── helper (主包空 + 3子包: geometry, metadata, validation - 共9 files)
├── preview (主包空 + 5子包: builder, mapper, payload, sampling, statistics - 共68 files)
├── step
│   ├── model (36个子包!!! 共1264 files)
│   ├── semantic (主包 + registry子包)
│   └── syntax (主包)
├── tool (3 files)
└── topology (约30 files)
```

### After Optimization (~30 packages)

```
com.minicad
├── app (13 files) ✅ unchanged
├── builder (9 files) ✅ unchanged
├── common (7 files) ✅ unchanged
├── export (主包 + 3子包) ✅ unchanged
├── geometry (38 files) ✅ unchanged (separate)
├── geometry2d (17 files) ✅ unchanged (separate)
├── helper (9 files) 🔥 FLATTENED: 3子包→1个包
├── preview (主包 + 5子包) ✅ unchanged
├── step
│   ├── model (1264 files) 🔥 FLATTENED: 36子包→1个包
│   ├── semantic (保持不变)
│   └── syntax (保持不变)
├── tool (3 files) ✅ unchanged
└── topology (约30 files) ✅ unchanged
```

---

## Impact Analysis

### step.model Consolidation (36 → 1 package)

**Advantages**:
- ✅ Import statements shortened by 60%+ (e.g., `com.minicad.step.model.geometry.StepCartesianPoint` → `com.minicad.step.model.StepCartesianPoint`)
- ✅ Code location trivial to find (all StepEntity classes in one package)
- ✅ IDE auto-import smarter (no 36-package selection ambiguity)
- ✅ Package count reduced from 60+ to ~30 (50% reduction)
- ✅ Aligns with Java conventions (Apache Commons, Spring have 1000+ classes in single packages)

**Disadvantages**:
- ❌ Loss of STEP standard semantic classification (workflow vs annotation vs manufacturing)
- ❌ Single package with 1264 files (large but manageable in IDE)
- ❌ Migration effort: ~1-2 days for file moves + import statement updates
- ❌ Git history may need careful preservation (use `git mv` to retain history)

**Decision rationale**: Advantages far outweigh disadvantages. Core pain points (navigation confusion, long imports) fully resolved. STEP classification can be documented in class-level comments if needed.

### helper Consolidation (3 → 1 package)

**Advantages**:
- ✅ Navigation efficiency improved (all helper classes in one place)
- ✅ Import statements simplified
- ✅ Semantically appropriate (9 files don't need 3 sub-packages)

**Disadvantages**:
- ❌ Minor loss of semantic classification (geometry vs metadata vs validation)
- ❌ Migration effort: ~50 import statement updates

**Decision rationale**: Pure optimization with minimal downside. Over-segmentation corrected.

---

## Migration Strategy

### Phase 1: step.model flattening

1. Move all StepEntity files from 36 sub-packages to `step.model` package
2. Use IDE refactoring (IntelliJ IDEA/Eclipse) to preserve imports and Git history
3. Validate: compile + test to ensure no broken references

### Phase 2: helper flattening

1. Move all helper files from 3 sub-packages to `helper` package
2. Update import statements across codebase
3. Validate: compile + test

### Phase 3: Clean up and validation

1. Remove empty sub-package directories
2. Run full test suite: `mvn clean test`
3. Run example regression tests: `mvn test -Dtest=ExamplesRegressionTest`
4. Update documentation (README.md package structure section)

---

## Success Criteria

- ✅ Package count reduced from 60+ to ~30
- ✅ All tests pass (`mvn clean test`)
- ✅ All example files parse successfully
- ✅ Import statements work correctly in IDE
- ✅ No Git history loss (all files moved with `git mv`)

---

## Risk Mitigation

- **File naming conflicts**: Check for duplicate class names across sub-packages before merge (unlikely due to StepXxx prefix)
- **Import statement updates**: Use IDE global refactoring tool (IntelliJ IDEA "Move Class" refactoring)
- **Git history preservation**: Use `git mv` or IDE refactoring with Git integration enabled
- **Test coverage**: Run full test suite after each phase

---

## Scope

- **Included**: step.model (36 sub-packages), helper (3 sub-packages)
- **Excluded**: geometry, geometry2d (user preference), export, preview, builder (already well-organized)

---

## Timeline Estimate

- **Phase 1 (step.model)**: 4-6 hours (1264 file moves + import updates)
- **Phase 2 (helper)**: 30 minutes (9 file moves + import updates)
- **Phase 3 (validation)**: 1 hour (test suite + documentation)
- **Total**: ~6-8 hours (1 day work)