#!/usr/bin/env python3
"""Convert switch expressions to if-else chains for JDK 11"""

import re

def convert_switch_expression(code):
    """Convert a single switch expression to if-else chain"""

    # Pattern: return switch (var) { case "A" -> expr1; case "B" -> expr2; default -> expr3; };
    # Convert to: if (var.equals("A")) { return expr1; } else if (var.equals("B")) { return expr2; } else { return expr3; }

    # Pattern: return switch (var) { case TypeA -> expr1; case TypeB -> expr2; default -> expr3; };
    # Convert to: if (var == TypeA) { return expr1; } else if (var == TypeB) { return expr2; } else { return expr3; }

    lines = []
    cases = []

    # Extract switch variable
    m = re.search(r'return\s+switch\s*\(([^)]+)\)\s*\{', code)
    if not m:
        return code

    switch_var = m.group(1)

    # Extract cases
    # Case patterns:
    # case "STRING" -> expr;
    # case ENUM_VALUE, ENUM_VALUE2 -> expr;
    # case Type -> { yield expr; }
    # default -> expr;

    case_pattern = r'case\s+([^->]+)\s+->\s+(?:\{([^}]+)\}|([^;]+));'

    for m in re.finditer(case_pattern, code):
        case_values = m.group(1).strip()
        expr_in_block = m.group(2)
        expr_simple = m.group(3)

        if expr_in_block:
            expr = expr_in_block.strip()
            # Remove 'yield' keyword
            if 'yield' in expr:
                expr = re.sub(r'yield\s+', '', expr)
        else:
            expr = expr_simple.strip()

        # Determine if string comparison or enum comparison
        is_string = case_values.startswith('"') or case_values.startswith("'")

        cases.append({
            'values': case_values.split(','),
            'expr': expr,
            'is_string': is_string
        })

    # Build if-else chain
    for i, case in enumerate(cases):
        for j, value in enumerate(case['values']):
            value = value.strip()

            if case['is_string']:
                condition = f'{switch_var}.equals({value})'
            else:
                # Check if it's 'default'
                if value == 'default':
                    condition = 'true'
                else:
                    condition = f'{switch_var} == {value}'

            if j == 0:
                # First value in case group
                if i == 0:
                    lines.append(f'if ({condition}) {{')
                else:
                    lines.append(f'}} else if ({condition}) {{')
            else:
                # Multiple values for same case (OR condition)
                if case['is_string']:
                    lines.append(f'}} else if ({switch_var}.equals({value})) {{')
                else:
                    lines.append(f'}} else if ({switch_var} == {value}) {{')

        lines.append(f'    return {case["expr"]};')

    lines.append('}')

    return '\n'.join(lines)

def fix_file(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        original = f.read()

    content = original
    lines = content.split('\n')
    output = []

    i = 0
    while i < len(lines):
        line = lines[i]

        # Detect switch expression start
        if 'return switch' in line:
            # Collect the full switch expression
            switch_lines = [line]
            brace_count = line.count('{') - line.count('}')

            while brace_count > 0 and i + 1 < len(lines):
                i += 1
                switch_lines.append(lines[i])
                brace_count += lines[i].count('{') - lines[i].count('}')

            switch_code = '\n'.join(switch_lines)

            # Convert switch expression
            try:
                converted = convert_switch_expression(switch_code)
                output.append(converted)
            except:
                # If conversion fails, keep original and add TODO
                output.append(switch_code)
                output.append('        // TODO: JDK11 - Manual conversion needed')

            i += 1
            continue

        output.append(line)
        i += 1

    new_content = '\n'.join(output)

    if new_content != original:
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(new_content)
        return True
    return False

# Manual processing for complex patterns
print("Use: python fix_switch_expr.py <file.java>")
print("This script handles basic switch expressions with arrow syntax.")
print("Complex patterns with nested blocks may need manual review.")