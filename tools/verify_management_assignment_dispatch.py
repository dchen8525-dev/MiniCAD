#!/usr/bin/env python3
"""1:1 body-fidelity check for the validateManagementAssignmentEntity fold.

validateManagementAssignmentEntity is a `private static` first-match-return chain,
so the generated lambdas carry no `self` and every branch body is copied verbatim
into the table. This verifier proves that: it pulls the original sequential-if
chain from git HEAD and the generated MANAGEMENT_ASSIGNMENT_RULES table from the
working tree and asserts each body matches (type-for-type, verbatim up to
whitespace). Exits non-zero on any divergence. This is the behavioral-equivalence
proof BEFORE the build runs.
"""
import re
import subprocess
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
SRC = ROOT / "src/main/java/com/minicad/app/StepDumpApp.java"

METHOD_SIG = "    private static Integer validateManagementAssignmentEntity("
TABLE_FIELD = "MANAGEMENT_ASSIGNMENT_RULES"
HEADER_RE = re.compile(r"if \((.*)\)\s*\{$")


def norm(s):
    return " ".join(s.split())


def method_bounds(lines):
    """Return (method_idx, terminal_idx) located structurally (not by a marker)."""
    mi = next(i for i, ln in enumerate(lines) if ln.startswith(METHOD_SIG))
    end = next(i for i in range(mi + 1, len(lines)) if lines[i] == "    }")
    return mi, end - 1


def extract_original_branches(text, sig=METHOD_SIG):
    lines = text.replace("\r\n", "\n").replace("\r", "\n").split("\n")
    mi, terminal = method_bounds(lines)
    bi = next(
        i
        for i in range(mi + 1, terminal)
        if lines[i].strip().startswith("if (entity instanceof ")
    )
    branches = []
    i = bi
    while i < terminal:
        line = lines[i]
        stripped = line.strip()
        if stripped.startswith("if (entity instanceof ") and stripped.endswith("{"):
            condition = HEADER_RE.search(line).group(1)
            type_name = re.search(r"instanceof (\w+)", condition).group(1)
            is_guarded = "&&" in condition
            depth = 1
            body = []
            k = i + 1
            while k < terminal:
                for ch in lines[k]:
                    if ch == "{":
                        depth += 1
                    elif ch == "}":
                        depth -= 1
                if depth == 0:
                    break
                body.append(lines[k])
                k += 1
            body_joined = " ".join(b.strip() for b in body)
            branches.append((type_name, body_joined, is_guarded))
            i = k + 1
            continue
        i += 1
    return branches


def extract_table_entries(text):
    text = text.replace("\r\n", "\n").replace("\r", "\n")
    marker = (
        "private static final List<ManagementAssignmentRule> " + TABLE_FIELD + " = List.of("
    )
    start = text.index(marker)
    region = text[start:]
    end = region.index("\n    );")
    region = region[: end + len("\n    );")]
    entries = []
    i = 0
    while True:
        j = region.find("managementAssignmentRule(", i)
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
            r"managementAssignmentRule\((\w+)\.class,\s*\(entity,\s*builder\)\s*->\s*\{(.*)\}\s*\)\s*,?$",
            entry,
            re.S,
        )
        if not m:
            print("PARSE FAIL at entry:\n", entry)
            sys.exit(1)
        entries.append((m.group(1), norm(m.group(2))))
        i = k + 1
    return entries


def main():
    head = subprocess.run(
        ["git", "show", "HEAD:" + SRC.relative_to(ROOT).as_posix()],
        cwd=ROOT, capture_output=True, text=True,
    ).stdout
    head = head.replace("\r\n", "\n").replace("\r", "\n")
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
        if o_type != g_type:
            print(f"MISMATCH type at {idx}: original {o_type} vs generated {g_type}")
            ok = False
            continue
        if o_guarded:
            print(
                f"UNEXPECTED guarded branch at {idx} ({o_type}): the loop is "
                "first-match-return and cannot express a guard"
            )
            ok = False
            continue
        if g_body != norm(o_body):
            print(
                f"MISMATCH body at {idx} ({o_type}):\n"
                f"  original ={norm(o_body)}\n  generated={g_body}"
            )
            ok = False

    if dup:
        print("DUPLICATE types in generated table:", sorted(dup))
        ok = False

    if ok:
        print(
            f"FAITHFUL: all {len(orig)} branch bodies match verbatim "
            "(validateManagementAssignmentEntity)"
        )
        sys.exit(0)
    sys.exit(1)


if __name__ == "__main__":
    main()
