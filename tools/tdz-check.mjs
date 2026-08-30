/**
 * tdz-check.mjs — evaluate the viewer ES-module graph in Node with `three` and
 * DOM stubbed, to surface any circular-dependency TDZ (temporal dead zone)
 * errors that occur during module *evaluation* (top-level code reading a
 * not-yet-initialized const/let binding pulled across a circular import).
 *
 * It does NOT verify runtime behaviour — only that the module graph links and
 * evaluates without a "Cannot access 'X' before initialization" ReferenceError.
 *
 * Run: node tools/tdz-check.mjs
 */
import { mkdirSync, rmSync, copyFileSync, readFileSync, writeFileSync, existsSync } from 'node:fs';
import { dirname, join, resolve } from 'node:path';
import { fileURLToPath, pathToFileURL } from 'node:url';

const __dirname = dirname(fileURLToPath(import.meta.url));
const SRC = resolve(__dirname, '../src/main/resources/static/viewer');
const OUT = resolve(__dirname, '../target/viewer-tdz-' + Date.now());

const DEEP_STUB_SRC = `
function makeDeepStub() {
  const fn = function () { return makeDeepStub(); };
  return new Proxy(fn, {
    get(_t, prop) {
      if (prop === Symbol.toPrimitive) return () => 0;
      if (prop === Symbol.iterator) return undefined;
      if (prop === 'then') return undefined;
      if (prop === 'length') return 0;
      if (prop === 'toArray') return () => [];
      if (prop === 'classList' || prop === 'style' || prop === 'userData') return makeDeepStub();
      return makeDeepStub();
    },
    set() { return true; },
    apply() { return makeDeepStub(); },
    construct() { return makeDeepStub(); }
  });
}
`;

mkdirSync(join(OUT, 'viewer'), { recursive: true });

// three stub: CJS default export = Proxy so `import THREE from` then `THREE.X` works.
writeFileSync(
  join(OUT, 'viewer', '__three_stub.cjs'),
  DEEP_STUB_SRC + '\nmodule.exports = makeDeepStub();\n'
);
// three/addons stub: static named exports (cjs-module-lexer detects exports.X =).
writeFileSync(
  join(OUT, 'viewer', '__three_addons_stub.cjs'),
  DEEP_STUB_SRC +
    '\nexports.OrbitControls = makeDeepStub();\nexports.GLTFLoader = makeDeepStub();\n'
);

const STAR_THREE = /import\s+\*\s+as\s+THREE\s+from\s+['"]three['"];/g;
const ADDONS = /from\s+['"]three\/addons\/[^'"]+['"]/g;

const files = [
  'state.js', 'log.js', 'format.js', 'matrix.js', 'bspline.js', 'pmi.js',
  'scene.js', 'selection.js', 'assembly.js', 'ui-panels.js',
  'parametric-geometry.js', 'model-io.js'
];

for (const f of files) {
  const src = readFileSync(join(SRC, f), 'utf8');
  const out = src
    .replace(STAR_THREE, "import THREE from './__three_stub.cjs';")
    .replace(ADDONS, "from './__three_addons_stub.cjs'");
  writeFileSync(join(OUT, 'viewer', f), out);
}

// Harness entry: set globals first, then dynamically import (imports are
// hoisted, so we must use dynamic import to guarantee globals exist at eval).
const harness = `
function elt() {
  const e = function () { return e; };
  return new Proxy(e, {
    get(_t, p) {
      if (p === 'classList' || p === 'style' || p === 'dataset' || p === 'userData') return e;
      if (['addEventListener','removeEventListener','appendChild','setAttribute','getAttribute','remove','focus','click','blur','dispatchEvent'].includes(p)) return () => {};
      if (p === 'querySelector') return elt;
      if (p === 'querySelectorAll') return () => [];
      return e;
    },
    set() { return true; },
    apply() { return e; }
  });
}
function setGlobal(name, val) {
  try { Object.defineProperty(globalThis, name, { value: val, writable: true, configurable: true }); }
  catch { try { globalThis[name] = val; } catch {} }
}
setGlobal('window', { devicePixelRatio: 1, addEventListener(){}, removeEventListener(){}, requestAnimationFrame(){}, innerWidth: 800, innerHeight: 600 });
setGlobal('document', { querySelector: elt, querySelectorAll: () => [], getElementById: elt, getElementsByClassName: () => [], createElement: elt, addEventListener(){}, body: elt(), documentElement: elt() });
setGlobal('navigator', { userAgent: 'node' });
setGlobal('localStorage', { getItem(){return null;}, setItem(){}, removeItem(){} });
setGlobal('fetch', () => Promise.resolve({ json: () => Promise.resolve({}), text: () => Promise.resolve('') }));
setGlobal('requestAnimationFrame', () => 0);
setGlobal('getComputedStyle', () => ({}));
setGlobal('ResizeObserver', class { observe() {} unobserve() {} disconnect() {} });

const modules = ['state.js','log.js','format.js','matrix.js','bspline.js','pmi.js','scene.js','selection.js','assembly.js','ui-panels.js','parametric-geometry.js','model-io.js'];
let ok = 0;
for (const m of modules) {
  try {
    await import('./viewer/' + m);
    ok++;
  } catch (e) {
    const isTdz = e instanceof ReferenceError && /before initialization/.test(e.message);
    console.error('FAIL ' + m + (isTdz ? ' [TDZ] ' : ' ') + e.message);
    if (isTdz) { console.error(e.stack); process.exitCode = 2; }
    else { console.error('(non-TDZ runtime error — likely stub artifact, not a module-graph fault)'); }
  }
}
if (process.exitCode !== 2) console.log('OK: ' + ok + '/' + modules.length + ' modules evaluated without TDZ');
process.exit(process.exitCode === 2 ? 2 : 0);
`;
writeFileSync(join(OUT, 'harness.mjs'), harness);

const node = process.execPath;
const { spawnSync } = await import('node:child_process');
const r = spawnSync(node, [join(OUT, 'harness.mjs')], { encoding: 'utf8' });
if (r.stdout) process.stdout.write(r.stdout);
if (r.stderr) process.stderr.write(r.stderr);
process.exit(r.status ?? 0);
