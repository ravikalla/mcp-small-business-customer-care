#\!/bin/bash
echo "Testing MCP server..."
echo '{"jsonrpc":"2.0","method":"initialize","params":{"protocolVersion":"2024-11-05"},"id":0}' | java -jar target/small-business-customer-care-1.0.0.jar &
PID=$\!
sleep 3
kill $PID 2>/dev/null
echo "Test completed"
EOF < /dev/null