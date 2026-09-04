#!/usr/bin/env python3
"""1:1 body-fidelity check for a StepDumpApp `validateXxxEntity` fold.

Parametric counterpart to gen_validate_entity_dispatch.py, replacing the
per-method verify_*.py copies. It pulls the ORIGINAL sequential-if chain from git
HEAD and the GENERATED table from the working tree, then asserts:

  1. same number of branches, same types, same order;
  2. every branch body is byte-identical up to whitespace;
  3. the fall-through semantics the chain required match the loop that shipped
     (a chain whose branches all return needs first-match; a chain with a
     falling-through branch needs the null-means-keep-looking loop);
  4. the terminal `return null;` survived;
  5. no duplicate types.

This is the behavioural-equivalence proof that runs BEFORE the build, because
the build cannot see the difference: the bodies are identical either way, only
the surrounding control flow differs.

    python tools/verify_validate_entity_dispatch.py --method validateAssignmentEntity
"""
import argparse
import re
import subprocess
import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
from gen_validate_entity_dispatch import (  # noqa: E402
    BRANCH_START,
    ROOT,
    SRC,
    derive,
    expand,
    extract_branches,
    method_bounds,
)

RESULT_TYPE = "Integer"


def norm(s):
    return " ".join(s.split())


def original_branches(lines, sig):
    """(type, body, guarded, exits) dicts from the pre-fold chain.

    Shares the generator's extractor so both agree on what counts as a branch --
    in particular on multi-line OR-group headers, where one body serves several
    types. OR groups are expanded to one dict per type, matching how the table
    is rendered, so the downstream `ob["type"]` / `ob["exits"]` indexing keeps
    working and the fall-through classification stays accurate.
    """
    mi, body_start, terminal, _end = method_bounds(lines, sig)
    bi = next(
        i for i in range(body_start, terminal) if lines[i].strip().startswith(BRANCH_START)
    )
    branches, _ = extract_branches(lines, bi, terminal)
    out = []
    for t, br in expand(branches):
        out.append(
            {
                "type": t,
                "body": norm(" ".join(b.strip() for b in br["body"])),
                "guarded": "&&" in br["condition"],
                "exits": br["exits"],
            }
        )
    return out


def table_entries(text, names):
    marker = "private static final List<%s> %s = List.of(" % (
        names["record"],
        names["table"],
    )
    start = text.index(marker)
    region = text[start:]
    region = region[: region.index("\n    );") + len("\n    );")]

    entry_re = re.compile(
        re.escape(names["factory"])
        + r"\((\w+)\.class,\s*\(entity,\s*builder\)\s*->\s*\{(.*)\}\s*\)\s*,?$",
        re.S,
    )
    entries = []
    i = 0
    while True:
        j = region.find(names["factory"] + "(", i)
        if j == -1:
            break
        depth = 0
        k = j
        entry = None
        while k < len(region):
            c = region[k]
            if c in "({":
                depth += 1
            elif c in ")}":
                depth -= 1
                if depth == 0:
                    entry = region[j:k + 1]
                    break
            k += 1
        if entry is None:
            print("PARSE FAIL: unbalanced entry starting at", j)
            sys.exit(1)
        m = entry_re.search(entry)
        if not m:
            print("PARSE FAIL at entry:\n", entry)
            sys.exit(1)
        entries.append((m.group(1), norm(m.group(2))))
        i = k + 1
    return entries


def shipped_mode(lines, sig):
    mi, _bs, _t, end = method_bounds(lines, sig)
    body = "\n".join(lines[mi:end])
    if "return null;" not in body:
        print("MISSING terminal `return null;` in the folded method")
        sys.exit(1)
    return "null-fallthrough" if "!= null" in body else "first-match"


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--method", required=True)
    args = ap.parse_args()

    names = derive(args.method)
    sig = "    private static %s %s(" % (RESULT_TYPE, names["method"])

    head = subprocess.run(
        ["git", "show", "HEAD:" + SRC.relative_to(ROOT).as_posix()],
        cwd=ROOT, capture_output=True, text=True,
    ).stdout.replace("\r\n", "\n").replace("\r", "\n")
    cur = SRC.read_text(encoding="utf-8").replace("\r\n", "\n").replace("\r", "\n")

    orig = original_branches(head.split("\n"), sig)
    gen = table_entries(cur, names)
    cur_lines = cur.split("\n")

    ok = True

    if len(orig) != len(gen):
        print("MISMATCH count: original %d vs generated %d" % (len(orig), len(gen)))
        sys.exit(1)

    needs = "null-fallthrough" if any(not b["exits"] for b in orig) else "first-match"
    ships = shipped_mode(cur_lines, sig)
    if needs != ships:
        print("MISMATCH semantics: chain needs %s, folded method ships %s" % (needs, ships))
        ok = False

    seen, dup = set(), []
    for t, _ in gen:
        if t in seen:
            dup.append(t)
        seen.add(t)
    if dup:
        print("DUPLICATE types in generated table:", sorted(set(dup)))
        ok = False

    for idx, (ob, (g_type, g_body)) in enumerate(zip(orig, gen), 1):
        if ob["type"] != g_type:
            print("MISMATCH type at %d: original %s vs generated %s" % (idx, ob["type"], g_type))
            ok = False
            continue
        if ob["guarded"]:
            print("UNEXPECTED guarded branch at %d (%s)" % (idx, ob["type"]))
            ok = False
            continue
        expected = ob["body"]
        if needs == "null-fallthrough" and not ob["exits"]:
            expected = norm(expected + " return null;")
        if g_body != expected:
            print(
                "MISMATCH body at %d (%s):\n  original =%s\n  generated=%s"
                % (idx, ob["type"], expected, g_body)
            )
            ok = False

    if ok:
        print(
            "FAITHFUL: all %d branch bodies match verbatim, dispatch is %s, terminal "
            "`return null;` intact (%s)" % (len(orig), ships, names["method"])
        )
        sys.exit(0)
    sys.exit(1)


if __name__ == "__main__":
    main()
