#!/usr/bin/env python3
"""Targeted fixes for StepDumpApp.java."""
import re
from pathlib import Path

p = Path('D:/work/MiniCAD/src/main/java/com/minicad/app/StepDumpApp.java')
lines = p.read_text(encoding='utf-8').split('\n')
out = []
i = 0
while i < len(lines):
    line = lines[i]
    s = line.strip()

    # Fix switch arrow cases with escape sequences
    m = re.match(r"^(\s*)case '(\\\\|\\[bfnrt])' -> (.+);$", line)
    if m:
        indent = m.group(1)
        esc = m.group(2)
        body = m.group(3)
        out.append(f"{indent}case '{esc}': {body}; break;")
        i += 1
        continue

    # Fix compound instanceof: if (expr instanceof Type var && cond) {
    m = re.match(r'^(\s*)if \((.+?) instanceof ([\w.]+) (\w+) && (.+?)\) \{$', line)
    if m:
        indent = m.group(1)
        expr = m.group(2).strip()
        tname = m.group(3)
        vname = m.group(4)
        cond = m.group(5)
        out.append(f'{indent}if ({expr} instanceof {tname}) {{')
        out.append(f'{indent}    {tname} {vname} = ({tname}) {expr};')
        out.append(f'{indent}    if ({cond}) {{')
        # Find the matching closing brace of this if block
        j = i + 1
        bd = 1
        while j < len(lines) and bd > 0:
            for c in lines[j]:
                if c == '{': bd += 1
                elif c == '}': bd -= 1
            j += 1
        # Emit body lines (between opening { and closing })
        for k in range(i + 1, j - 1):
            out.append(lines[k])
        # Close inner if and outer if
        out.append(f'{indent}    }}')
        out.append(f'{indent}}}')
        i = j
        continue

    # Fix default arrow block closing brace
    # Pattern: a lone '}' that closes a 'default -> {' block
    # Look for 'default:' followed later by a stray '}'
    # Actually, just fix the specific pattern in appendJsonString
    if s == '}' and i > 0:
        # Check if previous non-empty line ends with a statement (not {)
        prev = out[-1].strip() if out else ''
        # If this } follows a default: block body, replace with break;
        # Heuristic: if prev is '}' and before that is 'append(c);' etc.
        # Actually let me just check context
        pass

    # Fix generic type in record accessor: 'public StepEntity> resolved()' -> 'public Map<Integer, StepEntity> resolved()'
    if 'public StepEntity> resolved()' in line:
        line = line.replace('public StepEntity> resolved()', 'public Map<Integer, StepEntity> resolved()')

    out.append(line)
    i += 1

# Now do a second pass: fix the default case closing brace
# Find 'default:' in a switch and add 'break;' before the closing }
content = '\n'.join(out)

# Fix: in appendJsonString switch, the default case needs break; before closing }
# Pattern: the body of default case ends, then there's a } that should be 'break; }'
# The switch structure is:
#   default:
#       if (...) { ... } else { ... }
#   }  <- this should be 'break; }'
#   }  <- this closes the switch
#   }  <- this closes the for loop

# Let me find it by looking for 'default:' in the switch context
# Actually, let me just add 'break;' before the closing } of the switch default
# The pattern is: after 'default:' and its body, there's a '}' at the switch level

# Simpler approach: replace the specific pattern
# After the default case body (if-else), the closing } should become 'break; }'
content = re.sub(
    r'(default:\s*\n\s*if \(c < 0x20\) \{[^}]+\} else \{[^}]+\}\s*\n\s*)\}(\s*\n\s*\})',
    r'\1    break;\n            }\2',
    content
)

p.write_text(content, encoding='utf-8')
print('Fixes applied')
