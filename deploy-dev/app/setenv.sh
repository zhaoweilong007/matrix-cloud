#!/bin/bash
# 版本号更新脚本
# 用法: ./setenv.sh <新版本号>

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ENV_FILE="${SCRIPT_DIR}/.env"
APP_ENV_FILE="${SCRIPT_DIR}/dev.env"

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
NC='\033[0m'

log_info() { echo -e "${GREEN}[INFO]${NC} $1"; }
log_error() { echo -e "${RED}[ERROR]${NC} $1"; }

# 显示帮助
show_help() {
    echo "版本号更新脚本"
    echo ""
    echo "用法: $0 <新版本号>"
    echo ""
    echo "示例:"
    echo "  $0 4.0.1    # 更新版本号为 4.0.1"
}

# 参数验证
if [[ $# -lt 1 ]]; then
    log_error "缺少版本号参数"
    show_help
    exit 1
fi

if [[ "$1" == "-h" || "$1" == "--help" ]]; then
    show_help
    exit 0
fi

NEW_VERSION="$1"

# 验证版本号格式（简单验证）
if [[ ! "$NEW_VERSION" =~ ^[0-9]+\.[0-9]+\.[0-9]+$ ]]; then
    log_error "版本号格式错误，应为 x.y.z 格式"
    exit 1
fi

# 检查文件
if [[ ! -f "$ENV_FILE" ]]; then
    log_error ".env 文件不存在: $ENV_FILE"
    exit 1
fi

if [[ ! -f "$APP_ENV_FILE" ]]; then
    log_error "dev.env 文件不存在: $APP_ENV_FILE"
    exit 1
fi

# 更新版本号
log_info "更新版本号为: $NEW_VERSION"

sed -i "s/^VERSION=.*/VERSION=${NEW_VERSION}/" "$ENV_FILE"
sed -i "s/^VERSION=.*/VERSION=${NEW_VERSION}/" "$APP_ENV_FILE"

log_info "更新完成，当前版本配置:"
grep "^VERSION=" "$ENV_FILE" "$APP_ENV_FILE"
