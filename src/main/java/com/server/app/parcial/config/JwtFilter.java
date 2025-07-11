package com.server.app.parcial.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.app.parcial.model.Usuario;
import com.server.app.parcial.service.UsuarioService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    private final UsuarioService usuarioService;
    private final JwtConfig jwtConfig;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(@SuppressWarnings("null") HttpServletRequest request,
            @SuppressWarnings("null") HttpServletResponse response,
            @SuppressWarnings("null") FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = extractToken(request);

            if (token == null) {
                filterChain.doFilter(request, response);
                return;
            }

            if (jwtConfig.isTokenExpired(token)) {
                sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Token expirado");
                return;
            }

            int idUser = jwtConfig.extracClaims(token).get("id", Integer.class);
            System.out.println("Token: " + token);
            Usuario usuario = usuarioService.findById(idUser);
            System.out.println(usuario);
            UserDetails userDetails = User.builder()
                    .username(usuario.getUsername())
                    .password(usuario.getPassword())
                    .roles(String.valueOf(usuario.getRol().getName()))
                    .build();

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities());

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);

            System.out.println("Rol desde BD: " + usuario.getRol().getName());
            System.out.println("Authorities construidas: " + userDetails.getAuthorities());

        } catch (Exception e) {
            logger.error("JWT Error → {}", e.getMessage());
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Autenticación fallida: " + e.getMessage());
        }
    }

    private String extractToken(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(
                Map.of(
                        "status", status.value(),
                        "error", status.getReasonPhrase(),
                        "message", message,
                        "timestamp", Instant.now().toString())));
    }

    @Override
    protected boolean shouldNotFilter(@SuppressWarnings("null") HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/api/auth");
    }
}
