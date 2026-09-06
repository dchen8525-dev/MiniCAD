"""Re-freeze the dispatch order files for StepGeometryReverser.

Companion to the two tables folded out of the reverseCurve3 (13-branch) and
reverseSurfaceSense (16-branch) instanceof chains. Unlike the other gen_*.py
scripts in this directory -- which slice an ORIGINAL if/else-if chain out of a
live source and rewrite the file -- this one reads the already-folded table and
rewrites only the frozen expectation under src/test/resources.

Use it when a branch is legitimately added to or removed from a table: update
the table, run this, and review the diff to src/test/resources. The diff IS the
review -- a reordering you did not intend will show up here, which is the whole
point of freezing the order.

Usage:
    python tools/gen_reverse_dispatch.py
"""
import re
from pathlib import Path

HOST = Path("src/main/java/com/minicad/step/semantic/StepGeometryReverser.java")

TABLES = [
    ("REVERSE_CURVE3_RULES", Path("src/test/resources/reverse-curve3-dispatch-order.txt")),
    ("REVERSE_SURFACE_RULES", Path("src/test/resources/reverse-surface-dispatch-order.txt")),
]

HEADER_COMMENT = {
    "REVERSE_CURVE3_RULES": (
        "# Branch order of StepGeometryReverser.reverseCurve3, captured from the original\n"
        "# 13-branch `if (curve instanceof X)` chain before it was folded into\n"
        "# REVERSE_CURVE3_RULES.\n"
        "#\n"
        "# Every branch returns, so this is first-match-wins dispatch: instanceof also\n"
        "# matches subtypes, so a dropped, duplicated or reordered rule silently changes\n"
        "# which geometry is reversed. The 13 types are final classes implementing Curve3\n"
        "# directly (no subtype relation today), so the order is currently behaviour\n"
        "# neutral -- the frozen file turns any future reordering into a test failure\n"
        "# rather than a silent behaviour change.\n"
    ),
    "REVERSE_SURFACE_RULES": (
        "# Branch order of StepGeometryReverser.reverseSurfaceSense, captured from the\n"
        "# original 16-branch `if (surface instanceof X)` chain before it was folded into\n"
        "# REVERSE_SURFACE_RULES.\n"
        "#\n"
        "# Every branch returns, so this is first-match-wins dispatch: instanceof also\n"
        "# matches subtypes, so a dropped, duplicated or reordered rule silently changes\n"
        "# which geometry is reversed. The 16 types are final classes implementing\n"
        "# SurfaceGeometry directly (no subtype relation today), so the order is currently\n"
        "# behaviour neutral -- the frozen file turns any future reordering into a test\n"
        "# failure rather than a silent behaviour change.\n"
    ),
}


def extract_types(source_text, table_field):
    """Type simple-names in declaration order, same rule as the guard test."""
    field = source_text.find(table_field + " = List.of(")
    if field < 0:
        raise SystemExit("ABORT: cannot find %s in %s" % (table_field, HOST))
    list_of = source_text.find("List.of(", field)
    paren = list_of + len("List.of")
    depth = 1
    close = -1
    for i in range(paren + 1, len(source_text)):
        c = source_text[i]
        if c == "(":
            depth += 1
        elif c == ")":
            depth -= 1
            if depth == 0:
                close = i
                break
    if close < 0:
        raise SystemExit("ABORT: unterminated %s table" % table_field)
    body = source_text[paren + 1:close]
    types = []
    for match in re.finditer(r"([\w.]+)\.class\s*,", body):
        fqn = match.group(1)
        types.append(fqn.rsplit(".", 1)[-1])
    return types


def main():
    source_text = HOST.read_text(encoding="utf-8")
    for table_field, out in TABLES:
        types = extract_types(source_text, table_field)
        if not types:
            raise SystemExit("ABORT: %s produced no entries" % table_field)
        out.write_text(HEADER_COMMENT[table_field] + "\n".join(types) + "\n",
                       encoding="utf-8")
        print("%-24s -> %s (%d types)" % (table_field, out, len(types)))


if __name__ == "__main__":
    main()
