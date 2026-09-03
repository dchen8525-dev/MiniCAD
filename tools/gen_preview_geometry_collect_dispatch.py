#!/usr/bin/env python3
"""Fold PreviewGeometryCollector.collectStandaloneEdges's 38-branch sequential-if chain
into a table-driven dispatch (PREVIEW_EDGE_COLLECT_RULES).

collectStandaloneEdges is a *static void* recursive collector (lines ~350..669), structurally
identical to the already-folded StepEdgePayloadBuilder.collectStandaloneEdges:
  - static void; recurses collectStandaloneEdges(...) and mutates the edges map;
  - each branch does work then `return;`;
  - the chain ends with a method-call terminal `if (PreviewFaceBuilder.isSampledCurveSource(item))`
    (a catch-all, NOT an instanceof branch) which stays after the loop.

SPECIAL SHAPES (same as the StepEdgePayloadBuilder fold):
  - OR-compound branch `StepVertexShell || StepVertexLoop` split into TWO rules.
  - Dead-code duplicate types: StepAnnotationCurveOccurrence, StepFilletEdge, StepChamferEdge
    each appear twice with DIFFERENT bodies; the 2nd occurrence is unreachable (1st already
    returned) -> kept only first/reachable occurrence (dedup).
  - Terminal isSampledCurveSource block preserved verbatim after the loop.

STATIC CONTEXT: the method + every helper it calls are static, so the lambdas need NO `self`
parameter; handlers carry the method's 5 params (item, edges, resolved, builder, metadata).

Difference from gen_edge_collect_dispatch.py:
  - method is `public static void` here (multiline sig) -> match via substring, not startswith;
  - distinct rule/record/handler names (PreviewEdgeCollect*) to avoid any cross-class confusion.

Edits the source in place (idempotent: aborts if PREVIEW_EDGE_COLLECT_RULES already exists).
"""
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
SRC = ROOT / "src/main/java/com/minicad/preview/builder/PreviewGeometryCollector.java"
ORDER_TXT = ROOT / "src/test/resources/preview-geometry-collect-dispatch-order.txt"

TABLE_FIELD = "PREVIEW_EDGE_COLLECT_RULES"
METHOD_SIG_SUBSTR = "static void collectStandaloneEdges("
TERMINAL_MARKER = "isSampledCurveSource(item)"

NEW_DISPATCH = [
    "        for (PreviewEdgeCollectRule rule : PREVIEW_EDGE_COLLECT_RULES) {",
    "            if (rule.type().isInstance(item)) {",
    "                rule.handler().collect(item, edges, resolved, builder, metadata);",
    "                return;",
    "            }",
    "        }",
]

TABLE_HEADER = [
    "    // collectStandaloneEdges dispatch table (first-match-return, mirrors the original sequential ifs).",
    "    private record PreviewEdgeCollectRule(Class<? extends StepEntity> type, PreviewEdgeCollectHandler handler) {}",
    "",
    "    private interface PreviewEdgeCollectHandler {",
    "        void collect(StepEntity item, Map<Integer, EdgePayload> edges,",
    "                Map<Integer, StepEntity> resolved, StepCadBuilder builder,",
    "                StepMetadataExtractor metadata);",
    "    }",
    "",
    "    private static PreviewEdgeCollectRule previewEdgeCollectRule(",
    "            Class<? extends StepEntity> type, PreviewEdgeCollectHandler handler) {",
    "        return new PreviewEdgeCollectRule(type, handler);",
    "    }",
    "",
    "    private static final List<PreviewEdgeCollectRule> " + TABLE_FIELD + " = List.of(",
]

HEADER_RE = re.compile(r"if \((.*)\)\s*\{$")
TYPE_RE = re.compile(r"instanceof\s+([\w.]+)")


def split_types(condition):
    return TYPE_RE.findall(condition)


def simple(name):
    return name.split(".")[-1]


def extract_branches(lines):
    """Return list of (type_list, body_lines, is_guarded) for each branch.

    type_list may contain >1 entry for an OR-compound branch.
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
            type_list = split_types(condition)
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
            branches.append((type_list, body, is_guarded))
            i = k + 1
            continue
        i += 1
    return branches


def main() -> None:
    text = SRC.read_text(encoding="utf-8")
    if TABLE_FIELD in text:
        raise SystemExit("ABORT: " + TABLE_FIELD + " already present; refactor applied?")
    lines = text.split("\n")

    mi = next(i for i, ln in enumerate(lines) if METHOD_SIG_SUBSTR in ln)
    open_i = next(i for i in range(mi, len(lines)) if "{" in lines[i])
    bi = next(i for i in range(open_i + 1, len(lines)) if lines[i].strip().startswith("if (item instanceof "))
    ti = next(i for i in range(open_i + 1, len(lines)) if TERMINAL_MARKER in lines[i])

    branches = extract_branches(lines[bi:ti])
    if not branches:
        raise SystemExit("ABORT: no branches extracted")

    entries = []
    order = []
    seen = set()
    for type_list, body, is_guarded in branches:
        if is_guarded:
            raise SystemExit("ABORT: guarded (&&) branch in collectStandaloneEdges -- add null-fallthrough support")
        transformed = "\n".join("            " + b.strip() for b in body)
        for type_name in type_list:
            s = simple(type_name)
            if s in seen:
                # Original sequential-if chain lists this type twice (e.g.
                # StepFilletEdge at two points with DIFFERENT bodies). The second
                # occurrence is dead code -- the first already returned. Keep only
                # the first/reachable occurrence; dropping the rest is a
                # behavior-preserving cleanup the table makes obvious.
                continue
            seen.add(s)
            entries.append(
                "        previewEdgeCollectRule(%s.class, (item, edges, resolved, builder, metadata) -> {\n%s\n        })" % (type_name, transformed)
            )
            order.append(s)

    # 1) replace the branch interior with the for-loop; keep the terminal predicate block.
    lines[bi:ti] = NEW_DISPATCH + [""]

    # 2) insert the class-level table machinery before collectStandaloneEdges.
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
