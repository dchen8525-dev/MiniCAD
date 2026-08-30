/** PMI labels, their screen overlay and visibility. */

import * as THREE from 'three';
import { viewState } from './state.js';
import { assemblyButtons, assemblyGroups, focusAssemblyInstance } from './assembly.js';
import { camera, pmiRoot, requestRender, sceneHost } from './scene.js';
import { pointer, selectRenderable, stepObjects } from './selection.js';
import { setSelection } from './ui-panels.js';
export const pmiOverlay = document.querySelector('#pmi-overlay');

export const togglePmiButton = document.querySelector('#toggle-pmi');

export function renderPmi(pmi) {
    if (!Array.isArray(pmi) || pmi.length === 0) {
        return;
    }
    for (const item of pmi) {
        if (Array.isArray(item.leader) && item.leader.length >= 2) {
            const geometry = new THREE.BufferGeometry();
            geometry.setAttribute('position', new THREE.Float32BufferAttribute(item.leader.flat(), 3));
            const material = new THREE.LineBasicMaterial({ color: 0x4a423d });
            pmiRoot.add(new THREE.Line(geometry, material));
        }
        const label = document.createElement('div');
        label.className = 'pmi-label';
        label.textContent = item.text || item.name || 'PMI';
        const targets = Array.isArray(item.targets) ? item.targets : [];
        if ((Array.isArray(item.targetIds) && item.targetIds.length > 0) || targets.length > 0) {
            label.style.cursor = 'pointer';
            const displayTargets = targets.length > 0 ? targets.map((target) => `#${target.id} ${target.type}`) : item.targetIds.map((id) => `#${id}`);
            label.title = `关联 STEP 项: ${displayTargets.join(', ')}`;
            label.addEventListener('click', () => selectPmiTargets(targets, item.targetIds));
        }
        pmiOverlay.appendChild(label);
        viewState.pmiLabels.push({
            element: label,
            anchor: new THREE.Vector3(item.position[0], item.position[1], item.position[2]),
            targetIds: Array.isArray(item.targetIds) ? item.targetIds : [],
            targets
        });
    }
    applyPmiVisibility();
}

export function updatePmiOverlay() {
    if (viewState.pmiLabels.length === 0) {
        return;
    }
    const width = sceneHost.clientWidth;
    const height = sceneHost.clientHeight;
    for (const label of viewState.pmiLabels) {
        const screen = label.anchor.clone().project(camera);
        if (screen.z < -1 || screen.z > 1) {
            label.element.style.display = 'none';
            continue;
        }
        label.element.style.display = viewState.pmiVisible ? '' : 'none';
        label.element.style.left = `${(screen.x * 0.5 + 0.5) * width}px`;
        label.element.style.top = `${(-screen.y * 0.5 + 0.5) * height}px`;
    }
}

export function applyPmiVisibility() {
    pmiOverlay.style.display = viewState.pmiVisible ? '' : 'none';
    pmiRoot.visible = viewState.pmiVisible;
    if (togglePmiButton) {
        togglePmiButton.textContent = viewState.pmiVisible ? '隐藏 PMI' : '显示 PMI';
    }
    requestRender();
}

export function syncPmiTargetHighlight() {
    const selectedIds = new Set();
    if (viewState.selectedObject?.userData?.stepId != null) {
        selectedIds.add(viewState.selectedObject.userData.stepId);
    }
    for (const label of viewState.pmiLabels) {
        if (selectedIds.size === 0 || label.targetIds.length === 0) {
            label.element.classList.remove('dimmed');
            continue;
        }
        const matches = label.targetIds.some((id) => selectedIds.has(id));
        label.element.classList.toggle('dimmed', !matches);
    }
}

export function selectPmiTargets(targets, targetIds = []) {
    const effectiveTargets = Array.isArray(targets) ? targets : [];
    const effectiveIds = effectiveTargets.length > 0
        ? effectiveTargets.map((target) => target.id)
        : (Array.isArray(targetIds) ? targetIds : []);
    const instanceIds = effectiveTargets.flatMap((target) => Array.isArray(target.instanceIds) ? target.instanceIds : []);
    for (const instanceId of instanceIds) {
        if (assemblyGroups.has(instanceId)) {
            focusAssemblyInstance(instanceId, assemblyButtons.get(instanceId));
            break;
        }
    }
    if (effectiveIds.length === 0) {
        return;
    }
    for (const targetId of effectiveIds) {
        const matches = stepObjects.get(targetId);
        if (Array.isArray(matches) && matches.length > 0) {
            selectRenderable(matches[0]);
            return;
        }
    }
    const targetSummary = effectiveTargets.length > 0
        ? effectiveTargets.map((target) => `#${target.id} ${target.type}${target.name ? ` (${target.name})` : ''}`).join(', ')
        : effectiveIds.map((id) => `#${id}`).join(', ');
    setSelection([
        ['类型', 'PMI'],
        ['目标', targetSummary],
        ['实例', instanceIds.length > 0 ? instanceIds.join(', ') : '无实例映射'],
        ['说明', '已解析 semantic PMI 关联，但当前视图中没有可直接选中的对象。']
    ]);
}
