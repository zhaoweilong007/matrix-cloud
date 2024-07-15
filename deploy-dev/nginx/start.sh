#!/bin/bash

docker stop nginx
docker rm nginx
docker run -idt -p 80:80 -p 443:443 --name nginx --restart=always \
    -v `pwd`/conf/:/etc/nginx \
    -v `pwd`/logs/:/var/log/nginx  \
    -v `pwd`/html/crossdomain.xml:/usr/share/nginx/html/crossdomain.xml  \
    -e TZ="Asia/Shanghai" \
    nginx:1.17.0

