package com.ravikalla;

import com.ravikalla.service.BusinessService;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class SmallBusinessCustomerCareApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmallBusinessCustomerCareApplication.class, args);
    }

    @Bean
    public List<ToolCallback> businessTools(BusinessService businessService) {
        return List.of(ToolCallbacks.from(businessService));
    }

}