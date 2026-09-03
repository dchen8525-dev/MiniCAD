#!/usr/bin/env python3
"""Fold StepCurveTypeNameResolver.previewCurveTypeName's 60-branch sequential-if
chain into a table-driven dispatch (PREVIEW_CURVE_TYPE_NAME_RULES).

The original chain (lines ~25..209) is a sequence of
`if (item instanceof X [&& EXTRA]) { return ...; }` branches, each returning --
so it is first-match-return dispatch. Most branches return a constant STEP-LIKE
name string; two return a computed value (entityName() of the cast entity); and
ONE is guarded by an extra predicate:
    if (item instanceof StepGeometricReplica
            && "CURVE_REPLICA".equals(((StepGeometricReplica) item).entityName())) {
        return "CURVE_REPLICA";
    }
That guarded branch must FALL THROUGH (return null) when its predicate fails, so
the dispatch loop uses null-fallthrough: a handler returning non-null is adopted,
null continues to the next rule. This mirrors buildPreviewFaceResult's semantics.

This script edits the source in place:
  1. replaces the 60-branch interior of previewCurveTypeName with a null-fallthrough
     for-loop dispatch (keeps the terminal `return null;`);
  2. inserts the record/interface/table/helper at class level, before
     previewCurveBasisTypeName;
  3. writes the frozen primary-type order to src/test/resources/...-dispatch-order.txt.

Idempotent: aborts if PREVIEW_CURVE_TYPE_NAME_RULES already exists.

GOTCHA: Java method-invocation argument lists FORBID a trailing comma (unlike
array/enum initializers). Only the non-final table entries get a comma.
"""
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
SRC = ROOT / "src/main/java/com/minicad/export/json/StepCurveTypeNameResolver.java"
ORDER_TXT = ROOT / "src/test/resources/curve-type-name-dispatch-order.txt"

TABLE_FIELD = "PREVIEW_CURVE_TYPE_NAME_RULES"
METHOD_SIG = "    public static String previewCurveTypeName(StepEntity item) {"
TERMINAL = "return null;"
INSERT_BEFORE = "    public static String previewCurveBasisTypeName(StepEntity item) {"

NEW_DISPATCH = [
    "        for (CurveTypeNameRule rule : PREVIEW_CURVE_TYPE_NAME_RULES) {",
    "            if (rule.type().isInstance(item)) {",
    "                String name = rule.handler().name(item);",
    "                if (name != null) {",
    "                    return name;",
    "                }",
    "            }",
    "        }",
]

TABLE_HEADER = [
    "    // previewCurveTypeName dispatch table (first-match-return; the guarded",
    "    // StepGeometricReplica branch returns null to fall through). Mirrors the",
    "    // original sequential ifs.",
    "    private record CurveTypeNameRule(Class<? extends StepEntity> type, CurveTypeNameHandler handler) {}",
    "",
    "    private interface CurveTypeNameHandler {",
    "        String name(StepEntity item);",
    "    }",
    "",
    "    private static CurveTypeNameRule curveTypeNameRule(",
    "            Class<? extends StepEntity> type, CurveTypeNameHandler handler) {",
    "        return new CurveTypeNameRule(type, handler);",
    "    }",
    "",
    "    private static final List<CurveTypeNameRule> PREVIEW_CURVE_TYPE_NAME_RULES = List.of(",
]

HEADER_RE = re.compile(r"if \((.*)\)\s*\{$")


def extract_branches(lines):
    """Return list of (type_name, condition, body_lines, is_guarded) for each branch.

    A branch is an `if (item instanceof X [&& EXTRA]) { ... }` whose body returns.
    Brace matching is character-level (NOT per-line), so `} catch (...) {` style
    same-line braces do not throw the depth off.
    """
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
            depth = 1  # the `{` on the header line
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
    ti = next(i for i in range(mi, len(lines)) if lines[i].strip() == TERMINAL)
    bi = next(i for i in range(mi + 1, ti) if lines[i].strip().startswith("if (item instanceof "))

    branches = extract_branches(lines[bi:ti])
    if not branches:
        raise SystemExit("ABORT: no branches extracted")

    entries = []
    types = []
    for type_name, condition, body, is_guarded in branches:
        types.append(type_name)
        joined = " ".join(b.strip() for b in body)
        m = re.match(r'^return "([^"]*)";$', joined)
        if is_guarded:
            # Keep the original guard verbatim; fall through with null on mismatch.
            name = re.search(r'return "([^"]*)";', joined).group(1)
            entries.append(
                "        curveTypeNameRule(%s.class, (item) -> {\n"
                "            if (%s) {\n"
                '                return "%s";\n'
                "            }\n"
                "            return null;\n"
                "        })" % (type_name, condition, name)
            )
        elif m:
            entries.append(
                '        curveTypeNameRule(%s.class, (item) -> "%s")' % (type_name, m.group(1))
            )
        else:
            body_indent = "\n".join("            " + b.strip() for b in body)
            entries.append(
                "        curveTypeNameRule(%s.class, (item) -> {\n%s\n        })" % (type_name, body_indent)
            )

    # 1) replace the 60-branch interior with the null-fallthrough loop; keep terminal.
    lines[bi:ti] = NEW_DISPATCH + [""]

    # 2) insert the class-level table machinery before previewCurveBasisTypeName.
    # Java method-invocation arg lists forbid a trailing comma, so only non-final
    # entries get a comma.
    rendered = [e + "," if j < len(entries) - 1 else e for j, e in enumerate(entries)]
    ii = next(i for i, ln in enumerate(lines) if ln == INSERT_BEFORE)
    class_block = TABLE_HEADER + rendered + ["    );", ""]
    lines[ii:ii] = class_block

    # 3) ensure java.util.List is imported.
    if not any(ln.strip() == "import java.util.List;" for ln in lines):
        imp_i = next(i for i, ln in enumerate(lines) if ln.startswith("import "))
        lines.insert(imp_i, "import java.util.List;")

    SRC.write_text("\n".join(lines) + "\n", encoding="utf-8")

    # 4) write frozen primary-type order (used by the dispatch-table guard test).
    ORDER_TXT.parent.mkdir(parents=True, exist_ok=True)
    ORDER_TXT.write_text("\n".join(types) + "\n", encoding="utf-8")

    print("OK: wrote", SRC, "with", len(branches), "rules; order ->", ORDER_TXT)


if __name__ == "__main__":
    main()
