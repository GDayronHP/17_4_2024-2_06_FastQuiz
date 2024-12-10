package com.secure.fastquiz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:5173"); // Dominio permitido
        configuration.addAllowedMethod("*"); // Métodos permitidos (GET, POST, PUT, DELETE, etc.)
        configuration.addAllowedHeader("*"); // Encabezados permitidos
        configuration.setAllowCredentials(true); // Permitir credenciales (cookies, autenticación)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Aplica CORS a todas las rutas

        return new CorsFilter(source);
    }
}

