import * as THREE from 'three';
import { OrbitControls } from 'three/addons/controls/OrbitControls.js';

const stepInput = document.querySelector('#step-input');
const fileInput = document.querySelector('#file-input');
const renderButton = document.querySelector('#render-button');
const loadExampleButton = document.querySelector('#load-example');
const exampleSelect = document.querySelector('#example-select');
const statusText = document.querySelector('#status-text');
const selectionDetails = document.querySelector('#selection-details');
const statElements = new Map(
    Array.from(document.querySelectorAll('[data-stat]')).map((element) => [element.dataset.stat, element])
);

const sceneHost = document.querySelector('#scene');
const scene = new THREE.Scene();
scene.background = new THREE.Color(0xdfe7e8);

const camera = new THREE.PerspectiveCamera(55, 1, 0.01, 5000);
camera.position.set(3.5, 2.8, 3.5);

const renderer = new THREE.WebGLRenderer({ antialias: true });
renderer.setPixelRatio(Math.min(window.devicePixelRatio, 2));
renderer.outputColorSpace = THREE.SRGBColorSpace;
sceneHost.appendChild(renderer.domElement);

const controls = new OrbitControls(camera, renderer.domElement);
controls.enableDamping = true;
controls.dampingFactor = 0.08;
controls.target.set(0, 0, 0);

scene.add(new THREE.AmbientLight(0xffffff, 1.6));

const keyLight = new THREE.DirectionalLight(0xfff5e7, 2.4);
keyLight.position.set(5, 7, 6);
scene.add(keyLight);

const rimLight = new THREE.DirectionalLight(0xd7eef7, 1.3);
rimLight.position.set(-4, -2, -5);
scene.add(rimLight);

const grid = new THREE.GridHelper(10, 10, 0x67767a, 0x9eb2b7);
grid.material.opacity = 0.55;
grid.material.transparent = true;
scene.add(grid);

const axes = new THREE.AxesHelper(1.2);
scene.add(axes);

const modelRoot = new THREE.Group();
scene.add(modelRoot);

const raycaster = new THREE.Raycaster();
raycaster.params.Line.threshold = 0.14;
const pointer = new THREE.Vector2();
let interactiveObjects = [];
let selectedObject = null;

function resize() {
    const width = sceneHost.clientWidth;
    const height = sceneHost.clientHeight;
    if (width === 0 || height === 0) {
        return;
    }
    renderer.setSize(width, height, false);
    camera.aspect = width / height;
    camera.updateProjectionMatrix();
}

window.addEventListener('resize', resize);
new ResizeObserver(() => resize()).observe(sceneHost);
resize();

function animate() {
    controls.update();
    renderer.render(scene, camera);
    requestAnimationFrame(animate);
}

animate();

function setStatus(text) {
    statusText.textContent = text;
}

function updateStats(stats = {}) {
    for (const [key, element] of statElements.entries()) {
        element.textContent = stats[key] ?? 0;
    }
}

function setSelection(entries) {
    selectionDetails.innerHTML = entries.map(([label, value]) => `<dt>${label}</dt><dd>${value}</dd>`).join('');
}

function resetSelection() {
    if (selectedObject) {
        applySelectionStyle(selectedObject, false);
        selectedObject = null;
    }
    setSelection([
        ['类型', '未选中'],
        ['说明', '点击右侧模型中的面或边查看详情。']
    ]);
}

function clearModel() {
    while (modelRoot.children.length > 0) {
        const child = modelRoot.children[0];
        modelRoot.remove(child);
        disposeObject(child);
    }
    interactiveObjects = [];
    resetSelection();
}

function disposeObject(object) {
    object.traverse((node) => {
        if (node.geometry) {
            node.geometry.dispose();
        }
        if (Array.isArray(node.material)) {
            node.material.forEach((material) => material.dispose());
        } else if (node.material) {
            node.material.dispose();
        }
    });
}

function toVector3(point) {
    return new THREE.Vector3(point[0], point[1], point[2]);
}

function normalizeLoop(loop) {
    if (loop.length > 1) {
        const first = loop[0];
        const last = loop[loop.length - 1];
        if (first.distanceToSquared(last) < 1e-12) {
            return loop.slice(0, -1);
        }
    }
    return loop;
}

function projectFace(points3D, normal) {
    const origin = points3D[0];
    let xAxis = null;

    for (let i = 1; i < points3D.length; i += 1) {
        const candidate = points3D[i].clone().sub(origin);
        if (candidate.lengthSq() > 1e-12) {
            xAxis = candidate.normalize();
            break;
        }
    }

    if (!xAxis) {
        return null;
    }

    const yAxis = new THREE.Vector3().crossVectors(normal, xAxis).normalize();
    if (yAxis.lengthSq() < 1e-12) {
        return null;
    }

    const projected = points3D.map((point) => {
        const offset = point.clone().sub(origin);
        return new THREE.Vector2(offset.dot(xAxis), offset.dot(yAxis));
    });

    return { origin, xAxis, yAxis, projected };
}

function signedArea(points) {
    let area = 0;
    for (let i = 0; i < points.length; i += 1) {
        const current = points[i];
        const next = points[(i + 1) % points.length];
        area += current.x * next.y - next.x * current.y;
    }
    return area * 0.5;
}

function buildFaceMesh(face) {
    const outerLoop = face.loops.find((loop) => loop.outer);
    if (!outerLoop || outerLoop.points.length < 3) {
        return null;
    }

    const normal = toVector3(face.normal).normalize();
    const outerPoints3D = normalizeLoop(outerLoop.points.map(toVector3));
    const outerProjection = projectFace(outerPoints3D, normal);
    if (!outerProjection) {
        return null;
    }

    let contour2D = outerProjection.projected;
    let contour3D = outerPoints3D;

    if (signedArea(contour2D) < 0) {
        contour2D = contour2D.slice().reverse();
        contour3D = contour3D.slice().reverse();
    }

    const holes2D = [];
    const holes3D = [];

    for (const loop of face.loops.filter((entry) => !entry.outer)) {
        const loopPoints3D = normalizeLoop(loop.points.map(toVector3));
        if (loopPoints3D.length < 3) {
            continue;
        }
        const projected = loopPoints3D.map((point) => {
            const offset = point.clone().sub(outerProjection.origin);
            return new THREE.Vector2(
                offset.dot(outerProjection.xAxis),
                offset.dot(outerProjection.yAxis)
            );
        });
        if (signedArea(projected) > 0) {
            projected.reverse();
            loopPoints3D.reverse();
        }
        holes2D.push(projected);
        holes3D.push(loopPoints3D);
    }

    const allVertices3D = contour3D.concat(...holes3D);
    const triangles = THREE.ShapeUtils.triangulateShape(contour2D, holes2D);
    if (triangles.length === 0) {
        return null;
    }

    const positions = [];
    for (const triangle of triangles) {
        for (const index of triangle) {
            const point = allVertices3D[index];
            positions.push(point.x, point.y, point.z);
        }
    }

    const geometry = new THREE.BufferGeometry();
    geometry.setAttribute('position', new THREE.Float32BufferAttribute(positions, 3));
    geometry.computeVertexNormals();

    const material = new THREE.MeshStandardMaterial({
        color: 0xc87a52,
        transparent: true,
        opacity: 0.62,
        side: THREE.DoubleSide,
        roughness: 0.48,
        metalness: 0.08
    });

    return new THREE.Mesh(geometry, material);
}

function buildEdgeObject(edge, edgeIndex) {
    const geometry = new THREE.BufferGeometry();
    geometry.setAttribute('position', new THREE.Float32BufferAttribute(edge.points.flat(), 3));
    const material = new THREE.LineBasicMaterial({ color: 0x1b2d33 });
    const line = new THREE.Line(geometry, material);
    line.userData.selection = [
        ['类型', `边 #${edgeIndex + 1}`],
        ['采样点', String(edge.points.length)],
        ['线段数', String(Math.max(0, edge.points.length - 1))],
        ['起点', formatPoint(edge.points[0])],
        ['终点', formatPoint(edge.points[edge.points.length - 1])]
    ];
    line.userData.baseColor = 0x1b2d33;
    line.userData.selectedColor = 0xf06d3a;
    return line;
}

function formatPoint(point) {
    return point.map((value) => Number(value).toFixed(3)).join(', ');
}

function applySelectionStyle(object, selected) {
    const color = selected ? object.userData.selectedColor : object.userData.baseColor;
    object.material.color.setHex(color);
    if (object.isMesh) {
        object.material.opacity = selected ? 0.9 : 0.62;
    }
}

function fitCamera(bounds) {
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
}

function renderPreview(preview) {
    clearModel();

    for (let index = 0; index < preview.faces.length; index += 1) {
        const face = preview.faces[index];
        const mesh = buildFaceMesh(face);
        if (mesh) {
            const innerLoopCount = face.loops.filter((loop) => !loop.outer).length;
            const outerLoop = face.loops.find((loop) => loop.outer);
            mesh.userData.selection = [
                ['类型', `面 #${index + 1}`],
                ['边界环', String(face.loops.length)],
                ['内环', String(innerLoopCount)],
                ['外环采样点', String(outerLoop ? outerLoop.points.length : 0)],
                ['法向', formatPoint(face.normal)]
            ];
            mesh.userData.baseColor = 0xc87a52;
            mesh.userData.selectedColor = 0xf0b15a;
            interactiveObjects.push(mesh);
            modelRoot.add(mesh);
        }
    }

    for (let index = 0; index < preview.edges.length; index += 1) {
        const line = buildEdgeObject(preview.edges[index], index);
        interactiveObjects.push(line);
        modelRoot.add(line);
    }

    updateStats(preview.stats);
    fitCamera(preview.bounds);
    resetSelection();
}

async function requestPreview(stepText) {
    const response = await fetch('/api/preview', {
        method: 'POST',
        headers: {
            'Content-Type': 'text/plain; charset=utf-8'
        },
        body: stepText
    });

    const text = await response.text();
    if (!response.ok) {
        try {
            const errorPayload = JSON.parse(text);
            throw new Error(errorPayload.error || 'STEP 解析失败');
        } catch (error) {
            if (error instanceof SyntaxError) {
                throw new Error(text || 'STEP 解析失败');
            }
            throw error;
        }
    }

    return JSON.parse(text);
}

async function renderCurrentInput() {
    const stepText = stepInput.value.trim();
    if (!stepText) {
        setStatus('请先提供 STEP 内容。');
        updateStats();
        clearModel();
        return;
    }

    setStatus('正在解析 STEP 并生成预览...');
    try {
        const preview = await requestPreview(stepText);
        renderPreview(preview);
        const unsupported = preview.stats.unsupportedFaceCount ?? 0;
        const suffix = unsupported > 0 ? `，跳过 ${unsupported} 个暂不支持的面。` : '。';
        setStatus(`渲染完成：${preview.stats.faceCount} 个面，${preview.stats.edgeCount} 条边${suffix}`);
    } catch (error) {
        clearModel();
        updateStats();
        setStatus(error.message);
    }
}

renderButton.addEventListener('click', () => {
    void renderCurrentInput();
});

loadExampleButton.addEventListener('click', async () => {
    setStatus('正在加载示例...');
    try {
        const response = await fetch(`/api/example?name=${encodeURIComponent(exampleSelect.value)}`);
        if (!response.ok) {
            throw new Error('示例文件不可用');
        }
        stepInput.value = await response.text();
        setStatus(`示例 ${exampleSelect.value} 已加载，可以直接渲染。`);
    } catch (error) {
        setStatus(error.message);
    }
});

fileInput.addEventListener('change', async (event) => {
    const [file] = event.target.files;
    if (!file) {
        return;
    }
    stepInput.value = await file.text();
    setStatus(`已载入文件：${file.name}`);
});

renderer.domElement.addEventListener('click', (event) => {
    if (interactiveObjects.length === 0) {
        return;
    }

    const rect = renderer.domElement.getBoundingClientRect();
    pointer.x = ((event.clientX - rect.left) / rect.width) * 2 - 1;
    pointer.y = -((event.clientY - rect.top) / rect.height) * 2 + 1;
    raycaster.setFromCamera(pointer, camera);

    const hits = raycaster.intersectObjects(interactiveObjects, false);
    if (selectedObject) {
        applySelectionStyle(selectedObject, false);
        selectedObject = null;
    }

    if (hits.length === 0) {
        setSelection([
            ['类型', '未选中'],
            ['说明', '点击右侧模型中的面或边查看详情。']
        ]);
        return;
    }

    selectedObject = hits[0].object;
    applySelectionStyle(selectedObject, true);
    setSelection(selectedObject.userData.selection);
});
