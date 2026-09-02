"""Generate a table-driven dispatch for the Region C chain in
StepPmiPayloadBuilder.buildPmiPayloads.

buildPmiPayloads has three dispatch regions inside separate for-loops:
  * Region A (lines 78..177): 9-branch usage-target chain -- already table-driven
    via PMI_USAGE_TARGET_RULES (see tools/gen_pmi_usage_dispatch.py).
  * Region B (lines 80..85): a 1-branch chain (StepDraughtingCalloutRelationship)
    -- left untouched; it is not a chain.
  * Region C (this script): the `List<PmiPayload> pmi = ...` loop that builds the
    pmi list. It is a ~27-branch if/else-if instanceof chain. Two wrinkles the
    Region A generator did NOT face:
      - branch bodies contain NESTED if blocks (e.g. `if (position != null) {...}`),
        so naive "trim trailing structural lines" would delete a real `}`;
      - one branch has a COMPOUND condition:
        `entity instanceof StepGeometricReplica && "POINT_REPLICA".equals(...)`.
        That cannot be expressed as a plain `Class<?>` rule, so it becomes a
        predicate rule.

This script replaces ONLY the Region C chain with a dispatch table, moving every
branch body verbatim into a handler lambda (continue -> return, since the handler
returns instead of continue-ing the loop). Region A and B stay exactly as they are.

Branch boundaries are detected by the `instanceof` marker (so a nested
`if (position != null)` is NOT mistaken for a chain header), and each branch body
is the span between its header and the NEXT header line -- the next header's
leading `}` is this branch's close, so it is excluded. The final branch is
delimited by brace balancing.

Outputs:
  1. src/test/resources/pmi-payload-dispatch-order.txt
     Frozen ordered expectation (one type per rule, in Region C order).
  2. target/pmi-payload-new.java
     The full rewritten source, for review before it is moved into place.

Usage:
    python tools/gen_pmi_payload_dispatch.py
"""
import re
from pathlib import Path

SRC = Path("src/main/java/com/minicad/export/json/StepPmiPayloadBuilder.java")
NEW_JAVA = Path("target/pmi-payload-new.java")
ORDER_FILE = Path("src/test/resources/pmi-payload-dispatch-order.txt")

# A chain branch header is an `if`/`else if` whose condition mentions instanceof.
# A nested `if (position != null)` does NOT, so it is not mistaken for a header.
HEADER_RE = re.compile(r"^(?:\}\s*)?(?:else\s+)?if \(.*instanceof")


def find_region_c_loop(lines):
    """Return (loop_open_idx, chain_start_idx) 0-based for Region C."""
    pmi_decl = next((k for k, ln in enumerate(lines)
                     if "List<PmiPayload> pmi = new ArrayList<>();" in ln), None)
    if pmi_decl is None:
        raise SystemExit("ABORT: could not find `List<PmiPayload> pmi = new ArrayList<>();`")
    loop_open = next((k for k in range(pmi_decl + 1, len(lines))
                      if "for (StepEntity entity : resolved.values()) {" in lines[k]), None)
    if loop_open is None:
        raise SystemExit("ABORT: could not find Region C for-loop after `pmi` decl")
    chain_start = loop_open + 1
    while chain_start < len(lines) and not HEADER_RE.match(lines[chain_start].strip()):
        chain_start += 1
    if chain_start >= len(lines):
        raise SystemExit("ABORT: could not find Region C chain header")
    return loop_open, chain_start


def close_index(lines, h):
    """Index of the `}` that closes the branch whose header is at line h."""
    depth = 1  # the '{' on the header line
    j = h + 1
    while j < len(lines):
        in_str = False
        for ch in lines[j]:
            if ch == '"':
                in_str = not in_str
            elif not in_str:
                if ch == "{":
                    depth += 1
                elif ch == "}":
                    depth -= 1
        if depth == 0:
            return j
        j += 1
    raise SystemExit("ABORT: unbalanced braces for branch at line " + str(h + 1))


def extract_branches(lines, chain_start, for_close):
    # Every `if`/`else if` whose condition mentions instanceof, scanned across the
    # whole Region C chain (between the first header and the for-loop close). Body
    # lines that are NOT headers are simply skipped -- they are part of the
    # preceding branch's body.
    headers = [i for i in range(chain_start, for_close) if HEADER_RE.match(lines[i].strip())]
    if not headers:
        raise SystemExit("ABORT: no Region C branches found")

    branches = []
    for k, h in enumerate(headers):
        st = lines[h].strip()
        core = st[1:].strip() if st.startswith("}") else st
        if core.startswith("else if "):
            core = core[len("else if "):]
        else:
            core = core[len("if "):]
        rparen = core.rfind(")")
        cond = core[1:rparen].strip()
        types = re.findall(r"instanceof ((?:\w+\.)*\w+)", cond)
        if k < len(headers) - 1:
            # Next header line's leading '}' is this branch's close; exclude it.
            body = lines[h + 1:headers[k + 1]]
        else:
            body = lines[h + 1:close_index(lines, h)]
        branches.append({
            "types": types,
            "cond": cond,
            "has_predicate": "&&" in cond,
            "body": body,  # original indentation preserved
        })
    last_close = close_index(lines, headers[-1])
    return branches, last_close


def build_rules(branches):
    rules = []
    for b in branches:
        t = b["types"][0]
        predicate = None
        if b["has_predicate"]:
            # The `entity instanceof X` part is covered by type.isInstance; keep
            # only the extra guard, with the lambda param renamed to `e`.
            extra = re.sub(r"^\s*entity\s+instanceof\s+\w+\s*&&\s*", "", b["cond"])
            extra = re.sub(r"\bentity\b", "e", extra)
            predicate = "e -> " + extra
        rules.append({"type": t, "body": b["body"], "predicate": predicate})
    return rules


def emit_rule(r, last):
    t = r["type"]
    body_java = "\n".join(l.replace("continue;", "return;") for l in r["body"])
    if r["predicate"]:
        head = (
            "            pmiPayloadRule(\n"
            "                    " + t + ".class,\n"
            "                    " + r["predicate"] + ",\n"
            "                    (pmi, entity, targetsByUsageId, builder) -> {\n"
        )
    else:
        head = "            pmiPayloadRule(" + t + ".class, (pmi, entity, targetsByUsageId, builder) -> {\n"
    comma = "" if last else ","
    return head + body_java + "\n            })" + comma


SUPPORT = '''    /**
     * Dispatch table behind the Region C pmi-building chain in buildPmiPayloads.
     *
     * Replaces a ~27-branch if/else-if {@code instanceof} chain that built the
     * pmi list. The order below is load bearing: {@code instanceof} also matches
     * subtypes and the original chain was "first match wins", so entries keep
     * their original relative order. Each branch body was moved verbatim into a
     * handler that mutates the caller's pmi list (exactly what the branches did),
     * so behaviour is unchanged.
     *
     * The {@code StepGeometricReplica} branch had a compound condition
     * (instanceof AND an entityName() check) which a plain Class-based rule cannot
     * express, so it is a predicate rule. Region A (usage-target collection) and
     * Region B (the 1-branch callout propagation) are NOT part of this table.
     */
    @FunctionalInterface
    private interface PmiPayloadHandler {
        void handle(
                List<PmiPayload> pmi,
                StepEntity entity,
                Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
                StepCadBuilder builder
        );
    }

    private record PmiPayloadRule(Class<?> type, Predicate<StepEntity> predicate, PmiPayloadHandler handler) {
        boolean matches(StepEntity entity) {
            return type.isInstance(entity) && (predicate == null || predicate.test(entity));
        }
    }

    private static PmiPayloadRule pmiPayloadRule(Class<?> type, PmiPayloadHandler handler) {
        return new PmiPayloadRule(type, null, handler);
    }

    private static PmiPayloadRule pmiPayloadRule(Class<?> type, Predicate<StepEntity> predicate, PmiPayloadHandler handler) {
        return new PmiPayloadRule(type, predicate, handler);
    }

    private static final List<PmiPayloadRule> PMI_PAYLOAD_RULES = List.of(
%%RULES%%
    );

    private static void dispatchPmiPayloads(
            List<PmiPayload> pmi,
            StepEntity entity,
            Map<Integer, List<PmiTargetPayload>> targetsByUsageId,
            StepCadBuilder builder
    ) {
        for (PmiPayloadRule rule : PMI_PAYLOAD_RULES) {
            if (rule.matches(entity)) {
                rule.handler().handle(pmi, entity, targetsByUsageId, builder);
                return;
            }
        }
    }
'''


def main():
    lines = SRC.read_text(encoding="utf-8").splitlines()
    if any("PMI_PAYLOAD_RULES" in ln for ln in lines):
        raise SystemExit(
            "ABORT: source already contains PMI_PAYLOAD_RULES.\n"
            "       Restore the original first: git checkout -- " + str(SRC)
        )

    # java.util.* does NOT cover java.util.function.*, and the predicate rule
    # for StepGeometricReplica needs Predicate. spotless reorders imports on
    # apply, so just ensure the line is present for a compilable output.
    if not any("import java.util.function.Predicate;" in ln for ln in lines):
        for k in range(len(lines) - 1, -1, -1):
            if lines[k].startswith("import java.util"):
                lines.insert(k + 1, "import java.util.function.Predicate;")
                break

    loop_open, chain_start = find_region_c_loop(lines)
    for_close = close_index(lines, loop_open)
    branches, last_close = extract_branches(lines, chain_start, for_close)
    rules = build_rules(branches)

    print(f"Region C branches: {len(branches)}")
    preds = [r["type"] for r in rules if r["predicate"]]
    print(f"  predicate rules: {preds}")
    conts = [r["type"] for r in rules if any("continue;" in l for l in r["body"])]
    print(f"  branches with continue->return: {conts}")

    # 1. frozen ordered expectation
    payload = [
        "# Frozen dispatch order for the Region C chain in buildPmiPayloads.",
        "# Captured from the original if/else-if chain before table-driven conversion.",
        "# Order is load bearing: instanceof matches subtypes and the chain is",
        '# "first match wins", so the first matching entry must keep its position.',
    ] + [r["type"] for r in rules]
    ORDER_FILE.parent.mkdir(parents=True, exist_ok=True)
    ORDER_FILE.write_text("\n".join(payload) + "\n", encoding="utf-8")
    print(f"wrote {ORDER_FILE} ({len(rules)} types)")

    # 2. rules text
    parts = [emit_rule(r, last=(idx == len(rules) - 1)) for idx, r in enumerate(rules)]
    rules_text = "\n".join(parts)
    support = SUPPORT.replace("%%RULES%%", rules_text)

    # 3. splice: keep everything up to and including the Region C loop open,
    #    replace the chain (chain_start..last_close) with the dispatch call,
    #    then re-insert the support block before the class closing brace.
    before = lines[0:loop_open + 1]                      # start..loop open
    dispatch_call = [
        "            dispatchPmiPayloads(pmi, entity, targetsByUsageId, builder);",
    ]
    after = lines[last_close + 1:]                       # loop close + return + rest
    class_close = max((k for k, l in enumerate(after) if l.rstrip() == "}"), default=None)
    if class_close is None:
        raise SystemExit("ABORT: could not find the class closing brace")

    new_lines = (before + dispatch_call + after[:class_close]
                 + support.split("\n") + after[class_close:])
    NEW_JAVA.parent.mkdir(parents=True, exist_ok=True)
    if NEW_JAVA.exists():
        NEW_JAVA.unlink()
    NEW_JAVA.write_bytes(("\r\n".join(new_lines) + "\r\n").encode("utf-8"))
    print(f"wrote {NEW_JAVA} ({len(new_lines)} lines)")


if __name__ == "__main__":
    main()
