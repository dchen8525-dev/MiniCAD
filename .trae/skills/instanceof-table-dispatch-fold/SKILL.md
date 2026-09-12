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
`scan_instanceof_chains.py --min 6` 现应为 0 run(s)；剩余为 5/4 分支收益递减层。

## 关键教训
- 同名方法未必是重复：折叠前先比对两处实现的行为（StepDumpApp vs StepEntityNamingUtils
  的 stepEntityTypeName 差在 StepRepresentation 特判 + 反射尾），不同就保持两份表。
- 既有"不对称"原样保留并显式钉测：如球面 mapper project 返回余纬而 pointAt 收纬度、
  quadric project 返回裸 atan2 不 wrap——别假设 round-trip，按实际契约断言。
- 并行对同一文件发多个 Edit 会竞态丢改，务必串行。
