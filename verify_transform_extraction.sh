#!/bin/bash
# MeshTransformUtils.java 提取验证脚本

echo "===================================================================================================="
echo "MeshTransformUtils.java 提取验证"
echo "===================================================================================================="

# 设置Java环境
export JAVA_HOME="/c/Users/admin/.jdks/ms-11.0.31"
export PATH="$JAVA_HOME/bin:$PATH"

# 检查新文件是否创建
echo ""
echo "1. 检查新文件是否创建:"
if [ -f "src/main/java/com/minicad/app/MeshTransformUtils.java" ]; then
    echo "   ✅ MeshTransformUtils.java 已创建"
    lines=$(wc -l < src/main/java/com/minicad/app/MeshTransformUtils.java)
    echo "   ✅ 文件行数: $lines 行"
    
    # 检查方法是否存在
    methods=$(grep -c "public static" src/main/java/com/minicad/app/MeshTransformUtils.java 2>/dev/null || echo 0)
    echo "   ✅ Public static方法数: ~$methods 个"
else
    echo "   ❌ MeshTransformUtils.java 未创建"
    echo "   ⚠️  请在IDEA中完成Extract Class操作"
    exit 1
fi

# 检查源文件变化
echo ""
echo "2. 检查源文件变化:"
if [ -f "src/main/java/com/minicad/app/StepMeshExporter.java" ]; then
    old_lines=2858
    current_lines=$(wc -l < src/main/java/com/minicad/app/StepMeshExporter.java)
    diff=$((old_lines - current_lines))
    
    echo "   原始行数: $old_lines 行"
    echo "   当前行数: $current_lines 行"
    echo "   减少行数: $diff 行"
    
    if [ $diff -gt 200 ]; then
        echo "   ✅ 行数减少符合预期（~250行）"
    else
        echo "   ⚠️  行数减少较少，可能未完全提取"
    fi
    
    # 检查transform方法是否已删除
    transform_count=$(grep -c "private.*transform" src/main/java/com/minicad/app/StepMeshExporter.java 2>/dev/null || echo 0)
    if [ $transform_count -lt 3 ]; then
        echo "   ✅ Transform方法已从源文件删除"
    else
        echo "   ⚠️  源文件仍有transform方法定义"
    fi
    
    # 检查是否有MeshTransformUtils调用
    utils_calls=$(grep -c "MeshTransformUtils\." src/main/java/com/minicad/app/StepMeshExporter.java 2>/dev/null || echo 0)
    if [ $utils_calls -gt 20 ]; then
        echo "   ✅ 已找到 MeshTransformUtils 调用 ($utils_calls 次)"
    else
        echo "   ⚠️  MeshTransformUtils 调用较少 ($utils_calls 次)"
    fi
else
    echo "   ❌ StepMeshExporter.java 不存在"
    exit 1
fi

# 编译验证
echo ""
echo "3. 编译验证:"
cd /d/work/MiniCAD
mvn clean compile -DskipTests 2>&1 | tail -20

if echo "$output" | grep -q "BUILD SUCCESS"; then
    echo "   ✅ 编译成功"
else
    echo "   ❌ 编译失败"
    echo "   ⚠️  请检查IDEA中的错误提示"
    exit 1
fi

# 检查import语句
echo ""
echo "4. 检查import语句:"
if grep -q "import com.minicad.app.MeshTransformUtils" src/main/java/com/minicad/app/StepMeshExporter.java; then
    echo "   ✅ import语句已添加"
else
    echo "   ⚠️  import语句可能缺失"
    echo "   请在IDEA中执行: Code → Optimize Imports"
fi

# 最终检查
echo ""
echo "===================================================================================================="
echo "验证结果总结:"
echo "===================================================================================================="

if [ -f "src/main/java/com/minicad/app/MeshTransformUtils.java" ] && \
   [ $diff -gt 200 ] && \
   echo "$output" | grep -q "BUILD SUCCESS"; then
    echo ""
    echo "✅ 提取成功！"
    echo "✅ MeshTransformUtils.java 已创建并编译通过"
    echo "✅ StepMeshExporter.java 已更新并编译通过"
    echo ""
    echo "下一步建议:"
    echo "  1. Git提交: git add -A && git commit -m 'Extract MeshTransformUtils'"
    echo "  2. Git推送: git push origin main"
    echo "  3. 继续提取: MeshEarClipper.java (16个方法，30-40分钟)"
    echo ""
else
    echo ""
    echo "⚠️  提取可能未完全成功"
    echo "请检查IDEA中的问题并重新验证"
    echo ""
fi

echo "===================================================================================================="