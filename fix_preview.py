#!/usr/bin/env python3
"""Fix remaining patterns in StepPreviewJsonExporter.java."""
import re
from pathlib import Path

p = Path('D:/work/MiniCAD/src/main/java/com/minicad/app/StepPreviewJsonExporter.java')
lines = p.read_text(encoding='utf-8').split('\n')
out = []
i = 0
while i < len(lines):
    line = lines[i]
    s = line.strip()

    # Pattern 1: Negated instanceof with compound condition
    # if (!(bounds.getFirst().loop() instanceof EdgeLoop outerLoop) || outerLoop.edges().size() != 4) {
    m = re.match(
        r'^(\s*)if\s*\(!\((\S+)\s+instanceof\s+([\w.]+)\s+(\w+)\)\s*\|\|\s*(\w+)\.([\w]+)\(([^)]*)\)\s*(!=|==)\s*(\d+)\)\s*\{',
        line
    )
    if m:
        ci = m.group(1); expr = m.group(2); tn = m.group(3); vn = m.group(4)
        vr = m.group(5); method = m.group(6); args = m.group(7); op = m.group(8); val = m.group(9)
        out.append('%sif (!(%s instanceof %s)) {' % (ci, expr, tn))
        # Read body (should be "return null;")
        j = i + 1; body = []
        while j < len(lines):
            if lines[j].strip() == '}':
                break
            body.append(lines[j])
            j += 1
        for b in body:
            out.append(b)
        out.append('%s}' % ci)
        out.append('%s%s %s = (%s) %s;' % (ci, tn, vn, tn, expr))
        out.append('%sif (%s.%s(%s) %s %s) {' % (ci, vr, method, args, op, val))
        for b in body:
            out.append(b)
        out.append('%s}' % ci)
        i = j + 1; continue

    # Pattern 2: Multi-line compound instanceof
    # if (entity instanceof StepFaceEntity stepFace
    #         && condition) {
    m = re.match(r'^(\s*)if\s*\((\S+)\s+instanceof\s+([\w.]+)\s+(\w+)\s*$', line)
    if m and i + 1 < len(lines) and '&&' in lines[i + 1]:
        ci = m.group(1); expr = m.group(2); tn = m.group(3); vn = m.group(4)
        next_line = lines[i + 1].strip()
        # Extract condition after &&
        cond_match = re.match(r'&&\s*(.+?)\)\s*\{', next_line)
        if cond_match:
            cond = cond_match.group(1).strip()
            out.append('%sif (%s instanceof %s) {' % (ci, expr, tn))
            out.append('%s    %s %s = (%s) %s;' % (ci, tn, vn, tn, expr))
            out.append('%s    if (%s) {' % (ci, cond))
            # Find the matching closing brace for the outer if
            j = i + 2; bd = 1
            while j < len(lines) and bd > 0:
                for ch in lines[j]:
                    if ch == '{': bd += 1
                    elif ch == '}': bd -= 1
                j += 1
            # Emit body
            for k in range(i + 2, j - 1):
                out.append(lines[k])
            # Close inner if, close outer if
            out.append('%s    }' % ci)
            out.append('%s}' % ci)
            i = j; continue

    # Pattern 3: Inline compound instanceof in boolean expression
    # || (item instanceof StepGeometricReplica replica && "CURVE_REPLICA".equals(replica.entityName()));
    m = re.match(r'^(\s*)\|\|\s*\((\S+)\s+instanceof\s+([\w.]+)\s+(\w+)\s*&&\s*(.+)\)\s*;', line)
    if m:
        ci = m.group(1); expr = m.group(2); tn = m.group(3); vn = m.group(4); cond = m.group(5).strip()
        # Replace varName.method() with ((Type) expr).method()
        cond_casted = cond.replace(vn + '.', '((%s) %s).' % (tn, expr))
        out.append('%s|| (%s instanceof %s && %s);' % (ci, expr, tn, cond_casted))
        i += 1; continue

    out.append(line)
    i += 1

p.write_text('\n'.join(out), encoding='utf-8')
print('Fixed StepPreviewJsonExporter.java')
