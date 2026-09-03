#!/usr/bin/env python3
"""Scan the repo for the longest sequential `if (... instanceof ...)` dispatch chains.

For every .java file under src/main/java, find maximal *runs of consecutive top-level siblings*
that all look like `if (<cond-with-instanceof>) {`. A "top-level sibling" means the header sits at
the same brace depth inside a method body, i.e. the chain is a plain sequential dispatch that can
be folded into an ordered table.

Reports, per run: file:line, number of branches, and how many of them are "guarded"
(a condition with `&&` / `||` beyond a bare instanceof, which usually needs a Predicate or an
OR-split rather than a plain type rule). Sorted by branch count, descending.

Usage:
    python tools/scan_instanceof_chains.py [--min N] [--clean]

  --min N     only show runs with at least N branches (default 8)
  --clean     only show runs with zero guarded branches (i.e. trivially foldable)

NOTE (hard-won): the header matcher must accept *any* dispatch variable. An earlier version only
matched `if (item instanceof ...)` and therefore silently missed every chain dispatched on a
different variable (e.g. `entity instanceof`), which skewed the whole ranking for several rounds.
"""
import argparse
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
SRC = ROOT / "src/main/java"

# `if (` ... `) {` at end of line, capturing the condition.
HEADER_RE = re.compile(r"if \((.*)\)\s*\{$")


def count_braces(line):
    """Net brace delta of a line, ignoring braces inside string/char literals."""
    depth = 0
    in_str = in_ch = False
    for ch in line:
        if in_str:
            if ch == '"':
                in_str = False
            continue
        if in_ch:
            if ch == "'":
                in_ch = False
            continue
        if ch == '"':
            in_str = True
        elif ch == "'":
            in_ch = True
        elif ch == '{':
            depth += 1
        elif ch == '}':
            depth -= 1
    return depth


def is_dispatch_header(stripped):
    """True if the line is a top-level `if (<cond containing instanceof>) {` header."""
    if not stripped.startswith("if ("):
        return False
    m = HEADER_RE.search(stripped)
    return bool(m) and "instanceof" in m.group(1) and stripped.endswith("{")


def is_guarded(stripped):
    """A branch whose condition is more than a bare `x instanceof T`."""
    m = HEADER_RE.search(stripped)
    if not m:
        return False
    cond = m.group(1)
    return "&&" in cond or "||" in cond


def scan_file(path):
    """Return list of (start_lineno, n_branches, n_guarded) for each maximal run.

    Strict adjacency: a run is a sequence of sibling `if (... instanceof ...) {` headers where
    each branch's body is immediately followed by the next header (blank lines allowed, nothing
    else). This is exactly the shape a table-driven fold requires -- if any other statement sits
    between two branches, they are NOT one chain and must not be merged (an earlier, looser
    "same brace depth" test merged unrelated branches and produced absurd run lengths).
    """
    text = path.read_text(encoding="utf-8", errors="replace")
    lines = text.replace("\r\n", "\n").replace("\r", "\n").split("\n")
    n = len(lines)

    runs = []
    i = 0
    depth = 0
    while i < n:
        if is_dispatch_header(lines[i].strip()):
            run_depth = depth
            run = []
            while i < n:
                stripped = lines[i].strip()
                if not is_dispatch_header(stripped) or depth != run_depth:
                    break
                run.append((i, is_guarded(stripped)))
                depth += count_braces(lines[i])
                i += 1
                # consume this branch's body: stop as soon as depth returns to run_depth
                while i < n and depth > run_depth:
                    depth += count_braces(lines[i])
                    i += 1
                while i < n and lines[i].strip() == "":
                    i += 1
            if len(run) >= 2:
                runs.append(run)
            continue
        depth += count_braces(lines[i])
        i += 1

    out = []
    for run in runs:
        guarded = sum(1 for _, g in run if g)
        out.append((run[0][0] + 1, len(run), guarded))
    return out


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--min", type=int, default=8)
    ap.add_argument("--clean", action="store_true", help="only runs with zero guarded branches")
    args = ap.parse_args()

    results = []
    for path in sorted(SRC.rglob("*.java")):
        try:
            for start, n, guarded in scan_file(path):
                if n >= args.min and (not args.clean or guarded == 0):
                    results.append((n, guarded, path, start))
        except Exception as exc:  # noqa: BLE001 - a scan should never die on one file
            print(f"WARN: {path}: {exc}", file=sys.stderr)

    results.sort(key=lambda r: (-r[0], r[1]))
    print(f"{'branches':>8} {'guarded':>7}  location")
    print("-" * 72)
    for n, guarded, path, start in results:
        rel = path.relative_to(ROOT).as_posix()
        print(f"{n:>8} {guarded:>7}  {rel}:{start}")
    print(f"\n{len(results)} run(s) with >= {args.min} branches"
          + (" (clean only)" if args.clean else ""))


if __name__ == "__main__":
    main()
