#!/usr/bin/env python3
"""Fold StepDumpApp.validateManagementAssignmentEntity's 30-branch sequential-if
chain into a table-driven dispatch (MANAGEMENT_ASSIGNMENT_RULES).

The original chain (lines ~2833..2977) is a sequence of
`if (entity instanceof X) { ... }` branches, each returning -- so it is
first-match-return dispatch with a terminal `return null;`.

The method is `private static`, so NO `self` parameter is needed: the lambdas are
plain (entity, builder) -> Integer and are stored in a static List, which is legal
because they capture nothing.

This script edits the source in place:
  1. replaces the branch interior with a for-loop dispatch (keeps `return null;`);
  2. inserts the record/interface/table/helper at class level, before the method;
  3. writes the frozen type order to
     src/test/resources/management-assignment-dispatch-order.txt.

Idempotent: aborts if MANAGEMENT_ASSIGNMENT_RULES already exists.

GOTCHA: Java method-invocation argument lists FORBID a trailing comma. Only the
non-final table entries get a comma.
"""
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
SRC = ROOT / "src/main/java/com/minicad/app/StepDumpApp.java"
ORDER_TXT = ROOT / "src/test/resources/management-assignment-dispatch-order.txt"

TABLE_FIELD = "MANAGEMENT_ASSIGNMENT_RULES"
METHOD_SIG = "    private static Integer validateManagementAssignmentEntity("
INSERT_BEFORE = METHOD_SIG
RULE_RECORD = "ManagementAssignmentRule"
RULE_HANDLER = "ManagementAssignmentHandler"
RULE_FACTORY = "managementAssignmentRule"
RESULT_TYPE = "Integer"
PARAMS = ("entity", "builder")

NEW_DISPATCH = [
    "        for (ManagementAssignmentRule rule : MANAGEMENT_ASSIGNMENT_RULES) {",
    "            if (rule.type().isInstance(entity)) {",
    "                return rule.handler().validate(entity, builder);",
    "            }",
    "        }",
]

TABLE_HEADER = [
    "    // validateManagementAssignmentEntity dispatch table (first-match-return,",
    "    // mirrors the original sequential ifs).",
    "    private record ManagementAssignmentRule(",
    "            Class<? extends StepEntity> type, ManagementAssignmentHandler handler) {}",
    "",
    "    private interface ManagementAssignmentHandler {",
    "        Integer validate(StepEntity entity, StepCadBuilder builder);",
    "    }",
    "",
    "    private static ManagementAssignmentRule managementAssignmentRule(",
    "            Class<? extends StepEntity> type, ManagementAssignmentHandler handler) {",
    "        return new ManagementAssignmentRule(type, handler);",
    "    }",
    "",
    "    private static final List<ManagementAssignmentRule> "
    + TABLE_FIELD
    + " = List.of(",
]

HEADER_RE = re.compile(r"if \((.*)\)\s*\{$")


def method_bounds(lines):
    """Return (method_idx, body_start, terminal_idx, end_idx) for METHOD_SIG.

    terminal_idx is the index of the terminal statement (the line just before the
    method's closing brace) -- here `return null;`. Locating it structurally (rather
    than by a marker substring) avoids matching the same text in a neighbouring method.
    """
    mi = next(i for i, ln in enumerate(lines) if ln.startswith(METHOD_SIG))
    end = next(i for i in range(mi + 1, len(lines)) if lines[i] == "    }")
    terminal = end - 1
    terminal_text = lines[terminal].strip()
    if not terminal_text.startswith("return"):
        raise SystemExit(
            "ABORT: expected a terminal return before the closing brace, found: "
            + terminal_text
        )
    return mi, mi + 1, terminal, end


def extract_branches(lines):
    """Return list of (type_name, condition, body_lines, is_guarded) for each branch."""
    branches = []
    i = 0
    n = len(lines)
    while i < n:
        line = lines[i]
        stripped = line.strip()
        if stripped.startswith("if (entity instanceof ") and stripped.endswith("{"):
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
    lines = text.replace("\r\n", "\n").replace("\r", "\n").split("\n")

    mi, body_start, terminal, end = method_bounds(lines)
    bi = next(
        i
        for i in range(body_start, terminal)
        if lines[i].strip().startswith("if (entity instanceof ")
    )

    branches = extract_branches(lines[bi:terminal])
    if not branches:
        raise SystemExit("ABORT: no branches extracted")
    guarded = [t for t, _c, _b, g in branches if g]
    if guarded:
        raise SystemExit("ABORT: guarded branch(es) present: " + ", ".join(guarded))

    seen = set()
    for t, _c, _b, _g in branches:
        if t in seen:
            raise SystemExit("ABORT: duplicate type in chain: " + t)
        seen.add(t)

    entries = []
    types = []
    for type_name, _condition, body, _g in branches:
        types.append(type_name)
        # static method -> bodies are copied verbatim; no selfify needed.
        transformed = "\n".join("            " + b.strip() for b in body)
        entries.append(
            "        %s(%s.class, (%s) -> {\n%s\n        })"
            % (RULE_FACTORY, type_name, ", ".join(PARAMS), transformed)
        )

    # 1) replace the branch interior with the for-loop; keep the terminal `return null;`.
    lines[bi:terminal] = NEW_DISPATCH + [""]

    # 2) insert the class-level table machinery before the method.
    # Java method-invocation arg lists forbid a trailing comma; only non-final get one.
    rendered = [e + "," if j < len(entries) - 1 else e for j, e in enumerate(entries)]
    ii = next(i for i, ln in enumerate(lines) if ln.startswith(INSERT_BEFORE))
    class_block = TABLE_HEADER + rendered + ["    );", ""]
    lines[ii:ii] = class_block

    SRC.write_text("\n".join(lines) + "\n", encoding="utf-8")

    # 3) write frozen type order (used by the dispatch-table guard test).
    ORDER_TXT.parent.mkdir(parents=True, exist_ok=True)
    ORDER_TXT.write_text("\n".join(types) + "\n", encoding="utf-8")

    print("OK: wrote", SRC, "with", len(branches), "rules; order ->", ORDER_TXT)


if __name__ == "__main__":
    main()
