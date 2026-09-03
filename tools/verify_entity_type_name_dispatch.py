#!/usr/bin/env python3
"""1:1 body-fidelity check for the StepEntityNamingUtils.stepEntityTypeName fold.

stepEntityTypeName is a `static` first-match-return chain, so the generated lambdas
carry no `self` and every branch body is copied verbatim into the table. This
verifier proves that: it pulls the original sequential-if chain from git HEAD and
the generated ENTITY_TYPE_NAME_RULES table from the working tree and asserts each
body matches (type-for-type, verbatim up to whitespace).

It ALSO asserts the method's fall-through tail survived the fold. That tail is a
multi-statement fallback (getSimpleName -> strip "Step" prefix -> camelToUpperSnake),
not a single return, and an off-by-one slice in the generator would silently eat its
first three statements -- which no branch-body comparison would ever notice.

Exits non-zero on any divergence. This is the behavioral-equivalence proof BEFORE
the build runs.
"""
import re
import subprocess
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
SRC = ROOT / "src/main/java/com/minicad/step/semantic/StepEntityNamingUtils.java"

METHOD_SIG = "    static String stepEntityTypeName("
TABLE_FIELD = "ENTITY_TYPE_NAME_RULES"
RULE_RECORD = "EntityTypeNameRule"
RULE_FACTORY = "entityTypeNameRule"
HEADER_RE = re.compile(r"if \((.*)\)\s*\{$")


def norm(s):
    return " ".join(s.split())


def method_bounds(lines):
    """Return (method_idx, end_idx) located structurally (not by a marker substring)."""
    mi = next(i for i, ln in enumerate(lines) if ln.startswith(METHOD_SIG))
    end = next(i for i in range(mi + 1, len(lines)) if lines[i] == "    }")
    return mi, end


def scan_branches(lines, start, stop):
    """Return (branches, region_end); branches are (type_name, body_joined, is_guarded)."""
    branches = []
    region_end = start
    i = start
    while i < stop:
        stripped = lines[i].strip()
        if stripped.startswith("if (entity instanceof ") and stripped.endswith("{"):
            condition = HEADER_RE.search(lines[i]).group(1)
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
            branches.append((type_name, " ".join(b.strip() for b in body), is_guarded))
            region_end = k + 1
            i = k + 1
            continue
        i += 1
    return branches, region_end


def original_chain(text):
    """Extract (branches, tail) from the pre-fold source at git HEAD."""
    lines = text.replace("\r\n", "\n").replace("\r", "\n").split("\n")
    mi, end = method_bounds(lines)
    bi = next(
        i
        for i in range(mi + 1, end)
        if lines[i].strip().startswith("if (entity instanceof ")
    )
    branches, region_end = scan_branches(lines, bi, end)
    tail = [ln.strip() for ln in lines[region_end:end] if ln.strip()]
    return branches, tail


def folded_tail(text):
    """Extract the post-loop fall-through tail from the folded working-tree source."""
    lines = text.replace("\r\n", "\n").replace("\r", "\n").split("\n")
    mi, end = method_bounds(lines)
    close = next(i for i in range(mi + 1, end) if lines[i] == "        }")
    return [ln.strip() for ln in lines[close + 1 : end] if ln.strip()]


def extract_table_entries(text):
    text = text.replace("\r\n", "\n").replace("\r", "\n")
    marker = "private static final List<" + RULE_RECORD + "> " + TABLE_FIELD + " = List.of("
    start = text.index(marker)
    region = text[start:]
    end = region.index("\n    );")
    region = region[: end + len("\n    );")]
    entries = []
    i = 0
    while True:
        j = region.find(RULE_FACTORY + "(", i)
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
                    entry = region[j : k + 1]
                    break
            k += 1
        m = re.search(
            RULE_FACTORY + r"\((\w+)\.class,\s*\(entity\)\s*->\s*\{(.*)\}\s*\)\s*,?$",
            entry or "",
            re.S,
        )
        if not m:
            print("PARSE FAIL at entry:\n", entry)
            sys.exit(1)
        entries.append((m.group(1), norm(m.group(2))))
        i = k + 1
    return entries


def main():
    head = subprocess.run(
        ["git", "show", "HEAD:" + SRC.relative_to(ROOT).as_posix()],
        cwd=ROOT, capture_output=True, text=True,
    ).stdout
    head = head.replace("\r\n", "\n").replace("\r", "\n")
    cur = SRC.read_text(encoding="utf-8").replace("\r\n", "\n").replace("\r", "\n")

    orig, orig_tail = original_chain(head)
    gen = extract_table_entries(cur)
    gen_tail = folded_tail(cur)

    ok = True

    if len(orig) != len(gen):
        print(f"MISMATCH count: original {len(orig)} vs generated {len(gen)}")
        sys.exit(1)

    seen = set()
    dup = set()
    for t, _ in gen:
        if t in seen:
            dup.add(t)
        seen.add(t)

    for idx, (ob, gb) in enumerate(zip(orig, gen), 1):
        o_type, o_body, o_guarded = ob
        g_type, g_body = gb
        if o_type != g_type:
            print(f"MISMATCH type at {idx}: original {o_type} vs generated {g_type}")
            ok = False
            continue
        if o_guarded:
            print(
                f"UNEXPECTED guarded branch at {idx} ({o_type}): the loop is "
                "first-match-return and cannot express a guard"
            )
            ok = False
            continue
        if g_body != norm(o_body):
            print(
                f"MISMATCH body at {idx} ({o_type}):\n"
                f"  original ={norm(o_body)}\n  generated={g_body}"
            )
            ok = False

    if dup:
        print("DUPLICATE types in generated table:", sorted(dup))
        ok = False

    if not orig_tail:
        print("ABORT: no fall-through tail found in the original method")
        ok = False
    elif orig_tail != gen_tail:
        print(
            "MISMATCH fall-through tail (the multi-statement fallback after the chain):\n"
            "  original =%s\n  generated=%s" % (orig_tail, gen_tail)
        )
        ok = False

    if ok:
        print(
            f"FAITHFUL: all {len(orig)} branch bodies match verbatim and the "
            f"{len(orig_tail)}-statement fall-through tail is intact (stepEntityTypeName)"
        )
        sys.exit(0)
    sys.exit(1)


if __name__ == "__main__":
    main()
