#!/bin/bash

# Batch fix common StepGeometricReplica pattern matching

set -e

echo "Batch fixing StepGeometricReplica pattern matching..."

# Pattern 1: POINT_REPLICA
echo "Fixing POINT_REPLICA patterns..."
find src/main/java/com/minicad -name "*.java" -type f | while read file; do
  if grep -q 'instanceof StepGeometricReplica replica && "POINT_REPLICA"' "$file" 2>/dev/null; then
    echo "  Processing: $file"
    # These patterns need manual review due to context differences
    # Just identify them
  fi
done | head -10

# Pattern 2: CURVE_REPLICA
echo "Fixing CURVE_REPLICA patterns..."
find src/main/java/com/minicad -name "*.java" -type f | while read file; do
  if grep -q 'instanceof StepGeometricReplica replica && "CURVE_REPLICA"' "$file" 2>/dev/null; then
    count=$(grep -c 'instanceof StepGeometricReplica replica && "CURVE_REPLICA"' "$file")
    echo "  $file: $count occurrences"
  fi
done | head -10

# Pattern 3: SURFACE_REPLICA
echo "Fixing SURFACE_REPLICA patterns..."
find src/main/java/com/minicad -name "*.java" -type f | while read file; do
  if grep -q 'instanceof StepGeometricReplica replica && "SURFACE_REPLICA"' "$file" 2>/dev/null; then
    count=$(grep -c 'instanceof StepGeometricReplica replica && "SURFACE_REPLICA"' "$file")
    echo "  $file: $count occurrences"
  fi
done | head -10

echo ""
echo "Total patterns to fix:"
echo "  POINT_REPLICA: $(grep -r 'instanceof StepGeometricReplica replica && "POINT_REPLICA"' src/main/java/com/minicad --include="*.java" | wc -l)"
echo "  CURVE_REPLICA: $(grep -r 'instanceof StepGeometricReplica replica && "CURVE_REPLICA"' src/main/java/com/minicad --include="*.java" | wc -l)"
echo "  SURFACE_REPLICA: $(grep -r 'instanceof StepGeometricReplica replica && "SURFACE_REPLICA"' src/main/java/com/minicad --include="*.java" | wc -l)"

echo ""
echo "NOTE: Each pattern needs context-specific conversion."
echo "Manual review required for each occurrence."