#!/usr/bin/env python3
"""Fix record converter in convert_all.py - remove extra closing brace."""
from pathlib import Path

p = Path('D:/work/MiniCAD/convert_all.py')
content = p.read_text(encoding='utf-8')

# Remove the extra "L.append('')" line before return (which adds blank line before closing brace)
old = "        L.append('')\n        return '\\n'.join(L)"
new = "        return '\\n'.join(L)"
if old in content:
    content = content.replace(old, new)
    print('Fixed record converter')
else:
    print('Pattern not found - checking alternatives')
    # Maybe the pattern is slightly different
    if "L.append('')" in content:
        print('Found L.append - trying line-by-line fix')
    else:
        print('L.append not found either')

p.write_text(content, encoding='utf-8')
print('Done')
