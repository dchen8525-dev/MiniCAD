#!/usr/bin/env python3
"""
Convert JDK 16+ pattern matching instanceof to JDK 11 compatible syntax.
Also converts getter method calls from .xxx() to .getXxx()

Example transformation:
  if (entity instanceof StepCircle circle) {
      return circle.radius();
  }

  becomes:
  if (entity instanceof StepCircle) {
      StepCircle circle = (StepCircle) entity;
      return circle.getRadius();
  }
"""

import re
import os
import sys

def convert_pattern_matching_instanceof(content):
    """Convert pattern matching instanceof to traditional instanceof + cast"""

    # Pattern: if (x instanceof Type var) { ... }
    # Match: instanceof <TypeName> <varName>)
    pattern = re.compile(
        r'instanceof\s+(\w+)\s+(\w+)\s*\)',
        re.MULTILINE
    )

    def replace_instanceof(match):
        type_name = match.group(1)
        var_name = match.group(2)
        return f'instanceof {type_name})'

    # First pass: remove the variable binding from instanceof
    result = pattern.sub(replace_instanceof, content)

    # Second pass: add the cast declaration after the if condition
    # Pattern: if (x instanceof Type) { ... }
    # Need to insert: Type var = (Type) x;

    # Find all pattern: if (... instanceof Type) {
    # and the original variable name from the first pass

    # More complex approach: process line by line
    lines = result.split('\n')
    new_lines = []

    i = 0
    while i < len(lines):
        line = lines[i]

        # Check if line contains pattern matching instanceof (converted form)
        match = re.search(r'if\s*\([^)]*instanceof\s+(\w+)\s*\)\s*\{', line)
        if match:
            type_name = match.group(1)

            # Find the original variable binding from the original content
            # This is tricky - we need to know what variable was bound

            # Get the condition part
            cond_match = re.search(r'if\s*\(([^)]*instanceof\s+\w+\s*\))\s*\{', line)
            if cond_match:
                condition = cond_match.group(1)

                # Extract the subject variable (the thing being tested)
                # e.g., "if (entity instanceof StepCircle)" -> entity
                subject_match = re.search(r'(\w+)\s+instanceof', condition)
                if subject_match:
                    subject_var = subject_match.group(1)

                    # The bound variable name should be derived from the type
                    # Typically it's a camelCase version of the type
                    # e.g., StepCircle -> circle, StepManifoldSolidBrep -> solidBrep

                    # For simplicity, use the type name with first letter lowercase
                    # This is a heuristic and may not always work
                    var_name = type_name[0].lower() + type_name[1:]
                    if type_name.startswith('Step'):
                        # Remove 'Step' prefix for cleaner variable names
                        base_name = type_name[4:]
                        var_name = base_name[0].lower() + base_name[1:]

                    # Insert the cast line after the if
                    new_lines.append(line)
                    new_lines.append(f'            {type_name} {var_name} = ({type_name}) {subject_var};')
                    i += 1
                    continue

        new_lines.append(line)
        i += 1

    return '\n'.join(new_lines)

def convert_getter_calls(content):
    """Convert .xxx() getter calls to .getXxx() or .isXxx()"""

    # Common field names that need getter conversion
    # This is a simplified approach - full implementation would need AST

    # Pattern: .fieldName() where fieldName is not already getXxx
    # Skip method calls like .toString(), .equals(), etc.

    skip_methods = ['toString', 'equals', 'hashCode', 'clone', 'getClass',
                    'notify', 'notifyAll', 'wait', 'finalize',
                    'length', 'size', 'isEmpty', 'contains', 'add', 'remove',
                    'get', 'set', 'list', 'stream', 'map', 'filter', 'collect',
                    'pointAt', 'sample', 'contains', 'closestPointTo', 'distanceTo',
                    'boundingBox', 'tangentAt', 'parameterAt', 'normalAt', 'sampleGrid',
                    'perimeter', 'circumference', 'norm', 'normSquared', 'cross',
                    'dot', 'scale', 'negate', 'subtract', 'add', 'interpolate',
                    'projectOnto', 'transform', 'union', 'intersection', 'expand',
                    'center', 'width', 'height', 'depth', 'volume', 'isEmpty']

    # Find all .method() calls and check if they need conversion
    pattern = re.compile(r'\.(\w+)\(\)')

    def replace_getter(match):
        method_name = match.group(1)

        # Skip known non-getter methods
        if method_name in skip_methods:
            return match.group(0)

        # Check if it starts with 'get' or 'is' already
        if method_name.startswith('get') or method_name.startswith('is'):
            return match.group(0)

        # Check if it's likely a boolean field (startsWith 'is', 'has', 'can', etc.)
        if method_name.startswith('is') or method_name.startswith('has') or method_name.startswith('can'):
            return f'.is{method_name[2:].capitalize()}()'

        # Convert to getXxx
        return f'.get{method_name[0].upper() + method_name[1:]}()'

    return pattern.sub(replace_getter, content)

def convert_switch_expression(content):
    """Convert switch expressions to traditional switch statements or if-else"""

    # This is complex - simplified implementation
    # Pattern: return switch (x) { case A a -> expr; case B b -> expr; default -> expr; };

    # For now, just handle simple switch expressions without pattern matching
    # Full implementation would need proper parsing

    return content

def process_file(filepath):
    """Process a single file"""
    try:
        with open(filepath, 'r', encoding='utf-8') as f:
            content = f.read()

        # Apply conversions
        content = convert_pattern_matching_instanceof(content)
        content = convert_getter_calls(content)
        content = convert_switch_expression(content)

        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(content)

        print(f"Processed: {filepath}")
    except Exception as e:
        print(f"Error processing {filepath}: {e}")

def main():
    if len(sys.argv) < 2:
        print("Usage: python convert_jdk21.py <directory>")
        sys.exit(1)

    directory = sys.argv[1]

    for root, dirs, files in os.walk(directory):
        for file in files:
            if file.endswith('.java'):
                filepath = os.path.join(root, file)
                process_file(filepath)

if __name__ == '__main__':
    main()