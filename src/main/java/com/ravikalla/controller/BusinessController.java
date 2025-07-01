package com.ravikalla.controller;

import com.ravikalla.mcp.McpServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class BusinessController {
    
    @Autowired
    private McpServer mcpServer;
    
    @GetMapping("/health")
    public String health() {
        return "Small Business Customer Care MCP Server is running";
    }
    
    @PostMapping("/mcp/start")
    public String startMcpServer() {
        new Thread(() -> mcpServer.start()).start();
        return "MCP Server started";
    }
}