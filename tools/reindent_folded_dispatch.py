"""Restore the hanging indentation of multi-line returns in already-folded tables.

The first generators in the validateXxxEntity family rendered branch bodies with
`line.strip()` per line. That is correct for single-statement bodies, but it
left-aligns the continuation lines of a multi-line return:

    return validateSummaryEntity(appliedGroupAssignment.assignedGroup(), builder)
    + validateSummaryItems(appliedGroupAssignment.items(), builder);

which reads as two statements instead of one. The parameterized generator fixes
this via `reindent()` (re-base to the target indent, keep relative offsets), but
the tables committed before that fix still carry the flattened form.

This script repairs them without re-deriving anything by hand:

  1. read the ORIGINAL chain from the commit just before the fold, so the
     indentation comes from the source of truth rather than a guess;
  2. re-render the table entries with the fixed `reindent()`;
  3. refuse to write unless the re-rendered entries are byte-identical to the
     live ones after whitespace normalization -- that is the proof this is a
     cosmetic change and not a silent rewrite;
  4. splice the entries back into the live table.

Usage:
    python tools/reindent_folded_dispatch.py [--check]
"""

import argparse
import os
import pathlib
import re
import subprocess
import sys
import tempfile

sys.path.insert(0, str(pathlib.Path(__file__).resolve().parent))

from gen_validate_entity_dispatch import (  # noqa: E402
    BRANCH_START,
    FIRST_MATCH,
    SRC,
    derive,
    extract_branches,
    method_bounds,
    read_lines,
    render_entries,
)

REL_SRC = "src/main/java/com/minicad/app/StepDumpApp.java"

# method -> the commit that folded it (the original chain lives in <commit>~1).
FOLDED = [
    ("validateManagementAssignmentEntity", "f3078e23"),
    ("validateProductStructureEntity", "bc8fed4d"),
]


def normalize(text):
    """Collapse all whitespace so only the tokens remain."""
    return re.sub(r"\s+", " ", text).strip()


def original_branches(method, commit):
    """Extract the pre-fold chain, keeping its original indentation."""
    blob = subprocess.run(
        ["git", "show", "%s~1:%s" % (commit, REL_SRC)],
        capture_output=True,
        text=True,
        check=True,
    ).stdout
    with tempfile.NamedTemporaryFile(
        "w", suffix=".java", delete=False, encoding="utf-8", newline=""
    ) as handle:
        handle.write(blob)
        temp = pathlib.Path(handle.name)
    try:
        lines = read_lines(temp)
        sig = "    private static %s %s(" % ("Integer", method)
        _, body_start, terminal, _ = method_bounds(lines, sig)
        first = next(
            i
            for i in range(body_start, terminal)
            if lines[i].strip().startswith(BRANCH_START)
        )
        branches, _ = extract_branches(lines, first, terminal)
        return branches
    finally:
        temp.unlink()


def table_span(lines, names):
    """Locate the entry region of the live table: (first_entry, close_paren)."""
    opener = "    private static final List<%s> %s = List.of(" % (
        names["record"],
        names["table"],
    )
    start = next(i for i, ln in enumerate(lines) if ln.rstrip() == opener)
    close = next(i for i in range(start + 1, len(lines)) if lines[i].rstrip() == "    );")
    return start + 1, close


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument(
        "--check",
        action="store_true",
        help="report what would change without writing",
    )
    args = parser.parse_args()

    lines = read_lines(SRC)
    changed = 0

    for method, commit in FOLDED:
        names = derive(method)
        branches = original_branches(method, commit)
        rendered = render_entries(names, branches, FIRST_MATCH)

        first, close = table_span(lines, names)
        live = lines[first:close]

        # Proof of equivalence: the token streams must match exactly. If they do
        # not, the live table has drifted from the original chain and this script
        # must not touch it.
        if normalize("\n".join(live)) != normalize("\n".join(rendered)):
            raise SystemExit(
                "ABORT: %s table does not match the pre-fold chain token-for-token; "
                "refusing to rewrite it." % names["table"]
            )

        flattened = sum(1 for ln in live if re.match(r"^ {12}[+.]", ln))
        if live == rendered:
            print("%-36s already correct" % method)
            continue

        print(
            "%-36s %d entries, %d flattened continuation line(s) -> re-indented"
            % (method, len(branches), flattened)
        )
        # An entry spans several physical lines, so it arrives with embedded
        # "\n". Split it back into lines or the splice leaves LF-only lines
        # inside a CRLF file (219 of them, in the first run of this script).
        lines[first:close] = "\n".join(rendered).split("\n")
        changed += 1

    if not changed:
        print("nothing to do")
        return

    if args.check:
        print("\n--check: %d table(s) would be re-indented, nothing written" % changed)
        return

    # Line endings are the whole difficulty here, and getting them wrong touches
    # every line in the file:
    #   * the working-tree file can be CRLF while the git blob is LF
    #     (core.autocrlf=true converts on checkout only, never back on write),
    #     so the terminator must be detected from the file being rewritten. An
    #     earlier version of this script detected it from git, wrote LF into a
    #     CRLF file, and spotless:check rejected all 3k lines.
    #   * `read_lines` normalizes to LF and splits on "\n", so the trailing
    #     newline survives as a final empty element: joining already restores it,
    #     and appending another terminator would leave a blank line at EOF.
    original = SRC.read_bytes()
    crlf_count = original.count(b"\r\n")
    lf_only = original.count(b"\n") - crlf_count
    if crlf_count and lf_only:
        raise SystemExit(
            "ABORT: %s mixes CRLF (%d) and LF (%d) line endings; refusing to "
            "guess which one to write." % (REL_SRC, crlf_count, lf_only)
        )
    terminator = "\r\n" if crlf_count else "\n"

    # newline="" disables Python's own translation, so `terminator` is written
    # verbatim instead of being rewritten to the platform default. Every line --
    # including the ones embedded in a multi-line table entry -- is normalized
    # first, because joining the outer list only fixes the newline between lines.
    with open(SRC, "w", encoding="utf-8", newline="") as handle:
        handle.write(terminator.join(ln.rstrip("\r") for ln in lines))
        handle.flush()
        os.fsync(handle.fileno())

    # Cheap self-check: the terminator count must match the newline count,
    # otherwise this run just produced a mixed-endings file and spotless will
    # reject every line.
    written = SRC.read_bytes()
    total = written.count(b"\n")
    matched = written.count(b"\r\n") if terminator == "\r\n" else total - written.count(b"\r\n")
    if matched != total:
        # Leave the file byte-identical to how we found it rather than a
        # half-rewritten mess.
        SRC.write_bytes(original)
        raise SystemExit(
            "ABORT: wrote mixed line endings (%d of %d lines got %r); original "
            "restored." % (matched, total, terminator)
        )

    print(
        "\nrewrote %s (%d table(s), %s line endings)"
        % (REL_SRC, changed, "CRLF" if crlf_count else "LF")
    )


if __name__ == "__main__":
    main()
