#!/bin/bash

echo "Testing MCP Server for Small Business Customer Care"
echo "=================================================="

# Function to run MCP command and display result
run_mcp_test() {
    local test_name="$1"
    local command="$2"
    echo ""
    echo "[$test_name]"
    echo "Response:"
    echo "$command" | mvn -q exec:java -Dexec.mainClass="com.ravikalla.McpServerLauncher" 2>/dev/null
    echo "---"
}

# Build the project
echo "Building application..."
mvn -q clean compile
if [ $? -ne 0 ]; then
    echo "Build failed. Exiting."
    exit 1
fi

echo ""
echo "Running comprehensive MCP server tests..."

# Test 1: Initialize
run_mcp_test "Initialize MCP Server" '{"method": "initialize", "params": {}}'

# Test 2: List available tools
run_mcp_test "List Available Tools" '{"method": "tools/list", "params": {}}'

# Test 3: List all businesses
run_mcp_test "List All Businesses" '{"method": "tools/call", "params": {"name": "list_businesses", "arguments": {}}}'

# Test 4: Search businesses
run_mcp_test "Search Businesses (coffee)" '{"method": "tools/call", "params": {"name": "search_businesses", "arguments": {"query": "coffee"}}}'

# Test 5: Get specific business
run_mcp_test "Get Business ID 1" '{"method": "tools/call", "params": {"name": "get_business", "arguments": {"id": "1"}}}'

# Test 6: Create business
run_mcp_test "Create Business" '{"method": "tools/call", "params": {"name": "create_business", "arguments": {"name": "Test Pizza Place", "category": "Restaurant", "address": "123 Test Ave", "phone": "555-1234", "email": "test@pizza.com", "description": "Test pizza restaurant", "rating": 4.0}}}'

# Test 7: Update business
run_mcp_test "Update Business ID 1" '{"method": "tools/call", "params": {"name": "update_business", "arguments": {"id": "1", "name": "Updated Coffee Shop", "rating": 4.8}}}'

# Test 8: Remove business
run_mcp_test "Remove Business ID 1" '{"method": "tools/call", "params": {"name": "remove_business", "arguments": {"id": "1"}}}'

# Test 9: List businesses after removal
run_mcp_test "List Businesses After Removal" '{"method": "tools/call", "params": {"name": "list_businesses", "arguments": {}}}'

# Test 10: Try to remove non-existent business
run_mcp_test "Remove Non-existent Business" '{"method": "tools/call", "params": {"name": "remove_business", "arguments": {"id": "999"}}}'

# Test 11: Update non-existent business
run_mcp_test "Update Non-existent Business" '{"method": "tools/call", "params": {"name": "update_business", "arguments": {"id": "999", "name": "Does Not Exist"}}}'

# Test 12: Error handling - invalid tool
run_mcp_test "Invalid Tool" '{"method": "tools/call", "params": {"name": "invalid_tool", "arguments": {}}}'

echo ""
echo "=================================================="
echo "All MCP Server tests completed!"
echo "Test Coverage:"
echo "- Initialize: 1 test"
echo "- Tools List: 1 test"
echo "- List Businesses: 2 tests"
echo "- Search Businesses: 1 test"
echo "- Get Business: 1 test"
echo "- Create Business: 1 test"
echo "- Update Business: 2 tests"
echo "- Remove Business: 2 tests"
echo "- Error Handling: 1 test"
echo "Total: 12 tests"
echo "=================================================="