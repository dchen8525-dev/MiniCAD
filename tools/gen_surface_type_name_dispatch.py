#!/usr/bin/env python3
"""Fold StepTypeNameResolver.surfaceTypeName's 92-branch sequential-if chain into a
first-match table (SURFACE_TYPE_NAME_RULES).

The original chain (lines ~27..308) is a sequence of `if (geometry instanceof X) { return ...; }`
branches, each returning -- so it is first-match-return dispatch (NOT null-fallthrough).
It terminates with `return geometry.getClass().getSimpleName();` for unmatched types.

Most branches return a constant STEP-LIKE name string; five return a computed value
(entityName() of the cast entity, or a conditional on StepFaceBound.outer()). Those become
dynamic handlers; the rest become constant handlers.

This script edits the source in place:
  1. replaces the 92-branch interior of surfaceTypeName with a for-loop dispatch;
  2. inserts the record/interface/table/helper at class level, before geometryTypeName.

Idempotent: aborts if SURFACE_TYPE_NAME_RULES already exists.
"""
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
SRC = ROOT / "src/main/java/com/minicad/export/json/StepTypeNameResolver.java"

TABLE_FIELD = "SURFACE_TYPE_NAME_RULES"
METHOD_SIG = "    public static String surfaceTypeName(StepEntity geometry) {"
TERMINAL = "return geometry.getClass().getSimpleName();"
INSERT_BEFORE = "    public static String geometryTypeName(StepEntity entity) {"

NEW_DISPATCH = [
    "        for (SurfaceTypeNameRule rule : SURFACE_TYPE_NAME_RULES) {",
    "            if (rule.type().isInstance(geometry)) {",
    "                return rule.handler().name(geometry);",
    "            }",
    "        }",
]

TABLE_HEADER = [
    "    // surfaceTypeName dispatch table (first-match-return, mirrors the original sequential ifs).",
    "    private record SurfaceTypeNameRule(Class<? extends StepEntity> type, SurfaceTypeNameHandler handler) {}",
    "",
    "    private interface SurfaceTypeNameHandler {",
    "        String name(StepEntity geometry);",
    "    }",
    "",
    "    private static SurfaceTypeNameRule surfaceTypeNameRule(",
    "            Class<? extends StepEntity> type, SurfaceTypeNameHandler handler) {",
    "        return new SurfaceTypeNameRule(type, handler);",
    "    }",
    "",
    "    private static final List<SurfaceTypeNameRule> SURFACE_TYPE_NAME_RULES = List.of(",
]


def extract_branches(lines):
    """Return list of (type_name, body_lines) for each `if (geometry instanceof X) { ... }`."""
    branches = []
    i = 0
    n = len(lines)
    while i < n:
        line = lines[i]
        if line.strip().startswith("if (geometry instanceof ") and line.rstrip().endswith("{"):
            m = re.search(r"if \(geometry instanceof (\w+)\)\s*\{", line)
            type_name = m.group(1)
            depth = 1  # the `{` on this header line
            body = []
            k = i + 1
            while k < n:
                for ch in lines[k]:
                    if ch == '{':
                        depth += 1
                    elif ch == '}':
                        depth -= 1
                if depth == 0:
                    break
                body.append(lines[k])
                k += 1
            while body and body[0].strip() == "":
                body.pop(0)
            while body and body[-1].strip() == "":
                body.pop()
            branches.append((type_name, body))
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
    bi = next(i for i in range(mi + 1, ti) if lines[i].strip().startswith("if (geometry instanceof "))

    branches = extract_branches(lines[bi:ti])
    if not branches:
        raise SystemExit("ABORT: no branches extracted")

    entries = []
    for type_name, body in branches:
        joined = " ".join(b.strip() for b in body)
        m = re.match(r'^return "([^"]*)";$', joined)
        if m:
            entries.append(
                '        surfaceTypeNameRule(%s.class, (geometry) -> "%s")' % (type_name, m.group(1))
            )
        else:
            body_indent = "\n".join("            " + b.strip() for b in body)
            entries.append(
                "        surfaceTypeNameRule(%s.class, (geometry) -> {\n%s\n        })" % (type_name, body_indent)
            )

    # 1) replace the 92-branch interior with the for-loop; keep the terminal return line.
    lines[bi:ti] = NEW_DISPATCH + [""]

    # 2) insert the class-level table machinery before geometryTypeName.
    # NOTE: Java method-invocation argument lists FORBID a trailing comma (unlike
    # array/enum initializers). A trailing comma here makes javac emit
    # "illegal start of expression" at the closing ');', so only non-final entries
    # get a comma.
    rendered = [e + "," if j < len(entries) - 1 else e for j, e in enumerate(entries)]
    ii = next(i for i, ln in enumerate(lines) if ln == INSERT_BEFORE)
    class_block = TABLE_HEADER + rendered + ["    );", ""]
    lines[ii:ii] = class_block

    SRC.write_text("\n".join(lines) + "\n", encoding="utf-8")
    print("OK: wrote", SRC, "with", len(branches), "rules")


if __name__ == "__main__":
    main()
