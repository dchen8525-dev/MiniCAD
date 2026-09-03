#!/usr/bin/env python3
"""1:1 body-fidelity check for the pmiTargetName table-driven fold.

Compares the git-committed original sequential-if chain against the generated
PMI_TARGET_NAME_RULES table. Every branch is a computed handler (cast + .name(),
or StepMetadataHelper.faceDisplayName for StepFaceEntity), so the generated
lambda body must equal the original branch body verbatim. Exits non-zero on any
divergence. This is the behavioral-equivalence proof BEFORE the build runs.
"""
import re
import subprocess
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
SRC = ROOT / "src/main/java/com/minicad/preview/builder/PmiTargetHelper.java"

METHOD_SIG = "    public static String pmiTargetName(StepEntity target) {"
TERMINAL = 'return "";'
TABLE_FIELD = "PMI_TARGET_NAME_RULES"
HEADER_RE = re.compile(r"if \((.*)\)\s*\{$")


def norm(s):
    return " ".join(s.split())


def extract_original_branches(text):
    lines = text.replace("\r\n", "\n").replace("\r", "\n").split("\n")
    mi = next(i for i, ln in enumerate(lines) if ln == METHOD_SIG)
    ti = next(i for i in range(mi, len(lines)) if lines[i].strip() == TERMINAL)
    bi = next(i for i in range(mi + 1, ti) if lines[i].strip().startswith("if (target instanceof "))
    branches = []
    i = bi
    while i < ti:
        line = lines[i]
        stripped = line.strip()
        if stripped.startswith("if (target instanceof ") and stripped.endswith("{"):
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
            branches.append((type_name, condition, body_joined, is_guarded))
            i = k + 1
            continue
        i += 1
    return branches


def extract_table_entries(text):
    text = text.replace("\r\n", "\n").replace("\r", "\n")
    start = text.index("private static final List<PmiTargetNameRule> " + TABLE_FIELD + " = List.of(")
    region = text[start:]
    end = region.index("\n    );")
    region = region[: end + len("\n    );")]
    entries = []
    i = 0
    while True:
        j = region.find("pmiTargetNameRule(", i)
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
        type_name = re.search(r"pmiTargetNameRule\((\w+)\.class,\s*(.*)\)$", entry, re.S).group(1)
        handler = re.search(r"pmiTargetNameRule\(\w+\.class,\s*(.*)\)$", entry, re.S).group(1)
        entries.append((type_name, handler.strip()))
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

    dup = set()
    seen = set()
    for t, _ in gen:
        if t in seen:
            dup.add(t)
        seen.add(t)

    ok = True
    for idx, (ob, gb) in enumerate(zip(orig, gen), 1):
        o_type, o_cond, o_body, o_guarded = ob
        g_type, g_handler = gb
        if o_type != g_type:
            print(f"MISMATCH type at {idx}: original {o_type} vs generated {g_type}")
            ok = False
            continue
        gh = g_handler.strip()
        if o_guarded:
            name = re.search(r'return "([^"]*)";', o_body).group(1)
            if name not in gh or "return null" not in gh or norm(o_cond) not in norm(gh):
                print(f"MISMATCH guarded at {idx} ({o_type}): {norm(gh)}")
                ok = False
            continue
        m = re.match(r'^return "([^"]*)";$', o_body)
        if m:
            if gh != '(target) -> "%s"' % m.group(1):
                print(f"MISMATCH const at {idx} ({o_type}): {gh!r}")
                ok = False
        else:
            hbody = re.search(r"\{\s*(.*)\s*\}$", gh, re.S)
            if not hbody or norm(hbody.group(1)) != norm(o_body):
                print(f"MISMATCH computed at {idx} ({o_type}):\n"
                      f"  original={norm(o_body)}\n  generated={norm(gh)}")
                ok = False

    if dup:
        print("DUPLICATE types in generated table:", sorted(dup))
        ok = False

    if ok:
        print(f"FAITHFUL: all {len(orig)} branch bodies match verbatim (pmiTargetName)")
        sys.exit(0)
    sys.exit(1)


if __name__ == "__main__":
    main()
