#!/usr/bin/env python3
"""
Final manual fix for remaining pattern matching instanceof.
Simple and direct approach.
"""

import re

def fix_remaining_patterns(filepath):
    """Fix remaining pattern matching in a file."""

    with open(filepath, 'r', encoding='utf-8') as f:
        lines = f.readlines()

    fixed_lines = []
    i = 0
    while i < len(lines):
        line = lines[i]

        # Pattern 1: !(obj instanceof Type var) || var.method()
        # This is complex - need to handle carefully
        match = re.search(r'!\((\w+(?:\.\w+)*\(\))\s+instanceof\s+(\w+)\s+(\w+)\)\s*\|\|\s*(\w+)\.', line)
        if match:
            obj_call, type_name, var_name, var_use = match.groups()
            # Convert to: !(obj instanceof Type) || ((Type) obj).method()
            line = line.replace(
                match.group(0),
                f'!({obj_call} instanceof {type_name}) || (({type_name}) {obj_call}).'
            )

        # Pattern 2: if (!(obj instanceof Type var) || var.method()) { return null; }
        match = re.search(
            r'if\s*\(\s*!\((\w+(?:\.\w+)*\(\))\s+instanceof\s+(\w+)\s+(\w+)\)\s*\|\|\s*(\w+)\.\w+\(\)\.\w+\(\)\s*!=\s*\d+\s*\)\s*\{',
            line
        )
        if match:
            obj_call, type_name, var_name, var_use = match.groups()
            # Need to extract the full condition
            full_match = re.search(
                r'if\s*\(\s*!\(([^)]+)\s+instanceof\s+(\w+)\s+\w+\)\s*\|\|\s+([^)]+)\)',
                line
            )
            if full_match:
                obj_expr, type_name2, condition = full_match.groups()
                # Split condition to find var_name usage
                var_match = re.search(r'(\w+)\.', condition)
                if var_match:
                    var_name2 = var_match.group(1)
                    # Replace
                    line = re.sub(
                        r'!\([^)]+\s+instanceof\s+\w+\s+\w+\)',
                        f'!({obj_expr} instanceof {type_name2})',
                        line
                    )
                    line = re.sub(
                        r'\|\|\s+' + var_name2 + r'\.',
                        f'|| (({type_name2}) {obj_expr}).',
                        line
                    )

        # Pattern 3: if (obj instanceof Type var) { ... }
        # Simple pattern without && or ||
        match = re.search(
            r'(if\s*\(\s*)(\w+)\s+instanceof\s+(\w+(?:\.\w+)*)\s+(\w+)\s*\)\s*\{(?!\s*\3\s+\4\s*=)',
            line
        )
        if match and '&&' not in line and '||' not in line:
            prefix, obj, type_name, var_name = match.groups()
            # Check if next line uses var_name
            if i + 1 < len(lines) and var_name in lines[i + 1]:
                line = f'{prefix}{obj} instanceof {type_name}) {{ {type_name} {var_name} = ({type_name}) {obj};'

        # Pattern 4: else if (obj instanceof Type var && condition) { ... }
        match = re.search(
            r'(else\s+if\s*\(\s*)(\w+)\s+instanceof\s+(\w+)\s+(\w+)\s+&&\s+([^)]+)\)\s*\{(?!\s*\3\s+\4\s*=)',
            line
        )
        if match:
            prefix, obj, type_name, var_name, condition = match.groups()
            # Replace var_name in condition
            new_condition = condition.replace(f'{var_name}.', f'(({type_name}) {obj}).')
            line = f'{prefix}{obj} instanceof {type_name} && {new_condition}) {{ {type_name} {var_name} = ({type_name}) {obj};'

        # Pattern 5: if (obj instanceof Type var && condition) { ... }
        match = re.search(
            r'(if\s*\(\s*)(\w+)\s+instanceof\s+(\w+)\s+(\w+)\s+&&\s+([^)]+)\)\s*\{(?!\s*\3\s+\4\s*=)',
            line
        )
        if match:
            prefix, obj, type_name, var_name, condition = match.groups()
            new_condition = condition.replace(f'{var_name}.', f'(({type_name}) {obj}).')
            line = f'{prefix}{obj} instanceof {type_name} && {new_condition}) {{ {type_name} {var_name} = ({type_name}) {obj};'

        fixed_lines.append(line)
        i += 1

    with open(filepath, 'w', encoding='utf-8') as f:
        f.writelines(fixed_lines)

    print(f"[OK] Fixed {filepath}")


# Process all files with remaining pattern matching
files = [
    'src/main/java/com/minicad/app/StepPreviewJsonExporter.java',
    'src/main/java/com/minicad/app/PreviewUvMapper.java',
    'src/main/java/com/minicad/app/PreviewFaceBuilder.java',
]

for file in files:
    try:
        fix_remaining_patterns(file)
    except Exception as e:
        print(f"[ERROR] {file}: {e}")