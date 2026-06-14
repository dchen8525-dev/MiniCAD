#!/usr/bin/env python3
"""Convert records with compact constructors to JDK 11 classes"""

import re

def convert_record_to_class(record_code):
    """Convert a single record definition to a class"""

    # Extract record name and parameters
    m = re.search(r'record\s+(\w+)\s*\(([^)]+)\)', record_code)
    if not m:
        return record_code

    record_name = m.group(1)
    params_str = m.group(2)

    # Parse parameters
    params = []
    for param in params_str.split(','):
        param = param.strip()
        # Pattern: Type name or Type[] name
        m = re.search(r'(\w+(?:\[\])?)\s+(\w+)', param)
        if m:
            type = m.group(1)
            name = m.group(2)
            params.append({'type': type, 'name': name})

    # Extract compact constructor if exists
    compact_constructor = None
    m = re.search(rf'{record_name}\s*\{{([^}}]+)\}}', record_code)
    if m:
        compact_constructor = m.group(1)

    # Build class
    lines = []
    lines.append(f'public final class {record_name} {{')

    # Fields
    for param in params:
        lines.append(f'    private final {param["type"]} {param["name"]};')

    lines.append('')

    # Constructor
    constructor_params = ', '.join([f'{p["type"]} {p["name"]}' for p in params])
    lines.append(f'    public {record_name}({constructor_params}) {{')

    if compact_constructor:
        # Insert compact constructor logic before field assignments
        # Parse compact constructor assignments: name = expression;
        for assignment in re.findall(r'(\w+)\s+=\s+([^;]+);', compact_constructor):
            var_name = assignment[0]
            expr = assignment[1].strip()
            lines.append(f'        this.{var_name} = {expr};')
    else:
        # Simple constructor - just assign fields
        for param in params:
            lines.append(f'        this.{param["name"]} = {param["name"]};')

    lines.append('    }')
    lines.append('')

    # Getters (JavaBean style)
    for param in params:
        getter_name = f'get{param["name"][0].upper()}{param["name"][1:]}'
        lines.append(f'    public {param["type"]} {getter_name}() {{')
        lines.append(f'        return {param["name"]};')
        lines.append('    }')
    lines.append('')

    # equals()
    lines.append('    @Override')
    lines.append('    public boolean equals(Object o) {')
    lines.append('        if (this == o) return true;')
    lines.append('        if (o == null || getClass() != o.getClass()) return false;')
    lines.append(f'        {record_name} that = ({record_name}) o;')

    # Build equality checks
    checks = []
    for param in params:
        if param['type'] == 'int':
            checks.append(f'{param["name"]} == that.{param["name"]}')
        elif param['type'] == 'double':
            checks.append(f'Double.compare(that.{param["name"]}, {param["name"]}) == 0')
        elif param['type'] == 'boolean':
            checks.append(f'{param["name"]} == that.{param["name"]}')
        elif param['type'].endswith('[]'):
            checks.append(f'java.util.Arrays.equals({param["name"]}, that.{param["name"]})')
        else:
            checks.append(f'java.util.Objects.equals({param["name"]}, that.{param["name"]})')

    if checks:
        lines.append('        return ' + ' && '.join(checks) + ';')
    else:
        lines.append('        return true;')

    lines.append('    }')
    lines.append('')

    # hashCode()
    lines.append('    @Override')
    lines.append('    public int hashCode() {')

    hash_fields = []
    for param in params:
        if param['type'] == 'int':
            hash_fields.append(param['name'])
        elif param['type'] == 'double':
            hash_fields.append(f'Double.hashCode({param["name"]})')
        elif param['type'] == 'boolean':
            hash_fields.append(f'Boolean.hashCode({param["name"]})')
        elif param['type'].endswith('[]'):
            hash_fields.append(f'java.util.Arrays.hashCode({param["name"]})')
        else:
            hash_fields.append(param['name'])

    if hash_fields:
        lines.append(f'        return java.util.Objects.hash({", ".join(hash_fields)});')
    else:
        lines.append('        return 0;')

    lines.append('    }')
    lines.append('')

    # toString()
    lines.append('    @Override')
    lines.append('    public String toString() {')

    to_str_fields = []
    for param in params:
        if param['type'].endswith('[]'):
            to_str_fields.append(f'{param["name"]}=' + f'java.util.Arrays.toString({param["name"]})')
        else:
            to_str_fields.append(f'{param["name"]}=' + param['name'])

    lines.append(f'        return "{record_name}{{" + "{", ".join(to_str_fields)} + "}}";')
    lines.append('    }')

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

        # Detect record start
        if line.strip().startswith('record '):
            # Collect the full record definition
            record_lines = [line]
            brace_count = line.count('{') - line.count('}')

            while brace_count > 0 and i + 1 < len(lines):
                i += 1
                record_lines.append(lines[i])
                brace_count += lines[i].count('{') - lines[i].count('}')

            record_code = '\n'.join(record_lines)

            # Convert record to class
            try:
                converted = convert_record_to_class(record_code)
                output.append(converted)
            except Exception as e:
                # If conversion fails, keep original and add TODO
                output.append(record_code)
                output.append(f'// TODO: JDK11 - Manual conversion needed: {str(e)}')

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

print("Use: python convert_records.py <file.java>")
print("Converts records with compact constructors to JDK 11 compatible classes")