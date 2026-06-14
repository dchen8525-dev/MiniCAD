#!/usr/bin/env python3
"""Fix remaining negated compound instanceof patterns."""
from pathlib import Path

p = Path('D:/work/MiniCAD/src/main/java/com/minicad/app/StepPreviewJsonExporter.java')
content = p.read_text(encoding='utf-8')

old = 'if (!(bounds.getFirst().loop() instanceof EdgeLoop outerLoop) || outerLoop.edges().size() != 4) {\n            return null;\n        }'
new = 'if (!(bounds.getFirst().loop() instanceof EdgeLoop)) {\n            return null;\n        }\n        EdgeLoop outerLoop = (EdgeLoop) bounds.getFirst().loop();\n        if (outerLoop.edges().size() != 4) {\n            return null;\n        }'

count = content.count(old)
print(f'Found {count} occurrences')
content = content.replace(old, new)
p.write_text(content, encoding='utf-8')
print('Done')
