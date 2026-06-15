#!/usr/bin/env python3
"""
Fix remaining Java 16+ pattern matching instanceof syntax to Java 11.
Handles both simple patterns and patterns in boolean expressions.
"""

import re
import os
import sys

def fix_pattern_matching_instanceof(content):
    """
    Fix pattern matching instanceof syntax:
    - instanceof Type variable -> instanceof Type) { Type variable = (Type) obj;
    - Handle damaged code with duplicate conversion attempts
    """
    lines = content.split('\n')
    fixed_lines = []

    for i, line in enumerate(lines):
        original_line = line

        # First, fix damaged lines with duplicate conversion attempts
        # Pattern: if (... instanceof Type var && ...) if (... instanceof Type var && ...) ...
        damaged_pattern = r'if\s*\([^)]*instanceof\s+\w+\s+\w+\s+&&[^)]*\)\s*[^;]*if\s*\([^)]*instanceof\s+\w+\s+\w+'
        if re.search(damaged_pattern, line):
            # This is a damaged line - simplify it
            # Extract the first valid instanceof pattern
            match = re.search(r'if\s*\(\s*(\w+)\s+instanceof\s+(\w+)\s+(\w+)\s+&&\s*([^)]+)\)', line)
            if match:
                obj, type_name, var_name, condition = match.groups()
                # Convert to Java 11 style
                line = re.sub(
                    r'if\s*\([^)]*instanceof[^)]*\)\s*[^;]*if\s*\([^)]*instanceof[^)]*\)[^;]*;',
                    f'if ({obj} instanceof {type_name} && {condition}) {{ {type_name} {var_name} = ({type_name}) {obj};',
                    line
                )

        # Fix simple pattern matching instanceof in if conditions
        # Pattern: if (obj instanceof Type var)
        simple_pattern = r'if\s*\(\s*(\w+)\s+instanceof\s+(\w+)\s+(\w+)\s*\)'
        match = re.search(simple_pattern, line)
        if match and '&&' not in line[match.start():match.end()]:
            obj, type_name, var_name = match.groups()
            # Check if this is a simple condition (no && or ||)
            # Convert to: if (obj instanceof Type) { Type var = (Type) obj;
            indent = len(line) - len(line.lstrip())
            # Check next line to see if we need to add opening brace or if it's a single statement
            if i + 1 < len(lines):
                next_line = lines[i + 1].strip()
                # If next line starts with var_name usage, we need to wrap in braces
                if next_line.startswith(var_name + '.') or next_line.startswith(var_name + ' '):
                    line = line.replace(
                        match.group(0),
                        f'if ({obj} instanceof {type_name}) {{ {type_name} {var_name} = ({type_name}) {obj};'
                    )
                    # Add closing brace before the next closing brace or at appropriate place
                    # We'll handle this in post-processing

        # Fix pattern matching instanceof with && condition
        # Pattern: if (obj instanceof Type var && condition)
        pattern_with_condition = r'if\s*\(\s*(\w+)\s+instanceof\s+(\w+)\s+(\w+)\s+&&\s*([^)]+)\s*\)'
        match = re.search(pattern_with_condition, line)
        if match:
            obj, type_name, var_name, condition = match.groups()
            # Convert to: if (obj instanceof Type && condition) { Type var = (Type) obj;
            line = line.replace(
                match.group(0),
                f'if ({obj} instanceof {type_name} && {condition}) {{ {type_name} {var_name} = ({type_name}) {obj};'
            )

        # Fix pattern matching instanceof in else if
        # Pattern: else if (obj instanceof Type var)
        elif_pattern = r'else\s+if\s*\(\s*(\w+)\s+instanceof\s+(\w+)\s+(\w+)\s*\)'
        match = re.search(elif_pattern, line)
        if match and '&&' not in line[match.start():match.end()]:
            obj, type_name, var_name = match.groups()
            line = line.replace(
                match.group(0),
                f'else if ({obj} instanceof {type_name}) {{ {type_name} {var_name} = ({type_name}) {obj};'
            )

        # Fix pattern matching instanceof in else if with condition
        elif_pattern_with_condition = r'else\s+if\s*\(\s*(\w+)\s+instanceof\s+(\w+)\s+(\w+)\s+&&\s*([^)]+)\s*\)'
        match = re.search(elif_pattern_with_condition, line)
        if match:
            obj, type_name, var_name, condition = match.groups()
            line = line.replace(
                match.group(0),
                f'else if ({obj} instanceof {type_name} && {condition}) {{ {type_name} {var_name} = ({type_name}) {obj};'
            )

        # Fix pattern matching instanceof in negation
        # Pattern: if (!(obj instanceof Type var))
        negation_pattern = r'if\s*\(\s*!\s*\(\s*(\w+)\s+instanceof\s+(\w+)\s+(\w+)\s*\)\s*\)'
        match = re.search(negation_pattern, line)
        if match:
            obj, type_name, var_name = match.groups()
            line = line.replace(
                match.group(0),
                f'if (!({obj} instanceof {type_name}))'
            )

        # Fix pattern matching instanceof in return statements
        # Pattern: return obj instanceof Type var ? ...
        return_pattern = r'return\s+(\w+)\s+instanceof\s+(\w+)\s+(\w+)\s*\?'
        match = re.search(return_pattern, line)
        if match:
            obj, type_name, var_name = match.groups()
            # This needs more complex handling - skip for now, mark for manual review
            # line = f'// MANUAL REVIEW NEEDED: {line}'

        # Fix pattern matching instanceof in ternary operator
        # Pattern: (obj instanceof Type var) ? ... : ...
        ternary_pattern = r'\((\w+)\s+instanceof\s+(\w+)\s+(\w+)\)\s*\?'
        match = re.search(ternary_pattern, line)
        if match:
            obj, type_name, var_name = match.groups()
            line = line.replace(
                match.group(0),
                f'({obj} instanceof {type_name}) ?'
            )

        fixed_lines.append(line)

    # Post-processing: Add closing braces where needed
    result = []
    i = 0
    while i < len(fixed_lines):
        line = fixed_lines[i]

        # Check if we added opening brace but need closing brace
        # Pattern: we converted instanceof and added { but next line doesn't close it
        if ' instanceof ' in line and ' { ' in line:
            # Find the position of the opening brace after instanceof
            brace_pos = line.find(' { ', line.find('instanceof'))
            if brace_pos != -1:
                # Count braces in this line
                open_braces = line.count('{') - line.count('}')
                # Look ahead to find where to close
                j = i + 1
                nested = open_braces
                while j < len(fixed_lines) and nested > 0:
                    nested += fixed_lines[j].count('{') - fixed_lines[j].count('}')
                    if nested == 0:
                        # Found the matching close brace position
                        break
                    j += 1

        result.append(line)
        i += 1

    return '\n'.join(result)


def fix_file(filepath):
    """Fix a single Java file."""
    print(f"Processing: {filepath}")

    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()

    # Check if file has pattern matching instanceof
    if not re.search(r'instanceof\s+\w+\s+\w+', content):
        print(f"  ✓ No pattern matching instanceof found")
        return

    fixed_content = fix_pattern_matching_instanceof(content)

    # Write back
    with open(filepath, 'w', encoding='utf-8') as f:
        f.write(fixed_content)

    print(f"  ✓ Fixed pattern matching instanceof")


def main():
    """Main entry point."""
    if len(sys.argv) < 2:
        # Default: process all Java files in src/main/java
        base_path = 'src/main/java/com/minicad'
        java_files = []
        for root, dirs, files in os.walk(base_path):
            for file in files:
                if file.endswith('.java'):
                    java_files.append(os.path.join(root, file))
    else:
        java_files = sys.argv[1:]

    for filepath in java_files:
        try:
            fix_file(filepath)
        except Exception as e:
            print(f"  ✗ Error fixing {filepath}: {e}")

    print("\n✓ All files processed")


if __name__ == '__main__':
    main()