#!/usr/bin/env python3
"""Fold StepSolidBuilder.buildSolid's ~35-branch sequential `if (entity instanceof ...)` chain
into a table-driven dispatch (SOLID_BUILDER_RULES).

buildSolid is an *instance method* returning `Solid`. It is the first chain that is NOT a pure
first-match-return dispatch:

  - It dispatches on `entity` (not `item`), an instance field `builder` drives every branch, and
    two branches (`StepContextDependentShapeRepresentation`, `StepItemDefinedTransformation`) are
    *conditional-return fall-through*: they `return` only inside a nested `if`, otherwise fall
    through to the next branch / terminal `throw`.
  - Therefore the table uses the §5 NULL-FALLTHROUGH loop: each handler returns a (nullable)
    `Solid`; the loop adopts the first non-null result and continues on `null`. This mirrors the
    original exactly (a fall-through branch that does not return simply lets the loop try the next
    rule). The terminal `throw new StepResolutionException("entity #" + id + " ...")` stays after
    the loop.
  - Because it is an instance method, the handler carries `self` (StepSolidBuilder) plus the
    dispatch variable `entity` and the method param `id` (referenced by the StepSolidModel branch's
    throw message). Bodies are selfified: `builder.` -> `self.builder.`, `canBuildAsSolid(` ->
    `self.canBuildAsSolid(`.
  - Fall-through branches get a trailing `return null;` appended so the handler returns on all
    paths (the verifier strips it before comparing). Always-exit branches (ending in `return`/`throw`)
    need no such appendage.

Depth-aware branch extraction: a prior chain-scan fragmented at *nested* `if (... instanceof ...)`
bodies (e.g. StepFlatPattern's inner `if (flatPattern.flatGeometry() instanceof StepFaceEntity)`),
so this generator walks brace depth and collects only the top-level sibling headers, extracting
each branch body by brace balance (nested ifs stay inside the body).

Edits the source in place (idempotent: aborts if SOLID_BUILDER_RULES already present).
"""
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
SRC = ROOT / "src/main/java/com/minicad/step/semantic/StepSolidBuilder.java"
ORDER_TXT = ROOT / "src/test/resources/solid-builder-dispatch-order.txt"

TABLE_FIELD = "SOLID_BUILDER_RULES"
METHOD_SIG_SUBSTR = "Solid buildSolid("
TERMINAL_MARKER = "is not a supported SOLID"

HEADER_RE = re.compile(r"if \((.*)\)\s*\{$")
TYPE_RE = re.compile(r"instanceof\s+([\w.]+)")


def count_braces(line):
    depth = 0
    in_str = in_ch = False
    for ch in line:
        if in_str:
            if ch == '"':
                in_str = False
            continue
        if in_ch:
            if ch == "'":
                in_ch = False
            continue
        if ch == '"':
            in_str = True
        elif ch == "'":
            in_ch = True
        elif ch == '{':
            depth += 1
        elif ch == '}':
            depth -= 1
    return depth


def selfify(line):
    line = re.sub(r"\bbuilder\.", "self.builder.", line)
    line = re.sub(r"\bcanBuildAsSolid\(", "self.canBuildAsSolid(", line)
    return line


def simple(name):
    return name.split(".")[-1]


def split_types(condition):
    return TYPE_RE.findall(condition)


def extract_branches(lines):
    """Depth-aware extraction of top-level sibling `if (... instanceof ...) {` headers.

    Returns list of (type_list, body_lines, is_guarded). Nested instanceof ifs inside a branch
    body are NOT collected as separate branches; they stay inside the enclosing branch body.
    """
    n = len(lines)
    # Determine base_depth: brace depth at the first top-level `if (... instanceof)` header.
    depth = 0
    base_depth = None
    for i in range(n):
        stripped = lines[i].strip()
        m = HEADER_RE.search(stripped)
        if base_depth is None and stripped.startswith("if (") and m and "instanceof" in m.group(1) and stripped.endswith("{"):
            base_depth = depth
            break
        depth += count_braces(lines[i])
    if base_depth is None:
        return []

    branches = []
    depth = 0
    i = 0
    while i < n:
        stripped = lines[i].strip()
        m = HEADER_RE.search(stripped)
        is_header = (depth == base_depth and stripped.startswith("if (") and m
                     and "instanceof" in m.group(1) and stripped.endswith("{"))
        if is_header:
            condition = m.group(1)
            type_list = split_types(condition)
            is_guarded = "&&" in condition
            d = depth + 1
            body = []
            k = i + 1
            while k < n:
                for ch in lines[k]:
                    if ch == '{':
                        d += 1
                    elif ch == '}':
                        d -= 1
                if d == base_depth:
                    break
                body.append(lines[k])
                k += 1
            while body and body[0].strip() == "":
                body.pop(0)
            while body and body[-1].strip() == "":
                body.pop()
            branches.append((type_list, body, is_guarded))
            i = k + 1
            continue
        depth += count_braces(lines[i])
        i += 1
    return branches


def last_statement_exits(body):
    """A branch always exits if its last TOP-LEVEL (brace-depth 0) statement is return/throw.

    Multi-line return/throw (the closing `);` is the last *line*) and nested ifs inside the
    branch body must not confuse this -- only statements at brace-depth 0 count. A top-level
    statement finalizes at a `;` at depth 0 OR at a `}` that returns to depth 0 (a closed block).
    """
    text = " ".join(b.strip() for b in body)
    depth = 0
    last_stmt = ""
    cur = ""
    for ch in text:
        if ch == '{':
            depth += 1
            cur += ch
        elif ch == '}':
            depth -= 1
            cur += ch
            if depth == 0:
                last_stmt = cur.strip()
                cur = ""
        elif ch == ';' and depth == 0:
            last_stmt = cur.strip()
            cur = ""
        else:
            cur += ch
    if cur.strip():
        last_stmt = cur.strip()
    last_stmt = last_stmt.lstrip('}').strip()
    return last_stmt.startswith("return") or last_stmt.startswith("throw")


def is_fallthrough(body):
    return not last_statement_exits(body)


def main() -> None:
    text = SRC.read_text(encoding="utf-8")
    if TABLE_FIELD in text:
        raise SystemExit("ABORT: " + TABLE_FIELD + " already present; refactor applied?")
    lines = text.split("\n")

    mi = next(i for i, ln in enumerate(lines) if METHOD_SIG_SUBSTR in ln)
    open_i = next(i for i in range(mi, len(lines)) if "{" in lines[i])
    bi = next(i for i in range(open_i + 1, len(lines))
              if lines[i].strip().startswith("if (") and "instanceof" in lines[i]
              and lines[i].strip().endswith("{"))
    ti = next(i for i in range(open_i + 1, len(lines)) if TERMINAL_MARKER in lines[i])

    branches = extract_branches(lines[bi:ti])
    if not branches:
        raise SystemExit("ABORT: no branches extracted")

    entries = []
    order = []
    seen = set()
    for type_list, body, is_guarded in branches:
        if is_guarded:
            raise SystemExit("ABORT: guarded (&&) branch in buildSolid -- add predicate support")
        transformed = "\n".join("            " + selfify(b.strip()) for b in body)
        if is_fallthrough(body):
            # Conditional-return branch: append `return null;` so the handler returns on the
            # fall-through path (loop continues to the next rule). Stripped by the verifier.
            transformed += "\n            return null;"
        for type_name in type_list:
            s = simple(type_name)
            if s in seen:
                raise SystemExit("ABORT: duplicate type " + s + " -- dedup not expected for buildSolid")
            seen.add(s)
            entries.append(
                "        solidBuilderRule(%s.class, (self, entity, id) -> {\n%s\n        })" % (type_name, transformed)
            )
            order.append(s)

    NEW_DISPATCH = [
        "        for (SolidBuilderRule rule : SOLID_BUILDER_RULES) {",
        "            if (!rule.type().isInstance(entity)) {",
        "                continue;",
        "            }",
        "            Solid solid = rule.handler().build(this, entity, id);",
        "            if (solid != null) {",
        "                return solid;",
        "            }",
        "        }",
    ]

    TABLE_HEADER = [
        "    // buildSolid dispatch table (first-match; null-fallthrough for conditional-return branches).",
        "    private record SolidBuilderRule(Class<? extends StepEntity> type, SolidBuilderHandler handler) {}",
        "",
        "    private interface SolidBuilderHandler {",
        "        Solid build(StepSolidBuilder self, StepEntity entity, int id);",
        "    }",
        "",
        "    private static SolidBuilderRule solidBuilderRule(Class<? extends StepEntity> type, SolidBuilderHandler handler) {",
        "        return new SolidBuilderRule(type, handler);",
        "    }",
        "",
        "    private static final List<SolidBuilderRule> " + TABLE_FIELD + " = List.of(",
    ]

    # 1) replace the branch interior with the null-fallthrough loop; keep the terminal throw.
    lines[bi:ti] = NEW_DISPATCH + [""]

    # 2) insert the class-level table machinery before buildSolid.
    # Java method-invocation arg lists forbid a trailing comma; only non-final get one.
    rendered = [e + "," if j < len(entries) - 1 else e for j, e in enumerate(entries)]
    ii = next(i for i, ln in enumerate(lines) if METHOD_SIG_SUBSTR in ln)
    class_block = TABLE_HEADER + rendered + ["    );", ""]
    lines[ii:ii] = class_block

    SRC.write_text("\n".join(lines) + "\n", encoding="utf-8")

    # 3) write frozen primary-type order (used by the dispatch-table guard test).
    ORDER_TXT.parent.mkdir(parents=True, exist_ok=True)
    ORDER_TXT.write_text("\n".join(order) + "\n", encoding="utf-8")

    print("OK: wrote", SRC, "with", len(entries), "rules (from", len(branches), "branches); order ->", ORDER_TXT)


if __name__ == "__main__":
    main()
