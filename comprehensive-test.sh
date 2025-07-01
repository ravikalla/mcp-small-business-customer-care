#\!/bin/bash

echo "Testing MCP server with multiple requests..."

# Start the server in background
java -jar target/small-business-customer-care-1.0.0.jar &
SERVER_PID=$\!

# Give it a moment to start
sleep 1

# Test initialize
echo "Testing initialize..."
echo '{"jsonrpc":"2.0","method":"initialize","params":{"protocolVersion":"2024-11-05"},"id":0}' | java -jar target/small-business-customer-care-1.0.0.jar

echo ""
echo "Testing tools/list..."
echo '{"jsonrpc":"2.0","method":"tools/list","params":{},"id":1}' | java -jar target/small-business-customer-care-1.0.0.jar

echo ""
echo "Testing list_businesses tool..."
echo '{"jsonrpc":"2.0","method":"tools/call","params":{"name":"list_businesses","arguments":{}},"id":2}' | java -jar target/small-business-customer-care-1.0.0.jar

# Clean up
kill $SERVER_PID 2>/dev/null
echo ""
echo "Test completed"
EOF < /dev/null