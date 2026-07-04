#!/usr/bin/env python3
"""精确替换 record-style 访问器为 Java Bean 风格"""

import re
import sys
from pathlib import Path

def replace_instance_method(content, method_name, getter_name):
    """替换实例方法调用，排除静态方法调用"""
    # 匹配 variableName.methodName()，其中 variableName 以小写字母或下划线开头
    # 使用正向后瞻确保前面是小写字母或下划线
    pattern = rf'(?<=[a-z_])\.{method_name}\(\)'
    replacement = f'.{getter_name}()'
    return re.sub(pattern, replacement, content)

def process_file(file_path):
    """处理单个文件"""
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()
    
    original = content
    
    # 替换 x(), y(), z() → getX(), getY(), getZ()
    content = replace_instance_method(content, 'x', 'getX')
    content = replace_instance_method(content, 'y', 'getY')
    content = replace_instance_method(content, 'z', 'getZ')
    
    if content != original:
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(content)
        return True
    return False

def main():
    """主函数"""
    src_dir = Path('src/main/java')
    changed_files = []
    
    for java_file in src_dir.rglob('*.java'):
        if process_file(java_file):
            changed_files.append(java_file)
            print(f'Updated: {java_file}')
    
    print(f'\nTotal files changed: {len(changed_files)}')

if __name__ == '__main__':
    main()
