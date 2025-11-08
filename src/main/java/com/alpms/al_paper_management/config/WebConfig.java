package com.alpms.al_paper_management.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // This tells Spring Boot:
        // "Whenever someone visits /uploads/**,
        // serve files from the 'uploads' directory on your disk."

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}
