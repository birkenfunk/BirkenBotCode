package de.birkenfunk.birkenbotcode.persistent.repos;

import de.birkenfunk.birkenbotcode.persistent.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepo extends JpaRepository<Log, Integer> {
}
