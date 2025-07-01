package com.ravikalla;

import com.ravikalla.mcp.McpServer;

public class McpServerLauncher {
    public static void main(String[] args) {
        // Disable all Spring Boot logging completely
        System.setProperty("logging.level.root", "OFF");
        System.setProperty("spring.main.banner-mode", "off");
        
        McpServer server = new McpServer();
        server.start();
    }
}