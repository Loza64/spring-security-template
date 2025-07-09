package com.server.app.parcial.reposiories;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.server.app.parcial.model.Ticket;
import java.util.List;
import java.util.Optional;


@EnableJpaRepositories
public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    Optional<Ticket> findById(int id);
}
