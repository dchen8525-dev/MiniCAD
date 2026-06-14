#!/usr/bin/env python3
"""Convert text blocks to JDK 11 compatible string concatenation"""

import re

def convert_text_block(block_content):
    """Convert a text block to concatenated string"""

    lines = block_content.split('\n')

    # Remove common indentation (find minimum indentation)
    min_indent = min([len(line) - len(line.lstrip()) for line in lines if line.strip()])

    # Strip indentation from each line
    stripped_lines = [line[min_indent:] if len(line) > min_indent else '' for line in lines]

    # Remove trailing empty lines
    while stripped_lines and not stripped_lines[-1].strip():
        stripped_lines.pop()

    # Convert to concatenated string
    result_lines = []
    for i, line in enumerate(stripped_lines):
        if i < len(stripped_lines) - 1:
            # Add newline escape
            result_lines.append(f'"{line}\\n"')
        else:
            # Last line - no newline
            result_lines.append(f'"{line}"')

    # Join with +
    if len(result_lines) == 1:
        return result_lines[0]
    else:
        return '\n        ' + '\n        + '.join(result_lines)

def fix_file(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        original = f.read()

    content = original
    lines = content.split('\n')
    output = []

    i = 0
    while i < len(lines):
        line = lines[i]

        # Detect text block start
        if '"""' in line:
            # Find the start position
            start_idx = line.index('"""')

            # Extract leading part
            leading = line[:start_idx]

            # Collect text block content
            block_lines = []
            i += 1

            while i < len(lines) and '"""' not in lines[i]:
                block_lines.append(lines[i])
                i += 1

            # Found closing """
            if i < len(lines) and '"""' in lines[i]:
                block_content = '\n'.join(block_lines)
                converted = convert_text_block(block_content)

                # Add the converted string
                output.append(leading + converted)
                i += 1
                continue
            else:
                # No closing found - keep original
                output.append(line)
                continue

        output.append(line)
        i += 1

    new_content = '\n'.join(output)

    if new_content != original:
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(new_content)
        return True
    return False

def main():
    import glob

    files = glob.glob('src/test/java/com/minicad/**/*.java', recursive=True)

    fixed_count = 0
    for filepath in files:
        if fix_file(filepath):
            print(f"Fixed: {filepath}")
            fixed_count += 1

    print(f"\nTotal files fixed: {fixed_count}")

if __name__ == '__main__':
    main()