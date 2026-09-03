#!/usr/bin/env python3
"""Fold StepCadCurveBuilder.buildCurve3Internal's 25-branch sequential-if chain into a
table-driven dispatch (BUILD_CURVE3_RULES).

The original chain (lines ~1569..1674) is a sequence of
`if (curve instanceof X) { ... }` branches, each returning -- so it is
first-match-return dispatch. The terminal (line 1674) is a delegate RETURN
`return buildCurve3Callback.apply(curve.id());` (NOT a throw) -- the loop leaves
it in place after the table.

WHY THE HANDLER TAKES `self`:
buildCurve3Internal is an *instance* method. Branches reach instance state:
  - StepOrientedCurve / StepGeometricReplica call the instance field
    `buildCurve3Callback.apply(...)`;
  - StepGeometricReplica also calls the instance field `geometryOps.transformCurve3(...)`;
  - StepOrientedCurve calls the instance method `reverseCompositeCurve(composite)`;
  - StepBoundedCurve reads the instance field `entitiesById` and self-recurses
    `buildCurve3Internal(actual)`;
  - every other branch calls a `build*3(...)` instance method.
A static `List<>` of lambdas cannot capture `this`, so the handler interface is
    Curve3 build(StepCadCurveBuilder self, StepEntity curve);
and every lambda calls instance members through `self.`. The for-loop passes `this`.

This is *not* verbatim-body-preserving at the source level (the `self.` prefix is
mandatory), so verify_cad_curve3_dispatch.py reconstructs the original branches by
stripping `self.` and asserts they match the committed chain verbatim.

This script edits the source in place:
  1. replaces the 25-branch interior of buildCurve3Internal with a for-loop dispatch
     (keeps the terminal delegate return);
  2. inserts the record/interface/table/helper at class level, before buildCurve3Internal;
  3. writes the frozen primary-type order to src/test/resources/...-dispatch-order.txt.

Idempotent: aborts if BUILD_CURVE3_RULES already exists.

GOTCHA: Java method-invocation argument lists FORBID a trailing comma. Only the
non-final table entries get a comma.
"""
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
SRC = ROOT / "src/main/java/com/minicad/step/semantic/StepCadCurveBuilder.java"
ORDER_TXT = ROOT / "src/test/resources/cad-curve3-dispatch-order.txt"

TABLE_FIELD = "BUILD_CURVE3_RULES"
METHOD_SIG = "    Curve3 buildCurve3Internal(StepEntity curve) {"
TERMINAL = '        return buildCurve3Callback.apply(curve.id());'
INSERT_BEFORE = METHOD_SIG

NEW_DISPATCH = [
    "        for (CadCurve3Rule rule : BUILD_CURVE3_RULES) {",
    "            if (rule.type().isInstance(curve)) {",
    "                return rule.handler().build(this, curve);",
    "            }",
    "        }",
]

TABLE_HEADER = [
    "    // buildCurve3Internal dispatch table (first-match-return, mirrors the original sequential ifs).",
    "    private record CadCurve3Rule(Class<? extends StepEntity> type, CadCurve3Handler handler) {}",
    "",
    "    private interface CadCurve3Handler {",
    "        Curve3 build(StepCadCurveBuilder self, StepEntity curve);",
    "    }",
    "",
    "    private static CadCurve3Rule cadCurve3Rule(",
    "            Class<? extends StepEntity> type, CadCurve3Handler handler) {",
    "        return new CadCurve3Rule(type, handler);",
    "    }",
    "",
    "    private static final List<CadCurve3Rule> " + TABLE_FIELD + " = List.of(",
]

HEADER_RE = re.compile(r"if \((.*)\)\s*\{$")


def selfify(line):
    """Prefix instance method/field references so a static lambda can reach them."""
    line = re.sub(r"\b(build\w+)\(", r"self.\1(", line)
    line = line.replace("geometryOps", "self.geometryOps")
    line = line.replace("buildCurve3Callback", "self.buildCurve3Callback")
    line = line.replace("entitiesById", "self.entitiesById")
    line = line.replace("reverseCompositeCurve", "self.reverseCompositeCurve")
    return line


def extract_branches(lines):
    """Return list of (type_name, condition, body_lines, is_guarded) for each branch."""
    branches = []
    i = 0
    n = len(lines)
    while i < n:
        line = lines[i]
        stripped = line.strip()
        if stripped.startswith("if (curve instanceof ") and stripped.endswith("{"):
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
    bi = next(i for i in range(mi + 1, ti) if lines[i].strip().startswith("if (curve instanceof "))

    branches = extract_branches(lines[bi:ti])
    if not branches:
        raise SystemExit("ABORT: no branches extracted")

    entries = []
    types = []
    for type_name, condition, body, is_guarded in branches:
        types.append(type_name)
        if is_guarded:
            raise SystemExit("ABORT: guarded branch in buildCurve3Internal -- add null-fallthrough support")
        transformed = "\n".join("            " + selfify(b.strip()) for b in body)
        entries.append(
            "        cadCurve3Rule(%s.class, (self, curve) -> {\n%s\n        })" % (type_name, transformed)
        )

    # 1) replace the branch interior with the for-loop; keep the terminal delegate return.
    lines[bi:ti] = NEW_DISPATCH + [""]

    # 2) insert the class-level table machinery before buildCurve3Internal.
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
