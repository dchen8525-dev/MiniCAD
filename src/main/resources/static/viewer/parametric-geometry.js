/** Rebuilds parametric curve and face geometry from STEP surface payloads. The bulk of the original file, and self-contained: it needs only the B-spline and formatting helpers. */

import * as THREE from 'three';
import { bsplineSurfaceNormal, bsplineSurfacePoint } from './bspline.js';
import { orthonormalY, vectorFromArray } from './format.js';
import { logJson } from './log.js';
export function rebuildCurveEdgeGeometry(node) {
    const curve = node.userData?.curve;
    if (!curve || !node.geometry) {
        return;
    }
    const axis = vectorFromArray(curve.axis).normalize();
    const xDirection = vectorFromArray(curve.xDirection).normalize();
    const yDirection = orthonormalY(axis, xDirection);
    const center = vectorFromArray(curve.center);
    const sweep = Math.abs(curve.sweepAngle ?? 0);
    let segments = 0;
    const positions = [];

    if (curve.type === 'circle_arc' && Number.isFinite(curve.radius)) {
        segments = Math.max(128, Math.ceil(sweep / (Math.PI / 180.0)));
        for (let index = 0; index <= segments; index += 1) {
            const angle = curve.startAngle + curve.sweepAngle * index / segments;
            const point = center.clone()
                .addScaledVector(xDirection, Math.cos(angle) * curve.radius)
                .addScaledVector(yDirection, Math.sin(angle) * curve.radius);
            positions.push(point.x, point.y, point.z);
        }
    } else if (curve.type === 'ellipse_arc' && Number.isFinite(curve.semiAxis1) && Number.isFinite(curve.semiAxis2)) {
        segments = Math.max(160, Math.ceil(sweep / (Math.PI / 240.0)));
        for (let index = 0; index <= segments; index += 1) {
            const angle = curve.startAngle + curve.sweepAngle * index / segments;
            const point = center.clone()
                .addScaledVector(xDirection, Math.cos(angle) * curve.semiAxis1)
                .addScaledVector(yDirection, Math.sin(angle) * curve.semiAxis2);
            positions.push(point.x, point.y, point.z);
        }
    }

    if (positions.length >= 6) {
        const geometry = new THREE.BufferGeometry();
        geometry.setAttribute('position', new THREE.Float32BufferAttribute(positions, 3));
        node.geometry.dispose();
        node.geometry = geometry;
    }
}

export function rebuildParametricFaceGeometry(node) {
    const surface = node.userData?.surface;
    if (!surface || !node.isMesh) {
        return;
    }
    const faceLog = {
        stepId: node.userData?.stepId ?? null,
        instanceId: node.userData?.instanceId ?? null,
        surfaceType: node.userData?.selection?.find?.((entry) => entry[0] === '曲面')?.[1] ?? null,
        parametricType: surface.type,
        sameSense: node.userData?.sameSense ?? null,
        hasSurfaceLoops: Array.isArray(node.userData?.surfaceLoops),
        hasSurfaceUvLoops: Array.isArray(node.userData?.surfaceUvLoops),
        originalVertexCount: node.geometry?.getAttribute?.('position')?.count ?? 0
    };

    function evaluateBasisPoint(basis, u, v) {
        const type = basis.type;
        const center = basis.center ? vectorFromArray(basis.center) : new THREE.Vector3(0, 0, 0);
        const axis = basis.axis ? vectorFromArray(basis.axis).normalize() : new THREE.Vector3(0, 0, 1);
        const xDir = basis.xDirection ? vectorFromArray(basis.xDirection).normalize() : new THREE.Vector3(1, 0, 0);
        const yDir = axis.clone().cross(xDir).normalize();
        if (type === 'plane_face' || type === 'PLANE') {
            return [center.x + xDir.x * u + yDir.x * v, center.y + xDir.y * u + yDir.y * v, center.z + xDir.z * u + yDir.z * v];
        }
        if (type === 'cylindrical_strip' || type === 'CYLINDRICAL_SURFACE') {
            const r = basis.radius || 1;
            return [center.x + xDir.x * r * Math.cos(u * Math.PI * 2) + yDir.x * r * Math.sin(u * Math.PI * 2) + axis.x * v,
                     center.y + xDir.y * r * Math.cos(u * Math.PI * 2) + yDir.y * r * Math.sin(u * Math.PI * 2) + axis.y * v,
                     center.z + xDir.z * r * Math.cos(u * Math.PI * 2) + yDir.z * r * Math.sin(u * Math.PI * 2) + axis.z * v];
        }
        if (type === 'conical_strip' || type === 'CONICAL_SURFACE') {
            const r = (basis.radius || 1) + v * Math.tan(basis.semiAngle || 0.5);
            return [center.x + xDir.x * r * Math.cos(u * Math.PI * 2) + yDir.x * r * Math.sin(u * Math.PI * 2) + axis.x * v,
                     center.y + xDir.y * r * Math.cos(u * Math.PI * 2) + yDir.y * r * Math.sin(u * Math.PI * 2) + axis.y * v,
                     center.z + xDir.z * r * Math.cos(u * Math.PI * 2) + yDir.z * r * Math.sin(u * Math.PI * 2) + axis.z * v];
        }
        if (type === 'spherical_surface' || type === 'SPHERICAL_SURFACE') {
            const r = basis.radius || 1;
            const theta = u * Math.PI * 2;
            const phi = v * Math.PI;
            return [center.x + r * Math.sin(phi) * Math.cos(theta) * xDir.x + r * Math.sin(phi) * Math.sin(theta) * yDir.x + r * Math.cos(phi) * axis.x,
                     center.y + r * Math.sin(phi) * Math.cos(theta) * xDir.y + r * Math.sin(phi) * Math.sin(theta) * yDir.y + r * Math.cos(phi) * axis.y,
                     center.z + r * Math.sin(phi) * Math.cos(theta) * xDir.z + r * Math.sin(phi) * Math.sin(theta) * yDir.z + r * Math.cos(phi) * axis.z];
        }
        if (type === 'toroidal_strip' || type === 'TOROIDAL_SURFACE') {
            const R = basis.radius || 1;
            const r = basis.minorRadius || 0.3;
            const theta = u * Math.PI * 2;
            const phi = v * Math.PI * 2;
            const ringR = R + r * Math.cos(phi);
            return [center.x + ringR * Math.cos(theta) * xDir.x + ringR * Math.sin(theta) * yDir.x + r * Math.sin(phi) * axis.x,
                     center.y + ringR * Math.cos(theta) * xDir.y + ringR * Math.sin(theta) * yDir.y + r * Math.sin(phi) * axis.y,
                     center.z + ringR * Math.cos(theta) * xDir.z + ringR * Math.sin(theta) * yDir.z + r * Math.sin(phi) * axis.z];
        }
        if (type === 'surface_of_revolution' || type === 'SURFACE_OF_REVOLUTION') {
            const sweepAngle = basis.sweepAngle ?? Math.PI * 2;
            const startAngle = basis.startAngle ?? 0;
            const angle = startAngle + sweepAngle * u;
            const profileR = (basis.radius || 1) * (1 - v) + ((basis.semiAxis1 || basis.radius) || 1) * v;
            return [center.x + profileR * Math.cos(angle) * xDir.x + profileR * Math.sin(angle) * yDir.x + axis.x * v,
                     center.y + profileR * Math.cos(angle) * xDir.y + profileR * Math.sin(angle) * yDir.y + axis.y * v,
                     center.z + profileR * Math.cos(angle) * xDir.z + profileR * Math.sin(angle) * yDir.z + axis.z * v];
        }
        if (type === 'surface_of_linear_extrusion' || type === 'SURFACE_OF_LINEAR_EXTRUSION') {
            return [center.x + xDir.x * u + yDir.x * v * (basis.upperHeight || 1),
                     center.y + xDir.y * u + yDir.y * v * (basis.upperHeight || 1),
                     center.z + xDir.z * u + yDir.z * v * (basis.upperHeight || 1)];
        }
        if (type === 'ruled_surface' || type === 'RULED_SURFACE') {
            const d1 = basis.directrix1;
            const d2 = basis.directrix2;
            if (!d1 || !d2 || d1.length === 0) return null;
            const ci = Math.min(Math.floor(u * (d1.length - 1)), d1.length - 1);
            const p1 = vectorFromArray(d1[ci]);
            const p2 = vectorFromArray(d2[Math.min(ci, d2.length - 1)]);
            return [p1.x + (p2.x - p1.x) * v, p1.y + (p2.y - p1.y) * v, p1.z + (p2.z - p1.z) * v];
        }
        return null;
    }

    function evaluateBasisNormal(basis, u, v) {
        const type = basis.type;
        const center = basis.center ? vectorFromArray(basis.center) : new THREE.Vector3(0, 0, 0);
        const axis = basis.axis ? vectorFromArray(basis.axis).normalize() : new THREE.Vector3(0, 0, 1);
        const xDir = basis.xDirection ? vectorFromArray(basis.xDirection).normalize() : new THREE.Vector3(1, 0, 0);
        const yDir = axis.clone().cross(xDir).normalize();
        if (type === 'plane_face' || type === 'PLANE') {
            const n = axis.clone().cross(xDir).normalize();
            return [n.x, n.y, n.z];
        }
        if (type === 'cylindrical_strip' || type === 'CYLINDRICAL_SURFACE') {
            const angle = u * Math.PI * 2;
            return [xDir.x * Math.cos(angle) + yDir.x * Math.sin(angle),
                     xDir.y * Math.cos(angle) + yDir.y * Math.sin(angle),
                     xDir.z * Math.cos(angle) + yDir.z * Math.sin(angle)];
        }
        if (type === 'conical_strip' || type === 'CONICAL_SURFACE') {
            const angle = u * Math.PI * 2;
            const semiAngle = basis.semiAngle || 0.5;
            const n = new THREE.Vector3(
                xDir.x * Math.cos(angle) + yDir.x * Math.sin(angle) - axis.x * Math.tan(semiAngle),
                xDir.y * Math.cos(angle) + yDir.y * Math.sin(angle) - axis.y * Math.tan(semiAngle),
                xDir.z * Math.cos(angle) + yDir.z * Math.sin(angle) - axis.z * Math.tan(semiAngle)
            ).normalize();
            return [n.x, n.y, n.z];
        }
        if (type === 'spherical_surface' || type === 'SPHERICAL_SURFACE') {
            const theta = u * Math.PI * 2;
            const phi = v * Math.PI;
            const n = new THREE.Vector3(
                Math.sin(phi) * Math.cos(theta) * xDir.x + Math.sin(phi) * Math.sin(theta) * yDir.x + Math.cos(phi) * axis.x,
                Math.sin(phi) * Math.cos(theta) * xDir.y + Math.sin(phi) * Math.sin(theta) * yDir.y + Math.cos(phi) * axis.y,
                Math.sin(phi) * Math.cos(theta) * xDir.z + Math.sin(phi) * Math.sin(theta) * yDir.z + Math.cos(phi) * axis.z
            ).normalize();
            return [n.x, n.y, n.z];
        }
        if (type === 'toroidal_strip' || type === 'TOROIDAL_SURFACE') {
            const theta = u * Math.PI * 2;
            const phi = v * Math.PI * 2;
            const n = new THREE.Vector3(
                Math.cos(phi) * Math.cos(theta) * xDir.x + Math.cos(phi) * Math.sin(theta) * yDir.x + Math.sin(phi) * axis.x,
                Math.cos(phi) * Math.cos(theta) * xDir.y + Math.cos(phi) * Math.sin(theta) * yDir.y + Math.sin(phi) * axis.y,
                Math.cos(phi) * Math.cos(theta) * xDir.z + Math.cos(phi) * Math.sin(theta) * yDir.z + Math.sin(phi) * axis.z
            ).normalize();
            return [n.x, n.y, n.z];
        }
        // For complex types (bspline, etc.), return null to fall back to mesh
        return null;
    }

    if (surface.type === 'bspline_surface' && Array.isArray(node.userData?.surfaceUvLoops) && node.userData.surfaceUvLoops.length > 0) {
        const outerLoop = node.userData.surfaceUvLoops.find((loop) => loop.outer);
        if (!outerLoop || !Array.isArray(outerLoop.points) || outerLoop.points.length < 3) {
            logJson('parametricFace:skip', { ...faceLog, reason: 'missing-bspline-outer-loop' });
            return;
        }
        const outerPoints = outerLoop.points.slice(0, -1).map((point) => new THREE.Vector2(point[0], point[1]));
        const shapePoints = THREE.ShapeUtils.isClockWise(outerPoints) ? outerPoints.slice().reverse() : outerPoints;
        const shape = new THREE.Shape(shapePoints);
        for (const loop of node.userData.surfaceUvLoops) {
            if (loop.outer || !Array.isArray(loop.points) || loop.points.length < 3) {
                continue;
            }
            const holePoints = loop.points.slice(0, -1).map((point) => new THREE.Vector2(point[0], point[1]));
            const normalizedHole = THREE.ShapeUtils.isClockWise(holePoints) ? holePoints : holePoints.slice().reverse();
            shape.holes.push(new THREE.Path(normalizedHole));
        }
        const geometry2d = new THREE.ShapeGeometry(shape, 48);
        const positions = geometry2d.attributes.position;
        const normals = new Float32Array(positions.count * 3);
        const sameSense = node.userData?.sameSense !== false;
        for (let index = 0; index < positions.count; index += 1) {
            const u = positions.getX(index);
            const v = positions.getY(index);
            const point = bsplineSurfacePoint(surface, u, v);
            const normal = bsplineSurfaceNormal(surface, u, v);
            if (!sameSense) {
                normal.multiplyScalar(-1);
            }
            positions.setXYZ(index, point.x, point.y, point.z);
            normals[index * 3] = normal.x;
            normals[index * 3 + 1] = normal.y;
            normals[index * 3 + 2] = normal.z;
        }
        geometry2d.setAttribute('normal', new THREE.BufferAttribute(normals, 3));
        node.geometry.dispose();
        node.geometry = geometry2d;
        logJson('parametricFace:rebuilt', {
            ...faceLog,
            uvLoopCount: node.userData.surfaceUvLoops.length,
            rebuiltVertexCount: positions.count,
            rebuiltIndexCount: geometry2d.index?.count ?? 0
        });
        return;
    }
    if (surface.type === 'plane_face' && Array.isArray(node.userData?.surfaceLoops) && node.userData.surfaceLoops.length > 0) {
        const axis = vectorFromArray(surface.axis).normalize();
        const xDirection = vectorFromArray(surface.xDirection).normalize();
        const yDirection = orthonormalY(axis, xDirection);
        const center = vectorFromArray(surface.center);
        const toPlanePoint = (point) => {
            const value = vectorFromArray(point).sub(center);
            return new THREE.Vector2(value.dot(xDirection), value.dot(yDirection));
        };
        const outerLoop = node.userData.surfaceLoops.find((loop) => loop.outer);
        if (!outerLoop || !Array.isArray(outerLoop.points) || outerLoop.points.length < 3) {
            logJson('parametricFace:skip', { ...faceLog, reason: 'missing-plane-outer-loop' });
            return;
        }
        const outerPoints = outerLoop.points.slice(0, -1).map(toPlanePoint);
        const shapePoints = THREE.ShapeUtils.isClockWise(outerPoints) ? outerPoints.slice().reverse() : outerPoints;
        const shape = new THREE.Shape(shapePoints);
        for (const loop of node.userData.surfaceLoops) {
            if (loop.outer || !Array.isArray(loop.points) || loop.points.length < 3) {
                continue;
            }
            const holePoints = loop.points.slice(0, -1).map(toPlanePoint);
            const normalizedHole = THREE.ShapeUtils.isClockWise(holePoints) ? holePoints : holePoints.slice().reverse();
            shape.holes.push(new THREE.Path(normalizedHole));
        }
        const geometry2d = new THREE.ShapeGeometry(shape);
        const positions = geometry2d.attributes.position;
        const normals = new Float32Array(positions.count * 3);
        const sameSense = node.userData?.sameSense !== false;
        const planeNormal = sameSense ? axis : axis.clone().multiplyScalar(-1);
        for (let index = 0; index < positions.count; index += 1) {
            const u = positions.getX(index);
            const v = positions.getY(index);
            const point = center.clone()
                .addScaledVector(xDirection, u)
                .addScaledVector(yDirection, v);
            positions.setXYZ(index, point.x, point.y, point.z);
            normals[index * 3] = planeNormal.x;
            normals[index * 3 + 1] = planeNormal.y;
            normals[index * 3 + 2] = planeNormal.z;
        }
        geometry2d.setAttribute('normal', new THREE.BufferAttribute(normals, 3));
        node.geometry.dispose();
        node.geometry = geometry2d;
        logJson('parametricFace:rebuilt', {
            ...faceLog,
            loopCount: node.userData.surfaceLoops.length,
            rebuiltVertexCount: positions.count,
            rebuiltIndexCount: geometry2d.index?.count ?? 0
        });
        return;
    }
    if (surface.type === 'spherical_surface' && Number.isFinite(surface.radius)) {
        const axis = vectorFromArray(surface.axis).normalize();
        const xDirection = vectorFromArray(surface.xDirection).normalize();
        const yDirection = orthonormalY(axis, xDirection);
        const center = vectorFromArray(surface.center);
        const uMin = surface.trimU1 ?? 0;
        const uMax = surface.trimU2 ?? Math.PI * 2;
        const vMin = surface.trimV1 ?? -Math.PI / 2;
        const vMax = surface.trimV2 ?? Math.PI / 2;
        const uSpan = uMax - uMin;
        const vSpan = vMax - vMin;
        const radialSegments = Math.max(96, Math.ceil(Math.abs(uSpan) / (Math.PI / 180.0)));
        const heightSegments = Math.max(48, Math.ceil(Math.abs(vSpan) / (Math.PI / 180.0)));
        const vertexCount = (radialSegments + 1) * (heightSegments + 1);
        const positions = new Float32Array(vertexCount * 3);
        const normals = new Float32Array(vertexCount * 3);
        const indices = [];
        const sameSense = node.userData?.sameSense !== false;

        for (let v = 0; v <= heightSegments; v += 1) {
            const lat = vMin + vSpan * v / heightSegments;
            const cosLat = Math.cos(lat);
            const sinLat = Math.sin(lat);
            for (let u = 0; u <= radialSegments; u += 1) {
                const lon = uMin + uSpan * u / radialSegments;
                const cosLon = Math.cos(lon);
                const sinLon = Math.sin(lon);
                const offset = (v * (radialSegments + 1) + u) * 3;
                const dir = xDirection.clone().multiplyScalar(cosLat * cosLon)
                    .addScaledVector(yDirection, cosLat * sinLon)
                    .addScaledVector(axis, sinLat);
                positions[offset] = center.x + dir.x * surface.radius;
                positions[offset + 1] = center.y + dir.y * surface.radius;
                positions[offset + 2] = center.z + dir.z * surface.radius;
                const n = sameSense ? dir : dir.multiplyScalar(-1);
                normals[offset] = n.x;
                normals[offset + 1] = n.y;
                normals[offset + 2] = n.z;
            }
        }

        for (let v = 0; v < heightSegments; v += 1) {
            for (let u = 0; u < radialSegments; u += 1) {
                const a = v * (radialSegments + 1) + u;
                const b = a + 1;
                const c = a + radialSegments + 1;
                const d = c + 1;
                if (sameSense) {
                    indices.push(a, c, d, a, d, b);
                } else {
                    indices.push(a, d, c, a, b, d);
                }
            }
        }

        const geometry = new THREE.BufferGeometry();
        geometry.setAttribute('position', new THREE.BufferAttribute(positions, 3));
        geometry.setAttribute('normal', new THREE.BufferAttribute(normals, 3));
        geometry.setIndex(indices);
        node.geometry.dispose();
        node.geometry = geometry;
        logJson('parametricFace:rebuilt', {
            ...faceLog,
            radialSegments,
            heightSegments,
            rebuiltVertexCount: vertexCount,
            rebuiltIndexCount: indices.length
        });
        return;
    }

    if (surface.type === 'surface_of_revolution') {
        const axis = vectorFromArray(surface.axis).normalize();
        const xDirection = vectorFromArray(surface.xDirection).normalize();
        const yDirection = orthonormalY(axis, xDirection);
        const center = vectorFromArray(surface.center);
        const profileRadius = surface.radius || 1.0;
        const sweep = surface.sweepAngle ?? Math.PI * 2;
        const heightSpan = (surface.upperHeight ?? 0) - (surface.lowerHeight ?? 0);
        const radialSegments = Math.max(192, Math.ceil(Math.abs(sweep) / (Math.PI / 360.0)));
        const heightSegments = Math.max(12, Math.min(96, Math.ceil(Math.abs(heightSpan) / Math.max(profileRadius * 0.08, 1.0))));
        const vertexCount = (radialSegments + 1) * (heightSegments + 1);
        const positions = new Float32Array(vertexCount * 3);
        const normals = new Float32Array(vertexCount * 3);
        const indices = [];
        const sameSense = node.userData?.sameSense !== false;

        for (let v = 0; v <= heightSegments; v += 1) {
            const height = surface.lowerHeight + heightSpan * v / heightSegments;
            for (let u = 0; u <= radialSegments; u += 1) {
                const angle = surface.startAngle + sweep * u / radialSegments;
                const radial = xDirection.clone().multiplyScalar(Math.cos(angle))
                    .addScaledVector(yDirection, Math.sin(angle));
                const offset = (v * (radialSegments + 1) + u) * 3;
                positions[offset] = center.x + radial.x * profileRadius + axis.x * height;
                positions[offset + 1] = center.y + radial.y * profileRadius + axis.y * height;
                positions[offset + 2] = center.z + radial.z * profileRadius + axis.z * height;
                const n = sameSense ? radial : radial.clone().multiplyScalar(-1);
                normals[offset] = n.x;
                normals[offset + 1] = n.y;
                normals[offset + 2] = n.z;
            }
        }

        for (let v = 0; v < heightSegments; v += 1) {
            for (let u = 0; u < radialSegments; u += 1) {
                const a = v * (radialSegments + 1) + u;
                const b = a + 1;
                const c = a + radialSegments + 1;
                const d = c + 1;
                if (sameSense) {
                    indices.push(a, c, d, a, d, b);
                } else {
                    indices.push(a, d, c, a, b, d);
                }
            }
        }

        const geometry = new THREE.BufferGeometry();
        geometry.setAttribute('position', new THREE.BufferAttribute(positions, 3));
        geometry.setAttribute('normal', new THREE.BufferAttribute(normals, 3));
        geometry.setIndex(indices);
        node.geometry.dispose();
        node.geometry = geometry;
        logJson('parametricFace:rebuilt', {
            ...faceLog,
            radialSegments,
            heightSegments,
            rebuiltVertexCount: vertexCount,
            rebuiltIndexCount: indices.length
        });
        return;
    }

    if (surface.type === 'surface_of_linear_extrusion') {
        const axis = vectorFromArray(surface.axis).normalize();
        const xDirection = surface.xDirection ? vectorFromArray(surface.xDirection).normalize() : null;
        const heightSpan = (surface.upperHeight ?? 0) - (surface.lowerHeight ?? 0);
        const uMin = surface.trimU1 ?? 0;
        const uMax = surface.trimU2 ?? 1;
        const uSpan = uMax - uMin;
        const radialSegments = Math.max(96, Math.ceil(Math.abs(uSpan) * 48));
        const heightSegments = Math.max(8, Math.min(48, Math.ceil(Math.abs(heightSpan))));
        const vertexCount = (radialSegments + 1) * (heightSegments + 1);
        const positions = new Float32Array(vertexCount * 3);
        const normals = new Float32Array(vertexCount * 3);
        const indices = [];
        const sameSense = node.userData?.sameSense !== false;

        for (let v = 0; v <= heightSegments; v += 1) {
            const h = surface.lowerHeight + heightSpan * v / heightSegments;
            for (let u = 0; u <= radialSegments; u += 1) {
                const t = uMin + uSpan * u / radialSegments;
                const baseX = xDirection ? xDirection.x * t : t;
                const baseY = xDirection ? xDirection.y * t : 0;
                const baseZ = xDirection ? xDirection.z * t : 0;
                const offset = (v * (radialSegments + 1) + u) * 3;
                positions[offset] = (surface.center?.[0] ?? 0) + baseX + axis.x * h;
                positions[offset + 1] = (surface.center?.[1] ?? 0) + baseY + axis.y * h;
                positions[offset + 2] = (surface.center?.[2] ?? 0) + baseZ + axis.z * h;
                let tangent = xDirection || new THREE.Vector3(1, 0, 0);
                let n = tangent.clone().cross(axis).normalize();
                if (n.length() < 0.001) {
                    n = new THREE.Vector3(0, 1, 0).cross(axis).normalize();
                }
                if (!sameSense) {
                    n.multiplyScalar(-1);
                }
                normals[offset] = n.x;
                normals[offset + 1] = n.y;
                normals[offset + 2] = n.z;
            }
        }

        for (let v = 0; v < heightSegments; v += 1) {
            for (let u = 0; u < radialSegments; u += 1) {
                const a = v * (radialSegments + 1) + u;
                const b = a + 1;
                const c = a + radialSegments + 1;
                const d = c + 1;
                if (sameSense) {
                    indices.push(a, c, d, a, d, b);
                } else {
                    indices.push(a, d, c, a, b, d);
                }
            }
        }

        const geometry = new THREE.BufferGeometry();
        geometry.setAttribute('position', new THREE.BufferAttribute(positions, 3));
        geometry.setAttribute('normal', new THREE.BufferAttribute(normals, 3));
        geometry.setIndex(indices);
        node.geometry.dispose();
        node.geometry = geometry;
        logJson('parametricFace:rebuilt', {
            ...faceLog,
            radialSegments,
            heightSegments,
            rebuiltVertexCount: vertexCount,
            rebuiltIndexCount: indices.length
        });
        return;
    }

    if (surface.type === 'rational_bspline_surface' && Array.isArray(surface.controlPoints) && surface.controlPoints.length > 0) {
        const uMin = surface.trimU1 ?? surface.uStart ?? 0;
        const uMax = surface.trimU2 ?? surface.uEnd ?? 1;
        const vMin = surface.trimV1 ?? surface.vStart ?? 0;
        const vMax = surface.trimV2 ?? surface.vEnd ?? 1;
        const uSegments = Math.min(96, Math.max(24, surface.controlPoints.length * 4));
        const vSegments = Math.min(96, Math.max(24, (surface.controlPoints[0]?.length ?? 1) * 4));
        const vertexCount = (uSegments + 1) * (vSegments + 1);
        const positions = new Float32Array(vertexCount * 3);
        const normals = new Float32Array(vertexCount * 3);
        const indices = [];
        const sameSense = node.userData?.sameSense !== false;

        for (let v = 0; v <= vSegments; v += 1) {
            const sv = vMin + (vMax - vMin) * v / vSegments;
            for (let u = 0; u <= uSegments; u += 1) {
                const su = uMin + (uMax - uMin) * u / uSegments;
                const offset = (v * (uSegments + 1) + u) * 3;
                const point = bsplineSurfacePoint(surface, su, sv);
                positions[offset] = point.x;
                positions[offset + 1] = point.y;
                positions[offset + 2] = point.z;
                const normal = bsplineSurfaceNormal(surface, su, sv);
                const n = sameSense ? normal : normal.clone().multiplyScalar(-1);
                normals[offset] = n.x;
                normals[offset + 1] = n.y;
                normals[offset + 2] = n.z;
            }
        }

        for (let v = 0; v < vSegments; v += 1) {
            for (let u = 0; u < uSegments; u += 1) {
                const a = v * (uSegments + 1) + u;
                const b = a + 1;
                const c = a + uSegments + 1;
                const d = c + 1;
                if (sameSense) {
                    indices.push(a, c, d, a, d, b);
                } else {
                    indices.push(a, d, c, a, b, d);
                }
            }
        }

        const geometry = new THREE.BufferGeometry();
        geometry.setAttribute('position', new THREE.BufferAttribute(positions, 3));
        geometry.setAttribute('normal', new THREE.BufferAttribute(normals, 3));
        geometry.setIndex(indices);
        node.geometry.dispose();
        node.geometry = geometry;
        logJson('parametricFace:rebuilt', {
            ...faceLog,
            uSegments,
            vSegments,
            rebuiltVertexCount: vertexCount,
            rebuiltIndexCount: indices.length
        });
        return;
    }

    if (surface.type === 'paraboloid_surface') {
        const axis = vectorFromArray(surface.axis).normalize();
        const xDirection = vectorFromArray(surface.xDirection).normalize();
        const yDirection = orthonormalY(axis, xDirection);
        const center = vectorFromArray(surface.center);
        const focalLength = surface.radius;
        const uMin = surface.trimU1 ?? 0;
        const uMax = surface.trimU2 ?? Math.PI * 2;
        const vMin = surface.trimV1 ?? 0;
        const vMax = surface.trimV2 ?? 1.0;
        const uSpan = uMax - uMin;
        const vSpan = vMax - vMin;
        const radialSegments = Math.max(96, Math.ceil(Math.abs(uSpan) / (Math.PI / 180.0)));
        const heightSegments = Math.max(24, Math.ceil(Math.abs(vSpan) * 48));
        const vertexCount = (radialSegments + 1) * (heightSegments + 1);
        const positions = new Float32Array(vertexCount * 3);
        const normals = new Float32Array(vertexCount * 3);
        const indices = [];
        const sameSense = node.userData?.sameSense !== false;

        for (let v = 0; v <= heightSegments; v += 1) {
            const vz = vMin + vSpan * v / heightSegments;
            const r = Math.sqrt(4.0 * focalLength * Math.max(0.0, vz));
            for (let u = 0; u <= radialSegments; u += 1) {
                const angle = uMin + uSpan * u / radialSegments;
                const offset = (v * (radialSegments + 1) + u) * 3;
                const lx = r * Math.cos(angle);
                const ly = r * Math.sin(angle);
                const lz = vz;
                positions[offset] = center.x + xDirection.x * lx + yDirection.x * ly + axis.x * lz;
                positions[offset + 1] = center.y + xDirection.y * lx + yDirection.y * ly + axis.y * lz;
                positions[offset + 2] = center.z + xDirection.z * lx + yDirection.z * ly + axis.z * lz;
                // Gradient of F(x,y,z) = x^2 + y^2 - 4*f*z: (2x, 2y, -4f) in local
                const nx = 2.0 * lx;
                const ny = 2.0 * ly;
                const nz = -4.0 * focalLength;
                const localNormal = new THREE.Vector3(nx, ny, nz).normalize();
                const n = new THREE.Vector3(
                    xDirection.x * localNormal.x + yDirection.x * localNormal.y + axis.x * localNormal.z,
                    xDirection.y * localNormal.x + yDirection.y * localNormal.y + axis.y * localNormal.z,
                    xDirection.z * localNormal.x + yDirection.z * localNormal.y + axis.z * localNormal.z
                ).normalize();
                const finalN = sameSense ? n : n.multiplyScalar(-1);
                normals[offset] = finalN.x;
                normals[offset + 1] = finalN.y;
                normals[offset + 2] = finalN.z;
            }
        }

        for (let v = 0; v < heightSegments; v += 1) {
            for (let u = 0; u < radialSegments; u += 1) {
                const a = v * (radialSegments + 1) + u;
                const b = a + 1;
                const c = a + radialSegments + 1;
                const d = c + 1;
                if (sameSense) {
                    indices.push(a, c, d, a, d, b);
                } else {
                    indices.push(a, d, c, a, b, d);
                }
            }
        }

        const geometry = new THREE.BufferGeometry();
        geometry.setAttribute('position', new THREE.BufferAttribute(positions, 3));
        geometry.setAttribute('normal', new THREE.BufferAttribute(normals, 3));
        geometry.setIndex(indices);
        node.geometry.dispose();
        node.geometry = geometry;
        logJson('parametricFace:rebuilt', {
            ...faceLog,
            radialSegments,
            heightSegments,
            rebuiltVertexCount: vertexCount,
            rebuiltIndexCount: indices.length
        });
        return;
    }

    if (surface.type === 'hyperboloid_surface') {
        const axis = vectorFromArray(surface.axis).normalize();
        const xDirection = vectorFromArray(surface.xDirection).normalize();
        const yDirection = orthonormalY(axis, xDirection);
        const center = vectorFromArray(surface.center);
        const waistRadius = surface.radius;
        const semiAxis = surface.semiAngle;
        const uMin = surface.trimU1 ?? 0;
        const uMax = surface.trimU2 ?? Math.PI * 2;
        const vMin = surface.trimV1 ?? -1.0;
        const vMax = surface.trimV2 ?? 1.0;
        const uSpan = uMax - uMin;
        const vSpan = vMax - vMin;
        const radialSegments = Math.max(96, Math.ceil(Math.abs(uSpan) / (Math.PI / 180.0)));
        const heightSegments = Math.max(24, Math.ceil(Math.abs(vSpan) * 48));
        const vertexCount = (radialSegments + 1) * (heightSegments + 1);
        const positions = new Float32Array(vertexCount * 3);
        const normals = new Float32Array(vertexCount * 3);
        const indices = [];
        const sameSense = node.userData?.sameSense !== false;

        for (let v = 0; v <= heightSegments; v += 1) {
            const z = vMin + vSpan * v / heightSegments;
            const factor = Math.sqrt(1.0 + (z * z) / (semiAxis * semiAxis));
            const r = waistRadius * factor;
            for (let u = 0; u <= radialSegments; u += 1) {
                const angle = uMin + uSpan * u / radialSegments;
                const offset = (v * (radialSegments + 1) + u) * 3;
                const lx = r * Math.cos(angle);
                const ly = r * Math.sin(angle);
                positions[offset] = center.x + xDirection.x * lx + yDirection.x * ly + axis.x * z;
                positions[offset + 1] = center.y + xDirection.y * lx + yDirection.y * ly + axis.y * z;
                positions[offset + 2] = center.z + xDirection.z * lx + yDirection.z * ly + axis.z * z;
                // Gradient: (2x, 2y, -2*r0^2*z/b^2) in local
                const nx = 2.0 * lx;
                const ny = 2.0 * ly;
                const nz = -2.0 * waistRadius * waistRadius * z / (semiAxis * semiAxis);
                const localNormal = new THREE.Vector3(nx, ny, nz).normalize();
                const n = new THREE.Vector3(
                    xDirection.x * localNormal.x + yDirection.x * localNormal.y + axis.x * localNormal.z,
                    xDirection.y * localNormal.x + yDirection.y * localNormal.y + axis.y * localNormal.z,
                    xDirection.z * localNormal.x + yDirection.z * localNormal.y + axis.z * localNormal.z
                ).normalize();
                const finalN = sameSense ? n : n.multiplyScalar(-1);
                normals[offset] = finalN.x;
                normals[offset + 1] = finalN.y;
                normals[offset + 2] = finalN.z;
            }
        }

        for (let v = 0; v < heightSegments; v += 1) {
            for (let u = 0; u < radialSegments; u += 1) {
                const a = v * (radialSegments + 1) + u;
                const b = a + 1;
                const c = a + radialSegments + 1;
                const d = c + 1;
                if (sameSense) {
                    indices.push(a, c, d, a, d, b);
                } else {
                    indices.push(a, d, c, a, b, d);
                }
            }
        }

        const geometry = new THREE.BufferGeometry();
        geometry.setAttribute('position', new THREE.BufferAttribute(positions, 3));
        geometry.setAttribute('normal', new THREE.BufferAttribute(normals, 3));
        geometry.setIndex(indices);
        node.geometry.dispose();
        node.geometry = geometry;
        logJson('parametricFace:rebuilt', {
            ...faceLog,
            radialSegments,
            heightSegments,
            rebuiltVertexCount: vertexCount,
            rebuiltIndexCount: indices.length
        });
        return;
    }

    if (surface.type === 'surface_of_translation' || surface.type === 'surface_of_projection') {
        const dir = vectorFromArray(surface.axis).normalize();
        let xDirection = dir.clone().cross(new THREE.Vector3(1, 0, 0)).normalize();
        if (xDirection.length() < 0.001) {
            xDirection.copy(dir.clone().cross(new THREE.Vector3(0, 1, 0)).normalize());
        }
        const yDirection = dir.clone().cross(xDirection).normalize();
        const center = surface.center ? vectorFromArray(surface.center) : new THREE.Vector3(0, 0, 0);
        const uMin = surface.trimU1 ?? 0;
        const uMax = surface.trimU2 ?? 1.0;
        const vSpan = ((surface.upperHeight ?? 0) - (surface.lowerHeight ?? 0)) || 1.0;
        const vStart = surface.lowerHeight ?? 0;
        const uSpan = uMax - uMin;
        const curveSegments = Math.max(48, Math.ceil(Math.abs(uSpan) * 48));
        const heightSegments = Math.max(8, Math.min(48, Math.ceil(Math.abs(vSpan))));
        const vertexCount = (curveSegments + 1) * (heightSegments + 1);
        const positions = new Float32Array(vertexCount * 3);
        const normals = new Float32Array(vertexCount * 3);
        const indices = [];
        const sameSense = node.userData?.sameSense !== false;

        for (let v = 0; v <= heightSegments; v += 1) {
            const t = vStart + vSpan * v / heightSegments;
            for (let u = 0; u <= curveSegments; u += 1) {
                const s = uMin + uSpan * u / curveSegments;
                const offset = (v * (curveSegments + 1) + u) * 3;
                const lx = s;
                const ly = 0;
                const lz = 0;
                positions[offset] = center.x + xDirection.x * lx + yDirection.y * ly + dir.x * t;
                positions[offset + 1] = center.y + xDirection.y * lx + yDirection.y * ly + dir.y * t;
                positions[offset + 2] = center.z + xDirection.z * lx + yDirection.z * ly + dir.z * t;
                const tangent = xDirection;
                let n = tangent.clone().cross(dir).normalize();
                if (n.length() < 0.001) {
                    n = new THREE.Vector3(0, 1, 0).cross(dir).normalize();
                }
                if (!sameSense) {
                    n.multiplyScalar(-1);
                }
                normals[offset] = n.x;
                normals[offset + 1] = n.y;
                normals[offset + 2] = n.z;
            }
        }

        for (let v = 0; v < heightSegments; v += 1) {
            for (let u = 0; u < curveSegments; u += 1) {
                const a = v * (curveSegments + 1) + u;
                const b = a + 1;
                const c = a + curveSegments + 1;
                const d = c + 1;
                if (sameSense) {
                    indices.push(a, c, d, a, d, b);
                } else {
                    indices.push(a, d, c, a, b, d);
                }
            }
        }

        const geometry = new THREE.BufferGeometry();
        geometry.setAttribute('position', new THREE.BufferAttribute(positions, 3));
        geometry.setAttribute('normal', new THREE.BufferAttribute(normals, 3));
        geometry.setIndex(indices);
        node.geometry.dispose();
        node.geometry = geometry;
        logJson('parametricFace:rebuilt', {
            ...faceLog,
            parametricType: surface.type,
            curveSegments,
            heightSegments,
            rebuiltVertexCount: vertexCount,
            rebuiltIndexCount: indices.length
        });
        return;
    }

    if (surface.type === 'ruled_surface') {
        const d1 = surface.directrix1;
        const d2 = surface.directrix2;
        if (!d1 || !d2 || d1.length === 0 || d2.length === 0) {
            logJson('parametricFace:skip', { ...faceLog, reason: 'missing-directrix-curves' });
            return;
        }
        const curveSegments = Math.max(32, d1.length - 1);
        const ruleSegments = 8;
        const vertexCount = (curveSegments + 1) * (ruleSegments + 1);
        const positions = new Float32Array(vertexCount * 3);
        const normals = new Float32Array(vertexCount * 3);
        const indices = [];
        const sameSense = node.userData?.sameSense !== false;

        for (let i = 0; i <= curveSegments; i += 1) {
            const ci = Math.min(i, d1.length - 1);
            const p1 = vectorFromArray(d1[ci]);
            const p2 = vectorFromArray(d2[ci]);
            for (let j = 0; j <= ruleSegments; j += 1) {
                const t = j / ruleSegments;
                const offset = (i * (ruleSegments + 1) + j) * 3;
                positions[offset] = p1.x + (p2.x - p1.x) * t;
                positions[offset + 1] = p1.y + (p2.y - p1.y) * t;
                positions[offset + 2] = p1.z + (p2.z - p1.z) * t;
            }
        }

        for (let i = 0; i < curveSegments; i += 1) {
            const iNext = Math.min(i + 1, curveSegments);
            const p1 = vectorFromArray(d1[Math.min(i, d1.length - 1)]);
            const p1n = vectorFromArray(d1[Math.min(iNext, d1.length - 1)]);
            const p2 = vectorFromArray(d2[Math.min(i, d2.length - 1)]);
            const p2n = vectorFromArray(d2[Math.min(iNext, d2.length - 1)]);
            const tangent = p1n.clone().sub(p1).normalize();
            const ruling = p2.clone().sub(p1).normalize();
            let n = tangent.clone().cross(ruling).normalize();
            if (n.length() < 0.001) n = new THREE.Vector3(0, 0, 1);
            if (!sameSense) n.multiplyScalar(-1);
            for (let j = 0; j <= ruleSegments; j += 1) {
                const offset = (i * (ruleSegments + 1) + j) * 3;
                normals[offset] = n.x;
                normals[offset + 1] = n.y;
                normals[offset + 2] = n.z;
                const offset2 = (iNext * (ruleSegments + 1) + j) * 3;
                normals[offset2] = n.x;
                normals[offset2 + 1] = n.y;
                normals[offset2 + 2] = n.z;
            }
        }

        for (let i = 0; i < curveSegments; i += 1) {
            for (let j = 0; j < ruleSegments; j += 1) {
                const a = i * (ruleSegments + 1) + j;
                const b = a + 1;
                const c = a + ruleSegments + 1;
                const d = c + 1;
                if (sameSense) {
                    indices.push(a, c, d, a, d, b);
                } else {
                    indices.push(a, d, c, a, b, d);
                }
            }
        }

        const geometry = new THREE.BufferGeometry();
        geometry.setAttribute('position', new THREE.BufferAttribute(positions, 3));
        geometry.setAttribute('normal', new THREE.BufferAttribute(normals, 3));
        geometry.setIndex(indices);
        node.geometry.dispose();
        node.geometry = geometry;
        logJson('parametricFace:rebuilt', {
            ...faceLog,
            parametricType: 'ruled_surface',
            curveSegments,
            ruleSegments,
            rebuiltVertexCount: vertexCount,
            rebuiltIndexCount: indices.length
        });
        return;
    }

    if (surface.type === 'constant_radius_surface' || surface.type === 'offset_surface') {
        const basis = surface.basisSurface;
        if (!basis || !basis.type) {
            logJson('parametricFace:skip', { ...faceLog, reason: 'missing-basis-surface' });
            return;
        }
        const radius = surface.radius || 0;
        const isOffset = surface.type === 'offset_surface';
        const dist = isOffset ? (surface.offsetDistance ?? radius) : radius;

        const uSegments = 32;
        const vSegments = 32;
        const vertexCount = (uSegments + 1) * (vSegments + 1);
        const positions = new Float32Array(vertexCount * 3);
        const normals = new Float32Array(vertexCount * 3);
        const indices = [];
        const sameSense = node.userData?.sameSense !== false;

        for (let v = 0; v <= vSegments; v += 1) {
            for (let u = 0; u <= uSegments; u += 1) {
                const uu = u / uSegments;
                const vv = v / vSegments;
                const bp = evaluateBasisPoint(basis, uu, vv);
                const bn = evaluateBasisNormal(basis, uu, vv);
                if (!bp || !bn) {
                    const offset = (v * (uSegments + 1) + u) * 3;
                    positions[offset] = 0;
                    positions[offset + 1] = 0;
                    positions[offset + 2] = 0;
                    normals[offset] = 0;
                    normals[offset + 1] = 0;
                    normals[offset + 2] = 1;
                    continue;
                }
                const offset = (v * (uSegments + 1) + u) * 3;
                positions[offset] = bp[0] + bn[0] * dist;
                positions[offset + 1] = bp[1] + bn[1] * dist;
                positions[offset + 2] = bp[2] + bn[2] * dist;
                let n = new THREE.Vector3(bn[0], bn[1], bn[2]).normalize();
                if (!sameSense) n.multiplyScalar(-1);
                normals[offset] = n.x;
                normals[offset + 1] = n.y;
                normals[offset + 2] = n.z;
            }
        }

        for (let v = 0; v < vSegments; v += 1) {
            for (let u = 0; u < uSegments; u += 1) {
                const a = v * (uSegments + 1) + u;
                const b = a + 1;
                const c = a + uSegments + 1;
                const d = c + 1;
                if (sameSense) {
                    indices.push(a, c, d, a, d, b);
                } else {
                    indices.push(a, d, c, a, b, d);
                }
            }
        }

        const geometry = new THREE.BufferGeometry();
        geometry.setAttribute('position', new THREE.BufferAttribute(positions, 3));
        geometry.setAttribute('normal', new THREE.BufferAttribute(normals, 3));
        geometry.setIndex(indices);
        node.geometry.dispose();
        node.geometry = geometry;
        logJson('parametricFace:rebuilt', {
            ...faceLog,
            parametricType: surface.type,
            basisType: basis.type,
            offsetDistance: dist,
            rebuiltVertexCount: vertexCount,
            rebuiltIndexCount: indices.length
        });
        return;
    }

    if (!Number.isFinite(surface.radius) || surface.radius === 0.0) {
        logJson('parametricFace:skip', { ...faceLog, reason: 'missing-radius' });
        return;
    }

    const axis = vectorFromArray(surface.axis).normalize();
    const xDirection = vectorFromArray(surface.xDirection).normalize();
    const yDirection = orthonormalY(axis, xDirection);
    const center = vectorFromArray(surface.center);
    const sweep = surface.sweepAngle ?? 0;
    const heightSpan = (surface.upperHeight ?? 0) - (surface.lowerHeight ?? 0);
    const radialSegments = Math.max(192, Math.ceil(Math.abs(sweep) / (Math.PI / 360.0)));
    const heightSegments = Math.max(12, Math.min(96, Math.ceil(Math.abs(heightSpan) / Math.max(surface.radius * 0.08, 1.0))));
    const vertexCount = (radialSegments + 1) * (heightSegments + 1);
    const positions = new Float32Array(vertexCount * 3);
    const normals = new Float32Array(vertexCount * 3);
    const indices = [];
    const sameSense = node.userData?.sameSense !== false;

    for (let v = 0; v <= heightSegments; v += 1) {
        const height = surface.lowerHeight + heightSpan * v / heightSegments;
        for (let u = 0; u <= radialSegments; u += 1) {
            const angle = surface.startAngle + sweep * u / radialSegments;
            const radial = xDirection.clone().multiplyScalar(Math.cos(angle))
                .addScaledVector(yDirection, Math.sin(angle));
            let point;
            let normal;

            if (surface.type === 'cylindrical_strip') {
                point = center.clone()
                    .addScaledVector(radial, surface.radius)
                    .addScaledVector(axis, height);
                normal = sameSense ? radial : radial.clone().multiplyScalar(-1);
            } else if (surface.type === 'conical_strip' && Number.isFinite(surface.semiAngle)) {
                const radius = surface.radius + height * Math.tan(surface.semiAngle);
                point = center.clone()
                    .addScaledVector(radial, radius)
                    .addScaledVector(axis, height);
                const baseNormal = radial.clone().addScaledVector(axis, -Math.tan(surface.semiAngle)).normalize();
                normal = sameSense ? baseNormal : baseNormal.clone().multiplyScalar(-1);
            } else if (surface.type === 'toroidal_strip' && Number.isFinite(surface.minorRadius)) {
                const minorAngle = height;
                const ringRadius = surface.radius + surface.minorRadius * Math.cos(minorAngle);
                point = center.clone()
                    .addScaledVector(xDirection, Math.cos(angle) * ringRadius)
                    .addScaledVector(yDirection, Math.sin(angle) * ringRadius)
                    .addScaledVector(axis, surface.minorRadius * Math.sin(minorAngle));
                const baseNormal = xDirection.clone().multiplyScalar(Math.cos(angle) * Math.cos(minorAngle))
                    .addScaledVector(yDirection, Math.sin(angle) * Math.cos(minorAngle))
                    .addScaledVector(axis, Math.sin(minorAngle))
                    .normalize();
                normal = sameSense ? baseNormal : baseNormal.clone().multiplyScalar(-1);
            } else {
                logJson('parametricFace:skip', { ...faceLog, reason: 'unsupported-parametric-type' });
                return;
            }

            const offset = (v * (radialSegments + 1) + u) * 3;
            positions[offset] = point.x;
            positions[offset + 1] = point.y;
            positions[offset + 2] = point.z;
            normals[offset] = normal.x;
            normals[offset + 1] = normal.y;
            normals[offset + 2] = normal.z;
        }
    }

    for (let v = 0; v < heightSegments; v += 1) {
        for (let u = 0; u < radialSegments; u += 1) {
            const a = v * (radialSegments + 1) + u;
            const b = a + 1;
            const c = a + radialSegments + 1;
            const d = c + 1;
            if (sameSense) {
                indices.push(a, c, d, a, d, b);
            } else {
                indices.push(a, d, c, a, b, d);
            }
        }
    }

    const geometry = new THREE.BufferGeometry();
    geometry.setAttribute('position', new THREE.BufferAttribute(positions, 3));
    geometry.setAttribute('normal', new THREE.BufferAttribute(normals, 3));
    geometry.setIndex(indices);
    node.geometry.dispose();
    node.geometry = geometry;
    logJson('parametricFace:rebuilt', {
        ...faceLog,
        radialSegments,
        heightSegments,
        rebuiltVertexCount: vertexCount,
        rebuiltIndexCount: indices.length
    });
}
