#!/usr/bin/env python3
"""
Batch convert Java 21 records to JDK 11 compatible classes.
"""

import re
from pathlib import Path
from typing import List, Tuple, Optional


def convert_file(file_path: Path) -> bool:
    """Convert a single Java record file to a regular class."""
    try:
        content = file_path.read_text(encoding='utf-8')

        # Check if it's a record
        if 'public record ' not in content:
            return False

        new_content = convert_record_to_class(content)
        if new_content:
            file_path.write_text(new_content, encoding='utf-8')
            return True
        return False

    except Exception as e:
        print(f"ERROR processing {file_path}: {e}")
        import traceback
        traceback.print_exc()
        return False


def convert_record_to_class(content: str) -> Optional[str]:
    """Convert record content to class content."""

    # Extract package
    package_match = re.search(r'package\s+([\w.]+);', content)
    package = package_match.group(1) if package_match else ''

    # Extract imports (everything between package and record)
    imports_match = re.search(r'package\s+[\w.]+;(.*?)(?=/\*\*|public record)', content, re.DOTALL)
    imports = imports_match.group(1).strip() if imports_match else ''

    # Extract class-level Javadoc
    javadoc_match = re.search(r'(/\*\*[\s\S]*?\*/)\s*public record', content)
    javadoc = javadoc_match.group(1) if javadoc_match else ''

    # Parse record signature: public record Name(...) implements Interface {
    record_sig = re.search(
        r'public record\s+(\w+)\s*\(([\s\S]*?)\)(?:\s+implements\s+([\w\s,.<>?]+))?\s*\{',
        content
    )

    if not record_sig:
        print(f"Could not parse record signature")
        return None

    class_name = record_sig.group(1)
    params_str = record_sig.group(2)
    implements = record_sig.group(3).strip() if record_sig.group(3) else None

    # Parse fields
    fields = parse_fields(params_str)

    # Get body content (after the opening brace of the record)
    body_start = record_sig.end() - 1
    body = extract_brace_content(content[body_start:])

    # Parse compact constructor and methods
    compact_ctor, methods = parse_body(body, class_name)

    # Generate class
    return generate_class(package, imports, javadoc, class_name, implements, fields, compact_ctor, methods)


def parse_fields(params_str: str) -> List[Tuple[str, str]]:
    """Parse fields from record parameters. Returns list of (type, name) tuples."""
    fields = []
    # Split by comma, handling nested generics
    params = split_by_comma(params_str)

    for param in params:
        param = param.strip()
        if not param:
            continue

        # Last word is the field name
        parts = param.rsplit(None, 1)
        if len(parts) == 2:
            fields.append((parts[0], parts[1]))

    return fields


def split_by_comma(s: str) -> List[str]:
    """Split string by comma, respecting nested generics."""
    result = []
    current = []
    depth = 0

    for char in s:
        if char == '<':
            depth += 1
            current.append(char)
        elif char == '>':
            depth -= 1
            current.append(char)
        elif char == ',' and depth == 0:
            result.append(''.join(current))
            current = []
        else:
            current.append(char)

    if current:
        result.append(''.join(current))

    return result


def extract_brace_content(s: str) -> str:
    """Extract content between outermost braces."""
    depth = 0
    start = -1

    for i, char in enumerate(s):
        if char == '{':
            if depth == 0:
                start = i + 1
            depth += 1
        elif char == '}':
            depth -= 1
            if depth == 0:
                return s[start:i]

    return ''


def parse_body(body: str, class_name: str) -> Tuple[Optional[str], List[str]]:
    """Parse body for compact constructor and custom methods."""
    compact_ctor = None
    methods = []

    body = body.strip()

    # Look for compact constructor: public ClassName { ... }
    # Note: compact constructor has no parameters
    ctor_pattern = rf'public\s+{re.escape(class_name)}\s*\{{'
    ctor_match = re.search(ctor_pattern, body)

    if ctor_match:
        # Find the matching closing brace
        ctor_body = extract_brace_content(body[ctor_match.start():])
        compact_ctor = ctor_body.strip()

        # Remove compact constructor from body
        body = body[:ctor_match.start()] + body[ctor_match.start() + len(ctor_body) + 2:]  # +2 for braces
        body = body.strip()

    # Extract remaining methods
    # Match methods like: @Override public ... name(...) { ... }
    # or: public static ... name(...) { ... }
    pos = 0
    while pos < len(body):
        # Skip whitespace
        while pos < len(body) and body[pos] in ' \t\n\r':
            pos += 1

        if pos >= len(body):
            break

        # Look for method start
        rest = body[pos:]

        # Match annotations, modifiers, return type, method name, params
        method_match = re.match(
            r'((?:@\w+(?:\([\s\S]*?\))?\s*)*)'  # annotations
            r'(public|private|protected)?\s*'     # visibility
            r'(static\s+)?'                       # static
            r'(\w+(?:<[\w\s,?<>]+>)?)\s+'         # return type
            r'(\w+)\s*'                           # method name
            r'\(([\s\S]*?)\)\s*'                  # params
            r'(?:throws\s+[\w\s,]+)?\s*'           # throws clause
            r'\{',                                # opening brace
            rest
        )

        if method_match:
            # Found method start, now find its end
            brace_start = pos + method_match.end() - 1
            method_body = extract_brace_content(body[brace_start:])
            full_method = rest[:method_match.end() + len(method_body) + 1]

            # Don't include the compact constructor as a method
            if not re.match(rf'public\s+{re.escape(class_name)}\s*\(', full_method):
                methods.append(full_method.strip())

            pos = brace_start + len(method_body) + 1
        else:
            # No more methods found, move forward
            pos += 1

    return compact_ctor, methods


def generate_class(package: str, imports: str, javadoc: str, class_name: str,
                   implements: Optional[str], fields: List[Tuple[str, str]],
                   compact_ctor: Optional[str], methods: List[str]) -> str:
    """Generate the class source code."""

    lines = []

    # Package
    lines.append(f'package {package};')
    lines.append('')

    # Imports
    if imports:
        # Add Objects import if needed
        if 'import java.util.Objects;' not in imports:
            imports = 'import java.util.Objects;\n' + imports
        lines.append(imports)
        lines.append('')

    # Javadoc
    if javadoc:
        lines.append(javadoc)

    # Class declaration
    impl_clause = f' implements {implements}' if implements else ''
    lines.append(f'public final class {class_name}{impl_clause} {{')
    lines.append('')

    # Fields
    for field_type, field_name in fields:
        lines.append(f'    private final {field_type} {field_name};')
    if fields:
        lines.append('')

    # Constructor
    params = ', '.join(f'{t} {n}' for t, n in fields)
    lines.append(f'    public {class_name}({params}) {{')

    # Compact constructor body
    if compact_ctor:
        # Indent each line
        for line in compact_ctor.split('\n'):
            stripped = line.strip()
            if stripped:
                lines.append('        ' + stripped)
        lines.append('')

    # Field assignments
    for _, field_name in fields:
        lines.append(f'        this.{field_name} = {field_name};')
    lines.append('    }')
    lines.append('')

    # Getters
    for field_type, field_name in fields:
        getter = to_getter_name(field_name, field_type)
        lines.append(f'    public {field_type} {getter}() {{')
        lines.append(f'        return {field_name};')
        lines.append('    }')
        lines.append('')

    # equals
    lines.append('    @Override')
    lines.append('    public boolean equals(Object o) {')
    lines.append('        if (this == o) return true;')
    lines.append('        if (o == null || getClass() != o.getClass()) return false;')
    lines.append(f'        {class_name} that = ({class_name}) o;')

    comparisons = []
    for field_type, field_name in fields:
        if field_type in ('int', 'long', 'double', 'float', 'boolean', 'char', 'byte', 'short'):
            comparisons.append(f'{field_name} == that.{field_name}')
        else:
            comparisons.append(f'Objects.equals({field_name}, that.{field_name})')

    if comparisons:
        lines.append('        return ' + '\n            && '.join(comparisons) + ';')
    else:
        lines.append('        return true;')

    lines.append('    }')
    lines.append('')

    # hashCode
    lines.append('    @Override')
    lines.append('    public int hashCode() {')
    if fields:
        field_names = [n for _, n in fields]
        lines.append(f'        return Objects.hash({", ".join(field_names)});')
    else:
        lines.append('        return 0;')
    lines.append('    }')
    lines.append('')

    # toString
    lines.append('    @Override')
    lines.append('    public String toString() {')
    if fields:
        parts = []
        for i, (_, field_name) in enumerate(fields):
            if i == 0:
                parts.append(f'"{field_name}=" + {field_name}')
            else:
                parts.append(f' + ", {field_name}=" + {field_name}')
        lines.append(f'        return "{class_name}{{" + {"".join(parts)} + "}}";')
    else:
        lines.append(f'        return "{class_name}{{}}";')
    lines.append('    }')
    lines.append('')

    # Custom methods
    for method in methods:
        method_lines = method.split('\n')
        # Find the minimum indentation in the method (excluding empty lines)
        min_indent = 0
        for line in method_lines:
            if line.strip():
                indent = len(line) - len(line.lstrip())
                if min_indent == 0 or indent < min_indent:
                    min_indent = indent

        for line in method_lines:
            stripped = line.rstrip()
            if stripped:
                # Remove the base indentation and add class-level indentation (4 spaces)
                relative_indent = len(line) - len(line.lstrip()) - min_indent
                lines.append('    ' + ' ' * relative_indent + line.lstrip())
        lines.append('')

    # Close class
    lines.append('}')

    return '\n'.join(lines)


def to_getter_name(field_name: str, field_type: str) -> str:
    """Convert field name to getter method name."""
    # For boolean fields, use isXxx convention
    if field_type == 'boolean':
        # If already starts with 'is', keep as is
        if field_name.startswith('is') and len(field_name) > 2 and field_name[2].isupper():
            return field_name
        # Otherwise, use isXxx
        return 'is' + field_name[0].upper() + field_name[1:]

    # Standard getter: getXxx
    return 'get' + field_name[0].upper() + field_name[1:]


def main():
    """Main entry point."""
    base_path = Path('D:/work/MiniCAD/src/main/java/com/minicad/step/model')

    if not base_path.exists():
        print(f"Path does not exist: {base_path}")
        return

    # Find all Java files
    java_files = list(base_path.rglob('*.java'))
    print(f"Found {len(java_files)} Java files")

    converted = 0
    failed = 0

    for i, file_path in enumerate(java_files, 1):
        if i % 100 == 0:
            print(f"Processing {i}/{len(java_files)}... ({converted} converted so far)")

        try:
            if convert_file(file_path):
                converted += 1
        except Exception as e:
            failed += 1
            print(f"Failed: {file_path}: {e}")

    print(f"\nConverted {converted} records")
    print(f"Failed: {failed}")


if __name__ == '__main__':
    main()