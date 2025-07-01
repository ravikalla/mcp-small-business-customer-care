#!/bin/bash
set -e
cd "$(dirname "$0")"

# Fast startup using Spring Boot fat JAR with custom main class
exec java -Dloader.main=com.ravikalla.McpServerLauncher -jar target/small-business-customer-care-1.0.0.jar 2>/dev/null