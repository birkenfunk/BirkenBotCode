package de.birkenfunk.birkenbotcode.persistent.repos;

import de.birkenfunk.birkenbotcode.persistent.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
}
