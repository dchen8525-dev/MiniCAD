#!/usr/bin/env python3
"""1:1 body-fidelity check for the StepShellBuilder.buildShell table-driven fold.

buildShell is an instance method, so the generated lambdas must reach instance
state through `self.` (`self.builder.*`, `self.buildFaceShell`, ...) and carry the
`id` parameter (the StepSurfaceModel branch interpolates it into a throw message).
That means the generated table body is NOT verbatim-equal to the original branch
body -- every instance reference carries a `self.` prefix.

This verifier proves behavioral equivalence anyway: it pulls the original
sequential-if chain from git HEAD and the generated SHELL_RULES table from the
working tree, strips the `self.` prefix from each generated lambda body, and
asserts the result equals the original branch body (type-for-type, verbatim up to
whitespace). Exits non-zero on any divergence. This is the behavioral-equivalence
proof BEFORE the build runs.
"""
import re
import subprocess
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
SRC = ROOT / "src/main/java/com/minicad/step/semantic/StepShellBuilder.java"

METHOD_SIG = "    Shell buildShell(int id) {"
TERMINAL_MARKER = "is not an OPEN_SHELL, SURFACED_OPEN_SHELL"
TABLE_FIELD = "SHELL_RULES"
HEADER_RE = re.compile(r"if \((.*)\)\s*\{$")


def norm(s):
    return " ".join(s.split())


def terminal_start(lines, marker_idx):
    """Back up from the marker line to the first line of the terminal statement.

    The marker text lives inside a *string literal* on the second line of
        throw new StepResolutionException(
                "entity #" + id + " is not an OPEN_SHELL, ..."
        );
    so the chain's last branch actually ends *before* the `throw new ...(` opener.
    """
    k = marker_idx
    while k > 0 and not lines[k].strip().startswith("throw "):
        k -= 1
    return k


def extract_original_branches(text):
    lines = text.replace("\r\n", "\n").replace("\r", "\n").split("\n")
    mi = next(i for i, ln in enumerate(lines) if ln == METHOD_SIG)
    ti = terminal_start(
        lines, next(i for i in range(mi, len(lines)) if TERMINAL_MARKER in lines[i])
    )
    bi = next(
        i
        for i in range(mi + 1, ti)
        if lines[i].strip().startswith("if (entity instanceof ")
    )
    branches = []
    i = bi
    while i < ti:
        line = lines[i]
        stripped = line.strip()
        if stripped.startswith("if (entity instanceof ") and stripped.endswith("{"):
            condition = HEADER_RE.search(line).group(1)
            type_name = re.search(r"instanceof (\w+)", condition).group(1)
            is_guarded = "&&" in condition
            depth = 1
            body = []
            k = i + 1
            while k < ti:
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
    marker = "private static final List<ShellRule> " + TABLE_FIELD + " = List.of("
    start = text.index(marker)
    region = text[start:]
    end = region.index("\n    );")
    region = region[: end + len("\n    );")]
    entries = []
    i = 0
    while True:
        j = region.find("shellRule(", i)
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
            r"shellRule\((\w+)\.class,\s*\(self,\s*entity,\s*id\)\s*->\s*\{(.*)\}\s*\)\s*,?$",
            entry,
            re.S,
        )
        if not m:
            print("PARSE FAIL at entry:\n", entry)
            sys.exit(1)
        type_name = m.group(1)
        handler_body = m.group(2)
        # reverse the selfify() transform: drop the `self.` prefix to reconstruct
        # the original branch body that lived inside the sequential if.
        reconstructed = handler_body.replace("self.", "")
        entries.append((type_name, norm(reconstructed)))
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
            # buildShell is a pure first-match-return chain; a guarded branch would
            # need null-fallthrough support.
            print(f"UNEXPECTED guarded branch at {idx} ({o_type})")
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
        print(f"FAITHFUL: all {len(orig)} branch bodies match verbatim (buildShell, self.-stripped)")
        sys.exit(0)
    sys.exit(1)


if __name__ == "__main__":
    main()
