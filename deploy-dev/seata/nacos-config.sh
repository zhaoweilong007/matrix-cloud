#!/bin/bash
# Import seataServer.properties into Nacos (Nacos v3.1.1 compatible)
# Nacos v3.x 的 v1 API auth 默认关闭，不需要登录获取 token

set -e

# ================== 基础配置 ==================
NACOS_ADDR=${NACOS_ADDR:-"nacos"}
NACOS_PORT=${NACOS_PORT:-"8848"}
NACOS_GROUP=${NACOS_GROUP:-"SEATA_GROUP"}
NACOS_NAMESPACE=${NACOS_NAMESPACE:-""}   # namespaceId

DATA_ID=${DATA_ID:-"seataServer.properties"}
CONFIG_FILE=${CONFIG_FILE:-"/seata/seataServer.properties"}

echo "================================================="
echo " Seata Server Config Importer (Nacos v3.x)"
echo "-------------------------------------------------"
echo " Server    : http://${NACOS_ADDR}:${NACOS_PORT}"
echo " Namespace : ${NACOS_NAMESPACE:-<default>}"
echo " Group     : ${NACOS_GROUP}"
echo " DataId    : ${DATA_ID}"
echo " File      : ${CONFIG_FILE}"
echo "================================================="
echo

# ================== 校验 ==================
command -v curl >/dev/null || { echo "curl not found"; exit 1; }

if [ ! -f "$CONFIG_FILE" ]; then
  echo "❌ Config file not found: $CONFIG_FILE"
  exit 1
fi

# ================== 等待 Nacos 就绪 ==================
echo "⏳ Waiting for Nacos to be ready..."
until curl -sf "http://${NACOS_ADDR}:${NACOS_PORT}/nacos/actuator/health" > /dev/null 2>&1; do
  sleep 3
done
echo "✅ Nacos is ready"

# ================== 读取配置内容 ==================
config_content=$(cat "$CONFIG_FILE")

# ================== 构建 URL ==================
api_url="http://${NACOS_ADDR}:${NACOS_PORT}/nacos/v1/cs/configs"
api_url="${api_url}?dataId=$(printf "%s" "$DATA_ID" | jq -sRr @uri)"
api_url="${api_url}&group=$(printf "%s" "$NACOS_GROUP" | jq -sRr @uri)"
api_url="${api_url}&type=properties"
if [ -n "$NACOS_NAMESPACE" ]; then
  api_url="${api_url}&namespaceId=$(printf "%s" "$NACOS_NAMESPACE" | jq -sRr @uri)"
fi

# ================== 发布配置 ==================
echo "🚀 Publishing Seata Server config to Nacos..."

resp=$(curl -sS -X POST \
  -H "Content-Type: application/x-www-form-urlencoded" \
  --data-urlencode "content=${config_content}" \
  "$api_url")

if [ "$resp" = "true" ]; then
  echo "🎉 seataServer.properties published successfully!"
else
  echo "❌ Publish failed. Response:"
  echo "$resp"
  exit 1
fi
