#!/usr/bin/env python3
"""Fix the remaining switch expression in StepPreviewJsonExporter.java."""
import re
from pathlib import Path

def count_braces_line(s):
    o = c = 0; in_s = False; esc = False
    for ch in s:
        if esc: esc = False; continue
        if ch == '\\': esc = True; continue
        if ch == '"': in_s = not in_s; continue
        if in_s: continue
        if ch == '{': o += 1
        elif ch == '}': c += 1
    return o, c

p = Path('D:/work/MiniCAD/src/main/java/com/minicad/app/StepPreviewJsonExporter.java')
lines = p.read_text(encoding='utf-8').split('\n')

# Find "return switch (curve) {"
sw_start = None
for idx, line in enumerate(lines):
    if re.match(r'^\s*return\s+switch\s*\(curve\)\s*\{', line):
        sw_start = idx
        break

if sw_start is None:
    print('Switch expression not found!')
    exit(1)

print(f'Found switch expression at line {sw_start+1}')

# Parse cases
cases = []
i = sw_start + 1
cur = None
bd = 1  # inside switch body
while i < len(lines):
    line = lines[i]
    s = line.strip()

    # Check if we've exited the switch
    for ch in s:
        if ch == '{': bd += 1
        elif ch == '}': bd -= 1
    if bd == 0:
        if cur: cases.append(cur)
        end_idx = i
        break

    # Arrow block case: case Type var -> {
    m = re.match(r'^\s+case\s+(.+?)\s*->\s*\{', line)
    if m:
        if cur: cases.append(cur)
        lbl = m.group(1).strip()
        body = []
        bd2 = 1
        i += 1
        while i < len(lines) and bd2 > 0:
            for ch in lines[i]:
                if ch == '{': bd2 += 1
                elif ch == '}': bd2 -= 1
            if bd2 > 0: body.append(lines[i])
            i += 1
        cur = {'label': lbl, 'body': body, 'is_block': True, 'is_default': False}
        continue

    # Arrow simple case: case Type var -> expr;
    m = re.match(r'^\s+case\s+(.+?)\s*->\s*(.+);$', line)
    if m:
        if cur: cases.append(cur)
        cases.append({'label': m.group(1).strip(), 'body': [m.group(2).strip()], 'is_block': False, 'is_default': False})
        cur = None; i += 1; continue

    # Default arrow: default -> null;
    m = re.match(r'^\s+default\s*->\s*(.+);$', line)
    if m:
        if cur: cases.append(cur)
        cases.append({'label': 'default', 'body': [m.group(1).strip()], 'is_block': False, 'is_default': True})
        cur = None; i += 1; continue

    # Default arrow block
    m = re.match(r'^\s+default\s*->\s*\{', line)
    if m:
        if cur: cases.append(cur)
        body = []
        bd2 = 1
        i += 1
        while i < len(lines) and bd2 > 0:
            for ch in lines[i]:
                if ch == '{': bd2 += 1
                elif ch == '}': bd2 -= 1
            if bd2 > 0: body.append(lines[i])
            i += 1
        cases.append({'label': 'default', 'body': body, 'is_block': True, 'is_default': True})
        cur = None; continue

    if cur:
        cur['body'].append(line)
    i += 1
else:
    if cur: cases.append(cur)
    end_idx = i

print(f'Parsed {len(cases)} cases')

# Generate if-else chain
ind = '        '
out = []
for idx, case in enumerate(cases):
    lbl = case['label']
    body = case['body']
    is_block = case['is_block']
    is_default = case['is_default']

    if is_default:
        out.append(f'{ind}}} else {{')
    else:
        m = re.match(r'^([\w.]+)\s+(\w+)$', lbl)
        if m:
            tn, vn = m.group(1), m.group(2)
            if idx == 0:
                out.append(f'{ind}if (curve instanceof {tn}) {{')
            else:
                out.append(f'{ind}}} else if (curve instanceof {tn}) {{')
            out.append(f'{ind}    {tn} {vn} = ({tn}) curve;')
        else:
            if idx == 0:
                out.append(f'{ind}if ({lbl}.equals(curve)) {{')
            else:
                out.append(f'{ind}}} else if ({lbl}.equals(curve)) {{')

    # Emit body
    if is_block:
        for bline in body:
            bl = bline.strip()
            bl = re.sub(r'\byield\b', 'return', bl)
            out.append(f'{ind}    {bl}')
    else:
        expr = body[0]
        out.append(f'{ind}    return {expr};')

out.append(f'{ind}}}')

# Replace the switch expression with if-else chain
# Keep everything before the switch line
new_lines = lines[:sw_start]
# Remove the "return switch (curve) {" line and add the if-else chain
new_lines.extend(out)
# Skip the switch expression body and the closing "};"
new_lines.extend(lines[end_idx + 1:])

p.write_text('\n'.join(new_lines), encoding='utf-8')
print(f'Replaced switch expression ({sw_start+1}-{end_idx+1}) with if-else chain')
