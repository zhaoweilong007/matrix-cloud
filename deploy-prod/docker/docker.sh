#!/bin/bash

/deploy/docker/setenv.sh $2

# 设置 Docker Compose 文件路径
DOCKER_COMPOSE_FILE="/deploy/docker/docker-compose.yml"

# 启动服务
start_services() {
    docker-compose -f "$DOCKER_COMPOSE_FILE" up -d
}

# 停止服务
stop_services() {
    docker-compose -f "$DOCKER_COMPOSE_FILE" stop
}

# 重启服务
restart_services() {
    stop_services
    start_services
}

# 启动指定服务
start_service() {
    service_name=$1
    docker-compose -f "$DOCKER_COMPOSE_FILE" up  -d  "$service_name"
}

# 停止指定服务
stop_service() {
    service_name=$1
    docker-compose -f "$DOCKER_COMPOSE_FILE" stop "$service_name"
}

# 重启指定服务
restart_service() {
    service_name=$1
    docker-compose -f "$DOCKER_COMPOSE_FILE" restart "$service_name"
}


# 删除 <none> 标签的镜像
remove_none_images() {
    if [[ $(docker images -f "dangling=true" -q) ]]; then
        docker rmi $(docker images -f "dangling=true" -q)
    else
        echo "No <none> tagged images found."
    fi
}



# 删除镜像
remove_images() {
    service_name=$1

    if [[ -n "$service_name" ]]; then
        images=$(docker ps -a --filter "name=$service_name" --format "{{.Image}}")
        echo "Images for service $service_name: $images"
    else
        images=$(docker ps -a --format "{{.Image}}" | grep -E ".*matrix.*")
        echo "Images: $images"
    fi

    # 判断镜像列表是否为空
    if [[ -n "$images" ]]; then
        # 遍历镜像列表并删除镜像
        for image in $images
        do
            docker rmi -f "$image"
        done
    else
        echo "No images found with the specified name."
    fi
}



# 处理命令参数
case $1 in
    "start")
        start_services
        ;;
    "stop")
        stop_services
        ;;
    "restart")
        stop_services
        remove_images
        start_services
        ;;
    "start-service")
        start_service "$3"
        ;;
    "stop-service")
        stop_service "$3"
        ;;
    "restart-service")
        stop_service "$3"
        remove_images "$3"
        start_service "$3"
        ;;
    *)
        echo "Usage: $0 {start|stop|restart|start-service|stop-service|restart-service}"
        exit 1
        ;;
esac


remove_none_images