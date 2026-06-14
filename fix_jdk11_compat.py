#!/usr/bin/env python3
"""JDK 21 -> JDK 11 syntax converter for MiniCAD."""

import re, sys
from pathlib import Path

SRC = Path("D:/work/MiniCAD/src")

# ── 1. instanceof pattern matching ──────────────────────
def fix_instanceof(content):
    lines = content.split('\n')
    out = []
    i = 0
    while i < len(lines):
        line = lines[i]
        s = line.lstrip()
        ind = line[:len(line)-len(s)]

        # Simple positive: if (expr instanceof Type var) {
        m = re.match(r'^(\s*)(if\s*\()(.+?)\s+instanceof\s+([\w.]+)\s+(\w+)\)\s*\{?\s*$', line)
        if m and not '&&' in m.group(3) and not '||' in m.group(3):
            # Check this isn't a non-pattern instanceof (no binding var used later)
            tname, vname = m.group(4), m.group(5)
            # Verify it's actually a pattern binding (type looks real)
            if '.' in tname or tname[0].isupper():
                expr_part = m.group(3).rstrip()
                out.append(f'{m.group(1)}{m.group(2)}{expr_part} instanceof {tname}) {{')
                out.append(f'{ind}    {tname} {vname} = ({tname}) {expr_part};')
                i += 1; continue

        # Negated guard: if (!(expr instanceof Type var)) {
        m = re.match(r'^(\s*)(if\s*\()(.+?)!\((.+?)\s+instanceof\s+([\w.]+)\s+(\w+)\)\)\s*\{?\s*$', line)
        if m:
            tname, vname = m.group(5), m.group(6)
            if '.' in tname or tname[0].isupper():
                prefix = m.group(1)
                kw = m.group(2)
                before_neg = m.group(3)
                expr_part = m.group(4).rstrip()
                out.append(f'{prefix}{kw}{before_neg}!({expr_part} instanceof {tname})) {{')
                # Collect guard block body
                j = i + 1; bd = 1
                while j < len(lines) and bd > 0:
                    for c in lines[j]:
                        if c == '{': bd += 1
                        elif c == '}': bd -= 1
                    j += 1
                for k in range(i+1, j):
                    out.append(lines[k])
                out.append(f'{prefix}{tname} {vname} = ({tname}) {expr_part};')
                i = j; continue

        # Broken conversion: if (!(expr instanceof EdgeLoop || outerLoop.get... != 4) {
        m = re.match(r'^(\s*)(if\s*\()(.+?)!\((.+?)\s+instanceof\s+([\w.]+)\s*\|\|\s*(\w+)\.(\w+)\(([^)]*)\)\s*(!=|==)\s*(\d+)\)\)\s*\{?\s*$', line)
        if m:
            prefix = m.group(1); kw = m.group(2)
            before_neg = m.group(3); expr_part = m.group(4).rstrip()
            tname = m.group(5); var_ref = m.group(6)
            method_name = m.group(7); method_args = m.group(8)
            op = m.group(9); val = m.group(10)
            # Split into two conditions
            out.append(f'{prefix}{kw}{before_neg}!({expr_part} instanceof {tname})) {{')
            # Collect body (should be "return null;")
            j = i + 1; bd = 1
            while j < len(lines) and bd > 0:
                for c in lines[j]:
                    if c == '{': bd += 1
                    elif c == '}': bd -= 1
                j += 1
            body_lines = [lines[k] for k in range(i+1, j-1)]
            for bl in body_lines:
                out.append(bl)
            out.append(f'{prefix}}}')
            out.append(f'{prefix}{tname} {var_ref} = ({tname}) {expr_part};')
            out.append(f'{prefix}if ({var_ref}.{method_name}({method_args}) {op} {val}) {{')
            for bl in body_lines:
                out.append(bl)
            out.append(f'{prefix}}}')
            i = j; continue

        # Also handle: if (!(expr instanceof Type) || outerLoop.method() != 4) {
        # where the instanceof was already fixed but the || condition still references a missing variable
        m = re.match(r'^(\s*)(if\s*\()(.+?)!\((.+?)\s+instanceof\s+([\w.]+)\)\s*\|\|\s*(\w+)\.([\w]+)\(([^)]*)\)\s*(!=|==)\s*(\d+)\)\s*\{?\s*$', line)
        if m:
            prefix = m.group(1); kw = m.group(2)
            before_neg = m.group(3); expr_part = m.group(4).rstrip()
            tname = m.group(5); var_ref = m.group(6)
            method_name = m.group(7); method_args = m.group(8)
            op = m.group(9); val = m.group(10)
            out.append(f'{prefix}{kw}{before_neg}!({expr_part} instanceof {tname})) {{')
            j = i + 1; bd = 1
            while j < len(lines) and bd > 0:
                for c in lines[j]:
                    if c == '{': bd += 1
                    elif c == '}': bd -= 1
                j += 1
            body_lines = [lines[k] for k in range(i+1, j-1)]
            for bl in body_lines:
                out.append(bl)
            out.append(f'{prefix}}}')
            out.append(f'{prefix}{tname} {var_ref} = ({tname}) {expr_part};')
            out.append(f'{prefix}if ({var_ref}.{method_name}({method_args}) {op} {val}) {{')
            for bl in body_lines:
                out.append(bl)
            out.append(f'{prefix}}}')
            i = j; continue

        out.append(line)
        i += 1
    return '\n'.join(out)

# ── 2. Switch expressions (return switch + yield) ──────
def fix_switch_expressions(content):
    lines = content.split('\n')
    out = []
    i = 0
    while i < len(lines):
        line = lines[i]
        s = line.lstrip()
        ind = line[:len(line)-len(s)]

        # Detect: return switch (var) {
        m = re.match(r'^(\s*)return\s+switch\s*\((.+?)\)\s*\{', line)
        if m:
            indent = m.group(1)
            switch_var = m.group(2).strip()
            cases, end_idx = _parse_cases(lines, i+1)
            if cases is not None:
                _emit_if_else(out, indent, switch_var, cases, is_return=True)
                i = end_idx + 1; continue

        # Detect: varName = switch (expr) {
        m = re.match(r'^(\s*)(\w+)\s*=\s*switch\s*\((.+?)\)\s*\{', line)
        if m:
            indent = m.group(1)
            result_var = m.group(2)
            switch_var = m.group(3).strip()
            cases, end_idx = _parse_cases(lines, i+1)
            if cases is not None:
                _emit_if_else(out, indent, switch_var, cases, is_return=False, result_var=result_var)
                i = end_idx + 1; continue

        # Detect: switch (var) { with arrow cases (statement, not expression)
        m = re.match(r'^(\s*)switch\s*\((.+?)\)\s*\{', line)
        if m:
            indent = m.group(1)
            switch_var = m.group(2).strip()
            cases, end_idx = _parse_cases(lines, i+1)
            if cases is not None:
                _emit_if_else(out, indent, switch_var, cases, is_return=False)
                i = end_idx + 1; continue

        out.append(line)
        i += 1
    return '\n'.join(out)

def _parse_cases(lines, start):
    """Parse switch cases from lines starting at `start`. Returns (cases, end_idx) or (None, ...)."""
    cases = []
    i = start
    cur = None
    bd = 1  # inside switch body

    while i < len(lines):
        line = lines[i]
        stripped = line.lstrip()

        # Check for closing of switch
        if stripped.startswith('}') and bd == 1:
            # Check if it's "};"
            if cur: cases.append(cur)
            return cases, i

        for c in stripped:
            if c == '{': bd += 1
            elif c == '}': bd -= 1

        if bd == 0:
            if cur: cases.append(cur)
            return cases, i

        # Arrow block case: case Type var -> {
        m = re.match(r'^\s+case\s+(.+?)\s*->\s*\{', line)
        if m:
            if cur: cases.append(cur)
            label = m.group(1).strip()
            body = []
            bd2 = 1
            i += 1
            while i < len(lines) and bd2 > 0:
                for c in lines[i]:
                    if c == '{': bd2 += 1
                    elif c == '}': bd2 -= 1
                if bd2 > 0:
                    body.append(lines[i])
                i += 1
            cur = {'label': label, 'body': body, 'is_block': True, 'is_default': False}
            continue

        # Arrow simple case: case Type var -> expr;
        m = re.match(r'^\s+case\s+(.+?)\s*->\s*(.+);$', line)
        if m:
            if cur: cases.append(cur)
            label = m.group(1).strip()
            body_expr = m.group(2).strip()
            cases.append({'label': label, 'body': [body_expr], 'is_block': False, 'is_default': False})
            cur = None
            i += 1; continue

        # Default arrow block: default -> {
        m = re.match(r'^\s+default\s*->\s*\{', line)
        if m:
            if cur: cases.append(cur)
            body = []
            bd2 = 1
            i += 1
            while i < len(lines) and bd2 > 0:
                for c in lines[i]:
                    if c == '{': bd2 += 1
                    elif c == '}': bd2 -= 1
                if bd2 > 0:
                    body.append(lines[i])
                i += 1
            cur = {'label': 'default', 'body': body, 'is_block': True, 'is_default': True}
            cases.append(cur)
            cur = None
            continue

        # Default arrow simple: default -> expr;
        m = re.match(r'^\s+default\s*->\s*(.+);$', line)
        if m:
            if cur: cases.append(cur)
            cases.append({'label': 'default', 'body': [m.group(1).strip()], 'is_block': False, 'is_default': True})
            cur = None
            i += 1; continue

        # Part of current case body
        if cur:
            cur['body'].append(line)
        i += 1

    if cur: cases.append(cur)
    return None, i

def _emit_if_else(out, indent, switch_var, cases, is_return=True, result_var=None):
    for idx, case in enumerate(cases):
        label = case['label']
        body = case['body']
        is_block = case['is_block']
        is_default = case['is_default']

        if is_default:
            out.append(f'{indent}}} else {{')
        else:
            # Parse pattern: "Type var"
            m = re.match(r'^([\w.]+)\s+(\w+)$', label)
            if m:
                tname, vname = m.group(1), m.group(2)
                if idx == 0:
                    out.append(f'{indent}if ({switch_var} instanceof {tname}) {{')
                else:
                    out.append(f'{indent}}} else if ({switch_var} instanceof {tname}) {{')
                out.append(f'{indent}    {tname} {vname} = ({tname}) {switch_var};')
            else:
                # String constant
                if idx == 0:
                    out.append(f'{indent}if ({label}.equals({switch_var})) {{')
                else:
                    out.append(f'{indent}}} else if ({label}.equals({switch_var})) {{')

        # Emit body
        if is_block:
            for bline in body:
                bl = bline.strip()
                bl = _fix_yield(bl, is_return, result_var)
                out.append(f'{indent}    {bl}')
        else:
            expr = body[0]
            expr = _fix_yield(expr, is_return, result_var)
            if is_return:
                out.append(f'{indent}    return {expr};')
            elif result_var:
                out.append(f'{indent}    {result_var} = {expr};')
            else:
                out.append(f'{indent}    {expr};')

    out.append(f'{indent}}}')

def _fix_yield(line, is_return, result_var):
    s = line.strip()
    if s.startswith('yield '):
        expr = s[len('yield '):]
        if is_return:
            return 'return ' + expr
        elif result_var:
            return result_var + ' = ' + expr
    if s == 'yield null;':
        if is_return:
            return 'return null;'
        elif result_var:
            return result_var + ' = null;'
    return line

# ── 3. Records -> JavaBeans ────────────────────────────
def fix_records(content):
    # private record Name(Type f1, Type f2) {
    content = re.sub(
        r'private\s+record\s+(\w+)\s*\(([^)]*)\)\s*\{',
        lambda m: _record_to_class(m, 'private'),
        content
    )
    # public record Name(Type f1, ...) {
    content = re.sub(
        r'public\s+record\s+(\w+)\s*\(([^)]*)\)\s*\{',
        lambda m: _record_to_class(m, 'public'),
        content
    )
    # record with implements: public record Name(...) implements Foo {
    content = re.sub(
        r'(public|private)\s+record\s+(\w+)\s*\(([^)]*)\)\s+implements\s+([^{]+)\{',
        lambda m: _record_to_class_implements(m),
        content
    )
    return content

def _parse_fields(params):
    fields = []
    for p in params.split(','):
        p = p.strip()
        if not p: continue
        parts = p.rsplit(None, 1)
        if len(parts) == 2:
            fields.append((parts[0].strip(), parts[1].strip()))
    return fields

def _record_to_class(m, vis):
    name = m.group(1)
    fields = _parse_fields(m.group(2))
    lines = [f'{vis} static final class {name} {{']
    for ft, fn in fields:
        lines.append(f'    private final {ft} {fn};')
    lines.append('')
    params = ', '.join(f'{ft} {fn}' for ft, fn in fields)
    lines.append(f'    public {name}({params}) {{')
    for _, fn in fields:
        lines.append(f'        this.{fn} = {fn};')
    lines.append('    }')
    lines.append('')
    for ft, fn in fields:
        g = 'get' + fn[0].upper() + fn[1:]
        lines.append(f'    public {ft} {g}() {{ return {fn}; }}')
    lines.append('')
    lines.append('    @Override public boolean equals(Object o) {')
    lines.append('        if (this == o) return true;')
    lines.append(f'        if (!(o instanceof {name})) return false;')
    lines.append(f'        {name} that = ({name}) o;')
    comps = []
    prims = ('int','long','double','float','boolean','char','byte','short')
    for ft, fn in fields:
        if ft in prims:
            comps.append(f'{fn} == that.{fn}')
        else:
            comps.append(f'java.util.Objects.equals({fn}, that.{fn})')
    if comps:
        lines.append('        return ' + '\n            && '.join(comps) + ';')
    else:
        lines.append('        return true;')
    lines.append('    }')
    lines.append('')
    fnames = ', '.join(fn for _, fn in fields)
    lines.append(f'    @Override public int hashCode() {{ return java.util.Objects.hash({fnames}); }}')
    lines.append('')
    parts = []
    for i, (_, fn) in enumerate(fields):
        if i == 0:
            parts.append(f'"{fn}=" + {fn}')
        else:
            parts.append(f' + ", {fn}=" + {fn}')
    lines.append(f'    @Override public String toString() {{ return "{name}{{" + {"".join(parts)} + "}}"; }}')
    lines.append('}')
    return '\n'.join(lines)

def _record_to_class_implements(m):
    vis = m.group(1)
    name = m.group(2)
    fields = _parse_fields(m.group(3))
    impl = m.group(4).strip()
    lines = [f'{vis} static final class {name} implements {impl} {{']
    for ft, fn in fields:
        lines.append(f'    private final {ft} {fn};')
    lines.append('')
    params = ', '.join(f'{ft} {fn}' for ft, fn in fields)
    lines.append(f'    public {name}({params}) {{')
    for _, fn in fields:
        lines.append(f'        this.{fn} = {fn};')
    lines.append('    }')
    lines.append('')
    for ft, fn in fields:
        g = 'get' + fn[0].upper() + fn[1:]
        lines.append(f'    public {ft} {g}() {{ return {fn}; }}')
    lines.append('}')
    return '\n'.join(lines)

# ── Main ────────────────────────────────────────────────
def process(path):
    content = path.read_text('utf-8')
    orig = content
    content = fix_instanceof(content)
    content = fix_switch_expressions(content)
    content = fix_records(content)
    if content != orig:
        path.write_text(content, 'utf-8')
        return True
    return False

def main():
    files = sorted(SRC.rglob('*.java'))
    print(f"Scanning {len(files)} files...")
    n = 0
    for f in files:
        if process(f):
            n += 1
            print(f"  {f.relative_to(SRC)}")
    print(f"Modified {n} files.")

if __name__ == '__main__':
    main()
