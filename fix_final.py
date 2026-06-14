#!/usr/bin/env python3
"""Fix final 4 compilation errors."""
import re
from pathlib import Path

# Fix StepDumpApp.java: remaining switch arrow cases
p = Path('D:/work/MiniCAD/src/main/java/com/minicad/app/StepDumpApp.java')
lines = p.read_text(encoding='utf-8').split('\n')
for i, line in enumerate(lines):
    s = line.strip()
    indent = re.match(r'^(\s*)', line).group(1)
    # Fix: case '"' -> expr;
    m = re.match(r'^\s*case \'\"\' -> (.+);$', line)
    if m:
        lines[i] = "{0}case '\"': {1}; break;".format(indent, m.group(1))
    # Fix: default -> {
    if s == 'default -> {':
        lines[i] = indent + 'default:'
for i, line in enumerate(lines):
    if line.strip() == 'default:':
        # Find the closing } of this default block
        bd = 0
        j = i + 1
        while j < len(lines):
            for c in lines[j]:
                if c == '{': bd += 1
                elif c == '}': bd -= 1
            if bd < 0:
                indent = re.match(r'^(\s*)', lines[j]).group(1)
                lines[j] = indent + 'break;'
                break
            j += 1
p.write_text('\n'.join(lines), encoding='utf-8')
print('Fixed StepDumpApp.java switch')

# Fix PreviewFaceBuilder.java: compound instanceof with &&
p2 = Path('D:/work/MiniCAD/src/main/java/com/minicad/app/PreviewFaceBuilder.java')
lines2 = p2.read_text(encoding='utf-8').split('\n')
out = []
i = 0
while i < len(lines2):
    line = lines2[i]
    s = line.strip()
    indent = re.match(r'^(\s*)', line).group(1)

    # Compound instanceof: if (expr instanceof Type var && cond) {
    m = re.match(r'^(\s*)if\s*\((\S+)\s+instanceof\s+([\w.]+)\s+(\w+)\s*&&\s*(.+)\)\s*\{', line)
    if m:
        ci = m.group(1)
        expr = m.group(2)
        tname = m.group(3)
        vname = m.group(4)
        cond = m.group(5).strip()
        if cond.endswith('{'):
            cond = cond[:-1].strip()
        out.append('{0}if ({1} instanceof {2}) {{'.format(ci, expr, tname))
        out.append('{0}    {1} {2} = ({1}) {3};'.format(ci, tname, vname, expr))
        out.append('{0}    if ({1}) {{'.format(ci, cond))
        # Find the matching closing brace for the original if
        j = i + 1
        bd = 1
        while j < len(lines2) and bd > 0:
            for c in lines2[j]:
                if c == '{': bd += 1
                elif c == '}': bd -= 1
            j += 1
        # Emit body lines
        for k in range(i + 1, j - 1):
            out.append(lines2[k])
        # Close inner if, close outer if
        out.append('{0}    }}'.format(ci))
        out.append('{0}}}'.format(ci))
        i = j
        continue

    # Simple instanceof: if (expr instanceof Type var) {
    m = re.match(r'^(\s*)if\s*\((\S+)\s+instanceof\s+([\w.]+)\s+(\w+)\)\s*\{', line)
    if m and (m.group(3)[0].isupper() or '.' in m.group(3)):
        ci = m.group(1)
        expr = m.group(2)
        tname = m.group(3)
        vname = m.group(4)
        out.append('{0}if ({1} instanceof {2}) {{'.format(ci, expr, tname))
        out.append('{0}    {1} {2} = ({1}) {3};'.format(ci, tname, vname, expr))
        i += 1
        continue

    # Simple instanceof else-if: } else if (expr instanceof Type var) {
    m = re.match(r'^(\s*)\}\s*else\s+if\s*\((\S+)\s+instanceof\s+([\w.]+)\s+(\w+)\)\s*\{', line)
    if m:
        ci = m.group(1)
        expr = m.group(2)
        tname = m.group(3)
        vname = m.group(4)
        out.append('{0}}} else if ({1} instanceof {2}) {{'.format(ci, expr, tname))
        out.append('{0}    {1} {2} = ({1}) {3};'.format(ci, tname, vname, expr))
        i += 1
        continue

    out.append(line)
    i += 1

p2.write_text('\n'.join(out), encoding='utf-8')
print('Fixed PreviewFaceBuilder.java')
