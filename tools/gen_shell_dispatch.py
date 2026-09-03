#!/usr/bin/env python3
"""Fold StepShellBuilder.buildShell's 32-branch sequential-if chain into a
table-driven dispatch (SHELL_RULES).

The original chain (lines ~66..199) is a sequence of
`if (entity instanceof X) { ... }` branches, each returning or throwing -- so it
is *first-match-return* dispatch (no fall-through). The terminal (line 200) is a
throw, which the loop leaves in place after the table.

WHY THE HANDLER TAKES `self` + `id`:
buildShell is an *instance* method. Most branches call sibling instance methods
(buildFaceShell, buildConnectedFaceSet, ...) or the instance field `builder`, and
the StepSurfaceModel branch's throw message interpolates the method parameter
`id`. A static `List<>` of lambdas cannot capture `this`, so the handler is
    Shell build(StepShellBuilder self, StepEntity entity, int id);
and every lambda reaches instance state through `self.`. The for-loop passes `this`.

This is *not* verbatim-body-preserving at the source level (the `self.` prefix is
mandatory), so verify_shell_dispatch.py reconstructs the original branches by
stripping `self.` and asserts they match the committed chain verbatim.

This script edits the source in place:
  1. replaces the branch interior of buildShell with a for-loop dispatch
     (keeps the preamble `requireExistingEntity` and the terminal throw);
  2. inserts the record/interface/table/helper at class level, before buildShell;
  3. writes the frozen type order to src/test/resources/shell-dispatch-order.txt.

Idempotent: aborts if SHELL_RULES already exists.

GOTCHA: Java method-invocation argument lists FORBID a trailing comma. Only the
non-final table entries get a comma.
"""
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
SRC = ROOT / "src/main/java/com/minicad/step/semantic/StepShellBuilder.java"
ORDER_TXT = ROOT / "src/test/resources/shell-dispatch-order.txt"

TABLE_FIELD = "SHELL_RULES"
METHOD_SIG = "    Shell buildShell(int id) {"
TERMINAL_MARKER = "is not an OPEN_SHELL, SURFACED_OPEN_SHELL"
INSERT_BEFORE = METHOD_SIG

NEW_DISPATCH = [
    "        for (ShellRule rule : SHELL_RULES) {",
    "            if (rule.type().isInstance(entity)) {",
    "                return rule.handler().build(this, entity, id);",
    "            }",
    "        }",
]

TABLE_HEADER = [
    "    // buildShell dispatch table (first-match-return, mirrors the original sequential ifs).",
    "    private record ShellRule(Class<? extends StepEntity> type, ShellHandler handler) {}",
    "",
    "    private interface ShellHandler {",
    "        Shell build(StepShellBuilder self, StepEntity entity, int id);",
    "    }",
    "",
    "    private static ShellRule shellRule(Class<? extends StepEntity> type, ShellHandler handler) {",
    "        return new ShellRule(type, handler);",
    "    }",
    "",
    "    private static final List<ShellRule> " + TABLE_FIELD + " = List.of(",
]

HEADER_RE = re.compile(r"if \((.*)\)\s*\{$")
# NOTE: `(?!\s)` is essential -- a bare `^    ` also matches deeper-indented statement
# lines (they *start* with four spaces too), which would mis-read `return new Shell(`
# as a declaration of a method named `Shell` and selfify it into `new self.Shell(`.
MEMBER_RE = re.compile(r"^    (?!\s)(?!static\b)(?:private |public |protected |final )*[\w<>\[\], .?]+ (\w+)\(")
FIELD_RE = re.compile(r"^    (?!\s)(?!static\b)(?:private |public |protected |final )+[\w<>\[\], .?]+ (\w+) *(?:=|;)")

SELF_TYPE = "StepShellBuilder"
RESULT_TYPE = "Shell"
RULE_RECORD = "ShellRule"
RULE_FACTORY = "shellRule"


def instance_members(text):
    """Return (field_names, method_names) declared at class level (non-static)."""
    fields, methods = set(), set()
    for line in text.split("\n"):
        m = MEMBER_RE.match(line)
        if m:
            methods.add(m.group(1))
            continue
        m = FIELD_RE.match(line)
        if m:
            fields.add(m.group(1))
    return fields, methods


def make_selfify(fields, methods):
    def selfify(line):
        """Prefix instance field/method references so a static lambda can reach them."""
        for f in sorted(fields, key=len, reverse=True):
            line = re.sub(r"(?<![\w.])" + re.escape(f) + r"(?![\w])", "self." + f, line)
        for mth in sorted(methods, key=len, reverse=True):
            line = re.sub(r"(?<![\w.])" + re.escape(mth) + r"\(", "self." + mth + "(", line)
        # `x.self.foo` must never happen: collapse any stray `self.self.`
        line = line.replace("self.self.", "self.")
        return line

    return selfify


def extract_branches(lines):
    """Return list of (type_name, condition, body_lines, is_guarded) for each branch."""
    branches = []
    i = 0
    n = len(lines)
    while i < n:
        line = lines[i]
        stripped = line.strip()
        if stripped.startswith("if (entity instanceof ") and stripped.endswith("{"):
            m = HEADER_RE.search(line)
            condition = m.group(1)
            type_name = re.search(r"instanceof (\w+)", condition).group(1)
            is_guarded = "&&" in condition
            depth = 1
            body = []
            k = i + 1
            while k < n:
                for ch in lines[k]:
                    if ch == "{":
                        depth += 1
                    elif ch == "}":
                        depth -= 1
                if depth == 0:
                    break
                body.append(lines[k])
                k += 1
            while body and body[0].strip() == "":
                body.pop(0)
            while body and body[-1].strip() == "":
                body.pop()
            branches.append((type_name, condition, body, is_guarded))
            i = k + 1
            continue
        i += 1
    return branches


def terminal_start(lines, marker_idx):
    """Back up from the marker line to the first line of the terminal statement.

    The marker text lives inside a *string literal* on the second line of
        throw new StepResolutionException(
                "entity #" + id + " is not an OPEN_SHELL, ..."
        );
    so slicing `lines[bi:marker_idx]` would swallow the `throw new ...(` opener and
    leave a dangling string literal. Walk back to the statement's first line.
    """
    k = marker_idx
    while k > 0 and not lines[k].strip().startswith("throw "):
        k -= 1
    return k


def main() -> None:
    text = SRC.read_text(encoding="utf-8")
    if TABLE_FIELD in text:
        raise SystemExit("ABORT: " + TABLE_FIELD + " already present; refactor applied?")
    lines = text.split("\n")

    mi = next(i for i, ln in enumerate(lines) if ln == METHOD_SIG)
    ti = terminal_start(
        lines, next(i for i in range(mi, len(lines)) if TERMINAL_MARKER in lines[i])
    )
    bi = next(
        i
        for i in range(mi + 1, ti)
        if lines[i].strip().startswith("if (entity instanceof ")
    )

    branches = extract_branches(lines[bi:ti])
    if not branches:
        raise SystemExit("ABORT: no branches extracted")
    guarded = [t for t, _c, _b, g in branches if g]
    if guarded:
        raise SystemExit("ABORT: guarded branch(es) present: " + ", ".join(guarded))

    fields, methods = instance_members("\n".join(lines))
    selfify = make_selfify(fields, methods)
    print("instance fields:", sorted(fields), "methods:", sorted(methods))

    entries = []
    types = []
    for type_name, _condition, body, _g in branches:
        types.append(type_name)
        transformed = "\n".join("            " + selfify(b.strip()) for b in body)
        entries.append(
            "        %s(%s.class, (self, entity, id) -> {\n%s\n        })"
            % (RULE_FACTORY, type_name, transformed)
        )

    # 1) replace the branch interior with the for-loop; keep preamble + terminal throw.
    lines[bi:ti] = NEW_DISPATCH + [""]

    # 2) insert the class-level table machinery before buildShell.
    # Java method-invocation arg lists forbid a trailing comma; only non-final get one.
    rendered = [e + "," if j < len(entries) - 1 else e for j, e in enumerate(entries)]
    ii = next(i for i, ln in enumerate(lines) if ln == INSERT_BEFORE)
    class_block = TABLE_HEADER + rendered + ["    );", ""]
    lines[ii:ii] = class_block

    SRC.write_text("\n".join(lines) + "\n", encoding="utf-8")

    # 3) write frozen type order (used by the dispatch-table guard test).
    ORDER_TXT.parent.mkdir(parents=True, exist_ok=True)
    ORDER_TXT.write_text("\n".join(types) + "\n", encoding="utf-8")

    print("OK: wrote", SRC, "with", len(branches), "rules; order ->", ORDER_TXT)


if __name__ == "__main__":
    main()
