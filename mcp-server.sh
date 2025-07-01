#!/bin/bash
set -e
cd "$(dirname "$0")"

# Ensure Maven can find the project
if [ ! -f "pom.xml" ]; then
    echo "Error: pom.xml not found" >&2
    exit 1
fi

# Set Maven options for faster startup
export MAVEN_OPTS="-Xmx256m -XX:+UseG1GC -XX:+DisableExplicitGC"

# Suppress stderr to prevent Java warnings from interfering with MCP protocol
exec mvn -q exec:java -Dexec.mainClass=com.ravikalla.McpServerLauncher -Dexec.args="" 2>/dev/null