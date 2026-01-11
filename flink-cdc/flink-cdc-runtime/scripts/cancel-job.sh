#!/bin/bash
# 取消Flink作业脚本

set -e

JOB_ID=$1

if [ -z "$JOB_ID" ]; then
    echo "Usage: $0 <job-id>"
    echo "List all jobs: /opt/flink/bin/flink list -r"
    exit 1
fi

echo "Cancelling Flink job: $JOB_ID"

/opt/flink/bin/flink cancel "$JOB_ID"

echo "Job cancelled successfully!"
