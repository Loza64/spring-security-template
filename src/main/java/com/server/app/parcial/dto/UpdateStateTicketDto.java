package com.server.app.parcial.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStateTicketDto {
    
    @NotNull(message = "El ID del ticket es obligatorio")
    private int idTicket;
    
    @NotBlank(message = "El estado no puede estar vacío")
    @Size(max = 50, message = "El estado no puede exceder los 50 caracteres")
    private String state;
}