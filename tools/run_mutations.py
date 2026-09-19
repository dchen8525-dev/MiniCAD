"""Run a list of source mutations and report which ones the tests catch.

Every convergence round ends with mutations: apply a change that reverts what
the round did, run the guard, confirm the guard fails and names the right
test. Doing that by hand has cost this repo the same three hours more than
once, so the harness encodes them:

  * byte-exact restore, checked with a sha256 of the file before and after.
    A mutation that is not restored corrupts the final tree, and the next
    `verify` blames the round's own code;
  * anchors are matched on LF-normalized text and written back with the file's
    own line ending. Java sources here are CRLF, so a multi-line anchor written
    with plain \\n silently matches nothing;
  * "no surefire report" is NOT a pass. A mutation that does not compile
    produces no report and no failure, which reads as a missed mutation. The
    harness reports it as UNCERTAIN, with the compiler's first error line.

Input is a JSON file:

    [
      {
        "name": "tie-break back to <=",
        "file": "src/main/java/com/minicad/common/TrimmedParamRange.java",
        "old": "if (a < b && a < c) {",
        "new": "if (a <= b && a <= c) {",
        "test": "com.minicad.architecture.TrimmedCurveFamilyConvergenceTest"
      }
    ]

`old` must occur exactly once: a mutation that lands in the wrong place proves
nothing, so an ambiguous anchor is skipped with a loud message.

Usage:
    python tools/run_mutations.py mutations.json
    python tools/run_mutations.py mutations.json --keep-going

Exit code is 0 when every mutation was caught, 1 otherwise.
"""

import argparse
import hashlib
import io
import json
import re
import subprocess
import sys

PYTHON = sys.executable
MVN = ["tools/mvn.py", "-o", "test"]
TOTALS_RE = re.compile(r"Tests run: (\d+), Failures: (\d+), Errors: (\d+)")
# surefire prints failures as `fq.Class.method -- Time elapsed: ... <<< ERROR!`
# (or FAILURE!), both in the console and in target/surefire-reports; the report
# FILE name carries the package too, so never glob on the simple class name.
NAMED_RE = re.compile(r"\[ERROR\]\s+(\S+?):\d+")
COMPILE_RE = re.compile(r"\[ERROR\].*\.java:\[\d+")


def run_tests(test_class):
    result = subprocess.run(
        [PYTHON] + MVN + ["-Dtest=" + test_class, "-DfailIfNoSpecifiedTests=false"],
        capture_output=True,
        text=True,
    )
    output = result.stdout + result.stderr
    totals = TOTALS_RE.findall(output)
    if not totals:
        compile_error = COMPILE_RE.search(output)
        return None, [], (compile_error.group(0) if compile_error else "no surefire report")
    total = max(int(item[0]) for item in totals)
    failed = max(int(item[1]) + int(item[2]) for item in totals)
    return (total, failed), sorted(set(NAMED_RE.findall(output))), ""


def mutate(entry):
    path = entry["file"]
    with open(path, "rb") as handle:
        original = handle.read()
    digest = hashlib.sha256(original).hexdigest()
    text = io.open(path, "r", encoding="utf-8", newline="").read()
    crlf = "\r\n" in text
    flat = text.replace("\r\n", "\n")
    occurrences = flat.count(entry["old"])
    if occurrences != 1:
        return "SKIP", "anchor occurs %d times, not once" % occurrences, []
    mutated = flat.replace(entry["old"], entry["new"])
    io.open(path, "w", encoding="utf-8", newline="").write(
        mutated.replace("\n", "\r\n") if crlf else mutated
    )
    try:
        outcome, names, note = run_tests(entry["test"])
    finally:
        io.open(path, "w", encoding="utf-8", newline="").write(text)
        with open(path, "rb") as handle:
            restored = hashlib.sha256(handle.read()).hexdigest()
        if restored != digest:
            raise SystemExit("RESTORE FAILED for " + path + " - tree is corrupted, stop")
    if outcome is None:
        return "UNCERTAIN", "not a pass: " + note, names
    total, failed = outcome
    if failed:
        return "CAUGHT", "tests=%d failed=%d" % (total, failed), names
    return "MISSED", "tests=%d failed=0" % total, []


def main():
    parser = argparse.ArgumentParser(description=__doc__.splitlines()[0])
    parser.add_argument("mutations", help="JSON file with the mutation list")
    parser.add_argument("--keep-going", action="store_true", help="report every mutation, never stop early")
    args = parser.parse_args()

    with io.open(args.mutations, "r", encoding="utf-8") as handle:
        mutations = json.load(handle)

    missed = []
    for entry in mutations:
        verdict, note, names = mutate(entry)
        print("%-9s %-45s %s" % (verdict, entry["name"], note))
        if names:
            print("          named: " + ", ".join(names))
        if verdict != "CAUGHT":
            missed.append(verdict + " " + entry["name"])
            if not args.keep_going and verdict in ("MISSED", "RESTORE FAILED"):
                break
    if missed:
        print("\nnot caught by the guard: " + "; ".join(missed))
        return 1
    print("\nevery mutation was caught and named")
    return 0


if __name__ == "__main__":
    sys.exit(main())
