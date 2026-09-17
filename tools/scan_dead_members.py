#!/usr/bin/env python3
"""Report production-dead members, as a closure rather than a list.

This is the fourth view of duplication in this repo, next to
``scan_duplicate_methods.py`` (textually identical bodies),
``scan_instanceof_chains.py`` (long dispatch chains) and
``scan_dead_types.py`` (types nothing else names). Those three skip a shape
that has produced the largest deletions so far: a *family* of members where
each one's only remaining caller is another member of the family, and the
last of them has no caller at all.

The signal is reference counting, not call-graph resolution -- a member is
not dead because its callers are unreachable, it is dead because it has
none. Counting is deliberately conservative:

* every mention of the name counts -- in code, in comments and in string
  literals, in every file of the tree -- so reflection by name and javadoc
  are treated as uses;
* a mention inside the member's own body does not count;
* the member's own declaration line does not count;
* a name with more than one declaration in the same file (an overload set)
  is skipped, because the name alone cannot partition it.

A member with nothing else left is a *seed*. Blanking a seed's body can
only remove references, so the seeds are recomputed on the blanked corpus
and the process repeats until it stops growing. Everything reported is
therefore reachable only through already-dead members: the closure is
sound, and its seeds are where a round starts.

Test references are counted separately rather than treated as uses. A
member whose only callers are tests is dead code with a test on top, and
this repo's convergence rounds repoint the test -- ``PreviewPipelineTest``
drives ``StepFacePayloadBuilder`` today because its preview-side twin went.

Usage::

    python tools/scan_dead_members.py                  # whole tree
    python tools/scan_dead_members.py --path preview   # one directory
    python tools/scan_dead_members.py --quiet          # no wave headers
"""

from __future__ import annotations

import argparse
import collections
import pathlib
import re
import sys

NL = "\n"
MAIN_ROOT = pathlib.Path("src/main/java")
TEST_ROOT = pathlib.Path("src/test/java")

DECL = re.compile(
    r"(?m)^[ \t]*(?P<annos>(?:@[\w\.]+(?:\([^)]*\))?[ \t]*\r?\n[ \t]*)*)"
    r"(?P<mods>(?:public|protected|private|static|final|abstract|synchronized|native|default|strictfp)[ \t]+)*"
    r"[\w<>\[\],\.\? \t]+[ \t]+(?P<name>\w+)[ \t]*\((?P<params>[^;{)]*)\)[ \t\r\n]*"
    r"(?:throws [\w\.,\t ]+)?\{"
)
IDENT = re.compile(r"[A-Za-z_]\w*")
KEYWORDS = {
    "if", "for", "while", "switch", "catch", "return", "new",
    "synchronized", "try", "do", "else", "throw",
}


def read(path: pathlib.Path) -> str:
    return path.read_bytes().decode("utf-8", errors="replace").replace("\r\n", NL)


def members_of(text: str) -> list[dict]:
    """Class-level members with their body spans, in declaration order."""
    out = []
    for m in DECL.finditer(text):
        name = m.group("name")
        if name in KEYWORDS:
            continue
        depth = 0
        i = text.index("{", m.end() - 1)
        j = i
        while j < len(text):
            if text[j] == "{":
                depth += 1
            elif text[j] == "}":
                depth -= 1
                if depth == 0:
                    break
            j += 1
        mods = m.group("mods") or ""
        out.append({
            "name": name,
            "decl_line_start": text.rfind(NL, 0, m.start()) + 1,
            "decl_line_end": text.find(NL, m.start()),
            "body_start": i,
            "body_end": j,
            "line": text.count(NL, 0, m.start()) + 1,
            "public": "public" in mods,
            "private": "private" in mods,
            "static": "static" in mods,
        })
    return out


def word_count(text: str, name: str) -> int:
    return len(re.findall(r"(?<![\w.])" + re.escape(name) + r"(?![\w])", text))


def blank(text: str, spans: list[tuple[int, int]]) -> str:
    chars = list(text)
    for start, end in spans:
        for i in range(start, min(end + 1, len(chars))):
            if chars[i] != NL:
                chars[i] = " "
    return "".join(chars)


def locations(text: str, name: str) -> list[int]:
    return [text.count(NL, 0, m.start()) + 1
            for m in re.finditer(r"(?<![\w.])" + re.escape(name) + r"(?![\w])", text)]


def main() -> int:
    ap = argparse.ArgumentParser(description=__doc__)
    ap.add_argument("--path", default="", help="only report members under this path fragment")
    ap.add_argument("--all", action="store_true",
                    help="print every wave, not just the seeds (wave 1)")
    args = ap.parse_args()

    corpus = {p: read(p) for p in list(MAIN_ROOT.rglob("*.java")) + list(TEST_ROOT.rglob("*.java"))}
    main_files = [p for p in corpus if str(p).startswith(str(MAIN_ROOT))]
    test_files = [p for p in corpus if str(p).startswith(str(TEST_ROOT))]
    parsed = {p: members_of(corpus[p]) for p in main_files}

    dead: list[tuple] = []
    dead_ids: set[int] = set()
    wave = 1
    while True:
        main_counts = {p: collections.Counter(IDENT.findall(corpus[p])) for p in main_files}
        test_counts = collections.Counter()
        for p in test_files:
            test_counts.update(IDENT.findall(corpus[p]))
        total = collections.Counter()
        for c in main_counts.values():
            total.update(c)
        total.update(test_counts)

        findable = {name: sum(1 for p in main_files for m in parsed[p] if m["name"] == name)
                    for name in set(total)}
        found = []
        for p in main_files:
            text = corpus[p]
            for m in parsed[p]:
                if id(m) in dead_ids or findable.get(m["name"], 0) > 1:
                    continue
                name = m["name"]
                own = word_count(text[m["body_start"]:m["body_end"] + 1], name)
                decl = word_count(text[m["decl_line_start"]:max(m["decl_line_end"], m["decl_line_start"])], name)
                outside = total[name] - own - decl
                if outside <= 0:
                    found.append((p, m, test_counts[name]))
        if not found:
            break
        scoped = [(p, m, t) for p, m, t in found
                  if not args.path or args.path in str(p).replace("\\", "/")]
        print("wave %d: %d member(s)%s"
              % (wave, len(found), "" if len(scoped) == len(found) else " / %d in scope" % len(scoped)))
        if wave == 1 or args.all:
            for p, m, test_hits in sorted(scoped, key=lambda x: (str(x[0]), x[1]["line"])):
                rel = str(p).replace("\\", "/")
                note = "  (tests reference it: %d line(s))" % test_hits if test_hits else ""
                print("  %-74s %-40s line %d%s" % (rel, m["name"], m["line"], note))
        dead.extend((str(p).replace("\\", "/"), m["name"], m["line"], t) for p, m, t in found)
        for p, m, _ in found:
            dead_ids.add(id(m))
            corpus[p] = blank(corpus[p], [(m["decl_line_start"], m["body_end"])])
        wave += 1

    print("\n%d member(s) reachable only through other dead members." % len(dead))
    print("Wave 1 is the actionable set: nothing in the tree names those, at all. Later "
          "waves are the family behind them -- each is named only from a member an "
          "earlier wave already proved dead.")
    print("Dead here is not 'safe to delete'. Before deleting, find the capability's "
          "live home: the convergence guards pair every deletion with the export-side "
          "text that keeps it, so 'deleted a dead copy' cannot pass for 'dropped a type'.")
    print("Where it over-reports, so a hit is not automatically a bug: (1) "
          "com.minicad.preview.payload is serialized by property name, so a Jackson "
          "getter is used without ever being named in source; (2) "
          "src/main/java/com/minicad/step/model is a model layer whose accessors are "
          "reported en masse for the same reason. Scope with --path when working "
          "elsewhere in the tree.")
    return 0


if __name__ == "__main__":
    sys.exit(main())
