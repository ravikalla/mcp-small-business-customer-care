package com.ravikalla;

import com.ravikalla.mcp.McpServer;

public class McpServerLauncher {
    public static void main(String[] args) {
        McpServer server = new McpServer();
        server.start();
    }
}