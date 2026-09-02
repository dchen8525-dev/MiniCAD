"""Faithfulness check for the Region C table-driven conversion.

Compares every branch body extracted from the ORIGINAL chain (git HEAD) against
the handler body generated into target/pmi-payload-new.java. Bodies must be
identical after two normalisations:
  * strip per-line leading whitespace (indentation is spotless' job),
  * the single `continue;` in StepAnnotationTextOccurrence becomes `return;`
    (the handler returns instead of continue-ing the loop -- behaviourally
    identical because the loop body is only the dispatch).

Exits non-zero on any mismatch so it can gate the refactor.
"""
import re
import subprocess
import sys
from pathlib import Path

SRC_REL = "src/main/java/com/minicad/export/json/StepPmiPayloadBuilder.java"
NEW_JAVA = Path("target/pmi-payload-new.java")

HEADER_RE = re.compile(r"^(?:\}\s*)?(?:else\s+)?if \(.*instanceof")


def read_original():
    out = subprocess.run(
        ["git", "show", "HEAD:" + SRC_REL],
        capture_output=True, text=True, check=True,
    )
    return out.stdout.splitlines()


def close_index(lines, h):
    depth = 1
    j = h + 1
    while j < len(lines):
        in_str = False
        for ch in lines[j]:
            if ch == '"':
                in_str = not in_str
            elif not in_str:
                if ch == "{":
                    depth += 1
                elif ch == "}":
                    depth -= 1
        if depth == 0:
            return j
        j += 1
    raise SystemExit("ABORT: unbalanced braces at line " + str(h + 1))


def extract_original_chain(lines):
    pmi_decl = next((k for k, ln in enumerate(lines)
                     if "List<PmiPayload> pmi = new ArrayList<>();" in ln), None)
    loop_open = next((k for k in range(pmi_decl + 1, len(lines))
                      if "for (StepEntity entity : resolved.values()) {" in lines[k]), None)
    chain_start = loop_open + 1
    while chain_start < len(lines) and not HEADER_RE.match(lines[chain_start].strip()):
        chain_start += 1
    for_close = close_index(lines, loop_open)
    headers = [i for i in range(chain_start, for_close) if HEADER_RE.match(lines[i].strip())]
    last_close = close_index(lines, headers[-1])
    branches = []
    for k, h in enumerate(headers):
        if k < len(headers) - 1:
            body = lines[h + 1:headers[k + 1]]
        else:
            body = lines[h + 1:last_close]
        branches.append(body)
    return branches


def extract_generated_handlers(lines):
    start = next((k for k, ln in enumerate(lines) if "PMI_PAYLOAD_RULES = List.of(" in ln), None)
    if start is None:
        raise SystemExit("ABORT: PMI_PAYLOAD_RULES not found in generated file")
    # Walk rules until the closing ");" of the List.of(...).
    rules = []
    i = start + 1
    while i < len(lines):
        ln = lines[i].strip()
        if ln.startswith(");"):
            break
        if ln.startswith("pmiPayloadRule("):
            # Find the handler lambda open "{"
            j = i
            while j < len(lines) and "-> {" not in lines[j]:
                j += 1
            open_line = j
            depth = 0
            while j < len(lines):
                in_str = False
                for ch in lines[j]:
                    if ch == '"':
                        in_str = not in_str
                    elif not in_str:
                        if ch == "{":
                            depth += 1
                        elif ch == "}":
                            depth -= 1
                if depth == 0 and j > open_line:
                    break
                j += 1
            # body = between the "{" on open_line+1 and the matching "}" at j
            body = lines[open_line + 1:j]
            rules.append(body)
            i = j + 1
        else:
            i += 1
    return rules


def norm(body):
    out = []
    for ln in body:
        s = ln.strip()
        if s == "":
            continue
        s = s.replace("continue;", "return;")
        out.append(s)
    return out


def main():
    orig = read_original()
    orig_bodies = extract_original_chain(orig)
    gen = NEW_JAVA.read_text(encoding="utf-8").splitlines()
    gen_bodies = extract_generated_handlers(gen)

    print(f"original branches: {len(orig_bodies)}")
    print(f"generated handlers: {len(gen_bodies)}")
    if len(orig_bodies) != len(gen_bodies):
        print("MISMATCH: branch count differs")
        sys.exit(1)

    ok = True
    for idx, (o, g) in enumerate(zip(orig_bodies, gen_bodies), 1):
        if norm(o) != norm(g):
            ok = False
            print(f"\n--- MISMATCH at branch {idx} ---")
            print("ORIGINAL:")
            for ln in norm(o):
                print("  " + ln)
            print("GENERATED:")
            for ln in norm(g):
                print("  " + ln)
    if ok:
        print("FAITHFUL: all %d branch bodies match verbatim (continue->return normalised)." % len(orig_bodies))
    else:
        sys.exit(1)


if __name__ == "__main__":
    main()
