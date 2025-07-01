package com.ravikalla;

import com.ravikalla.mcp.McpServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SmallBusinessCustomerCareApplication implements ApplicationRunner {
    
    @Autowired
    private McpServer mcpServer;
    
    public static void main(String[] args) {
        System.setProperty("spring.main.keep-alive", "true");
        SpringApplication.run(SmallBusinessCustomerCareApplication.class, args);
    }
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Start MCP server in a separate thread to prevent blocking Spring Boot
        Thread mcpThread = new Thread(() -> {
            mcpServer.start();
        });
        mcpThread.setDaemon(false); // Keep JVM alive
        mcpThread.start();
        
        // Keep this thread alive to prevent Spring Boot from shutting down
        try {
            mcpThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}