#!/usr/bin/env python3
"""Batch fix getFirst/getLast and switch expressions in MiniCAD"""

import re
import sys
import glob

def fix_file(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        original = f.read()

    content = original

    # Fix getFirst() -> get(0)
    content = re.sub(r'\.getFirst\(\)', '.get(0)', content)

    # Fix getLast() -> get(size() - 1)
    # Pattern: obj.getLast() -> obj.get(obj.size() - 1)
    def fix_get_last(m):
        obj = m.group(1)
        return f'{obj}.get({obj}.size() - 1)'

    content = re.sub(r'(\w+)\.getLast\(\)', fix_get_last, content)

    # Fix isEmpty() in StringBuilder/String (keep for String, change for StringBuilder)
    # StringBuilder.isEmpty() is JDK 15+ but we can use length() == 0
    # Pattern: builder.isEmpty() -> builder.length() == 0 (for StringBuilder only)
    # This is tricky - skip for now

    # Fix switch expressions - mark for manual review
    if 'yield' in content or 'return switch' in content:
        # Add TODO markers at switch expression locations
        lines = content.split('\n')
        new_lines = []
        for i, line in enumerate(lines):
            new_lines.append(line)
            if 'return switch' in line or ('switch' in line and '->' in line):
                new_lines.append('        // TODO: JDK11 - Convert switch expression above')
        content = '\n'.join(new_lines)

    if content != original:
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(content)
        return True
    return False

def main():
    if len(sys.argv) > 1:
        files = sys.argv[1:]
    else:
        files = []
        files.extend(glob.glob('src/main/java/com/minicad/**/*.java', recursive=True))

    fixed_count = 0
    for filepath in files:
        if fix_file(filepath):
            print(f"Fixed: {filepath}")
            fixed_count += 1

    print(f"\nTotal files fixed: {fixed_count}")

if __name__ == '__main__':
    main()