#!/usr/bin/env python3
"""
Phase 3: Replace all record-style method calls with Java Bean getters.
More comprehensive than Phase 1 - handles chained calls, array access, etc.
"""
import re
import os
from pathlib import Path

def replace_method_calls(content, method_name, getter_name):
    """Replace all .method() calls with .getter() except in method definitions."""
    # Match .method() but NOT in a definition context (public/private/protected/return type)
    # Negative lookbehind for method definition patterns
    pattern = r'(?<!public\s)(?<!private\s)(?<!protected\s)(?<!\w)\.' + re.escape(method_name) + r'\(\)'
    
    # But we need to avoid replacing in method definitions like "public double x() {"
    # So we'll use a more targeted approach: replace .x() with .getX() but not if preceded by type
    
    # Simpler: just replace all .method() with .getter() and then fix definitions
    return re.sub(r'\.' + re.escape(method_name) + r'\(\)', '.' + getter_name + '()', content)

def restore_method_definitions(content, method_name):
    """Restore method definitions that were incorrectly replaced."""
    # Pattern: "public TYPE methodName() { return ..."
    # We need to restore these back to methodName() instead of getMethodName()
    # Pattern examples:
    #   public double x() { return x; }
    #   public double getX() { return getX(); }  <- this should be x()
    
    # Fix: if we see "public TYPE getMethodName() { return getMethodName(); }"
    # restore it to "public TYPE methodName() { return getMethodName(); }"
    pattern = r'(public\s+\w+(?:<[^>]+>)?(?:\[\])?)\s+get' + re.escape(method_name) + r'\(\)\s*\{\s*return\s+get' + re.escape(method_name) + r'\(\);\s*\}'
    
    def restore(m):
        return_type = m.group(1)
        return f'{return_type} {method_name}() {{ return get{method_name.capitalize()}(); }}'
    
    return re.sub(pattern, restore, content)

def process_file(filepath):
    """Process a single Java file."""
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
    
    original = content
    
    # List of method -> getter mappings
    mappings = [
        ('x', 'X'), ('y', 'Y'), ('z', 'Z'),
        ('position', 'Position'), ('radius', 'Radius'),
        ('origin', 'Origin'), ('direction', 'Direction'),
        ('normal', 'Normal'), ('axis', 'Axis'),
        ('location', 'Location'), ('segments', 'Segments'),
        ('points', 'Points'), ('distance', 'Distance'),
        ('profile', 'Profile'), ('uDegree', 'UDegree'),
        ('vDegree', 'VDegree'), ('controlPoints', 'ControlPoints'),
        ('uMultiplicities', 'UMultiplicities'), ('vMultiplicities', 'VMultiplicities'),
        ('uKnots', 'UKnots'), ('vKnots', 'VKnots'),
    ]
    
    for method, getter in mappings:
        content = replace_method_calls(content, method, 'get' + getter)
        content = restore_method_definitions(content, method)
    
    if content != original:
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(content)
        return True
    return False

def main():
    """Process all Java files in src/main/java."""
    src_dir = Path('src/main/java')
    modified = 0
    
    for java_file in src_dir.rglob('*.java'):
        if process_file(java_file):
            modified += 1
            print(f'Modified: {java_file}')
    
    print(f'\nTotal files modified: {modified}')

if __name__ == '__main__':
    main()
