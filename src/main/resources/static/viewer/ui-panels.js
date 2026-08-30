/** Status, statistics, validation, product and units panels. */

import { viewState } from './state.js';
import { applyAssemblyHighlight, selectedAssemblyButton, selectedAssemblyGroup } from './assembly.js';
import { escHtml, formatMetric, formatPoint, setTextAndShow, summarizeEntries, summarizeUnsupportedBooleans, summarizeUnsupportedFaces } from './format.js';
import { syncPmiTargetHighlight } from './pmi.js';
import { requestRender } from './scene.js';
import { refreshRenderableStyle, toggleEdgesButton } from './selection.js';
export const statusText = document.querySelector('#status-text');
export const validationDetails = document.querySelector('#validation-details');
export const validationReport = document.querySelector('#validation-report');
export const unsupportedFacesList = document.querySelector('#unsupported-faces');
export const unsupportedBooleansList = document.querySelector('#unsupported-booleans');
export const toggleUnsupportedViewButton = document.querySelector('#toggle-unsupported-view');
export const selectionDetails = document.querySelector('#selection-details');

export const statElements = new Map(
    Array.from(document.querySelectorAll('[data-stat]')).map((element) => [element.dataset.stat, element])
);

export let unsupportedViewMode = 'details';
export let currentUnsupportedFaces = [];
export let currentUnsupportedBooleans = [];

if (toggleUnsupportedViewButton) {
    // Set the initial label directly rather than via applyUnsupportedViewMode():
    // the latter calls requestRender() (from scene.js), and this module can be
    // evaluated before scene.js finishes initialising its render-scheduling state
    // during the circular scene <-> ui-panels import, which would throw a TDZ
    // ReferenceError on load. The unsupported lists are empty at boot, so the
    // full initialisation is deferred to the first user interaction / model load.
    toggleUnsupportedViewButton.textContent = unsupportedViewMode === 'summary' ? '展开详情' : '只看汇总';
    toggleUnsupportedViewButton.addEventListener('click', () => {
        unsupportedViewMode = unsupportedViewMode === 'summary' ? 'details' : 'summary';
        applyUnsupportedViewMode();
    });
}

export let selectedUnsupportedButton = null;

export function setStatus(text) {
    statusText.textContent = text;
}

export function updateStats(stats = {}) {
    for (const [key, element] of statElements.entries()) {
        element.textContent = stats[key] ?? 0;
    }
}

export function updateValidation(validation = {}) {
    const center = Array.isArray(validation.center) ? formatPoint(validation.center) : '0.000, 0.000, 0.000';
    const checks = Array.isArray(validation.report?.checks)
        ? validation.report.checks
        : (Array.isArray(validation.nativeChecks) ? validation.nativeChecks : []);
    const nativeChecks = checks.length > 0
        ? checks.map((check) => `${check.name}: ${check.matches ? 'OK' : `差异 ${formatMetric(check.delta)}`}`).join(' | ')
        : '无';
    const reportStatus = validation.report?.status ?? 'empty';
    validationDetails.innerHTML = [
        ['面', String(validation.renderedFaceCount ?? 0)],
        ['边', String(validation.renderedEdgeCount ?? 0)],
        ['面积', formatMetric(validation.approxSurfaceArea)],
        ['线长', formatMetric(validation.approxEdgeLength)],
        ['表示', String(validation.representationCount ?? 0)],
        ['实例', String(validation.instanceCount ?? 0)],
        ['中心', center],
        ['状态', reportStatus],
        ['校验', nativeChecks]
    ].map(([label, value]) => `<dt>${label}</dt><dd>${value}</dd>`).join('');
    updateValidationReport(validation.report ?? validation.nativeChecks);
}

export function updateProduct(product = {}) {
    const card = document.getElementById('product-card');
    const nameEl = document.getElementById('product-name');
    const descEl = document.getElementById('product-desc');
    const idEl = document.getElementById('product-identifier');
    const schemaEl = document.getElementById('product-schema');
    const compEl = document.getElementById('product-components');

    const productName = product.productName;
    const productDesc = product.productDescription;
    const productId = product.productIdentifier;
    const fileName = product.fileName;
    const schemas = Array.isArray(product.schemas) ? product.schemas : [];
    const components = Array.isArray(product.components) ? product.components : [];

    if (!productName && !productDesc && !productId && !fileName && schemas.length === 0 && components.length === 0) {
        card.style.display = 'none';
        return;
    }

    card.style.display = '';
    setTextAndShow(nameEl, productName || fileName || '');
    setTextAndShow(descEl, productDesc || '');
    setTextAndShow(idEl, productId ? `ID: ${productId}` : '');
    setTextAndShow(schemaEl, schemas.length > 0 ? schemas.join(', ') : '');
    compEl.innerHTML = components.map((c) => {
        const label = c.name || c.identifier || 'Component';
        return `<li class="component-item"><strong>${escHtml(label)}</strong>${c.description ? `<br><span style="color:var(--muted);font-size:0.8rem">${escHtml(c.description)}</span>` : ''}</li>`;
    }).join('');
}

export function updateUnits(units = {}) {
    const card = document.getElementById('units-card');
    const unitEl = document.getElementById('unit-value');
    const scaleEl = document.getElementById('unit-scale');
    const angleEl = document.getElementById('unit-angle');

    const lengthUnit = units.lengthUnit;
    const scaleToMeters = units.scaleToMeters;
    const angleUnit = units.angleUnit;

    if (!lengthUnit && !angleUnit) {
        card.style.display = 'none';
        return;
    }

    card.style.display = '';
    setTextAndShow(unitEl, lengthUnit || '未指定');
    setTextAndShow(scaleEl, scaleToMeters != null ? `1 单位 = ${scaleToMeters} 米` : '');
    setTextAndShow(angleEl, angleUnit ? `角度: ${angleUnit}` : '');
}

export function updateValidationReport(report = {}) {
    const checks = Array.isArray(report.checks) ? report.checks : (Array.isArray(report) ? report : []);
    const okCount = Number(report.okCount ?? checks.filter((check) => check.matches).length);
    const warnCount = Number(report.warnCount ?? checks.filter((check) => !check.matches).length);
    if (checks.length === 0) {
        validationReport.innerHTML = '<li><strong>无 native validation</strong><span>当前 STEP 未导出可对比的原生校验项。</span></li>';
        return;
    }
    const summary = `<li><strong>汇总</strong><span>OK ${okCount} 项 / Warn ${warnCount} 项</span></li>`;
    validationReport.innerHTML = summary + checks.map((check) => {
        const cssClass = check.matches ? 'ok' : 'warn';
        const detail = `${check.measureType}: 期望 ${formatMetric(check.expected)} / 实际 ${formatMetric(check.actual)} / 差值 ${formatMetric(check.delta)}`;
        return `<li class="${cssClass}"><strong>${check.name}</strong><span>${detail}</span></li>`;
    }).join('');
}

export function updateUnsupportedFaces(unsupportedFaces = []) {
    currentUnsupportedFaces = Array.isArray(unsupportedFaces) ? unsupportedFaces : [];
    if (!Array.isArray(unsupportedFaces) || unsupportedFaces.length === 0) {
        unsupportedFacesList.innerHTML = '<li><button type="button" disabled><strong>无</strong><span>当前预览没有被跳过的面。</span></button></li>';
        return;
    }
    unsupportedFacesList.innerHTML = '';
    const summary = summarizeUnsupportedFaces(unsupportedFaces);
    const summaryItem = document.createElement('li');
    summaryItem.className = 'summary';
    summaryItem.innerHTML = `<button type="button" disabled><strong>汇总</strong><span>${summarizeEntries(summary.bySurfaceType)}</span><span>${summarizeEntries(summary.byReason)}</span></button>`;
    unsupportedFacesList.appendChild(summaryItem);
    if (unsupportedViewMode === 'summary') {
        return;
    }
    for (const face of unsupportedFaces) {
        const item = document.createElement('li');
        const button = document.createElement('button');
        button.type = 'button';
        const name = face.name || `FACE #${face.id}`;
        const surfaceType = face.surfaceType || 'UNKNOWN';
        const reason = face.reason || '当前导出器已识别该面，但本轮预览仍将其跳过。';
        button.innerHTML = `<strong>#${face.id} ${name}</strong><span>${surfaceType}</span><span>${reason}</span>`;
        button.addEventListener('click', () => selectUnsupportedFace(face, button));
        item.appendChild(button);
        unsupportedFacesList.appendChild(item);
    }
}

export function updateUnsupportedBooleans(unsupportedBooleans = []) {
    currentUnsupportedBooleans = Array.isArray(unsupportedBooleans) ? unsupportedBooleans : [];
    if (!Array.isArray(unsupportedBooleans) || unsupportedBooleans.length === 0) {
        unsupportedBooleansList.innerHTML = '<li><button type="button" disabled><strong>无</strong><span>当前预览没有被跳过的布尔结果。</span></button></li>';
        return;
    }
    unsupportedBooleansList.innerHTML = '';
    const summary = summarizeUnsupportedBooleans(unsupportedBooleans);
    const summaryItem = document.createElement('li');
    summaryItem.className = 'summary';
    summaryItem.innerHTML = `<button type="button" disabled><strong>汇总</strong><span>${summarizeEntries(summary.byType)}</span><span>${summarizeEntries(summary.byReason)}</span></button>`;
    unsupportedBooleansList.appendChild(summaryItem);
    if (unsupportedViewMode === 'summary') {
        return;
    }
    for (const itemData of unsupportedBooleans) {
        const item = document.createElement('li');
        const button = document.createElement('button');
        button.type = 'button';
        button.disabled = true;
        const name = itemData.name || `${itemData.type || 'BOOLEAN'} #${itemData.id}`;
        const type = itemData.type || 'UNKNOWN';
        const reason = itemData.reason || '当前导出器已识别该布尔结果，但本轮预览仍将其跳过。';
        button.innerHTML = `<strong>#${itemData.id} ${name}</strong><span>${type}</span><span>${reason}</span>`;
        item.appendChild(button);
        unsupportedBooleansList.appendChild(item);
    }
}

export function applyUnsupportedViewMode() {
    if (toggleUnsupportedViewButton) {
        toggleUnsupportedViewButton.textContent = unsupportedViewMode === 'summary' ? '展开详情' : '只看汇总';
    }
    updateUnsupportedFaces(currentUnsupportedFaces);
    updateUnsupportedBooleans(currentUnsupportedBooleans);
    requestRender();
}

export function setSelection(entries) {
    selectionDetails.innerHTML = entries.map(([label, value]) => `<dt>${label}</dt><dd>${value}</dd>`).join('');
}

export function resetSelection() {
    if (viewState.selectedObject) {
        viewState.selectedObject.userData.objectSelected = false;
        refreshRenderableStyle(viewState.selectedObject);
        viewState.selectedObject = null;
    }
    if (selectedAssemblyGroup) {
        applyAssemblyHighlight(selectedAssemblyGroup, false);
        selectedAssemblyGroup = null;
    }
    if (selectedAssemblyButton) {
        selectedAssemblyButton.classList.remove('active');
        selectedAssemblyButton = null;
    }
    if (selectedUnsupportedButton) {
        selectedUnsupportedButton.classList.remove('active');
        selectedUnsupportedButton = null;
    }
    setSelection([
        ['类型', '未选中'],
        ['说明', '点击右侧模型中的面或边查看详情。']
    ]);
    syncPmiTargetHighlight();
}

export function selectUnsupportedFace(face, button) {
    if (viewState.selectedObject) {
        viewState.selectedObject.userData.objectSelected = false;
        refreshRenderableStyle(viewState.selectedObject);
        viewState.selectedObject = null;
    }
    if (selectedAssemblyGroup) {
        applyAssemblyHighlight(selectedAssemblyGroup, false);
        selectedAssemblyGroup = null;
    }
    if (selectedAssemblyButton) {
        selectedAssemblyButton.classList.remove('active');
        selectedAssemblyButton = null;
    }
    if (selectedUnsupportedButton) {
        selectedUnsupportedButton.classList.remove('active');
    }
    selectedUnsupportedButton = button;
    selectedUnsupportedButton.classList.add('active');
    setSelection([
        ['类型', '未支持面'],
        ['STEP', `#${face.id}`],
        ['名称', face.name || ''],
        ['曲面', face.surfaceType || 'UNKNOWN'],
        ['说明', face.reason || '当前导出器已识别该面，但本轮预览仍将其跳过。']
    ]);
    syncPmiTargetHighlight();
}

export function updateEdgeToggleButton() {
    if (!toggleEdgesButton) {
        return;
    }
    toggleEdgesButton.disabled = !viewState.modelHasEdgeLines;
    toggleEdgesButton.textContent = viewState.edgeLinesVisible ? '隐藏边线' : '显示边线';
}
