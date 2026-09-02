"""Verify the generated SEMANTIC_TARGET_RULES table is a faithful 1:1 copy of the
original else-if chain.

Usage:
    python tools/verify_semantic_dispatch.py <chain.json> <refactored.java>

For every branch of the original chain it checks, in order:
  1. the rule's key type equals the branch's instanceof type
  2. the rule body (whitespace-normalised) equals the branch body
Any mismatch is reported with the branch index and a unified diff.
Exits 0 when the table is faithful, 1 otherwise.
"""
import json
import re
import sys
import difflib


def norm(text):
    """Collapse indentation/blank lines so re-indentation is not a false alarm."""
    if isinstance(text, list):
        text = "\n".join(text)
    lines = [ln.strip() for ln in text.splitlines()]
    return [ln for ln in lines if ln]


RULE_START = re.compile(r"semanticRule\(\s*([\w.]+)\.class\s*,\s*\(([^)]*)\)\s*->\s*\{")


def parse_rules(src):
    """Extract (type, body) pairs out of the SEMANTIC_TARGET_RULES list literal."""
    anchor = src.find("SEMANTIC_TARGET_RULES = List.of(")
    if anchor < 0:
        raise SystemExit("SEMANTIC_TARGET_RULES not found - is the refactor applied?")
    start = src.index("(", anchor) + 1
    depth = 1
    i = start
    while depth:
        if src[i] == "(":
            depth += 1
        elif src[i] == ")":
            depth -= 1
        i += 1
    block = src[start:i - 1]

    rules = []
    pos = 0
    while True:
        m = RULE_START.search(block, pos)
        if not m:
            break
        body_open = m.end() - 1  # index of the '{'
        depth = 0
        j = body_open
        while True:
            if block[j] == "{":
                depth += 1
            elif block[j] == "}":
                depth -= 1
                if depth == 0:
                    break
            j += 1
        rules.append((m.group(1), block[body_open + 1:j]))
        pos = j + 1
    return rules


def main():
    chain_path, src_path = sys.argv[1], sys.argv[2]
    branches = json.load(open(chain_path, encoding="utf-8"))
    src = open(src_path, encoding="utf-8").read()
    rules = parse_rules(src)

    print("original branches : %d" % len(branches))
    print("generated rules   : %d" % len(rules))

    # A branch whose condition ORs several types expands into one rule per type
    # (each carrying a copy of the same body), so walk both lists with two cursors.
    bad = 0
    consumed = 0
    for idx, br in enumerate(branches):
        btypes = br["types"]
        simple = [t.split(".")[-1] for t in btypes]
        for offset, want in enumerate(simple):
            if consumed >= len(rules):
                bad += 1
                print("\n[MISSING RULE] branch #%d (line %s): no rule for %s"
                      % (idx, br.get("line"), want))
                break
            rtype, rbody = rules[consumed]
            consumed += 1
            # rules may carry a fully-qualified type name; compare on simple name
            if rtype.split(".")[-1] != want:
                bad += 1
                print("\n[TYPE MISMATCH] branch #%d (line %s) slot %d: expected %s, rule keys on %s"
                      % (idx, br.get("line"), offset, want, rtype))
                continue
            nb, nr = norm(br["body"]), norm(rbody)
            if nb != nr:
                bad += 1
                print("\n[BODY MISMATCH] branch #%d (line %s) type=%s"
                      % (idx, br.get("line"), rtype))
                for line in list(difflib.unified_diff(nb, nr, "original",
                                                      "generated", lineterm=""))[:40]:
                    print("   " + line)
        if bad >= 5:
            print("... stopping after 5 mismatches")
            break

    if consumed != len(rules):
        bad += 1
        print("\n[LEFTOVER RULES] %d rules emitted but only %d accounted for"
              % (len(rules), consumed))

    print("\nbranches=%d rules=%d consumed=%d" % (len(branches), len(rules), consumed))
    print("result: %s (%d problems)" % ("FAITHFUL" if bad == 0 else "DIFFERENT", bad))
    sys.exit(0 if bad == 0 else 1)


if __name__ == "__main__":
    main()
