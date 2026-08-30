"""Build a dependency map of viewer.js top-level declarations.

Prints each top-level name with the other top-level names it references, plus
its span, so module boundaries can be chosen from actual usage instead of
guessing. Names nothing else uses are leaf candidates (safe to extract first).
"""
import re
import sys

SRC = "src/main/resources/static/viewer.js"
DECL = re.compile(r"^(?:async\s+)?function\s+(\w+)|^(?:const|let|var)\s+(\w+)")


def top_level_decls(lines):
    """name -> (start, end). A top-level declaration ends at the first column-0
    line that closes a block (`}`) or terminates a statement (`;`)."""
    spans = []
    current = None
    for i, ln in enumerate(lines):
        m = DECL.match(ln)
        if m:
            current = (m.group(1) or m.group(2), i)
            # a one-line declaration such as `const x = 1;`
            if ln.rstrip().endswith(";") and ln.count("(") == ln.count(")"):
                spans.append((current[0], i, i))
                current = None
            continue
        if current is None:
            continue
        at_col0 = ln[:1] not in (" ", "\t")
        if ln == "}" or (at_col0 and ln.rstrip().endswith(";")):
            spans.append((current[0], current[1], i))
            current = None
    if current:
        spans.append((current[0], current[1], len(lines) - 1))
    return spans


def main():
    lines = open(SRC, encoding="utf-8").read().split("\n")
    spans = top_level_decls(lines)
    names = {n for n, _, _ in spans}

    deps, used_by = {}, {n: set() for n in names}
    for name, start, end in spans:
        body = "\n".join(lines[start:end + 1])
        refs = set()
        for other in names:
            if other == name:
                continue
            if re.search(r"\b" + re.escape(other) + r"\b", body):
                refs.add(other)
        deps[name] = refs
        for r in refs:
            used_by[r].add(name)

    mode = sys.argv[1] if len(sys.argv) > 1 else "leaf"
    if mode == "leaf":
        leaves = sorted(n for n in names if not used_by[n])
        print(f"top-level declarations: {len(names)}")
        print(f"referenced by nothing  : {len(leaves)}")
        for n in leaves:
            span = next(s for s in spans if s[0] == n)
            print(f"  {n}  (lines {span[1]+1}-{span[2]+1}, {span[2]-span[1]+1} lines)")
        return

    if mode == "hub":
        hubs = sorted(names, key=lambda n: -len(used_by[n]))[:20]
        for n in hubs:
            span = next(s for s in spans if s[0] == n)
            print(f"  {n:38s} used by {len(used_by[n]):3d}   lines {span[1]+1}-{span[2]+1}")
        return

    if mode == "spans":
        for name, start, end in sorted(spans, key=lambda s: s[1]):
            print(f"{start+1:5d}-{end+1:5d}  {end-start+1:5d}  {name}")
        return

    # default: dependency detail for one name
    target = mode
    span = next((s for s in spans if s[0] == target), None)
    if not span:
        print("no such declaration")
        return
    print(f"{target} (lines {span[1]+1}-{span[2]+1})")
    print(f"  uses     : {sorted(deps[target])}")
    print(f"  used by  : {sorted(used_by[target])}")


if __name__ == "__main__":
    main()
