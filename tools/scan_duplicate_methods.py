#!/usr/bin/env python3
"""Scan Java sources for duplicate method bodies that live in different classes.

This is the *convergence* half of the refactor loop: before folding a chain or
hoisting a helper you need to know which implementations already exist twice.
A group of two or more same-named methods whose normalised bodies match is a
candidate for "keep one, delegate the rest".

Design notes
------------
* Bodies are compared after comment stripping, literal masking and whitespace
  collapsing, so formatting differences do not hide a duplicate. Literals keep
  their text: two copies that differ only in an exception message are *not* the
  same body, and reporting them as one would send you into a fold that silently
  changes observable behaviour.
* ``--fuzzy`` additionally erases ``com.minicad.<pkg>.`` qualifiers, which is
  how one copy spelling ``com.minicad.geometry2d.Direction2`` while its twin
  imports ``Direction2`` still lines up.
* ``--ignore-literals`` goes the other way and treats every literal as equal.
  That is the view that surfaces "same code, different message" twins, which are
  real convergence candidates but need the message threaded through as a
  parameter (or unified on purpose) instead of being folded blind.
* Trivial bodies (getters, one-line delegations, ``return null;``) are filtered
  by ``--min-lines``; those converge to nothing useful.
* Constructors, ``toString``/``hashCode``/``equals``, and methods named after an
  enclosing type are skipped -- they are structurally required to repeat.
* Identical text is not identical code. The scanner prints a risk marker on
  groups that need a type-resolution check before they can be delegated:

    - ``nested:X`` -- the shared body mentions ``X`` and one of the member files
      declares its own nested type called ``X``. Unqualified ``X`` then resolves
      to *different* classes in the two files, so the copies cannot simply be
      merged (this cost a real detour: MeshTriangulatorParametric declared a
      nested ``UvPoint`` and its pcurve samplers looked identical to
      PcurveSamplingHelper's, which speaks com.minicad.preview.payload.UvPoint).
      Hoisting the nested type into a shared home is the prerequisite, and it
      also has to be a type nothing else still refers to unqualified.
    - ``literals-differ`` (``--ignore-literals`` only) -- the members agree once
      literals are erased but not before, so they differ in a message, a format
      string or a key. The distinct literal sets are printed underneath.
    - ``cross-package`` -- the members live in different packages, so the
      unqualified names in the body may resolve through different imports. Only
      counted in the summary: most groups span packages, so inline it was noise.

Offsets
-------
``strip_java`` preserves the length of its input, so an index into the stripped
text is an index into the source. Callers that edit files (rather than just
count) rely on this to locate a method's braces; it is why a literal is masked
in place rather than deleted.

Usage
-----
    python tools/scan_duplicate_methods.py                     # whole main tree
    python tools/scan_duplicate_methods.py --fuzzy --min-lines 4
    python tools/scan_duplicate_methods.py --fuzzy --ignore-literals
    python tools/scan_duplicate_methods.py --root src/main/java/com/minicad/export
"""
from __future__ import annotations

import argparse
import os
import re
import sys
from collections import defaultdict

KEYWORDS = {
    "if", "for", "while", "switch", "catch", "try", "else", "do",
    "synchronized", "return", "new", "assert", "throw",
}
SKIP_NAMES = {"toString", "hashCode", "equals", "compareTo", "main"}

TYPE_DECL = re.compile(r"\b(?:class|interface|enum|record|@interface)\s+(\w+)")
METHOD_DECL = re.compile(r"(?<![\w.])(?P<name>\w+)\s*\((?P<args>[^()]*)\)\s*$")
QUALIFIER = re.compile(r"\bcom\.minicad(?:\.[a-z0-9_]+)*\.")
LITERAL = re.compile(r"\"[^\"\n]*\"|'[^'\n]*'")

# Applied to the *interior* of a literal so it survives the comparison but stops
# being a literal as far as the structural scan is concerned: one character in,
# one character out, so every offset downstream still lines up with the source.
LITERAL_MASK = str.maketrans({
    "{": "(", "}": ")", ";": ",", "\\": "/", '"': "'", "'": "`", "\n": " ",
})


def strip_java(src: str) -> str:
    """Blank out comments and mask literals, preserving every offset.

    Comments become spaces. Literals keep their text -- two method bodies that
    differ only in an exception message must not come out equal -- but the
    characters this scanner gives structural meaning to (braces, semicolons,
    quotes, newlines) are swapped for harmless ones in place.
    """
    out: list[str] = []
    i, n = 0, len(src)
    while i < n:
        c = src[i]
        if c == "/" and i + 1 < n and src[i + 1] == "/":
            j = src.find("\n", i)
            j = n if j == -1 else j
            out.append(" " * (j - i))
            i = j
        elif c == "/" and i + 1 < n and src[i + 1] == "*":
            j = src.find("*/", i + 2)
            j = n if j == -1 else j + 2
            out.append("".join(ch if ch == "\n" else " " for ch in src[i:j]))
            i = j
        elif src.startswith('"""', i):
            j = src.find('"""', i + 3)
            j = n if j == -1 else j + 3
            out.append(src[i:j].translate(LITERAL_MASK))
            i = j
        elif c in "\"'":
            j = i + 1
            while j < n:
                if src[j] == "\\":
                    j += 2
                    continue
                if src[j] == c:
                    j += 1
                    break
                if src[j] == "\n":
                    break
                j += 1
            raw = src[i:j]
            if len(raw) >= 2 and raw.endswith(c):
                out.append(c + raw[1:-1].translate(LITERAL_MASK) + c)
            else:
                out.append(c + raw[1:].translate(LITERAL_MASK))
            i = j
        else:
            out.append(c)
            i += 1
    return "".join(out)


def classify(decl: str) -> tuple[str, str]:
    """Return (kind, name) for the text preceding an opening brace."""
    head = decl.lstrip()
    first = head.split(None, 1)[0] if head else ""
    if TYPE_DECL.search(decl):
        m = TYPE_DECL.search(decl)
        return "type", m.group(1)
    if not head or first in KEYWORDS or decl.rstrip().endswith("->") or " new " in f" {head}":
        return "other", ""
    m = METHOD_DECL.search(decl)
    if not m:
        return "other", ""
    return "method", m.group("name")


def extract(path: str) -> tuple[list[dict], set[str], str]:
    """Extract the methods, the nested type names and the package of `path`.

    The nested type names and package are what let ``collect`` tell "the same
    text" apart from "the same code": a nested type shadows an import, so an
    unqualified name in one member's body can resolve to a class the other
    member has never heard of.

    Every index returned here (``brace``) is an index into the source as well as
    into the stripped text, because ``strip_java`` preserves length. Callers that
    rewrite a body in place depend on that.
    """
    src = open(path, encoding="utf-8").read()
    package_match = re.search(r"(?m)^package\s+([\w.]+)\s*;", src)
    package = package_match.group(1) if package_match else ""
    s = strip_java(src)
    n = len(s)
    stack: list[dict] = []
    classes: list[str] = []
    nested: set[str] = set()
    pending: list[dict] = []
    methods: list[dict] = []
    boundary = 0
    i = 0
    while i < n:
        ch = s[i]
        if ch == "{":
            decl = re.sub(r"\s+", " ", s[boundary:i]).strip()
            kind, name = classify(decl)
            if kind == "type" and classes:
                nested.add(name)
            stack.append({"kind": kind, "name": name, "brace": i})
            if kind == "type":
                classes.append(name)
            elif kind == "method":
                pending.append({"name": name, "decl": decl, "brace": i,
                                "classes": list(classes)})
            boundary = i + 1
        elif ch == "}":
            if stack:
                top = stack.pop()
                if top["kind"] == "type":
                    if classes:
                        classes.pop()
                elif top["kind"] == "method" and pending:
                    entry = pending.pop()
                    entry["body"] = s[entry["brace"] + 1:i]
                    methods.append(entry)
            boundary = i + 1
        elif ch == ";":
            boundary = i + 1
        i += 1
    return methods, nested, package


def normalise(body: str, fuzzy: bool, ignore_literals: bool = False) -> str:
    text = body
    if ignore_literals:
        text = LITERAL.sub('""', text)
    text = re.sub(r"\s+", " ", text).strip()
    if fuzzy:
        text = QUALIFIER.sub("", text)
    return text


def risk_flags(body: str, members: list[dict], ignore_literals: bool = False) -> list[str]:
    """Heuristics for groups whose identical text may still be different code."""
    flags: list[str] = []
    nested = sorted({name for m in members for name in m["nested"]})
    shadowed = [name for name in nested
                if re.search(r"\b" + re.escape(name) + r"\b", body)]
    if shadowed:
        flags.append("nested:" + ",".join(shadowed))
    if ignore_literals and len({m["literals"] for m in members}) > 1:
        flags.append("literals-differ")
    if len({m["package"] for m in members}) > 1:
        flags.append("cross-package")
    return flags


def collect(root: str, min_lines: int, fuzzy: bool, ignore_literals: bool = False):
    groups: dict[tuple[str, str], list[dict]] = defaultdict(list)
    for dirpath, _dirnames, filenames in os.walk(root):
        for fn in filenames:
            if not fn.endswith(".java"):
                continue
            path = os.path.join(dirpath, fn)
            methods, nested, package = extract(path)
            for m in methods:
                body = m.get("body")
                if body is None:
                    continue
                if len([ln for ln in body.split("\n") if ln.strip()]) < min_lines:
                    continue
                if m["name"] in SKIP_NAMES:
                    continue
                owner = m["classes"][-1] if m["classes"] else "?"
                if m["name"] == owner:
                    continue
                key = (m["name"], normalise(body, fuzzy, ignore_literals))
                groups[key].append({
                    "file": path.replace("\\", "/"),
                    "owner": owner,
                    "classes": m["classes"],
                    "decl": m["decl"],
                    "lines": len([ln for ln in body.split("\n") if ln.strip()]),
                    "nested": nested,
                    "literals": tuple(sorted(LITERAL.findall(body))),
                    "package": package,
                })
    out = []
    for (name, body), members in groups.items():
        owners = {(m["owner"], m["file"]) for m in members}
        if len(owners) < 2:
            continue
        out.append((name, members, risk_flags(body, members, ignore_literals)))
    out.sort(key=lambda kv: (-max(m["lines"] for m in kv[1]), kv[0]))
    return out


def main() -> int:
    ap = argparse.ArgumentParser()
    ap.add_argument("--root", default="src/main/java")
    ap.add_argument("--min-lines", type=int, default=5,
                    help="minimum non-blank body lines to be reported")
    ap.add_argument("--fuzzy", action="store_true",
                    help="ignore com.minicad.* package qualifiers")
    ap.add_argument("--ignore-literals", action="store_true",
                    help="treat every literal as equal, surfacing the "
                         "'same code, different message' twins")
    ap.add_argument("--top", type=int, default=40)
    args = ap.parse_args()

    groups = collect(args.root, args.min_lines, args.fuzzy, args.ignore_literals)
    total_saved = 0
    for name, members, flags in groups[:args.top]:
        owners = sorted({(m["owner"], m["file"]) for m in members})
        lines_each = max(m["lines"] for m in members)
        total_saved += lines_each * (len(owners) - 1)
        warnings = [flag for flag in flags
                    if flag.startswith("nested:") or flag == "literals-differ"]
        marker = f"  [!] {' '.join(warnings)}" if warnings else ""
        print(f"\n### {name}  x{len(owners)}  (~{lines_each} lines each){marker}")
        for m in members:
            print(f"    {m['file']}:{m['owner']}  <- {m['decl'][:90]}")
        if "literals-differ" in flags:
            by_literals = defaultdict(list)
            for m in members:
                by_literals[m["literals"]].append(m["owner"])
            for literals, who in sorted(by_literals.items()):
                shown = " ".join(literals) if literals else ("<none>",)
                print(f"      literals {shown}  <- {', '.join(sorted(set(who)))}")
    nested = [g for g in groups if any(f.startswith("nested:") for f in g[2])]
    literal_diff = [g for g in groups if "literals-differ" in g[2]]
    cross = sum(1 for g in groups if "cross-package" in g[2])
    print(f"\n{len(groups)} duplicate group(s); ~{total_saved} lines reclaimable "
          f"if each group keeps one copy.")
    if nested:
        print(f"{len(nested)} group(s) marked [!] nested: one member declares a nested "
              f"type that shadows a name in the shared body, so the two copies may not "
              f"even be the same code. Check the types before delegating.")
    if literal_diff:
        print(f"{len(literal_diff)} group(s) marked [!] literals-differ: identical once "
              f"literals are erased, so they differ in a message -- thread it through as "
              f"a parameter instead of folding blind, or unify it on purpose.")
    if cross:
        print(f"{cross} group(s) span packages: their unqualified names may resolve "
              f"through different imports. Normal for a cross-layer helper, worth a "
              f"glance before merging.")
    return 0


if __name__ == "__main__":
    sys.exit(main())
