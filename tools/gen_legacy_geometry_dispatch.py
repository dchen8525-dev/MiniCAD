#!/usr/bin/env python3
"""Fold StepLegacyGeometryBuilder.buildLegacyGeometry's 6-branch else-if B-rep
shell-removal chain into a first-match table (LEGACY_SOLID_SHELL_RULES).

The original chain (lines ~92..119) is a clean else-if: the six B-rep types are
mutually exclusive (no subtype relationship), the first match wins, and a miss is
a no-op. That maps to a first-match `return` dispatch (NOT null-fallthrough).

This script edits the source in place:
  1. replaces the for/else-if block with a one-line delegation to
     removeShellsReferencedBySolids(...);
  2. inserts the record/interface/table/helper before FACE_PROGRESS_INTERVAL.

Idempotent: aborts if LEGACY_SOLID_SHELL_RULES already exists.
"""
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
SRC = ROOT / "src/main/java/com/minicad/export/json/StepLegacyGeometryBuilder.java"

START_MARK = "        // Remove shells that are referenced by B-rep solids to avoid duplicate processing"
END_MARK = "        GeometryCollection shellGeometry = buildGeometryForShells("
INSERT_BEFORE = "    private static final int FACE_PROGRESS_INTERVAL = 25;"

TABLE_FIELD = "LEGACY_SOLID_SHELL_RULES"

NEW_LOOP = [
    "        // Remove shells that are referenced by B-rep solids to avoid duplicate processing",
    "        for (Integer solidId : solidIds) {",
    "            removeShellsReferencedBySolids(resolved.get(solidId), shellIds);",
    "        }",
]

TABLE = [
    "    // buildLegacyGeometry B-rep-solid shell-removal rules.",
    "    // Clean first-match else-if chain: the six B-rep types are mutually exclusive (no subtype",
    "    // relationship), so order is not load-bearing, but the table preserves the original order.",
    "    private record LegacySolidShellRule(Class<? extends StepEntity> type, LegacySolidShellHandler handler) {}",
    "",
    "    private interface LegacySolidShellHandler {",
    "        void removeShells(Set<Integer> shellIds, StepEntity solidEntity);",
    "    }",
    "",
    "    private static LegacySolidShellRule legacySolidShellRule(",
    "            Class<? extends StepEntity> type, LegacySolidShellHandler handler) {",
    "        return new LegacySolidShellRule(type, handler);",
    "    }",
    "",
    "    private static final List<LegacySolidShellRule> LEGACY_SOLID_SHELL_RULES = List.of(",
    "        legacySolidShellRule(StepManifoldSolidBrep.class, (shellIds, solidEntity) -> {",
    "            shellIds.remove(((StepManifoldSolidBrep) solidEntity).outer().id());",
    "        }),",
    "        legacySolidShellRule(StepFacettedBrep.class, (shellIds, solidEntity) -> {",
    "            shellIds.remove(((StepFacettedBrep) solidEntity).outer().id());",
    "        }),",
    "        legacySolidShellRule(StepNonManifoldSolidBrep.class, (shellIds, solidEntity) -> {",
    "            shellIds.remove(((StepNonManifoldSolidBrep) solidEntity).outer().id());",
    "        }),",
    "        legacySolidShellRule(StepAdvancedBrep.class, (shellIds, solidEntity) -> {",
    "            StepAdvancedBrep brep = (StepAdvancedBrep) solidEntity;",
    "            shellIds.remove(brep.outer().id());",
    "            for (StepEntity voidShell : brep.voids()) {",
    "                shellIds.remove(voidShell.id());",
    "            }",
    "        }),",
    "        legacySolidShellRule(StepBrepWithVoids.class, (shellIds, solidEntity) -> {",
    "            StepBrepWithVoids brep = (StepBrepWithVoids) solidEntity;",
    "            shellIds.remove(brep.outer().id());",
    "            for (StepEntity voidShell : brep.voids()) {",
    "                shellIds.remove(voidShell.id());",
    "            }",
    "        }),",
    "        legacySolidShellRule(StepFacetedBrepAndBrepWithVoids.class, (shellIds, solidEntity) -> {",
    "            StepFacetedBrepAndBrepWithVoids brep = (StepFacetedBrepAndBrepWithVoids) solidEntity;",
    "            shellIds.remove(brep.outer().id());",
    "            for (StepEntity voidShell : brep.voids()) {",
    "                shellIds.remove(voidShell.id());",
    "            }",
    "        })",
    "    );",
    "",
    "    private static void removeShellsReferencedBySolids(StepEntity solidEntity, Set<Integer> shellIds) {",
    "        if (solidEntity == null) {",
    "            return;",
    "        }",
    "        for (LegacySolidShellRule rule : LEGACY_SOLID_SHELL_RULES) {",
    "            if (rule.type().isInstance(solidEntity)) {",
    "                rule.handler().removeShells(shellIds, solidEntity);",
    "                return;",
    "            }",
    "        }",
    "    }",
    "",
]


def main() -> None:
    text = SRC.read_text(encoding="utf-8")
    if TABLE_FIELD in text:
        raise SystemExit("ABORT: " + TABLE_FIELD + " already present; refactor applied?")
    lines = text.split("\n")

    # 1) locate the for/else-if block
    si = next(i for i, ln in enumerate(lines) if ln == START_MARK)
    ei = next(i for i in range(si, len(lines)) if lines[i].startswith(END_MARK))
    # `END_MARK` is the line right after the loop's closing brace; replace lines[si:ei]
    # (the comment + for/else-if block) with the new loop, keeping END_MARK in place.
    assert lines[ei].startswith(END_MARK), "END_MARK not found after loop"
    lines[si:ei] = NEW_LOOP

    # 2) insert the table machinery before FACE_PROGRESS_INTERVAL
    ii = next(i for i, ln in enumerate(lines) if ln == INSERT_BEFORE)
    lines[ii:ii] = TABLE

    SRC.write_text("\n".join(lines) + "\n", encoding="utf-8")
    print("OK: wrote", SRC)


if __name__ == "__main__":
    main()
