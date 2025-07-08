package com.server.app.parcial.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.app.parcial.dto.Response;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;

@Configuration
@EnableWebSecurity
public class AuthRole {

    private static final String[] SWAGGER_WHITELIST = {
        "/v3/api-docs/**",
        "/swagger-ui/**",
        "/swagger-ui.html",
        "/swagger-resources/**",
        "/webjars/**"
    };

    private static final String[] PUBLIC_ENDPOINTS = {
        "/api/usuarios/login",
        "/api/usuarios/register",
        "/api/public/**"
    };

    private final JwtFilter jwtFilter;
    private final ObjectMapper objectMapper;

    public AuthRole(@Lazy JwtFilter jwtFilter, ObjectMapper objectMapper) {
        this.jwtFilter = jwtFilter;
        this.objectMapper = objectMapper;
    }

    private String errorResponse(HttpStatus status, String message) throws JsonProcessingException {
        return objectMapper.writeValueAsString(Response.build(status.value(), message, null));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            // Configuración de cabeceras de seguridad
            .headers(headers -> headers
                .contentSecurityPolicy(csp -> csp
                    .policyDirectives("default-src 'self'; script-src 'self'; style-src 'self'; img-src 'self' data:;"))
                .httpStrictTransportSecurity(hsts -> hsts
                    .maxAgeInSeconds(31536000)
                    .includeSubDomains(true)
                    .preload(true))
                .xssProtection(xss -> xss
                    .headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK))
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::deny)
                .contentTypeOptions(Customizer.withDefaults()))
            
            // Configuración básica de seguridad
            .cors(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // Filtro JWT
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            
            // Autorización de endpoints
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS).permitAll() // Permitir preflight requests
                .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                .requestMatchers(SWAGGER_WHITELIST).permitAll()
                
                // Ejemplo de configuración de roles (descomentar y ajustar según necesidades)
                // .requestMatchers(HttpMethod.GET, "/api/actividades/**")
                //     .hasAnyAuthority("ENCARGADO", "INSTRUCTOR_NORMAL", "INSTRUCTOR_REMUNERADO")
                // .requestMatchers("/api/actividades/**")
                //     .hasAuthority("ENCARGADO")
                
                .anyRequest().authenticated())
            
            // Manejo de excepciones
            .exceptionHandling(exceptions -> exceptions
                .accessDeniedHandler((req, res, ex) -> {
                    res.setStatus(HttpStatus.FORBIDDEN.value());
                    res.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    res.getWriter().write(errorResponse(HttpStatus.FORBIDDEN,
                        "Acceso denegado: No tienes los permisos necesarios para este recurso"));
                })
                .authenticationEntryPoint((req, res, ex) -> {
                    res.setStatus(HttpStatus.UNAUTHORIZED.value());
                    res.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    res.getWriter().write(errorResponse(HttpStatus.UNAUTHORIZED,
                        "Autenticación requerida: Por favor inicie sesión con credenciales válidas"));
                }))
            .build();
    }
}