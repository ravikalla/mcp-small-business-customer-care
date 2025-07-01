package com.ravikalla;

import com.ravikalla.mcp.McpServer;

public class McpServerLauncher {
    public static void main(String[] args) {
        // Disable all logging completely for clean MCP communication
        System.setProperty("logging.level.root", "OFF");
        System.setProperty("spring.main.banner-mode", "off");
        
        McpServer server = new McpServer();
        
        // Add shutdown hook to gracefully handle termination
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // Graceful shutdown if needed
        }));
        
        // Start the MCP server - this will block and keep the process alive
        server.start();
    }
}