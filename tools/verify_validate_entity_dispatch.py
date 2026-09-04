#!/usr/bin/env python3
"""1:1 body-fidelity check for a table-driven `instanceof` fold.

Parametric counterpart to gen_validate_entity_dispatch.py. It pulls the
ORIGINAL sequential-if chain from git HEAD and the GENERATED table from the
working tree, then asserts:

  1. same number of branches, same types, same order;
  2. every branch body is byte-identical up to whitespace;
  3. the fall-through semantics the chain required match the loop that shipped
     (a chain whose branches all return needs first-match; a chain with a
     falling-through branch needs the null-means-keep-looking loop);
  4. the terminal statement survived;
  5. no duplicate types.

This is the behavioural-equivalence proof that runs BEFORE the build, because
the build cannot see the difference: the bodies are identical either way, only
the surrounding control flow differs.

    python tools/verify_validate_entity_dispatch.py --method validateAssignmentEntity
    python tools/verify_validate_entity_dispatch.py --method buildSemanticSurfaceGeometry \
        --source src/main/java/com/minicad/export/mesh/StepMeshExporter.java \
        --result-type SurfaceGeometry --params geometry,builder
"""
import argparse
import re
import subprocess
import sys
from pathlib import Path

import gen_validate_entity_dispatch as gen

RESULT_TYPE = gen.RESULT_TYPE


def norm(s):
    return " ".join(s.split())


def original_branches(lines, method):
    """(type, body, guarded, exits) dicts from the pre-fold chain.

    Shares the generator's extractor so both agree on what counts as a branch --
    in particular on multi-line OR-group headers, where one body serves several
    types. OR groups are expanded to one dict per type, matching how the table
    is rendered, so the downstream `ob["type"]` / `ob["exits"]` indexing keeps
    working and the fall-through classification stays accurate.
    """
    mi, body_start, terminal, _end = gen.method_bounds(lines, method)
    bi = next(
        i
        for i in range(body_start, terminal)
        if lines[i].strip().startswith("if (%s instanceof " % gen.SUBJECT)
    )
    branches, _ = gen.extract_branches(lines, bi, terminal, gen.SUBJECT)
    out = []
    for t, br in gen.expand(branches):
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

    params_pat = r"\s*,\s*".join(re.escape(p) for p in gen.PARAMS)
    entry_re = re.compile(
        re.escape(names["factory"])
        + r"\(([\w.]+)\.class,\s*\("
        + params_pat
        + r"\)\s*->\s*\{(.*)\}\s*\)\s*,?$",
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


def shipped_mode(lines, method):
    mi, _bs, _t, end = gen.method_bounds(lines, method)
    body = "\n".join(lines[mi:end])
    if gen.TERMINAL not in body:
        print("MISSING terminal `%s` in the folded method" % gen.TERMINAL)
        sys.exit(1)
    return "null-fallthrough" if "!= null" in body else "first-match"


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--method", required=True)
    ap.add_argument(
        "--source",
        default=str(gen.SRC),
        help="java source file containing the method (default: StepDumpApp.java)",
    )
    ap.add_argument("--result-type", default=gen.RESULT_TYPE)
    ap.add_argument(
        "--params",
        default=",".join(gen.PARAMS),
        help="comma-separated lambda parameters; first is the instanceof operand",
    )
    ap.add_argument("--terminal", default=gen.TERMINAL)
    args = ap.parse_args()

    # Drive the generator's globals so its extractor/matchers use this shape.
    gen.RESULT_TYPE = args.result_type
    gen.PARAMS = tuple(p.strip() for p in args.params.split(","))
    gen.SUBJECT = gen.PARAMS[0]
    gen.TERMINAL = args.terminal
    gen.SRC = Path(args.source).resolve()

    names = gen.derive(args.method)

    head = subprocess.run(
        ["git", "show", "HEAD:" + gen.SRC.relative_to(gen.ROOT).as_posix()],
        cwd=gen.ROOT, capture_output=True, text=True,
    ).stdout.replace("\r\n", "\n").replace("\r", "\n")
    cur = gen.SRC.read_text(encoding="utf-8").replace("\r\n", "\n").replace("\r", "\n")

    orig = original_branches(head.split("\n"), args.method)
    gen_tbl = table_entries(cur, names)
    cur_lines = cur.split("\n")

    ok = True

    if len(orig) != len(gen_tbl):
        print("MISMATCH count: original %d vs generated %d" % (len(orig), len(gen_tbl)))
        sys.exit(1)

    needs = "null-fallthrough" if any(not b["exits"] for b in orig) else "first-match"
    ships = shipped_mode(cur_lines, args.method)
    if needs != ships:
        print("MISMATCH semantics: chain needs %s, folded method ships %s" % (needs, ships))
        ok = False

    seen, dup = set(), []
    for t, _ in gen_tbl:
        if t in seen:
            dup.append(t)
        seen.add(t)
    if dup:
        print("DUPLICATE types in generated table:", sorted(set(dup)))
        ok = False

    for idx, (ob, (g_type, g_body)) in enumerate(zip(orig, gen_tbl), 1):
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
            expected = norm(expected + " " + gen.TERMINAL)
        if g_body != expected:
            print(
                "MISMATCH body at %d (%s):\n  original =%s\n  generated=%s"
                % (idx, ob["type"], expected, g_body)
            )
            ok = False

    if ok:
        print(
            "FAITHFUL: all %d branch bodies match verbatim, dispatch is %s, terminal "
            "`%s` intact (%s)" % (len(orig), ships, gen.TERMINAL, names["method"])
        )
        sys.exit(0)
    sys.exit(1)


if __name__ == "__main__":
    main()
