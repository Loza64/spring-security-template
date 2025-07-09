package com.server.app.parcial.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketRequestDTO {
    
    @NotBlank(message = "El código del ticket es obligatorio")
    @Size(max = 50, message = "El código no puede exceder los 50 caracteres")
    private String codigo;
    
    @NotBlank(message = "El estado es obligatorio")
    @Size(max = 50, message = "El estado no puede exceder los 50 caracteres")
    private String state;

}