package com.server.app.parcial.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface RolRepository extends JpaRepository<Rol, Integer> {
    Optional<Rol> findByName(String name);
}
