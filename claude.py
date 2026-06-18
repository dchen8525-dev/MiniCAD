import subprocess
import time
import datetime
import psutil

# ===================== 配置区域 =====================
RUN_INTERVAL_MIN = 10
PROMPT_TEXT = "将当前项目切换为JDK11，手动修复编译错误"
# 完整claude命令
CLAUDE_CMD = f'claude --dangerously-skip-permissions "{PROMPT_TEXT}"'
# ====================================================

def kill_claude_powershell():
    kill_count = 0
    for proc in psutil.process_iter(["pid", "name", "cmdline"]):
        try:
            proc_name = proc.info.get("name", "").lower()
            cmdline_data = proc.info.get("cmdline") or []
            full_cmd = " ".join(cmdline_data).lower()

            if proc_name == "powershell.exe" and "claude" in full_cmd:
                proc.kill()
                kill_count += 1
                now = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
                print(f"[{now}] 杀死旧PS窗口 PID:{proc.info['pid']}")
        except (psutil.NoSuchProcess, psutil.AccessDenied):
            continue
    if kill_count == 0:
        now = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        print(f"[{now}] 无正在运行claude的PowerShell")


def open_new_ps_window():
    """直接调用powershell.exe自带新开窗口参数，无参数解析bug"""
    # -NoExit 执行完不关闭窗口
    # /c 执行命令；start 强制弹出独立窗口
    cmd = [
        "cmd", "/c",
        "start", "powershell", "-NoExit", "-Command", CLAUDE_CMD
    ]
    result = subprocess.run(cmd, text=True)
    now = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    if result.returncode == 0:
        print(f"[{now}] 成功弹出PowerShell窗口执行指令")
    else:
        print(f"[{now}] 启动窗口失败，返回码：{result.returncode}")


def main():
    print("=== Claude定时启动脚本 ===")
    print(f"每{RUN_INTERVAL_MIN}分钟执行一次")
    print("流程：杀掉旧PS窗口 → 弹出新窗口运行claude\n")
    interval_sec = RUN_INTERVAL_MIN * 60

    try:
        while True:
            now = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
            print(f"\n==== 定时触发 {now} ====")
            kill_claude_powershell()
            time.sleep(1)
            open_new_ps_window()
            print(f"等待{RUN_INTERVAL_MIN}分钟...")
            time.sleep(interval_sec)
    except KeyboardInterrupt:
        print("\n脚本手动终止")


if __name__ == "__main__":
    main()