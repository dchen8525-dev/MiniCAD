#!/usr/bin/env python3
"""1:1 body-fidelity check for the StepTypeNameResolver.surfaceTypeName table-driven
refactor. Compares the git-committed original chain against the current (generated)
dispatch table:

  - same branch count and same type order;
  - each constant branch returns the same STEP-LIKE name string;
  - each dynamic branch's handler body is verbatim-identical (whitespace-normalized)
    to the original branch body.

Exits non-zero with a diagnostic if any branch diverges.
"""
import re
import subprocess
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
SRC = ROOT / "src/main/java/com/minicad/export/json/StepTypeNameResolver.java"
GIT_PATH = "src/main/java/com/minicad/export/json/StepTypeNameResolver.java"


def original_lines():
    out = subprocess.run(
        ["git", "show", "HEAD:" + GIT_PATH], capture_output=True, text=True, cwd=ROOT
    )
    if out.returncode != 0:
        raise SystemExit("ABORT: cannot read original via git show HEAD:" + GIT_PATH)
    return out.stdout.split("\n")


def extract_branches(lines):
    """(type_name, ('const', name)) | (type_name, ('dyn', [body_lines])) for the
    original sequential-if chain."""
    branches = []
    i = 0
    n = len(lines)
    while i < n:
        line = lines[i]
        if line.strip().startswith("if (geometry instanceof ") and line.rstrip().endswith("{"):
            m = re.search(r"if \(geometry instanceof (\w+)\)\s*\{", line)
            type_name = m.group(1)
            depth = 1
            body = []
            k = i + 1
            while k < n:
                for ch in lines[k]:
                    if ch == '{':
                        depth += 1
                    elif ch == '}':
                        depth -= 1
                if depth == 0:
                    break
                body.append(lines[k])
                k += 1
            body = [b.strip() for b in body]
            while body and body[0] == "":
                body.pop(0)
            while body and body[-1] == "":
                body.pop()
            joined = " ".join(body)
            cm = re.match(r'^return "([^"]*)";$', joined)
            if cm:
                branches.append((type_name, ("const", cm.group(1))))
            else:
                branches.append((type_name, ("dyn", body)))
            i = k + 1
            continue
        i += 1
    return branches


def extract_rules(lines):
    """(type_name, ('const', name)) | (type_name, ('dyn', [body_lines])) for the
    generated SURFACE_TYPE_NAME_RULES table."""
    # locate the List.of( opening
    start = next(i for i, ln in enumerate(lines) if "SURFACE_TYPE_NAME_RULES = List.of(" in ln)
    rules = []
    i = start + 1
    n = len(lines)
    # find table end: a line that is exactly "    );"
    while i < n and lines[i].strip() != ");":
        line = lines[i]
        if "surfaceTypeNameRule(" in line:
            m = re.search(r"surfaceTypeNameRule\((\w+)\.class, \(geometry\) -> (.+)$", line)
            type_name = m.group(1)
            rest = m.group(2).strip()
            if rest.startswith('"'):
                name = rest.strip().rstrip('),').strip('"')
                rules.append((type_name, ("const", name)))
                i += 1
                continue
            # dynamic: rest starts with "{" (possibly followed by content on same line)
            assert rest.startswith("{"), "unexpected handler form: " + rest
            body = []
            if rest != "{":
                first = rest[1:].strip()
                if first:
                    body.append(first)
            depth = 1  # the opening '{'
            k = i + 1
            while k < n:
                for ch in lines[k]:
                    if ch == '{':
                        depth += 1
                    elif ch == '}':
                        depth -= 1
                if depth == 0:
                    last = lines[k].strip().rstrip('),')
                    if last and last != "}":
                        body.append(last)
                    break
                body.append(lines[k].strip())
                k += 1
            rules.append((type_name, ("dyn", body)))
            i = k + 1
            continue
        i += 1
    return rules


def main() -> None:
    orig = extract_branches(original_lines())
    gen = extract_rules(SRC.read_text(encoding="utf-8").split("\n"))

    if len(orig) != len(gen):
        print("MISMATCH count: original %d vs generated %d" % (len(orig), len(gen)))
        sys.exit(1)
    for idx, (o, g) in enumerate(zip(orig, gen)):
        ot, op = o
        gt, gp = g
        if ot != gt:
            print("MISMATCH type order at %d: original %s vs generated %s" % (idx, ot, gt))
            sys.exit(1)
        if op[0] != gp[0]:
            print("MISMATCH kind at %d (%s): original %s vs generated %s" % (idx, ot, op[0], gp[0]))
            sys.exit(1)
        if op[0] == "const":
            if op[1] != gp[1]:
                print("MISMATCH const name at %d (%s): original %r vs generated %r" % (idx, ot, op[1], gp[1]))
                sys.exit(1)
        else:
            if op[1] != gp[1]:
                print("MISMATCH dyn body at %d (%s):\n  orig: %s\n  gen : %s" % (idx, ot, op[1], gp[1]))
                sys.exit(1)
    print("FAITHFUL: all %d branch bodies match verbatim (surfaceTypeName)" % len(orig))


if __name__ == "__main__":
    main()
