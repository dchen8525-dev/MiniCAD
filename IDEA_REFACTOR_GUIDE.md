# IntelliJ IDEA 重构指南

## 📋 当前状态

**分支**: `refactor/preview-integration`
**状态**: 准备好进行IDEA重构
**主文件**: `StepPreviewJsonExporter.java` (18094行，未改动)

**提取的文件** (在 `src/main/java/com/minicad/app/`):
- ✅ `PreviewSerializers.java` - 已集成
- ⚠️ `PreviewSurfaceSampler.java` (279行) - 未集成
- ⚠️ `PreviewEdgeSampler.java` (733行) - 未集成
- ⚠️ `PreviewPmiBuilder.java` (1673行) - 未集成
- ⚠️ `PreviewCurveEvaluator.java` (1734行) - 未集成
- ⚠️ `PreviewUvMapper.java` (2286行) - 未集成
- ⚠️ `PreviewFaceBuilder.java` (2829行) - 未集成

**备份位置**: `backup/extracted/` 和 Git分支

---

## 🎯 目标

将提取的6个Preview*文件集成到主文件中，删除重复代码：
- **预期减少**: ~9534行
- **目标**: StepPreviewJsonExporter.java 从 18094行 → ~8500行

---

## 🔧 方法1: Inline 反向集成（推荐）

适用于提取文件中的方法与主文件重复的情况。

### 步骤

#### 1. 在IDEA中打开项目
```bash
# 打开IDEA
# File → Open → D:\work\MiniCAD
```

#### 2. 对每个提取文件执行Inline

**从最简单的开始：PreviewSurfaceSampler.java**

```
1. 打开文件: src/main/java/com/minicad/app/PreviewSurfaceSampler.java
2. 右键类名 "PreviewSurfaceSampler"
3. 选择: Refactor → Inline (或按 Ctrl+Alt+N)
4. 在弹出的对话框中:
   - 选择 "Inline all methods and remove class"
   - 点击 "Refactor"
5. 检查预览窗口，确认更改
6. 点击 "Do Refactor"
```

**IDEA会自动**:
- 删除 PreviewSurfaceSampler.java
- 将所有方法移回 StepPreviewJsonExporter.java
- 更新所有方法调用
- 处理所有依赖关系

#### 3. 验证编译

```bash
# 在项目根目录执行
export JAVA_HOME="/c/Users/admin/.jdks/ms-11.0.31"
export PATH="$JAVA_HOME/bin:$PATH"
mvn clean compile -DskipTests
```

#### 4. 提交第一个集成

```bash
git add -A
git commit -m "Inline PreviewSurfaceSampler back to main file

- Integrated 279 lines
- Compilation: SUCCESS
- Ready for next file"
```

#### 5. 重复处理其他文件

按顺序处理：
1. ✅ PreviewSurfaceSampler.java (279行) - 最简单
2. ⏳ PreviewEdgeSampler.java (733行) - 较简单
3. ⏳ PreviewPmiBuilder.java (1673行) - 中等
4. ⏳ PreviewCurveEvaluator.java (1734行) - 中等
5. ⏳ PreviewUvMapper.java (2286行) - 复杂，有内部类
6. ⏳ PreviewFaceBuilder.java (2829行) - 最复杂

---

## 🔧 方法2: Extract Method 正向提取（从头开始）

如果Inline失败，使用此方法重新提取。

### 步骤

#### 1. 删除所有提取文件（已有备份）

```bash
# 删除未集成的文件
rm src/main/java/com/minicad/app/PreviewSurfaceSampler.java
rm src/main/java/com/minicad/app/PreviewEdgeSampler.java
rm src/main/java/com/minicad/app/PreviewPmiBuilder.java
rm src/main/java/com/minicad/app/PreviewCurveEvaluator.java
rm src/main/java/com/minicad/app/PreviewUvMapper.java
rm src/main/java/com/minicad/app/PreviewFaceBuilder.java

# 恢复到原始状态
mvn clean compile -DskipTests
```

#### 2. 在IDEA中重新提取

**提取示例：SurfaceSampler方法组**

```
1. 打开 StepPreviewJsonExporter.java
2. 找到这三个方法（在约13000行）:
   - sampleSurfaceGrid()
   - triangulatePatch()
   - buildFourSidedPatch()

3. 按住 Ctrl 选择这三个方法
4. 右键 → Refactor → Extract → Class
5. 配置:
   - Class name: PreviewSurfaceSampler
   - Package: com.minicad.app
   - Visibility: public
   - 勾选: Make methods static

6. IDEA会自动:
   - 创建新类文件
   - 移动方法
   - 更新主文件中的调用
   - 处理依赖

7. 验证编译:
   mvn clean compile -DskipTests
```

---

## ⚠️ 常见问题及解决方案

### 问题1: 类型不兼容

**现象**: `ParametricSurfaceMapper` 类型冲突

**解决**:
```java
// 方案A: 保留内部类在主文件
// 在 StepPreviewJsonExporter.java 中保留 ParametricSurfaceMapper

// 方案B: 提取到单独文件
// 创建 src/main/java/com/minicad/app/ParametricSurfaceMapper.java
```

### 问题2: 访问权限错误

**现象**: 方法是 private，无法访问

**解决**:
```java
// 在提取的类中，将 private 改为 public static
// 或者在IDEA中勾选 "Make methods public"
```

### 问题3: 方法依赖

**现象**: 方法A调用方法B，但方法B未被提取

**解决**:
```
在IDEA提取时:
- 同时选中方法A和方法B
- 或者在预览窗口手动添加依赖方法
```

### 问题4: 编译失败

**现象**: 找不到符号、类型不匹配

**解决**:
```bash
# 撤销最近的重构
git reset --hard HEAD

# 或者在IDEA中
Ctrl+Z (撤销)
```

---

## ✅ 每次重构后的验证清单

```bash
# 1. 编译检查
mvn clean compile -DskipTests

# 2. 单元测试
mvn test

# 3. 行数检查
wc -l src/main/java/com/minicad/app/StepPreviewJsonExporter.java

# 4. 功能验证
mvn exec:java -Dexec.mainClass="com.minicad.app.StepViewerApp" \
  -Dexec.args="examples/minimal-square.step"

# 5. Git提交
git add -A
git commit -m "Integrate PreviewSurfaceSampler"
```

---

## 📊 进度跟踪

| 文件 | 行数 | 状态 | 编译 | 测试 |
|------|------|------|------|------|
| PreviewSurfaceSampler.java | 279 | ⏳ 待处理 | - | - |
| PreviewEdgeSampler.java | 733 | ⏳ 待处理 | - | - |
| PreviewPmiBuilder.java | 1673 | ⏳ 待处理 | - | - |
| PreviewCurveEvaluator.java | 1734 | ⏳ 待处理 | - | - |
| PreviewUvMapper.java | 2286 | ⏳ 待处理 | - | - |
| PreviewFaceBuilder.java | 2829 | ⏳ 待处理 | - | - |
| **总计** | **9534** | | | |

---

## 🎯 最终目标

- [ ] StepPreviewJsonExporter.java < 2000行
- [ ] 所有测试通过
- [ ] mvn clean test 成功
- [ ] examples/minimal-square.step 正常加载
- [ ] Git提交完成

---

## 🆘 如果遇到问题

1. **撤销最近操作**: `Ctrl+Z` 或 `git reset --hard HEAD`
2. **恢复备份**: `git checkout main`
3. **查看日志**: `git log --oneline`
4. **查看差异**: `git diff`

---

## 📝 重构完成后

```bash
# 1. 切回主分支
git checkout main

# 2. 合并重构分支
git merge refactor/preview-integration

# 3. 最终验证
mvn clean test
mvn exec:java -Dexec.args="examples/minimal-square.step"
mvn exec:java -Dexec.args="examples/engine.stp"

# 4. 推送到远程
git push origin main
```

---

**开始时间**: 2026-07-04
**预计完成**: 每个文件 10-15 分钟
**总预计时间**: 60-90 分钟

**加油！按照这个指南一步步操作，很快就能完成！** 🚀