#!/usr/bin/env python3
from pathlib import Path

p = Path('D:/work/MiniCAD/src/main/java/com/minicad/app/StepDumpApp.java')
content = p.read_text(encoding='utf-8')

# Fix 1: Double Map<Integer, - replace with correct type
content = content.replace('Map<Integer, Map<Integer, StepEntity>', 'Map<Integer, StepEntity>')

# Fix 2: Convert switch expression to if-else
old = (
    '        String code = switch (exception) {\n'
    '            case StepParseException ignored -> "step.parse";\n'
    '            case StepResolutionException ignored -> "step.resolve";\n'
    '            case UnsupportedGeometryException ignored -> "step.unsupported";\n'
    '            case TopologyException ignored -> "topology.invalid";\n'
    '            case GeometryException ignored -> "geometry.invalid";\n'
    '            case IOException ignored -> "io.read";\n'
    '            default -> "step.failed";\n'
    '        };'
)
new = (
    '        String code;\n'
    '        if (exception instanceof StepParseException) {\n'
    '            code = "step.parse";\n'
    '        } else if (exception instanceof StepResolutionException) {\n'
    '            code = "step.resolve";\n'
    '        } else if (exception instanceof UnsupportedGeometryException) {\n'
    '            code = "step.unsupported";\n'
    '        } else if (exception instanceof TopologyException) {\n'
    '            code = "topology.invalid";\n'
    '        } else if (exception instanceof GeometryException) {\n'
    '            code = "geometry.invalid";\n'
    '        } else if (exception instanceof IOException) {\n'
    '            code = "io.read";\n'
    '        } else {\n'
    '            code = "step.failed";\n'
    '        }'
)
content = content.replace(old, new)

p.write_text(content, encoding='utf-8')
print('Fixed')
