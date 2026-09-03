#!/usr/bin/env python3
"""1:1 body-fidelity check for StepFacePayloadBuilder.buildPreviewFaceResult table-driven dispatch.

Independent of the generator: it re-extracts each original `if (previewGeometry instanceof X) { ... }`
branch body from the SOURCE and each handler lambda body from the GENERATED file, using a proper
whole-text brace matcher (character-position scan), then compares them in order.

A fallthrough branch (single type in FALLTHROUGH) gets `return null;` appended by the generator, so the
generated handler body is expected to equal the original body + that one trailing line. We normalise by
dropping a single trailing `return null;` from the generated body before comparing.

Exit code 0 = FAITHFUL (all branch bodies match verbatim); 1 = mismatch (prints the diff).
"""
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
SRC = ROOT / "src/main/java/com/minicad/export/json/StepFacePayloadBuilder.java"
GEN = ROOT / "target/preview-face-new.java"

BRANCH_START_RE = re.compile(r"^\s*if \(previewGeometry instanceof (\w+)")
TYPE_RE = re.compile(r"previewGeometry instanceof (\w+)")
FALLTHROUGH = {
    "StepCylindricalSurface",
    "StepConicalSurface",
    "StepToroidalSurfaceWithSpecifiedBends",
    "StepToroidalSurface",
}


def _matching_brace(text, open_pos):
    """Return the index of the `}` that balances the `{` at text[open_pos]."""
    depth = 0
    i = open_pos
    while i < len(text):
        c = text[i]
        if c == "{":
            depth += 1
        elif c == "}":
            depth -= 1
            if depth == 0:
                return i
        i += 1
    raise ValueError(f"unbalanced brace starting at {open_pos}")


def _line_to_text_pos(lines, line_idx, col):
    """Absolute character offset of (line_idx, col) in the joined text."""
    offset = 0
    for li in range(line_idx):
        offset += len(lines[li]) + 1  # +1 for the "\n" join
    return offset + col


def _clean(body):
    """Mirror the generator's body cleaning: strip leading/trailing blank lines and trailing comments."""
    body = [ln.rstrip() for ln in body]
    while body and body[0].strip() == "":
        body.pop(0)
    while body and body[-1].strip() == "":
        body.pop()
    while body and (body[-1].strip().startswith("//") or body[-1].strip().startswith("/*")):
        body.pop()
    return body


def extract_src_branches(lines):
    text = "\n".join(lines)
    method_open = next(i for i, ln in enumerate(lines)
                       if ln.strip().startswith("public static PreviewFaceResult buildPreviewFaceResult("))
    preview_line = next(i for i in range(method_open, len(lines))
                        if "StepEntity previewGeometry = unwrapParametricPreviewSurface(geometry);" in lines[i])
    terminal_line = next(i for i in range(preview_line, len(lines))
                         if lines[i].strip().startswith("String unsupportedSurface = describeUnsupportedPreviewSurface("))
    starts = [i for i in range(preview_line + 1, terminal_line)
              if BRANCH_START_RE.match(lines[i])]
    branches = []
    for s in starts:
        he = s
        while he < terminal_line and ") {" not in lines[he]:
            he += 1
        types = TYPE_RE.findall(" ".join(lines[s:he + 1]))
        col = lines[he].index("{")
        open_pos = _line_to_text_pos(lines, he, col)
        close_pos = _matching_brace(text, open_pos)
        body = text[open_pos + 1:close_pos].split("\n")
        branches.append((types, _clean(body)))
    return branches


def extract_gen_rules(lines):
    text = "\n".join(lines)
    start = next(i for i, ln in enumerate(lines) if "PREVIEW_FACE_RULES = List.of(" in ln)
    rules = []
    j = start + 1
    while j < len(lines):
        if "previewFaceRule(" not in lines[j]:
            j += 1
            continue
        m = re.search(r"previewFaceRule\((\w+)\.class,\s*(.+?),\s*\(stepFace", lines[j])
        primary = m.group(1)
        pred = m.group(2).strip()
        is_single = pred.endswith("::isInstance")
        # locate `-> {`
        k = j
        while k < len(lines) and "-> {" not in lines[k]:
            k += 1
        col = lines[k].index("{")
        open_pos = _line_to_text_pos(lines, k, col)
        close_pos = _matching_brace(text, open_pos)
        body = text[open_pos + 1:close_pos].split("\n")
        rules.append((primary, is_single, _clean(body)))
        j += 1
    return rules


def main():
    src_lines = SRC.read_text(encoding="utf-8").split("\n")
    gen_lines = GEN.read_text(encoding="utf-8").split("\n")

    src_branches = extract_src_branches(src_lines)
    gen_rules = extract_gen_rules(gen_lines)

    if len(src_branches) != len(gen_rules):
        print(f"MISMATCH: branch count {len(src_branches)} (src) != {len(gen_rules)} (gen)")
        sys.exit(1)

    mismatches = []
    for idx, ((src_types, src_body), (gen_primary, gen_single, gen_body)) in enumerate(
            zip(src_branches, gen_rules)):
        primary = src_types[0]
        if gen_primary != primary:
            mismatches.append((idx, primary, gen_primary, "order/primary-type mismatch"))
            continue
        expected = list(src_body)
        if gen_single and primary in FALLTHROUGH:
            if gen_body and gen_body[-1].strip() == "return null;":
                gen_body = gen_body[:-1]
            else:
                mismatches.append((idx, primary, gen_primary,
                                   "fallthrough branch missing trailing 'return null;'"))
                continue
        if gen_body != expected:
            # locate first difference for diagnostics
            diff = None
            for di in range(max(len(expected), len(gen_body))):
                a = expected[di] if di < len(expected) else "<EOF>"
                b = gen_body[di] if di < len(gen_body) else "<EOF>"
                if a != b:
                    diff = (di, a, b)
                    break
            mismatches.append((idx, primary, gen_primary, f"body differs (len {len(expected)} vs {len(gen_body)}; first@{diff}"))
            continue

    if mismatches:
        print(f"NOT FAITHFUL: {len(mismatches)} branch(es) differ:")
        for idx, prim, _, why in mismatches:
            print(f"  [{idx}] {prim}: {why}")
        sys.exit(1)

    print(f"FAITHFUL: all {len(src_branches)} branch bodies match verbatim "
          f"({sum(1 for t, _ in src_branches if len(t) == 1 and t[0] in FALLTHROUGH)} fallthrough)")
    sys.exit(0)


if __name__ == "__main__":
    main()
