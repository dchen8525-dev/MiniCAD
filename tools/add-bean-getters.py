#!/usr/bin/env python3
"""
Add Java Bean getters to all classes with record-style accessors.
This script analyzes compilation errors and adds missing getter methods.
"""

import re
import subprocess
from pathlib import Path
from typing import List, Dict, Set

def get_compilation_errors() -> List[str]:
    """Run Maven compile and extract error messages."""
    result = subprocess.run(
        ['mvn', 'clean', 'compile'],
        capture_output=True,
        text=True,
        cwd='/d/work/MiniCAD',
        env={
            'JAVA_HOME': '/c/Users/admin/.jdks/ms-11.0.31',
            'PATH': '/c/Users/admin/.jdks/ms-11.0.31/bin:/usr/bin:/bin'
        }
    )
    return result.stdout + result.stderr

def parse_errors(error_output: str) -> Dict[str, Set[str]]:
    """Parse compilation errors to find missing methods per class."""
    errors = {}
    
    # Pattern to match error lines
    pattern = r'符号:\s+方法\s+(\w+)\(\)\s+位置:\s+(?:类型为|类)\s+([^\n]+)'
    
    for match in re.finditer(pattern, error_output):
        method = match.group(1)
        class_info = match.group(2).strip()
        
        # Extract class name
        class_match = re.search(r'([\w.]+)(?:的变量|\s+)(\w+)?', class_info)
        if class_match:
            full_class = class_match.group(1)
            var_name = class_match.group(2)
            
            # Simplify class name
            class_name = full_class.split('.')[-1]
            
            if class_name not in errors:
                errors[class_name] = set()
            errors[class_name].add(method)
    
    return errors

def find_java_file(class_name: str) -> Path:
    """Find Java file for a given class name."""
    src_dir = Path('/d/work/MiniCAD/src/main/java')
    
    # Search in all packages
    for java_file in src_dir.rglob(f'{class_name}.java'):
        return java_file
    
    return None

def extract_record_accessors(java_file: Path) -> List[Dict]:
    """Extract record-style accessors from a Java file."""
    content = java_file.read_text(encoding='utf-8')
    
    # Pattern to match record-style accessors
    # public TYPE name() { return name; }
    pattern = r'public\s+(\w+(?:<[^>]+>)?(?:\[\])?)\s+(\w+)\(\)\s*\{\s*return\s+(\w+);\s*\}'
    
    accessors = []
    for match in re.finditer(pattern, content):
        return_type = match.group(1)
        method_name = match.group(2)
        field_name = match.group(3)
        
        # Skip if already has getter
        getter_name = 'get' + method_name[0].upper() + method_name[1:]
        if return_type == 'boolean':
            getter_name = 'is' + method_name[0].upper() + method_name[1:]
        
        accessors.append({
            'return_type': return_type,
            'method_name': method_name,
            'field_name': field_name,
            'getter_name': getter_name
        })
    
    return accessors

def add_getter_to_file(java_file: Path, accessor: Dict) -> bool:
    """Add a getter method to a Java file."""
    content = java_file.read_text(encoding='utf-8')
    
    # Check if getter already exists
    if accessor['getter_name'] + '()' in content:
        return False
    
    # Find the record-style accessor
    pattern = rf'public\s+{re.escape(accessor["return_type"])}\s+{re.escape(accessor["method_name"])}\(\)\s*\{{\s*return\s+{re.escape(accessor["field_name"])};\s*\}}'
    
    if not re.search(pattern, content):
        return False
    
    # Add getter after the record-style accessor
    getter_method = f'\n    public {accessor["return_type"]} {accessor["getter_name"]}() {{ return {accessor["field_name"]}; }}'
    
    new_content = re.sub(
        pattern,
        lambda m: m.group(0) + getter_method,
        content
    )
    
    if new_content != content:
        java_file.write_text(new_content, encoding='utf-8')
        return True
    
    return False

def main():
    print("Analyzing compilation errors...")
    error_output = get_compilation_errors()
    
    print("Parsing errors...")
    errors = parse_errors(error_output)
    
    print(f"Found {len(errors)} classes with missing methods:")
    for class_name, methods in sorted(errors.items()):
        print(f"  {class_name}: {', '.join(sorted(methods))}")
    
    print("\nAdding Java Bean getters...")
    total_added = 0
    
    for class_name in errors:
        java_file = find_java_file(class_name)
        if not java_file:
            print(f"  WARNING: Could not find file for {class_name}")
            continue
        
        print(f"\n  Processing {class_name}...")
        accessors = extract_record_accessors(java_file)
        
        for accessor in accessors:
            if add_getter_to_file(java_file, accessor):
                print(f"    Added {accessor['getter_name']}()")
                total_added += 1
    
    print(f"\n\nTotal getters added: {total_added}")
    print("\nRecompiling to verify...")
    
    # Recompile
    result = subprocess.run(
        ['mvn', 'clean', 'compile'],
        capture_output=True,
        text=True,
        cwd='/d/work/MiniCAD',
        env={
            'JAVA_HOME': '/c/Users/admin/.jdks/ms-11.0.31',
            'PATH': '/c/Users/admin/.jdks/ms-11.0.31/bin:/usr/bin:/bin'
        }
    )
    
    if result.returncode == 0:
        print("✓ Compilation successful!")
    else:
        print("✗ Compilation failed. Remaining errors:")
        print(result.stdout)
        print(result.stderr)

if __name__ == '__main__':
    main()
