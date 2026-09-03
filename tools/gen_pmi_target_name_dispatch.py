#!/usr/bin/env python3
"""Fold PmiTargetHelper.pmiTargetName's 44-branch sequential-if chain into a
table-driven dispatch (PMI_TARGET_NAME_RULES).

The original chain (lines ~150..324) is a sequence of
`if (target instanceof X) { return ...; }` branches, each returning -- so it is
first-match-return dispatch. Every branch is a *computed* handler: it casts the
target and returns `target.name()` (or `StepMetadataHelper.faceDisplayName(face)`
for StepFaceEntity). There are no constant-name branches and no guarded branches,
so the table is uniform: 44 (type, handler) rules plus a first-match-return loop.

This script edits the source in place:
  1. replaces the 44-branch interior of pmiTargetName with a for-loop dispatch
     (keeps the terminal `return "";`);
  2. inserts the record/interface/table/helper at class level, before pmiTargetName;
  3. writes the frozen primary-type order to src/test/resources/...-dispatch-order.txt.

Idempotent: aborts if PMI_TARGET_NAME_RULES already exists.

GOTCHA: Java method-invocation argument lists FORBID a trailing comma (unlike
array/enum initializers). Only the non-final table entries get a comma.
"""
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
SRC = ROOT / "src/main/java/com/minicad/preview/builder/PmiTargetHelper.java"
ORDER_TXT = ROOT / "src/test/resources/pmi-target-name-dispatch-order.txt"

TABLE_FIELD = "PMI_TARGET_NAME_RULES"
METHOD_SIG = "    public static String pmiTargetName(StepEntity target) {"
TERMINAL = 'return "";'
INSERT_BEFORE = "    public static String pmiTargetName(StepEntity target) {"

NEW_DISPATCH = [
    "        for (PmiTargetNameRule rule : PMI_TARGET_NAME_RULES) {",
    "            if (rule.type().isInstance(target)) {",
    "                return rule.handler().name(target);",
    "            }",
    "        }",
]

TABLE_HEADER = [
    "    // pmiTargetName dispatch table (first-match-return, mirrors the original sequential ifs).",
    "    private record PmiTargetNameRule(Class<? extends StepEntity> type, PmiTargetNameHandler handler) {}",
    "",
    "    private interface PmiTargetNameHandler {",
    "        String name(StepEntity target);",
    "    }",
    "",
    "    private static PmiTargetNameRule pmiTargetNameRule(",
    "            Class<? extends StepEntity> type, PmiTargetNameHandler handler) {",
    "        return new PmiTargetNameRule(type, handler);",
    "    }",
    "",
    "    private static final List<PmiTargetNameRule> PMI_TARGET_NAME_RULES = List.of(",
]

HEADER_RE = re.compile(r"if \((.*)\)\s*\{$")


def extract_branches(lines):
    """Return list of (type_name, condition, body_lines, is_guarded) for each branch."""
    branches = []
    i = 0
    n = len(lines)
    while i < n:
        line = lines[i]
        stripped = line.strip()
        if stripped.startswith("if (target instanceof ") and stripped.endswith("{"):
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
    ti = next(i for i in range(mi, len(lines)) if lines[i].strip() == TERMINAL)
    bi = next(i for i in range(mi + 1, ti) if lines[i].strip().startswith("if (target instanceof "))

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
            name = re.search(r'return "([^"]*)";', joined).group(1)
            entries.append(
                "        pmiTargetNameRule(%s.class, (target) -> {\n"
                "            if (%s) {\n"
                '                return "%s";\n'
                "            }\n"
                "            return null;\n"
                "        })" % (type_name, condition, name)
            )
        elif m:
            entries.append(
                '        pmiTargetNameRule(%s.class, (target) -> "%s")' % (type_name, m.group(1))
            )
        else:
            body_indent = "\n".join("            " + b.strip() for b in body)
            entries.append(
                "        pmiTargetNameRule(%s.class, (target) -> {\n%s\n        })" % (type_name, body_indent)
            )

    # 1) replace the branch interior with the for-loop; keep the terminal return "".
    lines[bi:ti] = NEW_DISPATCH + [""]

    # 2) insert the class-level table machinery before pmiTargetName.
    # Java method-invocation arg lists forbid a trailing comma; only non-final get one.
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
