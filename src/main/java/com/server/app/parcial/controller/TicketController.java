package com.server.app.parcial.controller;

import com.server.app.parcial.config.JwtConfig;
import com.server.app.parcial.dto.*;
import com.server.app.parcial.model.Ticket;
import com.server.app.parcial.model.Usuario;
import com.server.app.parcial.service.TicketService;
import com.server.app.parcial.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final UsuarioService usuarioService;
    private final JwtConfig jwtConfig;

    @PostMapping("created")
    public ResponseEntity<?> createTicket(@Valid @RequestBody TicketRequestDTO request,
            @RequestHeader("Authorization") String authorizationHeader) {

        String token = extractBearerToken(authorizationHeader);

        int idUser = jwtConfig.extracClaims(token).get("id", Integer.class);

        Usuario usuario = usuarioService.findById(idUser);

        Ticket data = new Ticket();
        data.setCodigo(request.getCodigo());
        data.setState("PENDING");
        data.setUsuario(usuario);
        Ticket createdTicket = ticketService.createTicked(data);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Response.build(
                        HttpStatus.CREATED.value(),
                        "Ticket creado exitosamente",
                        createdTicket));
    }

    @GetMapping("list")
    public List<Ticket> getList() {
        return ticketService.getAllTikects();
    }

    @PatchMapping("/state/update")
    public Ticket updateState(@Valid @RequestBody UpdateStateTicketDto data) {
        return ticketService.updateState(data.getIdTicket(), data.getState().toUpperCase());
    }

    private String extractBearerToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

}