package com.server.app.parcial.service;

import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.server.app.parcial.config.JwtConfig;
import com.server.app.parcial.model.Usuario;
import com.server.app.parcial.reposiories.UsuarioRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtConfig jwtConfig;

    public Optional<Usuario> login(String username, String rawPassword) {
        Optional<Usuario> userOpt = usuarioRepository.findByUsername(username);

        if (userOpt.isPresent()) {
            Usuario user = userOpt.get();
            if (passwordEncoder.matches(rawPassword, user.getPassword())) {
                return Optional.of(user);
            }
        }

        return Optional.empty();
    }

    public Usuario register(Usuario newUser) {
        String encodedPassword = passwordEncoder.encode(newUser.getPassword());
        newUser.setPassword(encodedPassword);
        if (newUser.getRol() == null) {
            return null;
        }

        System.out.println();

        return usuarioRepository.save(newUser);
    }

    public Usuario findById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de usuario inválido");
        }

        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public String generateToken(Usuario user) {
        return jwtConfig.createToken(user);
    }
}