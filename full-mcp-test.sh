#\!/bin/bash

# Test the complete MCP flow that Claude Desktop would use
(
  echo '{"jsonrpc":"2.0","method":"initialize","params":{"protocolVersion":"2024-11-05","capabilities":{},"clientInfo":{"name":"claude-ai","version":"0.1.0"}},"id":0}'
  sleep 0.1
  echo '{"jsonrpc":"2.0","method":"initialized","params":{}}'
  sleep 0.1
  echo '{"jsonrpc":"2.0","method":"tools/list","params":{},"id":1}'
  sleep 0.1
  echo '{"jsonrpc":"2.0","method":"tools/call","params":{"name":"list_businesses","arguments":{}},"id":2}'
  sleep 1
) | java -jar target/small-business-customer-care-1.0.0.jar
EOF < /dev/null