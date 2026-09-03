#!/usr/bin/env python3
"""1:1 body-fidelity check for the PreviewGeometryCollector.collectStandaloneEdges fold.

collectStandaloneEdges is a static void recursive collector. The generated lambdas are in
static context (no `self`), so each generated lambda body equals the original branch body
VERBATIM up to whitespace -- the lambda carries the method's params (item, edges, resolved,
builder, metadata) which the original body already referenced by those exact names. This
verifier proves that:

  1. the original sequential-if chain (from git HEAD) and the generated PREVIEW_EDGE_COLLECT_RULES
     table have the SAME type sequence (OR-compound branches split into per-type entries on
     BOTH sides; dead-code duplicate types deduped to their first/reachable occurrence on
     BOTH sides), and
  2. every branch body matches verbatim (whitespace-normalized).

Exits non-zero on any divergence. This is the behavioral-equivalence proof BEFORE the build.
"""
import re
import subprocess
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
SRC = ROOT / "src/main/java/com/minicad/preview/builder/PreviewGeometryCollector.java"

METHOD_SIG_SUBSTR = "static void collectStandaloneEdges("
TERMINAL_MARKER = "isSampledCurveSource(item)"
TABLE_FIELD = "PREVIEW_EDGE_COLLECT_RULES"
HEADER_RE = re.compile(r"if \((.*)\)\s*\{$")
TYPE_RE = re.compile(r"instanceof\s+([\w.]+)")


def norm(s):
    return " ".join(s.split())


def simple(name):
    return name.split(".")[-1]


def split_types(condition):
    return TYPE_RE.findall(condition)


def extract_original_branches(text):
    lines = text.replace("\r\n", "\n").replace("\r", "\n").split("\n")
    mi = next(i for i, ln in enumerate(lines) if METHOD_SIG_SUBSTR in ln)
    open_i = next(i for i in range(mi, len(lines)) if "{" in lines[i])
    bi = next(i for i in range(open_i + 1, len(lines)) if lines[i].strip().startswith("if (item instanceof "))
    ti = next(i for i in range(open_i + 1, len(lines)) if TERMINAL_MARKER in lines[i])
    branches = []
    seen = set()
    i = bi
    while i < ti:
        line = lines[i]
        stripped = line.strip()
        if stripped.startswith("if (item instanceof ") and stripped.endswith("{"):
            condition = HEADER_RE.search(line).group(1)
            type_list = split_types(condition)
            is_guarded = "&&" in condition
            depth = 1
            body = []
            k = i + 1
            while k < ti:
                for ch in lines[k]:
                    if ch == "{":
                        depth += 1
                    elif ch == "}":
                        depth -= 1
                if depth == 0:
                    break
                body.append(lines[k])
                k += 1
            body_joined = " ".join(b.strip() for b in body)
            for type_name in type_list:
                s = simple(type_name)
                if s in seen:
                    # Original chain lists this type twice (different bodies); the
                    # second is dead code. Keep only the first/reachable occurrence to
                    # match the deduplicated generated table.
                    continue
                seen.add(s)
                branches.append((s, body_joined, is_guarded))
            i = k + 1
            continue
        i += 1
    return branches


def extract_table_entries(text):
    text = text.replace("\r\n", "\n").replace("\r", "\n")
    marker = "private static final List<PreviewEdgeCollectRule> " + TABLE_FIELD + " = List.of("
    start = text.index(marker)
    region = text[start:]
    end = region.index("\n    );")
    region = region[: end + len("\n    );")]
    entries = []
    i = 0
    while True:
        j = region.find("previewEdgeCollectRule(", i)
        if j == -1:
            break
        depth = 0
        k = j
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
        m = re.search(r"previewEdgeCollectRule\(([\w.]+)\.class,\s*\(item,\s*edges,\s*resolved,\s*builder,\s*metadata\)\s*->\s*\{(.*)\}\s*\)\s*,?$", entry, re.S)
        if not m:
            print("PARSE FAIL at entry:\n", entry)
            sys.exit(1)
        type_name = m.group(1)
        handler_body = m.group(2)
        entries.append((simple(type_name), norm(handler_body)))
        i = k + 1
    return entries


def main():
    head = subprocess.run(
        ["git", "show", "HEAD:" + SRC.relative_to(ROOT).as_posix()],
        cwd=ROOT, capture_output=True, text=True,
    ).stdout
    head = head.replace("\r\n", "\n").replace("\r", "\n")
    cur = SRC.read_text(encoding="utf-8").replace("\r\n", "\n").replace("\r", "\n")

    orig = extract_original_branches(head)
    gen = extract_table_entries(cur)

    if len(orig) != len(gen):
        print(f"MISMATCH count: original {len(orig)} vs generated {len(gen)}")
        sys.exit(1)

    seen = set()
    dup = set()
    for t, _ in gen:
        if t in seen:
            dup.add(t)
        seen.add(t)

    ok = True
    for idx, (ob, gb) in enumerate(zip(orig, gen), 1):
        o_type, o_body, o_guarded = ob
        g_type, g_body = gb
        if o_type != g_type:
            print(f"MISMATCH type at {idx}: original {o_type} vs generated {g_type}")
            ok = False
            continue
        if o_guarded:
            print(f"UNEXPECTED guarded branch at {idx} ({o_type})")
            ok = False
            continue
        if g_body != norm(o_body):
            print(f"MISMATCH body at {idx} ({o_type}):\n"
                  f"  original ={norm(o_body)}\n  generated={g_body}")
            ok = False

    if dup:
        print("DUPLICATE types in generated table:", sorted(dup))
        ok = False

    if ok:
        print(f"FAITHFUL: all {len(orig)} branch bodies match verbatim (collectStandaloneEdges)")
        sys.exit(0)
    sys.exit(1)


if __name__ == "__main__":
    main()
