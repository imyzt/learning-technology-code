#!/bin/bash
# Flink CDC Pipeline提交脚本
# 用于提交YAML配置的Pipeline作业

set -e

PIPELINE_FILE=$1
JOB_NAME=${2:-"CDC Pipeline Job"}

if [ -z "$PIPELINE_FILE" ]; then
    echo "Usage: $0 <pipeline-yaml-file> [job-name]"
    echo "Example: $0 /opt/cdc-pipelines/pg-to-sr-pipeline.yaml"
    exit 1
fi

if [ ! -f "$PIPELINE_FILE" ]; then
    echo "Error: Pipeline file not found: $PIPELINE_FILE"
    exit 1
fi

echo "================================================"
echo "Submitting Flink CDC Pipeline"
echo "================================================"
echo "Pipeline File: $PIPELINE_FILE"
echo "Job Name: $JOB_NAME"
echo "================================================"

# 使用Flink CDC 3.5的提交脚本
if [ ! -f "/opt/flink-cdc/bin/flink-cdc.sh" ]; then
    echo "Error: Flink CDC CLI not found at /opt/flink-cdc/bin/flink-cdc.sh"
    exit 1
fi

echo "Using Flink CDC CLI: /opt/flink-cdc/bin/flink-cdc.sh"

# 提交Pipeline作业（Flink CDC 3.5新方式）
cd /opt/flink-cdc
./bin/flink-cdc.sh "$PIPELINE_FILE"

echo "================================================"
echo "Pipeline submitted successfully!"
echo "Check Flink Web UI: http://localhost:8081"
echo "================================================"
