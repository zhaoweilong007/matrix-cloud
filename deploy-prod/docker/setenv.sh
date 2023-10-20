#!/bin/bash

# 设置.env文件路径
ENV_FILE="/deploy/docker/.env"

# 设置应用.env文件路径
APP_ENV_FILE='/deploy/docker/prod.env'

# 设置要设置的新值
NEW_VERSION=$1

# 检查.env文件是否存在
if [[ ! -f "$ENV_FILE" ]]; then
    echo "Error: .env file not found."
    exit 1
fi

if [[ ! -f "$APP_ENV_FILE" ]]; then
    echo "Error: prod.env file not found."
    exit 1
fi


# 将VERSION环境变量的值替换为新值
sed -i "s/VERSION=.*/VERSION=$NEW_VERSION/" "$ENV_FILE"

sed -i "s/VERSION=.*/VERSION=$NEW_VERSION/" "$APP_ENV_FILE"

echo "Updated VERSION to $NEW_VERSION in .env file and prod.env"