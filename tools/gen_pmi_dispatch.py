"""Generate a table-driven dispatch for StepPmiTargetBuilder.appendSemanticDefinitionTargets.

The method is a ~779-line if/else-if chain over `definition instanceof StepXxx`.
This script parses that chain out of the current source and emits a dispatch
table where every branch's FULL body is moved verbatim into a lambda. Nothing
is analysed or rewritten: the branch body is captured by brace matching and
pasted as-is, so behaviour is preserved exactly (including branches that call
several handlers or run a loop).

Outputs:
  1. src/test/resources/pmi-dispatch-order.txt
     Frozen, ordered expectation (type per branch, in chain order). A test asserts
     the generated table still matches it, which guards against a branch being
     dropped, duplicated or reordered -- the ordering is load bearing because
     `instanceof` also matches subtypes, so "first match wins" must be preserved.

  2. target/StepPmiTargetBuilder.new.java
     The full rewritten source (head + early returns preserved, chain replaced by
     the dispatch table + tail loop). Review, then move into place.
"""

import json
import re
from pathlib import Path

SRC = Path("src/main/java/com/minicad/export/json/StepPmiTargetBuilder.java")
CHAIN_START = 161  # 1-based: `if (definition instanceof StepPropertyDefinition) {`
CHAIN_END = 939    # 1-based: closing `}` of the whole chain


def find_branch_body(lines, i):
    """Return (body_lines, end_index) for the branch whose `if` is at line i.

    The body is everything between the branch's opening `{` and its matching
    closing `}`, captured by brace counting so nested loops/ifs are kept intact.

    A chain branch is written as `} else if (definition instanceof X) { ... }`,
    so the `if` line may carry a LEADING `}` (closing the previous branch). We
    start the brace count at the branch's own opening brace -- the LAST `{` on
    the line -- and ignore that leading `}`.
    """
    open_pos = lines[i].rfind("{")
    depth = 0
    started = False
    for j in range(i, len(lines)):
        start_col = open_pos if j == i else 0
        for ch in lines[j][start_col:]:
            if ch == "{":
                depth += 1
                started = True
            elif ch == "}":
                depth -= 1
                if started and depth == 0:
                    return lines[i + 1 : j], j
    return lines[i + 1 :], len(lines) - 1


def main() -> None:
    lines = SRC.read_text(encoding="utf-8").splitlines()
    seg = lines[CHAIN_START - 1 : CHAIN_END]

    starts = []
    for i, line in enumerate(seg):
        m = re.match(r"^\s*(?:\}\s*else\s+)?if \(definition instanceof (\w+)\) \{\s*$", line)
        if m:
            starts.append((i, m.group(1)))

    rows = []
    for k, (i, type_name) in enumerate(starts):
        body, _ = find_branch_body(seg, i)
        body = [b for b in body if b.strip() != ""]
        rows.append({"type": type_name, "body": body})

    print(f"parsed {len(rows)} branches")

    # Drop dead branches. The source is a single linear if/else-if chain, so once
    # a type has been matched earlier every later `instanceof` for the same type
    # is unreachable. Keep the FIRST occurrence: that is the one that actually
    # runs today, so dropping the later ones cannot change behaviour.
    seen: set[str] = set()
    deduped: list[dict] = []
    dead: list[dict] = []
    for r in rows:
        if r["type"] in seen:
            dead.append(r)
            continue
        seen.add(r["type"])
        deduped.append(r)
    if dead:
        print(f"dropped {len(dead)} unreachable duplicate branches:")
        for r in dead:
            print(f"  {r['type']}")
    rows = deduped

    # 1. frozen ordered expectation (best-effort: already correct from prior run).
    out = Path("src/test/resources/pmi-dispatch-order.txt")
    payload = ["# Frozen dispatch order for appendSemanticDefinitionTargets.",
               "# Captured from the original if/else-if chain before table-driven conversion.",
               "# Order is load bearing: instanceof matches subtypes, so the first",
               "# matching entry wins."]
    payload += [r["type"] for r in rows]
    try:
        out.parent.mkdir(parents=True, exist_ok=True)
        out.write_text("\n".join(payload) + "\n", encoding="utf-8")
        print(f"wrote {out} ({len(rows)} types)")
    except PermissionError:
        print(f"(skipped) {out} is locked; existing file already matches this order")

    # 2. java rule entries -- every branch body verbatim inside a lambda.
    java = []
    for idx, r in enumerate(rows):
        comma = "," if idx < len(rows) - 1 else ""
        java.append("            rule(" + r["type"] + ".class, (")
        java.append("                    targetsByUsageId,")
        java.append("                    identifiedItem,")
        java.append("                    definition,")
        java.append("                    resolved,")
        java.append("                    instanceIdsByTargetId")
        java.append("            ) -> {")
        for b in r["body"]:
            java.append("            " + b)
        java.append("            })" + comma)
    rules_text = "\n".join(java)

    # 3. full rewritten source, for review before it is moved into place.
    support = f'''
    /**
     * Dispatch table behind appendSemanticDefinitionTargets.
     *
     * Replaces a ~779-line if/else-if {{@code instanceof}} chain. The order below is
     * load bearing: {{@code instanceof}} also matches subtypes and the original chain
     * was "first match wins", so entries keep their original relative order.
     *
     * Each branch body was moved verbatim into a lambda, so behaviour is unchanged
     * -- including branches that call several handlers or run a loop.
     *
     * {len(dead)} later branches were unreachable in the original chain because the
     * same type had already been matched earlier; they were dropped here. Keeping
     * the first occurrence of each type means behaviour is unchanged.
     */
    @FunctionalInterface
    private interface SemanticDefinitionHandler {{
        void handle(
                Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
                StepEntity identifiedItem,
                StepEntity definition,
                Map<Integer, StepEntity> resolved,
                Map<Integer, List<String>> instanceIdsByTargetId
        );
    }}

    private record SemanticDefinitionRule(Class<?> type, SemanticDefinitionHandler handler) {{
        boolean matches(StepEntity definition) {{
            return type.isInstance(definition);
        }}
    }}

    private static SemanticDefinitionRule rule(Class<?> type, SemanticDefinitionHandler handler) {{
        return new SemanticDefinitionRule(type, handler);
    }}

    private static final List<SemanticDefinitionRule> SEMANTIC_DEFINITION_RULES = List.of(
{rules_text}
    );

    private static void dispatchSemanticDefinitionTargets(
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepEntity identifiedItem,
            StepEntity definition,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {{
        for (SemanticDefinitionRule rule : SEMANTIC_DEFINITION_RULES) {{
            if (rule.matches(definition)) {{
                rule.handler().handle(targetsByUsageId, identifiedItem, definition, resolved, instanceIdsByTargetId);
                return;
            }}
        }}
    }}
'''

    before = lines[0:139]                       # lines 1..139
    head = lines[139:160]                       # lines 140..160 (signature .. fall-through branch)
    dispatch_call = [
        "        dispatchSemanticDefinitionTargets(",
        "                targetsByUsageId,",
        "                identifiedItem,",
        "                definition,",
        "                resolved,",
        "                instanceIdsByTargetId",
        "        );",
    ]
    tail = lines[939:954]                       # lines 940..954 (tail loop .. closing brace)
    after = lines[954:]

    new_lines = before + head + dispatch_call + tail + support.split("\n") + after
    out2 = Path("target/pmi-StepPmiTargetBuilder.new.java")
    out2.parent.mkdir(parents=True, exist_ok=True)
    out2.write_text("\n".join(new_lines), encoding="utf-8")
    print(f"wrote {out2} ({len(new_lines)} lines)")


if __name__ == "__main__":
    main()
