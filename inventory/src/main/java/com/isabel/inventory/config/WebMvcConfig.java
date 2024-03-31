package com.isabel.inventory.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig  implements WebMvcConfigurer{

    @Override
    public void addCorsMappings(@SuppressWarnings("null") CorsRegistry registry) {
        registry.addMapping("/api/**") // Apply CORS configuration to all API endpoints
                .allowedOrigins("http://localhost:1841") // Allow requests from this origin
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Allow specified HTTP methods
                .allowedHeaders("*") // Allow all headers
                .allowCredentials(true); // Allow credentials such as cookies, if required
    }
    
}
