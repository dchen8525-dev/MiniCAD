"""Find pairs of classes in the main tree that are structural twins.

A sixth view of duplicated code. The five existing scanners each miss one
shape that this one sees:

  scan_duplicate_methods  groups by method *name*, so a family whose copies
                          disagree about their own names (nearestPointIndex2 /
                          nearestPointIndex3 / nearestPointIndex) reports as
                          singletons;
  scan_dead_types         asks whether *every* member of a type is dead, so a
                          class with one live member is invisible;
  scan_dead_members       counts a name being mentioned anywhere, so a dead
                          twin is kept alive by the live twin that names it;
  scan_dead_private_...   requires `private`, and counts per file;
  scan_instanceof_chains  looks at dispatch chains, not at class shape.

What is left is the whole-class copy: the same members, in the same order,
renamed -- a "parallel class". The two classes usually live in different
packages (geometry vs geometry2d) or in the same package with a Rational /
Binary / 2D prefix, and they are almost never reported as a pair by anything,
because a pair-wise diff needs *both* names and *both* bodies to line up.

Scoring, in two passes:

  1. name similarity -- Jaccard over the two member-name sets. Pairs below
     --min-name-similarity are dropped, since renamed members mean the two
     files are not copies of each other.
  2. duplicated volume -- total length of the bodies that are byte-identical
     after whitespace normalization, over the shared names.

Volume, not similarity, is the ranking. Accessor boilerplate makes many
unrelated data classes look 100% identical on a handful of one-line bodies;
what matters is how much real code is duplicated. Ranking by similarity
surfaces the boilerplate, ranking by volume surfaces BSplineSurface3 /
RationalBSplineSurface3 (995 characters of identical code across 27 members).

Usage:
    python tools/scan_twin_classes.py
    python tools/scan_twin_classes.py --min-name-similarity 0.7 --top 15
    python tools/scan_twin_classes.py --root src/main/java --include step

A hit is a lead, not a verdict: `step/model` pairs are STEP entity models that
are *supposed* to mirror each other one field per entity, and a pair of
concrete types implementing the same interface with the same body is a
polymorphic implementation, not a copy. Read both files before acting.
"""

import argparse
import io
import itertools
import os
import re

DEFAULT_ROOT = os.path.join("src", "main", "java")

METHOD_RE = re.compile(
    r"^[ \t]*(?:(?:public|protected|private|static|final|abstract|synchronized|default|native)\s+)*"
    r"(?:<[^>]+>\s*)?"
    r"([A-Za-z_$][\w$.<>\[\], ?]*)\s+"
    r"([A-Za-z_$][\w$]*)\s*\([^;{]*\)\s*(?:throws [\w.,\s]+)?\{",
    re.MULTILINE,
)

# A "return type" position can also hold a control-flow keyword; those lines are
# not declarations.
NOT_A_TYPE = {
    "return", "new", "else", "if", "for", "while", "switch", "case", "throw",
    "do", "try", "catch", "synchronized", "yield",
}

NOT_A_NAME = {"if", "for", "while", "switch", "catch", "synchronized", "return", "new"}


def strip_comments(text):
    text = re.sub(r"/\*.*?\*/", "", text, flags=re.DOTALL)
    return re.sub(r"//[^\n]*", "", text)


def body_of(text, open_brace):
    depth = 0
    for index in range(open_brace, len(text)):
        character = text[index]
        if character == "{":
            depth += 1
        elif character == "}":
            depth -= 1
            if depth == 0:
                return text[open_brace + 1:index]
    return text[open_brace + 1:]


def normalize(body):
    return re.sub(r"\s+", " ", body).strip()


def read(path):
    with io.open(path, "r", encoding="utf-8", newline="") as handle:
        return handle.read().replace("\r\n", "\n")


def members(text):
    """{member name: normalized body}. Overloads get a #2, #3 suffix."""
    code = strip_comments(text)
    found = {}
    for match in METHOD_RE.finditer(code):
        return_type, name = match.group(1), match.group(2)
        if return_type in NOT_A_TYPE or name in NOT_A_NAME:
            continue
        name = name.split(".")[-1]
        key = name
        suffix = 1
        while key in found:
            suffix += 1
            key = "%s#%d" % (name, suffix)
        found[key] = normalize(body_of(code, match.end() - 1))
    return found


def jaccard(left, right):
    if not left or not right:
        return 0.0
    return len(left & right) / len(left | right)


def collect(root, include):
    classes = {}
    for directory, _dirs, files in os.walk(root):
        for name in files:
            if not name.endswith(".java"):
                continue
            path = os.path.join(directory, name)
            if include and include not in path.replace("\\", "/"):
                continue
            text = read(path)
            if len(text) < 1500:
                continue
            found = members(text)
            if len(found) >= 5:
                classes[path] = found
    return classes


def main():
    parser = argparse.ArgumentParser(description=__doc__.splitlines()[0])
    parser.add_argument("--root", default=DEFAULT_ROOT)
    parser.add_argument("--include", default="", help="only paths containing this substring")
    parser.add_argument("--min-name-similarity", type=float, default=0.55)
    parser.add_argument("--min-volume", type=int, default=200, help="characters of identical code")
    parser.add_argument("--top", type=int, default=25)
    args = parser.parse_args()

    classes = collect(args.root, args.include)
    print("classes considered: %d (>=5 members, >=1500 bytes)" % len(classes))

    scored = []
    for left, right in itertools.combinations(sorted(classes), 2):
        left_members, right_members = classes[left], classes[right]
        name_similarity = jaccard(set(left_members), set(right_members))
        if name_similarity < args.min_name_similarity:
            continue
        shared = set(left_members) & set(right_members)
        identical = [name for name in shared if left_members[name] == right_members[name]]
        volume = sum(len(left_members[name]) for name in identical)
        if volume < args.min_volume:
            continue
        scored.append((
            volume,
            len(identical) / len(shared),
            name_similarity,
            left,
            right,
            len(left_members),
            len(right_members),
        ))

    scored.sort(reverse=True)
    for volume, body_similarity, name_similarity, left, right, left_size, right_size in scored[:args.top]:
        print(
            "\n%d chars identical | names=%.2f bodies=%.2f | members %d / %d\n     %s\n     %s"
            % (volume, name_similarity, body_similarity, left_size, right_size, left, right)
        )
    if not scored:
        print("no pair above the thresholds")
    print(
        "\n%d pair(s). A hit is a lead, not a verdict: the step/model pairs mirror each other "
        "one field per STEP entity by design, and a pair of concrete types with the same body is "
        "a polymorphic implementation, not a copy." % len(scored)
    )


if __name__ == "__main__":
    main()
