package com.ravikalla;

import com.ravikalla.mcp.McpServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SmallBusinessCustomerCareApplication implements CommandLineRunner {
    
    @Autowired
    private McpServer mcpServer;
    
    public static void main(String[] args) {
        SpringApplication.run(SmallBusinessCustomerCareApplication.class, args);
    }
    
    @Override
    public void run(String... args) throws Exception {
        mcpServer.start();
    }
}