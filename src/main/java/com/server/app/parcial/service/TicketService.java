package com.server.app.parcial.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.server.app.parcial.model.Ticket;
import com.server.app.parcial.reposiories.TicketRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TicketService {

    private final TicketRepository repository;

    public List<Ticket> getAllTikects() {
        return repository.findAll();
    }

    public Ticket createTicked(Ticket data) {
        return repository.save(data);
    }


    public Ticket updateState(int id, String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("El estado no puede ser nulo o vacío");
        }

        Ticket ticket = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket no encontrado con ID: " + id));


        if (ticket.getState().equals(status)) {
            throw new IllegalStateException("El ticket ya tiene el estado: " + status);
        }

        ticket.setState(status.toUpperCase());

        return repository.save(ticket);
    }
}
