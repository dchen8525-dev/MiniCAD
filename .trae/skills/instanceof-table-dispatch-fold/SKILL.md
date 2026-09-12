---
name: "instanceof-table-dispatch-fold"
description: "把 MiniCAD 里长的 instanceof 顺序分发链折叠为 (type, guard, handler) 表驱动分发，并补齐冻结顺序守卫测试与门禁。当用户要求继续/执行 instanceof 链折叠、表驱动重构，或提到 scan_instanceof_chains、dispatch table、折叠某方法时使用。"
---

# instanceof 链表驱动折叠（MiniCAD）

把 `if (x instanceof T) { ... }` 的长顺序分发链折叠成有序 `(type, guard, handler)`
规则表，用首命中循环替换，并通过"冻结顺序 + 反射守卫 + 行为测试 + 全量门禁"保证
行为不变。这是 MiniCAD 的主线重构目标（见 project memory）。

## 何时用

- 用户说"继续执行任务/下一项工作"且上下文是 instanceof 折叠。
- 用户点名折叠某方法、或提到 scan/dispatch table/表驱动。
- 扫描器报告某条 ≥5 分支的链值得折。

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
（1 带守卫 + 2 无守卫）；`--min 4` 为 **7 run(s)**（StepEdgePayloadBuilder:133 是
分支内含 null 守卫的异构链，不建议折）。剩余 5 分支里
StepFacePayloadBuilder:1954 与 StepTrimResolver:275 是**异构长链**（各分支体逻辑/长度差异大，
前者还在 for 循环内需回传可变状态），折叠收益低风险高，优先找同构链。

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
