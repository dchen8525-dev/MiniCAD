#!/usr/bin/env python3
"""Report private members whose name occurs nowhere outside their own declaration.

This is the fifth view of duplication in this repo. The first four are blind to
the shape it catches:

* ``scan_duplicate_methods.py`` groups by *body text*, so it skips a copy that
  drifted by a line;
* ``scan_instanceof_chains.py`` looks for long chains, not members;
* ``scan_dead_types.py`` needs every member of a type to be dead;
* ``scan_dead_members.py`` counts *mentions of a name in the whole tree* and
  therefore reads a dead member as live whenever some **other** file declares
  the same name.

That last blind spot is the one this script removes, and it is the expensive
one. When a family is *extracted* rather than *moved* -- the new class takes a
private copy of five helpers and the old class keeps its originals -- every name
in the dead half also appears in the live half. ``scan_dead_members.py`` sees two
files mentioning the name and reports nothing; ``scan_dead_types.py`` sees each
class still has live members and reports nothing. The leftovers are invisible to
all four tools and survive every sweep.

The rule here is deliberately narrow and needs no call graph:

    a ``private`` member is dead when its name occurs exactly once *inside its
    own file* -- that one occurrence being its own declaration.

Privacy is what makes the rule sound: nothing outside the file can name the
member, so an occurrence count of one inside the file means no caller anywhere,
tests included. Counting **per file** rather than per tree is the whole point:
it is what makes the script immune to the twin that gave every other tool a
false "live".

Each seed is printed with the files that mention the *same* name, because in
this codebase that is almost always where the live twin is, and finding it is
the whole point of the round.

Caveats, stated rather than handled:

* reflection by name (``getDeclaredMethod``) is a use this cannot see. Run
  ``grep -rn getDeclaredMethod src/test`` before deleting a seed.
* a name that is also a type name or a local variable is not a use of the
  member, but it is counted as one here -- so this script under-reports, never
  over-reports.
* **a dead copy whose own file still spells the name for a different receiver
  is invisible here**, and this is the one shape the rule genuinely cannot
  reach. Mutation test, 2026-09-19: re-planting a private
  ``StepCadBuilder.perpendicularDirection(Direction3)`` -- dead, because the
  single call in that file reads ``geometryBuilder.perpendicularDirection(zAxis)``
  -- left the per-file count at two and this script stayed silent. The count sees
  a *name*, not a *declaration plus the callers of that declaration*. The
  mitigation for this shape is the other half of the rule: a signature-anchored
  guard that pins the declaration count in the file that should own it and zero
  elsewhere (``StepCadBuilderDeadOrphansTest.perpendicularDirectionHasOneHome``).

Usage::

    python tools/scan_dead_private_members.py                  # whole tree
    python tools/scan_dead_private_members.py --path step/semantic
    python tools/scan_dead_private_members.py --quiet
"""

from __future__ import annotations

import argparse
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
SRC = ROOT / "src"

# `private` member declarations only. Locals can never match: the modifier is
# not allowed on them. Fields, methods and nested types are all covered.
MEMBER = re.compile(
    r"^[ \t]+private[ \t]+"
    r"(?:(?:static|final|transient|volatile|abstract|synchronized|native|strictfp)[ \t]+)*"
    r"(?:[\w$<>,\[\]\.\?]+[ \t]+)*?"
    r"([\w$<>,\[\]\.\?]+)[ \t]+"  # return type
    r"(\w+)[ \t]*"  # member name
    r"(\(|;|=)",
    re.MULTILINE,
)

# Names that are dead but must stay: required by the language/serialization.
ALLOWED_DEAD = {"serialVersionUID"}


def strip_comments(text: str) -> str:
    """Blank out comments and string/char literals, keeping offsets stable.

    A name inside a javadoc is not a use, and this repo's javadoc names members
    constantly; a name inside a string literal *is* a possible reflection use,
    so string literals are kept. Comments are replaced by spaces so that line
    numbers survive.
    """
    out = list(text)
    i, n = 0, len(text)
    while i < n:
        c = text[i]
        if c == "/" and i + 1 < n and text[i + 1] == "/":
            j = text.find("\n", i)
            j = n if j < 0 else j
            for k in range(i, j):
                out[k] = " "
            i = j
        elif c == "/" and i + 1 < n and text[i + 1] == "*":
            j = text.find("*/", i + 2)
            j = n if j < 0 else j + 2
            for k in range(i, j):
                if text[k] != "\n":
                    out[k] = " "
            i = j
        elif c == '"':
            # keep the literal's text (a possible reflection name); blank only
            # the quotes' neighbours are irrelevant, so skip past it
            i += 1
            while i < n and text[i] != '"':
                i += 2 if text[i] == "\\" else 1
            i += 1
        elif c == "'":
            i += 1
            while i < n and text[i] != "'":
                i += 2 if text[i] == "\\" else 1
            i += 1
        else:
            i += 1
    return "".join(out)


def java_sources(include_tests: bool) -> list[Path]:
    roots = [SRC / "main" / "java"]
    if include_tests:
        roots.append(SRC / "test" / "java")
    return sorted(p for root in roots if root.exists() for p in root.rglob("*.java") if p.is_file())


def count_names(bodies: dict[Path, str]) -> dict[tuple[Path, int], tuple[str, str, str]]:
    """Return every private declaration keyed by (file, line)."""
    decls: dict[tuple[Path, int], tuple[str, str, str]] = {}
    for path, code in bodies.items():
        raw = path.read_text(encoding="utf-8", errors="replace")
        for m in MEMBER.finditer(code):
            name = m.group(2)
            if name in ALLOWED_DEAD:
                continue
            if code[: m.start(2)].count("\n") == 0:
                continue  # a member of the top-level type is always indented
            line = raw[: m.start(2)].count("\n") + 1
            kind = "method" if m.group(3) == "(" else "field"
            decls[(path, line)] = (name, kind, m.group(1))
    return decls


def occurrence_counts(
    bodies: dict[Path, str], names: set[str]
) -> tuple[dict[Path, dict[str, int]], dict[str, list[Path]]]:
    """One pass over the tree: per-file per-name totals, and who mentions each.

    A single alternation is used rather than one regex per name, because the
    naive version is quadratic in the number of private members and this tree
    has several thousand.
    """
    per_file: dict[Path, dict[str, int]] = {}
    files: dict[str, list[Path]] = {name: [] for name in names}
    if not names:
        return per_file, files
    combined = re.compile(r"\b(" + "|".join(re.escape(n) for n in sorted(names)) + r")\b")
    for path, code in bodies.items():
        counts = per_file.setdefault(path, {})
        seen: set[str] = set()
        for hit in combined.findall(code):
            counts[hit] = counts.get(hit, 0) + 1
            seen.add(hit)
        for hit in seen:
            files[hit].append(path)
    return per_file, files


def main() -> int:
    ap = argparse.ArgumentParser(description=__doc__)
    ap.add_argument("--path", default="", help="only report members under this path fragment")
    ap.add_argument("--quiet", action="store_true", help="one line per member")
    ap.add_argument(
        "--include-tests",
        action="store_true",
        help="also scan src/test/java (unused private test helpers are not usually worth a round)",
    )
    args = ap.parse_args()

    sources = java_sources(args.include_tests)
    bodies = {
        path: strip_comments(path.read_text(encoding="utf-8", errors="replace"))
        for path in sources
    }
    decls = count_names(bodies)
    per_file, mentioning = occurrence_counts(bodies, {name for name, _, _ in decls.values()})

    def displayed(path: Path) -> str:
        return str(path.relative_to(ROOT)).replace("\\", "/")

    # Where else does each name live? For a dead seed this is usually the live
    # twin's home -- the fact the other four scanners cannot show. These
    # mentions are *not* uses: the member is private, so no other file can name
    # it; the list exists to point at the twin.
    homes = {
        name: [displayed(path) for path in paths] for name, paths in mentioning.items()
    }

    seeds = []
    for (path, line), (name, kind, _declared_type) in decls.items():
        if per_file.get(path, {}).get(name, 0) != 1:
            continue
        shown = displayed(path)
        if args.path and args.path not in shown:
            continue
        seeds.append((shown, line, kind, name))

    seeds.sort()
    by_file: dict[str, list[tuple[int, str, str]]] = {}
    for shown, line, kind, name in seeds:
        by_file.setdefault(shown, []).append((line, kind, name))

    for shown, entries in by_file.items():
        print(f"{shown}")
        for line, kind, name in entries:
            print(f"    line {line:<5} {kind:<6} {name}")
            if not args.quiet:
                for twin in [h for h in homes.get(name, []) if h != shown]:
                    print(f"             same name also mentioned in {twin}")
        print()

    print(f"{len(seeds)} private member(s) whose name occurs nowhere but its own declaration.")
    print(
        "Every one is a seed, not a verdict: check src/test for getDeclaredMethod before "
        "deleting, and find the capability's live home first."
    )
    return 0

if __name__ == "__main__":
    sys.exit(main())
