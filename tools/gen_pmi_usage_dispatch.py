"""Generate a table-driven dispatch for the Region A chain in
StepPmiPayloadBuilder.buildPmiPayloads.

buildPmiPayloads contains three dispatch regions inside separate for-loops:
  * Region A (lines 78..177): a 9-branch if/else-if instanceof chain that
    populates `targetsByUsageId` by calling StepPmiTargetBuilder.append*.
    No `return`/`break`/`continue`; it only references entity, resolved,
    targetsByUsageId and instanceIdsByTargetId.
  * Region B (179..184): a 1-branch chain (left untouched).
  * Region C (186..): a multi-branch chain that builds `pmi` and contains
    `continue` -- out of scope for this generator.

This script replaces ONLY Region A with a dispatch table, moving every branch
body verbatim into a handler lambda so behaviour is unchanged. Region B and
Region C stay exactly as they are.

Outputs:
  1. src/test/resources/pmi-usage-dispatch-order.txt
     Frozen ordered expectation (one type per rule, in Region A order).
  2. target/pmi-usage-new.java
     The rewritten source, for review before it is moved into place.

Input: the branch JSON from tools/analyze_pmi_chain.py, filtered to Region A
(branches with line <= 177).
"""
import json
from pathlib import Path

SRC = Path("src/main/java/com/minicad/export/json/StepPmiPayloadBuilder.java")
CHAIN_JSON = Path("target/pmi-payload-chain.json")

CHAIN_START = 78   # 1-based: the `if (entity instanceof StepGeometricItemSpecificUsage) {`
CHAIN_END = 177    # 1-based: the `}` that closes the last `else if` branch


def main() -> None:
    branches = json.loads(CHAIN_JSON.read_text(encoding="utf-8"))
    if isinstance(branches, dict):
        branches = branches.get("branches", branches.get("chain", []))
    region_a = [b for b in branches if b["line"] <= 177]

    lines = SRC.read_text(encoding="utf-8").splitlines()

    if any("PMI_USAGE_TARGET_RULES" in ln for ln in lines):
        raise SystemExit(
            "ABORT: source already contains PMI_USAGE_TARGET_RULES.\n"
            "       Restore the original first: git checkout -- " + str(SRC)
        )
    if not lines[CHAIN_START - 1].lstrip().startswith("if (entity instanceof"):
        raise SystemExit(
            "ABORT: line " + str(CHAIN_START) + " is not the Region A chain header; "
            "the source may already be refactored. Restore the original first."
        )

    # One rule per branch (Region A has no `||` dual-type branches).
    rules = []
    for b in region_a:
        body = trim_branch_body(b["body"])
        rules.append({"type": b["types"][0], "body": body})

    seen = set()
    deduped, dead = [], []
    for r in rules:
        if r["type"] in seen:
            dead.append(r["type"])
            continue
        seen.add(r["type"])
        deduped.append(r)
    rules = deduped

    print(f"Region A branches: {len(region_a)} -> rules: {len(rules)}")
    if dead:
        print(f"dropped {len(dead)} unreachable duplicate rule(s): {sorted(set(dead))}")

    # 1. frozen ordered expectation
    order_path = Path("src/test/resources/pmi-usage-dispatch-order.txt")
    payload = [
        "# Frozen dispatch order for the Region A chain in buildPmiPayloads.",
        "# Captured from the original if/else-if chain before table-driven conversion.",
        "# Order is load bearing: instanceof matches subtypes and the chain is",
        '# "first match wins", so the first matching entry must keep its position.',
    ] + [r["type"].split(".")[-1] for r in rules]
    order_path.parent.mkdir(parents=True, exist_ok=True)
    order_path.write_text("\n".join(payload) + "\n", encoding="utf-8")
    print(f"wrote {order_path} ({len(rules)} types)")

    # 2. the rules, each branch body verbatim inside a lambda.
    java = []
    for idx, r in enumerate(rules):
        comma = "," if idx < len(rules) - 1 else ""
        compact = (
            "            pmiUsageTargetRule(" + r["type"] + ".class, "
            "(entity, resolved, targetsByUsageId, instanceIdsByTargetId) -> {"
        )
        if len(compact) <= 120:
            java.append(compact)
        else:
            java.append("            pmiUsageTargetRule(" + r["type"] + ".class, (")
            java.append("                    entity,")
            java.append("                    resolved,")
            java.append("                    targetsByUsageId,")
            java.append("                    instanceIdsByTargetId")
            java.append("            ) -> {")
        for b in r["body"]:
            java.append("            " + b)
        java.append("            })" + comma)
    rules_text = "\n".join(java)

    support = f'''
    /**
     * Dispatch table behind the Region A usage-target collection in
     * buildPmiPayloads.
     *
     * Replaces a 9-branch if/else-if {{@code instanceof}} chain (lines 78..177 of
     * the original). The order below is load bearing: {{@code instanceof}} also
     * matches subtypes and the original chain was "first match wins", so entries
     * keep their original relative order.
     *
     * Each branch body was moved verbatim into a handler; the handlers mutate the
     * caller's targetsByUsageId map (exactly what the branches did), so behaviour
     * is unchanged. Region B (a 1-branch chain) and Region C (which builds the
     * pmi list and contains continue) are intentionally NOT part of this table.
     */
    @FunctionalInterface
    private interface PmiUsageTargetHandler {{
        void handle(
                StepEntity entity,
                Map<Integer, StepEntity> resolved,
                Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
                Map<Integer, List<String>> instanceIdsByTargetId
        );
    }}

    private record PmiUsageTargetRule(Class<?> type, PmiUsageTargetHandler handler) {{
        boolean matches(StepEntity entity) {{
            return type.isInstance(entity);
        }}
    }}

    private static PmiUsageTargetRule pmiUsageTargetRule(Class<?> type, PmiUsageTargetHandler handler) {{
        return new PmiUsageTargetRule(type, handler);
    }}

    private static final List<PmiUsageTargetRule> PMI_USAGE_TARGET_RULES = List.of(
{rules_text}
    );

    private static void dispatchPmiUsageTargets(
            StepEntity entity,
            Map<Integer, StepEntity> resolved,
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            Map<Integer, List<String>> instanceIdsByTargetId
    ) {{
        for (PmiUsageTargetRule rule : PMI_USAGE_TARGET_RULES) {{
            if (rule.matches(entity)) {{
                rule.handler().handle(entity, resolved, targetsByUsageId, instanceIdsByTargetId);
                return;
            }}
        }}
    }}
'''

    # Insert the support block as new class-level members, just before the
    # class's closing brace. We deliberately do NOT brace-match the method body:
    # naive brace counting is fooled by '{'/'}' inside string literals and
    # comments, which would misplace the block (and did, on the first pass).
    post = lines[CHAIN_END:]                     # 1-based 178..end (Region B + Region C + rest of method + other members + class close)
    class_close = max((i for i, l in enumerate(post) if l.rstrip() == "}"), default=None)
    if class_close is None:
        raise SystemExit("ABORT: could not find the class closing brace in the source")

    before = lines[0:CHAIN_START - 1]            # 1..77 (signature + locals + for-open)
    dispatch_call = [
        "            dispatchPmiUsageTargets(entity, resolved, targetsByUsageId, instanceIdsByTargetId);",
    ]

    new_lines = before + dispatch_call + post[:class_close] + support.split("\n") + post[class_close:]
    out = Path("target/pmi-usage-new.java")
    out.parent.mkdir(parents=True, exist_ok=True)
    # The Windows sandbox blocks overwriting an existing file, so delete first.
    if out.exists():
        out.unlink()
    # The project source uses CRLF line endings; write CRLF so the generated
    # file is spotless-clean without a separate `mvn spotless:apply` pass.
    # spotless requires the file to end with a newline, so append one.
    # Write bytes directly: Path.write_text() opens in text mode and on Windows
    # would translate every '\n' -> '\r\n', turning our '\r\n' into '\r\r\n'.
    out.write_bytes(("\r\n".join(new_lines) + "\r\n").encode("utf-8"))
    print(f"wrote {out} ({len(new_lines)} lines)")


def trim_branch_body(body):
    """Return only the real statements of a branch body.

    analyze_pmi_chain.py bounds every branch by the *next* `else if`. The
    LAST Region A branch has no following `else if`, so its captured body
    overruns and pulls in the branch's own closing `}`, the Region A for-loop
    closer `}`, and the next region's `for (...)` header. Those structural
    lines are not part of the branch and must not be copied verbatim into the
    handler lambda. Drop trailing lines that are block closers or new
    block/loop headers so only genuine statements remain.

    Region A branch bodies are flat statement sequences (no nested blocks), so
    trimming trailing structural lines is safe and faithful.
    """
    stripped = [l for l in body if l.strip()]
    structural = ("}", "for (", "if (", "else", "while (", "do {", "switch (", "try {")
    while stripped:
        s = stripped[-1].strip()
        if s in structural or s.startswith(structural):
            stripped.pop()
        else:
            break
    # Fail loudly if any over-capture slipped through (e.g. a `for` mid-body).
    for ln in stripped:
        s = ln.strip()
        if s == "}" or s.startswith("for ("):
            raise SystemExit(
                "ABORT: branch body still contains structural line after trim: "
                + repr(ln)
            )
    return stripped


if __name__ == "__main__":
    main()
