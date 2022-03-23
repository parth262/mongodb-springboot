package com.example.mongospringboottest.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

@Configuration
public class SwaggerJsonConfig {
    
    @Bean
    public SwaggerJson swaggerConfig() throws IOException {
        File file = ResourceUtils.getFile("classpath:swagger.json");
        Path path = Paths.get(file.getPath());
        String jsonString = Files.readString(path);
        return new SwaggerJson(jsonString);
    }
    
}
