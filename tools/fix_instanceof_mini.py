#!/usr/bin/env python3
"""
MiniCAD-specific instanceof pattern matching fixer
Handles common patterns found in StepCadBuilder.java, StepEntityResolver.java, etc.
"""

import re

def fix_negated_instanceof_with_throw(content):
    """
    Pattern: if (!(obj instanceof Type var)) { throw ...; }
    Result:  if (!(obj instanceof Type)) { throw ...; }
             Type var = (Type) obj;
    """
    pattern = r'if\s*\(!\(([\w.()]+)\s+instanceof\s+(\w+)\s+(\w+)\)\)\s*\{\s*throw\s+[^}]+\}\s*'
    matches = list(re.finditer(pattern, content))

    for m in matches:
        obj_expr = m.group(1)
        type_name = m.group(2)
        var_name = m.group(3)

        old_code = m.group(0)
        new_code = f'if (!({obj_expr} instanceof {type_name})) {{\n            throw new StepResolutionException(...);\n        }}\n        {type_name} {var_name} = ({type_name}) {obj_expr};\n        '

        # Don't auto-replace throws - need manual review
        # Just add a TODO marker
        pass

    return content

def fix_simple_instanceof_if(content):
    """
    Pattern: if (obj instanceof Type var) { ... }
    Result:  if (obj instanceof Type) { Type var = (Type) obj; ... }
    """
    # Match: if (expr instanceof Type var) { ... }
    pattern = r'if\s*\(([\w.()]+)\s+instanceof\s+(\w+)\s+(\w+)\)\s*\{'

    def replacer(m):
        expr = m.group(1)
        type_name = m.group(2)
        var_name = m.group(3)
        return f'if ({expr} instanceof {type_name}) {{\n            {type_name} {var_name} = ({type_name}) {expr};'

    content = re.sub(pattern, replacer, content)
    return content

def fix_negated_instanceof_if(content):
    """
    Pattern: if (!(obj instanceof Type var)) { return null; }
             other_code_using_var;

    Result:  if (!(obj instanceof Type)) { return null; }
             Type var = (Type) obj;
             other_code_using_var;
    """
    # This needs careful handling - just mark for manual review
    pattern = r'if\s*\(!\(([\w.()]+)\s+instanceof\s+(\w+)\s+(\w+)\)\)'

    def marker(m):
        return f'/* TODO: JDK11 */ if (!({m.group(1)} instanceof {m.group(2)}))'

    content = re.sub(pattern, marker, content)
    return content

def fix_instanceof_ternary(content):
    """
    Pattern: return obj instanceof Type var ? expr : null;
    Result:  if (obj instanceof Type) { Type var = (Type) obj; return expr; }
             return null;
    """
    pattern = r'return\s+([\w.()]+)\s+instanceof\s+(\w+)\s+(\w+)\s+\?\s+([^:]+)\s+:\s+(null|\w+);'

    def replacer(m):
        expr_obj = m.group(1)
        type_name = m.group(2)
        var_name = m.group(3)
        return_expr = m.group(4).strip()
        fallback = m.group(5)

        return f'if ({expr_obj} instanceof {type_name}) {{\n        {type_name} {var_name} = ({type_name}) {expr_obj};\n        return {return_expr};\n    }}\n    return {fallback};'

    content = re.sub(pattern, replacer, content)
    return content

def fix_instanceof_assignment(content):
    """
    Pattern: Type var1 = obj instanceof Type2 var2 ? var2 : null;
    Result:  Type var1;\n    if (obj instanceof Type2) { Type2 var2 = (Type2) obj; var1 = var2; } else { var1 = null; }
    """
    pattern = r'(\w+)\s+(\w+)\s+=\s+([\w.()]+)\s+instanceof\s+(\w+)\s+(\w+)\s+\?\s+(\w+)\s+:\s+null;'

    def replacer(m):
        type1 = m.group(1)
        var1 = m.group(2)
        expr_obj = m.group(3)
        type2 = m.group(4)
        var2 = m.group(5)
        var_use = m.group(6)

        # Check if var1 == var_use
        if var1 == var_use:
            return f'{type1} {var1};\n    if ({expr_obj} instanceof {type2}) {{\n        {type2} {var2} = ({type2}) {expr_obj};\n        {var1} = {var2};\n    }} else {{\n        {var1} = null;\n    }}'
        else:
            return m.group(0)  # Keep original for manual review

    content = re.sub(pattern, replacer, content)
    return content

def process_file(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        original = f.read()

    content = original
    content = fix_simple_instanceof_if(content)
    content = fix_instanceof_ternary(content)
    content = fix_instanceof_assignment(content)

    if content != original:
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(content)
        return True
    return False

# Manual patterns to fix:
# 1. Negated instanceof with compound conditions
# 2. instanceof with && or || operators
# 3. instanceof in else-if chains

print("Processing MiniCAD JDK11 instanceof patterns...")
print("Use: python fix_instanceof_mini.py <file.java>")