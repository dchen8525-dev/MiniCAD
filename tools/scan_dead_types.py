#!/usr/bin/env python3
"""Find type declarations that nothing references.

``scan_duplicate_methods.py`` compares method bodies and ``scan_dead_methods.py``
counts how often a method name is mentioned. Neither can see the shape that cost
a real round here: a class declared twice, once as a top-level type nothing used
and once as a nested copy inside another class that every caller used. Both
scanners group by name, and a shared name resolves to two *different* types, so
the mentions of the dead copy's name look like ordinary references to something
live -- and the dead copy is a class, not a method, so the dead-method view never
considers it at all.

The rule
--------
A declaration of ``N`` in file ``F`` is reported when both hold:

  * ``F`` itself mentions ``N`` no more often than it declares ``N`` (so the type
    is not used inside its own file), and
  * every *other* file that mentions ``N`` is itself a file that declares an
    ``N`` -- a co-declarer, whose mentions are about its own type rather than
    about this one.

The second condition is what makes this conservative. Any other mention counts as
a live reference, including a same-package use, a fully qualified use, an import,
and a nested use such as ``Outer.Inner`` seen from another package -- only the
co-declarer case is filtered out, because that is the case that hides a dead
type. The view therefore under-reports, which is the direction a deletion tool
has to err in.

Entry points are skipped: a file holding a ``static void main`` is invoked from a
script or a shell, not from Java, so nothing in the tree mentions it by name.

Usage
-----
    python tools/scan_dead_types.py                        # whole main tree
    python tools/scan_dead_types.py --scope src/main/java/com/minicad/preview
    python tools/scan_dead_types.py --universe src/main/java src/test/java
"""
from __future__ import annotations

import argparse
import re
import sys
from collections import Counter, defaultdict
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent

IDENT = re.compile(r"[A-Za-z_$][\w$]*")
PACKAGE = re.compile(r"^package\s+([\w.]+)\s*;", re.M)
MAIN = re.compile(r"\bstatic\s+(?:final\s+)?void\s+main\s*\(")
TYPE_DECL = re.compile(
    r"\b(?:public\s+|protected\s+|private\s+|static\s+|final\s+|abstract\s+|sealed\s+|non-sealed\s+|strictfp\s+)*"
    r"(class|interface|enum|record)\s+([A-Z][\w$]*)"
)


def read(path: Path) -> str:
    return path.read_text(encoding="utf-8", errors="replace")


def java_files(root: Path):
    return sorted(root.rglob("*.java"))


def unix(path) -> str:
    return str(path).replace("\\", "/")


def display(path: Path) -> str:
    """Repo-relative when the path is inside the repo, absolute otherwise."""
    try:
        return unix(path.relative_to(ROOT))
    except ValueError:
        return unix(path)


def scan(universe):
    """Per file: package, identifier counter, and the type declarations it holds."""
    files = {}
    for f in universe:
        text = read(f)
        match = PACKAGE.search(text)
        files[f] = {
            "package": match.group(1) if match else "",
            "names": Counter(m.group() for m in IDENT.finditer(text)),
            "declared": [m.group(2) for m in TYPE_DECL.finditer(text)],
            "entry_point": bool(MAIN.search(text)),
            "text": text,
        }
    return files


def main() -> int:
    ap = argparse.ArgumentParser()
    ap.add_argument("--scope", default="src/main/java",
                    help="directory whose declarations are judged (default: the whole main tree)")
    ap.add_argument("--universe", nargs="+", default=["src/main/java", "src/test/java"],
                    help="directories searched for references")
    args = ap.parse_args()

    universe = [f for d in args.universe for f in java_files(ROOT / d)]
    files = scan(universe)

    declarations = []
    for f in java_files(ROOT / args.scope):
        info = files[f]
        if info["entry_point"]:
            continue
        for m in TYPE_DECL.finditer(info["text"]):
            declarations.append((m.group(2), m.group(1), f,
                                 info["text"][:m.start()].count("\n") + 1))

    rows = []
    for name, kind, f, line in declarations:
        info = files[f]
        same_file_decls = sum(1 for d in info["declared"] if d == name)
        if info["names"][name] > same_file_decls:
            continue  # used inside its own file
        for other in universe:
            if other == f:
                continue
            other_info = files[other]
            if other_info["names"][name] == 0:
                continue
            if name in other_info["declared"]:
                continue  # a co-declarer, not a reference
            break
        else:
            rows.append((name, kind, f, line, info["package"]))

    rows.sort(key=lambda r: (r[4], unix(r[2]), r[3]))
    print(f"{len(rows)} unreferenced type declaration(s) under {args.scope}")
    for name, kind, f, line, package in rows:
        print(f"    {kind:9s} {display(f)}:{line}  {name}   [{package}]")

    counts = Counter(package for _n, _k, _f, _l, package in rows)
    if counts:
        print("\nby package:")
        for package, count in counts.most_common():
            print(f"    {count:4d}  {package}")
    print(f"\nscanned {len(declarations)} declaration(s) in {args.scope} "
          f"against {len(universe)} source files")
    return 0


if __name__ == "__main__":
    sys.exit(main())
