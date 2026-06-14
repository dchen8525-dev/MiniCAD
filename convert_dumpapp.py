#!/usr/bin/env python3
"""Comprehensive JDK 21->11 converter for StepDumpApp.java - single pass."""
import re
from pathlib import Path

p = Path('D:/work/MiniCAD/src/main/java/com/minicad/app/StepDumpApp.java')
lines = p.read_text(encoding='utf-8').split('\n')
out = []
i = 0
extra_close_stack = []
brace_depth = 0

def count_braces(s):
    o = c = 0
    in_str = False
    esc = False
    for ch in s:
        if esc: esc = False; continue
        if ch == '\\': esc = True; continue
        if ch == '"': in_str = not in_str; continue
        if in_str: continue
        if ch == '{': o += 1
        elif ch == '}': c += 1
    return o, c

def split_by_comma(s):
    result, current, depth = [], [], 0
    for ch in s:
        if ch == '<': depth += 1; current.append(ch)
        elif ch == '>': depth -= 1; current.append(ch)
        elif ch == ',' and depth == 0: result.append(''.join(current)); current = []
        else: current.append(ch)
    if current: result.append(''.join(current))
    return result

while i < len(lines):
    line = lines[i]
    s = line.strip()
    indent = re.match(r'^(\s*)', line).group(1)

    # Insert extra } before closing brace if needed
    while extra_close_stack and extra_close_stack[-1][0] == brace_depth and s.startswith('}') and not s.startswith('} else'):
        _, ei = extra_close_stack.pop()
        out.append(ei + '    }')

    # Record declaration
    m = re.match(r'^(\s*)(private|public)\s+record\s+(\w+)\s*\((.*)$', line)
    if m:
        ri, vis, name, rest = m.group(1), m.group(2), m.group(3), m.group(4)
        params_text = rest
        while ')' not in params_text and i + 1 < len(lines):
            i += 1; params_text += ' ' + lines[i].strip()
        pi = params_text.index(')')
        params_str = params_text[:pi].strip()
        fields = []
        for part in split_by_comma(params_str):
            part = part.strip()
            if not part: continue
            pp = part.rsplit(None, 1)
            if len(pp) == 2: fields.append((pp[0].strip(), pp[1].strip()))
        out.append('{0}{1} static final class {2} {{'.format(ri, vis, name))
        for ft, fn in fields:
            out.append('{0}    private final {1} {2};'.format(ri, ft, fn))
        out.append('')
        pd = ', '.join('{0} {1}'.format(ft, fn) for ft, fn in fields)
        out.append('{0}    {1}({2}) {{'.format(ri, name, pd))
        for _, fn in fields:
            out.append('{0}        this.{1} = {1};'.format(ri, fn))
        out.append('{0}    }}'.format(ri))
        out.append('')
        for ft, fn in fields:
            out.append('{0}    public {1} {2}() {{ return {3}; }}'.format(ri, ft, fn, fn))
        out.append('')
        oc, cc = count_braces(line)
        brace_depth += oc - cc
        i += 1; continue

    # Compound instanceof else-if
    m = re.match(r'^(\s*)\}\s*else\s+if\s*\((\S+)\s+instanceof\s+([\w.]+)\s+(\w+)\s*&&\s*(.+)\)\s*\{', line)
    if m:
        ci, expr, tname, vname = m.group(1), m.group(2), m.group(3), m.group(4)
        cond = m.group(5).strip()
        if cond.endswith('{'): cond = cond[:-1].strip()
        out.append('{0}}} else if ({1} instanceof {2}) {{'.format(ci, expr, tname))
        out.append('{0}    {1} {2} = ({1}) {3};'.format(ci, tname, vname, expr))
        out.append('{0}    if ({1}) {{'.format(ci, cond))
        extra_close_stack.append((brace_depth, ci))
        oc, cc = count_braces(line)
        brace_depth += oc - cc
        i += 1; continue

    # Simple instanceof else-if
    m = re.match(r'^(\s*)\}\s*else\s+if\s*\((\S+)\s+instanceof\s+([\w.]+)\s+(\w+)\)\s*\{', line)
    if m:
        ci, expr, tname, vname = m.group(1), m.group(2), m.group(3), m.group(4)
        out.append('{0}}} else if ({1} instanceof {2}) {{'.format(ci, expr, tname))
        out.append('{0}    {1} {2} = ({1}) {3};'.format(ci, tname, vname, expr))
        oc, cc = count_braces(line)
        brace_depth += oc - cc
        i += 1; continue

    # Compound instanceof if
    m = re.match(r'^(\s*)if\s*\((\S+)\s+instanceof\s+([\w.]+)\s+(\w+)\s*&&\s*(.+)\)\s*\{', line)
    if m:
        ci, expr, tname, vname = m.group(1), m.group(2), m.group(3), m.group(4)
        cond = m.group(5).strip()
        if cond.endswith('{'): cond = cond[:-1].strip()
        out.append('{0}if ({1} instanceof {2}) {{'.format(ci, expr, tname))
        out.append('{0}    {1} {2} = ({1}) {3};'.format(ci, tname, vname, expr))
        out.append('{0}    if ({1}) {{'.format(ci, cond))
        extra_close_stack.append((brace_depth, ci))
        oc, cc = count_braces(line)
        brace_depth += oc - cc
        i += 1; continue

    # Simple instanceof if
    m = re.match(r'^(\s*)if\s*\((\S+)\s+instanceof\s+([\w.]+)\s+(\w+)\)\s*\{', line)
    if m and (m.group(3)[0].isupper() or '.' in m.group(3)):
        ci, expr, tname, vname = m.group(1), m.group(2), m.group(3), m.group(4)
        out.append('{0}if ({1} instanceof {2}) {{'.format(ci, expr, tname))
        out.append('{0}    {1} {2} = ({1}) {3};'.format(ci, tname, vname, expr))
        oc, cc = count_braces(line)
        brace_depth += oc - cc
        i += 1; continue

    # Negated instanceof guard
    m = re.match(r'^(\s*)if\s*\(!\((\S+)\s+instanceof\s+([\w.]+)\s+(\w+)\)\)\s*\{', line)
    if m and (m.group(3)[0].isupper() or '.' in m.group(3)):
        ci, expr, tname, vname = m.group(1), m.group(2), m.group(3), m.group(4)
        out.append('{0}if (!({1} instanceof {2})) {{'.format(ci, expr, tname))
        j = i + 1; bd = 1
        while j < len(lines) and bd > 0:
            o2, c2 = count_braces(lines[j]); bd += o2 - c2; j += 1
        for k in range(i + 1, j - 1):
            out.append(lines[k])
        out.append('{0}}}'.format(ci))
        out.append('{0}{1} {2} = ({1}) {3};'.format(ci, tname, vname, expr))
        i = j; continue

    # Switch arrow escape cases
    m = re.match(r"^(\s*)case '(\\\\|\\[bfnrt'])' -> (.+);$", line)
    if m:
        ci, esc, body = m.group(1), m.group(2), m.group(3)
        out.append("{0}case '{1}': {2}; break;".format(ci, esc, body))
        oc, cc = count_braces(line)
        brace_depth += oc - cc
        i += 1; continue

    # Switch expression: String code = switch (exception) {
    if 'switch (exception)' in s and '=' in s:
        out.append(indent + 'String code;')
        i += 1; cases = []
        while i < len(lines):
            cs = lines[i].strip()
            cm = re.match(r'case\s+(\w+)\s+\w+\s*->\s*(.+);', cs)
            if cm: cases.append((cm.group(1), cm.group(2).strip())); i += 1; continue
            dm = re.match(r'default\s*->\s*(.+);', cs)
            if dm: cases.append(('_default_', dm.group(1).strip())); i += 1; continue
            if cs == '};': i += 1; break
            i += 1
        for idx, (cls, val) in enumerate(cases):
            if cls == '_default_': out.append(indent + '} else {')
            elif idx == 0: out.append(indent + 'if (exception instanceof {0}) {{'.format(cls))
            else: out.append(indent + '}} else if (exception instanceof {0}) {{'.format(cls))
            out.append(indent + '    code = {0};'.format(val))
        out.append(indent + '}')
        continue

    # Default
    oc, cc = count_braces(line)
    brace_depth += oc - cc
    out.append(line)
    i += 1

content = '\n'.join(out)
# Fix boolean compound instanceof patterns
content = content.replace('|| surface instanceof StepGeometricReplica replica && "SURFACE_REPLICA".equals(replica.entityName());', '|| (surface instanceof StepGeometricReplica && "SURFACE_REPLICA".equals(((StepGeometricReplica) surface).entityName()));')
content = content.replace('|| placement instanceof StepCartesianPoint point && point.coordinates().size() == 2;', '|| (placement instanceof StepCartesianPoint && ((StepCartesianPoint) placement).coordinates().size() == 2);')
content = content.replace('|| element instanceof StepGeometricReplica replica && "POINT_REPLICA".equals(replica.entityName());', '|| (element instanceof StepGeometricReplica && "POINT_REPLICA".equals(((StepGeometricReplica) element).entityName()));')
content = content.replace('|| point instanceof StepGeometricReplica replica && "POINT_REPLICA".equals(replica.entityName());', '|| (point instanceof StepGeometricReplica && "POINT_REPLICA".equals(((StepGeometricReplica) point).entityName()));')

p.write_text(content, encoding='utf-8')
print('Done')
