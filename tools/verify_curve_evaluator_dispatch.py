#!/usr/bin/env python3
"""1:1 body fidelity check for the curveEvaluator table-driven refactor.

Extracts each branch body from the ORIGINAL chain (current working source, still
unchanged on disk) and each handler body from the generated target file, then asserts
they are byte-for-byte identical in order and content. Exits non-zero on any mismatch.
"""
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
SRC = ROOT / "src/main/java/com/minicad/export/json/StepRepresentationPayloadBuilder.java"
OUT = ROOT / "target/curve-evaluator-new.java"

ORIG_HEADER_RE = re.compile(r"^\s*(?:\} else )?if \(curve instanceof (\w+)\) \{$")
GEN_HEADER_RE = re.compile(r"^\s*curveEvalRule\((\w+)\.class, \(curve, builder\) -> \{$")
ELSE_RE = re.compile(r"^\s*\} else \{$")


def extract_original_bodies(lines):
    method_open = None
    for i, ln in enumerate(lines):
        if ln.strip().startswith("public static CurveEvaluator curveEvaluator("):
            method_open = i
            break
    headers = []
    for i in range(method_open, len(lines)):
        m = ORIG_HEADER_RE.match(lines[i])
        if m:
            headers.append((i, m.group(1)))
        if ELSE_RE.match(lines[i]):
            else_header = i
            break
    bodies = []
    for k, (h, t) in enumerate(headers):
        end = headers[k + 1][0] if k + 1 < len(headers) else else_header
        body = [ln for ln in lines[h + 1:end] if ln.strip() != ""]
        bodies.append((t, body))
    return bodies


def extract_generated_bodies(lines):
    bodies = []
    i = 0
    while i < len(lines):
        m = GEN_HEADER_RE.match(lines[i])
        if m:
            t = m.group(1)
            j = i + 1
            while j < len(lines):
                if lines[j].strip().startswith("})"):
                    body = lines[i + 1:j]
                    bodies.append((t, body))
                    i = j + 1
                    break
                j += 1
            else:
                raise SystemExit(f"ABORT: no closing `}})` for handler {t}")
        else:
            i += 1
    return bodies


def main():
    orig = extract_original_bodies(SRC.read_text(encoding="utf-8").split("\n"))
    gen = extract_generated_bodies(OUT.read_text(encoding="utf-8").split("\n"))

    if len(orig) != len(gen):
        print(f"MISMATCH count: original={len(orig)} generated={len(gen)}")
        sys.exit(1)

    ok = True
    for (to, bo), (tg, bg) in zip(orig, gen):
        if to != tg:
            print(f"ORDER MISMATCH: original={to} generated={tg}")
            ok = False
            break
        if bo != bg:
            print(f"BODY MISMATCH for {to}:")
            for a, b in zip(bo, bg):
                if a != b:
                    print(f"  orig: {a!r}")
                    print(f"  gen : {b!r}")
                    break
            if len(bo) != len(bg):
                print(f"  length orig={len(bo)} gen={len(bg)}")
            ok = False
            break

    if ok:
        print(f"FAITHFUL: all {len(orig)} branch bodies match verbatim")
    else:
        sys.exit(1)


if __name__ == "__main__":
    main()
