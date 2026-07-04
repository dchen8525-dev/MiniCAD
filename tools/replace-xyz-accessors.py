#!/usr/bin/env python3
"""
Replace all .x(), .y(), .z() calls with .getX(), .getY(), .getZ()
except in method definitions.
"""
import re
import sys
from pathlib import Path

def replace_accessor(content, old_method, new_method):
    """Replace .old() with .new() but not in method definitions."""
    # Match .method() but not "public TYPE method() {"
    # Use negative lookbehind to skip definitions
    
    # Pattern: .method() where the dot is NOT part of a method signature
    # Method signatures look like: "public double x() {" or "private int x() {"
    # Calls look like: "obj.x()" or "obj.x().y()"
    
    lines = content.split('\n')
    result_lines = []
    
    for line in lines:
        # Skip lines that are method definitions
        if re.match(r'\s*(public|private|protected)\s+\w+\s+' + re.escape(old_method) + r'\s*\(', line):
            result_lines.append(line)
        else:
            # Replace .method() with .new()
            new_line = re.sub(r'\.' + re.escape(old_method) + r'\(\)', '.' + new_method + '()', line)
            result_lines.append(new_line)
    
    return '\n'.join(result_lines)

def process_file(filepath):
    """Process a single Java file."""
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
    
    original = content
    
    # Replace x, y, z accessors
    content = replace_accessor(content, 'x', 'getX')
    content = replace_accessor(content, 'y', 'getY')
    content = replace_accessor(content, 'z', 'getZ')
    
    if content != original:
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(content)
        return True
    return False

def main():
    """Process all Java files."""
    src_dir = Path('src/main/java')
    modified = 0
    
    for java_file in src_dir.rglob('*.java'):
        if process_file(java_file):
            modified += 1
            print(f'Modified: {java_file}')
    
    print(f'\nTotal files modified: {modified}')

if __name__ == '__main__':
    main()
