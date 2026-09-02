#!/usr/bin/env python3
"""Generate a table-driven dispatch for StepRepresentationPayloadBuilder.curveEvaluator.

Replaces the ~50-branch `if/else-if (curve instanceof X)` chain (which builds and
returns a CurveEvaluator) with an ordered rule list CURVE_EVALUATOR_RULES plus a
dispatch method. Branch order is preserved exactly, so first-match-wins semantics are
identical to the original chain and the golden export SHA is unchanged.

Output:
  - target/curve-evaluator-new.java  (rewritten source for review)
  - src/test/resources/curve-evaluator-dispatch-order.txt  (frozen 50-type order)

Idempotency guard: aborts if the source already contains CURVE_EVALUATOR_RULES.
"""
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
SRC = ROOT / "src/main/java/com/minicad/export/json/StepRepresentationPayloadBuilder.java"
ORDER_FILE = ROOT / "src/test/resources/curve-evaluator-dispatch-order.txt"
OUT = ROOT / "target/curve-evaluator-new.java"

HEADER_RE = re.compile(r"^\s*(?:\} else )?if \(curve instanceof (\w+)\) \{$")
ELSE_RE = re.compile(r"^\s*\} else \{$")
INDENT = "    "  # 4 spaces, class-member base


def main():
    lines = SRC.read_text(encoding="utf-8").split("\n")
    if any("CURVE_EVALUATOR_RULES" in ln for ln in lines):
        raise SystemExit(
            "ABORT: source already contains CURVE_EVALUATOR_RULES.\n"
            "       Restore the original first: git checkout -- " + str(SRC)
        )

    # 1) locate method + chain
    method_open = None
    for i, ln in enumerate(lines):
        if ln.strip().startswith("public static CurveEvaluator curveEvaluator("):
            method_open = i
            break
    if method_open is None:
        raise SystemExit("ABORT: curveEvaluator method not found")

    # collect all branch header indices (first `if` + `} else if`)
    headers = []
    for i in range(method_open, len(lines)):
        m = HEADER_RE.match(lines[i])
        if m:
            headers.append((i, m.group(1)))
        if ELSE_RE.match(lines[i]):
            else_header = i
            break
    else:
        raise SystemExit("ABORT: no `} else {` terminal found in chain")

    if not headers:
        raise SystemExit("ABORT: no curveEvaluator branches found")

    # else_close = the `}` that closes the else block (first `}` after `return null;`)
    else_close = None
    for j in range(else_header + 1, len(lines)):
        if lines[j].strip() == "}":
            else_close = j
            break
    if else_close is None:
        raise SystemExit("ABORT: else block close not found")

    # 2) extract branch bodies (header+1 .. next header), last branch .. else_header
    branches = []
    for k, (h, t) in enumerate(headers):
        start = h + 1
        end = headers[k + 1][0] if k + 1 < len(headers) else else_header
        body = [ln for ln in lines[start:end] if ln.strip() != ""]
        if not body:
            raise SystemExit(f"ABORT: empty body for branch {t}")
        branches.append((t, body))

    # 3) emit dispatch call + support block, replacing method body (after comment).
    # The method's closing `}` (else_close + 1) must come BEFORE the support block so
    # the record/interface/static field/static method land at class level, not inside
    # curveEvaluator's body (Java forbids `private static` members in a method).
    dispatch_call = ["        return dispatchCurveEvaluator(curve, builder);", "    }"]
    support = build_support(branches)
    before = lines[0 : method_open + 2]          # signature + comment line
    after = lines[else_close + 2 :]               # rest of file after method close

    new_lines = before + dispatch_call + support + after

    # 4) write generated source (raw bytes, explicit CRLF to avoid double-CR on Windows)
    OUT.parent.mkdir(parents=True, exist_ok=True)
    if OUT.exists():
        OUT.unlink()
    OUT.write_bytes(("\r\n".join(new_lines) + "\r\n").encode("utf-8"))

    # 5) frozen order file
    ORDER_FILE.parent.mkdir(parents=True, exist_ok=True)
    if ORDER_FILE.exists():
        ORDER_FILE.unlink()
    header = [
        "# Frozen dispatch order for StepRepresentationPayloadBuilder.curveEvaluator.",
        f"# {len(branches)} leaf curve types, in first-match-wins order. Edit ONLY by re-running",
        "# tools/gen_curve_evaluator_dispatch.py after a behaviour-preserving change.",
        "# The order is load-bearing: subtypes must precede their supertypes",
        "# (e.g. StepBSplineCurveWithKnots before StepBSplineCurve, StepCircle2D before",
        "# StepCurve2D, StepCurve/StepBoundedCurve are the trailing catch-alls).",
        "",
    ]
    ORDER_FILE.write_bytes(("\r\n".join(header + [t for t, _ in branches]) + "\r\n").encode("utf-8"))

    print(f"branches: {len(branches)}")
    print(f"wrote {OUT} ({len(new_lines)} lines)")
    print(f"wrote {ORDER_FILE}")


def build_support(branches):
    out = []
    out.append("")
    out.append("    // curveEvaluator dispatch table.")
    out.append("    // First-match-wins, mirroring the original if/else-if chain. The order is")
    out.append("    // load-bearing: a subtype rule must precede its supertype rule, otherwise the")
    out.append("    // supertype would match first and drop the more specific evaluator.")
    out.append("    private record CurveEvalRule(Class<?> type, CurveEvalHandler handler) {")
    out.append("        boolean matches(StepEntity entity) {")
    out.append("            return type.isInstance(entity);")
    out.append("        }")
    out.append("    }")
    out.append("")
    out.append("    private interface CurveEvalHandler {")
    out.append("        CurveEvaluator handle(StepEntity curve, StepCadBuilder builder);")
    out.append("    }")
    out.append("")
    out.append("    private static CurveEvalRule curveEvalRule(Class<?> type, CurveEvalHandler handler) {")
    out.append("        return new CurveEvalRule(type, handler);")
    out.append("    }")
    out.append("")
    out.append("    private static final List<CurveEvalRule> CURVE_EVALUATOR_RULES = List.of(")
    for idx, (t, body) in enumerate(branches):
        comma = "," if idx + 1 < len(branches) else ""
        out.append(f"        curveEvalRule({t}.class, (curve, builder) -> {{")
        out.extend(body)  # verbatim, already indented at 12 spaces
        out.append(f"        }}){comma}")
    out.append("    );")
    out.append("")
    out.append("    private static CurveEvaluator dispatchCurveEvaluator(StepEntity curve, StepCadBuilder builder) {")
    out.append("        for (CurveEvalRule rule : CURVE_EVALUATOR_RULES) {")
    out.append("            if (rule.matches(curve)) {")
    out.append("                return rule.handler().handle(curve, builder);")
    out.append("            }")
    out.append("        }")
    out.append("        return null;")
    out.append("    }")
    out.append("")
    return out


if __name__ == "__main__":
    main()
