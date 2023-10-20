#!/bin/bash

container=filebeat_8.9.0

## 注1> /dev/null 表示只把标准输出丢弃,错误输出保留

if [ "$1" = "stop" ];then
  docker ps|grep $container 1> /dev/null
  if [ $? -eq 0 ];then
    docker stop $container
    echo "operation success......"
  else
    echo "[${container}] possible not exist......"
  fi
  exit
fi

## 判断该镜像及容器是否存在
docker images|grep elastic/filebeat|grep 8.9.0 1> /dev/null && docker ps -a -f "name=filebeat_8.9.0" |grep filebeat_8.9.0 1> /dev/null
if [ $? -eq 1 ];then
  docker run \
  --restart=always \
  --log-driver json-file \
  --log-opt max-size=100m \
  --log-opt max-file=2  \
  --name $container \
  --user=root -d  \
  -v /data/logs/big_log/:/data/logs/big_log/ \
  -v /data/filebeat/filebeat.yml:/usr/share/filebeat/filebeat.yml \
  elastic/filebeat:8.9.0
else
  ## 存在则直接重启相应容器
  docker restart $container
fi