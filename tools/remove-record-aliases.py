#!/usr/bin/env python3
"""
Phase 6: Remove record-style alias methods that delegate to Java Bean getters.
Pattern: public TYPE methodName() { return getMethodName(); }
"""
import re
from pathlib import Path
import sys

def remove_alias_methods(content):
    """Remove all single-line alias methods that just call getters."""
    # Pattern: public TYPE methodName() { return getMethodName(); }
    # Also handle: public TYPE methodName() { return getField; }
    pattern = r'    public [a-zA-Z0-9_<>\[\]]+ ([a-z][a-zA-Z0-9_]*)\(\) \{ return get[A-Z][a-zA-Z0-9_]*\(\); \}\n'
    
    # Remove these lines
    result = re.sub(pattern, '', content)
    return result

def process_file(filepath):
    """Process a single Java file."""
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
    
    new_content = remove_alias_methods(content)
    
    if new_content != content:
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(new_content)
        return True
    return False

def main():
    """Process all Java files in geometry and geometry2d packages."""
    src_dir = Path('src/main/java/com/minicad')
    modified = 0
    
    # Process geometry and geometry2d packages
    for package in ['geometry', 'geometry2d']:
        package_dir = src_dir / package
        if package_dir.exists():
            for java_file in package_dir.rglob('*.java'):
                if process_file(java_file):
                    modified += 1
                    print(f'Removed aliases from: {java_file.name}')
    
    print(f'\nTotal files modified: {modified}')

if __name__ == '__main__':
    main()
