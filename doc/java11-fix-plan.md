# Java 11 Compatibility Fix Plan

## Root Cause Analysis

Project downgrade from Java 21 to Java 11 is incomplete. Multiple Java 16+ features remain:

### Issue Categories

| Feature | Java Version | Files Affected | Fix Pattern |
|---------|-------------|---------------|-------------|
| Pattern matching instanceof | Java 16 | ~50 locations in StepParameterReader.java, StepCadBuilder.java | `if (x instanceof T y)` → `if (x instanceof T) { T y = (T)x; }` |
| Switch expressions | Java 14 | 20 files in app/, step.semantic/, geometry/ | `return switch (x) { case A -> a; }` → if-else chain |
| getFirst()/getLast() | Java 21 | 5 locations | `.getFirst()` → `.get(0)`<br/>`.getLast()` → `.get(list.size()-1)` |
| Inner class structure | - | TypedSelection in StepParameterReader.java | Fix indentation and move inside outer class |
| Duplicate comments | - | Direction3.java, TypedSelection | Remove duplicate javadoc |

## Execution Plan

### Phase 1: Core Files (Critical for compilation)

1. Fix `StepParameterReader.java`:
   - Convert all pattern matching instanceof
   - Fix TypedSelection inner class structure
   - Remove duplicate comments

2. Fix `StepCadBuilder.java`:
   - Convert pattern matching instanceof
   - Convert switch expressions if present

### Phase 2: Application Layer (20 files)

Fix files in order of dependency:
1. PreviewCurveEvaluator.java (switch expressions)
2. PreviewUvMapper.java (switch expressions)
3. PreviewFaceBuilder.java (switch expressions)
4. PreviewPmiBuilder.java (switch expressions)
5. PreviewSerializers.java (switch + getFirst/getLast)
6. PreviewSurfaceSampler.java
7. PreviewFaceBuilder.java
8. PreviewPmiBuilder.java
9. ProductMetadataExtractor.java
10. StepDumpApp.java (switch + getLast)
11. StepMetadataExtractor.java
12. StepPreviewJsonExporter.java (20+ switch expressions)
13. StepViewerApp.java
14. StepCapabilityRegistry.java
15. StepAssemblyGraphBuilder.java
16. StepMeshExporter.java
17. StepCapabilityReportApp.java
18. CompiledStepDocument.java
19. UnitExtractor.java
20. MiniCadIssue.java

### Phase 3: Verification

1. Check no Java 16+ syntax remains
2. Verify all imports correct
3. Run compilation test (when Maven available)

## Fix Patterns Reference

### Pattern 1: instanceof with variable

**Before (Java 16+):**
```java
if (value instanceof StepValue.StringValue stringValue) {
    return stringValue.value();
}
```

**After (Java 11):**
```java
if (value instanceof StepValue.StringValue) {
    StepValue.StringValue stringValue = (StepValue.StringValue) value;
    return stringValue.value();
}
```

### Pattern 2: Switch expression

**Before (Java 14+):**
```java
return switch (curve) {
    case Line3 line -> "line";
    case Circle circle -> "circle";
    default -> "unknown";
};
```

**After (Java 11):**
```java
if (curve instanceof Line3) {
    return "line";
} else if (curve instanceof Circle) {
    return "circle";
} else {
    return "unknown";
}
```

Or use traditional switch with enums:
```java
switch (curve.entityName()) {
    case "LINE": return "line";
    case "CIRCLE": return "circle";
    default: return "unknown";
}
```

### Pattern 3: getFirst/getLast

**Before (Java 21):**
```java
PointPayload last = loop.points().getLast();
```

**After (Java 11):**
```java
PointPayload last = loop.points().get(loop.points().size() - 1);
```

## Progress Tracking

- [ ] Phase 1: StepParameterReader.java fixed
- [ ] Phase 1: StepCadBuilder.java fixed
- [ ] Phase 2: All app/*.java files fixed (20 files)
- [ ] Phase 3: Verification complete