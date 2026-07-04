#!/usr/bin/env python3
"""
Migrate record-style accessors to Java Bean style.
Replaces .x(), .y(), .z() with .getX(), .getY(), .getZ() etc.
"""

import os
import re
from pathlib import Path

# Mapping of record-style to Java Bean style
ACCESSOR_MAP = {
    # 3D geometry
    r'\.x\(\)': '.getX()',
    r'\.y\(\)': '.getY()',
    r'\.z\(\)': '.getZ()',
    r'\.uDegree\(\)': '.getUDegree()',
    r'\.vDegree\(\)': '.getVDegree()',
    r'\.controlPoints\(\)': '.getControlPoints()',
    r'\.uMultiplicities\(\)': '.getUMultiplicities()',
    r'\.vMultiplicities\(\)': '.getVMultiplicities()',
    r'\.uKnots\(\)': '.getUKnots()',
    r'\.vKnots\(\)': '.getVKnots()',
    r'\.weightsData\(\)': '.getWeightsData()',
    r'\.weights\(\)': '.getWeights()',
    r'\.knots\(\)': '.getKnots()',
    r'\.knotMultiplicities\(\)': '.getKnotMultiplicities()',
    r'\.position\(\)': '.getPosition()',
    r'\.radius\(\)': '.getRadius()',
    r'\.origin\(\)': '.getOrigin()',
    r'\.direction\(\)': '.getDirection()',
    r'\.normal\(\)': '.getNormal()',
    r'\.axis\(\)': '.getAxis()',
    r'\.location\(\)': '.getLocation()',
    r'\.refDirection\(\)': '.getRefDirection()',
    r'\.segments\(\)': '.getSegments()',
    r'\.points\(\)': '.getPoints()',
    r'\.edges\(\)': '.getEdges()',
    r'\.faces\(\)': '.getFaces()',
    r'\.bounds\(\)': '.getBounds()',
    r'\.loop\(\)': '.getLoop()',
    r'\.surface\(\)': '.getSurface()',
    r'\.curve\(\)': '.getCurve()',
    r'\.curve3d\(\)': '.getCurve3d()',
    r'\.curve2\(\)': '.getCurve2()',
    r'\.start\(\)': '.getStart()',
    r'\.end\(\)': '.getEnd()',
    r'\.outerShell\(\)': '.getOuterShell()',
    r'\.voidShells\(\)': '.getVoidShells()',
    r'\.basisSurface\(\)': '.getBasisSurface()',
    r'\.distance\(\)': '.getDistance()',
    r'\.semiAngle\(\)': '.getSemiAngle()',
    r'\.semiAxis1\(\)': '.getSemiAxis1()',
    r'\.semiAxis2\(\)': '.getSemiAxis2()',
    r'\.semiAxisA\(\)': '.getSemiAxisA()',
    r'\.semiAxisB\(\)': '.getSemiAxisB()',
    r'\.focalDistance\(\)': '.getFocalDistance()',
    r'\.focalLength\(\)': '.getFocalLength()',
    r'\.majorRadius\(\)': '.getMajorRadius()',
    r'\.minorRadius\(\)': '.getMinorRadius()',
    r'\.directrix1\(\)': '.getDirectrix1()',
    r'\.directrix2\(\)': '.getDirectrix2()',
    r'\.sweptCurve\(\)': '.getSweptCurve()',
    r'\.sweptSurface\(\)': '.getSweptSurface()',
    r'\.extrusionVector\(\)': '.getExtrusionVector()',
    r'\.profile\(\)': '.getProfile()',
    r'\.projectionDirection\(\)': '.getProjectionDirection()',
    r'\.axisOrigin\(\)': '.getAxisOrigin()',
    r'\.axisDirection\(\)': '.getAxisDirection()',
    r'\.basisCurve\(\)': '.getBasisCurve()',
    r'\.trimParamStart\(\)': '.getTrimParamStart()',
    r'\.trimParamEnd\(\)': '.getTrimParamEnd()',
    r'\.point\(\)': '.getPoint()',
    r'\.vertex\(\)': '.getVertex()',
    r'\.edge\(\)': '.getEdge()',
    r'\.issues\(\)': '.getIssues()',
    
    # Boolean getters (is prefix)
    r'\.closed\(\)': '.isClosed()',
    r'\.sameSense\(\)': '.isSameSense()',
    r'\.orientation\(\)': '.isOrientation()',
    r'\.outer\(\)': '.isOuter()',
    r'\.senseAgreement\(\)': '.isSenseAgreement()',
}

def migrate_file(file_path):
    """Migrate a single Java file."""
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()
    
    original = content
    
    # Apply all replacements
    for pattern, replacement in ACCESSOR_MAP.items():
        content = re.sub(pattern, replacement, content)
    
    # Write back if changed
    if content != original:
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(content)
        return True
    return False

def main():
    """Main entry point."""
    # Find all Java files
    project_root = Path(__file__).parent.parent
    src_dir = project_root / 'src' / 'main' / 'java'
    
    java_files = list(src_dir.rglob('*.java'))
    
    migrated_count = 0
    for java_file in java_files:
        if migrate_file(java_file):
            migrated_count += 1
            print(f"Migrated: {java_file.relative_to(project_root)}")
    
    print(f"\nTotal files migrated: {migrated_count}")

if __name__ == '__main__':
    main()
