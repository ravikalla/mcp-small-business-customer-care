package com.ravikalla;

import com.ravikalla.mcp.McpServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SmallBusinessCustomerCareApplication implements CommandLineRunner {
    
    @Autowired
    private McpServer mcpServer;
    
    public static void main(String[] args) {
        // Disable the shutdown hook so the app stays alive
        SpringApplication app = new SpringApplication(SmallBusinessCustomerCareApplication.class);
        app.setRegisterShutdownHook(false);
        ConfigurableApplicationContext context = app.run(args);
        
        // Keep the application alive
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            context.close();
        }));
    }
    
    @Override
    public void run(String... args) throws Exception {
        mcpServer.start();
    }
}