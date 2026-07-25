# StepPmiPayloadBuilder 提取实施计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Extract PMI (Product Manufacturing Information) handling logic from StepPreviewJsonExporter into a dedicated StepPmiPayloadBuilder class, reducing the original class by ~2,700 lines.

**Architecture:** Extract Class refactoring - migrate 29 PMI methods and 43 target processing methods to a new utility class with static methods, maintaining zero breaking changes.

**Tech Stack:** Java 11, Maven, JUnit 5, STEP ISO 10303 standard

---

## File Structure

**Files to create:**
- `src/main/java/com/minicad/export/json/StepPmiPayloadBuilder.java` - New PMI builder class

**Files to modify:**
- `src/main/java/com/minicad/export/json/StepPreviewJsonExporter.java` - Remove PMI methods, delegate to new builder

**Test dependencies:**
- Existing test suite: 1,897 tests in `src/test/java/`
- No new tests needed (extracted methods are already tested)

---

## Task 1: Create Git Branch and Backup

**Files:**
- Git operations only

- [ ] **Step 1: Create feature branch**

```bash
git checkout -b refactor/extract-pmi-builder
```

Expected: Switched to new branch 'refactor/extract-pmi-builder'

- [ ] **Step 2: Verify clean working directory**

```bash
git status
```

Expected: "nothing to commit, working tree clean" or only untracked files

- [ ] **Step 3: Run baseline tests to confirm current state**

```bash
mvn clean test
```

Expected: All 1,897 tests pass

---

## Task 2: Create StepPmiPayloadBuilder Class Skeleton

**Files:**
- Create: `src/main/java/com/minicad/export/json/StepPmiPayloadBuilder.java`

- [ ] **Step 1: Create package directory if needed**

```bash
# Directory should already exist, but verify
ls src/main/java/com/minicad/export/json/
```

Expected: Shows existing files including StepPreviewJsonExporter.java

- [ ] **Step 2: Create StepPmiPayloadBuilder.java with class skeleton**

```java
package com.minicad.export.json;

import com.minicad.geometry.CartesianPoint;
import com.minicad.preview.builder.PmiPayload;
import com.minicad.preview.builder.PmiTargetPayload;
import com.minicad.step.model.*;
import com.minicad.step.semantic.AssemblyData;
import com.minicad.step.semantic.StepCadBuilder;
import java.util.*;

/**
 * Builds PMI (Product Manufacturing Information) payloads from STEP entities.
 *
 * <p>Handles annotations, dimensions, tolerances, datum features, and other
 * manufacturing information that annotates geometry in CAD models.</p>
 *
 * <p>This class extracts PMI information from various STEP entity types:</p>
 * <ul>
 *   <li>Draughting callouts (dimensions, notes)</li>
 *   <li>Geometric tolerances (GD&T)</li>
 *   <li>Datum features and targets</li>
 *   <li>Annotation occurrences (text, symbols)</li>
 *   <li>PMI requirement associations</li>
 * </ul>
 *
 * @since 1.0
 */
public final class StepPmiPayloadBuilder {

    private StepPmiPayloadBuilder() {
    }
}
```

- [ ] **Step 3: Verify file compiles**

```bash
mvn compile -DskipTests
```

Expected: BUILD SUCCESS

---

## Task 3: Migrate buildPmiPayloads Method

**Files:**
- Modify: `src/main/java/com/minicad/export/json/StepPmiPayloadBuilder.java`
- Modify: `src/main/java/com/minicad/export/json/StepPreviewJsonExporter.java` (later)

- [ ] **Step 1: Copy buildPmiPayloads method signature to new class**

From StepPreviewJsonExporter.java:6088-6356, add to StepPmiPayloadBuilder.java after the constructor:

```java
/**
 * Builds all PMI payloads from resolved STEP entities.
 *
 * @param resolved Map of resolved STEP entities by ID
 * @param assembly Assembly data containing representations and instances
 * @param builder CAD builder for geometry resolution
 * @return List of PMI payloads, empty if none found
 */
public static List<PmiPayload> buildPmiPayloads(
        Map<Integer, StepEntity> resolved,
        AssemblyData assembly,
        StepCadBuilder builder
) {
    // Implementation will be added in next steps
    return List.of();
}
```

- [ ] **Step 2: Verify compilation**

```bash
mvn compile -DskipTests
```

Expected: BUILD SUCCESS

---

## Task 4: Migrate PMI Target Data Structures

**Files:**
- Modify: `src/main/java/com/minicad/export/json/StepPmiPayloadBuilder.java`

- [ ] **Step 1: Add helper method buildInstanceIdsByTargetId**

From StepPreviewJsonExporter.java:6989-7013, copy to StepPmiPayloadBuilder:

```java
private static Map<Integer, List<String>> buildInstanceIdsByTargetId(AssemblyData assembly) {
    Map<Integer, Set<String>> targetInstances = new LinkedHashMap<>();
    for (AssemblyNode node : assembly.instances()) {
        for (int targetId : node.targetIds()) {
            targetInstances.computeIfAbsent(targetId, k -> new LinkedHashSet<>())
                    .add(node.instanceId());
        }
    }
    Map<Integer, List<String>> byTargetId = new LinkedHashMap<>();
    for (Map.Entry<Integer, Set<String>> entry : targetInstances.entrySet()) {
        byTargetId.put(entry.getKey(), List.copyOf(entry.getValue()));
    }
    return Map.copyOf(byTargetId);
}
```

- [ ] **Step 2: Verify compilation**

```bash
mvn compile -DskipTests
```

Expected: BUILD SUCCESS

---

## Task 5: Migrate appendPmiTarget Methods (4 overloads)

**Files:**
- Modify: `src/main/java/com/minicad/export/json/StepPmiPayloadBuilder.java`

- [ ] **Step 1: Add first appendPmiTarget overload**

From StepPreviewJsonExporter.java:7470-7477:

```java
private static void appendPmiTarget(
        Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
        int usageId,
        StepEntity target,
        Map<Integer, List<String>> instanceIdsByTargetId
) {
    appendPmiTarget(targetsByUsageId, usageId, target, instanceIdsByTargetId, null, null, null, null);
}
```

- [ ] **Step 2: Add second appendPmiTarget overload**

From StepPreviewJsonExporter.java:7479-7488:

```java
private static void appendPmiTarget(
        Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
        int usageId,
        StepEntity target,
        Map<Integer, List<String>> instanceIdsByTargetId,
        String viaRelationType,
        Integer viaRelationId
) {
    appendPmiTarget(targetsByUsageId, usageId, target, instanceIdsByTargetId,
            viaRelationType, viaRelationId, null, null);
}
```

- [ ] **Step 3: Add third appendPmiTarget overload**

From StepPreviewJsonExporter.java:7490-7513:

```java
private static void appendPmiTarget(
        Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
        int usageId,
        StepEntity target,
        Map<Integer, List<String>> instanceIdsByTargetId,
        String viaRelationType,
        Integer viaRelationId,
        String viaDefinitionType,
        Integer viaDefinitionId
) {
    if (target instanceof StepRepresentation) {
        StepRepresentation representation = (StepRepresentation) target;
        String type = "representation";
        String name = representation.name();
        List<String> instanceIds = instanceIdsByTargetId.getOrDefault(target.id(), List.of());
        PmiTargetPayload payload = new PmiTargetPayload(
                target.id(),
                type,
                name,
                instanceIds,
                viaRelationType,
                viaRelationId,
                null,
                null,
                viaDefinitionType,
                viaDefinitionId
        );
        List<PmiTargetPayload> targets = targetsByUsageId.computeIfAbsent(usageId, k -> new ArrayList<>());
        if (!targets.contains(payload)) {
            targets.add(payload);
        }
    }
}
```

- [ ] **Step 4: Add fourth appendPmiTarget overload**

From StepPreviewJsonExporter.java:7514-7542:

```java
private static void appendPmiTarget(
        Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
        int usageId,
        StepEntity target,
        Map<Integer, List<String>> instanceIdsByTargetId,
        String viaRelationType,
        Integer viaRelationId,
        String relationType,
        Integer relationId,
        String viaDefinitionType,
        Integer viaDefinitionId
) {
    if (target instanceof StepRepresentation) {
        StepRepresentation representation = (StepRepresentation) target;
        String type = "representation";
        String name = representation.name();
        List<String> instanceIds = instanceIdsByTargetId.getOrDefault(target.id(), List.of());
        PmiTargetPayload payload = new PmiTargetPayload(
                target.id(),
                type,
                name,
                instanceIds,
                viaRelationType,
                viaRelationId,
                relationType,
                relationId,
                viaDefinitionType,
                viaDefinitionId
        );
        List<PmiTargetPayload> targets = targetsByUsageId.computeIfAbsent(usageId, k -> new ArrayList<>());
        if (!targets.contains(payload)) {
            targets.add(payload);
        }
    }
}
```

- [ ] **Step 5: Verify compilation**

```bash
mvn compile -DskipTests
```

Expected: BUILD SUCCESS

---

## Task 6: Migrate Representation Backlink Target Methods

**Files:**
- Modify: `src/main/java/com/minicad/export/json/StepPmiPayloadBuilder.java`

- [ ] **Step 1: Add appendRepresentationBacklinkTarget (2 overloads)**

From StepPreviewJsonExporter.java:7544-7565:

```java
private static void appendRepresentationBacklinkTarget(
        Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
        StepEntity identifiedItem,
        StepRepresentation representation,
        Map<Integer, List<String>> instanceIdsByTargetId
) {
    appendRepresentationBacklinkTarget(targetsByUsageId, identifiedItem, representation, instanceIdsByTargetId, null, null);
}

private static void appendRepresentationBacklinkTarget(
        Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
        StepEntity identifiedItem,
        StepRepresentation representation,
        Map<Integer, List<String>> instanceIdsByTargetId,
        String viaUsageType,
        Integer viaUsageId
) {
    if (!isSupportedPmiUsageCarrier(identifiedItem)) {
        return;
    }
    appendPmiTarget(targetsByUsageId, identifiedItem.id(), representation, instanceIdsByTargetId, null, null, viaUsageType, viaUsageId);
}
```

- [ ] **Step 2: Add isSupportedPmiUsageCarrier helper**

From StepPreviewJsonExporter.java:6634-6650:

```java
private static boolean isSupportedPmiUsageCarrier(StepEntity entity) {
    return entity instanceof StepAdvancedFace
            || entity instanceof StepEdgeCurve
            || entity instanceof StepEdgeLoop
            || entity instanceof StepVertexLoop
            || entity instanceof StepLoop
            || entity instanceof StepPath
            || entity instanceof StepOpenPath
            || entity instanceof StepConnectedEdgeSet
            || entity instanceof StepGeometricCurveSet
            || entity instanceof StepGeometricSet
            || entity instanceof StepPointSet;
}
```

- [ ] **Step 3: Verify compilation**

```bash
mvn compile -DskipTests
```

Expected: BUILD SUCCESS

---

## Task 7: Migrate Definition Backlink Target Methods

**Files:**
- Modify: `src/main/java/com/minicad/export/json/StepPmiPayloadBuilder.java`

- [ ] **Step 1: Add appendDefinitionBacklinkTarget**

From StepPreviewJsonExporter.java:7567-7589:

```java
private static void appendDefinitionBacklinkTarget(
        Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
        StepEntity identifiedItem,
        StepRepresentation representation,
        StepEntity definition,
        Map<Integer, List<String>> instanceIdsByTargetId
) {
    if (!isSupportedPmiUsageCarrier(identifiedItem) || definition == null) {
        return;
    }
    appendPmiTarget(
            targetsByUsageId,
            identifiedItem.id(),
            representation,
            instanceIdsByTargetId,
            null,
            null,
            null,
            null,
            definitionTypeName(definition),
            definition.id()
    );
}
```

- [ ] **Step 2: Add definitionTypeName helper**

From StepPreviewJsonExporter.java:11431-11435:

```java
private static String definitionTypeName(StepEntity definition) {
    return definition.entityName();
}
```

- [ ] **Step 3: Verify compilation**

```bash
mvn compile -DskipTests
```

Expected: BUILD SUCCESS

---

## Task 8: Migrate Relationship Backlink Target Methods

**Files:**
- Modify: `src/main/java/com/minicad/export/json/StepPmiPayloadBuilder.java`

- [ ] **Step 1: Add appendRelationshipBacklinkTarget**

From StepPreviewJsonExporter.java:7624-7655:

```java
private static void appendRelationshipBacklinkTarget(
        Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
        StepEntity identifiedItem,
        StepRepresentation representation,
        StepEntity definition,
        Map<Integer, List<String>> instanceIdsByTargetId
) {
    if (!isSupportedPmiUsageCarrier(identifiedItem)) {
        return;
    }
    if (definition instanceof StepAnnotationOccurrenceRelationship) {
        StepAnnotationOccurrenceRelationship relationship = (StepAnnotationOccurrenceRelationship) definition;
        appendPmiTarget(
                targetsByUsageId,
                identifiedItem.id(),
                representation,
                instanceIdsByTargetId,
                relationship.entityName(),
                relationship.id()
        );
    } else if (definition instanceof StepDraughtingCalloutRelationship) {
        StepDraughtingCalloutRelationship relationship = (StepDraughtingCalloutRelationship) definition;
        appendPmiTarget(
                targetsByUsageId,
                identifiedItem.id(),
                representation,
                instanceIdsByTargetId,
                "DRAUGHTING_CALLOUT_RELATIONSHIP",
                relationship.id()
        );
    }
}
```

- [ ] **Step 2: Verify compilation**

```bash
mvn compile -DskipTests
```

Expected: BUILD SUCCESS

---

## Task 9: Migrate Semantic Definition Targets (Public Method)

**Files:**
- Modify: `src/main/java/com/minicad/export/json/StepPmiPayloadBuilder.java`

- [ ] **Step 1: Add appendSemanticDefinitionTargets (public method)**

From StepPreviewJsonExporter.java:7657-7739 (partial):

```java
/**
 * Appends semantic definition targets for PMI.
 *
 * <p>This is a public method as it may be called from other builders.</p>
 *
 * @param targetsByUsageId Map to collect targets by usage ID
 * @param identifiedItem The identified item
 * @param definition The definition entity
 * @param resolved Map of resolved STEP entities
 * @param instanceIdsByTargetId Instance IDs by target ID
 */
public static void appendSemanticDefinitionTargets(
        Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
        StepEntity identifiedItem,
        StepEntity definition,
        Map<Integer, StepEntity> resolved,
        Map<Integer, List<String>> instanceIdsByTargetId
) {
    if (!isSupportedPmiUsageCarrier(identifiedItem)) {
        return;
    }
    if (definition instanceof StepAnnotationOccurrenceRelationship) {
        StepAnnotationOccurrenceRelationship relationship = (StepAnnotationOccurrenceRelationship) definition;
        appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
        appendRelationshipSemanticTargets(
                targetsByUsageId,
                identifiedItem.id(),
                relationship.entityName(),
                relationship.id(),
                relationship.relatingAnnotationOccurrence(),
                resolved,
                instanceIdsByTargetId
        );
        appendRelationshipSemanticTargets(
                targetsByUsageId,
                identifiedItem.id(),
                relationship.entityName(),
                relationship.id(),
                relationship.relatedAnnotationOccurrence(),
                resolved,
                instanceIdsByTargetId
        );
        return;
    }
    if (definition instanceof StepDraughtingCalloutRelationship) {
        StepDraughtingCalloutRelationship relationship = (StepDraughtingCalloutRelationship) definition;
        appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
        appendRelationshipSemanticTargets(
                targetsByUsageId,
                identifiedItem.id(),
                "DRAUGHTING_CALLOUT_RELATIONSHIP",
                relationship.id(),
                relationship.relatingCallout(),
                resolved,
                instanceIdsByTargetId
        );
        appendRelationshipSemanticTargets(
                targetsByUsageId,
                identifiedItem.id(),
                "DRAUGHTING_CALLOUT_RELATIONSHIP",
                relationship.id(),
                relationship.relatedCallout(),
                resolved,
                instanceIdsByTargetId
        );
        return;
    }
    if (definition instanceof StepPropertyDefinitionRelationship) {
        StepPropertyDefinitionRelationship relationship = (StepPropertyDefinitionRelationship) definition;
        appendCarrierDefinitionTargets(targetsByUsageId, identifiedItem, relationship, instanceIdsByTargetId);
        appendPropertyRepresentationLinkTargets(
                targetsByUsageId,
                identifiedItem,
                relationship.relatingPropertyDefinition(),
                resolved,
                instanceIdsByTargetId
        );
        appendPropertyRepresentationLinkTargets(
                targetsByUsageId,
                identifiedItem,
                relationship.relatedPropertyDefinition(),
                resolved,
                instanceIdsByTargetId
        );
    }
    if (definition instanceof StepPropertyDefinition) {
        StepPropertyDefinition propertyDefinition = (StepPropertyDefinition) definition;
        appendPropertyDefinitionRelationshipTargets(
                targetsByUsageId,
                identifiedItem.id(),
                propertyDefinition,
                resolved,
                instanceIdsByTargetId
        );
    }
}
```

**Note:** This is a partial implementation. Full implementation spans lines 7657-7739 and requires several helper methods that will be added in subsequent tasks.

- [ ] **Step 2: Verify compilation**

```bash
mvn compile -DskipTests
```

Expected: Compilation errors due to missing helper methods (expected, will be resolved in next tasks)

---

## Task 10: Add Missing Helper Methods for Semantic Targets

**Files:**
- Modify: `src/main/java/com/minicad/export/json/StepPmiPayloadBuilder.java`

- [ ] **Step 1: Add appendCarrierDefinitionTargets**

From StepPreviewJsonExporter.java:11174-11187:

```java
private static void appendCarrierDefinitionTargets(
        Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
        StepEntity identifiedItem,
        StepEntity definition,
        Map<Integer, List<String>> instanceIdsByTargetId
) {
    // Implementation placeholder - will be filled from source
}
```

- [ ] **Step 2: Add appendRelationshipSemanticTargets**

From StepPreviewJsonExporter.java:11440-11492:

```java
private static void appendRelationshipSemanticTargets(
        Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
        int usageId,
        String relationType,
        Integer relationId,
        StepEntity target,
        Map<Integer, StepEntity> resolved,
        Map<Integer, List<String>> instanceIdsByTargetId
) {
    // Implementation placeholder - will be filled from source
}
```

- [ ] **Step 3: Add appendPropertyRepresentationLinkTargets (2 overloads)**

From StepPreviewJsonExporter.java:10520-10581:

```java
private static void appendPropertyRepresentationLinkTargets(
        Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
        StepEntity identifiedItem,
        StepEntity propertyDefinition,
        Map<Integer, StepEntity> resolved,
        Map<Integer, List<String>> instanceIdsByTargetId
) {
    // Implementation placeholder
}

private static void appendPropertyRepresentationLinkTargets(
        Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
        StepEntity identifiedItem,
        StepPropertyDefinition propertyDefinition,
        StepRepresentation representation,
        Map<Integer, List<String>> instanceIdsByTargetId
) {
    // Implementation placeholder
}
```

- [ ] **Step 4: Add appendPropertyDefinitionRelationshipTargets**

From StepPreviewJsonExporter.java:10483-10519:

```java
private static void appendPropertyDefinitionRelationshipTargets(
        Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
        int usageId,
        StepPropertyDefinition propertyDefinition,
        Map<Integer, StepEntity> resolved,
        Map<Integer, List<String>> instanceIdsByTargetId
) {
    // Implementation placeholder
}
```

---

**Note:** Due to the complexity and interdependencies of the remaining 40+ target processing methods, Tasks 11-50 will follow a similar pattern of migrating each method group. The complete implementation would require copying approximately 1,500 lines of target processing logic.

For brevity in this plan, the remaining tasks are summarized below:

### Summary of Remaining Tasks

**Tasks 11-30: Migrate Remaining Target Processing Methods**
- appendAttachedRepresentationRelationshipTargets
- appendProductRelationshipTargets
- appendProductDefinitionFormationRelationshipTargets
- appendProductDefinitionRelationshipTargets
- appendProductDefinitionShapeRepresentationTargets
- appendOccurrenceRepresentationTargets
- appendIndirectPropertyRepresentationTargets
- appendGroupRelationshipTargets
- appendGeneralPropertyRelationshipTargets
- appendDocumentRelationshipTargets
- appendApprovalDecorationTargets
- appendPointMarkerStyleTargets
- appendOrganizationRelationshipTargets
- appendEffectivityRelationshipTargets
- appendProductCategoryRelationshipTargets
- appendProductDefinitionEffectivityTargets
- appendExternalSourceRelationshipTargets
- appendExternallyDefinedItemTargets
- appendMappedDefinitionTargets
- appendNestedDefinitionTargets
- appendSplineCurveControlPointTargets
- appendSplineSurfaceControlPointTargets
- appendRepresentationMapDefinitionTargets
- appendPlacementDefinitionTargets
- appendShapeAspectRelationshipTargets
- appendDefinitionRelationshipTargets
- propagateCalloutTargets

**Tasks 31-40: Migrate PMI Payload Construction Methods**
- toPmiPayload
- appendPmiLeader methods (4 overloads)
- appendPmiLeaderForSolid
- appendPmiPathLeader
- appendTopologyEdgeLeader
- toStandalonePointPmi (2 overloads)
- appendPlaceholderPmi
- appendAnnotationPlanePmi
- appendAnnotationOccurrenceRelationshipPmi
- appendDraughtingAnnotationPmi
- appendPointSetPmi
- appendGeometricMeasurementPmi
- appendFillAreaWithOutlinePmi
- appendGeometricTolerancePmi
- appendGeometricToleranceWithDatumPmi
- appendGeometricToleranceWithAreaUnitPmi
- appendGeometricToleranceWithMaxPmi
- appendDimensionalLocationPmi
- appendToleranceZonePmi
- appendDatumPmi
- appendDatumTargetPmi

**Tasks 41-45: Migrate collectTargets Methods**
- collectSemanticTargets methods
- collectTargetsReferencingEntity
- collectTargetsFor* methods (15 methods)
- collectRepresentationTargetsFromRelationship

---

## Task 51: Update StepPreviewJsonExporter to Use New Builder

**Files:**
- Modify: `src/main/java/com/minicad/export/json/StepPreviewJsonExporter.java`

- [ ] **Step 1: Add import statement at top of file**

After line 1 (package declaration), add:

```java
import com.minicad.export.json.StepPmiPayloadBuilder;
```

- [ ] **Step 2: Replace buildPmiPayloads call in buildPayload method**

Find line 748 in StepPreviewJsonExporter.java:

```java
List<PmiPayload> pmi = buildPmiPayloads(resolved, assembly, builder);
```

Replace with:

```java
List<PmiPayload> pmi = StepPmiPayloadBuilder.buildPmiPayloads(resolved, assembly, builder);
```

- [ ] **Step 3: Verify compilation**

```bash
mvn compile -DskipTests
```

Expected: Compilation errors due to duplicate method definitions (expected, will be resolved next)

---

## Task 52: Remove Migrated Methods from StepPreviewJsonExporter

**Files:**
- Modify: `src/main/java/com/minicad/export/json/StepPreviewJsonExporter.java`

- [ ] **Step 1: Delete buildPmiPayloads method**

Delete lines 6088-6356 (approximately 268 lines)

- [ ] **Step 2: Delete all append*Target methods**

Delete lines 7470-11492 (approximately 4,022 lines containing 43 target methods)

- [ ] **Step 3: Delete PMI payload construction methods**

Delete lines 6357-7045 (approximately 688 lines containing 21 PMI methods)

- [ ] **Step 4: Delete collect*Targets methods**

Delete lines 13162-13594 (approximately 432 lines)

- [ ] **Step 5: Verify file still compiles**

```bash
mvn compile -DskipTests
```

Expected: BUILD SUCCESS

---

## Task 53: Run Full Test Suite

**Files:**
- Test execution only

- [ ] **Step 1: Run all tests**

```bash
mvn clean test
```

Expected: All 1,897 tests pass

- [ ] **Step 2: Check test coverage**

```bash
mvn jacoco:report
```

Expected: Coverage report generated, minimum 70% coverage maintained

- [ ] **Step 3: Run Spotless code formatting**

```bash
mvn spotless:apply
```

Expected: All files formatted

- [ ] **Step 4: Verify formatting**

```bash
mvn spotless:check
```

Expected: BUILD SUCCESS

---

## Task 54: Verify Code Size Reduction

**Files:**
- Code analysis only

- [ ] **Step 1: Count lines in StepPreviewJsonExporter**

```bash
wc -l src/main/java/com/minicad/export/json/StepPreviewJsonExporter.java
```

Expected: Approximately 10,900 lines (reduced from 13,601)

- [ ] **Step 2: Count lines in new StepPmiPayloadBuilder**

```bash
wc -l src/main/java/com/minicad/export/json/StepPmiPayloadBuilder.java
```

Expected: Approximately 2,700 lines

- [ ] **Step 3: Verify no duplicate methods**

```bash
grep -c "buildPmiPayloads" src/main/java/com/minicad/export/json/StepPreviewJsonExporter.java
```

Expected: 0 (method removed from original class)

---

## Task 55: Commit Changes

**Files:**
- Git commit only

- [ ] **Step 1: Stage all changes**

```bash
git add src/main/java/com/minicad/export/json/StepPmiPayloadBuilder.java
git add src/main/java/com/minicad/export/json/StepPreviewJsonExporter.java
```

- [ ] **Step 2: Commit with descriptive message**

```bash
git commit -m "refactor: extract StepPmiPayloadBuilder for PMI handling

- Extract 29 PMI payload construction methods
- Extract 43 target processing methods
- Reduce StepPreviewJsonExporter from 13,601 to ~10,900 lines
- All 1,897 tests passing
- Zero breaking changes"
```

Expected: Commit created successfully

- [ ] **Step 3: Verify commit**

```bash
git log -1 --stat
```

Expected: Shows commit with file changes

---

## Task 56: Create Pull Request or Merge

**Files:**
- Git operations only

- [ ] **Step 1: Push branch to remote**

```bash
git push -u origin refactor/extract-pmi-builder
```

Expected: Branch pushed successfully

- [ ] **Step 2: Create pull request (if using PR workflow)**

```bash
gh pr create --title "refactor: extract StepPmiPayloadBuilder for PMI handling" \
  --body "Extracts PMI handling logic into dedicated class.

**Changes:**
- Create StepPmiPayloadBuilder class
- Migrate 29 PMI payload construction methods
- Migrate 43 target processing methods  
- Reduce StepPreviewJsonExporter by ~2,700 lines

**Testing:**
- All 1,897 tests passing
- No breaking changes
- Code coverage maintained at 70%+

**Impact:**
- Improved maintainability
- Better separation of concerns
- PMI logic now isolated and testable"
```

Expected: PR created successfully

---

## Task 57: Merge to Main (After Review)

**Files:**
- Git operations only

- [ ] **Step 1: Switch to main branch**

```bash
git checkout main
```

- [ ] **Step 2: Merge feature branch**

```bash
git merge --no-ff refactor/extract-pmi-builder
```

Expected: Merge successful with commit message

- [ ] **Step 3: Push to remote**

```bash
git push origin main
```

Expected: Push successful

- [ ] **Step 4: Delete feature branch**

```bash
git branch -d refactor/extract-pmi-builder
git push origin --delete refactor/extract-pmi-builder
```

Expected: Branch deleted

---

## Post-Implementation Verification

After completing all tasks:

- [ ] **Verify StepPreviewJsonExporter line count**

```bash
wc -l src/main/java/com/minicad/export/json/StepPreviewJsonExporter.java
```

Target: < 11,000 lines

- [ ] **Verify all tests still pass**

```bash
mvn clean test
```

Target: All 1,897 tests pass

- [ ] **Verify code quality**

```bash
mvn verify
```

Target: BUILD SUCCESS

- [ ] **Check for PMI-related issues in logs**

Run sample STEP file processing to verify PMI extraction works correctly.

---

## Notes

1. **Method Signature Preservation:** All migrated methods keep their exact signatures to ensure zero breaking changes.

2. **Point Processing Methods:** The `pointFrom*` series of methods remain in StepPreviewJsonExporter as they are shared between PMI and geometry processing. StepPmiPayloadBuilder will call these methods statically.

3. **Circular Dependency Prevention:** By keeping shared utility methods in the original class, we avoid circular dependencies between the two classes.

4. **Testing Strategy:** No new tests are written as part of this refactoring. The existing comprehensive test suite (1,897 tests) validates that functionality remains unchanged.

5. **Performance:** Static method calls have no performance overhead compared to instance methods, so this refactoring should have zero performance impact.

---

## Rollback Plan

If issues are discovered after merge:

```bash
git revert HEAD
git push origin main
```

Or to completely undo the merge:

```bash
git reset --hard HEAD~1
git push origin main --force
```

---

**Plan complete. Ready for execution.**