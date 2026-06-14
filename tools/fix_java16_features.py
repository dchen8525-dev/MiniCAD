#!/usr/bin/env python3
"""
Java 16+ to Java 11 Compatibility Fixer

This script automatically converts Java 16+ features to Java 11 compatible code:
- Pattern matching instanceof (Java 16+) → explicit casting
- getFirst()/getLast() (Java 21+) → get(0)/get(size()-1)
- Switch expressions (Java 14+) → if-else chains (manual review needed)

Usage: python3 fix_java16_features.py <directory>
"""

import re
import os
import sys
from pathlib import Path

def fix_instanceof_pattern(content):
    """
    Convert: if (x instanceof Type var) { ... }
    To:      if (x instanceof Type) { Type var = (Type)x; ... }
    """
    # Pattern: instanceof Type variable
    pattern = r'instanceof\s+([A-Za-z_][A-Za-z0-9_]*(?:\.[A-Za-z_][A-Za-z0-9_]*)*)\s+([a-z_][a-z0-9_]*)\s*[;,{)]'

    def replace_instanceof(match):
        type_name = match.group(1)
        var_name = match.group(2)
        # Check if this is followed by { or statement
        suffix = match.group(3) if len(match.groups()) > 3 else ''
        return f'instanceof {type_name}) {{ {type_name} {var_name} = ({type_name}) value; '

    return re.sub(pattern, replace_instanceof, content)

def fix_get_first_last(content):
    """
    Convert: .getFirst() → .get(0)
    Convert: .getLast() → .get(list.size()-1)
    """
    # Fix .getFirst()
    content = re.sub(r'\.getFirst\(\)', '.get(0)', content)

    # Fix .getLast() - need to capture the list expression
    # Pattern: expression.getLast()
    pattern = r'([A-Za-z_][A-Za-z0-9_]*(?:\.\w+)*|[A-Za-z_][A-Za-z0-9_]*\(\))\.getLast\(\)'

    def replace_get_last(match):
        expr = match.group(1)
        return f'{expr}.get({expr}.size() - 1)'

    content = re.sub(pattern, replace_get_last, content)

    return content

def process_file(filepath):
    """Process a single Java file"""
    try:
        with open(filepath, 'r', encoding='utf-8') as f:
            content = f.read()

        original = content

        # Apply fixes
        content = fix_instanceof_pattern(content)
        content = fix_get_first_last(content)

        if content != original:
            # Write back
            with open(filepath, 'w', encoding='utf-8') as f:
                f.write(content)
            return True
        return False
    except Exception as e:
        print(f"Error processing {filepath}: {e}")
        return False

def main():
    if len(sys.argv) < 2:
        print("Usage: python3 fix_java16_features.py <directory>")
        print("Example: python3 fix_java16_features.py src/main/java/com/minicad")
        sys.exit(1)

    directory = Path(sys.argv[1])
    if not directory.exists():
        print(f"Directory not found: {directory}")
        sys.exit(1)

    # Find all Java files
    java_files = list(directory.rglob("*.java"))

    print(f"Found {len(java_files)} Java files to process")

    fixed_count = 0
    for java_file in java_files:
        if process_file(java_file):
            fixed_count += 1
            print(f"✓ Fixed: {java_file.relative_to(directory)}")

    print(f"\nTotal files fixed: {fixed_count}/{len(java_files)}")
    print("\nNOTE: Switch expressions require manual review.")
    print("Files with switch expressions need to be checked individually.")

if __name__ == '__main__':
    main()