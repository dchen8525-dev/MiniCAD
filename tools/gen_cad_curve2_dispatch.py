#!/usr/bin/env python3
"""Fold StepCadCurveBuilder.buildCurve2's 43-branch sequential-if chain into a
table-driven dispatch (BUILD_CURVE2_RULES).

The original chain (lines ~596..778) is a sequence of
`if (item instanceof X) { ... }` branches, each returning -- so it is
first-match-return dispatch. The terminal (line 779) is a throw, which the loop
leaves in place after the table.

WHY THE HANDLER TAKES `self`:
buildCurve2 is an *instance* method. Three branches need instance state:
  - StepOrientedCurve / StepBoundedCurve2D / StepMappedItem recurse into
    buildCurve2(...) (the same method);
  - StepBoundedCurve reads the instance field `entitiesById` and may recurse.
A static `List<>` of lambdas cannot capture `this`, so the handler interface is
    Object build(StepCadCurveBuilder self, StepEntity item);
and every lambda calls instance members through `self.` (e.g. `self.buildLine2`,
`self.buildCurve2`, `self.entitiesById`). The for-loop passes `this`.

This is *not* verbatim-body-preserving at the source level (the `self.` prefix is
mandatory), so verify_cad_curve2_dispatch.py reconstructs the original branches by
stripping `self.` and asserts they match the committed chain verbatim.

This script edits the source in place:
  1. replaces the 43-branch interior of buildCurve2 with a for-loop dispatch
     (keeps the terminal throw);
  2. inserts the record/interface/table/helper at class level, before buildCurve2;
  3. writes the frozen primary-type order to src/test/resources/...-dispatch-order.txt.

Idempotent: aborts if BUILD_CURVE2_RULES already exists.

GOTCHA: Java method-invocation argument lists FORBID a trailing comma. Only the
non-final table entries get a comma.
"""
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
SRC = ROOT / "src/main/java/com/minicad/step/semantic/StepCadCurveBuilder.java"
ORDER_TXT = ROOT / "src/test/resources/cad-curve2-dispatch-order.txt"

TABLE_FIELD = "BUILD_CURVE2_RULES"
METHOD_SIG = "    Object buildCurve2(StepEntity item) {"
TERMINAL = '        throw new UnsupportedGeometryException("2D curve type " + stepEntityTypeName(item) + " is not supported");'
INSERT_BEFORE = METHOD_SIG

NEW_DISPATCH = [
    "        for (CadCurve2Rule rule : BUILD_CURVE2_RULES) {",
    "            if (rule.type().isInstance(item)) {",
    "                return rule.handler().build(this, item);",
    "            }",
    "        }",
]

TABLE_HEADER = [
    "    // buildCurve2 dispatch table (first-match-return, mirrors the original sequential ifs).",
    "    private record CadCurve2Rule(Class<? extends StepEntity> type, CadCurve2Handler handler) {}",
    "",
    "    private interface CadCurve2Handler {",
    "        Object build(StepCadCurveBuilder self, StepEntity item);",
    "    }",
    "",
    "    private static CadCurve2Rule cadCurve2Rule(",
    "            Class<? extends StepEntity> type, CadCurve2Handler handler) {",
    "        return new CadCurve2Rule(type, handler);",
    "    }",
    "",
    "    private static final List<CadCurve2Rule> " + TABLE_FIELD + " = List.of(",
]

HEADER_RE = re.compile(r"if \((.*)\)\s*\{$")


def selfify(line):
    """Prefix instance method/field references so a static lambda can reach them."""
    line = re.sub(r"\b(build\w+)\(", r"self.\1(", line)
    line = line.replace("entitiesById", "self.entitiesById")
    return line


def extract_branches(lines):
    """Return list of (type_name, condition, body_lines, is_guarded) for each branch."""
    branches = []
    i = 0
    n = len(lines)
    while i < n:
        line = lines[i]
        stripped = line.strip()
        if stripped.startswith("if (item instanceof ") and stripped.endswith("{"):
            m = HEADER_RE.search(line)
            condition = m.group(1)
            type_name = re.search(r"instanceof (\w+)", condition).group(1)
            is_guarded = "&&" in condition
            depth = 1
            body = []
            k = i + 1
            while k < n:
                for ch in lines[k]:
                    if ch == "{":
                        depth += 1
                    elif ch == "}":
                        depth -= 1
                if depth == 0:
                    break
                body.append(lines[k])
                k += 1
            while body and body[0].strip() == "":
                body.pop(0)
            while body and body[-1].strip() == "":
                body.pop()
            branches.append((type_name, condition, body, is_guarded))
            i = k + 1
            continue
        i += 1
    return branches


def main() -> None:
    text = SRC.read_text(encoding="utf-8")
    if TABLE_FIELD in text:
        raise SystemExit("ABORT: " + TABLE_FIELD + " already present; refactor applied?")
    lines = text.split("\n")

    mi = next(i for i, ln in enumerate(lines) if ln == METHOD_SIG)
    ti = next(i for i in range(mi, len(lines)) if lines[i].strip() == TERMINAL.strip())
    bi = next(i for i in range(mi + 1, ti) if lines[i].strip().startswith("if (item instanceof "))

    branches = extract_branches(lines[bi:ti])
    if not branches:
        raise SystemExit("ABORT: no branches extracted")

    entries = []
    types = []
    for type_name, condition, body, is_guarded in branches:
        types.append(type_name)
        if is_guarded:
            raise SystemExit("ABORT: guarded branch in buildCurve2 -- add null-fallthrough support")
        transformed = "\n".join("            " + selfify(b.strip()) for b in body)
        entries.append(
            "        cadCurve2Rule(%s.class, (self, item) -> {\n%s\n        })" % (type_name, transformed)
        )

    # 1) replace the branch interior with the for-loop; keep the terminal throw.
    lines[bi:ti] = NEW_DISPATCH + [""]

    # 2) insert the class-level table machinery before buildCurve2.
    # Java method-invocation arg lists forbid a trailing comma; only non-final get one.
    rendered = [e + "," if j < len(entries) - 1 else e for j, e in enumerate(entries)]
    ii = next(i for i, ln in enumerate(lines) if ln == INSERT_BEFORE)
    class_block = TABLE_HEADER + rendered + ["    );", ""]
    lines[ii:ii] = class_block

    SRC.write_text("\n".join(lines) + "\n", encoding="utf-8")

    # 3) write frozen primary-type order (used by the dispatch-table guard test).
    ORDER_TXT.parent.mkdir(parents=True, exist_ok=True)
    ORDER_TXT.write_text("\n".join(types) + "\n", encoding="utf-8")

    print("OK: wrote", SRC, "with", len(branches), "rules; order ->", ORDER_TXT)


if __name__ == "__main__":
    main()
