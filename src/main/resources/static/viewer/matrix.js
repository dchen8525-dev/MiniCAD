/** Matrix/point conversion helpers shared by the geometry builders. */

import * as THREE from 'three';
export function matrixFromRowMajor(elements) {
    return new THREE.Matrix4().set(
        elements[0], elements[1], elements[2], elements[3],
        elements[4], elements[5], elements[6], elements[7],
        elements[8], elements[9], elements[10], elements[11],
        elements[12], elements[13], elements[14], elements[15]
    );
}

export function matrixToRows(matrixLike) {
    if (!Array.isArray(matrixLike) || matrixLike.length !== 16) {
        return matrixLike;
    }
    return [
        matrixLike.slice(0, 4),
        matrixLike.slice(4, 8),
        matrixLike.slice(8, 12),
        matrixLike.slice(12, 16)
    ];
}

export function toVector3(point) {
    return new THREE.Vector3(point[0], point[1], point[2]);
}
