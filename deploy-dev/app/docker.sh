#!/bin/bash
# Matrix Cloud 应用服务管理脚本
# 用法: ./docker.sh [命令] [服务名]

set -e

# 脚本所在目录
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
DOCKER_COMPOSE_FILE="${SCRIPT_DIR}/docker-compose.yml"
ENV_FILE="${SCRIPT_DIR}/.env"

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 日志函数
log_info() { echo -e "${GREEN}[INFO]${NC} $1"; }
log_warn() { echo -e "${YELLOW}[WARN]${NC} $1"; }
log_error() { echo -e "${RED}[ERROR]${NC} $1"; }

# 显示帮助
show_help() {
    echo -e "${BLUE}Matrix Cloud 应用服务管理${NC}"
    echo ""
    echo "用法: $0 <命令> [服务名]"
    echo ""
    echo "命令:"
    echo "  start              启动所有服务"
    echo "  stop               停止所有服务"
    echo "  restart            重启所有服务（不删除镜像）"
    echo "  rebuild <服务名>   重建并启动指定服务（删除旧镜像）"
    echo "  status             查看服务状态"
    echo "  logs [服务名]      查看服务日志（Ctrl+C退出）"
    echo "  logs-tail [服务名] 查看最后100行日志"
    echo "  ps                 显示容器列表"
    echo "  clean              清理停止的容器和悬空镜像"
    echo "  version <版本号>   更新版本号"
    echo ""
    echo "示例:"
    echo "  $0 start                    # 启动所有服务"
    echo "  $0 restart matrix-gateway   # 重启gateway"
    echo "  $0 logs matrix-gateway      # 查看gateway日志"
    echo "  $0 version 4.0.1            # 更新版本号"
}

# 检查依赖
check_deps() {
    if ! command -v docker &> /dev/null; then
        log_error "docker 未安装"
        exit 1
    fi
    if ! command -v docker-compose &> /dev/null; then
        log_error "docker-compose 未安装"
        exit 1
    fi
}

# 检查配置文件
check_files() {
    if [[ ! -f "$DOCKER_COMPOSE_FILE" ]]; then
        log_error "docker-compose.yml 不存在: $DOCKER_COMPOSE_FILE"
        exit 1
    fi
    if [[ ! -f "$ENV_FILE" ]]; then
        log_error ".env 不存在: $ENV_FILE"
        exit 1
    fi
}

# 启动所有服务
start_services() {
    log_info "启动所有服务..."
    docker-compose -f "$DOCKER_COMPOSE_FILE" up -d
    log_info "启动完成"
    show_status
}

# 停止所有服务
stop_services() {
    log_info "停止所有服务..."
    docker-compose -f "$DOCKER_COMPOSE_FILE" stop
    log_info "停止完成"
}

# 重启所有服务（不删除镜像）
restart_services() {
    log_info "重启所有服务..."
    docker-compose -f "$DOCKER_COMPOSE_FILE" restart
    log_info "重启完成"
    show_status
}

# 重建指定服务（删除旧容器，重新创建）
rebuild_service() {
    local service=$1
    if [[ -z "$service" ]]; then
        log_error "请指定服务名"
        echo "可用服务: matrix-gateway, matrix-resource, matrix-system, matrix-admin"
        exit 1
    fi
    log_info "重建服务: $service"
    docker-compose -f "$DOCKER_COMPOSE_FILE" up -d --force-recreate "$service"
    log_info "重建完成"
    show_service_status "$service"
}

# 启动指定服务
start_service() {
    local service=$1
    if [[ -z "$service" ]]; then
        log_error "请指定服务名"
        exit 1
    fi
    log_info "启动服务: $service"
    docker-compose -f "$DOCKER_COMPOSE_FILE" up -d "$service"
}

# 停止指定服务
stop_service() {
    local service=$1
    if [[ -z "$service" ]]; then
        log_error "请指定服务名"
        exit 1
    fi
    log_info "停止服务: $service"
    docker-compose -f "$DOCKER_COMPOSE_FILE" stop "$service"
}

# 重启指定服务
restart_service() {
    local service=$1
    if [[ -z "$service" ]]; then
        log_error "请指定服务名"
        exit 1
    fi
    log_info "重启服务: $service"
    docker-compose -f "$DOCKER_COMPOSE_FILE" restart "$service"
}

# 显示服务状态
show_status() {
    echo ""
    echo -e "${BLUE}=== 服务状态 ===${NC}"
    docker-compose -f "$DOCKER_COMPOSE_FILE" ps
    echo ""
    echo -e "${BLUE}=== 资源使用 ===${NC}"
    docker stats --no-stream --format "table {{.Name}}\t{{.MemUsage}}\t{{.MemPerc}}\t{{.CPUPerc}}" \
        $(docker-compose -f "$DOCKER_COMPOSE_FILE" ps -q 2>/dev/null) 2>/dev/null || true
}

# 显示单个服务状态
show_service_status() {
    local service=$1
    echo ""
    docker-compose -f "$DOCKER_COMPOSE_FILE" ps "$service"
}

# 查看日志
show_logs() {
    local service=$1
    if [[ -n "$service" ]]; then
        log_info "查看 $service 日志 (Ctrl+C 退出)..."
        docker-compose -f "$DOCKER_COMPOSE_FILE" logs -f "$service"
    else
        log_info "查看所有服务日志 (Ctrl+C 退出)..."
        docker-compose -f "$DOCKER_COMPOSE_FILE" logs -f
    fi
}

# 查看最后N行日志
show_logs_tail() {
    local service=$1
    if [[ -n "$service" ]]; then
        docker-compose -f "$DOCKER_COMPOSE_FILE" logs --tail=100 "$service"
    else
        docker-compose -f "$DOCKER_COMPOSE_FILE" logs --tail=100
    fi
}

# 显示容器列表
show_ps() {
    docker-compose -f "$DOCKER_COMPOSE_FILE" ps
}

# 清理资源
clean_resources() {
    log_info "清理停止的容器..."
    docker container prune -f
    log_info "清理悬空镜像..."
    docker image prune -f
    log_info "清理完成"
}

# 更新版本号
update_version() {
    local new_version=$1
    if [[ -z "$new_version" ]]; then
        log_error "请指定版本号"
        echo "示例: $0 version 4.0.1"
        exit 1
    fi
    
    log_info "更新版本号为: $new_version"
    
    # 更新 .env
    sed -i "s/^VERSION=.*/VERSION=${new_version}/" "$ENV_FILE"
    
    # 更新 dev.env
    sed -i "s/^VERSION=.*/VERSION=${new_version}/" "${SCRIPT_DIR}/dev.env"
    
    log_info "版本号已更新"
    grep "^VERSION=" "$ENV_FILE" "${SCRIPT_DIR}/dev.env"
}

# 主逻辑
main() {
    check_deps
    check_files
    
    case "${1:-}" in
        start)
            start_services
            ;;
        stop)
            stop_services
            ;;
        restart)
            restart_services
            ;;
        rebuild)
            rebuild_service "$2"
            ;;
        status)
            show_status
            ;;
        logs)
            show_logs "$2"
            ;;
        logs-tail)
            show_logs_tail "$2"
            ;;
        ps)
            show_ps
            ;;
        clean)
            clean_resources
            ;;
        version)
            update_version "$2"
            ;;
        start-service)
            start_service "$2"
            ;;
        stop-service)
            stop_service "$2"
            ;;
        restart-service)
            restart_service "$2"
            ;;
        *)
            show_help
            exit 1
            ;;
    esac
}

main "$@"
