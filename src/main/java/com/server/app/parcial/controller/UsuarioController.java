package com.server.app.parcial.controller;

import com.server.app.parcial.dto.LoginRequest;
import com.server.app.parcial.dto.RegisterRequest;
import com.server.app.parcial.dto.Response;
import com.server.app.parcial.model.Rol;
import com.server.app.parcial.model.Usuario;
import com.server.app.parcial.service.RolService;
import com.server.app.parcial.service.UsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para manejar operaciones relacionadas con usuarios
 */
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final RolService rolService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid LoginRequest credentials) {
        try {
            Optional<Usuario> usuarioOpt = usuarioService.login(credentials.username(), credentials.password());

            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();
                String token = usuarioService.generateToken(usuario);
                return Response.build(HttpStatus.OK.value(), "welcome " + usuario.getUsername(), token);
            }

            return Response.build(HttpStatus.UNAUTHORIZED.value(), "Invalid credentials ", null);

        } catch (Exception e) {
              return Response.build(HttpStatus.BAD_REQUEST.value(), "error" , e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody RegisterRequest usuario) {

        Rol rol = rolService.findRolByName(usuario.roll());
        Usuario data = new Usuario();
        data.setEmail(usuario.email());
        data.setUsername(usuario.username());
        data.setPhone(usuario.phone());
        data.setRol(rol);
        data.setPassword(usuario.password());

        Usuario nuevoUsuario = usuarioService.register(data);

        return Response.build(HttpStatus.OK.value(), "User Created ", nuevoUsuario);
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> getAllUsuarios() {
        return ResponseEntity.ok(usuarioService.findAll());
    }

    @GetMapping("/data/{id}")
    public ResponseEntity<?> getUsuarioById(@PathVariable("id") int id) {
        Usuario data = usuarioService.findById(id);
        return Response.build(HttpStatus.OK.value(), "User info ", data);
    }


      @GetMapping("/list")
    public ResponseEntity<?> getUsuariosList() {
        List<Usuario> data = usuarioService.findAll();
        return Response.build(HttpStatus.OK.value(), "User list ", data);
    }


}