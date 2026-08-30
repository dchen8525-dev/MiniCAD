/** Three.js scene, camera, renderer, post-processing, controls and the render loop. The top-level resize()/requestRender() calls are safe: they only schedule an animation frame, so nothing crosses a module boundary during evaluation. */

import * as THREE from 'three';
import { OrbitControls } from 'three/addons/controls/OrbitControls.js';
import { GLTFLoader } from 'three/addons/loaders/GLTFLoader.js';
import { boxToLog, clamp, niceCeil } from './format.js';
import { logDebug, logJson } from './log.js';
import { toVector3 } from './matrix.js';
import { updatePmiOverlay } from './pmi.js';
import { applyUnsupportedViewMode } from './ui-panels.js';
export const sceneHost = document.querySelector('#scene');
export const scene = new THREE.Scene();
scene.background = new THREE.Color(0xdfe7e8);

// Render scheduling state. Declared before any module-init call (resize(),
// applyUnsupportedViewMode(), …) can request a frame.
export let renderPending = false;

export const camera = new THREE.PerspectiveCamera(55, 1, 0.01, 5000);
camera.position.set(3.5, 2.8, 3.5);

export const renderer = new THREE.WebGLRenderer({ antialias: true });
export const basePixelRatio = Math.min(window.devicePixelRatio, 4);
renderer.setPixelRatio(basePixelRatio);
renderer.outputColorSpace = THREE.SRGBColorSpace;
renderer.domElement.style.width = '100%';
renderer.domElement.style.height = '100%';
renderer.domElement.style.display = 'block';
sceneHost.appendChild(renderer.domElement);

export const renderTarget = new THREE.WebGLRenderTarget(1, 1, {
    colorSpace: THREE.SRGBColorSpace
});
export const postScene = new THREE.Scene();
export const postCamera = new THREE.OrthographicCamera(-1, 1, 1, -1, 0, 1);

export const postMaterial = new THREE.ShaderMaterial({
    uniforms: {
        tDiffuse: { value: renderTarget.texture },
        resolution: { value: new THREE.Vector2(1, 1) }
    },
    vertexShader: `
        varying vec2 vUv;
        void main() {
            vUv = uv;
            gl_Position = vec4(position.xy, 0.0, 1.0);
        }
    `,
    fragmentShader: `
        uniform sampler2D tDiffuse;
        uniform vec2 resolution;
        varying vec2 vUv;

        void main() {
            vec2 inverseResolution = 1.0 / resolution;
            vec3 rgbNW = texture2D(tDiffuse, vUv + vec2(-1.0, -1.0) * inverseResolution).rgb;
            vec3 rgbNE = texture2D(tDiffuse, vUv + vec2(1.0, -1.0) * inverseResolution).rgb;
            vec3 rgbSW = texture2D(tDiffuse, vUv + vec2(-1.0, 1.0) * inverseResolution).rgb;
            vec3 rgbSE = texture2D(tDiffuse, vUv + vec2(1.0, 1.0) * inverseResolution).rgb;
            vec3 rgbM = texture2D(tDiffuse, vUv).rgb;

            vec3 luma = vec3(0.299, 0.587, 0.114);
            float lumaNW = dot(rgbNW, luma);
            float lumaNE = dot(rgbNE, luma);
            float lumaSW = dot(rgbSW, luma);
            float lumaSE = dot(rgbSE, luma);
            float lumaM = dot(rgbM, luma);

            float lumaMin = min(lumaM, min(min(lumaNW, lumaNE), min(lumaSW, lumaSE)));
            float lumaMax = max(lumaM, max(max(lumaNW, lumaNE), max(lumaSW, lumaSE)));

            vec2 dir;
            dir.x = -((lumaNW + lumaNE) - (lumaSW + lumaSE));
            dir.y = ((lumaNW + lumaSW) - (lumaNE + lumaSE));

            float dirReduce = max(
                (lumaNW + lumaNE + lumaSW + lumaSE) * (0.25 * (1.0 / 8.0)),
                1.0 / 128.0
            );
            float reciprocalDirMin = 1.0 / (min(abs(dir.x), abs(dir.y)) + dirReduce);
            dir = clamp(dir * reciprocalDirMin, vec2(-8.0), vec2(8.0)) * inverseResolution;

            vec3 rgbA = 0.5 * (
                texture2D(tDiffuse, vUv + dir * (1.0 / 3.0 - 0.5)).rgb +
                texture2D(tDiffuse, vUv + dir * (2.0 / 3.0 - 0.5)).rgb
            );
            vec3 rgbB = rgbA * 0.5 + 0.25 * (
                texture2D(tDiffuse, vUv + dir * -0.5).rgb +
                texture2D(tDiffuse, vUv + dir * 0.5).rgb
            );

            float lumaB = dot(rgbB, luma);
            if (lumaB < lumaMin || lumaB > lumaMax) {
                gl_FragColor = vec4(rgbA, 1.0);
            } else {
                gl_FragColor = vec4(rgbB, 1.0);
            }
        }
    `
});
postScene.add(new THREE.Mesh(new THREE.PlaneGeometry(2, 2), postMaterial));

export const controls = new OrbitControls(camera, renderer.domElement);
export const gltfLoader = new GLTFLoader();
controls.enableDamping = true;
controls.dampingFactor = 0.08;
controls.target.set(0, 0, 0);

scene.add(new THREE.AmbientLight(0xffffff, 1.6));

export const keyLight = new THREE.DirectionalLight(0xfff5e7, 2.4);
keyLight.position.set(5, 7, 6);
scene.add(keyLight);

export const rimLight = new THREE.DirectionalLight(0xd7eef7, 1.3);
rimLight.position.set(-4, -2, -5);
scene.add(rimLight);

export let grid = createGridHelper(10, 10);
scene.add(grid);

export let axes = createAxesHelper(1.2);
scene.add(axes);

export const modelRoot = new THREE.Group();
scene.add(modelRoot);
export const pmiRoot = new THREE.Group();
scene.add(pmiRoot);

export let lastRenderScale = -1;

export function createGridHelper(size, divisions) {
    const helper = new THREE.GridHelper(size, divisions, 0x67767a, 0x9eb2b7);
    const materials = Array.isArray(helper.material) ? helper.material : [helper.material];
    for (const material of materials) {
        material.opacity = 0.55;
        material.transparent = true;
    }
    return helper;
}

export function createAxesHelper(size) {
    return new THREE.AxesHelper(size);
}

export function updateReferenceGuides(bounds) {
    const min = toVector3(bounds.min);
    const max = toVector3(bounds.max);
    const size = max.clone().sub(min);
    const center = min.clone().add(max).multiplyScalar(0.5);
    const span = Math.max(size.x, size.y, 1);
    const gridSize = niceCeil(span * 1.4);
    const axesSize = Math.max(niceCeil(Math.max(size.x, size.y, size.z, 1) * 0.2), 1);

    scene.remove(grid);
    disposeObject(grid);
    grid = createGridHelper(gridSize, 10);
    grid.position.set(center.x, center.y, min.z);
    scene.add(grid);

    scene.remove(axes);
    disposeObject(axes);
    axes = createAxesHelper(axesSize);
    axes.position.set(min.x, min.y, min.z);
    scene.add(axes);

    logJson('updateReferenceGuides', {
        gridSize,
        axesSize,
        gridPosition: grid.position.toArray(),
        axesPosition: axes.position.toArray(),
        bounds: {
            min: bounds.min,
            max: bounds.max
        }
    });
}

export function currentRenderScale() {
    const distance = camera.position.distanceTo(controls.target);
    if (distance <= 2.5) {
        return 3.4;
    }
    if (distance <= 4.5) {
        return 2.8;
    }
    if (distance <= 8.0) {
        return 2.1;
    }
    return 1.5;
}

export function updateRenderResolution(force = false) {
    const width = sceneHost.clientWidth;
    const height = sceneHost.clientHeight;
    if (width === 0 || height === 0) {
        return;
    }
    const renderScale = currentRenderScale();
    if (!force && Math.abs(renderScale - lastRenderScale) < 0.05) {
        return;
    }
    lastRenderScale = renderScale;
    const scaledWidth = Math.max(1, Math.floor(width * renderer.getPixelRatio() * renderScale));
    const scaledHeight = Math.max(1, Math.floor(height * renderer.getPixelRatio() * renderScale));
    renderTarget.setSize(scaledWidth, scaledHeight);
    postMaterial.uniforms.resolution.value.set(scaledWidth, scaledHeight);
}

export function resize() {
    const width = sceneHost.clientWidth;
    const height = sceneHost.clientHeight;
    if (width === 0 || height === 0) {
        return;
    }
    renderer.setSize(width, height, false);
    updateRenderResolution(true);
    camera.aspect = width / height;
    camera.updateProjectionMatrix();
    requestRender();
}

window.addEventListener('resize', resize);
new ResizeObserver(() => resize()).observe(sceneHost);
resize();

export function renderFrame() {
    renderPending = false;
    // With damping enabled update() keeps returning true while inertia
    // settles, which keeps the on-demand loop alive until the camera rests.
    const cameraMoved = controls.update();
    updateRenderResolution();
    updatePmiOverlay();
    renderer.setRenderTarget(renderTarget);
    renderer.render(scene, camera);
    renderer.setRenderTarget(null);
    renderer.render(postScene, postCamera);
    if (cameraMoved) {
        requestRender();
    }
}

export function requestRender() {
    if (!renderPending) {
        renderPending = true;
        requestAnimationFrame(renderFrame);
    }
}

controls.addEventListener('change', requestRender);
requestRender();

export function disposeMaterial(material, disposedTextures = new Set()) {
    for (const value of Object.values(material)) {
        if (value?.isTexture && !disposedTextures.has(value)) {
            disposedTextures.add(value);
            disposeTexture(value);
        }
    }
    material.dispose();
}

export function disposeTexture(texture) {
    const image = texture.image;
    texture.dispose();
    if (image && typeof image.close === 'function') {
        image.close();
    }
}

export function disposeObject(object) {
    const disposedGeometries = new Set();
    const disposedMaterials = new Set();
    const disposedTextures = new Set();
    object.traverse((node) => {
        if (node.geometry && !disposedGeometries.has(node.geometry)) {
            disposedGeometries.add(node.geometry);
            node.geometry.dispose();
        }
        if (Array.isArray(node.material)) {
            node.material.forEach((material) => {
                if (material && !disposedMaterials.has(material)) {
                    disposedMaterials.add(material);
                    disposeMaterial(material, disposedTextures);
                }
            });
        } else if (node.material && !disposedMaterials.has(node.material)) {
            disposedMaterials.add(node.material);
            disposeMaterial(node.material, disposedTextures);
        }
    });
}

export function fitCamera(bounds) {
    logDebug('fitCamera:start', bounds);
    updateReferenceGuides(bounds);
    const min = toVector3(bounds.min);
    const max = toVector3(bounds.max);
    const center = min.clone().add(max).multiplyScalar(0.5);
    const size = max.clone().sub(min);
    const radius = Math.max(size.length() * 0.6, 1);

    controls.target.copy(center);
    camera.position.copy(center.clone().add(new THREE.Vector3(radius, radius * 0.75, radius)));
    camera.near = Math.max(radius / 200, 0.01);
    camera.far = Math.max(radius * 40, 100);
    camera.updateProjectionMatrix();
    controls.update();
    const sceneBox = new THREE.Box3().setFromObject(modelRoot);
    logJson('fitCamera:done', {
        center: center.toArray(),
        radius,
        near: camera.near,
        far: camera.far,
        position: camera.position.toArray(),
        previewBounds: {
            min: bounds.min,
            max: bounds.max
        },
        sceneBounds: boxToLog(sceneBox)
    });
}
