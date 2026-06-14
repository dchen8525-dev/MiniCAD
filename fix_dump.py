#!/usr/bin/env python3
"""Fix StepDumpApp.java specifically."""
import re
from pathlib import Path

p = Path('D:/work/MiniCAD/src/main/java/com/minicad/app/StepDumpApp.java')
content = p.read_text(encoding='utf-8')

# 1. Fix instanceof in else-if chains: } else if (expr instanceof Type var) {
def fix_else_if_instanceof(m):
    expr = m.group(1).strip()
    tname = m.group(2)
    vname = m.group(3)
    return '}} else if ({0} instanceof {1}) {{\n            {1} {2} = ({1}) {0};'.format(expr, tname, vname)

content = re.sub(
    r'\}\s*else\s+if\s*\((.+?)\s+instanceof\s+([\w.]+)\s+(\w+)\)\s*\{',
    fix_else_if_instanceof,
    content
)

# 2. Fix instanceof in if: if (expr instanceof Type var) {
def fix_if_instanceof(m):
    prefix = m.group(1)
    expr = m.group(2).strip()
    tname = m.group(3)
    vname = m.group(4)
    if tname[0].isupper() or '.' in tname:
        return '{0}if ({1} instanceof {2}) {{\n            {2} {3} = ({2}) {1};'.format(prefix, expr, tname, vname)
    return m.group(0)

content = re.sub(
    r'^(\s*)if\s*\((.+?)\s+instanceof\s+([\w.]+)\s+(\w+)\)\s*\{',
    fix_if_instanceof,
    content,
    flags=re.MULTILINE
)

# 3. Fix switch arrow single cases: case 'X' -> expr;
content = re.sub(
    r"case '(.)' -> (.+);",
    lambda m: "case '{}': {}; break;".format(m.group(1), m.group(2)),
    content
)

# 4. Fix switch arrow blocks: default -> { ... }
# Replace "default -> {" with "default:"
content = content.replace('default -> {', 'default:')

# 5. Fix records
# Convert records: replace "record Name(...) {" with "class Name { fields + ctor + accessors"
# Do NOT add closing } - the original record body's } will close the class
def convert_record(m):
    vis = m.group(1)
    name = m.group(2)
    params = m.group(3)
    fields = []
    for part in params.split(','):
        part = part.strip()
        if part:
            pp = part.rsplit(None, 1)
            if len(pp) == 2:
                fields.append((pp[0].strip(), pp[1].strip()))
    lines = ['{0} static final class {1} {{'.format(vis, name)]
    for ft, fn in fields:
        lines.append('    private final {0} {1};'.format(ft, fn))
    lines.append('')
    params_str = ', '.join('{0} {1}'.format(ft, fn) for ft, fn in fields)
    lines.append('    {0}({1}) {{'.format(name, params_str))
    for _, fn in fields:
        lines.append('        this.{0} = {0};'.format(fn))
    lines.append('    }')
    lines.append('')
    # Record-style accessors (field name as method name)
    for ft, fn in fields:
        lines.append('    public {0} {1}() {{ return {2}; }}'.format(ft, fn, fn))
    lines.append('')
    # Do NOT add closing brace - original record body will provide it
    return '\n'.join(lines)

content = re.sub(
    r'(private|public)\s+record\s+(\w+)\s*\(([^)]*)\)\s*\{',
    convert_record,
    content
)

p.write_text(content, encoding='utf-8')
print('StepDumpApp.java fixed')
