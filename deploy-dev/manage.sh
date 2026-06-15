#!/bin/bash
# deploy-dev 统一管理脚本
# 管理: infra(基础设施) / monitor(监控栈) / app(微服务) / all(全部)
set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
INFRA_COMPOSE="$SCRIPT_DIR/docker-compose.yml"
MONITOR_COMPOSE="$SCRIPT_DIR/docker-prometheus.yml"
APP_COMPOSE="$SCRIPT_DIR/app/docker-compose.yml"

# 颜色输出
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m'

log()  { echo -e "${GREEN}[✓] $1${NC}"; }
warn() { echo -e "${YELLOW}[!] $1${NC}"; }
err()  { echo -e "${RED}[✗] $1${NC}"; }

# ─── compose 操作 ───

compose_up() {
    local file="$1" name="$2"
    log "启动 $name ..."
    docker-compose -f "$file" up -d
    log "$name 已启动"
}

compose_down() {
    local file="$1" name="$2"
    log "停止 $name ..."
    docker-compose -f "$file" down
    log "$name 已停止"
}

compose_restart() {
    local file="$1" name="$2"
    log "重启 $name ..."
    docker-compose -f "$file" down
    docker-compose -f "$file" up -d
    log "$name 已重启"
}

compose_ps() {
    local file="$1" name="$2"
    echo -e "${YELLOW}--- $name ---${NC}"
    docker-compose -f "$file" ps
}

compose_logs() {
    local file="$1" name="$2" service="$3"
    if [[ -n "$service" ]]; then
        docker-compose -f "$file" logs -f --tail=200 "$service"
    else
        docker-compose -f "$file" logs --tail=50
    fi
}

compose_service_up() {
    local file="$1" name="$2" service="$3"
    log "启动 $name / $service ..."
    docker-compose -f "$file" up -d "$service"
}

compose_service_down() {
    local file="$1" name="$2" service="$3"
    log "停止 $name / $service ..."
    docker-compose -f "$file" stop "$service"
}

# ─── 组件选择 ───

resolve_files() {
    local target="$1"
    case "$target" in
        infra)   echo "$INFRA_COMPOSE infra" ;;
        monitor) echo "$MONITOR_COMPOSE monitor" ;;
        app)     echo "$APP_COMPOSE app" ;;
        all)     echo "$INFRA_COMPOSE infra $MONITOR_COMPOSE monitor $APP_COMPOSE app" ;;
        *)       err "未知组件: $target (可选: infra|monitor|app|all)"; exit 1 ;;
    esac
}

# ─── 帮助 ───

show_help() {
    cat <<EOF
Matrix-Cloud 开发环境管理脚本

用法: $(basename "$0") <action> [component] [service]

动作:
  up       启动
  down     停止并移除容器
  restart  重启(down + up)
  ps       查看容器状态
  logs     查看日志(指定service则跟踪输出)
  clean    清理悬空镜像

组件(不指定则默认 all):
  infra    基础设施(mysql/redis/nacos/sentinel/seata/xxl-job)
  monitor  监控栈(es/skywalking/prometheus/grafana)
  app      微服务(gateway/resource/system/admin)
  all      全部

示例:
  $(basename "$0") up                  # 启动全部
  $(basename "$0") up infra            # 仅启动基础设施
  $(basename "$0") up infra nacos      # 仅启动 nacos
  $(basename "$0") down monitor        # 停止监控栈
  $(basename "$0") logs infra mysql    # 查看 mysql 日志
  $(basename "$0") restart app         # 重启微服务
  $(basename "$0") clean               # 清理悬空镜像
EOF
}

# ─── 主逻辑 ───

ACTION="$1"
COMPONENT="$2"
SERVICE="$3"

[[ -z "$ACTION" ]] && { show_help; exit 0; }

case "$ACTION" in
    -h|--help|help) show_help; exit 0 ;;
esac

# clean 不需要组件参数
if [[ "$ACTION" == "clean" ]]; then
    log "清理悬空镜像 ..."
    dangling=$(docker images -f "dangling=true" -q)
    if [[ -n "$dangling" ]]; then
        docker rmi "$dangling"
        log "已清理悬空镜像"
    else
        warn "无悬空镜像"
    fi
    exit 0
fi

# 默认组件为 all
[[ -z "$COMPONENT" ]] && COMPONENT="all"

# 对 all 做批量操作；对单个组件支持指定 service
if [[ "$COMPONENT" == "all" ]]; then
    case "$ACTION" in
        up)
            compose_up "$INFRA_COMPOSE"   "infra"
            compose_up "$MONITOR_COMPOSE" "monitor"
            compose_up "$APP_COMPOSE"     "app"
            ;;
        down)
            compose_down "$APP_COMPOSE"     "app"
            compose_down "$MONITOR_COMPOSE" "monitor"
            compose_down "$INFRA_COMPOSE"   "infra"
            ;;
        restart)
            compose_down "$APP_COMPOSE"     "app"
            compose_down "$MONITOR_COMPOSE" "monitor"
            compose_down "$INFRA_COMPOSE"   "infra"
            compose_up "$INFRA_COMPOSE"     "infra"
            compose_up "$MONITOR_COMPOSE"   "monitor"
            compose_up "$APP_COMPOSE"       "app"
            ;;
        ps)
            compose_ps "$INFRA_COMPOSE"   "infra"
            compose_ps "$MONITOR_COMPOSE" "monitor"
            compose_ps "$APP_COMPOSE"     "app"
            ;;
        logs)
            compose_logs "$INFRA_COMPOSE"   "infra"
            compose_logs "$MONITOR_COMPOSE" "monitor"
            compose_logs "$APP_COMPOSE"     "app"
            ;;
        *)
            err "未知动作: $ACTION"; show_help; exit 1 ;;
    esac
else
    # 单个组件操作
    read file name <<< "$(resolve_files "$COMPONENT")"
    case "$ACTION" in
        up)
            [[ -n "$SERVICE" ]] && compose_service_up "$file" "$name" "$SERVICE" \
                                || compose_up "$file" "$name"
            ;;
        down)
            [[ -n "$SERVICE" ]] && compose_service_down "$file" "$name" "$SERVICE" \
                                || compose_down "$file" "$name"
            ;;
        restart) compose_restart "$file" "$name" ;;
        ps)      compose_ps "$file" "$name" ;;
        logs)    compose_logs "$file" "$name" "$SERVICE" ;;
        *)
            err "未知动作: $ACTION"; show_help; exit 1 ;;
    esac
fi
