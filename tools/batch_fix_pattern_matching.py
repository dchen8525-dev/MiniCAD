#!/usr/bin/env python3
"""
Batch fix remaining pattern matching instanceof in Java files
"""

import re
import sys
from pathlib import Path

def fix_pattern_matching_instanceof(content):
    """
    Convert pattern matching instanceof to explicit casting
    Pattern: instanceof Type var -> instanceof Type + Type var = (Type)x
    """

    # Pattern for: instanceof Type variable_name
    pattern = r'instanceof\s+([A-Za-z_][A-Za-z0-9_.]*)\s+([a-z_][a-z0-9_]*)([;,\)\{])'

    lines = content.split('\n')
    result_lines = []

    for line in lines:
        # Skip comments
        if '//' in line and 'instanceof' in line:
            result_lines.append(line)
            continue

        # Find pattern matching in this line
        matches = re.finditer(pattern, line)

        modified_line = line
        offset = 0

        for match in matches:
            type_name = match.group(1)
            var_name = match.group(2)
            suffix = match.group(3)

            # Check if this is inside a condition that needs the variable
            original_start = match.start() + offset
            original_end = match.end() + offset

            # Simple pattern: just instanceof Type var
            # Replace with: instanceof Type) && ... && ((Type)x).method()
            # For complex cases, need manual review

            # For now, just mark it for manual review
            # This script is for identifying patterns, not auto-fixing complex ones

        result_lines.append(modified_line)

    return '\n'.join(result_lines)

def identify_pattern_matching_files(directory):
    """Find all Java files with pattern matching instanceof"""

    pattern_files = {}

    for java_file in Path(directory).rglob("*.java"):
        content = java_file.read_text(encoding='utf-8')

        # Find pattern matching instanceof
        pattern = r'instanceof\s+[A-Za-z_][A-Za-z0-9_.]+\s+[a-z_][a-z0-9_]+'
        matches = list(re.finditer(pattern, content))

        # Filter out false positives (normal instanceof checks)
        real_matches = []
        for match in matches:
            line_start = content.rfind('\n', 0, match.start()) + 1
            line_end = content.find('\n', match.end())
            line = content[line_start:line_end]

            # Check if it's actually pattern matching (variable used later)
            # This is a heuristic - real pattern matching has variable binding
            if '&&' in line or '||' in line or line.strip().endswith('{'):
                real_matches.append(match)

        if real_matches:
            pattern_files[str(java_file)] = len(real_matches)

    return pattern_files

def main():
    if len(sys.argv) < 2:
        print("Usage: python3 batch_fix_pattern_matching.py <directory>")
        print("Example: python3 batch_fix_pattern_matching.py src/main/java/com/minicad")
        sys.exit(1)

    directory = sys.argv[1]

    print(f"Scanning {directory} for pattern matching instanceof...")

    pattern_files = identify_pattern_matching_files(directory)

    print(f"\nFiles with pattern matching instanceof:")
    for file, count in sorted(pattern_files.items(), key=lambda x: x[1], reverse=True):
        rel_path = Path(file).relative_to(directory)
        print(f"  {rel_path}: {count} patterns")

    print(f"\nTotal: {len(pattern_files)} files, {sum(pattern_files.values())} patterns")

    print("\n" + "="*60)
    print("NOTE: This script identifies patterns for manual fixing.")
    print("Complex pattern matching requires manual conversion.")
    print("="*60)

if __name__ == '__main__':
    main()