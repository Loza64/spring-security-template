package com.server.app.parcial.service;

import org.springframework.stereotype.Service;

import com.server.app.parcial.model.Rol;
import com.server.app.parcial.model.RolRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RolService {
    
    public final RolRepository repository;

    public Rol findRolByName(String name){
        return repository.findByName(name).orElseThrow(() -> new RuntimeException("Rol no encontrado con el nombre: " + name));
    }
}
