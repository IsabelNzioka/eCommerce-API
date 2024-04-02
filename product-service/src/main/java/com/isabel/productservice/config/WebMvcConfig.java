package com.isabel.productservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig  implements WebMvcConfigurer{

    @Override
    public void addCorsMappings(@SuppressWarnings("null") CorsRegistry registry) {
        registry.addMapping("/api/**") 
                .allowedOrigins("http://localhost:1841") 
                .allowedMethods("GET", "POST", "PUT", "DELETE") 
                .allowedHeaders("*") 
                .allowCredentials(true); 
    }
    
}
