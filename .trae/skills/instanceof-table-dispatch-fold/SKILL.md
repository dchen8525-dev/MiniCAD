---
name: "instanceof-table-dispatch-fold"
description: "把 MiniCAD 里长的 instanceof 顺序分发链折叠为 (type, guard, handler) 表驱动分发，并补齐冻结顺序守卫测试与门禁；同时覆盖「跨类重复实现收敛 + 防回潮守卫」（scan_duplicate_methods、抽取 helper、一行委托）。当用户要求继续/执行 instanceof 链折叠、表驱动重构、重复实现收敛，或提到 scan_instanceof_chains、scan_duplicate_methods、dispatch table、折叠某方法时使用。"
---

# instanceof 链表驱动折叠（MiniCAD）

把 `if (x instanceof T) { ... }` 的长顺序分发链折叠成有序 `(type, guard, handler)`
规则表，用首命中循环替换，并通过"冻结顺序 + 反射守卫 + 行为测试 + 全量门禁"保证
行为不变。这是 MiniCAD 的主线重构目标（见 project memory）。

## 何时用

- 用户说"继续执行任务/下一项工作"且上下文是 instanceof 折叠。
- 用户点名折叠某方法、或提到 scan/dispatch table/表驱动。
- 扫描器报告某条 ≥5 分支的链值得折。
- 用户要求"重复实现收敛 / 收敛重复 + 守卫"，或 `scan_instanceof_chains.py --min 5` 已为 0
  （干净同构链清完，主线转入收敛，见下文「重复实现收敛」章节）。

## 工作流（严格按序）

### 1. 选目标
```
py tools/scan_instanceof_chains.py --min 6            # 全部；--clean 只看无 guard 的
```
- 输出按分支数降序：`branches guarded location`。
- `guarded>0` 表示有 `&&`/`||` 复合条件，需 Predicate guard 或 OR 拆分（见下）。
- 已知局限：扫描器按"相邻性"计数，单行 `if (...) return ...;` 会被拆开，可能低估。
- 优先挑**同构**链（各分支体形态一致，如都 cast+比较、都 cast+return 常量），收益最高。

### 2. 核实类型层级（硬性前置）
折叠前必须确认链里每个 `T` 都是 **final 且直接 implements 分发基类型**
（StepEntity / SurfaceGeometry / Curve2 等），无父子关系：
```
Grep: public final class T\b        (src/main/java/.../<pkg>)
Grep: extends T\b|implements T\b    (确认无人继承它)
```
- 若类型间有子类型关系，顺序就是行为，务必逐字保留原顺序并在注释里写明。
- 若发现两条链**逐字相同仅参数来源不同**（如 offset 链），这是去重+折叠的双重机会：
  收敛成一个共享表 + 一个带参数的入口方法。

### 3. 折叠
沿用本仓库既有形态（先 `Grep` 同目录已有的 `*Rule`/`*RULES` 抄约定，别自创）。通用骨架：
```java
@FunctionalInterface
private interface XxxHandler { R apply(StepEntity entity); }        // 需要上下文时入参加 builder/acc

private record XxxRule(Class<? extends StepEntity> type, XxxHandler handler) {
    boolean matches(StepEntity entity) { return type.isInstance(entity); }
}
private static XxxRule xxxRule(Class<? extends StepEntity> t, XxxHandler h) { return new XxxRule(t, h); }

private static final List<XxxRule> XXX_RULES = List.of(
    xxxRule(A.class, (e) -> /* 原分支体，逐字搬，含 cast */),
    ...
);

static R entry(StepEntity entity) {
    for (XxxRule rule : XXX_RULES) {
        if (rule.matches(entity)) return rule.handler().apply(entity);
    }
    return /* 原链尾终态：null / 反射尾 / 类名尾 —— 原样保留 */;
}
```
铁律：
- **分支体逐字搬运**，不改逻辑、不顺手"优化"。链尾的回退（`return null`、反射
  `entityName()`、类名转换、通用包装）必须原样留在循环之后。
- **顺序 = 原链顺序**（首命中）。OR-guard 分支（`A instanceof X || A instanceof Y`）
  拆成一型一规则、共享同一 handler，保持原相对顺序。
- 复合 guard（如 `builder != null && instanceof && entityName()==...`）：类型进
  `type`，其余条件用 `Predicate`/`BiPredicate guard` 字段，或把短路挪进 handler
  （`builder == null ? null : ...`）。参考 StepPmiPayloadBuilder 的
  `OccurrencePointRule(type, guard, handler)` 三字段形态。
- 表内 lambda 若调用同类私有方法，把该方法改 `static`。
- 清理分支里冗余的全限定名（确认无同名遮蔽后）。
- 注释写明：这是有序数据非控制流、首命中、未匹配回退到哪。

### 4. 冻结顺序文件
`src/test/resources/<kebab>-dispatch-order.txt`：一行一个类型 simpleName，`#` 开头为注释。
OR 组展开后按原相对顺序逐行列出。

### 5. 守卫 + 行为测试
`src/test/java/<pkg>/<Name>DispatchTableTest.java`，抄同目录已有 `*DispatchTableTest`：
- 守卫①：反射读私有表字段，比对冻结文件（数量 + 顺序）。
- 守卫②：表内类型无重复（重复=后项不可达）。
- 行为：每条规则至少 1 例，钉住"命中返回什么 / 不命中"；含链尾回退的用例。
- 反射访问私有表/私有入口：`getDeclaredField`/`getDeclaredMethod` + `setAccessible(true)`
  （同包测试，仓库已允许）。
- **补零覆盖**：若该方法此前无直接测试，行为测试即为新增覆盖，在 commit message 里点明。

### 6. 门禁（本机 JDK 备忘见下）
```
mvn -q spotless:apply
mvn verify "-Djacoco.skip=true"
```
须：`BUILD SUCCESS`、`Failures: 0, Errors: 0`、`forbiddenapis ... 0 error(s)`。
先只跑新测试快速验：`mvn test -Dtest=<NewTest> -DfailIfNoTests=false "-Djacoco.skip=true"`。
**行数上限**:主源文件有 1000 行硬门禁（verify 报 `<file>: NNN lines (max: 1000)`）。
表定义比原 if 链冗长，接近上限的文件折叠前先 `wc -l <file>`（`(Get-Content).Count`
亦可），超了就把表内同形重复块抽成共享 helper（顺带真去重）。
**已知贴顶文件**：`preview/builder/PreviewGeometryCollector.java`（997 行，仅 3 行余量）
——动它之前必须先数行数，或先把既有同形块抽成 helper 腾空间。

### 7. 提交并推送
- 冻结 txt 用**文本级**换行归一为 CRLF（仓库约定）：
  `[IO.File]::ReadAllText/WriteAllText` + `Replace("`r`n","`n").Replace("`n","`r`n")`。
  **禁止**用字节级 `-replace 13`（会误删含 13 的字节，如 'q'=113）。
- 多行 message：写 `.git/COMMIT_MSG_TMP.txt` → `git commit -F` → 删临时文件。
  PowerShell 无 heredoc、不支持 `&&`（用 `;`）。
- 前缀 `refactor(<scope>): fold <Class>.<method> N-branch chain`；body 说明折叠形态、
  类型 final 已核实、新增覆盖、行为保留点。`git push origin main`。
- 更新会话记忆 `.workbuddy/memory/<date>.md`：记 commit 短哈希、分支数、坑。

## 环境备忘（本机）
- 仅 JDK 11 / JDK 26。JDK 26 下 JaCoCo 0.8.13 无法插桩（class 版本 70）→ 门禁必须
  `-Djacoco.skip=true`；跑 mvn 前设 `$env:JAVA_HOME='C:\Users\admin\.jdks\openjdk-26.0.2.1'`。
- 需要覆盖率报告时另装 JDK 17/21。

## 已折叠清单（避免重复劳动，均 final 直接实现分发基类）
StepCadGeometryOps.surfaceTypeName(16) · StepMeshExporter.transformSemanticSurfaceGeometry(7)
· StepResolverValueHelpers.literalText(7)/matchesUnitKind(6) · StepCadBuilder
offsetSupportedSurfaceGeometry+buildOffsetSurface2Geometry(6×2→共享表)
· MeshTriangulatorParametric.mapperFor(6) · StepPmiPayloadBuilder.pointFromAnnotationPoint(9→19)
· StepDumpApp.stepEntityTypeName(6，与 StepEntityNamingUtils 非重复，刻意两份表)
· StepDumpApp.validateGeometricPrimitiveEntity(23) 等。
5 分支层：StepTopologyResolver.pathEdges · MeshTriangulatorParametric
mapPointIntoFaceGeometry+acceptablePcurveBasisSurfaceIds(5×2→共享 SURFACE_UNWRAP_RULES)
· MeshTriangulatorParametric.sampleCurve2 · PcurveSamplingHelper.sampleCurve2
· StepMeshExporter.offsetSemanticSurfaceGeometry · PreviewGeometryCollector
collectMappedAnnotationCarrierEdges · PreviewFaceBuilder.toRectangularCompositeSurfaceFacePayload
(→COMPOSITE_BASIS_FACE_RULES，本文件原为并发会话半成品，补了 order 文件+守卫测试)
· PreviewSerializers.appendJsonValue(→JSON_VALUE_RULES，含 2 组 OR-guard，
拆成一型一规则)
4 分支层：StepPmiPayloadBuilder.pointFromPlaceholderItem(→PLACEHOLDER_POINT_RULES)
+ collectPlaceholderPositions(→PLACEHOLDER_CHILDREN_RULES)
——两表类型集合相同但**顺序不同**（children 表 PointSet 在前），刻意保留两张表
+ 两个 order 文件，折叠不得顺手统一两条本就不同的链。
· StepCadBuilder.implicitBSplineCurveData + implicitBSplineSurfaceData + buildSurfaceGeometry
(4×3，一次折完；PARAMETRIC_SURFACE_RULES 用未绑定实例方法引用 `StepCadBuilder::buildXxx`
匹配 `(builder, id) -> SurfaceGeometry`——静态表调用实例方法的技巧)
非 instanceof 的"条件→常量"链：StepReasonCodeClassifier.classifyReasonCode(34)
(→REASON_CODE_RULES，30 条 reason.contains 含一个 3 选 1 OR 组 + 4 条 ex instanceof
+ "unknown" 回退；冻结文件列 **code 序列**而非类型，守卫反射读 rule 的 `code()`；
`contains` 顺序敏感——泛化 "SURFACE_REPLICA" 必须排在具体 replica 规则之后)
`scan_instanceof_chains.py --min 6` 现应为 0 run(s)；`--min 5` 实测 **4 run(s)**
（1 带守卫 + 3 无守卫）；`--min 4` 为 **5 run(s)**。**2026-09-17 起干净同构链已全部折完**，
剩余 5 条逐一核实均为异构链、结论「不折」：

| 位置 | 形态 | 为何不折 |
|---|---|---|
| StepFacePayloadBuilder:1887 | 循环内 best-candidate | 跨分支共享可变 `best`/`bestScore`，用 `continue` 非 return |
| StepFacePayloadBuilder:2045 | 链表遍历 | 每分支重绑定 `current` + `continue`，末分支带复合 guard |
| PreviewGeometryCollector:321 | 全 return，但三分支共享递归模式 | 文件贴 1000 行上限 |
| StepTrimResolver:275 | 5 个各异的几何计算 | 仅 Line2 一行，其余差异大 |
| StepEdgePayloadBuilder:133 | 分支内含 null 守卫 + 落穿 | 异构 |

→ 别再按 `--min N` 数字选目标；**先看形态**。异构链（跨分支共享可变状态 /
重绑定后 continue / 分支体长度悬殊）折表需引入累加器或遍历抽象，收益低风险高，
应转向「重复实现收敛」或换轨。

**收敛必须留防回潮守卫**（本轮补的实践）：共享 helper 删除本地副本后，
若不留守卫，未来有人重写副本或 fork 共享体，**所有既有测试仍会通过**
（两份实现行为一致，直到漂移）。守卫三件套：
1. 共享目标上仍存在 public static 方法（`getDeclaredMethods` + `Modifier.isPublic`）；
2. 副本侧不得本地重声明（正则匹配 `^\s{4}(?:modifiers)?type name\(` 声明形态）；
3. 调用点走显式限定符（`text.contains("Shared." + name + "(")`）——裸调用意味着
   本地副本或 static import 又回来了。
   **注意**：只对「真正直调」的方法加断言——若副本只调 A 而 A 内部调 B，
   则 B 不应要求在副本侧出现（本轮 6 个 helper 里 json 只直调 5 个）。
   还要钉住「刻意不共享」的方法及其差异理由（如 `normalizeLoopRoles` 因日志差异
   保持不共享），防未来顺手合并。
   **写守卫时会推翻你自己的假设**：核对 modifiers 与实际调用图，别照抄 commit message。

**并行会话协作**：该仓库可能有其它 agent 同时提交（如 CommandCodeBot）。
开工前先 `git log --oneline` 与上次位置对比；工作树里出现陌生目录（如 `.commandcode/`）
是对方工具产物，**不要提交**；提交时用显式路径 `git add <file>` 而非 `git add -A`。

**收敛"同名重复链"前先核对 helper 逐字等价**：StepCadCurveBuilder.implicitBSplineCurveData
是 StepCadBuilder 折叠表的逐字副本，但两类的 `stepEntityTypeName` **不等价**
（Builder → StepEntityNamingUtils 渲染 UPPER_SNAKE；CurveBuilder 本地副本 camelCase）。
收敛模式：拆出包内可见静态 `implicitBSplineCurveDataOrNull`（无匹配返回 null），
**各调用方保留自己的抛错文案**；守卫测试加"委托存在 + 本地 instanceof 链不存在"双断言。
同类先例：`literalText` 有刻意双胞胎（StepParameterReader 渲染 ListValue "(a,b)"，
StepResolverValueHelpers 看不到 ListValue）——测试 javadoc 写明"两副本故意不同、
保持独立表"的，**不得合并**，各自独立折表（reader 侧 ParameterLiteralTextDispatchTableTest）。
镜像折表：`Curve3SamplingHelper.sampleLooseCurve(4)` → LOOSE_CURVE_SAMPLERS 完全
照抄既有 Curve2SamplingHelper 形态。
**第三次踩同一坑**：Line3.sample 采样无限直线 ±10 窗口（首点 origin−10·dir 而非 origin；
sample(72)=73 点）——写 fixture 断言前先查该类的 sample 语义。

**环境坑**：`.git/refs/remotes/origin/` 目录可能被并发进程实时删除（mkdir 后毫秒级消失、
packed-refs 停在旧值）⇒ `git status -sb` 显示 "ahead N" 假象。判据：`git ls-remote
origin main` 对比 `git rev-parse HEAD`，一致即推送成功，忽略 tracking ref 显示。

**谓词→boolean 链（返回 boolean 的 instanceof 链）**：`Edge.isClosedCurve(5)` →
`CLOSED_CURVE_RULES`，规则形态 `record ClosedCurveRule(Class<? extends T> type,
Predicate<T> handler)`，循环 `if (rule.type().isInstance(curve)) return
rule.handler().test(curve);`。要点：
- 所有类型 `final` 直接实现接口 ⇒ 互斥，原链的落穿语义（某分支条件不满足继续向后
  instanceof）可安全改为 handler 直接返回 false —— 但这条等价性论证必须写进 record
  javadoc 或 order 文件头注释。
- `Circle || Ellipse3` 一分支拆两型一规则（均返回 true，行为中立）。
- **窄参数方法引用不兼容宽谓词**：`Edge::isClosedBSpline` 不能当 `Predicate<Curve3>`
  （`test(Curve3)` 要求 handler 接受超类型，参数逆变），须 lambda + cast：
  `curve -> isClosedBSpline((BSplineCurve3) curve)`。
- 零覆盖谓词折叠时应**同时补行为测试**（参考 `EdgeClosedCurveDispatchTableTest`：
  3 守卫 + 5 行为）。fixture 坑：`Line3.sample(2)` 采样无限直线 ±10 世界单位窗口而非
  线段端点（线段 Composite 永远保守判 not-closed）；`CompositeCurve3` 构造器拒绝空段
  列表，"空集合"行为测试无法构造——先查构造器校验再写 fixture。

**静态表的参数传递约束**：表若声明为 `private static final`，其 handler **无法捕获宿主
方法的参数**（如 `json` / `builder` / `positions`）；必须把该参数纳入 handler 签名
（`void write(StringBuilder json, Object value)`），并在循环里传入。否则只能退化成方法内
的局部表 —— 而局部表无法被顺序守卫从源码解析出来，等于放弃冻结保护。
若 handler 需要调用宿主的**实例方法**，用未绑定实例方法引用 `Host::buildXxx` 匹配
`(Host self, int id) -> R` 签名（builder 作为首参在循环里传 `this`），表仍是 static ——
参考 `StepCadBuilder.PARAMETRIC_SURFACE_RULES`。

**谓词不是类型时的守卫变体**：`reason.contains(...)` 这类字符串分发链无法用
`Class` 表达规则，冻结文件改列**返回 code 的序列**（允许同 code 重复出现，头注释说明），
守卫测试反射读表（`getDeclaredField` + record 的 `code()`，`setAccessible(true)`）
比对序列 —— 比源码解析字符串字面量可靠得多。顺序敏感点（泛化片段必须排在具体片段之后）
单独写一条行为钉测。参考 `StepReasonCodeClassifierDispatchTableTest`。
5 分支层另 2 条已按"重复实现收敛"改委托（不折表，commit `c0a1b868`，net −85 行）：
`StepDumpApp.shellFaces -> ShellHelper`、
`StepLegacyGeometryBuilder.collectShellLikeIds -> PreviewGeometryCollector`；
`PreviewFaceBuilder.isShellEntity/isShellLikeEntity -> ShellHelper` 同时收敛谓词副本。
注意 `PreviewGeometryCollector.collectShellLikeIds` 现在**是** canonical，但它本身仍是
一条 5 分支链（可达 321 行处），因贴 1000 行上限暂不折。

## 关键教训：半成品折叠
生成器一次产出**三个**文件（宿主 java + `<slug>-dispatch-order.txt` + `<Name>DispatchTableTest.java`）。
agent 手工折叠时容易只改宿主、漏掉后两者 —— 那样的树**编译通过、测试也绿**，因为旧守卫根本没建立。
接手任何"看起来已折好"的未提交改动前，先 `git status` 看是否三件套齐全；缺件就补齐
（order 文件写类型 simpleName + 头部注释，守卫测试抄同目录既有 `*DispatchTableTest`）。

另一种手工折叠的半成品是**"表建好了但宿主没走表"**（方法体仍是旧 if 链，或反之表是死代码）：
编译通过、旧测试也绿，因为行为没变而守卫只解析表本身。**对策**：在守卫测试里加一条源码断言，
钉住入口方法确实遍历了自己的表，例如
`assertTrue(text.contains("for (PlaceholderChildrenRule rule : PLACEHOLDER_CHILDREN_RULES)"))`
（参考 `PmiPlaceholderPointDispatchTableTest`）。

## 关键教训
- 同名方法未必是重复：折叠前先比对两处实现的行为（StepDumpApp vs StepEntityNamingUtils
  的 stepEntityTypeName 差在 StepRepresentation 特判 + 反射尾），不同就保持两份表。
- 既有"不对称"原样保留并显式钉测：如球面 mapper project 返回余纬而 pointAt 收纬度、
  quadric project 返回裸 atan2 不 wrap——别假设 round-trip，按实际契约断言。
- 并行对同一文件发多个 Edit 会竞态丢改，务必串行。

## 重复实现收敛（delegation，比给小链加表更有价值）

同一方法在多个类里出现多份逐字/近似副本时，**先判断能否收敛**，别机械地每份都折一张表。
收敛 = 选定一个 canonical 实现，其余改成"保留入口签名 + 一行委托"（仓库既有范式，
如 `PreviewFaceBuilder.shellFaces -> ShellHelper`、
`StepRepresentationPayloadBuilder.buildBsplineSurface -> PreviewMeshExporter`）。

**等价性三重核对（缺一不可，凭 diff 相似就合并是事故源）**：
1. **受理集**：两版接受的类型/条件是否一致。最好能找到现成谓词精确对应
   （如 `StepDumpApp.shellFaces` 的 5 分支恰好等于 `ShellHelper.isShellEntity`）。
2. **终态**：异常类型与**逐字**文案、返回值（null / 反射尾 / 类名尾）是否一致。
   不同就保留各自的终态（外层 if 谓词 + 委托 + 原终态）。
3. **副作用/跳过表**：差异项是否真的可观测。例：legacy 的
   `collectShellLikeIds` 跳过表多一个 `StepFacetedBrepAndBrepWithVoids`，而该型在
   canonical 里会**逐条落到方法末尾、同样什么都不做** → 等价（no-op），可直接合并。
   反例：折 27 的 `OFFSET_SURFACE_RULES` 入口 hoist 的 scale 语义不同（带/不带 abs），
   绝不能"顺手统一"。

**方向选择**：优先让"已是其他入口的委托目标"的类当 canonical
（`PreviewGeometryCollector` 已被 `PreviewFaceBuilder` 委托），保持收敛故事单一。
包间依赖双向都已有先例（`export/json` ↔ `preview/builder`）时不算新增违例，但需核实。

**清理**：删掉谓词/分支体后，原文件可能留下失效的显式 import（`javac` 不报，
spotless 若未开 `removeUnusedImports` 也不会清），要自己 grep 一遍删掉；
若有 `import x.*;` 通配兜底则删除更安全。

**"非理性/理性"孪生类对（同包、互为镜像）→ 静态 helper + 一行委托**（2026-09-14，
`BSplineSurface3` ↔ `RationalBSplineSurface3`，commit `2ba27c8b`）：两型共享的
`expandedKnots`/`validateKnots`/`clamp`/`sampleGrid`/`boundingBox`×2/`closestPointTo`/
`distanceTo` 逐字相同，抽包内 `XxxHelper` 后各入口改一行委托；helper 用函数接口
（如 `PointEvaluator { R at(u,v); }`）解耦"求值方式"，从而同时服务两种求值。要点：
- **优先 helper、慎用抽象基类**：基类构造函数必须 `super()` 先行，只能把公共校验收进
  基类，子类专有校验（如权重网格）被迫挪后 ⇒ 会改变"同一输入同时违反公共规则与专有
  规则时抛哪条消息"的顺序，与"逐字保留"相冲。委托方案**构造函数原封不动**、零行为风险。
- 触碰 per-class 字段的缓存访问器（`uExpanded` 等）**无法**被静态 helper 吸收，留在各类内
  —— 这是有意保留的 boilerplate，别为消掉它去上继承。
- 测试三件套：反射断言"两型不再声明共享 kernel 名 + 公开入口方法仍在"（防再复制）；
  "中性参数下理性 ≡ 非理性"（钉住接线）；"非中性参数下仍偏离"（钉住专有逻辑没被顺手统一）。

### 找候选：`tools/scan_duplicate_methods.py`（2026-09-17，commit `b695c5d3`）

别再靠肉眼翻文件找重复——用扫描器。它按"方法名 + 归一化方法体"分组，报告**跨类**出现的
同体方法（同名同类不算），并给出每组可回收行数估算：

```
python tools/scan_duplicate_methods.py --min-lines 6 --top 30    # 严格（空白归一）
python tools/scan_duplicate_methods.py --fuzzy --min-lines 4     # 忽略 com.minicad.* 限定符
```

实现要点（改它时别踩）：先 `strip_java` 抹掉注释/字符串/文本块（保留换行与偏移）；用括号
深度走 `{`/`}` 栈，`{` 前的文本按"类型声明 / 方法声明 / 其它"分类，方法声明要求**方法名前
不是 `.`**（否则 `list.forEach(x -> {` 会被误判成方法 `forEach`），且拒绝以 `->` 结尾、
`new `、`if/for/while/catch/...` 开头；构造器、`toString/hashCode/equals` 跳过。
当前基线：**63 组、约 721 行可回收**——这是下几轮收敛的待办池。

注意 `--min-lines` 之下藏着大量 1〜5 行的短副本（如 `appendOrientedTriangle`、
`toPointPayload` 这类 shim），它们常常是"删一个正典方法就连带失效"的附属品，所以扫描时
**别把阈值压太低**去找它们，而是在删正典时顺手看它是否变成了孤儿（用 `grep -c` 数符号
出现次数：只剩 1 次 = 只有 import 行 = 已成死代码）。

### 扫描器会报"逐字相同但不是同一份代码"（2026-09-17）

**text 相同 ≠ code 相同。** 扫出来的每一组都必须再核一遍：**两侧那些未限定的简单名，各自解析到哪个类？**

- **嵌套同名类型遮蔽 import**：`MeshTriangulatorParametric` 自己声明了 `static final class UvPoint`，
  它的 `sampleLinePcurve`/`sampleCirclePcurve`/`sampleEllipsePcurve`/`sampleSplinePcurve`/
  `closestPointIndex` 与 `preview/sampling/PcurveSamplingHelper` 的**逐字相同**，但那一侧的 `UvPoint`
  是 `com.minicad.preview.payload.UvPoint`。文本一样、类型不同 ⇒ 直接委托**编译不过**。
  出路是先合并类型：本轮把 mesh 侧切到 payload `UvPoint`、删掉嵌套副本，5 个 sampler 才收敛得动。
  顺带收益是消掉一个重复值类型（两版 equals/hashCode 语义逐字可用，嵌套版 `Objects.hash(u,v)`
  与 payload 版 `Objects.hash(Double.hashCode(u), Double.hashCode(v))` 同值）。
- **同类各自持一份嵌套类型**：`StepCadBooleanBuilder` 与 `StepCadSweptBuilder` 各有一个
  `private static class CircularFrame`，所以 `circularFrame`（以及引用它的 `polygonNormal`）那两组
  **不能**跨文件合并——两侧的 `CircularFrame` 是两个不同的类。
- 扫描器已内置该检查：命中就在组标题打 `[!] nested:Xxx`。只有这一类打标——
  `cross-package` 命中率太高（约 8 成组都跨包），降级成末尾的一行汇总计数，不占行内版面。
  真阳性验证过两组：`sampledCurveEvaluator`（`PreviewCurveEvaluator` 有嵌套 `interface CurveEvaluator`）、
  `circularFrame`。

### 收敛前先数调用点：半收敛会留下孤儿副本（2026-09-17）

调用点早已改指向 helper、私有副本却还留在文件里，这种现象比想象中多。本轮 `PreviewCurveEvaluator` 的
`matrixForMappedPlacement`/`invertMatrix`/`composeMatrices`（:783 起，58 行）与公开的 `closestParameter`
**全都是 0 调用点**，`StepMeshExporter.Triangulator.orientSamples`（:924）也是——它们只是扫描器眼里的
"重复实现"。所以顺序应该是：**先 `grep -n <name>` 数调用点**，0 个就是死代码，
**直接删比改成委托更干净**（少留一层 facade）；删完顺手查一遍死 import。

### 新增文件必须先过 `spotless:apply`（2026-09-17）

门禁里的 spotless 对**新加文件**会开刀（google-java-format 会重排、并掉行），写完新 helper / 新守卫测试后
先跑 `python tools/mvn.py -o spotless:apply` 再 `verify`。否则 verify 直接挂在 spotless:check 上，
而且失败输出被截断（"… more lines that didn't fit"）看不到细节，白等一轮 1 分半的构建。

### 跨文件多站点手术：用一次性脚本 + 计数断言，别连发 Edit

同一文件要改 5+ 处（删 3 个方法体 + 改 3 个调用点 + 加 import）时，**不要**在一条消息里并行
发多个 Edit（会竞态丢改，见上文教训），也**不要**串行发 8 条消息。写一个一次性 Python 脚本：

- 行号锚点删除法：先 `check(lines[N-1], "锚点片段", ...)` 断言，再 `delete_lines` 按
  **(first, last) 自底向上**删（避免下标漂移）。**锚点行号必须取自替换前的原文件**——
  如果同一脚本里还有"整段替换"，务必让 `split('\n')` 发生在替换**之前**，否则行号全错
  （这个坑本轮踩过一次：PSS 的 220/248/252 锚点因先替换而偏移）。
- 字符串替换一律走 `replace_once(text, old, new)`，内部 `assert count == 1`。这比 Edit 强：
  Edit 只保证"能匹配"，脚本能保证"**恰好命中一次**"——多点或零点都会当场炸，不会静默半应用。
- 脚本本身用 `_` 前缀命名并在提交前删除（一次性）；可复用的扫描器才进 `tools/`。

### 收敛的守卫测试该钉什么（`PreviewTriangulationConvergenceTest` 范式）

收敛不留守卫会**静默回潮**：后人重写副本时两份实现一致，所有既有测试照样绿，直到漂移。
四类断言，缺一不可：

1. **正典可达性**（反射）：canonical helper 仍 `public static` 声明全部被收敛的方法名
   （用 `getDeclaredMethods()` 找名，断 `Modifier.isStatic/isPublic`）。
2. **副本不得回潮**（源码解析）：
   `(?m)^\s*(?:public |private |protected )?(?:static )?[\w<>\[\], .]+\s+NAME\s*\(` 扫宿主源码，
   断言每个被删的方法名**不再被声明**。
3. **调用点必须带限定符**：断言 `canonical + "." + name + "("` 出现——只断"名字出现"不够，
   裸调用意味着本地副本或 static import 又回来了。
4. **运行时仍一致**：facade 与 canonical 对同一 fixture 结果 `assertEquals`（含边界：
   退化网格短路、每四边形两三角的计数）。
5. **额外钉"刻意不收敛"**：本轮 `triangulateSphericalStrip` 无 canonical 孪生（它吃
   `Axis2Placement3D + radius` 而非 surface 对象），断言它**仍在原位**——防止后人"顺手补完"
   把它也删了。这与上文"不对称为刻意差异、要显式钉测"是同一原则。

**收敛方向选择**：本轮选 `TriangulationHelper` 当 canonical，因为它已被 `export/glb` 与
`export/json` 引用（是既有的公共入口），而 `PreviewSurfaceSampler` 只被 `PreviewFaceBuilder`
使用。让"只被一处使用的类"当 canonical 会把公共 helper 变成私有依赖，方向反了。
另：**保留一行委托 facade ≠ 重复**（`PreviewSurfaceSampler.triangulatePatch` 保留签名但
body 只有 `return TriangulationHelper.triangulatePatch(patch, sameSense);`）——外部调用方
不用改，且守卫只需断"委托存在 + 本地体不存在"。

### 同一簇副本要按调用点数分流：facade 还是删除（2026-09-17）

一个类里"成簇"的副本（本轮 `PreviewFaceBuilder` 一次 7 个）**不要一律处理成同一种形态**，
按调用点分：

- **有真实调用点 → 留一行委托 facade**（保留签名，body 只有
  `return Canonical.name(args);`）。理由不只是省改动：`PreviewGeometryCollector` 通过这个类
  取 styled item / 收拓扑边，`faceSameSense` 在本文件内有约 40 处裸调用——facade 是稳定的
  preview 侧入口。**并且它让并行会话的活跃文件零改动**（不必去改 `PreviewGeometryCollector`）。
- **零调用点 → 直接删**（`pointPayloadFromVertex`）。
- **只有一个调用点且是测试 → 删掉副本、把测试改指向 canonical**（`reverseFacePayload`
  仅 `PreviewBuilderMapperTest` 调用）。判据：让"唯一调用方是测试"的 facade 活着，等于
  为测试保留一个假的生产 API。

删除方法体后**必须查它孤立的 import**：本轮的 `StepStyledItem` / `StepOverRidingStyledItem` /
`TessellatedFaceExporter` 三个显式 import 都只被删掉的方法引用过。查法（几行 Python 就够）：

```python
body = re.sub(r'^import .*$', '', src, flags=re.M)   # 抹掉 import 行
for m in re.finditer(r'^import (?!.*\*)(\S+)\.(\w+);$', src, flags=re.M):
    if not re.search(r'\b' + m.group(2) + r'\b', body):
        print('UNUSED:', m.group(0))
```

注意 `import pkg.*;` 通配 import 会让"类型不再出现"这件事变得**更强**的信号而非更弱：
`PreviewFaceBuilder` 用 `com.minicad.step.model.*`，所以 `StepAdvancedFace` 等包装类型
"一次都不出现"才真正说明 instanceof 链被删干净了（若只是替换成 helper 调用，类型名也不会再出现）。
这类"包装类型名必须彻底消失"的断言很值，可以顺手加上。

### 判 canonical 归属：先全仓 grep 该名字，看有没有第三份已委托（2026-09-17）

不要凭"谁看起来更公共"拍脑袋。本轮 `PreviewSurfaceSampler` 的 `resamplePolyline` / `reversed`
被判给 `StepGeometryHelper`，依据是**`StepEdgePayloadBuilder` 早就把同名的自己那份委托过去了**
（`return StepGeometryHelper.reversed(points);`）——方向是既成事实，剩下的只是收尾。
同理 `buildFreeFormSurface` 判给 `PreviewMeshExporter`，因为 `PreviewSurfaceSampler` 自己的
`buildBsplineSurface` 已经委托给 `PreviewMeshExporter`，且活跃文件 `StepFacePayloadBuilder`
也直接调它。**grep 一遍名字的调用点，方向往往自己就露出来了。**

### 逐字比对要按 token 而不是按行（2026-09-17）

线级比对会把"仅换行位置不同"报成不一致：`PreviewMeshExporter.buildFreeFormSurface` 与
`PreviewSurfaceSampler` 的版本只差一处 `throw new UnsupportedGeometryException(` 被折成两行，
逐行 diff 却报出 20 处"差异"（后续全部错位）。**结论前先看差异形态**：如果是"某行被拆/被并
+ 由此产生的整体错位"，就是同一份代码；真差异是**语义 token 不同**（如
`instanceof StepCartesianPoint` vs `instanceof com.minicad.step.model.StepCartesianPoint`、
插入一行 `// Generate uniform knot vectors` 注释）。反过来说：跨文件副本里出现
**全限定名 / 错位缩进 / 多一行注释**这类"抄写痕迹"，正是副本关系的证据。

### 私有方法的 facade 一致性只能靠反射（2026-09-17）

`private static` 的 facade（`PreviewSurfaceSampler.resamplePolyline` / `reversed`）**同包测试
也访问不到**。守卫要用 `getDeclaredMethod(name, List.class, int.class)` + `setAccessible(true)`
+ `invoke(null, ...)`，并与 canonical 对同一 fixture 断言相等；**边界用例一起给**（空串、
单点、零长度折线走 `total <= Epsilon.EPS` 的退化分支），否则只能证明"正常路径没漂移"。

### 可见性别过度约束（2026-09-17）

`assertPublicStatic` 只该用在**跨包调用**的 canonical 上（`StepGeometryHelper.resamplePolyline`
/ `reversed`、`PreviewMeshExporter.buildFreeFormSurface`）。像 `pointAtDistance` / `interpolate`
这种被委托后只剩 canonical 类**内部**调用的，断言"仍被声明"就够——要求它们继续 `public`
会把合理的可见性收紧变成测试失败。

### 半收敛要如实钉住：把"能力还在"而不是"形状不变"写进守卫（2026-09-17）

`buildFourSidedPatch` 本轮**刻意没收敛**：唯一孪生在 `StepEdgePayloadBuilder`（并行会话活跃
文件），要收敛得先把那份 `static` 改成 `public static`。正确做法不是硬上，也不是假装收尾，而是
在守卫里钉**能力可达**而非**具体形状**：

```java
boolean local = declares(text, "buildFourSidedPatch");
boolean delegated = text.contains("StepEdgePayloadBuilder.buildFourSidedPatch(");
assertTrue(local || delegated, "预览侧入口必须继续存在，两种解法都算通过");
```

这样下一轮无论"删本地改委托"还是"保持现状"，守卫都不挡路；同时另一条断言钉住孪生仍在
（提醒债务存在）。**把"刻意不做"写成测试注释**，比写在 commit message 里更难丢。
