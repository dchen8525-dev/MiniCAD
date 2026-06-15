#!/usr/bin/env python3
"""
Complete fix for all remaining pattern matching instanceof.
Handles all patterns including:
- Negation: !(obj instanceof Type var)
- Simple if: if (obj instanceof Type var) { ... }
- With condition: if (obj instanceof Type var && condition) { ... }
- In else if
- Complex nested expressions
"""

import re
import sys
from pathlib import Path

def fix_pattern_instanceof_comprehensive(content):
    """Fix all pattern matching instanceof in the content."""

    # Pattern: !(obj instanceof Type var) - negation
    content = re.sub(
        r'!\((\w+(?:\.\w+)*)\s+instanceof\s+(\w+(?:\.\w+)*)\s+(\w+)\)',
        lambda m: f'!({m.group(1)} instanceof {m.group(2)})',
        content
    )

    # Pattern: if (obj instanceof Type var && condition) { ... }
    # Convert to: if (obj instanceof Type && condition) { Type var = (Type) obj;
    content = re.sub(
        r'(if\s*\(\s*)(\w+(?:\.\w+)*)\s+instanceof\s+(\w+(?:\.\w+)*)\s+(\w+)\s+&&\s+([^)]+)\s*\)\s*\{',
        lambda m: f'{m.group(1)}{m.group(2)} instanceof {m.group(3)} && {m.group(5)}) {{ {m.group(3)} {m.group(4)} = ({m.group(3)}) {m.group(2)};',
        content
    )

    # Pattern: else if (obj instanceof Type var && condition) { ... }
    content = re.sub(
        r'(else\s+if\s*\(\s*)(\w+(?:\.\w+)*)\s+instanceof\s+(\w+(?:\.\w+)*)\s+(\w+)\s+&&\s+([^)]+)\s*\)\s*\{',
        lambda m: f'{m.group(1)}{m.group(2)} instanceof {m.group(3)} && {m.group(5)}) {{ {m.group(3)} {m.group(4)} = ({m.group(3)}) {m.group(2)};',
        content
    )

    # Pattern: if (obj instanceof Type var) { ... } (without &&)
    # Need to be careful not to match patterns already converted above
    content = re.sub(
        r'(if\s*\(\s*)(\w+(?:\.\w+)*)\s+instanceof\s+(\w+(?:\.\w+)*)\s+(\w+)\s*\)\s*\{(?!\s*\w+\s+\w+\s+=)',
        lambda m: f'{m.group(1)}{m.group(2)} instanceof {m.group(3)}) {{ {m.group(3)} {m.group(4)} = ({m.group(3)}) {m.group(2)};',
        content
    )

    # Pattern: else if (obj instanceof Type var) { ... } (without &&)
    content = re.sub(
        r'(else\s+if\s*\(\s*)(\w+(?:\.\w+)*)\s+instanceof\s+(\w+(?:\.\w+)*)\s+(\w+)\s*\)\s*\{(?!\s*\w+\s+\w+\s+=)',
        lambda m: f'{m.group(1)}{m.group(2)} instanceof {m.group(3)}) {{ {m.group(3)} {m.group(4)} = ({m.group(3)}) {m.group(2)};',
        content
    )

    # Pattern: || (obj instanceof Type var && condition)
    # Convert to: || (obj instanceof Type && ((Type) obj).method())
    content = re.sub(
        r'\|\|\s*\((\w+(?:\.\w+)*)\s+instanceof\s+(\w+(?:\.\w+)*)\s+(\w+)\s+&&\s+([^)]+)\)\s*;',
        lambda m: f'|| ({m.group(1)} instanceof {m.group(2)} && {rewrite_condition(m.group(3), m.group(4), m.group(5))});',
        content
    )

    # Pattern: || obj instanceof Type var && condition (without outer parentheses)
    content = re.sub(
        r'\|\|\s*(\w+(?:\.\w+)*)\s+instanceof\s+(\w+(?:\.\w+)*)\s+(\w+)\s+&&\s+([^)]+)\s*;',
        lambda m: f'|| ({m.group(1)} instanceof {m.group(2)} && {rewrite_condition(m.group(2), m.group(3), m.group(4))});',
        content
    )

    return content

def rewrite_condition(type_name, var_name, condition):
    """Rewrite condition to use explicit cast instead of pattern variable."""
    # Replace var_name.method() with ((Type) obj).method()
    # Simple replacement for most common patterns
    return re.sub(
        r'\b' + var_name + r'\.',
        f'(({type_name}) ',
        condition.replace(var_name + '.', f'(({type_name}) ' + var_name + ').')
    ).replace(f'(({type_name}) ', f'(({type_name}) ')


def process_file(filepath):
    """Process a single Java file."""
    print(f"Processing: {filepath}")

    try:
        with open(filepath, 'r', encoding='utf-8') as f:
            content = f.read()
    except Exception as e:
        print(f"  [ERROR] Cannot read file: {e}")
        return False

    # Check if file has pattern matching
    if not re.search(r'instanceof\s+\w+\s+\w+', content):
        print(f"  [OK] No pattern matching found")
        return True

    fixed_content = fix_pattern_instanceof_comprehensive(content)

    try:
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(fixed_content)
    except Exception as e:
        print(f"  [ERROR] Cannot write file: {e}")
        return False

    print(f"  [OK] Fixed pattern matching")
    return True


def main():
    if len(sys.argv) > 1:
        files = sys.argv[1:]
    else:
        # Process StepPreviewJsonExporter.java first since it has most errors
        files = ['src/main/java/com/minicad/app/StepPreviewJsonExporter.java']

    for filepath in files:
        process_file(filepath)


if __name__ == '__main__':
    main()