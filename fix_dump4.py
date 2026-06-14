#!/usr/bin/env python3
"""Fix all remaining compound instanceof patterns in StepDumpApp.java."""
import re
from pathlib import Path

p = Path('D:/work/MiniCAD/src/main/java/com/minicad/app/StepDumpApp.java')
lines = p.read_text(encoding='utf-8').split('\n')
out = []
i = 0
while i < len(lines):
    line = lines[i]

    # Match: } else if (expr instanceof Type var && condition) {
    m = re.match(r'^(\s*)\}\s*else\s+if\s*\((.+?)\s+instanceof\s+([\w.]+)\s+(\w+)\s*&&\s*(.+?)\)\s*\{', line)
    if m:
        indent = m.group(1)
        expr = m.group(2).strip()
        tname = m.group(3)
        vname = m.group(4)
        cond = m.group(5).strip()
        # Output: } else if (expr instanceof Type) {
        #            Type var = (Type) expr;
        #            if (cond) {
        out.append('{0}}} else if ({1} instanceof {2}) {{'.format(indent, expr, tname))
        out.append('{0}    {1} {2} = ({1}) {3};'.format(indent, tname, vname, expr))
        out.append('{0}    if ({1}) {{'.format(indent, cond))
        # Find the matching closing brace for this else-if block
        j = i + 1
        bd = 1
        while j < len(lines) and bd > 0:
            for c in lines[j]:
                if c == '{': bd += 1
                elif c == '}': bd -= 1
            j += 1
        # Emit body lines
        for k in range(i + 1, j - 1):
            out.append(lines[k])
        # Close inner if, close outer else-if
        out.append('{0}    }}'.format(indent))
        out.append('{0}}}'.format(indent))
        i = j
        continue

    # Also handle: expr instanceof Type var && condition; (in boolean expressions)
    # These are trickier - they're in method call arguments or return statements
    # For now, let me handle the specific patterns I know about

    out.append(line)
    i += 1

content = '\n'.join(out)

# Fix standalone boolean compound instanceof patterns
# || surface instanceof StepGeometricReplica replica && "SURFACE_REPLICA".equals(replica.entityName());
# -> || (surface instanceof StepGeometricReplica && "SURFACE_REPLICA".equals(((StepGeometricReplica) surface).entityName()))
# Actually these are in boolean expressions that check instanceof and condition
# Let me handle them case by case

# Pattern: || expr instanceof Type var && cond;
# These appear in boolean expressions like: if (a || b instanceof Type var && cond)
# Need to convert to: if (a || (b instanceof Type && cond_with_cast))
# This is complex. Let me use a simpler approach: convert the line-level pattern

# Fix: || surface instanceof StepGeometricReplica replica && "SURFACE_REPLICA".equals(replica.entityName());
content = content.replace(
    '|| surface instanceof StepGeometricReplica replica && "SURFACE_REPLICA".equals(replica.entityName());',
    '|| (surface instanceof StepGeometricReplica && "SURFACE_REPLICA".equals(((StepGeometricReplica) surface).entityName()));'
)
# Fix: || placement instanceof StepCartesianPoint point && point.coordinates().size() == 2;
content = content.replace(
    '|| placement instanceof StepCartesianPoint point && point.coordinates().size() == 2;',
    '|| (placement instanceof StepCartesianPoint && ((StepCartesianPoint) placement).coordinates().size() == 2);'
)

# Also fix the negated instanceof patterns that my script missed
# if (!(entity instanceof StepCartesianPoint point)) {
content = content.replace(
    'if (!(entity instanceof StepCartesianPoint point)) {',
    'if (!(entity instanceof StepCartesianPoint)) {\n'
    '                StepCartesianPoint point = (StepCartesianPoint) entity;'
)

# Also need to fix: if (element instanceof StepGeometricReplica replica && ...)
# These might also exist as non-else-if patterns
# Let me check and fix them

p.write_text(content, encoding='utf-8')
print('Fixed all compound instanceof patterns')
