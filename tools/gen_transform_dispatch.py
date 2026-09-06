"""Re-freeze the dispatch order files for the StepCadGeometryOps transform chains.

Companion to the three tables folded out of the transformCurve3 (13-branch),
transformCurve2 (10-branch) and transformSurfaceGeometry (16-branch)
instanceof chains. Like tools/gen_reverse_dispatch.py -- which this is modeled
on -- it reads the already-folded table and rewrites only the frozen
expectation under src/test/resources; it never edits the host source.

Use it when a branch is legitimately added to or removed from a table: update
the table, run this, and review the diff to src/test/resources. The diff IS the
review -- a reordering you did not intend will show up here, which is the whole
point of freezing the order.

Usage:
    python tools/gen_transform_dispatch.py
"""
import re
from pathlib import Path

HOST = Path("src/main/java/com/minicad/step/semantic/StepCadGeometryOps.java")

TABLES = [
    ("TRANSFORM_CURVE3_RULES", Path("src/test/resources/transform-curve3-dispatch-order.txt")),
    ("TRANSFORM_CURVE2_RULES", Path("src/test/resources/transform-curve2-dispatch-order.txt")),
    ("TRANSFORM_SURFACE_RULES", Path("src/test/resources/transform-surface-dispatch-order.txt")),
]

HEADER_COMMENT = {
    "TRANSFORM_CURVE3_RULES": (
        "# Branch order of StepCadGeometryOps.transformCurve3, captured from the original\n"
        "# 13-branch `if (curve instanceof X)` chain before it was folded into\n"
        "# TRANSFORM_CURVE3_RULES.\n"
        "#\n"
        "# Every branch returns, so this is first-match-wins dispatch: instanceof also\n"
        "# matches subtypes, so a dropped, duplicated or reordered rule silently changes\n"
        "# which geometry is transformed. The 13 types are final classes implementing\n"
        "# Curve3 directly (no subtype relation today), so the order is currently\n"
        "# behaviour neutral -- the frozen file turns any future reordering into a test\n"
        "# failure rather than a silent behaviour change.\n"
    ),
    "TRANSFORM_CURVE2_RULES": (
        "# Branch order of StepCadGeometryOps.transformCurve2, captured from the original\n"
        "# 10-branch `if (curve instanceof X)` chain before it was folded into\n"
        "# TRANSFORM_CURVE2_RULES.\n"
        "#\n"
        "# Every branch returns, so this is first-match-wins dispatch: instanceof also\n"
        "# matches subtypes, so a dropped, duplicated or reordered rule silently changes\n"
        "# which geometry is transformed. The 10 types are final classes implementing\n"
        "# Curve2 directly (no subtype relation today), so the order is currently\n"
        "# behaviour neutral -- the frozen file turns any future reordering into a test\n"
        "# failure rather than a silent behaviour change.\n"
    ),
    "TRANSFORM_SURFACE_RULES": (
        "# Branch order of StepCadGeometryOps.transformSurfaceGeometry, captured from the\n"
        "# original 16-branch `if (surface instanceof X)` chain before it was folded into\n"
        "# TRANSFORM_SURFACE_RULES.\n"
        "#\n"
        "# Every branch returns, so this is first-match-wins dispatch: instanceof also\n"
        "# matches subtypes, so a dropped, duplicated or reordered rule silently changes\n"
        "# which geometry is transformed. The 16 types are final classes implementing\n"
        "# SurfaceGeometry directly (no subtype relation today), so the order is\n"
        "# currently behaviour neutral -- the frozen file turns any future reordering\n"
        "# into a test failure rather than a silent behaviour change.\n"
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
        print("%-26s -> %s (%d types)" % (table_field, out, len(types)))


if __name__ == "__main__":
    main()
