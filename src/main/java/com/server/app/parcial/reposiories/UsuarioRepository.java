package com.server.app.parcial.reposiories; // Fixed package name spelling

import com.server.app.parcial.model.Rol;
import com.server.app.parcial.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findById(int id);

    List<Usuario> findByRol(Rol rol); // Find users by role ID

    Optional<Usuario> findByUsername(String username);

    List<Usuario> findByUsernameContainingIgnoreCase(String username); // Search by username

}