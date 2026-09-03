#!/usr/bin/env python3
"""Generate a null-fallthrough table-driven dispatch for StepFacePayloadBuilder.buildPreviewFaceResult.

The original method is a sequence of `if (previewGeometry instanceof X) { ... }` blocks (NOT an
else-if chain). Most branches unconditionally return a PreviewFaceResult; a few (the ones whose
return is gated on `if (geometry instanceof X)`) fall through to the next block when geometry is a
wrapped surface (e.g. StepRectangularTrimmedSurface) whose unwrapped inner surface matches but the
wrapper itself does not. Those fall-through blocks are the load-bearing fallback for wrapped surfaces
(e.g. the OR-compound at the original line ~336 catches a cylindrical surface that fell through the
dedicated StepCylindricalSurface block).

This generator replaces the sequence with a table PREVIEW_FACE_RULES plus dispatchPreviewFace:
  - each rule = (primary type, Predicate<StepEntity> over previewGeometry, handler);
  - the handler returns a non-null PreviewFaceResult to adopt it, or null to continue to the next rule
    (replicating the original fall-through);
  - branches whose return is gated on `if (geometry instanceof X)` get `return null;` appended so the
    handler falls through instead of implicitly returning void.

Order is preserved exactly, so first-match-with-fallthrough semantics are identical to the original
sequence; the golden export test is the safety net.

Output:
  - target/preview-face-new.java  (rewritten source for review)
  - src/test/resources/preview-face-dispatch-order.txt  (frozen primary types, in order)

Idempotency guard: aborts if the source already contains PREVIEW_FACE_RULES.
"""
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
SRC = ROOT / "src/main/java/com/minicad/export/json/StepFacePayloadBuilder.java"
ORDER_FILE = ROOT / "src/test/resources/preview-face-dispatch-order.txt"
OUT = ROOT / "target/preview-face-new.java"

BRANCH_START_RE = re.compile(r"^\s*if \(previewGeometry instanceof (\w+)")
HEADER_CLOSE_RE = re.compile(r"^\s*}\s*else\s*\{$")
TYPE_RE = re.compile(r"previewGeometry instanceof (\w+)")
PREDICATE_IMPORT = "import java.util.function.Predicate;"

# Single-type branches whose body only returns inside `if (geometry instanceof X)` -> they fall
# through. Append `return null;` to replicate the original fall-through to the next block.
FALLTHROUGH = {
    "StepCylindricalSurface",
    "StepConicalSurface",
    "StepToroidalSurfaceWithSpecifiedBends",
    "StepToroidalSurface",
}


def main():
    lines = SRC.read_text(encoding="utf-8").split("\n")
    if any("PREVIEW_FACE_RULES" in ln for ln in lines):
        raise SystemExit(
            "ABORT: source already contains PREVIEW_FACE_RULES.\n"
            "       Restore the original first: git checkout -- " + str(SRC)
        )

    method_open = None
    for i, ln in enumerate(lines):
        if ln.strip().startswith("public static PreviewFaceResult buildPreviewFaceResult("):
            method_open = i
            break
    if method_open is None:
        raise SystemExit("ABORT: buildPreviewFaceResult not found")

    # previewGeometry assignment line (dispatch starts right after it)
    preview_line = None
    for i in range(method_open, len(lines)):
        if "StepEntity previewGeometry = unwrapParametricPreviewSurface(geometry);" in lines[i]:
            preview_line = i
            break
    if preview_line is None:
        raise SystemExit("ABORT: previewGeometry assignment not found")

    # default terminal start
    terminal_line = None
    for i in range(preview_line, len(lines)):
        if lines[i].strip().startswith("String unsupportedSurface = describeUnsupportedPreviewSurface("):
            terminal_line = i
            break
    if terminal_line is None:
        raise SystemExit("ABORT: default terminal not found")

    # method close = the `}` after the terminal's return
    method_close = None
    for i in range(terminal_line, len(lines)):
        if lines[i].strip() == "}":
            method_close = i
            break
    if method_close is None:
        raise SystemExit("ABORT: method close not found")

    # collect branch blocks: sequential top-level `if (previewGeometry instanceof ...)` blocks.
    # Branches are NOT nested, so each branch body runs from its header-close `{` up to the
    # next branch start (or the default terminal). No brace counting needed.
    starts = [i for i in range(preview_line + 1, terminal_line)
              if BRANCH_START_RE.match(lines[i])]
    if not starts:
        raise SystemExit("ABORT: no previewGeometry dispatch branches found")

    branches = []
    for si, s in enumerate(starts):
        # header spans from s until the line containing `) {`
        he = s
        while he < terminal_line and ") {" not in lines[he]:
            he += 1
        if he >= terminal_line:
            raise SystemExit(f"ABORT: no header close for branch starting at {s}")
        types = TYPE_RE.findall(" ".join(lines[s:he + 1]))
        # Brace-match the body: find the `}` that closes the `{` on the header line. This is
        # robust against bodies that end in a nested block `}` immediately before the branch
        # close (e.g. `... });\n}`), where a naive "pop all trailing `}`" would wrongly eat the
        # nested close and unbalance the generated code. The matched `}` line is excluded from
        # the body, so the handler body is always balanced.
        depth = 0
        close_line = None
        for li in range(he, terminal_line):
            for ch in lines[li]:
                if ch == "{":
                    depth += 1
                elif ch == "}":
                    depth -= 1
            if depth == 0:
                close_line = li
                break
        if close_line is None:
            raise SystemExit(f"ABORT: unbalanced branch starting at {s}")
        raw = lines[he + 1:close_line]
        # strip leading/trailing blank lines
        while raw and raw[0].strip() == "":
            raw.pop(0)
        while raw and raw[-1].strip() == "":
            raw.pop()
        # drop inter-branch trailing comment lines (they belong to the next branch)
        while raw and (raw[-1].strip().startswith("//") or raw[-1].strip().startswith("/*")):
            raw.pop()
        body = [ln for ln in raw if ln.strip() != ""]
        if not body:
            raise SystemExit(f"ABORT: empty body for branch {types}")
        branches.append((types, body))

    # build support block + replacement
    dispatch_call = [
        "        return dispatchPreviewFace(stepFace, geometry, previewGeometry, builder, metadata);",
        "    }",
    ]
    support = build_support(branches)
    # default terminal verbatim (tail of dispatchPreviewFace)
    default_terminal = [ln for ln in lines[terminal_line:method_close] if ln.strip() != ""]
    # build_support ends with ["    }", ""] (dispatch method close, then a trailing blank).
    # Splice the default terminal INSIDE the method: drop the trailing blank and the closing
    # brace, insert the terminal, then re-append the closing brace + blank.
    support = support[:-2] + default_terminal + support[-2:]  # splice default terminal before dispatch close

    before = lines[0:preview_line + 1]          # up to and including previewGeometry assignment
    after = lines[method_close + 1:]            # rest of file after method close
    new_lines = before + dispatch_call + support + after

    # insert Predicate import if missing (after last java.util.* import)
    if PREDICATE_IMPORT not in new_lines:
        for idx in range(len(new_lines) - 1, -1, -1):
            if new_lines[idx].startswith("import java.util"):
                new_lines.insert(idx + 1, PREDICATE_IMPORT)
                break

    OUT.parent.mkdir(parents=True, exist_ok=True)
    if OUT.exists():
        OUT.unlink()
    OUT.write_bytes(("\r\n".join(new_lines) + "\r\n").encode("utf-8"))

    ORDER_FILE.parent.mkdir(parents=True, exist_ok=True)
    if ORDER_FILE.exists():
        ORDER_FILE.unlink()
    header = [
        "# Frozen dispatch order for StepFacePayloadBuilder.buildPreviewFaceResult (previewGeometry rules).",
        f"# {len(branches)} rules, in first-match-with-fallthrough order. Edit ONLY by re-running",
        "# tools/gen_preview_face_dispatch.py after a behaviour-preserving change.",
        "# Order is load-bearing: a wrapped-surface fallback (e.g. StepCylindricalSurface) sits AFTER the",
        "# dedicated block so a trimmed-wrapper surface falls through to the generic trimmed rule.",
        "# OR-compound rules list their first type as the primary; the no-duplicate guard is intentionally",
        "# skipped for this table because OR rules legitimately re-list types already handled earlier.",
        "",
    ]
    ORDER_FILE.write_bytes(("\r\n".join(header + [t[0] for t, _ in branches]) + "\r\n").encode("utf-8"))

    print(f"branches: {len(branches)}")
    print(f"fallthrough: {sum(1 for t,_ in branches if len(t)==1 and t[0] in FALLTHROUGH)}")
    print(f"wrote {OUT} ({len(new_lines)} lines)")
    print(f"wrote {ORDER_FILE}")


def build_support(branches):
    out = []
    out.append("")
    out.append("    // buildPreviewFaceResult dispatch table (previewGeometry rules).")
    out.append("    // First-match-with-fallthrough: a rule whose handler returns non-null is adopted; a")
    out.append("    // null return continues to the next rule (replicating the original sequential ifs,")
    out.append("    // where a wrapped surface falls through the dedicated block to the generic fallback).")
    out.append("    private record PreviewFaceRule(Class<?> type, Predicate<StepEntity> matches, PreviewFaceHandler handler) {")
    out.append("        boolean matches(StepEntity entity) {")
    out.append("            return matches.test(entity);")
    out.append("        }")
    out.append("    }")
    out.append("")
    out.append("    private interface PreviewFaceHandler {")
    out.append("        PreviewFaceResult handle(StepFaceEntity stepFace, StepEntity geometry, StepEntity previewGeometry,")
    out.append("                StepCadBuilder builder, StepMetadataExtractor.DisplayMetadata metadata);")
    out.append("    }")
    out.append("")
    out.append("    private static PreviewFaceRule previewFaceRule(Class<?> type, Predicate<StepEntity> matches, PreviewFaceHandler handler) {")
    out.append("        return new PreviewFaceRule(type, matches, handler);")
    out.append("    }")
    out.append("")
    out.append("    private static final List<PreviewFaceRule> PREVIEW_FACE_RULES = List.of(")
    for idx, (types, body) in enumerate(branches):
        comma = "," if idx + 1 < len(branches) else ""
        if len(types) == 1:
            primary = types[0]
            if primary in FALLTHROUGH:
                matches = f"{primary}.class::isInstance"
                handler_body = body + ["        return null;"]
            else:
                matches = f"{primary}.class::isInstance"
                handler_body = body
        else:
            or_expr = " || ".join(f"{t}.class.isInstance(e)" for t in types)
            matches = f"e -> {or_expr}"
            primary = types[0]
            handler_body = body
        out.append(f"        previewFaceRule({primary}.class, {matches}, (stepFace, geometry, previewGeometry, builder, metadata) -> {{")
        out.extend(handler_body)
        out.append(f"        }}){comma}")
    out.append("    );")
    out.append("")
    out.append("    private static PreviewFaceResult dispatchPreviewFace(")
    out.append("            StepFaceEntity stepFace, StepEntity geometry, StepEntity previewGeometry,")
    out.append("            StepCadBuilder builder, StepMetadataExtractor.DisplayMetadata metadata) {")
    out.append("        for (PreviewFaceRule rule : PREVIEW_FACE_RULES) {")
    out.append("            if (rule.matches(previewGeometry)) {")
    out.append("                PreviewFaceResult result = rule.handler().handle(stepFace, geometry, previewGeometry, builder, metadata);")
    out.append("                if (result != null) {")
    out.append("                    return result;")
    out.append("                }")
    out.append("            }")
    out.append("        }")
    # default terminal spliced in by caller before this closing brace
    out.append("    }")
    out.append("")
    return out


if __name__ == "__main__":
    main()
