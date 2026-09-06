# 任务交接：StepCadGeometryOps transform 链折叠（进行中）

> 写于 2026-09-06 13:20。本文档供接手模型续接执行，无需重读历史会话。
> 仓库：`D:\work\MiniCAD`，分支 `main`，工作区有 1 个未提交改动。

## 一、任务背景（重构主线）

全仓范围的 instanceof dispatch chain 折叠：把长 `if (x instanceof A a) ... else if (x instanceof B b) ...` 链折叠为**有序 (type, handler) record 规则表 + 首匹配返回**，配套黄金测试与 frozen-order dispatch guard。每轮：挑体量最大的 clean chain → 折叠 → 守卫测试 + 行为测试 → build gate → commit + push（commit 信息英文，`refactor(scope): ...` 格式）。

上一轮已完成 `StepGeometryReverser` 反转链折叠 + 三重去重（commit `1f172d87`，已推送）。本轮目标：**`StepCadGeometryOps` 的三条 transform 链（共 39 分支）**。

## 二、当前进度（⭐ = 已完成，□ = 待做）

- ⭐ **三条链已折叠为表驱动并落盘**：`src/main/java/com/minicad/step/semantic/StepCadGeometryOps.java`
  - `TRANSFORM_CURVE3_RULES`（原 transformCurve3，13 分支）— 第 305 行起
  - `TRANSFORM_CURVE2_RULES`（原 transformCurve2，10 分支）— 第 413 行起
  - `TRANSFORM_SURFACE_RULES`（原 transformSurfaceGeometry，16 分支）— 第 503 行起
  - 三个入口方法 `transformCurve3` / `transformCurve2` / `transformSurfaceGeometry` 已改为遍历规则表 + 末尾统一 `throw new UnsupportedGeometryException(...)`（语义不变）
  - 文件从 ~1450 行降到 **1080 行**；规则 record 为 `TransformCurve3Rule` / `TransformCurve2Rule` / `TransformSurfaceRule`
- ⭐ **编译验证通过**：`mvn -o compile` BUILD SUCCESS（JDK 21）
- ⭐ 中间产物：`target/transform-new-block.java`（拼接用的新代码块，完成后可删）
- □ **写守卫测试**：`TransformGeometryDispatchTableTest`
  - 三份 frozen-order 快照 `src/test/resources/transform-curve3-dispatch-order.txt`、`transform-curve2-dispatch-order.txt`、`transform-surface-dispatch-order.txt`
  - 守卫内容参照 `src/test/java/com/minicad/step/semantic/ReverseGeometryDispatchTableTest.java`：读宿主源码按声明序提取规则表中的 `.class` 条目与快照比对 + 重复类型检查 + **防回归检查（不许再长出 instanceof 长链）**
- □ **写运行时行为测试**：每个分支至少 1 条，基于真实 `StepCartesianTransformationOperator`
  - 关键调研结论（已验证）：
    - `transformPoint3` 依赖 `StepCadBuilder.buildPoint/Direction`，行为测试需真实 builder；搭建方式参考 `src/test/java/com/minicad/step/semantic/StepCadBuilderTest.java`
    - `StepCadGeometryOps` 是 package-private，测试同包即可
    - 变换算子实体名 `CARTESIAN_TRANSFORMATION_OPERATOR_3D`；参数解析见 `TransformationResolver.resolveCartesianTransformationOperator3D` 与 `MiscellaneousRegistry1.java` 405 行附近
    - **必测路径**：`DegenerateCurve2` 不在 transformCurve2 覆盖内 → 应抛 `UnsupportedGeometryException`（terminal throw 路径）
    - 全部相关几何类均为 `final`、直接实现 Curve2/Curve3/SurfaceGeometry，无子类关系
- □ **重冻结脚本**（可选但推荐）：仿照 `tools/gen_reverse_dispatch.py` 写 `tools/gen_transform_dispatch.py`
- □ **build gate**：`python tools/mvn.py -o test de.thetaphi:forbiddenapis:3.8:check` 全绿
  - 上一轮基线：**2248 tests**，JaCoCo bundle LINE **0.6379**（gate 0.60）
  - spotless:check **不能当门禁**（全仓 95+ 既有报错）；只手动核验改动文件无行尾空白、有末尾换行
  - `mvn -o verify` 不可用（缺 jar 插件缓存）
- □ **commit + push**：`git add <改动文件> && git commit`（英文正文写动机/改动/门禁数据）→ `git push origin main`
- □ **更新记忆**：`D:\work\MiniCAD\.workbuddy\memory\2026-09-06.md`（追加）+ `MEMORY.md`（更新候选链清单）

## 三、环境与命令（必须遵守）

- **JDK 固定** `C:\Users\admin\.jdks\ms-21.0.12.1`（JDK 21）。`tools/mvn.py` 已实现动态发现并优先该路径，**不要绕过它直接跑 mvn**。
- Maven 命令统一：`"C:/Users/admin/.workbuddy/binaries/python/versions/3.13.12/python.exe" tools/mvn.py -o test de.thetaphi:forbiddenapis:3.8:check`
  - 长命令用 `run_in_background: true` + `TaskOutput` 取结果
  - 插件前缀（如 `spotless:check`）离线解析失败，必须用**全限定坐标**：`com.diffplug.spotless:spotless-maven-plugin:2.43.0:check`、`de.thetaphi:forbiddenapis:3.8:check`
- 扫描候选链：`python tools/scan_instanceof_chains.py`

## 四、本轮折叠细节（供守卫测试与 commit 正文使用）

- 原链特征：每支 `return`（新建变换后副本）、末尾统一 `throw new UnsupportedGeometryException`，分支顺序即派发顺序——折叠后**必须保持原分支顺序**（首匹配语义）
- 规则表声明顺序可从当前源码 305/413/503 行起的 `List.of(...)` 直接提取；快照文件每行一个简单类名即可
- `StepCadBuilder` 对这三个方法只有薄委托调用（`geometryOps.transformXxx`），不需要改动
- 全仓无其他文件调用这三个方法（已 grep 确认）

## 五、下一轮候选（本轮完成后）

扫描器剩余最大链已降到 14 分支：`PreviewCurveEvaluator:1220`、`SurfaceMapperHelper:146`、`StepEdgePayloadBuilder:206`。按"体量最大且 clean"续接。

## 六、历史教训（避免重蹈）

1. 折叠前先 grep 引用——扫描器报的"大链"可能是死代码（上轮 StepGeometryReverser 即是）。
2. 行为测试要真实验证可观察语义，断言别想当然（上轮 `reverseBSplineControlGrid` 是行内反转、行序不变，首版断言写反被测试当场抓住）。
3. 编辑大 Java 文件用 Python 按内容定位拼接，不要行号硬编码。
