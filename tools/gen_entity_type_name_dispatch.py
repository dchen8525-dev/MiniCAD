#!/usr/bin/env python3
"""Fold StepEntityNamingUtils.stepEntityTypeName's 26-branch sequential-if chain
into a table-driven dispatch (ENTITY_TYPE_NAME_RULES).

The original chain (lines ~52..135) is a sequence of `if (entity instanceof X) { ... }`
branches, each returning -- first-match-return dispatch.

Three ways this chain differs from the previously folded ones, all handled here:

  1. THE TERMINAL IS A MULTI-STATEMENT TAIL, not a single `return`/`throw`:

         String simpleName = entity.getClass().getSimpleName();
         if (simpleName.startsWith("Step")) {
             simpleName = simpleName.substring(4);
         }
         return camelToUpperSnake(simpleName);

     So we must NOT slice up to `end - 1` (that would swallow the first three tail
     statements). Instead we replace exactly [first_header, last_branch_close) and
     leave everything after the final branch untouched.

  2. The handler takes ONLY `entity` (no builder/self): the method is `static` and
     the bodies reference nothing but the entity and static helpers.

  3. The file has no `java.util` import yet, so `import java.util.List;` is injected
     after the last existing import (this codebase puts java.* after com.minicad.*).

Also note the table machinery must be inserted BEFORE the method's javadoc, not
before the signature line -- otherwise the javadoc gets detached from its method.

Idempotent: aborts if ENTITY_TYPE_NAME_RULES already exists.

GOTCHA: Java method-invocation argument lists FORBID a trailing comma. Only the
non-final table entries get a comma.
"""
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
SRC = ROOT / "src/main/java/com/minicad/step/semantic/StepEntityNamingUtils.java"
ORDER_TXT = ROOT / "src/test/resources/entity-type-name-dispatch-order.txt"

TABLE_FIELD = "ENTITY_TYPE_NAME_RULES"
METHOD_SIG = "    static String stepEntityTypeName("
RULE_RECORD = "EntityTypeNameRule"
RULE_HANDLER = "EntityTypeNameHandler"
RULE_FACTORY = "entityTypeNameRule"
PARAMS = ("entity",)
NEEDED_IMPORT = "import java.util.List;"

NEW_DISPATCH = [
    "        for (" + RULE_RECORD + " rule : " + TABLE_FIELD + ") {",
    "            if (rule.type().isInstance(entity)) {",
    "                return rule.handler().name(entity);",
    "            }",
    "        }",
]

TABLE_HEADER = [
    "    // stepEntityTypeName dispatch table (first-match-return, mirrors the original",
    "    // sequential ifs). Branches that only return a constant literal and branches",
    "    // that delegate to the entity's own entityName() both live here unchanged.",
    "    private record " + RULE_RECORD + "(",
    "            Class<? extends StepEntity> type, " + RULE_HANDLER + " handler) {}",
    "",
    "    private interface " + RULE_HANDLER + " {",
    "        String name(StepEntity entity);",
    "    }",
    "",
    "    private static " + RULE_RECORD + " " + RULE_FACTORY + "(",
    "            Class<? extends StepEntity> type, " + RULE_HANDLER + " handler) {",
    "        return new " + RULE_RECORD + "(type, handler);",
    "    }",
    "",
    "    private static final List<" + RULE_RECORD + "> " + TABLE_FIELD + " = List.of(",
]

HEADER_RE = re.compile(r"if \((.*)\)\s*\{$")


def method_end(lines, mi):
    """Index of the method's closing brace (`    }`) after signature line mi."""
    return next(i for i in range(mi + 1, len(lines)) if lines[i] == "    }")


def javadoc_start(lines, mi):
    """Index where the method's leading javadoc begins, or mi if there is none.

    The table machinery must be inserted here -- inserting at `mi` would wedge the
    table between the javadoc and the method it documents.
    """
    j = mi - 1
    while j >= 0 and lines[j].strip() == "":
        j -= 1
    if j < 0 or lines[j].strip() != "*/":
        return mi
    while j >= 0 and lines[j].strip() != "/**":
        j -= 1
    return j if j >= 0 else mi


def extract_branches(lines, start, stop):
    """Extract consecutive `if (entity instanceof X) {...}` branches from [start, stop).

    Returns (branches, region_end) where branches is a list of
    (type_name, condition, body_lines, is_guarded) and region_end is the index just
    past the final branch's closing brace -- everything from region_end onwards is
    the method's fall-through tail and must be preserved verbatim.
    """
    branches = []
    region_end = start
    i = start
    while i < stop:
        stripped = lines[i].strip()
        if stripped.startswith("if (entity instanceof ") and stripped.endswith("{"):
            m = HEADER_RE.search(lines[i])
            condition = m.group(1)
            type_name = re.search(r"instanceof (\w+)", condition).group(1)
            is_guarded = "&&" in condition
            depth = 1
            body = []
            k = i + 1
            while k < stop:
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
            region_end = k + 1
            i = k + 1
            continue
        i += 1
    return branches, region_end


def main() -> None:
    text = SRC.read_text(encoding="utf-8")
    if TABLE_FIELD in text:
        raise SystemExit("ABORT: " + TABLE_FIELD + " already present; refactor applied?")
    lines = text.replace("\r\n", "\n").replace("\r", "\n").split("\n")

    mi = next(i for i, ln in enumerate(lines) if ln.startswith(METHOD_SIG))
    end = method_end(lines, mi)
    bi = next(
        i
        for i in range(mi + 1, end)
        if lines[i].strip().startswith("if (entity instanceof ")
    )

    branches, region_end = extract_branches(lines, bi, end)
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

    tail = [ln for ln in lines[region_end:end] if ln.strip()]
    if not tail:
        raise SystemExit("ABORT: no fall-through tail found; expected a fallback block")
    if not tail[-1].strip().startswith("return"):
        raise SystemExit("ABORT: tail does not end in a return: " + tail[-1].strip())

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

    # 1) replace ONLY the branch region; the fall-through tail after it stays put.
    lines[bi:region_end] = NEW_DISPATCH + [""]

    # 2) insert the class-level table machinery before the method's javadoc.
    # Java method-invocation arg lists forbid a trailing comma; only non-final get one.
    rendered = [e + "," if j < len(entries) - 1 else e for j, e in enumerate(entries)]
    mi = next(i for i, ln in enumerate(lines) if ln.startswith(METHOD_SIG))
    ii = javadoc_start(lines, mi)
    lines[ii:ii] = TABLE_HEADER + rendered + ["    );", ""]

    # 3) inject `import java.util.List;` after the last existing import.
    if NEEDED_IMPORT not in lines:
        last_import = max(i for i, ln in enumerate(lines) if ln.startswith("import "))
        lines[last_import + 1 : last_import + 1] = ["", NEEDED_IMPORT]

    SRC.write_text("\n".join(lines) + "\n", encoding="utf-8")

    # 4) write frozen type order (used by the dispatch-table guard test).
    ORDER_TXT.parent.mkdir(parents=True, exist_ok=True)
    ORDER_TXT.write_text("\n".join(types) + "\n", encoding="utf-8")

    print(
        "OK: wrote %s with %d rules; tail kept (%d lines); order -> %s"
        % (SRC, len(branches), len(tail), ORDER_TXT)
    )


if __name__ == "__main__":
    main()
