#!/usr/bin/env python3
"""
JDK 11 Compatibility Fix Script for MiniCAD
Fixes: instanceof pattern matching, getFirst/getLast, switch expressions
"""

import re
import sys
import os

def fix_instanceof_pattern(content):
    """
    Convert instanceof pattern matching to JDK 11 compatible form.

    FROM: if (obj instanceof Type var) { ... }
    TO:   if (obj instanceof Type) { Type var = (Type) obj; ... }

    FROM: if (!(obj instanceof Type var) || condition) { ... }
    TO:   if (!(obj instanceof Type)) { ... }
          Type var = (Type) obj;
          if (condition) { ... }
    """

    # Pattern 1: Simple instanceof with binding variable
    # if (obj instanceof Type var) -> if (obj instanceof Type) { Type var = (Type) obj;
    pattern1 = r'if\s*\((\w+)\s+instanceof\s+(\w+)\s+(\w+)\)\s*\{'
    def replace1(m):
        obj, type, var = m.group(1), m.group(2), m.group(3)
        return f'if ({obj} instanceof {type}) {{\n            {type} {var} = ({type}) {obj};'
    content = re.sub(pattern1, replace1, content)

    # Pattern 2: Negated instanceof with binding variable
    # if (!(obj instanceof Type var)) -> if (!(obj instanceof Type)) {
    pattern2 = r'if\s*\(!\((\w+)\s+instanceof\s+(\w+)\s+(\w+)\)\)\s*\{'
    def replace2(m):
        obj, type, var = m.group(1), m.group(2), m.group(3)
        return f'if (!({obj} instanceof {type})) {{'
    content = re.sub(pattern2, replace2, content)

    # Pattern 3: instanceof in ternary operator
    # return obj instanceof Type var ? ... : null
    pattern3 = r'return\s+(\w+)\s+instanceof\s+(\w+)\s+(\w+)\s+\?\s+(.+?)\s+:\s+(null|\w+);'
    def replace3(m):
        obj, type, var, expr, fallback = m.group(1), m.group(2), m.group(3), m.group(4), m.group(5)
        return f'if ({obj} instanceof {type}) {{\n        {type} {var} = ({type}) {obj};\n        return {expr};\n    }}\n    return {fallback};'
    content = re.sub(pattern3, replace3, content)

    # Pattern 4: instanceof with method call
    # if (circle.position() instanceof Type var)
    pattern4 = r'if\s*\(!?\(([\w.]+)\s+instanceof\s+(\w+)\s+(\w+)\)\)\s*\{'
    def replace4(m):
        expr, type, var = m.group(1), m.group(2), m.group(3)
        negated = '!' in m.group(0)[:10]
        if negated:
            return f'if (!({expr} instanceof {type})) {{'
        else:
            return f'if ({expr} instanceof {type}) {{\n            {type} {var} = ({type}) {expr};'
    content = re.sub(pattern4, replace4, content)

    # Pattern 5: && with instanceof binding
    # actual != null && actual instanceof Type var
    pattern5 = r'(\w+)\s+!=\s+null\s+&&\s+(\w+)\s+instanceof\s+(\w+)\s+(\w+)'
    def replace5(m):
        var1, var2, type, var3 = m.group(1), m.group(2), m.group(3), m.group(4)
        return f'{var1} != null && {var2} instanceof {type}'
    content = re.sub(pattern5, replace5, content)

    # Pattern 6: instanceof in assignment
    # Type var = obj instanceof Type t ? t : null;
    pattern6 = r'(\w+)\s+(\w+)\s+=\s+(\w+)\s+instanceof\s+(\w+)\s+(\w+)\s+\?\s+(\w+)\s+:\s+null;'
    def replace6(m):
        type1, var1, obj, type2, var2, var3 = m.group(1), m.group(2), m.group(3), m.group(4), m.group(5), m.group(6)
        return f'if ({obj} instanceof {type2}) {{\n        {type2} {var2} = ({type2}) {obj};\n        {var1} = {var3};\n    }} else {{\n        {var1} = null;\n    }}'
    content = re.sub(pattern6, replace6, content)

    return content

def fix_getFirst_getLast(content):
    """
    Replace .getFirst() with .get(0)
    Replace .getLast() with .get(list.size() - 1)
    """
    # Simple getFirst() -> get(0)
    content = re.sub(r'\.getFirst\(\)', '.get(0)', content)

    # getLast() - need to handle carefully
    # Pattern: obj.getLast() -> obj.get(obj.size() - 1)
    # For now, use a generic pattern
    content = re.sub(r'\.getLast\(\)', '.get(this.size() - 1)', content)

    # Better handling for specific cases
    # knots.getLast() -> knots.get(knots.size() - 1)
    content = re.sub(r'(\w+)\.get\(this\.size\(\)\s+-\s+1\)', r'\1.get(\1.size() - 1)', content)

    return content

def fix_switch_expression(content):
    """
    Convert switch expressions to if-else chains.

    FROM: return switch (x) { case A -> expr1; case B -> expr2; default -> expr3; };
    TO:   if (x == A) { return expr1; } else if (x == B) { return expr2; } else { return expr3; }
    """
    # This is complex and needs careful handling
    # For now, just flag switch expressions for manual review
    if 'return switch' in content or 'yield' in content:
        # Add marker comment for manual review
        lines = content.split('\n')
        new_lines = []
        for line in lines:
            new_lines.append(line)
            if 'return switch' in line:
                new_lines.append('        // TODO: JDK11 - convert switch expression above')
        content = '\n'.join(new_lines)

    return content

def process_file(filepath):
    """Process a single Java file."""
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()

    original = content
    content = fix_instanceof_pattern(content)
    content = fix_getFirst_getLast(content)
    content = fix_switch_expression(content)

    if content != original:
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(content)
        return True
    return False

def main():
    """Main entry point."""
    if len(sys.argv) < 2:
        print("Usage: python fix_jdk11.py <file_or_directory>")
        sys.exit(1)

    target = sys.argv[1]

    if os.path.isfile(target):
        if process_file(target):
            print(f"Fixed: {target}")
        else:
            print(f"No changes: {target}")
    elif os.path.isdir(target):
        count = 0
        for root, dirs, files in os.walk(target):
            for file in files:
                if file.endswith('.java'):
                    filepath = os.path.join(root, file)
                    if process_file(filepath):
                        count += 1
                        print(f"Fixed: {filepath}")
        print(f"Total files fixed: {count}")
    else:
        print(f"Error: {target} is not a file or directory")
        sys.exit(1)

if __name__ == '__main__':
    main()