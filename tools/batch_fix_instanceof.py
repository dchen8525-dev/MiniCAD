#!/usr/bin/env python3
"""Batch fix instanceof pattern matching in MiniCAD"""

import re
import sys
import glob

def fix_file(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        original = f.read()

    lines = original.split('\n')
    output_lines = []
    i = 0

    while i < len(lines):
        line = lines[i]

        # Pattern 1: Negated instanceof with throw
        # if (!(obj instanceof Type var)) { throw ... }
        m = re.search(r'if\s*\(!\(([\w.()]+)\s+instanceof\s+(\w+)\s+(\w+)\)\)\s*\{\s*throw', line)
        if m:
            obj_expr = m.group(1)
            type_name = m.group(2)
            var_name = m.group(3)

            # Extract the throw statement
            throw_match = re.search(r'throw\s+([^;]+);', lines[i] + (lines[i+1] if i+1 < len(lines) else ''))

            if throw_match:
                throw_stmt = throw_match.group(1)
                output_lines.append(f'        if (!({obj_expr} instanceof {type_name})) {{')
                output_lines.append(f'            throw {throw_stmt};')
                output_lines.append(f'        }}')
                output_lines.append(f'        {type_name} {var_name} = ({type_name}) {obj_expr};')

                # Skip the next line if we consumed it
                if ';' in lines[i] and 'throw' in lines[i]:
                    i += 1
                else:
                    i += 2  # Skip the line with throw statement
                continue

        # Pattern 2: Simple instanceof with if block start
        # if (obj instanceof Type var) {
        m = re.search(r'if\s*\(([\w.()]+)\s+instanceof\s+(\w+)\s+(\w+)\)\s*\{', line)
        if m:
            obj_expr = m.group(1)
            type_name = m.group(2)
            var_name = m.group(3)

            indent = len(line) - len(line.lstrip())
            indent_str = ' ' * indent

            output_lines.append(f'{indent_str}if ({obj_expr} instanceof {type_name}) {{')
            output_lines.append(f'{indent_str}    {type_name} {var_name} = ({type_name}) {obj_expr};')
            i += 1
            continue

        # Pattern 3: instanceof in ternary return
        # return obj instanceof Type var ? expr : null;
        m = re.search(r'return\s+([\w.()]+)\s+instanceof\s+(\w+)\s+(\w+)\s+\?\s+([^:]+)\s+:\s+(null|[\w.]+);', line)
        if m:
            obj_expr = m.group(1)
            type_name = m.group(2)
            var_name = m.group(3)
            true_expr = m.group(4).strip()
            false_expr = m.group(5)

            indent = len(line) - len(line.lstrip())
            indent_str = ' ' * indent

            output_lines.append(f'{indent_str}if ({obj_expr} instanceof {type_name}) {{')
            output_lines.append(f'{indent_str}    {type_name} {var_name} = ({type_name}) {obj_expr};')
            output_lines.append(f'{indent_str}    return {true_expr};')
            output_lines.append(f'{indent_str}}}')
            output_lines.append(f'{indent_str}return {false_expr};')
            i += 1
            continue

        # Pattern 4: instanceof in assignment (ternary)
        # Type var1 = obj instanceof Type2 var2 ? var2 : null;
        m = re.search(r'(\w+)\s+(\w+)\s+=\s+([\w.()]+)\s+instanceof\s+(\w+)\s+(\w+)\s+\?\s+(\w+)\s+:\s+null;', line)
        if m:
            type1 = m.group(1)
            var1 = m.group(2)
            obj_expr = m.group(3)
            type2 = m.group(4)
            var2 = m.group(5)
            var_use = m.group(6)

            if var_use == var2:
                indent = len(line) - len(line.lstrip())
                indent_str = ' ' * indent

                output_lines.append(f'{indent_str}{type1} {var1};')
                output_lines.append(f'{indent_str}if ({obj_expr} instanceof {type2}) {{')
                output_lines.append(f'{indent_str}    {type2} {var2} = ({type2}) {obj_expr};')
                output_lines.append(f'{indent_str}    {var1} = {var2};')
                output_lines.append(f'{indent_str}}} else {{')
                output_lines.append(f'{indent_str}    {var1} = null;')
                output_lines.append(f'{indent_str}}}')
                i += 1
                continue

        # Pattern 5: && with instanceof
        # actual != null && actual instanceof Type var
        m = re.search(r'(\w+)\s+!=\s+null\s+&&\s+(\w+)\s+instanceof\s+(\w+)\s+(\w+)', line)
        if m:
            var1 = m.group(1)
            var2 = m.group(2)
            type_name = m.group(3)
            var3 = m.group(4)

            # Remove the binding variable from instanceof
            new_line = re.sub(r'(\w+)\s+instanceof\s+(\w+)\s+(\w+)', r'\1 instanceof \2', line)
            output_lines.append(new_line)

            # Add cast after the if block
            # Need to look ahead to find the block
            # This is complex, so we'll just mark it
            i += 1
            continue

        # Keep original line
        output_lines.append(line)
        i += 1

    new_content = '\n'.join(output_lines)

    if new_content != original:
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(new_content)
        return True
    return False

def main():
    if len(sys.argv) > 1:
        files = sys.argv[1:]
    else:
        # Process all semantic and app Java files
        files = []
        files.extend(glob.glob('src/main/java/com/minicad/step/semantic/*.java'))
        files.extend(glob.glob('src/main/java/com/minicad/app/*.java'))
        files.extend(glob.glob('src/main/java/com/minicad/topology/*.java'))

    fixed_count = 0
    for filepath in files:
        if fix_file(filepath):
            print(f"Fixed: {filepath}")
            fixed_count += 1
        else:
            print(f"No changes: {filepath}")

    print(f"\nTotal files fixed: {fixed_count}")

if __name__ == '__main__':
    main()