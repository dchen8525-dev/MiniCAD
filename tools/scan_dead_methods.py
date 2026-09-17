#!/usr/bin/env python3
"""Find methods that no longer have a caller, and duplicate declarations of a
single name that were left half-converged.

``scan_duplicate_methods.py`` answers "are these two bodies the same?". This
tool answers the question that comes *before* it: "is anyone still calling
this?" -- the state a convergence pass leaves behind when it deletes the caller
but forgets the callee.

Two views, both built from one cheap pass:

  --dead   names whose total occurrence count over the whole tree equals the
           number of declarations in the scanned package. Occurrences are
           counted on raw text, comments and strings included, so a count can
           only be inflated -- the view under-reports and never calls a live
           method dead. Safe to delete, but still read the body: a name can be
           spelled once in a comment and look dead for the wrong reason.

  --dupes  names declared more than once in the package. This is the view that
           catches a half-converged pair: the pass rewrote one copy into a
           one-line delegation and left the other as a full body. Neither copy
           has a "zero references" signature, so --dead is blind to it. The
           output lists every declaration site (file:line) and every reference
           line, which is what tells you *which* copy the call sites target.

Line numbers come from ``extract``'s ``brace`` index, which points into the
length-preserving stripped text, so it doubles as an index into the source.
"""
import argparse
import bisect
import re
import sys
from collections import Counter, defaultdict
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
sys.path.insert(0, str(ROOT / "tools"))
from scan_duplicate_methods import extract  # noqa: E402

IDENT = re.compile(r"[A-Za-z_$][\w$]*")


def line_index(text: str):
    """Return offsets of line starts plus a helper turning an offset into 1-based lines."""
    starts = [0]
    for i, ch in enumerate(text):
        if ch == "\n":
            starts.append(i + 1)
    return starts


def line_of(starts, offset: int) -> int:
    return bisect.bisect_right(starts, offset)


def scan(java_files):
    """Global identifier counts and, per file, identifier occurrences with lines."""
    counts = Counter()
    occurrences = []
    for f in java_files:
        text = f.read_text(encoding="utf-8", errors="replace")
        starts = line_index(text)
        for m in IDENT.finditer(text):
            counts[m.group()] += 1
            occurrences.append((m.group(), f, line_of(starts, m.start()), text))
    return counts, occurrences


def main() -> int:
    ap = argparse.ArgumentParser()
    ap.add_argument("--scope", default="src/main/java/com/minicad/step/semantic")
    ap.add_argument("--view", choices=("dead", "dupes", "both"), default="both")
    ap.add_argument("--min-lines", type=int, default=3,
                    help="ignore declarations spanning fewer than this many source lines")
    ap.add_argument("--max-refs", type=int, default=6,
                    help="in --dupes, how many reference lines to print per name")
    args = ap.parse_args()

    src = ROOT / "src"
    all_java = sorted(
        list((src / "main" / "java").rglob("*.java"))
        + list((src / "test" / "java").rglob("*.java"))
    )
    counts, occurrences = scan(all_java)

    scope = ROOT / args.scope
    declared = defaultdict(list)  # name -> [(relpath, decl_line, end_line, decl_text)]
    for f in sorted(scope.rglob("*.java")):
        methods, _nested, _pkg = extract(str(f))
        rel = f.relative_to(ROOT).as_posix()
        text = f.read_text(encoding="utf-8")
        starts = line_index(text)
        total_lines = text.count("\n") + 1
        for m in methods:
            start = line_of(starts, m["brace"])
            end = line_of(starts, min(m["brace"] + 1 + len(m.get("body", "")), len(text) - 1))
            if end - start + 1 < args.min_lines:
                continue
            declared[m["name"]].append((rel, start, min(end, total_lines), m["decl"]))

    by_name = defaultdict(list)
    for name, f, ln, text in occurrences:
        by_name[name].append((f, ln, text))

    shown = 0
    if args.view in ("dead", "both"):
        rows = [
            (name, hosts, counts[name])
            for name, hosts in declared.items()
            if counts[name] <= len(hosts)
        ]
        rows.sort(key=lambda r: (r[1][0][0], r[1][0][1]))
        print(f"== dead ({len(rows)} names: zero call sites in the tree) ==")
        for name, hosts, total in rows:
            print(f"{name}  ({total} refs = {len(hosts)} decls)")
            for rel, a, b, decl in hosts:
                print(f"    {rel}:{a}-{b}  {decl[:120]}")
        print()
        shown += len(rows)

    if args.view in ("dupes", "both"):
        rows = [(n, h) for n, h in declared.items() if len(h) > 1]
        rows.sort(key=lambda r: (r[1][0][0], r[1][0][1]))
        print(f"== duplicate declarations ({len(rows)} names) ==")
        for name, hosts in rows:
            print(f"{name}  ({counts[name]} refs / {len(hosts)} decls)")
            for rel, a, b, decl in hosts:
                print(f"    decl {rel}:{a}-{b}  {decl[:110]}")
            sites = by_name.get(name, [])
            for f, ln, text in sites[: args.max_refs]:
                stripped = text.strip()
                if len(stripped) > 130:
                    stripped = stripped[:130] + "..."
                print(f"    ref  {f.relative_to(ROOT).as_posix()}:{ln}  {stripped}")
            if len(sites) > args.max_refs:
                print(f"    ... {len(sites) - args.max_refs} more references")
        print()
        shown += len(rows)

    print(f"scanned {args.scope} ({sum(len(v) for v in declared.values())} declarations) "
          f"and {len(all_java)} source files; {shown} rows")
    return 0


if __name__ == "__main__":
    sys.exit(main())
