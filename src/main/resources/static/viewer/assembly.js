/** Assembly tree rendering and per-instance visibility. */

import * as THREE from 'three';
import { viewState } from './state.js';
import { boxToLog } from './format.js';
import { logJson } from './log.js';
import { camera, controls, requestRender } from './scene.js';
import { refreshRenderableStyle } from './selection.js';
import { setSelection } from './ui-panels.js';
export const assemblyTree = document.querySelector('#assembly-tree');

export const isolateSelectionButton = document.querySelector('#isolate-selection');
export const showAllButton = document.querySelector('#show-all');

export let selectedAssemblyButton = null;
export let selectedAssemblyGroup = null;

export const assemblyGroups = new Map();
export const assemblyButtons = new Map();

export function renderAssemblyTree(instances) {
    assemblyTree.innerHTML = '';
    assemblyButtons.clear();
    if (!Array.isArray(instances) || instances.length === 0) {
        const item = document.createElement('li');
        item.innerHTML = '<button type="button" disabled><span class="assembly-item-label">无装配实例</span><span class="assembly-item-meta">当前预览未导出实例树。</span></button>';
        assemblyTree.appendChild(item);
        return;
    }

    const childrenByParent = new Map();
    for (const instance of instances) {
        const key = instance.parentId ?? '__root__';
        if (!childrenByParent.has(key)) {
            childrenByParent.set(key, []);
        }
        childrenByParent.get(key).push(instance);
    }

    const appendItems = (parentId, depth) => {
        const items = childrenByParent.get(parentId ?? '__root__') ?? [];
        for (const instance of items) {
            const item = document.createElement('li');
            item.style.paddingLeft = `${depth * 0.9}rem`;
            const button = document.createElement('button');
            button.type = 'button';
            button.dataset.instanceId = instance.id;
            assemblyButtons.set(instance.id, button);
            button.title = instance.description || instance.label || instance.id;
            button.innerHTML = `<span class="assembly-item-label">${instance.label || instance.id}</span>
                <span class="assembly-item-meta">pd #${instance.productDefinitionId}${instance.occurrenceId ? ` / occ #${instance.occurrenceId}` : ''}${Array.isArray(instance.representationIds) && instance.representationIds.length > 0 ? ` / rep ${instance.representationIds.map((id) => `#${id}`).join(', ')}` : ''}</span>`;
            button.addEventListener('click', () => focusAssemblyInstance(instance.id, button));
            item.appendChild(button);
            assemblyTree.appendChild(item);
            appendItems(instance.id, depth + 1);
        }
    };

    appendItems(null, 0);
}

export function focusAssemblyInstance(instanceId, button = null) {
    const group = assemblyGroups.get(instanceId);
    if (!group) {
        logJson('focusAssemblyInstance:missing-group', { instanceId });
        return;
    }

    logJson('focusAssemblyInstance:start', {
        instanceId,
        triggeredByButton: Boolean(button),
        cameraPosition: camera.position.toArray(),
        controlsTarget: controls.target.toArray(),
        selectedObjectStepId: viewState.selectedObject?.userData?.stepId ?? null
    });

    if (viewState.selectedObject) {
        viewState.selectedObject.userData.objectSelected = false;
        refreshRenderableStyle(viewState.selectedObject);
        viewState.selectedObject = null;
    }
    activateAssemblyInstance(group, button ?? assemblyButtons.get(instanceId) ?? null);

    const box = new THREE.Box3().setFromObject(group);
    if (box.isEmpty()) {
        return;
    }
    const center = box.getCenter(new THREE.Vector3());
    const size = box.getSize(new THREE.Vector3());
    const radius = Math.max(size.length() * 0.7, 1);

    controls.target.copy(center);
    camera.position.copy(center.clone().add(new THREE.Vector3(radius, radius * 0.75, radius)));
    camera.near = Math.max(radius / 200, 0.01);
    camera.far = Math.max(radius * 40, 100);
    camera.updateProjectionMatrix();
    controls.update();

    logJson('focusAssemblyInstance:done', {
        instanceId,
        triggeredByButton: Boolean(button),
        bounds: boxToLog(box),
        center: center.toArray(),
        radius,
        cameraPosition: camera.position.toArray(),
        controlsTarget: controls.target.toArray()
    });

    setSelection([
        ['类型', '装配实例'],
        ['实例', group.userData.instanceLabel || instanceId],
        ['描述', group.userData.instanceDescription || ''],
        ['层级', String(group.userData.instanceDepth ?? 0)],
        ['表示', String(group.userData.representationCount ?? 0)],
        ['说明', '已定位并高亮该实例。']
    ]);
}

export function activateAssemblyInstance(group, button = null) {
    if (!group) {
        return;
    }
    if (selectedAssemblyGroup && selectedAssemblyGroup !== group) {
        applyAssemblyHighlight(selectedAssemblyGroup, false);
    }
    selectedAssemblyGroup = group;
    applyAssemblyHighlight(group, true);

    if (selectedAssemblyButton) {
        selectedAssemblyButton.classList.remove('active');
    }
    selectedAssemblyButton = button ?? assemblyButtons.get(group.userData.instanceId) ?? null;
    if (selectedAssemblyButton) {
        selectedAssemblyButton.classList.add('active');
    }
}

export function setGroupVisibility(group, visible) {
    group.visible = visible;
    requestRender();
}

export function showOnlyInstance(instanceId) {
    if (assemblyGroups.size === 0) {
        return;
    }
    for (const [id, group] of assemblyGroups.entries()) {
        setGroupVisibility(group, id === instanceId);
    }
}

export function showAllInstances() {
    if (assemblyGroups.size > 0) {
        for (const group of assemblyGroups.values()) {
            setGroupVisibility(group, true);
        }
    }
    for (const object of viewState.interactiveObjects) {
        object.visible = true;
    }
}

export function applyAssemblyHighlight(group, selected) {
    group.traverse((node) => {
        if (!node.material) {
            return;
        }
        node.userData.instanceHighlighted = selected;
        refreshRenderableStyle(node);
    });
}
