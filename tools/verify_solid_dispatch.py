#!/usr/bin/env python3
"""1:1 body-fidelity check for the StepCadSolidBuilder.buildSolid fold.

buildSolid is an instance-method dispatch returning `Solid`. The generated lambdas carry
`self` (StepCadSolidBuilder) + the dispatch var `entity` + the method param `id`, and are
selfified (`builder.` -> `self.builder.`, `canBuildAsSolid(` -> `self.canBuildAsSolid(`). The
verifier reconstructs the original by stripping `self.` and (for the two conditional-return
fall-through branches) dropping the appended `return null;`, then whitespace-normalizes and
compares to the HEAD original chain. Exits non-zero on any divergence.
"""
import re
import subprocess
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
SRC = ROOT / "src/main/java/com/minicad/step/semantic/StepCadSolidBuilder.java"

METHOD_SIG_SUBSTR = "Solid buildSolid("
TERMINAL_MARKER = "is not a supported SOLID"
TABLE_FIELD = "SOLID_RULES"
HEADER_RE = re.compile(r"if \((.*)\)\s*\{$")
TYPE_RE = re.compile(r"instanceof\s+([\w.]+)")


def norm(s):
    return " ".join(s.split())


def simple(name):
    return name.split(".")[-1]


def split_types(condition):
    return TYPE_RE.findall(condition)


def count_braces(line):
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


def extract_branches(lines):
    n = len(lines)
    depth = 0
    base_depth = None
    for i in range(n):
        stripped = lines[i].strip()
        m = HEADER_RE.search(stripped)
        if base_depth is None and stripped.startswith("if (") and m and "instanceof" in m.group(1) and stripped.endswith("{"):
            base_depth = depth
            break
        depth += count_braces(lines[i])
    if base_depth is None:
        return []
    branches = []
    depth = 0
    i = 0
    while i < n:
        stripped = lines[i].strip()
        m = HEADER_RE.search(stripped)
        is_header = (depth == base_depth and stripped.startswith("if (") and m
                     and "instanceof" in m.group(1) and stripped.endswith("{"))
        if is_header:
            condition = m.group(1)
            type_list = split_types(condition)
            is_guarded = "&&" in condition
            d = depth + 1
            body = []
            k = i + 1
            while k < n:
                for ch in lines[k]:
                    if ch == '{':
                        d += 1
                    elif ch == '}':
                        d -= 1
                if d == base_depth:
                    break
                body.append(lines[k])
                k += 1
            while body and body[0].strip() == "":
                body.pop(0)
            while body and body[-1].strip() == "":
                body.pop()
            branches.append((type_list, body, is_guarded))
            i = k + 1
            continue
        depth += count_braces(lines[i])
        i += 1
    return branches


def last_exits(body):
    last = body[-1].strip() if body else ""
    return last.startswith("return") or last.startswith("throw")


def extract_original_branches(text):
    lines = text.replace("\r\n", "\n").replace("\r", "\n").split("\n")
    mi = next(i for i, ln in enumerate(lines) if METHOD_SIG_SUBSTR in ln)
    open_i = next(i for i in range(mi, len(lines)) if "{" in lines[i])
    bi = next(i for i in range(open_i + 1, len(lines))
              if lines[i].strip().startswith("if (") and "instanceof" in lines[i]
              and lines[i].strip().endswith("{"))
    ti = next(i for i in range(open_i + 1, len(lines)) if TERMINAL_MARKER in lines[i])
    branches = []
    seen = set()
    for type_list, body, is_guarded in extract_branches(lines[bi:ti]):
        for type_name in type_list:
            s = simple(type_name)
            if s in seen:
                continue
            seen.add(s)
            branches.append((s, " ".join(b.strip() for b in body), is_guarded))
    return branches


def extract_table_entries(text):
    text = text.replace("\r\n", "\n").replace("\r", "\n")
    marker = "private static final List<SolidRule> " + TABLE_FIELD + " = List.of("
    start = text.index(marker)
    region = text[start:]
    end = region.index("\n    );")
    region = region[: end + len("\n    );")]
    entries = []
    i = 0
    while True:
        j = region.find("solidRule(", i)
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
        m = re.search(
            r"solidRule\(([\w.]+)\.class,\s*\(self,\s*entity,\s*id\)\s*->\s*\{(.*)\}\s*\)\s*,?$",
            entry, re.S)
        if not m:
            print("PARSE FAIL at entry:\n", entry)
            sys.exit(1)
        type_name = m.group(1)
        handler_body = m.group(2).replace("self.", "")  # reconstruct original (drop self prefix)
        entries.append((simple(type_name), norm(handler_body)))
        i = k + 1
    return entries


def main():
    head = subprocess.run(
        ["git", "show", "HEAD:" + SRC.relative_to(ROOT).as_posix()],
        cwd=ROOT, capture_output=True, text=True,
    ).stdout.replace("\r\n", "\n").replace("\r", "\n")
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
        # Fall-through branches (conditional-return) have an appended `return null;` in the
        # generated body; drop it before comparing to the original (which falls through).
        # NB: the normalised body keeps the trailing semicolon, so strip `return null;`.
        o_body = norm(o_body)
        g_body = norm(g_body)
        for tail in ("return null;", "return null"):
            if g_body.endswith(tail):
                g_body = g_body[: -len(tail)].rstrip()
                break
        if o_type != g_type:
            print(f"MISMATCH type at {idx}: original {o_type} vs generated {g_type}")
            ok = False
            continue
        if o_guarded:
            print(f"UNEXPECTED guarded branch at {idx} ({o_type})")
            ok = False
            continue
        if g_body != o_body:
            print(f"MISMATCH body at {idx} ({o_type}):\n"
                  f"  original ={o_body}\n  generated={g_body}")
            ok = False

    if dup:
        print("DUPLICATE types in generated table:", sorted(dup))
        ok = False

    if ok:
        print(f"FAITHFUL: all {len(orig)} branch bodies match verbatim (buildSolid, self.-stripped + fall-through normalised)")
        sys.exit(0)
    sys.exit(1)


if __name__ == "__main__":
    main()
