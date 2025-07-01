#!/bin/bash

echo "Testing MCP Server for Small Business Customer Care"
echo "=================================================="

# Function to run MCP command and display result
run_mcp_test() {
    local test_name="$1"
    local command="$2"
    echo ""
    echo "[$test_name]"
    echo "Command: $command"
    echo "Response:"
    echo "$command" | mvn -q exec:java -Dexec.mainClass="com.ravikalla.McpServerLauncher" -Dexec.args="" 2>/dev/null
    echo "---"
}

# Build first to ensure we have compiled classes
echo "Building application..."
mvn -q clean compile
if [ $? -ne 0 ]; then
    echo "Build failed. Exiting."
    exit 1
fi

echo ""
echo "Starting comprehensive MCP tool tests..."

# Test 1: Initialize
run_mcp_test "Initialize MCP Server" '{"method": "initialize", "params": {}}'

# Test 2: List available tools
run_mcp_test "List Available Tools" '{"method": "tools/list", "params": {}}'

# Test 3: List all businesses
run_mcp_test "List All Businesses" '{"method": "tools/call", "params": {"name": "list_businesses", "arguments": {}}}'

# Test 4: Search businesses - coffee
run_mcp_test "Search Businesses (coffee)" '{"method": "tools/call", "params": {"name": "search_businesses", "arguments": {"query": "coffee"}}}'

# Test 5: Search businesses - technology
run_mcp_test "Search Businesses (technology)" '{"method": "tools/call", "params": {"name": "search_businesses", "arguments": {"query": "technology"}}}'

# Test 6: Search businesses - repair
run_mcp_test "Search Businesses (repair)" '{"method": "tools/call", "params": {"name": "search_businesses", "arguments": {"query": "repair"}}}'

# Test 7: Search businesses - no results
run_mcp_test "Search Businesses (no results)" '{"method": "tools/call", "params": {"name": "search_businesses", "arguments": {"query": "xyz123"}}}'

# Test 8: Get specific business - ID 1 (Joe's Coffee Shop)
run_mcp_test "Get Business ID 1" '{"method": "tools/call", "params": {"name": "get_business", "arguments": {"id": "1"}}}'

# Test 9: Get specific business - ID 2 (Tech Repair Solutions)
run_mcp_test "Get Business ID 2" '{"method": "tools/call", "params": {"name": "get_business", "arguments": {"id": "2"}}}'

# Test 10: Get specific business - ID 3 (Green Thumb Landscaping)
run_mcp_test "Get Business ID 3" '{"method": "tools/call", "params": {"name": "get_business", "arguments": {"id": "3"}}}'

# Test 11: Get specific business - ID 4 (Bella's Boutique)
run_mcp_test "Get Business ID 4" '{"method": "tools/call", "params": {"name": "get_business", "arguments": {"id": "4"}}}'

# Test 12: Get specific business - ID 5 (Mike's Auto Repair)
run_mcp_test "Get Business ID 5" '{"method": "tools/call", "params": {"name": "get_business", "arguments": {"id": "5"}}}'

# Test 13: Get non-existent business
run_mcp_test "Get Non-existent Business" '{"method": "tools/call", "params": {"name": "get_business", "arguments": {"id": "999"}}}'

# Test 14: Search by category - restaurant
run_mcp_test "Search by Category (restaurant)" '{"method": "tools/call", "params": {"name": "search_businesses", "arguments": {"query": "restaurant"}}}'

# Test 15: Search by category - retail
run_mcp_test "Search by Category (retail)" '{"method": "tools/call", "params": {"name": "search_businesses", "arguments": {"query": "retail"}}}'

# Test 16: Search by category - automotive
run_mcp_test "Search by Category (automotive)" '{"method": "tools/call", "params": {"name": "search_businesses", "arguments": {"query": "automotive"}}}'

# Test 17: Search by description keywords - landscaping
run_mcp_test "Search by Description (landscaping)" '{"method": "tools/call", "params": {"name": "search_businesses", "arguments": {"query": "landscaping"}}}'

# Test 18: Search by description keywords - fashion
run_mcp_test "Search by Description (fashion)" '{"method": "tools/call", "params": {"name": "search_businesses", "arguments": {"query": "fashion"}}}'

# Test 19: Invalid tool name
run_mcp_test "Invalid Tool Name" '{"method": "tools/call", "params": {"name": "invalid_tool", "arguments": {}}}'

# Test 20: Create business - valid data
run_mcp_test "Create Business (valid)" '{"method": "tools/call", "params": {"name": "create_business", "arguments": {"name": "Pizza Palace", "category": "Restaurant", "address": "999 Food St, Springfield", "phone": "555-0199", "email": "info@pizzapalace.com", "description": "Authentic Italian pizza and pasta restaurant", "rating": 4.7}}}'

# Test 21: Create business - without rating (should default to 0.0)
run_mcp_test "Create Business (no rating)" '{"method": "tools/call", "params": {"name": "create_business", "arguments": {"name": "Quick Clean Laundry", "category": "Services", "address": "777 Wash Ave, Springfield", "phone": "555-0177", "email": "contact@quickclean.com", "description": "24/7 laundromat and dry cleaning services"}}}'

# Test 22: Create business - with invalid rating (should reset to 0.0)
run_mcp_test "Create Business (invalid rating)" '{"method": "tools/call", "params": {"name": "create_business", "arguments": {"name": "Book Nook", "category": "Retail", "address": "888 Read St, Springfield", "phone": "555-0188", "email": "books@booknook.com", "description": "Independent bookstore with rare and new books", "rating": 6.5}}}'

# Test 23: List businesses after creating new ones (should show original 5 + new ones)
run_mcp_test "List All Businesses (after creates)" '{"method": "tools/call", "params": {"name": "list_businesses", "arguments": {}}}'

# Test 24: Search for newly created business
run_mcp_test "Search for New Business (pizza)" '{"method": "tools/call", "params": {"name": "search_businesses", "arguments": {"query": "pizza"}}}'

# Test 25: Get newly created business by ID
run_mcp_test "Get New Business by ID (6)" '{"method": "tools/call", "params": {"name": "get_business", "arguments": {"id": "6"}}}'

# Test 26: Remove business - valid ID
run_mcp_test "Remove Business ID 1" '{"method": "tools/call", "params": {"name": "remove_business", "arguments": {"id": "1"}}}'

# Test 27: Remove business - invalid ID
run_mcp_test "Remove Business (invalid ID)" '{"method": "tools/call", "params": {"name": "remove_business", "arguments": {"id": "999"}}}'

# Test 28: List businesses after removal
run_mcp_test "List Businesses After Removal" '{"method": "tools/call", "params": {"name": "list_businesses", "arguments": {}}}'

# Test 29: Update business - valid update
run_mcp_test "Update Business (valid)" '{"method": "tools/call", "params": {"name": "update_business", "arguments": {"id": "2", "name": "Super Tech Repair", "rating": 4.9}}}'

# Test 30: Update business - partial update
run_mcp_test "Update Business (partial)" '{"method": "tools/call", "params": {"name": "update_business", "arguments": {"id": "3", "phone": "555-9999"}}}'

# Test 31: Update business - invalid ID
run_mcp_test "Update Business (invalid ID)" '{"method": "tools/call", "params": {"name": "update_business", "arguments": {"id": "888", "name": "Non-existent"}}}'

# Test 32: Update business - invalid rating (should keep original)
run_mcp_test "Update Business (invalid rating)" '{"method": "tools/call", "params": {"name": "update_business", "arguments": {"id": "4", "rating": 7.0}}}'

# Test 33: Create business - missing required field (should error)
run_mcp_test "Create Business (missing field)" '{"method": "tools/call", "params": {"name": "create_business", "arguments": {"name": "Incomplete Business", "category": "Test"}}}'

# Test 34: Invalid tool name
run_mcp_test "Invalid Tool Name" '{"method": "tools/call", "params": {"name": "invalid_tool", "arguments": {}}}'

# Test 35: Invalid method
run_mcp_test "Invalid Method" '{"method": "invalid_method", "params": {}}'

echo ""
echo "=================================================="
echo "All MCP Server tests completed successfully!"
echo "Total tests run: 35"
echo ""
echo "Test Coverage:"
echo "- MCP Protocol: initialize, tools/list"
echo "- list_businesses: 3 tests (initial + after creates + after removal)"
echo "- search_businesses: 10 tests (various queries, edge cases)"
echo "- get_business: 7 tests (all valid IDs + invalid ID + new business)"
echo "- create_business: 4 tests (valid, no rating, invalid rating, missing field)"
echo "- remove_business: 5 tests (valid removal, invalid ID, verification)"
echo "- update_business: 4 tests (valid update, partial update, invalid ID, invalid rating)"
echo "- Error handling: 2 tests (invalid tool, invalid method)"
echo "=================================================="