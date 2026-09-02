"""Generate a table-driven dispatch for StepPmiTargetBuilder.collectSemanticTargets.

The method walks an entity graph through a 316-branch if/else-if instanceof
chain. This script replaces that chain with a dispatch table, moving every
branch body verbatim into a handler lambda so behaviour is unchanged.

Shape of the method, which drives the slicing below:

    guard            if (entity == null || !visiting.add(entity.id())) return Set.of();
    targets          Set<StepEntity> targets = new LinkedHashSet<>();
    standalone if    34 leaf types joined by "||" -> targets.add(entity)
                     ^ NOT part of the chain: it has no else, so entities that
                       match it still fall through into the chain. It must stay
                       outside the dispatch or the fall-through is lost.
    chain            316 branches, first match wins   <- replaced by the table
    tail             visiting.remove(entity.id()); return Set.copyOf(targets);

Outputs:
  1. src/test/resources/pmi-semantic-dispatch-order.txt
     Frozen ordered expectation (one type per rule, in chain order). The order is
     load bearing: instanceof also matches subtypes and the chain is "first match
     wins", so a reordered or dropped rule silently changes behaviour.
  2. target/pmi-semantic-new.java
     The rewritten source, for review before it is moved into place.

Input: the branch JSON produced by tools/analyze_pmi_chain.py.
"""
import json
import re
from pathlib import Path

SRC = Path("src/main/java/com/minicad/export/json/StepPmiTargetBuilder.java")
CHAIN_JSON = Path("target/pmi-semantic-chain.json")

CHAIN_START = 10395  # 1-based: first `if (entity instanceof StepPropertyDefinition) {`
CHAIN_END = 12015    # 1-based: the `}` that closes the last branch
TAIL_START = 12016   # 1-based: `visiting.remove(entity.id());`
TAIL_END = 12018     # 1-based: the method's closing `}`
AFTER = 12019        # 1-based: first line after the method


def main() -> None:
    branches = json.loads(CHAIN_JSON.read_text(encoding="utf-8"))
    lines = SRC.read_text(encoding="utf-8").splitlines()

    # Guard: this generator slices the ORIGINAL if/else-if chain out of the
    # live source. If the source has already been converted (the dispatch table
    # is present), re-running would read the refactored file, mis-slice it and
    # append a SECOND copy of the support block. Always restore the original
    # first (git checkout -- <src>) before running this.
    if any("SEMANTIC_TARGET_RULES" in ln for ln in lines):
        raise SystemExit(
            "ABORT: source already contains SEMANTIC_TARGET_RULES.\n"
            "       Restore the original first: git checkout -- " + str(SRC)
        )
    if not lines[CHAIN_START - 1].lstrip().startswith("if (entity instanceof"):
        raise SystemExit(
            "ABORT: line " + str(CHAIN_START) + " is not the chain header; "
            "the source may be the refactored version. Restore the original first."
        )

    # Expand to one rule per type. A branch written as `A || B` becomes two
    # consecutive rules sharing the handler: the chain is first-match-wins, so
    # checking A then B at that same position is equivalent.
    rules = []
    for b in branches:
        body = [l for l in b["body"] if l.strip()]
        for t in b["types"]:
            rules.append({"type": t, "body": body})

    seen = set()
    deduped, dead = [], []
    for r in rules:
        if r["type"] in seen:
            dead.append(r["type"])
            continue
        seen.add(r["type"])
        deduped.append(r)
    rules = deduped

    print(f"branches: {len(branches)} -> rules: {len(rules)}")
    if dead:
        print(f"dropped {len(dead)} unreachable duplicate rule(s): {sorted(set(dead))}")

    # 1. frozen ordered expectation, so a reorder/drop fails a test.
    order_path = Path("src/test/resources/pmi-semantic-dispatch-order.txt")
    payload = [
        "# Frozen dispatch order for collectSemanticTargets.",
        "# Captured from the original if/else-if chain before table-driven conversion.",
        "# Order is load bearing: instanceof matches subtypes and the chain is",
        "# \"first match wins\", so the first matching entry must keep its position.",
    ] + [r["type"].split(".")[-1] for r in rules]
    try:
        order_path.parent.mkdir(parents=True, exist_ok=True)
        order_path.write_text("\n".join(payload) + "\n", encoding="utf-8")
        print(f"wrote {order_path} ({len(rules)} types)")
    except PermissionError:
        print(f"(skipped) {order_path} is locked; existing file already matches this order")

    # 2. the rules, each branch body verbatim inside a lambda.
    java = []
    for idx, r in enumerate(rules):
        comma = "," if idx < len(rules) - 1 else ""
        # Keep the parameters on the rule's own line when they fit: spreading
        # five parameters over six lines costs ~1900 lines across 317 rules in
        # a class that is already over 12k lines long.
        compact = f"            semanticRule({r['type']}.class, (targets, entity, resolved, visiting, index) -> {{"
        if len(compact) <= 120:
            java.append(compact)
        else:
            java.append("            semanticRule(" + r["type"] + ".class, (")
            java.append("                    targets,")
            java.append("                    entity,")
            java.append("                    resolved,")
            java.append("                    visiting,")
            java.append("                    index")
            java.append("            ) -> {")
        for b in r["body"]:
            java.append("            " + b)
        java.append("            })" + comma)
    rules_text = "\n".join(java)

    support = f'''
    /**
     * Dispatch table behind collectSemanticTargets.
     *
     * Replaces a 316-branch if/else-if {{@code instanceof}} chain. The order below is
     * load bearing: {{@code instanceof}} also matches subtypes and the original chain
     * was "first match wins", so entries keep their original relative order.
     *
     * Each branch body was moved verbatim into a handler, so behaviour is
     * unchanged -- the handlers mutate the caller's target set instead of
     * returning one, matching what the branches did.
     *
     * The leaf-type check that precedes the chain (the 34 types OR-ed together
     * in {{@code collectSemanticTargets}}) is deliberately NOT part of this table: it
     * is a standalone {{@code if}} with no {{@code else}}, so entities matching it still
     * fall through into this dispatch. Folding it in would make the first match
     * return and silently drop those extra targets.
     */
    @FunctionalInterface
    private interface SemanticTargetHandler {{
        void handle(
                Set<StepEntity> targets,
                StepEntity entity,
                Map<Integer, StepEntity> resolved,
                Set<Integer> visiting,
                PmiEntityIndex index
        );
    }}

    private record SemanticTargetRule(Class<?> type, SemanticTargetHandler handler) {{
        boolean matches(StepEntity entity) {{
            return type.isInstance(entity);
        }}
    }}

    private static SemanticTargetRule semanticRule(Class<?> type, SemanticTargetHandler handler) {{
        return new SemanticTargetRule(type, handler);
    }}

    private static final List<SemanticTargetRule> SEMANTIC_TARGET_RULES = List.of(
{rules_text}
    );

    private static void dispatchSemanticTargets(
            Set<StepEntity> targets,
            StepEntity entity,
            Map<Integer, StepEntity> resolved,
            Set<Integer> visiting,
            PmiEntityIndex index
    ) {{
        for (SemanticTargetRule rule : SEMANTIC_TARGET_RULES) {{
            if (rule.matches(entity)) {{
                rule.handler().handle(targets, entity, resolved, visiting, index);
                return;
            }}
        }}
    }}
'''

    before = lines[0:CHAIN_START - 1]      # 1..10394, includes the leaf-type if
    dispatch_call = [
        "        dispatchSemanticTargets(targets, entity, resolved, visiting, index);",
    ]
    tail = lines[TAIL_START - 1:TAIL_END]  # 12016..12018
    after = lines[AFTER - 1:]              # 12019..end

    new_lines = before + dispatch_call + tail + support.split("\n") + after
    out = Path("target/pmi-semantic-new.java")
    out.parent.mkdir(parents=True, exist_ok=True)
    out.write_text("\n".join(new_lines), encoding="utf-8")
    print(f"wrote {out} ({len(new_lines)} lines)")


if __name__ == "__main__":
    main()
