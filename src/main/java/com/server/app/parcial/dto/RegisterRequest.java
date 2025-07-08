package com.server.app.parcial.dto;

import jakarta.validation.constraints.*;

public record RegisterRequest(
    @NotBlank(message = "El username es obligatorio")
    @Size(max = 50, message = "El username no puede exceder los 50 caracteres")
    String username,

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ser un email válido")
    String email,

    @Size(max = 15, message = "El teléfono no puede exceder los 15 caracteres")
    String phone,

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
        message = "La contraseña debe contener al menos 1 mayúscula, 1 minúscula, 1 número y 1 carácter especial"
    )
    String password,

    @NotNull(message = "El rol es obligatorio")
    String roll 
) {}