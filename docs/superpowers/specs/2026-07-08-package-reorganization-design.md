# MiniCAD Package Reorganization Design Document

**Date**: 2026-07-08
**Author**: ZCode Agent
**Status**: Draft - Pending User Review

---

## 1. Background and Problem Statement

### 1.1 Current Structure Analysis

**MiniCAD** is an experimental Java CAD kernel for parsing STEP (ISO 10303) files. After analysis, the following structural problems were identified:

#### Problem 1: `app` Package Overloaded
- **Current state**: 56 files in `com.minicad.app`
- **Issue**: Contains both application entry points (StepViewerApp, StepDumpApp) AND business logic classes (PreviewGlbBuilder, StepMeshExporter, CurveEvaluator, etc.)
- **Impact**:
  - Difficult navigation - too many files in one package
  - Violates single responsibility principle
  - Mixes entry points with implementation

#### Problem 2: `step.model` Package Over-fragmented
- **Current state**: 32 sub-packages, 1264 files
- **Issue**: Many packages are too small (< 25 files)
  - `analysis`: 15 files
  - `organization`: 19 files
  - `document`: 21 files
  - `unit`: 21 files
  - `profile`: 7 files
  - `date_time`: ~5 files
- **Impact**:
  - Navigation complexity - 32 packages to browse
  - No logical grouping for related packages
  - Inconsistent naming (snake_case vs camelCase)

#### Problem 3: Inconsistent Naming
- **Issue**: Package naming style inconsistent
  - Snake_case: `config_mgmt`, `log_audit`, `backup_recovery`
  - CamelCase: `workflow`, `annotation`, `product`
- **Impact**: Violates Java naming conventions (should be camelCase)

#### Problem 4: `tools` Package Naming
- **Current state**: Package named `tools` (plural)
- **Issue**: Java package names conventionally use singular form
- **Impact**: Minor inconsistency with Java conventions

---

## 2. Design Goals

### 2.1 Primary Goals
1. **Separation of Concerns**: Separate application entry points from business logic
2. **Package Consolidation**: Merge small packages to reduce navigation complexity
3. **Naming Consistency**: Unify package naming to camelCase
4. **Logical Grouping**: Add intermediate grouping packages for related entities

### 2.2 Constraints
1. **Preserve STEP Standard Organization**: `step.model` classes are auto-generated STEP entities - minimize changes to preserve upgrade path
2. **Maintain Architecture**: Keep layer separation (syntax → semantic → geometry → application)
3. **Minimize Import Changes**: Keep file moves logical to reduce refactoring effort
4. **Backward Compatibility**: Consider impact on existing code and tests

---

## 3. Detailed Design

### 3.1 App Package Reorganization

**Objective**: Split `app` package (56 files) into 5 packages based on responsibilities.

#### 3.1.1 New Package Structure

```
com.minicad.app (保留4个入口类)
├── StepViewerApp.java        -- Web viewer entry point
├── StepDumpApp.java          -- CLI dump tool entry point
├── StepBenchmarkApp.java     -- Performance benchmark entry point
└── StepCapabilityReportApp.java -- Capability report entry point

com.minicad.export (新建，~15个类)
├── glb/                      -- GLB format export
│   ├── PreviewGlbBuilder.java
│   ├── PreviewMeshExporter.java
│   ├── PreviewMaterialExporter.java
│   └── TessellatedFaceExporter.java
├── mesh/                     -- Mesh export
│   ├── StepMeshExporter.java
│   ├── MeshTriangulatorPlanar.java
│   └── MeshTriangulatorParametric.java
└── json/                     -- JSON export
    ├── StepPreviewJsonExporter.java
    ├── PreviewSerializers.java
    ├── SerializationHelper.java
    └── JsonBuilder.java

com.minicad.preview (新建，~25个类)
├── sampling/                 -- Curve/surface sampling
│   ├── CurveEvaluator.java
│   ├── PreviewCurveEvaluator.java
│   ├── PreviewEdgeSampler.java
│   ├── PreviewSurfaceSampler.java
│   ├── PreviewPcurveSampler.java
│   ├── ConicSamplingHelper.java
│   ├── Curve2SamplingHelper.java
│   ├── Curve3SamplingHelper.java
│   ├── PcurveSamplingHelper.java
│   └── TriangulationHelper.java
├── builder/                  -- Preview model building
│   ├── PreviewFaceBuilder.java
│   ├── PreviewPmiBuilder.java
│   ├── PreviewGeometryCollector.java
│   └── PreviewPmiPayloadTypes.java
├── mapper/                   -- UV/parameter mapping
│   ├── ParametricSurfaceMapper.java
│   ├── PreviewUvMapper.java
│   ├── SurfaceMapperHelper.java
│   └── PreviewUvCoords.java
├── statistics/               -- Statistics and measurement
│   ├── PreviewStatisticsHelper.java
│   ├── BoundsAccumulator.java
│   └ GeometryMeasurementHelper.java
└── payload/                  -- Preview data structures
    ├── PreviewBinaryPayloadTypes.java
    ├── PreviewMeshPayloadTypes.java
    ├── PreviewValidationPayloadTypes.java
    ├── PreviewPayload.java
    ├── PayloadConversionHelper.java
    └── PayloadReductionHelper.java

com.minicad.helper (新建，~10个类)
├── geometry/                 -- Geometry helpers
│   ├── SurfaceGeometryHelper.java
│   ├── MathUtilityHelper.java
│   └── ShellHelper.java
├── metadata/                 -- Metadata extraction
│   ├── ProductMetadataExtractor.java
│   ├── UnitExtractor.java
│   ├── StepMetadataExtractor.java
│   └── StepTextReader.java
└── validation/               -- Validation helpers
    ├── ValidationReportHelper.java
    └── StepValidationMatcher.java

com.minicad.builder (新建，~5个类)
├── StepAssemblyGraphBuilder.java   -- Assembly graph building
├── CompiledStepDocument.java       -- Compiled STEP document (moved from app)
└── StepCapabilityRegistry.java     -- Capability registry
```

#### 3.1.2 File Move Mapping

| Current Location | New Location | File Count | Purpose |
|------------------|--------------|------------|---------|
| app/ | app/ (keep) | 4 | Application entry points only |
| app/ | export/glb/ | 4 | GLB export functionality |
| app/ | export/mesh/ | 3 | Mesh export functionality |
| app/ | export/json/ | 4 | JSON export functionality |
| app/ | preview/sampling/ | 10 | Curve/surface sampling |
| app/ | preview/builder/ | 4 | Preview model building |
| app/ | preview/mapper/ | 4 | UV/parameter mapping |
| app/ | preview/statistics/ | 3 | Statistics helpers |
| app/ | preview/payload/ | 6 | Preview data structures |
| app/ | helper/geometry/ | 3 | Geometry helpers |
| app/ | helper/metadata/ | 4 | Metadata extraction |
| app/ | helper/validation/ | 2 | Validation helpers |
| app/ | builder/ | 3 | Model building |

**Total**: 56 files moved to 5 packages

---

### 3.2 Step.Model Package Reorganization

**Objective**: Reduce sub-packages from 32 to 20 by merging small packages and adding logical grouping.

#### 3.2.1 Packages to Keep Unchanged

Keep large packages (> 60 files) unchanged:

| Package | Files | Purpose | Decision |
|---------|-------|---------|----------|
| workflow | 201 | Workflow entities | ✓ Keep |
| annotation | 118 | Annotation entities | ✓ Keep |
| manufacturing | 117 | Manufacturing entities | ✓ Keep |
| geometry | 116 | Geometry entities | ✓ Keep |
| product | 112 | Product entities | ✓ Keep |
| resource | 67 | Resource entities | ✓ Keep |
| validation | 61 | Validation entities | ✓ Keep |
| action | 49 | Action entities | ✓ Keep |
| fea | 48 | FEA entities | ✓ Keep |
| topology | 32 | Topology entities | ✓ Keep |
| representation | ~60 | Representation entities | ✓ Keep |

**Total kept**: 11 packages, ~1000 files

#### 3.2.2 New Grouping Packages

**A. `core` Package - Core Foundation (~56 files)**

```
core/
├── base/              (11 files) - Base entity classes
│   ├── StepEntity.java
│   ├── StepRepresentationItem.java
│   ├── StepGeometricRepresentationItem.java
│   └── ... (8 more base classes)
├── element/           (~15 files) - Basic elements
│   ├── StepElement*.java
│   └── ...
└── misc/              (~30 files) - Miscellaneous entities
    └── ... (uncategorized entities)
```

**Merge**: base + element + misc → core (reduces 2 packages)

**Rationale**:
- All are foundational/uncategorized entities
- Logical grouping under "core"

---

**B. `management` Package - System Management (~91 files)**

```
management/
├── config/            (32 files) - Configuration management (was config_mgmt)
│   ├── StepConfiguration*.java
│   └── ...
├── log/               (23 files) - Log/audit (was log_audit)
│   ├── StepLog*.java
│   ├── StepAudit*.java
│   └── ...
├── security/          (22 files) - Security entities
│   ├── StepSecurity*.java
│   └── ...
└── backup/            (14 files) - Backup/recovery (was backup_recovery)
│   ├── StepBackup*.java
│   ├── StepRecovery*.java
│   └── ...
```

**Merge**: config_mgmt + log_audit + security + backup_recovery → management (reduces 3 packages)

**Naming Changes**:
- `config_mgmt` → `config` (snake_case → camelCase)
- `log_audit` → `log` (snake_case → camelCase)
- `backup_recovery` → `backup` (snake_case → camelCase)

**Rationale**:
- All are system management related
- Logical grouping under "management"
- Naming unification to camelCase

---

**C. `organization` Package - Organization Entities (~60 files)**

```
organization/
├── org/               (19 files) - Organization structure (was organization)
│   ├── StepOrganization*.java
│   └── ...
├── document/          (21 files) - Document entities
│   ├── StepDocument*.java
│   └── ...
└── approval/          (~20 files) - Approval workflow
│   ├── StepApproval*.java
│   └── ...
```

**Merge**: organization → org + document + approval → organization (reduces 2 packages)

**Naming Changes**:
- `organization` → `org` (avoid package name collision with grouping package)

**Rationale**:
- All are organization/document/approval related
- Logical grouping under "organization"

---

**D. `technical` Package - Technical Specifications (~99 files)**

```
technical/
├── tolerance/         (39 files) - Tolerance specifications
│   ├── StepTolerance*.java
│   └── ...
├── kinematic/         (39 files) - Kinematics
│   ├── StepKinematic*.java
│   └── ...
├── unit/              (21 files) - Unit definitions
│   ├── StepUnit*.java
│   └── ...
└── date/              (~5 files) - Date/time (was date_time)
│   ├── StepDate*.java
│   ├── StepTime*.java
│   └── ...
```

**Merge**: tolerance + kinematic + unit + date_time → technical (reduces 3 packages)

**Naming Changes**:
- `date_time` → `date` (snake_case → camelCase)

**Rationale**:
- All are technical specification entities
- Logical grouping under "technical"

---

**E. `profile_analysis` Package - Profile and Analysis (~22 files)**

```
profile_analysis/
├── profile/           (7 files) - Profile definitions
│   ├── StepProfile*.java
│   └── ...
└── analysis/          (15 files) - Analysis entities
│   ├── StepAnalysis*.java
│   └── ...
```

**Merge**: profile + analysis → profile_analysis (reduces 1 package)

**Rationale**:
- Both are profile/analysis related
- Logical grouping

---

**F. Other Packages to Keep**

```
step/model/
├── classification/    (30 files) ✓ Keep - Classification entities
├── expression/        (~20 files) ✓ Keep - Expression entities
└── system/            (10 files) ✓ Keep - System entities
```

**Total**: 3 additional packages kept

---

#### 3.2.3 Package Count Summary

| Category | Before | After | Change |
|----------|--------|-------|--------|
| Large packages kept | 11 | 11 | 0 |
| Small packages merged | 21 | 9 | -12 |
| **Total sub-packages** | **32** | **20** | **-12** |

---

### 3.3 Other Packages Optimization

#### 3.3.1 Packages to Keep Unchanged

| Package | Files | Purpose | Decision |
|---------|-------|---------|----------|
| common | 9 | Common utilities/exceptions | ✓ Keep unchanged |
| geometry | 38 | 3D geometry classes | ✓ Keep unchanged |
| geometry2d | 16 | 2D geometry classes | ✓ Keep unchanged |
| topology | 12 | Topology classes | ✓ Keep unchanged |
| step.syntax | 12 | Syntax layer (Tokenizer, Parser) | ✓ Keep unchanged |
| step.semantic | 43 | Semantic layer (Resolver, Builder) | ✓ Keep unchanged |

**Rationale**:
- All have clear responsibilities
- File counts are reasonable
- Separation is correct (geometry vs geometry2d vs topology)

---

#### 3.3.2 Package Naming Change

| Package | Current | New | Reason |
|---------|---------|-----|--------|
| tools | `tools` (plural) | `tool` (singular) | Java naming convention - packages should be singular |

---

#### 3.3.3 Test Package Structure

**Objective**: Create standard test package structure mirroring source structure.

```
src/test/java/com/minicad/
├── app/               -- Test app entry points
├── export/            -- Test export functionality
│   ├── glb/
│   ├── mesh/
│   └── json/
├── preview/           -- Test preview functionality
│   ├── sampling/
│   ├── builder/
│   ├── mapper/
│   ├── statistics/
│   └── payload/
├── helper/            -- Test helpers
│   ├── geometry/
│   ├── metadata/
│   └── validation/
├── builder/           -- Test builders
├── common/            -- Test common utilities
├── geometry/          -- Test 3D geometry
├── geometry2d/        -- Test 2D geometry
├── topology/          -- Test topology
├── tool/              -- Test tools
└── step/              -- Test STEP parsing
    ├── syntax/
    ├── semantic/
    └── model/
```

**Principle**: Test package structure mirrors source package structure exactly.

---

## 4. Final Package Structure

```
com.minicad/
├── app/               (4 files) -- Application entry points
├── export/            (15 files) -- Export functionality
│   ├── glb/
│   ├── mesh/
│   └── json/
├── preview/           (25 files) -- Preview functionality
│   ├── sampling/
│   ├── builder/
│   ├── mapper/
│   ├── statistics/
│   └── payload/
├── helper/            (10 files) -- Helper utilities
│   ├── geometry/
│   ├── metadata/
│   └── validation/
├── builder/           (5 files) -- Model builders
├── common/            (9 files) -- Common utilities
├── geometry/          (38 files) -- 3D geometry
├── geometry2d/        (16 files) -- 2D geometry
├── topology/          (12 files) -- Topology
├── tool/              (5 files) -- Utility tools
└── step/              (1319 files) -- STEP parsing
    ├── syntax/        (12 files) -- Syntax layer
    ├── semantic/      (43 files) -- Semantic layer
    │   └── registry/
    └── model/         (1264 files) -- Entity models
        ├── workflow/          (201 files)
        ├── annotation/        (118 files)
        ├── manufacturing/     (117 files)
        ├── geometry/          (116 files)
        ├── product/           (112 files)
        ├── resource/          (67 files)
        ├── validation/        (61 files)
        ├── action/            (49 files)
        ├── fea/               (48 files)
        ├── topology/          (32 files)
        ├── representation/    (~60 files)
        ├── classification/    (30 files)
        ├── expression/        (~20 files)
        ├── system/            (10 files)
        ├── core/              (~56 files)
        │   ├── base/
        │   ├── element/
        │   └── misc/
        ├── management/        (~91 files)
        │   ├── config/
        │   ├── log/
        │   ├── security/
        │   └── backup/
        ├── organization/      (~60 files)
        │   ├── org/
        │   ├── document/
        │   └── approval/
        ├── technical/         (~99 files)
        │   ├── tolerance/
        │   ├── kinematic/
        │   ├── unit/
        │   └── date/
        └── profile_analysis/  (~22 files)
            ├── profile/
            └── analysis/
```

---

## 5. Implementation Strategy

### 5.1 Phased Implementation

**Phase 1**: App Package Reorganization (Highest Impact)
- Move 52 files from `app` to 4 new packages
- Update all imports in affected files
- Run tests to verify functionality

**Phase 2**: Step.Model Package Consolidation (Medium Impact)
- Create 5 new grouping packages (core, management, organization, technical, profile_analysis)
- Move files from 16 small packages to new structure
- Update package declarations and imports
- Preserve STEP entity organization logic

**Phase 3**: Other Package Optimization (Low Impact)
- Rename `tools` → `tool`
- Create test package structure
- Update imports

### 5.2 Implementation Order

1. Create new package directories
2. Move files using `git mv` to preserve history
3. Update package declarations in moved files
4. Update import statements in dependent files
5. Update test imports
6. Run full test suite: `mvn clean test`
7. Commit changes

### 5.3 Risk Mitigation

**Risk 1**: Import statement breakage
- **Mitigation**: Use IDE refactoring tools or automated import fixer
- **Validation**: Run tests after each phase

**Risk 2**: Package name collisions in step.model
- **Mitigation**: Use intermediate package names (org instead of organization)
- **Validation**: Check for duplicate package names before moving

**Risk 3**: Build/test failures
- **Mitigation**: Phase implementation with test validation after each phase
- **Validation**: `mvn clean test` after each phase

---

## 6. Acceptance Criteria

### 6.1 Structure Verification
- ✓ App package contains only 4 entry point classes
- ✓ Export package created with glb/mesh/json sub-packages
- ✓ Preview package created with sampling/builder/mapper/statistics/payload sub-packages
- ✓ Helper package created with geometry/metadata/validation sub-packages
- ✓ Builder package created
- ✓ Step.model sub-packages reduced from 32 to 20
- ✓ All snake_case package names converted to camelCase
- ✓ Tools package renamed to tool

### 6.2 Code Quality
- ✓ All package declarations updated correctly
- ✓ All import statements updated correctly
- ✓ All tests pass: `mvn clean test` returns 0
- ✓ No compiler errors
- ✓ No runtime errors

### 6.3 Documentation
- ✓ README.md package structure diagram updated
- ✓ AGENTS.md package descriptions updated
- ✓ Code comments preserved during moves

---

## 7. Post-Implementation Tasks

1. Update README.md architecture diagram to reflect new structure
2. Update AGENTS.md package descriptions
3. Review and update any hardcoded package references
4. Update CI/CD if any package references exist
5. Notify team members of package structure changes

---

## 8. Appendix: Complete File Move List

### A. App Package Moves (56 files → 4 packages)

**Export Package (15 files)**:
```
PreviewGlbBuilder.java          → export/glb/
PreviewMeshExporter.java        → export/glb/
PreviewMaterialExporter.java    → export/glb/
TessellatedFaceExporter.java    → export/glb/
StepMeshExporter.java           → export/mesh/
MeshTriangulatorPlanar.java     → export/mesh/
MeshTriangulatorParametric.java → export/mesh/
StepPreviewJsonExporter.java    → export/json/
PreviewSerializers.java         → export/json/
SerializationHelper.java        → export/json/
JsonBuilder.java                → export/json/
PreviewPayload.java             → export/json/
PreviewBinaryPayloadTypes.java  → export/json/
PreviewMeshPayloadTypes.java    → export/json/
PreviewValidationPayloadTypes.java → export/json/
```

**Preview Package (25 files)**:
```
CurveEvaluator.java             → preview/sampling/
PreviewCurveEvaluator.java      → preview/sampling/
PreviewEdgeSampler.java         → preview/sampling/
PreviewSurfaceSampler.java      → preview/sampling/
PreviewPcurveSampler.java       → preview/sampling/
ConicSamplingHelper.java        → preview/sampling/
Curve2SamplingHelper.java       → preview/sampling/
Curve3SamplingHelper.java       → preview/sampling/
PcurveSamplingHelper.java       → preview/sampling/
TriangulationHelper.java        → preview/sampling/
PreviewFaceBuilder.java         → preview/builder/
PreviewPmiBuilder.java          → preview/builder/
PreviewGeometryCollector.java   → preview/builder/
PreviewPmiPayloadTypes.java     → preview/builder/
ParametricSurfaceMapper.java    → preview/mapper/
PreviewUvMapper.java            → preview/mapper/
SurfaceMapperHelper.java        → preview/mapper/
PreviewUvCoords.java            → preview/mapper/
PreviewStatisticsHelper.java    → preview/statistics/
BoundsAccumulator.java          → preview/statistics/
GeometryMeasurementHelper.java  → preview/statistics/
PayloadConversionHelper.java    → preview/payload/
PayloadReductionHelper.java     → preview/payload/
```

**Helper Package (10 files)**:
```
SurfaceGeometryHelper.java      → helper/geometry/
MathUtilityHelper.java          → helper/geometry/
ShellHelper.java                → helper/geometry/
ProductMetadataExtractor.java   → helper/metadata/
UnitExtractor.java              → helper/metadata/
StepMetadataExtractor.java      → helper/metadata/
StepTextReader.java             → helper/metadata/
ValidationReportHelper.java     → helper/validation/
StepValidationMatcher.java      → helper/validation/
```

**Builder Package (3 files)**:
```
StepAssemblyGraphBuilder.java   → builder/
CompiledStepDocument.java       → builder/
StepCapabilityRegistry.java     → builder/
```

**Remaining in App (4 files)**:
```
StepViewerApp.java              → stays in app/
StepDumpApp.java                → stays in app/
StepBenchmarkApp.java           → stays in app/
StepCapabilityReportApp.java    → stays in app/
Main.java                       → delete or move (unused entry point)
```

---

## 9. Revision History

| Date | Version | Author | Changes |
|------|---------|--------|---------|
| 2026-07-08 | 1.0 | ZCode Agent | Initial design document |

---

**Next Step**: User review and approval before implementation.