#!/usr/bin/env python3
"""Parametric folder for the StepDumpApp `validateXxxEntity` dispatch family.

StepDumpApp holds ten sibling methods with an identical shape:

    private static Integer validateXxxEntity(StepEntity entity, StepCadBuilder builder) {
        if (entity instanceof StepA) { ...; return ...; }
        if (entity instanceof StepB) { ...; return ...; }
        ...
        return null;
    }

Two of them (validateManagementAssignmentEntity, validateProductStructureEntity)
were already folded by hand-copied one-off scripts. Copying the script a third
time is how a generator family rots, so this script replaces the per-method
scripts: everything that differs between siblings is derived from the method
name alone.

Derivation, for --method validateProductStructureEntity:
    base           ProductStructure         (strip `validate` / `Entity`)
    table field    PRODUCT_STRUCTURE_RULES
    record         ProductStructureRule
    handler        ProductStructureHandler
    factory        productStructureRule
    order file     src/test/resources/product-structure-dispatch-order.txt
    guard test     src/test/java/com/minicad/app/ProductStructureDispatchTableTest.java

which reproduces the identifiers the two hand-written folds already use, so the
family is uniform rather than merely automated.

Dispatch semantics are detected, not assumed:

  * every branch body exits (return/throw)  -> FIRST_MATCH
        for (Rule rule : RULES) {
            if (rule.type().isInstance(entity)) {
                return rule.handler().validate(entity, builder);
            }
        }
  * some branch body falls through          -> NULL_FALLTHROUGH
        for (Rule rule : RULES) {
            if (rule.type().isInstance(entity)) {
                Integer result = rule.handler().validate(entity, builder);
                if (result != null) {
                    return result;
                }
            }
        }
    A falling-through branch in a sequential-if chain keeps testing the
    *following* ifs, which the loop reproduces exactly -- provided no branch
    returns `null` on purpose. That case is rejected rather than guessed.

Usage:
    python tools/gen_validate_entity_dispatch.py --method validateAssignmentEntity --dry-run
    python tools/gen_validate_entity_dispatch.py --method validateAssignmentEntity \\
        --summary "the sum of validateSummaryEntity(...) over the entities it references"

Idempotent: aborts if the derived table field is already present.

GOTCHA: Java method-invocation argument lists FORBID a trailing comma. Only the
non-final table entries get a comma.
"""
import argparse
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
SRC = ROOT / "src/main/java/com/minicad/app/StepDumpApp.java"
MODEL_DIR = ROOT / "src/main/java/com/minicad/step/model"
TEST_DIR = ROOT / "src/test/java/com/minicad/app"
RES_DIR = ROOT / "src/test/resources"

# These four are the dispatch shape; the CLI overrides them per method so the
# same generator folds any static `instanceof` chain (the validateXxxEntity
# family is just the default). `SUBJECT` is the instanceof operand -- also the
# first lambda parameter. `TERMINAL` is the fallback statement after the chain.
RESULT_TYPE = "Integer"
PARAMS = ("entity", "builder")
SUBJECT = "entity"
HANDLER_METHOD = "validate"
TERMINAL = "return null;"
NEEDED_IMPORT = "import java.util.List;"
# IS_STATIC / PARAM_TYPES are the dispatch shape, derived per method from its
# declaration (see derive_shape): an instance method's lambdas capture `this`,
# so the table field is non-static and the handler takes only the subject.
IS_STATIC = True
PARAM_TYPES = ("StepEntity", "StepCadBuilder")

HEADER_RE = re.compile(r"if \((.*)\)\s*\{$")
# Kept as the entity-default for the historical reindent/audit importers; live
# branch detection derives the subject from `--params` instead.
BRANCH_START = "if (entity instanceof "

FIRST_MATCH = "FIRST_MATCH"
NULL_FALLTHROUGH = "NULL_FALLTHROUGH"


# --------------------------------------------------------------------------
# name derivation
# --------------------------------------------------------------------------
def derive(method):
    # The validateXxxEntity family was folded first and already ships tables
    # named ASSIGNMENT_RULES / MANAGEMENT_ASSIGNMENT_RULES / ... -- keep those
    # identifiers stable so the idempotency guard and guard tests keep working.
    if method.startswith("validate") and method.endswith("Entity"):
        base = method[len("validate"):-len("Entity")]
    else:
        # Strip a leading verb (build / get / make / ...) to get the noun the
        # table is "about": buildCurve3 -> Curve3, buildBooleanOperandSolid ->
        # BooleanOperandSolid. This is the generic path for every other chain.
        verb = re.match(r"^[a-z]+", method)
        base = method[verb.end():] if verb else method
    if not base or not base[0].isupper():
        raise SystemExit("ABORT: cannot derive a base name from: " + method)
    words = re.findall(r"[A-Z][a-z0-9]*", base)
    if "".join(words) != base:
        raise SystemExit(
            "ABORT: " + base + " is not plain CamelCase; acronyms would produce a "
            "misleading table name, wire this one up by hand"
        )
    return {
        "method": method,
        "base": base,
        "table": "_".join(w.upper() for w in words) + "_RULES",
        "record": base + "Rule",
        "handler": base + "Handler",
        "factory": base[0].lower() + base[1:] + "Rule",
        "slug": "-".join(w.lower() for w in words),
        "test_class": base + "DispatchTableTest",
    }


# --------------------------------------------------------------------------
# source analysis
# --------------------------------------------------------------------------
def read_lines(path):
    text = path.read_text(encoding="utf-8")
    return text.replace("\r\n", "\n").replace("\r", "\n").split("\n")


def method_bounds(lines, sig):
    """Return (method_idx, body_start, terminal_idx, end_idx).

    `sig` may be a bare method name or a declaration prefix; the method name is
    extracted so a `static` method declared without `private` still matches the
    validateXxxEntity family that is always `private static`.
    """
    if "(" in sig:
        m = re.search(r"(\w+)\s*\(", sig)
        name = m.group(1) if m else sig
    else:
        name = sig
    pat = re.compile(r"\b" + re.escape(name) + r"\s*\(")
    # A declaration opens its body on the same line (`) {`); a call site ends
    # with `);`. Prefer that to avoid matching `count = name(...)` call sites.
    hits = [
        i for i, ln in enumerate(lines)
        if pat.search(ln) and ln.rstrip().endswith("{")
    ]
    if len(hits) != 1:
        # Fall back to "preceded by modifiers / a return type" for methods
        # whose body brace is on the next line.
        pat2 = re.compile(r"\b(?:[a-z]+\s+)*\b" + re.escape(name) + r"\s*\(")
        hits = [i for i, ln in enumerate(lines) if pat2.search(ln)]
    if len(hits) != 1:
        raise SystemExit(
            "ABORT: expected exactly one declaration matching %r, found %d"
            % (name, len(hits))
        )
    mi = hits[0]
    end = next(i for i in range(mi + 1, len(lines)) if lines[i] == "    }")
    return mi, mi + 1, end - 1, end


def parse_params(decl_line):
    """Return [(type, name), ...] from a method declaration line.

    `Solid buildBooleanOperandSolid(StepEntity operand) {` -> [("StepEntity",
    "operand")]. Generics in params are not present in our chains; a comma can
    only separate parameters here.
    """
    m = re.search(r"\(([^)]*)\)", decl_line)
    if not m or not m.group(1).strip():
        return []
    out = []
    for part in m.group(1).split(","):
        part = part.strip()
        if not part:
            continue
        toks = part.split()
        out.append((" ".join(toks[:-1]), toks[-1]))
    return out


def declaration_text(lines, mi):
    """Full declaration, joining a signature that wraps across lines.

    A single declaration line cannot carry a wrapped parameter list:
    `public static FacePayload facePayloadFromTopologyFace(` has no closing
    paren, so parse_params() sees nothing and SUBJECT silently falls back to
    "entity" -- the branch search then dies with StopIteration instead of
    reporting the real problem.
    """
    out = []
    depth = 0
    for i in range(mi, min(mi + 40, len(lines))):
        out.append(lines[i])
        depth += lines[i].count("(") - lines[i].count(")")
        if depth <= 0 and "{" in lines[i]:
            break
    return " ".join(s.strip() for s in out)


def subject_type():
    """Declared type of the instanceof operand.

    The rule record is bounded by THIS, not by a hardcoded StepEntity: a chain
    dispatching on SurfaceGeometry must not emit `Class<? extends StepEntity>`,
    a symbol that may not even be imported in the host file. Fold 21 hit
    exactly that (COMPILATION ERROR: cannot find symbol StepEntity).
    """
    return PARAM_TYPES[0] if PARAM_TYPES else "StepEntity"


def derive_shape(decl, method, overrides):
    """Set the dispatch-shape globals from a declaration plus CLI overrides.

    Auto-detection (no override):
      * `static` in the declaration  -> STATIC, handler takes every parameter.
      * instance method              -> the lambdas capture `this`, so the
                                        handler takes only the instanceof
                                        subject and the table field is non-static.
      * return type / param types    -> read straight off the declaration.
      * handler method name          -> `validate` for validateXxxEntity, else
                                        `build` (override with --handler-method).

    This lets one generator fold any chain without per-method scripting.
    """
    global RESULT_TYPE, PARAMS, SUBJECT, HANDLER_METHOD, TERMINAL, IS_STATIC, PARAM_TYPES
    parsed = parse_params(decl)
    is_static = bool(re.search(r"\bstatic\b", decl))

    idx = decl.index(method + "(")
    before = decl[:idx].split()
    result_type = overrides.get("result_type") or (before[-1] if before else "void")

    if overrides.get("params"):
        pnames = [p.strip() for p in overrides["params"].split(",")]
    elif is_static:
        pnames = [n for _, n in parsed]
    else:
        pnames = [parsed[0][1]] if parsed else []

    subject = overrides.get("subject") or (pnames[0] if pnames else "entity")
    handler_method = overrides.get("handler_method")
    if not handler_method:
        handler_method = "validate" if method.startswith("validate") else "build"
    terminal = overrides.get("terminal") or "return null;"

    RESULT_TYPE = result_type
    PARAMS = tuple(pnames)
    SUBJECT = subject
    HANDLER_METHOD = handler_method
    TERMINAL = terminal
    IS_STATIC = is_static
    PARAM_TYPES = tuple(t for t, _ in parsed)
    return {
        "is_static": is_static,
        "result_type": result_type,
        "params": PARAMS,
        "subject": subject,
        "handler_method": handler_method,
        "terminal": terminal,
        "param_types": PARAM_TYPES,
    }


def javadoc_start(lines, mi):
    """Index of the method's javadoc opener, or mi when it has none.

    Inserting the table between a javadoc block and its method silently
    detaches the doc comment; fold 10 hit exactly that.
    """
    j = mi - 1
    while j >= 0 and lines[j].strip() == "":
        j -= 1
    if j >= 0 and lines[j].strip().endswith("*/"):
        while j >= 0 and not lines[j].strip().startswith("/**"):
            j -= 1
        return j
    return mi


STRING_RE = re.compile(r'"(?:\\.|[^"\\])*"')
CHAR_RE = re.compile(r"'(?:\\.|[^'\\])*'")


def find_constructors(lines, host_class):
    """Indices of every constructor declaration of `host_class`.

    A constructor is the only `<ClassName>(` that is not preceded by a return
    type, so `StepCadBuilder fromResolved(...)` (a factory) does not match while
    `StepCadBuilder(Map...) {` does.
    """
    pat = re.compile(r"\b" + re.escape(host_class) + r"\s*\(")
    cts = []
    for i, ln in enumerate(lines):
        if pat.search(ln) and ln.rstrip().endswith("{"):
            cts.append(i)
    if not cts:
        for i, ln in enumerate(lines):
            if pat.search(ln):
                j = i + 1
                while j < len(lines) and not lines[j].strip():
                    j += 1
                if j < len(lines) and lines[j].strip().endswith("{"):
                    cts.append(i)
    return cts


def constructor_end(lines, ci):
    """Index of the constructor's closing brace (matching the decl's `{`)."""
    depth = 0
    for k in range(ci, len(lines)):
        for ch in lines[k]:
            if ch == "{":
                depth += 1
            elif ch == "}":
                depth -= 1
        if depth == 0 and k > ci:
            return k
    return len(lines) - 1


def strip_literals(line):
    """Blank out string/char literals so brace counting cannot be fooled by text."""
    return CHAR_RE.sub("''", STRING_RE.sub('""', line))


def top_level_statements(body):
    """Indices of the first line of each depth-0 statement in a branch body.

    Needed because two shapes both defeat "look at the last non-empty line":

        return validateSummaryEntity(x.role(), builder)
                + validateSummaryItems(x.items(), builder);   // continuation line

        if (cond) {
            return 1;                                        // nested, can fall out
        }
    """
    stmts = []
    depth = 0
    pending = None
    for i, ln in enumerate(body):
        s = ln.strip()
        if not s or s.startswith("//"):
            continue
        if pending is None:
            pending = i
        bare = strip_literals(ln)
        depth += bare.count("{") - bare.count("}")
        if depth <= 0:
            depth = 0
            if s.endswith(";") or s.endswith("}"):
                stmts.append(pending)
                pending = None
    if pending is not None:
        stmts.append(pending)
    return stmts


def last_statement_exits(body):
    """True when the branch body cannot fall out of the if.

    Only the last *top-level* statement counts. A trailing `if (...) { return; }`
    is deliberately reported as falling through: it exits on one path only.
    """
    stmts = top_level_statements(body)
    if not stmts:
        return False
    s = body[stmts[-1]].strip()
    return s.startswith("return") or s.startswith("throw")


def extract_branches(lines, start, stop, subject="entity"):
    """Return (branches, region_end).

    branches: list of dicts with type/condition/body/guarded/exits.
    region_end: index just past the last branch's closing brace, so a caller can
    replace only the chain and leave whatever follows it untouched.
    `subject` is the instanceof operand (the first lambda parameter); it selects
    which `if (<subject> instanceof ...)` lines are branches.
    """
    branch_start = "if (%s instanceof " % subject
    branches = []
    region_end = start
    i = start
    while i < stop:
        stripped = lines[i].strip()
        if not stripped.startswith(branch_start):
            i += 1
            continue

        # A branch header can wrap onto following lines when it is an OR group:
        #
        #     if (entity instanceof StepManifoldSolidBrep
        #             || entity instanceof StepBrepWithVoids
        #             || entity instanceof StepCsgSolid) {
        #
        # so the header runs until the line that opens the body.
        header = i
        while not lines[header].strip().endswith("{") and header + 1 < stop:
            header += 1
        raw_header = " ".join(x.strip() for x in lines[i : header + 1])
        match = HEADER_RE.search(raw_header)
        if not match:
            # Not a header this extractor understands (e.g. a one-line body).
            # Leave it alone rather than crash; the contiguity check downstream
            # will refuse the fold if it sits inside the chain.
            i += 1
            continue
        condition = match.group(1)

        depth = 1
        body = []
        k = header + 1
        while k < stop:
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

        types = re.findall(r"instanceof ([\w.]+)", condition)
        if len(types) > 1:
            # Several types sharing one body is only table-driven if the condition
            # is a pure OR of instanceof tests. Anything else (a && guard, a test
            # that is not instanceof) is a real predicate and needs a different
            # rule shape -- refuse instead of silently dropping the guard.
            operands = [x.strip() for x in condition.split("||")]
            pure_or = (
                "&&" not in condition
                and all(
                    re.fullmatch(r"%s instanceof [\w.]+" % subject, o) for o in operands
                )
            )
            if not pure_or:
                raise SystemExit(
                    "ABORT: multi-type condition is not a pure OR of instanceof "
                    "tests, so it cannot become one rule per type:\n  " + condition
                )

        branches.append(
            {
                "type": types[0],
                "types": types,
                "simple": [t.split(".")[-1] for t in types],
                "condition": condition,
                "body": body,
                # A `&&` guard is a real predicate the plain type table cannot
                # express. A `||` OR of pure instanceof tests is NOT guarded: it
                # expands into one rule per type (handled by expand()/render).
                "guarded": "&&" in condition,
                "exits": last_statement_exits(body),
                "start": i,
                "end": k + 1,
            }
        )
        region_end = k + 1
        i = k + 1
    return branches, region_end


def supertypes(type_names):
    """Map type -> declared `extends` supertype, for the types in the table.

    A parent listed before its child makes the child unreachable; the guard test
    javadoc must not claim the order is inert unless it actually is.
    """
    out = {}
    for name in type_names:
        path = MODEL_DIR / (name + ".java")
        if not path.exists():
            out[name] = "<missing source>"
            continue
        m = re.search(
            r"\b(?:class|record|interface)\s+" + name + r"\b[^{]*?\bextends\s+(\w+)",
            path.read_text(encoding="utf-8"),
        )
        out[name] = m.group(1) if m else None
    return out


# --------------------------------------------------------------------------
# rendering
# --------------------------------------------------------------------------
def render_dispatch(names, mode):
    call = "rule.handler().%s(%s)" % (HANDLER_METHOD, ", ".join(PARAMS))
    head = "        for (%s rule : %s) {" % (names["record"], names["table"])
    if mode == FIRST_MATCH:
        return [
            head,
            "            if (rule.type().isInstance(%s)) {" % SUBJECT,
            "                return " + call + ";",
            "            }",
            "        }",
        ]
    return [
        head,
        "            if (rule.type().isInstance(%s)) {" % SUBJECT,
        "                %s result = %s;" % (RESULT_TYPE, call),
        "                if (result != null) {",
        "                    return result;",
        "                }",
        "            }",
        "        }",
    ]


def render_table_header(names, mode, static=True, param_types=None, param_names=None):
    semantics = (
        "first-match-return" if mode == FIRST_MATCH else "null means keep looking"
    )
    if param_types is None:
        param_types = PARAM_TYPES
    if param_names is None:
        param_names = PARAMS
    iface_params = ", ".join("%s %s" % (t, n) for t, n in zip(param_types, param_names))
    field_mod = "private static final" if static else "private final"
    header = [
        "    // %s dispatch table (%s," % (names["method"], semantics),
        "    // mirrors the original sequential ifs).",
        "    private record %s(" % names["record"],
        "            Class<? extends %s> type, %s handler) {}" % (subject_type(), names["handler"]),
        "",
        "    private interface %s {" % names["handler"],
        "        %s %s(%s);" % (RESULT_TYPE, HANDLER_METHOD, iface_params),
        "    }",
        "",
        "    private static %s %s(" % (names["record"], names["factory"]),
        "            Class<? extends %s> type, %s handler) {" % (subject_type(), names["handler"]),
        "        return new %s(type, handler);" % names["record"],
        "    }",
        "",
    ]
    if static:
        # Static table: the field is initialised inline with the rule list.
        header.append(
            "    %s List<%s> %s = List.of("
            % (field_mod, names["record"], names["table"])
        )
    else:
        # Instance table: the field is declared here but assigned inside the
        # constructor(s), because its lambdas capture `this` and read final
        # fields (builder / entitiesById / curveBuilder) that are only assigned
        # in the constructor -- a field initializer would hit Java's
        # definite-assignment rule ("might not have been initialized").
        header.append(
            "    %s List<%s> %s;" % (field_mod, names["record"], names["table"])
        )
    return header


def render_ctor_assignment(names, branches, mode):
    """The `RULES = List.of(...)` block injected into each constructor.

    Eight-space base (constructor body), entries at twelve spaces.
    """
    entries = render_entries(names, branches, mode, indent="            ")
    return ["        %s = List.of(" % names["table"]] + entries + ["        );", ""]


def reindent(body, indent="            "):
    """Re-base a branch body to `indent` while preserving relative indentation.

    Flattening every line with strip() would left-align the continuation lines of
    multi-line returns, e.g.

        return validateSummaryEntity(x.person(), builder)
        + validateSummaryEntity(x.organization(), builder);

    which reads as two statements. Re-basing keeps the continuation hanging.
    """
    meaningful = [ln for ln in body if ln.strip()]
    if not meaningful:
        return []
    base = min(len(ln) - len(ln.lstrip()) for ln in meaningful)
    return [indent + ln[base:].rstrip() if ln.strip() else "" for ln in body]


def expand(branches):
    """Flatten the chain into one (type, branch) pair per dispatched type.

    An OR group (`entity instanceof A || entity instanceof B`) serves one body
    for several types. The table stays "one rule per type" and repeats the body,
    because that keeps the frozen order file a flat list and keeps the guard
    test's `type()` accessor working unchanged; the cost is a repeated lambda for
    group members, which is acceptable since a group's body cannot cast to any
    one of its types anyway.
    """
    return [(t, br) for br in branches for t in br["types"]]


def render_entries(names, branches, mode, indent="        "):
    """Render the table entries.

    `indent` is the 4-space multiple at which each `factory(...)` call sits. The
    static table lives at class level (indent 8); the constructor assignment for
    an instance table lives one level deeper (indent 12), so the body is re-based
    four spaces further in.
    """
    body_indent = indent + "    "
    rendered_by_type = {}
    for type_name, br in expand(branches):
        body = reindent(br["body"], body_indent)
        if mode == NULL_FALLTHROUGH and not br["exits"]:
            # The original branch fell out of the if and kept testing the
            # following types; `return null` makes the loop do the same.
            body.append(body_indent + "return null;")
        rendered_by_type[type_name] = (
            indent + "%s(%s.class, (%s) -> {\n%s\n" % (
                names["factory"], type_name, ", ".join(PARAMS), "\n".join(body)
            )
            + indent + "})"
        )
    entries = [rendered_by_type[t] for t, _ in expand(branches)]
    return [e + "," if j < len(entries) - 1 else e for j, e in enumerate(entries)]


TEST_TEMPLATE = '''package {host_pkg};

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Guards the table-driven dispatch introduced for {host_class}.{method}.
 *
{intro}
 *
{item1}
{item2}
 *
 * src/test/resources/{slug}-dispatch-order.txt freezes the type order captured
 * from the original chain (see tools/gen_validate_entity_dispatch.py). This test
 * asserts the live table still matches it, pinning both the order and the
 * handler wiring-by-type.
 */
class {test_class} {{

    private static final Path FROZEN_ORDER =
            Paths.get("src/test/resources/{slug}-dispatch-order.txt");
    private static final String TABLE_FIELD = "{table}";

    @Test
    @DisplayName("{method} dispatch table keeps the original branch order")
    void dispatchTableShouldMatchFrozenOrder() throws Exception {{
        List<String> expected = frozenTypes();
        List<String> actual = liveHandlerTypes();

        assertEquals(expected.size(), actual.size(),
                "Dispatch table branch count changed. Expected " + expected.size()
                        + " branches from the original chain, found " + actual.size() + ".");
        assertEquals(expected, actual,
                "Dispatch table order/types changed. The table is ordered data, not "
                        + "control flow: instanceof matches subtypes and the first match wins, "
                        + "so reordering silently changes which entity is validated.");
    }}

    @Test
    @DisplayName("{method} dispatch table has no duplicate types")
    void dispatchTableShouldHaveNoDuplicateTypes() throws Exception {{
        List<String> actual = liveHandlerTypes();
        Set<String> seen = new HashSet<>();
        List<String> duplicates = new ArrayList<>();
        for (String type : actual) {{
            if (!seen.add(type)) {{
                duplicates.add(type);
            }}
        }}
        assertEquals(List.of(), duplicates,
                "Duplicate types in the dispatch table: later entries are unreachable, "
                        + "because the first match returns.");
    }}

    private static List<String> frozenTypes() throws IOException {{
        if (!Files.exists(FROZEN_ORDER)) {{
            fail("Missing frozen dispatch order at " + FROZEN_ORDER.toAbsolutePath()
                    + " - regenerate with tools/gen_validate_entity_dispatch.py");
        }}
        List<String> types = new ArrayList<>();
        for (String line : Files.readAllLines(FROZEN_ORDER, StandardCharsets.UTF_8)) {{
            String trimmed = line.trim();
            if (!trimmed.isEmpty() && !trimmed.startsWith("#")) {{
                types.add(trimmed);
            }}
        }}
        return types;
    }}

    @SuppressWarnings("unchecked")
    private static List<String> liveHandlerTypes() throws Exception {{
        Field field = {host_class}.class.getDeclaredField(TABLE_FIELD);
        field.setAccessible(true);
        List<?> rules = (List<?>) field.get(null);

        List<String> types = new ArrayList<>();
        for (Object rule : rules) {{
            Method accessor = rule.getClass().getDeclaredMethod("type");
            accessor.setAccessible(true);
            types.add(((Class<?>) accessor.invoke(rule)).getSimpleName());
        }}
        return types;
    }}
}}
'''

# Guard test for an INSTANCE-field table: the lambdas capture `this`, so the
# field is non-static and the guard test cannot reflect it off the class.
# Instead it reads the host source file (deterministically available at
# project root) and extracts the table's (.class) entries in declaration order,
# which still pins both the order and the per-type handler wiring.
TEST_TEMPLATE_INSTANCE = '''package {host_pkg};

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Guards the table-driven dispatch introduced for {host_class}.{method}.
 *
{intro}
 *
{item1}
{item2}
 *
 * src/test/resources/{slug}-dispatch-order.txt freezes the type order captured
 * from the original chain (see tools/gen_validate_entity_dispatch.py). The table
 * is an instance field whose lambdas capture `this`, so this test reads the host
 * source and extracts the table's (.class) entries in declaration order, pinning
 * both the order and the handler wiring-by-type.
 */
class {test_class} {{

    private static final Path FROZEN_ORDER =
            Paths.get("src/test/resources/{slug}-dispatch-order.txt");
    private static final String TABLE_FIELD = "{table}";
    private static final String HOST_SOURCE = "src/main/java/{host_path}.java";

    @Test
    @DisplayName("{method} dispatch table keeps the original branch order")
    void dispatchTableShouldMatchFrozenOrder() throws Exception {{
        List<String> expected = frozenTypes();
        List<String> actual = liveHandlerTypes();

        assertEquals(expected.size(), actual.size(),
                "Dispatch table branch count changed. Expected " + expected.size()
                        + " branches from the original chain, found " + actual.size() + ".");
        assertEquals(expected, actual,
                "Dispatch table order/types changed. The table is ordered data, not "
                        + "control flow: instanceof matches subtypes and the first match wins, "
                        + "so reordering silently changes which entity is validated.");
    }}

    @Test
    @DisplayName("{method} dispatch table has no duplicate types")
    void dispatchTableShouldHaveNoDuplicateTypes() throws Exception {{
        List<String> actual = liveHandlerTypes();
        Set<String> seen = new HashSet<>();
        List<String> duplicates = new ArrayList<>();
        for (String type : actual) {{
            if (!seen.add(type)) {{
                duplicates.add(type);
            }}
        }}
        assertEquals(List.of(), duplicates,
                "Duplicate types in the dispatch table: later entries are unreachable, "
                        + "because the first match returns.");
    }}

    private static List<String> frozenTypes() throws IOException {{
        if (!Files.exists(FROZEN_ORDER)) {{
            fail("Missing frozen dispatch order at " + FROZEN_ORDER.toAbsolutePath()
                    + " - regenerate with tools/gen_validate_entity_dispatch.py");
        }}
        List<String> types = new ArrayList<>();
        for (String line : Files.readAllLines(FROZEN_ORDER, StandardCharsets.UTF_8)) {{
            String trimmed = line.trim();
            if (!trimmed.isEmpty() && !trimmed.startsWith("#")) {{
                types.add(trimmed);
            }}
        }}
        return types;
    }}

    private static List<String> liveHandlerTypes() throws Exception {{
        if (!Files.exists(Paths.get(HOST_SOURCE))) {{
            fail("Cannot read " + HOST_SOURCE + " to verify the dispatch table order.");
        }}
        String text = Files.readString(Paths.get(HOST_SOURCE), StandardCharsets.UTF_8);
        int field = text.indexOf(TABLE_FIELD + " = List.of(");
        if (field < 0) {{
            fail("Cannot find " + TABLE_FIELD + " in " + HOST_SOURCE);
        }}
        // The table is assigned inside the constructor as
        // `NAME = List.of(entry, entry, ...)`. Count the `List.of(` opener's own
        // paren as depth 1 so the matching `)` is the List.of closer -- not the
        // first entry's closing paren (which would stop after one rule).
        int listOf = text.indexOf("List.of(", field);
        int paren = listOf + "List.of".length();
        int depth = 1;
        int close = -1;
        for (int i = paren + 1; i < text.length(); i++) {{
            char c = text.charAt(i);
            if (c == '(') {{
                depth++;
            }} else if (c == ')') {{
                depth--;
                if (depth == 0) {{
                    close = i;
                    break;
                }}
            }}
        }}
        if (close < 0) {{
            fail("Unterminated " + TABLE_FIELD + " table in " + HOST_SOURCE);
        }}
        String body = text.substring(paren + 1, close);
        List<String> types = new ArrayList<>();
        Matcher m = Pattern.compile("([\\\\w.]+)\\\\.class\\\\s*,").matcher(body);
        while (m.find()) {{
            String fqn = m.group(1);
            types.add(fqn.substring(fqn.lastIndexOf('.') + 1));
        }}
        return types;
    }}
}}
'''


def wrap_javadoc(paragraph, first=" * ", cont=" * ", width=78):
    """Wrap a paragraph into javadoc lines, keeping `code` tokens unbroken."""
    words = paragraph.split()
    out, line, prefix = [], "", first
    for w in words:
        candidate = (line + " " + w).strip()
        if line and len(prefix) + len(candidate) > width:
            out.append(prefix + line)
            prefix, line = cont, w
        else:
            line = candidate
    if line:
        out.append(prefix + line)
    return "\n".join(out)


def render_test(
    names, count, mode, summary, order_doc, host_class, host_pkg,
    static=True, host_path=None,
):
    semantics_doc = (
        "Every branch returns, so it is first-match-return dispatch with a "
        "terminal `return null;` for unsupported entities."
        if mode == FIRST_MATCH
        else "Some branches fall out of the if and keep testing the following "
        "types, so the table is walked until a handler returns non-null, with a "
        "terminal `return null;` for unsupported entities."
    )
    intro = wrap_javadoc(
        "The method used to be a %d-branch sequential-if chain that cast the STEP "
        "entity and returned %s. %s It is now an ordered list of (type, handler) "
        "rules. Two things can go wrong in that shape, and neither is visible to "
        "the compiler:" % (count, summary.strip().rstrip("."), semantics_doc)
    )
    item1 = wrap_javadoc(
        "a branch dropped, duplicated or reordered -- ordering is load-bearing "
        "because instanceof also matches subtypes and the first match wins. "
        + order_doc,
        first=" *   1. ",
        cont=" *      ",
    )
    item2 = wrap_javadoc(
        "a type wired to the wrong handler -- the compiler accepts any handler "
        "whose signature matches, so a copy/paste slip would compile cleanly.",
        first=" *   2. ",
        cont=" *      ",
    )
    if host_path is None:
        host_path = host_pkg.replace(".", "/") + "/" + host_class
    template = TEST_TEMPLATE if static else TEST_TEMPLATE_INSTANCE
    return template.format(
        method=names["method"],
        intro=intro,
        item1=item1,
        item2=item2,
        slug=names["slug"],
        table=names["table"],
        test_class=names["test_class"],
        host_class=host_class,
        host_pkg=host_pkg,
        host_path=host_path,
    )


# --------------------------------------------------------------------------
def main():
    global RESULT_TYPE, PARAMS, SUBJECT, HANDLER_METHOD, TERMINAL, SRC, IS_STATIC
    ap = argparse.ArgumentParser()
    ap.add_argument("--method", required=True, help="e.g. validateAssignmentEntity")
    ap.add_argument(
        "--summary",
        default="a per-type count over the entities it references",
        help="javadoc fragment describing what the handlers return",
    )
    ap.add_argument(
        "--source",
        default=str(SRC),
        help="java source file containing the method (default: StepDumpApp.java)",
    )
    ap.add_argument(
        "--result-type",
        default=RESULT_TYPE,
        help="method return type (default: Integer)",
    )
    ap.add_argument(
        "--params",
        default=",".join(PARAMS),
        help="comma-separated lambda parameters; first is the instanceof operand "
        "(default: entity,builder)",
    )
    ap.add_argument(
        "--terminal",
        default=TERMINAL,
        help="expected fallback statement after the chain (default: return null;)",
    )
    ap.add_argument(
        "--handler-method",
        default=HANDLER_METHOD,
        help="name of the handler interface method (default: validate)",
    )
    ap.add_argument(
        "--dry-run",
        action="store_true",
        help="analyse the chain and print the plan without touching any file",
    )
    args = ap.parse_args()

    SRC = Path(args.source).resolve()

    names = derive(args.method)

    text = SRC.read_text(encoding="utf-8")
    for ident in (names["table"], names["record"], names["handler"], names["factory"]):
        # Word-boundary, not substring: ASSIGNMENT_RULES is a substring of the
        # already-folded MANAGEMENT_ASSIGNMENT_RULES, and AssignmentRule of
        # ManagementAssignmentRule -- distinct identifiers, no collision.
        if re.search(r"\b" + re.escape(ident) + r"\b", text) and not args.dry_run:
            raise SystemExit(
                "ABORT: identifier %s already present; refactor applied?" % ident
            )
    lines = read_lines(SRC)

    mi, body_start, terminal, end = method_bounds(lines, args.method)
    # Derive the dispatch shape (static vs instance, return type, params, handler
    # name, terminal) from the declaration so one generator folds any chain. CLI
    # flags override only when explicitly different from the validate defaults.
    overrides = {}
    if args.result_type != "Integer":
        overrides["result_type"] = args.result_type
    if args.params != "entity,builder":
        overrides["params"] = args.params
    if args.handler_method != "validate":
        overrides["handler_method"] = args.handler_method
    if args.terminal != "return null;":
        overrides["terminal"] = args.terminal
    derive_shape(declaration_text(lines, mi), args.method, overrides)
    terminal_text = lines[terminal].strip()

    bi = next(
        i
        for i in range(body_start, terminal)
        if lines[i].strip().startswith("if (%s instanceof " % SUBJECT)
    )
    if bi != body_start:
        # Comments and blank lines before the chain are harmless (they stay put,
        # the fold only replaces the branch region). A real statement/declaration
        # preceding the chain would be a local the lambdas cannot capture, so it
        # still aborts.
        prefix = [
            lines[i]
            for i in range(body_start, bi)
            if lines[i].strip() and not lines[i].strip().startswith("//")
        ]
        if prefix:
            raise SystemExit(
                "ABORT: statements precede the chain and may declare locals the "
                "lambdas would need to capture:\n  " + "\n  ".join(prefix)
            )

    branches, region_end = extract_branches(lines, bi, terminal, SUBJECT)
    if len(branches) < 2:
        raise SystemExit("ABORT: %d branch(es) extracted; nothing to fold" % len(branches))

    # An OR group (A || B || C) becomes one rule per type, so the table count
    # is the expanded list, not the branch count.
    expanded = expand(branches)

    guarded = [b["type"] for b in branches if b["guarded"]]
    if guarded:
        raise SystemExit(
            "ABORT: predicate-guarded branch(es) present (a real `&&` guard the "
            "plain type table cannot express): " + ", ".join(guarded)
        )

    # An OR group contributes one rule per type, so duplicates are checked over
    # the expanded list -- a type repeated inside or across groups would make the
    # later rule unreachable.
    seen = set()
    for b in branches:
        for t in b["types"]:
            if t in seen:
                raise SystemExit("ABORT: duplicate type in chain: " + t)
            seen.add(t)

    # The fold replaces [first branch, last branch) in one slice, so anything
    # sitting *between* two branches would be deleted. Refuse rather than eat it.
    interleaved = []
    for prev, nxt in zip(branches, branches[1:]):
        for i in range(prev["end"], nxt["start"]):
            s = lines[i].strip()
            if s and not s.startswith("//"):
                interleaved.append("after %s: %s" % (prev["type"], s))
    if interleaved:
        raise SystemExit(
            "ABORT: the chain is not contiguous, %d statement(s) sit between "
            "branches and a single-slice fold would delete them:\n  %s"
            % (len(interleaved), "\n  ".join(interleaved))
        )

    # Comments/blank lines between the last branch and the terminal are harmless
    # (they stay put); a real statement would be unreachable code after the fold.
    tail = [
        lines[i]
        for i in range(region_end, terminal)
        if lines[i].strip() and not lines[i].strip().startswith("//")
    ]
    if tail:
        raise SystemExit(
            "ABORT: statements sit between the chain and the terminal return:\n  "
            + "\n  ".join(tail)
        )

    fallthrough = [b["type"] for b in branches if not b["exits"]]
    mode = FIRST_MATCH if not fallthrough else NULL_FALLTHROUGH
    if mode == NULL_FALLTHROUGH:
        # The loop falls through to the terminal, so it must be a neutral
        # `return null;` -- a `throw` as the fallback would change behaviour.
        if terminal_text != "return null;":
            raise SystemExit(
                "ABORT: NULL_FALLTHROUGH needs a `return null;` terminal, found: "
                + terminal_text
            )
        for b in branches:
            last = [x.strip() for x in b["body"] if x.strip()][-1]
            if last == "return null;":
                raise SystemExit(
                    "ABORT: %s returns null explicitly, which the null-means-keep-"
                    "looking loop would misread as fall-through" % b["type"]
                )
    # For FIRST_MATCH any terminal (return null; / return x; / throw) is the
    # unreachable fallback we keep verbatim -- no constraint.

    all_types = [s for b in branches for s in b["simple"]]
    parents = supertypes(all_types)
    in_table = set(all_types)
    related = {t: p for t, p in parents.items() if p and p in in_table}
    missing = [t for t, p in parents.items() if p == "<missing source>"]

    host_class = SRC.stem
    host_pkg = ""
    for ln in lines:
        m = re.match(r"\s*package\s+([\w.]+)\s*;", ln)
        if m:
            host_pkg = m.group(1)
            break
    host_path = host_pkg.replace(".", "/") + "/" + host_class
    test_dir = ROOT / "src/test/java" / host_pkg.replace(".", "/")

    print("method      :", names["method"], "(%s line %d)" % (host_class, mi + 1))
    print("source      :", SRC)
    print("branches    :", len(branches), "(expands to %d table rules)" % len(expanded))
    print("mode        :", mode, ("(fall-through: %s)" % ", ".join(fallthrough)) if fallthrough else "")
    print("table field :", names["table"])
    print("guard test  : %s.java" % names["test_class"])
    print("inheritance :", related if related else "none of the table types extends another")
    if missing:
        print("WARNING: no model source found for:", ", ".join(missing))

    if args.dry_run:
        print("\nDRY RUN: no files written")
        return

    # Table types may be related by inheritance (e.g. StepSolidModel is the base
    # of several branches). The original sequential-if chain is already ordered
    # correctly (children before parents) so the first match is the most specific
    # subtype; the frozen order file pins that. If a parent precedes a child we
    # would silently break reachability, so refuse and reorder the chain first.
    expanded_order = [s for b in branches for s in b["simple"]]
    name_to_idx = {n: i for i, n in enumerate(expanded_order)}
    bad_order = [
        "%s before its child %s" % (p, c)
        for c, p in related.items()
        if name_to_idx.get(p, -1) >= 0 and name_to_idx[p] < name_to_idx[c]
    ]
    if bad_order:
        raise SystemExit(
            "ABORT: table types are ordered parent-before-child %s; reorder the "
            "chain so each child is tested before its parent" % bad_order
        )

    if related:
        order_doc = (
            "Some of the %d types are related by inheritance, so the order is "
            "load-bearing: a child must be tested before its parent or the parent's "
            "rule would shadow it. The frozen file pins the original child-first "
            "ordering so any future reordering becomes a test failure rather than a "
            "silent behaviour change;"
            % len(expanded)
        )
    else:
        order_doc = (
            "The %d types are unrelated today (each is a direct %s subtype), "
            "so the order happens not to matter, but the frozen file turns any future "
            "reordering into a test failure rather than a silent behaviour change;"
            % (len(expanded), subject_type())
        )

    # 1) replace the branch region with the dispatch loop; the terminal
    #    statement (return null; / return x; / throw) stays put.
    dispatch = render_dispatch(names, mode)
    lines[bi:region_end] = dispatch + [""]

    # 2) insert the table. Static: the field is initialised inline at class
    #    level (before the method). Instance: the field is declared at class
    #    level but assigned inside the constructor(s), because its lambdas
    #    capture `this` and read final fields only set in the constructor.
    ii = javadoc_start(lines, mi)
    if IS_STATIC:
        entries = render_entries(names, branches, mode, indent="        ")
        lines[ii:ii] = (
            render_table_header(names, mode, static=True) + entries + ["    );", ""]
        )
    else:
        lines[ii:ii] = render_table_header(names, mode, static=False) + [""]
        ctor_assign = render_ctor_assignment(names, branches, mode)
        for ci in sorted(find_constructors(lines, host_class), reverse=True):
            ce = constructor_end(lines, ci)
            lines[ce:ce] = ctor_assign

    # 3) make sure List is visible. A wildcard `import java.util.*;` already
    # provides it -- StepDumpApp has one -- so injecting the single-type import
    # would only add redundant noise in the wrong alphabetical slot.
    wildcard = NEEDED_IMPORT.rsplit(".", 1)[0] + ".*;"
    if not any(ln.strip() in (NEEDED_IMPORT, wildcard) for ln in lines):
        last_import = max(i for i, ln in enumerate(lines) if ln.startswith("import "))
        lines[last_import + 1:last_import + 1] = [NEEDED_IMPORT]
        print("note        : injected", NEEDED_IMPORT)

    # read_lines() yields a trailing "" element (the file's final newline), so
    # "\n".join(lines) already ends in one newline. Normalise then add exactly
    # one: this avoids a double trailing newline that spotless rejects.
    SRC.write_text("\n".join(lines).rstrip("\n") + "\n", encoding="utf-8")

    order_txt = RES_DIR / (names["slug"] + "-dispatch-order.txt")
    order_txt.parent.mkdir(parents=True, exist_ok=True)
    order_txt.write_text(
        "\n".join(s for b in branches for s in b["simple"]) + "\n", encoding="utf-8"
    )

    test_java = test_dir / (names["test_class"] + ".java")
    test_java.parent.mkdir(parents=True, exist_ok=True)
    test_java.write_text(
        render_test(
            names, len(expanded), mode, args.summary, order_doc,
            host_class, host_pkg, static=IS_STATIC, host_path=host_path,
        ),
        encoding="utf-8",
    )

    print("\nOK: folded %d rules into %s" % (len(expanded), names["table"]))
    print("    ", SRC)
    print("    ", order_txt)
    print("    ", test_java)


if __name__ == "__main__":
    main()
