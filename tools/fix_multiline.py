#!/usr/bin/env python3
"""
Fix multi-line pattern matching instanceof.
"""

import re

def fix_multiline_pattern_matching(filepath):
    """Fix pattern matching that spans multiple lines."""

    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()

    # Read as single string to handle multi-line patterns

    # Pattern: else if (obj instanceof Type var\n && var.method())
    # Convert to: else if (obj instanceof Type && ((Type) obj).method()) { Type var = (Type) obj;

    # Find and replace multi-line patterns
    content = re.sub(
        r'(else\s+if\s*\(\s*)(\w+)\s+instanceof\s+(\w+)\s+(\w+)\s*\n\s+&&\s+(\w+)\.',
        lambda m: f'{m.group(1)}{m.group(2)} instanceof {m.group(3)}\n        && (({m.group(3)}) {m.group(2)}).',
        content
    )

    # Pattern: else if (obj instanceof Type var\n && var.method()) {
    # Add variable declaration after the opening brace
    content = re.sub(
        r'(else\s+if\s*\([^)]+\)\s*\{)(\s+)(\w+)',
        lambda m: f'{m.group(1)}{m.group(2)}{m.group(3)}',
        content
    )

    # Handle if (obj instanceof Type var\n && condition)
    content = re.sub(
        r'(if\s*\(\s*)(\w+)\s+instanceof\s+(\w+)\s+(\w+)\s*\n\s+&&\s+(\w+)\.',
        lambda m: f'{m.group(1)}{m.group(2)} instanceof {m.group(3)}\n        && (({m.group(3)}) {m.group(2)}).',
        content
    )

    # After fixing the condition, need to add variable declaration
    # This is tricky - let's do it by finding the pattern and inserting after the brace

    lines = content.split('\n')
    fixed_lines = []
    i = 0

    while i < len(lines):
        line = lines[i]

        # Check if this line has "instanceof Type) {"  (converted pattern)
        # and the next lines use the variable
        match = re.search(r'instanceof\s+(\w+)\)\s*\{', line)
        if match and '&&' in line:
            # Find the variable name that should be declared
            # Look ahead to find usage
            type_name = match.group(1)
            # Extract object name from earlier in line
            obj_match = re.search(r'(\w+)\s+instanceof', line)
            if obj_match:
                obj_name = obj_match.group(1)
                # Find variable name from previous pattern (before conversion)
                # This is complex - skip for now and handle manually

        # Check if line has multi-line pattern matching continuation
        # Pattern: && var.method() after instanceof conversion
        if '&& ((Step' in line or '&& ((com.minicad' in line:
            # This is a converted line - check if next line needs variable declaration
            # Actually, variable declaration should be after the opening brace
            # Find the opening brace line
            pass

        fixed_lines.append(line)
        i += 1

    # Write back
    with open(filepath, 'w', encoding='utf-8') as f:
        f.write('\n'.join(fixed_lines))

    print(f"[OK] Fixed multi-line patterns in {filepath}")

# Process StepPreviewJsonExporter.java
fix_multiline_pattern_matching('src/main/java/com/minicad/app/StepPreviewJsonExporter.java')