package de.birkenfunk.birkenbotcode.persistent.repos;

import de.birkenfunk.birkenbotcode.persistent.entity.Command;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommandRepo extends JpaRepository<Command, Integer> {
}
