#!/usr/bin/env python3
"""Improved record converter with proper file handling"""

import re

def convert_record_block(record_text):
    """Convert a single record block to class"""

    # Match record declaration
    m = re.match(r'record\s+(\w+)\s*\(([^)]+)\)\s*\{', record_text)
    if not m:
        return record_text

    class_name = m.group(1)
    params_str = m.group(2).strip()

    # Parse parameters: Type name, Type[] name, List<Type> name, etc.
    params = []
    # Split by comma, but be careful with generic types like List<Type>
    parts = re.split(r',\s*', params_str)
    for part in parts:
        part = part.strip()
        if not part:
            continue
        # Match: Type name or Type[] name or Generic<Type> name
        m = re.match(r'([\w.<>\[\]]+)\s+(\w+)', part)
        if m:
            params.append({'type': m.group(1), 'name': m.group(2)})

    # Find compact constructor body
    compact_body = None
    pattern = class_name + r'\s+\{([^{}]*+(?:\{[^{}]*\}[^{}]*)*)\}'
    m = re.search(pattern, record_text)
    if m:
        compact_body = m.group(1).strip()

    # Build class
    lines = []
    lines.append(f'public final class {class_name} {{')

    # Fields
    for p in params:
        lines.append(f'    private final {p["type"]} {p["name"]};')
    lines.append('')

    # Constructor
    param_list = ', '.join([f'{p["type"]} {p["name"]}' for p in params])
    lines.append(f'    public {class_name}({param_list}) {{')

    if compact_body:
        # Parse compact constructor assignments
        for assign_m in re.finditer(r'(\w+)\s*=\s*([^;]+);', compact_body):
            var = assign_m.group(1)
            expr = assign_m.group(2).strip()
            lines.append(f'        this.{var} = {expr};')
    else:
        # Simple assignment
        for p in params:
            lines.append(f'        this.{p["name"]} = {p["name"]};')

    lines.append('    }')
    lines.append('')

    # Getters - use getXXX style for JavaBean convention
    for p in params:
        name = p['name']
        getter = f'get{name[0].upper()}{name[1:]}' if name else name
        lines.append(f'    public {p["type"]} {getter}() {{')
        lines.append(f'        return {name};')
        lines.append('    }')

    # equals, hashCode, toString - simplified
    lines.append('')
    lines.append('    @Override public boolean equals(Object o) {')
    lines.append('        if (this == o) return true;')
    lines.append(f'        if (!(o instanceof {class_name})) return false;')
    lines.append(f'        {class_name} that = ({class_name}) o;')
    if params:
        checks = []
        for p in params:
            n = p['name']
            t = p['type']
            if t in ['int', 'long', 'short', 'byte', 'char', 'boolean']:
                checks.append(f'{n} == that.{n}')
            elif t == 'double' or t == 'float':
                checks.append(f'Double.compare(that.{n}, {n}) == 0')
            elif t.endswith('[]'):
                checks.append(f'java.util.Arrays.equals({n}, that.{n})')
            else:
                checks.append(f'java.util.Objects.equals({n}, that.{n})')
        lines.append('        return ' + ' && '.join(checks) + ';')
    else:
        lines.append('        return true;')
    lines.append('    }')

    lines.append('')
    lines.append('    @Override public int hashCode() {')
    if params:
        hash_args = []
        for p in params:
            n = p['name']
            t = p['type']
            if t in ['int', 'long', 'short', 'byte', 'char', 'boolean']:
                hash_args.append(n)
            elif t == 'double' or t == 'float':
                hash_args.append(f'Double.hashCode({n})')
            elif t.endswith('[]'):
                hash_args.append(f'java.util.Arrays.hashCode({n})')
            else:
                hash_args.append(n)
        lines.append(f'        return java.util.Objects.hash({", ".join(hash_args)});')
    else:
        lines.append('        return 0;')
    lines.append('    }')

    lines.append('')
    lines.append('    @Override public String toString() {')
    if params:
        fields_str = ', '.join([f'{p["name"]}=' + (f'java.util.Arrays.toString({p["name"]})' if p['type'].endswith('[]') else p['name']) for p in params])
        lines.append(f'        return "{class_name}{{" + "{fields_str}" + "}}";')
    else:
        lines.append(f'        return "{class_name}{{}}";')
    lines.append('    }')

    lines.append('}')

    return '\n'.join(lines)

def process_file(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        original = f.read()

    # Find all record blocks
    # Pattern: record Name(...) { ... }
    # Use regex to find and replace each record

    result = original

    # Find record declarations
    pattern = r'record\s+\w+\s*\([^)]*\)\s*\{(?:[^{}]|{[^{}]*})+}'

    for m in re.finditer(pattern, original):
        record_block = m.group(0)
        converted = convert_record_block(record_block)
        result = result.replace(record_block, converted)

    if result != original:
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(result)
        return True
    return False

if __name__ == '__main__':
    import sys
    if len(sys.argv) > 1:
        for f in sys.argv[1:]:
            if process_file(f):
                print(f'Fixed: {f}')
    else:
        print('Usage: python convert_records_improved.py <file.java>')