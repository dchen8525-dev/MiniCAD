#!/usr/bin/env python3
"""Apply convert_dumpapp.py style conversion to all remaining files."""
import re
from pathlib import Path

SRC = Path('D:/work/MiniCAD/src')
FILES = [
    'main/java/com/minicad/app/PreviewFaceBuilder.java',
    'main/java/com/minicad/app/PreviewPmiBuilder.java',
    'main/java/com/minicad/app/StepMeshExporter.java',
    'main/java/com/minicad/app/StepMetadataExtractor.java',
    'main/java/com/minicad/app/UnitExtractor.java',
    'main/java/com/minicad/step/semantic/StepEntityResolver.java',
    'main/java/com/minicad/step/semantic/StepTopologyBuilder.java',
]

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

for fname in FILES:
    p = SRC / fname
    if not p.exists():
        continue
    lines = p.read_text(encoding='utf-8').split('\n')
    out = []
    changed = False
    i = 0
    extra_close_stack = []
    brace_depth = 0

    while i < len(lines):
        line = lines[i]
        s = line.strip()
        indent = re.match(r'^(\s*)', line).group(1)

        # Insert extra } before closing brace if needed
        while extra_close_stack and extra_close_stack[-1][0] == brace_depth and s.startswith('}') and not s.startswith('} else'):
            _, ei = extra_close_stack.pop()
            out.append(ei + '    }')

        # Compound instanceof else-if: } else if (expr instanceof Type var && cond) {
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
            changed = True
            i += 1; continue

        # Simple instanceof else-if: } else if (expr instanceof Type var) {
        m = re.match(r'^(\s*)\}\s*else\s+if\s*\((\S+)\s+instanceof\s+([\w.]+)\s+(\w+)\)\s*\{', line)
        if m:
            ci, expr, tname, vname = m.group(1), m.group(2), m.group(3), m.group(4)
            out.append('{0}}} else if ({1} instanceof {2}) {{'.format(ci, expr, tname))
            out.append('{0}    {1} {2} = ({1}) {3};'.format(ci, tname, vname, expr))
            oc, cc = count_braces(line)
            brace_depth += oc - cc
            changed = True
            i += 1; continue

        # Compound instanceof if: if (expr instanceof Type var && cond) {
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
            changed = True
            i += 1; continue

        # Simple instanceof if: if (expr instanceof Type var) {
        m = re.match(r'^(\s*)if\s*\((\S+)\s+instanceof\s+([\w.]+)\s+(\w+)\)\s*\{', line)
        if m and (m.group(3)[0].isupper() or '.' in m.group(3)):
            ci, expr, tname, vname = m.group(1), m.group(2), m.group(3), m.group(4)
            out.append('{0}if ({1} instanceof {2}) {{'.format(ci, expr, tname))
            out.append('{0}    {1} {2} = ({1}) {3};'.format(ci, tname, vname, expr))
            oc, cc = count_braces(line)
            brace_depth += oc - cc
            changed = True
            i += 1; continue

        # Negated instanceof guard: if (!(expr instanceof Type var)) {
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
            changed = True
            i = j; continue

        oc, cc = count_braces(line)
        brace_depth += oc - cc
        out.append(line)
        i += 1

    if changed:
        # Also fix boolean compound instanceof patterns
        content = '\n'.join(out)
        content = content.replace(
            '|| element instanceof StepGeometricReplica replica && "POINT_REPLICA".equals(replica.entityName());',
            '|| (element instanceof StepGeometricReplica && "POINT_REPLICA".equals(((StepGeometricReplica) element).entityName()));'
        )
        content = content.replace(
            '|| surface instanceof StepGeometricReplica replica && "SURFACE_REPLICA".equals(replica.entityName());',
            '|| (surface instanceof StepGeometricReplica && "SURFACE_REPLICA".equals(((StepGeometricReplica) surface).entityName()));'
        )
        p.write_text(content, encoding='utf-8')
        print('Fixed: ' + fname)
    else:
        print('No changes: ' + fname)
