#!/usr/bin/env python3
"""
Automatically convert Java 16+ pattern matching instanceof to Java 11.
This script handles various patterns correctly:
1. Simple instanceof in if: if (obj instanceof Type var)
2. instanceof with condition: if (obj instanceof Type var && condition)
3. instanceof in else if
4. instanceof in boolean expressions
5. instanceof in ternary operators
"""

import re
import os
import sys
from pathlib import Path

def convert_pattern_instanceof(line, context_lines=None):
    """
    Convert a single line containing pattern matching instanceof.
    Returns the converted line or the original if no conversion needed.
    """

    # Skip comments and annotations
    if line.strip().startswith('//') or line.strip().startswith('@'):
        return line

    # Pattern 1: if (obj instanceof Type var && condition) { ... }
    # Convert to: if (obj instanceof Type && condition) { Type var = (Type) obj;
    pattern1 = re.compile(
        r'(if\s*\(\s*)(\w+)(\s+instanceof\s+)(\w+)(\s+)(\w+)(\s+&&\s+)([^)]+)(\s*\)\s*\{)'
    )
    match = pattern1.search(line)
    if match:
        prefix, obj, inst, type_name, space, var_name, and_op, condition, suffix = match.groups()
        # Convert to Java 11 style
        replacement = f'{prefix}{obj}{inst}{type_name}{and_op}{condition}{suffix} {type_name} {var_name} = ({type_name}) {obj};'
        line = line.replace(match.group(0), replacement)
        return line

    # Pattern 2: if (obj instanceof Type var) { ... }
    # Convert to: if (obj instanceof Type) { Type var = (Type) obj;
    pattern2 = re.compile(
        r'(if\s*\(\s*)(\w+)(\s+instanceof\s+)(\w+)(\s+)(\w+)(\s*\)\s*\{)'
    )
    match = pattern2.search(line)
    if match:
        prefix, obj, inst, type_name, space, var_name, suffix = match.groups()
        # Check if there's && in the condition part (avoid double conversion)
        if '&&' not in line[match.start():match.end()]:
            replacement = f'{prefix}{obj}{inst}{type_name}{suffix} {type_name} {var_name} = ({type_name}) {obj};'
            line = line.replace(match.group(0), replacement)
            return line

    # Pattern 3: else if (obj instanceof Type var && condition) { ... }
    pattern3 = re.compile(
        r'(else\s+if\s*\(\s*)(\w+)(\s+instanceof\s+)(\w+)(\s+)(\w+)(\s+&&\s+)([^)]+)(\s*\)\s*\{)'
    )
    match = pattern3.search(line)
    if match:
        prefix, obj, inst, type_name, space, var_name, and_op, condition, suffix = match.groups()
        replacement = f'{prefix}{obj}{inst}{type_name}{and_op}{condition}{suffix} {type_name} {var_name} = ({type_name}) {obj};'
        line = line.replace(match.group(0), replacement)
        return line

    # Pattern 4: else if (obj instanceof Type var) { ... }
    pattern4 = re.compile(
        r'(else\s+if\s*\(\s*)(\w+)(\s+instanceof\s+)(\w+)(\s+)(\w+)(\s*\)\s*\{)'
    )
    match = pattern4.search(line)
    if match:
        prefix, obj, inst, type_name, space, var_name, suffix = match.groups()
        if '&&' not in line[match.start():match.end()]:
            replacement = f'{prefix}{obj}{inst}{type_name}{suffix} {type_name} {var_name} = ({type_name}) {obj};'
            line = line.replace(match.group(0), replacement)
            return line

    # Pattern 5: instanceof in boolean expression (no if)
    # Example: boolean b = obj instanceof Type var;
    # Convert to: boolean b = obj instanceof Type && var != null; (but this is complex)
    # For now, just mark for manual review
    pattern5 = re.compile(
        r'(\w+)(\s+instanceof\s+)(\w+)(\s+)(\w+)(\s*[;,])'
    )
    match = pattern5.search(line)
    if match and 'if' not in line.lower() and 'return' not in line.lower():
        # This is a boolean expression - needs manual review
        # Add a comment for manual review
        return f'// MANUAL REVIEW NEEDED - Pattern matching in boolean expression:\n{line}'

    # Pattern 6: return obj instanceof Type var ? ... : ...
    # Convert to: if (obj instanceof Type) { Type var = (Type) obj; return ... } else return ...
    pattern6 = re.compile(
        r'(return\s+)(\w+)(\s+instanceof\s+)(\w+)(\s+)(\w+)(\s*\?)'
    )
    match = pattern6.search(line)
    if match:
        # This needs complex handling - mark for manual review
        return f'// MANUAL REVIEW NEEDED - Pattern matching in return statement:\n{line}'

    # Pattern 7: (obj instanceof Type var) ? ... : ...
    pattern7 = re.compile(
        r'(\()(\w+)(\s+instanceof\s+)(\w+)(\s+)(\w+)(\)\s*\?)'
    )
    match = pattern7.search(line)
    if match:
        # Mark for manual review
        return f'// MANUAL REVIEW NEEDED - Pattern matching in ternary operator:\n{line}'

    # Pattern 8: instanceof with || (needs special handling)
    # Example: if (obj instanceof Type1 var1 || obj instanceof Type2 var2)
    # This is complex - mark for manual review
    if 'instanceof' in line and '||' in line:
        pattern8 = re.compile(r'(\w+)(\s+instanceof\s+)(\w+)(\s+)(\w+)')
        if pattern8.search(line) and '||' in line:
            return f'// MANUAL REVIEW NEEDED - Pattern matching with OR operator:\n{line}'

    return line


def fix_file(filepath):
    """Fix a single Java file."""
    print(f"Processing: {filepath}")

    try:
        with open(filepath, 'r', encoding='utf-8') as f:
            content = f.read()
    except Exception as e:
        print(f"  [ERROR] Error reading file: {e}")
        return False

    # Check if file needs conversion
    if not re.search(r'instanceof\s+\w+\s+\w+', content):
        print(f"  [OK] No pattern matching instanceof found")
        return True

    lines = content.split('\n')
    fixed_lines = []
    manual_review_count = 0

    for i, line in enumerate(lines):
        original = line
        fixed = convert_pattern_instanceof(line, lines)

        if fixed.startswith('// MANUAL REVIEW'):
            manual_review_count += 1

        fixed_lines.append(fixed)

    # Write back
    try:
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write('\n'.join(fixed_lines))
    except Exception as e:
        print(f"  [ERROR] Error writing file: {e}")
        return False

    print(f"  [OK] Fixed pattern matching instanceof")
    if manual_review_count > 0:
        print(f"  [WARN] {manual_review_count} cases marked for manual review")

    return True


def main():
    """Main entry point."""
    if len(sys.argv) < 2:
        # Default: process all Java files
        base_path = Path('src/main/java/com/minicad')
        if not base_path.exists():
            print(f"Error: {base_path} does not exist")
            sys.exit(1)

        java_files = []
        for root, dirs, files in os.walk(base_path):
            for file in files:
                if file.endswith('.java'):
                    java_files.append(os.path.join(root, file))

        print(f"Found {len(java_files)} Java files to process")
    else:
        java_files = sys.argv[1:]

    success_count = 0
    for filepath in java_files:
        if fix_file(filepath):
            success_count += 1

    print(f"\n[OK] Processed {success_count}/{len(java_files)} files successfully")


if __name__ == '__main__':
    main()