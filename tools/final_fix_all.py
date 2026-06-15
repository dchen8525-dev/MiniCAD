#!/usr/bin/env python3
"""
Final comprehensive fix for ALL remaining pattern matching instanceof.
Uses simple regex patterns to convert all occurrences.
"""

import re

def fix_all_pattern_matching(filepath):
    """Fix all remaining pattern matching in a file."""

    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()

    # Pattern: instanceof Type var && condition
    # Convert: instanceof Type && ((Type) obj).condition
    # Then add: Type var = (Type) obj;

    # Strategy: Replace all at once, then fix structure

    # Step 1: Replace all "instanceof Type var &&"
    content = re.sub(
        r'(\w+(?:\.\w+)*\(\)?)\s+instanceof\s+(\w+(?:\.\w+)*)\s+(\w+)\s+&&\s+(\w+)\.',
        lambda m: f'{m.group(1)} instanceof {m.group(2)} && (({m.group(2)}) {m.group(1)}).',
        content
    )

    # Step 2: Replace all "instanceof Type var)"  (at end of condition)
    # This needs special handling to add variable declaration after the opening brace
    content = re.sub(
        r'(\w+(?:\.\w+)*\(\)?)\s+instanceof\s+(\w+(?:\.\w+)*)\s+(\w+)\s*\)\s*\{',
        lambda m: f'{m.group(1)} instanceof {m.group(2)}) {{ {m.group(2)} {m.group(3)} = ({m.group(2)}) {m.group(1)};',
        content
    )

    # Step 3: Replace negation patterns: !(obj instanceof Type var)
    content = re.sub(
        r'!\((\w+(?:\.\w+)*\(\)?)\s+instanceof\s+(\w+(?:\.\w+)*)\s+(\w+)\)',
        lambda m: f'!({m.group(1)} instanceof {m.group(2)})',
        content
    )

    # Step 4: Handle || with pattern matching
    # This is complex - just convert the instanceof part
    content = re.sub(
        r'\|\|\s*(\w+(?:\.\w+)*\(\)?)\s+instanceof\s+(\w+(?:\.\w+)*)\s+(\w+)\s+&&',
        lambda m: f'|| ({m.group(1)} instanceof {m.group(2)} &&',
        content
    )

    # Write back
    with open(filepath, 'w', encoding='utf-8') as f:
        f.write(content)

    print(f"[OK] Fixed {filepath}")

# Process all Java files
import os
for root, dirs, files in os.walk('src/main/java/com/minicad'):
    for file in files:
        if file.endswith('.java'):
            filepath = os.path.join(root, file)
            try:
                fix_all_pattern_matching(filepath)
            except Exception as e:
                print(f"[ERROR] {filepath}: {e}")